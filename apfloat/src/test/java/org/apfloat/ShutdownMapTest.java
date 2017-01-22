package org.apfloat;

import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.6.2
 * @author Mikko Tommila
 */

public class ShutdownMapTest
    extends TestCase
{
    public ShutdownMapTest(String methodName)
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

        suite.addTest(new ShutdownMapTest("testMap"));

        return suite;
    }

    public static void testMap()
    {
        Map<Integer, Apfloat> map = new ShutdownMap<Integer, Apfloat>();

        try
        {
            map.put(0, Apfloat.ZERO);
            fail("ShutdownMap allowed put()");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK
        }

        try
        {
            map.get(0);
            fail("ShutdownMap allowed get()");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK
        }
    }
}
