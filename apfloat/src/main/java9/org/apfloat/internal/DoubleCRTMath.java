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
package org.apfloat.internal;

import static org.apfloat.internal.DoubleModConstants.*;
import static org.apfloat.internal.DoubleRadixConstants.*;

/**
 * Basic arithmetic for calculating the Chinese Remainder
 * Theorem. Works for the <code>double</code> type.
 *
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class DoubleCRTMath
    extends DoubleBaseMath
{
    /**
     * Creates a carry-CRT math using the specified radix.
     *
     * @param radix The radix that will be used.
     */

    public DoubleCRTMath(int radix)
    {
        super(radix);
        this.base = (long) BASE[radix];
        this.inverseBase = 1.0 / BASE[radix];
    }

    /**
     * Multiplies two words by one word to produce a result of three words.
     * Most significant word is stored first.
     *
     * @param src Source array, first multiplicand.
     * @param factor Second multiplicand.
     * @param dst Destination array.
     */

    public final void multiply(double[] src, double factor, double[] dst)
    {
        double carry = Math.floor(src[1] * factor * INVERSE_MAX_POWER_OF_TWO_BASE),
               tmp = Math.fma(src[1], factor, carry * -MAX_POWER_OF_TWO_BASE);
        if (tmp < 0)
        {
            carry--;
            tmp += MAX_POWER_OF_TWO_BASE;
        }

        dst[2] = tmp;

        double c = carry;
        carry = Math.floor(Math.fma(src[0], factor, carry) * INVERSE_MAX_POWER_OF_TWO_BASE);
        tmp = Math.fma(src[0], factor, carry * -MAX_POWER_OF_TWO_BASE) + c;
        if (tmp < 0)
        {
            carry--;
            tmp += MAX_POWER_OF_TWO_BASE;
        }

        dst[1] = tmp;

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

    public final double compare(double[] src1, double[] src2)
    {
        double result = src1[0] - src2[0];

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

    public final double add(double[] src, double[] srcDst)
    {
        double result = srcDst[2] + src[2],
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

    public final void subtract(double[] src, double[] srcDst)
    {
        double result = srcDst[2] - src[2],
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

    public final double divide(double[] srcDst)
    {
        double result = Math.floor(Math.fma(srcDst[0], MAX_POWER_OF_TWO_BASE, srcDst[1]) * this.inverseBase),
               carry = Math.fma(-result, this.base, srcDst[0] * MAX_POWER_OF_TWO_BASE) + srcDst[1];

        if (carry >= this.base)
        {
            carry -= this.base;
            result++;
        }
        if (carry < 0)
        {
            carry += this.base;
            result--;
        }

        srcDst[0] = 0;
        srcDst[1] = result;

        result = Math.floor(Math.fma(carry, MAX_POWER_OF_TWO_BASE, srcDst[2]) * this.inverseBase);
        carry = Math.fma(-result, this.base, carry * MAX_POWER_OF_TWO_BASE) + srcDst[2];

        if (carry >= this.base)
        {
            carry -= this.base;
            result++;
        }
        if (carry < 0)
        {
            carry += this.base;
            result--;
        }

        srcDst[2] = result;

        return carry;
    }

    private static final long serialVersionUID = -8414531999881223922L;

    private static final double INVERSE_MAX_POWER_OF_TWO_BASE = 1.0 / MAX_POWER_OF_TWO_BASE;

    private long base;
    private double inverseBase;
}
