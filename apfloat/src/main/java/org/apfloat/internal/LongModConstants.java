/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
