/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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
import java.util.NoSuchElementException;

/**
 * Helper class for continued fractions.
 *
 * @since 1.12.0
 * @version 1.12.0
 * @author Mikko Tommila
 */

class ContinuedFractionHelper
{
    private static abstract class ContinuedFractionIterator<T extends Apfloat>
        implements Iterator<Apint>
    {
        public ContinuedFractionIterator(T x)
        {
            this.i = x.truncate();
            this.f = subtract(x, i);
        }

        @Override
        public boolean hasNext()
        {
            return this.i != null;
        }

        @Override
        public Apint next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            Apint next = this.i;
            if (this.f.signum() == 0)
            {
                this.i = null;
            }
            else
            {
                T x = inverse(this.f);
                this.i = x.truncate();
                this.f = subtract(x, i);
            }
            return next;
        }

        protected abstract T subtract(T x, Apint y);

        protected abstract T inverse(T x);

        private Apint i;
        private T f;
    }

    private ContinuedFractionHelper()
    {
    }

    public static Iterator<Apint> continuedFraction(Apfloat x)
        throws ApfloatRuntimeException
    {
        return new ContinuedFractionIterator<Apfloat>(x)
        {
            @Override
            protected Apfloat subtract(Apfloat x, Apint y)
            {
                return x.subtract(y);
            }

            @Override
            protected Apfloat inverse(Apfloat x)
            {
                return ApfloatMath.inverseRoot(x, 1);
            }
        };
    }

    public static Iterator<Apint> continuedFraction(Aprational x)
        throws ApfloatRuntimeException
    {
        return new ContinuedFractionIterator<Aprational>(x)
        {
            @Override
            protected Aprational subtract(Aprational x, Apint y)
            {
                return x.subtract(y);
            }

            @Override
            protected Aprational inverse(Aprational x)
            {
                return Aprational.ONE.divide(x);
            }
        };
    }

    public static Iterator<Aprational> convergents(Iterator<Apint> continuedFraction, int radix)
    {
        return new Iterator<Aprational>()
        {
            @Override
            public boolean hasNext()
            {
                return continuedFraction.hasNext();
            }

            @Override
            public Aprational next()
            {
                
                Apint a = continuedFraction.next(),
                      numerator = a.multiply(h1).add(h2),
                      denominator = a.multiply(k1).add(k2);
                this.h2 = this.h1;
                this.k2 = this.k1;
                this.h1 = numerator;
                this.k1 = denominator;
                return new Aprational(numerator, denominator);
            }

            private Apint h1 = Apint.ONES[radix],
                          h2 = Apint.ZEROS[radix],
                          k1 = Apint.ZEROS[radix],
                          k2 = Apint.ONES[radix];
        };
    }
}
