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
import jdk.incubator.vector.VectorOperators;

/**
 * Vector utility functions.
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

class VectorUtil
{
    private VectorUtil()
    {
    }

    /**
     * The floor function. May only work correctly for nonnegative values.
     *
     * @param v The vector.
     *
     * @return The floor of <code>v</code>.
     */

    public static DoubleVector floor(DoubleVector v)
    {
        return v.convert(VectorOperators.D2L, 0).convert(VectorOperators.L2D, 0).reinterpretAsDoubles();
    }

    /**
     * Convert to double.
     *
     * @param v The vector.
     * @param part The part, 0 or 1.
     *
     * @return The double vector.
     */

    public static DoubleVector toDouble(FloatVector v, int part)
    {
        return v.convert(VectorOperators.F2D, part).reinterpretAsDoubles();
    }

    /**
     * Convert to double.
     *
     * @param v The vector.
     * @param part The part, 0 or 1.
     *
     * @return The double vector.
     */

    public static DoubleVector toDouble(IntVector v, int part)
    {
        return v.convert(VectorOperators.I2D, part).reinterpretAsDoubles();
    }

    /**
     * Convert to double.
     *
     * @param v The vector.
     *
     * @return The double vector.
     */

    public static DoubleVector toDouble(LongVector v)
    {
        return v.convert(VectorOperators.L2D, 0).reinterpretAsDoubles();
    }

    /**
     * Convert to float.
     *
     * @param v The vector.
     * @param part The part, 0 or -1.
     *
     * @return The float vector.
     */

    public static FloatVector toFloat(DoubleVector v, int part)
    {
        return v.convert(VectorOperators.D2F, part).reinterpretAsFloats();
    }

    /**
     * Convert to int.
     *
     * @param v The vector.
     * @param part The part, 0 or -1.
     *
     * @return The int vector.
     */

    public static IntVector toInt(DoubleVector v, int part)
    {
        return v.convert(VectorOperators.D2I, part).reinterpretAsInts();
    }

    /**
     * Convert to long.
     *
     * @param v The vector.
     *
     * @return The long vector.
     */

    public static LongVector toLong(DoubleVector v)
    {
        return v.convert(VectorOperators.D2L, 0).reinterpretAsLongs();
    }
}
