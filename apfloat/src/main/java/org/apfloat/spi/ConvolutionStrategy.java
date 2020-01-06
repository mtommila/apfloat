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
 * Generic convolution strategy. To perform the convolution,
 * an implementing class could use e.g.
 *
 * <ul>
 *   <li>A simple long multiplication convolution with O(n<sup>2</sup>) complexity</li>
 *   <li>An O(n<sup>log2(3)</sup>) Karatsuba type algorithm, e.g. <a href="http://www.apfloat.org/log23.html" target="_blank">as desribed in Knuth's Seminumerical Algorithms</a></li>
 *   <li>Floating-point Fast Fourier Transform (FFT) based convolution</li>
 *   <li><a href="http://www.apfloat.org/ntt.html" target="_blank">Number-Theoretic Transform (NTT)</a> based convolution, with the <a href="http://www.apfloat.org/crt.html" target="_blank">Chinese Remainder Theorem</a> used</li>
 * </ul>
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface ConvolutionStrategy
{
    /**
     * Convolutes the two sets of data.
     *
     * @param x First data set.
     * @param y Second data set.
     * @param resultSize Number of elements needed in the result data.
     *
     * @return The convolved data.
     */

    public DataStorage convolute(DataStorage x, DataStorage y, long resultSize)
        throws ApfloatRuntimeException;
}
