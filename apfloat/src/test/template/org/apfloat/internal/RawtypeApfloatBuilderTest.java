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

import java.io.PushbackReader;
import java.io.StringReader;
import java.io.IOException;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeApfloatBuilderTest
    extends RawtypeTestCase
{
    public RawtypeApfloatBuilderTest(String methodName)
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

        suite.addTest(new RawtypeApfloatBuilderTest("testLongCreate"));
        suite.addTest(new RawtypeApfloatBuilderTest("testDoubleCreate"));
        suite.addTest(new RawtypeApfloatBuilderTest("testStringCreate"));
        suite.addTest(new RawtypeApfloatBuilderTest("testStreamCreate"));

        return suite;
    }

    public static void testLongCreate()
    {
        ApfloatImpl impl;
        ApfloatBuilder apfloatBuilder = new RawtypeApfloatBuilder();

        impl = apfloatBuilder.createApfloat(5, Apfloat.INFINITE, 11);
        assertEquals("normal radix", 11, impl.radix());
        assertEquals("normal precision", Apfloat.INFINITE, impl.precision());
        assertEquals("normal String", "5", impl.toString(true));

        impl = apfloatBuilder.createApfloat(6, 7, 11);
        assertEquals("prec radix", 11, impl.radix());
        assertEquals("prec precision", 7, impl.precision());
        assertEquals("prec String", "6", impl.toString(true));

        try
        {
            impl = apfloatBuilder.createApfloat(7, 8, -1);
            fail("Invalid radix accepted");
        }
        catch (RuntimeException re)
        {
            // OK: should not be allowed
        }
    }

    public static void testDoubleCreate()
    {
        ApfloatImpl impl;
        ApfloatBuilder apfloatBuilder = new RawtypeApfloatBuilder();

        impl = apfloatBuilder.createApfloat(5.0, Apfloat.INFINITE, 11);
        assertEquals("normal radix", 11, impl.radix());
        assertEquals("normal precision", Apfloat.INFINITE, impl.precision());
        assertEquals("normal String", "5", impl.toString(true));

        impl = apfloatBuilder.createApfloat(6.0, 7, 11);
        assertEquals("prec radix", 11, impl.radix());
        assertEquals("prec precision", 7, impl.precision());
        assertEquals("prec String", "6", impl.toString(true));

        try
        {
            impl = apfloatBuilder.createApfloat(7.0, 8, -1);
            fail("Invalid radix accepted");
        }
        catch (RuntimeException re)
        {
            // OK: should not be allowed
        }
    }

    public static void testStringCreate()
    {
        ApfloatImpl impl;
        ApfloatBuilder apfloatBuilder = new RawtypeApfloatBuilder();

        impl = apfloatBuilder.createApfloat("5", Apfloat.INFINITE, 11, true);
        assertEquals("normal radix", 11, impl.radix());
        assertEquals("normal precision", Apfloat.INFINITE, impl.precision());
        assertEquals("normal String", "5", impl.toString(true));

        impl = apfloatBuilder.createApfloat("6.5", 7, 11, false);
        assertEquals("prec radix", 11, impl.radix());
        assertEquals("prec precision", 7, impl.precision());
        assertEquals("prec String", "6.5", impl.toString(true));

        try
        {
            impl = apfloatBuilder.createApfloat("7.0", 8, -1, false);
            fail("Invalid radix accepted");
        }
        catch (RuntimeException re)
        {
            // OK: should not be allowed
        }

        try
        {
            impl = apfloatBuilder.createApfloat("7.0", 8, 9, true);
            fail("Invalid string accepted");
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

    public static void testStreamCreate()
        throws IOException
    {
        ApfloatImpl impl;
        ApfloatBuilder apfloatBuilder = new RawtypeApfloatBuilder();

        impl = apfloatBuilder.createApfloat(getReader("5"), Apfloat.INFINITE, 11, true);
        assertEquals("normal radix", 11, impl.radix());
        assertEquals("normal precision", Apfloat.INFINITE, impl.precision());
        assertEquals("normal String", "5", impl.toString(true));

        impl = apfloatBuilder.createApfloat(getReader("6.5"), 7, 11, false);
        assertEquals("prec radix", 11, impl.radix());
        assertEquals("prec precision", 7, impl.precision());
        assertEquals("prec String", "6.5", impl.toString(true));

        try
        {
            impl = apfloatBuilder.createApfloat(getReader("7.0"), 8, -1, false);
            fail("Invalid radix accepted");
        }
        catch (RuntimeException re)
        {
            // OK: should not be allowed
        }
    }
}
