/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apfloat.aparapi;

import org.apfloat.internal.TwoPassFNTStrategy;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.DataStorage;
import org.apfloat.spi.NTTStepStrategy;

/**
 * Two-pass NTT implementation that processes the data in the columns of the matrix.<p>
 *
 * This transform only works together with an {@link NTTStepStrategy} implementation
 * that processes the data in columns instead of rows.<p>
 *
 * Note that if the data size is too big compared to the maximum available memory then the
 * data is read from disk in too thin slices and the level of parallelism may become too
 * small for the GPU, ruining the performance. The GPU global size i.e. the number of columns
 * read from the data matrix to memory at one time should be at least 1024.
 *
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class ColumnTwoPassFNTStrategy
    extends TwoPassFNTStrategy
{
    /**
     * Basic constructor.
     *
     * @param stepStrategy A step strategy that can process data in columns.
     */

    public ColumnTwoPassFNTStrategy(NTTStepStrategy stepStrategy)
    {
        // Note that there is no defaultStrategy here; if we get to the two-pass algorithm then we
        // assume that the data size is always "big enough" for a sufficient level of parallelism on the GPU
        super.stepStrategy = stepStrategy;
    }

    @Override
    protected ArrayAccess getColumns(DataStorage dataStorage, int startColumn, int columns, int rows)
    {
        // Get columns un-transposed
        return dataStorage.getArray(DataStorage.READ_WRITE, startColumn, columns, rows);
    }

    @Override
    protected ArrayAccess getRows(DataStorage dataStorage, int startRow, int rows, int columns)
    {
        // Get rows transposed as we want to organize the data in columns
        return dataStorage.subsequence(startRow * columns, rows * columns).getTransposedArray(DataStorage.READ_WRITE, 0, columns, rows);
    }

    @Override
    protected void multiplyElements(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
    {
        // Data is processed in transposed form compared to the normal two-pass algorithm, so swap rows and columns
        super.multiplyElements(arrayAccess, startColumn, startRow, columns, rows, length, totalTransformLength, isInverse, modulus);
    }
}
