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

import org.apfloat.*;
import org.apfloat.spi.*;

/**
 * @version 1.9.0
 * @author Mikko Tommila
 */

public abstract class RawtypeNTTStrategyTestCase
    extends RawtypeTestCase
    implements RawtypeModConstants
{
    protected RawtypeNTTStrategyTestCase(String methodName)
    {
        super(methodName);
    }

    protected static DataStorage createDataStorage(int size)
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * RawType.BYTES);
        dataStorage.setSize(size);

        try (ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size))
        {
            for (int i = 0; i < size; i++)
            {
                arrayAccess.getRawtypeData()[arrayAccess.getOffset() + i] = (rawtype) (i + 1);
            }
        }

        return dataStorage;
    }

    protected static rawtype[] getPlainArray(DataStorage dataStorage)
    {
        int size = (int) dataStorage.getSize();
        rawtype[] data = new rawtype[size];
        try (ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size))
        {
            System.arraycopy(arrayAccess.getRawtypeData(), arrayAccess.getOffset(), data, 0, size);
        }

        return data;
    }

    protected static rawtype[] ntt(rawtype[] data, int modulus)
    {
        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[modulus]);

        rawtype[] transform = new rawtype[data.length];
        rawtype w = math.getForwardNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (rawtype) 1;

        for (int i = 0; i < data.length; i++)
        {
            rawtype wj = (rawtype) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            wi = math.modMultiply(wi, w);
        }

        return transform;
    }

    protected static rawtype[] inverseNtt(rawtype[] data, int modulus)
    {
        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[modulus]);

        rawtype[] transform = new rawtype[data.length];
        rawtype w = math.getInverseNthRoot(PRIMITIVE_ROOT[modulus], data.length),
                wi = (rawtype) 1;

        for (int i = 0; i < data.length; i++)
        {
            rawtype wj = (rawtype) 1;

            for (int j = 0; j < data.length; j++)
            {
                transform[i] = math.modAdd(transform[i], math.modMultiply(wj, data[j]));
                wj = math.modMultiply(wj, wi);
            }

            transform[i] = math.modDivide(transform[i], (rawtype) data.length);

            wi = math.modMultiply(wi, w);
        }

        return transform;
    }

    protected static void runRoundTrip(NTTStrategy nttStrategy, int size)
    {
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            nttStrategy.transform(dataStorage, modulus);

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getRawtype());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("round-tripped [" + i + "]", i + 6, (long) iterator.getRawtype());
                iterator.next();
            }
        }
    }
}
