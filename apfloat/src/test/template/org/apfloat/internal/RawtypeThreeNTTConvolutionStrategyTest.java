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

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeThreeNTTConvolutionStrategyTest
    extends RawtypeConvolutionStrategyTestCase
    implements RawtypeRadixConstants
{
    public RawtypeThreeNTTConvolutionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testFull"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testTruncated"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testAuto"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testFullBig"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testFullBigParallel"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testFullHugeParallel"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testTruncatedBig"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testAutoBig"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testAutoBigParallel"));
        suite.addTest(new RawtypeThreeNTTConvolutionStrategyTest("testAutoHugeParallel"));

        return suite;
    }

    public void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 5, (rawtype) 6 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1 }),
                        src99 = createDataStorage(new rawtype[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new RawtypeTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new rawtype[] { 0, (rawtype) 5, (rawtype) 16, (rawtype) 27, (rawtype) 38, (rawtype) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new rawtype[] { b1, b1, b1 - (rawtype) 1, b1, b1, b1, 0, 0, (rawtype) 1 }, result);
        }
    }

    public void testTruncated()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 5, (rawtype) 6 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1 }),
                        src99 = createDataStorage(new rawtype[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new RawtypeTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src1, src2, 3);

            check("normal", radix, new rawtype[] { 0, (rawtype) 5, (rawtype) 16 }, result);

            result = convolutionStrategy.convolute(src9, src99, 6);

            check("max", radix, new rawtype[] { b1, b1, b1 - (rawtype) 1, b1, b1, b1 }, result);
        }
    }

    public void testAuto()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src9 = createDataStorage(new rawtype[] { b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new RawtypeTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 6);

            check("max", radix, new rawtype[] { b1, b1, b1 - (rawtype) 1, 0, 0, (rawtype) 1 }, result);
        }
    }

    public void testFullBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testFullBigParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(4);

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testFullHugeParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize();
        ctx.setNumberOfProcessors(4);
        ctx.setMaxMemoryBlockSize(65536);

        runBig(20000, 12000);

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setMaxMemoryBlockSize(maxMemoryBlockSize);
    }


    private void runBig()
    {
        runBig(500, 300);
    }

    private void runBig(int size1, int size2)
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            rawtype[] array1 = new rawtype[size1],
                      array2 = new rawtype[size2],
                      array3 = new rawtype[size1 + size2];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (rawtype) (i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
                array3[i + size1] = (rawtype) (i == size2 - 1 ? 1 : 0);
            }
            DataStorage src9 = createDataStorage(array1),
                        src99 = createDataStorage(array2);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new SixStepFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src99, size1 + size2);

            check("max", radix, array3, result);
        }
    }

    public void testTruncatedBig()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            int size1 = 500,
                size2 = 300;
            rawtype[] array1 = new rawtype[size1],
                      array2 = new rawtype[size2],
                      array3 = new rawtype[size1];
            for (int i = 0; i < size1; i++)
            {
                array1[i] = b1;
                array3[i] = b1 - (rawtype) (i == size1 - 1 || i == size2 - 1 ? 1 : 0);
            }
            for (int i = 0; i < size2; i++)
            {
                array2[i] = b1;
            }
            DataStorage src9 = createDataStorage(array1),
                        src99 = createDataStorage(array2);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new RawtypeTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src99, src9, size1);

            check("max", radix, array3, result);
        }
    }

    public void testAutoBig()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runAutoBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testAutoBigParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(4);

        runAutoBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public void testAutoHugeParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        long maxMemoryBlockSize = ctx.getMaxMemoryBlockSize();
        ctx.setNumberOfProcessors(4);
        ctx.setMaxMemoryBlockSize(65536);

        runAutoBig(20000);

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setMaxMemoryBlockSize(maxMemoryBlockSize);
    }

    private void runAutoBig()
    {
        runAutoBig(500);
    }

    private void runAutoBig(int size)
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            rawtype[] array1 = new rawtype[size],
                      array2 = new rawtype[2 * size];
            for (int i = 0; i < size; i++)
            {
                array1[i] = b1;
                array2[i] = b1 - (rawtype) (i == size - 1 ? 1 : 0);
                array2[i + size] = (rawtype) (i == size - 1 ? 1 : 0);
            }
            DataStorage src9 = createDataStorage(array1);

            ConvolutionStrategy convolutionStrategy = createConvolutionStrategy(radix, new RawtypeTableFNTStrategy());

            DataStorage result = convolutionStrategy.convolute(src9, src9, 2 * size);

            check("max", radix, array2, result);
        }
    }

    protected ConvolutionStrategy createConvolutionStrategy(int radix, NTTStrategy transform)
    {
        return new ThreeNTTConvolutionStrategy(radix, transform);
    }
}
