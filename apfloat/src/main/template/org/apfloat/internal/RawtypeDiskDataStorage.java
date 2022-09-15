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

import java.nio.ByteBuffer;
import java.nio.RawtypeBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.DataStorage;
import org.apfloat.spi.ArrayAccess;

/**
 * Disk-based data storage for the <code>rawtype</code> element type.
 *
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class RawtypeDiskDataStorage
    extends DiskDataStorage
{
    /**
     * Default constructor.
     */

    public RawtypeDiskDataStorage()
        throws ApfloatRuntimeException
    {
    }

    /**
     * Subsequence constructor.
     *
     * @param rawtypeDiskDataStorage The originating data storage.
     * @param offset The subsequence starting position.
     * @param length The subsequence length.
     */

    protected RawtypeDiskDataStorage(RawtypeDiskDataStorage rawtypeDiskDataStorage, long offset, long length)
    {
        super(rawtypeDiskDataStorage, offset, length);
    }

    @Override
    protected DataStorage implSubsequence(long offset, long length)
        throws ApfloatRuntimeException
    {
        return new RawtypeDiskDataStorage(this, offset + getOffset(), length);
    }

    private class RawtypeDiskArrayAccess
        extends RawtypeMemoryArrayAccess
    {
        // fileOffset is absolute position in file
        public RawtypeDiskArrayAccess(int mode, long fileOffset, int length)
            throws ApfloatRuntimeException
        {
            super(new rawtype[length], 0, length);
            this.mode = mode;
            this.fileOffset = fileOffset;

            if ((mode & READ) != 0)
            {
                rawtype[] array = getRawtypeData();
                WritableByteChannel out = new WritableByteChannel()
                {
                    @Override
                    public int write(ByteBuffer buffer)
                    {
                        RawtypeBuffer src = buffer.asRawtypeBuffer();
                        int readLength = src.remaining();

                        src.get(array, this.readPosition, readLength);

                        this.readPosition += readLength;
                        buffer.position(buffer.position() + readLength * sizeof(rawtype));

                        return readLength * sizeof(rawtype);
                    }

                    @Override public void close() {}
                    @Override public boolean isOpen() { return true; }

                    private int readPosition = 0;
                };

                transferTo(out, fileOffset * sizeof(rawtype), (long) length * sizeof(rawtype));
            }
        }

        @Override
        public void close()
            throws ApfloatRuntimeException
        {
            if ((this.mode & WRITE) != 0 && getData() != null)
            {
                rawtype[] array = getRawtypeData();
                ReadableByteChannel in = new ReadableByteChannel()
                {
                    @Override
                    public int read(ByteBuffer buffer)
                    {
                        RawtypeBuffer dst = buffer.asRawtypeBuffer();
                        int writeLength = dst.remaining();

                        dst.put(array, this.writePosition, writeLength);

                        this.writePosition += writeLength;
                        buffer.position(buffer.position() + writeLength * sizeof(rawtype));

                        return writeLength * sizeof(rawtype);
                    }

                    @Override public void close() {}
                    @Override public boolean isOpen() { return true; }

                    private int writePosition = 0;
                };

                transferFrom(in, this.fileOffset * sizeof(rawtype), (long) array.length * sizeof(rawtype));
            }

            super.close();
        }

        private static final long serialVersionUID = ${org.apfloat.internal.RawtypeDiskDataStorage$RawtypeDiskArrayAccess.serialVersionUID};

        private int mode;
        private long fileOffset;
    }

    @Override
    protected ArrayAccess implGetArray(int mode, long offset, int length)
        throws ApfloatRuntimeException
    {
        return new RawtypeDiskArrayAccess(mode, getOffset() + offset, length);
    }

    @Override
    protected ArrayAccess createArrayAccess(int mode, int startColumn, int columns, int rows)
    {
        return new MemoryArrayAccess(mode, new rawtype[columns * rows], startColumn, columns, rows);
    }

    @Override
    protected ArrayAccess createTransposedArrayAccess(int mode, int startColumn, int columns, int rows)
    {
        return new TransposedMemoryArrayAccess(mode, new rawtype[columns * rows], startColumn, columns, rows);
    }

    private class MemoryArrayAccess
        extends RawtypeMemoryArrayAccess
    {
        public MemoryArrayAccess(int mode, rawtype[] data, int startColumn, int columns, int rows)
        {
            super(data, 0, data.length);
            this.mode = mode;
            this.startColumn = startColumn;
            this.columns = columns;
            this.rows = rows;
        }

        @Override
        public void close()
            throws ApfloatRuntimeException
        {
            if ((this.mode & WRITE) != 0 && getData() != null)
            {
                setArray(this, this.startColumn, this.columns, this.rows);
            }
            super.close();
        }

        private static final long serialVersionUID = ${org.apfloat.internal.RawtypeDiskDataStorage$MemoryArrayAccess.serialVersionUID};

        private int mode,
                    startColumn,
                    columns,
                    rows;
    }

    private class TransposedMemoryArrayAccess
        extends RawtypeMemoryArrayAccess
    {
        public TransposedMemoryArrayAccess(int mode, rawtype[] data, int startColumn, int columns, int rows)
        {
            super(data, 0, data.length);
            this.mode = mode;
            this.startColumn = startColumn;
            this.columns = columns;
            this.rows = rows;
        }

        @Override
        public void close()
            throws ApfloatRuntimeException
        {
            if ((this.mode & WRITE) != 0 && getData() != null)
            {
                setTransposedArray(this, this.startColumn, this.columns, this.rows);
            }
            super.close();
        }

        private static final long serialVersionUID = ${org.apfloat.internal.RawtypeDiskDataStorage$TransposedMemoryArrayAccess.serialVersionUID};

        private int mode,
                    startColumn,
                    columns,
                    rows;
    }

    private class BlockIterator
        extends AbstractIterator
    {
        public BlockIterator(int mode, long startPosition, long endPosition)
            throws IllegalArgumentException, IllegalStateException, ApfloatRuntimeException
        {
            super(mode, startPosition, endPosition);
            this.arrayAccess = null;
            this.remaining = 0;
        }

        @Override
        public void next()
            throws IllegalStateException, ApfloatRuntimeException
        {
            checkLength();

            assert (this.remaining > 0);

            checkAvailable();

            this.offset += getIncrement();
            this.remaining--;

            if (this.remaining == 0)
            {
                close();
            }

            super.next();
        }

        @Override
        public rawtype getRawtype()
            throws IllegalStateException, ApfloatRuntimeException
        {
            checkGet();
            checkAvailable();
            return this.data[this.offset];
        }

        @Override
        public void setRawtype(rawtype value)
            throws IllegalStateException, ApfloatRuntimeException
        {
            checkSet();
            checkAvailable();
            this.data[this.offset] = value;
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

        /**
         * Closes the iterator. This needs to be called only if the
         * iterator is not iterated to the end.
         */

        @Override
        public void close()
            throws ApfloatRuntimeException
        {
            if (this.arrayAccess != null)
            {
                this.data = null;
                this.arrayAccess.close();
                this.arrayAccess = null;
            }
        }

        private void checkAvailable()
            throws ApfloatRuntimeException
        {
            if (this.arrayAccess == null)
            {
                boolean isForward = (getIncrement() > 0);
                int length = (int) Math.min(getLength(), getBlockSize() / sizeof(rawtype));
                long offset = (isForward ? getPosition() : getPosition() - length + 1);

                this.arrayAccess = getArray(getMode(), offset, length);
                this.data = this.arrayAccess.getRawtypeData();
                this.offset = this.arrayAccess.getOffset() + (isForward ? 0 : length - 1);
                this.remaining = length;
            }
        }

        private static final long serialVersionUID = ${org.apfloat.internal.RawtypeDiskDataStorage$BlockIterator.serialVersionUID};

        private ArrayAccess arrayAccess;
        private rawtype[] data;
        private int offset,
                    remaining;
    }

    @Override
    public Iterator iterator(int mode, long startPosition, long endPosition)
        throws IllegalArgumentException, IllegalStateException, ApfloatRuntimeException
    {
        if ((mode & READ_WRITE) == 0)
        {
            throw new IllegalArgumentException("Illegal mode: " + mode);
        }
        return new BlockIterator(mode, startPosition, endPosition);
    }

    @Override
    protected int getUnitSize()
    {
        return sizeof(rawtype);
    }

    private static final long serialVersionUID = ${org.apfloat.internal.RawtypeDiskDataStorage.serialVersionUID};
}
