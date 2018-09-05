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
package org.apfloat.aparapi;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.MatrixStrategy;
import org.apfloat.internal.ApfloatInternalException;

import com.aparapi.Range;

/**
 * Matrix transposition in the GPU for the <code>long</code> type.
 *
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class LongAparapiMatrixStrategy
    implements MatrixStrategy
{
    /**
     * Default constructor.
     */

    public LongAparapiMatrixStrategy()
    {
    }

    @Override
    public void transpose(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException
    {
        if (n1 != (n1 & -n1) ||
            n2 != (n2 & -n2) ||
            n1 <= 0 || n2 <= 0)
        {
            throw new ApfloatInternalException("Matrix size must be a power of two, not " + n1 + " x " + n2);
        }
        if (n1 == n2)
        {
            // Simply transpose

            transposeSquare(arrayAccess, n1, n1);
        }
        else if (n2 == 2 * n1)
        {
            // First transpose two n1 x n1 blocks
            transposeSquare(arrayAccess, n1, n2);
            transposeSquare(arrayAccess.subsequence(n1, arrayAccess.getLength() - n1), n1, n2);

            // Then permute the rows to correct order
            permuteToHalfWidth(arrayAccess, n1, n2);
        }
        else if (n1 == 2 * n2)
        {
            // First permute the rows to correct order
            permuteToDoubleWidth(arrayAccess, n1, n2);

            // Then transpose two n2 x n2 blocks
            transposeSquare(arrayAccess, n2, n1);
            transposeSquare(arrayAccess.subsequence(n2, arrayAccess.getLength() - n2), n2, n1);
        }
        else
        {
            throw new ApfloatInternalException("Must be n1 = n2, n1 = 2*n2 or n2 = 2*n1; matrix is " + n1 + " x " + n2);
        }
    }

    @Override
    public void transposeSquare(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException
    {
        LongKernel kernel = LongKernel.getInstance();
        kernel.setOp(LongKernel.TRANSPOSE);
        kernel.setArrayAccess(arrayAccess);
        kernel.setN2(n2);

        Range range = Range.create2D(n1, n1);
        kernel.execute(range);
    }

    @Override
    public void permuteToHalfWidth(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException
    {
        if (n1 != (n1 & -n1) ||
            n2 != (n2 & -n2) ||
            n1 <= 0 || n2 <= 0)
        {
            throw new ApfloatInternalException("Matrix size must be a power of two, not " + n1 + " x " + n2);
        }

        // Permute the rows of matrix to correct order, to make the n1 x n2 matrix half as wide (2*n1 x n2/2)
        if (n1 < 2)
        {
            return;
        }

        int twicen1 = 2 * n1;
        boolean[] isRowDone = new boolean[twicen1];
        int[] index = new int[twicen1 * 2]; // Overly big but twicen1 just isn't enough

        int j = 1,
            p = 0;
        do
        {
            int m = j;

            index[p++] = m;

            isRowDone[m] = true;

            m = (m < n1 ? 2 * m : 2 * (m - n1) + 1);

            while (m != j)
            {
                isRowDone[m] = true;

                index[p++] = m;

                m = (m < n1 ? 2 * m : 2 * (m - n1) + 1);
            }

            index[p++] = 0;

            while (isRowDone[j])
            {
                j++;
            }
        } while (j < twicen1 - 1);

        LongKernel kernel = LongKernel.getInstance();
        kernel.setOp(LongKernel.PERMUTE);
        kernel.setArrayAccess(arrayAccess);
        kernel.setN2(n2 / 2);
        kernel.setIndex(index);
        kernel.setIndexCount(p);

        kernel.put(index);

        Range range = Range.create(n2 / 2);
        kernel.execute(range);
    }

    @Override
    public void permuteToDoubleWidth(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException
    {
        if (n1 != (n1 & -n1) ||
            n2 != (n2 & -n2) ||
            n1 <= 0 || n2 <= 0)
        {
            throw new ApfloatInternalException("Matrix size must be a power of two, not " + n1 + " x " + n2);
        }
        if (n1 < 2)
        {
            throw new ApfloatInternalException("Matrix height must be at least 2.");
        }

        // Permute the rows of matrix to correct order, to make the n1 x n2 matrix twice as wide (n1/2 x 2*n2)
        if (n1 < 4)
        {
            return;
        }

        int halfn1 = n1 / 2;
        boolean[] isRowDone = new boolean[n1];
        int[] index = new int[n1 * 2];  // Overly big but n1 just isn't enough

        int j = 1,
            p = 0;
        do
        {
            int m = j;

            index[p++] = m;

            isRowDone[m] = true;

            m = ((m & 1) != 0 ? m / 2 + halfn1 : m / 2);

            while (m != j)
            {
                isRowDone[m] = true;

                index[p++] = m;

                m = ((m & 1) != 0 ? m / 2 + halfn1 : m / 2);
            }

            index[p++] = 0;

            while (isRowDone[j])
            {
                j++;
            }
        } while (j < n1 - 1);

        LongKernel kernel = LongKernel.getInstance();
        kernel.setOp(LongKernel.PERMUTE);
        kernel.setArrayAccess(arrayAccess);
        kernel.setN2(n2);
        kernel.setIndex(index);
        kernel.setIndexCount(p);

        kernel.put(index);

        Range range = Range.create(n2);
        kernel.execute(range);
    }
}
