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

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.10.0
 * @author Mikko Tommila
 */

public class RawtypeNTTBuilderTest
    extends RawtypeTestCase
{
    public RawtypeNTTBuilderTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new RawtypeNTTBuilderTest("testCreate"));

        return suite;
    }

    public static void testCreate()
    {
        NTTBuilder nttBuilder = new RawtypeNTTBuilder();
        ApfloatContext ctx = ApfloatContext.getContext();
        int cacheSize = ctx.getCacheL1Size() / sizeof(rawtype);
        long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize() / sizeof(rawtype);

        assertTrue("Fits in cache", nttBuilder.createNTT(cacheSize / 2) instanceof RawtypeTableFNTStrategy);
        assertTrue("Fits in memory", nttBuilder.createNTT(Util.round2down(Math.min(maxMemoryBlockSize, Integer.MAX_VALUE))) instanceof SixStepFNTStrategy);
        assertTrue("Does not fit in memory", nttBuilder.createNTT(Util.round2down(maxMemoryBlockSize * 2)) instanceof TwoPassFNTStrategy);
        assertTrue("Factor 3", nttBuilder.createNTT(3) instanceof Factor3NTTStrategy);

        assertTrue("NTTStepStrategy", nttBuilder.createNTTSteps() instanceof NTTStepStrategy);
        assertTrue("NTTConvolutionStepStrategy", nttBuilder.createNTTConvolutionSteps() instanceof NTTConvolutionStepStrategy);
        assertTrue("Factor3NTTStepStrategy", nttBuilder.createFactor3NTTSteps() instanceof Factor3NTTStepStrategy);

        ctx.setMaxMemoryBlockSize(0x80000000L * sizeof(rawtype));
        assertTrue("Does not fit in array", nttBuilder.createNTT(0x80000000L) instanceof TwoPassFNTStrategy);
        ctx.setMaxMemoryBlockSize(maxMemoryBlockSize * sizeof(rawtype));
    }
}
