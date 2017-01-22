package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeMemoryArrayAccessTest
    extends RawtypeTestCase
{
    public RawtypeMemoryArrayAccessTest(String methodName)
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

        suite.addTest(new RawtypeMemoryArrayAccessTest("testGet"));
        suite.addTest(new RawtypeMemoryArrayAccessTest("testSubsequence"));

        return suite;
    }

    public static void testGet()
    {
        rawtype[] data = { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 };
        ArrayAccess arrayAccess = new RawtypeMemoryArrayAccess(data, 0, 4);

        assertTrue("class", arrayAccess.getData() instanceof rawtype[]);
        assertEquals("[0]", 1, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 2, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 1]);
        assertEquals("[2]", 3, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 2]);
        assertEquals("[3]", 4, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 3]);
        assertEquals("length", 4, arrayAccess.getLength());
    }

    public static void testSubsequence()
    {
        rawtype[] data = { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 };
        ArrayAccess arrayAccess = new RawtypeMemoryArrayAccess(data, 0, 4);
        arrayAccess = arrayAccess.subsequence(1, 2);

        assertTrue("class", arrayAccess.getData() instanceof rawtype[]);
        assertEquals("[0]", 2, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset()]);
        assertEquals("[1]", 3, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 1]);
        assertEquals("length", 2, arrayAccess.getLength());
    }
}
