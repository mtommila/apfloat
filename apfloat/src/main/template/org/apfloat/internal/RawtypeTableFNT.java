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
package org.apfloat.internal;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;

/**
 * Fast Number Theoretic Transform that uses lookup tables
 * for powers of n:th root of unity and permutation indexes.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeTableFNT
    extends RawtypeModMath
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
        int nn, offset, istep, mmax, r;
        rawtype[] data;

        data   = arrayAccess.getRawtypeData();
        offset = arrayAccess.getOffset();
        nn     = arrayAccess.getLength();

        assert (nn == (nn & -nn));

        if (nn < 2)
        {
            return;
        }

        r = 1;
        mmax = nn >> 1;
        while (mmax > 0)
        {
            istep = mmax << 1;

            // Optimize first step when wr = 1

            for (int i = offset; i < offset + nn; i += istep)
            {
                int j = i + mmax;
                rawtype a = data[i];
                rawtype b = data[j];
                data[i] = modAdd(a, b);
                data[j] = modSubtract(a, b);
            }

            int t = r;

            for (int m = 1; m < mmax; m++)
            {
                for (int i = offset + m; i < offset + nn; i += istep)
                {
                    int j = i + mmax;
                    rawtype a = data[i];
                    rawtype b = data[j];
                    data[i] = modAdd(a, b);
                    data[j] = modMultiply(wTable[t], modSubtract(a, b));
                }
                t += r;
            }
            r <<= 1;
            mmax >>= 1;
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
        int nn, offset, istep, mmax, r;
        rawtype[] data;

        data   = arrayAccess.getRawtypeData();
        offset = arrayAccess.getOffset();
        nn     = arrayAccess.getLength();

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
        while (nn > mmax)
        {
            istep = mmax << 1;
            r >>= 1;

            // Optimize first step when w = 1

            for (int i = offset; i < offset + nn; i += istep)
            {
                int j = i + mmax;
                rawtype wTemp = data[j];
                data[j] = modSubtract(data[i], wTemp);
                data[i] = modAdd(data[i], wTemp);
            }

            int t = r;

            for (int m = 1; m < mmax; m++)
            {
                for (int i = offset + m; i < offset + nn; i += istep)
                {
                    int j = i + mmax;
                    rawtype wTemp = modMultiply(wTable[t], data[j]);
                    data[j] = modSubtract(data[i], wTemp);
                    data[i] = modAdd(data[i], wTemp);
                }
                t += r;
            }
            mmax = istep;
        }
    }
}
