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
package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeWTablesTest
    extends RawtypeTestCase
    implements RawtypeModConstants
{
    public RawtypeWTablesTest(String methodName)
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

        suite.addTest(new RawtypeWTablesTest("testGetWTable"));
        suite.addTest(new RawtypeWTablesTest("testGetInverseWTable"));

        return suite;
    }

    public static void testGetWTable()
    {
        rawtype[] wTable = RawtypeWTables.getWTable(0, 4);

        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[0]);
        assertEquals("[0]", 1, (long) wTable[0]);
        assertEquals("[1]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[1], 2));
        assertEquals("[2]", (long) MODULUS[0] - 1, (long) wTable[2]);
        assertEquals("[3]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[3], 2));
    }

    public static void testGetInverseWTable()
    {
        rawtype[] wTable = RawtypeWTables.getInverseWTable(0, 4);

        RawtypeModMath math = new RawtypeModMath();
        math.setModulus(MODULUS[0]);
        assertEquals("[0]", 1, (long) wTable[0]);
        assertEquals("[1]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[1], 2));
        assertEquals("[2]", (long) MODULUS[0] - 1, (long) wTable[2]);
        assertEquals("[3]", (long) MODULUS[0] - 1, (long) math.modPow(wTable[3], 2));
    }
}
