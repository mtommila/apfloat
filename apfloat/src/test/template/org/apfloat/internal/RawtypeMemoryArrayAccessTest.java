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
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeMemoryArrayAccessTest
    extends RawtypeTestCase
{
    public RawtypeMemoryArrayAccessTest(String methodName)
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

        suite.addTest(new RawtypeMemoryArrayAccessTest("testGet"));
        suite.addTest(new RawtypeMemoryArrayAccessTest("testSubsequence"));

        return suite;
    }

    public static void testGet()
    {
        rawtype[] data = { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 };
        try (ArrayAccess arrayAccess = new RawtypeMemoryArrayAccess(data, 0, 4))
        {
            assertTrue("class", arrayAccess.getData() instanceof rawtype[]);
            assertEquals("[0]", 1, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset()]);
            assertEquals("[1]", 2, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 1]);
            assertEquals("[2]", 3, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 2]);
            assertEquals("[3]", 4, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 3]);
            assertEquals("length", 4, arrayAccess.getLength());
        }
    }

    public static void testSubsequence()
    {
        rawtype[] data = { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 };
        try (ArrayAccess baseArrayAccess = new RawtypeMemoryArrayAccess(data, 0, 4);
             ArrayAccess arrayAccess = baseArrayAccess.subsequence(1, 2))
        {
            assertTrue("class", arrayAccess.getData() instanceof rawtype[]);
            assertEquals("[0]", 2, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset()]);
            assertEquals("[1]", 3, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 1]);
            assertEquals("length", 2, arrayAccess.getLength());
        }
    }
}
