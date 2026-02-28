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

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestSuite;

/**
 * @version 1.16.0
 * @author Mikko Tommila
 */

public class BinomialHelperTest
    extends ApfloatTestCase
{
    public BinomialHelperTest(String methodName)
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

        suite.addTest(new BinomialHelperTest("testBinomials"));

        return suite;
    }

    public static void testBinomials()
    {
        Iterator<Apint> binomials = BinomialHelper.binomials(7, 0, 10);
        assertEquals("(7, 0)", new Apint(1), binomials.next());
        assertEquals("(7, 1)", new Apint(7), binomials.next());
        assertEquals("(7, 2)", new Apint(21), binomials.next());
        assertEquals("(7, 3)", new Apint(35), binomials.next());
        assertEquals("(7, 4)", new Apint(35), binomials.next());
        assertEquals("(7, 5)", new Apint(21), binomials.next());
        assertEquals("(7, 6)", new Apint(7), binomials.next());
        assertEquals("(7, 7)", new Apint(1), binomials.next());
        assertFalse("7 next", binomials.hasNext());

        binomials = BinomialHelper.binomials(3, 1, 10);
        assertEquals("(3, 1)", new Apint(3), binomials.next());
        assertEquals("(3, 2)", new Apint(3), binomials.next());
        assertEquals("(3, 3)", new Apint(1), binomials.next());
        assertFalse("3 next", binomials.hasNext());

        binomials = BinomialHelper.binomials(0, 0, 10);
        assertEquals("(0, 0)", new Apint(1), binomials.next());
        assertFalse("0 next", binomials.hasNext());

        binomials = BinomialHelper.binomials(1, 0, 10);
        assertEquals("(1, 0)", new Apint(1), binomials.next());
        assertEquals("(1, 1)", new Apint(1), binomials.next());
        assertFalse("1 next", binomials.hasNext());

        binomials = BinomialHelper.binomials(2, 0, 13);
        assertEquals("(2, 0)", new Apint(1, 13), binomials.next());
        assertEquals("(2, 1)", new Apint(2, 13), binomials.next());
        assertEquals("(2, 2)", new Apint(1, 13), binomials.next());
        assertFalse("2 next", binomials.hasNext());

        try
        {
            binomials.next();
            fail("Going past iterator end accepted");
        }
        catch (NoSuchElementException nsee)
        {
            // OK; at the end of iterator
        }

        try
        {
            BinomialHelper.binomials(-1, 5, 10);
            fail("Negative n accepted");
        }
        catch (AssertionError ae)
        {
            // OK; illegal
        }
        try
        {
            BinomialHelper.binomials(5, -1, 10);
            fail("Negative n accepted");
        }
        catch (AssertionError ae)
        {
            // OK; illegal
        }
    }
}
