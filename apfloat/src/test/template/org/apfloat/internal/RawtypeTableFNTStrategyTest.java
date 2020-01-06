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

import java.util.Arrays;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeTableFNTStrategyTest
    extends RawtypeNTTStrategyTestCase
{
    public RawtypeTableFNTStrategyTest(String methodName)
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

        suite.addTest(new RawtypeTableFNTStrategyTest("testForward"));
        suite.addTest(new RawtypeTableFNTStrategyTest("testInverse"));
        suite.addTest(new RawtypeTableFNTStrategyTest("testRoundTrip"));

        return suite;
    }

    public static void testForward()
    {
        for (int modulus = 0; modulus < 3; modulus++)
        {
            int size = 8192;        // Good for memory threshold of 64kB
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            rawtype[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            RawtypeScramble.scramble(expectedTransform, 0, Scramble.createScrambleTable(size));
            //Arrays.sort(expectedTransform);

            NTTStrategy nttStrategy = new RawtypeTableFNTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            rawtype[] actualTransform = getPlainArray(dataStorage);
            //Arrays.sort(actualTransform);

            assertEquals("expected length", size, expectedTransform.length);
            assertEquals("actual length", size, actualTransform.length);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], [" + i + "]", (long) expectedTransform[i], (long) actualTransform[i]);
            }
        }
    }

    public static void testInverse()
    {
        for (int modulus = 0; modulus < 3; modulus++)
        {
            int size = 8192;        // Good for memory threshold of 64kB
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            rawtype[] data = getPlainArray(dataStorage);
            RawtypeScramble.scramble(data, 0, Scramble.createScrambleTable(size));
            rawtype[] expectedTransform = inverseNtt(data, modulus);
            Arrays.sort(expectedTransform);

            NTTStrategy nttStrategy = new RawtypeTableFNTStrategy();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            rawtype[] actualTransform = getPlainArray(dataStorage);
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
        int size = 8192;        // Good for memory threshold of 64kB
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            NTTStrategy nttStrategy = new RawtypeTableFNTStrategy();

            nttStrategy.transform(dataStorage, modulus);

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getRawtype());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getRawtype());
                iterator.next();
            }
        }
    }
}
