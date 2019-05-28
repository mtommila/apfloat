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

/**
 * Functions to perform bit-reverse ordering of <code>rawtype</code> data.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeScramble
{
    private RawtypeScramble()
    {
    }

    /**
     * Permute the data in the table to bit-reversed order.<p>
     *
     * The permutation table argument should contain pairs of indexes
     * that indicate array elements whose contents are swapped.
     *
     * @param data The array to permute.
     * @param offset The offset within the array to permute.
     * @param permutationTable Table of indexes indicating, which elements in the <code>data</code> are to be swapped.
     */

    public static void scramble(rawtype[] data, int offset, int[] permutationTable)
    {
        for (int k = 0; k < permutationTable.length; k += 2)
        {
            int i = offset + permutationTable[k],
                j = offset + permutationTable[k + 1];
            rawtype tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }
    }
}
