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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigInteger;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.DataStorage;

import static org.apfloat.internal.LongModConstants.*;
import static org.apfloat.internal.LongRadixConstants.*;

/**
 * Mathematical operations on numbers in a base.
 * Implementation for the <code>long</code> type.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class LongBaseMath
    implements Serializable
{
    /**
     * Creates a base math using the specified radix.
     *
     * @param radix The radix that will be used.
     */

    public LongBaseMath(int radix)
    {
        this.radix = radix;
        this.inverseBase = 1.0 / BASE[radix];
        this.inverseBaseLong = INVERSE_BASE[radix];
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

    public long baseAdd(DataStorage.Iterator src1, DataStorage.Iterator src2, long carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 == null || src1 != src2);

        boolean sameDst = (src1 == dst || src2 == dst);
        long base = BASE[this.radix];

        for (long i = 0; i < size; i++)
        {
            long result = (src1 == null ? 0 : src1.getLong()) + carry +
                          (src2 == null ? 0 : src2.getLong());

            carry = (result >= base ? 1 : 0);
            result -= (result >= base ? base : 0);

            dst.setLong(result);

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

    public long baseSubtract(DataStorage.Iterator src1, DataStorage.Iterator src2, long carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 == null || src1 != src2);
        assert (src2 != dst);

        long base = BASE[this.radix];

        for (long i = 0; i < size; i++)
        {
            long result = (src1 == null ? 0 : src1.getLong()) - carry -
                          (src2 == null ? 0 : src2.getLong());

            carry = (result < 0 ? 1 : 0);
            result += (result < 0 ? base : 0);

            dst.setLong(result);

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

    public long baseMultiplyAdd(DataStorage.Iterator src1, DataStorage.Iterator src2, long src3, long carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 != src2);
        assert (src1 != dst);

        long base = BASE[this.radix];

        for (long i = 0; i < size; i++)
        {
            long a = src1.getLong(),
                 b = src3;

            carry += (src2 == null ? 0 : src2.getLong());
            final long SHIFT = 64 - 2 * (64 - MAX_POWER_OF_TWO_BITS),
                       MASK = (1L << SHIFT) - 1;
            long tmp = a * b,
                 tmp2 = ((tmp & MASK) + (carry & MASK) >>> SHIFT) + (tmp >>> SHIFT) + (carry >>> SHIFT);
            tmp += carry;
            tmp2 += Math.multiplyHigh(a, b) << 2 * (64 - MAX_POWER_OF_TWO_BITS);
            carry = Math.multiplyHigh(tmp2, this.inverseBaseLong);
            tmp -= carry * base;

            carry += (tmp >= base ? 1 : 0);
            tmp -= (tmp >= base ? base : 0);

            dst.setLong(tmp);                           // = a * b % base

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

    public long baseDivide(DataStorage.Iterator src1, long src2, long carry, DataStorage.Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        assert (src1 != dst);

        long base = BASE[this.radix];

        double inverseDivisor = 1.0 / src2;

        for (long i = 0; i < size; i++)
        {
            long a = (src1 == null ? 0 : src1.getLong()),
                 tmp = carry * base + a,
                 result = (long) (((double) carry * (double) base + (double) a) * inverseDivisor);
            carry = tmp - result * src2;
            int tmp2 = (int) ((double) carry * inverseDivisor);
            result += tmp2;
            carry -= tmp2 * src2;

            result += (carry >= src2 ? 1 : 0);
            carry -= (carry >= src2 ? src2 : 0);
            result += (carry >= src2 ? 1 : 0);
            carry -= (carry >= src2 ? src2 : 0);

            result -= (carry < 0 ? 1 : 0);
            carry += (carry < 0 ? src2 : 0);
            result -= (carry < 0 ? 1 : 0);
            carry += (carry < 0 ? src2 : 0);

            dst.setLong(result);                        // = carry * base % src2

            if (src1 != null) src1.next();
            dst.next();
        }

        return carry;
    }

    int radix()
    {
        return this.radix;
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        this.inverseBaseLong = INVERSE_BASE[this.radix];
    }

    private static final long serialVersionUID = -6469225916787810664L;

    private static final long[] INVERSE_BASE;

    static
    {
        INVERSE_BASE = new long[Character.MAX_RADIX + 1];
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            INVERSE_BASE[radix] = BigInteger.ONE.shiftLeft(2 * MAX_POWER_OF_TWO_BITS).divide(BigInteger.valueOf(BASE[radix])).longValueExact();
        }
    }

    private int radix;
    @SuppressWarnings("unused") // Kept only for serialization compatibility
    private double inverseBase;
    private transient long inverseBaseLong;
}
