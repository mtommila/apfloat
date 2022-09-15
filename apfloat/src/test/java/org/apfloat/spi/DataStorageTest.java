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
package org.apfloat.spi;

import org.apfloat.ApfloatRuntimeException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.8.1
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class DataStorageTest
    extends TestCase
{
    private static class DummyDataStorage
        extends DataStorage
    {
        public DummyDataStorage(long size)
        {
            this.size = size;
        }

        @Override
        protected DataStorage implSubsequence(long offset, long length)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void implCopyFrom(DataStorage dataStorage, long size)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        protected long implGetSize()
        {
            return this.size;
        }

        @Override
        protected void implSetSize(long size)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        protected ArrayAccess implGetArray(int mode, long offset, int length)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        protected ArrayAccess implGetArray(int mode, int startColumn, int columns, int rows)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        protected ArrayAccess implGetTransposedArray(int mode, int startColumn, int columns, int rows)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator iterator(int mode, long startPosition, long endPosition)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isCached()
        {
            throw new UnsupportedOperationException();
        }

        private static final long serialVersionUID = 1L;

        private long size;
    }

    private static class DummyArrayAccess
        extends ArrayAccess
    {
        public DummyArrayAccess()
        {
            super(0, 0);
        }

        @Override
        public ArrayAccess subsequence(int offset, int length)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getData()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close()
        {
            throw new UnsupportedOperationException();
        }

        private static final long serialVersionUID = 1L;
    }

    public DataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new DataStorageTest("testGetArray"));
        suite.addTest(new DataStorageTest("testGetArrayBig"));
        suite.addTest(new DataStorageTest("testGetArrayBlockSmall"));
        suite.addTest(new DataStorageTest("testGetArrayBlockBig"));
        suite.addTest(new DataStorageTest("testGetTransposedArrayBlockSmall"));
        suite.addTest(new DataStorageTest("testGetTransposedArrayBlockBig"));

        return suite;
    }

    public static void testGetArray()
    {
        ArrayAccess arrayAccess = new DummyArrayAccess();
        DataStorage dataStorage = new DummyDataStorage(10)
        {
            @Override
            protected ArrayAccess implGetArray(int mode, long offset, int length)
            {
                return arrayAccess;
            }

            private static final long serialVersionUID = 1L;
        };

        assertSame("0 10", arrayAccess, dataStorage.getArray(DataStorage.READ, 0, 10));
        assertSame("1 9", arrayAccess, dataStorage.getArray(DataStorage.READ, 9, 1));

        try
        {
            dataStorage.getArray(DataStorage.READ, 10, 1);
            fail("Too big offset accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, 11);
            fail("Too big array accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, -1, 10);
            fail("Negative offset accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, -1);
            fail("Negative length accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }
    }

    public static void testGetArrayBig()
    {
        ArrayAccess arrayAccess = new DummyArrayAccess();
        DataStorage dataStorage = new DummyDataStorage(Long.MAX_VALUE)
        {
            @Override
            protected ArrayAccess implGetArray(int mode, long offset, int length)
            {
                return arrayAccess;
            }

            private static final long serialVersionUID = 1L;
        };

        assertSame("0 max", arrayAccess, dataStorage.getArray(DataStorage.READ, 0, Integer.MAX_VALUE));
        assertSame("1 max-1", arrayAccess, dataStorage.getArray(DataStorage.READ, Long.MAX_VALUE - 1, 1));

        try
        {
            dataStorage.getArray(DataStorage.READ, Long.MAX_VALUE, 1);
            fail("Too big offset accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }
    }

    public static void testGetArrayBlockSmall()
    {
        ArrayAccess arrayAccess = new DummyArrayAccess();
        DataStorage dataStorage = new DummyDataStorage(100)
        {
            @Override
            protected ArrayAccess implGetArray(int mode, int startColumn, int columns, int rows)
            {
                return arrayAccess;
            }

            private static final long serialVersionUID = 1L;
        };

        assertSame("0 10 10", arrayAccess, dataStorage.getArray(DataStorage.READ, 0, 10, 10));
        assertSame("1 9 10", arrayAccess, dataStorage.getArray(DataStorage.READ, 1, 9, 10));
        assertSame("9 1 10", arrayAccess, dataStorage.getArray(DataStorage.READ, 9, 1, 10));

        try
        {
            dataStorage.getArray(DataStorage.READ, 1, 10, 10);
            fail("Too big start column accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, 11, 10);
            fail("Too many columns accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, 10, 11);
            fail("Too many rows accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, -1, 10, 10);
            fail("Negative start column accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, -1, 10);
            fail("Negative columns accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, 10, -1);
            fail("Negative rows accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }
    }

    public static void testGetArrayBlockBig()
    {
        ArrayAccess arrayAccess = new DummyArrayAccess();
        DataStorage dataStorage = new DummyDataStorage((long) Integer.MAX_VALUE * Integer.MAX_VALUE)
        {
            @Override
            protected ArrayAccess implGetArray(int mode, int startColumn, int columns, int rows)
            {
                return arrayAccess;
            }

            private static final long serialVersionUID = 1L;
        };

        assertSame("0 1 max", arrayAccess, dataStorage.getArray(DataStorage.READ, 0, 1, Integer.MAX_VALUE));
        assertSame("max-1 1 max", arrayAccess, dataStorage.getArray(DataStorage.READ, Integer.MAX_VALUE - 1, 1, Integer.MAX_VALUE));

        try
        {
            dataStorage.getArray(DataStorage.READ, 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
            fail("Too big start column accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, 0x10000, 0x8000);
            fail("Too big array accepted");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK, too big array
        }

        try
        {
            dataStorage.getArray(DataStorage.READ, 0, 2, Integer.MAX_VALUE);
            fail("Too big array 2 accepted");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK, too big array
        }
    }

    public static void testGetTransposedArrayBlockSmall()
    {
        ArrayAccess arrayAccess = new DummyArrayAccess();
        DataStorage dataStorage = new DummyDataStorage(100)
        {
            @Override
            protected ArrayAccess implGetTransposedArray(int mode, int startColumn, int columns, int rows)
            {
                return arrayAccess;
            }

            private static final long serialVersionUID = 1L;
        };

        assertSame("0 10 10", arrayAccess, dataStorage.getTransposedArray(DataStorage.READ, 0, 10, 10));
        assertSame("1 9 10", arrayAccess, dataStorage.getTransposedArray(DataStorage.READ, 1, 9, 10));
        assertSame("9 1 10", arrayAccess, dataStorage.getTransposedArray(DataStorage.READ, 9, 1, 10));

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 1, 10, 10);
            fail("Too big start column accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 0, 11, 10);
            fail("Too many columns accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 0, 10, 11);
            fail("Too many rows accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, -1, 10, 10);
            fail("Negative start column accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 0, -1, 10);
            fail("Negative columns accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 0, 10, -1);
            fail("Negative rows accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }
    }

    public static void testGetTransposedArrayBlockBig()
    {
        ArrayAccess arrayAccess = new DummyArrayAccess();
        DataStorage dataStorage = new DummyDataStorage((long) Integer.MAX_VALUE * Integer.MAX_VALUE)
        {
            @Override
            protected ArrayAccess implGetTransposedArray(int mode, int startColumn, int columns, int rows)
            {
                return arrayAccess;
            }

            private static final long serialVersionUID = 1L;
        };

        assertSame("0 1 max", arrayAccess, dataStorage.getTransposedArray(DataStorage.READ, 0, 1, Integer.MAX_VALUE));
        assertSame("max-1 1 max", arrayAccess, dataStorage.getTransposedArray(DataStorage.READ, Integer.MAX_VALUE - 1, 1, Integer.MAX_VALUE));

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
            fail("Too big start column accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, out of range
        }

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 0, 0x10000, 0x8000);
            fail("Too big array accepted");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK, too big array
        }

        try
        {
            dataStorage.getTransposedArray(DataStorage.READ, 0, 2, Integer.MAX_VALUE);
            fail("Too big array 2 accepted");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK, too big array
        }
    }
}
