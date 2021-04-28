/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
