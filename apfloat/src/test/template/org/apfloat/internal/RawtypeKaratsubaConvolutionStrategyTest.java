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
 * @version 1.4
 * @author Mikko Tommila
 */

public class RawtypeKaratsubaConvolutionStrategyTest
    extends RawtypeConvolutionStrategyTestCase
    implements RawtypeRadixConstants
{
    public RawtypeKaratsubaConvolutionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeKaratsubaConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7, (rawtype) 8 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4, (rawtype) 5, (rawtype) 6 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 }),
                        src99 = createDataStorage(new rawtype[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new RawtypeKaratsubaConvolutionStrategy(radix);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 6
            DataStorage result = convolutionStrategy.convolute(src1, src2, 14);

            check("normal", radix, new rawtype[] { 0, (rawtype) 1, (rawtype) 4, (rawtype) 10, (rawtype) 20, (rawtype) 35, (rawtype) 56, (rawtype) 77, (rawtype) 98, (rawtype) 110, (rawtype) 112, (rawtype) 103, (rawtype) 82, (rawtype) 48 }, result);

            // Will only test Karatsuba actually if CUTOFF_POINT is set to at most 18
            result = convolutionStrategy.convolute(src9, src99, 63);

            check("max", radix, new rawtype[] { b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1 - (rawtype) 1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, b1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (rawtype) 1 }, result);
        }
    }
}
