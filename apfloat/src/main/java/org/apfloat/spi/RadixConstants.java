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
package org.apfloat.spi;

/**
 * Constants related to different radixes.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface RadixConstants
{
    /**
     * Factors of numbers 2, ..., 36. For 2 &lt;= radix &lt;= 36,
     * <code>RADIX_FACTORS[radix]</code> contains an array of
     * integers containing the different factors of the radix.
     */

    public static final int RADIX_FACTORS[][] = { null, null, { 2 }, { 3 }, { 2 }, { 5 }, { 2, 3 }, { 7 }, { 2 }, { 3 }, { 2, 5 }, { 11 }, { 2, 3 }, { 13 }, { 2, 7 }, { 3, 5 }, { 2 }, { 17 }, { 2, 3 }, { 19 }, { 2, 5 }, { 3, 7 }, { 2, 11 }, { 23 }, { 2, 3 }, { 5 }, { 2, 13 }, { 3 }, { 2, 7 }, { 29 }, { 2, 3, 5 }, { 31 }, { 2 }, { 3, 11 }, { 2, 17 }, { 5, 7 }, { 2, 3 } };

    /**
     * Precision of a <code>float</code> in the digits of each radix.
     */

    public static final int FLOAT_PRECISION[] = { -1, -1, 24, 16, 12, 11, 10, 9, 8, 8, 8, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5 };

    /**
     * Precision of a <code>double</code> in the digits of each radix.
     */

    public static final int DOUBLE_PRECISION[] = { -1, -1, 53, 34, 27, 23, 21, 19, 18, 17, 16, 16, 15, 15, 14, 14, 14, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 11, 11, 11, 11, 11, 11, 11, 11 };

    /**
     * Precision of a <code>long</code> in the digits of each radix.
     */

    public static final int LONG_PRECISION[] = { -1, -1, 63, 40, 32, 28, 25, 23, 21, 20, 19, 19, 18, 18, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 13, 13 };

    /**
     * How many digits maximally fit in a <code>long</code> in each radix.
     */

    public static final int LONG_DIGITS[] = { -1, -1, 63, 39, 31, 27, 24, 22, 21, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12 };
}
