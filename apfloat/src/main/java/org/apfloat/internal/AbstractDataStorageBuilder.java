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
import org.apfloat.spi.DataStorageBuilder;
import org.apfloat.spi.DataStorage;

/**
 * Abstract base class for a data storage creation strategy.
 * Depending on the implementation-specific element size, the
 * requested data length and threshold values configured in the
 * current {@link ApfloatContext}, different types of data storages
 * are created.
 *
 * @since 1.7.0
 * @version 1.8.2
 * @author Mikko Tommila
 */

public abstract class AbstractDataStorageBuilder
    implements DataStorageBuilder
{
    /**
     * Subclass constructor.
     */

    protected AbstractDataStorageBuilder()
    {
    }

    @Override
    public DataStorage createDataStorage(long size)
        throws ApfloatRuntimeException
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        // Sizes are in bytes
        if (size <= ctx.getMemoryThreshold() && size <= getMaxCachedSize())
        {
            return createCachedDataStorage();
        }
        else
        {
            return createNonCachedDataStorage();
        }
    }

    @Override
    public DataStorage createCachedDataStorage(long size)
        throws ApfloatRuntimeException
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        // Sizes are in bytes
        if (size <= ctx.getMaxMemoryBlockSize() && size <= getMaxCachedSize())
        {
            // Use memory data storage if it can fit in memory
            return createCachedDataStorage();
        }
        else
        {
            // If it can't fit in memory then still have to use disk data storage
            return createNonCachedDataStorage();
        }
    }

    @Override
    public DataStorage createDataStorage(DataStorage dataStorage)
        throws ApfloatRuntimeException
    {
        if (isCached(dataStorage))
        {
            long size = dataStorage.getSize();
            ApfloatContext ctx = ApfloatContext.getContext();

            // Sizes are in bytes
            if (size > ctx.getMemoryThreshold())
            {
                // If it is a memory data storage and should be moved to disk then do so
                DataStorage tmp = createNonCachedDataStorage();
                tmp.copyFrom(dataStorage);
                dataStorage = tmp;
            }
        }
        return dataStorage;
    }

    /**
     * Get the maximum cached data storage size.
     *
     * @return The maximum cached data storage size.
     */

    protected abstract long getMaxCachedSize();

    /**
     * Create a cached data storage.
     *
     * @return A new cached data storage.
     */

    protected abstract DataStorage createCachedDataStorage()
        throws ApfloatRuntimeException;

    /**
     * Create a non-cached data storage.
     *
     * @return A new non-cached data storage.
     */

    protected abstract DataStorage createNonCachedDataStorage()
        throws ApfloatRuntimeException;

    /**
     * Test if the data storage is of cached type.
     *
     * @param dataStorage The data storage.
     *
     * @return If the data storage is cached.
     */

    protected abstract boolean isCached(DataStorage dataStorage)
        throws ApfloatRuntimeException;
}
