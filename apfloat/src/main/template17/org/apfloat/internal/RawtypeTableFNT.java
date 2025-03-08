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
        int[] indexes = IntStream.range(0, length).toArray();

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

            if (mmax < length)
            {
                for (int k = 0; k < nn / 2; k++)
                {
                    int m = k & mmaxmask;
                    int t = m * r;
                    int i = offset + m + ((k >> mmaxbits) << istepbits);
                    int j = i + mmax;
                    rawtype a = data[i];
                    rawtype b = data[j];
                    data[i] = modAdd(a, b);
                    data[j] = modMultiply(wTable[t], modSubtract(a, b));
                }
            }
            else
            {
                for (int k = 0; k < nn / 2; k += length)
                {
                    int m = k & mmaxmask;
                    int t = m * r;
                    int i = offset + m + ((k >> mmaxbits) << istepbits);
                    int j = i + mmax;
                    RawtypeVector a = RawtypeVector.fromArray(s, data, i);
                    RawtypeVector b = RawtypeVector.fromArray(s, data, j);
                    RawtypeVector w = RawtypeVector.fromArray(s, wTable, t, indexes, 0);
                    modAdd(a, b).intoArray(data, i);
                    modMultiply(w, modSubtract(a, b)).intoArray(data, j);
                }
            }
            r <<= 1;
            mmax >>= 1;
            mmaxbits--;
            for (int i = 0; i < indexes.length; i++)
            {
                indexes[i] <<= 1;
            }
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
        int[] indexes = IntStream.range(0, length).map(i -> nn * i).toArray();

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
            for (int i = 0; i < indexes.length; i++)
            {
                indexes[i] >>= 1;
            }

            if (mmax < length)
            {
                for (int k = 0; k < nn / 2; k++)
                {
                    int m = k & mmaxmask;
                    int t = m * r;
                    int i = offset + m + ((k >> mmaxbits) << istepbits);
                    int j = i + mmax;
                    rawtype wTemp = modMultiply(wTable[t], data[j]);
                    data[j] = modSubtract(data[i], wTemp);
                    data[i] = modAdd(data[i], wTemp);
                }
            }
            else
            {
                for (int k = 0; k < nn / 2; k += length)
                {
                    int m = k & mmaxmask;
                    int t = m * r;
                    int i = offset + m + ((k >> mmaxbits) << istepbits);
                    int j = i + mmax;
                    RawtypeVector wTemp = modMultiply(RawtypeVector.fromArray(s, wTable, t, indexes, 0), RawtypeVector.fromArray(s, data, j));
                    RawtypeVector temp = RawtypeVector.fromArray(s, data, i);
                    modSubtract(temp, wTemp).intoArray(data, j);
                    modAdd(temp, wTemp).intoArray(data, i);
                }
            }
            mmax <<= 1;
            mmaxbits++;
        }
    }
}
