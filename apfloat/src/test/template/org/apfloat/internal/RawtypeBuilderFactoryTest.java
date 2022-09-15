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

import java.lang.reflect.Array;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeBuilderFactoryTest
    extends RawtypeTestCase
{
    public RawtypeBuilderFactoryTest(String methodName)
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

        suite.addTest(new RawtypeBuilderFactoryTest("testBuilders"));

        return suite;
    }

    public static void testBuilders()
    {
        BuilderFactory builderFactory = new RawtypeBuilderFactory();
        assertTrue("ApfloatBuilder", builderFactory.getApfloatBuilder() instanceof ApfloatBuilder);
        assertTrue("DataStorageBuilder", builderFactory.getDataStorageBuilder() instanceof DataStorageBuilder);
        assertTrue("AdditionBuilder", builderFactory.getAdditionBuilder(RawType.TYPE) instanceof AdditionBuilder);
        assertTrue("ConvolutionBuilder", builderFactory.getConvolutionBuilder() instanceof ConvolutionBuilder);
        assertTrue("NTTBuilder", builderFactory.getNTTBuilder() instanceof NTTBuilder);
        assertTrue("MatrixBuilder", builderFactory.getMatrixBuilder() instanceof MatrixBuilder);
        assertTrue("CarryCRTBuilder", builderFactory.getCarryCRTBuilder(rawtype[].class) instanceof CarryCRTBuilder);

        assertEquals("getElementType()", RawType.TYPE, builderFactory.getElementType());
        assertEquals("getElementArrayType()", rawtype[].class, builderFactory.getElementArrayType());
        assertEquals("getElementSize()", sizeof(rawtype), builderFactory.getElementSize());

        Class<?>[] types = { Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };
        for (Class<?> type : types)
        {
            if (!type.equals(RawType.TYPE))
            {
                try
                {
                    builderFactory.getAdditionBuilder(type);
                    fail("Invalid AdditonStrategy type accepted");
                }
                catch (IllegalArgumentException iae)
                {
                    // OK: should not be allowed
                }

                try
                {
                    builderFactory.getCarryCRTBuilder(Array.newInstance(type, 0).getClass());
                    fail("Invalid CarryCRTBuilder type accepted");
                }
                catch (IllegalArgumentException iae)
                {
                    // OK: should not be allowed
                }
            }
        }
    }
}
