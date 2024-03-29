/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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

import org.apfloat.spi.*;

import junit.framework.TestSuite;

/**
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeMemoryArrayAccessTest
    extends RawtypeTestCase
{
    public RawtypeMemoryArrayAccessTest(String methodName)
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

        suite.addTest(new RawtypeMemoryArrayAccessTest("testGet"));
        suite.addTest(new RawtypeMemoryArrayAccessTest("testSubsequence"));

        return suite;
    }

    public static void testGet()
    {
        rawtype[] data = { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 };
        try (ArrayAccess arrayAccess = new RawtypeMemoryArrayAccess(data, 0, 4))
        {
            assertTrue("class", arrayAccess.getData() instanceof rawtype[]);
            assertEquals("[0]", 1, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset()]);
            assertEquals("[1]", 2, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 1]);
            assertEquals("[2]", 3, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 2]);
            assertEquals("[3]", 4, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 3]);
            assertEquals("length", 4, arrayAccess.getLength());
        }
    }

    public static void testSubsequence()
    {
        rawtype[] data = { (rawtype) 1, (rawtype) 2, (rawtype) 3, (rawtype) 4 };
        try (ArrayAccess baseArrayAccess = new RawtypeMemoryArrayAccess(data, 0, 4);
             ArrayAccess arrayAccess = baseArrayAccess.subsequence(1, 2))
        {
            assertTrue("class", arrayAccess.getData() instanceof rawtype[]);
            assertEquals("[0]", 2, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset()]);
            assertEquals("[1]", 3, (int) arrayAccess.getRawtypeData()[arrayAccess.getOffset() + 1]);
            assertEquals("length", 2, arrayAccess.getLength());
        }
    }
}
