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
package org.apfloat.aparapi;

import org.apfloat.*;
import org.apfloat.spi.*;
import org.apfloat.internal.*;

import junit.framework.TestSuite;

/**
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class IntAparapiMatrixStrategyTest
    extends IntTestCase
{
    public IntAparapiMatrixStrategyTest(String methodName)
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

        suite.addTest(new IntAparapiMatrixStrategyTest("testTransposeSquare"));
        suite.addTest(new IntAparapiMatrixStrategyTest("testTransposeSquarePart"));
        suite.addTest(new IntAparapiMatrixStrategyTest("testTransposeWide"));
        suite.addTest(new IntAparapiMatrixStrategyTest("testTransposeTall"));
        suite.addTest(new IntAparapiMatrixStrategyTest("testPermuteToDoubleWidth"));
        suite.addTest(new IntAparapiMatrixStrategyTest("testPermuteToHalfWidth"));

        return suite;
    }

    private static ArrayAccess getArray(int count)
    {
        int[] data = new int[count + 5];
        ArrayAccess arrayAccess = new IntMemoryArrayAccess(data, 5, count);

        for (int i = 0; i < count; i++)
        {
            data[i + 5] = (int) (i + 1);
        }

        return arrayAccess;
    }

    public static void testTransposeSquare()
    {
        ArrayAccess arrayAccess = getArray(16);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, 4, 4);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                assertEquals("16 elem [" + i + "][" + j + "]", 4 * j + i + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 4 * i + j]);
            }
        }

        arrayAccess = getArray(18).subsequence(1, 16);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, 4, 4);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                assertEquals("16 elem sub [" + i + "][" + j + "]", 4 * j + i + 2, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 4 * i + j]);
            }
        }

        ApfloatContext ctx = ApfloatContext.getContext();
        int cacheBurstBlockSize = Util.round2down(ctx.getCacheBurst() / 4),   // Cache burst in ints
            cacheL1Size = Util.sqrt4down(ctx.getCacheL1Size() / 4),           // To fit in processor L1 cache
            cacheL2Size = Util.sqrt4down(ctx.getCacheL2Size() / 4),           // To fit in processor L2 cache
            bigSize = cacheL2Size * 2;                                                      // To not fit in processor L2 cache

        arrayAccess = getArray(cacheBurstBlockSize * cacheBurstBlockSize);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, cacheBurstBlockSize, cacheBurstBlockSize);

        for (int i = 0; i < cacheBurstBlockSize; i++)
        {
            for (int j = 0; j < cacheBurstBlockSize; j++)
            {
                assertEquals("cacheBurstBlockSize [" + i + "][" + j + "]", cacheBurstBlockSize * j + i + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + cacheBurstBlockSize * i + j]);
            }
        }

        arrayAccess = getArray(cacheL1Size * cacheL1Size);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, cacheL1Size, cacheL1Size);

        for (int i = 0; i < cacheL1Size; i++)
        {
            for (int j = 0; j < cacheL1Size; j++)
            {
                assertEquals("cacheL1Size [" + i + "][" + j + "]", cacheL1Size * j + i + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + cacheL1Size * i + j]);
            }
        }

        arrayAccess = getArray(cacheL2Size * cacheL2Size);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, cacheL2Size, cacheL2Size);

        for (int i = 0; i < cacheL2Size; i++)
        {
            for (int j = 0; j < cacheL2Size; j++)
            {
                assertEquals("cacheL2Size [" + i + "][" + j + "]", cacheL2Size * j + i + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + cacheL2Size * i + j]);
            }
        }

        arrayAccess = getArray(bigSize * bigSize);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, bigSize, bigSize);

        for (int i = 0; i < bigSize; i++)
        {
            for (int j = 0; j < bigSize; j++)
            {
                assertEquals("bigSize [" + i + "][" + j + "]", bigSize * j + i + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + bigSize * i + j]);
            }
        }
    }

    public static void testTransposeSquarePart()
    {
        ArrayAccess arrayAccess = getArray(32);

        new IntAparapiMatrixStrategy().transposeSquare(arrayAccess, 4, 8);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                assertEquals("1st transposed [" + i + "][" + j + "]", 8 * j + i + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 8 * i + j]);
            }
            for (int j = 4; j < 8; j++)
            {
                assertEquals("1st untransposed [" + i + "][" + j + "]", 8 * i + j + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 8 * i + j]);
            }
        }

        arrayAccess = getArray(32);

        new IntAparapiMatrixStrategy().transposeSquare(arrayAccess.subsequence(4, 28), 4, 8);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                assertEquals("2nd untransposed [" + i + "][" + j + "]", 8 * i + j + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 8 * i + j]);
            }
            for (int j = 4; j < 8; j++)
            {
                assertEquals("2nd transposed [" + i + "][" + j + "]", 8 * (j - 4) + (i + 4) + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 8 * i + j]);
            }
        }
    }

    public static void testTransposeWide()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int cacheL2Size = Util.sqrt4down(ctx.getCacheL2Size() / 4),           // To fit in processor L2 cache
            bigSize = cacheL2Size * 2;                                                      // To not fit in processor L2 cache

        ArrayAccess arrayAccess = getArray(2 * bigSize * bigSize + 5).subsequence(5, 2 * bigSize * bigSize);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, bigSize, 2 * bigSize);

        for (int i = 0; i < 2 * bigSize; i++)
        {
            for (int j = 0; j < bigSize; j++)
            {
                assertEquals("transposed [" + i + "][" + j + "]", 2 * bigSize * j + i + 6, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + bigSize * i + j]);
            }
        }
    }

    public static void testTransposeTall()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int cacheL2Size = Util.sqrt4down(ctx.getCacheL2Size() / 4),           // To fit in processor L2 cache
            bigSize = cacheL2Size * 2;                                                      // To not fit in processor L2 cache

        ArrayAccess arrayAccess = getArray(2 * bigSize * bigSize + 5).subsequence(5, 2 * bigSize * bigSize);

        new IntAparapiMatrixStrategy().transpose(arrayAccess, 2 * bigSize, bigSize);

        for (int i = 0; i < bigSize; i++)
        {
            for (int j = 0; j < 2 * bigSize; j++)
            {
                assertEquals("transposed [" + i + "][" + j + "]", bigSize * j + i + 6, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 2 * bigSize * i + j]);
            }
        }
    }

    public static void testPermuteToDoubleWidth()
    {
        ArrayAccess arrayAccess = getArray(256);

        new IntAparapiMatrixStrategy().permuteToDoubleWidth(arrayAccess, 8, 32);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 32; j++)
            {
                assertEquals("permuted to double width [" + i + "][" + j + "]", 32 * i + j + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 64 * i + j]);
            }
            for (int j = 32; j < 64; j++)
            {
                assertEquals("permuted to double width [" + i + "][" + j + "]", 32 * i + j - 32 + 128 + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 64 * i + j]);
            }
        }
    }

    public static void testPermuteToHalfWidth()
    {
        ArrayAccess arrayAccess = getArray(256);

        new IntAparapiMatrixStrategy().permuteToHalfWidth(arrayAccess, 4, 64);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 32; j++)
            {
                assertEquals("permuted to half width [" + i + "][" + j + "]", 64 * i + j + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 32 * i + j]);
            }
        }
        for (int i = 4; i < 8; i++)
        {
            for (int j = 0; j < 32; j++)
            {
                assertEquals("permuted to half width [" + i + "][" + j + "]", 64 * (i - 4) + j + 32 + 1, (long) arrayAccess.getIntData()[arrayAccess.getOffset() + 32 * i + j]);
            }
        }
    }
}
