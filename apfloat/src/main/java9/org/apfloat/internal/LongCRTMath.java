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
import static org.apfloat.internal.LongRadixConstants.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;

/**
 * Basic arithmetic for calculating the Chinese Remainder
 * Theorem. Works for the <code>long</code> type.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class LongCRTMath
    extends LongBaseMath
{
    /**
     * Creates a carry-CRT math using the specified radix.
     *
     * @param radix The radix that will be used.
     */

    public LongCRTMath(int radix)
    {
        super(radix);
        this.base = BASE[radix];
        this.inverseBase = 1.0 / BASE[radix];
        this.inverseBaseLong = INVERSE_BASE[radix];
    }

    /**
     * Multiplies two words by one word to produce a result of three words.
     * Most significant word is stored first.
     *
     * @param src Source array, first multiplicand.
     * @param factor Second multiplicand.
     * @param dst Destination array.
     */

    public final void multiply(long[] src, long factor, long[] dst)
    {
        long tmp = src[1] * factor,
             carry = Math.multiplyHigh(src[1], factor) << 64 - MAX_POWER_OF_TWO_BITS | tmp >>> MAX_POWER_OF_TWO_BITS;

        dst[2] = tmp & BASE_MASK;               // = tmp % MAX_POWER_OF_TWO_BASE

        tmp = src[0] * factor;
        long tmp2 = (tmp & BASE_MASK) + carry;
        carry = (Math.multiplyHigh(src[0], factor) << 64 - MAX_POWER_OF_TWO_BITS) + (tmp >>> MAX_POWER_OF_TWO_BITS) + (tmp2 >>> MAX_POWER_OF_TWO_BITS);

        dst[1] = tmp2 & BASE_MASK;              // = tmp % MAX_POWER_OF_TWO_BASE

        dst[0] = carry;
    }

    /**
     * Compares three words. Most significant word is stored first.
     *
     * @param src1 First operand.
     * @param src2 Second operand.
     *
     * @return Less than zero if <code>src1 &lt; src2</code>, greater than zero if <code>src1 &gt; src2</code> and zero if <code>src1 == src2</code>.
     */

    public final long compare(long[] src1, long[] src2)
    {
        long result = src1[0] - src2[0];

        if (result != 0)
        {
            return result;
        }

        result = src1[1] - src2[1];

        if (result != 0)
        {
            return result;
        }

        return src1[2] - src2[2];
    }

    /**
     * Adds three words. Most significant word is stored first.
     *
     * @param src First operand.
     * @param srcDst Second operand, and destination of the operation.
     *
     * @return Overflow carry bit.
     */

    public final long add(long[] src, long[] srcDst)
    {
        long result = srcDst[2] + src[2],
             carry = (result >= MAX_POWER_OF_TWO_BASE ? 1 : 0);
        result = (result >= MAX_POWER_OF_TWO_BASE ? result - MAX_POWER_OF_TWO_BASE : result);

        srcDst[2] = result;

        result = srcDst[1] + src[1] + carry;
        carry = (result >= MAX_POWER_OF_TWO_BASE ? 1 : 0);
        result = (result >= MAX_POWER_OF_TWO_BASE ? result - MAX_POWER_OF_TWO_BASE : result);

        srcDst[1] = result;

        result = srcDst[0] + src[0] + carry;
        carry = (result >= MAX_POWER_OF_TWO_BASE ? 1 : 0);
        result = (result >= MAX_POWER_OF_TWO_BASE ? result - MAX_POWER_OF_TWO_BASE : result);

        srcDst[0] = result;

        return carry;
    }

    /**
     * Subtracts three words. Most significant word is stored first.
     *
     * @param src First operand.
     * @param srcDst Second operand, and destination of the operation.
     */

    public final void subtract(long[] src, long[] srcDst)
    {
        long result = srcDst[2] - src[2],
             carry = (result < 0 ? 1 : 0);
        result = (result < 0 ? result + MAX_POWER_OF_TWO_BASE : result);

        srcDst[2] = result;

        result = srcDst[1] - src[1] - carry;
        carry = (result < 0 ? 1 : 0);
        result = (result < 0 ? result + MAX_POWER_OF_TWO_BASE : result);

        srcDst[1] = result;

        result = srcDst[0] - src[0] - carry;
        // carry = (result < 0 ? 1 : 0);
        result = (result < 0 ? result + MAX_POWER_OF_TWO_BASE : result);

        srcDst[0] = result;
    }

    /**
     * Divides three words by the base to produce two words. Most significant word is stored first.
     *
     * @param srcDst Source and destination of the operation.
     *
     * @return Remainder of the division.
     */

    public final long divide(long[] srcDst)
    {
        long tmp = srcDst[0] << 64 - MAX_POWER_OF_TWO_BITS | srcDst[1] >> MAX_POWER_OF_TWO_BITS,
             result = Math.multiplyHigh(tmp, this.inverseBaseLong),
             carry = (srcDst[0] << MAX_POWER_OF_TWO_BITS | srcDst[1]) - result * this.base;     // = tmp % this.base
        tmp = Math.multiplyHigh(carry, this.inverseBaseLong) >> 64 - 2* (64 - MAX_POWER_OF_TWO_BITS);
        result += tmp;
        carry -= tmp * this.base;

        result += (carry >= this.base ? 1 : 0);
        carry -= (carry >= this.base ? this.base : 0);

        srcDst[0] = 0;
        srcDst[1] = result;

        tmp = carry << 64 - MAX_POWER_OF_TWO_BITS | srcDst[2] >> MAX_POWER_OF_TWO_BITS;
        result = Math.multiplyHigh(tmp, this.inverseBaseLong) + (tmp < 0 ? this.inverseBaseLong : 0);   // Signed multiplication to unsigned
        carry = (carry << MAX_POWER_OF_TWO_BITS | srcDst[2]) - result * this.base;              // = tmp % this.base
        tmp = Math.multiplyHigh(carry, this.inverseBaseLong) >> 64 - 2* (64 - MAX_POWER_OF_TWO_BITS);
        result += tmp;
        carry -= tmp * this.base;

        result += (carry >= this.base ? 1 : 0);
        carry -= (carry >= this.base ? this.base : 0);

        srcDst[2] = result;

        return carry;
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.inverseBaseLong = INVERSE_BASE[radix()];
    }

    private static final long serialVersionUID = 7400961005627736773L;

    private static final long BASE_MASK = (1L << MAX_POWER_OF_TWO_BITS) - 1;
    private static final long[] INVERSE_BASE;

    static
    {
        INVERSE_BASE = new long[Character.MAX_RADIX + 1];
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            INVERSE_BASE[radix] = BigInteger.ONE.shiftLeft(2 * MAX_POWER_OF_TWO_BITS).divide(BigInteger.valueOf(BASE[radix])).longValueExact();
        }
    }

    private long base;
    @SuppressWarnings("unused") // Kept only for serialization compatibility
    private double inverseBase;
    private transient long inverseBaseLong;
}
