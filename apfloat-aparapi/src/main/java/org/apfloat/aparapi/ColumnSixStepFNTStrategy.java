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

import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.MatrixStrategy;
import org.apfloat.spi.NTTStepStrategy;
import org.apfloat.internal.SixStepFNTStrategy;

/**
 * Six-step NTT implementation that processes the data in the columns of the matrix.<p>
 *
 * This transform only works together with an {@link NTTStepStrategy} implementation
 * that processes the data in columns instead of rows and a {@link MatrixStrategy}
 * implementation that can transpose the data.<p>
 *
 * The data size should be sufficiently large to meet the parallelization needs of the GPU.
 * The GPU global size i.e. the number of columns in the data matrix should be at least 1024.
 *
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class ColumnSixStepFNTStrategy
    extends SixStepFNTStrategy
{
    /**
     * Basic constructor.
     *
     * @param stepStrategy A step strategy that can process data in columns.
     * @param matrixStrategy A matrix strategy that can process the data.
     */

    public ColumnSixStepFNTStrategy(NTTStepStrategy stepStrategy, MatrixStrategy matrixStrategy)
    {
        super.stepStrategy = stepStrategy;
        super.matrixStrategy = matrixStrategy;
    }

    @Override
    protected void transposeInitial(ArrayAccess arrayAccess, int n1, int n2, boolean isInverse)
    {
        // Omitted as we want to process the columns, not rows
    }

    @Override
    protected void transposeMiddle(ArrayAccess arrayAccess, int n1, int n2, boolean isInverse)
    {
        // Matrix is in transposed form compared to the normal six-step algorithm, so swap n1 and n2 
        super.transposeMiddle(arrayAccess, n2, n1, isInverse);
    }

    @Override
    protected void multiplyElements(ArrayAccess arrayAccess, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
    {
        // Matrix is in transposed form compared to the normal six-step algorithm, so swap rows and columns
        super.multiplyElements(arrayAccess, columns, rows, length, totalTransformLength, isInverse, modulus);
    }
}
