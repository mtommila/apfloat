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
