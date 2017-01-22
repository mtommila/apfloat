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

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.NTTStepStrategy;
import static org.apfloat.internal.RawtypeModConstants.*;

/**
 * Common methods to calculate Fast Number Theoretic Transforms
 * in parallel using multiple threads.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.7.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class RawtypeNTTStepStrategy
    extends RawtypeTableFNT
    implements NTTStepStrategy, Parallelizable
{
    // Runnable for calculating the row transforms in parallel
    private class TableFNTRunnable
        implements Runnable
    {
        public TableFNTRunnable(int length, boolean isInverse, ArrayAccess arrayAccess, rawtype[] wTable, int[] permutationTable)
        {
            this.length = length;               // Transform length
            this.isInverse = isInverse;
            this.arrayAccess = arrayAccess;
            this.wTable = wTable;
            this.permutationTable = permutationTable;
        }

        public void run()
        {
            int maxI = this.arrayAccess.getLength();

            for (int i = 0; i < maxI; i += this.length)
            {
                ArrayAccess arrayAccess = this.arrayAccess.subsequence(i, this.length);

                if (this.isInverse)
                {
                    inverseTableFNT(arrayAccess, this.wTable, this.permutationTable);
                }
                else
                {
                    tableFNT(arrayAccess, this.wTable, this.permutationTable);
                }
            }
        }

        private int length;
        private boolean isInverse;
        private ArrayAccess arrayAccess;
        private rawtype[] wTable;
        private int[] permutationTable;
    }

    // Runnable for multiplying elements in the matrix
    private class MultiplyRunnable
        implements Runnable
    {
        public MultiplyRunnable(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, rawtype w, rawtype scaleFactor)
        {
            this.arrayAccess = arrayAccess;
            this.startRow = startRow;
            this.startColumn = startColumn;
            this.rows = rows;
            this.columns = columns;
            this.w = w;
            this.scaleFactor = scaleFactor;
        }

        public void run()
        {
            rawtype[] data = this.arrayAccess.getRawtypeData();
            int position = this.arrayAccess.getOffset();
            rawtype rowFactor = modPow(this.w, (rawtype) this.startRow);
            rawtype columnFactor = modPow(this.w, (rawtype) this.startColumn);
            rawtype rowStartFactor = modMultiply(this.scaleFactor, modPow(rowFactor, (rawtype) this.startColumn));

            for (int i = 0; i < this.rows; i++)
            {
                rawtype factor = rowStartFactor;

                for (int j = 0; j < this.columns; j++, position++)
                {
                    data[position] = modMultiply(data[position], factor);
                    factor = modMultiply(factor, rowFactor);
                }

                rowFactor = modMultiply(rowFactor, this.w);
                rowStartFactor = modMultiply(rowStartFactor, columnFactor);
            }
         }

        private ArrayAccess arrayAccess;
        private int startRow;
        private int startColumn;
        private int rows;
        private int columns;
        private rawtype w;
        private rawtype scaleFactor;
    }

    /**
     * Default constructor.
     */

    public RawtypeNTTStepStrategy()
    {
    }

    public void multiplyElements(ArrayAccess arrayAccess, int startRow, int startColumn, int rows, int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
        throws ApfloatRuntimeException
    {
        ParallelRunnable parallelRunnable = createMultiplyElementsParallelRunnable(arrayAccess, startRow, startColumn, rows, columns, length, totalTransformLength, isInverse, modulus);

        ParallelRunner.runParallel(parallelRunnable);
    }

    public void transformRows(ArrayAccess arrayAccess, int length, int count, boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException
    {
        ParallelRunnable parallelRunnable = createTransformRowsParallelRunnable(arrayAccess, length, count, isInverse, permute, modulus);

        ParallelRunner.runParallel(parallelRunnable);
    }

    public long getMaxTransformLength()
    {
        return MAX_TRANSFORM_LENGTH;
    }

    /**
     * Create a ParallelRunnable object for multiplying the elements of the matrix.
     *
     * @param arrayAccess The memory array to multiply.
     * @param startRow Which row in the whole matrix the starting row in the <code>arrayAccess</code> is.
     * @param startColumn Which column in the whole matrix the starting column in the <code>arrayAccess</code> is.
     * @param rows The number of rows in the <code>arrayAccess</code> to multiply.
     * @param columns The number of columns in the matrix (= n<sub>2</sub>).
     * @param length The length of data in the matrix being transformed.
     * @param totalTransformLength The total transform length, for the scaling factor. Used only for the inverse case.
     * @param isInverse If the multiplication is done for the inverse transform or not.
     * @param modulus Index of the modulus.
     *
     * @return An object suitable for multiplying the elements of the matrix in parallel.
     */

    protected ParallelRunnable createMultiplyElementsParallelRunnable(final ArrayAccess arrayAccess, final int startRow, final int startColumn, final int rows, final int columns, long length, long totalTransformLength, boolean isInverse, int modulus)
        throws ApfloatRuntimeException
    {
        setModulus(MODULUS[modulus]);
        final rawtype w = (isInverse ?
                           getInverseNthRoot(PRIMITIVE_ROOT[modulus], length) :
                           getForwardNthRoot(PRIMITIVE_ROOT[modulus], length));
        final rawtype scaleFactor = (isInverse ?
                                     modDivide((rawtype) 1, (rawtype) totalTransformLength) :
                                     (rawtype) 1);

        ParallelRunnable parallelRunnable = new ParallelRunnable(rows)
        {
            public Runnable getRunnable(int strideStartRow, int strideRows)
            {
                ArrayAccess subArrayAccess = arrayAccess.subsequence(strideStartRow * columns, strideRows * columns);
                return new MultiplyRunnable(subArrayAccess, startRow + strideStartRow, startColumn, strideRows, columns, w, scaleFactor);
            }
        };

        return parallelRunnable;
    }

    /**
     * Create a ParallelRunnable object for transforming the rows of the matrix.
     *
     * @param arrayAccess The memory array to split to rows and to transform.
     * @param length Length of one transform (one row).
     * @param count Number of rows.
     * @param isInverse <code>true</code> if an inverse transform is performed, <code>false</code> if a forward transform is performed.
     * @param permute If permutation should be done.
     * @param modulus Index of the modulus.
     *
     * @return An object suitable for transforming the rows of the matrix in parallel.
     */

    protected ParallelRunnable createTransformRowsParallelRunnable(final ArrayAccess arrayAccess, final int length, final int count, final boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException
    {
        setModulus(MODULUS[modulus]);
        final rawtype[] wTable = (isInverse ?
                                  RawtypeWTables.getInverseWTable(modulus, length) :
                                  RawtypeWTables.getWTable(modulus, length));
        final int[] permutationTable = (permute ? Scramble.createScrambleTable(length) : null);

        ParallelRunnable parallelRunnable = new ParallelRunnable(count)
        {
            public Runnable getRunnable(int startIndex, int strideCount)
            {
                ArrayAccess subArrayAccess = arrayAccess.subsequence(startIndex * length, strideCount * length);
                return new TableFNTRunnable(length, isInverse, subArrayAccess, wTable, permutationTable);
            }
        };

        return parallelRunnable;
    }
}
