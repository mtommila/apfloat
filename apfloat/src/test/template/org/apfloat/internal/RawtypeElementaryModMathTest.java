/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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
