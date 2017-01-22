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
