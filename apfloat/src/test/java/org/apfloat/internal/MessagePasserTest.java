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
package org.apfloat.internal;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.6
 * @version 1.9.0
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
        MessagePasser<Integer, String> messagePasser = new MessagePasser<>();

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
        MessagePasser<Integer, String> messagePasser = new MessagePasser<>();
        Thread thread = new Thread()
        {
            @Override
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
