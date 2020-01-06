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

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class RawtypeSixStepFNTStrategyTest
    extends RawtypeNTTStrategyTestCase
{
    public RawtypeSixStepFNTStrategyTest(String methodName)
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

        suite.addTest(new RawtypeSixStepFNTStrategyTest("testForward"));
        suite.addTest(new RawtypeSixStepFNTStrategyTest("testForwardBig"));
        suite.addTest(new RawtypeSixStepFNTStrategyTest("testRoundTrip"));
        suite.addTest(new RawtypeSixStepFNTStrategyTest("testRoundTripBig"));
        suite.addTest(new RawtypeSixStepFNTStrategyTest("testRoundTripMultithread"));
        suite.addTest(new RawtypeSixStepFNTStrategyTest("testRoundTripMultithreadBig"));

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
        for (int modulus = 0; modulus < 3; modulus++)
        {
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            rawtype[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            RawtypeScramble.scramble(expectedTransform, 0, Scramble.createScrambleTable(size));
            Arrays.sort(expectedTransform);

            AbstractStepFNTStrategy nttStrategy = new SixStepFNTStrategy();

            nttStrategy.transform(dataStorage, modulus);

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
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(1);
        runRoundTrip(1024);
    }

    public static void testRoundTripBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(1);
        int size = (int) Math.min(1 << 21, RawtypeModConstants.MAX_TRANSFORM_LENGTH & -RawtypeModConstants.MAX_TRANSFORM_LENGTH);
        runRoundTrip(size);
    }

    public static void testRoundTripMultithread()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip(1024);
    }

    public static void testRoundTripMultithreadBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        int size = (int) Math.min(1 << 21, RawtypeModConstants.MAX_TRANSFORM_LENGTH & -RawtypeModConstants.MAX_TRANSFORM_LENGTH);
        runRoundTrip(size);
    }

    private static void runRoundTrip(int size)
    {
        runRoundTrip(new SixStepFNTStrategy(), size);
    }
}
