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
