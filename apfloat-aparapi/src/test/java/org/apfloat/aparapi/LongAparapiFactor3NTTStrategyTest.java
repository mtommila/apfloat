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
package org.apfloat.aparapi;

import java.util.Arrays;

import org.apfloat.*;
import org.apfloat.spi.*;
import org.apfloat.internal.*;

import junit.framework.TestSuite;

/**
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class LongAparapiFactor3NTTStrategyTest
    extends LongNTTStrategyTestCase
{
    public LongAparapiFactor3NTTStrategyTest(String methodName)
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

        suite.addTest(new LongAparapiFactor3NTTStrategyTest("testForward"));
        suite.addTest(new LongAparapiFactor3NTTStrategyTest("testRoundTrip"));
        suite.addTest(new LongAparapiFactor3NTTStrategyTest("testRoundTrip2"));

        return suite;
    }

    public static void testForward()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        ctx.setMemoryThreshold(Long.MAX_VALUE);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            int size = 3 * 2048;
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            long[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            Arrays.sort(expectedTransform);

            NTTStrategy nttStrategy = new LongAparapiFactor3NTTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            long[] actualTransform = getPlainArray(dataStorage);
            Arrays.sort(actualTransform);

            assertEquals("expected length", size, expectedTransform.length);
            assertEquals("actual length", size, actualTransform.length);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], [" + i + "]", (long) expectedTransform[i], (long) actualTransform[i]);
            }
        }
    }

    public static void testRoundTrip()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);
        ctx.setMemoryThreshold(Long.MAX_VALUE);

        runRoundTrip();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    private static void runRoundTrip()
    {
        int size = (int) Math.min(3 * 1048576, LongModConstants.MAX_TRANSFORM_LENGTH);       // Will use six-step transform
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            Factor3NTTStrategy nttStrategy = new LongAparapiFactor3NTTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            assertEquals("transformed size", size, dataStorage.getSize());

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getLong());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            assertEquals("inverse transformed size", size, dataStorage.getSize());

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getLong());
                iterator.next();
            }
        }
    }

    public static void testRoundTrip2()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);
        ctx.setMemoryThreshold(Long.MAX_VALUE);

        runRoundTrip2();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    private static void runRoundTrip2()
    {
        int size = 2048;                                                        // Will fall back to the power-of-two length transform
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            Factor3NTTStrategy nttStrategy = new LongAparapiFactor3NTTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            assertEquals("transformed size", size, dataStorage.getSize());

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getLong());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            assertEquals("inverse transformed size", size, dataStorage.getSize());

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getLong());
                iterator.next();
            }
        }
    }
}
