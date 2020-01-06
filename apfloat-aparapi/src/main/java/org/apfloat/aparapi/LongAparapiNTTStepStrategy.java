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
