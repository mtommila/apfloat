/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
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

        try (ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size))
        {
            System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        }

        return dataStorage;
    }

    protected static void check(String message, int radix, rawtype[] expected, DataStorage actual)
    {
        try (ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length))
        {
            assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
            for (int i = 0; i < arrayAccess.getLength(); i++)
            {
                assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + i]);
            }
        }
    }
}
