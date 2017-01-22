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
