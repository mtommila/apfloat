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
package org.apfloat.aparapi;

import java.util.Arrays;

import org.apfloat.*;
import org.apfloat.spi.*;
import org.apfloat.internal.*;

import junit.framework.TestSuite;

/**
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class IntAparapiFactor3NTTStrategyTest
    extends IntNTTStrategyTestCase
{
    public IntAparapiFactor3NTTStrategyTest(String methodName)
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

        suite.addTest(new IntAparapiFactor3NTTStrategyTest("testForward"));
        suite.addTest(new IntAparapiFactor3NTTStrategyTest("testRoundTrip"));
        suite.addTest(new IntAparapiFactor3NTTStrategyTest("testRoundTrip2"));

        return suite;
    }

    public static void testForward()
    {
        runForward();
    }

    private static void runForward()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        ctx.setMemoryThreshold(Long.MAX_VALUE);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            int size = 3 * 2048;
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            int[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            Arrays.sort(expectedTransform);

            NTTStrategy nttStrategy = new IntAparapiFactor3NTTStrategy(new IntAparapiColumnSixStepFNTStrategy());

            nttStrategy.transform(dataStorage, modulus);

            int[] actualTransform = getPlainArray(dataStorage);
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
        int size = (int) Math.min(3 * 1048576, IntModConstants.MAX_TRANSFORM_LENGTH);       // Will use six-step transform
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            Factor3NTTStrategy nttStrategy = new IntAparapiFactor3NTTStrategy(new IntAparapiColumnSixStepFNTStrategy());

            nttStrategy.transform(dataStorage, modulus);

            assertEquals("transformed size", size, dataStorage.getSize());

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getInt());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            assertEquals("inverse transformed size", size, dataStorage.getSize());

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getInt());
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
            Factor3NTTStrategy nttStrategy = new IntAparapiFactor3NTTStrategy(new IntAparapiSixStepFNTStrategy());

            nttStrategy.transform(dataStorage, modulus);

            assertEquals("transformed size", size, dataStorage.getSize());

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getInt());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            assertEquals("inverse transformed size", size, dataStorage.getSize());

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getInt());
                iterator.next();
            }
        }
    }
}
