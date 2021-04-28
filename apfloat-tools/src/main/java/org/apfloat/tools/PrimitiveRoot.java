/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
package org.apfloat.tools;

import org.apfloat.internal.LongModMath;

/**
 * This tool can be used to generate a primitive root
 * for a prime modulus.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public class PrimitiveRoot
    extends LongModMath
{
    private PrimitiveRoot(long p)
    {
        setModulus(p);
    }

    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.err.println("Usage: PrimitiveRoot p");
            System.err.println("       where p must be prime");
            return;
        }

        long p = Long.parseLong(args[0]);

        System.out.println(new PrimitiveRoot(p).findPrimitiveRoot());
    }

    private long findPrimitiveRoot()
    {
        long p1 = getModulus() - 1,
             root = 1;
        int i;

        long[] factors = factorize(p1);

        do
        {
            root++;
            for (i = 0; i < factors.length; i++)
            {
                if (modPow(root, p1 / factors[i]) == 1)
                {
                    break;
                }
            }
        } while (i < factors.length);

        return root;
    }

    private static long[] factorize(long n)
    {
        long[] factors = new long[64];
        int i;

        for (i = 0; (n & 1) == 0; i++)
        {
            factors[i] = 2;
            n /= 2;
        }

        for (long f = 3; n > 1; f += 2)
        {
            for (; n % f == 0; i++)
            {
                factors[i] = f;
                n /= f;
            }
        }

        long[] buffer = new long[i];
        System.arraycopy(factors, 0, buffer, 0, i);

        return buffer;
    }
}
