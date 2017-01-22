package org.apfloat.internal;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class RawtypeFactor3NTTStrategyTest
    extends RawtypeNTTStrategyTestCase
{
    public RawtypeFactor3NTTStrategyTest(String methodName)
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

        suite.addTest(new RawtypeFactor3NTTStrategyTest("testForward"));
        suite.addTest(new RawtypeFactor3NTTStrategyTest("testRoundTrip"));
        suite.addTest(new RawtypeFactor3NTTStrategyTest("testRoundTripParallel"));
        suite.addTest(new RawtypeFactor3NTTStrategyTest("testRoundTrip2"));
        suite.addTest(new RawtypeFactor3NTTStrategyTest("testRoundTrip2Parallel"));

        return suite;
    }

    public static void testForward()
    {
        for (int modulus = 0; modulus < 3; modulus++)
        {
            int size = 3 * 2048;
            DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

            rawtype[] data = getPlainArray(dataStorage),
                      expectedTransform = ntt(data, modulus);
            Arrays.sort(expectedTransform);

            NTTStrategy nttStrategy = new Factor3NTTStrategy(new RawtypeTableFNTStrategy());

            nttStrategy.transform(dataStorage, modulus);

            rawtype[] actualTransform = getPlainArray(dataStorage);
            Arrays.sort(actualTransform);

            assertEquals("expected length", size, expectedTransform.length);
            assertEquals("actual length", size, actualTransform.length);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], [" + i + "]", (long) expectedTransform[i], (long) actualTransform[i]);
            }
        }
    }

    public static void testRoundTrip()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runRoundTrip();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public static void testRoundTripParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ExecutorService executorService = ctx.getExecutorService();
        ctx.setNumberOfProcessors(4);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

        runRoundTrip();

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setExecutorService(executorService);
    }

    private static void runRoundTrip()
    {
        int size = (int) Math.min(3 * 1048576, RawtypeModConstants.MAX_TRANSFORM_LENGTH);       // Will use six-step transform
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            Factor3NTTStrategy nttStrategy = new Factor3NTTStrategy(new SixStepFNTStrategy());

            nttStrategy.transform(dataStorage, modulus);

            assertEquals("transformed size", size, dataStorage.getSize());

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getRawtype());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            assertEquals("inverse transformed size", size, dataStorage.getSize());

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getRawtype());
                iterator.next();
            }
        }
    }

    public static void testRoundTrip2()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runRoundTrip2();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public static void testRoundTrip2Parallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ExecutorService executorService = ctx.getExecutorService();
        ctx.setNumberOfProcessors(4);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

        runRoundTrip2();

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setExecutorService(executorService);
    }

    private static void runRoundTrip2()
    {
        int size = 2048;                                                        // Will fall back to the power-of-two length transform
        DataStorage dataStorage = createDataStorage(size + 5).subsequence(5, size);

        for (int modulus = 0; modulus < 3; modulus++)
        {
            Factor3NTTStrategy nttStrategy = new Factor3NTTStrategy(new RawtypeTableFNTStrategy());

            nttStrategy.transform(dataStorage, modulus);

            assertEquals("transformed size", size, dataStorage.getSize());

            DataStorage.Iterator iterator = dataStorage.iterator(DataStorage.READ, 0, 1);

            assertTrue("transformed [0]", 6 != (long) iterator.getRawtype());
            iterator.close();

            nttStrategy.inverseTransform(dataStorage, modulus, size);

            assertEquals("inverse transformed size", size, dataStorage.getSize());

            iterator = dataStorage.iterator(DataStorage.READ, 0, size);

            for (int i = 0; i < size; i++)
            {
                assertEquals("MODULUS[" + modulus + "], round-tripped [" + i + "]", i + 6, (long) iterator.getRawtype());
                iterator.next();
            }
        }
    }
}
