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

import java.util.concurrent.Future;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeParallelThreeNTTConvolutionStrategyTest
    extends RawtypeThreeNTTConvolutionStrategyTest
{
    public RawtypeParallelThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAutoBigParallel"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testSharedMemoryLock"));

        return suite;
    }

    @Override
    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, transform);
    }

    public void testSharedMemoryLock() throws Exception
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        long sharedMemoryTreshold = ctx.getSharedMemoryTreshold();
        ctx.setSharedMemoryTreshold(16384);

        Future<?> future = ctx.getExecutorService().submit(this::testFullHugeParallel);
        testFullHugeParallel();
        future.get();

        ctx.setSharedMemoryTreshold(sharedMemoryTreshold);
    }
}
