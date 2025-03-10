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
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;

/**
 * Vector utility functions. The obvious methods to do these operations using the Vector API can be two
 * orders of magnitude slower, if the CPU does not support AVX-512.
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
     * Round to nearest integer. Only works for values smaller than 1 << 52. Does not work for negative values.
     *
     * @param v The vector.
     *
     * @return <code>v</code> rounded to nearest integer.
     */

    public static DoubleVector rint(DoubleVector v)
    {
        return v.add(4503599627370496.0).sub(4503599627370496.0);
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
        DoubleVector s = v.and(0x8000000000000000L).or(0x3FF0000000000000L).viewAsFloatingLanes();
        LongVector va = v.abs();    // Should consider the case that v is Long.MIN_VALUE
        VectorMask<Long> m52 = va.compare(VectorOperators.UNSIGNED_LT, 0x0010000000000000L);
        VectorMask<Long> m53 = va.compare(VectorOperators.UNSIGNED_LT, 0x0020000000000000L);
        VectorMask<Long> m54 = va.compare(VectorOperators.UNSIGNED_LT, 0x0040000000000000L);
        VectorMask<Long> m55 = va.compare(VectorOperators.UNSIGNED_LT, 0x0080000000000000L);
        VectorMask<Long> m56 = va.compare(VectorOperators.UNSIGNED_LT, 0x0100000000000000L);
        VectorMask<Long> m57 = va.compare(VectorOperators.UNSIGNED_LT, 0x0200000000000000L);
        VectorMask<Long> m58 = va.compare(VectorOperators.UNSIGNED_LT, 0x0400000000000000L);
        VectorMask<Long> m59 = va.compare(VectorOperators.UNSIGNED_LT, 0x0800000000000000L);
        VectorMask<Long> m60 = va.compare(VectorOperators.UNSIGNED_LT, 0x1000000000000000L);
        VectorMask<Long> m61 = va.compare(VectorOperators.UNSIGNED_LT, 0x2000000000000000L);
        VectorMask<Long> m62 = va.compare(VectorOperators.UNSIGNED_LT, 0x4000000000000000L);
        VectorMask<Long> m63 = va.compare(VectorOperators.UNSIGNED_LT, 0x8000000000000000L);

        LongVector r52 = va.add(0x4330000000000000L).viewAsFloatingLanes().sub(4503599627370496.0).viewAsIntegralLanes();
        LongVector r53 = va.add(0x4320000000000000L);
        LongVector r54 = va.lanewise(VectorOperators.ASHR, 1).add(0x4330000000000000L);
        LongVector r55 = va.lanewise(VectorOperators.ASHR, 2).add(0x4340000000000000L);
        LongVector r56 = va.lanewise(VectorOperators.ASHR, 3).add(0x4350000000000000L);
        LongVector r57 = va.lanewise(VectorOperators.ASHR, 4).add(0x4360000000000000L);
        LongVector r58 = va.lanewise(VectorOperators.ASHR, 5).add(0x4370000000000000L);
        LongVector r59 = va.lanewise(VectorOperators.ASHR, 6).add(0x4380000000000000L);
        LongVector r60 = va.lanewise(VectorOperators.ASHR, 7).add(0x4390000000000000L);
        LongVector r61 = va.lanewise(VectorOperators.ASHR, 8).add(0x43a0000000000000L);
        LongVector r62 = va.lanewise(VectorOperators.ASHR, 9).add(0x43b0000000000000L);
        LongVector r63 = va.lanewise(VectorOperators.ASHR, 10).add(0x43c0000000000000L);
        LongVector r64 = v.lanewise(VectorOperators.ASHR, 11).abs().add(0x43d0000000000000L);   // Handle v Long.MIN_VALUE
        DoubleVector r = r64.blend(r63, m63).blend(r62, m62).blend(r61, m61).blend(r60, m60).blend(r59, m59).blend(r58, m58).blend(r57, m57).blend(r56, m56).blend(r55, m55).blend(r54, m54).blend(r53, m53).blend(r52, m52).viewAsFloatingLanes();

        r = r.blend(r.neg(), s.lt(0.0));
        return r;
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
        DoubleVector va = v.abs();
        VectorMask<Double> m52 = va.lt(4503599627370496.0);
        VectorMask<Double> m53 = va.lt(9007199254740992.0);
        VectorMask<Double> m54 = va.lt(18014398509481984.0);
        VectorMask<Double> m55 = va.lt(36028797018963968.0);
        VectorMask<Double> m56 = va.lt(72057594037927936.0);
        VectorMask<Double> m57 = va.lt(144115188075855872.0);
        VectorMask<Double> m58 = va.lt(288230376151711744.0);
        VectorMask<Double> m59 = va.lt(576460752303423488.0);
        VectorMask<Double> m60 = va.lt(1152921504606846976.0);
        VectorMask<Double> m61 = va.lt(2305843009213693952.0);
        VectorMask<Double> m62 = va.lt(4611686018427387904.0);

        DoubleVector r52 = va.add(4503599627370496.0).viewAsIntegralLanes().sub(0x4330000000000000L).viewAsFloatingLanes();
        DoubleVector r53 = va.viewAsIntegralLanes().sub(0x4320000000000000L).viewAsFloatingLanes();
        DoubleVector r54 = va.viewAsIntegralLanes().sub(0x4330000000000000L).lanewise(VectorOperators.LSHL, 1).viewAsFloatingLanes();
        DoubleVector r55 = va.viewAsIntegralLanes().sub(0x4340000000000000L).lanewise(VectorOperators.LSHL, 2).viewAsFloatingLanes();
        DoubleVector r56 = va.viewAsIntegralLanes().sub(0x4350000000000000L).lanewise(VectorOperators.LSHL, 3).viewAsFloatingLanes();
        DoubleVector r57 = va.viewAsIntegralLanes().sub(0x4360000000000000L).lanewise(VectorOperators.LSHL, 4).viewAsFloatingLanes();
        DoubleVector r58 = va.viewAsIntegralLanes().sub(0x4370000000000000L).lanewise(VectorOperators.LSHL, 5).viewAsFloatingLanes();
        DoubleVector r59 = va.viewAsIntegralLanes().sub(0x4380000000000000L).lanewise(VectorOperators.LSHL, 6).viewAsFloatingLanes();
        DoubleVector r60 = va.viewAsIntegralLanes().sub(0x4390000000000000L).lanewise(VectorOperators.LSHL, 7).viewAsFloatingLanes();
        DoubleVector r61 = va.viewAsIntegralLanes().sub(0x43a0000000000000L).lanewise(VectorOperators.LSHL, 8).viewAsFloatingLanes();
        DoubleVector r62 = va.viewAsIntegralLanes().sub(0x43b0000000000000L).lanewise(VectorOperators.LSHL, 9).viewAsFloatingLanes();
        DoubleVector r63 = va.viewAsIntegralLanes().sub(0x43c0000000000000L).lanewise(VectorOperators.LSHL, 10).viewAsFloatingLanes();
        LongVector r = r63.blend(r62, m62).blend(r61, m61).blend(r60, m60).blend(r59, m59).blend(r58, m58).blend(r57, m57).blend(r56, m56).blend(r55, m55).blend(r54, m54).blend(r53, m53).blend(r52, m52).viewAsIntegralLanes();

        r = r.blend(r.neg(), v.viewAsIntegralLanes().lt(0));
        return r;
    }
}
