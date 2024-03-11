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
package org.apfloat;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apfloat.spi.Util;

import static java.util.Comparator.comparing;

import static org.apfloat.ApcomplexMath.abs;
import static org.apfloat.ApcomplexMath.exp;
import static org.apfloat.ApcomplexMath.gamma;
import static org.apfloat.ApcomplexMath.isNonPositiveInteger;
import static org.apfloat.ApcomplexMath.pow;
import static org.apfloat.ApcomplexMath.sin;
import static org.apfloat.ApcomplexMath.sqrt;
import static org.apfloat.ApfloatMath.pi;
import static org.apfloat.ApfloatMath.scale;

/**
 * Helper class for hypergeometric functions.
 *
 * @since 1.11.0
 * @version 1.14.0
 * @author Mikko Tommila
 */

class HypergeometricHelper
{
    // See: Computing hypergeometric functions rigorously by Fredrik Johansson, https://arxiv.org/pdf/1606.06977.pdf

    private static class RetryException
        extends RuntimeException
    {
        public RetryException(long precisionLoss)
        {
            this.precisionLoss = precisionLoss;
        }

        public long getPrecisionLoss()
        {
            return precisionLoss;
        }

        private static final long serialVersionUID = 1L;

        private long precisionLoss;
    }

    private static class NotConvergingException
        extends RuntimeException
    {
            private static final long serialVersionUID = 1L;
    }

    private class Hypergeometric2F1Helper
    {
        public Hypergeometric2F1Helper(boolean retry)
        {
            this.a = HypergeometricHelper.this.a[0];
            this.b = HypergeometricHelper.this.a[1];
            this.c = HypergeometricHelper.this.b[0];
            this.z = HypergeometricHelper.this.z;
            this.one = HypergeometricHelper.this.one;
            this.retry = retry;
        }

        public void ensurePrecisions()
        {
            a = ensurePrecision(a);
            b = ensurePrecision(b);
            c = ensurePrecision(c);
            z = ensurePrecision(z);
        }

        public void adjustIntegerAB()
        {
            // Transforms T2 and T3 are of the form factor * (term1 - term2) where term1 and term2 can be very close to each other, so overall precision needs to be increased to get the actual result, instead of just zero
            Apcomplex ab = a.subtract(b);
            long digitLoss;
            if (ab.isInteger())
            {
                // Adjust the one which has larger magnitude in real part
                swapLargerAB();
                digitLoss = workingPrecision;
                workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                Apfloat offset = offset(-digitLoss);
                if (!b.equals(offset))  // It might be that b is exactly the same as the offset so a - b is not integer after all (but would be if we did add the offset), we just need to increase the precisions
                {
                    a = new Apcomplex(a.real().precision(Apfloat.INFINITE).add(offset), a.imag());
                }
                b = new Apcomplex(adjustOffset(b.real(), offset), b.imag());
                ensurePrecisions();
            }
            else
            {
                Apint abRounded = RoundingHelper.roundToInteger(ab.real(), RoundingMode.HALF_EVEN).truncate();
                digitLoss = Math.min(workingPrecision, -ab.subtract(abRounded).scale());
                if (digitLoss > 0)
                {
                    workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                    // Set precision of a and b so that after computing a - b the difference still has the required precision
                    Apfloat offset = offset(-digitLoss);
                    a = new Apcomplex(adjustOffset(a.real(), offset), a.imag());
                    b = new Apcomplex(adjustOffset(b.real(), offset), b.imag());
                    ensurePrecisions();
                }
            }
        }

        public void adjustIntegerCAB()
        {
            // Transforms T4 and T5 are of the form factor * (term1 - term2) where term1 and term2 can be very close to each other, so overall precision needs to be increased to get the actual result, instead of just zero
            Apcomplex cab = c.subtract(a).subtract(b);
            long digitLoss;
            if (cab.isInteger())
            {
                Apcomplex aOrig = a,
                          bOrig = b,
                          cOrig = c;
                // Adjust the one which has largest magnitude in real part
                swapLargerAB();
                digitLoss = workingPrecision;
                workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                Apfloat offset = offset(-digitLoss);
                if (c.real().scale() > a.real().scale())
                {
                    c = new Apcomplex(c.real().precision(Apfloat.INFINITE).add(offset), c.imag());
                    a = new Apcomplex(adjustOffset(a.real(), offset), a.imag());
                }
                else
                {
                    c = new Apcomplex(adjustOffset(c.real(), offset), c.imag());
                    a = new Apcomplex(a.real().precision(Apfloat.INFINITE).add(offset), a.imag());
                }
                b = new Apcomplex(adjustOffset(b.real(), offset), b.imag());
                ensurePrecisions();
                if (c.subtract(a).subtract(b).isInteger())
                {
                    // It could be that a+b happened to be equal to the offset, to suitable precision, so we don't need to adjust by offset after all, we just need to increase the precision
                    a = aOrig;
                    b = bOrig;
                    c = cOrig;
                    ensurePrecisions();
                    assert (!c.subtract(a).subtract(b).isInteger());
                }
            }
            else
            {
                Apint cabRounded = RoundingHelper.roundToInteger(cab.real(), RoundingMode.HALF_EVEN).truncate();
                digitLoss = Math.min(workingPrecision, -cab.subtract(cabRounded).scale());
                if (digitLoss > 0)
                {
                    workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                    Apfloat offset = offset(-digitLoss);
                    a = new Apcomplex(adjustOffset(a.real(), offset), a.imag());
                    b = new Apcomplex(adjustOffset(b.real(), offset), b.imag());
                    c = new Apcomplex(adjustOffset(c.real(), offset), c.imag());
                    ensurePrecisions();
                }
            }
        }

        private void swapLargerAB()
        {
            if (a.real().scale() < b.real().scale())
            {
                // a is always the one that has larger magnitude in real part
                Apcomplex tmp = a;
                a = b;
                b = tmp;
            }
        }

        public Apcomplex transform(Apcomplex s, Apcomplex c, Apcomplex base1, Apcomplex exp1, Apcomplex g1, Apcomplex g2, Apcomplex a1, Apcomplex b1, Apcomplex c1, Apcomplex base2, Apcomplex exp2, Apcomplex base3, Apcomplex exp3, Apcomplex g3, Apcomplex g4, Apcomplex a2, Apcomplex b2, Apcomplex c2, Apcomplex z)
        {
            Apcomplex term1,
                      term2;
            if (isNonPositiveInteger(g1) || isNonPositiveInteger(g2))
            {
                term1 = zero;   // Division by infinity
            }
            else
            {
                term1 = pow(base1, exp1).divide(gamma(g1).multiply(gamma(g2)).multiply(gamma(c1))).multiply(evaluate(a1, b1, c1, z));
            }
            if (isNonPositiveInteger(g3) || isNonPositiveInteger(g4))
            {
                term2 = zero;   // Division by infinity
            }
            else
            {
                term2 = pow(base2, exp2).multiply(pow(base3, exp3)).divide(gamma(g3).multiply(gamma(g4)).multiply(gamma(c2))).multiply(evaluate(a2, b2, c2, z));
            }
            Apcomplex d = term1.subtract(term2);
            long precisionLoss = (d.isZero() ? workingPrecision : targetPrecision - d.precision());
            if (retry && precisionLoss > 1) // Allow a precision loss of 1 (which happens often), otherwise retry with increased precision
            {
                throw new RetryException(precisionLoss);
            }
            Apfloat pi = pi(workingPrecision, radix);
            return gamma(c).multiply(pi).divide(sin(pi.multiply(s))).multiply(d);
        }

        private Apcomplex evaluate(Apcomplex a, Apcomplex b, Apcomplex c, Apcomplex z)
        {
            return HypergeometricHelper.this.evaluate(new Apcomplex[] { a, b }, new Apcomplex[] { c }, z);
        }

        public Apcomplex a,
                         b,
                         c,
                         z;
        public Apint one;
        private boolean retry;
    }

    // T0 is the direct evaluation of the series without transformation, T1 - T5 are as per DLMF 15.8.1 - 15.8.5  
    private static enum Transformation
    {
        T0 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return true;
            }

            @Override
            public Apcomplex z(Apcomplex z)
            {
                return z;
            }

            @Override
            public Apcomplex value(Hypergeometric2F1Helper helper)
            {
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return helper.evaluate(a, b, c, z);
            }
        },
        T1 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return !z.equals(Apint.ONES[z.radix()]);
            }

            @Override
            public Apcomplex z(Apcomplex z)
            {
                Apint one = Apint.ONES[z.radix()];
                return z.divide(z.subtract(one));
            }

            @Override
            public Apcomplex value(Hypergeometric2F1Helper helper)
            {
                Apint one = helper.one;
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z,
                          z1 = one.subtract(z),
                          s = c.subtract(a),
                          t = c.subtract(b);
                if (isNonPositiveInteger(s) && (!isNonPositiveInteger(t) || s.real().compareTo(t.real()) >= 0))
                {
                    return ApcomplexMath.pow(z1, b.negate()).multiply(helper.evaluate(s, b, c, z(z)));
                }
                return ApcomplexMath.pow(z1, a.negate()).multiply(helper.evaluate(a, t, c, z(z)));
            }
        },
        T2 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return !z.isZero();
            }

            @Override
            public Apcomplex z(Apcomplex z)
            {
                Apint one = Apint.ONES[z.radix()];
                return one.divide(z);
            }

            @Override
            public Apcomplex value(Hypergeometric2F1Helper helper)
            {
                helper.adjustIntegerAB();
                Apint one = helper.one;
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return helper.transform(b.subtract(a), c, z.negate(), a.negate(), b, c.subtract(a), a, a.subtract(c).add(one), a.subtract(b).add(one), z.negate(), b.negate(), one, one, a, c.subtract(b), b, b.subtract(c).add(one), b.subtract(a).add(one), z(z));
            }
        },
        T3 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return !z.equals(Apint.ONES[z.radix()]);
            }

            @Override
            public Apcomplex z(Apcomplex z)
            {
                Apint one = Apint.ONES[z.radix()];
                return one.divide(one.subtract(z));
            }

            @Override
            public Apcomplex value(Hypergeometric2F1Helper helper)
            {
                helper.adjustIntegerAB();
                Apint one = helper.one;
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return helper.transform(b.subtract(a), c, one.subtract(z), a.negate(), b, c.subtract(a), a, c.subtract(b), a.subtract(b).add(one), one.subtract(z), b.negate(), one, one, a, c.subtract(b), b, c.subtract(a), b.subtract(a).add(one), z(z));
            }
        },
        T4 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return true;
            }

            @Override
            public Apcomplex z(Apcomplex z)
            {
                Apint one = Apint.ONES[z.radix()];
                return one.subtract(z);
            }

            @Override
            public Apcomplex value(Hypergeometric2F1Helper helper)
            {
                helper.adjustIntegerCAB();
                Apint one = helper.one;
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return helper.transform(c.subtract(b).subtract(a), c, one, one, c.subtract(a), c.subtract(b), a, b, a.add(b).subtract(c).add(one), one.subtract(z), c.subtract(a).subtract(b), one, one, a, b, c.subtract(a), c.subtract(b), c.subtract(a).subtract(b).add(one), z(z));
            }
        },
        T5 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return !z.isZero();
            }

            @Override
            public Apcomplex z(Apcomplex z)
            {
                Apint one = Apint.ONES[z.radix()];
                return one.subtract(one.divide(z));
            }

            @Override
            public Apcomplex value(Hypergeometric2F1Helper helper)
            {
                helper.adjustIntegerCAB();
                Apint one = helper.one;
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return helper.transform(c.subtract(b).subtract(a), c, z, a.negate(), c.subtract(a), c.subtract(b), a, a.subtract(c).add(one), a.add(b).subtract(c).add(one), one.subtract(z), c.subtract(a).subtract(b), z, a.subtract(c), a, b, c.subtract(a), one.subtract(a), c.subtract(a).subtract(b).add(one), z(z));
            }
        };

        public abstract boolean isApplicable(Apcomplex z);

        public abstract Apcomplex z(Apcomplex z);

        public abstract Apcomplex value(Hypergeometric2F1Helper helper);
    }

    /**
     * Helper for the generalized hypergeometric function <i><sub>p</sub>F<sub>q</sub></i>.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param b The second argument.
     * @param z The third argument.
     */

    private HypergeometricHelper(Apcomplex[] a, Apcomplex[] b, Apcomplex z)
    {
        this.radix = z.radix();
        this.extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix);
        this.targetPrecision = ApfloatHelper.extendPrecision(precision(a, b, z), extraPrecision);
        this.workingPrecision = targetPrecision;
        this.a = Arrays.stream(a).map(c -> ApfloatHelper.extendPrecision(c, extraPrecision)).toArray(Apcomplex[]::new);
        this.b = Arrays.stream(b).map(c -> ApfloatHelper.extendPrecision(c, extraPrecision)).toArray(Apcomplex[]::new);
        this.z = ensurePrecision(z);
        this.one = Apint.ONES[radix];
        this.zero = Apint.ZEROS[radix];
    }

    /**
     * Generalized hypergeometric function <i><sub>p</sub>F<sub>q</sub></i>.<p>
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
     * @return <i><sub>p</sub>F<sub>q</sub>(a<sub>1</sub>, …, a<sub>p</sub>; b<sub>1</sub>, …, b<sub>q</sub>; z)</i>
     *
     * @throws ArithmeticException If the series does not converge.
     */

    public static Apcomplex hypergeometricPFQ(Apcomplex[] a, Apcomplex[] b, Apcomplex z)
    {
        HypergeometricHelper helper = new HypergeometricHelper(a, b, z);
        return helper.result(helper.hypergeometricPFQ());
    }

    /**
     * Regularized generalized hypergeometric function <i><sub>p</sub>F̃<sub>q</sub></i>.<p>
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
     * @return <i><sub>p</sub>F̃<sub>q</sub>(a<sub>1</sub>, …, a<sub>p</sub>; b<sub>1</sub>, …, b<sub>q</sub>; z)</i>
     *
     * @throws ArithmeticException If the series does not converge.
     *
     * @since 1.13.0
     */

    public static Apcomplex hypergeometricPFQRegularized(Apcomplex[] a, Apcomplex[] b, Apcomplex z)
    {
        HypergeometricHelper helper = new HypergeometricHelper(a, b, z);
        return helper.result(helper.hypergeometricPFQRegularized());
    }

    private Apcomplex hypergeometricPFQRegularized()
    {
        Apcomplex result;
        Apfloat n = null;
        int j = -1;
        // Find smallest integer from b that is <= 0, if any
        for (int i = 0; i < b.length; i++)
        {
            Apfloat br = b[i].real().negate();
            if (isNonPositiveInteger(b[i]))
            {
                if (n == null)
                {
                    j = i;
                    n = br;
                }
                else if (br.compareTo(n) > 0)
                {
                    j = i;
                    n = br;
                }
            }
        }
        if (n == null)
        {
            // None of the b is a nonpositive integer, regularization is trivial
            Apcomplex[] gamma = Arrays.stream(b).map(this::ensureGammaPrecision).map(ApcomplexMath::gamma).toArray(Apcomplex[]::new);
            result = hypergeometricPFQ().divide(ApcomplexMath.product(gamma));
        }
        else
        {
            // At least one of the b is a nonpositive integer, regularization needs to be done by omitting the terms where the divisor is infinite
            int radix = z.radix();
            Apfloat n1 = n.add(Apint.ONES[radix]);
            Apcomplex[] pochhammer = Arrays.stream(a).map(ai -> ApcomplexMath.pochhammer(ai, n1)).toArray(Apcomplex[]::new);
            result = ApcomplexMath.product(pochhammer);
            if (!result.isZero())
            {
                Apfloat n2 = n.add(new Apint(2, radix));
                Apcomplex[] gamma = Arrays.stream(b).map(n1::add).map(this::ensureGammaPrecision).map(ApcomplexMath::gamma).toArray(Apcomplex[]::new);
                result = result.multiply(ApcomplexMath.pow(z, n1)).divide(ApcomplexMath.gamma(n2).multiply(ApcomplexMath.product(gamma)));
                a = Arrays.stream(a).map(n1::add).toArray(Apcomplex[]::new);
                b = Arrays.stream(b).map(n1::add).toArray(Apcomplex[]::new);
                b[j] = n2;
                result = result.multiply(hypergeometricPFQ());
            }
        }
        return result;
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
     * @param fastOnly Only attempt relatively fast algorithms and return null if not applicable
     *
     * @return <i>U(a, b, z)</i>
     *
     * @since 1.13.0
     */

    public static Apcomplex hypergeometricU(Apcomplex a, Apcomplex b, Apcomplex z, boolean fastOnly)
    {
        HypergeometricHelper helper = new HypergeometricHelper(new Apcomplex[] { a }, new Apcomplex[] { b }, z);
        return helper.result(helper.hypergeometricU(fastOnly));
    }

    // In the general case (p = q + 1) this works only for |z| < 1, and in general, may converge slowly for very large |z|
    private Apcomplex hypergeometricPFQ()
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex result = checkResult();
        if (result != null)
        {
            return result;
        }
        // After checking for polynomial case, if any a and b are equal, they can be removed
        List<Apcomplex> bList = new ArrayList<>(Arrays.asList(b));
        a = Arrays.stream(a).filter(z -> !bList.remove(z)).toArray(Apcomplex[]::new);
        b = bList.toArray(new Apcomplex[0]);
        if (a.length == 2 && b.length == 1)
        {
            return hypergeometric2F1(a[0], a[1], b[0], z);
        }
        if (a.length > b.length + 1L)
        {
            if (z.isZero())
            {
                return new Apfloat(1, targetPrecision, radix);
            }
            throw new ArithmeticException("Series does not converge");
        }
        if (a.length == 0 && b.length == 0)
        {
            return ApcomplexMath.exp(z);
        }
        if (a.length == 1 && b.length == 0)
        {
            return ApcomplexMath.pow(one.subtract(z), a[0].negate());
        }
        if (a.length == 0 && b.length == 1)
        {
            return hypergeometric0F1(b[0], z);
        }
        if (a.length == 1 && b.length == 1)
        {
            return hypergeometric1F1(a[0], b[0], z);
        }
        assert (b.length > 0);

        result = evaluate(a, b, z);
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    private Apcomplex hypergeometric0F1(Apcomplex b, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (abs(z).doubleValue() > targetPrecision * Math.log(radix))
        {
            // Evaluate with 1F1
            // https://functions.wolfram.com/HypergeometricFunctions/Hypergeometric0F1/27/01/
            Apint two = new Apint(2, radix),
                  four = new Apint(4, radix);
            Aprational half = new Aprational(one, two);
            Apcomplex b12 = ensurePrecision(b.subtract(half));
            if (isNonPositiveInteger(b12))
            {
                // This transformation wouldn't work correctly if 0F1 is not polynomial, but 1F1 becomes polynomial (b12 is nonpositive integer)
                // Note that if b is not a nonpositive integer (in which case 0F1 would also throw exception) then 2b-1 is not a nonpositive integer either so b21 is not a problem like b12
                long digitLoss = workingPrecision;
                workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                Apfloat offset = offset(-digitLoss);
                b = new Apcomplex(b.real().precision(Apfloat.INFINITE).add(offset), b.imag());
                b = ensurePrecision(b);
                z = ensurePrecision(z);
                b12 = ensurePrecision(b.subtract(half));
            }
            Apcomplex b21 = ensurePrecision(two.multiply(b).subtract(one)),
                      result;
            if (z.real().signum() >= 0)
            {
                // Use the formula for Bessel I and Bessel I as 1F1
                Apcomplex sqrtZ = sqrt(z);
                result = exp(two.negate().multiply(sqrtZ)).multiply(hypergeometric1F1(b12, b21, four.multiply(sqrtZ)));
            }
            else
            {
                // Use the formula for Bessel J and Bessel J as 1F1
                Apcomplex i = new Apcomplex(zero, one),
                          sqrtZ = sqrt(z.negate());
                result = exp(two.negate().multiply(i).multiply(sqrtZ)).multiply(hypergeometric1F1(b12, b21, four.multiply(i).multiply(sqrtZ)));
            }
            return ApfloatHelper.limitPrecision(result, targetPrecision);
        }

        Apcomplex result = evaluate(new Apcomplex[0], new Apcomplex[] { b }, z);
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    private Apcomplex hypergeometric1F1(Apcomplex a, Apcomplex b, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (abs(z).doubleValue() > targetPrecision * Math.log(radix))
        {
            try
            {
                // Evaluate with U*
                // Note: this transformation works correctly if 1F1 is not polynomial but U* becomes polynomial (a or a-b+1 is nonpositive integer, or b-a or 1-a is nonpositive integer)
                Apcomplex ba = ensurePrecision(b.subtract(a));
                Apcomplex result = zero;
                if (!isNonPositiveInteger(ba))
                {
                    result = pow(z.negate(), a.negate()).divide(gamma(ensureGammaPrecision(ba))).multiply(hypergeometricUStar(a, b, z));
                }
                result = result.add(pow(z, ba.negate()).multiply(exp(z)).divide(gamma(ensureGammaPrecision(a))).multiply(hypergeometricUStar(ba, b, z.negate()))).multiply(gamma(ensureGammaPrecision(b)));
                return ApfloatHelper.limitPrecision(result, targetPrecision);
            } catch (NotConvergingException nce)
            {
                // Ignore and retry with the (possibly very slow) direct evaluation of the series
            }
        }

        Apcomplex result = hypergeometric1F1series(a, b, z);
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    private Apcomplex hypergeometric1F1series(Apcomplex a, Apcomplex b, Apcomplex z)
    {
        if (a.equals(b) && !isNonPositiveInteger(a))
        {
            return ApcomplexMath.exp(z);
        }
        Apcomplex factor = one;
        if (z.real().signum() < 0)
        {
            // Kummer transformation
            factor = ApcomplexMath.exp(z);
            a = ensurePrecision(b.subtract(a));
            z = z.negate();
        }

        Apcomplex result = evaluate(new Apcomplex[] { a }, new Apcomplex[] { b }, z).multiply(factor);
        return result;
    }

    private Apcomplex hypergeometricUStar(Apcomplex a, Apcomplex b, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex ab1 = ensurePrecision(a.subtract(b).add(one));
        Apcomplex result = evaluate(new Apcomplex[] { a, ab1 }, new Apcomplex[0], one.divide(z).negate());
        return result;
    }

    private Apcomplex hypergeometricU(boolean fastOnly)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (abs(z).doubleValue() > targetPrecision * Math.log(radix))
        {
            try
            {
                // Evaluate with U*
                Apcomplex result = pow(z, a[0].negate()).multiply(hypergeometricUStar(a[0], b[0], z));
                return ApfloatHelper.limitPrecision(result, targetPrecision);
            }
            catch (NotConvergingException nce)
            {
                // Ignore and retry with the (possibly very slow) direct evaluation of the series
                if (fastOnly)
                {
                    // Too slow, try another algorithm
                    return null;
                }
            }
        }
        if (fastOnly && (Stream.concat(Arrays.stream(a), Arrays.stream(b)).map(Apcomplex::real).reduce(ApfloatMath::min).get().doubleValue() < -100.0 * targetPrecision ||  // Check minimum number of evaluation terms
                         z.scale() > 5.0 / Math.log(radix)))    // Check convergence speed
        {
            // Too slow, try another algorithm
            return null;
        }

        // Evaluate two 1F1 functions, this often results in cancellation of significant digits so we may retry with higher precision
        Apcomplex result;
        long precisionLoss;
        do
        {
            Apcomplex a = this.a[0],
                      b = this.b[0];
            a = ensurePrecision(a);
            b = ensurePrecision(b);
            z = ensurePrecision(z);

            // First check if b is an integer or near-integer and adjust if necessary
            if (b.isInteger())
            {
                long digitLoss = workingPrecision;
                workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                Apfloat offset = offset(-digitLoss);
                b = new Apcomplex(b.real().precision(Apfloat.INFINITE).add(offset), b.imag());
                a = ensurePrecision(a);
                b = ensurePrecision(b);
                z = ensurePrecision(z);
            }
            else
            {
                Apint bRounded = RoundingHelper.roundToInteger(b.real(), RoundingMode.HALF_EVEN).truncate();
                long digitLoss = Math.min(workingPrecision, -b.subtract(bRounded).scale());
                if (digitLoss > 0)
                {
                    workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                    a = ensurePrecision(a);
                    b = ensurePrecision(b);
                    z = ensurePrecision(z);
                }
            }

            Apcomplex ab1 = ensureGammaPrecision(a.subtract(b).add(one));
            result = zero;
            if (!isNonPositiveInteger(ab1))
            {
                Apcomplex b1n = ensureGammaPrecision(one.subtract(b));
                result = gamma(b1n).divide(gamma(ab1)).multiply(hypergeometric1F1series(a, b, z));
            }
            if (!isNonPositiveInteger(a))
            {
                Apint two = new Apint(2, radix);
                Apcomplex b1 = ensureGammaPrecision(b.subtract(one)),
                          b1n = ensurePrecision(one.subtract(b)),
                          b2 = ensurePrecision(two.subtract(b));
                a = ensureGammaPrecision(a);
                result = result.add(gamma(b1).divide(gamma(a)).multiply(pow(z, b1n)).multiply(hypergeometric1F1series(ab1, b2, z)));
            }
            precisionLoss = (result.isZero() ? workingPrecision : targetPrecision - result.precision());
            workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + precisionLoss);
        } while (precisionLoss > 0);
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    private Apcomplex hypergeometric2F1(Apcomplex a, Apcomplex b, Apcomplex c, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        // Rules (in order!):
        // If a or b is a nonpositive integer, result is a polynomial (unless c is a nonpositive integer > a and b)
        // If c is a nonpositive integer, result is infinity (unless a or b is a nonpositive integer >= c)
        // If c and (a or b) is same nonpositive integer -> result is still a polynomial
        // If z is 1, result is infinity if re(a+b-c) >= 0
        // Could also check if c-a or c-b is a nonpositive integer, then use transformation formula and function value is a polynomial
        // If c = a or c = b (and not a nonpositive integer) then it's 1F0
        // Then perform transformation so that transformed z is as small as possible (in absolute value)
        // If transformed z is too big (i.e. near exp(±i pi / 3)) then use alternative algorithm
        // If transformation requires division by possible zero (e.g. b - a), then alter the parameters slightly to be nonzero, e.g. by largest ulp of a or b, ensure the required working precision (nb. probably no point to calculate average of two calls with +-ulp due to shape of function)
        // Real version is real if z <= 1 (may be infinite if z = 1 depending on a, b and c as said above) or in case of polynomial always
        if (z.equals(one))
        {
            if (a.real().add(b.real()).subtract(c.real()).signum() >= 0)
            {
                throw new ArithmeticException("Does not converge");
            }
            Apcomplex s = c.subtract(a),
                      t = c.subtract(b);
            if (isNonPositiveInteger(s) || isNonPositiveInteger(t))
            {
                return zero;
            }
            Apcomplex cab = s.subtract(b);
            s = ApfloatHelper.ensureGammaPrecision(s, workingPrecision);
            t = ApfloatHelper.ensureGammaPrecision(t, workingPrecision);
            cab = ApfloatHelper.ensureGammaPrecision(cab, workingPrecision);
            c = ApfloatHelper.ensureGammaPrecision(c, workingPrecision);
            return ApcomplexMath.gamma(c).multiply(ApcomplexMath.gamma(cab)).divide(ApcomplexMath.gamma(s).multiply(ApcomplexMath.gamma(t)));
        }
        Apcomplex zDoublePrecision = z.precision(ApfloatHelper.getDoublePrecision(radix));
        // Does it make any sense to check first if the transform results in a polynomial? The polynomial could potentially be of huge degree anyways
        Transformation transformation = Arrays.stream(Transformation.values()).filter(t -> t.isApplicable(z)).min(comparing(t -> abs(t.z(zDoublePrecision)))).get();
        Apcomplex result;
        if (abs(transformation.z(zDoublePrecision)).doubleValue() > 0.8)
        {
            // Use alternative algorithm as none of the transforms is very good
            result = alternative(a, b, c, z);
        }
        else
        {
            result = null;
            try
            {
                result = transformation.value(new Hypergeometric2F1Helper(true));
            }
            catch (RetryException re)
            {
                workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + re.getPrecisionLoss());
                result = transformation.value(new Hypergeometric2F1Helper(false));  // Retry once if there is unexpected precision loss
            }
        }
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    private static long precision(Apcomplex[] z0, Apcomplex[] z1, Apcomplex z2)
    {
        Apcomplex[] z = Stream.concat(Stream.concat(Arrays.stream(z0), Arrays.stream(z1)), Stream.of(z2)).toArray(Apcomplex[]::new);
        return precision(z);
    }

    private static long precision(Apcomplex... z)
    {
        return Arrays.stream(z).mapToLong(Apcomplex::precision).min().getAsLong();
    }

    private Apcomplex checkResult()
    {
        Apfloat minNonPositiveIntegerA = maxNonPositiveInteger(a),
                minNonPositiveIntegerB = maxNonPositiveInteger(b);
        if (minNonPositiveIntegerB != null && (minNonPositiveIntegerA == null || minNonPositiveIntegerA.compareTo(minNonPositiveIntegerB) < 0))
        {
            throw new ArithmeticException("Division by zero");
        }
        if (z.isZero())
        {
            return new Apfloat(1, targetPrecision, radix);
        }
        if (targetPrecision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate hypergeometric function to infinite precision");
        }
        if (minNonPositiveIntegerA != null)
        {
            // Result is a polynomial, we should evaluate here as e.g. a transformation of 2F1 won't work if c is a non-positive integer
            Apcomplex result = evaluate(a, b, z);
            return ApfloatHelper.limitPrecision(result, targetPrecision);
        }
        return null;
    }

    public static Apfloat maxNonPositiveInteger(Apcomplex... a)
    {
        return Arrays.stream(a).filter(Apcomplex::isInteger).map(Apcomplex::real).filter(x -> x.signum() <= 0).reduce(ApfloatMath::max).orElse(null);
    }

    // It could be that the terms initially grow for a long time, until they start getting smaller
    // Also it could be that the sum similarly grows first, but then starts also getting significantly smaller (by many orders of magnitude) -> this causes a precision loss, which needs to be detected, and handled by retrying with increased precision
    private Apcomplex evaluate(Apcomplex[] a, Apcomplex[] b, Apcomplex z)
    {
        Apcomplex[] aOrig = a.clone(),
                    bOrig = b.clone();
        Apcomplex s;
        Apint minN = ApintMath.max(one, Stream.concat(Arrays.stream(a), Arrays.stream(b)).map(Apcomplex::real).reduce(ApfloatMath::min).get().truncate().negate()).add(one);
        long precisionLoss = 0,
             extraPrecision,
             extendedPrecision = ApfloatHelper.extendPrecision(workingPrecision, minN.scale()); // Estimate for accumulated round-off error precision due to repeated multiplication only (not scale based digit loss, see below)
        boolean divergentSeries = a.length - b.length > 1;

        ensurePrecision(a, a, extendedPrecision);
        ensurePrecision(b, b, extendedPrecision);
        z = ApfloatHelper.ensurePrecision(z, extendedPrecision);

        do
        {
            long maxSScale = 1; // Scale of 1, the initial s
            Apint i = zero;
            Apcomplex numerator = one,
                      denominator = one,
                      o = null,
                      t = null;
            boolean minIterations;

            extraPrecision = precisionLoss;
            s = one;

            do
            {
                minIterations = i.compareTo(minN) <= 0;
                if (divergentSeries && !minIterations)
                {
                    checkDivergence(o, t);
                }
                i = i.add(one);
                for (int j = 0; j < a.length; j++)
                {
                    numerator = numerator.multiply(a[j]);
                    a[j] = a[j].add(one);
                }
                if (numerator.isZero())
                {
                    return s;   // It was a polynomial
                }
                numerator = numerator.multiply(z);
                for (int j = 0; j < b.length; j++)
                {
                    denominator = denominator.multiply(b[j]);
                    b[j] = b[j].add(one);
                }
                denominator = denominator.multiply(i);
                o = t;
                t = numerator.divide(denominator);
                s = s.add(t);
                maxSScale = Math.max(maxSScale, s.scale());
            } while (minIterations || s.isZero() || s.scale() - t.scale() <= workingPrecision);  // Subtraction might overflow

            precisionLoss = (s.isZero() ? extendedPrecision : maxSScale - s.scale()); // Loss due to scale of s reduced from its peak (loss off most significant digits)
            if (workingPrecision - s.precision() > 1)  // Often the precision is reduced by 1
            {
                precisionLoss = Util.ifFinite(precisionLoss, precisionLoss + workingPrecision - s.precision()); // Loss due to accumulation (loss off least significant digits)
            }
            if (precisionLoss > extraPrecision)
            {
                extendedPrecision = Util.ifFinite(workingPrecision, workingPrecision + precisionLoss);
                ensurePrecision(aOrig, a, extendedPrecision);
                ensurePrecision(bOrig, b, extendedPrecision);
                z = ApfloatHelper.ensurePrecision(z, extendedPrecision);
            }
        } while (precisionLoss > extraPrecision);
        return s;
    }

    private void checkDivergence(Apcomplex old, Apcomplex term)
    {
        // Check if the divergent series reached the smallest term already, even though the required precision was not reached
        if (old != null && abs(old).compareTo(abs(term)) < 0)
        {
            throw new NotConvergingException();
        }
    }

    // See: https://def.fe.up.pt/pipermail/maxima-discuss/2006.txt "Methods for numerically difficult cases of 2F1(a,b;c|z)", alternative algorithm by Bill Gosper
    private Apcomplex alternative(Apcomplex a, Apcomplex b, Apcomplex c, Apcomplex z)
    {
        if (c.real().signum() <= 0)
        {
            Apint cRounded = RoundingHelper.roundToInteger(c.real(), RoundingMode.HALF_EVEN).truncate();
            long digitLoss = -c.subtract(cRounded).scale();
            if (digitLoss > 0)
            {
                workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
                a = ensurePrecision(a);
                b = ensurePrecision(b);
                c = ensurePrecision(c);
                z = ensurePrecision(z);
            }
        }

        Apint two = new Apint(2, radix),
              four = new Apint(4, radix),
              k = zero;
        Apcomplex d = zero,
                  e = one,
                  f = zero,
                  z1 = ensurePrecision(one.subtract(z)),
                  z12 = two.multiply(z1),
                  z2 = ensurePrecision(z.subtract(two)),
                  abz = a.multiply(b).multiply(z),
                  c2 = c.divide(two),
                  c12 = ensurePrecision(c.add(one)).divide(two),
                  cba = ensurePrecision(c.subtract(b).subtract(a));

        assert (c2.real().signum() > 0 || c2.imag().signum() != 0 || !c2.isInteger());  // c2 should not be rounded to negative integer

        do
        {
            Apcomplex kc2 = k.add(c2),
                      kakbz = k.add(a).multiply(k.add(b)).multiply(z),
                      divisor = one.divide(four.multiply(k.add(one)).multiply(kc2).multiply(k.add(c12))),
                      d1 = kakbz.multiply(e.subtract(k.add(cba).multiply(d).multiply(z).divide(z1))).multiply(divisor),
                      e1 = kakbz.multiply(abz.multiply(d).divide(z1).add(k.add(c).multiply(e))).multiply(divisor),
                      f1 = f.subtract(d.multiply(k.multiply(cba.multiply(z).add(k.multiply(z2)).subtract(c)).subtract(abz)).divide(kc2.multiply(z12))).add(e);
            d = ensurePrecision(d1);
            e = ensurePrecision(e1);
            f = ensurePrecision(f1);
            k = k.add(one);
        } while (d.scale() >= -workingPrecision || e.scale() >= -workingPrecision);
        return f;
    }

    private Apfloat offset(long scale)
    {
        Apfloat offset = scale(new Apfloat("0.1", workingPrecision, radix), scale);
        return offset;
    }

    private Apfloat adjustOffset(Apfloat x, Apfloat offset)
    {
        if (x.scale() <= offset.scale())
        {
            return x;
        }
        return x.precision(Apfloat.INFINITE).add(offset).subtract(offset);
    }

    private Apcomplex ensurePrecision(Apcomplex z)
    {
        return ApfloatHelper.ensurePrecision(z, workingPrecision);
    }

    private void ensurePrecision(Apcomplex[] src, Apcomplex[] dest, long extendedPrecision)
    {
        for (int i = 0; i < dest.length; i++)
        {
            dest[i] = ApfloatHelper.ensurePrecision(src[i], extendedPrecision);
        }
    }

    private Apcomplex ensureGammaPrecision(Apcomplex z)
    {
        return ApfloatHelper.ensureGammaPrecision(z, workingPrecision);
    }

    private Apcomplex result(Apcomplex z)
    {
        return (z == null ? z : ApfloatHelper.reducePrecision(z, extraPrecision));
    }

    private long targetPrecision,
                 extraPrecision,
                 workingPrecision;
    private int radix;
    private Apcomplex[] a,
                        b;
    private Apcomplex z;
    private Apint one,
                  zero;
}
