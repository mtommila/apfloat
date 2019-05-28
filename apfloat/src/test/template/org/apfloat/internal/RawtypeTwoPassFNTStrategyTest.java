/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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

public class RawtypeTwoPassFNTStrategyTest
    extends RawtypeNTTStrategyTestCase
{
    public RawtypeTwoPassFNTStrategyTest(String methodName)
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

        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTrip"));
        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTripBig"));
        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTripMultithread"));
        suite.addTest(new RawtypeTwoPassFNTStrategyTest("testRoundTripMultithreadBig"));

        return suite;
    }

    public static void testRoundTrip()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(1);
        runRoundTrip(131072);
    }

    public static void testRoundTripBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(1);
        runRoundTrip((int) Math.min(1 << 21, Util.round2down(RawtypeModConstants.MAX_TRANSFORM_LENGTH)));
    }

    public static void testRoundTripMultithread()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip(131072);
    }

    public static void testRoundTripMultithreadBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setMaxMemoryBlockSize(65536);
        ctx.setMemoryThreshold(1024);
        ctx.setBlockSize(256);
        ctx.setNumberOfProcessors(3);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());
        runRoundTrip((int) Math.min(1 << 21, Util.round2down(RawtypeModConstants.MAX_TRANSFORM_LENGTH)));
    }

    private static void runRoundTrip(int size)
    {
        runRoundTrip(new TwoPassFNTStrategy(), size);
    }
}
