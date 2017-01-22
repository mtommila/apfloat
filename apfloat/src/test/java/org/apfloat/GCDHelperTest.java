/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
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
