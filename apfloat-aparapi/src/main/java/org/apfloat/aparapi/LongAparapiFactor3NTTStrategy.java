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
package org.apfloat.aparapi;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.DataStorage;
import org.apfloat.internal.ApfloatInternalException;
import org.apfloat.internal.Factor3NTTStrategy;

/**
 * Factor-3 NTT implementation for the <code>long</code> element type.<p>
 *
 * @since 1.8.3
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class LongAparapiFactor3NTTStrategy
    extends Factor3NTTStrategy
{
    /**
     * Default constructor.
     */

    public LongAparapiFactor3NTTStrategy()
    {
        super(new ColumnSixStepFNTStrategy(new LongAparapiNTTStepStrategy(), new LongAparapiMatrixStrategy()));
        super.stepStrategy = new LongAparapiFactor3NTTStepStrategy();
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

        LongKernel kernel = LongKernel.getInstance();
        kernel.setExplicit(true);
        kernel.put(arrayAccess.getLongData());
    }

    private void postTransform(DataStorage dataStorage)
    {
        long length = dataStorage.getSize();

        assert (length <= Integer.MAX_VALUE);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 0, (int) length);

        LongKernel kernel = LongKernel.getInstance();
        kernel.get(arrayAccess.getLongData());
        //kernel.cleanUpArrays(); // FIXME needs to be fixed in aparapi
    }
}
