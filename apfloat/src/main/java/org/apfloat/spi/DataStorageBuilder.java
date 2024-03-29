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
package org.apfloat.spi;

import org.apfloat.ApfloatRuntimeException;

/**
 * Interface for determining a suitable storage
 * type for data of some expected size. The factory method
 * pattern is used for creating the data storages.<p>
 *
 * The storage type can be different based on the size of the
 * data. For example, it may be beneficial to store small amounts
 * of data always in memory, for small overhead in access times,
 * and to store larger objects on disk files, to avoid running
 * out of memory.<p>
 *
 * Further, an implementing class may provide data storage objects
 * that store data in disk files, for Java client applications, or
 * e.g. in a relational database, for an EJB server environment
 * where files are not allowed to be used.
 *
 * @see DataStorage
 *
 * @version 1.5.1
 * @author Mikko Tommila
 */

public interface DataStorageBuilder
{
    /**
     * Get an appropriate type of data storage for the requested size of data.<p>
     *
     * Note that the returned data storage object is not set to have the
     * requested size, so the client should call the object's {@link DataStorage#setSize(long)}
     * method before storing data to it.
     *
     * @param size The size of data to be stored in the storage, in bytes.
     *
     * @return An empty <code>DataStorage</code> object of an appropriate type for storing <code>size</code> bytes of data.
     */

    public DataStorage createDataStorage(long size)
        throws ApfloatRuntimeException;

    /**
     * Get a data storage that is cached in memory, if possible, for the requested size of data.<p>
     *
     * Note that the returned data storage object is not set to have the
     * requested size, so the client should call the object's {@link DataStorage#setSize(long)}
     * method before storing data to it.
     *
     * @param size The size of data to be stored in the storage, in bytes.
     *
     * @return An empty <code>DataStorage</code> object of an appropriate type for storing <code>size</code> bytes of data, cached if possible.
     *
     * @since 1.5.1
     */

    public DataStorage createCachedDataStorage(long size)
        throws ApfloatRuntimeException;

    /**
     * Convert cached data storage to the appropriate normal data storage type.<p>
     *
     * If the data storage already has the appropriate type for its size, the data
     * storage may be returned unchanged. The argument data storage does not necessarily
     * have to be created with the {@link #createCachedDataStorage(long)} method, it can
     * be created as well with the {@link #createDataStorage(long)} method.<p>
     *
     * If the given data storage does not have the appropriate type for its size, then
     * a new data storage of the appropriate type is created and the data is copied to it.<p>
     *
     * @param dataStorage The data storage to be converted, if necessary.
     *
     * @return A <code>DataStorage</code> that can be the original data storage or a copy of it, with the appropriate type.
     *
     * @since 1.5.1
     */

    public DataStorage createDataStorage(DataStorage dataStorage)
        throws ApfloatRuntimeException;
}
