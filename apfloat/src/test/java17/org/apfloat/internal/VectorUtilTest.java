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
package org.apfloat.internal;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.LongVector;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class VectorUtilTest
    extends TestCase
{
    public VectorUtilTest(String methodName)
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

        suite.addTest(new VectorUtilTest("testRint"));
        suite.addTest(new VectorUtilTest("testToDouble"));
        suite.addTest(new VectorUtilTest("testToFloat"));
        suite.addTest(new VectorUtilTest("testToInt"));
        suite.addTest(new VectorUtilTest("testToLong"));

        return suite;
    }

    public static void testRint()
    {
        assertEquals("rint", v(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 2.0, 2.0), VectorUtil.rint(v(0.1, 0.5, 0.9, 0, 1, 1.1, 1.5, 1.9)));
        assertEquals("rint", v(4503599627370495.0, 4503599627370495.0, 4503599627370495.0, 4503599627370494.0, 4503599627370494.0, 2251799813685248.0, 2251799813685247.0, 2251799813685246.0), VectorUtil.rint(v(4503599627370495.25, 4503599627370495.0, 4503599627370494.75, 4503599627370494.5, 4503599627370494.25, 2251799813685247.5, 2251799813685246.75, 2251799813685246.5)));
    }

    public static void testToDouble()
    {
        assertEquals("F2D 0", v(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0), VectorUtil.toDouble(v(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f, 6.5f, 7.0f, 7.5f, 8.0f), 0));
        assertEquals("F2D 1", v(4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0), VectorUtil.toDouble(v(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f, 6.5f, 7.0f, 7.5f, 8.0f), 1));
        assertEquals("I2D 0", v(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), VectorUtil.toDouble(v(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16), 0));
        assertEquals("I2D 1", v(9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0), VectorUtil.toDouble(v(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16), 1));
        assertEquals("L2D", v(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), VectorUtil.toDouble(v(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)));
        assertEquals("L2D", v(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0), VectorUtil.toDouble(v(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L)));
        assertEquals("L2D", v(0x7FFFFFFFFFFFFC00P0, 0x7FFFFFFFFFFFFC00P0, 0x7FFFFFFFFFFFF800P0, 0x0200000000000000P0, 0x01FFFFFFFFFFFFF0P0, 0x0010000000000000P0, 0x000FFFFFFFFFFFFFP0, 0.0), VectorUtil.toDouble(v(Long.MAX_VALUE, 0x7FFFFFFFFFFFFC00L, 0x7FFFFFFFFFFFF800L, 0x0200000000000000L, 0x01FFFFFFFFFFFFFFL, 0x0010000000000000L, 0x000FFFFFFFFFFFFFL, 0L)));
        assertEquals("L2D", v(-0x8000000000000000P0, -0x7FFFFFFFFFFFFC00P0, -0x7FFFFFFFFFFFFC00P0, -0x7FFFFFFFFFFFE800P0, -0x0200000000000000P0, -0x01FFFFFFFFFFFFF0P0, -0x0010000000000000P0, -0x000FFFFFFFFFFFFFP0), VectorUtil.toDouble(v(Long.MIN_VALUE, -Long.MAX_VALUE, -0x7FFFFFFFFFFFFC00L, -0x7FFFFFFFFFFFE800L, -0x0200000000000000L, -0x01FFFFFFFFFFFFFFL, -0x0010000000000000L, -0x000FFFFFFFFFFFFFL)));
    }

    public static void testToFloat()
    {
        assertEquals("D2F 0", v(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), VectorUtil.toFloat(v(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0), 0));
        assertEquals("D2F -1", v(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f), VectorUtil.toFloat(v(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0), -1));
    }

    public static void testToInt()
    {
        assertEquals("D2I 0", v(1, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0), VectorUtil.toInt(v(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), 0));
        assertEquals("D2I -1", v(0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8), VectorUtil.toInt(v(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), -1));
    }

    public static void testToLong()
    {
        assertEquals("D2L", v(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L), VectorUtil.toLong(v(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)));
        assertEquals("D2L", v(-1L, -2L, -3L, -4L, -5L, -6L, -7L, -8L), VectorUtil.toLong(v(-1.0, -2.0, -3.0, -4.0, -5.0, -6.0, -7.0, -8.0)));
        assertEquals("D2L", v(0x7FFFFFFFFFFFF800L, 0x7FFFFFFFFFFFF000L, 0x7FFFFFFFFFFFE000L, 0x0200000000000000L, 0x01FFFFFFFFFFFFE0L, 0x0010000000000000L, 0x000FFFFFFFFFFFFFL, 0L), VectorUtil.toLong(v(0x7FFFFFFFFFFFF800P0, 0x7FFFFFFFFFFFF000P0, 0x7FFFFFFFFFFFE000P0, 0x0200000000000000P0, 0x01FFFFFFFFFFFFE0P0, 0x0010000000000000P0, 0x000FFFFFFFFFFFFFP0, 0.0)));
        assertEquals("D2L", v(-0x8000000000000000L, -0x7FFFFFFFFFFFF800L, -0x7FFFFFFFFFFFF000L, -0x7FFFFFFFFFFFE000L, -0x0200000000000000L, -0x01FFFFFFFFFFFFE0L, -0x0010000000000000L, -0x000FFFFFFFFFFFFFL), VectorUtil.toLong(v(-0x8000000000000000P0, -0x7FFFFFFFFFFFF800P0, -0x7FFFFFFFFFFFF000P0, -0x7FFFFFFFFFFFE000P0, -0x0200000000000000P0, -0x01FFFFFFFFFFFFE0P0, -0x0010000000000000P0, -0x000FFFFFFFFFFFFFP0)));
    }

    private static DoubleVector v(double... v)
    {
        return DoubleVector.fromArray(DoubleVector.SPECIES_512, v, 0);
    }

    private static FloatVector v(float... v)
    {
        return FloatVector.fromArray(FloatVector.SPECIES_512, v, 0);
    }

    private static IntVector v(int... v)
    {
        return IntVector.fromArray(IntVector.SPECIES_512, v, 0);
    }

    private static LongVector v(long... v)
    {
        return LongVector.fromArray(LongVector.SPECIES_512, v, 0);
    }
}
