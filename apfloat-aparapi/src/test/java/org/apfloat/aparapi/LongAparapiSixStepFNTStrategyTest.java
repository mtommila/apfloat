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
package org.apfloat.aparapi;

import org.apfloat.internal.*;

import junit.framework.TestSuite;

/**
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class LongAparapiSixStepFNTStrategyTest
    extends LongNTTStrategyTestCase
{
    public LongAparapiSixStepFNTStrategyTest(String methodName)
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

        suite.addTest(new LongAparapiSixStepFNTStrategyTest("testRoundTrip"));
        suite.addTest(new LongAparapiSixStepFNTStrategyTest("testRoundTripBig"));

        return suite;
    }

    public static void testRoundTrip()
    {
        runRoundTrip(1024);
    }

    public static void testRoundTripBig()
    {
        int size = (int) Math.min(1 << 21, LongModConstants.MAX_TRANSFORM_LENGTH & -LongModConstants.MAX_TRANSFORM_LENGTH);
        runRoundTrip(size);
    }

    private static void runRoundTrip(int size)
    {
        runRoundTrip(new ColumnSixStepFNTStrategy(new LongAparapiNTTStepStrategy()), size);
    }
}
