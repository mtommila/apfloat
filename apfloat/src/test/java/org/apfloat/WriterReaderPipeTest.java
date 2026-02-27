/*
 * MIT License
 *
 * Copyright (c) 2002-2026 Mikko Tommila
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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.16.0
 * @author Mikko Tommila
 */

public class WriterReaderPipeTest
    extends TestCase
{
    public WriterReaderPipeTest(String methodName)
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

        suite.addTest(new WriterReaderPipeTest("testSuccess"));
        suite.addTest(new WriterReaderPipeTest("testVeryLong"));
        suite.addTest(new WriterReaderPipeTest("testEmpty"));
        suite.addTest(new WriterReaderPipeTest("testNoWrite"));
        suite.addTest(new WriterReaderPipeTest("testWriterInterrupted"));
        suite.addTest(new WriterReaderPipeTest("testReaderInterrupted"));
        suite.addTest(new WriterReaderPipeTest("testEOFInterrupted"));
        suite.addTest(new WriterReaderPipeTest("testCloseReader"));
        suite.addTest(new WriterReaderPipeTest("testCloseWriter"));
        suite.addTest(new WriterReaderPipeTest("testCloseBoth"));

        return suite;
    }

    public static void testSuccess()
        throws IOException
    {
        Reader in = WriterReaderPipe.open(out -> out.write("Hello, world!"));
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = in.read()) != -1)
        {
            buffer.append((char) c);
        }
        assertEquals("Text", "Hello, world!", buffer.toString());
        assertEquals("EOF", -1, in.read());
    }

    public static void testVeryLong()
        throws IOException
    {
        Reader in = WriterReaderPipe.open(out -> out.write(String.join("", Collections.nCopies(10000000, "a"))));
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = in.read()) != -1)
        {
            buffer.append((char) c);
        }
        assertEquals("Text", String.join("", Collections.nCopies(10000000, "a")), buffer.toString());
        assertEquals("EOF", -1, in.read());
    }

    public static void testEmpty()
        throws IOException
    {
        Reader in = WriterReaderPipe.open(out -> out.write(""));
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = in.read()) != -1)
        {
            buffer.append((char) c);
        }
        assertEquals("Text", "", buffer.toString());
        assertEquals("EOF", -1, in.read());
    }

    public static void testNoWrite()
        throws IOException
    {
        Reader in = WriterReaderPipe.open(out -> {});
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = in.read()) != -1)
        {
            buffer.append((char) c);
        }
        assertEquals("Text", "", buffer.toString());
        assertEquals("EOF", -1, in.read());
    }

    public static void testWriterInterrupted()
        throws IOException
    {
        Reader in = WriterReaderPipe.open(out ->
        {
            Thread thread = Thread.currentThread();
            new Thread(() -> {
                sleep(500);
                thread.interrupt();
            }).start();
            sleep(1000);
        });
        try
        {
            in.read();
            fail("Reader didn't fail");
        }
        catch (IOException ioe)
        {
            // Reader should fail
        }
    }

    public static void testReaderInterrupted()
        throws IOException
    {
        AtomicReference<String> text = new AtomicReference<>();
        Reader in = WriterReaderPipe.open(out ->
        {
            sleep(1000);
            try
            {
                out.write(String.join("", Collections.nCopies(10000000, "a")));
            }
            catch (IOException ioe)
            {
                text.set("Writer failed");
            }
        });
        Thread thread = Thread.currentThread();
        new Thread(() -> {
            sleep(500);
            thread.interrupt();
        }).start();
        try
        {
            in.read();
            fail("Reader didn't fail");
        }
        catch (IOException ioe)
        {
            // Reader should fail
        }
        sleepUninterruptibly(1000);
        assertEquals("Writer failure", "Writer failed", text.get());
    }

    public static void testEOFInterrupted()
        throws IOException
    {
        AtomicReference<String> text = new AtomicReference<>();
        Reader in = WriterReaderPipe.open(out ->
        {
            Thread thread = Thread.currentThread();
            new Thread(() -> {
                sleep(500);
                thread.interrupt();
                sleep(250);
                thread.interrupt();
            }).start();
            try
            {
                out.write(String.join("", Collections.nCopies(1048576, "a")));
            }
            catch (IOException ioe)
            {
                text.set("Writer failed");
            }
        });
        sleep(1000);
        try
        {
            while (in.read() != -1);
            fail("Reader didn't fail");
        }
        catch (IOException ioe)
        {
            // Reader should fail
        }
        assertNull("Writer failure", text.get());
    }

    public static void testCloseReader()
        throws IOException
    {
        Reader in = WriterReaderPipe.open(out -> out.write("Hello, world!"));
        try (in)
        {
            StringBuilder buffer = new StringBuilder();
            int c;
            while ((c = in.read()) != -1)
            {
                buffer.append((char) c);
            }
            assertEquals("Text", "Hello, world!", buffer.toString());
        }
        try
        {
            in.read();
            fail("Reader allows reading after closing");
        }
        catch (IOException ioe)
        {
            // Reader is closed
        }
    }

    public static void testCloseWriter()
        throws IOException
    {
        AtomicReference<Writer> writer = new AtomicReference<>();
        Reader in = WriterReaderPipe.open(out ->
        {
            try (out)
            {
                out.write("Hello, world!");
            }
            writer.set(out);
        });
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = in.read()) != -1)
        {
            buffer.append((char) c);
        }
        assertEquals("Text", "Hello, world!", buffer.toString());
        try
        {
            writer.get().write("x");
            fail("Writer allows writing after closing");
        }
        catch (IOException ioe)
        {
            // Writer is closed
        }
    }

    public static void testCloseBoth()
        throws IOException
    {
        AtomicReference<Writer> writer = new AtomicReference<>();
        Reader in = WriterReaderPipe.open(out ->
        {
            try (out)
            {
                out.write("Hello, world!");
            }
            writer.set(out);
        });
        try (in)
        {
            StringBuilder buffer = new StringBuilder();
            int c;
            while ((c = in.read()) != -1)
            {
                buffer.append((char) c);
            }
            assertEquals("Text", "Hello, world!", buffer.toString());
        }
        try
        {
            in.read();
            fail("Reader allows reading after closing");
        }
        catch (IOException ioe)
        {
            // Reader is closed
        }
        try
        {
            writer.get().write("x");
            fail("Writer allows writing after closing");
        }
        catch (IOException ioe)
        {
            // Writer is closed
        }
    }

    private static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException ie)
        {
            throw new RuntimeException(ie);
        }
    }

    private static void sleepUninterruptibly(long millis)
    {
        while (millis > 0)
        {
            long time = System.currentTimeMillis();
            try
            {
                Thread.sleep(millis);
                millis -= System.currentTimeMillis() - time;
            }
            catch (InterruptedException ie)
            {
                // Keep sleeping
            }
        }
    }
}
