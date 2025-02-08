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
package org.apfloat.aparapi;

import org.apfloat.spi.Util;

import com.aparapi.Range;

/**
 * Range helper methods.
 *
 * @since 1.11.0
 * @version 1.15.0
 *
 * @author Mikko Tommila
 */

class RangeHelper
{
    // Note: it seems that this class is just a workaround to aparapi issue #171 where the local size is sometimes not set correctly
    // See: https://github.com/Syncleus/aparapi/issues/171

    private RangeHelper()
    {
    }

    /**
     * Creates a one-dimensional range with the maximum local size.
     *
     * @param size The size.
     *
     * @return The range.
     */

    public static Range create(int size)
    {
        assert (size == (size & -size));    // Must be a power of two
        int localSize = Math.min(size, MAX_LOCAL_SIZE);
        return Range.create(size, localSize);
    }

    /**
     * Creates a two-dimensional range with the maximum local sizes.
     *
     * @param width Size of the first dimension.
     * @param height Size of the second dimension.
     *
     * @return The range.
     */

    public static Range create2D(int width, int height)
    {
        assert (width == (width & -width));     // Must be a power of two
        assert (height == (height & -height));  // Must be a power of two
        int localWidth = Math.min(width, Util.sqrt4up(MAX_LOCAL_SIZE)),
            localHeight = Math.min(height, MAX_LOCAL_SIZE / localWidth);
        return Range.create2D(width, height, localWidth, localHeight);
    }

    /**
     * Maximum OpenCL work-group size. Note that this is the local sizes in all dimensions multiplied together.
     */

    public static final int MAX_LOCAL_SIZE = 256;
}
