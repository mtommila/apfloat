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
package org.apfloat.internal;

import java.util.Random;
import java.math.BigInteger;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeElementaryModMathTest
    extends RawtypeTestCase
    implements RawtypeModConstants
{
    public RawtypeElementaryModMathTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new RawtypeElementaryModMathTest("testMultiply"));
        suite.addTest(new RawtypeElementaryModMathTest("testAdd"));
        suite.addTest(new RawtypeElementaryModMathTest("testSubtract"));

        return suite;
    }

    public static void testMultiply()
    {
        RawtypeElementaryModMath math = new RawtypeElementaryModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            BigInteger m = BigInteger.valueOf((long) MODULUS[modulus]);
            long lm = (long) MODULUS[modulus];

            runOneMultiply(math, 0, 0, m);
            runOneMultiply(math, 1, 0, m);
            runOneMultiply(math, 0, 1, m);
            runOneMultiply(math, 0, lm - 1, m);
            runOneMultiply(math, lm - 1, 0, m);
            runOneMultiply(math, 1, lm - 1, m);
            runOneMultiply(math, lm - 1, 1, m);
            runOneMultiply(math, lm - 1, lm - 1, m);

            for (int i = 0; i < 1000; i++)
            {
                long x = Math.abs(random.nextLong()) % lm,
                     y = Math.abs(random.nextLong()) % lm;

                runOneMultiply(math, x, y, m);
                runOneMultiply(math, x, lm - 1, m);
                runOneMultiply(math, lm - 1, x, m);
            }
        }
    }

    private static void runOneMultiply(RawtypeElementaryModMath math, long x, long y, BigInteger m)
    {
        long r = (long) math.modMultiply((rawtype) x, (rawtype) y);

        BigInteger xTrue = BigInteger.valueOf(x),
                   yTrue = BigInteger.valueOf(y),
                   rTrue = xTrue.multiply(yTrue).mod(m);

        assertEquals(x + " * " + y + " % " + m, rTrue.longValue(), r);
    }

    public static void testAdd()
    {
        RawtypeElementaryModMath math = new RawtypeElementaryModMath();

        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", (long) MODULUS[0] - 5, (long) math.modAdd(MODULUS[0] - (rawtype) 8, (rawtype) 3));
        assertEquals("just no overflow", (long) MODULUS[0] - 1, (long) math.modAdd(MODULUS[0] - (rawtype) 4, (rawtype) 3));
        assertEquals("just overflow", 0, (long) math.modAdd(MODULUS[0] - (rawtype) 3, (rawtype) 3));
        assertEquals("overflow", 5, (long) math.modAdd(MODULUS[0] - (rawtype) 3, (rawtype) 8));
    }

    public static void testSubtract()
    {
        RawtypeElementaryModMath math = new RawtypeElementaryModMath();

        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", 5, (long) math.modSubtract((rawtype) 8, (rawtype) 3));
        assertEquals("just no overflow", 0, (long) math.modSubtract((rawtype) 3, (rawtype) 3));
        assertEquals("just overflow", (long) MODULUS[0] - 1, (long) math.modSubtract((rawtype) 3, (rawtype) 4));
        assertEquals("overflow", (long) MODULUS[0] - 5, (long) math.modSubtract((rawtype) 3, (rawtype) 8));
    }
}
