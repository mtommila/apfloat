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
import org.apfloat.spi.Factor3NTTStepStrategy;
import org.apfloat.spi.DataStorage;
import static org.apfloat.internal.RawtypeModConstants.*;

/**
 * Steps for the factor-3 NTT.<p>
 *
 * The transform is done using a parallel algorithm, if the data fits in memory.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeFactor3NTTStepStrategy
    extends RawtypeModMath
    implements Factor3NTTStepStrategy, Parallelizable
{
    // Runnable for transforming the columns in a factor-3 transform
    private class ColumnTransformRunnable
        implements Runnable
    {
        public ColumnTransformRunnable(DataStorage dataStorage0, DataStorage dataStorage1, DataStorage dataStorage2, long startColumn, long columns, rawtype w, rawtype ww, rawtype w1, rawtype w2, boolean isInverse)
        {
            this.dataStorage0 = dataStorage0;
            this.dataStorage1 = dataStorage1;
            this.dataStorage2 = dataStorage2;
            this.startColumn = startColumn;
            this.columns = columns;
            this.w = w;
            this.ww = ww;
            this.w1 = w1;
            this.w2 = w2;
            this.isInverse = isInverse;
        }

        @Override
        public void run()
        {
            rawtype tmp1 = modPow(this.w, (rawtype) this.startColumn),
                    tmp2 = modPow(this.ww, (rawtype) this.startColumn);

            DataStorage.Iterator iterator0 = this.dataStorage0.iterator(DataStorage.READ_WRITE, this.startColumn, this.startColumn + this.columns),
                                 iterator1 = this.dataStorage1.iterator(DataStorage.READ_WRITE, this.startColumn, this.startColumn + this.columns),
                                 iterator2 = this.dataStorage2.iterator(DataStorage.READ_WRITE, this.startColumn, this.startColumn + this.columns);

            for (long i = 0; i < this.columns; i++)
            {
                // 3-point WFTA on the corresponding array elements

                rawtype x0 = iterator0.getRawtype(),
                        x1 = iterator1.getRawtype(),
                        x2 = iterator2.getRawtype(),
                        t;

                if (this.isInverse)
                {
                    // Multiply before transform
                    x1 = modMultiply(x1, tmp1);
                    x2 = modMultiply(x2, tmp2);
                }

                // Transform columns
                t = modAdd(x1, x2);
                x2 = modSubtract(x1, x2);
                x0 = modAdd(x0, t);
                t = modMultiply(t, this.w1);
                x2 = modMultiply(x2, this.w2);
                t = modAdd(t, x0);
                x1 = modAdd(t, x2);
                x2 = modSubtract(t, x2);

                if (!this.isInverse)
                {
                    // Multiply after transform
                    x1 = modMultiply(x1, tmp1);
                    x2 = modMultiply(x2, tmp2);
                }

                iterator0.setRawtype(x0);
                iterator1.setRawtype(x1);
                iterator2.setRawtype(x2);

                iterator0.next();
                iterator1.next();
                iterator2.next();

                tmp1 = modMultiply(tmp1, this.w);
                tmp2 = modMultiply(tmp2, this.ww);
            }
        }

        private DataStorage dataStorage0;
        private DataStorage dataStorage1;
        private DataStorage dataStorage2;
        private long startColumn;
        private long columns;
        private rawtype w;
        private rawtype ww;
        private rawtype w1;
        private rawtype w2;
        private boolean isInverse;
    }

    /**
     * Default constructor.
     */

    public RawtypeFactor3NTTStepStrategy()
    {
    }

    @Override
    public void transformColumns(DataStorage dataStorage0, DataStorage dataStorage1, DataStorage dataStorage2, long startColumn, long columns, long power2length, long length, boolean isInverse, int modulus)
        throws ApfloatRuntimeException
    {
        // Transform length is three times a power of two
        assert (length == 3 * power2length);

        ParallelRunnable parallelRunnable = createColumnTransformParallelRunnable(dataStorage0, dataStorage1, dataStorage2, startColumn, columns, power2length, length, isInverse, modulus);

        if (columns <= Integer.MAX_VALUE &&                                     // Only if the size fits in an integer, but with memory arrays it should
            dataStorage0.isCached() &&                                          // Only if the data storage supports efficient parallel random access
            dataStorage1.isCached() &&
            dataStorage2.isCached())
        {
            ParallelRunner.runParallel(parallelRunnable);
        }
        else
        {
            parallelRunnable.run();                                             // Just run in current thread without parallelization
        }
    }

    @Override
    public long getMaxTransformLength()
    {
        return MAX_TRANSFORM_LENGTH;
    }

    /**
     * Create a ParallelRunnable object for transforming the columns of the matrix
     * using a 3-point NTT transform.
     *
     * @param dataStorage0 The data of the first column.
     * @param dataStorage1 The data of the second column.
     * @param dataStorage2 The data of the third column.
     * @param startColumn The starting element index in the data storages to transform.
     * @param columns How many columns to transform.
     * @param power2length Length of the column transform.
     * @param length Length of total transform (three times the length of one column).
     * @param isInverse <code>true</code> if an inverse transform is performed, <code>false</code> if a forward transform is performed.
     * @param modulus Index of the modulus.
     *
     * @return A suitable object for performing the 3-point transforms in parallel.
     */

    protected ParallelRunnable createColumnTransformParallelRunnable(DataStorage dataStorage0, DataStorage dataStorage1, DataStorage dataStorage2, long startColumn, long columns, long power2length, long length, boolean isInverse, int modulus)
    {
        setModulus(MODULUS[modulus]);                                             // Modulus
        rawtype w = (isInverse ?
                     getInverseNthRoot(PRIMITIVE_ROOT[modulus], length) :
                     getForwardNthRoot(PRIMITIVE_ROOT[modulus], length)),   // Forward/inverse n:th root
                w3 = modPow(w, (rawtype) power2length),                     // Forward/inverse 3rd root
                ww = modMultiply(w, w),
                w1 = negate(modDivide((rawtype) 3, (rawtype) 2)),
                w2 = modAdd(w3, modDivide((rawtype) 1, (rawtype) 2));

        ParallelRunnable parallelRunnable = new ParallelRunnable(columns)
        {
            @Override
            public Runnable getRunnable(long strideStartColumn, long strideColumns)
            {
                return new ColumnTransformRunnable(dataStorage0, dataStorage1, dataStorage2, startColumn + strideStartColumn, strideColumns, w, ww, w1, w2, isInverse);
            }
        };
        return parallelRunnable;
    }
}
