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

import java.util.Arrays;
import java.util.Iterator;
import java.util.OptionalLong;

/**
 * Helper class for Stirling numbers.
 *
 * @since 1.16.0
 * @author Mikko Tommila
 */
class StirlingHelper {

    private StirlingHelper()
    {
    }

    /**
     * Stirling number of the first kind.
     *
     * @param n
     * @param k
     * @param radix
     *
     * @return s(n, k)
     */

    public static Apint stirlingS1(long n, long k, int radix)
    {
        if (n < 0 || k < 0)
        {
            throw new IllegalArgumentException("Arguments must be nonnegative");
        }
        Apint s1;
        if (k == n)
        {
            s1 = Apint.ONES[radix];
        }
        else if (k > n || k == 0)
        {
            s1 = Apint.ZEROS[radix];
        }
        else if (k == 1)
        {
            s1 = ApintMath.factorial(n - 1, radix);
        }
        else if (k == n - 1)
        {
            s1 = ApintMath.binomial(n, 2, radix);
        }
        else if (k == n - 2)
        {
            s1 = ApintMath.binomial(n, 3, radix).multiply(new Apint(3, radix).multiply(new Apint(n, radix)).subtract(new Apint(1, radix))).divide(new Apint(4, radix));
        }
        else if (k == n - 3)
        {
            s1 = ApintMath.binomial(n, 2, radix).multiply(ApintMath.binomial(n, 4, radix));
        }
        else if (k < Integer.MAX_VALUE / 2 - 1)
        {
            // Very rough estimate of computational effort
            long nk = n - k;
            double polynomialCost = n * Math.log(n) / 0.8;
            double explicitCost = Math.pow(nk * Math.log(nk), 3) / 400000;
            if (polynomialCost < explicitCost)
            {
                s1 = stirlingS1s(n, (int) k, radix)[(int) k];
            }
            else
            {
                s1 = stirlingS1explicit(new Apint(n, radix), new Apint(k, radix));
            }
        }
        else
        {
            s1 = stirlingS1explicit(new Apint(n, radix), new Apint(k, radix));
        }
        return ((n - k & 1) == 0 ? s1 : s1.negate());
    }

    /**
     * Stirling numbers of the first kind.
     *
     * @param n
     * @param radix
     *
     * @return Array of s(n, k) of length k + 1 where k is the array index.
     */

    public static Apint[] stirlingS1s(int n, int radix)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("Argument must be nonnegative");
        }
        Apint[] s1s = stirlingS1s(n, n, radix);
        for (int i = n - 1; i >= 0; i -= 2)
        {
            s1s[i] = s1s[i].negate();
        }
        return s1s;
    }

    // Unsigned Stirling numbers of the first kind
    static Apint[] stirlingS1s(long n, int k, int radix)
    {
        assert (n >= 0);
        assert (k >= 0);
        assert (k <= n);
        if (n == 0)
        {
            return new Apint[] { Apint.ONES[radix] };
        }
        return polynomialMultiply(0, n - 1, k, radix);
    }

    private static Apint[] polynomialMultiply(long n, long m, int maxDegree, int radix)
    {
        if (n == m)
        {
            return new Apint[] { new Apint(n, radix), Apint.ONES[radix] };
        }
        long k = n + m >>> 1;
        Apint[] a = polynomialMultiply(n, k, maxDegree, radix),
                b = polynomialMultiply(k + 1, m, maxDegree, radix);
        return polynomialMultiply(a, b, maxDegree);
    }

    private static Apint[] polynomialMultiply(Apint[] a, Apint[] b, int maxDegree)
    {
        int radix = a[0].radix();
        // Kronecker substitution
        long stride = maxScale(a) + maxScale(b) + (long) Math.ceil(Math.log(a.length + b.length) / Math.log(radix) + 0.01);
        Apint x = Splitter.combine(stride, a),
              y = Splitter.combine(stride, b);
        Apint[] result = Splitter.split(stride, x.multiply(y).mod(ApintMath.pow(new Apint(radix, radix), stride * (maxDegree + 1))));
        return result;
    }

    private static long maxScale(Apint[] a)
    {
        return Math.max(1, Arrays.stream(a).mapToLong(Apint::scale).max().getAsLong());
    }

    /**
     * Stirling number of the first kind.
     *
     * @param n
     * @param k
     *
     * @return s(n, k)
     */

    public static Apint stirlingS1(Apint n, Apint k)
    {
        if (n.signum() < 0 || k.signum() < 0)
        {
            throw new IllegalArgumentException("Arguments must be nonnegative");
        }
        int radix = n.radix();
        Apint s1;
        if (k.equals(n))
        {
            s1 = Apint.ONES[radix];
        }
        else if (k.compareTo(n) > 0 || k.signum() == 0)
        {
            s1 = Apint.ZEROS[radix];
        }
        else
        {
            OptionalLong longN = optionalLong(n),
                         longK = optionalLong(k);
            if (longN.isPresent() && longK.isPresent())
            {
                return stirlingS1(longN.getAsLong(), longK.getAsLong(), radix);
            }
            s1 = stirlingS1explicit(n, k);
        }
        Apint two = new Apint(2, radix);
        return (n.mod(two).signum() == 0 ^ k.mod(two).signum() == 0 ? s1.negate() : s1);
    }

    private static OptionalLong optionalLong(Apint i)
    {
        long value = i.longValue();
        return (new Apint(value, i.radix()).equals(i) ? OptionalLong.of(value) : OptionalLong.empty());
    }

    // Unsigned Stirling number of the first kind
    static Apint stirlingS1explicit(Apint n, Apint k)
    {
        assert (n.signum() >= 0);
        assert (k.signum() >= 0);
        assert (k.compareTo(n) < 0);
        int radix = n.radix();
        long nk;
        try
        {
            nk = n.subtract(k).longValueExact();
        }
        catch (ApfloatArithmeticException aae)
        {
            throw new OverflowException("Overflow", "overflow", aae);
        }
        Apint zero = Apint.ZEROS[radix],
              one = Apint.ONES[radix],
              two = new Apint(2, n.radix()),
              n2k = n.add(n).subtract(k),
              k1 = k.subtract(one),
              factor = ApintMath.pow(ApintMath.factorial(nk, radix), 2),    // The algorithm would need to use rational numbers unless we first multiply everything by a factor and then divide by it in the end
              binomial = ApintMath.binomial(n.subtract(one), k1).multiply(ApintMath.binomial(n2k, n)).multiply(factor),
              sum = zero;
        for (Apint j = n; j.compareTo(n2k) <= 0; j = j.add(one))
        {
            Apint jn = j.subtract(n),
                  divisor = ApintMath.factorial(jn.longValueExact(), radix);
            long jk = j.subtract(k).longValueExact();
            boolean odd = n.mod(two).signum() != 0 ^ k.mod(two).signum() != 0;
            for (Apint m = zero; m.compareTo(jn) <= 0; m = m.add(one))
            {
                if (m.signum() > 0)
                {
                    divisor = divisor.multiply(m);
                }
                Apint term = binomial.multiply(ApintMath.pow(m, jk)).divide(divisor);
                Apint jnm = jn.subtract(m);
                if (jnm.signum() != 0)
                {
                    divisor = divisor.divide(jnm);
                }
                sum = (odd ^ m.mod(two).signum() != 0 ? sum.subtract(term) : sum.add(term));
            }
            binomial = binomial.multiply(j).divide(j.subtract(k1)).multiply(n2k.subtract(j)).divide(j.add(one));
        }
        return sum.divide(factor);
    }

    /**
     * Stirling number of the second kind.
     *
     * @param n
     * @param k
     *
     * @return S(n, k)
     */

    public static Apint stirlingS2(Apint n, Apint k)
    {
        long nn, kk;
        try
        {
            nn = n.longValueExact();
            kk = k.longValueExact();
        }
        catch (ApfloatArithmeticException aae)
        {
            throw new OverflowException("Overflow", "overflow", aae);
        }
        return stirlingS2(nn, kk, n.radix());
    }

    /**
     * Stirling number of the second kind.
     *
     * @param n
     * @param k
     * @param radix
     *
     * @return S(n, k)
     */

    public static Apint stirlingS2(long n, long k, int radix)
    {
        if (n < 0 || k < 0)
        {
            throw new IllegalArgumentException("Arguments must be nonnegative");
        }
        if (k == n)
        {
            return Apint.ONES[radix];
        }
        if (k > n || k == 0)
        {
            return Apint.ZEROS[radix];
        }
        if (k == 1)
        {
            return Apint.ONES[radix];
        }
        if (k == n - 1)
        {
            return ApintMath.binomial(n, 2, radix);
        }
        if (k == 2)
        {
            return ApintMath.pow(new Apint(2, radix), n - 1).subtract(Apint.ONES[radix]);
        }
        Apint sum = Apint.ZEROS[radix];
        Iterator<Apint> binomial = BinomialHelper.binomials(k, 0, radix);
        for (long i = 0; i <= k; i++)
        {
            Apint term = binomial.next().multiply(ApintMath.pow(new Apint(i, radix), n));
            sum = ((k - i & 1) == 0 ? sum.add(term) : sum.subtract(term));
        }
        return sum.divide(ApintMath.factorial(k, radix));
    }

    /**
     * Stirling numbers of the second kind.
     *
     * @param n
     * @param radix
     *
     * @return Iterator for S(n, k)
     */

    public static Iterator<Apint> stirlingS2s(int n, int radix)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("Argument must be nonnegative");
        }
        if (n == Integer.MAX_VALUE)
        {
            throw new ApfloatRuntimeException("Maximum array size exceeded: " + (n + 1L), "maximumArraySizeExceeded", n + 1L);
        }
        Apint[] s2 = new Apint[n + 1];
        Apint one = Apint.ONES[radix];
        s2[0] = (n == 0 ? one : Apint.ZEROS[radix]);
        return new Iterator<Apint>()
        {
            @Override
            public boolean hasNext()
            {
                return (this.k <= n);
            }

            @Override
            public Apint next()
            {
                if (this.k > 0)
                {
                    Apint kk = new Apint(this.k, radix);
                    this.factorial = this.factorial.multiply(kk);
                    s2[this.k] = ApintMath.pow(kk, n);
                    Apint factor = one;
                    for (int r = 1; r < this.k; r++)
                    {
                        factor = factor.multiply(new Apint(this.k - r + 1, radix));
                        s2[this.k] = s2[this.k].subtract(s2[r].multiply(factor));
                    }
                    s2[this.k] = s2[this.k].divide(factorial);
                }
                return s2[this.k++];
            }

            private int k;
            private Apint factorial = one;
        };
    }
}
