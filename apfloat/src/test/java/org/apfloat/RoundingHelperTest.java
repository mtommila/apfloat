/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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
package org.apfloat;

import java.math.RoundingMode;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.11.0
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

        suite.addTest(new RoundingHelperTest("testRoundToPrecision"));
        suite.addTest(new RoundingHelperTest("testRoundToInteger"));
        suite.addTest(new RoundingHelperTest("testRoundToPlaces"));
        suite.addTest(new RoundingHelperTest("testRoundToMultiple"));
        suite.addTest(new RoundingHelperTest("testCompareToHalf"));

        return suite;
    }

    public static void testRoundToPrecision()
    {
        assertEquals("roundToPrecision", new Apfloat("1.0"), RoundingHelper.roundToPrecision(new Apfloat("1.1"), 1, RoundingMode.DOWN));
    }

    public static void testRoundToInteger()
    {
        assertEquals("roundToInteger", new Apfloat(12346), RoundingHelper.roundToInteger(new Apfloat("12345.67"), RoundingMode.HALF_EVEN));
    }

    public static void testRoundToPlaces()
    {
        assertEquals("roundToPlaces", new Apfloat("12345.6"), RoundingHelper.roundToPlaces(new Apfloat("12345.65"), 1, RoundingMode.HALF_EVEN));
    }

    public static void testRoundToMultiple()
    {
        assertEquals("roundToMultiple float", new Apfloat("6.6"), RoundingHelper.roundToMultiple(new Apfloat("5.5"), new Apfloat("2.2"), RoundingMode.HALF_UP));
        assertEquals("roundToMultiple rational", new Aprational("4/5"), RoundingHelper.roundToMultiple(new Apint(1), new Aprational("2/5"), RoundingMode.HALF_DOWN));
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
