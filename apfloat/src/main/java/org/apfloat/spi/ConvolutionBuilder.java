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
package org.apfloat.spi;

/**
 * Interface of a factory for creating convolutors.
 * The factory method pattern is used.
 *
 * @see ConvolutionStrategy
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface ConvolutionBuilder
{
    /**
     * Returns a convolution strategy of suitable
     * type for the specified length.
     *
     * @param radix The radix that will be used.
     * @param size1 Length of first data set.
     * @param size2 Length of second data set.
     * @param resultSize Minimum number of elements needed in the result data.
     *
     * @return A suitable object for performing the convolution.
     */

    public ConvolutionStrategy createConvolution(int radix, long size1, long size2, long resultSize);
}
