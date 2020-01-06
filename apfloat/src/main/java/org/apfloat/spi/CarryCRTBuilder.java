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

/**
 * Interface of a factory for creating carry-CRT related objects.
 * The factory method pattern is used.
 *
 * @param <T> The element array type of the CRT.
 *
 * @see CarryCRTStrategy
 * @see CarryCRTStepStrategy
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public interface CarryCRTBuilder<T>
{
    /**
     * Creates an object for implementing the carry-CRT of a three-NTT
     * based convolution using the specified radix.
     *
     * @param radix The radix that will be used.
     *
     * @return A suitable object for performing the carry-CRT.
     */

    public CarryCRTStrategy createCarryCRT(int radix);

    /**
     * Creates an object for implementing the steps of the carry-CRT
     * of a three-NTT based convolution using the specified radix.
     *
     * @param radix The radix that will be used.
     *
     * @return A suitable object for performing the carry-CRT steps.
     */

    public CarryCRTStepStrategy<T> createCarryCRTSteps(int radix);
}
