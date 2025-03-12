/*
 * MIT License
 *
 * Copyright (c) 2002-2025 Mikko Tommila
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

import java.util.stream.IntStream;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;

import jdk.incubator.vector.RawtypeVector;
import jdk.incubator.vector.VectorSpecies;

/**
 * Fast Number Theoretic Transform that uses lookup tables
 * for powers of n:th root of unity and permutation indexes.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.7.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class RawtypeTableFNT
    extends RawtypeVectorModMath
{
    /**
     * Default constructor.
     */

    public RawtypeTableFNT()
    {
    }

    /**
     * Forward (Sande-Tukey) fast Number Theoretic Transform.
     * Data length must be a power of two.
     *
     * @param arrayAccess The data array to transform.
     * @param wTable Table of powers of n:th root of unity <code>w</code> modulo the current modulus.
     * @param permutationTable Table of permutation indexes, or <code>null</code> if the data should not be permuted.
     */

    public void tableFNT(ArrayAccess arrayAccess, rawtype[] wTable, int[] permutationTable)
        throws ApfloatRuntimeException
    {
        int nn, offset, mmax, r;
        rawtype[] data;

        data   = arrayAccess.getRawtypeData();
        offset = arrayAccess.getOffset();
        nn     = arrayAccess.getLength();

        VectorSpecies<RawType> s = RawtypeVector.SPECIES_PREFERRED;
        int length = s.length();
        int[] wIndex = new int[length];
        int[] iIndex = new int[length];
        int[] jIndex = new int[length];

        assert (nn == (nn & -nn));

        if (nn < 2)
        {
            return;
        }

        r = 1;
        mmax = nn >> 1;
        int mmaxbits = Integer.numberOfTrailingZeros(mmax);
        while (mmax > 0)
        {
            int mmaxmask = mmax - 1;
            int istepbits = mmaxbits + 1;

            for (int k = 0; k < nn / 2;)
            {
                for (int v = 0; v < length; v++, k++) {
                    int m = k & mmaxmask;
                    int t = m * r;
                    int i = offset + m + ((k >> mmaxbits) << istepbits);
                    int j = i + mmax;
                    iIndex[v] = i;
                    jIndex[v] = j;
                    wIndex[v] = t;
                }
                RawtypeVector a = RawtypeVector.fromArray(s, data, 0, iIndex, 0);
                RawtypeVector b = RawtypeVector.fromArray(s, data, 0, jIndex, 0);
                RawtypeVector w = RawtypeVector.fromArray(s, wTable, 0, wIndex, 0);
                modAdd(a, b).intoArray(data, 0, iIndex, 0);
                modMultiply(w, modSubtract(a, b)).intoArray(data, 0, jIndex, 0);
            }
            r <<= 1;
            mmax >>= 1;
            mmaxbits--;
        }

        if (permutationTable != null)
        {
            RawtypeScramble.scramble(data, offset, permutationTable);
        }
    }

    /**
     * Inverse (Cooley-Tukey) fast Number Theoretic Transform.
     * Data length must be a power of two.
     *
     * @param arrayAccess The data array to transform.
     * @param wTable Table of powers of n:th root of unity <code>w</code> modulo the current modulus.
     * @param permutationTable Table of permutation indexes, or <code>null</code> if the data should not be permuted.
     */

    public void inverseTableFNT(ArrayAccess arrayAccess, rawtype[] wTable, int[] permutationTable)
        throws ApfloatRuntimeException
    {
        int nn, offset, mmax, r;
        rawtype[] data;

        data   = arrayAccess.getRawtypeData();
        offset = arrayAccess.getOffset();
        nn     = arrayAccess.getLength();

        VectorSpecies<RawType> s = RawtypeVector.SPECIES_PREFERRED;
        int length = s.length();
        int[] wIndex = new int[length];
        int[] iIndex = new int[length];
        int[] jIndex = new int[length];

        assert (nn == (nn & -nn));

        if (nn < 2)
        {
            return;
        }

        if (permutationTable != null)
        {
            RawtypeScramble.scramble(data, offset, permutationTable);
        }

        r = nn;
        mmax = 1;
        int mmaxbits = 0;
        while (nn > mmax)
        {
            r >>= 1;
            int mmaxmask = mmax - 1;
            int istepbits = mmaxbits + 1;

            for (int k = 0; k < nn / 2;)
            {
                for (int v = 0; v < length; v++, k++) {
                    int m = k & mmaxmask;
                    int t = m * r;
                    int i = offset + m + ((k >> mmaxbits) << istepbits);
                    int j = i + mmax;
                    iIndex[v] = i;
                    jIndex[v] = j;
                    wIndex[v] = t;
                }
                RawtypeVector wTemp = modMultiply(RawtypeVector.fromArray(s, wTable, 0, wIndex, 0), RawtypeVector.fromArray(s, data, 0, jIndex, 0));
                RawtypeVector temp = RawtypeVector.fromArray(s, data, 0, iIndex, 0);
                modSubtract(temp, wTemp).intoArray(data, 0, jIndex, 0);
                modAdd(temp, wTemp).intoArray(data, 0, iIndex, 0);
            }
            mmax <<= 1;
            mmaxbits++;
        }
    }
}
