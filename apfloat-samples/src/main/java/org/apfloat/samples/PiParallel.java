/*
 * MIT License
 *
 * Copyright (c) 2002-2024 Mikko Tommila
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
package org.apfloat.samples;

import java.io.PrintWriter;
import java.io.IOException;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatInterruptedException;
import org.apfloat.ApfloatRuntimeException;

/**
 * Calculates pi using multiple threads in parallel.<p>
 *
 * Note that to get any performance gain from running many
 * threads in parallel, the JVM must be executing native threads.
 * If the JVM is running in green threads mode, there is no
 * advantage of having multiple threads, as the JVM will in fact
 * execute just one thread and divide its time to multiple
 * simulated threads.
 *
 * @version 1.14.0
 * @author Mikko Tommila
 */

public class PiParallel
    extends Pi
{
    /**
     * Parallel version of the binary splitting algorithm.
     * Uses multiple threads to calculate pi in parallel.
     */

    protected static class ParallelBinarySplittingPiCalculator
        extends BinarySplittingPiCalculator
    {
        /**
         * Construct a parallel pi calculator with the specified precision and radix.
         *
         * @param series The binary splitting series to be used.
         */

        public ParallelBinarySplittingPiCalculator(BinarySplittingSeries series)
            throws ApfloatRuntimeException
        {
            super(series);
        }

        @Override
        public void r(long n1, long n2, ApfloatHolder T, ApfloatHolder Q, ApfloatHolder P, BinarySplittingProgressIndicator progressIndicator)
            throws ApfloatRuntimeException
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            int numberOfProcessors = ctx.getNumberOfProcessors();

            if (n1 == n2)
            {
                // Pathological case where available threads > terms needed

                T.setApfloat(Apfloat.ZERO);
                Q.setApfloat(Apfloat.ONE);
                if (P != null) P.setApfloat(Apfloat.ONE);
            }
            else if (numberOfProcessors == 1)
            {
                // End of splitting work between threads
                // calculate remaining terms on the current thread

                super.r(n1, n2, T, Q, P, progressIndicator);
            }
            else
            {
                // Multiple threads available

                ApfloatHolder LT = new ApfloatHolder(),
                              LQ = new ApfloatHolder(),
                              LP = new ApfloatHolder();

                if (split(n1, n2, numberOfProcessors))
                {
                    // Split work in ratio of number of threads and execute in parallel

                    int numberOfProcessors1 = numberOfProcessors / 2,
                        numberOfProcessors2 = numberOfProcessors - numberOfProcessors1;

                    long nMiddle = n1 + (n2 - n1) * numberOfProcessors1 / numberOfProcessors;

                    if (DEBUG) Pi.err.println("PiParallel.r(" + n1 + ", " + n2 + ") splitting " + numberOfProcessors + " threads to r(" + n1 + ", " + nMiddle + ") " + numberOfProcessors1 + " threads, r(" + nMiddle + ", " + n2 + ") " + numberOfProcessors2 + " threads");

                    // Call recursively this r() method to further split the term calculation
                    Operation<?> operation1 = () ->
                    {
                        r(n1, nMiddle, LT, LQ, LP, progressIndicator);
                        return null;
                    };
                    Operation<?> operation2 = () ->
                    {
                        r(nMiddle, n2, T, Q, P, progressIndicator);
                        return null;
                    };

                    BackgroundOperation<?> backgroundOperation = new BackgroundOperation<>(new ThreadLimitedOperation<>(operation1, numberOfProcessors1));
                    Operation<?> directOperation = () ->
                    {
                        try
                        {
                            operation2.execute();
                        }
                        catch (ApfloatInterruptedException aie)
                        {
                            backgroundOperation.cancel();
                            throw aie;
                        }
                        return backgroundOperation.getResult();     // Waits for the background operation to complete, must be run within the thread-limited context
                    };
                    new ThreadLimitedOperation<>(directOperation, numberOfProcessors2).execute();
                }
                else
                {
                    // Do not split at this point

                    if (DEBUG) Pi.err.println("PiParallel.r(" + n1 + ", " + n2 + ") not splitting " + numberOfProcessors + " threads");

                    long nMiddle = (n1 + n2) / 2;

                    r(n1, nMiddle, LT, LQ, LP, progressIndicator);
                    r(nMiddle, n2, T, Q, P, progressIndicator);
                }

                // Combine recursed results whether split in parallel or not, using all threads available here

                T.setApfloat(Q.getApfloat().multiply(LT.getApfloat()).add(LP.getApfloat().multiply(T.getApfloat())));
                Q.setApfloat(LQ.getApfloat().multiply(Q.getApfloat()));
                if (P != null) P.setApfloat(LP.getApfloat().multiply(P.getApfloat()));

                if (progressIndicator != null)
                {
                    progressIndicator.progress(n1, n2);
                }
            }
        }

        private static boolean split(long n1, long n2, int numberOfProcessors)
        {
            long termsPerThread = (n2 - n1) / numberOfProcessors;

            if (DEBUG) Pi.err.println("PiParallel.r(" + n1 + ", " + n2 + ") terms per thread " + termsPerThread);

            ApfloatContext ctx = ApfloatContext.getContext();
            long threshold = ctx.getSharedMemoryTreshold() / 32;    // Heuristically chosen value

            return termsPerThread < threshold;
        }

        private static final long serialVersionUID = 1L;
    }

    /**
     * Class for calculating pi using the parallel Chudnovskys' binary splitting algorithm.
     */

    public static class ParallelChudnovskyPiCalculator
        extends ChudnovskyPiCalculator
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public ParallelChudnovskyPiCalculator(long precision, int radix)
            throws ApfloatRuntimeException
        {
            this(new ParallelBinarySplittingPiCalculator(new ChudnovskyBinarySplittingSeries(precision, radix)), precision, radix);
        }

        /**
         * Construct a pi calculator with the specified binary splitting algorithm.
         *
         * @param calculator The binary splitting algorithm to be used.
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        protected ParallelChudnovskyPiCalculator(BinarySplittingPiCalculator calculator, long precision, int radix)
            throws ApfloatRuntimeException
        {
            super(calculator, precision, radix);
        }

        @Override
        public Apfloat execute()
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            int numberOfProcessors = ctx.getNumberOfProcessors();

            if (numberOfProcessors > 1)
            {
                Pi.err.println("Using up to " + numberOfProcessors + " parallel operations for calculation");
            }

            return super.execute();
        }

        private static final long serialVersionUID = 1L;
    }

    /**
     * Class for calculating pi using the parallel Ramanujan's binary splitting algorithm.
     */

    public static class ParallelRamanujanPiCalculator
        extends RamanujanPiCalculator
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public ParallelRamanujanPiCalculator(long precision, int radix)
            throws ApfloatRuntimeException
        {
            this(new ParallelBinarySplittingPiCalculator(new RamanujanBinarySplittingSeries(precision, radix)), precision, radix);
        }

        /**
         * Construct a pi calculator with the specified binary splitting algorithm.
         *
         * @param calculator The binary splitting algorithm to be used.
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        protected ParallelRamanujanPiCalculator(BinarySplittingPiCalculator calculator, long precision, int radix)
            throws ApfloatRuntimeException
        {
            super(calculator, precision, radix);
        }

        @Override
        public Apfloat execute()
        {
            ApfloatContext ctx = ApfloatContext.getContext();
            int numberOfProcessors = ctx.getNumberOfProcessors();

            if (numberOfProcessors > 1)
            {
                Pi.err.println("Using up to " + numberOfProcessors + " parallel operations for calculation");
            }

            return super.execute();
        }

        private static final long serialVersionUID = 1L;
    }

    /**
     * Class to execute operations while setting {@link ApfloatContext#setNumberOfProcessors(int)}
     * to some value.
     */

    protected static class ThreadLimitedOperation<T>
        implements Operation<T>
    {
        /**
         * Wrap an existing operation to a thread limited context.
         *
         * @param operation The operation whose execution will have a limited number of threads available.
         * @param numberOfProcessors The maximum number of threads that can be used in the execution.
         */

        public ThreadLimitedOperation(Operation<T> operation, int numberOfProcessors)
        {
            this.operation = operation;
            this.numberOfProcessors = numberOfProcessors;
        }

        /**
         * Execute the operation.
         *
         * @return Result of the operation.
         */

        @Override
        public T execute()
        {
            ApfloatContext threadCtx = ApfloatContext.getThreadContext();
            ApfloatContext ctx = (ApfloatContext) ApfloatContext.getContext().clone();
            ctx.setNumberOfProcessors(this.numberOfProcessors);
            ApfloatContext.setThreadContext(ctx);

            T result = this.operation.execute();

            if (threadCtx != null)
            {
                ApfloatContext.setThreadContext(threadCtx);
            }
            else
            {
               ApfloatContext.removeThreadContext();
            }

            return result;
        }

        private static final long serialVersionUID = 1L;

        private Operation<T> operation;
        private int numberOfProcessors;
    }

    PiParallel()
    {
    }

    /**
     * Command-line entry point.
     *
     * @param args Command-line parameters.
     *
     * @exception IOException In case writing the output fails.
     */

    public static void main(String[] args)
        throws IOException, ApfloatRuntimeException
    {
        if (args.length < 1)
        {
            System.err.println("USAGE: PiParallel digits [method] [threads] [radix]");
            System.err.println("    radix must be 2...36");

            return;
        }

        long precision = getPrecision(args[0]);
        int method = (args.length > 1 ? getInt(args[1], "method", 0, 1) : 0),
            numberOfProcessors = (args.length > 2 ? getInt(args[2], "threads", 1, Integer.MAX_VALUE) : ApfloatContext.getContext().getNumberOfProcessors()),
            radix = (args.length > 3 ? getRadix(args[3]) : ApfloatContext.getContext().getDefaultRadix());

        // Also set the executor service to use the corresponding number of threads
        ApfloatContext ctx = ApfloatContext.getContext();
        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

        Operation<Apfloat> operation;

        switch (method)
        {
            case 0:
                operation = new ParallelChudnovskyPiCalculator(precision, radix);
                break;
            default:
                operation = new ParallelRamanujanPiCalculator(precision, radix);
        }

        setOut(new PrintWriter(System.out, true));
        setErr(new PrintWriter(System.err, true));

        run(precision, radix, operation);
    }

    private static final boolean DEBUG = false;
}
