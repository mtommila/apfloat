package org.apfloat.samples;

import java.io.Serializable;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatMath;
import org.apfloat.ApfloatRuntimeException;

/**
 * Calculates pi using four different algorithms.
 *
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class Pi
{
    // Implementation note: we use printf() for printing, because it flushes the output (print() does not)

    /**
     * Terms for the binary splitting series.
     */

    protected static interface BinarySplittingSeries
        extends Serializable
    {
        /**
         * Binary splitting term.
         *
         * @param n The term.
         *
         * @return The value.
         */

        public Apfloat a(long n)
            throws ApfloatRuntimeException;

        /**
         * Binary splitting term.
         *
         * @param n The term.
         *
         * @return The value.
         */

        public Apfloat p(long n)
            throws ApfloatRuntimeException;

        /**
         * Binary splitting term.
         *
         * @param n The term.
         *
         * @return The value.
         */

        public Apfloat q(long n)
            throws ApfloatRuntimeException;
    }

    /**
     * Abstract base class for the binary splitting series.
     */

    protected static abstract class AbstractBinarySplittingSeries
        implements BinarySplittingSeries
    {
        /**
         * Construct a binary splitting series with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        protected AbstractBinarySplittingSeries(long precision, int radix)
        {
            this.precision = precision;
            this.radix = radix;
        }

        /**
         * Target precision.
         */

        protected long precision;

        /**
         * Radix to be used.
         */

        protected int radix;
    }

    /**
     * Chudnovskys' algorithm terms for the binary splitting series.
     */

    protected static class ChudnovskyBinarySplittingSeries
        extends AbstractBinarySplittingSeries
    {
        /**
         * Basic constructor.
         *
         * @param precision The precision.
         * @param radix The radix.
         */

        public ChudnovskyBinarySplittingSeries(long precision, int radix)
        {
            super(precision, radix);
            this.A = new Apfloat(13591409, precision, radix);
            this.B = new Apfloat(545140134, precision, radix);
            this.J = new Apfloat(10939058860032000L, precision, radix);
            this.ONE = new Apfloat(1, precision, radix);
            this.TWO = new Apfloat(2, precision, radix);
            this.FIVE = new Apfloat(5, precision, radix);
            this.SIX = new Apfloat(6, precision, radix);
        }

        public Apfloat a(long n)
            throws ApfloatRuntimeException
        {
            Apfloat s = new Apfloat(n, Apfloat.INFINITE, this.radix),
                    v = this.A.add(this.B.multiply(s));

            v = ((n & 1) == 0 ? v : v.negate());

            return v;
        }

        public Apfloat p(long n)
            throws ApfloatRuntimeException
        {
            Apfloat v;

            if (n == 0)
            {
                v = this.ONE;
            }
            else
            {
                Apfloat f = new Apfloat(n, Apfloat.INFINITE, this.radix),
                        sixf = this.SIX.multiply(f);

                v = sixf.subtract(this.ONE).multiply(this.TWO.multiply(f).subtract(this.ONE)).multiply(sixf.subtract(this.FIVE));
            }

            return v;
        }

        public Apfloat q(long n)
            throws ApfloatRuntimeException
        {
            Apfloat v;

            if (n == 0)
            {
                v = this.ONE;
            }
            else
            {
                Apfloat f = new Apfloat(n, Apfloat.INFINITE, this.radix);

                v = this.J.multiply(f.multiply(f).multiply(f));
            }

            return v;
        }

        private final Apfloat A;
        private final Apfloat B;
        private final Apfloat J;
        private final Apfloat ONE;
        private final Apfloat TWO;
        private final Apfloat FIVE;
        private final Apfloat SIX;
    }

    /**
     * Ramanujan's algorithm terms for the binary splitting series.
     */

    protected static class RamanujanBinarySplittingSeries
        extends AbstractBinarySplittingSeries
    {
        /**
         * Basic constructor.
         *
         * @param precision The precision.
         * @param radix The radix.
         */

        public RamanujanBinarySplittingSeries(long precision, int radix)
        {
            super(precision, radix);
            this.A = new Apfloat(1103, precision, radix);
            this.B = new Apfloat(26390, precision, radix);
            this.J = new Apfloat(3073907232L, precision, radix);
            this.ONE = new Apfloat(1, precision, radix);
            this.TWO = new Apfloat(2, precision, radix);
            this.THREE = new Apfloat(3, precision, radix);
            this.FOUR = new Apfloat(4, precision, radix);
        }

        public Apfloat a(long n)
            throws ApfloatRuntimeException
        {
            Apfloat s = new Apfloat(n, Apfloat.INFINITE, this.radix),
                    v = this.A.add(this.B.multiply(s));

            return v;
        }

        public Apfloat p(long n)
            throws ApfloatRuntimeException
        {
            Apfloat v;

            if (n == 0)
            {
                v = this.ONE;
            }
            else
            {
                Apfloat f = new Apfloat(n, Apfloat.INFINITE, this.radix),
                        fourf = this.FOUR.multiply(f);

                v = fourf.subtract(this.ONE).multiply(this.TWO.multiply(f).subtract(this.ONE)).multiply(fourf.subtract(this.THREE));
            }

            return v;
        }

        public Apfloat q(long n)
            throws ApfloatRuntimeException
        {
            Apfloat v;

            if (n == 0)
            {
                v = this.ONE;
            }
            else
            {
                Apfloat f = new Apfloat(n, Apfloat.INFINITE, this.radix);

                v = this.J.multiply(f.multiply(f).multiply(f));
            }

            return v;
        }

        private final Apfloat A;
        private final Apfloat B;
        private final Apfloat J;
        private final Apfloat ONE;
        private final Apfloat TWO;
        private final Apfloat THREE;
        private final Apfloat FOUR;
    }

    /**
     * Class for implementing the binary splitting algorithm.
     */

    protected static class BinarySplittingPiCalculator
        implements Serializable
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param series The binary splitting series to be used.
         */

        public BinarySplittingPiCalculator(BinarySplittingSeries series)
        {
            this.series = series;
        }

        /**
         * Entry point for the binary splitting algorithm.
         *
         * @param n1 Start term.
         * @param n2 End term.
         * @param T Algorithm parameter.
         * @param Q Algorithm parameter.
         * @param P Algorithm parameter.
         * @param progressIndicator Class to print out the progress of the calculation.
         */

        public void r(long n1, long n2, ApfloatHolder T, ApfloatHolder Q, ApfloatHolder P, BinarySplittingProgressIndicator progressIndicator)
            throws ApfloatRuntimeException
        {
            checkAlive();

            assert (n1 != n2);
            long length = n2 - n1;

            if (length == 1)
            {
                Apfloat p0 = p(n1);

                T.setApfloat(a(n1).multiply(p0));
                Q.setApfloat(q(n1));
                if (P != null) P.setApfloat(p0);
            }
            else
            {
                long nMiddle = n1 + n2 >> 1;
                ApfloatHolder LT = new ApfloatHolder(),
                              LQ = new ApfloatHolder(),
                              LP = new ApfloatHolder();

                r(n1, nMiddle, LT, LQ, LP, progressIndicator);
                r(nMiddle, n2, T, Q, P, progressIndicator);

                T.setApfloat(Q.getApfloat().multiply(LT.getApfloat()).add(LP.getApfloat().multiply(T.getApfloat())));
                Q.setApfloat(LQ.getApfloat().multiply(Q.getApfloat()));
                if (P != null) P.setApfloat(LP.getApfloat().multiply(P.getApfloat()));
            }

            if (progressIndicator != null)
            {
                progressIndicator.progress(n1, n2);
            }
        }

        private Apfloat a(long n)
            throws ApfloatRuntimeException
        {
            return this.series.a(n);
        }

        private Apfloat p(long n)
            throws ApfloatRuntimeException
        {
            return this.series.p(n);
        }

        private Apfloat q(long n)
            throws ApfloatRuntimeException
        {
            return this.series.q(n);
        }

        private BinarySplittingSeries series;
    }

    /**
     * Basic class for calculating pi using the Chudnovskys' binary splitting algorithm.
     */

    public static class ChudnovskyPiCalculator
        implements Operation<Apfloat>
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public ChudnovskyPiCalculator(long precision, int radix)
            throws ApfloatRuntimeException
        {
            this(new BinarySplittingPiCalculator(new ChudnovskyBinarySplittingSeries(precision, radix)), precision, radix);
        }

        /**
         * Construct a pi calculator with the specified binary splitting algorithm.
         *
         * @param calculator The binary splitting algorithm to be used.
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        protected ChudnovskyPiCalculator(BinarySplittingPiCalculator calculator, long precision, int radix)
            throws ApfloatRuntimeException
        {
            this.calculator = calculator;
            this.precision = precision;
            this.radix = radix;
        }

        /**
         * Calculate pi using the Chudnovskys' binary splitting algorithm.
         */

        public Apfloat execute()
        {
            Pi.err.println("Using the Chudnovsky brothers' binary splitting algorithm");

            ApfloatHolder T = new ApfloatHolder(),
                          Q = new ApfloatHolder();

            // Perform the calculation of T, Q and P to requested precision only, to improve performance

            long terms = (long) ((double) this.precision * Math.log((double) this.radix) / 32.654450041768516);

            long time = System.currentTimeMillis();
            this.calculator.r(0, terms + 1, T, Q, null, new BinarySplittingProgressIndicator(terms));
            time = System.currentTimeMillis() - time;

            Pi.err.println("100% complete, elapsed time " + time / 1000.0 + " seconds");
            Pi.err.printf("Final value ");

            time = System.currentTimeMillis();
            Apfloat t = T.getApfloat(),
                    q = Q.getApfloat();
            checkAlive();
            Apfloat factor = ApfloatMath.inverseRoot(new Apfloat(1823176476672000L, this.precision, this.radix), 2);
            checkAlive();
            Apfloat pi = ApfloatMath.inverseRoot(factor.multiply(t), 1).multiply(q);
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            return pi;
        }

        private BinarySplittingPiCalculator calculator;
        private long precision;
        private int radix;
    }

    /**
     * Basic class for calculating pi using the Ramanujan binary splitting algorithm.
     */

    public static class RamanujanPiCalculator
        implements Operation<Apfloat>
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public RamanujanPiCalculator(long precision, int radix)
            throws ApfloatRuntimeException
        {
            this(new BinarySplittingPiCalculator(new RamanujanBinarySplittingSeries(precision, radix)), precision, radix);
        }

        /**
         * Construct a pi calculator with the specified binary splitting algorithm.
         *
         * @param calculator The binary splitting algorithm to be used.
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        protected RamanujanPiCalculator(BinarySplittingPiCalculator calculator, long precision, int radix)
            throws ApfloatRuntimeException
        {
            this.calculator = calculator;
            this.precision = precision;
            this.radix = radix;
        }

        /**
         * Calculate pi using the Ramanujan binary splitting algorithm.
         */

        public Apfloat execute()
        {
            Pi.err.println("Using the Ramanujan binary splitting algorithm");

            ApfloatHolder T = new ApfloatHolder(),
                          Q = new ApfloatHolder();

            // Perform the calculation of T, Q and P to requested precision only, to improve performance

            long terms = (long) ((double) this.precision * Math.log((double) this.radix) / 18.38047940053836);

            long time = System.currentTimeMillis();
            this.calculator.r(0, terms + 1, T, Q, null, new BinarySplittingProgressIndicator(terms));
            time = System.currentTimeMillis() - time;

            Pi.err.println("100% complete, elapsed time " + time / 1000.0 + " seconds");
            Pi.err.printf("Final value ");

            time = System.currentTimeMillis();
            Apfloat t = T.getApfloat(),
                    q = Q.getApfloat();
            checkAlive();
            Apfloat factor = ApfloatMath.inverseRoot(new Apfloat(8, this.precision, this.radix), 2);
            checkAlive();
            Apfloat pi = ApfloatMath.inverseRoot(t, 1).multiply(factor).multiply(new Apfloat(9801, Apfloat.INFINITE, this.radix)).multiply(q);
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            return pi;
        }

        private BinarySplittingPiCalculator calculator;
        private long precision;
        private int radix;
    }

    /**
     * Indicates progress of the pi calculation using
     * the binary splitting algorithm.<p>
     *
     * This implementation is thread safe for multiple
     * threads to use concurrently.
     */

    public static class BinarySplittingProgressIndicator
        implements Serializable
    {
        /**
         * Construct a progress indicator with the specified
         * number of terms of the series.
         *
         * @param terms Total number of terms to be calculated.
         */

        public BinarySplittingProgressIndicator(long terms)
        {
            this.totalElements = (long) (terms * (Math.log((double) terms) / Math.log(2.0) + 1.0)) + 1;
            this.currentElements = new AtomicLong();    // Use atomic long for thread safe but non-blocking access
        }

        /**
         * Advances the progress.
         *
         * @param n1 First term that has been calculated.
         * @param n2 Last term that has been calculated, minus one.
         */

        public void progress(long n1, long n2)
        {
            long length = n2 - n1;
            // For small ranges the amount of progress is advanced in one chunk, including the progress of all the recursive steps
            long addedElements;
            if (length < PROGRESS_RECURSION_THRESHOLD >> 1)
            {
                // Sub-range of a small range, was progressed already
                return;
            }
            else if (length < PROGRESS_RECURSION_THRESHOLD)
            {
                // Small range, calculate the recursive progress
                addedElements = recursiveLength(length);
            }
            else
            {
                // Large range, calculate just this step
                addedElements = length;
            }

            long oldElements = this.currentElements.getAndAdd(addedElements);
            long elements = oldElements + addedElements;

            int oldPercentComplete = (int) (100 * oldElements / this.totalElements);
            int percentComplete = (int) (100 * elements / this.totalElements);

            if (percentComplete != oldPercentComplete)
            {
                Pi.err.printf(percentComplete + "%% complete\r");
            }
        }

        private long recursiveLength(long length)
        {
            long recursiveLength;
            if (length == 1)
            {
                recursiveLength = 1;
            }
            else if (length == PROGRESS_RECURSION_THRESHOLD - 1)
            {
                // Borderline case, half of this will be calculated separately
                recursiveLength = recursiveLength(length >> 1) + length;
            }
            else
            {
                long halfLength = length >> 1;
                recursiveLength = recursiveLength(halfLength) + recursiveLength(length - halfLength) + length;
            }
            return recursiveLength;
        }

        private static final long PROGRESS_RECURSION_THRESHOLD = 32;

        private long totalElements;
        private AtomicLong currentElements;
    }

    /**
     * Calculates pi using the Gauss-Legendre algorithm.
     */

    public static class GaussLegendrePiCalculator
        implements Operation<Apfloat>
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public GaussLegendrePiCalculator(long precision, int radix)
        {
            this.precision = precision;
            this.radix = radix;
        }

        /**
         * Calculate pi using the Gauss-Legendre iteration.
         */

        public Apfloat execute()
        {
            Pi.err.println("Using the Gauss-Legendre iteration");

            int iterations = 0;

            while (gaussLegendrePrecision(iterations, 4, this.radix) < this.precision)
            {
                iterations++;
            }

            Pi.err.println("Total " + iterations + " iterations");

            Pi.err.printf("Initial values ");

            long time = System.currentTimeMillis();
            Apfloat two = new Apfloat(2, this.precision, this.radix),
                    four = new Apfloat(4, this.precision, this.radix),
                    a = new Apfloat(1, this.precision, this.radix),
                    b = ApfloatMath.inverseRoot(two, 2),
                    t = a.divide(four);
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            for (int i = 0; i < iterations; i++)
            {
                checkAlive();

                Pi.err.printf("Iteration " + (i + 1) + " ");

                time = System.currentTimeMillis();

                Apfloat tmp = a;
                a = a.add(b).divide(two);
                checkAlive();
                b = tmp.multiply(b);
                checkAlive();
                b = ApfloatMath.sqrt(b);

                checkAlive();
                t = t.subtract(new Apfloat(1L << i, this.precision, this.radix).multiply(ApfloatMath.pow(tmp.subtract(a), 2)));

                time = System.currentTimeMillis() - time;

                Pi.err.println("took " + time / 1000.0 + " seconds");
            }

            checkAlive();

            Pi.err.printf("Final value ");

            time = System.currentTimeMillis();
            a = a.add(b);
            t = four.multiply(t);
            Apfloat pi = ApfloatMath.pow(a, 2).divide(t);
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            return pi;
        }

        // What precision is achieved with k Gauss-Legendre iterations
        private static long gaussLegendrePrecision(int k, int r, int radix)
        {
            return (long) ((Math.pow(2.0, (double) k) * Math.sqrt((double) r) * Math.PI - Math.log(16.0 * Math.sqrt((double) r)) - k * Math.log(2.0)) / Math.log((double) radix));
        }

        private long precision;
        private int radix;
    }

    /**
     * Calculates pi using the Borweins' quartic algorithm.
     */

    public static class BorweinPiCalculator
        implements Operation<Apfloat>
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public BorweinPiCalculator(long precision, int radix)
        {
            this.precision = precision;
            this.radix = radix;
        }

        /**
         * Calculate pi using the Borweins' quartic iteration.
         */

        public Apfloat execute()
        {
            Pi.err.println("Using the Borweins' quartic iteration");

            int iterations = 0;

            while (borweinPrecision(iterations, 4, this.radix) < this.precision)
            {
                iterations++;
            }

            Pi.err.println("Total " + iterations + " iterations");

            Pi.err.printf("Initial values ");

            long time = System.currentTimeMillis();
            Apfloat one = new Apfloat(1, this.precision, this.radix),
                    two = new Apfloat(2, this.precision, this.radix),
                    four = new Apfloat(4, this.precision, this.radix),
                    y = ApfloatMath.sqrt(two).subtract(one),
                    a = two.subtract(four.multiply(y));
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            for (int i = 0; i < iterations; i++)
            {
                checkAlive();

                Pi.err.printf("Iteration " + (i + 1) + " ");

                time = System.currentTimeMillis();

                Apfloat tmp = ApfloatMath.pow(y, 4);
                y = one.subtract(tmp);
                checkAlive();
                y = ApfloatMath.inverseRoot(y, 4);
                checkAlive();
                y = y.subtract(one).divide(y.add(one));

                checkAlive();
                tmp = ApfloatMath.pow(y.add(one), 2);
                checkAlive();
                a = a.multiply(tmp).multiply(tmp);

                checkAlive();
                a = a.subtract(new Apfloat(1L << (2 * i + 3), this.precision, this.radix).multiply(y).multiply(tmp.subtract(y)));

                time = System.currentTimeMillis() - time;

                Pi.err.println("took " + time / 1000.0 + " seconds");
            }

            checkAlive();

            Pi.err.printf("Final value ");

            time = System.currentTimeMillis();
            Apfloat pi = one.divide(a);
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            return pi;
        }

        // What precision is achieved with k Borweins' quartic iterations
        private static long borweinPrecision(int k, int r, int radix)
        {
            return (long) ((Math.pow(4.0, (double) k) * Math.sqrt((double) r) * Math.PI - Math.log(16.0 * Math.sqrt((double) r)) - k * Math.log(4.0)) / Math.log((double) radix));
        }

        private long precision;
        private int radix;
    }

    /**
     * Parse a long from an argument.
     *
     * @param arg The string to be parsed.
     * @param name Description of the argument.
     * @param minValue Minimum allowed value.
     * @param maxValue Maximum allowed value.
     *
     * @return Valid <code>long</code>.
     */

    protected static long getLong(String arg, String name, long minValue, long maxValue)
    {
        long value = 0;

        try
        {
            value = Long.parseLong(arg);
            if (value < minValue || value > maxValue)
            {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("Invalid " + name + ": " + arg);

            System.exit(1);
        }

        return value;
    }

    /**
     * Parse an integer from an argument.
     *
     * @param arg The string to be parsed.
     * @param name Description of the argument.
     * @param minValue Minimum allowed value.
     * @param maxValue Maximum allowed value.
     *
     * @return Valid integer.
     */

    protected static int getInt(String arg, String name, int minValue, int maxValue)
    {
        return (int) getLong(arg, name, minValue, maxValue);
    }

    /**
     * Parse the precision from an argument.
     *
     * @param arg The string to be parsed.
     *
     * @return Valid precision.
     */

    protected static long getPrecision(String arg)
    {
        return getLong(arg, "digits", 1, Apfloat.INFINITE - 1);
    }

    /**
     * Parse the radix from an argument.
     *
     * @param arg The string to be parsed.
     *
     * @return Valid radix.
     */

    protected static int getRadix(String arg)
    {
        return getInt(arg, "radix", Character.MIN_RADIX, Character.MAX_RADIX);
    }

    private static void dump()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        Pi.err.println("builderFactory = " + ctx.getBuilderFactory().getClass().getName());
        Pi.err.println("maxMemoryBlockSize = " + ctx.getMaxMemoryBlockSize());
        Pi.err.println("cacheL1Size = " + ctx.getCacheL1Size());
        Pi.err.println("cacheL2Size = " + ctx.getCacheL2Size());
        Pi.err.println("cacheBurst = " + ctx.getCacheBurst());
        Pi.err.println("memoryThreshold = " + ctx.getMemoryThreshold());
        Pi.err.println("sharedMemoryTreshold = " + ctx.getSharedMemoryTreshold());
        Pi.err.println("blockSize = " + ctx.getBlockSize());
        Pi.err.println("numberOfProcessors = " + ctx.getNumberOfProcessors());
    }

    /**
     * Execute an operation and display some additional information.
     * The return value of the operation is written to {@link #out}.
     *
     * @param precision The precision to be used.
     * @param radix The radix to be used.
     * @param operation The operation to execute.
     *
     * @exception IOException In case writing the output fails.
     */

    public static void run(long precision, int radix, Operation<Apfloat> operation)
        throws IOException, ApfloatRuntimeException
    {
        dump();
        Pi.err.println("Calculating pi to " + precision + " radix-" + radix + " digits");

        long time = System.currentTimeMillis();
        Apfloat pi = operation.execute();
        time = System.currentTimeMillis() - time;

        pi.writeTo(Pi.out, true);
        Pi.out.println();

        Pi.err.println("Total elapsed time " + time / 1000.0 + " seconds");
    }


    /**
     * Set the output stream for the result printout.
     *
     * @param out The output stream.
     */

    public static void setOut(PrintWriter out)
    {
        Pi.out = out;
    }

    /**
     * Get the output stream for the result printout.
     *
     * @return The output stream.
     */

    public static PrintWriter getOut()
    {
        return Pi.out;
    }

    /**
     * Set the output stream for status messages printout.
     *
     * @param err The output stream.
     */

    public static void setErr(PrintWriter err)
    {
        Pi.err = err;
    }

    /**
     * Get the output stream for status messages printout.
     *
     * @return The output stream.
     */

    public static PrintWriter getErr()
    {
        return Pi.err;
    }

    /**
     * Set whether the program should stop executing.
     *
     * @param isAlive <code>true</code> to keep running the program, <code>false</code> to stop.
     */

    public static void setAlive(boolean isAlive)
    {
        Pi.isAlive = isAlive;
    }

    Pi()
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
            System.err.println("USAGE: Pi digits [method] [radix]");
            System.err.println("    method: 0 = Chudnovskys' binsplit");
            System.err.println("            1 = Ramanujan binsplit");
            System.err.println("            2 = Gauss-Legendre");
            System.err.println("            3 = Borweins' quartic");
            System.err.println("    radix must be 2...36");

            return;
        }

        long precision = getPrecision(args[0]);
        int method = (args.length > 1 ? getInt(args[1], "method", 0, 3) : 0),
            radix = (args.length > 2 ? getRadix(args[2]) : ApfloatContext.getContext().getDefaultRadix());

        Operation<Apfloat> operation;

        switch (method)
        {
            case 0:
                operation = new ChudnovskyPiCalculator(precision, radix);
                break;
            case 1:
                operation = new RamanujanPiCalculator(precision, radix);
                break;
            case 2:
                operation = new GaussLegendrePiCalculator(precision, radix);
                break;
            default:
                operation = new BorweinPiCalculator(precision, radix);
        }

        setOut(new PrintWriter(System.out, true));
        setErr(new PrintWriter(System.err, true));

        run(precision, radix, operation);
    }

    /**
     * Check whether the program should stop executing.
     *
     * @exception ThreadDeath in case {@link #setAlive(boolean)} has been set to <code>false</code>.
     */

    protected static void checkAlive()
    {
        if (!Pi.isAlive)
        {
            throw new ThreadDeath();
        }
    }

    /**
     * Output stream for the result printout.
     */

    protected static PrintWriter out;

    /**
     * Output stream for status messages printout.
     */

    protected static PrintWriter err;

    // Interactive execution stop check
    private static volatile boolean isAlive = true;
}
