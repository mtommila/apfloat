/*
 * MIT License
 *
 * Copyright (c) 2002-2025 Mikko Tommila
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

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.PriorityQueue;

import org.apfloat.spi.Util;

/**
 * Various mathematical functions for arbitrary precision complex numbers.
 *
 * @see ApfloatMath
 *
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class ApcomplexMath
{
    private ApcomplexMath()
    {
    }

    /**
     * Negative value.
     *
     * @deprecated Use {@link Apcomplex#negate()}.
     *
     * @param z The argument.
     *
     * @return <code>-z</code>.
     */

    @Deprecated
    public static Apcomplex negate(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return z.negate();
    }

    /**
     * Absolute value.
     *
     * @param z The argument.
     *
     * @return <code>sqrt(x<sup>2</sup> + y<sup>2</sup>)</code>, where <code>z = x + <i>i</i> y</code>.
     */

    public static Apfloat abs(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.real().signum() == 0)
        {
             return ApfloatMath.abs(z.imag());
        }
        else if (z.imag().signum() == 0)
        {
             return ApfloatMath.abs(z.real());
        }
        else
        {
             return ApfloatMath.sqrt(norm(z));
        }
    }

    /**
     * Norm. Square of the magnitude.
     *
     * @param z The argument.
     *
     * @return <code>x<sup>2</sup> + y<sup>2</sup></code>, where <code>z = x + <i>i</i> y</code>.
     */

    public static Apfloat norm(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return ApfloatMath.multiplyAdd(z.real(), z.real(), z.imag(), z.imag());
    }

    /**
     * Angle of the complex vector in the complex plane.
     *
     * @param z The argument.
     *
     * @return <code>arctan(y / x)</code> from the appropriate branch, where <code>z = x + <i>i</i> y</code>.
     *
     * @exception ArithmeticException If <code>z</code> is zero.
     */

    public static Apfloat arg(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return ApfloatMath.atan2(z.imag(), z.real());
    }

    /**
     * Multiply by a power of the radix.
     *
     * @param z The argument.
     * @param scale The scaling factor.
     *
     * @return <code>z * z.radix()<sup>scale</sup></code>.
     */

    public static Apcomplex scale(Apcomplex z, long scale)
        throws ApfloatRuntimeException
    {
        return new Apcomplex(ApfloatMath.scale(z.real(), scale),
                             ApfloatMath.scale(z.imag(), scale));
    }

    /**
     * Integer power.
     *
     * @param z Base of the power operator.
     * @param n Exponent of the power operator.
     *
     * @return <code>z</code> to the <code>n</code>:th power, that is <code>z<sup>n</sup></code>.
     *
     * @exception ArithmeticException If both <code>z</code> and <code>n</code> are zero.
     */

    public static Apcomplex pow(Apcomplex z, long n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (n == 0)
        {
            if (z.isZero())
            {
                throw new ApfloatArithmeticException("Zero to power zero", "pow.zeroToZero");
            }

            return new Apcomplex(new Apfloat(1, Apfloat.INFINITE, z.radix()));
        }
        else if (n < 0)
        {
            z = Apcomplex.ONES[z.radix()].divide(z);
            n = -n;
        }

        return powAbs(z, n);
    }

    // Absolute value of n used
    private static Apcomplex powAbs(Apcomplex z, long n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        long precision = z.precision();
        z = ApfloatHelper.extendPrecision(z);   // Big exponents will accumulate round-off errors

        // Algorithm improvements by Bernd Kellner
        int b2pow = 0;

        while ((n & 1) == 0)
        {
            b2pow++;
            n >>>= 1;
        }

        Apcomplex r = z;

        while ((n >>>= 1) > 0)
        {
            z = z.multiply(z);
            if ((n & 1) != 0)
            {
                r = r.multiply(z);
            }
        }

        while (b2pow-- > 0)
        {
            r = r.multiply(r);
        }

        return ApfloatHelper.setPrecision(r, precision);
    }

    /**
     * Square root.
     *
     * @param z The argument.
     *
     * @return Square root of <code>z</code>.
     */

    public static Apcomplex sqrt(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return root(z, 2);
    }

    /**
     * Cube root.
     *
     * @param z The argument.
     *
     * @return Cube root of <code>z</code>.
     */

    public static Apcomplex cbrt(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return root(z, 3);
    }

    /**
     * Positive integer root. The branch that has the smallest angle
     * and same sign of imaginary part as <code>z</code> is always chosen.
     *
     * @param z The argument.
     * @param n Which root to take.
     *
     * @return <code>n</code>:th root of <code>z</code>, that is <code>z<sup>1/n</sup></code>.
     *
     * @exception ArithmeticException If <code>n</code> is zero.
     */

    public static Apcomplex root(Apcomplex z, long n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return root(z, n, 0);
    }

    /**
     * Positive integer root. The specified branch counting from the smallest angle
     * and same sign of imaginary part as <code>z</code> is chosen.
     *
     * @param z The argument.
     * @param n Which root to take.
     * @param k Which branch to take.
     *
     * @return <code>n</code>:th root of <code>z</code>, that is <code>z<sup>1/n</sup>e<sup>i2&pi;sk/n</sup></code> where <code>s</code> is the signum of the imaginary part of <code>z</code>.
     *
     * @exception ArithmeticException If <code>n</code> is zero.
     *
     * @since 1.5
     */

    public static Apcomplex root(Apcomplex z, long n, long k)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (n == 0)
        {
            throw new ApfloatArithmeticException("Zeroth root", "root.zeroth");
        }
        else if (z.isZero())
        {
            if (n < 0)
            {
                throw new ApfloatArithmeticException("Inverse root of zero", "inverseRoot.ofZero");
            }
            return Apcomplex.ZEROS[z.radix()];  // Avoid division by zero
        }
        else if (n == 1)
        {
            return z;
        }
        k %= n;
        if (z.imag().signum() == 0 && z.real().signum() > 0 && k == 0)
        {
            return new Apcomplex(ApfloatMath.root(z.real(), n));
        }
        else if (n < 0)                         // Also correctly handles 0x8000000000000000L
        {
            return inverseRootAbs(z, -n, k);
        }
        else if (n == 2)
        {
            return z.multiply(inverseRootAbs(z, 2, k));
        }
        else if (n == 3)
        {
            // Choose the correct branch
            if (z.real().signum() < 0)
            {
                k = (z.imag().signum() == 0 ? 1 - k : k - 1);
                k %= n;
            }
            else
            {
                k = -k;
            }
            Apcomplex w = z.multiply(z);
            return z.multiply(inverseRootAbs(w, 3, k));
        }
        else
        {
            return inverseRootAbs(inverseRootAbs(z, n, k), 1, 0);
        }
    }

    /**
     * Inverse positive integer root. The branch that has the smallest angle
     * and different sign of imaginary part than <code>z</code> is always chosen.
     *
     * @param z The argument.
     * @param n Which inverse root to take.
     *
     * @return Inverse <code>n</code>:th root of <code>z</code>, that is <code>z<sup>-1/n</sup></code>.
     *
     * @exception ArithmeticException If <code>z</code> or <code>n</code> is zero.
     */

    public static Apcomplex inverseRoot(Apcomplex z, long n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return inverseRoot(z, n, 0);
    }

    /**
     * Inverse positive integer root. The specified branch counting from the smallest angle
     * and different sign of imaginary part than <code>z</code> is chosen.
     *
     * @param z The argument.
     * @param n Which inverse root to take.
     * @param k Which branch to take.
     *
     * @return Inverse <code>n</code>:th root of <code>z</code>, that is <code>z<sup>-1/n</sup>e<sup>-i2&pi;k/n</sup></code>.
     *
     * @exception ArithmeticException If <code>z</code> or <code>n</code> is zero.
     */

    public static Apcomplex inverseRoot(Apcomplex z, long n, long k)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isZero())
        {
            throw new ApfloatArithmeticException("Inverse root of zero", "inverseRoot.ofZero");
        }
        else if (n == 0)
        {
            throw new ApfloatArithmeticException("Inverse zeroth root", "inverseRoot.zeroth");
        }
        k %= n;
        if (z.imag().signum() == 0 && z.real().signum() > 0 && k == 0)
        {
            return new Apcomplex(ApfloatMath.inverseRoot(z.real(), n));
        }
        else if (n < 0)
        {
            return inverseRootAbs(inverseRootAbs(z, -n, k), 1, 0);      // Also correctly handles 0x8000000000000000L
        }

        return inverseRootAbs(z, n, k);
    }

    // Absolute value of n used
    private static Apcomplex inverseRootAbs(Apcomplex z, long n, long k)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.equals(Apcomplex.ONE) && k == 0)
        {
            // Trivial case
            return z;
        }
        else if (n == 2 && z.imag().signum() == 0 && z.real().signum() < 0)
        {
            // Avoid round-off errors and produce a pure imaginary result
            Apfloat y = ApfloatMath.inverseRoot(z.real().negate(), n);
            return new Apcomplex(Apfloat.ZEROS[z.radix()], k == 0 ? y.negate() : y);
        }

        long targetPrecision = z.precision();

        if (targetPrecision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate inverse root to infinite precision", "inverseRoot.infinitePrecision");
        }

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                divisor = ApfloatMath.abs(new Apfloat(n, Apfloat.INFINITE, z.radix()));

        double doubleReal,
               doubleImag,
               magnitude,
               angle,
               doubleN = Math.abs((double) n);

        long realScale = z.real().scale(),
             imagScale = z.imag().scale(),
             scale = Math.max(realScale, imagScale),
             scaleDiff = scale - Math.min(realScale, imagScale),
             doublePrecision = ApfloatHelper.getDoublePrecision(z.radix()),
             precision = doublePrecision,       // Accuracy of initial guess
             scaleQuot = scale / n,             // If n is 0x8000000000000000 then this will be zero
             scaleRem = scale - scaleQuot * n;
        double scaleRemFactor = Math.pow((double) z.radix(), (double) -scaleRem / doubleN);

        Apcomplex result;

        // Calculate initial guess from z
        if (z.imag().signum() == 0 ||
            (scaleDiff > doublePrecision / 2 || scaleDiff < 0) && realScale > imagScale)        // Detect overflow
        {
            // z.real() is a lot bigger in magnitude than z.imag()
            Apfloat tmpReal = z.real().precision(doublePrecision),
                    tmpImag = z.imag().precision(doublePrecision);
            Apcomplex tweak = new Apcomplex(Apfloat.ZERO,
                                            tmpImag.divide(divisor.multiply(tmpReal)));

            tmpReal = ApfloatMath.scale(tmpReal, -tmpReal.scale());     // Allow exponents in excess of doubles'

            if ((magnitude = tmpReal.doubleValue()) >= 0.0)
            {
                doubleReal = Math.pow(magnitude, -1.0 / doubleN) * scaleRemFactor;
                doubleImag = 0.0;
            }
            else
            {
                magnitude = Math.pow(-magnitude, -1.0 / doubleN) * scaleRemFactor;
                angle = (tmpImag.signum() >= 0 ? -Math.PI : Math.PI) / doubleN;
                doubleReal = magnitude * Math.cos(angle);
                doubleImag = magnitude * Math.sin(angle);
            }

            tmpReal = ApfloatMath.scale(new Apfloat(doubleReal, doublePrecision, z.radix()), -scaleQuot);
            tmpImag = ApfloatMath.scale(new Apfloat(doubleImag, doublePrecision, z.radix()), -scaleQuot);
            result = new Apcomplex(tmpReal, tmpImag);
            result = result.subtract(result.multiply(tweak));               // Must not be real
        }
        else if (z.real().signum() == 0 ||
                 (scaleDiff > doublePrecision / 2 || scaleDiff < 0) && imagScale > realScale)        // Detect overflow
        {
            // z.imag() is a lot bigger in magnitude than z.real()
            Apfloat tmpReal = z.real().precision(doublePrecision),
                    tmpImag = z.imag().precision(doublePrecision);
            Apcomplex tweak = new Apcomplex(Apfloat.ZERO,
                                            tmpReal.divide(divisor.multiply(tmpImag)));

            tmpImag = ApfloatMath.scale(tmpImag, -tmpImag.scale());     // Allow exponents in exess of doubles'

            if ((magnitude = tmpImag.doubleValue()) >= 0.0)
            {
                magnitude = Math.pow(magnitude, -1.0 / doubleN) * scaleRemFactor;
                angle = -Math.PI / (2.0 * doubleN);
            }
            else
            {
                magnitude = Math.pow(-magnitude, -1.0 / doubleN) * scaleRemFactor;
                angle = Math.PI / (2.0 * doubleN);
            }

            doubleReal = magnitude * Math.cos(angle);
            doubleImag = magnitude * Math.sin(angle);

            tmpReal = ApfloatMath.scale(new Apfloat(doubleReal, doublePrecision, z.radix()), -scaleQuot);
            tmpImag = ApfloatMath.scale(new Apfloat(doubleImag, doublePrecision, z.radix()), -scaleQuot);
            result = new Apcomplex(tmpReal, tmpImag);
            result = result.add(result.multiply(tweak));               // Must not be pure imaginary
        }
        else
        {
            // z.imag() and z.real() approximately the same in magnitude
            Apfloat tmpReal = z.real().precision(doublePrecision),
                    tmpImag = z.imag().precision(doublePrecision);

            tmpReal = ApfloatMath.scale(tmpReal, -scale);       // Allow exponents in exess of doubles'
            tmpImag = ApfloatMath.scale(tmpImag, -scale);       // Allow exponents in exess of doubles'

            doubleReal = tmpReal.doubleValue();
            doubleImag = tmpImag.doubleValue();

            magnitude = Math.pow(doubleReal * doubleReal + doubleImag * doubleImag, -1.0 / (2.0 * doubleN)) * scaleRemFactor;
            angle = -Math.atan2(doubleImag, doubleReal) / doubleN;

            doubleReal = magnitude * Math.cos(angle);
            doubleImag = magnitude * Math.sin(angle);

            tmpReal = ApfloatMath.scale(new Apfloat(doubleReal, doublePrecision, z.radix()), -scaleQuot);
            tmpImag = ApfloatMath.scale(new Apfloat(doubleImag, doublePrecision, z.radix()), -scaleQuot);
            result = new Apcomplex(tmpReal, tmpImag);
        }

        // Alter the angle by the branch chosen
        if (k != 0)
        {
            Apcomplex branch;
            // Handle exact cases
            k = (k < 0 ? k + n : k);
            if (n % 4 == 0 && (n >>> 2) == k)
            {
                branch = new Apcomplex(Apfloat.ZERO, one);
            }
            else if (n % 4 == 0 && (n >>> 2) * 3 == k)
            {
                branch = new Apcomplex(Apfloat.ZERO, one.negate());
            }
            else if (n % 2 == 0 && (n >>> 1) == k)
            {
                branch = one.negate();
            }
            else
            {
                angle = 2.0 * Math.PI * (double) k / doubleN;
                doubleReal = Math.cos(angle);
                doubleImag = Math.sin(angle);
                Apfloat tmpReal = new Apfloat(doubleReal, doublePrecision, z.radix());
                Apfloat tmpImag = new Apfloat(doubleImag, doublePrecision, z.radix());
                branch = new Apcomplex(tmpReal, tmpImag);
            }
            result = result.multiply(z.imag().signum() >= 0 ? branch.conj() : branch);
        }

        int iterations = 0;

        // Compute total number of iterations
        for (long maxPrec = precision; maxPrec < targetPrecision; maxPrec <<= 1)
        {
            iterations++;
        }

        int precisingIteration = iterations;

        // Check where the precising iteration should be done
        for (long minPrec = precision; precisingIteration > 0; precisingIteration--, minPrec <<= 1)
        {
            if ((minPrec - Apcomplex.EXTRA_PRECISION) << precisingIteration >= targetPrecision)
            {
                break;
            }
        }

        z = ApfloatHelper.extendPrecision(z);

        // Newton's iteration
        while (iterations-- > 0)
        {
            precision *= 2;
            result = ApfloatHelper.setPrecision(result, Math.min(precision, targetPrecision));

            Apcomplex t = powAbs(result, n);
            t = lastIterationExtendPrecision(iterations, precisingIteration, t);
            t = one.subtract(z.multiply(t));
            if (iterations < precisingIteration)
            {
                t = new Apcomplex(t.real().precision(precision / 2),
                                  t.imag().precision(precision / 2));
            }

            result = lastIterationExtendPrecision(iterations, precisingIteration, result);
            result = result.add(result.multiply(t).divide(divisor));

            // Precising iteration
            if (iterations == precisingIteration)
            {
                t = powAbs(result, n);
                t = lastIterationExtendPrecision(iterations, -1, t);

                result = lastIterationExtendPrecision(iterations, -1, result);
                result = result.add(result.multiply(one.subtract(z.multiply(t))).divide(divisor));
            }
        }

        return ApfloatHelper.setPrecision(result, targetPrecision);
    }

    /**
     * All values of the positive integer root.<p>
     *
     * Returns all of the <code>n</code> values of the root, in the order
     * of the angle, starting from the smallest angle and same sign of
     * imaginary part as <code>z</code>.
     *
     * @param z The argument.
     * @param n Which root to take.
     *
     * @return All values of the <code>n</code>:th root of <code>z</code>, that is <code>z<sup>1/n</sup></code>, in the order of the angle.
     *
     * @exception ArithmeticException If <code>n</code> is zero.
     *
     * @since 1.5
     */

    public static Apcomplex[] allRoots(Apcomplex z, int n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (n == 0)
        {
            throw new ApfloatArithmeticException("Zeroth root", "root.zeroth");
        }
        else if (n == 1)
        {
            return new Apcomplex[] { z };
        }
        else if (n == 0x80000000)
        {
            throw new ApfloatRuntimeException("Maximum array size exceeded: " + -(long) n, "maximumArraySizeExceeded", -(long) n);
        }
        else if (z.isZero())
        {
            if (n < 0)
            {
                throw new ApfloatArithmeticException("Inverse root of zero", "inverseRoot.ofZero");
            }
            Apcomplex[] allRoots = new Apcomplex[n];
            Arrays.fill(allRoots, Apcomplex.ZEROS[z.radix()]);
            return allRoots;                                    // Avoid division by zero
        }

        boolean inverse = (n < 0);
        n = Math.abs(n);

        long precision = z.precision();
        z = ApfloatHelper.extendPrecision(z);                   // Big roots will accumulate round-off errors

        Apcomplex w = inverseRootAbs(new Apfloat(1, precision, z.radix()), n, 1);
        w = (z.imag().signum() >= 0 ^ inverse ? w.conj() : w);  // Complex n:th root of unity

        Apcomplex[] allRoots = new Apcomplex[n];
        Apcomplex root = (inverse ? inverseRootAbs(z, n, 0) : root(z, n));
        allRoots[0] = ApfloatHelper.setPrecision(root, precision);
        for (int i = 1; i < n; i++)
        {
            root = root.multiply(w);
            allRoots[i] = ApfloatHelper.setPrecision(root, precision);
        }
        return allRoots;
    }

    /**
     * Arithmetic-geometric mean.
     *
     * @param a First argument.
     * @param b Second argument.
     *
     * @return Arithmetic-geometric mean of <code>a</code> and <code>b</code>.
     */

    public static Apcomplex agm(Apcomplex a, Apcomplex b)
        throws ApfloatRuntimeException
    {
        return agm(a, b, null);
    }

    static Apcomplex agm(Apcomplex a, Apcomplex b, Consumer<Apcomplex> consumer)
        throws ApfloatRuntimeException
    {
        if (a.isZero() || b.isZero())       // Would not converge quadratically
        {
            return Apcomplex.ZEROS[a.radix()];
        }

        if (a.real().signum() == b.real().signum() &&
            a.imag().signum() == 0 &&
            b.imag().signum() == 0)
        {
            return ApfloatMath.agm(a.real(), b.real(), consumer == null ? null : consumer::accept);
        }

        if (a.equals(b))                                              // Thanks to Marko Gaspersic for finding several bugs in issue #12
        {
            return a.precision(Math.min(a.precision(), b.precision()));
        }

        if (a.equals(b.negate()))                                     // Would not converge quadratically
        {
            return Apcomplex.ZEROS[a.radix()];
        }

        long workingPrecision = Math.min(a.precision(), b.precision()),
             targetPrecision = workingPrecision;

        if (workingPrecision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate agm to infinite precision", "agm.infinitePrecision");
        }

        // Some minimum precision is required for the algorithm to work
        workingPrecision = ApfloatHelper.extendPrecision(workingPrecision);
        a = ApfloatHelper.ensurePrecision(a, workingPrecision);
        b = ApfloatHelper.ensurePrecision(b, workingPrecision);

        long precision = 0,
             halfWorkingPrecision = (workingPrecision + 1) / 2;
        final long CONVERGING = 1000;           // Arbitrarily chosen value...
        Apfloat two = new Apfloat(2, Apfloat.INFINITE, a.radix());
        Apcomplex c2 = null;

        if (consumer != null)
        {
            c2 = ApfloatHelper.ensurePrecision(a.multiply(a).subtract(b.multiply(b)), workingPrecision);
            consumer.accept(c2);
        }

        // First check convergence
        while (precision < CONVERGING && precision < halfWorkingPrecision)
        {
            Apcomplex t = limitPrecision(a.add(b)).divide(two);
            b = rightSqrt(a.multiply(b), t);
            a = t;

            // Conserve precision in case of accumulating round-off errors
            a = ApfloatHelper.ensurePrecision(a, workingPrecision);
            b = ApfloatHelper.ensurePrecision(b, workingPrecision);

            precision = a.equalDigits(b);

            c2 = agmConsume(consumer, a, c2, workingPrecision);
        }

        // Now we know quadratic convergence
        while (precision <= halfWorkingPrecision)
        {
            Apcomplex t = a.add(b).divide(two);
            b = rightSqrt(a.multiply(b), t);
            a = t;

            // Conserve precision in case of accumulating round-off errors
            a = ApfloatHelper.ensurePrecision(a, workingPrecision);
            b = ApfloatHelper.ensurePrecision(b, workingPrecision);

            precision *= 2;

            c2 = agmConsume(consumer, a, c2, workingPrecision);
        }

        Apcomplex result = a.add(b).divide(two);
        agmConsume(consumer, result, c2, workingPrecision);
        return ApfloatHelper.setPrecision(result, targetPrecision);
    }

    private static Apcomplex limitPrecision(Apcomplex z)
    {
        return z.precision(z.precision());
    }

    private static Apcomplex rightSqrt(Apcomplex z, Apcomplex reference)
    {
        // See  D. A. Cox, "The Arithmetic-Geometric Mean of Gauss", L'Enseignement Mathematique, Vol. 30, 1984, pp. 275-330
        // or for example Tomack Gilmore's paper about it: https://homepage.univie.ac.at/tomack.gilmore/papers/Agm.pdf
        // 1. norm(a1 - b1) <= norm(a1 + b1)
        // 2. If norm(a1 - b1) = norm(a1 + b1) then imag(b1 / a1) > 0
        Apcomplex result = sqrt(z);

        // First compare norms with low precision
        int doublePrecision = ApfloatHelper.getDoublePrecision(z.radix());
        Apcomplex approxResult = result.precision(doublePrecision);
        Apcomplex approxReference = reference.precision(doublePrecision);
        int comparison = norm(approxReference.subtract(approxResult)).compareTo(norm(approxReference.add(approxResult)));
        if (comparison == 0)
        {
            // Full precision comparison as they are equal to low precision
            comparison = norm(reference.subtract(result)).compareTo(norm(reference.add(result)));
        }
        if (comparison > 0 || comparison == 0 && result.divide(reference).imag().signum() <= 0)
        {
            result = result.negate();
        }
        return result;
    }

    private static Apcomplex agmConsume(Consumer<Apcomplex> consumer, Apcomplex a, Apcomplex c2, long workingPrecision)
    {
        if (consumer != null)
        {
            Apfloat four = new Apfloat(4, Apfloat.INFINITE, a.radix());
            Apcomplex c = ApfloatHelper.ensurePrecision(c2.divide(four.multiply(a)), workingPrecision);
            c2 = ApfloatHelper.ensurePrecision(c.multiply(c), workingPrecision);
            consumer.accept(c2);
        }
        return c2;
    }

    /**
     * Natural logarithm.<p>
     *
     * The logarithm is calculated using the arithmetic-geometric mean.
     * See the Borweins' book for the formula.
     *
     * @param z The argument.
     *
     * @return Natural logarithm of <code>z</code>.
     *
     * @exception ArithmeticException If <code>z</code> is zero.
     */

    public static Apcomplex log(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            if (z.real().signum() >= 0)
            {
                return ApfloatMath.log(z.real());
            }
            return new Apcomplex(ApfloatMath.log(z.real().negate()), ApfloatMath.pi(z.precision(), z.radix()));
        }

        // Calculate the log using 1 / radix <= |z| < 1 and the log addition formula
        // because the agm converges badly for big z

        long targetPrecision = z.precision();

        if (targetPrecision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate logarithm to infinite precision", "log.infinitePrecision");
        }

        // If the absolute value of the argument is very big, the result is more accurate
        Apfloat x = abs(z);
        if (x.scale() > 1)
        {
            double logScale = Math.log((double) x.scale() - 1) / Math.log((double) x.radix());
            logScale += Math.ulp(logScale);
            targetPrecision = Util.ifFinite(targetPrecision, targetPrecision + (long) logScale);
        }

        Apfloat imagBias;

        // Scale z so that real part of z is always >= 0, that is its angle is -pi/2 <= angle(z) <= pi/2 to avoid possible instability near z.imag() = +-pi
        if (z.real().signum() < 0)
        {
            Apfloat pi = ApfloatHelper.extendPrecision(ApfloatMath.pi(targetPrecision, z.radix()), z.radix() <= 3 ? 1 : 0);     // pi may have 1 digit more than pi/2

            if (z.imag().signum() >= 0)
            {
                imagBias = pi;
            }
            else
            {
                imagBias = pi.negate();
            }

            z = z.negate();
        }
        else
        {
            // No bias
            imagBias = Apfloat.ZERO;
        }

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix());

        long originalScale = z.scale();

        z = scale(z, -originalScale);   // Set z's scale to zero

        Apfloat radixPower;
        if (originalScale == 0)
        {
            radixPower = Apfloat.ZERO;
        }
        else
        {
            Apfloat logRadix = ApfloatHelper.extendPrecision(ApfloatMath.logRadix(targetPrecision, z.radix()));
            radixPower = new Apfloat(originalScale, Apfloat.INFINITE, z.radix()).multiply(logRadix);
        }

        Apcomplex result = ApfloatHelper.extendPrecision(rawLog(z)).add(radixPower);

        // If the absolute value of the argument is close to 1, the real part of the result is less accurate
        // If the angle of the argument is close to zero, the imaginary part of the result is less accurate
        long finalRealPrecision = Math.max(targetPrecision - one.equalDigits(x), 1),
             finalImagPrecision = Math.max(targetPrecision - 1 + result.imag().scale(), 1);     // Scale of pi/2 is always 1

        return new Apcomplex(result.real().precision(finalRealPrecision),
                             result.imag().precision(finalImagPrecision).add(imagBias));
    }

    /**
     * Logarithm in arbitrary base.<p>
     *
     * @param z The argument.
     * @param w The base.
     *
     * @return Base-<code>w</code> logarithm of <code>z</code>.
     *
     * @exception ArithmeticException If <code>z</code> or <code>w</code> is zero.
     *
     * @since 1.6
     */

    public static Apcomplex log(Apcomplex z, Apcomplex w)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.real().signum() >= 0 && z.imag().signum() == 0 &&
            w.real().signum() >= 0 && w.imag().signum() == 0)
        {
            return ApfloatMath.log(z.real(), w.real());
        }

        long targetPrecision = Math.min(z.precision(), w.precision());

        if (z.real().signum() >= 0 && z.imag().signum() == 0)
        {
            Apfloat x = z.real();

            Apfloat one = new Apfloat(1, Apfloat.INFINITE, x.radix());
            targetPrecision = Util.ifFinite(targetPrecision, targetPrecision + one.equalDigits(x)); // If the log() argument is close to 1, the result is less accurate
            x = x.precision(Math.min(x.precision(), targetPrecision));

            return ApfloatMath.log(x).divide(log(w));
        }
        else if (w.real().signum() >= 0 && w.imag().signum() == 0)
        {
            Apfloat y = w.real();

            Apfloat one = new Apfloat(1, Apfloat.INFINITE, y.radix());
            targetPrecision = Util.ifFinite(targetPrecision, targetPrecision + one.equalDigits(y)); // If the log() argument is close to 1, the result is less accurate
            y = y.precision(Math.min(y.precision(), targetPrecision));

            return log(z).divide(ApfloatMath.log(y));
        }
        else
        {
            return log(z).divide(log(w));
        }
    }

    // Raw logarithm, regardless of z
    // Doesn't work for really big z, but is faster if used alone for small numbers
    private static Apcomplex rawLog(Apcomplex z)
        throws ApfloatRuntimeException
    {
        assert (!z.isZero());      // Infinity

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix());

        final int EXTRA_PRECISION = 25;

        long targetPrecision = z.precision(),
             workingPrecision = ApfloatHelper.extendPrecision(targetPrecision),
             n = targetPrecision / 2 + EXTRA_PRECISION;                 // Very rough estimate

        z = ApfloatHelper.extendPrecision(z, EXTRA_PRECISION);

        Apfloat e = one.precision(workingPrecision);
        e = ApfloatMath.scale(e, -n);
        z = scale(z, -n);

        Apfloat agme = ApfloatHelper.extendPrecision(ApfloatMath.agm(one, e));
        Apcomplex agmez = ApfloatHelper.extendPrecision(agm(one, z));

        Apfloat pi = ApfloatHelper.extendPrecision(ApfloatMath.pi(targetPrecision, z.radix()));
        Apcomplex log = pi.multiply(agmez.subtract(agme)).divide(new Apfloat(2, Apfloat.INFINITE, z.radix()).multiply(agme).multiply(agmez));

        return ApfloatHelper.setPrecision(log, targetPrecision);
    }

    /**
     * Exponent function.
     * Calculated using Newton's iteration for the inverse of logarithm.
     *
     * @param z The argument.
     *
     * @return <code>e<sup>z</sup></code>.
     */

    public static Apcomplex exp(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.exp(z.real());
        }

        int radix = z.radix();
        Apfloat one = new Apfloat(1, Apfloat.INFINITE, radix);

        long doublePrecision = ApfloatHelper.getDoublePrecision(radix);

        if (z.real().compareTo(new Apfloat(Long.MIN_VALUE * Math.log((double) radix), doublePrecision, radix)) <= 0)
        {
            // Underflow

            return Apcomplex.ZEROS[z.radix()];
        }
        // If the real part of the argument is close to 0, the result is more accurate; if it's very big the result is less accurate
        if (z.real().precision() < z.real().scale() - 1)
        {
            throw new LossOfPrecisionException("Complete loss of accurate digits in real part", "real.lossOfPrecision");
        }
        // The imaginary part must be scaled to the range of -pi ... pi, which may limit the precision
        if (z.imag().precision() < z.imag().scale())
        {
            throw new LossOfPrecisionException("Complete loss of accurate digits in imaginary part", "imag.lossOfPrecision");
        }
        long realPrecision = Util.ifFinite(z.real().precision(), z.real().precision() + 1 - z.real().scale()),
             imagPrecision = Util.ifFinite(z.imag().precision(), 1 + z.imag().precision() - z.imag().scale()),
             targetPrecision = Math.min(realPrecision, imagPrecision);

        if (targetPrecision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate exponent to infinite precision", "exp.infinitePrecision");
        }
        else if (z.real().compareTo(new Apfloat((double) Long.MAX_VALUE * Math.log((double) radix), doublePrecision, radix)) >= 0)
        {
            throw new OverflowException("Overflow", "overflow");
        }

        boolean negateResult = false;                           // If the final result is to be negated
        Apfloat zImag;

        if (z.imag().scale() > 0)
        {
            long piPrecision = Util.ifFinite(targetPrecision, targetPrecision + z.imag().scale());
            Apfloat pi = ApfloatMath.pi(piPrecision, radix),    // This is precalculated for initial check only
                    twoPi = pi.add(pi),
                    halfPi = pi.divide(new Apfloat(2, targetPrecision, radix));

            // Scale z so that -pi < z.imag() <= pi
            zImag = ApfloatMath.fmod(z.imag(), twoPi);
            if (zImag.compareTo(pi) > 0)
            {
                zImag = zImag.subtract(twoPi);
            }
            else if (zImag.compareTo(pi.negate()) <= 0)
            {
                zImag = zImag.add(twoPi);
            }
            // More, scale z so that -pi/2 < z.imag() <= pi/2 to avoid instability near z.imag() = +-pi
            if (zImag.compareTo(halfPi) > 0)
            {
                // exp(z - i*pi) = exp(z)/exp(i*pi) = -exp(z)
                zImag = zImag.subtract(pi);
                negateResult = true;
            }
            else if (zImag.compareTo(halfPi.negate()) <= 0)
            {
                // exp(z + i*pi) = exp(z)*exp(i*pi) = -exp(z)
                zImag = zImag.add(pi);
                negateResult = true;
            }
        }
        else
        {
            // No need to scale the imaginary part since it's small, -pi/2 < z.imag() <= pi/2
            zImag = z.imag();
        }
        z = new Apcomplex(z.real(), zImag);

        Apfloat resultReal;
        Apcomplex resultImag;

        // First handle the real part

        if (z.real().signum() == 0)
        {
            resultReal = one;
        }
        else if (z.real().scale() < -doublePrecision / 2)
        {
            // Taylor series: exp(x) = 1 + x + x^2/2 + ...

            long precision = Util.ifFinite(-z.real().scale(), -2 * z.real().scale());
            resultReal = one.precision(precision).add(z.real());
        }
        else
        {
            // Approximate starting value for iteration

            // An overflow or underflow should not occur
            long scaledRealPrecision = Math.max(0, z.real().scale()) + doublePrecision;
            Apfloat logRadix = ApfloatMath.log(new Apfloat((double) radix, scaledRealPrecision, radix)),
                    scaledReal = z.real().precision(scaledRealPrecision).divide(logRadix),
                    integerPart = scaledReal.truncate(),
                    fractionalPart = scaledReal.frac();

            resultReal = new Apfloat(Math.pow((double) radix, fractionalPart.doubleValue()), doublePrecision, radix);
            resultReal = ApfloatMath.scale(resultReal, integerPart.longValue());

            if (resultReal.signum() == 0) {
                // Underflow
                return Apcomplex.ZEROS[z.radix()];
            }
        }

        // Then handle the imaginary part

        if (zImag.signum() == 0)
        {
            // Imaginary part may have been reduced to zero e.g. if it was exactly pi
            resultImag = one;
        }
        else if (zImag.scale() < -doublePrecision / 2)
        {
            // Taylor series: exp(z) = 1 + z + z^2/2 + ...

            long precision = Util.ifFinite(-zImag.scale(), -2 * zImag.scale());
            resultImag = new Apcomplex(one.precision(precision), zImag.precision(-zImag.scale()));
        }
        else
        {
            // Approximate starting value for iteration

            double doubleImag = zImag.doubleValue();
            resultImag = new Apcomplex(new Apfloat(Math.cos(doubleImag), doublePrecision, radix),
                                       new Apfloat(Math.sin(doubleImag), doublePrecision, radix));
        }

        // Starting value is (real part starting value) * (imag part starting value)
        Apcomplex result = resultReal.multiply(resultImag);

        long precision = result.precision();    // Accuracy of initial guess

        int iterations = 0;

        // Compute total number of iterations
        for (long maxPrec = precision; maxPrec < targetPrecision; maxPrec <<= 1)
        {
            iterations++;
        }

        int precisingIteration = iterations;

        // Check where the precising iteration should be done
        for (long minPrec = precision; precisingIteration > 0; precisingIteration--, minPrec <<= 1)
        {
            if ((minPrec - Apcomplex.EXTRA_PRECISION) << precisingIteration >= targetPrecision)
            {
                break;
            }
        }

        if (iterations > 0)
        {
            // Precalculate the needed values once to the required precision
            ApfloatMath.logRadix(targetPrecision, radix);
        }

        z = ApfloatHelper.extendPrecision(z);

        // Newton's iteration
        while (iterations-- > 0)
        {
            precision *= 2;
            result = ApfloatHelper.setPrecision(result, Math.min(precision, targetPrecision));

            Apcomplex t = log(result);
            t = lastIterationExtendPrecision(iterations, precisingIteration, t);
            t = z.subtract(t);

            if (iterations < precisingIteration)
            {
                t = new Apcomplex(t.real().precision(precision / 2),
                                  t.imag().precision(precision / 2));
            }

            result = lastIterationExtendPrecision(iterations, precisingIteration, result);
            result = result.add(result.multiply(t));

            // Precising iteration
            if (iterations == precisingIteration)
            {
                t = log(result);
                t = lastIterationExtendPrecision(iterations, -1, t);

                result = lastIterationExtendPrecision(iterations, -1, result);
                result = result.add(result.multiply(z.subtract(t)));
            }
        }

        return ApfloatHelper.setPrecision(negateResult ? result.negate() : result, targetPrecision);
    }

    /**
     * Arbitrary power. Calculated using <code>log()</code> and <code>exp()</code>.<p>
     *
     * @param z The base.
     * @param w The exponent.
     *
     * @return <code>z<sup>w</sup></code>.
     *
     * @exception ArithmeticException If both <code>z</code> and <code>w</code> are zero.
     */

    public static Apcomplex pow(Apcomplex z, Apcomplex w)
        throws ApfloatRuntimeException
    {
        long targetPrecision = Math.min(z.precision(), w.precision());

        Apcomplex result = ApfloatHelper.checkPow(z, w, targetPrecision);
        if (result != null)
        {
            return result;
        }

        if (z.real().signum() >= 0 && z.imag().signum() == 0)
        {
            Apfloat x = z.real();

            // Limits precision for log() but may be sub-optimal; precision could be limited more
            Apfloat one = new Apfloat(1, Apfloat.INFINITE, x.radix());
            targetPrecision = Util.ifFinite(targetPrecision, targetPrecision + one.equalDigits(x)); // If the log() argument is close to 1, the result is less accurate
            x = x.precision(Math.min(x.precision(), targetPrecision));

            return exp(w.multiply(ApfloatMath.log(x)));
        }
        else
        {
            return exp(w.multiply(log(z)));
        }
    }

    /**
     * Inverse cosine. Calculated using <code>log()</code>.
     *
     * @param z The argument.
     *
     * @return Inverse cosine of <code>z</code>.
     *
     * @exception InfiniteExpansionException If <code>z</code> is zero.
     */

    public static Apcomplex acos(Apcomplex z)
        throws ApfloatRuntimeException
    {
        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix());

        if (z.imag().signum() == 0 && ApfloatMath.abs(z.real()).compareTo(one) <= 0)
        {
            return ApfloatMath.acos(z.real());
        }

        Apcomplex i = new Apcomplex(Apfloat.ZERO, one),
                  w;

        if (z.real().signum() > 0 || z.real().signum() == 0 && z.imag().signum() > 0)
        {
            w = i.multiply(log(z.add(sqrt(z.multiply(z).subtract(one)))));
        }
        else
        {
            w = i.multiply(log(z.subtract(sqrt(z.multiply(z).subtract(one)))));
        }

        if (z.imag().signum() < 0 || z.imag().signum() == 0 && z.real().signum() > 0)
        {
            return w;
        }
        else
        {
            return w.negate();
        }
    }

    static Apcomplex acos(Apcomplex z, long precision)
    {
        if (z.isZero())
        {
            return ApfloatMath.halfPi(z.radix(), precision);
        }
        return acos(z);
    }

    /**
     * Inverse hyperbolic cosine. Calculated using <code>log()</code>.
     *
     * @param z The argument.
     *
     * @return Inverse hyperbolic cosine of <code>z</code>.
     *
     * @exception InfiniteExpansionException If <code>z</code> is zero.
     */

    public static Apcomplex acosh(Apcomplex z)
        throws ApfloatRuntimeException
    {
        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix());

        if (z.real().signum() > 0 || z.real().signum() == 0 && z.imag().signum() >= 0)
        {
            return log(z.add(sqrt(z.multiply(z).subtract(one))));
        }
        else
        {
            return log(z.subtract(sqrt(z.multiply(z).subtract(one))));
        }
    }

    static Apcomplex acosh(Apcomplex z, long precision)
    {
        if (z.isZero())
        {
            return new Apcomplex(Apfloat.ZEROS[z.radix()], ApfloatMath.halfPi(z.radix(), precision));
        }
        return acosh(z);
    }

    /**
     * Inverse sine. Calculated using <code>log()</code>.
     *
     * @param z The argument.
     *
     * @return Inverse sine of <code>z</code>.
     */

    public static Apcomplex asin(Apcomplex z)
        throws ApfloatRuntimeException
    {
        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix());

        if (z.imag().signum() == 0 && ApfloatMath.abs(z.real()).compareTo(one) <= 0)
        {
            return ApfloatMath.asin(z.real());
        }

        Apcomplex i = new Apcomplex(Apfloat.ZERO, one);

        if (z.imag().signum() > 0 || z.imag().signum() == 0 && z.real().signum() < 0)
        {
            return i.multiply(log(sqrt(one.subtract(z.multiply(z))).subtract(i.multiply(z))));
        }
        else
        {
            return i.multiply(log(i.multiply(z).add(sqrt(one.subtract(z.multiply(z)))))).negate();
        }
    }

    /**
     * Inverse hyperbolic sine. Calculated using <code>log()</code>.
     *
     * @param z The argument.
     *
     * @return Inverse hyperbolic sine of <code>z</code>.
     */

    public static Apcomplex asinh(Apcomplex z)
        throws ApfloatRuntimeException
    {
        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix());

        if (z.real().signum() > 0 || z.real().signum() == 0 && z.imag().signum() > 0)
        {
            return log(sqrt(z.multiply(z).add(one)).add(z));
        }
        else
        {
            return log(sqrt(z.multiply(z).add(one)).subtract(z)).negate();
        }
    }

    /**
     * Inverse tangent. Calculated using <code>log()</code>.
     *
     * @param z The argument.
     *
     * @return Inverse tangent of <code>z</code>.
     *
     * @exception ArithmeticException If <code>z == <i>i</i></code>.
     */

    public static Apcomplex atan(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.atan(z.real());
        }

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex i = new Apcomplex(Apfloat.ZERO, one),
                  w = log(i.add(z).divide(i.subtract(z))).multiply(i).divide(two);

        if (z.real().signum() == 0 && z.imag().signum() > 0)
        {
            return new Apcomplex(w.real().negate(), w.imag());
        }
        else
        {
            return w;
        }
    }

    /**
     * Inverse hyperbolic tangent. Calculated using <code>log()</code>.
     *
     * @param z The argument.
     *
     * @return Inverse hyperbolic tangent of <code>z</code>.
     *
     * @exception ArithmeticException If <code>z</code> is 1 or -1.
     */

    public static Apcomplex atanh(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex w = log(one.add(z).divide(one.subtract(z))).divide(two);

        if (z.real().signum() > 0 && z.imag().signum() == 0)
        {
            return w.conj();
        }
        else
        {
            return w;
        }
    }

    /**
     * Cosine. Calculated using <code>exp()</code>.
     *
     * @param z The argument.
     *
     * @return Cosine of <code>z</code>.
     */

    public static Apcomplex cos(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.cos(z.real());
        }

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex i = new Apcomplex(Apfloat.ZERO, one),
                  w = exp(i.multiply(z));

        return (w.add(one.divide(w))).divide(two);
    }

    /**
     * Hyperbolic cosine. Calculated using <code>exp()</code>.
     *
     * @param z The argument.
     *
     * @return Hyperbolic cosine of <code>z</code>.
     */

    public static Apcomplex cosh(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.cosh(z.real());
        }

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex w = exp(z);

        return (w.add(one.divide(w))).divide(two);
    }

    /**
     * Sine. Calculated using <code>exp()</code>.
     *
     * @param z The argument.
     *
     * @return Sine of <code>z</code>.
     */

    public static Apcomplex sin(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.sin(z.real());
        }

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex i = new Apcomplex(Apfloat.ZERO, one),
                  w = exp(i.multiply(z));

        return one.divide(w).subtract(w).multiply(i).divide(two);
    }

    /**
     * Hyperbolic sine. Calculated using <code>exp()</code>.
     *
     * @param z The argument.
     *
     * @return Hyperbolic sine of <code>z</code>.
     */

    public static Apcomplex sinh(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.sinh(z.real());
        }

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex w = exp(z);

        return (w.subtract(one.divide(w))).divide(two);
    }

    /**
     * Tangent. Calculated using <code>exp()</code>.
     *
     * @param z The argument.
     *
     * @return Tangent of <code>z</code>.
     *
     * @exception ArithmeticException If <code>z</code> is &pi;/2 + n &pi; where n is an integer.
     */

    public static Apcomplex tan(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return tan(z, z.imag().signum() < 0);
    }

    static Apcomplex tanFixedPrecision(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return tan(z, z.imag().signum() > 0);
    }

    static Apcomplex tan(Apcomplex z, boolean negate)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.tan(z.real());
        }

        z = (negate ? z.negate() : z);

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex i = new Apcomplex(Apfloat.ZERO, one),
                  w = exp(two.multiply(i).multiply(z));

        w = i.multiply(one.subtract(w)).divide(one.add(w));

        return (negate ? w.negate() : w);
    }

    /**
     * Hyperbolic tangent. Calculated using <code>exp()</code>.
     *
     * @param z The argument.
     *
     * @return Hyperbolic tangent of <code>z</code>.
     *
     * @exception ArithmeticException If <code>z</code> is <i>i</i> (&pi;/2 + n &pi;) where n is an integer.
     */

    public static Apcomplex tanh(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return tanh(z, z.real().signum() > 0);
    }

    static Apcomplex tanhFixedPrecision(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return tanh(z, z.real().signum() < 0);
    }

    private static Apcomplex tanh(Apcomplex z, boolean negate)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.imag().signum() == 0)
        {
            return ApfloatMath.tanh(z.real());
        }

        z = (negate ? z.negate() : z);

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex w = exp(two.multiply(z));

        w = w.subtract(one).divide(w.add(one));

        return (negate ? w.negate() : w);
    }

    static Apcomplex cot(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        boolean negate = z.imag().signum() < 0;
        z = (negate ? z.negate() : z);

        Apfloat one = new Apfloat(1, Apfloat.INFINITE, z.radix()),
                two = new Apfloat(2, Apfloat.INFINITE, z.radix());
        Apcomplex i = new Apcomplex(Apfloat.ZERO, one),
                  w = expNoLoP(two.multiply(i).multiply(z));

        w = i.multiply(two.multiply(w).divide(w.subtract(one)).subtract(one));

        return (negate ? w.negate() : w);
    }

    /**
     * Sinc.
     *
     * @param z The argument.
     *
     * @return sinc(z)
     *
     * @since 1.14.0
     */

    public static Apcomplex sinc(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return Apcomplex.ONES[z.radix()];
        }
        return sin(z).divide(z);
    }

    /**
     * Lambert W function. The W function gives the solution to the equation
     * <code>W e<sup>W</sup> = z</code>. Also known as the product logarithm.<p>
     *
     * This function gives the solution to the principal branch, W<sub>0</sub>.
     *
     * @param z The argument.
     *
     * @return <code>W<sub>0</sub>(z)</code>.
     *
     * @since 1.8.0
     */

    public static Apcomplex w(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return LambertWHelper.w(z);
    }

    /**
     * Lambert W function for the specified branch.<p>
     *
     * @param z The argument.
     * @param k The branch.
     *
     * @return <code>W<sub>k</sub>(z)</code>.
     *
     * @exception ArithmeticException If <code>z</code> is zero and <code>k</code> is not zero.
     *
     * @see #w(Apcomplex)
     *
     * @since 1.8.0
     */

    public static Apcomplex w(Apcomplex z, long k)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return LambertWHelper.w(z, k);
    }

    /**
     * Product of numbers.
     * The precision used in the multiplications is only
     * what is needed for the end result. This method may
     * perform significantly better than simply multiplying
     * the numbers sequentially.<p>
     *
     * If there are no arguments, the return value is <code>1</code>.
     *
     * @param z The argument(s).
     *
     * @return The product of the given numbers.
     *
     * @since 1.3
     */

    public static Apcomplex product(Apcomplex... z)
        throws ApfloatRuntimeException
    {
        if (z.length == 0)
        {
            return Apcomplex.ONE;
        }

        // Determine working precision
        long maxPrec = Apcomplex.INFINITE;
        for (int i = 0; i < z.length; i++)
        {
            if (z[i].real().signum() == 0 && z[i].imag().signum() == 0)
            {
                return Apcomplex.ZEROS[z[i].radix()];
            }
            maxPrec = Math.min(maxPrec, z[i].precision());
        }

        // Do not use z.clone() as the array might be of some subclass type, resulting in ArrayStoreException later
        Apcomplex[] tmp = new Apcomplex[z.length];

        // Add sqrt length digits for round-off errors
        long extraPrec = (long) Math.sqrt((double) z.length),
             destPrec = ApfloatHelper.extendPrecision(maxPrec, extraPrec);
        for (int i = 0; i < z.length; i++)
        {
            tmp[i] = z[i].precision(destPrec);
        }
        z = tmp;

        // Create a heap, ordered by size
        Queue<Apcomplex> heap = new PriorityQueue<>(z.length, Comparator.comparing(Apcomplex::size));

        // Perform the multiplications in parallel
        ParallelHelper.ProductKernel<Apcomplex> kernel = (h) ->
        {
            Apcomplex a = h.remove();
            Apcomplex b = h.remove();
            Apcomplex c = a.multiply(b);
            h.add(c);
        };
        ParallelHelper.parallelProduct(z, heap, kernel);

        return ApfloatHelper.setPrecision(heap.remove(), maxPrec);
    }

    /**
     * Sum of numbers.
     * The precision used in the additions is only
     * what is needed for the end result. This method may
     * perform significantly better than simply adding
     * the numbers sequentially.<p>
     *
     * If there are no arguments, the return value is <code>0</code>.
     *
     * @param z The argument(s).
     *
     * @return The sum of the given numbers.
     *
     * @since 1.3
     */

    public static Apcomplex sum(Apcomplex... z)
        throws ApfloatRuntimeException
    {
        if (z.length == 0)
        {
            return Apcomplex.ZERO;
        }

        Apfloat[] x = new Apfloat[z.length],
                  y = new Apfloat[z.length];
        for (int i = 0; i < z.length; i++)
        {
            x[i] = z[i].real();
            y[i] = z[i].imag();
        }
        return new Apcomplex(ApfloatMath.sum(x), ApfloatMath.sum(y));
    }

    /**
     * Gamma function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the gamma function.
     *
     * @param z The argument.
     *
     * @return <code>&Gamma;(z)</code>
     *
     * @throws ArithmeticException If <code>z</code> is a nonpositive integer.
     *
     * @since 1.9.0
     */

    public static Apcomplex gamma(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        // Implementation note: the ck are actually constant (wrt. to precision and radix) so we could cache them,
        // however since this is a slow algorithm, the factors ck would take up quite a lot of space, and would not
        // improve the asymptotic complexity, so it's not really worth it - this function is anyway only useful for
        // a few thousand digits of precision, no matter of what optimization we might try.
        if (z.equals(Apfloat.ONE))
        {
            return z;
        }
        long precision = z.precision();
        int radix = z.radix();
        Apint one = Apint.ONES[radix];
        if (z.scale() < -precision)
        {
            return one.divide(z);
        }
        if (z.imag().signum() == 0)
        {
            if (z.real().signum() == 0)
            {
                throw new ApfloatArithmeticException("Gamma of zero", "gamma.ofZero");
            }
            if (z.real().isInteger())
            {
                if (z.real().signum() < 0)
                {
                    throw new ApfloatArithmeticException("Gamma of negative integer", "gamma.ofNegativeInteger");
                }
                long n = ApfloatHelper.longValueExact(z.real().truncate());
                // If n is extremely large and precision is relatively low, then computing it as gamma is faster
                // If n is relatively small compared to precision, then computing it as factorial is faster
                double gammaEffort = Math.log(precision) * precision * precision;
                double factorialEffort = Math.log(precision) * precision * Math.log(n) * n / 2e6;
                if (factorialEffort < gammaEffort)
                {
                    return ApfloatMath.factorial(n - 1, precision, radix);
                }
            }
        }
        if (precision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate gamma function to infinite precision", "gamma.infinitePrecision");
        }
        if (z.real().signum() < 0)
        {
            // Reduced precision for negative near-integers
            long targetPrecision = precision;
            Apint zRounded = RoundingHelper.roundToInteger(z.real(), RoundingMode.HALF_EVEN).truncate();
            if (zRounded.signum() < 0)
            {
                long digitLoss = -z.subtract(zRounded).scale();
                if (digitLoss >= targetPrecision)
                {
                    throw new ApfloatArithmeticException("Gamma of negative integer within precision", "gamma.ofNegativeIntegerWithinPrecision");
                }
                if (digitLoss > 0)
                {
                    targetPrecision -= digitLoss;
                }
            }

            // Use reflection formula, see e.g. https://functions.wolfram.com/GammaBetaErf/Gamma/16/03/01/
            precision = ApfloatHelper.extendPrecision(precision, ApfloatHelper.getSmallExtraPrecision(radix));
            z = ApfloatHelper.ensurePrecision(z.negate(), precision);
            Apfloat pi = ApfloatMath.pi(precision, radix);
            return ApfloatHelper.limitPrecision(pi.negate().divide(z.multiply(sin(pi.multiply(z))).multiply(gamma(z))), targetPrecision);
        }
        long a1 = (long) (precision / Math.log(2 * Math.PI) * Math.log(radix));
        long workingPrecision = ApfloatHelper.extendPrecision(precision, (long) (precision * 0.5) + Apfloat.EXTRA_PRECISION); // increase intermediate precision - ck are large and alternating in sign, lots of precision loss
        z = ApfloatHelper.ensurePrecision(z, workingPrecision).subtract(one);
        Apfloat a = new Apint(a1 + 1, radix);
        Apint two = new Apint(2, radix);
        Apfloat c0 = ApfloatMath.sqrt(ApfloatMath.pi(workingPrecision, radix).multiply(two));
        Apcomplex sum = c0;
        Apfloat e = ApfloatMath.exp(one.precision(workingPrecision));
        Apfloat divisor = ApfloatMath.exp(new Apfloat(-a1, workingPrecision, radix));
        for (long k = 1; k <= a1; k++)
        {
            Apint kk = new Apint(k, radix);
            Apfloat ak = a.subtract(kk).precision(workingPrecision);
            Apfloat ck = ApfloatMath.inverseRoot(ak, 2).multiply(ApfloatMath.pow(ak, k)).divide(divisor);
            sum = sum.add(ck.divide(z.add(kk)));
            if (k < a1)
            {
                divisor = divisor.multiply(e).multiply(kk).negate();
            }
        }
        a = a.precision(workingPrecision);
        Apfloat half = new Aprational(one, two).precision(workingPrecision);
        Apcomplex result = ApcomplexMath.pow(z.add(a), z.add(half)).multiply(ApcomplexMath.exp(z.negate().subtract(a))).multiply(sum);
        double normalizedScale = result.scale() * Math.log(radix);
        if (normalizedScale > 0 && z.real().scale() > 0)
        {
            precision -= (long) (1.01 * Math.log(normalizedScale) / Math.log(radix)); // Very large results have a reduced precision
        }
        else if (normalizedScale < 0)
        {
            precision -= (long) (1.148 * Math.log(-normalizedScale) / Math.log(radix)); // Very small results also have a reduced precision
        }
        if (precision <= 0)
        {
            throw new LossOfPrecisionException("Complete loss of accurate digits", "lossOfPrecision");
        }
        return ApfloatHelper.limitPrecision(result, precision);
    }

    /**
     * Incomplete gamma function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the incomplete gamma function.
     *
     * @param a The first argument.
     * @param z The second argument.
     *
     * @return <code>&Gamma;(a, z)</code>
     *
     * @throws ArithmeticException If the real part of <code>a</code> is nonpositive and <code>z</code> is zero.
     *
     * @since 1.10.0
     */

    public static Apcomplex gamma(Apcomplex a, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return IncompleteGammaHelper.gamma(a, z);
    }

    /**
     * Generalized incomplete gamma function.<p>
     *
     * This function is defined as: <code>&Gamma;(a, z0, z1) = &Gamma;(a, z0) - &Gamma;(a, z1)</code><p>
     *
     * The lower gamma function can be calculated with: <code>&gamma;(a, z) = &Gamma;(a, 0, z)</code><p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the incomplete gamma function.
     *
     * @param a The first argument.
     * @param z0 The second argument.
     * @param z1 The third argument.
     *
     * @return <code>&Gamma;(a, z0, z1)</code>
     *
     * @throws ArithmeticException If the real part of <code>a</code> is nonpositive and either <code>z0</code> or <code>z1</code> is zero. For the lower gamma function if <code>a</code> is a nonpositive integer.
     *
     * @since 1.10.0
     */

    public static Apcomplex gamma(Apcomplex a, Apcomplex z0, Apcomplex z1)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return IncompleteGammaHelper.gamma(a, z0, z1);
    }

    /**
     * Logarithm of the gamma function. Note that this function has a different branch
     * structure than <code>log(gamma(z))</code>.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the gamma function.
     *
     * @param z The argument.
     *
     * @return <code>log&Gamma;(z)</code>
     *
     * @throws ArithmeticException If <code>z</code> is a nonpositive integer.
     *
     * @since 1.11.0
     */

    public static Apcomplex logGamma(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        // |B_2n| ~ 4 sqrt(pi n) (n / (pi e))^2n
        // Stirling's series (does not converge)
        // B_2n / (2n(2n-1)z^(2n-1))

        // The sum diverges, but depending on how large re(z) is, the terms initially get smaller and smaller,
        // until they start getting bigger and bigger (and grow to infinity)

        // By truncating the sum at the point where the terms are the smallest, we can get a good approximation

        // Thus we can calculate which is the smallest term, given any z
        // The larger re(z) is, the larger the n of the term is, and the smaller the term is
        // For higher precision we need more terms, and a larger re(z)
        // Use the recurrence formula to move re(z) to be as large as needed
        // For negative re(z) use first the reflection formula

        // To calculate how many terms of the sum we need, and how big should re(z) be:
        // Use the asymptotic formula for B_2n (which is good enough for n >= 3)
        // The term in the sum is B_2n / (2n(2n-1)z^(2n-1))
        //
        // which is approximately
        //
        //  z (E Pi z / n)^(-2n) / (2n (2n - 1))
        //
        // Take derivative with respect to n and solve when it's zero
        //
        // z = (n exp((4n - 1) / (2n - 4n^2))) / Pi
        //
        // Substitute back to what the term is at that point
        //
        // exp(-(2n - 1)^2 / (2n)) / (2 Pi (2 n - 1))
        //
        // For precision p in base b, we want that term to be equal to b^-p, solve that for n
        // (cannot be solved but approximately 2n ~ 2n-1 for large n, then it can be solved)
        //
        // n = 1/2 (1 + W(b^p / (2 Pi)))
        //
        // Use formula further above to get corresponding value for z
        //
        // W is Lambert's W function
        // W can be approximated by log(z) - log(log(z))
        // Followed possibly by iteration(s) of w = w/(1 + w) (1 + log(x/w))
        //
        // The bernoulli number factor of ~sqrt(n) has been ignored in the above calculations,
        // compensate by adding a few digits of extra precision

        long precision = z.precision();
        if (z.imag().signum() == 0)
        {
            if (z.real().signum() == 0)
            {
                throw new ApfloatArithmeticException("Log gamma of zero", "logGamma.ofZero");
            }
            if (z.real().isInteger() && z.real().signum() < 0)
            {
                throw new ApfloatArithmeticException("Log gamma of negative integer", "logGamma.ofNegativeInteger");
            }
        }
        if (precision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate log gamma function to infinite precision", "logGamma.infinitePrecision");
        }

        int radix = z.radix();
        Apint one = Apint.ONES[radix],
              two = new Apint(2, radix);
        long workingPrecision = ApfloatHelper.extendPrecision(precision);
        Apfloat pi = ApfloatMath.pi(workingPrecision, radix);

        if (z.real().signum() <= 0)
        {
            long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix);
            z = ApfloatHelper.extendPrecision(z, extraPrecision);

            // Use reflection formula
            Apcomplex result;
            if (z.scale() < -precision)
            {
                // See e.g. https://functions.wolfram.com/GammaBetaErf/LogGamma/16/01/01/
                // Note that with z so small, now sin(z) ~= z, accurate to the precision of z
                result = log(pi).subtract(log(pi.multiply(z))).subtract(logGamma(z.negate())).subtract(log(z.negate()));
            }
            else
            {
                // See: Arbitrary-precision computation of the gamma function, Fredrik Johansson, https://arxiv.org/pdf/2109.08392.pdf
                result = log(pi).subtract(logSin(z)).subtract(logGamma(one.subtract(z)));
            }
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }

        double adjust = Math.log(precision) + 1,    // Adjustment for the sqrt(n) factor in bernoulli numbers
               w = (precision + adjust) * Math.log(radix) - Math.log(2 * Math.PI);
        long n = (long) Math.ceil(0.5 * (1 + w - Math.log(w)));
        Apfloat zReal = new Apfloat(n * Math.exp((4. * n - 1) / (2 * n - 4. * n * n)) / Math.PI, precision, radix);
        Apcomplex s = Apcomplex.ZERO;
        if (z.real().compareTo(zReal) < 0)
        {
            long N = zReal.subtract(z.real()).roundAway().longValueExact();
            // Use recurrence formula
            s = s.subtract(logPochhammer(z, N));
            z = z.add(new Apfloat(N, precision, radix));
        }

        s = s.add(z.subtract(new Aprational(one, two)).multiply(log(z))).subtract(z).add(log(two.multiply(pi)).divide(two));
        Apcomplex z2 = z.multiply(z),
                  zp = z;
        Iterator<Aprational> bernoulli2 = AprationalMath.bernoullis2(n, radix);
        for (long k = 1; k <= n; k++)
        {
            long k2 = Util.multiplyExact(k,  2);
            Apcomplex term = bernoulli2.next().precision(workingPrecision).divide(new Apint(k2, radix).multiply(new Apint(k2 - 1, radix)).multiply(zp));
            if (k < n)
            {
                zp = zp.multiply(z2);
            }

            long[] matchingPrecisionsReal = ApfloatHelper.getMatchingPrecisions(s.real(), term.real());
            long[] matchingPrecisionsImag = ApfloatHelper.getMatchingPrecisions(s.imag(), term.imag());
            if (matchingPrecisionsReal[1] == 0 && matchingPrecisionsImag[1] == 0)
            {
                // The rest of the terms would be insignificantly small
                break;
            }

            s = s.add(term);
        }

        return s;
    }

    // log(sin(pi z)) with correct branch structure
    private static Apcomplex logSin(Apcomplex z)
    {
        // See https://arxiv.org/pdf/2109.08392.pdf Arbitrary-precision computation of the gamma function by Fredrik Johansson
        long precision = z.precision(),
             workingPrecision = ApfloatHelper.extendPrecision(precision);
        int radix = z.radix();
        Apint n = z.real().floor(),
              one = Apint.ONES[radix],
              two = new Apint(2, radix);
        Apfloat half = new Aprational(one, two).precision(workingPrecision),
                pi = ApfloatMath.pi(workingPrecision, radix);
        Apcomplex i = new Apcomplex(Apint.ZERO, Apint.ONES[radix]);
        Apcomplex offset = n.multiply(pi).multiply(i);
        assert (z.real().signum() <= 0);
        if (z.imag().signum() >= 0)
        {
            offset = offset.negate();
        }
        z = z.subtract(n);
        Apcomplex ls;
        if (z.imag().compareTo(one) > 0)
        {
            ls = log(half.multiply(one.subtract(expNoLoP(two.multiply(i).multiply(pi).multiply(z))))).subtract(i.multiply(pi).multiply(z.subtract(half)));
        }
        else if (z.imag().compareTo(one.negate()) < 0)
        {
            ls = log(half.multiply(one.subtract(expNoLoP(two.negate().multiply(i).multiply(pi).multiply(z))))).add(i.multiply(pi).multiply(z.subtract(half)));
        }
        else
        {
            ls = log(sin(pi.multiply(z)));
        }
        return ls.add(offset);
    }

    private static Apcomplex expNoLoP(Apcomplex z)
    {
        // Avoid loss of precision if z is too big
        if (z.real().signum() < 0)
        {
            if (z.real().scale() > 1)
            {
                if (z.real().precision() <= z.real().scale() - 1)
                {
                    z = new Apcomplex(z.real().precision(z.real().scale()), z.imag());
                }
            }
        }
        return exp(z);
    }

    private static Apcomplex logPochhammer(Apcomplex z, long n)
    {
        // Hare's algorithm
        boolean conj = (z.imag().signum() < 0);
        if (conj)
        {
            z = z.conj();
        }
        int radix = z.radix();
        Apcomplex s = z;
        long m = 0;
        for (long k = 1; k < n; k++)
        {
            Apcomplex t = s.multiply(z.add(new Apint(k, radix)));
            if (s.imag().signum() >= 0 && t.imag().signum() < 0)
            {
                 m += 2;
            }
            s = t;
        }
        if (s.real().signum() < 0)
        {
            if (s.imag().signum() >= 0)
            {
                m++;
            }
            else
            {
                m--;
            }
            s = s.negate();
        }
        Apcomplex i = new Apcomplex(Apint.ZERO, Apint.ONES[radix]);
        Apfloat pi = ApfloatMath.pi(z.precision(), radix);
        Apcomplex result = log(s).add(pi.multiply(i).multiply(new Apint(m, radix)));
        return (conj ? result.conj() : result);
    }

    /**
     * Digamma function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the digamma function.
     *
     * @param z The argument.
     *
     * @return <code>&psi;(z)</code>
     *
     * @throws ArithmeticException If <code>z</code> is a nonpositive integer.
     *
     * @since 1.11.0
     */

    public static Apcomplex digamma(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        // digamma(x) = digamma(1-x) - pi cot(pi x)
        // |B_2n| ~ 4 sqrt(pi n) (n / (pi e))^2n
        // digamma(z) ~ ln(z) - 1 / 2z - sum (j=1, infinity, B_2j / (2j z^2j)), re(z) > 0
        // digamma(z) = digamma(z + N) - sum (k=0, N-1, 1 / (z + k))

        // The sum diverges, but depending on how large re(z) is, the terms initially get smaller and smaller,
        // until they start getting bigger and bigger (and grow to infinity)

        // By truncating the sum at the point where the terms are the smallest, we can get a good approximation

        // Thus we can calculate which is the smallest term, given any z
        // The larger re(z) is, the larger the n of the term is, and the smaller the term is
        // For higher precision we need more terms, and a larger re(z)
        // Use the recurrence formula to move re(z) to be as large as needed
        // For negative re(z) use first the reflection formula

        // To calculate how many terms of the sum we need, and how big should re(z) be:
        // Use the asymptotic formula for B_2n (which is good enough for n >= 3)
        // The term in the sum is B_2n / (2n z^2n)
        //
        // which is approximately
        //
        // (n / (pi e))^2n / (2n z^2n)
        // =
        // 1/2 n^(2n-1) (e pi z)^(-2n)
        // =
        // 1/(2n) (e pi z / n)^(-2n)
        //
        // Take derivative with respect to n and solve when it's zero
        //
        // n = 1/(2 W(1/(2 pi z)))
        // so then (solve for z)
        // z = e^(-1/(2n)) n / pi
        //
        // Substitute back to what the term is at that point
        //
        // e^(1-2n) / (2n)
        //
        // For precision p in base b, we want that term to be equal to b^-p, solve that for n
        //
        // n = 1/2 W(b^p e)
        //
        // Use formula further above to get corresponding value for z
        //
        // W is Lambert's W function
        // W can be approximated by log(z) - log(log(z))
        // Followed possibly by iteration(s) of w = w/(1 + w) (1 + log(x/w))
        //
        // The bernoulli number factor of ~sqrt(n) has been ignored in the above calculations,
        // compensate by adding a few digits of extra precision

        long precision = z.precision();
        int radix = z.radix();
        Apint one = Apint.ONES[radix];

        if (z.real().signum() <= 0)
        {
            if (z.real().isInteger() && z.imag().signum() == 0)
            {
                throw new ApfloatArithmeticException("Digamma of nonpositive integer", "digamma.ofNonpositiveInteger");
            }
            if (precision == Apfloat.INFINITE)
            {
                throw new InfiniteExpansionException("Cannot calculate digamma function to infinite precision", "digamma.infinitePrecision");
            }

            long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix);
            precision = ApfloatHelper.extendPrecision(precision, extraPrecision);
            z = ApfloatHelper.ensurePrecision(z, precision);
            Apfloat pi = ApfloatMath.pi(precision, radix);
            // Use reflection formula, see e.g. https://functions.wolfram.com/GammaBetaErf/PolyGamma/16/01/01/
            Apcomplex result;
            if (z.scale() < -precision)
            {
                result = digamma(z.negate()).subtract(pi.multiply(cot(pi.multiply(z)))).subtract(one.divide(z));
            }
            else
            {
                result = digamma(one.subtract(z)).subtract(pi.multiply(cot(pi.multiply(z))));
            }
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        if (precision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate digamma function to infinite precision", "digamma.infinitePrecision");
        }

        double adjust = Math.log(precision) + 1,    // Adjustment for the sqrt(n) factor in bernoulli numbers
               w = (precision + adjust) * Math.log(radix) + 1;
        long n = (long) Math.ceil(0.5 * (w - Math.log(w)));
        Apfloat zReal = new Apfloat(Math.exp(-0.5 / n) * n / Math.PI, precision, radix);
        Apcomplex s = Apfloat.ZERO;
        if (z.real().compareTo(zReal) < 0)
        {
            long N = zReal.subtract(z.real()).roundAway().longValueExact();
            // Use recurrence formula
            for (long k = 0; k < N; k++)
            {
                s = s.subtract(one.divide(z.add(new Apint(k, radix))));
            }
            z = z.add(new Apfloat(N, precision, radix));
        }

        Apint two = new Apint(2, radix);
        s = s.add(log(z)).subtract(one.divide(two.multiply(z)));
        Apcomplex z2 = z.multiply(z),
                  zp = one;
        Iterator<Aprational> bernoulli2 = AprationalMath.bernoullis2(n, radix);
        for (long k = 1; k <= n; k++)
        {
            long k2 = Util.multiplyExact(k,  2);
            zp = zp.multiply(z2);
            Apcomplex term = bernoulli2.next().precision(precision).divide(new Apint(k2, radix).multiply(zp));

            long[] matchingPrecisionsReal = ApfloatHelper.getMatchingPrecisions(s.real(), term.real());
            long[] matchingPrecisionsImag = ApfloatHelper.getMatchingPrecisions(s.imag(), term.imag());
            if (matchingPrecisionsReal[1] == 0 && matchingPrecisionsImag[1] == 0)
            {
                // The rest of the terms would be insignificantly small
                break;
            }

            s = s.subtract(term);
        }

        return s;
    }

    /**
     * Polygamma function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the polygamma function.
     *
     * @param n The order.
     * @param z The argument.
     *
     * @return <code>&psi;<sup>(n)</sup>(z)</code>
     *
     * @throws ArithmeticException If <code>n</code> is negative or <code>z</code> is a nonpositive integer.
     *
     * @since 1.13.0
     */

    public static Apcomplex polygamma(long n, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (n < 0)
        {
            throw new ApfloatArithmeticException("Polygamma of negative order", "polygamma.ofNegativeOrder");
        }
        if (isNonPositiveInteger(z))
        {
            throw new ApfloatArithmeticException("Polygamma of nonpositive integer", "polygamma.ofNonpositiveInteger");
        }
        if (n == 0)
        {
            return digamma(z);
        }

        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apint one = Apint.ONES[radix];
        Apfloat n1 = new Apfloat(n, precision, radix).add(one);
        Apcomplex result = ApfloatMath.gamma(n1).multiply(zeta(n1, z));
        return ApfloatHelper.reducePrecision((n & 1) == 1 ? result : result.negate(), extraPrecision);
    }

    /**
     * Beta function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the beta function.
     *
     * @param a The first argument.
     * @param b The second argument.
     *
     * @return B(a, b)
     *
     * @throws ArithmeticException If <code>a</code> or <code>b</code> is a nonpositive integer but <code>a + b</code> is not. Also if both <code>a</code> and <code>b</code> are nonpositive integers.
     *
     * @since 1.13.0
     */

    public static Apcomplex beta(Apcomplex a, Apcomplex b)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex ab = a.add(b);
        boolean aNonpositiveInteger = isNonPositiveInteger(a),
                bNonpositiveInteger = isNonPositiveInteger(b),
                abNonpositiveInteger = isNonPositiveInteger(ab),
                aOrBNonpositiveInteger = aNonpositiveInteger || bNonpositiveInteger;
        if (aOrBNonpositiveInteger && !abNonpositiveInteger ||
            aNonpositiveInteger && bNonpositiveInteger)
        {
            // Infinite divided by finite, or two infinities divided by one infinity
            throw new ApfloatArithmeticException("Beta is infinite", "beta.infinite");
        }
        int radix = a.radix();
        if (!aOrBNonpositiveInteger && abNonpositiveInteger)
        {
            // Finite divided by infinity
            return Apcomplex.ZEROS[radix];
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Math.min(a.precision(), b.precision()), extraPrecision);
        if (aOrBNonpositiveInteger && abNonpositiveInteger)
        {
            // Infinity divided by infinity, needs different algorithm
            if (aNonpositiveInteger)
            {
                // Make b the nonpositive integer always
                Apcomplex tmp = b;
                b = a;
                a = tmp;
            }
            a = ApfloatHelper.ensureGammaPrecision(a, precision);
            b = ApfloatHelper.ensurePrecision(b, precision);
            return ApfloatHelper.reducePrecision(gamma(a).divide(pochhammer(b, a)), extraPrecision);
        }
        // The trivial case
        a = ApfloatHelper.ensureGammaPrecision(a, precision);
        b = ApfloatHelper.ensureGammaPrecision(b, precision);
        ab = ApfloatHelper.ensureGammaPrecision(ab, precision);
        return ApfloatHelper.reducePrecision(gamma(a).multiply(gamma(b)).divide(gamma(ab)), extraPrecision);
    }

    /**
     * Incomplete beta function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The first argument.
     * @param a The second argument.
     * @param b The third argument.
     *
     * @return B<sub>z</sub>(a, b)
     *
     * @throws ArithmeticException If <code>a</code> is a nonpositive integer or <code>z</code> is zero and <code>a</code> has nonpositive real part.
     *
     * @since 1.13.0
     */

    public static Apcomplex beta(Apcomplex z, Apcomplex a, Apcomplex b)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (isNonPositiveInteger(a))
        {
            throw new ApfloatArithmeticException("Incomplete beta with a nonpositive integer", "betaIncomplete.withNonpositiveInteger");
        }
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Util.min(z.precision(), a.precision(), b.precision()), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        a = ApfloatHelper.ensurePrecision(a, precision);
        Apfloat one = new Apfloat(1, ApfloatHelper.extendPrecision(precision, 1), radix);
        Apcomplex result = pow(z, a).divide(a).multiply(hypergeometric2F1(a, ApfloatHelper.ensurePrecision(one.subtract(b), precision), ApfloatHelper.ensurePrecision(a.add(one), precision), z));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Generalized incomplete beta function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z1 The first argument.
     * @param z2 The second argument.
     * @param a The third argument.
     * @param b The fourth argument.
     *
     * @return B<sub>(z1, z2)</sub>(a, b)
     *
     * @throws ArithmeticException If <code>a</code> is a nonpositive integer or <code>z1</code> or <code>z2</code> is zero and <code>a</code> has nonpositive real part.
     *
     * @since 1.13.0
     */

    public static Apcomplex beta(Apcomplex z1, Apcomplex z2, Apcomplex a, Apcomplex b)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z1.radix();
        if (z1.equals(z2))
        {
            return Apint.ZEROS[radix];
        }
        if (isNonPositiveInteger(a))
        {
            throw new ApfloatArithmeticException("Generalized incomplete beta with a nonpositive integer", "betaIncompleteGeneralized.withNonpositiveInteger");
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Util.min(z1.precision(), z2.precision(), a.precision(), b.precision()), extraPrecision);
        z1 = ApfloatHelper.ensurePrecision(z1, precision);
        z2 = ApfloatHelper.ensurePrecision(z2, precision);
        a = ApfloatHelper.ensurePrecision(a, precision);
        Apfloat one = new Apfloat(1, ApfloatHelper.extendPrecision(precision, 1), radix);
        Apcomplex a1 = ApfloatHelper.ensurePrecision(a.add(one), precision),
                  b1 = ApfloatHelper.ensurePrecision(one.subtract(b), precision);
        Apcomplex result = pow(z2, a).multiply(hypergeometric2F1(a, b1, a1, z2)).subtract(pow(z1, a).multiply(hypergeometric2F1(a, b1, a1, z1))).divide(a);
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Pochhammer symbol.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the pochhammer symbol.
     *
     * @param z The first argument.
     * @param n The second argument.
     *
     * @return <code>(z)<sub>n</sub></code>
     *
     * @throws ArithmeticException If <code>z + n</code> is a nonpositive integer but <code>z</code> is not.
     *
     * @since 1.13.0
     */

    public static Apcomplex pochhammer(Apcomplex z, Apcomplex n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z.radix();
        long precision = Math.min(z.precision(), n.precision());
        Apint one = Apint.ONES[radix];
        if (n.isZero())
        {
            return one.precision(precision);
        }
        long longPrecision = ApfloatHelper.getLongPrecision(radix);
        Apcomplex zn = ApfloatHelper.extendPrecision(z, longPrecision).add(ApfloatHelper.extendPrecision(n, longPrecision));    // Keep sufficient precision regardless of the scale difference of z and n
        if (isNonPositiveInteger(z))    // gamma(z) is infinite
        {
            if (isNonPositiveInteger(zn)) // gamma(z + n) is infinite, too
            {
                z = one.subtract(z).subtract(n);
                Apcomplex result = pochhammer(z, n);
                Apint two = new Apint(2, radix);
                return (n.real().truncate().mod(two).signum() == 0 ? result : result.negate());
            }
            return Apint.ZEROS[radix];
        }
        if (n.isInteger() && n.real().signum() > 0 && n.real().compareTo(new Apint(precision, radix)) <= 0)
        {
            // If n is integer and relatively small, just evaluating the multiplication is probably faster
            return pochhammer(z, n.longValueExact());
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             extendedPrecision = ApfloatHelper.extendPrecision(precision, extraPrecision);
        zn = ApfloatHelper.ensureGammaPrecision(zn, extendedPrecision);
        z = ApfloatHelper.ensureGammaPrecision(z, extendedPrecision);
        return ApfloatHelper.limitPrecision(gamma(zn).divide(gamma(z)), precision);
    }

    static Apcomplex pochhammer(Apcomplex z, long n)
    {
        z = ApfloatHelper.extendPrecision(z);
        long precision = z.precision();
        Apfloat one = Apcomplex.ONES[z.radix()].precision(precision);
        Apcomplex p = one;
        for (int k = 0; k < n; k++)
        {
            p = ApfloatHelper.ensurePrecision(p.multiply(z), precision);
            z = ApfloatHelper.ensurePrecision(z.add(one), precision);
        }
        return ApfloatHelper.reducePrecision(p);
    }

    /**
     * Binomial coefficient. Calculated using the {@link #gamma(Apcomplex)} function.
     *
     * @param n The first argument.
     * @param k The second argument.
     *
     * @return <math xmlns="http://www.w3.org/1998/Math/MathML">
     *           <mrow>
     *             <mo>(</mo>
     *               <mfrac linethickness="0">
     *                 <mi>n</mi>
     *                 <mi>k</mi>
     *               </mfrac>
     *             <mo>)</mo>
     *           </mrow>
     *         </math>
     *
     * @throws ArithmeticException If <code>n</code> is a negative integer and <code>k</code> is noninteger.
     *
     * @since 1.11.0
     */

    public static Apcomplex binomial(Apcomplex n, Apcomplex k)
        throws ArithmeticException, ApfloatRuntimeException
    {
        long precision = Math.min(n.precision(), k.precision());
        int radix = n.radix();
        Apint threshold = new Apint(precision, radix);
        if (n.isInteger() && k.isInteger() && n.real().compareTo(threshold) <= 0 && k.real().compareTo(threshold) <= 0)
        {
            // If n and k are integers and relatively small, evaluating factorials is probably faster
            return ApintMath.binomial(n.real().truncate(), k.real().truncate()).precision(precision);
        }
        Apcomplex nk = n.subtract(k);
        if (k.isInteger() && k.real().signum() < 0 ||
            nk.isInteger() && nk.real().signum() < 0)
        {
            // The divisor is infinity (but the dividend isn't) so we get zero
            return Apcomplex.ZEROS[radix];
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix);
        precision = ApfloatHelper.extendPrecision(precision, extraPrecision);
        Apint one = Apint.ONES[radix];
        Apcomplex n1 = ApfloatHelper.ensureGammaPrecision(n.add(one), precision),
                  k1 = ApfloatHelper.ensureGammaPrecision(k.add(one), precision),
                  nk1 = ApfloatHelper.ensureGammaPrecision(nk.add(one), precision);
        Apcomplex result = gamma(n1).divide(gamma(k1).multiply(gamma(nk1)));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Riemann zeta function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few hundred digits. At the time of
     * implementation no generic fast algorithm is known for the zeta function.
     *
     * @param s The argument.
     *
     * @return <code>&zeta;(s)</code>
     *
     * @throws ArithmeticException If <code>s</code> is <code>1</code>.
     *
     * @since 1.11.0
     */

    public static Apcomplex zeta(Apcomplex s)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return ZetaHelper.zeta(s);
    }

    /**
     * Hurwitz zeta function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few hundred digits. At the time of
     * implementation no generic fast algorithm is known for the zeta function.
     *
     * @param s The first argument.
     * @param a The second argument.
     *
     * @return <code>&zeta;(s, a)</code>
     *
     * @throws ArithmeticException If <code>s</code> is <code>1</code> or if <code>a</code> is a nonpositive integer.
     *
     * @since 1.11.0
     */

    public static Apcomplex zeta(Apcomplex s, Apcomplex a)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return HurwitzZetaHelper.zeta(s, a);
    }

    /**
     * Confluent hypergeometric function <i><sub>0</sub>F<sub>1</sub></i>.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param z The second argument.
     *
     * @return <i><sub>0</sub>F<sub>1</sub>(; a; z)</i>
     *
     * @throws ArithmeticException If the function value is not finite.
     *
     * @since 1.11.0
     */

    public static Apcomplex hypergeometric0F1(Apcomplex a, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return HypergeometricHelper.hypergeometricPFQ(new Apcomplex[0], new Apcomplex[] { a }, z);
    }

    /**
     * Regularized confluent hypergeometric function <i><sub>0</sub>F̃<sub>1</sub></i>.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param z The second argument.
     *
     * @return <i><sub>0</sub>F̃<sub>1</sub>(; a; z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex hypergeometric0F1Regularized(Apcomplex a, Apcomplex z)
        throws ApfloatRuntimeException
    {
        return HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[0], new Apcomplex[] { a }, z);
    }

    /**
     * Kummer confluent hypergeometric function <i><sub>1</sub>F<sub>1</sub></i>.
     * Also known as the confluent hypergeometric function of the first kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param b The second argument.
     * @param z The third argument.
     *
     * @return <i><sub>1</sub>F<sub>1</sub>(a; b; z)</i>
     *
     * @throws ArithmeticException If the function value is not finite.
     *
     * @since 1.11.0
     */

    public static Apcomplex hypergeometric1F1(Apcomplex a, Apcomplex b, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return HypergeometricHelper.hypergeometricPFQ(new Apcomplex[] { a }, new Apcomplex[] { b }, z);
    }

    /**
     * Regularized Kummer confluent hypergeometric function <i><sub>1</sub>F̃<sub>1</sub></i>.
     * Also known as the regularized confluent hypergeometric function of the first kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param b The second argument.
     * @param z The third argument.
     *
     * @return <i><sub>1</sub>F̃<sub>1</sub>(a; b; z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex hypergeometric1F1Regularized(Apcomplex a, Apcomplex b, Apcomplex z)
        throws ApfloatRuntimeException
    {
        return HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { a }, new Apcomplex[] { b }, z);
    }

    /**
     * Hypergeometric function <i><sub>2</sub>F<sub>1</sub></i>.
     * Also known as the Gaussian or ordinary hypergeometric function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param b The second argument.
     * @param c The third argument.
     * @param z The fourth argument.
     *
     * @return <i><sub>2</sub>F<sub>1</sub>(a, b; c; z)</i>
     *
     * @throws ArithmeticException If the function value is not finite.
     *
     * @since 1.11.0
     */

    public static Apcomplex hypergeometric2F1(Apcomplex a, Apcomplex b, Apcomplex c, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return HypergeometricHelper.hypergeometricPFQ(new Apcomplex[] { a, b }, new Apcomplex[] { c }, z);
    }

    /**
     * Regularized hypergeometric function <i><sub>2</sub>F̃<sub>1</sub></i>.
     * Also known as the regularized Gaussian or ordinary hypergeometric function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param b The second argument.
     * @param c The third argument.
     * @param z The fourth argument.
     *
     * @return <i><sub>2</sub>F̃<sub>1</sub>(a, b; c; z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex hypergeometric2F1Regularized(Apcomplex a, Apcomplex b, Apcomplex c, Apcomplex z)
        throws ApfloatRuntimeException
    {
        return HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { a, b }, new Apcomplex[] { c }, z);
    }

    /**
     * Tricomi's confluent hypergeometric function <i>U</i>.
     * Also known as the confluent hypergeometric function of the second kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param b The second argument.
     * @param z The third argument.
     *
     * @return <i>U(a, b, z)</i>
     *
     * @throws ArithmeticException If the result is not finite.
     *
     * @since 1.13.0
     */

    public static Apcomplex hypergeometricU(Apcomplex a, Apcomplex b, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return HypergeometricHelper.hypergeometricU(a, b, z, false);
    }

    /**
     * Error function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return <i>erf(z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex erf(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return z;
        }
        if (z.scale() > 0)
        {
            // More accurate algorithm for larger values
            int radix = z.radix();
            long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
                 precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
            z = ApfloatHelper.ensurePrecision(z, precision);
            Apint one = Apint.ONES[radix],
                  two = new Apint(2, radix);
            Apfloat sqrtPi = ApfloatMath.sqrt(ApfloatMath.pi(precision, radix)),
                    half = new Aprational(one, two).precision(precision);
            Apcomplex result = one.subtract(gamma(half, z.multiply(z)).divide(sqrtPi));
            boolean negate = (z.real().signum() == 0 ? z.imag().signum() < 0 : z.real().signum() < 0);
            return ApfloatHelper.reducePrecision(negate ? result.negate() : result, extraPrecision);
        }
        // More accurate algorithm for smaller values
        return erfFixedPrecision(z);
    }

    static Apcomplex erfFixedPrecision(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return z;
        }
        int radix = z.radix();
        long targetPrecision = z.precision(),
             extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(HypergeometricHelper.ensureHypergeometricPrecision(z.multiply(z), targetPrecision), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apint one = Apint.ONES[radix],
              two = new Apint(2, radix),
              three = new Apint(3, radix);
        Apfloat sqrtPi = ApfloatMath.sqrt(ApfloatMath.pi(precision, radix)),
                half = new Aprational(one, two).precision(precision),
                threeHalfs = new Aprational(three, two).precision(precision);
        Apcomplex result = two.multiply(z).divide(sqrtPi).multiply(hypergeometric1F1(half, threeHalfs, z.multiply(z).negate()));
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    /**
     * Complementary error function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return <i>erfc(z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex erfc(Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        Apint one = Apint.ONES[radix];
        if (z.scale() > 0 && z.real().signum() > 0)
        {
            // More accurate algorithm for larger values
            long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
                 precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
            z = ApfloatHelper.ensurePrecision(z, precision);
            Apfloat sqrtPi = ApfloatMath.sqrt(ApfloatMath.pi(precision, radix)),
                    two = new Apfloat(2, precision, radix),
                    half = one.divide(two);
            Apcomplex result = gamma(half, z.multiply(z)).divide(sqrtPi);
            return ApfloatHelper.reducePrecision(result, extraPrecision);   // Note that compared to the erf() algorithm, z.real().signum() > 0 always so no need to negate the result ever
        }
        // More accurate algorithm for smaller values
        return one.subtract(erf(z));
    }

    static Apcomplex erfcFixedPrecision(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.scale() > 0 && z.real().signum() > 0 && z.real().scale() >= z.imag().scale())
        {
            return erfc(z);
        }
        Apint one = Apint.ONES[z.radix()];
        return one.subtract(erfFixedPrecision(z));
    }

    /**
     * Imaginary error function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return <i>erfi(z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex erfi(Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        Apcomplex i = new Apcomplex(Apfloat.ZEROS[radix], Apfloat.ONES[radix]);
        return i.multiply(erf(i.multiply(z))).negate();
    }

    static Apcomplex erfiFixedPrecision(Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        Apcomplex i = new Apcomplex(Apfloat.ZEROS[radix], Apfloat.ONES[radix]);
        return i.multiply(erfFixedPrecision(i.multiply(z))).negate();
    }

    /**
     * Fresnel integral S.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return <i>S(z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex fresnelS(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return z;
        }
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat two = new Apfloat(2, precision, radix),
                pi = ApfloatMath.pi(precision, radix);
        if (z.scale() > 0)
        {
            Apint one = Apfloat.ONES[radix];
            Apfloat half = one.divide(two),
                    invSqrtPi = ApfloatMath.inverseRoot(pi, 2);
            Apcomplex i = new Apcomplex(Apfloat.ZEROS[radix], one),
                      z2 = z.multiply(z),
                      iz2 = i.multiply(z2),
                      iHalfPiZ2 = iz2.multiply(pi).divide(two),
                      result = i.multiply(z).multiply(inverseRoot(two, 2)).divide(two).multiply(fresnelTerm(one, half, invSqrtPi, iz2, iHalfPiZ2).subtract(fresnelTerm(one, half, invSqrtPi, iz2.negate(), iHalfPiZ2.negate())));
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        Apfloat three = new Apfloat(3, precision, radix),
                four = new Apfloat(4, precision, radix),
                six = new Apfloat(6, precision, radix),
                seven = new Apfloat(7, precision, radix),
                sixteen = new Apfloat(16, precision, radix);
        Apcomplex[] a = { three.divide(four) },
                    b = { three.divide(two), seven.divide(four) };
        Apcomplex result = pi.multiply(pow(z, 3)).divide(six).multiply(HypergeometricHelper.hypergeometricPFQ(a, b, pi.multiply(pi).negate().multiply(pow(z, 4)).divide(sixteen)));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Fresnel integral C.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return <i>C(z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex fresnelC(Apcomplex z)
        throws ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return z;
        }
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat two = new Apfloat(2, precision, radix),
                pi = ApfloatMath.pi(precision, radix);
        Apint one = Apfloat.ONES[radix];
        if (z.scale() > 0)
        {
            Apfloat half = one.divide(two),
                    invSqrtPi = ApfloatMath.inverseRoot(pi, 2);
            Apcomplex i = new Apcomplex(Apfloat.ZEROS[radix], one),
                      z2 = z.multiply(z),
                      iz2 = i.multiply(z2),
                      iHalfPiZ2 = iz2.multiply(pi).divide(two),
                      result = z.multiply(inverseRoot(two, 2)).divide(two).multiply(fresnelTerm(one, half, invSqrtPi, iz2, iHalfPiZ2).add(fresnelTerm(one, half, invSqrtPi, iz2.negate(), iHalfPiZ2.negate())));
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        Apfloat four = new Apfloat(4, precision, radix),
                five = new Apfloat(5, precision, radix),
                sixteen = new Apfloat(16, precision, radix);
        Apcomplex[] a = { one.divide(four) },
                    b = { one.divide(two), five.divide(four) };
        Apcomplex result = z.multiply(HypergeometricHelper.hypergeometricPFQ(a, b, pi.multiply(pi).negate().multiply(pow(z, 4)).divide(sixteen)));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    private static Apcomplex fresnelTerm(Apint one, Apfloat half, Apfloat invSqrtPi, Apcomplex iz2, Apcomplex iHalfPiZ2)
        throws ApfloatRuntimeException
    {
        return inverseRoot(iz2, 2).multiply(one.subtract(invSqrtPi.multiply(gamma(half, iHalfPiZ2))));
    }

    /**
     * Exponential integral E.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>E<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If real part of <code>ν</code> is &le; 1 and <code>z</code> is zero. 
     *
     * @since 1.13.0
     */

    public static Apcomplex expIntegralE(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = ν.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Math.min(ν.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat one = Apint.ONES[radix].precision(ApfloatHelper.extendPrecision(precision, 1));
        Apcomplex ν1 = ApfloatHelper.ensureGammaPrecision(ν.subtract(one), precision),
                  result = pow(z, ν1).multiply(gamma(ν1.negate(), z));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Exponential integral Ei.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Ei(z)
     *
     * @throws ArithmeticException If <code>z</code> is zero. 
     *
     * @since 1.13.0
     */

    public static Apcomplex expIntegralEi(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apint zero = Apint.ZEROS[radix];
        Apfloat adjust;
        if (z.imag().signum() == 0)
        {
            adjust = z.real().signum() > 0 ? ApfloatMath.pi(precision, radix).negate() : zero;
        }
        else
        {
            Apfloat pi = ApfloatMath.pi(precision, radix);
            adjust = z.imag().signum() < 0 ? pi.negate() : pi;
        }
        Apcomplex result = gamma(zero, z.negate()).negate().add(new Apcomplex(zero, adjust));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Logarithmic integral.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return li(z)
     *
     * @throws ArithmeticException If <code>z</code> is zero. 
     *
     * @since 1.13.0
     */

    public static Apcomplex logIntegral(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return z;
        }
        return expIntegralEi(log(z));
    }

    /**
     * Sine integral.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Si(z)
     *
     * @since 1.13.0
     */

    public static Apcomplex sinIntegral(Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat zero = Apint.ZEROS[radix],
                one = Apint.ONES[radix],
                two = new Apfloat(2, precision, radix);
        if (z.scale() > 0)
        {
            Apfloat adjust = ApfloatMath.pi(precision, radix);
            if (z.real().signum() > 0 || z.real().signum() == 0 && z.imag().signum() > 0)
            {
                adjust = adjust.negate();
            }
            Apcomplex i = new Apcomplex(zero, one),
                      iz = i.multiply(z),
                      result = gamma(zero, iz.negate()).subtract(gamma(zero, iz)).add(new Apcomplex(zero, adjust)).multiply(i).divide(two);
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        Apfloat three = new Apfloat(3, precision, radix),
                four = new Apfloat(4, precision, radix);
        Apcomplex[] a = { one.divide(two) },
                    b = { three.divide(two), three.divide(two) };
        Apcomplex result = z.multiply(HypergeometricHelper.hypergeometricPFQ(a, b, z.multiply(z).divide(four).negate()));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Cosine integral.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Ci(z)
     *
     * @throws ArithmeticException If <code>z</code> is zero. 
     *
     * @since 1.13.0
     */

    public static Apcomplex cosIntegral(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(z.precision(), extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat zero = Apint.ZEROS[radix],
                one = Apint.ONES[radix],
                two = new Apfloat(2, precision, radix);
        if (z.scale() > 0)
        {
            Apfloat adjust = zero;
            if (z.real().signum() < 0 || z.real().signum() == 0 && z.imag().signum() < 0)
            {
                adjust = ApfloatMath.pi(precision, radix);
                adjust = (z.imag().signum() < 0 ? adjust.negate() : adjust);
            }
            Apcomplex i = new Apcomplex(zero, one),
                      iz = i.multiply(z),
                      result = gamma(zero, iz.negate()).add(gamma(zero, iz)).divide(two).negate().add(new Apcomplex(zero, adjust));
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        Apcomplex logz = log(z);
        Apfloat three = new Apfloat(3, precision, radix),
                four = new Apfloat(4, precision, radix),
                euler = ApfloatMath.euler(precision, radix);
        Apcomplex[] a = { one, one },
                    b = { two, two, three.divide(two) };
        Apcomplex z24 = z.multiply(z).divide(four).negate(),
                  result = z24.multiply(HypergeometricHelper.hypergeometricPFQ(a, b, z24)).add(logz).add(euler);
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Hyperbolic sine integral.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Shi(z)
     *
     * @since 1.13.0
     */

    public static Apcomplex sinhIntegral(Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        Apcomplex i = new Apcomplex(Apint.ZEROS[radix], Apint.ONES[radix]);
        return i.multiply(sinIntegral(i.multiply(z))).negate();
    }

    /**
     * Hyperbolic cosine integral.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Chi(z)
     *
     * @throws ArithmeticException If <code>z</code> is zero. 
     *
     * @since 1.13.0
     */

    public static Apcomplex coshIntegral(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z.radix();
        long precision = z.precision();
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, radix);
        Apcomplex i = new Apcomplex(zero, one),
                  ci = cosIntegral(i.multiply(z));
        Apfloat adjust = ApfloatMath.pi(precision, radix).divide(two).negate();
        if (z.real().signum() < 0 && z.imag().signum() >= 0)
        {
            Apint three = new Apint(3, radix);
            adjust = adjust.multiply(three).negate();
        }
        return ci.add(new Apcomplex(zero, adjust));
    }

    /**
     * Airy function Ai.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Ai(z)
     *
     * @throws InfiniteExpansionException If <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex airyAi(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return airyAi(z, z.precision());
    }

    static Apcomplex airyAi(Apcomplex z0, long targetPrecision)
        throws ApfloatRuntimeException
    {
        int radix = z0.radix();
        Apcomplex result = airy(precision ->
        {
            Apfloat one = Apint.ONES[radix].precision(precision),
                    two = new Apfloat(2, precision, radix),
                    three = new Apfloat(3, precision, radix),
                    four = new Apfloat(4, precision, radix),
                    nine = new Apfloat(9, precision, radix),
                    twoThirds = two.divide(three),
                    invCube3 = ApfloatMath.inverseRoot(three, 3);
            Apcomplex z = ApfloatHelper.ensurePrecision(z0, precision),
                      z39 = pow(z, 3).divide(nine);
            return invCube3.multiply(invCube3).divide(gamma(twoThirds)).multiply(hypergeometric0F1(twoThirds, z39)).subtract(z.multiply(invCube3).divide(gamma(one.divide(three))).multiply(hypergeometric0F1(four.divide(three), z39)));
        }, targetPrecision, radix);
        return result;
    }

    /**
     * Derivative of the Airy function Ai.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Ai′(z)
     *
     * @throws InfiniteExpansionException If <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex airyAiPrime(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return airyAiPrime(z, z.precision());
    }

    static Apcomplex airyAiPrime(Apcomplex z0, long targetPrecision)
        throws ApfloatRuntimeException
    {
        int radix = z0.radix();
        Apcomplex result;
        result = airy(precision ->
        {
            Apfloat one = Apint.ONES[radix].precision(precision),
                    two = new Apfloat(2, precision, radix),
                    three = new Apfloat(3, precision, radix),
                    five = new Apfloat(5, precision, radix),
                    nine = new Apfloat(9, precision, radix),
                    oneThird = one.divide(three),
                    invCube3 = ApfloatMath.inverseRoot(three, 3);
            Apcomplex z = ApfloatHelper.ensurePrecision(z0, precision),
                      z39 = pow(z, 3).divide(nine);
            return z.multiply(z).divide(two).multiply(invCube3).multiply(invCube3).divide(gamma(two.divide(three))).multiply(hypergeometric0F1(five.divide(three), z39)).subtract(invCube3.divide(gamma(oneThird)).multiply(hypergeometric0F1(oneThird, z39)));
        }, targetPrecision, radix);
        return result;
    }

    /**
     * Airy function Bi.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Bi(z)
     *
     * @throws InfiniteExpansionException If <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex airyBi(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return airyBi(z, z.precision());
    }

    static Apcomplex airyBi(Apcomplex z0, long targetPrecision)
        throws ApfloatRuntimeException
    {
        int radix = z0.radix();
        Apcomplex result = airy(precision ->
        {
            Apfloat one = Apint.ONES[radix].precision(precision),
                    two = new Apfloat(2, precision, radix),
                    three = new Apfloat(3, precision, radix),
                    four = new Apfloat(4, precision, radix),
                    nine = new Apfloat(9, precision, radix),
                    twoThirds = two.divide(three),
                    invSixth3 = ApfloatMath.inverseRoot(three, 6);
            Apcomplex z = ApfloatHelper.ensurePrecision(z0, precision),
                      z39 = pow(z, 3).divide(nine);
            return invSixth3.divide(gamma(twoThirds)).multiply(hypergeometric0F1(twoThirds, z39)).add(z.divide(invSixth3.multiply(gamma(one.divide(three)))).multiply(hypergeometric0F1(four.divide(three), z39)));
        }, targetPrecision, radix);
        return result;
    }

    /**
     * Derivative of the Airy function Bi.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return Bi′(z)
     *
     * @throws InfiniteExpansionException If <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex airyBiPrime(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return airyBiPrime(z, z.precision());
    }

    static Apcomplex airyBiPrime(Apcomplex z0, long targetPrecision)
        throws ApfloatRuntimeException
    {
        int radix = z0.radix();
        Apcomplex result = airy(precision ->
        {
            Apfloat one = Apint.ONES[radix].precision(precision),
                    two = new Apfloat(2, precision, radix),
                    three = new Apfloat(3, precision, radix),
                    five = new Apfloat(5, precision, radix),
                    nine = new Apfloat(9, precision, radix),
                    oneThird = one.divide(three),
                    invSixth3 = ApfloatMath.inverseRoot(three, 6);
            Apcomplex z = ApfloatHelper.ensurePrecision(z0, precision),
                      z39 = pow(z, 3).divide(nine);
            return inverseRoot(invSixth3.multiply(gamma(oneThird)), 1).multiply(hypergeometric0F1(oneThird, z39)).add(z.multiply(z).divide(two).multiply(invSixth3).divide(gamma(two.divide(three))).multiply(hypergeometric0F1(five.divide(three), z39)));
        }, targetPrecision, radix);
        return result;
    }

    private static Apcomplex airy(Function<Long, Apcomplex> f, long targetPrecision, int radix)
    {
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             resultPrecision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision),
             precision = resultPrecision,
             precisionLoss;
        Apcomplex result;
        do
        {
            result = f.apply(precision);
            precisionLoss = (result.isZero() ? precision : resultPrecision - result.precision());   // The result shouldn't be exactly zero, it means full loss of significant digits
            precision = Util.ifFinite(precision, precision + precisionLoss);
        } while (precisionLoss > 0);
        long reducePrecision = Math.max(0, (long) Math.round(Math.log(result.scale()) / Math.log(radix)));
        targetPrecision = ApfloatHelper.reducePrecision(targetPrecision, reducePrecision);
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    /**
     * Bessel function of the first kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>J<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If the real part of <code>ν</code> is &lt; 0 and <code>ν</code> is not an integer and <code>z</code> is zero. Also if the real part of <code>ν</code> is zero but the imaginary part is not, and <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex besselJ(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return BesselHelper.besselJ(ν, z);
    }

    /**
     * Modified Bessel function of the first kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>I<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If the real part of <code>ν</code> is &lt; 0 and <code>ν</code> is not an integer and <code>z</code> is zero. Also if the real part of <code>ν</code> is zero but the imaginary part is not, and <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex besselI(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return BesselHelper.besselI(ν, z);
    }

    /**
     * Bessel function of the second kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>Y<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex besselY(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return BesselHelper.besselY(ν, z);
    }

    /**
     * Modified Bessel function of the second kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>K<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex besselK(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return BesselHelper.besselK(ν, z);
    }

    /**
     * Struve function 𝐇.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>𝐇<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is zero and real part of <code>ν</code> is <= -1.
     *
     * @since 1.15.0
     */

    public static Apcomplex struveH(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return struve(ν, z, true);
    }

    /**
     * Modified Struve function 𝐋.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>𝐋<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is zero and real part of <code>ν</code> is <= -1.
     *
     * @since 1.15.0
     */

    public static Apcomplex struveL(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return struve(ν, z, false);
    }

    private static Apcomplex struve(Apcomplex ν, Apcomplex z, boolean negate)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Math.min(ν.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat one = Apint.ONES[radix].precision(precision),
                two = new Apfloat(2, precision, radix),
                three = new Apfloat(3, precision, radix),
                oneAndHalf = three.divide(two);
        Apcomplex[] a = { one },
                    b = { oneAndHalf, ν.add(oneAndHalf) };
        Apcomplex z2 = z.divide(two),
                  z24 = pow(z2, 2);
        if (negate)
        {
            z24 = z24.negate();
        }
        Apcomplex result = pow(z2, ν.add(one)).multiply(HypergeometricHelper.hypergeometricPFQRegularized(a, b, z24));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Anger function 𝐉.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>𝐉<sub>ν</sub>(z)</i>
     *
     * @since 1.15.0
     */

    public static Apcomplex angerJ(Apcomplex ν, Apcomplex z)
        throws ApfloatRuntimeException
    {
        return angerWeber(ν, z, false);
    }

    /**
     * Weber function 𝐄.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The order.
     * @param z The argument.
     *
     * @return <i>𝐄<sub>ν</sub>(z)</i>
     *
     * @since 1.15.0
     */

    public static Apcomplex weberE(Apcomplex ν, Apcomplex z)
        throws ApfloatRuntimeException
    {
        return angerWeber(ν, z, true);
    }

    private static Apcomplex angerWeber(Apcomplex ν, Apcomplex z, boolean weber)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Math.min(ν.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat one = Apint.ONES[radix].precision(precision),
                two = new Apfloat(2, precision, radix),
                three = new Apfloat(3, precision, radix),
                oneAndHalf = three.divide(two);
        Apcomplex ν2 = ν.divide(two),
                  πν2 = ApfloatMath.pi(precision, radix).multiply(ν2),
                  z2 = z.divide(two),
                  z24 = pow(z2, 2).negate(),
                  f1 = (weber ? one : z2),
                  f2 = (weber ? z2 : one);
        Apcomplex[] a = { one },
                    b1 = { oneAndHalf.subtract(ν2), oneAndHalf.add(ν2) },
                    b2 = { one.subtract(ν2), one.add(ν2) };
        if (weber)
        {
            Apcomplex[] tmp = b1;
            b1 = b2;
            b2 = tmp;
        }
        Apcomplex t1 = f1.multiply(sin(πν2)).multiply(HypergeometricHelper.hypergeometricPFQRegularized(a, b1, z24));
        Apcomplex t2 = f2.multiply(cos(πν2)).multiply(HypergeometricHelper.hypergeometricPFQRegularized(a, b2, z24));
        Apcomplex result = (weber ? t1.subtract(t2) : t1.add(t2));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Complete elliptic integral of the first kind.<p>
     * 
     * Note that this function uses the definition:
     *   <math xmlns="http://www.w3.org/1998/Math/MathML">
     *     <mrow>
     *       <mrow>
     *         <mrow>
     *           <mi>K</mi>
     *           <mo>(</mo>
     *           <mi>z</mi>
     *           <mo>)</mo>
     *         </mrow>
     *         <mo>&#10869;</mo>
     *         <mrow>
     *           <msubsup>
     *             <mo>&#8747;</mo>
     *             <mn>0</mn>
     *             <mfrac>
     *               <mi>&#960;</mi>
     *               <mn>2</mn>
     *             </mfrac>
     *           </msubsup>
     *           <mrow>
     *             <mfrac>
     *               <mn>1</mn>
     *               <msqrt>
     *                 <mrow>
     *                   <mn>1</mn>
     *                   <mo>-</mo>
     *                   <mrow>
     *                     <mi>z</mi>
     *                     <mo>&#8290;</mo>
     *                     <mrow>
     *                       <msup>
     *                         <mi>sin</mi>
     *                         <mn>2</mn>
     *                       </msup>
     *                       <mo>(</mo>
     *                       <mi>t</mi>
     *                       <mo>)</mo>
     *                     </mrow>
     *                   </mrow>
     *                 </mrow>
     *               </msqrt>
     *             </mfrac>
     *             <mo>&#8290;</mo>
     *             <mrow>
     *               <mo>&#8518;</mo>
     *               <mi>t</mi>
     *             </mrow>
     *           </mrow>
     *         </mrow>
     *       </mrow>
     *     </mrow>
     *   </math>
     *
     * @param z The argument.
     *
     * @return <i>K(z)</i>
     *
     * @throws InfiniteExpansionException If <code>z</code> is zero.
     * @throws ArithmeticException If <code>z</code> is one.
     *
     * @since 1.13.0
     */

    public static Apcomplex ellipticK(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return ellipticK(z, z.precision());
    }

    static Apcomplex ellipticK(Apcomplex z, long precision)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return ellipticK(z, precision, null);
    }

    static Apcomplex ellipticK(Apcomplex z, long precision, Consumer<Apcomplex> consumer)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix);
        precision = ApfloatHelper.extendPrecision(precision, extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat one = Apint.ONES[radix],
                two = new Apfloat(2, precision, radix),
                pi = ApfloatMath.pi(precision, radix);
        Apcomplex result = pi.divide(two.multiply(agm(one, sqrt(ApfloatHelper.ensurePrecision(one.subtract(z), precision)), consumer)));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Complete elliptic integral of the second kind.<p>
     *
     * Note that this function uses the definition:
     *   <math xmlns="http://www.w3.org/1998/Math/MathML">
     *     <mrow>
     *       <mrow>
     *         <mrow>
     *           <mi>E</mi>
     *           <mo>(</mo>
     *           <mi>z</mi>
     *           <mo>)</mo>
     *         </mrow>
     *         <mo>&#10869;</mo>
     *         <mrow>
     *           <msubsup>
     *             <mo>&#8747;</mo>
     *             <mn>0</mn>
     *             <mfrac>
     *               <mi>&#960;</mi>
     *               <mn>2</mn>
     *             </mfrac>
     *           </msubsup>
     *           <mrow>
     *             <msqrt>
     *               <mrow>
     *                 <mn>1</mn>
     *                 <mo>-</mo>
     *                 <mrow>
     *                   <mi>z</mi>
     *                   <mo>&#8290;</mo>
     *                   <mrow>
     *                     <msup>
     *                       <mi>sin</mi>
     *                       <mn>2</mn>
     *                     </msup>
     *                     <mo>(</mo>
     *                     <mi>t</mi>
     *                     <mo>)</mo>
     *                   </mrow>
     *                 </mrow>
     *               </mrow>
     *             </msqrt>
     *             <mo>&#8290;</mo>
     *             <mrow>
     *               <mo>&#8518;</mo>
     *               <mi>t</mi>
     *             </mrow>
     *           </mrow>
     *         </mrow>
     *       </mrow>
     *     </mrow>
     *   </math>
     * 
     * @param z The argument.
     *
     * @return <i>E(z)</i>
     *
     * @throws InfiniteExpansionException If <code>z</code> is zero.
     *
     * @since 1.13.0
     */

    public static Apcomplex ellipticE(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return ellipticE(z, z.precision());
    }

    static Apcomplex ellipticE(Apcomplex z, long precision)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, radix);
        if (z.equals(one))
        {
            return z;
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix);
        precision = ApfloatHelper.extendPrecision(precision, extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apcomplex[] sum = new Apcomplex[] { zero };
        Aprational[] p2 = new Aprational[1];
        Apcomplex k = ellipticK(z, precision, c2 -> sum[0] = sum[0].add((p2[0] = (p2[0] == null ? new Aprational(one, two) : p2[0].multiply(two))).multiply(c2))),
                  result = one.subtract(sum[0]).multiply(k);
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Hermite function. For integer values of <code>ν</code> gives the Hermite polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>H<sub>ν</sub>(z)</i>
     *
     * @since 1.14.0
     */

    public static Apcomplex hermiteH(Apcomplex ν, Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        long targetPrecision = Math.min(ν.precision(), z.precision()),
             extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             resultPrecision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision),
             precision = resultPrecision,
             precisionLoss;
        Apint one = Apint.ONES[radix];
        if (ν.isZero())
        {
            return one.precision(targetPrecision);
        }
        if (z.isZero())
        {
            Apint two = new Apint(2, radix);
            if (ν.isInteger() && ν.real().signum() > 0 && ν.real().truncate().mod(two).signum() > 0)
            {
                return Apint.ZEROS[radix];
            }
            ν = ApfloatHelper.ensurePrecision(ν, precision);
            Apfloat pi = ApfloatMath.pi(precision, radix);
            Apcomplex ν12 = ApfloatHelper.ensureGammaPrecision(ApfloatHelper.ensurePrecision(one.subtract(ν), precision).divide(two), precision),
                      result = pow(two.precision(precision), ν).multiply(sqrt(pi)).divide(gamma(ν12));
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        Apcomplex result;
        do
        {
            ν = ApfloatHelper.ensurePrecision(ν, precision);
            z = ApfloatHelper.ensurePrecision(z, precision);
            Apfloat two = new Apfloat(2, precision, radix),
                    three = new Apfloat(3, precision, radix);
            Apcomplex n12 = one.subtract(ν).divide(two),
                      nn2 = ν.negate().divide(two),
                      z2 = z.multiply(z);
            result = Apint.ZEROS[radix];
            if (!isNonPositiveInteger(n12))
            {
                result = inverseRoot(gamma(ApfloatHelper.ensureGammaPrecision(n12, precision)), 1).multiply(hypergeometric1F1(ApfloatHelper.ensurePrecision(nn2, precision), one.divide(two), z2));
            }
            if (!isNonPositiveInteger(nn2))
            {
                result = result.subtract(two.multiply(z).divide(gamma(ApfloatHelper.ensureGammaPrecision(nn2, precision))).multiply(hypergeometric1F1(ApfloatHelper.ensurePrecision(n12, precision), three.divide(two), z2)));
            }
            precisionLoss = (result.isZero() ? precision : resultPrecision - result.precision());   // The result shouldn't be exactly zero, it means full loss of significant digits
            precision = Util.ifFinite(precision, precision + precisionLoss);
        } while (precisionLoss > 0);
        precision = resultPrecision;
        Apfloat two = new Apfloat(2, precision, radix),
                pi = ApfloatMath.pi(precision, radix);
        ν = ApfloatHelper.limitPrecision(ν, precision);
        result = pow(two, ν).multiply(sqrt(pi)).multiply(result);
        if (ν.imag().signum() == 0 && z.imag().signum() == 0)
        {
            result = result.real(); // With purely real input arguments the result should be purely real
        }
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Laguerre function. For integer values of <code>ν</code> gives the Laguerre polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>L<sub>ν</sub>(z)</i>
     *
     * @since 1.14.0
     */

    public static Apcomplex laguerreL(Apcomplex ν, Apcomplex z)
        throws ApfloatRuntimeException
    {
        long precision = Math.min(ν.precision(), z.precision());
        Apfloat one = Apint.ONES[ν.radix()].precision(precision);
        return hypergeometric1F1(ν.negate(), one, z);
    }

    /**
     * Generalized Laguerre function. For integer values of <code>ν</code> gives the generalized Laguerre polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param λ The second argument.
     * @param z The third argument.
     *
     * @return <i>L<sub>ν</sub><sup style='position: relative; left: -0.4em;'>λ</sup>(z)</i>
     *
     * @since 1.14.0
     */

    public static Apcomplex laguerreL(Apcomplex ν, Apcomplex λ, Apcomplex z)
        throws ApfloatRuntimeException
    {
        long precision = Util.min(ν.precision(), λ.precision(), z.precision());
        Apfloat one = Apint.ONES[ν.radix()];
        Apcomplex ν1 = ApfloatHelper.ensurePrecision(ν.add(one), precision),
                  λ1 = ApfloatHelper.ensurePrecision(λ.add(one), precision);
        return pochhammer(ν1, λ).multiply(hypergeometric1F1Regularized(ν.negate(), λ1, z));
    }

    /**
     * Legendre function. For integer values of <code>ν</code> gives the Legendre polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>P<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>ν</code> is not an integer and <code>z</code> is -1.
     *
     * @since 1.14.0
     */

    public static Apcomplex legendreP(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex zero = Apcomplex.ZEROS[ν.radix()];
        return legendreP(ν, zero, z);
    }

    /**
     * Associated Legendre function of the first kind. Gives Legendre functions of type 2.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param μ The second argument.
     * @param z The third argument.
     *
     * @return <i>P<sub>ν</sub><sup style='position: relative; left: -0.4em;'>μ</sup>(z)</i>
     *
     * @throws ArithmeticException If <code>ν</code> is not an integer and <code>z</code> is -1.
     *
     * @since 1.14.0
     */

    public static Apcomplex legendreP(Apcomplex ν, Apcomplex μ, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = ν.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Util.min(ν.precision(), μ.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        μ = ApfloatHelper.ensurePrecision(μ, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat one = Apint.ONES[radix],
                two = new Apint(2, radix);
        Apcomplex ν1 = ApfloatHelper.ensurePrecision(ν.add(one), precision),
                  μ1 = ApfloatHelper.ensurePrecision(one.subtract(μ), precision),
                  z12 = ApfloatHelper.ensurePrecision(one.subtract(z), precision).divide(two);
        Apcomplex result = hypergeometric2F1Regularized(ν.negate(), ν1, μ1, z12);
        if (!μ.isZero())
        {
            Apcomplex μ2 = μ.divide(two),
                      z1 = ApfloatHelper.ensurePrecision(one.add(z), precision),
                      z1n = ApfloatHelper.ensurePrecision(one.subtract(z), precision);
            result = result.multiply(pow(z1, μ2)).divide(pow(z1n, μ2));
        }
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Legendre function of the second kind.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>Q<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is 1 or -1.
     *
     * @since 1.14.0
     */

    public static Apcomplex legendreQ(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex zero = Apcomplex.ZEROS[ν.radix()];
        return legendreQ(ν, zero, z);
    }

    /**
     * Associated Legendre function of the second kind. Gives Legendre functions of type 2.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param μ The second argument.
     * @param z The third argument.
     *
     * @return <i>Q<sub>ν</sub><sup style='position: relative; left: -0.4em;'>μ</sup>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is 1 or -1.
     *
     * @since 1.14.0
     */

    public static Apcomplex legendreQ(Apcomplex ν, Apcomplex μ, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = ν.radix();
        long targetPrecision = Util.min(ν.precision(), μ.precision(), z.precision()),
             extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             resultPrecision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision),
             precision = resultPrecision,
             precisionLoss;
        Apint one = Apint.ONES[radix];
        boolean isOne = (z.equals(one) || z.equals(one.negate()));
        Apcomplex result;
        do
        {
            ν = ApfloatHelper.ensurePrecision(ν, precision);
            μ = ApfloatHelper.ensurePrecision(μ, precision);
            z = ApfloatHelper.ensurePrecision(z, precision);
            Apfloat two = new Apfloat(2, precision, radix),
                    three = new Apint(3, radix),
                    pi = ApfloatMath.pi(precision, radix);
            Apcomplex z2 = z.multiply(z),
                      p1 = pochhammer(ApfloatHelper.ensurePrecision(one.subtract(μ).add(ν), precision).divide(two), ApfloatHelper.ensurePrecision(one.divide(two).add(μ), precision)),
                      p2 = pochhammer(ApfloatHelper.ensurePrecision(two.subtract(μ).add(ν), precision).divide(two), ApfloatHelper.ensurePrecision(μ.subtract(one.divide(two)), precision));
            result = Apint.ZEROS[radix];
            if (!p1.isZero())
            {
                Apcomplex μν = ApfloatHelper.ensurePrecision(μ.add(ν), precision),
                          μν12 = ApfloatHelper.ensurePrecision(one.subtract(μ).subtract(ν), precision).divide(two),
                          νμ = ApfloatHelper.ensurePrecision(ν.subtract(μ), precision),
                          νμ12 = ApfloatHelper.ensurePrecision(νμ.divide(two).add(one), precision);
                result = result.add(cos(μν.divide(two).multiply(pi)).multiply(p1).multiply(z).divide(two).multiply(hypergeometric2F1Regularized(μν12, νμ12, three.divide(two), z2)));
            }
            if (!p2.isZero())
            {
                Apcomplex μν = ApfloatHelper.ensurePrecision(μ.add(ν), precision),
                          μν2 = ApfloatHelper.ensurePrecision(μ.negate().subtract(ν), precision).divide(two),
                          νμ12 = ApfloatHelper.ensurePrecision(ν.subtract(μ).add(one), precision).divide(two);
                result = result.subtract(sin(μν.divide(two).multiply(pi)).multiply(p2).divide(two).multiply(hypergeometric2F1Regularized(μν2, νμ12, one.divide(two), z2)));
            }
            precisionLoss = (!isOne && result.isZero() ? precision : resultPrecision - result.precision());   // The result shouldn't be exactly zero, it means full loss of significant digits
            precision = Util.ifFinite(precision, precision + precisionLoss);
        } while (precisionLoss > 0);
        precision = resultPrecision;
        Apfloat two = new Apfloat(2, precision, radix),
                pi = ApfloatMath.pi(precision, radix);
        μ = ApfloatHelper.limitPrecision(μ, precision);
        z = ApfloatHelper.limitPrecision(z, precision);
        Apcomplex z2 = z.multiply(z);
        result = result.multiply(pow(two, μ)).multiply(pi).multiply(pow(ApfloatHelper.ensurePrecision(one.subtract(z2), precision), μ.negate().divide(two)));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Spherical harmonic function.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param λ The first argument.
     * @param μ The second argument.
     * @param ϑ The third argument.
     * @param ϕ The fourth argument.
     *
     * @return <i>Y<sub>λ</sub><sup style='position: relative; left: -0.1em;'>μ</sup>(ϑ, &phi;)</i>
     *
     * @throws ArithmeticException If <code>ϑ</code> is &pi; plus a multiple of 2 &pi; and μ is not an integer and has a negative real part, or if <code>λ - μ</code> is a negative integer.
     *
     * @since 1.14.0
     */

    public static Apcomplex sphericalHarmonicY(Apcomplex λ, Apcomplex μ, Apcomplex ϑ, Apcomplex ϕ)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (λ.isInteger() && μ.isInteger())
        {
            // The polynomial version
            return sphericalHarmonicY(λ.real().truncate(), μ.real().truncate(), ϑ, ϕ);
        }
        int radix = λ.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Util.min(λ.precision(), μ.precision(), ϑ.precision(), ϕ.precision()), extraPrecision);
        λ = ApfloatHelper.ensurePrecision(λ, precision);
        μ = ApfloatHelper.ensurePrecision(μ, precision);
        ϑ = ApfloatHelper.ensurePrecision(ϑ, precision);
        ϕ = ApfloatHelper.ensurePrecision(ϕ, precision);
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, radix),
              four = new Apint(4, radix);
        Apcomplex i = new Apcomplex(zero, one),
                  result = two.multiply(λ).add(one);
        if (result.isZero())
        {
            return result;
        }
        Apfloat pi = ApfloatMath.pi(precision, radix);
        result = sqrt(result.divide(four.multiply(pi)));
        if (!μ.isZero())
        {
            Apcomplex λμ1 = λ.add(μ).add(one);
            if (λμ1.isInteger() && λμ1.real().signum() <= 0)
            {
                return zero;
            }
            Apcomplex λnμ1 = ApfloatHelper.ensureGammaPrecision(λ.subtract(μ).add(one), precision);
            λμ1 = ApfloatHelper.ensureGammaPrecision(λμ1, precision);
            result = result.multiply(sqrt(gamma(λnμ1))).divide(sqrt(gamma(λμ1))).multiply(exp(i.multiply(ϕ).multiply(μ)));
        }
        return ApfloatHelper.reducePrecision(result.multiply(legendreP(λ, μ, cos(ϑ))), extraPrecision);
    }

    private static Apcomplex sphericalHarmonicY(Apint n, Apint m, Apcomplex ϑ, Apcomplex ϕ)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = n.radix();
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, radix),
              four = new Apint(4, radix);
        if (n.signum() < 0)
        {
            return sphericalHarmonicY(n.negate().subtract(one), m, ϑ, ϕ);
        }
        if (n.compareTo(ApfloatMath.abs(m)) < 0)
        {
            return zero;
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Util.min(ϑ.precision(), ϕ.precision()), extraPrecision);
        ϑ = ApfloatHelper.ensurePrecision(ϑ, precision);
        ϕ = ApfloatHelper.ensurePrecision(ϕ, precision);
        Apfloat pi = ApfloatMath.pi(precision, radix);
        Apcomplex i = new Apcomplex(zero, one),
                  result = sqrt(two.multiply(n).add(one).multiply(ApfloatMath.factorial(ApfloatHelper.longValueExact(n.subtract(m)), precision, radix)).divide(four.multiply(pi).multiply(ApfloatMath.factorial(ApfloatHelper.longValueExact(n.add(m)), precision, radix)))).multiply(exp(i.multiply(m).multiply(ϕ))).multiply(legendreP(n, m, cos(ϑ)));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Chebyshev function of the first kind. For integer values of <code>ν</code> gives the Chebyshev polynomial of the first kind.<p>
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>T<sub>ν</sub>(z)</i>
     *
     * @since 1.14.0
     */

    public static Apcomplex chebyshevT(Apcomplex ν, Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = ν.radix();
        if (ν.isZero() && z.isZero())
        {
            return Apcomplex.ONES[radix];
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Math.min(ν.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apcomplex result = cos(ν.multiply(acos(z, precision)));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Chebyshev function of the second kind. For integer values of <code>ν</code> gives the Chebyshev polynomial of the second kind.<p>
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>U<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is -1 and <code>ν</code> is not an integer.
     *
     * @since 1.14.0
     */

    public static Apcomplex chebyshevU(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = ν.radix();
        Apint one = Apint.ONES[radix];
        if (ν.isZero() && z.isZero())
        {
            return one;
        }
        if (z.equals(one))
        {
            return one.add(ν);
        }
        if (z.equals(one.negate()) && ν.isInteger())
        {
            Apcomplex result = one.add(ν);
            boolean negate = (ν.real().truncate().mod(new Apint(2, radix)).signum() != 0);
            return (negate ? result.negate() : result);
        }
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Math.min(ν.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apcomplex result = sin(ν.add(one).multiply(acos(z, precision))).multiply(inverseRoot(one.subtract(pow(z, 2)), 2));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    /**
     * Renormalized Gegenbauer function.<p>
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>C<sub>ν</sub><sup style='position: relative; left: -0.4em;'>(0)</sup>(z)</i>
     *
     * @throws ArithmeticException If <code>ν</code> is zero.
     *
     * @since 1.14.0
     */

    public static Apcomplex gegenbauerC(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apint two = new Apint(2, ν.radix());
        return two.divide(ν).multiply(chebyshevT(ν, z));
    }

    /**
     * Gegenbauer function. For nonnegative integer values of <code>ν</code> gives the Gegenbauer polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param λ The second argument.
     * @param z The third argument.
     *
     * @return <i>C<sub>ν</sub><sup style='position: relative; left: -0.4em;'>λ</sup>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is -1 and real part of <code>λ</code> is > 1/2. Also if <code>z</code> is -1 and <code>λ</code> is 1/2 and <code>ν</code> is not an integer.
     *
     * @since 1.14.0
     */

    public static Apcomplex gegenbauerC(Apcomplex ν, Apcomplex λ, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (λ.isZero())
        {
            return λ;
        }
        if (ν.isInteger() && ν.real().signum() >= 0)
        {
            return gegenbauerC(ApfloatHelper.longValueExact(ν.real().truncate()), λ, z);
        }
        int radix = ν.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Util.min(ν.precision(), λ.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        λ = ApfloatHelper.ensurePrecision(λ, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat one = Apint.ONES[radix],
                two = new Apfloat(2, precision, radix),
                pi = ApfloatMath.pi(precision, radix);
        Apcomplex λ12 = ApfloatHelper.ensurePrecision(one.subtract(two.multiply(λ)), precision),
                  νλ = ApfloatHelper.ensurePrecision(ν.add(λ), precision),
                  ν1 = ApfloatHelper.ensurePrecision(ν.add(one), precision),
                  λ2ν = ApfloatHelper.ensurePrecision(two.multiply(λ).add(ν), precision),
                  λhalf = ApfloatHelper.ensurePrecision(λ.add(one.divide(two)), precision),
                  z12 = ApfloatHelper.ensurePrecision(one.subtract(z), precision).divide(two),
                  result;
        if (isNonPositiveInteger(ν1))
        {
            if (isNonPositiveInteger(λ))
            {
                return Apint.ZEROS[radix];
            }
            result = pochhammer(ν1, λ12.negate()).divide(gamma(ApfloatHelper.ensureGammaPrecision(λ, precision)));
        }
        else
        {
            result = pochhammer(λ, νλ).divide(gamma(ApfloatHelper.ensureGammaPrecision(ν1, precision)));
        }
        if (result.isZero())
        {
            return result;
        }
        result = result.multiply(pow(two, λ12)).multiply(sqrt(pi)).multiply(hypergeometric2F1Regularized(ν.negate(), λ2ν, λhalf, z12));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    private static Apcomplex gegenbauerC(long n, Apcomplex λ, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        long precision = Math.min(λ.precision(), z.precision());
        int radix = λ.radix();
        if (n == 0)
        {
            return new Apfloat(1, precision, radix);
        }
        precision = ApfloatHelper.extendPrecision(precision);
        λ = ApfloatHelper.ensurePrecision(λ, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        long n2 = n / 2,
             k = n2,
             n2k = n - 2 * k;
        Apcomplex sum = Apint.ZEROS[radix],
                  z2 = z.multiply(new Apint(2, radix)),
                  numerator = pochhammer(λ, n - k).multiply(n2k == 0 ? Apint.ONES[radix] : z2),
                  denominator = ApfloatMath.factorial(k, precision, radix).multiply(ApfloatMath.factorial(n2k, precision, radix));
        z2 = z2.multiply(z2);
        for (; k >= 0; k--, n2k += 2)
        {
            if (k < n2)
            {
                numerator = numerator.multiply(λ.add(new Apint(n - k - 1, radix))).multiply(z2);
                denominator = denominator.multiply(new Apint(n2k - 1, radix)).multiply(new Apint(n2k, radix));
            }
            Apcomplex term = numerator.divide(denominator);
            sum = ((k & 1) == 0 ? sum.add(term) : sum.subtract(term));
            if (k > 0)
            {
                numerator = numerator.multiply(new Apint(k, radix));
            }
        }
        return ApfloatHelper.reducePrecision(sum);
    }

    /**
     * Jacobi function. For nonnegative integer values of <code>ν</code> gives the Jacobi polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param a The second argument.
     * @param b The third argument.
     * @param z The fourth argument.
     *
     * @return <i>P<sub>ν</sub><sup style='position: relative; left: -0.4em;'>(a,b)</sup>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is -1 and real part of <code>b</code> is > 0 and <code>ν</code> is not a positive integer. Also if <code>ν + a</code> is a negative integer and <code>ν</code> is not an integer.
     *
     * @since 1.14.0
     */

    public static Apcomplex jacobiP(Apcomplex ν, Apcomplex a, Apcomplex b, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (ν.isInteger() && ν.real().signum() >= 0)
        {
            return jacobiP(ApfloatHelper.longValueExact(ν.real().truncate()), a, b, z);
        }
        int radix = ν.radix();
        long extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(Util.min(ν.precision(), a.precision(), b.precision(), z.precision()), extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        a = ApfloatHelper.ensurePrecision(a, precision);
        b = ApfloatHelper.ensurePrecision(b, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apint one = Apint.ONES[radix],
              two = new Apint(2, radix);
        Apcomplex ν1 = ApfloatHelper.ensurePrecision(ν.add(one), precision),
                  abν1 = ApfloatHelper.ensurePrecision(a.add(b).add(ν).add(one), precision),
                  a1 = ApfloatHelper.ensurePrecision(a.add(one), precision),
                  z12 = ApfloatHelper.ensurePrecision(one.subtract(z), precision).divide(two),
                  result = pochhammer(ν1, a).multiply(hypergeometric2F1Regularized(ν.negate(), abν1, a1, z12));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    private static Apcomplex jacobiP(long n, Apcomplex a, Apcomplex b, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        long precision = Util.min(a.precision(), b.precision(), z.precision());
        int radix = a.radix();
        if (n == 0)
        {
            return new Apfloat(1, precision, radix);
        }
        precision = ApfloatHelper.extendPrecision(precision);
        a = ApfloatHelper.ensurePrecision(a, precision);
        b = ApfloatHelper.ensurePrecision(b, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apint one = Apint.ONES[radix],
              two = new Apint(2, radix);
        Apcomplex sum = Apint.ZEROS[radix],
                  a1 = ApfloatHelper.ensurePrecision(a.add(one), precision),
                  abn1 = ApfloatHelper.ensurePrecision(a1.add(b).add(new Apint(n, radix)), precision),
                  z12 = ApfloatHelper.ensurePrecision(one.subtract(z), precision).divide(two),
                  numerator = one,
                  denominator = ApfloatMath.factorial(n, precision, radix);
        for (long k = 0; k <= n; k++)
        {
            Apint kk = new Apint(k, radix);
            Apcomplex term = numerator.multiply(pochhammer(a1.add(kk), n - k)).divide(denominator);
            sum = sum.add(term);
            if (k < n)
            {
                Apcomplex abnk1 = ApfloatHelper.ensurePrecision(abn1.add(kk), precision);
                numerator = numerator.multiply(new Apint(-n + k, radix)).multiply(abnk1).multiply(z12);
                denominator = denominator.multiply(kk.add(one));
            }
        }
        return ApfloatHelper.reducePrecision(sum);
    }

    /**
     * Fibonacci function. For nonnegative integer values of <code>ν</code> gives the Fibonacci polynomial.<p>
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return <i>F<sub>ν</sub>(z)</i>
     *
     * @throws ArithmeticException If <code>z</code> is -1 and <code>ν</code> is not an integer.
     *
     * @since 1.14.0
     */

    public static Apcomplex fibonacci(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = ν.radix();
        long targetPrecision = Math.min(ν.precision(), z.precision()),
             extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision);
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apint zero = Apfloat.ZEROS[radix],
              two = new Apint(2, radix),
              four = new Apint(4, radix);
        Apcomplex twoI = new Apcomplex(zero, two),
                  result;
        if (ν.isInteger() && (z.equals(twoI) || z.equals(twoI.negate())))
        {
            long n = ν.real().truncate().mod(four).longValueExact();
            Apcomplex i = new Apcomplex(zero, new Apint(z.imag().signum() < 0 ? -1 : 1, radix));
            result = pow(i, n + 1).multiply(ν).negate();
        }
        else
        {
            Apfloat pi = ApfloatMath.pi(precision, radix);
            Apcomplex ν2 = pow(two, ν),
                      z4 = sqrt(ApfloatHelper.ensurePrecision(z.multiply(z).add(four), precision)),
                      zz4ν = pow(ApfloatHelper.ensurePrecision(z.add(z4), precision), ν);
            result = zz4ν.divide(ν2).subtract(cos(pi.multiply(ν)).multiply(ν2).divide(zz4ν)).divide(z4);
        }
        targetPrecision = ApfloatHelper.reducePrecision(targetPrecision, Math.max(0, (long) Math.log(result.scale() * 0.3)));
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    /**
     * Euler polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param n The first argument.
     * @param z The second argument.
     *
     * @return <i>E<sub>n</sub>(z)</i>
     *
     * @throws IllegalArgumentException If <code>n</code> &lt; 0.
     *
     * @since 1.14.0
     */

    public static Apcomplex eulerE(long n, Apcomplex z)
        throws IllegalArgumentException, ApfloatRuntimeException
    {
        return eulerE(n, z, z.precision());
    }

    static Apcomplex eulerE(long n, Apcomplex z, long precision)
        throws IllegalArgumentException, ApfloatRuntimeException
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("Negative Euler polynomial");
        }
        int radix = z.radix();
        long n1 = Util.addExact(n, 1);
        if (z.isZero())
        {
            if (n > 0 && (n & 1) == 0)
            {
                return z;
            }
            if (precision == Apfloat.INFINITE)
            {
                Apint one = Apint.ONES[radix],
                      two = new Apint(2, radix);
                return AprationalMath.bernoulli(n1, radix).multiply(two).multiply(ApintMath.pow(two, n1).subtract(one)).divide(new Apint(n1, radix)).negate();
            }
        }
        if (n == 0)
        {
            return new Apfloat(1, precision, radix);
        }
        long extraPrecision = (long) Math.ceil(2.7 * n / Math.log(radix)),
            workingPrecision = ApfloatHelper.extendPrecision(precision, extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, workingPrecision);
        Apfloat two = new Apfloat(2, workingPrecision, radix),
                nn = new Apfloat(-n, workingPrecision, radix);
        Apcomplex result = two.multiply(pow(two, n1).multiply(zeta(nn, z.divide(two))).subtract(zeta(nn, z)));
        return ApfloatHelper.limitPrecision(result, precision);
    }

    /**
     * Bernoulli polynomial.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param n The first argument.
     * @param z The second argument.
     *
     * @return <i>B<sub>n</sub>(z)</i>
     *
     * @throws IllegalArgumentException If <code>n</code> &lt; 0.
     *
     * @since 1.14.0
     */

    public static Apcomplex bernoulliB(long n, Apcomplex z)
        throws IllegalArgumentException, ApfloatRuntimeException
    {
        return bernoulliB(n, z, z.precision());
    }

    static Apcomplex bernoulliB(long n, Apcomplex z, long precision)
        throws IllegalArgumentException, ApfloatRuntimeException
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("Negative Bernoulli polynomial");
        }
        int radix = z.radix();
        if (z.isZero())
        {
            if (n > 1 && (n & 1) == 1)
            {
                return z;
            }
            if (precision == Apfloat.INFINITE)
            {
                return AprationalMath.bernoulli(n, radix);
            }
        }
        if (n == 0)
        {
            return new Apfloat(1, precision, radix);
        }
        long extraPrecision = (long) Math.ceil(2.7 * n / Math.log(radix)),
             workingPrecision = ApfloatHelper.extendPrecision(precision, extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, workingPrecision);
        Apfloat nn = new Apfloat(-n, workingPrecision, radix),
                n1 = new Apfloat(1 - n, workingPrecision, radix);
        Apcomplex result = nn.multiply(zeta(n1, z));
        return ApfloatHelper.limitPrecision(result, precision);
    }

    /**
     * Harmonic number.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The argument.
     *
     * @return <i>H<sub>z</sub></i>
     *
     * @throws ArithmeticException If <code>z</code> is a negative integer.
     *
     * @since 1.14.0
     */

    public static Apcomplex harmonicNumber(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return z;
        }
        long precision = z.precision();
        int radix = z.radix();
        Apint one = Apint.ONES[radix];
        Apfloat eulerGamma = ApfloatMath.euler(precision, radix);
        Apcomplex z1 = ApfloatHelper.ensurePrecision(z.add(one), precision);
        return digamma(z1).add(eulerGamma);
    }

    /**
     * Generalized harmonic number.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param z The first argument.
     * @param r The second argument.
     *
     * @return <i>H<sub>z</sub><sup style='position: relative; left: -0.4em;'>(r)</sup></i>
     *
     * @throws ArithmeticException If <code>z</code> is a negative integer, unless <code>r</code> has a negative real part or is zero.
     *
     * @since 1.14.0
     */

    public static Apcomplex harmonicNumber(Apcomplex z, Apcomplex r)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isZero() || r.isZero())
        {
            return z;
        }
        int radix = z.radix();
        Apint one = Apint.ONES[radix];
        if (r.equals(one))
        {
            return harmonicNumber(z);
        }
        long precision = Math.min(z.precision(), r.precision());
        Apcomplex z1 = ApfloatHelper.ensurePrecision(z.add(one), precision);
        return zeta(r).subtract(zeta(r, z1));
    }

    /**
     * Polylogarithm.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param ν The first argument.
     * @param z The second argument.
     *
     * @return Li<sub>ν</sub>(z)
     *
     * @throws ArithmeticException If the real part of <code>ν</code> is &le; 1 and <code>z</code> is 1.
     *
     * @since 1.14.0
     */

    public static Apcomplex polylog(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isZero())
        {
            return z;
        }
        int radix = ν.radix();
        long targetPrecision = Math.min(ν.precision(), z.precision()),
             extraPrecision = (long) Math.ceil(Math.max(ApfloatHelper.getSmallExtraPrecision(radix), 2.7 * ApfloatHelper.longValueExact(ν.real().ceil()) / Math.log(radix))),
             precision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision);
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, radix);
        if (z.equals(one) && ν.real().compareTo(one) <= 0)
        {
            throw new ApfloatArithmeticException("Polylogarithm is infinite", "polylog.infinite");
        }
        if (ν.isInteger() && ν.real().signum() > 0)
        {
            // Avoid gamma of nonpositive integer
            ν = ν.precision(Apfloat.INFINITE).add(scale(new Apfloat("0.1", precision, radix), -precision));
            precision = Util.ifFinite(precision, precision + precision);
        }
        else if (ν.real().signum() > 0)
        {
            Apint νRounded = RoundingHelper.roundToInteger(ν.real(), RoundingMode.HALF_EVEN).truncate();
            long digitLoss = Math.min(precision, -ν.subtract(νRounded).scale());
            if (digitLoss > 0)
            {
                precision = Util.ifFinite(precision, precision + digitLoss);
            }
        }
        ν = ApfloatHelper.ensurePrecision(ν, precision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        if (ν.isZero())
        {
            // Avoid zeta of one
            Apcomplex z1 = ApfloatHelper.ensurePrecision(one.subtract(z), precision);
            return ApfloatHelper.limitPrecision(z.divide(z1), targetPrecision);
        }
        Apfloat pi = ApfloatMath.pi(precision, radix);
        boolean unitLine = (z.imag().signum() == 0 && z.real().signum() > 0 && z.real().compareTo(one) < 0);
        Apcomplex i = new Apcomplex(zero, one),
                  epiνi2 = exp(pi.multiply(ν).multiply(i).divide(two)),
                  zn = (unitLine ? z : z.negate()),
                  offset = (unitLine ? zero : pi.multiply(i)),
                  ν1 = ApfloatHelper.ensurePrecision(ν.subtract(one), precision),
                  logznpi2i = ApfloatHelper.ensurePrecision(log(zn).add(offset), precision).divide(pi.multiply(two).multiply(i)),
                  oneMinusLogznpi2i = ApfloatHelper.ensurePrecision(one.subtract(logznpi2i), precision),
                  result = pow(two.multiply(pi), ν1).multiply(i).multiply(gamma(ApfloatHelper.ensureGammaPrecision(ν1.negate(), precision))).multiply(zeta(ν1.negate(), logznpi2i).divide(epiνi2).subtract(epiνi2.multiply(zeta(ν1.negate(), oneMinusLogznpi2i))));
        targetPrecision = ApfloatHelper.reducePrecision(targetPrecision, Math.max(0, (long) Math.log(result.scale() * 0.3)));
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    /**
     * Logistic sigmoid.
     *
     * @param z The argument.
     *
     * @return &sigma;(z)
     *
     * @throws ArithmeticException If <code>z</code> is an odd integer multiple of &pi; i.
     *
     * @since 1.14.0
     */

    public static Apcomplex logisticSigmoid(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = z.radix();
        Apint one = Apint.ONES[radix];
        if (z.isZero())
        {
            Apint two = new Apint(2, radix);
            return new Aprational(one, two);
        }

        long precision = z.precision();
        Apint minusOne = new Apint(-1, radix);
        Apcomplex e = (z.scale() < -precision ? one : exp(z.negate()));
        return one.precision(precision).divide(one.precision(ApfloatHelper.extendPrecision(precision, e.equalDigits(minusOne))).add(e));
    }

    /**
     * Returns the unit in the last place of the argument, considering the
     * scale and precision. This is maximum of the ulps of the real and
     * imaginary part of the argument.
     * If the precision of the argument is infinite, zero is returned.
     *
     * @param z The argument.
     *
     * @return The ulp of the argument.
     *
     * @since 1.10.0
     */

    public static Apfloat ulp(Apcomplex z)
    {
        return ApfloatMath.max(ApfloatMath.ulp(z.real()), ApfloatMath.ulp(z.imag()));
    }

    // Extend the precision on last iteration
    private static Apcomplex lastIterationExtendPrecision(int iterations, int precisingIteration, Apcomplex z)
    {
        return (iterations == 0 && precisingIteration != 0 ? ApfloatHelper.extendPrecision(z) : z);
    }

    static boolean isNonPositiveInteger(Apcomplex z)
    {
        return (z.isInteger() && z.real().signum() <= 0);
    }
}
