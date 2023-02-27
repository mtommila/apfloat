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
package org.apfloat.internal;

import static org.apfloat.internal.LongModConstants.*;

import java.math.BigInteger;

/**
 * Elementary modulo arithmetic functions for <code>long</code> data.<p>
 *
 * Modular addition and subtraction are trivial, when the modulus is less
 * than 2<sup>63</sup> and overflow can be detected easily.<p>
 *
 * Modular multiplication is more complicated, and since it is usually
 * the single most time consuming operation in the whole program execution,
 * the very core of the Number Theoretic Transform (NTT), it should be
 * carefully optimized.<p>
 *
 * The algorithm for multiplying two <code>long</code>s and taking the
 * remainder is not entirely obvious. The basic problem is to get the
 * full 128-bit result of multiplying two 64-bit integers. Before the
 * introduction of {@link Math#multiplyHigh(long, long)} it was impractically
 * slow. The legacy of this code is that the older algorithm used an
 * approach where the top 52 bits of the multiplication would be retrieved
 * by converting the operands to <code>double</code> and <code>long</code>s
 * would only be used to get the lowest 64 bits of the result. With round-off
 * errors this allows using only 57 bits in the multiplication operands. For
 * serialization compatibility, this algorithm still has the same limitation.
 * <p>
 *
 * The first observation is that since the modulus is practically
 * constant, it should be more efficient to calculate (once) the inverse
 * of the modulus, and then subsequently multiply by the inverse modulus
 * instead of dividing by the modulus.<p>
 *
 * The second observation is that to get the remainder of the division,
 * we don't necessarily need the actual result of the division (we just
 * want the remainder). So, we should discard the top half bits of the
 * full 128-bit result whenever possible, to save a few operations.<p>
 *
 * The basic approach is to get an approximation of <code>a * b / modulus</code>.
 * To calculate the approximate division, we multiply by the inverse modulus
 * using fixed-point multiplication (where the inverse modulus is calculated to
 * 63 bits of precision) and {@link Math#multiplyHigh(long, long)}. With 57-bit
 * multiplicands and 63 bits of the inverse modulus, and a prime modulus, the
 * approximation should be within 1 of the correct result. Thus we simply calculate
 * <code>a * b - approximateDivision * modulus</code> to get the initial remainder.
 * It is then easy to detect the case when the approximate division was off by one
 * as the final step of the algorithm.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class LongElementaryModMath
{
    /**
     * Default constructor.
     */

    public LongElementaryModMath()
    {
    }

    /**
     * Modular multiplication.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>a * b % modulus</code>
     */

    public final long modMultiply(long a, long b)
    {
        long tl = a * b,
             th = Math.multiplyHigh(a, b) << 9 | tl >>> 55;
        long r1 = tl - Math.multiplyHigh(th, this.inverseModulus) * this.modulus,
             r2 = r1 - this.modulus;

        return (r2 < 0 ? r1 : r2);
    }

    /**
     * Modular addition.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a + b) % modulus</code>
     */

    public final long modAdd(long a, long b)
    {
        long r = a + b;

        return (r >= this.modulus ? r - this.modulus : r);
    }

    /**
     * Modular subtraction. The result is always &gt;= 0.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a - b + modulus) % modulus</code>
     */

    public final long modSubtract(long a, long b)
    {
        long r = a - b;

        return (r < 0 ? r + this.modulus : r);
    }

    /**
     * Get the modulus.
     *
     * @return The modulus.
     */

    public final long getModulus()
    {
        return this.modulus;
    }

    /**
     * Set the modulus.
     *
     * @param modulus The modulus.
     */

    public final void setModulus(long modulus)
    {
        if (modulus == MODULUS[0])
        {
            this.inverseModulus = INVERSE_MODULUS[0];
        }
        else if (modulus == MODULUS[1])
        {
            this.inverseModulus = INVERSE_MODULUS[1];
        }
        else if (modulus == MODULUS[2])
        {
            this.inverseModulus = INVERSE_MODULUS[2];
        }
        else
        {
            assert (false);
        }
        this.modulus = modulus;
    }

    private static final long[] INVERSE_MODULUS = { BigInteger.ONE.shiftLeft(119).divide(BigInteger.valueOf(MODULUS[0])).longValueExact(),
                                                    BigInteger.ONE.shiftLeft(119).divide(BigInteger.valueOf(MODULUS[1])).longValueExact(),
                                                    BigInteger.ONE.shiftLeft(119).divide(BigInteger.valueOf(MODULUS[2])).longValueExact() };

    private long modulus;
    private long inverseModulus;
}
