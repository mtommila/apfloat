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

public class IntAparapiSixStepFNTStrategyTest
    extends IntNTTStrategyTestCase
{
    public IntAparapiSixStepFNTStrategyTest(String methodName)
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

        suite.addTest(new IntAparapiSixStepFNTStrategyTest("testForward"));
        suite.addTest(new IntAparapiSixStepFNTStrategyTest("testForwardBig"));
        suite.addTest(new IntAparapiSixStepFNTStrategyTest("testRoundTrip"));
        suite.addTest(new IntAparapiSixStepFNTStrategyTest("testRoundTripBig"));

        return suite;
    }

    public static void testForward()
    {
        runTestForward(1024);
    }

    public static void testForwardBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMemoryThreshold(8192);
        runTestForward(4096);
    }

    private static void runTestForward(int size)
    {
        runTestForward(new IntAparapiSixStepFNTStrategy(), size);
    }

    protected static void runTestForward(AbstractStepFNTStrategy nttStrategy, int size)
    {
        for (int modulus = 0; modulus < 3; modulus++)
        {
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            int[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            IntScramble.scramble(expectedTransform, 0, Scramble.createScrambleTable(size));
            Arrays.sort(expectedTransform);

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
        runRoundTrip(1024);
    }

    public static void testRoundTripBig()
    {
        int size = (int) Math.min(1 << 21, IntModConstants.MAX_TRANSFORM_LENGTH & -IntModConstants.MAX_TRANSFORM_LENGTH);
        runRoundTrip(size);
    }

    private static void runRoundTrip(int size)
    {
        runRoundTrip(new IntAparapiSixStepFNTStrategy(), size);
    }
}
