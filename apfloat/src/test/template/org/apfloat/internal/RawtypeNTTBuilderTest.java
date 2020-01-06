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
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeNTTBuilderTest
    extends RawtypeTestCase
{
    public RawtypeNTTBuilderTest(String methodName)
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

        suite.addTest(new RawtypeNTTBuilderTest("testCreate"));

        return suite;
    }

    public static void testCreate()
    {
        NTTBuilder nttBuilder = new RawtypeNTTBuilder();
        ApfloatContext ctx = ApfloatContext.getContext();
        int cacheSize = ctx.getCacheL1Size() / sizeof(rawtype);
        long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize() / sizeof(rawtype);

        assertTrue("Fits in cache", nttBuilder.createNTT(cacheSize / 2) instanceof RawtypeTableFNTStrategy);
        assertTrue("Fits in memory", nttBuilder.createNTT(Util.round2down(maxMemoryBlockSize)) instanceof SixStepFNTStrategy);
        assertTrue("Does not fit in memory", nttBuilder.createNTT(Util.round2down(maxMemoryBlockSize * 2)) instanceof TwoPassFNTStrategy);
        assertTrue("Factor 3", nttBuilder.createNTT(3) instanceof Factor3NTTStrategy);

        assertTrue("NTTStepStrategy", nttBuilder.createNTTSteps() instanceof NTTStepStrategy);
        assertTrue("NTTConvolutionStepStrategy", nttBuilder.createNTTConvolutionSteps() instanceof NTTConvolutionStepStrategy);
        assertTrue("Factor3NTTStepStrategy", nttBuilder.createFactor3NTTSteps() instanceof Factor3NTTStepStrategy);

        ctx.setMaxMemoryBlockSize(0x80000000L * sizeof(rawtype));
        assertTrue("Does not fit in array", nttBuilder.createNTT(0x80000000L) instanceof TwoPassFNTStrategy);
        ctx.setMaxMemoryBlockSize(maxMemoryBlockSize * sizeof(rawtype));
    }
}
