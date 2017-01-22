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

    public RawType add(Iterator src1, Iterator src2, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseAdd(src1, src2, carry, dst, size);
    }

    public RawType subtract(Iterator src1, Iterator src2, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseSubtract(src1, src2, carry, dst, size);
    }

    public RawType multiplyAdd(Iterator src1, Iterator src2, RawType src3, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseMultiplyAdd(src1, src2, src3, carry, dst, size);
    }

    public RawType divide(Iterator src1, RawType src2, RawType carry, Iterator dst, long size)
        throws ApfloatRuntimeException
    {
        return baseDivide(src1, src2, carry, dst, size);
    }

    public RawType zero()
    {
        return (rawtype) 0;
    }

    private static final long serialVersionUID = ${org.apfloat.internal.RawtypeAdditionStrategy.serialVersionUID};
}
