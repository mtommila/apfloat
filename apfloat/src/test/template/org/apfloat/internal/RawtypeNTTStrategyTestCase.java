/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * sizeof(rawtype));
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
