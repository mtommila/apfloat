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

import org.apfloat.spi.BuilderFactory;
import org.apfloat.spi.NTTStrategy;
import org.apfloat.ApfloatContext;
import org.apfloat.internal.IntNTTBuilder;

/**
 * NTT Builder for aparapi transform implementations for the <code>int</code> element type.
 *
 * @since 1.8.3
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class IntAparapiNTTBuilder
    extends IntNTTBuilder
{
    private static final int MIN_GPU_LENGTH = 1048576;

    /**
     * Basic constructor.
     *
     * @param rowOrientation If the data is using row orientation.
     */

    public IntAparapiNTTBuilder(boolean rowOrientation)
    {
        this.rowOrientation = rowOrientation;
    }

    @Override
    protected NTTStrategy createSixStepFNTStrategy(long size)
    {
        long length = size;
        if (length < MIN_GPU_LENGTH)
        {
            return super.createSixStepFNTStrategy(size); 
        }
        return this.rowOrientation ? new IntAparapiSixStepFNTStrategy() : new IntAparapiColumnSixStepFNTStrategy();
    }

    @Override
    protected NTTStrategy createTwoPassFNTStrategy(long size)
    {
        long length = size;
        if (length < MIN_GPU_LENGTH)
        {
            return super.createTwoPassFNTStrategy(size);
        }
        return (this.rowOrientation ? new IntAparapiTwoPassFNTStrategy() : new IntAparapiColumnTwoPassFNTStrategy());
    }

    @Override
    protected NTTStrategy createFactor3NTTStrategy(long size, NTTStrategy nttStrategy)
    {
        if (nttStrategy instanceof IntAparapiNTTStrategy)
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            BuilderFactory builderFactory = ctx.getBuilderFactory();
            long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize() / builderFactory.getElementSize();

            if (size <= maxMemoryBlockSize && size <= Integer.MAX_VALUE)
            {
                return new IntAparapiFactor3NTTStrategy(nttStrategy);
            }
        }
        return super.createFactor3NTTStrategy(size, nttStrategy);
    }

    private boolean rowOrientation;
}
