/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
import org.apfloat.internal.LongNTTStepStrategy;
import org.apfloat.internal.LongWTables;
import org.apfloat.internal.Scramble;

import static org.apfloat.internal.LongModConstants.*;

/**
 * NTT steps for the <code>long</code> element type aparapi transforms.
 *
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class LongAparapiNTTStepStrategy
    extends LongNTTStepStrategy
{
    /**
     * Default constructor.
     */

    public LongAparapiNTTStepStrategy()
    {
    }

    @Override
    public void multiplyElements(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
        throws ApfloatRuntimeException
    {
        setModulus(MODULUS[modulus]);
        long w = (isInverse ?
                  getInverseNthRoot(PRIMITIVE_ROOT[modulus], length) :
                  getForwardNthRoot(PRIMITIVE_ROOT[modulus], length));
        long scaleFactor = (isInverse ?
                            modDivide((long) 1, (long) totalTransformLength) :
                            (long) 1);

        LongKernel kernel = LongKernel.getInstance();
        kernel.setOp(LongKernel.MULTIPLY_ELEMENTS);
        kernel.setArrayAccess(arrayAccess);
        kernel.setStartRow(startRow);
        kernel.setStartColumn(startColumn);
        kernel.setRows(rows);
        kernel.setColumns(columns);
        kernel.setW(w);
        kernel.setScaleFactor(scaleFactor);
        kernel.setModulus(MODULUS[modulus]);

        kernel.execute(columns);
    }

    /**
     * Transform the columns of the data matrix. Note that this method expects the data
     * to be organized in columns, not rows. The arguments <code>length</code> and
     * <code>count</code> still mean the length of one transform and number of transforms
     * to be done.
     *
     * @param arrayAccess The memory array to split to columns and to transform.
     * @param length Length of one transform (one columns).
     * @param count Number of columns.
     * @param isInverse <code>true</code> if an inverse transform is performed, <code>false</code> if a forward transform is performed.
     * @param permute If permutation should be done.
     * @param modulus Index of the modulus.
     */

    @Override
    public void transformRows(ArrayAccess arrayAccess, int length, int count, boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException
    {
        long[] wTable = (isInverse ?
                         LongWTables.getInverseWTable(modulus, length) :
                         LongWTables.getWTable(modulus, length));
        int[] permutationTable = (permute ? Scramble.createScrambleTable(length) : null);

        LongKernel kernel = LongKernel.getInstance();
        kernel.setOp(isInverse ? LongKernel.INVERSE_TRANSFORM_ROWS : LongKernel.TRANSFORM_ROWS);
        kernel.setLength(length);
        kernel.setArrayAccess(arrayAccess);
        kernel.setWTable(wTable);
        kernel.setPermutationTable(permutationTable);
        kernel.setModulus(MODULUS[modulus]);

        kernel.put(wTable);
        if (permutationTable != null)
        {
            kernel.put(permutationTable);
        }

        kernel.execute(count);
    }
}
