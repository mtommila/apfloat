package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class RawtypeDataStorageBuilderTest
    extends RawtypeTestCase
{
    public RawtypeDataStorageBuilderTest(String methodName)
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

        suite.addTest(new RawtypeDataStorageBuilderTest("testCreate"));

        return suite;
    }

    public static void testCreate()
    {
        DataStorageBuilder dataStorageBuilder = new RawtypeDataStorageBuilder();

        assertTrue("Normal", dataStorageBuilder.createDataStorage(1) instanceof DataStorage);

        DataStorage dataStorage = dataStorageBuilder.createCachedDataStorage(1);
        assertTrue("Cached", dataStorage.isCached());

        ApfloatContext ctx = ApfloatContext.getContext();
        long memoryThreshold = ctx.getMemoryThreshold();
        ctx.setMemoryThreshold(65536);
        dataStorage.setSize(ctx.getMemoryThreshold() + 1);
        dataStorage = dataStorageBuilder.createDataStorage(dataStorage);
        assertFalse("Not cached", dataStorage.isCached());

        dataStorage = dataStorageBuilder.createCachedDataStorage(ctx.getMemoryThreshold() + 1);
        assertTrue("Not cached although might", dataStorage.isCached());

        ctx.setMemoryThreshold(memoryThreshold);
    }
}
