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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import org.apfloat.spi.Util;

/**
 * Helper class for Bernoulli numbers.
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

class BernoulliHelper
{
    public static abstract class ConvertingIterator<T extends Apfloat>
        implements Iterator<T>
    {
        public ConvertingIterator(BiFunction<Apint, Apint, T> converter)
        {
            this.converter = converter;
        }

        protected BiFunction<Apint, Apint, T> converter;
    }

    // Uses the Akiyama–Tanigawa algorithm
    public static abstract class AbstractBernoulliIterator<T extends Apfloat>
        extends ConvertingIterator<T>
    {
        public AbstractBernoulliIterator(int radix, BiFunction<Apint, Apint, T> converter)
        {
            super(converter);
            this.radix = radix;
            this.all = new ArrayList<>();
            this.denominator = Apint.ONES[radix];
        }

        @Override
        public boolean hasNext()
        {
            return true;
        }

        @Override
        public T next()
        {
            Apint n1 = new Apint(this.n + 1, radix);
            for (int i = 0; i < this.n; i++)
            {
                this.all.set(i, this.all.get(i).multiply(n1));
            }
            this.all.add(this.denominator);
            this.denominator = this.denominator.multiply(n1);
            for (int i = this.n; i > 0; i--)
            {
                this.all.set(i - 1, this.all.get(i - 1).subtract(this.all.get(i)).multiply(new Apint(i, radix)));
            }
            Apint numerator = this.all.get(0);
            if (this.n == 1)
            {
                numerator = numerator.negate();
            }
            this.n++;

            return super.converter.apply(numerator, this.denominator);
        }

        private int radix,
                    n;
        private List<Apint> all;
        private Apint denominator;
    }

    private static BiFunction<Apint, Apint, Apfloat> toApfloat(long precision)
    {
        return (numerator, denominator) -> numerator.precision(precision).divide(denominator);
    }

    public static class ApfloatBernoulliIterator
        extends AbstractBernoulliIterator<Apfloat>
    {
        public ApfloatBernoulliIterator(long precision, int radix)
        {
            super(radix, toApfloat(precision));
        }
    }

    public static class AprationalBernoulliIterator
        extends AbstractBernoulliIterator<Aprational>
    {
        public AprationalBernoulliIterator(int radix)
        {
            super(radix, Aprational::new);
        }
    }

    // Returns the even Bernoulli numbers B_2n, n > 0
    public static class Bernoulli2Iterator<T extends Apfloat>
        implements Iterator<T>
    {
        public Bernoulli2Iterator(Iterator<T> iterator)
        {
            this.iterator = iterator;
            this.iterator.next();
        }
        
        @Override
        public boolean hasNext()
        {
            return true;
        }

        @Override
        public T next()
        {
            this.iterator.next();
            return this.iterator.next();
        }

        private Iterator<T> iterator;
    }

    public static Iterator<Apfloat> bernoullis(long n, long precision, int radix)
    {
        return (n <= BIG_THRESHOLD ? bernoullisSmall(precision, radix) : bernoullisBig(n, precision, radix));
    }

    public static Iterator<Apfloat> bernoullisSmall(long precision, int radix)
    {
        return new ApfloatBernoulliIterator(precision, radix);
    }

    public static Iterator<Apfloat> bernoullisBig(long n, long precision, int radix)
    {
        return bernoullisBig(n, radix, toApfloat(precision));
    }

    public static Iterator<Apfloat> bernoullis2(long n, long precision, int radix)
    {
        return (n < BIG_THRESHOLD / 2 ? bernoullis2Small(precision, radix) : bernoullis2Big(n, precision, radix));
    }

    public static Iterator<Apfloat> bernoullis2Small(long precision, int radix)
    {
        return new Bernoulli2Iterator<>(bernoullisSmall(precision, radix));
    }

    public static Iterator<Apfloat> bernoullis2Big(long n, long precision, int radix)
    {
        return bernoullis2Big(n, radix, toApfloat(precision));
    }

    public static Aprational bernoulli(long n, int radix)
    {
        return (n <= BernoulliHelper.BIG_THRESHOLD ? BernoulliHelper.bernoulliSmall(n, radix) : BernoulliHelper.bernoulliBig(n, radix));
    }

    public static Aprational bernoulliSmall(long n, int radix)
    {
        assert (n > 0);
        Aprational sum = Aprational.ZERO;
        for (long k = 1; k <= n; k++)
        {
            Apint binomial = Apint.ONES[radix];
            Apint part = Apint.ZERO;
            for (long v = 1; v <= k; v++)
            {
                binomial = binomial.multiply(new Apint(k + 1 - v, radix)).divide(new Apint(v, radix));
                Apint term = binomial.multiply(ApintMath.pow(new Apint(v, radix), n));
                part = ((v & 1) == 0 ? part.add(term) : part.subtract(term));
            }
            sum = sum.add(new Aprational(part, new Apint(k + 1, radix)));
        }
        return sum;

        // Alternative algorithm that uses more memory but isn't really much faster:
        //
        //     factorial = factorial.multiply(new Apint(k, radix));
        //     sum = sum.multiply(new Apint(k + 1, radix)).add(part.multiply(factorial));
        // }
        // return new Aprational(sum, ApintMath.factorial(n + 1, radix));
    }

    public static Aprational bernoulliBig(long n, int radix)
    {
        // See https://arxiv.org/abs/1108.0286 for the algorithm, Fast computation of Bernoulli, Tangent and Secant numbers by Richard P. Brent, David Harvey
        assert (n > 1);
        assert (n & 1) == 0;
        n >>= 1;

        Apint one = Apint.ONES[radix],
              two = new Apint(2, radix);
        long p = Math.max(1, (long) Math.ceil(n * Math.log(n) / Math.log(radix))),
             precision = ApfloatHelper.extendPrecision(Util.multiplyExact(Util.multiplyExact(2, n) + 1, p));  // 2 * n * p + 2 is not enough!
        Apint f2n1 = ApintMath.factorial(2 * n - 1,  radix);
        Apfloat z = ApfloatMath.scale(new Apfloat(1, precision, radix), -p),
                v = ApfloatMath.scale(f2n1.multiply(ApfloatMath.tan(z)), -p);
        v = ApfloatMath.scale(v, 2 * p * (n - 1));
        v = v.frac();
        v = ApfloatMath.scale(v, 2 * p);
        Apint t = v.truncate();
        Apint two2n1 = ApintMath.pow(two, 2 * n - 1),
              two2n = two2n1.multiply(two);
        Aprational b = new Aprational(new Apint((n & 1) == 1 ? n : -n, radix).multiply(t), two2n.subtract(one).multiply(two2n1));
        return b;
    }

    public static Iterator<Aprational> bernoullis(long n, int radix)
    {
        return (n <= BIG_THRESHOLD ? bernoullisSmall(radix) : bernoullisBig(n, radix));
    }

    public static Iterator<Aprational> bernoullisSmall(int radix)
    {
        return new AprationalBernoulliIterator(radix);
    }

    public static Iterator<Aprational> bernoullisBig(long n, int radix)
    {
        return bernoullisBig(n, radix, Aprational::new);
    }

    public static Iterator<Aprational> bernoullis2(long n, int radix)
    {
        return (n < BIG_THRESHOLD / 2 ? bernoullis2Small(radix) : bernoullis2Big(n, radix));
    }

    public static Iterator<Aprational> bernoullis2Small(int radix)
    {
        return new Bernoulli2Iterator<>(bernoullisSmall(radix));
    }

    public static Iterator<Aprational> bernoullis2Big(long n, int radix)
    {
        return bernoullis2Big(n, radix, Aprational::new);
    }

    public static <T extends Apfloat> Iterator<T> bernoullisBig(long n, int radix, BiFunction<Apint, Apint, T> converter)
    {
        Iterator<T> i = bernoullis2Big(n >> 1, radix, converter);
        return new Iterator<T>()
        {
            @Override
            public boolean hasNext()
            {
                return this.k <= n;
            }

            @Override
            public T next()
            {
                if (this.k > n)
                {
                    throw new NoSuchElementException();
                }

                T b;
                if (this.k == 0)
                {
                    b = converter.apply(Apint.ONES[radix], Apint.ONES[radix]);
                }
                else if (this.k == 1)
                {
                    b = converter.apply(new Apint(-1, radix), new Apint(2, radix));
                }
                else if ((this.k & 1) == 1)
                {
                    b = converter.apply(Apint.ZEROS[radix], Apint.ONES[radix]);
                }
                else
                {
                    b = i.next();
                }
                this.k++;

                return b;
            }

            private long k;
        };
    }

    // Returns the even Bernoulli numbers B_2n, n > 0
    private static <T extends Apfloat> Iterator<T> bernoullis2Big(long n, int radix, BiFunction<Apint, Apint, T> converter)
    {
        Apint one = Apint.ONES[radix],
              two = new Apint(2, radix);
        long p = (long) Math.ceil(n * Math.log(n) / Math.log(radix)),
             precision = ApfloatHelper.extendPrecision(Util.multiplyExact(Util.multiplyExact(2, n) + 1, p));  // 2 * n * p + 2 is not enough!
        Apint f2n1 = ApintMath.factorial(2 * n - 1,  radix);
        Apfloat z = ApfloatMath.scale(new Apfloat(1, precision, radix), -p);

        return new Iterator<T>()
        {
            @Override
            public boolean hasNext()
            {
                return this.k <= n;
            }

            @Override
            public T next()
            {
                if (this.k > n)
                {
                    throw new NoSuchElementException();
                }

                long k = this.k;
                this.v = ApfloatMath.scale(this.v, 2 * p);
                Apint t1 = this.v.truncate();
                this.f2k1 = (k == 1 ? this.f2k1 : this.f2k1.multiply(new Apint(2 * k - 1, radix)).multiply(new Apint(2 * k - 2, radix)));
                Apint t = t1.multiply(this.f2k1).divide(f2n1);
                this.v = this.v.frac();
                this.two2k = this.two2k1.multiply(two);
                T b = converter.apply(new Apint((k & 1) == 1 ? k : -k, radix).multiply(t), this.two2k.subtract(one).multiply(this.two2k1));
                this.two2k1 = this.two2k.multiply(two);
                this.k++;
                return b;
            }

            private long k = 1;
            private Apfloat v = ApfloatMath.scale(f2n1.multiply(ApfloatMath.tan(z)), -p);
            private Apint f2k1 = one,
                    two2k1 = two,
                    two2k;
        };
    }

    private static final int BIG_THRESHOLD = 200000;
}
