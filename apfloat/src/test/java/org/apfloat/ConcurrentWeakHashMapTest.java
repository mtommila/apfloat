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
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class ConcurrentWeakHashMapTest
    extends TestCase
{
    public ConcurrentWeakHashMapTest(String methodName)
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

        suite.addTest(new ConcurrentWeakHashMapTest("testWeak"));

        return suite;
    }

    public static void testWeak() throws Exception
    {
        Map<Thread, String> map = new ConcurrentWeakHashMap<Thread, String>();
        Thread thread = new Thread();

        assertTrue("Initially empty", map.isEmpty());

        map.put(thread, "First");
        assertEquals("Size after put", 1, map.size());
        assertFalse("Not empty after put empty", map.isEmpty());
        assertEquals("Value after put", "First", map.get(thread));

        thread = null;
        System.gc();
        Thread.sleep(1000);
        // Expunging garbage collected entries may happen only with put()
        for (int i = 0; i < 1000; i++)
        {
            map.put(new Thread(), "Dummy");
        }
        assertEquals("Size after GC", 1000, map.size());

        thread = new Thread();
        map.put(thread, "Bogus");
        map.clear();
        assertNull("Explicitly cleared", map.get(thread));

        assertNull("Removed", map.remove(thread));

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
