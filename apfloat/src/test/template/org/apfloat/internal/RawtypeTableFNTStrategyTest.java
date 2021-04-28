/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
