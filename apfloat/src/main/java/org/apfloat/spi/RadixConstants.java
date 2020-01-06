/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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
