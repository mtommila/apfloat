/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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
package org.apfloat.internal;

import org.apfloat.spi.MatrixBuilder;
import org.apfloat.spi.MatrixStrategy;

/**
 * Creates matrix operations objects, for the
 * <code>rawtype</code> type.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeMatrixBuilder
    implements MatrixBuilder
{
    /**
     * Default constructor.
     */

    public RawtypeMatrixBuilder()
    {
    }

    @Override
    public MatrixStrategy createMatrix()
    {
        return RawtypeMatrixBuilder.matrixStrategy;
    }

    private static MatrixStrategy matrixStrategy = new RawtypeMatrixStrategy();
}
