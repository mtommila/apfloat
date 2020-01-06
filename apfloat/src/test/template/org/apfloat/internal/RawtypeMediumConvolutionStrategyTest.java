/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeMediumConvolutionStrategyTest
    extends RawtypeConvolutionStrategyTestCase
    implements RawtypeRadixConstants
{
    public RawtypeMediumConvolutionStrategyTest(String methodName)
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

        suite.addTest(new RawtypeMediumConvolutionStrategyTest("testFull"));

        return suite;
    }

    public static void testFull()
    {
        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            rawtype b1 = BASE[radix] - (rawtype) 1;
            DataStorage src1 = createDataStorage(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 }),
                        src2 = createDataStorage(new rawtype[] { (rawtype) 5, (rawtype) 6 }),
                        src9 = createDataStorage(new rawtype[] { b1, b1, b1 }),
                        src99 = createDataStorage(new rawtype[] { b1, b1, b1, b1, b1, b1 });

            ConvolutionStrategy convolutionStrategy = new RawtypeMediumConvolutionStrategy(radix);

            DataStorage result = convolutionStrategy.convolute(src1, src2, 6);

            check("normal", radix, new rawtype[] { 0, (rawtype) 5, (rawtype) 16, (rawtype) 27, (rawtype) 38, (rawtype) 24 }, result);

            result = convolutionStrategy.convolute(src9, src99, 9);

            check("max", radix, new rawtype[] { b1, b1, b1 - (rawtype) 1, b1, b1, b1, 0, 0, (rawtype) 1 }, result);
        }
    }
}
