/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
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

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeParallelThreeNTTConvolutionStrategyTest
    extends RawtypeThreeNTTConvolutionStrategyTest
{
    public RawtypeParallelThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAutoBigParallel"));

        return suite;
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, transform);
    }
}
