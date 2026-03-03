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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

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
        suite.addTest(new RecursiveHelperTest("testRecursiveComputeMoreIndexesThanProcessors"));
        suite.addTest(new RecursiveHelperTest("testRecursiveComputeMoreProcessorsThanIndexes"));
        suite.addTest(new RecursiveHelperTest("testSequentialCompute"));

        return suite;
    }

    public static void testRecursiveCompute()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        ExecutorService globalExecutorService = ctx.getExecutorService();
        int globalNumberOfProcessors = ctx.getNumberOfProcessors();

        for (int n = 7; n <= 19; n++)
        {
            long expected = 1;
            for (int i = 1; i <= n; i++)
            {
                expected *= i;
            }
            for (int p = 2; p <= 32; p++)
            {
                ExecutorService executorService = new ForkJoinPool(p - 1);
                ctx.setExecutorService(executorService);
                ctx.setNumberOfProcessors(p);

                long factorial = RecursiveHelper.recursiveCompute(1, n, i -> i, (a, b) -> a * b);
                assertEquals(n + "! with " + p + " threads", expected, factorial);

                executorService.shutdown();
            }
        }

        ctx.setExecutorService(globalExecutorService);
        ctx.setNumberOfProcessors(globalNumberOfProcessors);
    }

    public static void testRecursiveComputeMoreIndexesThanProcessors()
    {
        // Actually same number of indexes and processors
        ApfloatContext ctx = ApfloatContext.getContext();
        ExecutorService globalExecutorService = ctx.getExecutorService();
        int globalNumberOfProcessors = ctx.getNumberOfProcessors();

        for (int p = 2; p <= 32; p++)
        {
            ExecutorService executorService = new ForkJoinPool(p - 1);
            ctx.setExecutorService(executorService);
            ctx.setNumberOfProcessors(p);
            assertNull("Thread context before", ApfloatContext.getThreadContext());

            Set<Thread> threads = ConcurrentHashMap.newKeySet();
            CountDownLatch latch = new CountDownLatch(p);
            int t = p;
            RecursiveHelper.recursiveCompute(1, p, i -> await(t, i, threads, latch), (a, b) -> null);
            assertEquals(p + " threads", p, threads.size());

            assertNull("Thread context after", ApfloatContext.getThreadContext());
            executorService.shutdown();
        }

        ctx.setExecutorService(globalExecutorService);
        ctx.setNumberOfProcessors(globalNumberOfProcessors);
    }

    private static Void await(int t, long i, Set<Thread> threads, CountDownLatch latch)
    {
        int numberOfProcessors = ApfloatContext.getContext().getNumberOfProcessors();
        assertEquals("Number of processors", 1, numberOfProcessors);
        threads.add(Thread.currentThread());
        latch.countDown();
        try
        {
            assertTrue(t + " threads, index " + i + ", more indexes than processors, latch reached zero", latch.await(5000, TimeUnit.MILLISECONDS));
        }
        catch (InterruptedException ie)
        {
            throw new RuntimeException(ie);
        }
        return null;
    }

    public static void testRecursiveComputeMoreProcessorsThanIndexes()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        ExecutorService globalExecutorService = ctx.getExecutorService();
        int globalNumberOfProcessors = ctx.getNumberOfProcessors();

        for (int p = 2; p <= 32; p++)
        {
            ExecutorService executorService = new ForkJoinPool(p * p - 1);
            ctx.setExecutorService(executorService);
            ctx.setNumberOfProcessors(p * p);
            assertNull("Thread context before", ApfloatContext.getThreadContext());

            Map<Integer, Integer> processorCounts = new ConcurrentHashMap<>();
            CountDownLatch latch = new CountDownLatch(p);
            int t = p;
            RecursiveHelper.recursiveCompute(1, p, i -> await(t, i, processorCounts, latch), (a, b) -> null);
            assertEquals(p + " threads different processor counts", 1, processorCounts.size());
            assertEquals(p + " threads processor count", (Integer) p, processorCounts.get(p));

            assertNull("Thread context after", ApfloatContext.getThreadContext());
            executorService.shutdown();
        }

        ctx.setExecutorService(globalExecutorService);
        ctx.setNumberOfProcessors(globalNumberOfProcessors);
    }

    private static Void await(int p, long i, Map<Integer, Integer> processorCounts, CountDownLatch latch)
    {
        int numberOfProcessors = ApfloatContext.getContext().getNumberOfProcessors();
        assertEquals("Index " + i + " number of processors", p, numberOfProcessors);
        processorCounts.compute(p, (k, v) -> (v == null ? 0 : v) + 1);
        latch.countDown();
        try
        {
            assertTrue(p + " threads, index " + i + ", more processors than indexes, latch reached zero", latch.await(5000, TimeUnit.MILLISECONDS));
        }
        catch (InterruptedException ie)
        {
            throw new RuntimeException(ie);
        }
        return null;
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
