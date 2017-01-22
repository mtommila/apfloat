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
