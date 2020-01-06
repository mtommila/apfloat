/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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
