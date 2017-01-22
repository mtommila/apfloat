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

import java.io.Writer;
import java.io.PushbackReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Formatter;
import java.util.Locale;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class ApcomplexTest
    extends ApfloatTestCase
{
    public ApcomplexTest(String methodName)
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

        suite.addTest(new ApcomplexTest("testConstructor"));
        suite.addTest(new ApcomplexTest("testStringConstructor"));
        suite.addTest(new ApcomplexTest("testStreamConstructor"));
        suite.addTest(new ApcomplexTest("testComplexMethods"));
        suite.addTest(new ApcomplexTest("testRadix"));
        suite.addTest(new ApcomplexTest("testPrecision"));
        suite.addTest(new ApcomplexTest("testScale"));
        suite.addTest(new ApcomplexTest("testSize"));
        suite.addTest(new ApcomplexTest("testNegate"));
        suite.addTest(new ApcomplexTest("testAdd"));
        suite.addTest(new ApcomplexTest("testSubtract"));
        suite.addTest(new ApcomplexTest("testMultiply"));
        suite.addTest(new ApcomplexTest("testDivide"));
        suite.addTest(new ApcomplexTest("testNumberValues"));
        suite.addTest(new ApcomplexTest("testEqualDigits"));
        suite.addTest(new ApcomplexTest("testEquals"));
        suite.addTest(new ApcomplexTest("testHashCode"));
        suite.addTest(new ApcomplexTest("testToString"));
        suite.addTest(new ApcomplexTest("testWriteTo"));
        suite.addTest(new ApcomplexTest("testFormatTo"));
        suite.addTest(new ApcomplexTest("testSerialization"));

        return suite;
    }

    public static void testConstructor()
    {
        Apcomplex a = new Apcomplex(new Apfloat(5));
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", Apfloat.INFINITE, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        a = new Apcomplex(new Apfloat(5, Apfloat.DEFAULT, 12), new Apfloat(7, Apfloat.DEFAULT, 12));
        assertEquals("(5, 7) radix", 12, a.radix());
        assertEquals("(5, 7) precision", Apfloat.INFINITE, a.precision());
        assertEquals("(5, 7) String", "(5, 7)", a.toString(true));

        a = new Apcomplex(new Apfloat(0, Apfloat.DEFAULT, 11), new Apfloat(7, Apfloat.DEFAULT, 12));
        a = new Apcomplex(new Apfloat(7, Apfloat.DEFAULT, 11), new Apfloat(0, Apfloat.DEFAULT, 12));

        try
        {
            a = new Apcomplex(new Apfloat(5, Apfloat.DEFAULT, 11), new Apfloat(7, Apfloat.DEFAULT, 12));
            fail("Different radixes accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: different radixes
        }
    }

    public static void testStringConstructor()
    {
        Apcomplex a = new Apcomplex("5");
        assertEquals("5 String", "5", a.toString(true));

        a = new Apcomplex("(5)");
        assertEquals("(5) String", "5", a.toString(true));

        a = new Apcomplex("( 5 )");
        assertEquals("( 5 ) String", "5", a.toString(true));

        a = new Apcomplex("(5,7)");
        assertEquals("(5,7) String", "(5, 7)", a.toString(true));

        a = new Apcomplex("( 5 , 7 )");
        assertEquals("( 5 , 7 ) String", "(5, 7)", a.toString(true));

        try
        {
            a = new Apcomplex("(3,4");
            fail("Missing end parenthesis accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: missing end parenthesis
        }
    }

    public static void testStreamConstructor()
        throws IOException
    {
        PushbackReader in = new PushbackReader(new StringReader("5"));
        Apcomplex a = new Apcomplex(in);
        assertEquals("5 String", "5", a.toString(true));

        in = new PushbackReader(new StringReader("(5)"));
        a = new Apcomplex(in);
        assertEquals("(5) String", "5", a.toString(true));

        in = new PushbackReader(new StringReader("( 5 )"));
        a = new Apcomplex(in);
        assertEquals("( 5 ) String", "5", a.toString(true));

        in = new PushbackReader(new StringReader("(5,7)"));
        a = new Apcomplex(in);
        assertEquals("(5,7) String", "(5, 7)", a.toString(true));

        in = new PushbackReader(new StringReader("( 5 , 7 )"));
        a = new Apcomplex(in);
        assertEquals("( 5 , 7 ) String", "(5, 7)", a.toString(true));

        try
        {
            a = new Apcomplex(new PushbackReader(new StringReader("(3,4")));
            fail("Missing end parenthesis accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: missing end parenthesis
        }
    }

    public static void testComplexMethods()
    {
        Apcomplex a = new Apcomplex("(5, 7)");
        assertEquals("real", new Apfloat(5), a.real());
        assertEquals("imag", new Apfloat(7), a.imag());
        assertEquals("conj", new Apcomplex("(5, -7)"), a.conj());
    }

    public static void testRadix()
    {
        Apcomplex a = new Apcomplex(new Apfloat(2, 500, 5));
        assertEquals("radix 5", 5, a.radix());

        a = new Apcomplex(new Apfloat(2, 500, 6), new Apfloat(0, 500, 5));
        assertEquals("radix 6", 6, a.radix());

        a = new Apcomplex(new Apfloat(0, 500, 6), new Apfloat(2, 500, 7));
        assertEquals("radix 7", 7, a.radix());

        a = new Apcomplex(new Apfloat(0, 500, 8), new Apfloat(0, 500, 7));
        assertEquals("radix 8", 8, a.radix());

        a = new Apcomplex("(16,32)");
        assertEquals("10 -> 16", new Apcomplex(new Apfloat("10", 2, 16), new Apfloat("20", 2, 16)), a.toRadix(16));
    }

    public static void testPrecision()
    {
        Apcomplex a = new Apcomplex("(1, 1)");
        assertEquals("(1, 1) precision", 1, a.precision());
        a = new Apcomplex("(1, 10)");
        assertEquals("(1, 10) precision", 2, a.precision());
        a = new Apcomplex("(10, 1)");
        assertEquals("(10, 1) precision", 2, a.precision());
        a = new Apcomplex(new Apfloat(1), new Apfloat("1"));
        assertEquals("(1, 1.) precision", 1, a.precision());
        a = new Apcomplex(new Apfloat("1"), new Apfloat(1));
        assertEquals("(1., 1) precision", 1, a.precision());

        a = new Apcomplex(new Apfloat(1), new Apfloat(1));
        assertEquals("(infinite, infinite) -> 5", 5, a.precision(5).precision());
        assertEquals("(infinite, infinite) -> 5 real", 5, a.precision(5).real().precision());
        assertEquals("(infinite, infinite) -> 5 imag", 5, a.precision(5).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(1, 5));
        assertEquals("(5, 5) -> infinite", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).precision());
        assertEquals("(5, 5) -> infinite real", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).real().precision());
        assertEquals("(5, 5) -> infinite imag", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).imag().precision());
        a = new Apcomplex(new Apfloat(1), new Apfloat(1, 10));
        assertEquals("(infinite, 10) -> 5", 5, a.precision(5).precision());
        assertEquals("(infinite, 10) -> 5 real", 5, a.precision(5).real().precision());
        assertEquals("(infinite, 10) -> 5 imag", 5, a.precision(5).imag().precision());
        a = new Apcomplex(new Apfloat(1), new Apfloat(1, 5));
        assertEquals("(infinite, 5) -> 10", 10, a.precision(10).precision());
        assertEquals("(infinite, 5) -> 10 real", 10, a.precision(10).real().precision());
        assertEquals("(infinite, 5) -> 10 imag", 10, a.precision(10).imag().precision());
        a = new Apcomplex(new Apfloat(1, 10), new Apfloat(1));
        assertEquals("(10, infinite) -> 5", 5, a.precision(5).precision());
        assertEquals("(10, infinite) -> 5 real", 5, a.precision(5).real().precision());
        assertEquals("(10, infinite) -> 5 imag", 5, a.precision(5).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(1));
        assertEquals("(5, infinite) -> 10", 10, a.precision(10).precision());
        assertEquals("(5, infinite) -> 10 real", 10, a.precision(10).real().precision());
        assertEquals("(5, infinite) -> 10 imag", 10, a.precision(10).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(10, 6));
        assertEquals("(5, 6) -> 10", 10, a.precision(10).precision());
        assertEquals("(5, 6) -> 10 real", 9, a.precision(10).real().precision());
        assertEquals("(5, 6) -> 10 imag", 10, a.precision(10).imag().precision());
        a = new Apcomplex(new Apfloat(10, 6), new Apfloat(1, 5));
        assertEquals("(6, 5) -> 10", 10, a.precision(10).precision());
        assertEquals("(6, 5) -> 10 real", 10, a.precision(10).real().precision());
        assertEquals("(6, 5) -> 10 imag", 9, a.precision(10).imag().precision());
        a = new Apcomplex(new Apfloat(1, 10), new Apfloat(10, 11));
        assertEquals("(10, 11) -> 5", 5, a.precision(5).precision());
        assertEquals("(10, 11) -> 5 real", 4, a.precision(5).real().precision());
        assertEquals("(10, 11) -> 5 imag", 5, a.precision(5).imag().precision());
        a = new Apcomplex(new Apfloat(10, 11), new Apfloat(1, 10));
        assertEquals("(11, 10) -> 5", 5, a.precision(5).precision());
        assertEquals("(11, 10) -> 5 real", 5, a.precision(5).real().precision());
        assertEquals("(11, 10) -> 5 imag", 4, a.precision(5).imag().precision());
        a = new Apcomplex(new Apfloat(1), new Apfloat(1, 5));
        assertEquals("(infinite, 5) -> infinite", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).precision());
        assertEquals("(infinite, 5) -> infinite real", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).real().precision());
        assertEquals("(infinite, 5) -> infinite imag", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(1));
        assertEquals("(5, infinite) -> infinite", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).precision());
        assertEquals("(5, infinite) -> infinite real", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).real().precision());
        assertEquals("(5, infinite) -> infinite imag", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(1, 5));
        assertEquals("(5, 5) -> infinite", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).precision());
        assertEquals("(5, 5) -> infinite real", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).real().precision());
        assertEquals("(5, 5) -> infinite imag", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(10, 6));
        assertEquals("(5, 6) -> infinite", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).precision());
        assertEquals("(5, 6) -> infinite real", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).real().precision());
        assertEquals("(5, 6) -> infinite imag", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).imag().precision());
        a = new Apcomplex(new Apfloat(10, 6), new Apfloat(1, 5));
        assertEquals("(6, 5) -> infinite", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).precision());
        assertEquals("(6, 5) -> infinite real", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).real().precision());
        assertEquals("(6, 5) -> infinite imag", Apcomplex.INFINITE, a.precision(Apcomplex.INFINITE).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(1, 5));
        assertEquals("(5, 5) -> 5", 5, a.precision(5).precision());
        assertEquals("(5, 5) -> 5 real", 5, a.precision(5).real().precision());
        assertEquals("(5, 5) -> 5 imag", 5, a.precision(5).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(10, 6));
        assertEquals("(5, 6) -> 6", 6, a.precision(6).precision());
        assertEquals("(5, 6) -> 6 real", 5, a.precision(6).real().precision());
        assertEquals("(5, 6) -> 6 imag", 6, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(10, 6), new Apfloat(1, 5));
        assertEquals("(6, 5) -> 6", 6, a.precision(6).precision());
        assertEquals("(6, 5) -> 6 real", 6, a.precision(6).real().precision());
        assertEquals("(6, 5) -> 6 imag", 5, a.precision(6).imag().precision());

        a = new Apcomplex(new Apfloat(10), new Apfloat(1));
        assertEquals("(10/inf, 1/inf) -> 6 value", new Apcomplex(new Apfloat(10), new Apfloat(1)), a.precision(6));
        assertEquals("(10/inf, 1/inf) -> 6", 6, a.precision(6).precision());
        assertEquals("(10/inf, 1/inf) -> 6 real", 6, a.precision(6).real().precision());
        assertEquals("(10/inf, 1/inf) -> 6 imag", 5, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(1), new Apfloat(10));
        assertEquals("(1/inf, 10/inf) -> 6 value", new Apcomplex(new Apfloat(1), new Apfloat(10)), a.precision(6));
        assertEquals("(1/inf, 10/inf) -> 6", 6, a.precision(6).precision());
        assertEquals("(1/inf, 10/inf) -> 6 real", 5, a.precision(6).real().precision());
        assertEquals("(1/inf, 10/inf) -> 6 imag", 6, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(10), new Apfloat(1, 5));
        assertEquals("(10/inf, 1/5) -> 6 value", new Apcomplex(new Apfloat(10), new Apfloat(1)), a.precision(6));
        assertEquals("(10/inf, 1/5) -> 6", 6, a.precision(6).precision());
        assertEquals("(10/inf, 1/5) -> 6 real", 6, a.precision(6).real().precision());
        assertEquals("(10/inf, 1/5) -> 6 imag", 5, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(10));
        assertEquals("(1/5, 10/inf) -> 6 value", new Apcomplex(new Apfloat(1), new Apfloat(10)), a.precision(6));
        assertEquals("(1/5, 10/inf) -> 6", 6, a.precision(6).precision());
        assertEquals("(1/5, 10/inf) -> 6 real", 5, a.precision(6).real().precision());
        assertEquals("(1/5, 10/inf) -> 6 imag", 6, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(10, 10), new Apfloat(1, 5));
        assertEquals("(10/10, 1/5) -> 6 value", new Apcomplex(new Apfloat(10), new Apfloat(1)), a.precision(6));
        assertEquals("(10/10, 1/5) -> 6", 6, a.precision(6).precision());
        assertEquals("(10/10, 1/5) -> 6 real", 6, a.precision(6).real().precision());
        assertEquals("(10/10, 1/5) -> 6 imag", 5, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(1, 5), new Apfloat(10, 10));
        assertEquals("(1/5, 10/10) -> 6 value", new Apcomplex(new Apfloat(1), new Apfloat(10)), a.precision(6));
        assertEquals("(1/5, 10/10) -> 6", 6, a.precision(6).precision());
        assertEquals("(1/5, 10/10) -> 6 real", 5, a.precision(6).real().precision());
        assertEquals("(1/5, 10/10) -> 6 imag", 6, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(10, 6), new Apfloat(1));
        assertEquals("(10/6, 1/inf) -> 6 value", new Apcomplex(new Apfloat(10), new Apfloat(1)), a.precision(6));
        assertEquals("(10/6, 1/inf) -> 6", 6, a.precision(6).precision());
        assertEquals("(10/6, 1/inf) -> 6 real", 6, a.precision(6).real().precision());
        assertEquals("(10/6, 1/inf) -> 6 imag", 5, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(1), new Apfloat(10, 6));
        assertEquals("(1/inf, 10/6) -> 6 value", new Apcomplex(new Apfloat(1), new Apfloat(10)), a.precision(6));
        assertEquals("(1/inf, 10/6) -> 6", 6, a.precision(6).precision());
        assertEquals("(1/inf, 10/6) -> 6 real", 5, a.precision(6).real().precision());
        assertEquals("(1/inf, 10/6) -> 6 imag", 6, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(10, 6), new Apfloat(1, 10));
        assertEquals("(10/6, 1/10) -> 6 value", new Apcomplex(new Apfloat(10), new Apfloat(1)), a.precision(6));
        assertEquals("(10/6, 1/10) -> 6", 6, a.precision(6).precision());
        assertEquals("(10/6, 1/10) -> 6 real", 6, a.precision(6).real().precision());
        assertEquals("(10/6, 1/10) -> 6 imag", 5, a.precision(6).imag().precision());
        a = new Apcomplex(new Apfloat(1, 10), new Apfloat(10, 6));
        assertEquals("(1/10, 10/6) -> 6 value", new Apcomplex(new Apfloat(1), new Apfloat(10)), a.precision(6));
        assertEquals("(1/10, 10/6) -> 6", 6, a.precision(6).precision());
        assertEquals("(1/10, 10/6) -> 6 real", 5, a.precision(6).real().precision());
        assertEquals("(1/10, 10/6) -> 6 imag", 6, a.precision(6).imag().precision());

        a = new Apcomplex(new Apfloat(10), new Apfloat(0));
        assertEquals("(10, 0) -> 6 value", new Apcomplex(new Apfloat(10), new Apfloat(0)), a.precision(6));
        assertEquals("(10, 0) -> 6", 6, a.precision(6).precision());
        assertEquals("(10, 0) -> 6 real", 6, a.precision(6).real().precision());
        a = new Apcomplex(new Apfloat(0), new Apfloat(10));
        assertEquals("(0, 10) -> 6 value", new Apcomplex(new Apfloat(0), new Apfloat(10)), a.precision(6));
        assertEquals("(0, 10) -> 6", 6, a.precision(6).precision());
        assertEquals("(0, 10) -> 6 imag", 6, a.precision(6).imag().precision());

        a = new Apcomplex(new Apfloat(100000), new Apfloat(1));
        assertEquals("(100000, 1) -> 6 value", new Apcomplex(new Apfloat(100000), new Apfloat(1)), a.precision(6));
        a = new Apcomplex(new Apfloat(1), new Apfloat(100000));
        assertEquals("(1, 100000) -> 6 value", new Apcomplex(new Apfloat(1), new Apfloat(100000)), a.precision(6));
        a = new Apcomplex(new Apfloat(1000000), new Apfloat(1));
        assertEquals("(1000000, 1) -> 6 value", new Apcomplex(new Apfloat(1000000), new Apfloat(0)), a.precision(6));
        a = new Apcomplex(new Apfloat(1), new Apfloat(1000000));
        assertEquals("(1, 1000000) -> 6 value", new Apcomplex(new Apfloat(0), new Apfloat(1000000)), a.precision(6));

        a = new Apcomplex(new Apfloat(0), new Apfloat(0));
        assertEquals("(0, 0) -> 6 value", new Apcomplex(new Apfloat(0), new Apfloat(0)), a.precision(6));
    }

    public static void testNegate()
    {
        Apcomplex z = new Apcomplex("(2,3)");
        assertEquals("(2,3)", new Apcomplex("(-2,-3)"), z.negate());

        z = new Apcomplex("0");
        assertEquals("0", new Apcomplex("0"), z.negate());
    }

    public static void testScale()
    {
        Apcomplex a = new Apcomplex("(1, 1)");
        assertEquals("(1, 1) scale", 1, a.scale());
        a = new Apcomplex("(1, 10)");
        assertEquals("(1, 10) scale", 2, a.scale());
        a = new Apcomplex("(0, 10)");
        assertEquals("(0, 10) scale", 2, a.scale());
        a = new Apcomplex("(10, 0)");
        assertEquals("(10, 0) scale", 2, a.scale());
        a = new Apcomplex("0");
        assertEquals("0 scale", -Apfloat.INFINITE, a.scale());
    }

    public static void testSize()
    {
        Apcomplex a = new Apcomplex("(1, 1)");
        assertEquals("(1, 1) size", 1, a.size());
        a = new Apcomplex("(1, 11)");
        assertEquals("(1, 11) size", 2, a.size());
        a = new Apcomplex("(0, 10)");
        assertEquals("(0, 10) size", 1, a.size());
        a = new Apcomplex("(10, 0)");
        assertEquals("(10, 0) size", 1, a.size());
        a = new Apcomplex("0");
        assertEquals("0 size", 0, a.size());
    }

    public static void testAdd()
    {
        Apcomplex a = new Apcomplex("(2, 3)"),
                  b = new Apcomplex("(4, 5)");
        assertEquals("(2, 3) + (4, 5) precision", 1, a.add(b).precision());
        assertEquals("(2, 3) + (4, 5) value", new Apcomplex("(6, 8)"), a.add(b));
    }

    public static void testSubtract()
    {
        Apcomplex a = new Apcomplex("(2, 3)"),
                  b = new Apcomplex("(4, 1)");
        assertEquals("(2, 3) - (4, 1) precision", 1, a.subtract(b).precision());
        assertEquals("(2, 3) - (4, 1) value", new Apcomplex("(-2, 2)"), a.subtract(b));
    }

    public static void testMultiply()
    {
        Apcomplex a = new Apcomplex("(2.0, 3.0)"),
                  b = new Apcomplex("(4.0, 5.0)");
        assertEquals("(2, 3) * (4, 5) precision", 2, a.multiply(b).precision());
        assertEquals("(2, 3) * (4, 5) value", new Apcomplex("(-7, 22)"), a.multiply(b));

        a = new Apcomplex(new Apfloat(2, 50), new Apfloat("3e10", 100));
        b = new Apcomplex(new Apfloat("3e20", 100), new Apfloat(5, 50));
        assertEquals("a precision", 60, a.precision());
        assertEquals("b precision", 70, b.precision());
        assertEquals("(2, 3e10) * (3e20, 5) precision", 60, a.multiply(b).precision());
        assertEquals("(2, 3e10) * (3e20, 5) value", new Apcomplex("(599999999850000000000, 9000000000000000000000000000010)"), a.multiply(b));
    }

    public static void testDivide()
    {
        Apcomplex a = new Apcomplex("(-7,22)"),
                  b = new Apcomplex("(2.0,3.0)");
        assertEquals("(-7,22) / (2,3) precision", 2, a.divide(b).precision());
        assertEquals("(-7,22) / (2,3) value", new Apcomplex("(4,5)"), a.divide(b));
        assertEquals("0 / (2,3)", new Apcomplex("0"), new Apcomplex("0").divide(b));
        assertEquals("(2,3) / 1 precision", 1, b.divide(new Apcomplex("1")).precision());
        assertEquals("(2,3) / 1 value", new Apcomplex("(2,3)"), b.divide(new Apcomplex("1")));
        assertEquals("(2,3) / i precision", 1, b.divide(new Apcomplex("(0,1)")).precision());
        assertEquals("(2,3) / i value", new Apcomplex("(3,-2)"), b.divide(new Apcomplex("(0,1)")));
        assertEquals("(6,8) / 2 precision", 1, new Apcomplex("(6,8)").divide(new Apcomplex("2")).precision());
        assertEquals("(6,8) / 2 value", new Apcomplex("(3,4)"), new Apcomplex("(6,8)").divide(new Apcomplex("2")));
        assertEquals("(6,8) / 2i precision", 1, new Apcomplex("(6,8)").divide(new Apcomplex("(0,2)")).precision());
        assertEquals("(6,8) / 2i value", new Apcomplex("(4,-3)"), new Apcomplex("(6,8)").divide(new Apcomplex("(0,2)")));
        assertEquals("8 / 4 precision", 1, new Apcomplex("8").divide(new Apcomplex("4")).precision());
        assertEquals("8 / 4 value", new Apcomplex("2"), new Apcomplex("8").divide(new Apcomplex("4")));

        a = new Apcomplex("(202020202020202020202,303030303030303030303)");
        b = new Apcomplex("(0,101010101010101010101)");
        assertEquals("(202020202020202020202,303030303030303030303) / 101010101010101010101i precision", 21, a.divide(b).precision());
        assertEquals("(202020202020202020202,303030303030303030303) / 101010101010101010101i value", new Apcomplex("(3.00000000000000000000,-2.00000000000000000000)"), a.divide(b), new Apfloat("0.00000000000000000001"));
        a = new Apcomplex("(606060606060606060606,808080808080808080808)");
        b = new Apcomplex("202020202020202020202");
        assertEquals("(606060606060606060606,808080808080808080808) / 202020202020202020202 precision", 21, a.divide(b).precision());
        assertEquals("(606060606060606060606,808080808080808080808) / 202020202020202020202 value", new Apcomplex("(3.00000000000000000000,4.00000000000000000000)"), a.divide(b), new Apfloat("0.00000000000000000001"));
        b = new Apcomplex("(0,202020202020202020202)");
        assertEquals("(606060606060606060606,808080808080808080808) / 202020202020202020202i precision", 21, a.divide(b).precision());
        assertEquals("(606060606060606060606,808080808080808080808) / 202020202020202020202i value", new Apcomplex("(4.00000000000000000000,-3.00000000000000000000)"), a.divide(b), new Apfloat("0.00000000000000000001"));

        a = new Apcomplex("(-6172839504938271605,12345679009876543210)");
        b = new Apcomplex("(3333333333,4444444444)");
        assertEquals("(-6172839504938271605,12345679009876543210) / (3333333333,4444444444) precision", 10, a.divide(b).precision());
        assertEquals("(-6172839504938271605,12345679009876543210) / (3333333333,4444444444) value", new Apcomplex("(1111111111,2222222222)"), a.divide(b), new Apfloat(1));
        a = new Apcomplex("(-6172839504938271605,12345679009876543210)");
        b = new Apcomplex("(3333333333.0000000000,4444444444.0000000000)");
        assertEquals("(-6172839504938271605,12345679009876543210) / (3333333333.0000000000,4444444444.0000000000) precision", 20, a.divide(b).precision());
        assertEquals("(-6172839504938271605,12345679009876543210) / (3333333333.0000000000,4444444444.0000000000) value", new Apcomplex("(1111111111.0000000000,2222222222.0000000000)"), a.divide(b), new Apfloat("0.0000000001"));

        try
        {
            a.divide(new Apcomplex("0"));
            fail("Division by zero allowed");
        }
        catch (ArithmeticException ae)
        {
            // OK: division by zero
        }
    }

    public static void testNumberValues()
    {
        Apcomplex a = new Apcomplex("(5,6)");
        assertEquals("5 longValue", 5, a.longValue());
        assertEquals("5 intValue", 5, a.intValue());
        assertEquals("5 shortValue", 5, a.shortValue());
        assertEquals("5 byteValue", 5, a.byteValue());
        assertEquals("5 doubleValue", 5.0, a.doubleValue(), 0.0);
        assertEquals("5 floatValue", 5.0f, a.floatValue(), 0.0f);
    }

    public static void testEqualDigits()
    {
        Apcomplex a = new Apcomplex("(5,6)"),
                  b = new Apcomplex("(6,6)");
        assertEquals("(5,6) eq (6,6)", 0, a.equalDigits(b));
        a = new Apcomplex("(6,5)");
        b = new Apcomplex("(6,6)");
        assertEquals("(6,5) eq (6,6)", 0, a.equalDigits(b));
        a = new Apcomplex("(10,11)");
        b = new Apcomplex("(11,12)");
        assertEquals("(10,11) eq (11,12)", 1, a.equalDigits(b));
        a = new Apcomplex("(10,11)");
        b = new Apcomplex("(10,12)");
        assertEquals("(10,11) eq (10,12)", 1, a.equalDigits(b));
        a = new Apcomplex("(10,12)");
        b = new Apcomplex("(11,12)");
        assertEquals("(10,12) eq (11,12)", 1, a.equalDigits(b));
        a = new Apcomplex("(10,11)");
        b = new Apcomplex("(10,11)");
        assertEquals("(10,11) eq (10,11)", 2, a.equalDigits(b));
        a = new Apcomplex(new Apfloat(10), new Apfloat(11));
        b = new Apcomplex(new Apfloat(10),new Apfloat(11));
        assertEquals("(10,11) eq (10,11) infinite", Apfloat.INFINITE, a.equalDigits(b));
        a = new Apcomplex("0");
        b = new Apcomplex("0");
        assertEquals("0 eq 0", Apfloat.INFINITE, a.equalDigits(b));
        a = new Apcomplex("0");
        b = new Apcomplex("1");
        assertEquals("0 eq 1", 0, a.equalDigits(b));
        a = new Apcomplex("1");
        b = new Apcomplex("0");
        assertEquals("1 eq 0", 0, a.equalDigits(b));
        a = new Apcomplex("0");
        b = new Apcomplex("(0,1)");
        assertEquals("0 eq i", 0, a.equalDigits(b));
        a = new Apcomplex("(0,1)");
        b = new Apcomplex("0");
        assertEquals("i eq 0", 0, a.equalDigits(b));

        a = new Apcomplex("(1000,100)");
        b = new Apcomplex("(1000,100)");
        assertEquals("(1000,100) eq (1000,100)", 4, a.equalDigits(b));
        a = new Apcomplex(new Apfloat(1000), new Apfloat(100));
        b = new Apcomplex(new Apfloat(1000), new Apfloat(100));
        assertEquals("(1000,100) eq (1000,100) infinite", Apfloat.INFINITE, a.equalDigits(b));
        a = new Apcomplex("(1000,100)");
        b = new Apcomplex("(1000,101)");
        assertEquals("(1000,100) eq (1000,101)", 3, a.equalDigits(b));

        a = new Apcomplex("(1000,0)");
        b = new Apcomplex("(1000,100)");
        assertEquals("(1000,0) eq (1000,100)", 1, a.equalDigits(b));
        a = new Apcomplex("(1000,1)");
        b = new Apcomplex("(1000,0)");
        assertEquals("(1000,1) eq (1000,0)", 3, a.equalDigits(b));

        a = new Apcomplex("(0.01000,0.00100)");
        b = new Apcomplex("(0.01000,0.00100)");
        assertEquals("(0.01000,0.00100) eq (0.01000,0.00100)", 4, a.equalDigits(b));
        a = new Apcomplex(new Apfloat("0.01000", Apfloat.INFINITE), new Apfloat("0.00100", Apfloat.INFINITE));
        b = new Apcomplex(new Apfloat("0.01000", Apfloat.INFINITE), new Apfloat("0.00100", Apfloat.INFINITE));
        assertEquals("(0.01000,0.00100) eq (0.01000,0.00100) infinite", Apfloat.INFINITE, a.equalDigits(b));
        a = new Apcomplex("(0.01000,0.00100)");
        b = new Apcomplex("(0.01000,0.00101)");
        assertEquals("(0.01000,0.00100) eq (0.01000,0.00101)", 3, a.equalDigits(b));

        a = new Apcomplex("(0.01000,0)");
        b = new Apcomplex("(0.01000,0.00100)");
        assertEquals("(0.01000,0) eq (0.01000,0.00100)", 1, a.equalDigits(b));
        a = new Apcomplex("(0.01000,0.00001)");
        b = new Apcomplex("(0.01000,0)");
        assertEquals("(0.01000,0.00001) eq (0.01000,0)", 3, a.equalDigits(b));

        a = new Apcomplex("(1000,0)");
        b = new Apcomplex("(1001,0)");
        assertEquals("(1000,0) eq (1001,0)", 3, a.equalDigits(b));

        a = new Apcomplex("(0,1000)");
        b = new Apcomplex("(0,1001)");
        assertEquals("(0,1000) eq (0,1001)", 3, a.equalDigits(b));

        a = new Apcomplex("5e-1");
        b = new Apcomplex("1e-38");
        assertEquals("5e-1 eq 1e-38", 0, a.equalDigits(b));

        a = new Apcomplex("1.5e-2");
        b = new Apcomplex("4.0e-3");
        assertEquals("1.5e-2 eq 4.0e-3", 0, a.equalDigits(b));
    }

    public static void testEquals()
    {
        Apcomplex a = new Apcomplex("0"),
                  b = new Apcomplex("1");
        assertEquals("0 == 1", false, a.equals(b));
        a = new Apcomplex("(5,5)");
        b = new Apcomplex("(5,6)");
        assertEquals("(5,5) == (5,6)", false, a.equals(b));
        a = new Apcomplex("(5,6)");
        b = new Apcomplex("(5,5)");
        assertEquals("(5,6) == (5,5)", false, a.equals(b));
        a = new Apcomplex("(6,6)");
        b = new Apcomplex("(6,6)");
        assertEquals("(6,6) == (6,6)", true, a.equals(b));
        assertEquals("a == a", true, a.equals(a));

        assertEquals("(6,6) == something else", false, a.equals("bogus"));
    }

    public static void testHashCode()
    {
        Apcomplex a = new Apcomplex("0"),
                  b = new Apcomplex("1");
        assertTrue("0 != 1", a.hashCode() != b.hashCode());
        a = new Apcomplex("(5,5)");
        b = new Apcomplex("(5,6)");
        assertTrue("(5,5) != (5,6)", a.hashCode() != b.hashCode());
        a = new Apcomplex("(6,5)");
        b = new Apcomplex("(5,6)");
        assertTrue("(6,5) != (5,6)", a.hashCode() != b.hashCode());
        a = new Apcomplex("(6,6)");
        b = new Apcomplex("(6,6)");
        assertEquals("(6,6) == (6,6)", a.hashCode(), b.hashCode());
    }

    public static void testToString()
    {
        Apcomplex a = new Apcomplex("0");
        assertEquals("0", "0", "" + a);
        a = new Apcomplex("6");
        assertEquals("6", "6", "" + a);
        a = new Apcomplex("(5,6)");
        assertEquals("(5,6)", "(5, 6)", "" + a);
        a = new Apcomplex("(50,60)");
        assertEquals("(50,60)", "(5e1, 6e1)", "" + a);
        a = new Apcomplex("(50,60)");
        assertEquals("(50,60) pretty", "(50, 60)", a.toString(true));
    }

    public static void testWriteTo()
        throws IOException
    {
        StringWriter out = new StringWriter();
        Apcomplex a = new Apcomplex("0");
        a.writeTo(out);
        a = new Apcomplex("6");
        a.writeTo(out);
        a = new Apcomplex("(50,60)");
        a.writeTo(out);
        a.writeTo(out, true);
        assertEquals("string", "06(5e1, 6e1)(50, 60)", out.toString());
    }

    public static void testFormatTo()
        throws IOException
    {
        Locale locale = null;
        assertEquals("null %s", "(1.23456789e5, 1.23456e2)", String.format(locale, "%s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %S", "(1.23456789E5, 1.23456E2)", String.format(locale, "%S", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %#s", "(123456.789, 123.456)", String.format(locale, "%#s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %.3s", "(1.23e5, 1.23e2)", String.format(locale, "%.3s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %.26s", "(1.23456789e5, 1.23456e2)", String.format(locale, "%.26s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %26s", " (1.23456789e5, 1.23456e2)", String.format(locale, "%26s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %-26s", "(1.23456789e5, 1.23456e2) ", String.format(locale, "%-26s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %#26s", "     (123456.789, 123.456)", String.format(locale, "%#26s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("null %#-26s", "(123456.789, 123.456)     ", String.format(locale, "%#-26s", new Apcomplex("(123456.789,123.456)")));

        assertEquals("null %s apfloat", "1.23456789e5", String.format(locale, "%s", new Apcomplex("123456.789")));

        locale = new Locale("fi", "FI");
        assertEquals("fi_FI %s", "(1,23456789e5, 1,23456e2)", String.format(locale, "%s", new Apcomplex("(123456.789,123.456)")));
        assertEquals("fi_FI %s radix 11", "(1,23456e5, 1,23456e5)", String.format(locale, "%s", new Apcomplex(new Apfloat("123456", 6, 11), new Apfloat("123456", 6, 11))));

        locale = new Locale("hi", "IN");
        assertEquals("hi_IN %#.6s", "(\u0967\u0968\u0969\u096a\u096b\u096c, \u0967\u0968\u0969\u096a\u096b\u096c)", String.format(locale, "%#.6s", new Apcomplex(new Apfloat("123456.7890123456"), new Apfloat("123456.7890123456"))));
        assertEquals("hi_IN %#s radix 9", "(\u0967\u0968\u0969\u096a\u096b\u096c, \u0967\u0968\u0969\u096a\u096b\u096c)", String.format(locale, "%#s", new Apcomplex(new Apfloat("123456", 6, 9), new Apfloat("123456", 6, 9))));
        assertEquals("hi_IN %s radix 11", "(1.23456e5, 1.23456e5)", String.format(locale, "%s", new Apcomplex(new Apfloat("123456", 6, 11), new Apfloat("123456", 6, 11))));

        Writer writer = new Writer()
        {
             public void write(char cbuf[], int off, int len)
                 throws IOException
             {
                 throw new IOException();
             }

             public void flush()
                 throws IOException
             {
                 throw new IOException();
             }

             public void close()
                 throws IOException
             {
                 throw new IOException();
             }
        };
        Formatter formatter = new Formatter(writer);
        new Apcomplex("123456.789").formatTo(formatter, 0, -1, -1);
    }

    public static void testSerialization()
        throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        Apcomplex a = new Apcomplex("(5,6)");
        out.writeObject(a);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        Apcomplex b = (Apcomplex) in.readObject();
        assertEquals("(5,6) equals", a, b);
        assertNotSame("(5,6) !=", a, b);
    }
}
