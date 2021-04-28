/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Method;

import junit.framework.TestSuite;

/**
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeCRTMathTest
    extends RawtypeTestCase
    implements RawtypeModConstants
{
    public RawtypeCRTMathTest(String methodName)
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

        suite.addTest(new RawtypeCRTMathTest("testMultiply"));
        suite.addTest(new RawtypeCRTMathTest("testCompare"));
        suite.addTest(new RawtypeCRTMathTest("testAdd"));
        suite.addTest(new RawtypeCRTMathTest("testSubtract"));
        suite.addTest(new RawtypeCRTMathTest("testDivide"));
        suite.addTest(new RawtypeCRTMathTest("testSerialization"));

        return suite;
    }

    public static void testMultiply()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype[] src = { b1, b1 };
        rawtype[] dst = new rawtype[3];

        new RawtypeCRTMath(2).multiply(src, b1, dst);

        assertEquals("max[0]", (long) b1 - 1, (long) dst[0]);
        assertEquals("max[1]", (long) b1, (long) dst[1]);
        assertEquals("max[2]", (long) 1, (long) dst[2]);

        src = new rawtype[] { (rawtype) 2, (rawtype) 4 };

        new RawtypeCRTMath(2).multiply(src, (rawtype) 3, dst);

        assertEquals("normal[0]", 0, (long) dst[0]);
        assertEquals("normal[1]", 6, (long) dst[1]);
        assertEquals("normal[2]", 12, (long) dst[2]);
    }

    public static void testCompare()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 },
                                                new rawtype[] { (rawtype) 2, (rawtype) 1, (rawtype) 1 });
        assertTrue("1st", result < 0);

        result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 1 },
                                                new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 });
        assertTrue("2nd", result > 0);

        result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 0 },
                                                new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) b1 });
        assertTrue("3rd", result < 0);

        result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 },
                                                new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 });
        assertTrue("equal", result == 0);
    }

    public static void testAdd()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype[] src = { b1, b1, b1 };
        rawtype[] srcDst = { b1, b1, b1 };

        rawtype carry = new RawtypeCRTMath(2).add(src, srcDst);

        assertEquals("max carry", 1, (long) carry);
        assertEquals("max[0]", (long) b1, (long) srcDst[0]);
        assertEquals("max[1]", (long) b1, (long) srcDst[1]);
        assertEquals("max[2]", (long) b1 - 1, (long) srcDst[2]);

        src = new rawtype[] { (rawtype) 2, (rawtype) 4, (rawtype) 6 };
        srcDst = new rawtype[] { (rawtype) 3, (rawtype) 5, (rawtype) 7 };

        carry = new RawtypeCRTMath(2).add(src, srcDst);

        assertEquals("normal carry", 0, (long) carry);
        assertEquals("normal[0]", 5, (long) srcDst[0]);
        assertEquals("normal[1]", 9, (long) srcDst[1]);
        assertEquals("normal[2]", 13, (long) srcDst[2]);
    }

    public static void testSubtract()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype[] src = { b1, b1, b1 };
        rawtype[] srcDst = { b1, b1, b1 };

        new RawtypeCRTMath(2).subtract(src, srcDst);

        assertEquals("max[0]", 0, (long) srcDst[0]);
        assertEquals("max[1]", 0, (long) srcDst[1]);
        assertEquals("max[2]", 0, (long) srcDst[2]);

        src = new rawtype[] { 0, 0, (rawtype) 1 };
        srcDst = new rawtype[] { (rawtype) 1, 0, 0 };

        new RawtypeCRTMath(2).subtract(src, srcDst);

        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", (long) b1, (long) srcDst[1]);
        assertEquals("normal[2]", (long) b1, (long) srcDst[2]);
    }

    public static void testDivide()
    {
        rawtype[] srcDst = new rawtype[] { (rawtype) 1, 0, 1 };

        rawtype remainder = new RawtypeCRTMath(2).divide(srcDst);

        assertEquals("normal remainder", 1, (long) remainder);
        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", 2, (long) srcDst[1]);
        assertEquals("normal[2]", 0, (long) srcDst[2]);
    }

    public void testSerialization()
        throws Exception
    {
        if (RawType.TYPE.getName().equals("long"))
        {
            String className = RawtypeCRTMath.class.getName();
            Java9ClassLoader classLoader = new Java9ClassLoader(getClass().getClassLoader());
            classLoader.loadJava9Class(RawtypeBaseMath.class.getName()); // Load base class Java 9 specific version also
            Class<?> crtMathClass = classLoader.loadJava9Class(className);
            Object crtMath = crtMathClass.getConstructor(Integer.TYPE).newInstance(2);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(buffer))
            {
                out.writeObject(crtMath);
            }
            byte[] java9Data = buffer.toByteArray();
            buffer = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(buffer))
            {
                out.writeObject(new RawtypeCRTMath(2));
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
            crtMath = in.readObject();
            Method method = crtMath.getClass().getMethod("divide", rawtype[].class);
            rawtype[] srcDst =  { (rawtype) 1, (rawtype) 0, (rawtype) 1 };
            rawtype remainder = (RawType) method.invoke(crtMath, srcDst);
            assertEquals("Deserialized java 9 classloader", classLoader, crtMath.getClass().getClassLoader());
            assertEquals("Deserialized java 9 remainder", (rawtype) 1, remainder);
            assertEquals("Deserialized java 9 result [0]", (rawtype) 0, srcDst[0]);
            assertEquals("Deserialized java 9 result [1]", (rawtype) 2, srcDst[1]);
            assertEquals("Deserialized java 9 result [2]", (rawtype) 0, srcDst[2]);
        }
    }
}
