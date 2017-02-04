/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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

    protected ArrayAccess getColumns(DataStorage dataStorage, int startColumn, int columns, int rows)
    {
        // Get columns un-transposed
        return dataStorage.getArray(DataStorage.READ_WRITE, startColumn, columns, rows);
    }

    protected ArrayAccess getRows(DataStorage dataStorage, int startRow, int rows, int columns)
    {
        // Get rows transposed as we want to organize the data in columns
        return dataStorage.subsequence(startRow * columns, rows * columns).getTransposedArray(DataStorage.READ_WRITE, 0, columns, rows);
    }

    protected void multiplyElements(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
    {
        // Data is processed in transposed form compared to the normal two-pass algorithm, so swap rows and columns
        super.multiplyElements(arrayAccess, startColumn, startRow, columns, rows, length, totalTransformLength, isInverse, modulus);
    }
}
