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
package org.apfloat;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.6
 * @version 1.6
 * @author Mikko Tommila
 */

public class GCDHelperTest
    extends TestCase
{
    public GCDHelperTest(String methodName)
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

        suite.addTest(new GCDHelperTest("testGcd"));

        return suite;
    }

    public static void testGcd()
    {
        Random random = new Random();

        for (int i = 0; i < 5; i++)
        {
            Apint a = new Apint(getString(random));
            Apint b = new Apint(getString(random));

            Apint c = elementaryGcd(a, b);
            Apint d = ApintMath.gcd(a, b);

            assertEquals("GCD", c, d);
        }
    }

    private static String getString(Random random)
    {
        int length = random.nextInt(65000) + 35000;
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            buffer.append((char) (random.nextInt(10) + (int) '0'));
        }

        return buffer.toString();
    }

    private static Apint elementaryGcd(Apint a, Apint b)
        throws ApfloatRuntimeException
    {
        while (b.signum() != 0)
        {
            Apint r = a.mod(b);
            a = b;
            b = r;
        }

        return ApintMath.abs(a);
    }
}
