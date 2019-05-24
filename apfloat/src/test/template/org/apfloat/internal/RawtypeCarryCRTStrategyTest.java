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

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;

import org.apfloat.*;
import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeCarryCRTStrategyTest
    extends RawtypeTestCase
    implements RawtypeModConstants, RawtypeRadixConstants
{
    public RawtypeCarryCRTStrategyTest(String methodName)
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

        suite.addTest(new RawtypeCarryCRTStrategyTest("testFullLength"));
        suite.addTest(new RawtypeCarryCRTStrategyTest("testTruncatedLength"));
        suite.addTest(new RawtypeCarryCRTStrategyTest("testBigFullLength"));
        suite.addTest(new RawtypeCarryCRTStrategyTest("testBigFullLengthParallel"));

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

    public static void testFullLength()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            BigInteger base = BigInteger.valueOf((long) BASE[radix]),
                       m0 = BigInteger.valueOf((long) MODULUS[0]),
                       m1 = BigInteger.valueOf((long) MODULUS[1]),
                       m2 = BigInteger.valueOf((long) MODULUS[2]),
                       value = BigInteger.valueOf(1).multiply(base).add(BigInteger.valueOf(2)).multiply(base).add(BigInteger.valueOf(3));

            DataStorage src0 = createDataStorage(new rawtype[] { 0, value.mod(m0).rawtypeValue(), value.mod(m0).rawtypeValue(), 0 }),
                        src1 = createDataStorage(new rawtype[] { 0, value.mod(m1).rawtypeValue(), value.mod(m1).rawtypeValue(), 0 }),
                        src2 = createDataStorage(new rawtype[] { 0, value.mod(m2).rawtypeValue(), value.mod(m2).rawtypeValue(), 0 });

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, 4);

            check("normal", radix, new rawtype[] {(rawtype) 1, (rawtype) 3, (rawtype) 5, (rawtype) 3 }, dst);
        }
    }

    public static void testTruncatedLength()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            BigInteger base = BigInteger.valueOf((long) BASE[radix]),
                       m0 = BigInteger.valueOf((long) MODULUS[0]),
                       m1 = BigInteger.valueOf((long) MODULUS[1]),
                       m2 = BigInteger.valueOf((long) MODULUS[2]),
                       value = BigInteger.valueOf(1).multiply(base).add(BigInteger.valueOf(2)).multiply(base).add(BigInteger.valueOf(3));

            DataStorage src0 = createDataStorage(new rawtype[] { 0, value.mod(m0).rawtypeValue(), (rawtype) 1, 0 }),
                        src1 = createDataStorage(new rawtype[] { 0, value.mod(m1).rawtypeValue(), (rawtype) 1, 0 }),
                        src2 = createDataStorage(new rawtype[] { 0, value.mod(m2).rawtypeValue(), (rawtype) 1, 0 });

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, 1);

            check("normal", radix, new rawtype[] {(rawtype) 1 }, dst);
        }
    }

    public static void testBigFullLength()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ctx.setNumberOfProcessors(1);

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
    }

    public static void testBigFullLengthParallel()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = ctx.getNumberOfProcessors();
        ExecutorService executorService = ctx.getExecutorService();
        ctx.setNumberOfProcessors(4);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

        runBig();

        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setExecutorService(executorService);
    }

    private static void runBig()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            BigInteger base = BigInteger.valueOf((long) BASE[radix]),
                       bm1 = BigInteger.valueOf((long) BASE[radix] - 1),
                       bm2 = BigInteger.valueOf((long) BASE[radix] - 2),
                       m0 = BigInteger.valueOf((long) MODULUS[0]),
                       m1 = BigInteger.valueOf((long) MODULUS[1]),
                       m2 = BigInteger.valueOf((long) MODULUS[2]),
                       value = bm2.multiply(base).add(bm1).multiply(base).add(BigInteger.valueOf(1));

            final int SIZE = 500;

            rawtype[] data0 = new rawtype[SIZE],
                      data1 = new rawtype[SIZE],
                      data2 = new rawtype[SIZE],
                      expected = new rawtype[SIZE];

            rawtype value0 = value.mod(m0).rawtypeValue(),
                    value1 = value.mod(m1).rawtypeValue(),
                    value2 = value.mod(m2).rawtypeValue();

            for (int i = 1; i < SIZE - 1; i++)
            {
                data0[i] = value0;
                data1[i] = value1;
                data2[i] = value2;
                expected[i] = BASE[radix] - (rawtype) 1;
            }
            expected[0] = BASE[radix] - (rawtype) 1;
            expected[1] = BASE[radix] - (rawtype) 2;
            expected[SIZE - 2] = 0;
            expected[SIZE - 1] = (rawtype) 1;

            DataStorage src0 = createDataStorage(data0),
                        src1 = createDataStorage(data1),
                        src2 = createDataStorage(data2);

            StepCarryCRTStrategy crt = new StepCarryCRTStrategy(radix);

            DataStorage dst = crt.carryCRT(src0, src1, src2, SIZE);

            check("normal", radix, expected, dst);
        }
    }
}
