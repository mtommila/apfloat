/*
 * MIT License
 *
 * Copyright (c) 2002-2025 Mikko Tommila
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
package org.apfloat;

import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.15.0
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
        Map<Thread, String> map = new ConcurrentWeakHashMap<>();
        Thread thread = new Thread();

        assertTrue("Initially empty", map.isEmpty());

        map.put(thread, "First");
        assertEquals("Size after put", 1, map.size());
        assertFalse("Not empty after put empty", map.isEmpty());
        assertEquals("Value after put", "First", map.get(thread));
        assertEquals("Values size after put", 1, map.values().size());
        assertEquals("Values after put", "First", map.values().iterator().next());

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
