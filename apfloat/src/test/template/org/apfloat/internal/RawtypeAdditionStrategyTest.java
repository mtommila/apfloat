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

import junit.framework.TestSuite;

/**
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeAdditionStrategyTest
    extends RawtypeTestCase
    implements RawtypeRadixConstants
{
    public RawtypeAdditionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeAdditionStrategyTest("testAdd"));
        suite.addTest(new RawtypeAdditionStrategyTest("testSubtract"));
        suite.addTest(new RawtypeAdditionStrategyTest("testMultiplyAdd"));
        suite.addTest(new RawtypeAdditionStrategyTest("testDivide"));

        return suite;
    }

    private static DataStorage createDataStorage(rawtype[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * RawType.BYTES);
        dataStorage.setSize(size);

        try (ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size))
        {
            System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        }

        return dataStorage;
    }

    private static void check(String message, rawtype[] expected, DataStorage actual)
    {
        try (ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length))
        {
            assertEquals(message + " length", expected.length, arrayAccess.getLength());
            for (int i = 0; i < arrayAccess.getLength(); i++)
            {
                assertEquals(message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + i]);
            }
        }
    }

    public static void testAdd()
    {
        DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3 }),
                    src2 = createDataStorage(new rawtype[] { (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7 }),
                    dst = createDataStorage(new rawtype[4]);

        RawtypeAdditionStrategy strategy = new RawtypeAdditionStrategy(10);
        rawtype carry = strategy.zero();

        carry = strategy.add(src1.iterator(DataStorage.READ, 0, 4),
                             src2.iterator(DataStorage.READ, 0, 4),
                             carry,
                             dst.iterator(DataStorage.WRITE, 0, 4),
                             4);

        assertEquals("carry", strategy.zero(), (RawType) carry);
        check("result", new rawtype[] { (rawtype) 4, (rawtype) 6, (rawtype) 8, (rawtype) 10 }, dst);
    }

    public static void testSubtract()
    {
        DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7 }),
                    src2 = createDataStorage(new rawtype[] { (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3 }),
                    dst = createDataStorage(new rawtype[4]);

        RawtypeAdditionStrategy strategy = new RawtypeAdditionStrategy(10);
        rawtype carry = strategy.zero();

        carry = strategy.subtract(src1.iterator(DataStorage.READ, 0, 4),
                                  src2.iterator(DataStorage.READ, 0, 4),
                                  carry,
                                  dst.iterator(DataStorage.WRITE, 0, 4),
                                  4);

        assertEquals("carry", strategy.zero(), (RawType) carry);
        check("result", new rawtype[] { (rawtype) 4, (rawtype) 4, (rawtype) 4, (rawtype) 4 }, dst);
    }

    public static void testMultiplyAdd()
    {
        DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 }),
                    src2 = createDataStorage(new rawtype[] { (rawtype) 5, (rawtype) 6, (rawtype) 7, (rawtype) 8 }),
                    dst = createDataStorage(new rawtype[4]);

        RawtypeAdditionStrategy strategy = new RawtypeAdditionStrategy(10);
        rawtype carry = strategy.zero();

        carry = strategy.multiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                     src2.iterator(DataStorage.READ, 0, 4),
                                     (rawtype) 9,
                                     carry,
                                     dst.iterator(DataStorage.WRITE, 0, 4),
                                     4);

        assertEquals("carry", strategy.zero(), (RawType) carry);
        check("result", new rawtype[] { (rawtype) 14, (rawtype) 24, (rawtype) 34, (rawtype) 44 }, dst);
    }

    public static void testDivide()
    {
        DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 0, (rawtype) 2, (rawtype) 4, (rawtype) 7 }),
                    dst = createDataStorage(new rawtype[4]);

        RawtypeAdditionStrategy strategy = new RawtypeAdditionStrategy(10);
        rawtype carry = strategy.zero();

        carry = strategy.divide(src1.iterator(DataStorage.READ, 0, 4),
                                (rawtype) 2,
                                carry,
                                dst.iterator(DataStorage.WRITE, 0, 4),
                                4);

        assertEquals("carry", 1, (long) carry);
        check("result", new rawtype[] { (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3 }, dst);
    }
}
