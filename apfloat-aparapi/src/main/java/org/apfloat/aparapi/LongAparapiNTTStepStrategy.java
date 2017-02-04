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

import com.aparapi.Kernel;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.internal.LongNTTStepStrategy;
import org.apfloat.internal.LongWTables;
import org.apfloat.internal.Scramble;

import static org.apfloat.internal.LongModConstants.*;

/**
 * NTT steps for the <code>long</code> element type aparapi transforms.
 * The data is organized in columns, not rows, for efficient processing on the GPU.
 *
 * Due to the extreme parallelization requirements (global size should be at lest 1024)
 * this algorithm works efficiently only with 8 million decimal digit calculations or bigger.
 * However with 8 million digits, it's only approximately as fast as the pure-Java
 * version (depending on the GPU and CPU hardware). Depending on the total amount of memory
 * available for the GPU this algorithm will fail (or revert to the very slow software emulation)
 * e.g. at one-billion-digit calculations if your GPU has 1 GB of memory. The maximum power-of-two
 * size for a Java array is one billion (2<sup>30</sup>) so if your GPU has more than 8 GB of
 * memory then the algorithm can never fail (as any Java long[] will always fit to the GPU memory).<p>
 *
 * Some notes about the aparapi specific requirements for code that must be converted to OpenCL:
 * <ul>
 *   <li><code>assert()</code> does not work</li>
 *   <li>Can't check for null</li>
 *   <li>Can't get array length</li>
 *   <li>Arrays referenced by the kernel can't be null even if they are not accessed</li>
 *   <li>Arrays referenced by the kernel can't be zero-length even if they are not accessed</li>
 *   <li>Can't invoke methods in other classes e.g. enclosing class of an inner class</li>
 *   <li>Early return statements do not work</li>
 *   <li>Variables used inside loops must be initialized before the loop</li>
 *   <li>Must compile the class with full debug information i.e. with <code>-g</code></li>
 * </ul>
 *
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class LongAparapiNTTStepStrategy
    extends LongNTTStepStrategy
{
    // Runnable for calculating the column transforms in parallel
    private static class ColumnTableFNTRunnable
        extends Kernel
    {
        public ColumnTableFNTRunnable()
        {
        }

        public void setLength(int length)
        {
            this.length = length;               // Transform length
        }

        public void setInverse(boolean isInverse)
        {
            this.isInverse = isInverse ? 1 : 0;
        }

        public void setArrayAccess(ArrayAccess arrayAccess)
            throws ApfloatRuntimeException
        {
            this.data = arrayAccess.getLongData();
            this.offset = arrayAccess.getOffset();
            this.stride = arrayAccess.getLength() / this.length;
        }

        public void setWTable(long[] wTable)
        {
            this.wTable = wTable;
        }

        public void setPermutationTable(int[] permutationTable)
        {
            this.permutationTable = (permutationTable == null ? new int[1] : permutationTable); // Zero-length array or null won't work
            this.permutationTableLength = (permutationTable == null ? 0 : permutationTable.length);
        }

        public void run()
        {
            if (isInverse())
            {
                inverseColumnTableFNT();
            }
            else
            {
                columnTableFNT();
            }
        }

        private boolean isInverse()
        {
            return this.isInverse != 0;
        }

        private void columnTableFNT()
        {
            int nn, istep = 0, mmax = 0, r = 0;

            long[] data = this.data;
            int offset = this.offset + getGlobalId();
            int stride = this.stride;
            nn     = this.length;

            if (nn >= 2)
            {
                r = 1;
                mmax = nn >> 1;
                while (mmax > 0)
                {
                    istep = mmax << 1;

                    // Optimize first step when wr = 1

                    for (int i = offset; i < offset + nn * stride; i += istep * stride)
                    {
                        int j = i + mmax * stride;
                        long a = data[i];
                        long b = data[j];
                        data[i] = modAdd(a, b);
                        data[j] = modSubtract(a, b);
                    }

                    int t = r;

                    for (int m = 1; m < mmax; m++)
                    {
                        for (int i = offset + m * stride; i < offset + nn * stride; i += istep * stride)
                        {
                            int j = i + mmax * stride;
                            long a = data[i];
                            long b = data[j];
                            data[i] = modAdd(a, b);
                            data[j] = modMultiply(this.wTable[t], modSubtract(a, b));
                        }
                        t += r;
                    }
                    r <<= 1;
                    mmax >>= 1;
                }

                if (this.permutationTableLength > 0)
                {
                    columnScramble(offset);
                }
            }
        }

        private void inverseColumnTableFNT()
        {
            int nn, istep = 0, mmax = 0, r = 0;

            long[] data = this.data;
            int offset = this.offset + getGlobalId();
            int stride = this.stride;
            nn     = this.length;

            if (nn >= 2)
            {
                if (this.permutationTableLength > 0)
                {
                    columnScramble(offset);
                }

                r = nn;
                mmax = 1;
                while (nn > mmax)
                {
                    istep = mmax << 1;
                    r >>= 1;

                    // Optimize first step when w = 1

                    for (int i = offset; i < offset + nn * stride; i += istep * stride)
                    {
                        int j = i + mmax * stride;
                        long wTemp = data[j];
                        data[j] = modSubtract(data[i], wTemp);
                        data[i] = modAdd(data[i], wTemp);
                    }

                    int t = r;

                    for (int m = 1; m < mmax; m++)
                    {
                        for (int i = offset + m * stride; i < offset + nn * stride; i += istep * stride)
                        {
                            int j = i + mmax * stride;
                            long wTemp = modMultiply(this.wTable[t], data[j]);
                            data[j] = modSubtract(data[i], wTemp);
                            data[i] = modAdd(data[i], wTemp);
                        }
                        t += r;
                    }
                    mmax = istep;
                }
            }
        }

        private void columnScramble(int offset)
        {
            for (int k = 0; k < this.permutationTableLength; k += 2)
            {
                int i = offset + this.permutationTable[k] * this.stride,
                    j = offset + this.permutationTable[k + 1] * this.stride;
                long tmp = this.data[i];
                this.data[i] = this.data[j];
                this.data[j] = tmp;
            }
        }

        private long modMultiply(long a, long b)
        {
            long r = a * b - this.modulus * (long) ((double) a * (double) b * this.inverseModulus);
            r -= this.modulus * (int) ((double) r * this.inverseModulus);

            r = (r >= this.modulus ? r - this.modulus : r);
            r = (r < 0 ? r + this.modulus : r);

            return r;
        }

        private long modAdd(long a, long b)
        {
            long r = a + b;

            return (r >= this.modulus ? r - this.modulus : r);
        }

        private long modSubtract(long a, long b)
        {
            long r = a - b;

            return (r < 0 ? r + this.modulus : r);
        }

        public void setModulus(long modulus)
        {
            this.inverseModulus = 1.0 / modulus;
            this.modulus = modulus;
        }

        private int stride;
        private int length;
        private int isInverse;
        private long[] data;
        private int offset;
        private long[] wTable;
        private int[] permutationTable;
        private int permutationTableLength;

        private long modulus;
        private double inverseModulus;
    }

    private static ThreadLocal<ColumnTableFNTRunnable> kernel = new ThreadLocal<ColumnTableFNTRunnable>()
    {
        public ColumnTableFNTRunnable initialValue()
        {
            return new ColumnTableFNTRunnable();
        }
    };

    /**
     * Default constructor.
     */

    public LongAparapiNTTStepStrategy()
    {
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

    public void transformRows(ArrayAccess arrayAccess, int length, int count, boolean isInverse, boolean permute, int modulus)
        throws ApfloatRuntimeException
    {
        long[] wTable = (isInverse ?
                         LongWTables.getInverseWTable(modulus, length) :
                         LongWTables.getWTable(modulus, length));
        int[] permutationTable = (permute ? Scramble.createScrambleTable(length) : null);

        ColumnTableFNTRunnable kernel = LongAparapiNTTStepStrategy.kernel.get();
        kernel.setLength(length);
        kernel.setInverse(isInverse);
        kernel.setArrayAccess(arrayAccess);
        kernel.setWTable(wTable);
        kernel.setPermutationTable(permutationTable);
        kernel.setModulus(MODULUS[modulus]);

        kernel.execute(count);
    }
}
