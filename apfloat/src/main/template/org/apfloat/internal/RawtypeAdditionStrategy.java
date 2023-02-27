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

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.AdditionStrategy;
import org.apfloat.spi.DataStorage.Iterator;

/**
 * Basic addition strategy for the <code>rawtype</code> element type.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */
public class RawtypeAdditionStrategy
    extends RawtypeBaseMath
    implements AdditionStrategy<RawType>
{
    /**
     * Creates an addition strategy using the specified radix.
     *
     * @param radix The radix that will be used.
     */

    public RawtypeAdditionStrategy(int radix)
    {
        super(radix);
    }

    @Override
    public RawType add(Iterator src1, Iterator src2, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseAdd(src1, src2, carry, dst, size);
    }

    @Override
    public RawType subtract(Iterator src1, Iterator src2, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseSubtract(src1, src2, carry, dst, size);
    }

    @Override
    public RawType multiplyAdd(Iterator src1, Iterator src2, RawType src3, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseMultiplyAdd(src1, src2, src3, carry, dst, size);
    }

    @Override
    public RawType divide(Iterator src1, RawType src2, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseDivide(src1, src2, carry, dst, size);
    }

    @Override
    public RawType zero()
    {
        return (rawtype) 0;
    }

    private static final long serialVersionUID = ${org.apfloat.internal.RawtypeAdditionStrategy.serialVersionUID};
}
