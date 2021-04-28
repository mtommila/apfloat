/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class RawtypeTwoPassFNTStrategyTest
    extends RawtypeNTTStrategyTestCase
{
    public RawtypeTwoPassFNTStrategyTest(String methodName)
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

        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTrip"));
        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTripBig"));
        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTripMultithread"));
        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTripMultithreadBig"));

        return suite;
    }

    public static void testRoundTrip()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(1);
        runRoundTrip(131072);
    }

    public static void testRoundTripBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(1);
        runRoundTrip((int) Math.min(1 << 21, Util.round2down(RawtypeModConstants.MAX_TRANSFORM_LENGTH)));
    }

    public static void testRoundTripMultithread()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip(131072);
    }

    public static void testRoundTripMultithreadBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip((int) Math.min(1 << 21, Util.round2down(RawtypeModConstants.MAX_TRANSFORM_LENGTH)));
    }

    private static void runRoundTrip(int size)
    {
        runRoundTrip(new TwoPassFNTStrategy(), size);
    }
}
