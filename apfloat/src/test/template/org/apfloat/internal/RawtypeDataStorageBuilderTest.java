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

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.15.0
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
        assertFalse("Not cached from another", dataStorage.isCached());

        dataStorage = dataStorageBuilder.createCachedDataStorage(ctx.getMemoryThreshold() + 1);
        assertTrue("Not cached although might", dataStorage.isCached());

        dataStorage = dataStorageBuilder.createDataStorage(ctx.getMemoryThreshold() + 1);
        assertFalse("Not cached", dataStorage.isCached());

        boolean cleanupAtExit = ctx.getCleanupAtExit();
        try
        {
            ctx.setCleanupAtExit(false);
            dataStorageBuilder.createDataStorage(ctx.getMemoryThreshold() + 1);
            fail("Allowed");
        }
        catch (BackingStorageException bse)
        {
            // OK; not allowed
            assertEquals("Localization key", "file.allow", bse.getLocalizationKey());
        }
        finally
        {
            ctx.setCleanupAtExit(cleanupAtExit);
        }

        ctx.setMemoryThreshold(memoryThreshold);
    }
}
