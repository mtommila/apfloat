package org.apfloat.internal;

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeParallelThreeNTTConvolutionStrategyTest
    extends RawtypeThreeNTTConvolutionStrategyTest
{
    public RawtypeParallelThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new RawtypeParallelThreeNTTConvolutionStrategyTest("testAutoBigParallel"));

        return suite;
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ParallelThreeNTTConvolutionStrategy(radix, transform);
    }
}
