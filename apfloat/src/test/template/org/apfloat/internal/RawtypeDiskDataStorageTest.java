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
package org.apfloat.internal;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeDiskDataStorageTest
    extends RawtypeDataStorageTestCase
{
    private RawtypeDiskDataStorageTest()
    {
    }

    public RawtypeDiskDataStorageTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new RawtypeDiskDataStorageTest().realSuite();

        suite.addTest(new RawtypeDiskDataStorageTest("testGetPartialArray"));
        suite.addTest(new RawtypeDiskDataStorageTest("testGetPartialArrayBig"));
        suite.addTest(new RawtypeDiskDataStorageTest("testGetPartialArrayWide"));
        suite.addTest(new RawtypeDiskDataStorageTest("testGetPartialArrayWideBig"));
        suite.addTest(new RawtypeDiskDataStorageTest("testGetTransposedArray"));
        suite.addTest(new RawtypeDiskDataStorageTest("testGetTransposedArrayBig"));
        suite.addTest(new RawtypeDiskDataStorageTest("testGetTransposedArrayWide"));
        suite.addTest(new RawtypeDiskDataStorageTest("testGetTransposedArrayWideBig"));
        suite.addTest(new RawtypeDiskDataStorageTest("testIsCached"));

        return suite;
    }

    @Override
    public TestCase createTestCase(String methodName)
    {
        return new RawtypeDiskDataStorageTest(methodName);
    }

    @Override
    public DataStorage createDataStorage()
        throws ApfloatRuntimeException
    {
        return new RawtypeDiskDataStorage();
    }

    public static void testGetPartialArray()
    {
        runGetPartialArray(64, 128, 8);
    }

    public static void testGetPartialArrayBig()
    {
        runGetPartialArray(1024, 2048, 32);
    }

    public static void testGetPartialArrayWide()
    {
        runGetPartialArray(8, 64, 16);
    }

    public static void testGetPartialArrayWideBig()
    {
        runGetPartialArray(32, 2048, 128);
    }

    public static void testGetTransposedArray()
    {
        runGetTransposedArray(64, 128, 8);
    }

    public static void testGetTransposedArrayBig()
    {
        runGetTransposedArray(1024, 2048, 32);
    }

    public static void testGetTransposedArrayWide()
    {
        runGetTransposedArray(8, 64, 16);
    }

    public static void testGetTransposedArrayWideBig()
    {
        runGetTransposedArray(32, 2048, 128);
    }

    public static void testIsCached()
    {
        assertFalse(new RawtypeDiskDataStorage().isCached());
    }

    private static void runGetPartialArray(int n1, int n2, int b)
    {
        int size = n1 * n2;
        DataStorage dataStorage = new RawtypeDiskDataStorage();
        dataStorage.setSize(size + 5);
        dataStorage = dataStorage.subsequence(5, size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getRawtypeData()[arrayAccess.getOffset() + i] = (rawtype) (i + 1);
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ_WRITE, 2 * b, b, n1);
        assertEquals("array size", n1 * b, arrayAccess.getLength());
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < b; j++)
            {
                rawtype value = arrayAccess.getRawtypeData()[arrayAccess.getOffset() + b * i + j];
                assertEquals("[" + i + "][" + j + "]", n2 * i + 2 * b + j + 1, (int) value);
                arrayAccess.getRawtypeData()[arrayAccess.getOffset() + b * i + j] = -value;
            }
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < n2; j++)
            {
                rawtype value = arrayAccess.getRawtypeData()[arrayAccess.getOffset() + n2 * i + j],
                        expectedValue = n2 * i + j + 1;
                if (j >= 2 * b && j < 3 * b)
                {
                    assertEquals("[" + i + "][" + j + "]", (int) -expectedValue, (int) value);
                }
                else
                {
                    assertEquals("[" + i + "][" + j + "]", (int) expectedValue, (int) value);
                }
            }
        }
        arrayAccess.close();
    }

    private static void runGetTransposedArray(int n1, int n2, int b)
    {
        int size = n1 * n2;
        DataStorage dataStorage = new RawtypeDiskDataStorage();
        dataStorage.setSize(size + 5);
        dataStorage = dataStorage.subsequence(5, size);

        ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size);
        for (int i = 0; i < size; i++)
        {
            arrayAccess.getRawtypeData()[arrayAccess.getOffset() + i] = (rawtype) (i + 1);
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getTransposedArray(DataStorage.READ_WRITE, 2 * b, b, n1);
        assertEquals("array size", b * n1, arrayAccess.getLength());
        for (int i = 0; i < b; i++)
        {
            for (int j = 0; j < n1; j++)
            {
                rawtype value = arrayAccess.getRawtypeData()[arrayAccess.getOffset() + n1 * i + j];
                assertEquals("[" + i + "][" + j + "]", n2 * j + 2 * b + i + 1, (int) value);
                arrayAccess.getRawtypeData()[arrayAccess.getOffset() + n1 * i + j] = -value;
            }
        }
        arrayAccess.close();

        arrayAccess = dataStorage.getArray(DataStorage.READ, 0, size);
        for (int i = 0; i < n1; i++)
        {
            for (int j = 0; j < n2; j++)
            {
                rawtype value = arrayAccess.getRawtypeData()[arrayAccess.getOffset() + n2 * i + j],
                        expectedValue = n2 * i + j + 1;
                if (j >= 2 * b && j < 3 * b)
                {
                    assertEquals("[" + i + "][" + j + "]", (int) -expectedValue, (int) value);
                }
                else
                {
                    assertEquals("[" + i + "][" + j + "]", (int) expectedValue, (int) value);
                }
            }
        }
        arrayAccess.close();
    }
}
