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
