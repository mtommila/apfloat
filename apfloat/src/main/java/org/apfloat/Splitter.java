/*
 * MIT License
 *
 * Copyright (c) 2002-2026 Mikko Tommila
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

import java.util.Arrays;

/**
 * Combines multiple Apints into one, or splits one to multiple, for Kronecker substitution.
 *
 * @since 1.16.0
 * @author Mikko Tommila
 */

class Splitter {

    private Splitter()
    {
    }

    public static Apint combine(long stride, Apint... a)
    {
        // Equivalent to a sum of the numbers in array a, each element multiplied by radix^(stride * index)
        assert (a.length > 0);
        assert (Arrays.stream(a).mapToInt(Apint::radix).distinct().count() == 1);

        Apint[] scaled = new Apint[a.length];
        for (int i = 0; i < a.length; i++)
        {
            scaled[a.length - 1 - i] = ApintMath.scale(a[i], stride * i);   // Largest first
        }
        return Apfloat.combine(scaled).truncate();
    }

    private static long scale(Apint i)
    {
        return Math.max(1, i.scale());  // When printed out, zero has one digit i.e. effectively equivalent to a scale of 1
    }

    public static Apint[] split(long stride, Apint a)
    {
        // Equivalent to a / radix^(stride * index) % radix^stride
        int radix = a.radix();
        long digits = scale(a),
             length = (digits + stride - 1) / stride;
        if (length > Integer.MAX_VALUE)
        {
            throw new ApfloatRuntimeException("Maximum array size exceeded: " + length, "maximumArraySizeExceeded", length);
        }
        Apint one = Apint.ONES[radix];

        // Disk transfer speed or memory transfer speed will be a bottleneck, no point in parallelizing this
        Apint[] array = new Apint[(int) length];
        for (int i = 0; i < array.length; i++)
        {
            long position = (i + 1L) * stride,
                 limit = digits - i * stride,
                 shift = -i * stride;
            Apint m = ApintMath.scale(one, position);
            array[i] = a.precision(limit).mod(m).scale(shift).truncate();
        }
        return array;
    }
}
