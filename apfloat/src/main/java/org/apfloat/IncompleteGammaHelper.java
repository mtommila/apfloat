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
package org.apfloat;

import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.stream.Stream;

/**
 * Helper class for the incomplete gamma function.
 *
 * @since 1.10.0
 * @version 1.10.0
 * @author Mikko Tommila
 */

class IncompleteGammaHelper
{
    // See https://hal.archives-ouvertes.fr/hal-01329669/document
    // Fast and accurate evaluation of a generalized incomplete gamma function Rémy Abergel, Lionel Moisan

    private static class Sequence
    {
        public Sequence(LongFunction<Apcomplex> a, LongFunction<Apcomplex> b)
        {
            this.a = a;
            this.b = b;
        }

        public Apcomplex a(long n)
        {
            return this.a.apply(n);
        }

        public Apcomplex b(long n)
        {
            return this.b.apply(n);
        }

        private LongFunction<Apcomplex> a;
        private LongFunction<Apcomplex> b;
    }

    public static Apcomplex gamma(Apcomplex a, Apcomplex z)
    {
        if (z.real().signum() == 0 && z.imag().signum() == 0)
        {
            if (a.real().signum() <= 0)
            {
                throw new ArithmeticException("Upper gamma with first argument real part nonpositive and second argment zero");
            }
            return ApcomplexMath.gamma(a);
        }
        return upperGamma(a, z);
    }

    public static Apcomplex gamma(Apcomplex a, Apcomplex z0, Apcomplex z1)
    {
        if (a.real().signum() == 0 && a.imag().signum() == 0 &&
            z0.real().signum() == 0 && z0.imag().signum() == 0 &&
            z1.real().signum() == 0 && z1.imag().signum() == 0)
        {
            throw new ArithmeticException("Gamma of zero");
        }
        if (z0.equals(z1))
        {
            return Apcomplex.ZERO;
        }

        if (z0.real().signum() == 0 && z0.imag().signum() == 0)
        {
            return lowerGamma(a, z1);
        }
        if (z1.real().signum() == 0 && z1.imag().signum() == 0)
        {
            return lowerGamma(a, z0).negate();
        }

        if (useLowerGamma(a, z0) && useLowerGamma(a, z1))
        {
            // More efficient algorithm in this case
            return lowerGamma(a, z1).subtract(lowerGamma(a, z0));
        }

        return upperGamma(a, z0).subtract(upperGamma(a, z1));
    }

    private static Apcomplex upperGamma(Apcomplex a, Apcomplex z)
    {
        if (a.isInteger() && a.real().signum() <= 0)
        {
            // Note that this transformation may be extremely slow if n is large
            // gamma(-n,z) = (-1)^n/n! gamma(0,z) - e^-z sum[z^(k-n-1)/(-n)_k,{k,1,n}]
            // See https://functions.wolfram.com/GammaBetaErf/Gamma2/17/02/01/
            long n = a.longValueExact(); // If this overflows then the factorial would overflow anyways
            return upperGamma(n, z);
        }
        if (useLowerGamma(a, z))
        {
            // The algorithm for upper gamma would not converge well
            return ApcomplexMath.gamma(a).subtract(lowerGamma(a, z));
        }

        return upperGammaG(a, z);
    }

    private static Apcomplex lowerGamma(Apcomplex a, Apcomplex z)
    {
        if (a.isInteger() && a.real().signum() <= 0)
        {
            throw new ArithmeticException("Lower gamma with first argument nonpositive integer");
        }
        if (z.scale() <= 0)
        {
            // The series is fastest for small z
            return sum(a, z);
        }
        if (useUpperGamma(a, z))
        {
            // The algorithm for lower gamma would not converge well
            return ApcomplexMath.gamma(a).subtract(upperGamma(a, z));
        }

        return lowerGammaG(a, z);
    }

    private static boolean useLowerGamma(Apcomplex a, Apcomplex z)
    {
        // The continued fraction for upper gamma would not converge well
        return z.scale() < a.scale() || mustUseLowerGamma(z);
    }

    private static boolean mustUseLowerGamma(Apcomplex z)
    {
        // If z is too close to the negative real axis, the upper gamma continued fraction converges poorly
        return (z.real().signum() <= 0 || z.real().scale() < 0) && z.imag().scale() < 0;
    }

    private static boolean useUpperGamma(Apcomplex a, Apcomplex z)
    {
        // The continued fraction for lower gamma would not converge well
        return a.scale() < z.scale() && !mustUseLowerGamma(z);
    }

    private static Apcomplex upperGammaG(Apcomplex a, Apcomplex z)
    {
        Apcomplex g = g(IncompleteGammaHelper::upperGammaSequence, a, z);
        return g;
    }

    private static Apcomplex lowerGammaG(Apcomplex a, Apcomplex z)
    {
        Apcomplex g = g(IncompleteGammaHelper::lowerGammaSequence, a, z);
        return g;
    }

    // Converges well only when |z| > |a|, a can be zero but z cannot
    private static Sequence upperGammaSequence(Apcomplex a, Apcomplex z)
    {
        int radix = z.radix();
        Apfloat one = new Apint(1, radix);
        Apcomplex za = z.subtract(a);
        Sequence s = new Sequence(n -> {
            if (n == 1)
            {
                return one;
            }
            else
            {
                Apint n1 = new Apint(n - 1, radix);
                return n1.multiply(a.subtract(n1));
            }
        }, n -> new Apint(2 * n - 1, radix).add(za));
        return s;
    }

    // Converges best when |z| <= |a|, both a and z must be nonzero and a must not be a negative integer
    private static Sequence lowerGammaSequence(Apcomplex a, Apcomplex z)
    {
        int radix = z.radix();
        Apfloat one = new Apint(1, radix);
        Sequence s = new Sequence(n -> {
            if (n == 1)
            {
                return one;
            }
            else if (n % 2 == 0)
            {
                n /= 2;
                return new Apint(1 - n, radix).subtract(a).multiply(z);
            }
            else
            {
                n /= 2;
                return new Apint(n, radix).multiply(z);
            }
        }, n -> new Apint(n - 1, radix).add(a));
        return s;
    }

    private static Apcomplex g(BiFunction<Apcomplex, Apcomplex, Sequence> s, Apcomplex a, Apcomplex z)
    {
        // Continued fractions sometimes converge strangely with larger input values, try to handle some of these
        long extraPrecision = Apfloat.EXTRA_PRECISION;
        if ((Math.abs(a.real().doubleValue()) > 1e2 || Math.abs(a.imag().doubleValue()) > 1e2) &&
            (Math.abs(z.real().doubleValue()) > 1e1 || Math.abs(z.imag().doubleValue()) > 1e1))
        {
            double minPrecision = Stream.of(a, z).flatMap(v -> Stream.of(v.real(), v.imag())).mapToLong(Apfloat::longValue).map(Math::abs).max().getAsLong() / Math.log10(a.radix());
            extraPrecision = Math.max(extraPrecision, (long) minPrecision - Math.min(a.precision(), z.precision()));
        }
        a = ApfloatHelper.extendPrecision(a, extraPrecision);
        z = ApfloatHelper.extendPrecision(z, extraPrecision);

        Apcomplex f = continuedFraction(s.apply(a, z), z.radix(), Math.min(a.precision(), z.precision()));
        Apcomplex g = f.multiply(ApcomplexMath.exp(a.multiply(ApcomplexMath.log(z)).subtract(z)));

        return ApfloatHelper.reducePrecision(g, extraPrecision);
    }

    // Modified Lentz's method
    private static Apcomplex continuedFraction(Sequence s, int radix, long workingPrecision)
    {
        Apint one = new Apint(1, radix);
        long n = 1;
        Apcomplex an = s.a(n);
        Apcomplex bn = s.b(n);
        Apcomplex dm = tiny(bn, workingPrecision);
        Apcomplex f = an.divide(bn);
        Apcomplex c = an.divide(dm);
        Apcomplex d = one.divide(bn);
        Apcomplex delta;
        do {
            n++;
            an = s.a(n);
            bn = s.b(n);
            an = ApfloatHelper.ensurePrecision(an, workingPrecision);
            bn = ApfloatHelper.ensurePrecision(bn, workingPrecision);
            d = d.multiply(an).add(bn);
            d = ApfloatHelper.ensurePrecision(d, workingPrecision);
            if (d.real().signum() == 0 && d.imag().signum() == 0)
            {
                d = tiny(bn, workingPrecision);
            }
            c = bn.add(an.divide(c));
            c = ApfloatHelper.ensurePrecision(c, workingPrecision);
            if (c.real().signum() == 0 && c.imag().signum() == 0)
            {
                c = tiny(bn, workingPrecision);
            }
            d = one.divide(d);
            delta = c.multiply(d);
            f = f.multiply(delta);
        } while (delta.equalDigits(one) < workingPrecision - Apfloat.EXTRA_PRECISION / 2);  // Due to round-off errors we cannot always reach workingPrecision but slightly less is sufficient
        return f;
    }

    private static Apcomplex tiny(Apcomplex bn, long workingPrecision)
    {
        return ApcomplexMath.scale(ApcomplexMath.ulp(bn), -workingPrecision).precision(Apfloat.INFINITE);
    }

    // Upper gamma of nonpositive integer
    private static Apcomplex upperGamma(long mn, Apcomplex z)
    {
        Apcomplex result = e1(z);   // Same as upperGamma(0, z)
        long n = Math.negateExact(mn);
        if (n > 0)
        {
            long workingPrecision = ApfloatHelper.extendPrecision(z.precision());
            int radix = z.radix();
            result = result.divide(ApfloatMath.factorial(n, workingPrecision, radix));
            if ((n & 1) == 1)
            {
                result = result.negate();
            }
            Apcomplex ez = ApcomplexMath.exp(z.negate());
            z = ApfloatHelper.extendPrecision(z);
            Apcomplex s = ApcomplexMath.pow(z, mn).divide(new Apint(mn, radix)),
                      sum = s;
            for (long k = 2; k <= n; k++)
            {
                mn++;
                s = s.multiply(z).divide(new Apint(mn, radix));
                sum = sum.add(s);
            }
            sum = ApfloatHelper.reducePrecision(sum);
            result = result.subtract(ez.multiply(sum));
        }
        return result;
    }

    private static Apcomplex sum(Apcomplex a, Apcomplex z)
    {
        a = ApfloatHelper.extendPrecision(a);
        z = ApfloatHelper.extendPrecision(z);

        boolean useAlternatingSum = (z.real().signum() >= 0);
        Apcomplex za = ApcomplexMath.pow(z, a);
        if (!useAlternatingSum)
        {
            za = za.multiply(ApcomplexMath.exp(z.negate()));
        }
        long targetPrecision = Math.min(a.precision(), z.precision());
        int radix = z.radix();
        Apcomplex sum = Apcomplex.ZERO;
        Apint one = new Apint(1, radix);
        Apcomplex f = (useAlternatingSum ? one.precision(targetPrecision) : a);
        long n = 0;
        Apcomplex t;
        do
        {
            if (useAlternatingSum)
            {
                Apint nn = new Apint(n, radix);
                Apcomplex an = a.add(nn);
                if (n > 0)
                {
                    za = za.multiply(z);
                    f = f.multiply(nn);
                }
                t = za.divide(f.multiply(an));
                sum = (n & 1) == 0 ? sum.add(t) : sum.subtract(t);
            }
            else
            {
                if (n > 0)
                {
                    a = a.add(one);
                    za = za.multiply(z);
                    f = f.multiply(a);
                }
                t = za.divide(f);
                sum = sum.add(t);
            }
            n++;
        } while (sum.scale() - t.scale() < targetPrecision && !t.equals(Apcomplex.ZERO)); // Also check for underflow of t

        return ApfloatHelper.reducePrecision(sum);
    }

    // Exponential integral for upperGamma(0, z)
    private static Apcomplex e1(Apcomplex z)
    {
        if (z.real().signum() == 0 && z.imag().signum() == 0)
        {
            throw new ArithmeticException("E1 of zero");
        }

        int radix = z.radix();
        Apcomplex result;
        if (z.scale() <= 1)
        {
            // Small value, use series
            long targetPrecision = z.precision();
            Apcomplex mz = ApfloatHelper.extendPrecision(z).negate();
            Apcomplex s = mz,
                      sum = s,
                      t;
            long k = 1;
            do
            {
                k++;
                Apint kk = new Apint(k, radix);
                s = s.multiply(mz).divide(kk);
                t = s.divide(kk);
                sum = sum.add(t);
            } while (sum.scale() - t.scale() < targetPrecision && !t.equals(Apcomplex.ZERO));   // Also check for underflow of t

            result = ApfloatMath.euler(targetPrecision).negate().subtract(ApcomplexMath.log(z)).subtract(ApfloatHelper.reducePrecision(sum));
        }
        else
        {
            // Large value, use continued fraction
            Apcomplex zz = ApfloatHelper.extendPrecision(z);
            long workingPrecision = zz.precision();
            Apfloat one = new Apfloat(1, workingPrecision, radix);
            Sequence sequence = new Sequence(a -> new Apint(a == 1 ? 1 : a / 2, radix), b -> (b & 1) == 0 ? one : zz);
            Apcomplex continuedFraction = continuedFraction(sequence , radix, workingPrecision);

            result = ApcomplexMath.exp(z.negate()).multiply(ApfloatHelper.reducePrecision(continuedFraction));
        }
        return result;
    }
}
