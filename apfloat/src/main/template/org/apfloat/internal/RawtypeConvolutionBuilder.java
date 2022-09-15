/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
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
package org.apfloat.internal;

import org.apfloat.spi.ConvolutionStrategy;
import org.apfloat.spi.NTTStrategy;
import static org.apfloat.internal.RawtypeConstants.*;

/**
 * Creates convolutions of suitable type for the <code>rawtype</code> type.<p>
 *
 * @see RawtypeShortConvolutionStrategy
 * @see RawtypeMediumConvolutionStrategy
 * @see RawtypeKaratsubaConvolutionStrategy
 * @see ThreeNTTConvolutionStrategy
 *
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeConvolutionBuilder
    extends AbstractConvolutionBuilder
{
    /**
     * Default constructor.
     */

    public RawtypeConvolutionBuilder()
    {
    }

    @Override
    protected int getKaratsubaCutoffPoint()
    {
        return RawtypeKaratsubaConvolutionStrategy.CUTOFF_POINT;
    }

    @Override
    protected float getKaratsubaCostFactor()
    {
        return KARATSUBA_COST_FACTOR;
    }

    @Override
    protected float getNTTCostFactor()
    {
        return NTT_COST_FACTOR;
    }

    @Override
    protected ConvolutionStrategy createShortConvolutionStrategy(int radix)
    {
        return new RawtypeShortConvolutionStrategy(radix);
    }

    @Override
    protected ConvolutionStrategy createMediumConvolutionStrategy(int radix)
    {
        return new RawtypeMediumConvolutionStrategy(radix);
    }

    @Override
    protected ConvolutionStrategy createKaratsubaConvolutionStrategy(int radix)
    {
        return new RawtypeKaratsubaConvolutionStrategy(radix);
    }

    @Override
    protected ConvolutionStrategy createThreeNTTConvolutionStrategy(int radix, NTTStrategy nttStrategy)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, nttStrategy);
    }
}
