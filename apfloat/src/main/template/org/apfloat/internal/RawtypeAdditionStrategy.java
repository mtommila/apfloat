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
