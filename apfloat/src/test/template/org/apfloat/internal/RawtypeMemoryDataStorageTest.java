package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeMemoryDataStorageTest
    extends RawtypeDataStorageTestCase
{
    private RawtypeMemoryDataStorageTest()
    {
    }

    public RawtypeMemoryDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new RawtypeMemoryDataStorageTest().realSuite();

        suite.addTest(new RawtypeDiskDataStorageTest("testIsCached"));

        return suite;
    }

    public TestCase createTestCase(String methodName)
    {
        return new RawtypeMemoryDataStorageTest(methodName);
    }

    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new RawtypeMemoryDataStorage();
    }

    public static void testIsCached()
    {
        assertTrue(new RawtypeDiskDataStorage().isCached());
    }
}
