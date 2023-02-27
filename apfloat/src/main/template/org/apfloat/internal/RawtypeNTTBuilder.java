/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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

import org.apfloat.spi.NTTStrategy;
import org.apfloat.spi.NTTStepStrategy;
import org.apfloat.spi.Factor3NTTStepStrategy;
import org.apfloat.spi.NTTConvolutionStepStrategy;

/**
 * Creates Number Theoretic Transforms for the
 * <code>rawtype</code> type.
 *
 * @see RawtypeTableFNTStrategy
 * @see SixStepFNTStrategy
 * @see TwoPassFNTStrategy
 * @see Factor3NTTStrategy
 *
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class RawtypeNTTBuilder
    extends AbstractNTTBuilder
{
    /**
     * Default constructor.
     */

    public RawtypeNTTBuilder()
    {
    }

    @Override
    public NTTStepStrategy createNTTSteps()
    {
        return new RawtypeNTTStepStrategy();
    }

    @Override
    public NTTConvolutionStepStrategy createNTTConvolutionSteps()
    {
        return new RawtypeNTTConvolutionStepStrategy();
    }

    @Override
    public Factor3NTTStepStrategy createFactor3NTTSteps()
    {
        return new RawtypeFactor3NTTStepStrategy();
    }

    @Override
    protected NTTStrategy createSimpleFNTStrategy(long size)
    {
        return new RawtypeTableFNTStrategy();
    }
}
