/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
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
package org.apfloat;

import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.6.2
 * @author Mikko Tommila
 */

public class ShutdownMapTest
    extends TestCase
{
    public ShutdownMapTest(String methodName)
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

        suite.addTest(new ShutdownMapTest("testMap"));

        return suite;
    }

    public static void testMap()
    {
        Map<Integer, Apfloat> map = new ShutdownMap<Integer, Apfloat>();

        try
        {
            map.put(0, Apfloat.ZERO);
            fail("ShutdownMap allowed put()");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK
        }

        try
        {
            map.get(0);
            fail("ShutdownMap allowed get()");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK
        }
    }
}
