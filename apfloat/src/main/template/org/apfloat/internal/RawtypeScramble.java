/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
