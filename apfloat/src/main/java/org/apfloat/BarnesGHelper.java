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

import java.util.Iterator;

import static org.apfloat.ApcomplexMath.exp;
import static org.apfloat.ApcomplexMath.log;
import static org.apfloat.ApcomplexMath.logGamma;
import static org.apfloat.ApcomplexMath.polylog;
import static org.apfloat.ApcomplexMath.pow;
import static org.apfloat.ApcomplexMath.zetaPrime;

/**
 * Helper class for the Barnes G function.
 *
 * @since 1.16.0
 * @version 1.16.0
 * @author Mikko Tommila
 */

class BarnesGHelper
{
    public static Apcomplex barnesG(Apcomplex z)
        throws ApfloatRuntimeException
    {
        int radix = z.radix();
        if (z.isInteger() && z.real().signum() <= 0)
        {
            return Apcomplex.ZEROS[radix];
        }

        if (z.real().signum() < 0)
        {
            // Use reflection formula
            // barnesG(1 + z) = barnesG(1 - z) exp((i (pi^2 (6 z^2 - 1) + 6 polylog(2, exp(2 i pi z)))) / (12 pi)) (2 pi / (1 - exp(2 i pi z)))^z
            long targetPrecision = z.precision(),
                 extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
                 precision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision);
            Apfloat zero = Apint.ZEROS[radix],
                    one = Apint.ONES[radix],
                    two = new Apint(2, radix),
                    six = new Apint(6, radix),
                    twelve = new Apfloat(12, precision, radix),
                    pi = ApfloatMath.pi(precision, radix),
                    pi2 = pi.multiply(two);
            z = ApfloatHelper.ensurePrecision(z.subtract(one), precision);
            Apcomplex i = new Apcomplex(zero, one),
                      expi2piz = ApfloatHelper.limitPrecision(exp(i.multiply(pi2).multiply(z)), precision),  // Limit precision in case z is even integer
                      expi2piz1 = ApfloatHelper.ensurePrecision(one.subtract(expi2piz), precision),
                      z1 = ApfloatHelper.ensurePrecision(one.subtract(z), precision),
                      z61 = ApfloatHelper.ensurePrecision(six.multiply(z).multiply(z).subtract(one), precision),
                      result = barnesG(z1).multiply(exp(pi.multiply(pi).multiply(z61).add(six.multiply(polylog(two, expi2piz))).multiply(i).divide(twelve.multiply(pi)))).multiply(pow(pi2.divide(expi2piz1), z));
            return ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        if (z.isInteger() && z.real().compareTo(new Apint(1826012431, radix)) <= 0) // Approximate value that is at the borderline of overflow (in max radix)
        {
            long precision = z.precision();
            if (z.real().compareTo(new Apint(3, radix)) <= 0)
            {
                return Apfloat.ONES[radix].precision(precision);
            }
            if (precision >= z.real().longValueExact() / 1500)  // Extremely rough estimate of computational effort
            {
                return barnesG(z.real().intValueExact(), precision, radix);
            }
        }

        return barnesG(z, false);
    }

    public static Apcomplex logBarnesG(Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isInteger() && z.real().signum() <= 0)
        {
            throw new ApfloatArithmeticException("Log Barnes G of nonpositive integer", "logG.ofNonpositiveInteger");
        }

        int radix = z.radix();
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, radix),
              three = new Apint(3, radix);
        if (z.equals(one) || z.equals(two) || z.equals(three))
        {
            return zero;
        }

        long targetPrecision = z.precision(),
             extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision);
        Apfloat pi = ApfloatMath.pi(precision, radix),
                pi2 = pi.multiply(two);
        Apcomplex result;
        if (z.real().signum() < 0)
        {
            // Use reflection formula (https://en.wikipedia.org/wiki/Barnes_G-function#Reflection_formula)
            // log(barnesG(1 - z)) = log(barnesG(1 + z)) - z log(2 pi) + integrate(pi x cot(pi x), {x, 0, z})
            // The integral can be computed in closed form using log and polylog
            // log(barnesG(1 + z)) = log(barnesG(1 - z)) + z log(2 pi) + 1 / 12 i pi (6 z^2 - 1) - z log(1 - exp(2 i pi z)) + (i polylog(2, exp(2 i pi z))) / (2 pi)
            boolean conj = false;
            if (z.imag().signum() < 0)  // Not entirely sure why this is sometimes required to get the correct imaginary part
            {
                z = z.conj();
                conj  = true;
            }
            Apfloat six = new Apint(6, radix),
                    twelve = new Apfloat(12, precision, radix);
            z = ApfloatHelper.ensurePrecision(z.subtract(one), precision);
            Apcomplex i = new Apcomplex(zero, one),
                      expi2piz = ApfloatHelper.limitPrecision(exp(i.multiply(pi2).multiply(z)), precision), // Limit precision in case z is even near-integer
                      expi2piz1 = ApfloatHelper.ensurePrecision(one.subtract(expi2piz), precision),
                      z1 = ApfloatHelper.ensurePrecision(one.subtract(z), precision),
                      z61 = ApfloatHelper.ensurePrecision(six.multiply(z).multiply(z).subtract(one), precision);
            result = logBarnesG(z1).add(z.multiply(log(pi2))).add(i.multiply(pi).multiply(z61).divide(twelve)).subtract(z.multiply(log(expi2piz1))).add(i.multiply(polylog(two, expi2piz)).divide(pi2));
            result = ApfloatHelper.reducePrecision(result, extraPrecision);
            result = (conj ? result.conj() : result);
        }
        else if (z.scale() > 1 && z.scale() >= z.precision() / 10)
        {
            // Use asymptotic expansion for large z (with relatively small precision) https://en.wikipedia.org/wiki/Barnes_G-function#Asymptotic_expansion
            // logG(z + 1) = z^2 / 2 log z - 3z^2 / 4 + z / 2 log (2 pi) - 1 / 12 log z + 1 / 12 - log A + sum(B_(2k + 2) / (4k (k + 1) z^(2k)), {k, 1, N})
            long N = z.precision() / z.scale() + 1;
            Apfloat A = ApfloatMath.glaisher(precision, radix),
                    four = new Apint(4, radix),
                    twelve = new Apfloat(12, precision, radix);
            z = ApfloatHelper.ensurePrecision(z.subtract(one), precision);
            Apcomplex z2 = z.multiply(z),
                      logz = log(z),
                      zp = one,
                      sum = zero;
            Iterator<Apfloat> bernoullis2 = BernoulliHelper.bernoullis2(N + 1, precision, radix);
            bernoullis2.next();
            for (long k = 1; k <= N; k++)
            {
                zp = zp.multiply(z2);
                Apcomplex term = bernoullis2.next().divide(new Apint(4 * k * (k + 1), radix).multiply(zp));
                sum = sum.add(term);
            }
            result = z2.divide(two).multiply(logz).subtract(three.multiply(z2).divide(four)).add(z.divide(two).multiply(log(pi2))).subtract(logz.divide(twelve)).add(one.divide(twelve)).subtract(log(A)).add(sum);
            result = ApfloatHelper.reducePrecision(result, extraPrecision);
        }
        else
        {
            result = barnesG(z, true);
        }
        return result;
    }

    private static Apcomplex barnesG(Apcomplex z, boolean isLog)
        throws ArithmeticException, ApfloatRuntimeException
    {
        // See for example V. S. Adamchik "On the Barnes function" https://viterbi-web.usc.edu/~adamchik/articles/issac01/issac01.pdf
        // logG(z) = (z - 1) logGamma(z) + 1/12 - log(A) - zeta'(-1, z)
        int radix = z.radix();
        long targetPrecision = z.precision(),
             extraPrecision = ApfloatHelper.getSmallExtraPrecision(radix),
             precision = ApfloatHelper.extendPrecision(targetPrecision, extraPrecision);
        z = ApfloatHelper.ensurePrecision(z, precision);
        Apfloat A = ApfloatMath.glaisher(precision, radix),
                one = Apint.ONES[radix],
                oneTwelfth = one.divide(new Apfloat(12, precision, radix));
        Apcomplex z1 = ApfloatHelper.ensurePrecision(z.subtract(one), precision),
                  result = (isLog ? z1.multiply(logGamma(z)).add(oneTwelfth).subtract(log(A)).subtract(zetaPrime(one.negate(), z))
                                  : exp(z1.multiply(logGamma(z)).add(oneTwelfth).subtract(zetaPrime(one.negate(), z))).divide(A));
        return ApfloatHelper.reducePrecision(result, extraPrecision);
    }

    static Apfloat barnesG(int n, long precision, int radix)
    {
        long[] p = new long[n - 1];
        for (int i = 2; i < n - 1; i++)
        {
            p[i] = n - i - 1;
        }
        int maxp = (int) Math.sqrt(n - 2);
        for (int i = 2; i <= maxp; i++)
        {
            if (p[i] != 0)
            {
                for (int j = 2 * i; j < n - 1; j += i)
                {
                    int f = j;
                    while (f % i == 0)
                    {
                        p[i] += p[j];
                        f /= i;
                    }
                    p[f] += p[j];
                    p[j] = 0;
                }
            }
        }
        return product(p, 2, n - 2, precision, radix);
    }

    private static Apfloat product(long[] a, int n, int m, long precision, int radix)
    {
        if (n == m)
        {
            return ApfloatMath.pow(new Apfloat(n, precision, radix), a[n]);
        }
        int k = (n + m) >>> 1;
        return product(a, n, k, precision, radix).multiply(product(a, k + 1, m, precision, radix));
    }
}
