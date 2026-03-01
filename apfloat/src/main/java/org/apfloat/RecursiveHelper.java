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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiFunction;
import java.util.function.LongFunction;

/**
 * Helper class for recursive computations.
 *
 * @since 1.16.0
 * @author Mikko Tommila
 */

class RecursiveHelper
{
    private static class ParallelRecursiveTask<V>
        extends RecursiveTask<V>
    {
        public ParallelRecursiveTask(long start, long end, LongFunction<V> unitFunction, BiFunction<V, V, V> combineFunction, int numberOfProcessors)
        {
            this.start = start;
            this.end = end;
            this.unitFunction = unitFunction;
            this.combineFunction = combineFunction;
            this.numberOfProcessors = numberOfProcessors;
        }

        @Override
        public final V compute()
        {
            ApfloatContext threadCtx = ApfloatContext.getThreadContext();
            ApfloatContext ctx = (ApfloatContext) ApfloatContext.getContext().clone();
            ctx.setNumberOfProcessors(this.numberOfProcessors);
            ApfloatContext.setThreadContext(ctx);
    
            V result;
            try
            {
                if (this.start == this.end || this.numberOfProcessors <= 1)
                {
                    result = compute(this.start, this.end);
                }
                else
                {
                    int leftProcessors = this.numberOfProcessors >>> 1,
                        rightProcessors = this.numberOfProcessors + 1 >>> 1;
                    long mid = start + (end - start) / this.numberOfProcessors * leftProcessors;
                    ParallelRecursiveTask<V> left = new ParallelRecursiveTask<>(this.start, mid, this.unitFunction, this.combineFunction, leftProcessors),
                                             right = new ParallelRecursiveTask<>(mid + 1, this.end, this.unitFunction, this.combineFunction, rightProcessors);
                    right.fork();
                    V leftResult = left.compute(),
                      rightResult = right.join();
                    result = this.combineFunction.apply(leftResult, rightResult);
                }
            }
            finally
            {
                if (threadCtx != null)
                {
                    ApfloatContext.setThreadContext(threadCtx);
                }
                else
                {
                   ApfloatContext.removeThreadContext();
                }
            }
    
            return result;
        }

        private V compute(long n, long m)
        {
            if (n == m)
            {
                return this.unitFunction.apply(n);
            }
            long k = n + m >>> 1;
            return this.combineFunction.apply(compute(n, k), compute(k + 1, m));
        }

        public V computeSequential()
        {
            return compute(this.start, this.end);
    
        }

        private long start;
        private long end;
        private LongFunction<V> unitFunction;
        private BiFunction<V, V, V> combineFunction;
        private int numberOfProcessors;
    
        private static final long serialVersionUID = 1L;
    }

    private RecursiveHelper()
    {
    }

    /**
     * Computes the given recursive task in parallel using multiple threads.
     * The task can be computed in parallel only if the current context
     * ExecutorService is a ForkJoinPool (which is the default). Otherwise
     * it's computed sequentially.
     *
     * @param <V> Return value type.
     * @param start Start index for recursive computation (inclusive).
     * @param end End index for recursive computation (inclusive).
     * @param unitFunction Function to calculate result for a single index.
     * @param combineFunction Function to combine two sub-results.
     *
     * @return The computed value.
     */

    public static <V> V recursiveCompute(long start, long end, LongFunction<V> unitFunction, BiFunction<V, V, V> combineFunction)
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ParallelRecursiveTask<V> task = new ParallelRecursiveTask<V>(start, end, unitFunction, combineFunction, numberOfProcessors);
        ExecutorService executorService = ctx.getExecutorService();
        if (executorService instanceof ForkJoinPool)
        {
            ForkJoinPool forkJoinPool = (ForkJoinPool) executorService;
            return forkJoinPool.invoke(task);
        }
        else
        {
            return task.computeSequential();
        }
    }
}
