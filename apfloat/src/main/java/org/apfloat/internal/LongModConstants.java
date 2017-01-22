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
 * Constants needed for various modular arithmetic operations for the <code>long</code> type.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface LongModConstants
{
    /**
     * Moduli to be used in number theoretic transforms.
     * Allows transform lengths upto 3*2<sup>47</sup>.
     */

    public static final long MODULUS[] = { 136796838681378817L, 127508164449927169L, 119063915148607489L };

    /**
     * Primitive roots for the corresponding moduli.
     */

    public static final long PRIMITIVE_ROOT[] = { 5, 14, 26 };

    /**
     * Maximum transform length for the moduli.
     */

    public static final long MAX_TRANSFORM_LENGTH = 422212465065984L;

    /**
     * Maximum bits in a power-of-two base that fits in a <code>long</code>.
     */

    public static final int MAX_POWER_OF_TWO_BITS = 57;

    /**
     * Maximum power-of-two base that fits in a <code>long</code>.
     */

    public static final long MAX_POWER_OF_TWO_BASE = 1L << MAX_POWER_OF_TWO_BITS;
}
