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

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.DataStorage;
import org.apfloat.spi.NTTStepStrategy;
import org.apfloat.internal.SixStepFNTStrategy;

/**
 * Six-step NTT implementation that processes the data in the columns of the matrix.<p>
 *
 * This transform only works together with an {@link NTTStepStrategy} implementation
 * that processes the data in columns instead of rows.<p>
 *
 * If the data size is not sufficiently large to meet the parallelization needs of the GPU
 * then this transform delegates to the default pure-Java transform for more efficient processing.
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
     */

    public ColumnSixStepFNTStrategy(NTTStepStrategy stepStrategy)
    {
        this.defaultStrategy = new SixStepFNTStrategy();
        super.stepStrategy = stepStrategy;
    }

    public void transform(DataStorage dataStorage, int modulus)
        throws ApfloatRuntimeException
    {
        long length = dataStorage.getSize();

        // Because data is treated as a matrix, the number of columns in the matrix
        // i.e. the GPU global size is 1024 if the data size is 1024 * 1024 = 1048576
        if (length < 1048576)
        {
            this.defaultStrategy.transform(dataStorage, modulus);
        }
        else
        {
            super.transform(dataStorage, modulus);
        }
    }

    public void inverseTransform(DataStorage dataStorage, int modulus, long totalTransformLength)
        throws ApfloatRuntimeException
    {
        long length = dataStorage.getSize();

        // Because data is treated as a matrix, the number of columns in the matrix
        // i.e. the GPU global size is 1024 if the data size is 1024 * 1024 = 1048576
        if (length < 1048576)
        {
            this.defaultStrategy.inverseTransform(dataStorage, modulus, totalTransformLength);
        }
        else
        {
            super.inverseTransform(dataStorage, modulus, totalTransformLength);
        }
    }

    protected void transposeInitial(ArrayAccess arrayAccess, int n1, int n2, boolean isInverse)
    {
        // Omitted as we want to process the columns, not rows
    }

    protected void transposeMiddle(ArrayAccess arrayAccess, int n1, int n2, boolean isInverse)
    {
        // Matrix is in transposed form compared to the normal six-step algorithm, so swap n1 and n2 
        super.transposeMiddle(arrayAccess, n2, n1, isInverse);
    }

    protected void multiplyElements(ArrayAccess arrayAccess, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
    {
        // Matrix is in transposed form compared to the normal six-step algorithm, so swap rows and columns
        super.multiplyElements(arrayAccess, columns, rows, length, totalTransformLength, isInverse, modulus);
    }

    private SixStepFNTStrategy defaultStrategy;
}
