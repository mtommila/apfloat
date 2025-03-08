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
package org.apfloat.internal;

import static org.apfloat.internal.RawtypeModConstants.MODULUS;

import java.math.BigInteger;
import java.util.Random;

import jdk.incubator.vector.RawtypeVector;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class RawtypeVectorModMathTest
    extends TestCase
{
    public RawtypeVectorModMathTest(String methodName)
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

        suite.addTest(new RawtypeVectorModMathTest("testMultiply"));
        suite.addTest(new RawtypeVectorModMathTest("testAdd"));
        suite.addTest(new RawtypeVectorModMathTest("testSubtract"));

        return suite;
    }

    public static void testMultiply()
    {
        RawtypeVectorModMath math = new RawtypeVectorModMath();
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

    private static void runOneMultiply(RawtypeVectorModMath math, long x, long y, BigInteger m)
    {
        RawtypeVector v = math.modMultiply(v((rawtype) x), v((rawtype) y));

        BigInteger xTrue = BigInteger.valueOf(x),
                   yTrue = BigInteger.valueOf(y),
                   rTrue = xTrue.multiply(yTrue).mod(m);

        for (int i = 0; i < v.length(); i++)
        {
            assertEquals(x + " * " + y + " % " + m, rTrue.longValue(), (long) v.lane(i));
        }
    }

    public static void testAdd()
    {
        RawtypeVectorModMath math = new RawtypeVectorModMath();

        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", v(MODULUS[0] - 5), math.modAdd(v(MODULUS[0] - 8), v(3)));
        assertEquals("just no overflow", v(MODULUS[0] - 1), math.modAdd(v(MODULUS[0] - 4), v(3)));
        assertEquals("just overflow", v(0), math.modAdd(v(MODULUS[0] - 3), v(3)));
        assertEquals("overflow", v(5), math.modAdd(v(MODULUS[0] - 3), v(8)));
    }

    public static void testSubtract()
    {
        RawtypeVectorModMath math = new RawtypeVectorModMath();

        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", v(5), math.modSubtract(v(8), v(3)));
        assertEquals("just no overflow", v(0), math.modSubtract(v(3), v(3)));
        assertEquals("just overflow", v(MODULUS[0] - 1), math.modSubtract(v(3), v(4)));
        assertEquals("overflow", v(MODULUS[0] - 5), math.modSubtract(v(3), v(8)));
    }

    private static RawtypeVector v(rawtype v)
    {
        return RawtypeVector.broadcast(RawtypeVector.SPECIES_PREFERRED, v);
    }
}
