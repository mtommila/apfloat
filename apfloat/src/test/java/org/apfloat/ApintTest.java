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
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class ApintTest
    extends ApfloatTestCase
{
    public ApintTest(String methodName)
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

        suite.addTest(new ApintTest("testLongConstructor"));
        suite.addTest(new ApintTest("testStringConstructor"));
        suite.addTest(new ApintTest("testStreamConstructor"));
        suite.addTest(new ApintTest("testBigIntegerConstructor"));
        suite.addTest(new ApintTest("testRationalMethods"));
        suite.addTest(new ApintTest("testRadix"));
        suite.addTest(new ApintTest("testPrecision"));
        suite.addTest(new ApintTest("testScale"));
        suite.addTest(new ApintTest("testSize"));
        suite.addTest(new ApintTest("testIsInteger"));
        suite.addTest(new ApintTest("testNegate"));
        suite.addTest(new ApintTest("testAdd"));
        suite.addTest(new ApintTest("testSubtract"));
        suite.addTest(new ApintTest("testMultiply"));
        suite.addTest(new ApintTest("testDivide"));
        suite.addTest(new ApintTest("testMod"));
        suite.addTest(new ApintTest("testFloor"));
        suite.addTest(new ApintTest("testCeil"));
        suite.addTest(new ApintTest("testTruncate"));
        suite.addTest(new ApintTest("testFrac"));
        suite.addTest(new ApintTest("testRoundAway"));
        suite.addTest(new ApintTest("testAbs"));
        suite.addTest(new ApintTest("testCompareToHalf"));
        suite.addTest(new ApintTest("testNumberValues"));
        suite.addTest(new ApintTest("testEqualDigits"));
        suite.addTest(new ApintTest("testToBigInteger"));
        suite.addTest(new ApintTest("testCompareTo"));
        suite.addTest(new ApintTest("testEquals"));
        suite.addTest(new ApintTest("testHashCode"));
        suite.addTest(new ApintTest("testToString"));
        suite.addTest(new ApintTest("testWriteTo"));
        suite.addTest(new ApintTest("testFormatTo"));
        suite.addTest(new ApintTest("testSerialization"));

        return suite;
    }

    public static void testLongConstructor()
    {
        Apint a = new Apint(5);
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 String", "5", a.toString());
        a = new Apint(5, 6);
        assertEquals("5, 6 radix", 6, a.radix());
        assertEquals("5, 6 String", "5", a.toString());
    }

    public static void testStringConstructor()
    {
        Apint a = new Apint("5");
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 String", "5", a.toString());

        a = new Apint("aa", 12);
        assertEquals("aa radix", 12, a.radix());
        assertEquals("aa String", "aa", a.toString());

        a = new Apint("-bb", 13);
        assertEquals("-bb radix", 13, a.radix());
        assertEquals("-bb String", "-bb", a.toString());

        try
        {
            a = new Apint("2.");
            fail("2. accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: illegal number
        }

        try
        {
            a = new Apint("2e");
            fail("2e accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: illegal number
        }
    }

    public static void testStreamConstructor()
        throws IOException
    {
        PushbackReader in = new PushbackReader(new StringReader("5"));
        Apint a = new Apint(in);
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 String", "5", a.toString());

        in = new PushbackReader(new StringReader("aa"));
        a = new Apint(in, 12);
        assertEquals("aa radix", 12, a.radix());
        assertEquals("aa String", "aa", a.toString());

        in = new PushbackReader(new StringReader("2."));
        a = new Apint(in);
        assertEquals("2. String", "2", a.toString());
        assertEquals("2. next char", '.', in.read());

        in = new PushbackReader(new StringReader("2e"));
        a = new Apint(in);
        assertEquals("2e String", "2", a.toString());
        assertEquals("2e next char", 'e', in.read());

        in = new PushbackReader(new StringReader("05"));
        a = new Apint(in);
        assertEquals("05 String", "5", a.toString());
        assertEquals("05 next char", -1, in.read());
    }

    public static void testBigIntegerConstructor()
    {
        Apint a = new Apint(BigInteger.valueOf(5));
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 String", "5", a.toString());
        a = new Apint(BigInteger.valueOf(6), 12);
        assertEquals("6 radix", 12, a.radix());
        assertEquals("6 String", "6", a.toString());
    }

    public static void testRationalMethods()
    {
        Apint a = new Apint(5);
        assertEquals("numerator", a, a.numerator());
        assertEquals("denominator", Apint.ONE, a.denominator());
    }

    public static void testRadix()
    {
        Apint a = new Apint(2, 5);
        assertEquals("radix", 5, a.radix());

        a = new Apint(16);
        assertEquals("10 -> 16", new Apint("10", 16), a.toRadix(16));
    }

    public static void testPrecision()
    {
        Apint a = new Apint(5);
        assertEquals("int precision", Apfloat.INFINITE, a.precision());
        Apfloat f = a.precision(4);
        assertEquals("4 precision", 4, f.precision());
        a = new Apint(0);
        assertEquals("0 precision", Apint.INFINITE, a.precision());
    }

    public static void testScale()
    {
        Apint a = new Apint(9);
        assertEquals("9 scale", 1, a.scale());
        a = new Apint(10);
        assertEquals("10 scale", 2, a.scale());
        a = new Apint(0);
        assertEquals("0 scale", -Apint.INFINITE, a.scale());

        a = new Apint(9);
        assertEquals("9 scaled 1", new Apint(90), a.scale(1));
        assertEquals("9 scaled -1", new Aprational("9/10"), a.scale(-1));
    }

    public static void testSize()
    {
        Apint a = new Apint(9);
        assertEquals("9 size", 1, a.size());
        a = new Apint(10);
        assertEquals("10 size", 1, a.size());
        a = new Apint(100010);
        assertEquals("100010 size", 5, a.size());
        a = new Apint(1000000000000010L);
        assertEquals("1000000000000010 size", 15, a.size());
        a = new Apint("100000000000000000000000100");
        assertEquals("100000000000000000000000100 size", 25, a.size());
        a = new Apint(0);
        assertEquals("0 size", 0, a.size());
    }

    public static void testIsInteger()
    {
        Apint a = new Apint(0);
        assertTrue("0", a.isInteger());
        a = new Apint(1);
        assertTrue("1", a.isInteger());
        a = new Apint(-1);
        assertTrue("-1", a.isInteger());
    }

    public static void testNegate()
    {
        Apint x = new Apint(2);
        assertEquals("2", new Apint(-2), x.negate());

        x = new Apint(-2);
        assertEquals("-2", new Apint(2), x.negate());

        x = new Apint(0);
        assertEquals("0", new Apint(0), x.negate());

        Aprational y = new Apint(2);
        assertEquals("-2", new Aprational("-2"), y.negate());
    }

    public static void testAdd()
    {
        Apint a = new Apint(4),
              b = new Apint(5);
        assertEquals("4 + 5", new Apint(9), a.add(b));
        assertEquals("4 + 0", new Apint(4), a.add(new Apint(0)));
        assertEquals("0 + 4", new Apint(4), new Apint(0).add(a));

        a = new Apint("1");
        Apfloat f = new Apfloat("0.00001");
        assertEquals("1 + 0.00001", new Apfloat("1.00001"), a.add(f));
        assertEquals("1 + 0.00001 precision", 6, a.add(f).precision());
        assertEquals("0.00001 + 1", new Apfloat("1.00001"), f.add(a));
    }

    public static void testSubtract()
    {
        Apint a = new Apint(4),
              b = new Apint(5);
        assertEquals("4 - 5", new Apint(-1), a.subtract(b));
        assertEquals("4 - 0", new Apint(4), a.subtract(new Apint(0)));
        assertEquals("0 - 4", new Apint(-4), new Apint(0).subtract(a));
    }

    public static void testMultiply()
    {
        Apint a = new Apint(4, 12),
              b = new Apint(5, 12);
        assertEquals("4 * 5", new Apint(20, 12), a.multiply(b));
        assertEquals("4 * 0", new Apint(0), a.multiply(new Apint(0)));
        assertEquals("0 * 4", new Apint(0), new Apint(0).multiply(a));
        assertEquals("4 * 1", new Apint(4, 12), a.multiply(new Apint(1, 12)));
        assertEquals("1 * 4", new Apint(4, 12), new Apint(1, 12).multiply(a));
        assertEquals("4 * ONE", new Apint(4, 12), a.multiply(Apint.ONE));
        assertEquals("ONE * 4", new Apint(4, 12), Apint.ONE.multiply(a));
    }

    public static void testDivide()
    {
        Apint a = new Apint(4, 12),
              b = new Apint(3, 12);
        assertEquals("4 / 3", new Apint("1", 12), a.divide(b));
        assertEquals("0 / 4", new Apint(0), new Apint(0).divide(a));
        assertEquals("4 / 1", new Apint(4, 12), a.divide(new Apint(1, 12)));
        assertEquals("4 / ONE", new Apint(4, 12), a.divide(Apint.ONE));

        try
        {
            a.divide(new Apint(0));
            fail("Division by zero allowed");
        }
        catch (ArithmeticException ae)
        {
            // OK: division by zero
        }

        assertEquals("long / long", new Apint(1), new Apint("101010101010101010101010101010101010101").divide(new Apint("101010101010101010101010101010101010101")));
        assertEquals("long / -long", new Apint(-1), new Apint("101010101010101010101010101010101010101").divide(new Apint("-101010101010101010101010101010101010101")));
        assertEquals("-long / long", new Apint(-1), new Apint("-101010101010101010101010101010101010101").divide(new Apint("101010101010101010101010101010101010101")));
        assertEquals("-long / -long", new Apint(1), new Apint("-101010101010101010101010101010101010101").divide(new Apint("-101010101010101010101010101010101010101")));

        assertEquals("very long / very long", new Apint("88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), new Apint("610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547").divide(new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
        assertEquals("very long / -very long", new Apint("-88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), new Apint("610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547").divide(new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
        assertEquals("-very long / very long", new Apint("-88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), new Apint("-610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547").divide(new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
        assertEquals("-very long / -very long", new Apint("88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), new Apint("-610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547").divide(new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
    }

    public static void testMod()
    {
        Apint a = new Apint(4),
              b = new Apint(3);
        assertEquals("4 % 3", new Apint(1), a.mod(b));
    }

    public static void testFloor()
    {
        Apint a = new Apint("2");
        assertEquals("2 floor", new Apint(2), a.floor());
        a = new Apint(0);
        assertEquals("0 floor", new Apint(0), a.floor());
    }

    public static void testCeil()
    {
        Apint a = new Apint("2");
        assertEquals("2 ceil", new Apint(2), a.ceil());
        a = new Apint(0);
        assertEquals("0 ceil", new Apint(0), a.ceil());
    }

    public static void testTruncate()
    {
        Apint a = new Apint("2");
        assertEquals("2 truncate", new Apint(2), a.truncate());
        a = new Apint(0);
        assertEquals("0 truncate", new Apint(0), a.truncate());
    }

    public static void testFrac()
    {
        Apint a = new Apint("2");
        assertEquals("2 frac", new Apint(0), a.frac());
        a = new Apint(0);
        assertEquals("0 truncate", new Apint(0), a.frac());
    }

    public static void testRoundAway()
    {
        Apint a = new Apint("2");
        assertEquals("2 roundAway", new Apint(2), a.roundAway());
        a = new Apint(0);
        assertEquals("0 roundAway", new Apint(0), a.roundAway());
    }

    public static void testAbs()
    {
        Apint a = new Apint("2");
        assertEquals("2 abs", new Apint(2), a.abs());
        a = new Apint(0);
        assertEquals("0 abs", new Apint(0), a.abs());
        a = new Apint(-2);
        assertEquals("-2 abs", new Apint(2), a.abs());
    }

    public static void testCompareToHalf()
    {
        Apint a = new Apint(0);
        assertEquals("0 compareToHalf", -1, a.compareToHalf());
        a = new Apint(1);
        assertEquals("1 compareToHalf", 1, a.compareToHalf());
        a = new Apint(2);
        assertEquals("2 compareToHalf", 1, a.compareToHalf());
    }

    public static void testNumberValues()
    {
        Apint a = new Apint(5);
        assertEquals("5 longValue", 5, a.longValue());
        assertEquals("5 intValue", 5, a.intValue());
        assertEquals("5 shortValue", 5, a.shortValue());
        assertEquals("5 byteValue", 5, a.byteValue());
        assertEquals("5 floatValue", 5.0f, a.floatValue(), 0.0f);
        assertEquals("5 doubleValue", 5.0, a.doubleValue(), 0.0);
        a = new Apint(1000000000000L);
        assertEquals("1000000000000 intValue", Integer.MAX_VALUE, a.intValue());
        assertEquals("1000000000000 shortValue", Short.MAX_VALUE, a.shortValue());
        assertEquals("1000000000000 byteValue", Byte.MAX_VALUE, a.byteValue());
        a = new Apint(-1000000000000L);
        assertEquals("-1000000000000 intValue", Integer.MIN_VALUE, a.intValue());
        assertEquals("-1000000000000 shortValue", Short.MIN_VALUE, a.shortValue());
        assertEquals("-1000000000000 byteValue", Byte.MIN_VALUE, a.byteValue());
        a = new Apfloat("1e100").floor();
        assertEquals("1e100 floatValue", Float.POSITIVE_INFINITY, a.floatValue(), 0.0f);
        a = new Apfloat("-1e100").floor();
        assertEquals("-1e100 floatValue", Float.NEGATIVE_INFINITY, a.floatValue(), 0.0f);
        a = new Apfloat("1e1000").floor();
        assertEquals("1e1000 doubleValue", Double.POSITIVE_INFINITY, a.doubleValue(), 0.0);
        a = new Apfloat("-1e1000").floor();
        assertEquals("-1e1000 doubleValue", Double.NEGATIVE_INFINITY, a.doubleValue(), 0.0);
    }

    public static void testEqualDigits()
    {
        Apint a = new Apint(5),
              b = new Apint(6);
        assertEquals("5 eq 6", 0, a.equalDigits(b));
        a = new Apint(10);
        b = new Apint(11);
        assertEquals("10 eq 11", 1, a.equalDigits(b));
        a = new Apint(7);
        b = new Apint(7);
        assertEquals("7 eq 7", Apint.INFINITE, a.equalDigits(b));
    }

    public static void testToBigInteger()
    {
        Apint a = new Apint("1234567"),
              b = new Apint("-12345678901234567890");
        assertEquals("1234567", new BigInteger("1234567"), a.toBigInteger());
        assertEquals("-12345678901234567890", new BigInteger("-12345678901234567890"), b.toBigInteger());
        assertEquals("0", BigInteger.ZERO, Apint.ZERO.toBigInteger());

        try
        {
            a = new Apfloat("1", Apfloat.INFINITE, 16).scale(1000000000000000L).truncate();
            a.toBigInteger();
            fail("Too big integer allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, should be thrown
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void testCompareTo()
    {
        Apint a = new Apint(0),
              b = new Apint(1);
        assertEquals("0 cmp 1", -1, a.compareTo(b));
        a = new Apint(6);
        b = new Apint(5);
        assertEquals("6 cmp 5", 1, a.compareTo(b));
        a = new Apint(6);
        b = new Apint(6);
        assertEquals("6 cmp 6", 0, a.compareTo(b));

        a = new Apint(12);
        Aprational r = new Aprational("12/1");
        assertEquals("12 cmp rational 12", 0, a.compareTo(r));
        assertEquals("rational 12 cmp 12", 0, r.compareTo(a));
        r = new Apint(12);
        assertEquals("12 cmp rational int 12", 0, a.compareTo(r));
        assertEquals("rational int 12 cmp 12", 0, r.compareTo(a));

        a = new Apint(3);
        Apfloat f = new Apfloat("3.3");
        assertEquals("3 cmp 3.3", -1, a.compareTo(f));
        assertEquals("3.3 cmp 3", 1, f.compareTo(a));
        f = new Aprational("10/3");
        assertEquals("3 cmp float 10/3", -1, a.compareTo(f));
        assertEquals("float 10/3 cmp 3", 1, f.compareTo(a));

        Comparable obj1 = new Apint(3),
                   obj2 = new Apint(4);
        assertEquals("obj 3 cmp 4", -1, obj1.compareTo(obj2));
        assertEquals("obj 4 cmp 3", 1, obj2.compareTo(obj1));

        obj1 = new Apint(3);
        obj2 = new Aprational("8/3");
        assertEquals("obj 3 cmp 8/3", 1, obj1.compareTo(obj2));
        assertEquals("obj 8/3 cmp 3", -1, obj2.compareTo(obj1));

        obj1 = new Apint(3);
        obj2 = new Apfloat("3.3");
        assertEquals("obj 3 cmp 3.3", -1, obj1.compareTo(obj2));
        assertEquals("obj 3.3 cmp 3", 1, obj2.compareTo(obj1));

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
        Apint a = new Apint(0),
              b = new Apint(1);
        assertEquals("0 == 1", false, a.equals(b));
        a = new Apint(6);
        b = new Apint(5);
        assertEquals("6 == 5", false, a.equals(b));
        a = new Apint(6);
        b = new Apint(6);
        assertEquals("6 == 6", true, a.equals(b));

        assertEquals("6 == something else", false, a.equals("bogus"));

        a = new Apint(12);
        Aprational r = new Aprational("12/1");
        assertEquals("12 == rational 12", true, a.equals(r));
        assertEquals("rational 12 == 12", true, r.equals(a));

        a = new Apint(3);
        Apfloat f = new Apfloat("3");
        assertEquals("3 == float 3", true, a.equals(f));
        assertEquals("float 3 == 3", true, f.equals(a));

        Object obj1 = new Apint(3),
               obj2 = new Apint(4);
        assertEquals("obj 3 == 4", false, obj1.equals(obj2));
        assertEquals("obj 4 == 3", false, obj2.equals(obj1));

        obj1 = new Apint(3);
        obj2 = new Aprational("8/3");
        assertEquals("obj 3 == 8/3", false, obj1.equals(obj2));
        assertEquals("obj 8/3 == 3", false, obj2.equals(obj1));

        obj1 = new Apint(3);
        obj2 = new Apfloat("3");
        assertEquals("obj 3 == float 3", true, obj1.equals(obj2));
        assertEquals("obj float 3 == 3", true, obj2.equals(obj1));
    }

    public static void testHashCode()
    {
        Apint a = new Apint(0),
              b = new Apint(1);
        assertTrue("0 != 1", a.hashCode() != b.hashCode());
        a = new Apint(6);
        b = new Apint(5);
        assertTrue("5 != 6", a.hashCode() != b.hashCode());
        a = new Apint(6);
        b = new Apint(6);
        assertEquals("6 == 6", a.hashCode(), b.hashCode());
    }

    public static void testToString()
    {
        Apint a = new Apint(0);
        assertEquals("0", "0", "" + a);
        a = new Apint(6);
        assertEquals("6", "6", "" + a);
        a = new Apint(123456789);
        assertEquals("123456789", "123456789", "" + a);
        assertEquals("123456789 unpretty", "1.23456789e8", a.toString(false));
    }

    public static void testWriteTo()
        throws IOException
    {
        StringWriter out = new StringWriter();
        Apint a = new Apint(0);
        a.writeTo(out);
        a = new Apint(6);
        a.writeTo(out);
        a = new Apint(123456789);
        a.writeTo(out);
        a.writeTo(out, false);
        assertEquals("string", "061234567891.23456789e8", out.toString());
    }

    public static void testFormatTo()
        throws IOException
    {
        System.setProperty("java.locale.providers", "COMPAT,SPI"); // Required since Java 10 to have all locale providers available

        Locale locale = null;
        assertEquals("null %s", "123456789", String.format(locale, "%s", new Apint("123456789")));
        assertEquals("null %S", "123456789A", String.format(locale, "%S", new Apint("123456789a", 11)));
        assertEquals("null %10s", " 123456789", String.format(locale, "%10s", new Apint("123456789")));
        assertEquals("null %-10s", "123456789 ", String.format(locale, "%-10s", new Apint("123456789")));

        locale = new Locale("hi", "IN");
        assertEquals("hi_IN %s", "\u0967\u0968\u0969\u096a\u096b\u096c\u096d\u096e\u096f", String.format(locale, "%s", new Apint("123456789")));
        assertEquals("hi_IN %s radix 9", "\u0967\u0968\u0969\u096a\u096b\u096c\u096d\u096e", String.format(locale, "%s", new Apint("12345678", 9)));
        assertEquals("hi_IN %s radix 11", "123456789", String.format(locale, "%s", new Apint("123456789", 11)));

        try
        {
            String.format(locale, "%#s", new Apint("123456789"));
            fail("# flag allowed");
        }
        catch (IllegalFormatException ife)
        {
            // OK: alternate format not allowed with integers
        }

        try
        {
            String.format(locale, "%.1s", new Apint("123456789"));
            fail("Precision allowed");
        }
        catch (IllegalFormatException ife)
        {
            // OK: precision not allowed with integers
        }
    }

    public static void testSerialization()
        throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        Apint a = new Apint(5);
        out.writeObject(a);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        Apint b = (Apint) in.readObject();
        assertEquals("5 equals", a, b);
        assertNotSame("5 !=", a, b);
    }
}
