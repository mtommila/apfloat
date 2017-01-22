package org.apfloat.internal;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.6
 * @version 1.6
 * @author Mikko Tommila
 */

public class MessagePasserTest
    extends TestCase
{
    public MessagePasserTest(String methodName)
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

        suite.addTest(new MessagePasserTest("testPassMessage"));
        suite.addTest(new MessagePasserTest("testPassMessageParallel"));

        return suite;
    }

    public static void testPassMessage()
    {
        MessagePasser<Integer, String> messagePasser = new MessagePasser<Integer, String>();

        assertNull("No message yet", messagePasser.getMessage(1));

        messagePasser.sendMessage(1, "Hello, world!");
        assertEquals("Message received", "Hello, world!", messagePasser.receiveMessage(1));
        assertNull("No message anymore", messagePasser.getMessage(1));

        messagePasser.sendMessage(2, "Hello, world!");
        assertEquals("Message got", "Hello, world!", messagePasser.getMessage(2));
        assertNull("No message anymore 2", messagePasser.getMessage(2));
    }

    public static void testPassMessageParallel()
    {
        final MessagePasser<Integer, String> messagePasser = new MessagePasser<Integer, String>();
        Thread thread = new Thread()
        {
            public void run()
            {
                assertNull("No message yet", messagePasser.getMessage(1));
                assertEquals("Message received", "Hello, world!", messagePasser.receiveMessage(1));
            }
        };
        thread.start();
        sleepUninterrupted(100);

        messagePasser.sendMessage(1, "Hello, world!");
        sleepUninterrupted(100);
        assertNull("No message anymore", messagePasser.getMessage(1));
    }

    private static void sleepUninterrupted(long time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException ie)
        {
            fail(ie.toString());
        }
    }
}
