package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeShortConvolutionStrategyTest
    extends RawtypeConvolutionStrategyTestCase
    implements RawtypeRadixConstants
{
    public RawtypeShortConvolutionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeShortConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 5 }),
                        src3 = createDataStorage(new rawtype[] { (rawtype) 1 }),
                        src9 = createDataStorage(new rawtype[] { b1 }),
                        src99 = createDataStorage(new rawtype[] { b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new RawtypeShortConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 5);

            check("normal", radix, new rawtype[] { 0, (rawtype) 5, (rawtype) 10, (rawtype) 15, (rawtype) 20 }, result);

            result = convolutionStrategy.convolute(src9, src99, 5);

            check("max", radix, new rawtype[] { b1 - (rawtype) 1, b1, b1, b1, (rawtype) 1 }, result);

            result = convolutionStrategy.convolute(src3, src1, 5);

            check("one", radix, new rawtype[] { 0, (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 }, result);
        }
    }
}
