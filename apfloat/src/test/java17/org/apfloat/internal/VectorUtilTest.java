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

        suite.addTest(new VectorUtilTest("testFloor"));
        suite.addTest(new VectorUtilTest("testToDouble"));
        suite.addTest(new VectorUtilTest("testToFloat"));
        suite.addTest(new VectorUtilTest("testToInt"));
        suite.addTest(new VectorUtilTest("testToLong"));

        return suite;
    }

    public static void testFloor()
    {
        assertEquals("floor", v(0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0), VectorUtil.floor(v(0.1, 0.5, 0.9, 0, 1, 1.1, 1.5, 1.9)));
    }

    public static void testToDouble()
    {
        assertEquals("F2D 0", v(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0), VectorUtil.toDouble(v(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f, 6.5f, 7.0f, 7.5f, 8.0f), 0));
        assertEquals("F2D 1", v(4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0), VectorUtil.toDouble(v(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f, 6.5f, 7.0f, 7.5f, 8.0f), 1));
        assertEquals("I2D 0", v(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), VectorUtil.toDouble(v(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16), 0));
        assertEquals("I2D 1", v(9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0), VectorUtil.toDouble(v(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16), 1));
        assertEquals("L2D", v(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), VectorUtil.toDouble(v(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)));
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
