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
package org.apfloat;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.6
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class ConcurrentSoftHashMapTest
    extends TestCase
{
    public ConcurrentSoftHashMapTest(String methodName)
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

        suite.addTest(new ConcurrentSoftHashMapTest("testSoft"));

        return suite;
    }

    public static void testSoft()
    {
        Map<Integer, byte[]> map = new ConcurrentSoftHashMap<>();

        final int SIZE = 1000000;

        byte[] value = new byte[4 * SIZE];
        map.put(1, value);
        assertEquals("Size after put", 1, map.size());
        assertNotNull("Value", map.get(1));
        value = null;

        // Force out of memory
        try
        {
            List<byte[]> dummy = new ArrayList<>();
            for (int i = 2; i < 1000000000 && map.get(1) != null; i++)
            {
                dummy.add(new byte[SIZE]);
            }
        }
        catch (OutOfMemoryError oome)
        {
            // Should happen eventually
        }

        assertNull("Cleared", map.get(1));

        map.put(1, value);
        map.clear();
        assertNull("Explicitly cleared", map.get(1));

        assertNull("Removed", map.remove(1));

        try
        {
            map.entrySet();
        }
        catch (UnsupportedOperationException uoe)
        {
            // Ignore
        }
    }
}
