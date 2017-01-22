package org.apfloat.internal;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.DataStorage;

/**
 * Default data storage creation strategy for the <code>rawtype</code> data type.
 *
 * @see RawtypeMemoryDataStorage
 * @see RawtypeDiskDataStorage
 *
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class RawtypeDataStorageBuilder
    extends AbstractDataStorageBuilder
{
    /**
     * Default constructor.
     */

    public RawtypeDataStorageBuilder()
    {
    }

    protected long getMaxCachedSize()
    {
        return (long) sizeof(rawtype) * Integer.MAX_VALUE;
    }

    protected DataStorage createCachedDataStorage()
        throws ApfloatRuntimeException
    {
        return new RawtypeMemoryDataStorage();
    }

    protected DataStorage createNonCachedDataStorage()
        throws ApfloatRuntimeException
    {
        return new RawtypeDiskDataStorage();
    }

    protected boolean isCached(DataStorage dataStorage)
        throws ApfloatRuntimeException
    {
        return (dataStorage instanceof RawtypeMemoryDataStorage);
    }
}
