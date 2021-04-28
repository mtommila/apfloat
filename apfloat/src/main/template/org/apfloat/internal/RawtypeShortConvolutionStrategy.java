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
