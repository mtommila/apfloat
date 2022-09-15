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

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class ScrambleTest
    extends TestCase
{
    public ScrambleTest(String methodName)
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

        suite.addTest(new ScrambleTest("testPermute"));
        suite.addTest(new ScrambleTest("testCreateScrambleTable"));

        return suite;
    }

    public static void testPermute()
    {
        assertEquals("permute(0, 1)", 0, Scramble.permute(0, 1));
        assertEquals("permute(0, 2)", 0, Scramble.permute(0, 2));
        assertEquals("permute(1, 2)", 1, Scramble.permute(1, 2));
        assertEquals("permute(1, 8)", 4, Scramble.permute(1, 8));
        assertEquals("permute(3, 16)", 12, Scramble.permute(3, 16));
        assertEquals("permute(5, 256)", 160, Scramble.permute(5, 256));
    }

    public static void testCreateScrambleTable()
    {
        int[] table = Scramble.createScrambleTable(8);
        assertEquals("length", 4, table.length);
        int[] ints = { 0, 1, 2, 3, 4, 5, 6, 7 };
        int tmp;
        tmp = ints[table[0]];
        ints[table[0]] = ints[table[1]];
        ints[table[1]] = tmp;
        tmp = ints[table[2]];
        ints[table[2]] = ints[table[3]];
        ints[table[3]] = tmp;
        assertEquals("[0]", 0, ints[0]);
        assertEquals("[1]", 4, ints[1]);
        assertEquals("[2]", 2, ints[2]);
        assertEquals("[3]", 6, ints[3]);
        assertEquals("[4]", 1, ints[4]);
        assertEquals("[5]", 5, ints[5]);
        assertEquals("[6]", 3, ints[6]);
        assertEquals("[7]", 7, ints[7]);
    }
}
