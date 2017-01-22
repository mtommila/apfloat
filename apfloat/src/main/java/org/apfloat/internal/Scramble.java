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
package org.apfloat.internal;

import org.apfloat.spi.Util;

/**
 * Functions to perform bit-reverse ordering of data.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public class Scramble
{
    private Scramble()
    {
    }

    /**
     * Permute the bits of a number to reverse order.<p>
     *
     * For example, if <code>n</code> is 5 and the transform
     * length is 256, the permutation is (in binary)<p>
     *
     * 00000101 &rarr; 10100000
     *
     * @param n The number whose bits to reverse.
     * @param length The FFT transform length for which the bit reversal to perform.
     *
     * @return The bits of <code>n</code> reversed.
     */

    public static int permute(int n, int length)
    {
        assert (length == (length & -length));

        int p = 1;

        while (p < length)
        {
            p += p + (n & 1);
            n >>= 1;
        }

        return p - length;
    }

    /**
     * Create a table of indexes for scrambling an array for FFT.<p>
     *
     * The returned table contains pairs of indexes that should be swapped
     * to scramble an array. For example, for transform length 8 the
     * returned table contains <code>{ 1, 4, 3, 6 }</code> to indicate
     * that the array elements [1] and [4] should be swapped, and the elements
     * [3] and [6] should be swapped.
     *
     * @param length The FFT transform length for which the scrambling table is created.
     *
     * @return An array of pairs of indexes that indicate, which array elements should be swapped to scramble the array.
     */

    public static int[] createScrambleTable(int length)
    {
        assert (length == (length & -length));

        int[] scrambleTable = new int[length - Util.sqrt4up(length)];

        for (int i = 0, k = 0; i < length; i++)
        {
            int j = permute(i, length);
            if (j < i)
            {
                scrambleTable[k] = i;
                scrambleTable[k + 1] = j;
                k += 2;
            }
        }

        return scrambleTable;
    }
}
