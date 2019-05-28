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

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.NTTStrategy;
import org.apfloat.spi.DataStorage;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.Util;
import static org.apfloat.internal.RawtypeModConstants.*;

/**
 * Fast Number Theoretic Transform strategy that uses lookup tables
 * for powers of n:th root of unity and permutation indexes.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeTableFNTStrategy
    extends RawtypeTableFNT
    implements NTTStrategy
{
    /**
     * Default constructor.
     */

    public RawtypeTableFNTStrategy()
    {
    }

    @Override
    public void transform(DataStorage dataStorage, int modulus)
        throws ApfloatRuntimeException
    {
        long length = dataStorage.getSize();            // Transform length n

        if (length > MAX_TRANSFORM_LENGTH)
        {
            throw new TransformLengthExceededException("Maximum transform length exceeded: " + length + " > " + MAX_TRANSFORM_LENGTH);
        }
        else if (length > Integer.MAX_VALUE)
        {
            throw new ApfloatInternalException("Maximum array length exceeded: " + length);
        }

        setModulus(MODULUS[modulus]);                                       // Modulus
        rawtype[] wTable = RawtypeWTables.getWTable(modulus, (int) length);

        try (ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 0, (int) length))
        {
            tableFNT(arrayAccess, wTable, null);
        }
    }

    @Override
    public void inverseTransform(DataStorage dataStorage, int modulus, long totalTransformLength)
        throws ApfloatRuntimeException
    {
        long length = dataStorage.getSize();            // Transform length n

        if (Math.max(length, totalTransformLength) > MAX_TRANSFORM_LENGTH)
        {
            throw new TransformLengthExceededException("Maximum transform length exceeded: " + Math.max(length, totalTransformLength) + " > " + MAX_TRANSFORM_LENGTH);
        }
        else if (length > Integer.MAX_VALUE)
        {
            throw new ApfloatInternalException("Maximum array length exceeded: " + length);
        }

        setModulus(MODULUS[modulus]);                                       // Modulus
        rawtype[] wTable = RawtypeWTables.getInverseWTable(modulus, (int) length);

        try (ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 0, (int) length))
        {
            inverseTableFNT(arrayAccess, wTable, null);

            divideElements(arrayAccess, (rawtype) totalTransformLength);
        }
    }

    @Override
    public long getTransformLength(long size)
    {
        return Util.round2up(size);
    }

    private void divideElements(ArrayAccess arrayAccess, rawtype divisor)
        throws ApfloatRuntimeException
    {
        rawtype inverseFactor = modDivide((rawtype) 1, divisor);
        rawtype[] data = arrayAccess.getRawtypeData();
        int length = arrayAccess.getLength(),
            offset = arrayAccess.getOffset();

        for (int i = 0; i < length; i++)
        {
            data[i + offset] = modMultiply(data[i + offset], inverseFactor);
        }
    }
}
