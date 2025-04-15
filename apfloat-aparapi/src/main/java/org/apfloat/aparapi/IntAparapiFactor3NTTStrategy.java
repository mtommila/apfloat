/*
 * MIT License
 *
 * Copyright (c) 2002-2025 Mikko Tommila
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
package org.apfloat.aparapi;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.DataStorage;
import org.apfloat.internal.ApfloatInternalException;
import org.apfloat.internal.Factor3NTTStrategy;
import org.apfloat.internal.SixStepFNTStrategy;

/**
 * Factor-3 NTT implementation for the <code>int</code> element type.<p>
 *
 * @since 1.8.3
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class IntAparapiFactor3NTTStrategy
    extends Factor3NTTStrategy
{
    /**
     * Basic constructor.
     * 
     * @param rowOrientation If the data is using row orientation.
     */

    public IntAparapiFactor3NTTStrategy(boolean rowOrientation)
    {
        super(rowOrientation ?
              new SixStepFNTStrategy(new IntAparapiNTTStepStrategy(rowOrientation), new IntAparapiMatrixStrategy()) :
              new ColumnSixStepFNTStrategy(new IntAparapiNTTStepStrategy(rowOrientation), new IntAparapiMatrixStrategy()),
              new IntAparapiFactor3NTTStepStrategy());
    }

    @Override
    public void transform(DataStorage dataStorage, int modulus)
        throws ApfloatRuntimeException
    {
        preTransform(dataStorage);
        super.transform(dataStorage, modulus);
        postTransform(dataStorage);
    }

    @Override
    public void inverseTransform(DataStorage dataStorage, int modulus, long totalTransformLength)
        throws ApfloatRuntimeException
    {
        preTransform(dataStorage);
        super.inverseTransform(dataStorage, modulus, totalTransformLength);
        postTransform(dataStorage);
    }

    private void preTransform(DataStorage dataStorage)
    {
        long length = dataStorage.getSize();

        if (length > Integer.MAX_VALUE)
        {
            throw new ApfloatInternalException("Maximum array length exceeded: " + length);
        }

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 0, (int) length);

        IntKernel kernel = IntKernel.getInstance();
        kernel.setExplicit(true);
        kernel.put(arrayAccess.getIntData());
    }

    private void postTransform(DataStorage dataStorage)
    {
        long length = dataStorage.getSize();

        assert (length <= Integer.MAX_VALUE);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 0, (int) length);

        IntKernel kernel = IntKernel.getInstance();
        kernel.get(arrayAccess.getIntData());
        kernel.cleanUpArrays();
    }
}
