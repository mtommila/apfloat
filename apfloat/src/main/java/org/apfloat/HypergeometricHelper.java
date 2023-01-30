/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
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

import java.util.Arrays;
import java.util.stream.Stream;

import org.apfloat.spi.Util;

import static java.util.Comparator.comparing;

import static org.apfloat.ApcomplexMath.abs;
import static org.apfloat.ApcomplexMath.gamma;
import static org.apfloat.ApcomplexMath.pow;
import static org.apfloat.ApcomplexMath.sin;
import static org.apfloat.ApcomplexMath.ulp;
import static org.apfloat.ApfloatMath.max;
import static org.apfloat.ApfloatMath.pi;
import static org.apfloat.ApfloatMath.scale;

/**
 * Helper class for hypergeometric functions.
 *
 * @since 1.11.0
 * @version 1.11.0
 * @author Mikko Tommila
 */

class HypergeometricHelper
{
    // See: Computing hypergeometric functions rigorously by Fredrik Johansson, https://arxiv.org/pdf/1606.06977.pdf

    private class Hypergeometric2F1Helper
    {
        public Hypergeometric2F1Helper()
        {
            this.a = HypergeometricHelper.this.a[0];
            this.b = HypergeometricHelper.this.a[1];
            this.c = HypergeometricHelper.this.b[0];
            this.z = HypergeometricHelper.this.z;
            this.precision = HypergeometricHelper.this.precision;
            this.radix = HypergeometricHelper.this.radix;
        }

        public void ensurePrecision(long precision)
        {
            this.precision = precision;
            this.a = ApfloatHelper.ensurePrecision(this.a, precision);
            this.b = ApfloatHelper.ensurePrecision(this.b, precision);
            this.c = ApfloatHelper.ensurePrecision(this.c, precision);
            this.z = ApfloatHelper.ensurePrecision(this.z, precision);
        }

        public Apcomplex evaluate(Apcomplex[] a, Apcomplex[] b, Apcomplex z, Apint minN)
        {
            return HypergeometricHelper.this.evaluate(a, b, z, minN);
        }

        public long precision;
        public int radix;
        public Apcomplex a,
                         b,
                         c,
                         z;
    }

    private static enum Transformation
    {
        T0 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return true;
            }

            @Override
            public Apint polynomialTerms(Apcomplex a, Apcomplex b, Apcomplex c)
            {
                return minNegativeIntegerNegated(a, b);
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
                return evaluate(helper, a, b, c, z);
            }
        },
        T1 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return !z.equals(Apint.ONES[z.radix()]);
            }

            @Override
            public Apint polynomialTerms(Apcomplex a, Apcomplex b, Apcomplex c)
            {
                return minNegativeIntegerNegated(c.subtract(a), c.subtract(b));
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
                Apint one = Apint.ONES[helper.radix];
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z,
                          z1 = one.subtract(z),
                          s = c.subtract(a),
                          t = c.subtract(b);
                if (s.isInteger() && s.real().signum() <= 0 && (!t.isInteger() || t.real().signum() > 0 || s.real().compareTo(t.real()) >= 0))
                {
                    return ApcomplexMath.pow(z1, b.negate()).multiply(evaluate(helper, s, b, c, z(z)));
                }
                return ApcomplexMath.pow(z1, a.negate()).multiply(evaluate(helper, a, t, c, z(z)));
            }
        },
        T2 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return z.real().signum() != 0 || z.imag().signum() != 0;
            }

            @Override
            public Apint polynomialTerms(Apcomplex a, Apcomplex b, Apcomplex c)
            {
                return null;
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
                adjustEqualAB(helper);
                Apint one = Apint.ONES[helper.radix];
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return transform(helper, b.subtract(a), c, z.negate(), a.negate(), b, c.subtract(a), a, a.subtract(c).add(one), a.subtract(b).add(one), z.negate(), b.negate(), one, one, a, c.subtract(b), b, b.subtract(c).add(one), b.subtract(a).add(one), z(z));
            }
        },
        T3 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return !z.equals(Apint.ONES[z.radix()]);
            }

            @Override
            public Apint polynomialTerms(Apcomplex a, Apcomplex b, Apcomplex c)
            {
                return null;
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
                adjustEqualAB(helper);
                Apint one = Apint.ONES[helper.radix];
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return transform(helper, b.subtract(a), c, one.subtract(z), a.negate(), b, c.subtract(a), a, c.subtract(b), a.subtract(b).add(one), one.subtract(z), b.negate(), one, one, a, c.subtract(b), b, c.subtract(a), b.subtract(a).add(one), z(z));
            }
        },
        T4 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return true;
            }

            @Override
            public Apint polynomialTerms(Apcomplex a, Apcomplex b, Apcomplex c)
            {
                return null;
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
                adjustEqualCAB(helper);
                Apint one = Apint.ONES[helper.radix];
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return transform(helper, c.subtract(b).subtract(a), c, one, one, c.subtract(a), c.subtract(b), a, b, a.add(b).subtract(c).add(one), one.subtract(z), c.subtract(a).subtract(b), one, one, a, b, c.subtract(a), c.subtract(b), c.subtract(a).subtract(b).add(one), z(z));
            }
        },
        T5 {
            @Override
            public boolean isApplicable(Apcomplex z)
            {
                return z.real().signum() != 0 || z.imag().signum() != 0;
            }

            @Override
            public Apint polynomialTerms(Apcomplex a, Apcomplex b, Apcomplex c)
            {
                return null;
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
                adjustEqualCAB(helper);
                Apint one = Apint.ONES[helper.radix];
                Apcomplex a = helper.a,
                          b = helper.b,
                          c = helper.c,
                          z = helper.z;
                return transform(helper, c.subtract(b).subtract(a), c, z, a.negate(), c.subtract(a), c.subtract(b), a, a.subtract(c).add(one), a.add(b).subtract(c).add(one), one.subtract(z), c.subtract(a).subtract(b), z, a.subtract(c), a, b, c.subtract(a), one.subtract(a), c.subtract(a).subtract(b).add(one), z(z));
            }
        };

        public abstract boolean isApplicable(Apcomplex z);

        public abstract Apint polynomialTerms(Apcomplex a, Apcomplex b, Apcomplex c);

        public abstract Apcomplex z(Apcomplex z);

        public abstract Apcomplex value(Hypergeometric2F1Helper helper);

        private static Apint minNegativeIntegerNegated(Apcomplex a, Apcomplex b)
        {
            return Stream.of(a, b).filter(Apcomplex::isInteger).map(Apcomplex::real).filter(x -> x.signum() <= 0).map(Apfloat::negate).min(Apfloat::compareTo).map(Apfloat::truncate).orElse(null);
        }

        private static void adjustEqualAB(Hypergeometric2F1Helper helper)
        {
            Apcomplex a = helper.a,
                      b = helper.b;
            long equalDigits;
            if (a.equals(b))
            {
                helper.a = ApfloatHelper.extendPrecision(a, 1).add(scale(max(ulp(a), ulp(b)), -1));
                equalDigits = helper.precision;
            }
            else
            {
                equalDigits = a.equalDigits(b);
            }
            if (equalDigits > 0)
            {
                long precision = Util.ifFinite(equalDigits, helper.precision + equalDigits);
                helper.ensurePrecision(precision);
            }
        }

        private static void adjustEqualCAB(Hypergeometric2F1Helper helper)
        {
            Apcomplex a = helper.a,
                      b = helper.b,
                      c = helper.c,
                      ab = a.add(b);
            long equalDigits;
            if (c.equals(ab))
            {
                helper.c = ApfloatHelper.extendPrecision(c, 1).add(scale(max(ulp(ab), ulp(c)), 1));
                equalDigits = helper.precision;
            }
            else
            {
                equalDigits = c.equalDigits(ab);
            }
            if (equalDigits > 0)
            {
                long precision = Util.ifFinite(equalDigits, helper.precision + equalDigits);
                helper.ensurePrecision(precision);
            }
        }

        private static Apcomplex transform(Hypergeometric2F1Helper helper, Apcomplex s, Apcomplex c, Apcomplex base1, Apcomplex exp1, Apcomplex g1, Apcomplex g2, Apcomplex a1, Apcomplex b1, Apcomplex c1, Apcomplex base2, Apcomplex exp2, Apcomplex base3, Apcomplex exp3, Apcomplex g3, Apcomplex g4, Apcomplex a2, Apcomplex b2, Apcomplex c2, Apcomplex z)
        {
            Apfloat pi = pi(helper.precision, helper.radix);
            return gamma(c).multiply(pi).divide(sin(pi.multiply(s))).multiply(pow(base1, exp1).divide(gamma(g1).multiply(gamma(g2)).multiply(gamma(c1))).multiply(evaluate(helper, a1, b1, c1, z)).subtract(pow(base2, exp2).multiply(pow(base3, exp3)).divide(gamma(g3).multiply(gamma(g4)).multiply(gamma(c2))).multiply(evaluate(helper, a2, b2, c2, z))));
        }

        private static Apcomplex evaluate(Hypergeometric2F1Helper helper, Apcomplex a, Apcomplex b, Apcomplex c, Apcomplex z)
        {
            int radix = helper.radix;
            Apint one = Apint.ONES[radix];
            Apint minN = ApintMath.max(Apint.ONES[radix], c.real().truncate().negate()).add(one);

            return helper.evaluate(new Apcomplex[] { a, b }, new Apcomplex[] { c }, z, minN);
        }
    }

    /**
     * Helper for the generalized hypergeometric function <i><sub>p</sub>F<sub>q</sub></i>.<p>
     *
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @param a The first argument.
     * @param b The second argument.
     * @param z The third argument.
     */

    public HypergeometricHelper(Apcomplex[] a, Apcomplex[] b, Apcomplex z)
    {
        this.a = a;
        this.b = b;
        this.z = z;
        this.precision = precision(a, b, z);
        this.radix = z.radix();
    }

    /**
     * Generalized hypergeometric function <i><sub>p</sub>F<sub>q</sub></i>.<p>
     *
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the function.
     *
     * @return <i><sub>p</sub>F<sub>q</sub>(a<sub>1</sub>, …, a<sub>p</sub>; b<sub>1</sub>, …, b<sub>q</sub>; z)</i>
     *
     * @throws ArithmeticException If the series does not converge.
     */

    // Does not work in the general case for all z
    public Apcomplex hypergeometricPFQ()
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex result = checkResult();
        if (result != null)
        {
            return result;
        }
        if (a.length == 2 && b.length == 1)
        {
            return hypergeometric2F1(a[0], a[1], b[0], z);
        }
        if (a.length > b.length + 1L)
        {
            if (z.real().signum() == 0 && z.imag().signum() == 0)
            {
                return new Apfloat(1, precision, radix);
            }
            throw new ArithmeticException("Series does not converge");
        }
        if (a.length == 0 && b.length == 0)
        {
            return ApcomplexMath.exp(z);
        }
        Apint one = Apint.ONES[radix];
        if (a.length == 1 && b.length == 0)
        {
            return ApcomplexMath.pow(one.subtract(z), a[0].negate());
        }
        assert (b.length > 0);
        Apint minN = ApintMath.max(Apint.ONES[radix], Arrays.stream(b).map(Apcomplex::real).reduce(ApfloatMath::min).get().truncate().negate()).add(one);

        return evaluate(a, b, z, minN);
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
        // If transformed z is too big (i.e. near exp(i pi / 3)) then use alternative algorithm
        // If transformation requires division by possible zero (e.g. b - a), then alter the parameters slightly to be nonzero, e.g. by largest ulp of a or b, ensure the required working precision (or calculate average of two calls with +-ulp)
        // Real version is real if z <= 1 (may be infinite if z = 1 depending on a, b and c as said above)
        Apint one = Apint.ONES[radix];
        if (z.equals(one))
        {
            if (a.real().add(b.real()).subtract(c.real()).signum() >= 0)
            {
                throw new ArithmeticException("Does not converge");
            }
            Apcomplex s = c.subtract(a),
                      t = c.subtract(b);
            if (s.isInteger() && s.real().signum() <= 0 || t.isInteger() && t.real().signum() <= 0)
            {
                return Apint.ZEROS[radix];
            }
            return gamma(c).multiply(gamma(s.subtract(b))).divide(gamma(s).multiply(gamma(t)));
        }
        Apcomplex zDoublePrecision = z.precision(ApfloatHelper.getDoublePrecision(radix));
        Transformation transformation = Arrays.stream(Transformation.values()).filter(t -> t.isApplicable(z)).min(comparing(t -> abs(t.z(zDoublePrecision)))).get();
        if (abs(transformation.z(zDoublePrecision)).doubleValue() > 0.8) // TODO does it make any sense to check for polynomial? The polynomial could potentially be of huge degree anyways
        {
            // Use alternative algorithm as none of the transforms is very good
            return alternative(a, b, c, z);
        }

        return ApfloatHelper.limitPrecision(transformation.value(new Hypergeometric2F1Helper()), precision);
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
        if (z.real().signum() == 0 && z.imag().signum() == 0)
        {
            return new Apfloat(1, precision, radix);
        }
        Apfloat minNonPositiveIntegerA = minNonPositiveInteger(a),
                minNonPositiveIntegerB = minNonPositiveInteger(b);
        if (minNonPositiveIntegerB != null && (minNonPositiveIntegerA == null || minNonPositiveIntegerA.compareTo(minNonPositiveIntegerB) < 0))
        {
            throw new ArithmeticException("Division by zero");
        }
        if (minNonPositiveIntegerA != null)
        {
            // Result is a polynomial
            return evaluate(a, b, z, minNonPositiveIntegerA.truncate().negate());
        }
        return null;
    }

    private static Apfloat minNonPositiveInteger(Apcomplex[] a)
    {
        return Arrays.stream(a).filter(Apcomplex::isInteger).map(Apcomplex::real).filter(x -> x.signum() <= 0).min(Apfloat::compareTo).orElse(null);
    }

    private Apcomplex evaluate(Apcomplex[] a, Apcomplex[] b, Apcomplex z, Apint minN)
    {
        long maxTScale = Long.MIN_VALUE;
        Apint one = Apint.ONES[radix],
              i = Apint.ZEROS[radix];
        Apcomplex numerator = one,
                  denominator = one,
                  s = one,
                  t;
        do
        {
            i = i.add(one);
            for (int j = 0; j < a.length; j++)
            {
                numerator = numerator.multiply(a[j]);
                a[j] = a[j].add(one);
            }
            if (numerator.real().signum() == 0 && numerator.imag().signum() == 0)
            {
                break;
            }
            numerator = numerator.multiply(z);
            for (int j = 0; j < b.length; j++)
            {
                denominator = denominator.multiply(b[j]);
                b[j] = b[j].add(one);
            }
            denominator = denominator.multiply(i);
            t = numerator.divide(denominator);
            s = s.add(t);
            maxTScale = Math.max(maxTScale, t.scale());
        } while (i.compareTo(minN) <= 0 || t.scale() >= maxTScale - precision); // Subtraction might overflow
        return s;
    }

    // See: https://def.fe.up.pt/pipermail/maxima-discuss/2006.txt "Methods for numerically difficult cases of 2F1(a,b;c|z)", alternative algorithm by Bill Gosper
    private Apcomplex alternative(Apcomplex a, Apcomplex b, Apcomplex c, Apcomplex z)
    {
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, radix),
              four = new Apint(4, radix),
              k = zero;
        Apcomplex d = zero,
                  e = one,
                  f = zero,
                  z1 = one.subtract(z),
                  z12 = two.multiply(z1),
                  z2 = z.subtract(two),
                  abz = a.multiply(b).multiply(z),
                  c2 = c.divide(two),
                  c12 = c.add(one).divide(two),
                  cba = c.subtract(b).subtract(a);
        do
        {
            Apcomplex kc2 = k.add(c2),
                      kakbz = k.add(a).multiply(k.add(b)).multiply(z),
                      divisor = one.divide(four.multiply(k.add(one)).multiply(kc2).multiply(k.add(c12))),
                      d1 = kakbz.multiply(e.subtract(k.add(cba).multiply(d).multiply(z).divide(z1))).multiply(divisor),
                      e1 = kakbz.multiply(abz.multiply(d).divide(z1).add(k.add(c).multiply(e))).multiply(divisor),
                      f1 = f.subtract(d.multiply(k.multiply(cba.multiply(z).add(k.multiply(z2)).subtract(c)).subtract(abz)).divide(kc2.multiply(z12))).add(e);
            d = d1;
            e = e1;
            f = f1;
            k = k.add(one);
        } while (d.scale() >= -precision || e.scale() >= -precision);
        return f;
    }

    private long precision;
    private int radix;
    private Apcomplex[] a,
                        b;
    private Apcomplex z;
}
