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

import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.NTTStrategy;
import org.apfloat.spi.Factor3NTTStepStrategy;
import org.apfloat.spi.DataStorage;
import org.apfloat.spi.Util;

/**
 * A transform that implements a 3-point transform on
 * top of another Number Theoretic Transform that does
 * transforms of length 2<sup>n</sup>.
 *
 * @see Factor3NTTStepStrategy
 *
 * @since 1.7.0
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class Factor3NTTStrategy
    implements NTTStrategy, Parallelizable
{
    /**
     * Creates a new factor-3 transform strategy on top of an existing transform.
     * The underlying transform needs to be capable of only doing transforms of
     * length 2<sup>n</sup>.
     *
     * @param factor2Strategy The underlying transformation strategy, that can be capable of only doing radix-2 transforms.
     */

    public Factor3NTTStrategy(NTTStrategy factor2Strategy)
    {
        this.factor2Strategy = factor2Strategy;
        ApfloatContext ctx = ApfloatContext.getContext();
        this.stepStrategy = ctx.getBuilderFactory().getNTTBuilder().createFactor3NTTSteps();
    }

    @Override
    public void transform(DataStorage dataStorage, int modulus)
        throws ApfloatRuntimeException
    {
        long length = dataStorage.getSize(),
             power2length = (length & -length);

        if (length > this.stepStrategy.getMaxTransformLength())
        {
            throw new TransformLengthExceededException("Maximum transform length exceeded: " + length + " > " + this.stepStrategy.getMaxTransformLength());
        }

        if (length == power2length)
        {
            // Transform length is a power of two
            this.factor2Strategy.transform(dataStorage, modulus);
        }
        else
        {
            // Transform length is three times a power of two
            assert (length == 3 * power2length);

            DataStorage dataStorage0 = dataStorage.subsequence(0, power2length),
                        dataStorage1 = dataStorage.subsequence(power2length, power2length),
                        dataStorage2 = dataStorage.subsequence(2 * power2length, power2length);

            // Transform the columns
            this.stepStrategy.transformColumns(dataStorage0, dataStorage1, dataStorage2, 0, power2length, power2length, length, false, modulus);

            // Transform the rows
            this.factor2Strategy.transform(dataStorage0, modulus);
            this.factor2Strategy.transform(dataStorage1, modulus);
            this.factor2Strategy.transform(dataStorage2, modulus);
        }
    }

    @Override
    public void inverseTransform(DataStorage dataStorage, int modulus, long totalTransformLength)
        throws ApfloatRuntimeException
    {
        long length = dataStorage.getSize(),
             power2length = (length & -length);

        if (Math.max(length, totalTransformLength) > this.stepStrategy.getMaxTransformLength())
        {
            throw new TransformLengthExceededException("Maximum transform length exceeded: " + Math.max(length, totalTransformLength) + " > " + this.stepStrategy.getMaxTransformLength());
        }

        if (length == power2length)
        {
            // Transform length is a power of two
            this.factor2Strategy.inverseTransform(dataStorage, modulus, totalTransformLength);
        }
        else
        {
            // Transform length is three times a power of two
            assert (length == 3 * power2length);

            DataStorage dataStorage0 = dataStorage.subsequence(0, power2length),
                        dataStorage1 = dataStorage.subsequence(power2length, power2length),
                        dataStorage2 = dataStorage.subsequence(2 * power2length, power2length);

            // Transform the rows
            this.factor2Strategy.inverseTransform(dataStorage0, modulus, totalTransformLength);
            this.factor2Strategy.inverseTransform(dataStorage1, modulus, totalTransformLength);
            this.factor2Strategy.inverseTransform(dataStorage2, modulus, totalTransformLength);

            // Transform the columns
            this.stepStrategy.transformColumns(dataStorage0, dataStorage1, dataStorage2, 0, power2length, power2length, length, true, modulus);
        }
    }

    @Override
    public long getTransformLength(long size)
    {
        // Calculates the needed transform length, that is
        // a power of two, or three times a power of two
        return Util.round23up(size);
    }

    /**
     * The factor-3 NTT steps.
     */
    protected Factor3NTTStepStrategy stepStrategy;

    private NTTStrategy factor2Strategy;
}
