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

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeScrambleTest
    extends RawtypeTestCase
{
    public RawtypeScrambleTest(String methodName)
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

        suite.addTest(new RawtypeScrambleTest("testScramble"));

        return suite;
    }

    public static void testScramble()
    {
        int[] permutationTable = Scramble.createScrambleTable(8);
        rawtype[] ints = { (rawtype) -1, (rawtype) 0, (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4, (rawtype) 5, (rawtype) 6, (rawtype) 7 };
        RawtypeScramble.scramble(ints, 1, permutationTable);
        assertEquals("[0]", 0, (int) ints[1]);
        assertEquals("[1]", 4, (int) ints[2]);
        assertEquals("[2]", 2, (int) ints[3]);
        assertEquals("[3]", 6, (int) ints[4]);
        assertEquals("[4]", 1, (int) ints[5]);
        assertEquals("[5]", 5, (int) ints[6]);
        assertEquals("[6]", 3, (int) ints[7]);
        assertEquals("[7]", 7, (int) ints[8]);
    }
}
