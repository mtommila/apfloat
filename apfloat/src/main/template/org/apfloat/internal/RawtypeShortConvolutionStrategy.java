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

import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ConvolutionStrategy;
import org.apfloat.spi.DataStorageBuilder;
import org.apfloat.spi.DataStorage;
import org.apfloat.spi.ArrayAccess;

/**
 * Short convolution strategy.
 * Performs a simple multiplication when the size of one operand is 1.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeShortConvolutionStrategy
    extends RawtypeBaseMath
    implements ConvolutionStrategy
{
    // Implementation notes:
    // - Assumes that the operands have been already truncated to match resultSize (the resultSize argument is ignored)
    // - This class shouldn't be converted to a single class using generics because the performance impact is noticeable

    /**
     * Creates a convolution strategy using the specified radix.
     *
     * @param radix The radix that will be used.
     */

    public RawtypeShortConvolutionStrategy(int radix)
    {
        super(radix);
    }

    @Override
    public DataStorage convolute(DataStorage x, DataStorage y, long resultSize)
        throws ApfloatRuntimeException
    {
        DataStorage shortStorage, longStorage, resultStorage;

        if (x.getSize() > 1)
        {
            shortStorage = y;
            longStorage = x;
        }
        else
        {
            shortStorage = x;
            longStorage = y;
        }

        assert (shortStorage.getSize() == 1);

        long size = longStorage.getSize() + 1;

        rawtype factor;
        try (ArrayAccess arrayAccess = shortStorage.getArray(DataStorage.READ, 0, 1))
        {
            factor = arrayAccess.getRawtypeData()[arrayAccess.getOffset()];
        }

        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        resultStorage = dataStorageBuilder.createDataStorage(size * sizeof(rawtype));
        resultStorage.setSize(size);

        DataStorage.Iterator src = longStorage.iterator(DataStorage.READ, size - 1, 0);
        try (DataStorage.Iterator dst = resultStorage.iterator(DataStorage.WRITE, size, 0))
        {
            rawtype carry = baseMultiplyAdd(src, null, factor, 0, dst, size - 1);
            dst.setRawtype(carry);
        }

        return resultStorage;
    }

    private static final long serialVersionUID = ${org.apfloat.internal.RawtypeShortConvolutionStrategy.serialVersionUID};
}
