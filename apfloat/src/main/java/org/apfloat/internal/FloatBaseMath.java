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

import java.io.Serializable;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.DataStorage;
import static org.apfloat.internal.FloatRadixConstants.*;

/**
 * Mathematical operations on numbers in a base.
 * Implementation for the <code>float</code> type.
 *
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class FloatBaseMath
    implements Serializable
{
    /**
     * Creates a base math using the specified radix.
     *
     * @param radix The radix that will be used.
     */

    public FloatBaseMath(int radix)
    {
        this.radix = radix;
    }

    /**
     * Addition in some base. Adds the data words
     * of <code>src1</code> and <code>src2</code> and stores the result to
     * <code>dst</code>. <code>src2</code> may be <code>null</code>, in
     * which case it is ignored (only the carry is propagated).<p>
     *
     * Essentially calculates <code>dst[i] = src1[i] + src2[i]</code>.
     *
     * @param src1 First source data sequence. Can be <code>null</code>, in which case it's ignored.
     * @param src2 Second source data sequence. Can be <code>null</code>, in which case it's ignored.
     * @param carry Input carry bit. This is added to the first (rightmost) word in the accessed sequence.
     * @param dst Destination data sequence.
     * @param size Number of elements to process.
     *
     * @return Overflow carry bit. Propagated carry bit from the addition of the last (leftmost) word in the accessed sequence.
     */

    public float baseAdd(DataStorage.Iterator src1, DataStorage.Iterator src2, float carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 == null || src1 != src2);

        boolean sameDst = (src1 == dst || src2 == dst);
        float base = BASE[this.radix];

        for (long i = 0; i < size; i++)
        {
            float result = (src1 == null ? 0 : src1.getFloat()) - base + carry + (src2 == null ? 0 : src2.getFloat());

            carry = (result >= 0 ? 1 : 0);
            result += (result >= 0 ? 0 : base);

            dst.setFloat(result);

            if (src1 != null) src1.next();
            if (src2 != null) src2.next();
            if (!sameDst) dst.next();
        }

        return carry;
    }

    /**
     * Subtraction in some base. Subtracts the data words
     * of <code>src1</code> and <code>src2</code> and stores the result to
     * <code>dst</code>. <code>src1</code> and <code>src2</code> may be
     * <code>null</code>, in which case they are ignored (the values are assumed
     * to be zero and only the carry is propagated).<p>
     *
     * Essentially calculates <code>dst[i] = src1[i] - src2[i]</code>.
     *
     * @param src1 First source data sequence. Can be <code>null</code>, in which case the input values are assumed to be zero.
     * @param src2 Second source data sequence. Can be <code>null</code>, in which case it's ignored, or can be the same as <code>dst</code>.
     * @param carry Input carry bit. This is subtracted from the first (rightmost) word in the accessed sequence.
     * @param dst Destination data sequence.
     * @param size Number of elements to process.
     *
     * @return Overflow carry bit. Propagated carry bit from the subtraction of the last (leftmost) word in the accessed sequence. The value is <code>1</code> if the carry is set, and <code>0</code> otherwise.
     */

    public float baseSubtract(DataStorage.Iterator src1, DataStorage.Iterator src2, float carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 == null || src1 != src2);
        assert (src2 != dst);

        float base = BASE[this.radix];

        for (long i = 0; i < size; i++)
        {
            float result = (src1 == null ? 0 : src1.getFloat()) - carry -
                           (src2 == null ? 0 : src2.getFloat());

            carry = (result < 0 ? 1 : 0);
            result += (result < 0 ? base : 0);

            dst.setFloat(result);

            if (src1 != null && src1 != dst) src1.next();
            if (src2 != null) src2.next();
            dst.next();
        }

        return carry;
    }

    /**
     * Multiplication and addition in some base. Multiplies the data words
     * of <code>src1</code> by <code>src3</code> and adds the result to the
     * words in <code>src2</code>, and stores the result to <code>dst</code>.
     * <code>src2</code> may be <code>null</code>, in which case it is ignored
     * (the values are assumed to be zero).<p>
     *
     * Assumes that the result from the addition doesn't overflow the upper
     * result word (to larger than the base). This is the case e.g. when using
     * this method to perform an arbitrary precision multiplication.<p>
     *
     * Essentially calculates <code>dst[i] = src1[i] * src3 + src2[i]</code>.
     *
     * @param src1 First source data sequence.
     * @param src2 Second source data sequence. Can be <code>null</code>, in which case it's ignored, or can be the same as <code>dst</code>.
     * @param src3 Multiplicand. All elements of <code>src1</code> are multiplied by this value.
     * @param carry Input carry word. This is added to the first (rightmost) word in the accessed sequence.
     * @param dst Destination data sequence.
     * @param size Number of elements to process.
     *
     * @return Overflow carry word. Propagated carry word from the multiplication and addition of the last (leftmost) word in the accessed sequence.
     */

    public float baseMultiplyAdd(DataStorage.Iterator src1, DataStorage.Iterator src2, float src3, float carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 != src2);
        assert (src1 != dst);

        double base = BASE[this.radix];

        for (long i = 0; i < size; i++)
        {
            double tmp = (double) src1.getFloat() * (double) src3 +
                         (double) (src2 == null ? 0 : src2.getFloat()) + (double) carry;

            carry = (float) (int) (tmp / base);

            dst.setFloat((float) (tmp - (double) carry * base));        // = tmp % base

            src1.next();
            if (src2 != null && src2 != dst) src2.next();
            dst.next();
        }

        return carry;
    }

    /**
     * Division in some base. Divides the data words
     * of <code>src1</code> by <code>src2</code> and stores the result to
     * <code>dst</code>. <code>src1</code> may be <code>null</code>,
     * in which case it is ignored (the values are assumed to be
     * zero and only the carry division is propagated).<p>
     *
     * Essentially calculates <code>dst[i] = src1[i] / src2</code>.
     *
     * @param src1 First source data sequence. Can be <code>null</code>, in which case the input values are assumed to be zero.
     * @param src2 Divisor. All elements of <code>src1</code> are divided by this value.
     * @param carry Input carry word. Used as the upper word for the division of the first input element. This should be the remainder word returned from the previous block processed.
     * @param dst Destination data sequence.
     * @param size Number of elements to process.
     *
     * @return Remainder word of the propagated division of the last (rightmost) word in the accessed sequence.
     */

    public float baseDivide(DataStorage.Iterator src1, float src2, float carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 != dst);

        double base = BASE[this.radix],
               divisor = src2;

        for (long i = 0; i < size; i++)
        {
            double tmp = (double) carry * base +
                         (double) (src1 == null ? 0 : src1.getFloat());
            float result = (float) (int) (tmp / divisor);

            carry = (float) (tmp - (double) result * divisor);          // = tmp % src2

            dst.setFloat(result);

            if (src1 != null) src1.next();
            dst.next();
        }

        return carry;
    }

    private static final long serialVersionUID = -2321698097908304307L;

    private int radix;
}
