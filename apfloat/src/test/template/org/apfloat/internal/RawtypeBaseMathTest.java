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

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Method;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeBaseMathTest
    extends RawtypeTestCase
    implements RawtypeRadixConstants
{
    public RawtypeBaseMathTest(String methodName)
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

        suite.addTest(new RawtypeBaseMathTest("testAdd"));
        suite.addTest(new RawtypeBaseMathTest("testSubtract"));
        suite.addTest(new RawtypeBaseMathTest("testMultiplyAdd"));
        suite.addTest(new RawtypeBaseMathTest("testDivide"));
        suite.addTest(new RawtypeBaseMathTest("testSerialization"));

        return suite;
    }

    private static DataStorage createDataStorage(rawtype[] data)
    {
        int size = data.length;
        ApfloatContext ctx = ApfloatContext.getContext();
        DataStorageBuilder dataStorageBuilder = ctx.getBuilderFactory().getDataStorageBuilder();
        DataStorage dataStorage = dataStorageBuilder.createDataStorage(size * sizeof(rawtype));
        dataStorage.setSize(size);

        try (ArrayAccess arrayAccess = dataStorage.getArray(DataStorage.WRITE, 0, size))
        {
            System.arraycopy(data, 0, arrayAccess.getData(), arrayAccess.getOffset(), size);
        }

        return dataStorage;
    }

    private static void check(String message, int radix, rawtype[] expected, DataStorage actual)
    {
        try (ArrayAccess arrayAccess = actual.getArray(DataStorage.READ, 0, expected.length))
        {
            assertEquals("radix " + radix + " " + message + " length", expected.length, arrayAccess.getLength());
            for (int i = 0; i < arrayAccess.getLength(); i++)
            {
                assertEquals("radix " + radix + " " + message + " [" + i + "]", (long) expected[i], (long) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + i]);
            }
        }
    }

    public static void testAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new rawtype[4]);

            RawtypeBaseMath math = new RawtypeBaseMath(radix);
            rawtype carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new rawtype[] { (rawtype) 4, (rawtype) 6, (rawtype) 8, (rawtype) 10 }, dst);

            carry = (rawtype) 1;

            carry = math.baseAdd(src9.iterator(DataStorage.READ, 0, 4),
                                 src9.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new rawtype[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseAdd(src1.iterator(DataStorage.READ, 0, 4),
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new rawtype[] { (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 src2.iterator(DataStorage.READ, 0, 4),
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " src2 carry", 0, (long) carry);
            check("src2", radix, new rawtype[] { (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7 }, dst);

            carry = 0;

            carry = math.baseAdd(null,
                                 null,
                                 carry,
                                 dst.iterator(DataStorage.WRITE, 0, 4),
                                 4);

            assertEquals("radix " + radix + " nulls carry", 0, (long) carry);
            check("nulls", radix, new rawtype[] { 0, 0, 0, 0 }, dst);
        }
    }

    public static void testSubtract()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new rawtype[4]);

            RawtypeBaseMath math = new RawtypeBaseMath(radix);
            rawtype carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      src2.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new rawtype[] { (rawtype) 4, (rawtype) 4, (rawtype) 4, (rawtype) 4 }, dst);

            carry = (rawtype) 1;

            carry = math.baseSubtract(src9.iterator(DataStorage.READ, 0, 4),
                                      src9.iterator(DataStorage.READ, 0, 4),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " max carry", 1, (long) carry);
            check("max", radix, new rawtype[] { b1, b1, b1, b1 }, dst);

            carry = 0;

            carry = math.baseSubtract(src1.iterator(DataStorage.READ, 0, 4),
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new rawtype[] { (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7 }, dst);

            carry = 0;

            carry = math.baseSubtract(null,
                                      src2.iterator(DataStorage.READ, 4, 0),
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 4, 0),
                                      4);

            assertEquals("radix " + radix + " src2 carry", 1, (long) carry);
            check("src2", radix, new rawtype[] { BASE[radix] - (rawtype) 1, BASE[radix] - (rawtype) 2, BASE[radix] - (rawtype) 3, BASE[radix] - (rawtype) 3 }, dst);

            carry = 1;

            carry = math.baseSubtract(null,
                                      null,
                                      carry,
                                      dst.iterator(DataStorage.WRITE, 0, 4),
                                      4);

            assertEquals("radix " + radix + " nulls carry", 1, (long) carry);
            check("nulls", radix, new rawtype[] { b1, b1, b1, b1 }, dst);
        }
    }

    public static void testMultiplyAdd()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 5, (rawtype) 6, (rawtype) 7, (rawtype) 8 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new rawtype[4]);

            RawtypeBaseMath math = new RawtypeBaseMath(radix);
            rawtype carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         src2.iterator(DataStorage.READ, 0, 4),
                                         (rawtype) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " both carry", 0, (long) carry);
            check("both", radix, new rawtype[] { (rawtype) 14, (rawtype) 24, (rawtype) 34, (rawtype) 44 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         src9.iterator(DataStorage.READ, 4, 0),
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max2 carry", (long) b1, (long) carry);
            check("max2", radix, new rawtype[] { b1, b1, b1, 0 }, dst);

            carry = b1;

            carry = math.baseMultiplyAdd(src9.iterator(DataStorage.READ, 4, 0),
                                         null,
                                         b1,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 4, 0),
                                         4);

            assertEquals("radix " + radix + " max1 carry", (long) b1, (long) carry);
            check("max1", radix, new rawtype[] { 0, 0, 0, 0 }, dst);

            carry = 0;

            carry = math.baseMultiplyAdd(src1.iterator(DataStorage.READ, 0, 4),
                                         null,
                                         (rawtype) 9,
                                         carry,
                                         dst.iterator(DataStorage.WRITE, 0, 4),
                                         4);

            assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
            check("src1", radix, new rawtype[] { (rawtype) 9, (rawtype) 18, (rawtype) 27, (rawtype) 36 }, dst);
        }
    }

    public static void testDivide()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 0, (rawtype) 2, (rawtype) 4, (rawtype) 7 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1, b1 }),
                        dst = createDataStorage(new rawtype[4]);

            RawtypeBaseMath math = new RawtypeBaseMath(radix);
            rawtype carry = 0;

            carry = math.baseDivide(src1.iterator(DataStorage.READ, 0, 4),
                                    (rawtype) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " both carry", 1, (long) carry);
            check("both", radix, new rawtype[] { (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3 }, dst);

            carry = b1 - (rawtype) 1;

            carry = math.baseDivide(src9.iterator(DataStorage.READ, 0, 4),
                                    b1,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            assertEquals("radix " + radix + " max carry", (long) b1 - 1, (long) carry);
            check("max", radix, new rawtype[] { b1, b1, b1, b1 }, dst);

            carry = (rawtype) 1;

            carry = math.baseDivide(null,
                                    (rawtype) 2,
                                    carry,
                                    dst.iterator(DataStorage.WRITE, 0, 4),
                                    4);

            if ((radix & 1) == 0)
            {
                assertEquals("radix " + radix + " src1 carry", 0, (long) carry);
                check("src1", radix, new rawtype[] { BASE[radix] / (rawtype) 2, 0, 0, 0 }, dst);
            }
            else
            {
                assertEquals("radix " + radix + " src1 carry", 1, (long) carry);
                check("src1", radix, new rawtype[] { b1 / (rawtype) 2, b1 / (rawtype) 2, b1 / (rawtype) 2, b1 / (rawtype) 2 }, dst);
            }
        }
    }

    public void testSerialization()
        throws Exception
    {
        if (RawType.TYPE.getName().equals("long"))
        {
            String className = RawtypeBaseMath.class.getName();
            Java9ClassLoader classLoader = new Java9ClassLoader(getClass().getClassLoader());
            Class<?> baseMathClass = classLoader.loadJava9Class(className);
            Object baseMath = baseMathClass.getConstructor(Integer.TYPE).newInstance(10);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(buffer))
            {
                out.writeObject(baseMath);
            }
            byte[] java9Data = buffer.toByteArray();
            buffer = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(buffer))
            {
                out.writeObject(new RawtypeBaseMath(10));
            }
            byte[] java8Data = buffer.toByteArray();
            assertArrayEquals("Serialized data", java8Data, java9Data);

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(java8Data))
            {
                @Override
                public Class<?> resolveClass(ObjectStreamClass desc)
                    throws IOException, ClassNotFoundException
                {
                    String name = desc.getName();
                    return classLoader.loadClass(name);
                }
            };
            baseMath = in.readObject();
            Method method = baseMath.getClass().getMethod("baseMultiplyAdd", DataStorage.Iterator.class, DataStorage.Iterator.class, Long.TYPE, Long.TYPE, DataStorage.Iterator.class, Long.TYPE);
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 10 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 20 }),
                        dst = createDataStorage(new rawtype[1]);
            rawtype src3 = (rawtype) 30,
                    carry = (rawtype) 0;
            carry = (RawType) method.invoke(baseMath, src1.iterator(DataStorage.READ, 0, 1), src2.iterator(DataStorage.READ, 0, 1), src3, carry, dst.iterator(DataStorage.WRITE, 0, 1), 1L);
            ArrayAccess arrayAccess = dst.getArray(DataStorage.READ, 0, 1);
            assertEquals("Deserialized java 9 classloader", classLoader, baseMath.getClass().getClassLoader());
            assertEquals("Deserialized java 9 carry", (rawtype) 0, carry);
            assertEquals("Deserialized java 9 result", (rawtype) 320, arrayAccess.getRawtypeData()[arrayAccess.getOffset()]);
        }
    }
}
