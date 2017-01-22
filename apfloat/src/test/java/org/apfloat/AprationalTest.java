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

import java.math.BigInteger;
import java.io.PushbackReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.IllegalFormatException;

import junit.framework.TestSuite;

/**
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class AprationalTest
    extends ApfloatTestCase
{
    public AprationalTest(String methodName)
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

        suite.addTest(new AprationalTest("testApintConstructor"));
        suite.addTest(new AprationalTest("testStringConstructor"));
        suite.addTest(new AprationalTest("testStreamConstructor"));
        suite.addTest(new AprationalTest("testBigIntegerConstructor"));
        suite.addTest(new AprationalTest("testRationalMethods"));
        suite.addTest(new AprationalTest("testIsShort"));
        suite.addTest(new AprationalTest("testRadix"));
        suite.addTest(new AprationalTest("testPrecision"));
        suite.addTest(new AprationalTest("testScale"));
        suite.addTest(new AprationalTest("testSize"));
        suite.addTest(new AprationalTest("testNegate"));
        suite.addTest(new AprationalTest("testAdd"));
        suite.addTest(new AprationalTest("testSubtract"));
        suite.addTest(new AprationalTest("testMultiply"));
        suite.addTest(new AprationalTest("testDivide"));
        suite.addTest(new AprationalTest("testMod"));
        suite.addTest(new AprationalTest("testFloor"));
        suite.addTest(new AprationalTest("testCeil"));
        suite.addTest(new AprationalTest("testTruncate"));
        suite.addTest(new AprationalTest("testFrac"));
        suite.addTest(new AprationalTest("testRoundAway"));
        suite.addTest(new AprationalTest("testAbs"));
        suite.addTest(new AprationalTest("testCompareToHalf"));
        suite.addTest(new AprationalTest("testNumberValues"));
        suite.addTest(new AprationalTest("testEqualDigits"));
        suite.addTest(new AprationalTest("testCompareTo"));
        suite.addTest(new AprationalTest("testEquals"));
        suite.addTest(new AprationalTest("testHashCode"));
        suite.addTest(new AprationalTest("testToString"));
        suite.addTest(new AprationalTest("testWriteTo"));
        suite.addTest(new AprationalTest("testFormatTo"));
        suite.addTest(new AprationalTest("testSerialization"));

        return suite;
    }

    public static void testApintConstructor()
    {
        Aprational a = new Aprational(new Apint(5));
        assertEquals("5 String", "5", a.toString());
        a = new Aprational(new Apint(5), new Apint(6));
        assertEquals("5/6 String", "5/6", a.toString());
        a = new Aprational(new Apint(0), new Apint(6));
        assertEquals("0/6 String", "0", a.toString());
        assertEquals("0/6 denominator", new Apint(1), a.denominator());

        a = new Aprational(new Apint(5), new Apint(-6));
        assertEquals("5/-6 numerator", new Apint(-5), a.numerator());
        assertEquals("5/-6 denominatoror", new Apint(6), a.denominator());
        assertEquals("5/-6 String", "-5/6", a.toString());

        a = new Aprational(new Apint(-5), new Apint(6));
        assertEquals("-5/6 numerator", new Apint(-5), a.numerator());
        assertEquals("-5/6 denominatoror", new Apint(6), a.denominator());
        assertEquals("-5/6 String", "-5/6", a.toString());

        a = new Aprational(new Apint(-5), new Apint(-6));
        assertEquals("-5/-6 numerator", new Apint(5), a.numerator());
        assertEquals("-5/-6 denominatoror", new Apint(6), a.denominator());
        assertEquals("-5/-6 String", "5/6", a.toString());

        a = new Aprational(new Apint(15), new Apint(-12));
        assertEquals("15/-12 numerator", new Apint(-5), a.numerator());
        assertEquals("15/-12 denominatoror", new Apint(4), a.denominator());
        assertEquals("15/-12 String", "-5/4", a.toString());

        a = new Aprational(new Apint(1, 10), new Apint(6, 12));
        a = new Aprational(new Apint(6, 10), new Apint(1, 12));

        try
        {
            a = new Aprational(new Apint(2), new Apint(0));
            fail("2/0 accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: result would be infinite
        }
    }

    public static void testStringConstructor()
    {
        Apfloat a = new Aprational("5");
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 String", "5", a.toString());
        a = new Aprational("0/6");
        assertEquals("0/6 String", "0", a.toString());
        a = new Aprational("0/-6");
        assertEquals("0/-6 String", "0", a.toString());
        a = new Aprational("5/6");
        assertEquals("5/6 String", "5/6", a.toString());
        a = new Aprational("5 / 6");
        assertEquals("5 / 6 String", "5/6", a.toString());
        a = new Aprational("-5/6");
        assertEquals("-5/6 String", "-5/6", a.toString());
        a = new Aprational("5/-6");
        assertEquals("5/-6 String", "-5/6", a.toString());
        a = new Aprational("-5/-6");
        assertEquals("-5/-6 String", "5/6", a.toString());
        a = new Aprational("55/67");
        assertEquals("55/67 String", "55/67", a.toString());

        a = new Aprational("ab/-bc", 13);
        assertEquals("ab/-bc radix", 13, a.radix());
        assertEquals("ab/-bc String", "-ab/bc", a.toString());

        try
        {
            a = new Aprational("2/");
            fail("2/ accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: illegal number
        }

        try
        {
            a = new Aprational("2/0");
            fail("2/0 accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: result would be infinite
        }
    }

    public static void testStreamConstructor()
        throws IOException
    {
        PushbackReader in = new PushbackReader(new StringReader("5"));
        Apfloat a = new Aprational(in);
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 String", "5", a.toString());
        in = new PushbackReader(new StringReader("5/6"));
        a = new Aprational(in);
        assertEquals("5/6 String", "5/6", a.toString());
        in = new PushbackReader(new StringReader("5 / 6"));
        a = new Aprational(in);
        assertEquals("5 / 6 String", "5/6", a.toString());
        in = new PushbackReader(new StringReader("55/67"));
        a = new Aprational(in);
        assertEquals("55/67 String", "55/67", a.toString());

        in = new PushbackReader(new StringReader("ab/-bc"));
        a = new Aprational(in, 13);
        assertEquals("ab/-bc radix", 13, a.radix());
        assertEquals("ab/-bc String", "-ab/bc", a.toString());

        in = new PushbackReader(new StringReader("5*"));
        a = new Aprational(in);
        assertEquals("5* String", "5", a.toString());
        assertEquals("5* next char", '*', in.read());

        try
        {
            in = new PushbackReader(new StringReader("2/"));
            a = new Aprational(in);
            fail("2/ accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: illegal number
        }

        try
        {
            in = new PushbackReader(new StringReader("2/0"));
            a = new Aprational(in);
            fail("2/0 accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: result would be infinite
        }
    }

    public static void testBigIntegerConstructor()
    {
        Aprational a = new Aprational(BigInteger.valueOf(5));
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 String", "5", a.toString());
        a = new Aprational(BigInteger.valueOf(11), 12);
        assertEquals("11 radix", 12, a.radix());
        assertEquals("11 String", "b", a.toString());
    }

    public static void testRationalMethods()
    {
        Aprational a = new Aprational("5/8");
        assertEquals("numerator", new Apint(5), a.numerator());
        assertEquals("denominator", new Apint(8), a.denominator());
    }

    public static void testIsShort()
    {
        Aprational a = new Aprational("5");
        assertTrue("5", a.isShort());

        a = new Aprational(new Apint(Long.MAX_VALUE));
        assertFalse("MAX", a.isShort());

        a = new Aprational("1/2");
        assertFalse("1/2", a.isShort());
    }

    public static void testRadix()
    {
        Aprational a = new Aprational("2", 5);
        assertEquals("radix", 5, a.radix());

        a = new Aprational(Apint.ONE, new Apint(2, 3));
        assertEquals("1/2", 3, a.radix());

        a = new Aprational("16/255");
        assertEquals("10 -> 16", new Aprational("10/FF", 16), a.toRadix(16));
    }

    public static void testPrecision()
    {
        Aprational a = new Aprational("5");
        assertEquals("precision", Aprational.INFINITE, a.precision());

        a = new Aprational("2/3");
        assertEquals("approx 50", new Apfloat("0.66666666666666666666666666666666666666666666666666"), a.precision(50));
        assertEquals("approx 100", new Apfloat("0.6666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666"), a.precision(100));
        assertEquals("approx 60", new Apfloat("0.666666666666666666666666666666666666666666666666666666666666"), a.precision(60));

        a = new Aprational(new Apint(2), new Apint(Long.MAX_VALUE));
        assertEquals("MAX approx 50 precision", 50, a.precision(50).precision());
    }

    public static void testScale()
    {
        Aprational a = new Aprational("9");
        assertEquals("9 scale", 1, a.scale());
        assertEquals("9 scale cached", 1, a.scale());
        a = new Aprational("10");
        assertEquals("10 scale", 2, a.scale());
        a = new Aprational("1/10");
        assertEquals("1/10 scale", 0, a.scale());
        a = new Aprational("9/10");
        assertEquals("9/10 scale", 0, a.scale());
        a = new Aprational("1/100");
        assertEquals("1/100 scale", -1, a.scale());
        a = new Aprational("9/100");
        assertEquals("9/100 scale", -1, a.scale());
        a = new Aprational("99/100");
        assertEquals("99/100 scale", 0, a.scale());
        a = new Aprational("1/101");
        assertEquals("1/101 scale", -2, a.scale());
        a = new Aprational("1/2");
        assertEquals("1/2 scale", 0, a.scale());
        a = new Aprational("1/3");
        assertEquals("1/3 scale", 0, a.scale());
        a = new Aprational("1/9");
        assertEquals("1/9 scale", 0, a.scale());
        a = new Aprational("1/11");
        assertEquals("1/11 scale", -1, a.scale());
        a = new Aprational("0");
        assertEquals("0 scale", -Aprational.INFINITE, a.scale());
        a = new Aprational("101010101010101010101010101010101010101010101/101010101010101010101010101010101010101010102");
        assertEquals("101...101/101...102 scale", 0, a.scale());
        a = new Aprational("101010101010101010101010101010101010101010102/101010101010101010101010101010101010101010101");
        assertEquals("101...102/101...101 scale", 1, a.scale());

        a = new Aprational("9");
        assertEquals("9 scaled 1", new Aprational("90"), a.scale(1));
        assertEquals("9 scaled -1", new Aprational("9/10"), a.scale(-1));
    }

    public static void testSize()
    {
        Aprational a = new Aprational("9");
        assertEquals("9 size", 1, a.size());
        a = new Aprational("10");
        assertEquals("10 size", 1, a.size());
        a = new Aprational("1/10");
        assertEquals("1/10 size", 1, a.size());
        a = new Aprational("9/10");
        assertEquals("9/10 size", 1, a.size());
        a = new Aprational("-99/100");
        assertEquals("-99/100 size", 2, a.size());
        a = new Aprational("199/2");
        assertEquals("199/2 size", 3, a.size());
        a = new Aprational("1/2");
        assertEquals("1/2 size", 1, a.size());
        a = new Aprational("1/3");
        assertEquals("1/3 size", Aprational.INFINITE, a.size());
        a = new Aprational("1/18446744073709551616");
        assertEquals("1/2^64 size", 45, a.size());
        a = new Aprational("1/542101086242752217003726400434970855712890625");
        assertEquals("1/5^64 size", 20, a.size());
        a = new Aprational("1/7orp63sh4dphi", 34);
        assertEquals("1/2^64 radix 34 size", 52, a.size());
        a = new Aprational("1/18446744073709551617");
        assertEquals("1/(2^64+1) size", Aprational.INFINITE, a.size());
        a = new Aprational("-1/18446744073709551617");
        assertEquals("-1/(2^64+1) size", Aprational.INFINITE, a.size());
        a = new Aprational("101010101010101010101010101010101010101010102/101010101010101010101010101010101010101010101");
        assertEquals("101...102/101...101 size", Aprational.INFINITE, a.size());
        a = new Aprational("-101010101010101010101010101010101010101010102/101010101010101010101010101010101010101010101");
        assertEquals("-101...102/101...101 size", Aprational.INFINITE, a.size());
        a = new Aprational("0");
        assertEquals("0 size", 0, a.size());
    }

    public static void testNegate()
    {
        Aprational x = new Aprational("2/3");
        assertEquals("2/3", new Aprational("-2/3"), x.negate());

        x = new Aprational("-2/3");
        assertEquals("-2/3", new Aprational("2/3"), x.negate());

        x = new Aprational("0");
        assertEquals("0", new Aprational("0"), x.negate());

        Apfloat y = new Aprational("2/3");
        assertEquals("-2", new Aprational("-2/3"), y.negate());
    }

    public static void testAdd()
    {
        Aprational a = new Aprational("1/5"),
                   b = new Aprational("2/5");
        assertEquals("1/5 + 2/5", new Aprational("3/5"), a.add(b));
        assertEquals("1/5 + 0", new Aprational("1/5"), a.add(new Aprational("0")));
        assertEquals("0 + 1/5", new Aprational("1/5"), Aprational.ZERO.add(a));

        a = new Aprational("1/3");
        b = new Aprational("2/3");
        assertEquals("1/3 + 2/3", new Aprational("1"), a.add(b));

        a = new Aprational("1/3");
        b = new Aprational("-1/3");
        assertEquals("1/3 + -1/3", new Aprational("0"), a.add(b));
        assertEquals("1/3 + -1/3 denominator", new Apint(1), a.add(b).denominator());

        a = new Aprational("1/24");
        b = new Aprational("5/24");
        assertEquals("1/24 + 5/24", new Aprational("1/4"), a.add(b));

        a = new Aprational("2", 12);
        b = new Aprational("3", 12);
        assertEquals("2 + 3", new Aprational("5", 12), a.add(b));
    }

    public static void testSubtract()
    {
        Aprational a = new Aprational("1/5"),
                   b = new Aprational("2/5");
        assertEquals("1/5 - 2/5", new Aprational("-1/5"), a.subtract(b));
        assertEquals("1/5 - 0", new Aprational("1/5"), a.subtract(new Aprational("0")));
        assertEquals("0 - 1/5", new Aprational("-1/5"), Aprational.ZERO.subtract(a));

        a = new Aprational("-1/3");
        b = new Aprational("2/3");
        assertEquals("-1/3 - 2/3", new Aprational("-1"), a.subtract(b));

        a = new Aprational("1/3");
        b = new Aprational("1/3");
        assertEquals("1/3 - 1/3", new Aprational("0"), a.subtract(b));
        assertEquals("1/3 - 1/3 denominator", new Apint(1), a.subtract(b).denominator());

        a = new Aprational("7/24");
        b = new Aprational("5/24");
        assertEquals("7/24 - 5/24", new Aprational("1/12"), a.subtract(b));

        a = new Aprational("2", 12);
        b = new Aprational("7", 12);
        assertEquals("2 - 7", new Aprational("-5", 12), a.subtract(b));
    }

    public static void testMultiply()
    {
        Aprational a = new Aprational("1/5"),
                   b = new Aprational("2/5");
        assertEquals("1/5 * 2/5", new Aprational("2/25"), a.multiply(b));
        assertEquals("1/5 * 0", new Aprational("0"), a.multiply(new Aprational("0")));
        assertEquals("0 * 1/5", new Aprational("0"), Aprational.ZERO.multiply(a));
        assertEquals("1/5 * 1", new Aprational("1/5"), a.multiply(new Aprational("1")));
        assertEquals("1 * 1/5", new Aprational("1/5"), new Aprational("1").multiply(a));
        assertEquals("1/5 * ONE", new Aprational("1/5", 12), new Aprational("1/5", 12).multiply(Aprational.ONE));
        assertEquals("ONE * 1/5", new Aprational("1/5", 12), Aprational.ONE.multiply(new Aprational("1/5", 12)));

        a = new Aprational("1/3");
        b = new Aprational("3/2");
        assertEquals("1/3 * 3/2", new Aprational("1/2"), a.multiply(b));

        a = new Aprational("1/3");
        b = new Aprational("-3");
        assertEquals("1/3 * -3", new Aprational("-1"), a.multiply(b));
    }

    public static void testDivide()
    {
        Aprational a = new Aprational("1/5"),
                   b = new Aprational("2/5");
        assertEquals("1/5 / 2/5", new Aprational("1/2"), a.divide(b));
        assertEquals("0 / 1/5", new Aprational("0"), Aprational.ZERO.divide(a));
        assertEquals("1/5 / 1", new Aprational("1/5"), a.divide(new Aprational("1")));
        assertEquals("1/5 / ONE", new Aprational("1/5", 12), new Aprational("1/5", 12).divide(Aprational.ONE));

        a = new Aprational("1/3");
        b = new Aprational("3/2");
        assertEquals("1/3 / 3/2", new Aprational("2/9"), a.divide(b));

        a = new Aprational("1/3");
        b = new Aprational("-1/3");
        assertEquals("1/3 / -1/3", new Aprational("-1"), a.divide(b));

        try
        {
            a.divide(new Aprational("0"));
            fail("Division by zero allowed");
        }
        catch (ArithmeticException ae)
        {
            // OK: division by zero
        }
    }

    public static void testMod()
    {
        assertEquals("0, 1", new Aprational("0"), new Aprational("0").mod(Aprational.ONE));
        assertEquals("1, 0", new Aprational("0"), new Aprational("1").mod(Aprational.ZERO));

        assertEquals("2, 3", new Aprational("2"), new Aprational("2").mod(new Aprational("3")));
        assertEquals("2, -3", new Aprational("2"), new Aprational("2").mod(new Aprational("-3")));
        assertEquals("-2, 3", new Aprational("-2"), new Aprational("-2").mod(new Aprational("3")));
        assertEquals("-2, -3", new Aprational("-2"), new Aprational("-2").mod(new Aprational("-3")));

        assertEquals("3, 2", new Aprational("1"), new Aprational("3").mod(new Aprational("2")));
        assertEquals("3, -2", new Aprational("1"), new Aprational("3").mod(new Aprational("-2")));
        assertEquals("-3, 2", new Aprational("-1"), new Aprational("-3").mod(new Aprational("2")));
        assertEquals("-3, -2", new Aprational("-1"), new Aprational("-3").mod(new Aprational("-2")));

        assertEquals("11/4, 2/3", new Aprational("1/12"), new Aprational("11/4").mod(new Aprational("2/3")));
        assertEquals("11/4, -2/3", new Aprational("1/12"), new Aprational("11/4").mod(new Aprational("-2/3")));
        assertEquals("-11/4, 2/3", new Aprational("-1/12"), new Aprational("-11/4").mod(new Aprational("2/3")));
        assertEquals("-11/4, -2/3", new Aprational("-1/12"), new Aprational("-11/4").mod(new Aprational("-2/3")));
    }

    public static void testFloor()
    {
        Aprational a = new Aprational("11/10");
        assertEquals("11/10 floor", new Apint(1), a.floor());
        assertEquals("11/10 floor reverse", a.floor(), new Apint(1));
        a = new Aprational("1");
        assertEquals("1 floor", new Apint(1), a.floor());
        a = new Aprational("199999999999999/200000000000000");
        assertEquals("199999999999999/200000000000000 floor", new Apint(0), a.floor());
        a = new Aprational("-11/10");
        assertEquals("-11/10 floor", new Apint(-2), a.floor());
        a = new Aprational("0");
        assertEquals("0 floor", new Apint(0), a.floor());
    }

    public static void testCeil()
    {
        Aprational a = new Aprational("11/10");
        assertEquals("11/10 ceil", new Apint(2), a.ceil());
        assertEquals("11/10 ceil reverse", a.ceil(), new Apint(2));
        a = new Aprational("1");
        assertEquals("1 ceil", new Apint(1), a.ceil());
        a = new Aprational("199999999999999/200000000000000");
        assertEquals("199999999999999/200000000000000 ceil", new Apint(1), a.ceil());
        a = new Aprational("200000000000001/200000000000000");
        assertEquals("200000000000001/200000000000000 ceil", new Apint(2), a.ceil());
        a = new Aprational("-11/10");
        assertEquals("-11/10 ceil", new Apint(-1), a.ceil());
        a = new Aprational("0");
        assertEquals("0 ceil", new Apint(0), a.ceil());
    }

    public static void testTruncate()
    {
        Aprational a = new Aprational("11/10");
        assertEquals("11/10 truncate", new Apint(1), a.truncate());
        assertEquals("11/10 truncate reverse", a.truncate(), new Apint(1));
        a = new Aprational("1");
        assertEquals("1 truncate", new Apint(1), a.truncate());
        a = new Aprational("199999999999999/200000000000000");
        assertEquals("199999999999999/200000000000000 truncate", new Apint(0), a.truncate());
        a = new Aprational("200000000000001/200000000000000");
        assertEquals("200000000000001/200000000000000 truncate", new Apint(1), a.truncate());
        a = new Aprational("-11/10");
        assertEquals("-11/10 truncate", new Apint(-1), a.truncate());
        a = new Aprational("0");
        assertEquals("0 truncate", new Apint(0), a.truncate());
    }

    public static void testFrac()
    {
        Aprational a = new Aprational("11/10");
        assertEquals("11/10 frac", new Aprational("1/10"), a.frac());
        assertEquals("11/10 frac reverse", a.frac(), new Aprational("1/10"));
        a = new Aprational("1");
        assertEquals("1 frac", new Apint(0), a.frac());
        a = new Aprational("199999999999999/200000000000000");
        assertEquals("199999999999999/200000000000000 frac", new Aprational("199999999999999/200000000000000"), a.frac());
        a = new Aprational("200000000000001/200000000000000");
        assertEquals("200000000000001/200000000000000 frac", new Aprational("1/200000000000000"), a.frac());
        a = new Aprational("-11/10");
        assertEquals("-11/10 frac", new Aprational("-1/10"), a.frac());
        a = new Aprational("0");
        assertEquals("0 frac", new Apint(0), a.frac());
    }

    public static void testRoundAway()
    {
        Aprational a = new Aprational("11/10");
        assertEquals("1.1 roundAway", new Apint(2), a.roundAway());
        a = new Aprational("-11/10");
        assertEquals("-1.1 roundAway", new Apint(-2), a.roundAway());
    }

    public static void testAbs()
    {
        Aprational a = new Aprational("2");
        assertEquals("2 abs", new Aprational("2"), a.abs());
        a = new Aprational("0");
        assertEquals("0 abs", new Aprational("0"), a.abs());
        a = new Aprational("-2");
        assertEquals("-2 abs", new Aprational("2"), a.abs());
    }

    public static void testCompareToHalf()
    {
        Aprational a = new Aprational("4/10");
        assertEquals("0.4 compareToHalf", -1, a.compareToHalf());
        a = new Aprational("5/10");
        assertEquals("0.5 compareToHalf", 0, a.compareToHalf());
        a = new Aprational("6/10");
        assertEquals("0.6 compareToHalf", 1, a.compareToHalf());
    }

    public static void testNumberValues()
    {
        Aprational a = new Aprational("5");
        assertEquals("5 longValue", 5, a.longValue());
        assertEquals("5 intValue", 5, a.intValue());
        assertEquals("5 shortValue", 5, a.shortValue());
        assertEquals("5 byteValue", 5, a.byteValue());
        assertEquals("5 floatValue", 5.0f, a.floatValue(), 0.0f);
        assertEquals("5 doubleValue", 5.0, a.doubleValue(), 0.0);

        a = new Aprational("1/3");
        assertEquals("1/3 longValue", 0, a.longValue());
        assertEquals("1/3 intValue", 0, a.intValue());
        assertEquals("1/3 shortValue", 0, a.shortValue());
        assertEquals("1/3 byteValue", 0, a.byteValue());
        assertEquals("1/3 floatValue", 0.333333333333f, a.floatValue(), Math.ulp(0.333333333333f));
        assertEquals("1/3 doubleValue", 0.33333333333333333333, a.doubleValue(), Math.ulp(0.33333333333333333333));

        a = new Aprational("2000000000000000000000001/2000000000000000000000000");
        assertEquals("2000000000000000000000001/2000000000000000000000000 longValue", 1, a.longValue());
        assertEquals("2000000000000000000000001/2000000000000000000000000 intValue", 1, a.intValue());
        assertEquals("2000000000000000000000001/2000000000000000000000000 shortValue", 1, a.shortValue());
        assertEquals("2000000000000000000000001/2000000000000000000000000 byteValue", 1, a.byteValue());
        assertEquals("2000000000000000000000001/2000000000000000000000000 floatValue", 1.0f, a.floatValue(), 0.0f);
        assertEquals("2000000000000000000000001/2000000000000000000000000 doubleValue", 1.0, a.doubleValue(), 0.0);
    }

    public static void testEqualDigits()
    {
        Aprational a = new Aprational("1/3");
        Apfloat b = new Apfloat("0.3333333333");
        assertEquals("1/3 eq 0.3333333333", 10, a.equalDigits(b));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void testCompareTo()
    {
        Aprational a = new Aprational("0"),
                   b = new Aprational("1");
        assertEquals("0 cmp 1", -1, a.compareTo(b));
        assertEquals("0 cmp 1 as apfloat", -1, a.compareTo((Apfloat) b));
        a = new Aprational("6");
        b = new Aprational("5");
        assertEquals("6 cmp 5", 1, a.compareTo(b));
        a = new Aprational("6");
        b = new Aprational("6");
        assertEquals("6 cmp 6", 0, a.compareTo(b));
        a = new Aprational("1/6");
        b = new Aprational("1/5");
        assertEquals("1/6 cmp 1/5", -1, a.compareTo(b));
        a = new Aprational("5/12");
        b = new Aprational("-7/12");
        assertEquals("5/12 cmp -7/12", 1, a.compareTo(b));

        a = new Aprational("1/3");
        Apfloat f = new Apfloat("0.3333333333");
        assertEquals("1/3 cmp 0.3333333333", 1, a.compareTo(f));
        assertEquals("0.3333333333 cmp 1/3", -1, f.compareTo(a));

        a = new Aprational("1/7");
        f = new Aprational("1/3");
        assertEquals("1/7 cmp float 1/3", -1, a.compareTo(f));
        assertEquals("float 1/3 cmp 1/7", 1, f.compareTo(a));

        Comparable obj1 = new Aprational("1/3"),
                   obj2 = new Aprational("1/4");
        assertEquals("obj 1/3 cmp 1/4", 1, obj1.compareTo(obj2));
        assertEquals("obj 1/4 cmp 1/3", -1, obj2.compareTo(obj1));

        obj1 = new Aprational("1/3");
        obj2 = new Apfloat("0.3333333333");
        assertEquals("obj 1/3 cmp 0.3333333333", 1, obj1.compareTo(obj2));
        assertEquals("obj 0.3333333333 cmp 1/3", -1, obj2.compareTo(obj1));

        try
        {
            Comparable c = a;
            c.compareTo("bogus");
            fail("Comparison to different class allowed");
        }
        catch (ClassCastException cce)
        {
            // OK: class can't be cast to Apfloat
        }
    }

    public static void testEquals()
    {
        Aprational a = new Aprational("0"),
                   b = new Aprational("1");
        assertEquals("0 == 1", false, a.equals(b));
        a = new Aprational("6");
        b = new Aprational("5");
        assertEquals("6 == 5", false, a.equals(b));
        assertEquals("6 == something else", false, a.equals("bogus"));
        a = new Aprational("6");
        b = new Aprational("6");
        assertEquals("6 == 6", true, a.equals(b));
        a = new Aprational("1/6");
        b = new Aprational("1/5");
        assertEquals("1/6 == 1/5", false, a.equals(b));
        a = new Aprational("5/12");
        b = new Aprational("-7/12");
        assertEquals("5/12 == -7/12", false, a.equals(b));

        assertEquals("a == a", true, a.equals(a));

        a = new Aprational("1/3");
        Apfloat f = new Apfloat("0.3333333333");
        assertEquals("1/3 == 0.3333333333", false, a.equals(f));
        assertEquals("0.3333333333 == 1/3", false, f.equals(a));

        a = new Aprational("1/3");
        f = new Aprational("1/7");
        assertEquals("1/3 cmp float 1/7", false, a.equals(f));
        assertEquals("float 1/7 cmp 1/3", false, f.equals(a));

        a = new Aprational("1/2");
        f = new Apfloat("0.5");
        assertEquals("1/2 == 0.5", true, a.equals(f));
        assertEquals("0.5 == 1/2", true, f.equals(a));

        Object obj1 = new Aprational("1/3"),
               obj2 = new Aprational("1/4");
        assertEquals("obj 1/3 == 1/4", false, obj1.equals(obj2));
        assertEquals("obj 1/4 == 1/3", false, obj2.equals(obj1));

        obj1 = new Aprational("1/3");
        obj2 = new Apfloat("0.3333333333");
        assertEquals("obj 1/3 == 0.3333333333", false, obj1.equals(obj2));
        assertEquals("obj 0.3333333333 == 1/3", false, obj2.equals(obj1));
    }

    public static void testHashCode()
    {
        Aprational a = new Aprational("1/2"),
                   b = new Aprational("2/1");
        assertTrue("1/2 != 2/1", a.hashCode() != b.hashCode());
        a = new Aprational("6");
        b = new Aprational("5");
        assertTrue("5 != 6", a.hashCode() != b.hashCode());
        a = new Aprational("5/6");
        b = new Aprational("5/6");
        assertEquals("5/6 == 5/6", a.hashCode(), b.hashCode());
        a = new Aprational("2/1", 12);
        b = new Aprational("2");
        assertTrue("2/1 == 2", a.hashCode() == b.hashCode());
    }

    public static void testToString()
    {
        Aprational a = new Aprational("0");
        assertEquals("0", "0", "" + a);
        a = new Aprational("6");
        assertEquals("6", "6", "" + a);
        a = new Aprational("123456789/555555555555557");
        assertEquals("123456789/555555555555557", "123456789/555555555555557", "" + a);
        a = new Aprational("123456789/555555555555557");
        assertEquals("123456789/555555555555557 unpretty", "1.23456789e8/5.55555555555557e14", a.toString(false));
    }

    public static void testWriteTo()
        throws IOException
    {
        StringWriter out = new StringWriter();
        Aprational a = new Aprational("0");
        a.writeTo(out);
        a = new Aprational("6");
        a.writeTo(out);
        a = new Aprational("123456789/555555555555557");
        a.writeTo(out);
        a = new Aprational("123456789/555555555555557");
        a.writeTo(out, false);
        assertEquals("string", "06123456789/5555555555555571.23456789e8/5.55555555555557e14", out.toString());
    }

    public static void testFormatTo()
        throws IOException
    {
        Locale locale = null;
        assertEquals("null %s", "123456789/1234", String.format(locale, "%s", new Aprational("123456789/1234")));
        assertEquals("null %S", "123456789/123A", String.format(locale, "%S", new Aprational("123456789/123a", 11)));
        assertEquals("null %15s", " 123456789/1234", String.format(locale, "%15s", new Aprational("123456789/1234")));
        assertEquals("null %-15s", "123456789/1234 ", String.format(locale, "%-15s", new Aprational("123456789/1234")));

        assertEquals("null %s apint", "123456789", String.format(locale, "%s", new Aprational("123456789")));

        locale = new Locale("hi", "IN");
        assertEquals("hi_IN %s", "\u0967\u0968\u0969\u096a\u096b\u096c\u096d\u096e\u096f/\u0967\u0968\u0969\u096a", String.format(locale, "%s", new Aprational("123456789/1234")));
        assertEquals("hi_IN %s radix 9", "\u0967\u0968\u0969\u096a\u096b\u096c\u096d\u096e/\u0967\u0968\u096a", String.format(locale, "%s", new Aprational("12345678/124", 9)));
        assertEquals("hi_IN %s radix 11", "12345678/124", String.format(locale, "%s", new Aprational("12345678/124", 11)));

        try
        {
            String.format(locale, "%#s", new Aprational("123456789/1234"));
            fail("# flag allowed");
        }
        catch (IllegalFormatException ife)
        {
            // OK: alternate format not allowed with integers
        }

        try
        {
            String.format(locale, "%.1s", new Aprational("123456789/1234"));
            fail("Precision allowed");
        }
        catch (IllegalFormatException ife)
        {
            // OK: precision not allowed with integers
        }
    }

    private static String getString(char character, int length)
    {
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            buffer.append(character);
        }

        return buffer.toString();
    }

    public static void testSerialization()
        throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        Aprational a = new Aprational("5/6");
        out.writeObject(a);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        Aprational b = (Aprational) in.readObject();
        assertEquals("5/6 equals", a, b);
        assertNotSame("5/6 !=", a, b);

        a = new Aprational(getString('a', 1000000) + "/7", 12);
        a.precision(2000000);   // Return value not used
        buffer.reset();
        out = new ObjectOutputStream(buffer);
        out.writeObject(a);
        out.close();
        assertTrue("Data has been used: " + buffer.size() + " > 400000", buffer.size() > 400000);
        assertTrue("Transient data is not written: " + buffer.size() + " < 700000", buffer.size() < 700000);

        // Serialization from legacy data
        // new Aprational("1/100") in apfloat 1.7.1 format
        byte[] bytes = { -84, -19, 0, 5, 115, 114, 0, 22, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 114, 97, 116,
                         105, 111, 110, 97, 108, -4, -29, -68, 72, -61, -95, -26, 23, 2, 0, 3, 74, 0, 5, 115, 99, 97, 108, 101, 76, 0, 11,
                         100, 101, 110, 111, 109, 105, 110, 97, 116, 111, 114, 116, 0, 19, 76, 111, 114, 103, 47, 97, 112, 102, 108, 111,
                         97, 116, 47, 65, 112, 105, 110, 116, 59, 76, 0, 9, 110, 117, 109, 101, 114, 97, 116, 111, 114, 113, 0, 126, 0, 1,
                         120, 114, 0, 19, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 102, 108, 111, 97, 116, -1, 125,
                         -106, -56, -92, 28, 107, 73, 2, 0, 1, 76, 0, 4, 105, 109, 112, 108, 116, 0, 29, 76, 111, 114, 103, 47, 97, 112,
                         102, 108, 111, 97, 116, 47, 115, 112, 105, 47, 65, 112, 102, 108, 111, 97, 116, 73, 109, 112, 108, 59, 120, 114,
                         0, 21, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 99, 111, 109, 112, 108, 101, 120, 50,
                         -114, 76, 62, -100, 91, -70, -73, 2, 0, 2, 76, 0, 4, 105, 109, 97, 103, 116, 0, 21, 76, 111, 114, 103, 47, 97,
                         112, 102, 108, 111, 97, 116, 47, 65, 112, 102, 108, 111, 97, 116, 59, 76, 0, 4, 114, 101, 97, 108, 113, 0, 126, 0,
                         5, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29,
                         11, -108, -32, -117, 2, 0, 0, 120, 112, 112, 112, 112, -128, 0, 0, 0, 0, 0, 0, 0, 115, 114, 0, 17, 111, 114, 103,
                         46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 105, 110, 116, 75, 19, 49, 114, 115, -59, -82, 83, 2, 0, 1, 76,
                         0, 5, 118, 97, 108, 117, 101, 113, 0, 126, 0, 5, 120, 113, 0, 126, 0, 0, 112, 112, 112, -128, 0, 0, 0, 0, 0, 0, 0,
                         112, 112, 115, 113, 0, 126, 0, 2, 112, 112, 115, 114, 0, 36, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116,
                         46, 105, 110, 116, 101, 114, 110, 97, 108, 46, 76, 111, 110, 103, 65, 112, 102, 108, 111, 97, 116, 73, 109, 112,
                         108, -30, 36, -29, 106, -22, 40, -77, 43, 2, 0, 8, 74, 0, 8, 101, 120, 112, 111, 110, 101, 110, 116, 73, 0, 8,
                         104, 97, 115, 104, 67, 111, 100, 101, 73, 0, 13, 105, 110, 105, 116, 105, 97, 108, 68, 105, 103, 105, 116, 115,
                         74, 0, 10, 108, 101, 97, 115, 116, 90, 101, 114, 111, 115, 74, 0, 9, 112, 114, 101, 99, 105, 115, 105, 111, 110,
                         73, 0, 5, 114, 97, 100, 105, 120, 73, 0, 4, 115, 105, 103, 110, 76, 0, 11, 100, 97, 116, 97, 83, 116, 111, 114,
                         97, 103, 101, 116, 0, 29, 76, 111, 114, 103, 47, 97, 112, 102, 108, 111, 97, 116, 47, 115, 112, 105, 47, 68, 97,
                         116, 97, 83, 116, 111, 114, 97, 103, 101, 59, 120, 114, 0, 33, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116,
                         46, 105, 110, 116, 101, 114, 110, 97, 108, 46, 76, 111, 110, 103, 66, 97, 115, 101, 77, 97, 116, 104, -90, 56,
                         -79, 77, -38, 28, -98, -104, 2, 0, 2, 68, 0, 11, 105, 110, 118, 101, 114, 115, 101, 66, 97, 115, 101, 73, 0, 5,
                         114, 97, 100, 105, 120, 120, 112, 60, 103, 14, -11, 70, 70, -44, -105, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,
                         0, 0, -128, 0, 0, 0, -1, -1, -1, -1, -128, 0, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 10, 0, 0, 0, 1, 115,
                         114, 0, 42, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 105, 110, 116, 101, 114, 110, 97, 108, 46, 76,
                         111, 110, 103, 77, 101, 109, 111, 114, 121, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, -84, 74, -31, 104,
                         107, 113, 66, 75, 2, 0, 1, 91, 0, 4, 100, 97, 116, 97, 116, 0, 2, 91, 74, 120, 114, 0, 27, 111, 114, 103, 46, 97,
                         112, 102, 108, 111, 97, 116, 46, 115, 112, 105, 46, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 25, -41, 65,
                         37, -43, -67, -21, -93, 2, 0, 5, 90, 0, 10, 105, 115, 82, 101, 97, 100, 79, 110, 108, 121, 90, 0, 14, 105, 115,
                         83, 117, 98, 115, 101, 113, 117, 101, 110, 99, 101, 100, 74, 0, 6, 108, 101, 110, 103, 116, 104, 74, 0, 6, 111,
                         102, 102, 115, 101, 116, 76, 0, 19, 111, 114, 105, 103, 105, 110, 97, 108, 68, 97, 116, 97, 83, 116, 111, 114, 97,
                         103, 101, 113, 0, 126, 0, 12, 120, 112, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 112, 117, 114, 0, 2,
                         91, 74, 120, 32, 4, -75, 18, -79, 117, -109, 2, 0, 0, 120, 112, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 100, 115, 113, 0,
                         126, 0, 8, 112, 112, 112, -128, 0, 0, 0, 0, 0, 0, 0, 112, 112, 115, 113, 0, 126, 0, 2, 112, 112, 115, 113, 0, 126,
                         0, 11, 60, 103, 14, -11, 70, 70, -44, -105, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, -1, -1,
                         -1, -1, -128, 0, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 10, 0, 0, 0, 1, 115, 113, 0, 126, 0, 15, 1, 0, 0,
                         0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 112, 117, 113, 0, 126, 0, 19, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1 };
        in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        a = (Aprational) in.readObject();
        assertEquals("Legacy scale", -1, a.scale());
        assertEquals("Legacy size", 1, a.size());
    }
}
