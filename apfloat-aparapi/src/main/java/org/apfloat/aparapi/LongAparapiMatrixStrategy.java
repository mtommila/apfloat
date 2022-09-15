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
