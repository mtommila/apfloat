/*
 * MIT License
 *
 * Copyright (c) 2002-2026 Mikko Tommila
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
package org.apfloat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import junit.framework.TestSuite;

/**
 * @version 1.16.0
 * @author Mikko Tommila
 */

public class RecursiveHelperTest
    extends ApfloatTestCase
{
    public RecursiveHelperTest(String methodName)
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

        suite.addTest(new RecursiveHelperTest("testRecursiveCompute"));
        suite.addTest(new RecursiveHelperTest("testSequentialCompute"));

        return suite;
    }

    public static void testRecursiveCompute()
    {
        ApfloatContext ctx = (ApfloatContext) ApfloatContext.getContext().clone();
        ApfloatContext.setThreadContext(ctx);
        for (int n = 7; n <= 19; n++)
        {
            long expected = 1;
            for (int i = 1; i <= n; i++)
            {
                expected *= i;
            }
            for (int p = 1; p <= 32; p++)
            {
                ExecutorService executorService = new ForkJoinPool(p);
                ctx.setExecutorService(executorService);
                ctx.setNumberOfProcessors(p);

                long factorial = RecursiveHelper.recursiveCompute(1, n, i -> i, (a, b) -> a * b);
                assertEquals(n + "! with " + p + " threads", expected, factorial);

                executorService.shutdown();
            }
        }
        ApfloatContext.removeThreadContext();
    }

    public static void testSequentialCompute()
    {
        ApfloatContext ctx = (ApfloatContext) ApfloatContext.getContext().clone();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ctx.setExecutorService(executorService);
        ApfloatContext.setThreadContext(ctx);

        long factorial = RecursiveHelper.recursiveCompute(1, 15, i -> i, (a, b) -> a * b);
        assertEquals("15!", 1307674368000L, factorial);

        ApfloatContext.removeThreadContext();
        executorService.shutdown();
    }
}
