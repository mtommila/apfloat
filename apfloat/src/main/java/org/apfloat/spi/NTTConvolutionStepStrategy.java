/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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
package org.apfloat.spi;

import org.apfloat.ApfloatRuntimeException;

/**
 * Steps for a three-NTT convolution. This includes element-by-element
 * multiplication and squaring of the transformed data.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public interface NTTConvolutionStepStrategy
{
    /**
     * Linear multiplication in the number theoretic domain.
     * The operation is <code>sourceAndDestination[i] *= source[i] (mod m)</code>.<p>
     *
     * For maximum performance, <code>sourceAndDestination</code>
     * should be in memory if possible.
     *
     * @param sourceAndDestination The first source data storage, which is also the destination.
     * @param source The second source data storage.
     * @param modulus Which modulus to use (0, 1, 2)
     */

    public void multiplyInPlace(DataStorage sourceAndDestination, DataStorage source, int modulus)
        throws ApfloatRuntimeException;

    /**
     * Linear squaring in the number theoretic domain.
     * The operation is <code>sourceAndDestination[i] *= sourceAndDestination[i] (mod m)</code>.<p>
     *
     * For maximum performance, <code>sourceAndDestination</code>
     * should be in memory if possible.
     *
     * @param sourceAndDestination The source data storage, which is also the destination.
     * @param modulus Which modulus to use (0, 1, 2)
     */

    public void squareInPlace(DataStorage sourceAndDestination, int modulus)
        throws ApfloatRuntimeException;
}
