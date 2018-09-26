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
import org.apfloat.spi.NTTConvolutionStepStrategy;
import org.apfloat.spi.DataStorage;
import static org.apfloat.internal.RawtypeModConstants.*;

/**
 * Steps of a three-NTT convolution for the <code>rawtype</code> type.
 * This class implements the details of the element-by-element multiplication
 * and element-by-element squaring of the transformed elements.<p>
 *
 * The in-place multiplication and squaring of the data elements is done
 * using a parallel algorithm, if the data fits in memory.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeNTTConvolutionStepStrategy
    extends RawtypeModMath
    implements NTTConvolutionStepStrategy, Parallelizable
{
    // Runnable for multiplying elements in place
    private class MultiplyInPlaceRunnable
        implements Runnable
    {
        public MultiplyInPlaceRunnable(DataStorage sourceAndDestination, DataStorage source, long offset, long length)
        {
            this.sourceAndDestination = sourceAndDestination;
            this.source = source;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void run()
        {
            DataStorage.Iterator dest = this.sourceAndDestination.iterator(DataStorage.READ_WRITE, this.offset, this.offset + this.length),
                                 src = this.source.iterator(DataStorage.READ, this.offset, this.offset + this.length);

            while (this.length > 0)
            {
                dest.setRawtype(modMultiply(dest.getRawtype(), src.getRawtype()));

                dest.next();
                src.next();
                this.length--;
            }
        }

        private DataStorage sourceAndDestination,
                            source;
        private long offset,
                     length;
    }

    // Runnable for squaring elements in place
    private class SquareInPlaceRunnable
        implements Runnable
    {
        public SquareInPlaceRunnable(DataStorage sourceAndDestination, long offset, long length)
        {
            this.sourceAndDestination = sourceAndDestination;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void run()
        {
            DataStorage.Iterator iterator = this.sourceAndDestination.iterator(DataStorage.READ_WRITE, this.offset, this.offset + this.length);

            while (this.length > 0)
            {
                rawtype value = iterator.getRawtype();
                iterator.setRawtype(modMultiply(value, value));

                iterator.next();
                this.length--;
            }
        }

        private DataStorage sourceAndDestination;
        private long offset,
                     length;
    }

    /**
     * Default constructor.
     */

    public RawtypeNTTConvolutionStepStrategy()
    {
    }

    @Override
    public void multiplyInPlace(DataStorage sourceAndDestination, DataStorage source, int modulus)
        throws ApfloatRuntimeException
    {
        assert (sourceAndDestination != source);

        long size = sourceAndDestination.getSize();

        ParallelRunnable parallelRunnable = createMultiplyInPlaceParallelRunnable(sourceAndDestination, source, modulus);

        if (size <= Integer.MAX_VALUE &&                                        // Only if the size fits in an integer, but with memory arrays it should
            sourceAndDestination.isCached() && source.isCached())               // Only if the data storage supports efficient parallel random access
        {
            ParallelRunner.runParallel(parallelRunnable);
        }
        else
        {
            parallelRunnable.run();                                             // Just run in current thread without parallelization
        }
    }

    @Override
    public void squareInPlace(DataStorage sourceAndDestination, int modulus)
        throws ApfloatRuntimeException
    {
        long size = sourceAndDestination.getSize();

        ParallelRunnable parallelRunnable = createSquareInPlaceParallelRunnable(sourceAndDestination, modulus);

        if (size <= Integer.MAX_VALUE &&                                    // Only if the size fits in an integer, but with memory arrays it should
            sourceAndDestination.isCached())                                // Only if the data storage supports efficient parallel random access
        {
            ParallelRunner.runParallel(parallelRunnable);
        }
        else
        {
            parallelRunnable.run();                                         // Just run in current thread without parallelization
        }
    }

    /**
     * Create a ParallelRunnable for multiplying the elements in-place.
     *
     * @param sourceAndDestination The first source data storage, which is also the destination.
     * @param source The second source data storage.
     * @param modulus Which modulus to use (0, 1, 2)
     *
     * @return An object suitable for multiplying the elements in parallel.
     */

    protected ParallelRunnable createMultiplyInPlaceParallelRunnable(DataStorage sourceAndDestination, DataStorage source, int modulus)
    {
        long size = sourceAndDestination.getSize();

        setModulus(MODULUS[modulus]);

        ParallelRunnable parallelRunnable = new ParallelRunnable(size)
        {
            @Override
            public Runnable getRunnable(long offset, long length)
            {
                return new MultiplyInPlaceRunnable(sourceAndDestination, source, offset, length);
            }
        };
        return parallelRunnable;
    }

    /**
     * Create a ParallelRunnable for squaring the elements in-place.
     *
     * @param sourceAndDestination The source data storage, which is also the destination.
     * @param modulus Which modulus to use (0, 1, 2)
     *
     * @return An object suitable for squaring the elements in parallel.
     */

    protected ParallelRunnable createSquareInPlaceParallelRunnable(DataStorage sourceAndDestination, int modulus)
    {
        long size = sourceAndDestination.getSize();

        setModulus(MODULUS[modulus]);

        ParallelRunnable parallelRunnable = new ParallelRunnable(size)
        {
            @Override
            public Runnable getRunnable(long offset, long length)
            {
                return new SquareInPlaceRunnable(sourceAndDestination, offset, length);
            }
        };
        return parallelRunnable;
    }
}
