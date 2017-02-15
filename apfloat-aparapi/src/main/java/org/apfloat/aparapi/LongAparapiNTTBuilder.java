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

import org.apfloat.spi.BuilderFactory;
import org.apfloat.spi.NTTStrategy;
import org.apfloat.ApfloatContext;
import org.apfloat.internal.LongNTTBuilder;

/**
 * NTT Builder for aparapi transform implementations for the <code>long</code> element type.
 *
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class LongAparapiNTTBuilder
    extends LongNTTBuilder
{
    private static final int MIN_GPU_LENGTH = 1048576;

    /**
     * Default constructor.
     */

    public LongAparapiNTTBuilder()
    {
    }

    @Override
    protected NTTStrategy createSixStepFNTStrategy(long size)
    {
        long length = size;
        if (length < MIN_GPU_LENGTH)
        {
            return super.createSixStepFNTStrategy(size); 
        }
        return new LongAparapiSixStepFNTStrategy();
    }

    @Override
    protected NTTStrategy createTwoPassFNTStrategy(long size)
    {
        long length = size;
        if (length < MIN_GPU_LENGTH)
        {
            return super.createTwoPassFNTStrategy(size);
        }
        return new ColumnTwoPassFNTStrategy(new LongAparapiNTTStepStrategy());
    }

    @Override
    protected NTTStrategy createFactor3NTTStrategy(long size, NTTStrategy nttStrategy)
    {
        if (nttStrategy instanceof LongAparapiSixStepFNTStrategy)
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            BuilderFactory builderFactory = ctx.getBuilderFactory();
            long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize() / builderFactory.getElementSize();

            if (size <= maxMemoryBlockSize && size <= Integer.MAX_VALUE)
            {
                return new LongAparapiFactor3NTTStrategy();
            }
        }
        return super.createFactor3NTTStrategy(size, nttStrategy);
    }
}
