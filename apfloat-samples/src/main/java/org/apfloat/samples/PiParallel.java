package org.apfloat.samples;

import java.io.PrintWriter;
import java.io.IOException;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
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
 * @version 1.8.1
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

        public void r(final long n1, final long n2, final ApfloatHolder T, final ApfloatHolder Q, final ApfloatHolder P, final BinarySplittingProgressIndicator progressIndicator)
            throws ApfloatRuntimeException
        {
            checkAlive();

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

                final ApfloatHolder LT = new ApfloatHolder(),
                                    LQ = new ApfloatHolder(),
                                    LP = new ApfloatHolder();

                if (split(n1, n2, numberOfProcessors))
                {
                    // Split work in ratio of number of threads and execute in parallel

                    int numberOfProcessors1 = numberOfProcessors / 2,
                        numberOfProcessors2 = numberOfProcessors - numberOfProcessors1;

                    final long nMiddle = n1 + (n2 - n1) * numberOfProcessors1 / numberOfProcessors;

                    if (DEBUG) Pi.err.println("PiParallel.r(" + n1 + ", " + n2 + ") splitting " + numberOfProcessors + " threads to r(" + n1 + ", " + nMiddle + ") " + numberOfProcessors1 + " threads, r(" + nMiddle + ", " + n2 + ") " + numberOfProcessors2 + " threads");

                    // Call recursively this r() method to further split the term calculation
                    Operation<Object> operation1 = new Operation<Object>()
                    {
                        public Object execute()
                        {
                            r(n1, nMiddle, LT, LQ, LP, progressIndicator);
                            return null;
                        }
                    };
                    Operation<Object> operation2 = new Operation<Object>()
                    {
                        public Object execute()
                        {
                            r(nMiddle, n2, T, Q, P, progressIndicator);
                            return null;
                        }
                    };

                    BackgroundOperation<?> operation = new BackgroundOperation<Object>(new ThreadLimitedOperation<Object>(operation1, numberOfProcessors1));
                    new ThreadLimitedOperation<Object>(operation2, numberOfProcessors2).execute();
                    operation.getResult();                          // Waits for operation to complete
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

        public T execute()
        {
            checkAlive();

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
