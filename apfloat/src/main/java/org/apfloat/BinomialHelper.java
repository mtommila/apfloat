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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Helper class for binomial numbers.
 *
 * @since 1.16.0
 * @author Mikko Tommila
 */
class BinomialHelper {

    private BinomialHelper()
    {
    }

    /**
     * Iterator for binomial numbers.
     *
     * @param n
     * @param start Starting value for k, should be small (e.g. 0 or 1)
     * @param radix
     *
     * @return The iterator returns binomial(n, k) where k starts with the start value
     */
    public static Iterator<Apint> binomials(long n, long start, int radix)
    {
        assert (n >= 0);
        assert (start >= 0);
        Iterator<Apint> iterator = new Iterator<Apint>()
        {
            @Override
            public boolean hasNext()
            {
                return this.i <= n;
            }

            @Override
            public Apint next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }

                if (this.i > 0)
                {
                    this.binomial = this.binomial.multiply(new Apint(n - this.i + 1, radix)).divide(new Apint(this.i, radix));
                }
                this.i++;
                return this.binomial;
            }

            private long i;
            private Apint binomial = Apint.ONES[radix];
        };

        while (start > 0)
        {
            iterator.next();
            start--;
        }
        return iterator;
    }
}
