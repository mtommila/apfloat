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
package org.apfloat.aparapi;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;

import com.aparapi.Range;

import org.apfloat.internal.IntNTTStepStrategy;
import org.apfloat.internal.IntWTables;
import org.apfloat.internal.Scramble;

import static org.apfloat.internal.IntModConstants.*;

/**
 * NTT steps for the <code>int</code> element type aparapi transforms.
 *
 * @since 1.8.3
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class IntAparapiNTTStepStrategy
    extends IntNTTStepStrategy
{
    /**
     * Basic constructor.
     *
     * @param rowOrientation If the data is using row orientation.
     */

    public IntAparapiNTTStepStrategy(boolean rowOrientation)
    {
        this.rowOrientation = rowOrientation;
    }

    @Override
    public void multiplyElements(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
        throws ApfloatRuntimeException
    {
        setModulus(MODULUS[modulus]);
        int w = (isInverse ?
                 getInverseNthRoot(PRIMITIVE_ROOT[modulus], length) :
                 getForwardNthRoot(PRIMITIVE_ROOT[modulus], length));
        int scaleFactor = (isInverse ?
                           modDivide((int) 1, (int) totalTransformLength) :
                           (int) 1);

        IntKernel kernel = IntKernel.getInstance();
        kernel.setOp(IntKernel.MULTIPLY_ELEMENTS);
        kernel.setArrayAccess(arrayAccess);
        kernel.setStartRow(startRow);
        kernel.setStartColumn(startColumn);
        kernel.setRows(rows);
        kernel.setColumns(columns);
        kernel.setW(w);
        kernel.setScaleFactor(scaleFactor);
        kernel.setModulus(MODULUS[modulus]);

        Range range = RangeHelper.create(columns);
        kernel.execute(range);
    }

    /**
     * Transform the rows or columns of the data matrix.
     * If the data is oriented in rows, transforms physically the rows.
     * If the data is oriented in columns, transforms physically columns.
     *
     * @param arrayAccess The memory array to split and transform.
     * @param length Length of one transform.
     * @param count Number of transforms to be done.
     * @param isInverse <code>true</code> if an inverse transform is performed, <code>false</code> if a forward transform is performed.
     * @param permute If permutation should be done.
     * @param modulus Index of the modulus.
     */

    @Override
    public void transformRows(ArrayAccess arrayAccess, int length, int count, boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException
    {
        int[] wTable = (isInverse ?
                        IntWTables.getInverseWTable(modulus, length) :
                        IntWTables.getWTable(modulus, length));
        int[] permutationTable = (permute ? Scramble.createScrambleTable(length) : null);

        IntKernel kernel = IntKernel.getInstance();
        kernel.setOp(this.rowOrientation ? (isInverse ? IntKernel.INVERSE_TRANSFORM_ROWS_ROWORIENTATION : IntKernel.TRANSFORM_ROWS_ROWORIENTATION) :
                                           (isInverse ? IntKernel.INVERSE_TRANSFORM_ROWS : IntKernel.TRANSFORM_ROWS));
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

        Range range;
        if (this.rowOrientation)
        {
            int width = Math.min(length, RangeHelper.MAX_LOCAL_SIZE);
            range = Range.create2D(width, count, width, 1);
        }
        else
        {
            range = RangeHelper.create(count);
        }
        kernel.execute(range);
    }

    private boolean rowOrientation;
}
