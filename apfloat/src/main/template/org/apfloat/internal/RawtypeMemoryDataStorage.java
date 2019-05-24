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
import org.apfloat.spi.DataStorage;
import org.apfloat.spi.ArrayAccess;

/**
 * Memory based data storage implementation for the <code>rawtype</code>
 * element type.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeMemoryDataStorage
    extends DataStorage
{
    /**
     * Default constructor.
     */

    public RawtypeMemoryDataStorage()
    {
        this.data = new rawtype[0];
    }

    /**
     * Subsequence constructor.
     *
     * @param rawtypeMemoryDataStorage The originating data storage.
     * @param offset The subsequence starting position.
     * @param length The subsequence length.
     */

    protected RawtypeMemoryDataStorage(RawtypeMemoryDataStorage rawtypeMemoryDataStorage, long offset, long length)
    {
        super(rawtypeMemoryDataStorage, offset, length);
        this.data = rawtypeMemoryDataStorage.data;
    }

    @Override
    public boolean isCached()
    {
        return true;
    }

    @Override
    protected DataStorage implSubsequence(long offset, long length)
        throws ApfloatRuntimeException
    {
        return new RawtypeMemoryDataStorage(this, offset + getOffset(), length);
    }

    @Override
    protected void implCopyFrom(DataStorage dataStorage, long size)
        throws ApfloatRuntimeException
    {
        assert (size > 0);
        assert (!isReadOnly());
        assert (!isSubsequenced());

        if (size > Integer.MAX_VALUE)
        {
            throw new ApfloatInternalException("Size too big for memory array: " + size);
        }

        if (dataStorage == this)
        {
            setSize(size);
            return;
        }

        this.data = new rawtype[(int) size];

        ApfloatContext ctx = ApfloatContext.getContext();
        int readSize = (int) Math.min(size, dataStorage.getSize()),
            position = 0,
            bufferSize = ctx.getBlockSize() / sizeof(rawtype);

        while (readSize > 0)
        {
            int length = (int) Math.min(bufferSize, readSize);

            try (ArrayAccess arrayAccess = dataStorage.getArray(READ, position, length))
            {
                System.arraycopy(arrayAccess.getRawtypeData(), arrayAccess.getOffset(), this.data, position, length);
            }

            readSize -= length;
            position += length;
       }
    }

    @Override
    protected long implGetSize()
    {
        return this.data.length;
    }

    @Override
    protected void implSetSize(long size)
        throws ApfloatRuntimeException
    {
        assert (size > 0);
        assert (!isReadOnly());
        assert (!isSubsequenced());

        if (size == this.data.length)
        {
            return;
        }

        if (size > Integer.MAX_VALUE)
        {
            throw new ApfloatInternalException("Size too big for memory array: " + size);
        }

        int newSize = (int) size;

        rawtype[] newData = new rawtype[newSize];
        System.arraycopy(this.data, 0, newData, 0, Math.min(this.data.length, newSize));
        this.data = newData;
    }

    @Override
    protected ArrayAccess implGetArray(int mode, long offset, int length)
        throws ApfloatRuntimeException
    {
        return new RawtypeMemoryArrayAccess(this.data, (int) (offset + getOffset()), length);
    }

    @Override
    protected ArrayAccess implGetArray(int mode, int startColumn, int columns, int rows)
        throws ApfloatRuntimeException
    {
        throw new ApfloatInternalException("Method not implemented - would be sub-optimal; change the apfloat configuration settings");
    }

    @Override
    protected ArrayAccess implGetTransposedArray(int mode, int startColumn, int columns, int rows)
        throws ApfloatRuntimeException
    {
        throw new ApfloatInternalException("Method not implemented - would be sub-optimal; change the apfloat configuration settings");
    }

    private class ReadWriteIterator
        extends AbstractIterator
    {
        public ReadWriteIterator(long startPosition, long endPosition)
            throws IllegalArgumentException, IllegalStateException, ApfloatRuntimeException
        {
            this(READ_WRITE, startPosition, endPosition);
        }

        protected ReadWriteIterator(int mode, long startPosition, long endPosition)
            throws IllegalArgumentException, IllegalStateException, ApfloatRuntimeException
        {
            super(mode, startPosition, endPosition);

            this.data = RawtypeMemoryDataStorage.this.data;

            this.position = (int) getPosition() + (int) getOffset();
            this.length = (int) getLength();
        }

        @Override
        public boolean hasNext()
        {
            return (this.length > 0);
        }

        @Override
        public void next()
            throws IllegalStateException
        {
            checkLength();
            this.position += getIncrement();
            this.length--;
        }

        @Override
        public rawtype getRawtype()
            throws IllegalStateException
        {
            checkLength();
            return this.data[this.position];
        }

        @Override
        public void setRawtype(rawtype value)
            throws IllegalStateException
        {
            checkLength();
            this.data[this.position] = value;
        }

        @Override
        public <T> T get(Class<T> type)
            throws UnsupportedOperationException, IllegalStateException
        {
            if (!(type.equals(RawType.TYPE)))
            {
                throw new UnsupportedOperationException("Unsupported data type " + type.getCanonicalName() + ", the only supported type is rawtype");
            }
            @SuppressWarnings("unchecked")
            T value = (T) (RawType) getRawtype();
            return value;
        }

        @Override
        public <T> void set(Class<T> type, T value)
            throws UnsupportedOperationException, IllegalArgumentException, IllegalStateException
        {
            if (!(type.equals(RawType.TYPE)))
            {
                throw new UnsupportedOperationException("Unsupported data type " + type.getCanonicalName() + ", the only supported type is rawtype");
            }
            if (!(value instanceof RawType))
            {
                throw new IllegalArgumentException("Unsupported value type " + value.getClass().getCanonicalName() + ", the only supported type is RawType");
            }
            setRawtype((RawType) value);
        }

        @Override
        protected void checkLength()
            throws IllegalStateException
        {
            if (this.length == 0)
            {
                throw new IllegalStateException("At the end of iterator");
            }
        }

        private static final long serialVersionUID = ${org.apfloat.internal.RawtypeMemoryDataStorage$ReadWriteIterator};

        private rawtype[] data;
        private int position,
                    length;
    }

    private class ReadOnlyIterator
        extends ReadWriteIterator
    {
        public ReadOnlyIterator(long startPosition, long endPosition)
            throws IllegalArgumentException, IllegalStateException, ApfloatRuntimeException
        {
            super(READ, startPosition, endPosition);
        }

        @Override
        public void setRawtype(rawtype value)
            throws IllegalStateException
        {
            throw new IllegalStateException("Not a writable iterator");
        }

        private static final long serialVersionUID = ${org.apfloat.internal.RawtypeMemoryDataStorage$ReadOnlyIterator};
    }

    private class WriteOnlyIterator
        extends ReadWriteIterator
    {
        public WriteOnlyIterator(long startPosition, long endPosition)
            throws IllegalArgumentException, IllegalStateException, ApfloatRuntimeException
        {
            super(WRITE, startPosition, endPosition);
        }

        @Override
        public rawtype getRawtype()
            throws IllegalStateException
        {
            throw new IllegalStateException("Not a readable iterator");
        }

        private static final long serialVersionUID = ${org.apfloat.internal.RawtypeMemoryDataStorage$WriteOnlyIterator};
    }

    @Override
    public Iterator iterator(int mode, long startPosition, long endPosition)
        throws IllegalArgumentException, IllegalStateException, ApfloatRuntimeException
    {
        Iterator iterator;
        switch (mode & READ_WRITE)
        {
            case READ:
                iterator = new ReadOnlyIterator(startPosition, endPosition);
                break;
            case WRITE:
                iterator = new WriteOnlyIterator(startPosition, endPosition);
                break;
            case READ_WRITE:
                iterator = new ReadWriteIterator(startPosition, endPosition);
                break;
            default:
                throw new IllegalArgumentException("Illegal mode: " + mode);
        }
        return iterator;
    }

    private static final long serialVersionUID = ${org.apfloat.internal.RawtypeMemoryDataStorage.serialVersionUID};

    private rawtype[] data;
}
