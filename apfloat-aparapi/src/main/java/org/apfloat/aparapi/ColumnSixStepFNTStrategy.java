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
