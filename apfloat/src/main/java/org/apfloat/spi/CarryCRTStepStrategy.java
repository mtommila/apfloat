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

import org.apfloat.ApfloatRuntimeException;

/**
 * Interface for performing the steps of a carry-CRT operation in a convolution.
 *
 * @param <T> The element array type of the carry-CRT steps.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public interface CarryCRTStepStrategy<T>
{
    /**
     * Perform the Chinese Remainder Theorem (CRT) on each element
     * of the three result data sets to get the result of each element
     * modulo the product of the three moduli. Then it calculates the carries
     * for the block of data to get the final result.<p>
     *
     * Note that the return value's initial word may be zero or non-zero,
     * depending on how large the result is.<p>
     *
     * Assumes that <code>MODULUS[0] &gt; MODULUS[1] &gt; MODULUS[2]</code>.
     *
     * @param resultMod0 The result modulo <code>MODULUS[0]</code>.
     * @param resultMod1 The result modulo <code>MODULUS[1]</code>.
     * @param resultMod2 The result modulo <code>MODULUS[2]</code>.
     * @param dataStorage The destination data storage of the computation.
     * @param size The number of elements in the whole data set.
     * @param resultSize The number of elements needed in the final result.
     * @param offset The offset within the data for the block to be computed.
     * @param length Length of the block to be computed.
     *
     * @return The carries overflowing from this block (two elements).
     */

    public T crt(DataStorage resultMod0, DataStorage resultMod1, DataStorage resultMod2, DataStorage dataStorage, long size, long resultSize, long offset, long length)
        throws ApfloatRuntimeException;

    /**
     * Propagate carries from the previous block computed with the CRT
     * method.
     *
     * @param dataStorage The destination data storage of the computation.
     * @param size The number of elements in the whole data set.
     * @param resultSize The number of elements needed in the final result.
     * @param offset The offset within the data for the block to be computed.
     * @param length Length of the block to be computed.
     * @param results The carry overflow from this block (two elements).
     * @param previousResults The carry overflow from the previous block (two elements).
     *
     * @return The carries overflowing from this block (two elements).
     */

    public T carry(DataStorage dataStorage, long size, long resultSize, long offset, long length, T results, T previousResults)
        throws ApfloatRuntimeException;
}
