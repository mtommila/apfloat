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

import java.io.Reader;
import java.io.PushbackReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Random;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class RawtypeApfloatImplTest
    extends RawtypeTestCase
{
    public RawtypeApfloatImplTest(String methodName)
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

        suite.addTest(new RawtypeApfloatImplTest("testLongConstructor"));
        suite.addTest(new RawtypeApfloatImplTest("testDoubleConstructor"));
        suite.addTest(new RawtypeApfloatImplTest("testStringConstructor"));
        suite.addTest(new RawtypeApfloatImplTest("testStreamConstructor"));
        suite.addTest(new RawtypeApfloatImplTest("testAdd"));
        suite.addTest(new RawtypeApfloatImplTest("testSubtract"));
        suite.addTest(new RawtypeApfloatImplTest("testMultiply"));
        suite.addTest(new RawtypeApfloatImplTest("testIsShort"));
        suite.addTest(new RawtypeApfloatImplTest("testDivideShort"));
        suite.addTest(new RawtypeApfloatImplTest("testModShort"));
        suite.addTest(new RawtypeApfloatImplTest("testAbsFloor"));
        suite.addTest(new RawtypeApfloatImplTest("testAbsCeil"));
        suite.addTest(new RawtypeApfloatImplTest("testFrac"));
        suite.addTest(new RawtypeApfloatImplTest("testSize"));
        suite.addTest(new RawtypeApfloatImplTest("testNegate"));
        suite.addTest(new RawtypeApfloatImplTest("testDoubleValue"));
        suite.addTest(new RawtypeApfloatImplTest("testLongValue"));
        suite.addTest(new RawtypeApfloatImplTest("testEqualDigits"));
        suite.addTest(new RawtypeApfloatImplTest("testCompareTo"));
        suite.addTest(new RawtypeApfloatImplTest("testEquals"));
        suite.addTest(new RawtypeApfloatImplTest("testHashCode"));
        suite.addTest(new RawtypeApfloatImplTest("testToString"));
        suite.addTest(new RawtypeApfloatImplTest("testWriteTo"));
        suite.addTest(new RawtypeApfloatImplTest("testSerialization"));

        return suite;
    }

    private static String pad(String text, int length)
    {
        StringBuilder buffer = new StringBuilder(length);

        buffer.append(text);

        length -= text.length();

        for (int i = 0; i < length; i++)
        {
            buffer.append('0');
        }

        return buffer.toString();
    }

    public static void testLongConstructor()
    {
        ApfloatImpl impl;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " zero sign", 0, impl.signum());
            assertEquals("radix " + radix + " zero precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " zero radix", radix, impl.radix());
            assertEquals("radix " + radix + " zero String", "0", impl.toString(true));

            impl = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " one sign", 1, impl.signum());
            assertEquals("radix " + radix + " one precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " one radix", radix, impl.radix());
            assertEquals("radix " + radix + " one scale", 1, impl.scale());
            assertEquals("radix " + radix + " one String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " minus one sign", -1, impl.signum());
            assertEquals("radix " + radix + " minus one precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " minus one radix", radix, impl.radix());
            assertEquals("radix " + radix + " minus one scale", 1, impl.scale());
            assertEquals("radix " + radix + " minus one String", "-1", impl.toString(true));

            impl = new RawtypeApfloatImpl(Long.MAX_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " max sign", 1, impl.signum());
            assertEquals("radix " + radix + " max precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " max radix", radix, impl.radix());
            assertEquals("radix " + radix + " max scale", Long.toString(Long.MAX_VALUE, radix).length(), impl.scale());
            assertEquals("radix " + radix + " max String", Long.toString(Long.MAX_VALUE, radix), impl.toString(true));

            impl = new RawtypeApfloatImpl(-Long.MAX_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " min+1 sign", -1, impl.signum());
            assertEquals("radix " + radix + " min+1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " min+1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " min+1 scale", Long.toString(Long.MAX_VALUE, radix).length(), impl.scale());
            assertEquals("radix " + radix + " min+1 String", Long.toString(-Long.MAX_VALUE, radix), impl.toString(true));

            impl = new RawtypeApfloatImpl(Long.MIN_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " min sign", -1, impl.signum());
            assertEquals("radix " + radix + " min precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " min radix", radix, impl.radix());
            assertEquals("radix " + radix + " min scale", Long.toString(Long.MIN_VALUE, radix).length() - 1, impl.scale());
            assertEquals("radix " + radix + " min String", Long.toString(Long.MIN_VALUE, radix), impl.toString(true));

            impl = new RawtypeApfloatImpl(radix * radix, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " radix^2 sign", 1, impl.signum());
            assertEquals("radix " + radix + " radix^2 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " radix^2 radix", radix, impl.radix());
            assertEquals("radix " + radix + " radix^2 scale", 3, impl.scale());
            assertEquals("radix " + radix + " radix^2 String", "100", impl.toString(true));

            impl = new RawtypeApfloatImpl(Long.MAX_VALUE, 4, radix);
            String expected = Long.toString(Long.MAX_VALUE, radix);
            expected = pad(expected.substring(0, 4), expected.length());
            assertEquals("radix " + radix + " max sign", 1, impl.signum());
            assertEquals("radix " + radix + " max precision", 4, impl.precision());
            assertEquals("radix " + radix + " max radix", radix, impl.radix());
            assertEquals("radix " + radix + " max scale", Long.toString(Long.MAX_VALUE, radix).length(), impl.scale());
            assertEquals("radix " + radix + " max String", expected, impl.toString(true));
        }

        impl = new RawtypeApfloatImpl(1000000000000000001L, 1, 10);
        assertEquals("truncated String", "1000000000000000000", impl.precision(Apfloat.INFINITE).toString(true));

        impl = new RawtypeApfloatImpl(1, 1, 10);
        assertTrue("one", impl.isOne());

        impl = new RawtypeApfloatImpl(-1, 1, 10);
        assertFalse("not one", impl.isOne());
    }

    public static void testDoubleConstructor()
    {
        ApfloatImpl impl;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            impl = new RawtypeApfloatImpl(0.0, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " zero sign", 0, impl.signum());
            assertEquals("radix " + radix + " zero precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " zero radix", radix, impl.radix());
            assertEquals("radix " + radix + " zero String", "0", impl.toString(true));

            impl = new RawtypeApfloatImpl(1.0, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " one sign", 1, impl.signum());
            assertEquals("radix " + radix + " one precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " one radix", radix, impl.radix());
            assertEquals("radix " + radix + " one scale", 1, impl.scale());
            assertEquals("radix " + radix + " one String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(-1.0, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " minus one sign", -1, impl.signum());
            assertEquals("radix " + radix + " minus one precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " minus one radix", radix, impl.radix());
            assertEquals("radix " + radix + " minus one scale", 1, impl.scale());
            assertEquals("radix " + radix + " minus one String", "-1", impl.toString(true));

            impl = new RawtypeApfloatImpl(Double.MAX_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " max sign", 1, impl.signum());
            assertEquals("radix " + radix + " max precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " max radix", radix, impl.radix());

            impl = new RawtypeApfloatImpl(Double.MIN_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " max sign", 1, impl.signum());
            assertEquals("radix " + radix + " max precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " max radix", radix, impl.radix());

            impl = new RawtypeApfloatImpl(1.0 * radix * radix + 0.0000001, 1, radix);
            assertEquals("radix " + radix + " radix^2 sign", 1, impl.signum());
            assertEquals("radix " + radix + " radix^2 precision", 1, impl.precision());
            assertEquals("radix " + radix + " radix^2 radix", radix, impl.radix());
            assertEquals("radix " + radix + " radix^2 scale", 3, impl.scale());
            assertEquals("radix " + radix + " radix^2 String", "100", impl.toString(true));

            impl = new RawtypeApfloatImpl(1.0 / radix + 0.0000001, 1, radix);
            assertEquals("radix " + radix + " 1/radix sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1/radix precision", 1, impl.precision());
            assertEquals("radix " + radix + " 1/radix radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1/radix scale", 0, impl.scale());
            assertEquals("radix " + radix + " 1/radix String", "0.1", impl.toString(true));
        }

        impl = new RawtypeApfloatImpl(10000.1, 1, 10);
        assertEquals("truncated String", "10000", impl.precision(Apfloat.INFINITE).toString(true));

        impl = new RawtypeApfloatImpl(Double.MAX_VALUE, Apfloat.INFINITE, 10);
        assertEquals("max 10 sign", 1, impl.signum());
        assertEquals("max 10 scale", 309, impl.scale());
        assertEquals("max 10 String length", 309, impl.toString(true).length());
        assertEquals("max 10 String begin", "1797693134862315", impl.toString(true).substring(0, 16));

        impl = new RawtypeApfloatImpl(Double.MIN_VALUE, Apfloat.INFINITE, 10);
        assertEquals("min 10 sign", 1, impl.signum());
        assertEquals("min 10 scale", -323, impl.scale());
        assertEquals("min 10 String begin", "0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049", impl.toString(true).substring(0, 327));

        impl = new RawtypeApfloatImpl(1.0, 2, 10);
        assertEquals("1 size", 1, impl.size());

        impl = new RawtypeApfloatImpl(111111.0, 7, 10);
        assertEquals("111111 size", 6, impl.size());

        try
        {
            impl = new RawtypeApfloatImpl(Double.NaN, Apfloat.INFINITE, 10);
            fail("NaN accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl(Double.POSITIVE_INFINITY, Apfloat.INFINITE, 10);
            fail("+Inf accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl(Double.NEGATIVE_INFINITY, Apfloat.INFINITE, 10);
            fail("-Inf accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }
    }

    public static void testStringConstructor()
    {
        ApfloatImpl impl;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            impl = new RawtypeApfloatImpl("0", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 0 sign", 0, impl.signum());
            assertEquals("radix " + radix + " 0 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 0 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0 String", "0", impl.toString(true));

            impl = new RawtypeApfloatImpl("00", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 00 sign", 0, impl.signum());
            assertEquals("radix " + radix + " 00 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 00 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 00 String", "0", impl.toString(true));

            if (radix < 15)
            {
                impl = new RawtypeApfloatImpl("0e5", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0e5 sign", 0, impl.signum());
                assertEquals("radix " + radix + " 0e5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0e5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0e5 String", "0", impl.toString(true));

                impl = new RawtypeApfloatImpl("1E5", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1E5 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1E5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1E5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1E5 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1E5 String", "100000", impl.toString(true));

                impl = new RawtypeApfloatImpl("1E+5", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1E+5 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1E+5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1E+5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1E+5 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1E+5 String", "100000", impl.toString(true));

                impl = new RawtypeApfloatImpl("1.0E5", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1.0E5 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1.0E5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1.0E5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1.0E5 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1.0E5 String", "100000", impl.toString(true));

                impl = new RawtypeApfloatImpl("1.1E5", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1.1E5 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1.1E5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1.1E5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1.1E5 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1.1E5 String", "110000", impl.toString(true));

                impl = new RawtypeApfloatImpl("1.1E5", 1, radix, false);
                assertEquals("radix " + radix + " 1.1E5 prec1 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1.1E5 prec1 precision", 1, impl.precision());
                assertEquals("radix " + radix + " 1.1E5 prec1 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1.1E5 prec1 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1.1E5 prec1 String", "100000", impl.toString(true));

                impl = new RawtypeApfloatImpl("1e50", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e50 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e50 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e50 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e50 scale", 51, impl.scale());
                assertEquals("radix " + radix + " 1e50 String", "100000000000000000000000000000000000000000000000000", impl.toString(true));

                impl = new RawtypeApfloatImpl("1e-1", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e-1 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e-1 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e-1 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e-1 scale", 0, impl.scale());
                assertEquals("radix " + radix + " 1e-1 String", "0.1", impl.toString(true));

                impl = new RawtypeApfloatImpl("1e-2", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e-2 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e-2 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e-2 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e-2 scale", -1, impl.scale());
                assertEquals("radix " + radix + " 1e-2 String", "0.01", impl.toString(true));

                impl = new RawtypeApfloatImpl("1e-50", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e-50 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e-50 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e-50 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e-50 scale", -49, impl.scale());
                assertEquals("radix " + radix + " 1e-50 String", "0.00000000000000000000000000000000000000000000000001", impl.toString(true));

                impl = new RawtypeApfloatImpl("0.1e" + Long.MIN_VALUE, Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0.1eMIN sign", 0, impl.signum());
                assertEquals("radix " + radix + " 0.1eMIN precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0.1eMIN radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0.1eMIN String", "0", impl.toString(true));

                impl = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0.1eMIN+400 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 0.1eMIN+400 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0.1eMIN+400 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0.1eMIN+400 defaultprec scale", Long.MIN_VALUE + 401, impl.scale());

                impl = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0.1eMAX-400 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 0.1eMAX-400 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0.1eMAX-400 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0.1eMAX-400 defaultprec scale", Long.MAX_VALUE - 399, impl.scale());
            }

            impl = new RawtypeApfloatImpl("1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("01", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 01 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 01 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 01 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 01 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 01 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("1.", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1. sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1. precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1. radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1. scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1. String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("1.0", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1.0 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1.0 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1.0 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1.0 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1.0 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("01.0", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 01.0 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 01.0 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 01.0 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 01.0 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 01.0 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("0.1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 0.1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 0.1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 0.1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0.1 scale", 0, impl.scale());
            assertEquals("radix " + radix + " 0.1 String", "0.1", impl.toString(true));

            impl = new RawtypeApfloatImpl(".1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " .1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " .1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " .1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " .1 scale", 0, impl.scale());
            assertEquals("radix " + radix + " .1 String", "0.1", impl.toString(true));

            impl = new RawtypeApfloatImpl("0.01", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 0.01 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 0.01 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 0.01 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0.01 scale", -1, impl.scale());
            assertEquals("radix " + radix + " 0.01 String", "0.01", impl.toString(true));

            impl = new RawtypeApfloatImpl("0.01", 3, radix, false);
            assertEquals("radix " + radix + " 0.01 prec3 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 0.01 prec3 precision", 3, impl.precision());
            assertEquals("radix " + radix + " 0.01 prec3 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0.01 prec3 scale", -1, impl.scale());
            assertEquals("radix " + radix + " 0.01 prec3 String", "0.01", impl.toString(true));

            impl = new RawtypeApfloatImpl("1.01", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1.01 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1.01 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1.01 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1.01 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1.01 String", "1.01", impl.toString(true));

            impl = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " -1 sign", -1, impl.signum());
            assertEquals("radix " + radix + " -1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " -1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " -1 scale", 1, impl.scale());
            assertEquals("radix " + radix + " -1 String", "-1", impl.toString(true));

            impl = new RawtypeApfloatImpl("+1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " +1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " +1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " +1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " +1 scale", 1, impl.scale());
            assertEquals("radix " + radix + " +1 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("1", Apfloat.INFINITE, radix, true);
            assertEquals("radix " + radix + " 1 integer sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1 integer precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1 integer radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1 integer scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1 integer String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("1000000000000000000000000000000000000000000000000000000001", 1, radix, false);
            assertEquals("radix " + radix + " 1e57+1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1e57+1 precision", 1, impl.precision());
            assertEquals("radix " + radix + " 1e57+1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1e57+1 scale", 58, impl.scale());
            assertEquals("radix " + radix + " 1e57+1 String", "1000000000000000000000000000000000000000000000000000000000", impl.toString(true));
            assertEquals("radix " + radix + " 1e57+1 extended String", "1000000000000000000000000000000000000000000000000000000000", impl.precision(Apfloat.INFINITE).toString(true));

            impl = new RawtypeApfloatImpl("1", Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 1 defaultprec sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1 defaultprec precision", 1, impl.precision());
            assertEquals("radix " + radix + " 1 defaultprec radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1 defaultprec scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1 defaultprec String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("1.0", Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 1.0 defaultprec sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1.0 defaultprec precision", 2, impl.precision());
            assertEquals("radix " + radix + " 1.0 defaultprec radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1.0 defaultprec scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1.0 defaultprec String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl("01", Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 01 defaultprec sign", 1, impl.signum());
            assertEquals("radix " + radix + " 01 defaultprec precision", 1, impl.precision());
            assertEquals("radix " + radix + " 01 defaultprec radix", radix, impl.radix());
            assertEquals("radix " + radix + " 01 defaultprec scale", 1, impl.scale());
            assertEquals("radix " + radix + " 01 defaultprec String", "1", impl.toString(true));
        }

        try
        {
            impl = new RawtypeApfloatImpl("1e0", Apfloat.INFINITE, 10, true);
            fail("1e0 integer accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl("1.", Apfloat.INFINITE, 10, true);
            fail("1. integer accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl("", Apfloat.INFINITE, 10, false);
            fail("empty string accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl(".", Apfloat.INFINITE, 10, false);
            fail(". accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl("1e", Apfloat.INFINITE, 10, false);
            fail("1e accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl("e1", Apfloat.INFINITE, 10, false);
            fail("e1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl("1e-", Apfloat.INFINITE, 10, false);
            fail("1e- accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl("-e-", Apfloat.INFINITE, 10, false);
            fail("-e- accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl("10e" + Long.MAX_VALUE, Apfloat.INFINITE, 10, false);
            fail("10eMAX accepted");
        }
        catch (RuntimeException re)
        {
            // OK: should not be allowed
        }
    }

    public static void testStreamConstructor()
        throws IOException
    {
        ApfloatImpl impl;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            impl = new RawtypeApfloatImpl(getReader("0"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 0 sign", 0, impl.signum());
            assertEquals("radix " + radix + " 0 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 0 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0 String", "0", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("00"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 00 sign", 0, impl.signum());
            assertEquals("radix " + radix + " 00 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 00 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 00 String", "0", impl.toString(true));

            if (radix < 15)
            {
                impl = new RawtypeApfloatImpl(getReader("0e5"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0e5 sign", 0, impl.signum());
                assertEquals("radix " + radix + " 0e5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0e5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0e5 String", "0", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1E5"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1E5 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1E5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1E5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1E5 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1E5 String", "100000", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1.0E5"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1.0E5 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1.0E5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1.0E5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1.0E5 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1.0E5 String", "100000", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1.1E5"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1.1E5 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1.1E5 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1.1E5 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1.1E5 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1.1E5 String", "110000", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1.1E5"), 1, radix, false);
                assertEquals("radix " + radix + " 1.1E5 prec1 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1.1E5 prec1 precision", 1, impl.precision());
                assertEquals("radix " + radix + " 1.1E5 prec1 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1.1E5 prec1 scale", 6, impl.scale());
                assertEquals("radix " + radix + " 1.1E5 prec1 String", "100000", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1e50"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e50 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e50 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e50 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e50 scale", 51, impl.scale());
                assertEquals("radix " + radix + " 1e50 String", "100000000000000000000000000000000000000000000000000", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1e-1"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e-1 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e-1 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e-1 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e-1 scale", 0, impl.scale());
                assertEquals("radix " + radix + " 1e-1 String", "0.1", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1e-2"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e-2 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e-2 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e-2 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e-2 scale", -1, impl.scale());
                assertEquals("radix " + radix + " 1e-2 String", "0.01", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1e-50"), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e-50 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 1e-50 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 1e-50 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 1e-50 scale", -49, impl.scale());
                assertEquals("radix " + radix + " 1e-50 String", "0.00000000000000000000000000000000000000000000000001", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("0.1e" + Long.MIN_VALUE), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0.1eMIN sign", 0, impl.signum());
                assertEquals("radix " + radix + " 0.1eMIN precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0.1eMIN radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0.1eMIN String", "0", impl.toString(true));

                impl = new RawtypeApfloatImpl(getReader("1e" + (Long.MIN_VALUE + 400)), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0.1eMIN+400 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 0.1eMIN+400 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0.1eMIN+400 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0.1eMIN+400 defaultprec scale", Long.MIN_VALUE + 401, impl.scale());

                impl = new RawtypeApfloatImpl(getReader("1e" + (Long.MAX_VALUE - 400)), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 0.1eMAX-400 sign", 1, impl.signum());
                assertEquals("radix " + radix + " 0.1eMAX-400 precision", Apfloat.INFINITE, impl.precision());
                assertEquals("radix " + radix + " 0.1eMAX-400 radix", radix, impl.radix());
                assertEquals("radix " + radix + " 0.1eMAX-400 defaultprec scale", Long.MAX_VALUE - 399, impl.scale());
            }

            impl = new RawtypeApfloatImpl(getReader("1"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("01"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 01 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 01 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 01 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 01 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 01 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("1."), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1. sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1. precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1. radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1. scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1. String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("1.0"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1.0 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1.0 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1.0 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1.0 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1.0 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("01.0"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 01.0 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 01.0 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 01.0 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 01.0 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 01.0 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("0.1"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 0.1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 0.1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 0.1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0.1 scale", 0, impl.scale());
            assertEquals("radix " + radix + " 0.1 String", "0.1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader(".1"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " .1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " .1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " .1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " .1 scale", 0, impl.scale());
            assertEquals("radix " + radix + " .1 String", "0.1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("0.01"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 0.01 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 0.01 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 0.01 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0.01 scale", -1, impl.scale());
            assertEquals("radix " + radix + " 0.01 String", "0.01", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("0.01"), 3, radix, false);
            assertEquals("radix " + radix + " 0.01 prec3 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 0.01 prec3 precision", 3, impl.precision());
            assertEquals("radix " + radix + " 0.01 prec3 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 0.01 prec3 scale", -1, impl.scale());
            assertEquals("radix " + radix + " 0.01 prec3 String", "0.01", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("1.01"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1.01 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1.01 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1.01 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1.01 scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1.01 String", "1.01", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("-1"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " -1 sign", -1, impl.signum());
            assertEquals("radix " + radix + " -1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " -1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " -1 scale", 1, impl.scale());
            assertEquals("radix " + radix + " -1 String", "-1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("+1"), Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " +1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " +1 precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " +1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " +1 scale", 1, impl.scale());
            assertEquals("radix " + radix + " +1 String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("1"), Apfloat.INFINITE, radix, true);
            assertEquals("radix " + radix + " 1 integer sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1 integer precision", Apfloat.INFINITE, impl.precision());
            assertEquals("radix " + radix + " 1 integer radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1 integer scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1 integer String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("1000000000000000000000000000000000000000000000000000000001"), 1, radix, false);
            assertEquals("radix " + radix + " 1e57+1 sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1e57+1 precision", 1, impl.precision());
            assertEquals("radix " + radix + " 1e57+1 radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1e57+1 scale", 58, impl.scale());
            assertEquals("radix " + radix + " 1e57+1 String", "1000000000000000000000000000000000000000000000000000000000", impl.toString(true));
            assertEquals("radix " + radix + " 1e57+1 extended String", "1000000000000000000000000000000000000000000000000000000000", impl.precision(Apfloat.INFINITE).toString(true));

            impl = new RawtypeApfloatImpl(getReader("1"), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 1 defaultprec sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1 defaultprec precision", 1, impl.precision());
            assertEquals("radix " + radix + " 1 defaultprec radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1 defaultprec scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1 defaultprec String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("1.0"), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 1.0 defaultprec sign", 1, impl.signum());
            assertEquals("radix " + radix + " 1.0 defaultprec precision", 2, impl.precision());
            assertEquals("radix " + radix + " 1.0 defaultprec radix", radix, impl.radix());
            assertEquals("radix " + radix + " 1.0 defaultprec scale", 1, impl.scale());
            assertEquals("radix " + radix + " 1.0 defaultprec String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader("01"), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 01 defaultprec sign", 1, impl.signum());
            assertEquals("radix " + radix + " 01 defaultprec precision", 1, impl.precision());
            assertEquals("radix " + radix + " 01 defaultprec radix", radix, impl.radix());
            assertEquals("radix " + radix + " 01 defaultprec scale", 1, impl.scale());
            assertEquals("radix " + radix + " 01 defaultprec String", "1", impl.toString(true));

            impl = new RawtypeApfloatImpl(getReader('1', 9 * 16383), Apfloat.INFINITE, radix, true);
            assertEquals("radix " + radix + " 9 * 16383 String length", 9 * 16383, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 16384), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 9 * 16384 String length", 9 * 16384, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 16385), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 9 * 16385 String length", 9 * 16385, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 65535), Apfloat.INFINITE, radix, true);
            assertEquals("radix " + radix + " 9 * 65535 String length", 9 * 65535, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 65536), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 9 * 65536 String length", 9 * 65536, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 65537), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 9 * 65537 String length", 9 * 65537, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 1048575), Apfloat.INFINITE, radix, true);
            assertEquals("radix " + radix + " 9 * 1048575 String length", 9 * 1048575, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 1048576), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 9 * 1048576 String length", 9 * 1048576, impl.toString(true).length());
            impl = new RawtypeApfloatImpl(getReader('1', 9 * 1048577), Apfloat.DEFAULT, radix, false);
            assertEquals("radix " + radix + " 9 * 1048577 String length", 9 * 1048577, impl.toString(true).length());
        }

        PushbackReader reader = getReader("10e-1");
        impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
        assertEquals("10e-1 size", 1, impl.size());
        assertTrue("10e-1 isOne", impl.isOne());

        reader = getReader("0.1e1");
        impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
        assertEquals("0.1e1 size", 1, impl.size());
        assertTrue("0.1e1 isOne", impl.isOne());

        reader = getReader("1e1");
        impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
        assertEquals("1e1 size", 1, impl.size());
        assertFalse("1e1 isOne", impl.isOne());

        reader = getReader("1.1e1");
        impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
        assertEquals("1.1e1 size", 2, impl.size());
        assertFalse("1.1e1 isOne", impl.isOne());

        reader = getReader("1a");
        impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
        assertEquals("1a read as is", 'a', reader.read());
        assertEquals("1a read incorrectly", "1", impl.toString(true));

        reader = getReader("1e0");
        impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, true);
        assertEquals("1e0 integer read as is", 'e', reader.read());
        assertEquals("1e0 integer read incorrectly", "1", impl.toString(true));

        reader = getReader("1.");
        impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, true);
        assertEquals("1. integer read as is", '.', reader.read());
        assertEquals("1. integer read incorrectly", "1", impl.toString(true));

        // Test transition from memory allocation to disk allocation
        ApfloatContext ctx = ApfloatContext.getContext();
        long memoryThreshold = ctx.getMemoryThreshold();
        int blockSize = ctx.getBlockSize();
        try
        {
            int size = 1 << 7;
            ctx.setMemoryThreshold(size);
            ctx.setBlockSize(size);

            Random random = new Random();
            StringBuilder buffer = new StringBuilder();
            buffer.append('1');                     // First character(s) should not be zero digits
            for (int i = 0; i < 4 * size - 1; i++)
            {
                buffer.append(Character.forDigit(random.nextInt(16), 16));
            }
            String text = buffer.toString();
            reader = getReader(text);
            impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 16, true);
            assertEquals("Memory to disk transition", text, impl.toString(true));
        }
        finally
        {
           ctx.setMemoryThreshold(memoryThreshold);
           ctx.setBlockSize(blockSize);
        }

        try
        {
            impl = new RawtypeApfloatImpl(getReader(""), Apfloat.INFINITE, 10, false);
            fail("empty string accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = new RawtypeApfloatImpl(getReader("."), Apfloat.INFINITE, 10, false);
            fail(". accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        reader = getReader(".[");
        try
        {
            impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
            fail(". accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
            assertEquals(". read next", '[', reader.read());
        }

        try
        {
            impl = new RawtypeApfloatImpl(getReader("1e"), Apfloat.INFINITE, 10, false);
            fail("1e accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        reader = getReader("e1");
        try
        {
            impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
            fail("e1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
            assertEquals("e1 read next", 'e', reader.read());
        }

        try
        {
            impl = new RawtypeApfloatImpl(getReader("1e-"), Apfloat.INFINITE, 10, false);
            fail("1e- accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
        }

        reader = getReader("1e-[");
        try
        {
            impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
            fail("1e- accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
            assertEquals("1e- read next", '[', reader.read());
        }

        reader = getReader("-e-");
        try
        {
            impl = new RawtypeApfloatImpl(reader, Apfloat.INFINITE, 10, false);
            fail("-e- accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK: should not be allowed
            assertEquals("-e- read next", 'e', reader.read());
        }

        try
        {
            impl = new RawtypeApfloatImpl(getReader("10e" + Long.MAX_VALUE), Apfloat.INFINITE, 10, false);
            fail("10eMAX accepted");
        }
        catch (RuntimeException re)
        {
            // OK: should not be allowed
        }
    }

    private static PushbackReader getReader(String value)
    {
        return new PushbackReader(new StringReader(value));
    }

    private static PushbackReader getReader(char character, long size)
    {
        return new PushbackReader(new Reader()
        {
            @Override
            public int read()
            {
                if (this.remaining > 0 )
                {
                    this.remaining--;
                    return character;
                }
                else
                {
                    return -1;
                }
            }

            @Override
            public int read(char[] buffer, int offset, int length)
            {
                length = (int) Math.min(length, this.remaining);
                for (int i = 0; i < length; i++)
                {
                    buffer[offset + i] = character;
                }
                this.remaining -= length;
                return length;
            }

            @Override
            public void close() {}

            private long remaining = size;
        });
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

    public static void testAdd()
    {
        ApfloatImpl a, b, r;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            char nine = Character.forDigit(radix - 1, radix),
                 eight = Character.forDigit(radix - 2, radix);

            for (int i = 10000; i <= 10005; i++)
            {
                a = new RawtypeApfloatImpl(getString(nine, i), Apfloat.INFINITE, radix, true);
                b = new RawtypeApfloatImpl(getString(nine, i), Apfloat.INFINITE, radix, true);
                r = a.addOrSubtract(a, false);
                assertEquals("radix " + radix + " 9(" + i + ") * 2 precision", Apfloat.INFINITE, r.precision());
                assertEquals("radix " + radix + " 9(" + i + ") * 2 String", '1' + getString(nine, i - 1) + eight, r.toString(true));
                r = a.addOrSubtract(b, false);
                assertEquals("radix " + radix + " 9(" + i + ") + 9(" + i + ") precision", Apfloat.INFINITE, r.precision());
                assertEquals("radix " + radix + " 9(" + i + ") + 9(" + i + ") String", '1' + getString(nine, i - 1) + eight, r.toString(true));

                a = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                b = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                r = a.addOrSubtract(a, false);
                assertEquals("radix " + radix + " 9(" + i + ") * 2, prec precision", i + 1, r.precision());
                assertEquals("radix " + radix + " 9(" + i + ") * 2, prec String", '1' + getString(nine, i - 1) + eight, r.toString(true));
                r = a.addOrSubtract(b, false);
                assertEquals("radix " + radix + " 9(" + i + ") + 9(" + i + "), prec precision", i + 1, r.precision());
                assertEquals("radix " + radix + " 9(" + i + ") + 9(" + i + "), prec String", '1' + getString(nine, i - 1) + eight, r.toString(true));
            }
        }

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(a, false);
        assertEquals("1 * 2 precision", Apfloat.INFINITE, r.precision());
        assertEquals("1 * 2 String", "2", r.toString(true));

        a = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(a, false);
        assertEquals("1 * 2, prec precision", 1, r.precision());
        assertEquals("1 * 2, prec String", "2", r.toString(true));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("1 + 1 precision", Apfloat.INFINITE, r.precision());
        assertEquals("1 + 1 String", "2", r.toString(true));

        a = new RawtypeApfloatImpl(1, 1, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("1 + 1, prec precision", 1, r.precision());
        assertEquals("1 + 1, prec String", "2", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(a, false);
        assertEquals("999999999 * 2 precision", Apfloat.INFINITE, r.precision());
        assertEquals("999999999 * 2 String", "1999999998", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(999999999, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("999999999 + 999999999 precision", Apfloat.INFINITE, r.precision());
        assertEquals("999999999 + 999999999 String", "1999999998", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, 9, 10);
        r = a.addOrSubtract(a, false);
        assertEquals("999999999 * 2, prec precision", 10, r.precision());
        assertEquals("999999999 * 2, prec String", "1999999998", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, 9, 10);
        b = new RawtypeApfloatImpl(999999999, 9, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("999999999 + 999999999, prec precision", 10, r.precision());
        assertEquals("999999999 + 999999999, prec String", "1999999998", r.toString(true));

        a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, 2, false);
        r = a.addOrSubtract(b, false);
        assertEquals("max + min precision", Apfloat.INFINITE, r.precision());
        assertEquals("max + min scale", Long.MAX_VALUE - 399, r.scale());

        a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), 1, 2, false);
        b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), 1, 2, false);
        r = a.addOrSubtract(b, false);
        assertEquals("max + min, prec precision", 1, r.precision());
        assertEquals("max + min, prec scale", Long.MAX_VALUE - 399, r.scale());

        a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, 2, false);
        try
        {
            for (int i = 0; i < 400; i++)
            {
                r = a.addOrSubtract(b, false);
                a = a.addOrSubtract(b, false);
                b = r;
            }
            fail("No overflow adding");
        }
        catch (OverflowException oe)
        {
            // OK: overflow
            assertEquals("Localization key", "overflow", oe.getLocalizationKey());
        }

        a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, 2, false);
        try
        {
            for (int i = 0; i < 400; i++)
            {
                a = a.addOrSubtract(a, false);
            }
            fail("No overflow doubling");
        }
        catch (OverflowException oe)
        {
            // OK: overflow
            assertEquals("Localization key", "overflow", oe.getLocalizationKey());
        }

        a = new RawtypeApfloatImpl(999999999, 9, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("999999999 + 1, prec precision", 10, r.precision());
        assertEquals("999999999 + 1, prec String", "1000000000", r.toString(true));

        a = new RawtypeApfloatImpl(1, 1, 10);
        b = new RawtypeApfloatImpl(999999999, 9, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("1 + 999999999, prec precision", 10, r.precision());
        assertEquals("1 + 999999999, prec String", "1000000000", r.toString(true));

        a = new RawtypeApfloatImpl(999999998, 9, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("999999998 + 1, prec precision", 9, r.precision());
        assertEquals("999999998 + 1, prec String", "999999999", r.toString(true));

        a = new RawtypeApfloatImpl(9999999999L, 10, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("9999999999 + 1, prec precision", 11, r.precision());
        assertEquals("9999999999 + 1, prec String", "10000000000", r.toString(true));

        a = new RawtypeApfloatImpl(1, 1, 10);
        b = new RawtypeApfloatImpl(9999999999L, 10, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("1 + 9999999999, prec precision", 11, r.precision());
        assertEquals("1 + 9999999999, prec String", "10000000000", r.toString(true));

        a = new RawtypeApfloatImpl(-999999999, 9, 10);
        b = new RawtypeApfloatImpl(-1, 1, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("-999999999 - 1, prec precision", 10, r.precision());
        assertEquals("-999999999 - 1, prec String", "-1000000000", r.toString(true));

        a = new RawtypeApfloatImpl(-1, 1, 10);
        b = new RawtypeApfloatImpl(-999999999, 9, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("-1 - 999999999, prec precision", 10, r.precision());
        assertEquals("-1 - 999999999, prec String", "-1000000000", r.toString(true));

        a = new RawtypeApfloatImpl(-999999998, 9, 10);
        b = new RawtypeApfloatImpl(-1, 1, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("-999999998 - 1, prec precision", 9, r.precision());
        assertEquals("-999999998 - 1, prec String", "-999999999", r.toString(true));

        a = new RawtypeApfloatImpl(-9999999999L, 10, 10);
        b = new RawtypeApfloatImpl(-1, 1, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("-9999999999 - 1, prec precision", 11, r.precision());
        assertEquals("-9999999999 - 1, prec String", "-10000000000", r.toString(true));

        a = new RawtypeApfloatImpl(-1, 1, 10);
        b = new RawtypeApfloatImpl(-9999999999L, 10, 10);
        r = a.addOrSubtract(b, false);
        assertEquals("-1 - 9999999999, prec precision", 11, r.precision());
        assertEquals("-1 - 9999999999, prec String", "-10000000000", r.toString(true));

        a = new RawtypeApfloatImpl(4444444445L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(5555555555L, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(b, false);
        assertTrue("4444444445 + 5555555555, isShort", r.isShort());
        assertEquals("4444444445 + 5555555555, String", "10000000000", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999999999999999999999999999999999900000000000000000000", 60, 10, false);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999999999999999999999999", Apfloat.DEFAULT, 10, false);
        r = a.addOrSubtract(b, false);
        assertEquals("Partial overlap 1, prec precision", 61, r.precision());
        assertEquals("Partial overlap 1, prec String", "10000000000000000000099999999999999999999999999999899999999990000000000", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999999999999999999999999999999999900000000000000000000", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999999999999999999999999", Apfloat.INFINITE, 10, true);
        r = a.addOrSubtract(b, false);
        assertEquals("Partial overlap 1 precision", Apfloat.INFINITE, r.precision());
        assertEquals("Partial overlap 1 String", "10000000000000000000099999999999999999999999999999899999999999999999999", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999900000000000000000000000000000000000000000000000000", 60, 10, false);
        b = new RawtypeApfloatImpl("0000000000000000000000000000000000000000000000000099999999999999999999", Apfloat.DEFAULT, 10, false);
        r = a.addOrSubtract(b, false);
        assertEquals("Partial overlap 2, prec precision", 60, r.precision());
        assertEquals("Partial overlap 2, prec String", "9999999999999999999900000000000000000000000000000099999999990000000000", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999900000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl("0000000000000000000000000000000000000000000000000099999999999999999999", Apfloat.INFINITE, 10, true);
        r = a.addOrSubtract(b, false);
        assertEquals("Partial overlap 2 precision", Apfloat.INFINITE, r.precision());
        assertEquals("Partial overlap 2 String", "9999999999999999999900000000000000000000000000000099999999999999999999", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999999999999999999999999999999999999999999999999999999", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999900000000000000000000", 40, 10, false);
        r = a.addOrSubtract(b, false);
        assertEquals("Partial overlap 3, prec precision", 61, r.precision());
        assertEquals("Partial overlap 3, prec String", "10000000000000000000099999999999999999999999999999899999999990000000000", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999999999999999999999999999999999999999999999999999999", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999900000000000000000000", Apfloat.INFINITE, 10, true);
        r = a.addOrSubtract(b, false);
        assertEquals("Partial overlap 3 precision", Apfloat.INFINITE, r.precision());
        assertEquals("Partial overlap 3 String", "10000000000000000000099999999999999999999999999999899999999999999999999", r.toString(true));

        a = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 3, false);
        try
        {
            a.addOrSubtract(b, false);
            fail("No radix mismatch");
        }
        catch (RadixMismatchException rme)
        {
            // OK: radix mismatch
            assertEquals("Localization key", "radix.mismatch", rme.getLocalizationKey());
        }

        b = (RawType.TYPE.equals(Integer.TYPE) ? new LongApfloatImpl(0, Apfloat.INFINITE, 10) : new IntApfloatImpl(0, Apfloat.INFINITE, 10));
        try
        {
            a.addOrSubtract(b, false);
            fail("No implementation mismatch");
        }
        catch (ImplementationMismatchException ime)
        {
            // OK: wrong implementation class
            assertEquals("Localization key", "type.mismatch", ime.getLocalizationKey());
        }
    }

    public static void testSubtract()
    {
        ApfloatImpl a, b, r;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            char nine = Character.forDigit(radix - 1, radix),
                 eight = Character.forDigit(radix - 2, radix);

            for (int i = 10000; i <= 10005; i++)
            {
                a = new RawtypeApfloatImpl(getString(nine, i), Apfloat.INFINITE, radix, true);
                b = new RawtypeApfloatImpl(getString(nine, i), Apfloat.INFINITE, radix, true);
                r = a.addOrSubtract(b, true);
                assertEquals("radix " + radix + " 9(" + i + ") - 9(" + i + ") precision", Apfloat.INFINITE, r.precision());
                assertEquals("radix " + radix + " 9(" + i + ") - 9(" + i + ") String", "0", r.toString(true));

                a = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                b = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                r = a.addOrSubtract(b, true);
                assertEquals("radix " + radix + " 9(" + i + ") - 9(" + i + "), prec precision", Apfloat.INFINITE, r.precision());
                assertEquals("radix " + radix + " 9(" + i + ") - 9(" + i + "), prec String", "0", r.toString(true));

                if (radix > 2)
                {
                    a = new RawtypeApfloatImpl(getString(nine, i), Apfloat.INFINITE, radix, true);
                    b = new RawtypeApfloatImpl(getString(eight, i), Apfloat.INFINITE, radix, true);
                    r = a.addOrSubtract(b, true);
                    assertEquals("radix " + radix + " 9(" + i + ") - 8(" + i + ") precision", Apfloat.INFINITE, r.precision());
                    assertEquals("radix " + radix + " 9(" + i + ") - 8(" + i + ") String", getString('1', i), r.toString(true));

                    // "eight" is zero in base-2
                    a = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                    b = new RawtypeApfloatImpl(getString(eight, i), i, radix, false);
                    r = a.addOrSubtract(b, true);
                    assertEquals("radix " + radix + " 9(" + i + ") - 8(" + i + "), prec precision", i, r.precision());
                    assertEquals("radix " + radix + " 9(" + i + ") - 8(" + i + "), prec String", getString('1', i), r.toString(true));

                    a = new RawtypeApfloatImpl(getString(eight, i), Apfloat.INFINITE, radix, true);
                    b = new RawtypeApfloatImpl(getString(nine, i), Apfloat.INFINITE, radix, true);
                    r = a.addOrSubtract(b, true);
                    assertEquals("radix " + radix + " 8(" + i + ") - 9(" + i + ") precision", Apfloat.INFINITE, r.precision());
                    assertEquals("radix " + radix + " 8(" + i + ") - 9(" + i + ") String", '-' + getString('1', i), r.toString(true));

                    a = new RawtypeApfloatImpl(getString(eight, i), i, radix, false);
                    b = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                    r = a.addOrSubtract(b, true);
                    assertEquals("radix " + radix + " 8(" + i + ") - 9(" + i + "), prec precision", i, r.precision());
                    assertEquals("radix " + radix + " 8(" + i + ") - 9(" + i + "), prec String", '-' + getString('1', i), r.toString(true));
                }
            }
        }

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(a, true);
        assertEquals("1 to 0 precision", Apfloat.INFINITE, r.precision());
        assertEquals("1 to 0 String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("1 - 1 precision", Apfloat.INFINITE, r.precision());
        assertEquals("1 - 1 String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(1, 1, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("1 - 1, prec precision", Apfloat.INFINITE, r.precision());
        assertEquals("1 - 1, prec String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(999999999, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("999999999 - 999999999 precision", Apfloat.INFINITE, r.precision());
        assertEquals("999999999 - 999999999 String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, 9, 10);
        b = new RawtypeApfloatImpl(999999999, 9, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("999999999 - 999999999, prec precision", Apfloat.INFINITE, r.precision());
        assertEquals("999999999 - 999999999, prec String", "0", r.toString(true));

        a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, 2, false);
        r = a.addOrSubtract(b, true);
        assertEquals("max + min precision", Apfloat.INFINITE, r.precision());
        assertEquals("max + min scale", Long.MAX_VALUE - 399, r.scale());

        a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), 1, 2, false);
        b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), 1, 2, false);
        r = a.addOrSubtract(b, true);
        assertEquals("max + min, prec precision", 1, r.precision());
        assertEquals("max + min, prec scale", Long.MAX_VALUE - 399, r.scale());

        a = new RawtypeApfloatImpl("1.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, 10, false);
        r = a.addOrSubtract(b, true);
        assertTrue("min underflow precondition a", !a.toString().equals("1e" + (Long.MIN_VALUE + 400)));
        assertTrue("min underflow precondition b", !b.toString().equals("1e" + (Long.MIN_VALUE + 400)));
        assertEquals("min underflow precision", Apfloat.INFINITE, r.precision());
        assertEquals("min underflow String", "0", r.toString(true));

        a = new RawtypeApfloatImpl("1.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001e" + (Long.MIN_VALUE + 400), Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("1.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002e" + (Long.MIN_VALUE + 400), Apfloat.DEFAULT, 10, false);
        r = a.addOrSubtract(b, true);
        assertTrue("min underflow, prec precondition a", !a.toString().equals("1e" + (Long.MIN_VALUE + 400)));
        assertTrue("min underflow, prec precondition b", !b.toString().equals("1e" + (Long.MIN_VALUE + 400)));
        assertEquals("min underflow, prec precision", Apfloat.INFINITE, r.precision());
        assertEquals("min underflow, prec String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(1000000000L, 10, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("1000000000 - 1, prec precision", 9, r.precision());
        assertEquals("1000000000 - 1, prec String", "999999999", r.toString(true));

        a = new RawtypeApfloatImpl(1, 1, 10);
        b = new RawtypeApfloatImpl(1000000000L, 10, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("1 - 1000000000, prec precision", 9, r.precision());
        assertEquals("1 - 1000000000, prec String", "-999999999", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, 9, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("999999999 - 1, prec precision", 9, r.precision());
        assertEquals("999999999 - 1, prec String", "999999998", r.toString(true));

        a = new RawtypeApfloatImpl(10000000000L, 11, 10);
        b = new RawtypeApfloatImpl(1, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("10000000000 - 1, prec precision", 10, r.precision());
        assertEquals("10000000000 - 1, prec String", "9999999999", r.toString(true));

        a = new RawtypeApfloatImpl(1, 1, 10);
        b = new RawtypeApfloatImpl(10000000000L, 11, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("1 - 10000000000, prec precision", 10, r.precision());
        assertEquals("1 - 10000000000, prec String", "-9999999999", r.toString(true));

        a = new RawtypeApfloatImpl(-1000000000L, 10, 10);
        b = new RawtypeApfloatImpl(-1, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("-1000000000 + 1, prec precision", 9, r.precision());
        assertEquals("-1000000000 + 1, prec String", "-999999999", r.toString(true));

        a = new RawtypeApfloatImpl(-1, 1, 10);
        b = new RawtypeApfloatImpl(-1000000000L, 10, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("-1 + 1000000000, prec precision", 9, r.precision());
        assertEquals("-1 + 1000000000, prec String", "999999999", r.toString(true));

        a = new RawtypeApfloatImpl(-999999999, 9, 10);
        b = new RawtypeApfloatImpl(-1, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("-999999999 + 1, prec precision", 9, r.precision());
        assertEquals("-999999999 + 1, prec String", "-999999998", r.toString(true));

        a = new RawtypeApfloatImpl(-10000000000L, 11, 10);
        b = new RawtypeApfloatImpl(-1, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("-10000000000 + 1, prec precision", 10, r.precision());
        assertEquals("-10000000000 + 1, prec String", "-9999999999", r.toString(true));

        a = new RawtypeApfloatImpl(-1, 1, 10);
        b = new RawtypeApfloatImpl(-10000000000L, 11, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("-1 + 10000000000, prec precision", 10, r.precision());
        assertEquals("-1 + 10000000000, prec String", "9999999999", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, 9, 10);
        b = new RawtypeApfloatImpl(999999998, 9, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("999999999 - 999999998, limited precision precision", 1, r.precision());
        assertEquals("999999999 - 999999998, limited precision String", "1", r.toString(true));

        a = new RawtypeApfloatImpl(999923456, 9, 10);
        b = new RawtypeApfloatImpl(999912345, 9, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("999923456 - 999912345, limited precision precision", 5, r.precision());
        assertEquals("999923456 - 999912345, limited precision String", "11111", r.toString(true));

        a = new RawtypeApfloatImpl(923456789, 9, 10);
        b = new RawtypeApfloatImpl(912345678, 9, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("923456789 - 912345678, limited precision precision", 8, r.precision());
        assertEquals("923456789 - 912345678, limited precision String", "11111111", r.toString(true));

        a = new RawtypeApfloatImpl(9999999999L, 10, 10);
        b = new RawtypeApfloatImpl(9999999998L, 10, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("9999999999 - 9999999998, limited precision precision", 1, r.precision());
        assertEquals("9999999999 - 9999999998, limited precision String", "1", r.toString(true));

        a = new RawtypeApfloatImpl(9999923456L, 10, 10);
        b = new RawtypeApfloatImpl(9999912345L, 10, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("9999923456 - 9999912345, limited precision precision", 5, r.precision());
        assertEquals("9999923456 - 9999912345, limited precision String", "11111", r.toString(true));

        a = new RawtypeApfloatImpl(9234567899L, 10, 10);
        b = new RawtypeApfloatImpl(9123456788L, 10, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("9234567899 - 9123456788, limited precision precision", 9, r.precision());
        assertEquals("9234567899 - 9123456788, limited precision String", "111111111", r.toString(true));

        a = new RawtypeApfloatImpl(999999999, 1, 10);
        b = new RawtypeApfloatImpl(999999998, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("999999999 - 999999998, lost precision precision", Apfloat.INFINITE, r.precision());
        assertEquals("999999999 - 999999998, lost precision String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(999923456, 4, 10);
        b = new RawtypeApfloatImpl(999912345, 4, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("999923456 - 999912345, lost precision precision", Apfloat.INFINITE, r.precision());
        assertEquals("999923456 - 999912345, lost precision String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(9999999999L, 2, 10);
        b = new RawtypeApfloatImpl(9999999998L, 2, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("9999999999 - 9999999998, lost precision precision", Apfloat.INFINITE, r.precision());
        assertEquals("9999999999 - 9999999998, lost precision String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(9999923456L, 5, 10);
        b = new RawtypeApfloatImpl(9999912345L, 5, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("9999923456 - 9999912345, lost precision precision", Apfloat.INFINITE, r.precision());
        assertEquals("9999923456 - 9999912345, lost precision String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(9999999999L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(999999999L, Apfloat.INFINITE, 10);
        r = a.addOrSubtract(b, true);
        assertTrue("9999999999 - 999999999, isShort", r.isShort());
        assertEquals("9999999999 - 999999999, String", "9000000000", r.toString(true));

        a = new RawtypeApfloatImpl(1000000000, 2, 10);
        b = new RawtypeApfloatImpl(990000000, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("1000000000 - 990000000, lost precision precision", Apfloat.INFINITE, r.precision());
        assertEquals("1000000000 - 990000000, lost precision String", "0", r.toString(true));

        a = new RawtypeApfloatImpl(1000000000, 2, 10);
        b = new RawtypeApfloatImpl(999999999, 1, 10);
        r = a.addOrSubtract(b, true);
        assertEquals("1000000000 - 999999999, lost precision precision", Apfloat.INFINITE, r.precision());
        assertEquals("1000000000 - 999999999, lost precision String", "0", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999000000000099999999999999999999999999999900000000000000000000", 60, 10, false);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999999999999999999999999", Apfloat.DEFAULT, 10, false);
        r = a.addOrSubtract(b, true);
        assertEquals("Partial overlap 1, prec precision", 60, r.precision());
        assertEquals("Partial overlap 1, prec String", "9999999998999999999999999999999999999999999999999900000000000000000000", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999000000000099999999999999999999999999999900000000000000000000", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999999999999999999999999", Apfloat.INFINITE, 10, true);
        r = a.addOrSubtract(b, true);
        assertEquals("Partial overlap 1 precision", Apfloat.INFINITE, r.precision());
        assertEquals("Partial overlap 1 String", "9999999998999999999999999999999999999999999999999900000000000000000001", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999900000000000000000000000000000000000000000000000000", 60, 10, false);
        b = new RawtypeApfloatImpl("0000000000000000000000000000000000000000000000000099999999999999999999", Apfloat.DEFAULT, 10, false);
        r = a.addOrSubtract(b, true);
        assertEquals("Partial overlap 2, prec precision", 60, r.precision());
        assertEquals("Partial overlap 2, prec String", "9999999999999999999899999999999999999999999999999900000000000000000000", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999900000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl("0000000000000000000000000000000000000000000000000099999999999999999999", Apfloat.INFINITE, 10, true);
        r = a.addOrSubtract(b, true);
        assertEquals("Partial overlap 2 precision", Apfloat.INFINITE, r.precision());
        assertEquals("Partial overlap 2 String", "9999999999999999999899999999999999999999999999999900000000000000000001", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999900000000009999999999999999999999999999999999999999", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999900000000000000000000", 40, 10, false);
        r = a.addOrSubtract(b, true);
        assertEquals("Partial overlap 3, prec precision", 60, r.precision());
        assertEquals("Partial overlap 3, prec String", "9999999999999999999800000000010000000000000000000099999999990000000000", r.toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999900000000009999999999999999999999999999999999999999", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl("0000000000000000000099999999999999999999999999999900000000000000000000", Apfloat.INFINITE, 10, true);
        r = a.addOrSubtract(b, true);
        assertEquals("Partial overlap 3 precision", Apfloat.INFINITE, r.precision());
        assertEquals("Partial overlap 3 String", "9999999999999999999800000000010000000000000000000099999999999999999999", r.toString(true));

        a = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 3, false);
        try
        {
            a.addOrSubtract(b, true);
            fail("No radix mismatch");
        }
        catch (RadixMismatchException rme)
        {
            // OK: radix mismatch
            assertEquals("Localization key", "radix.mismatch", rme.getLocalizationKey());
        }
    }

    public static void testMultiply()
    {
        ApfloatContext.getContext().setMemoryThreshold(1 << 30);        // To improve performance and stability...

        ApfloatImpl a, b;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " 0 * 1", "0", a.multiply(b).toString(true));

            a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " 1 * 1", "1", a.multiply(b).toString(true));

            a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " 1 * -1", "-1", a.multiply(b).toString(true));

            a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " -1 * 1.1", "-1.1", a.multiply(b).toString(true));

            if (radix < 15)
            {
                a = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " e * e", "0", a.multiply(b).toString(true));

                a = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE / 2), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE / 2), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " e * e just", "0", a.multiply(b).toString(false));

                a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, radix, false);
                try
                {
                    a.multiply(b);
                    fail("radix " + radix + " no overflow");
                }
                catch (OverflowException oe)
                {
                    // OK: overflow
                    assertEquals("Localization key", "overflow", oe.getLocalizationKey());
                }
            }

            char nine = Character.forDigit(radix - 1, radix),
                 eight = Character.forDigit(radix - 2, radix);

            for (int i = 2; i <= 131072; i *= 2)
            {
                a = new RawtypeApfloatImpl(getString(nine, i), Apfloat.INFINITE, radix, false);
                for (int j = 2; j <= i; j *= 2)
                {
                    String expected = getString(nine, j - 1) + eight + getString(nine, i - j) + getString('0', j - 1) + '1';
                    b = new RawtypeApfloatImpl(getString(nine, j), Apfloat.INFINITE, radix, false);
                    assertEquals("radix " + radix + " 9(" + i + ") * 9(" + j + ")", expected, a.multiply(b).toString(true));
                    if (j == i)
                    {
                        assertEquals("radix " + radix + " 9(" + i + ")^2", expected, a.multiply(a).toString(true));
                    }
                }
            }

            for (int i = 10000; i <= 10005; i++)
            {
                a = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                b = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                assertEquals("radix " + radix + " 9(" + i + ") * 9(" + i + "), prec", getString(nine, i - 2), a.multiply(b).toString(true).substring(0, i - 2));
            }
        }

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1", 16, 10, false);
        assertEquals("1234567890123456 * 1", "1234567890123456", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10", 16, 10, false);
        assertEquals("1234567890123456 * 10", "12345678901234560", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100", 16, 10, false);
        assertEquals("1234567890123456 * 100", "123456789012345600", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000", 16, 10, false);
        assertEquals("1234567890123456 * 1000", "1234567890123456000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000", 16, 10, false);
        assertEquals("1234567890123456 * 10000", "12345678901234560000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000", 16, 10, false);
        assertEquals("1234567890123456 * 100000", "123456789012345600000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000000", 16, 10, false);
        assertEquals("1234567890123456 * 1000000", "1234567890123456000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000000", 16, 10, false);
        assertEquals("1234567890123456 * 10000000", "12345678901234560000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000000", 16, 10, false);
        assertEquals("1234567890123456 * 100000000", "123456789012345600000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000000000", 16, 10, false);
        assertEquals("1234567890123456 * 1000000000", "1234567890123456000000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000000000", 16, 10, false);
        assertEquals("1234567890123456 * 10000000000", "12345678901234560000000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000000000", 16, 10, false);
        assertEquals("1234567890123456 * 100000000000", "123456789012345600000000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000000000000", 16, 10, false);
        assertEquals("1234567890123456 * 1000000000000", "1234567890123456000000000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000000000000", 16, 10, false);
        assertEquals("1234567890123456 * 10000000000000", "12345678901234560000000000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000000000000", 16, 10, false);
        assertEquals("1234567890123456 * 100000000000000", "123456789012345600000000000000", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 3);
        b = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 3);
        assertEquals("1 * 2", "2", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("aaa", Apfloat.INFINITE, 11, true);
        b = new RawtypeApfloatImpl("aaa", Apfloat.INFINITE, 11, true);
        assertEquals("aaa * aaa", "aa9001", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("9999999999999999999999999999999999999999", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl("9999999999999999999999999999999999999999", Apfloat.INFINITE, 10, true);
        assertEquals("1e40-1 * 1e40-1", "99999999999999999999999999999999999999980000000000000000000000000000000000000001", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        assertEquals("1.1 * 1.1", "1.21", a.multiply(b).toString(true));

        a = new RawtypeApfloatImpl(999999999, 8, 10);
        b = new RawtypeApfloatImpl(999999999, 8, 10);
        assertEquals("999999999 * 999999999, prec", "999999990000000000", a.multiply(b).toString(true));
        assertEquals("999999999^2, prec", "999999990000000000", a.multiply(a).toString(true));

        // Underflow
        a = new RawtypeApfloatImpl("1e-9000000000000000000", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1e-9000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("1e-9000000000000000000 * 1e-9000000000000000000", "0", a.multiply(b).toString(true));

        if (a instanceof IntApfloatImpl)
        {
            a = new RawtypeApfloatImpl("d3eafc3af14601", 14, 16, false);
            b = new RawtypeApfloatImpl("13540775b48cc32ba01", 19, 16, false);
            assertTrue("Fermat number, prec3 isShort", a.multiply(b).isShort());
            assertEquals("Fermat number, prec3", "100000000000000000000000000000000", a.multiply(b).toString(true));

            a = new RawtypeApfloatImpl("d3eafc3af14601", 21, 16, false);
            b = new RawtypeApfloatImpl("13540775b48cc32ba01", 21, 16, false);
            assertTrue("Fermat number, prec4 isShort", a.multiply(b).isShort());
            assertEquals("Fermat number, prec4", "100000000000000000000000000000000", a.multiply(b).toString(true));
        }

        a = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 3, false);
        try
        {
            a.multiply(b);
            fail("No radix mismatch");
        }
        catch (RadixMismatchException rme)
        {
            // OK: radix mismatch
            assertEquals("Localization key", "radix.mismatch", rme.getLocalizationKey());
        }

        b = (RawType.TYPE.equals(Integer.TYPE) ? new LongApfloatImpl(0, Apfloat.INFINITE, 10) : new IntApfloatImpl(0, Apfloat.INFINITE, 10));
        try
        {
            a.multiply(b);
            fail("No implementation mismatch");
        }
        catch (ImplementationMismatchException ime)
        {
            // OK: wrong implementation class
            assertEquals("Localization key", "type.mismatch", ime.getLocalizationKey());
        }
    }

    public static void testIsShort()
    {
        ApfloatImpl impl = new RawtypeApfloatImpl(5, 2, 10);
        assertTrue("5", impl.isShort());

        impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertTrue("0", impl.isShort());

        impl = new RawtypeApfloatImpl("1111111111111111111111111111111111111111111111111111111111111111111111", Apfloat.DEFAULT, 10, false);
        assertTrue("1...e70", !impl.isShort());
    }

    public static void testDivideShort()
    {
        ApfloatImpl a, b;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " 1 / 1", "1", a.divideShort(b).toString(true));

            a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " 1 / -1", "-1", a.divideShort(b).toString(true));

            a = new RawtypeApfloatImpl("11", 2, radix, true);
            b = new RawtypeApfloatImpl("11", 2, radix, true);
            assertEquals("radix " + radix + " 11 / 11", "1", a.divideShort(b).toString(true));

            a = new RawtypeApfloatImpl(67, 1, radix);
            b = new RawtypeApfloatImpl(37, 1, radix);
            assertEquals("radix " + radix + " 67 / 37", "1", a.divideShort(b).toString(true));

            if (radix < 15)
            {
                a = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " min / max", "0", a.divideShort(b).toString(true));

                a = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE / 2), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE / 2), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " min / max just", "0", a.divideShort(b).toString(false));

                a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, radix, false);
                try
                {
                    a.divideShort(b);
                    fail("radix " + radix + " no overflow");
                }
                catch (OverflowException oe)
                {
                    // OK: overflow
                    assertEquals("Localization key", "overflow", oe.getLocalizationKey());
                }
            }

            char nine = Character.forDigit(radix - 1, radix);

            for (int i = 10000; i <= 10005; i++)
            {
                a = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                b = new RawtypeApfloatImpl(getString(nine, 1), i, radix, false);
                assertEquals("radix " + radix + " 9(" + i + ") / 9, prec", getString('1', i), a.divideShort(b).toString(true));
            }

            a = new RawtypeApfloatImpl(getString(nine, 10) + '.' + nine, Apfloat.INFINITE, radix, false);
            b = new RawtypeApfloatImpl(1, 11, radix);
            assertEquals("radix " + radix + " 9999999999.9 / 1, prec", getString(nine, 10) + '.' + nine, a.divideShort(b).toString(true));
            assertEquals("radix " + radix + " 9999999999.9 / 1 precision", 11, a.divideShort(b).precision());
        }

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1", 16, 10, false);
        assertEquals("1.234567890123456 / 1", "1.234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10", 16, 10, false);
        assertEquals("1.234567890123456 / 10", "0.1234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100", 16, 10, false);
        assertEquals("1.234567890123456 / 100", "0.01234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000", 16, 10, false);
        assertEquals("1.234567890123456 / 1000", "0.001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000", 16, 10, false);
        assertEquals("1.234567890123456 / 10000", "0.0001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000", 16, 10, false);
        assertEquals("1.234567890123456 / 100000", "0.00001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000000", 16, 10, false);
        assertEquals("1.234567890123456 / 1000000", "0.000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000000", 16, 10, false);
        assertEquals("1.234567890123456 / 10000000", "0.0000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000000", 16, 10, false);
        assertEquals("1.234567890123456 / 100000000", "0.00000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000000000", 16, 10, false);
        assertEquals("1.234567890123456 / 1000000000", "0.000000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000000000", 16, 10, false);
        assertEquals("1.234567890123456 / 10000000000", "0.0000000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000000000", 16, 10, false);
        assertEquals("1.234567890123456 / 100000000000", "0.00000000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000000000000", 16, 10, false);
        assertEquals("1.234567890123456 / 1000000000000", "0.000000000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("10000000000000", 16, 10, false);
        assertEquals("1.234567890123456 / 10000000000000", "0.0000000000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("100000000000000", 16, 10, false);
        assertEquals("1.234567890123456 / 100000000000000", "0.00000000000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1000000000000000", 16, 10, false);
        assertEquals("1.234567890123456 / 1000000000000000", "0.000000000000001234567890123456", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(2, 50, 10);
        b = new RawtypeApfloatImpl(3, 50, 10);
        assertEquals("2 / 3", "0.66666666666666666666666666666666666666666666666666", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(3, 50, 20);
        b = new RawtypeApfloatImpl(6, 50, 20);
        assertTrue("3 / 6 isShort", a.divideShort(b).isShort());
        assertEquals("3 / 6 String", "0.a", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(6, 50, 7);
        b = new RawtypeApfloatImpl(3, 50, 7);
        assertTrue("6 / 3 isShort", a.divideShort(b).isShort());
        assertEquals("6 / 3", "2", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(5, Apfloat.INFINITE, 10);
        assertEquals("2 / 5", "0.4", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(5, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        assertEquals("5 / 2", "2.5", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(8388608, Apfloat.INFINITE, 10);
        assertEquals("1 / 8388608", "0.00000011920928955078125", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(9765625, Apfloat.INFINITE, 10);
        assertEquals("1 / 9765625", "0.0000001024", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(10000000, Apfloat.INFINITE, 10);
        assertEquals("1 / 10000000", "0.0000001", a.divideShort(b).toString(true));

        // Underflow
        a = new RawtypeApfloatImpl("1e-9000000000000000000", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("2e9000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("1e-9000000000000000000 / 2e9000000000000000000", "0", a.divideShort(b).toString(true));

        a = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(3, Apfloat.INFINITE, 10);
        try
        {
            a.divideShort(b);
            fail("Infinite expansion allowed");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK: would be an infinite expansion
            assertEquals("Localization key", "divide.infinitePrecision", iee.getLocalizationKey());
        }

        a = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 3, false);
        try
        {
            a.divideShort(b);
            fail("No radix mismatch");
        }
        catch (RadixMismatchException rme)
        {
            // OK: radix mismatch
            assertEquals("Localization key", "radix.mismatch", rme.getLocalizationKey());
        }

        b = (RawType.TYPE.equals(Integer.TYPE) ? new LongApfloatImpl(0, Apfloat.INFINITE, 10) : new IntApfloatImpl(0, Apfloat.INFINITE, 10));
        try
        {
            a.divideShort(b);
            fail("No implementation mismatch");
        }
        catch (ImplementationMismatchException ime)
        {
            // OK: wrong implementation class
            assertEquals("Localization key", "type.mismatch", ime.getLocalizationKey());
        }
    }

    public static void testModShort()
    {
        ApfloatImpl a, b;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " 1 % 1", "0", a.modShort(b).toString(true));

            a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " 1 % -1", "0", a.modShort(b).toString(true));

            a = new RawtypeApfloatImpl("11", 2, radix, true);
            b = new RawtypeApfloatImpl("11", 2, radix, true);
            assertEquals("radix " + radix + " 11 % 11", "0", a.modShort(b).toString(true));

            a = new RawtypeApfloatImpl(67, 1, radix);
            b = new RawtypeApfloatImpl(33, 1, radix);
            assertEquals("radix " + radix + " 67 % 33", "1", a.modShort(b).toString(true));

            if (radix < 15)
            {
                a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE - 400), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " max % min", "0", a.modShort(b).toString(true));

                a = new RawtypeApfloatImpl("1e" + (Long.MAX_VALUE / 2), Apfloat.INFINITE, radix, false);
                b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE / 2), Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " max % min just", "0", a.modShort(b).toString(false));
            }

            char nine = Character.forDigit(radix - 1, radix);

            for (int i = 10000; i <= 10005; i++)
            {
                a = new RawtypeApfloatImpl(getString(nine, i), i, radix, false);
                b = new RawtypeApfloatImpl(getString(nine, 1), i, radix, false);
                assertEquals("radix " + radix + " 9(" + i + ") % 9, prec", "0", a.modShort(b).toString(true));

                a = new RawtypeApfloatImpl('1' + getString('0', i - 1), i, radix, false);
                b = new RawtypeApfloatImpl(getString(nine, 1), i, radix, false);
                assertEquals("radix " + radix + " 10(" + i + ") % 9, prec", radix == 2 ? "0" : "1", a.modShort(b).toString(true));

                a = new RawtypeApfloatImpl('1' + getString('0', i - 2) + '1', i, radix, false);
                b = new RawtypeApfloatImpl(getString(nine, 1), i, radix, false);
                assertEquals("radix " + radix + " 10(" + i + ")1 % 9, prec", radix == 2 || radix == 3 ? "0" : "2", a.modShort(b).toString(true));
            }

            a = new RawtypeApfloatImpl(getString(nine, 10) + '.' + nine, Apfloat.INFINITE, radix, false);
            b = new RawtypeApfloatImpl(1, 11, radix);
            assertEquals("radix " + radix + " 9999999999.9 % 1", "0." + nine, a.modShort(b).toString(true));
            assertEquals("radix " + radix + " 9999999999.9 % 1 precision", 10, a.modShort(b).precision());
        }

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("1", 16, 10, false);
        assertEquals("1.234567890123456 % 1", "0.234567890123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.1", 16, 10, false);
        assertEquals("1.234567890123456 % 0.1", "0.034567890123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.01", 16, 10, false);
        assertEquals("1.234567890123456 % 0.01", "0.004567890123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.001", "0.000567890123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.0001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.0001", "0.000067890123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.00001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.00001", "0.000007890123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.000001", "0.000000890123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.0000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.0000001", "0.000000090123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.00000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.00000001", "0.000000000123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.000000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.000000001", "0.000000000123456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.0000000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.0000000001", "0.000000000023456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.00000000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.00000000001", "0.000000000003456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.000000000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.000000000001", "0.000000000000456", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.0000000000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.0000000000001", "0.000000000000056", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.00000000000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.00000000000001", "0.000000000000006", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1.234567890123456", 16, 10, false);
        b = new RawtypeApfloatImpl("0.000000000000001", 16, 10, false);
        assertEquals("1.234567890123456 % 0.000000000000001", "0", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(100000000000007L, 50, 10);
        b = new RawtypeApfloatImpl(2, 50, 10);
        assertEquals("100000000000007 % 2", "1", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(100000000000007L, 50, 10);
        b = new RawtypeApfloatImpl(5, 50, 10);
        assertEquals("100000000000007 % 5", "2", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(1000000000000000007L, 50, 10);
        b = new RawtypeApfloatImpl(2, 50, 10);
        assertEquals("1000000000000000007 % 2", "1", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(1000000000000000007L, 50, 10);
        b = new RawtypeApfloatImpl(5, 50, 10);
        assertEquals("1000000000000000007 % 5", "2", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(1000000000000000007L, 50, 10);
        b = new RawtypeApfloatImpl(4, 50, 10);
        assertEquals("1000000000000000007 % 4", "3", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(1000000000000000007L, 50, 10);
        b = new RawtypeApfloatImpl(1048576, 50, 10);
        assertEquals("1000000000000000007 % 1048576", "262151", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(1000000000000000007L, 50, 10);
        b = new RawtypeApfloatImpl(390625, 50, 10);
        assertEquals("1000000000000000007 % 390625", "7", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("10000000000000000000000000007", 50, 10, false);
        b = new RawtypeApfloatImpl(1048576, 50, 10);
        assertEquals("10000000000000000000000000007 % 1048576", "7", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("10000000000000000000000000007", 50, 10, false);
        b = new RawtypeApfloatImpl(390625, 50, 10);
        assertEquals("10000000000000000000000000007 % 390625", "7", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("98324579823470982347698345782390982374598234752310000000000000000000000000007", 100, 10, false);
        b = new RawtypeApfloatImpl(1048576, 100, 10);
        assertEquals("98324579823470982347698345782390982374598234752310000000000000000000000000007 % 1048576", "7", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("98324579823470982347698345782390982374598234752310000000000000000000000000007", 100, 10, false);
        b = new RawtypeApfloatImpl(390625, 100, 10);
        assertEquals("98324579823470982347698345782390982374598234752310000000000000000000000000007 % 390625", "7", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("98324579823470982347698345782390982374598234752310000000000000000000000000007.1", 100, 10, false);
        b = new RawtypeApfloatImpl(1048576, 100, 10);
        assertEquals("98324579823470982347698345782390982374598234752310000000000000000000000000007.1 % 1048576", "7.1", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("98324579823470982347698345782390982374598234752310000000000000000000000000007.1234567890123456789", 100, 10, false);
        b = new RawtypeApfloatImpl(390625, 100, 10);
        assertEquals("98324579823470982347698345782390982374598234752310000000000000000000000000007.1234567890123456789 % 390625", "7.1234567890123456789", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("365615844006297600000000000000000000", Apfloat.INFINITE, 10, true);
        b = new RawtypeApfloatImpl(63, Apfloat.INFINITE, 10);
        assertEquals("365615844006297600000000000000000000 % 63", "9", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl(6, 50, 20);
        b = new RawtypeApfloatImpl(3, 50, 20);
        assertEquals("6 % 3 radix 20", "0", a.modShort(b).toString(true));
        assertEquals("6 % 3 radix 20 radix", 20, a.modShort(b).radix());

        a = new RawtypeApfloatImpl(6, 50, 7);
        b = new RawtypeApfloatImpl(3, 50, 7);
        assertEquals("6 % 3 radix 7", "0", a.modShort(b).toString(true));
        assertEquals("6 % 3 radix 7 radix", 7, a.modShort(b).radix());

        a = new RawtypeApfloatImpl(9223372036854775807L, 50, 34);
        b = new RawtypeApfloatImpl(8388608, 50, 34);
        if (b.isShort())
        {
            assertEquals("9223372036854775807 % 8388608 radix 34", "69ejp", a.modShort(b).toString(true));
        }

        a = new RawtypeApfloatImpl(5, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        assertEquals("5 % 2", "1", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1e1000000000000000", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl(8388608, Apfloat.INFINITE, 10);
        assertEquals("1e1000000000000000 % 8388608", "0", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1e1000000000000000", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl(9765625, Apfloat.INFINITE, 10);
        assertEquals("1e1000000000000000 % 9765625", "0", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("100000000000000000000", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl(8388608, Apfloat.INFINITE, 10);
        assertEquals("100000000000000000000 % 8388608", "1048576", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1000000000", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl(9765625, Apfloat.INFINITE, 10);
        assertEquals("1000000000 % 9765625", "3906250", a.modShort(b).toString(true));
        assertEquals("1000000000 % 9765625 precision", Apfloat.INFINITE, a.modShort(b).precision());

        a = new RawtypeApfloatImpl("123.456", 6, 10, false);
        b = new RawtypeApfloatImpl("0.1", 10, 10, false);
        assertEquals("123.456 % 0.1", "0.056", a.modShort(b).toString(true));
        assertEquals("123.456 % 0.1 precision", 2, a.modShort(b).precision());

        a = new RawtypeApfloatImpl("123.456", 10, 10, false);
        b = new RawtypeApfloatImpl("0.2", 3, 10, false);
        assertEquals("123.456 % 0.2", "0.056", a.modShort(b).toString(true));
        assertEquals("123.456 % 0.2 precision", 2, a.modShort(b).precision());

        // Underflow
        a = new RawtypeApfloatImpl("1." + getString('0', 1000) +  "1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + 400), Apfloat.INFINITE, 10, false);
        assertEquals("1.000...0001eMIN % 1eMIN", "0", a.modShort(b).toString(true));

        a = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 3, false);
        try
        {
            a.modShort(b);
            fail("No radix mismatch");
        }
        catch (RadixMismatchException rme)
        {
            // OK: radix mismatch
            assertEquals("Localization key", "radix.mismatch", rme.getLocalizationKey());
        }

        b = (RawType.TYPE.equals(Integer.TYPE) ? new LongApfloatImpl(0, Apfloat.INFINITE, 10) : new IntApfloatImpl(0, Apfloat.INFINITE, 10));
        try
        {
            a.modShort(b);
            fail("No implementation mismatch");
        }
        catch (ImplementationMismatchException ime)
        {
            // OK: wrong implementation class
            assertEquals("Localization key", "type.mismatch", ime.getLocalizationKey());
        }
    }

    public static void testAbsFloor()
    {
        ApfloatImpl impl;

        impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertEquals("zero", "0", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl(1, 1, 10);
        assertEquals("one precision", Apfloat.INFINITE, impl.absFloor().precision());
        assertEquals("one", "1", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl(-1, 1, 10);
        assertEquals("minus one precision", Apfloat.INFINITE, impl.absFloor().precision());
        assertEquals("minus one", "-1", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("1.1", Apfloat.DEFAULT, 10, false);
        assertEquals("1.1", "1", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("-1.1", Apfloat.DEFAULT, 10, false);
        assertEquals("-1.1", "-1", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("999999999.999999999999999999", Apfloat.INFINITE, 10, false);
        assertEquals("999999999.999999999999999999", "999999999", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("-999999999.999999999999999999", Apfloat.INFINITE, 10, false);
        assertEquals("-999999999.999999999999999999", "-999999999", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false);
        assertEquals("1000000000.0000000001", "1000000000", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("-1000000000.0000000001", Apfloat.INFINITE, 10, false);
        assertEquals("-1000000000.0000000001", "-1000000000", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("1e70", "10000000000000000000000000000000000000000000000000000000000000000000000", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("-10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("-1e70", "-10000000000000000000000000000000000000000000000000000000000000000000000", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("0.1", Apfloat.INFINITE, 10, false);
        assertEquals("0.1", "0", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("-0.1", Apfloat.INFINITE, 10, false);
        assertEquals("-0.1", "0", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("1e-50", Apfloat.INFINITE, 10, false);
        assertEquals("1e-50", "0", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("-1e-50", Apfloat.INFINITE, 10, false);
        assertEquals("-1e-50", "0", impl.absFloor().toString(true));

        impl = new RawtypeApfloatImpl("200000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("200000000000000 infinite", "200000000000000", impl.absFloor().toString(true));
        assertEquals("200000000000000 infinite, prec", Apfloat.INFINITE, impl.absFloor().precision());

        impl = new RawtypeApfloatImpl("200000000000000", 50, 10, false);
        assertEquals("200000000000000 50", "200000000000000", impl.absFloor().toString(true));
        assertEquals("200000000000000 50, prec", Apfloat.INFINITE, impl.absFloor().precision());

        impl = new RawtypeApfloatImpl("200000000000000.1", Apfloat.INFINITE, 10, false).precision(15);
        assertEquals("200000000000000.1 15", "200000000000000", impl.absFloor().toString(true));
        assertEquals("200000000000000.1 15, prec", Apfloat.INFINITE, impl.absFloor().precision());
    }

    public static void testAbsCeil()
    {
        ApfloatImpl impl;

        impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertEquals("zero", "0", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl(1, 1, 10);
        assertEquals("one precision", Apfloat.INFINITE, impl.absCeil().precision());
        assertEquals("one", "1", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl(-1, 1, 10);
        assertEquals("minus one precision", Apfloat.INFINITE, impl.absCeil().precision());
        assertEquals("minus one", "-1", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("1.1", Apfloat.DEFAULT, 10, false);
        assertEquals("1.1", "2", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("-1.1", Apfloat.DEFAULT, 10, false);
        assertEquals("-1.1", "-2", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("999999999.999999999999999999", Apfloat.INFINITE, 10, false);
        assertEquals("999999999.999999999999999999", "1000000000", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("-999999999.999999999999999999", Apfloat.INFINITE, 10, false);
        assertEquals("-999999999.999999999999999999", "-1000000000", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false);
        assertEquals("1000000000.0000000001", "1000000001", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("-1000000000.0000000001", Apfloat.INFINITE, 10, false);
        assertEquals("-1000000000.0000000001", "-1000000001", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("1e70", "10000000000000000000000000000000000000000000000000000000000000000000000", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("-10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("-1e70", "-10000000000000000000000000000000000000000000000000000000000000000000000", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("0.1", Apfloat.INFINITE, 10, false);
        assertEquals("0.1", "1", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("-0.1", Apfloat.INFINITE, 10, false);
        assertEquals("-0.1", "-1", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("1e-50", Apfloat.INFINITE, 10, false);
        assertEquals("1e-50", "1", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("-1e-50", Apfloat.INFINITE, 10, false);
        assertEquals("-1e-50", "-1", impl.absCeil().toString(true));

        impl = new RawtypeApfloatImpl("200000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("200000000000000 infinite", "200000000000000", impl.absCeil().toString(true));
        assertEquals("200000000000000 infinite, prec", Apfloat.INFINITE, impl.absCeil().precision());

        impl = new RawtypeApfloatImpl("200000000000000", 50, 10, false);
        assertEquals("200000000000000 50", "200000000000000", impl.absCeil().toString(true));
        assertEquals("200000000000000 50, prec", Apfloat.INFINITE, impl.absCeil().precision());

        impl = new RawtypeApfloatImpl("200000000000000.1", Apfloat.INFINITE, 10, false).precision(15);
        assertEquals("200000000000000.1 15", "200000000000000", impl.absCeil().toString(true));
        assertEquals("200000000000000.1 15, prec", Apfloat.INFINITE, impl.absCeil().precision());
    }

    public static void testFrac()
    {
        ApfloatImpl impl;

        impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertEquals("zero", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl(1, 1, 10);
        assertEquals("one", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl(-1, 1, 10);
        assertEquals("minus one", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1.1", Apfloat.DEFAULT, 10, false);
        assertEquals("1.1 precision", 1, impl.frac().precision());
        assertEquals("1.1", "0.1", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("-1.1", Apfloat.DEFAULT, 10, false);
        assertEquals("-1.1 precision", 1, impl.frac().precision());
        assertEquals("-1.1", "-0.1", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("999999999.999999999999999999", Apfloat.INFINITE, 10, false);
        assertEquals("999999999.999999999999999999 precision", Apfloat.INFINITE, impl.frac().precision());
        assertEquals("999999999.999999999999999999", "0.999999999999999999", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("-999999999.999999999999999999", Apfloat.INFINITE, 10, false);
        assertEquals("-999999999.999999999999999999 precision", Apfloat.INFINITE, impl.frac().precision());
        assertEquals("-999999999.999999999999999999", "-0.999999999999999999", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.DEFAULT, 10, false);
        assertEquals("1000000000.0000000001 precision", 1, impl.frac().precision());
        assertEquals("1000000000.0000000001", "0.0000000001", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("-1000000000.0000000001", Apfloat.DEFAULT, 10, false);
        assertEquals("-1000000000.0000000001 precision", 1, impl.frac().precision());
        assertEquals("-1000000000.0000000001", "-0.0000000001", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("1e70", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("-10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, 10, false);
        assertEquals("-1e70", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("0.1", Apfloat.INFINITE, 10, false);
        assertEquals("0.1", "0.1", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("-0.1", Apfloat.INFINITE, 10, false);
        assertEquals("-0.1", "-0.1", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1e-50", Apfloat.INFINITE, 10, false);
        assertEquals("1e-50", "1e-50", impl.frac().toString(false));

        impl = new RawtypeApfloatImpl("-1e-50", Apfloat.INFINITE, 10, false);
        assertEquals("-1e-50", "-1e-50", impl.frac().toString(false));

        impl = new RawtypeApfloatImpl("100000000000000.0000000001", Apfloat.INFINITE, 10, false).precision(5);
        assertEquals("100000000000000.0000000001 5", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("100000000000000.0000000001", Apfloat.INFINITE, 10, false).precision(25);
        assertEquals("100000000000000.0000000001 25", "0.0000000001", impl.frac().toString(true));
        assertEquals("100000000000000.0000000001 25, prec", 1, impl.frac().precision());

        impl = new RawtypeApfloatImpl("100000000000000.0000000001", Apfloat.INFINITE, 10, false).precision(24);
        assertEquals("100000000000000.0000000001 24", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("100000000000000.000000001", Apfloat.INFINITE, 10, false).precision(24);
        assertEquals("100000000000000.000000001 24", "0.000000001", impl.frac().toString(true));
        assertEquals("100000000000000.000000001 24, prec", 1, impl.frac().precision());

        impl = new RawtypeApfloatImpl("100000000000000.000000001", Apfloat.INFINITE, 10, false).precision(23);
        assertEquals("100000000000000.000000001 23", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("10000000000000.000000001", Apfloat.INFINITE, 10, false).precision(23);
        assertEquals("10000000000000.000000001 23", "0.000000001", impl.frac().toString(true));
        assertEquals("10000000000000.000000001 23, prec", 1, impl.frac().precision());

        impl = new RawtypeApfloatImpl("10000000000000.000000001", Apfloat.INFINITE, 10, false).precision(22);
        assertEquals("10000000000000.000000001 22", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false).precision(1);
        assertEquals("1000000000.0000000001 1", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false).precision(9);
        assertEquals("1000000000.0000000001 9", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false).precision(10);
        assertEquals("1000000000.0000000001 10", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false).precision(11);
        assertEquals("1000000000.0000000001 11", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false).precision(19);
        assertEquals("1000000000.0000000001 19", "0", impl.frac().toString(true));

        impl = new RawtypeApfloatImpl("1000000000.0000000001", Apfloat.INFINITE, 10, false).precision(20);
        assertEquals("1000000000.0000000001 20", "0.0000000001", impl.frac().toString(true));
        assertEquals("1000000000.0000000001 20, prec", 1, impl.frac().precision());
    }

    public static void testSize()
    {
        ApfloatImpl impl = new RawtypeApfloatImpl(1010000, Apfloat.INFINITE, 10);
        assertEquals("1010000", 3, impl.size());

        impl = new RawtypeApfloatImpl("10000000000000000000000000000000000000001", Apfloat.INFINITE, 10, true);
        assertEquals("10000000000000000000000000000000000000001", 41, impl.size());

        impl = impl.precision(40);
        assertEquals("10000000000000000000000000000000000000001 prec 40", 1, impl.size());
    }

    public static void testNegate()
    {
        ApfloatImpl impl = new RawtypeApfloatImpl(5, Apfloat.INFINITE, 10);
        assertEquals("-5", "-5", impl.negate().toString(true));

        impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertEquals("0", "0", impl.negate().toString(true));
    }

    public static void testDoubleValue()
    {
        ApfloatImpl impl;
        double e1 = 1.0 - 2.0 * Math.ulp(1.0); // 1 minus two epsilons

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " zero", 0.0, impl.doubleValue(), 0.0);

            impl = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " one", 1.0, impl.doubleValue(), 2e-15);

            impl = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " minus one", -1.0, impl.doubleValue(), 2e-15);

            impl = new RawtypeApfloatImpl(e1 * Double.MAX_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " max", Double.MAX_VALUE, impl.doubleValue(), 2e293);

            impl = new RawtypeApfloatImpl(-e1 * Double.MAX_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " -max", -Double.MAX_VALUE, impl.doubleValue(), 2e293);

            impl = new RawtypeApfloatImpl(Double.MIN_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " min", Double.MIN_VALUE, impl.doubleValue());

            impl = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1.1", 1.0 + 1.0 / radix, impl.doubleValue(), 2e-15);

            impl = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " -1.1", -1.0 - 1.0 / radix, impl.doubleValue(), 2e-15);

            if (radix < 15)
            {
                impl = new RawtypeApfloatImpl("1e10000", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " 1e10000", Double.POSITIVE_INFINITY, impl.doubleValue(), 0.0);

                impl = new RawtypeApfloatImpl("-1e10000", Apfloat.INFINITE, radix, false);
                assertEquals("radix " + radix + " -1e10000", Double.NEGATIVE_INFINITY, impl.doubleValue(), 0.0);
            }
        }

        impl = new RawtypeApfloatImpl("1.797693134862317E308", Apfloat.INFINITE, 10, false);
        assertEquals("max+1", Double.POSITIVE_INFINITY, impl.doubleValue(), 0.0);

        impl = new RawtypeApfloatImpl("-1.797693134862317E308", Apfloat.INFINITE, 10, false);
        assertEquals("-max+1", Double.NEGATIVE_INFINITY, impl.doubleValue(), 0.0);

        impl = new RawtypeApfloatImpl("2.4E-324", Apfloat.INFINITE, 10, false);
        assertEquals("min-1", 0.0, impl.doubleValue(), 0.0);
    }

    public static void testLongValue()
    {
        ApfloatImpl impl;

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            impl = new RawtypeApfloatImpl(0, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " zero", 0, impl.longValue());

            impl = new RawtypeApfloatImpl(1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " one", 1, impl.longValue());

            impl = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " minus one", -1, impl.longValue());

            impl = new RawtypeApfloatImpl(Long.MAX_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " max", Long.MAX_VALUE, impl.longValue());

            impl = new RawtypeApfloatImpl(Long.MIN_VALUE, Apfloat.INFINITE, radix);
            assertEquals("radix " + radix + " min", Long.MIN_VALUE, impl.longValue());

            impl = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1.1", 1, impl.longValue());

            impl = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " -1.1", -1, impl.longValue());

            impl = new RawtypeApfloatImpl("10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1e70", Long.MAX_VALUE, impl.longValue());

            impl = new RawtypeApfloatImpl("-10000000000000000000000000000000000000000000000000000000000000000000000", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " -1e70", Long.MIN_VALUE, impl.longValue());

            impl = new RawtypeApfloatImpl("11111111111111111111111111111111111111111111111111111111111111111111111", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " 1x70", Long.MAX_VALUE, impl.longValue());

            impl = new RawtypeApfloatImpl("-11111111111111111111111111111111111111111111111111111111111111111111111", Apfloat.INFINITE, radix, false);
            assertEquals("radix " + radix + " -1x70", Long.MIN_VALUE, impl.longValue());
        }

        impl = new RawtypeApfloatImpl("9223372036854775808", Apfloat.INFINITE, 10, true);
        assertEquals("max+1", Long.MAX_VALUE, impl.longValue());

        impl = new RawtypeApfloatImpl("-9223372036854775809", Apfloat.INFINITE, 10, true);
        assertEquals("min-1", Long.MIN_VALUE, impl.longValue());

        impl = new RawtypeApfloatImpl("-9223372036854775810", Apfloat.INFINITE, 10, true);
        assertEquals("min-2", Long.MIN_VALUE, impl.longValue());
    }

    public static void testEqualDigits()
    {
        ApfloatImpl a, b;

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertEquals("0 eqd 0", Apfloat.INFINITE, a.equalDigits(b));

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("0 eqd 1", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("-1 eqd 1", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        assertEquals("1 eqd 10", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("10 eqd 1", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(100, Apfloat.INFINITE, 10);
        assertEquals("1 eqd 100", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(100, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("100 eqd 1", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        assertEquals("1 eqd 2", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("2 eqd 1", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        assertEquals("-1 eqd -2", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        assertEquals("-2 eqd -1", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("1 eqd 1", Apfloat.INFINITE, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1, 25, 10);
        b = new RawtypeApfloatImpl(1, 55, 10);
        assertEquals("1 eqd 1, prec", 25, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10, 1, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertEquals("1(0) eqd 1(1)", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 1, 10);
        assertEquals("1(1) eqd 1(0)", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10, 2, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertEquals("10 eqd 1(1)", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 2, 10);
        assertEquals("1(1) eqd 10", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        assertEquals("1 eqd 1.1", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        assertEquals("1.1 eqd 1", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        assertEquals("-1 eqd -1.1", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        assertEquals("-1.1 eqd -1", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl(123456789, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(123456788, Apfloat.INFINITE, 10);
        assertEquals("123456789 eqd 123456788", 8, a.equalDigits(b));

        a = new RawtypeApfloatImpl(123456788, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(123456789, Apfloat.INFINITE, 10);
        assertEquals("123456788 eqd 123456789", 8, a.equalDigits(b));

        a = new RawtypeApfloatImpl(123456789, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(223456789, Apfloat.INFINITE, 10);
        assertEquals("123456789 eqd 223456789", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(223456789, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(123456789, Apfloat.INFINITE, 10);
        assertEquals("223456789 eqd 123456789", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1234567890L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1234567891L, Apfloat.INFINITE, 10);
        assertEquals("1234567890 eqd 1234567891", 9, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1234567891L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1234567890L, Apfloat.INFINITE, 10);
        assertEquals("1234567891 eqd 1234567890", 9, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1234567890L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(2234567890L, Apfloat.INFINITE, 10);
        assertEquals("1234567890 eqd 2234567890", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(2234567890L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1234567890L, Apfloat.INFINITE, 10);
        assertEquals("2234567890 eqd 1234567890", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1234567890L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1334567890L, Apfloat.INFINITE, 10);
        assertEquals("1234567890 eqd 1334567890", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl(1334567890L, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1234567890L, Apfloat.INFINITE, 10);
        assertEquals("1334567890 eqd 1234567890", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10000000000L, 10, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertEquals("1000000000(0) eqd 1000000000(1)", 10, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 10, 10);
        assertEquals("1000000000(1) eqd 1000000000(0)", 10, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10000000000L, 11, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertEquals("10000000000 eqd 1000000000(1)", 10, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 11, 10);
        assertEquals("1000000000(1) eqd 10000000000", 10, a.equalDigits(b));

        a = new RawtypeApfloatImpl("1000000000000000000", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("999999999999999999", Apfloat.DEFAULT, 10, false);
        assertEquals("1000000000000000000 eqd 999999999999999999", 17, a.equalDigits(b));

        a = new RawtypeApfloatImpl("999999999999999999", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("1000000000000000000", Apfloat.DEFAULT, 10, false);
        assertEquals("999999999999999999 eqd 1000000000000000000", 17, a.equalDigits(b));

        a = new RawtypeApfloatImpl("1000000007000000000", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("999999999999999999", Apfloat.DEFAULT, 10, false);
        assertEquals("1000000007000000000 eqd 999999999999999999", 8, a.equalDigits(b));

        a = new RawtypeApfloatImpl("999999999999999999", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("1000000007000000000", Apfloat.DEFAULT, 10, false);
        assertEquals("999999999999999999 eqd 1000000007000000000", 8, a.equalDigits(b));

        a = new RawtypeApfloatImpl("1000000000", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("900000000", Apfloat.DEFAULT, 10, false);
        assertEquals("1000000000 eqd 900000000", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl("900000000", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("1000000000", Apfloat.DEFAULT, 10, false);
        assertEquals("900000000 eqd 1000000000", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl("1000000000", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("990000000", Apfloat.DEFAULT, 10, false);
        assertEquals("1000000000 eqd 990000000", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl("990000000", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("1000000000", Apfloat.DEFAULT, 10, false);
        assertEquals("990000000 eqd 1000000000", 1, a.equalDigits(b));

        a = new RawtypeApfloatImpl(10, 5, 10);
        b = new RawtypeApfloatImpl(9, 5, 10);
        assertEquals("10 eqd 9", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl(9, 5, 10);
        b = new RawtypeApfloatImpl(10, 5, 10);
        assertEquals("9 eqd 10", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl("123456789000100000000000000000000000", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("123456789000099999999999999999999425", Apfloat.DEFAULT, 10, false);
        assertEquals("123456789000100000000000000000000000 eqd 123456789000099999999999999999999425", 33, a.equalDigits(b));

        a = new RawtypeApfloatImpl("123456789000099999999999999999999425", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("123456789000100000000000000000000000", Apfloat.DEFAULT, 10, false);
        assertEquals("123456789000099999999999999999999425 eqd 123456789000100000000000000000000000", 33, a.equalDigits(b));

        a = new RawtypeApfloatImpl("12000100000123456789", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("12000099999234567890", Apfloat.DEFAULT, 10, false);
        assertEquals("12000100000123456789 eqd 12000099999234567890", 11, a.equalDigits(b));

        a = new RawtypeApfloatImpl("12000099999234567890", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("12000100000123456789", Apfloat.DEFAULT, 10, false);
        assertEquals("12000099999234567890 eqd 12000100000123456789", 11, a.equalDigits(b));

        a = new RawtypeApfloatImpl("12000100000234567890", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("12000099999123456789", Apfloat.DEFAULT, 10, false);
        assertEquals("12000100000234567890 eqd 12000099999123456789", 10, a.equalDigits(b));

        a = new RawtypeApfloatImpl("12000099999123456789", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("12000100000234567890", Apfloat.DEFAULT, 10, false);
        assertEquals("12000099999123456789 eqd 12000100000234567890", 10, a.equalDigits(b));

        a = new RawtypeApfloatImpl("0.15", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("0.04", Apfloat.DEFAULT, 10, false);
        assertEquals("0.15 eq 0.04", 0, a.equalDigits(b));

        a = new RawtypeApfloatImpl("2.0", Apfloat.DEFAULT, 10, false);
        b = new RawtypeApfloatImpl("0.9", Apfloat.DEFAULT, 10, false);
        assertEquals("2.0 eq 0.9", 0, a.equalDigits(b));
        assertEquals("0.9 eq 2.0", 0, b.equalDigits(a));

        a = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 3, false);
        try
        {
            a.equalDigits(b);
            fail("No radix mismatch");
        }
        catch (RadixMismatchException rme)
        {
            // OK: radix mismatch
            assertEquals("Localization key", "radix.mismatch", rme.getLocalizationKey());
        }

        b = (RawType.TYPE.equals(Integer.TYPE) ? new LongApfloatImpl(0, Apfloat.INFINITE, 10) : new IntApfloatImpl(0, Apfloat.INFINITE, 10));
        try
        {
            a.equalDigits(b);
            fail("No implementation mismatch");
        }
        catch (ImplementationMismatchException ime)
        {
            // OK: wrong implementation class
            assertEquals("Localization key", "type.mismatch", ime.getLocalizationKey());
        }
    }

    public static void testCompareTo()
    {
        ApfloatImpl a, b;

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertEquals("0 == 0", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("0 < 1", -1, a.compareTo(b));

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("-1 < 1", -1, a.compareTo(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        assertEquals("1 > -1", 1, a.compareTo(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        assertEquals("1 < 10", -1, a.compareTo(b));

        a = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("10 > 1", 1, a.compareTo(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        assertEquals("1 < 2", -1, a.compareTo(b));

        a = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("2 > 1", 1, a.compareTo(b));

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        assertEquals("-1 > -2", 1, a.compareTo(b));

        a = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        assertEquals("-2 < -1", -1, a.compareTo(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertEquals("1 == 1", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(1, 25, 10);
        b = new RawtypeApfloatImpl(1, 55, 10);
        assertEquals("1 == 1, prec", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(10, 1, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertEquals("1(0) == 1(1)", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 1, 10);
        assertEquals("1(1) == 1(0)", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(10, 2, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertEquals("10 == 1(1)", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 2, 10);
        assertEquals("1(1) == 10", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        assertEquals("1 < 1.1", -1, a.compareTo(b));

        a = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        assertEquals("1.1 > 1", 1, a.compareTo(b));

        a = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        assertEquals("-1 > -1.1", 1, a.compareTo(b));

        a = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        assertEquals("-1.1 < -1", -1, a.compareTo(b));

        a = new RawtypeApfloatImpl(10000000000L, 10, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertEquals("1000000000(0) == 1000000000(1)", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 10, 10);
        assertEquals("1000000000(1) == 1000000000(0)", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(10000000000L, 11, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertEquals("10000000000 == 1000000000(1)", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 11, 10);
        assertEquals("1000000000(1) == 10000000000", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl(10000000001L, 11, 10);
        b = new RawtypeApfloatImpl(10000000001L, 11, 10);
        assertEquals("10000000001 == 10000000001", 0, a.compareTo(b));

        a = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 2, false);
        b = new RawtypeApfloatImpl("1e5", Apfloat.INFINITE, 3, false);
        try
        {
            a.compareTo(b);
            fail("No radix mismatch");
        }
        catch (RadixMismatchException rme)
        {
            // OK: radix mismatch
            assertEquals("Localization key", "radix.mismatch", rme.getLocalizationKey());
        }

        b = (RawType.TYPE.equals(Integer.TYPE) ? new LongApfloatImpl(0, Apfloat.INFINITE, 10) : new IntApfloatImpl(0, Apfloat.INFINITE, 10));
        try
        {
            a.compareTo(b);
            fail("No implementation mismatch");
        }
        catch (ImplementationMismatchException ime)
        {
            // OK: wrong implementation class
            assertEquals("Localization key", "type.mismatch", ime.getLocalizationKey());
        }
    }

    public static void testEquals()
    {
        ApfloatImpl a, b;

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertTrue("0 == 0", a.equals(b));

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("0 =! 1", !a.equals(b));

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("-1 != 1", !a.equals(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        assertTrue("1 != 10", !a.equals(b));

        a = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("10 != 1", !a.equals(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        assertTrue("1 != 2", !a.equals(b));

        a = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("2 != 1", !a.equals(b));

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        assertTrue("-1 != -2", !a.equals(b));

        a = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        assertTrue("-2 != -1", !a.equals(b));

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("1 == 1", a.equals(b));

        a = new RawtypeApfloatImpl(1, 25, 10);
        b = new RawtypeApfloatImpl(1, 55, 10);
        assertTrue("1 == 1, prec", a.equals(b));

        a = new RawtypeApfloatImpl(10, 1, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertTrue("1(0) == 1(1)", a.equals(b));

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 1, 10);
        assertTrue("1(1) == 1(0)", a.equals(b));

        a = new RawtypeApfloatImpl(10, 2, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertTrue("10 == 1(1)", a.equals(b));

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 2, 10);
        assertTrue("1(1) == 10", a.equals(b));

        a = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        assertTrue("1 != 1.1", !a.equals(b));

        a = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        assertTrue("1.1 != 1", !a.equals(b));

        a = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        assertTrue("-1 != -1.1", !a.equals(b));

        a = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        assertTrue("-1.1 != -1", !a.equals(b));

        a = new RawtypeApfloatImpl(10000000000L, 10, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertTrue("1000000000(0) == 1000000000(1)", a.equals(b));

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 10, 10);
        assertTrue("1000000000(1) == 1000000000(0)", a.equals(b));

        a = new RawtypeApfloatImpl(10000000000L, 11, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertTrue("10000000000 == 1000000000(1)", a.equals(b));

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 11, 10);
        assertTrue("1000000000(1) == 10000000000", a.equals(b));

        assertFalse("wrong type", a.equals("Bogus"));

        a = new RawtypeApfloatImpl("2", Apfloat.INFINITE, 3, false);
        b = new RawtypeApfloatImpl("2", Apfloat.INFINITE, 4, false);
        assertFalse("wrong radix", a.equals(b));

        a = new RawtypeApfloatImpl("2", Apfloat.INFINITE, 3, false);
        b = (RawType.TYPE.equals(Integer.TYPE) ? new LongApfloatImpl(0, Apfloat.INFINITE, 10) : new IntApfloatImpl(0, Apfloat.INFINITE, 10));
        assertFalse("wrong implementation", a.equals(b));
    }

    public static void testHashCode()
    {
        ApfloatImpl a, b;

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        assertTrue("0 == 0", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(0, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("0 =! 1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("-1 != 1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        assertTrue("1 != 10", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(10, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("10 != 1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        assertTrue("1 != 2", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("2 != 1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        assertTrue("-1 != -2", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(-2, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(-1, Apfloat.INFINITE, 10);
        assertTrue("-2 != -1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        b = new RawtypeApfloatImpl(1, Apfloat.INFINITE, 10);
        assertTrue("1 == 1", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(1, 25, 10);
        b = new RawtypeApfloatImpl(1, 55, 10);
        assertTrue("1 == 1, prec", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(10, 1, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertTrue("1(0) == 1(1)", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 1, 10);
        assertTrue("1(1) == 1(0)", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(10, 2, 10);
        b = new RawtypeApfloatImpl(11, 1, 10);
        assertTrue("10 == 1(1)", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(11, 1, 10);
        b = new RawtypeApfloatImpl(10, 2, 10);
        assertTrue("1(1) == 10", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        assertTrue("1 != 1.1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl("1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("1", Apfloat.INFINITE, 10, false);
        assertTrue("1.1 != 1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        assertTrue("-1 != -1.1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl("-1.1", Apfloat.INFINITE, 10, false);
        b = new RawtypeApfloatImpl("-1", Apfloat.INFINITE, 10, false);
        assertTrue("-1.1 != -1", a.hashCode() != b.hashCode());

        a = new RawtypeApfloatImpl(10000000000L, 10, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertTrue("1000000000(0) == 1000000000(1)", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 10, 10);
        assertTrue("1000000000(1) == 1000000000(0)", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(10000000000L, 11, 10);
        b = new RawtypeApfloatImpl(10000000001L, 10, 10);
        assertTrue("10000000000 == 1000000000(1)", a.hashCode() == b.hashCode());

        a = new RawtypeApfloatImpl(10000000001L, 10, 10);
        b = new RawtypeApfloatImpl(10000000000L, 11, 10);
        assertTrue("1000000000(1) == 10000000000", a.hashCode() == b.hashCode());
    }

    public static void testToString()
    {
        ApfloatImpl a = new RawtypeApfloatImpl("1234560.01", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1234560.01", a.toString(true));

        a = new RawtypeApfloatImpl("123456000000000.01", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "123456000000000.01", a.toString(true));

        a = new RawtypeApfloatImpl("1234560000000000.01", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1234560000000000.01", a.toString(true));

        a = new RawtypeApfloatImpl("1234560000000001.01", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1234560000000001.01", a.toString(true));

        a = new RawtypeApfloatImpl("1234560000000001.101", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1234560000000001.101", a.toString(true));

        a = new RawtypeApfloatImpl("123456", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1.23456e5", a.toString(false));

        a = new RawtypeApfloatImpl("123456789", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1.23456789e8", a.toString(false));

        a = new RawtypeApfloatImpl("1234567890", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1.23456789e9", a.toString(false));

        a = new RawtypeApfloatImpl("12345.6", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1.23456e4", a.toString(false));

        a = new RawtypeApfloatImpl("1.23456", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1.23456", a.toString(false));

        a = new RawtypeApfloatImpl("123456780", 8, 10, false);
        assertEquals("unpretty", "1.2345678e8", a.toString(false));

        a = new RawtypeApfloatImpl("1234567890", 9, 10, false);
        assertEquals("unpretty", "1.23456789e9", a.toString(false));

        a = new RawtypeApfloatImpl("0.1", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1e-1", a.toString(false));

        a = new RawtypeApfloatImpl("0.000000001", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1e-9", a.toString(false));

        a = new RawtypeApfloatImpl("0.12", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1.2e-1", a.toString(false));

        a = new RawtypeApfloatImpl("0.0000000012", Apfloat.INFINITE, 10, false);
        assertEquals("unpretty", "1.2e-9", a.toString(false));

        for (int radix = Character.MIN_RADIX; radix < 15; radix++)
        {
            for (int i = 1; i < 400; i++)
            {
                a = new RawtypeApfloatImpl("1e" + (Long.MIN_VALUE + i), Apfloat.INFINITE, radix, false);
                a.toString(false);
                try
                {
                    a.toString(true);
                }
                catch (ApfloatRuntimeException are)
                {
                    // OK if number doesn't fit to a string
                }
            }
        }
    }

    public static void testWriteTo()
        throws IOException
    {
        ApfloatImpl a = new RawtypeApfloatImpl("123450", 5, 10, true);
        StringWriter writer = new StringWriter();

        a.writeTo(writer, true);
        assertEquals("pretty", "123450", writer.toString());

        writer = new StringWriter();
        a.writeTo(writer, false);
        assertEquals("unpretty", "1.2345e5", writer.toString());

        writer = new StringWriter();
        a = new RawtypeApfloatImpl("0", 5, 10, true);
        a.writeTo(writer, false);
        assertEquals("zero", "0", writer.toString());
    }

    public static void testSerialization()
        throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        ApfloatImpl a = new RawtypeApfloatImpl(5, Apfloat.INFINITE, 10);
        out.writeObject(a);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        ApfloatImpl b = (ApfloatImpl) in.readObject();
        assertEquals("5 equals", a, b);
        assertTrue("5 !=", a != b);

        a = new RawtypeApfloatImpl(getString('a', 1000000), Apfloat.DEFAULT, 12, false);
        buffer.reset();
        out = new ObjectOutputStream(buffer);
        out.writeObject(a);
        out.close();
        in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        b = (ApfloatImpl) in.readObject();
        assertEquals("1000000 equals", a, b);
        assertTrue("1000000 !=", a != b);
    }
}
