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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apfloat.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.1
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class ParallelRunnerTest
    extends TestCase
{
    private static abstract class DummyFuture
        implements Future<Object>
    {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning)
        {
            return false;
        }

        @Override
        public boolean isCancelled()
        {
            return false;
        }

        @Override
        public Object get()
        {
            return null;
        }

        @Override
        public Object get(long timeout, TimeUnit unit)
        {
            return null;
        }
    }

    public ParallelRunnerTest(String methodName)
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

        suite.addTest(new ParallelRunnerTest("testRunParallel"));
        suite.addTest(new ParallelRunnerTest("testRunParallelLong"));
        suite.addTest(new ParallelRunnerTest("testRunParallelTwo"));
        suite.addTest(new ParallelRunnerTest("testWait"));

        return suite;
    }

    public static void testRunParallel()
    {
        for (int threads = 1; threads <= 32; threads++)
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            ctx.setNumberOfProcessors(threads);
            ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

            final int LENGTH = 1000000;
            int[] values = new int[LENGTH];
            ParallelRunnable parallelRunnable = new ParallelRunnable(LENGTH)
            {
                @Override
                public Runnable getRunnable(int start, int length)
                {
                    return () ->
                    {
                        for (int i = start; i < start + length; i++)
                        {
                            values[i] += i;
                        }
                    };
                }
            };

            ParallelRunner.runParallel(parallelRunnable);

            for (int i = 0; i < LENGTH; i++)
            {
                assertEquals(threads + " threads, element " + i, i, values[i]);
            }
        }
    }

    public static void testRunParallelLong()
    {
        for (int threads = 1; threads <= 32; threads++)
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            ctx.setNumberOfProcessors(threads);
            ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

            final long LENGTH = 1000000000000L;
            final int SQRT_LENGTH = 1000000;
            Map<Long, Long> starts = new ConcurrentHashMap<>();
            ParallelRunnable parallelRunnable = new ParallelRunnable(LENGTH)
            {
                @Override
                public Runnable getRunnable(long start, long length)
                {
                    return () ->
                    {
                        starts.put(start, start);
                        assertEquals("length", SQRT_LENGTH, length);
                    };
                }

                @Override
                protected long getPreferredBatchSize()
                {
                    return SQRT_LENGTH;
                }
            };

            ParallelRunner.runParallel(parallelRunnable);

            assertEquals("starts size", SQRT_LENGTH, starts.size());
            for (int i = 0; i < SQRT_LENGTH; i++)
            {
                assertTrue(i + " started", starts.containsKey(SQRT_LENGTH * (long) i));
            }
        }
    }

    public static void testRunParallelTwo()
        throws Exception
    {
        for (int threads = 1; threads <= 32; threads++)
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            ctx.setNumberOfProcessors(threads);
            ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

            final int LENGTH = 1000000;
            int[] values = new int[LENGTH];
            ParallelRunnable parallelRunnable = new ParallelRunnable(LENGTH)
            {
                @Override
                public Runnable getRunnable(int start, int length)
                {
                    return () ->
                    {
                        for (int i = start; i < start + length; i++)
                        {
                            values[i] += i;
                        }
                    };
                }
            };

            int[] values2 = new int[LENGTH];
            ParallelRunnable parallelRunnable2 = new ParallelRunnable(LENGTH)
            {
                @Override
                public Runnable getRunnable(int start, int length)
                {
                    return () ->
                    {
                        for (int i = start; i < start + length; i++)
                        {
                            values2[i] += i;
                        }
                    };
                }
            };

            Future<?> future = ctx.getExecutorService().submit(parallelRunnable);
            ParallelRunner.runParallel(parallelRunnable2);
            future.get();

            for (int i = 0; i < LENGTH; i++)
            {
                assertEquals(threads + " threads, element " + i, i, values[i]);
            }

            for (int i = 0; i < LENGTH; i++)
            {
                assertEquals(threads + " threads, other element " + i, i, values2[i]);
            }
        }
    }

    public static void testWait()
    {
        for (int threads = 2; threads <= 32; threads += 2)
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            ctx.setNumberOfProcessors(threads);
            ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
            ctx.setNumberOfProcessors(threads / 2); // Both ParallelRunner and stealer thread use half of the available processors

            final int LENGTH = 10000;
            Map<String, String> threadNames = new ConcurrentHashMap<>();
            AtomicBoolean done = new AtomicBoolean();
            ParallelRunnable parallelRunnable = new ParallelRunnable(LENGTH)
            {
                @Override
                public Runnable getRunnable(int start, int length)
                {
                    return () ->
                    {
                        threadNames.put(Thread.currentThread().getName(), "");
                        sleepUninterrupted(20);
                    };
                }
            };

            // In some other thread, steal some work for the entire execution time (do not let the ExecutorService use this thread for anything else)
            Runnable otherTask = () ->
            {
                sleepUninterrupted(10);
                Future<?> dummyFuture = new DummyFuture()
                {
                    @Override
                    public boolean isDone()
                    {
                        return done.get();
                    }
                };
                ctx.wait(dummyFuture);
            };

            ctx.getExecutorService().execute(otherTask);
            ParallelRunner.runParallel(parallelRunnable);
            done.set(true);

            assertEquals(threads + " threads (" + threadNames + ")", threads, threadNames.size());
        }
    }

    private static void sleepUninterrupted(long time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException ie)
        {
            fail(ie.toString());
        }
    }
}
