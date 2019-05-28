/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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
