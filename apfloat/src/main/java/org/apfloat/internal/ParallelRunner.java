/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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
package org.apfloat.internal;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatRuntimeException;

/**
 * Class for running <code>ParallelRunnable</code> objects in parallel using
 * multiple threads.<p>
 *
 * The ParallelRunner assumes that the current {@link ApfloatContext} returns an
 * <code>ExecutorService</code> that is limited to a number of threads that is
 * one less than the number of processors. This way, when also the current thread
 * runs batches from the <code>ParallelRunnable</code>, CPU utilization should be
 * maximized but only so that no more threads are actively executing than the
 * number of processors.
 *
 * @since 1.1
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class ParallelRunner
{
    private ParallelRunner()
    {
    }

    /**
     * Run a ParallelRunnable object in parallel using multiple threads.
     * The method assumes that the <code>ExecutorService</code> returned from
     * {@link ApfloatContext#getExecutorService()} is limited to using one
     * thread less than the number of processors. This maximizes CPU usage,
     * When the <code>ParallelRunnable</code> is also run from the current thread.
     *
     * @param parallelRunnable The ParallelRunnable to be run.
     */

    public static void runParallel(ParallelRunnable parallelRunnable)
        throws ApfloatRuntimeException
    {
        ParallelRunner.tasks.add(parallelRunnable);
        try
        {
            runTasks(parallelRunnable);
        }
        finally
        {
            ParallelRunner.tasks.remove(parallelRunnable);
        }
    }

    /**
     * While waiting for a <code>Future</code> to be completed, steal a minimal
     * amount of work from any running task and run it.
     *
     * @param future The Future to wait for.
     */

    public static void wait(Future<?> future)
    {
        Runnable stealer = () -> {
            while (!future.isDone())
            {
                // Try and get any running task
                ParallelRunnable parallelRunnable = ParallelRunner.tasks.peek();
                if (parallelRunnable != null)
                {
                    // Steal a minimal amount of work while we wait
                    parallelRunnable.runBatch();
                }
                else
                {
                    // Actually idle - give up the rest of the CPU time slice
                    Thread.yield();
                }
            }
        };

        runTasks(stealer);
    }

    private static void runTasks(Runnable runnable)
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();

        if (numberOfProcessors > 1)
        {
            ExecutorService executorService = ctx.getExecutorService();

            for (int i = 0; i < numberOfProcessors - 1; i++)
            {
                // Process the task also in other threads
                executorService.execute(runnable);
            }
        }

        // Also process the task in the current thread, until it is finished
        runnable.run();
    }

    // Implemented as a List because the assumption is that the number of concurrent tasks is very small
    private static Queue<ParallelRunnable> tasks = new ConcurrentLinkedQueue<>();
}
