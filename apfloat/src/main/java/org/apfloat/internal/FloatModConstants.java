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
 * Constants needed for various modular arithmetic operations for the <code>float</code> type.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface FloatModConstants
{
    /**
     * Moduli to be used in number theoretic transforms.
     * Allows transform lengths upto 3*2<sup>17</sup>.
     */

    public static final float MODULUS[] = { 16515073.0f, 14942209.0f, 14155777.0f };

    /**
     * Primitive roots for the corresponding moduli.
     */

    public static final float PRIMITIVE_ROOT[] = { 5.0f, 11.0f, 7.0f };

    /**
     * Maximum transform length for the moduli.
     */

    public static final long MAX_TRANSFORM_LENGTH = 393216;

    /**
     * Maximum bits in a power-of-two base that fits in a <code>float</code>.
     */

    public static final int MAX_POWER_OF_TWO_BITS = 24;

    /**
     * Maximum power-of-two base that fits in a <code>float</code>.
     */

    public static final float MAX_POWER_OF_TWO_BASE = (float) (1 << MAX_POWER_OF_TWO_BITS);
}
