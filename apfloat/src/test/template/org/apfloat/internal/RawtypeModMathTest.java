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
package org.apfloat.internal;

import java.util.Random;

import junit.framework.TestSuite;

/**
 * @version 1.0.1
 * @author Mikko Tommila
 */

public class RawtypeModMathTest
    extends RawtypeTestCase
    implements RawtypeModConstants
{
    public RawtypeModMathTest(String methodName)
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

        suite.addTest(new RawtypeModMathTest("testCreateWTable"));
        suite.addTest(new RawtypeModMathTest("testGetForwardNthRoot"));
        suite.addTest(new RawtypeModMathTest("testGetInverseNthRoot"));
        suite.addTest(new RawtypeModMathTest("testInverse"));
        suite.addTest(new RawtypeModMathTest("testDivide"));
        suite.addTest(new RawtypeModMathTest("testNegate"));
        suite.addTest(new RawtypeModMathTest("testPow"));

        return suite;
    }

    public static void testCreateWTable()
    {
        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[0]);
        rawtype[] wTable = math.createWTable((rawtype) 2, 5);

        assertEquals("[0]", 1, (long) wTable[0]);
        assertEquals("[1]", 2, (long) wTable[1]);
        assertEquals("[2]", 4, (long) wTable[2]);
        assertEquals("[3]", 8, (long) wTable[3]);
        assertEquals("[4]", 16, (long) wTable[4]);
    }

    public static void testGetForwardNthRoot()
    {
        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[0]);
        rawtype w = math.getForwardNthRoot(PRIMITIVE_ROOT[0], 4);

        assertEquals("w^(n/2)", (long) MODULUS[0] - 1, (long) math.modMultiply(w, w));
        assertEquals("w^n", 1, (long) math.modMultiply(w, math.modMultiply(w, math.modMultiply(w, w))));
    }

    public static void testGetInverseNthRoot()
    {
        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[0]);
        rawtype w = math.getInverseNthRoot(PRIMITIVE_ROOT[0], 4);

        assertEquals("w^(n/2)", (long) MODULUS[0] - 1, (long) math.modMultiply(w, w));
        assertEquals("w^n", 1, (long) math.modMultiply(w, math.modMultiply(w, math.modMultiply(w, w))));
        assertTrue("inverse vs. forward", math.getForwardNthRoot(PRIMITIVE_ROOT[0], 4) != w);
    }

    public static void testInverse()
    {
        RawtypeModMath math = new RawtypeModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            long lm = (long) MODULUS[modulus],
                 x;

            x = 1;
            assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((rawtype) x), (rawtype) x));

            x = lm - 1;
            assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((rawtype) x), (rawtype) x));

            for (int i = 0; i < 1000; i++)
            {
                x = Math.abs(random.nextLong()) % lm;

                assertEquals(x + " ^ -1 % " + lm, 1L, (long) math.modMultiply(math.modInverse((rawtype) x), (rawtype) x));
            }
        }
    }

    public static void testDivide()
    {
        RawtypeModMath math = new RawtypeModMath();
        Random random = new Random();

        for (int modulus = 0; modulus < 3; modulus++)
        {
            math.setModulus(MODULUS[modulus]);
            long lm = (long) MODULUS[modulus],
                 x, y;

            x = 0;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((rawtype) x, (rawtype) y), (rawtype) y));

            x = 0;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((rawtype) x, (rawtype) y), (rawtype) y));

            x = 1;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((rawtype) x, (rawtype) y), (rawtype) y));

            x = lm - 1;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((rawtype) x, (rawtype) y), (rawtype) y));

            x = 1;
            y = lm - 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((rawtype) x, (rawtype) y), (rawtype) y));

            x = lm - 1;
            y = 1;
            assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((rawtype) x, (rawtype) y), (rawtype) y));

            for (int i = 0; i < 1000; i++)
            {
                x = Math.abs(random.nextLong()) % lm;
                y = Math.abs(random.nextLong()) % lm;

                assertEquals(x + " / " + y + " % " + lm, x, (long) math.modMultiply(math.modDivide((rawtype) x, (rawtype) y), (rawtype) y));
            }
        }
    }

    public static void testNegate()
    {
        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[0]);

        assertEquals("zero", 0, (long) math.negate(0));
        assertEquals("non-zero", (long) MODULUS[0] - 1, (long) math.negate((rawtype) 1));
    }

    public static void testPow()
    {
        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[0]);

        assertEquals("no overflow", 3125, (long) math.modPow((rawtype) 5, (rawtype) 5));
        assertEquals("overflow", ((long) MODULUS[0] + 1) / 2, (long) math.modPow((rawtype) 2, (rawtype) -1));
    }
}
