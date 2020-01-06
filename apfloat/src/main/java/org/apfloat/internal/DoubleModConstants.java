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
package org.apfloat.internal;

/**
 * Constants needed for various modular arithmetic operations for the <code>double</code> type.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface DoubleModConstants
{
    /**
     * Moduli to be used in number theoretic transforms.
     * Allows transform lengths upto 3*2<sup>43</sup>.
     */

    public static final double MODULUS[] = { 1952732650930177.0, 1899956092796929.0, 1636073302130689.0 };

    /**
     * Primitive roots for the corresponding moduli.
     */

    public static final double PRIMITIVE_ROOT[] = { 5.0, 7.0, 17.0 };

    /**
     * Maximum transform length for the moduli.
     */

    public static final long MAX_TRANSFORM_LENGTH = 26388279066624L;

    /**
     * Maximum bits in a power-of-two base that fits in a <code>double</code>.
     */

    public static final int MAX_POWER_OF_TWO_BITS = 51;

    /**
     * Maximum power-of-two base that fits in a <code>double</code>.
     */

    public static final double MAX_POWER_OF_TWO_BASE = (double) (1L << MAX_POWER_OF_TWO_BITS);
}
