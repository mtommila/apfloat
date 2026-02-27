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

import java.util.Arrays;

import junit.framework.TestSuite;

/**
 * @version 1.16.0
 * @author Mikko Tommila
 */

public class SplitterTest
    extends ApfloatTestCase
{
    public SplitterTest(String methodName)
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

        suite.addTest(new SplitterTest("testCombine"));
        suite.addTest(new SplitterTest("testSplit"));

        return suite;
    }

    public static void testCombine()
    {
        Apint[] a = { new Apint(2), new Apint(3), new Apint(4) };
        assertEquals("2, 3, 4 stride 5", new Apint("40000300002"), Splitter.combine(5, a));

        a = new Apint[] { new Apint(2), new Apint(3), new Apint(4) };
        assertEquals("2, 3, 4 stride 1", new Apint("432"), Splitter.combine(1, a));

        a = new Apint[] { new Apint(2), new Apint(0), new Apint(4) };
        assertEquals("2, 0, 4 stride 1", new Apint("402"), Splitter.combine(1, a));

        a = new Apint[] { new Apint(0), new Apint(1) };
        assertEquals("0, 1 stride 2", new Apint("100"), Splitter.combine(2, a));

        a = new Apint[] { new Apint(0) };
        assertEquals("0 stride 1", new Apint("0"), Splitter.combine(1, a));

        a = new Apint[] { new Apint("a", 16), new Apint("e", 16), new Apint("f", 16) };
        assertEquals("a, e, f stride 2", new Apint("f0e0a", 16), Splitter.combine(2, a));
    }

    public static void testSplit()
    {
        assertEquals("2, 3, 4 stride 5", Arrays.asList(new Apint(2), new Apint(3), new Apint(4)), Arrays.asList(Splitter.split(5, new Apint("40000300002"))));
        assertEquals("2, 3, 4 stride 1", Arrays.asList(new Apint(2), new Apint(3), new Apint(4)), Arrays.asList(Splitter.split(1, new Apint("432"))));
        assertEquals("2, 0, 4 stride 1", Arrays.asList(new Apint(2), new Apint(0), new Apint(4)), Arrays.asList(Splitter.split(1, new Apint("402"))));
        assertEquals("0, 1 stride 2", Arrays.asList(new Apint(0), new Apint(1)), Arrays.asList(Splitter.split(2, new Apint("100"))));
        assertEquals("0 stride 1", Arrays.asList(new Apint(0)), Arrays.asList(Splitter.split(1, new Apint("0"))));
        assertEquals("a, e, f stride 2", Arrays.asList(new Apint("a", 16), new Apint("e", 16), new Apint("f", 16)), Arrays.asList(Splitter.split(2, new Apint("f0e0a", 16))));
    }
}
