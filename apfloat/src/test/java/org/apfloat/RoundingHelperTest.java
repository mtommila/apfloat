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
package org.apfloat;

import java.math.RoundingMode;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RoundingHelperTest
    extends TestCase
{
    public RoundingHelperTest(String methodName)
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

        suite.addTest(new RoundingHelperTest("testRound"));
        suite.addTest(new RoundingHelperTest("testCompareToHalf"));

        return suite;
    }

    public static void testRound()
    {
        assertEquals("round", new Apfloat("1.0"), RoundingHelper.round(new Apfloat("1.1"), 1, RoundingMode.DOWN));
    }

    public static void testCompareToHalf()
    {
        assertEquals("0", -1, RoundingHelper.compareToHalf(new Apfloat("0")));
        assertEquals("0.4", -1, RoundingHelper.compareToHalf(new Apfloat("0.4")));
        assertEquals("0.5", 0, RoundingHelper.compareToHalf(new Apfloat("0.5")));
        assertEquals("0.6", 1, RoundingHelper.compareToHalf(new Apfloat("0.6")));

        assertEquals("0.4 radix 9", -1, RoundingHelper.compareToHalf(new Apfloat("0.4", 1, 9)));
        assertEquals("0.5 radix 9", 1, RoundingHelper.compareToHalf(new Apfloat("0.6", 1, 9)));

        assertEquals("1/3", -1, RoundingHelper.compareToHalf(new Aprational("1/3")));
        assertEquals("1/2", 0, RoundingHelper.compareToHalf(new Aprational("1/2")));
        assertEquals("2/3", 1, RoundingHelper.compareToHalf(new Aprational("2/3")));

        assertEquals("1/3 radix 3", -1, RoundingHelper.compareToHalf(new Aprational("1/10", 3)));
        assertEquals("1/2 radix 3", 0, RoundingHelper.compareToHalf(new Aprational("1/2", 3)));
        assertEquals("2/3 radix 3", 1, RoundingHelper.compareToHalf(new Aprational("2/10", 3)));
    }
}
