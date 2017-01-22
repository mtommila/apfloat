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

import org.apfloat.*;
import org.apfloat.spi.*;

public abstract class RawtypeConvolutionStrategyTestCase
    extends RawtypeTestCase
    implements RawtypeRadixConstants
{
    protected RawtypeConvolutionStrategyTestCase(String methodName)
    {
        super(methodName);
    }

    protected static DataStorage createDataStorage(rawtype[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * sizeof(rawtype));
        dataStorage.setSize(size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        arrayAccess.close();

        return dataStorage;
    }

    protected static void check(String message, int radix, rawtype[] expected, DataStorage actual)
    {
        ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length);
        assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
        for (int i = 0; i < arrayAccess.getLength(); i++)
        {
            assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + i]);
        }
        arrayAccess.close();
    }
}
