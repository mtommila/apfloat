/*
 * MIT License
 *
 * Copyright (c) 2002-2024 Mikko Tommila
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

import junit.framework.TestSuite;

/**
 * @version 1.13.0
 * @author Mikko Tommila
 */

public class BesselHelperTest
    extends ApfloatTestCase
{
    public BesselHelperTest(String methodName)
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

        suite.addTest(new BesselHelperTest("testBesselJ"));
        suite.addTest(new BesselHelperTest("testBesselI"));
        suite.addTest(new BesselHelperTest("testBesselY"));
        suite.addTest(new BesselHelperTest("testBesselK"));

        return suite;
    }

    public static void testBesselJ()
    {
        Apcomplex a = BesselHelper.besselJ(new Apcomplex("(-3.000000,-4.000000)"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("-3 - 4i, 5 + 6i precision", 6, a.precision());
        assertEquals("-3 - 4i, 5 + 6i value", new Apcomplex("(-99583.5,102827)"), a, new Apfloat("5e0"));

        a = BesselHelper.besselJ(new Apcomplex("0"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("0, 5 + 6i precision", 7, a.precision());
        assertEquals("0, 5 + 6i value", new Apcomplex("(-4.14447,58.15938)"), a, new Apfloat("5e-5"));

        a = BesselHelper.besselJ(new Apcomplex("-1.000000"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("-1, 5 + 6i precision", 6, a.precision());
        assertEquals("-1, 5 + 6i value", new Apcomplex("(55.4684,1.42477)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselJ(new Apcomplex("-0.999999"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("-0.999999, 5 + 6i precision", 6, a.precision());
        assertEquals("-0.999999, 5 + 6i value", new Apcomplex("(55.4684,1.42486)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselJ(new Apcomplex("-1.00000"), new Apcomplex("(0,6.00000)"));
        assertEquals("-1, 6i precision", 6, a.precision());
        assertEquals("-1, 6i value", new Apcomplex("(0,-61.3419)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselJ(new Apcomplex("-0.999999"), new Apcomplex("(0,6.000000)"));
        assertEquals("-0.999999, 6i precision", 6, a.precision());
        assertEquals("-0.999999, 6i value", new Apcomplex("(0.0000963557,-61.3419)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselJ(new Apcomplex("-1.999999"), new Apcomplex("3.000000"));
        assertEquals("-1.999999, 3 precision", 6, a.precision());
        assertEquals("-1.999999, 3 value", new Apcomplex("0.486087"), a, new Apfloat("5e-6"));

        a = BesselHelper.besselJ(new Apcomplex("-1.9999999999998"), new Apcomplex("3.0000000000000"));
        assertEquals("-1.9999999999998, 3 precision", 14, a.precision());
        assertEquals("-1.9999999999998, 3 value", new Apcomplex("0.48609126058580"), a, new Apfloat("5e-14"));

        a = BesselHelper.besselJ(new Apcomplex("-1.9999999999998000"), new Apcomplex("3.0000000000000000"));
        assertEquals("-1.9999999999998, 3 precision", 17, a.precision());
        assertEquals("-1.9999999999998, 3 value", new Apcomplex("0.48609126058580704"), a, new Apfloat("5e-17"));

        /*
        a = BesselHelper.besselJ(new Apcomplex("1e-1000000000000"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("1e-1000000000000, 5 + 6i precision", 7, a.precision());
        assertEquals("1e-1000000000000, 5 + 6i value", new Apcomplex("(-4.14447,58.15938)"), a, new Apfloat("5e-5"));
        */

        a = BesselHelper.besselJ(new Apcomplex("0"), new Apcomplex("5.52007811028631064959660411281"));
        assertEquals("0, 2nd zero precision", 30, a.precision());
        assertEquals("0, 2nd zero value", new Apcomplex("0"), a, new Apfloat("5e-30"));

        a = BesselHelper.besselJ(new Apcomplex("-0.500000000000000000000000000000"), new Apcomplex("7.85398163397448309615660845821"));
        assertEquals("-1/2, 3rd zero precision", 30, a.precision());
        assertEquals("-1/2, 3rd zero value", new Apcomplex("0"), a, new Apfloat("5e-30"));

        a = BesselHelper.besselJ(new Apcomplex("0"), new Apcomplex("0"));
        assertEquals("0, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("0, 0 value", new Apcomplex("1"), a);

        a = BesselHelper.besselJ(new Apcomplex("-1.0"), new Apcomplex("0"));
        assertEquals("-1, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("-1, 0 value", new Apcomplex("0"), a);

        a = BesselHelper.besselJ(new Apcomplex(new Apfloat(3, 20, 2), new Apfloat(-4, 20, 2)), new Apcomplex(new Apfloat(5, 20, 2), new Apfloat(-6, 20, 2)));
        assertEquals("3 - 4i, 5 - 6i precision", 17, a.precision());
        assertEquals("3 - 4i, 5 - 6i radix", 2, a.radix());
        assertEquals("3 - 4i, 5 - 6i value", new Apcomplex(new Apfloat("0.011001100101101100", 17, 2), new Apfloat("0.010111000001000100", 17, 2)), a, new Apfloat("1e-18", 1, 2));

        a = BesselHelper.besselJ(new Apcomplex(new Apint(0, 2)), new Apcomplex(new Apint(0, 2)));
        assertEquals("0, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("0, 0 radix", 2, a.radix());
        assertEquals("0, 0 value", new Apcomplex("1"), a);

        a = BesselHelper.besselJ(new Apcomplex(new Apint(-1, 2)), new Apcomplex(new Apint(0, 2)));
        assertEquals("-1, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("-1, 0 radix", 2, a.radix());
        assertEquals("-1, 0 value", new Apcomplex("0"), a);

        try
        {
            BesselHelper.besselJ(new Apcomplex("-0.1"), new Apcomplex("0"));
            fail("-0.1, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselJ(new Apcomplex("(0,0.1)"), new Apcomplex("0"));
            fail("0.1i, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }

        try
        {
            BesselHelper.besselJ(new Apcomplex(Apfloat.ONE, new Apfloat(4)), new Apcomplex(Apfloat.ONE, new Apfloat(4)));
            fail("Infinite expansion");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK
        }
    }

    public static void testBesselI()
    {
        Apcomplex a = BesselHelper.besselI(new Apcomplex("(-3.000000,-4.000000)"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("-3 - 4i, 5 + 6i precision", 6, a.precision());
        assertEquals("-3 - 4i, 5 + 6i value", new Apcomplex("(540.404,-264.967)"), a, new Apfloat("5e-3"));

        a = BesselHelper.besselI(new Apcomplex("0"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("0, 5 + 6i precision", 7, a.precision());
        assertEquals("0, 5 + 6i value", new Apcomplex("(15.87945,-14.34085)"), a, new Apfloat("5e-5"));

        a = BesselHelper.besselI(new Apcomplex("-1.000000"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("-1, 5 + 6i precision", 6, a.precision());
        assertEquals("-1, 5 + 6i value", new Apcomplex("(15.9742,-12.9498)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselI(new Apcomplex("-0.999999"), new Apcomplex("(5.000000,6.000000)"));
        assertEquals("-0.999999, 5 + 6i precision", 6, a.precision());
        assertEquals("-0.999999, 5 + 6i value", new Apcomplex("(15.9742,-12.9498)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselI(new Apcomplex("0"), new Apcomplex("0"));
        assertEquals("0, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("0, 0 value", new Apcomplex("1"), a);

        a = BesselHelper.besselI(new Apcomplex("-1.0"), new Apcomplex("0"));
        assertEquals("-1, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("-1, 0 value", new Apcomplex("0"), a);

        a = BesselHelper.besselI(new Apcomplex(new Apfloat(3, 20, 2), new Apfloat(-4, 20, 2)), new Apcomplex(new Apfloat(5, 20, 2), new Apfloat(-6, 20, 2)));
        assertEquals("3 - 4i, 5 - 6i precision", 17, a.precision());
        assertEquals("3 - 4i, 5 - 6i radix", 2, a.radix());
        assertEquals("3 - 4i, 5 - 6i value", new Apcomplex(new Apfloat("-11.100010111001110001", 17, 2), new Apfloat("111.0101111110001111", 17, 2)), a, new Apfloat("1e-16", 1, 2));

        a = BesselHelper.besselI(new Apcomplex(new Apint(0, 2)), new Apcomplex(new Apint(0, 2)));
        assertEquals("0, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("0, 0 radix", 2, a.radix());
        assertEquals("0, 0 value", new Apcomplex("1"), a);

        a = BesselHelper.besselI(new Apcomplex(new Apint(-1, 2)), new Apcomplex(new Apint(0, 2)));
        assertEquals("-1, 0 precision", Apfloat.INFINITE, a.precision());
        assertEquals("-1, 0 radix", 2, a.radix());
        assertEquals("-1, 0 value", new Apcomplex("0"), a);

        try
        {
            BesselHelper.besselI(new Apcomplex("-0.1"), new Apcomplex("0"));
            fail("-0.1, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselI(new Apcomplex("(0,0.1)"), new Apcomplex("0"));
            fail("0.1i, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }

        try
        {
            BesselHelper.besselI(new Apcomplex(Apfloat.ONE, new Apfloat(4)), new Apcomplex(Apfloat.ONE, new Apfloat(4)));
            fail("Infinite expansion");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK
        }
    }

    public static void testBesselY()
    {
        Apcomplex a = BesselHelper.besselY(new Apcomplex("(-3.00000,-4.00000)"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("-3 - 4i, 5 + 6i precision", 6, a.precision());
        assertEquals("-3 - 4i, 5 + 6i value", new Apcomplex("(-102827,-99583.5)"), a, new Apfloat("5e-0"));

        a = BesselHelper.besselY(new Apcomplex("0"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("0, 5 + 6i precision", 6, a.precision());
        assertEquals("0, 5 + 6i value", new Apcomplex("(-58.1598,-4.1439)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselY(new Apcomplex("-1.00000"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("-1, 5 + 6i precision", 6, a.precision());
        assertEquals("-1, 5 + 6i value", new Apcomplex("(-1.4253,55.4679)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselY(new Apcomplex("-10").precision(30), new Apcomplex("(5,6)").precision(30));
        assertEquals("-10, 5 + 6i precision", 30, a.precision());
        assertEquals("-10, 5 + 6i value", new Apcomplex("(-0.341222371369833270387557564868,0.198626104551143979520413812554)"), a, new Apfloat("5e-30"));

        a = BesselHelper.besselY(new Apcomplex("-0.999999"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("-0.999999, 5 + 6i precision", 6, a.precision());
        assertEquals("-0.999999, 5 + 6i value", new Apcomplex("(-1.42544,55.4679)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselY(new Apcomplex("-1.00000"), new Apcomplex("(0,6.00000)"));
        assertEquals("-1, 6i precision", 6, a.precision());
        assertEquals("-1, 6i value", new Apcomplex("(61.3419,-0.000855566)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselY(new Apcomplex("0"), new Apcomplex("13.3610974738727634782676945857"));
        assertEquals("0, 5th zero precision", 30, a.precision());
        assertEquals("0, 5th zero value", new Apcomplex("0"), a, new Apfloat("5e-30"));

        a = BesselHelper.besselY(new Apcomplex("0.500000000000000000000000000000"), new Apcomplex("7.85398163397448309615660845821"));
        assertEquals("1/2, 3rd zero precision", 30, a.precision());
        assertEquals("1/2, 3rd zero value", new Apcomplex("0"), a, new Apfloat("5e-30"));

        a = BesselHelper.besselY(new Apcomplex("0"), new Apcomplex("(0.444223,0.938874)"));
        assertEquals("Root of i precision", 6, a.precision());
        assertEquals("Root of i value", new Apcomplex("(0,1.00000)"), a, new Apfloat("5e-5"));

        a = BesselHelper.besselY(new Apcomplex(new Apfloat(3, 17, 2), new Apfloat(-4, 17, 2)), new Apcomplex(new Apfloat(5, 17, 2), new Apfloat(-6, 17, 2)));
        assertEquals("3 - 4i, 5 - 6i precision", 17, a.precision());
        assertEquals("3 - 4i, 5 - 6i radix", 2, a.radix());
        assertEquals("3 - 4i, 5 - 6i value", new Apcomplex(new Apfloat("0.010110111000100010010000", 17, 2), new Apfloat("-0.01001011011100111100111", 17, 2)), a, new Apfloat("1e-18", 1, 2));

        try
        {
            BesselHelper.besselY(new Apcomplex("0"), new Apcomplex("0"));
            fail("0, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselY(new Apcomplex("1"), new Apcomplex("0"));
            fail("1, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselY(new Apcomplex("-0.1"), new Apcomplex("0"));
            fail("-0.1, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselY(new Apcomplex("(0,0.1)"), new Apcomplex("0"));
            fail("0.1i, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }

        try
        {
            BesselHelper.besselY(new Apcomplex(Apfloat.ONE, new Apfloat(4)), new Apcomplex(Apfloat.ONE, new Apfloat(4)));
            fail("Infinite expansion");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK
        }
    }

    public static void testBesselK()
    {
        Apcomplex a = BesselHelper.besselK(new Apcomplex("(-3.00000,-4.00000)"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("-3 - 4i, 5 + 6i precision", 6, a.precision());
        assertEquals("-3 - 4i, 5 + 6i value", new Apcomplex("(0.00282215,0.00595941)"), a, new Apfloat("5e-8"));

        a = BesselHelper.besselK(new Apcomplex("0"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("0, 5 + 6i precision", 6, a.precision());
        assertEquals("0, 5 + 6i value", new Apcomplex("(0.002959927,-0.000427643)"), a, new Apfloat("5e-8"));

        a = BesselHelper.besselK(new Apcomplex("-1.00000"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("-1, 5 + 6i precision", 6, a.precision());
        assertEquals("-1, 5 + 6i value", new Apcomplex("(0.003061552,-0.000585260)"), a, new Apfloat("5e-8"));

        a = BesselHelper.besselK(new Apcomplex("-10").precision(30), new Apcomplex("(5,6)").precision(30));
        assertEquals("-10, 5 + 6i precision", 30, a.precision());
        assertEquals("-10, 5 + 6i value", new Apcomplex("(-0.132997176584901416563825674362,0.213283091988362072925433855996)"), a, new Apfloat("5e-30"));

        a = BesselHelper.besselK(new Apcomplex("-0.999999"), new Apcomplex("(5.00000,6.00000)"));
        assertEquals("-0.999999, 5 + 6i precision", 6, a.precision());
        assertEquals("-0.999999, 5 + 6i value", new Apcomplex("(0.00306155,-0.00058526)"), a, new Apfloat("5e-8"));

        a = BesselHelper.besselK(new Apcomplex("-1.00000"), new Apcomplex("(0,6.00000)"));
        assertEquals("-1, 6i precision", 6, a.precision());
        assertEquals("-1, 6i value", new Apcomplex("(0.434614,-0.274906)"), a, new Apfloat("5e-4"));

        a = BesselHelper.besselK(new Apcomplex("0"), new Apcomplex("(-0.458343,3.90004)"));
        assertEquals("Root of i precision", 6, a.precision());
        assertEquals("Root of i value", new Apcomplex("(0,1.00000)"), a, new Apfloat("5e-5"));

        a = BesselHelper.besselK(new Apcomplex(new Apfloat(3, 17, 2), new Apfloat(-4, 17, 2)), new Apcomplex(new Apfloat(5, 17, 2), new Apfloat(-6, 17, 2)));
        assertEquals("3 - 4i, 5 - 6i precision", 17, a.precision());
        assertEquals("3 - 4i, 5 - 6i radix", 2, a.radix());
        assertEquals("3 - 4i, 5 - 6i value", new Apcomplex(new Apfloat("1.011100011110011110010e-9", 17, 2), new Apfloat("-1.1000011010001110010000e-8", 17, 2)), a, new Apfloat("1e-18", 1, 2));

        try
        {
            BesselHelper.besselK(new Apcomplex("0"), new Apcomplex("0"));
            fail("0, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselK(new Apcomplex("1"), new Apcomplex("0"));
            fail("1, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselK(new Apcomplex("-0.1"), new Apcomplex("0"));
            fail("-0.1, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
        try
        {
            BesselHelper.besselK(new Apcomplex("(0,0.1)"), new Apcomplex("0"));
            fail("0.1i, 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }

        try
        {
            BesselHelper.besselK(new Apcomplex(Apfloat.ONE, new Apfloat(4)), new Apcomplex(Apfloat.ONE, new Apfloat(4)));
            fail("Infinite expansion");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK
        }
    }
}
