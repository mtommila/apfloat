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
package org.apfloat.internal;

/**
 * Constants needed for various modular arithmetic operations for the <code>int</code> type.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface IntModConstants
{
    /**
     * Moduli to be used in number theoretic transforms.
     * Allows transform lengths upto 3*2<sup>24</sup>.
     */

    public static final int MODULUS[] = { 2113929217, 2013265921, 1811939329 };

    /**
     * Primitive roots for the corresponding moduli.
     */

    public static final int PRIMITIVE_ROOT[] = { 5, 31, 13 };

    /**
     * Maximum transform length for the moduli.
     */

    public static final long MAX_TRANSFORM_LENGTH = 50331648;

    /**
     * Maximum bits in a power-of-two base that fits in an <code>int</code>.
     */

    public static final int MAX_POWER_OF_TWO_BITS = 31;

    /**
     * Maximum power-of-two base that fits in an <code>int</code>.
     */

    public static final int MAX_POWER_OF_TWO_BASE = 1 << MAX_POWER_OF_TWO_BITS;
}
