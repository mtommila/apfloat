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
package org.apfloat.spi;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apfloat.Apfloat;

/**
 * @version 1.6
 * @author Mikko Tommila
 */

public class UtilTest
    extends TestCase
{
    public UtilTest(String methodName)
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

        suite.addTest(new UtilTest("testRound2down"));
        suite.addTest(new UtilTest("testRound2up"));
        suite.addTest(new UtilTest("testRound23down"));
        suite.addTest(new UtilTest("testRound23up"));
        suite.addTest(new UtilTest("testSqrt4down"));
        suite.addTest(new UtilTest("testSqrt4up"));
        suite.addTest(new UtilTest("testLog2down"));
        suite.addTest(new UtilTest("testLog2up"));
        suite.addTest(new UtilTest("testIfFinite"));

        return suite;
    }

    public static void testRound2down()
    {
        assertEquals("0", 0, Util.round2down(0));
        assertEquals("1", 1, Util.round2down(1));
        assertEquals("2", 2, Util.round2down(2));
        assertEquals("3", 2, Util.round2down(3));
        assertEquals("4", 4, Util.round2down(4));
        assertEquals("5", 4, Util.round2down(5));
        assertEquals("7", 4, Util.round2down(7));
        assertEquals("8", 8, Util.round2down(8));
        assertEquals("9", 8, Util.round2down(9));
        assertEquals("max integer", 1 << 30, Util.round2down(Integer.MAX_VALUE));

        assertEquals("0L", 0, Util.round2down(0L));
        assertEquals("1L", 1, Util.round2down(1L));
        assertEquals("2L", 2, Util.round2down(2L));
        assertEquals("3L", 2, Util.round2down(3L));
        assertEquals("4L", 4, Util.round2down(4L));
        assertEquals("5L", 4, Util.round2down(5L));
        assertEquals("7L", 4, Util.round2down(7L));
        assertEquals("8L", 8, Util.round2down(8L));
        assertEquals("9L", 8, Util.round2down(9L));
        assertEquals("max long", 1L << 62, Util.round2down(Long.MAX_VALUE));
    }

    public static void testRound2up()
    {
        assertEquals("0", 0, Util.round2up(0));
        assertEquals("1", 1, Util.round2up(1));
        assertEquals("2", 2, Util.round2up(2));
        assertEquals("3", 4, Util.round2up(3));
        assertEquals("4", 4, Util.round2up(4));
        assertEquals("5", 8, Util.round2up(5));
        assertEquals("7", 8, Util.round2up(7));
        assertEquals("8", 8, Util.round2up(8));
        assertEquals("9", 16, Util.round2up(9));
        assertEquals("max integer", 1 << 30, Util.round2up(0x20000001));
        assertEquals("max integer", 1 << 30, Util.round2up(0x40000000));

        assertEquals("0L", 0, Util.round2up(0L));
        assertEquals("1L", 1, Util.round2up(1L));
        assertEquals("2L", 2, Util.round2up(2L));
        assertEquals("3L", 4, Util.round2up(3L));
        assertEquals("4L", 4, Util.round2up(4L));
        assertEquals("5L", 8, Util.round2up(5L));
        assertEquals("7L", 8, Util.round2up(7L));
        assertEquals("8L", 8, Util.round2up(8L));
        assertEquals("9L", 16, Util.round2up(9L));
        assertEquals("max long rounded", 1L << 62, Util.round2up(0x2000000000000001L));
        assertEquals("max long", 1L << 62, Util.round2up(0x4000000000000000L));
    }

    public static void testRound23down()
    {
        assertEquals("0", 0, Util.round23down(0));
        assertEquals("1", 1, Util.round23down(1));
        assertEquals("2", 2, Util.round23down(2));
        assertEquals("3", 3, Util.round23down(3));
        assertEquals("4", 4, Util.round23down(4));
        assertEquals("5", 4, Util.round23down(5));
        assertEquals("6", 6, Util.round23down(6));
        assertEquals("7", 6, Util.round23down(7));
        assertEquals("8", 8, Util.round23down(8));
        assertEquals("9", 8, Util.round23down(9));
        assertEquals("max integer", 3 << 29, Util.round23down(Integer.MAX_VALUE));

        assertEquals("0L", 0, Util.round23down(0L));
        assertEquals("1L", 1, Util.round23down(1L));
        assertEquals("2L", 2, Util.round23down(2L));
        assertEquals("3L", 3, Util.round23down(3L));
        assertEquals("4L", 4, Util.round23down(4L));
        assertEquals("5L", 4, Util.round23down(5L));
        assertEquals("6L", 6, Util.round23down(6L));
        assertEquals("7L", 6, Util.round23down(7L));
        assertEquals("8L", 8, Util.round23down(8L));
        assertEquals("9L", 8, Util.round23down(9L));
        assertEquals("max long", 3L << 61, Util.round23down(Long.MAX_VALUE));
    }

    public static void testRound23up()
    {
        assertEquals("0", 0, Util.round23up(0));
        assertEquals("1", 1, Util.round23up(1));
        assertEquals("2", 2, Util.round23up(2));
        assertEquals("3", 3, Util.round23up(3));
        assertEquals("4", 4, Util.round23up(4));
        assertEquals("5", 6, Util.round23up(5));
        assertEquals("6", 6, Util.round23up(6));
        assertEquals("7", 8, Util.round23up(7));
        assertEquals("8", 8, Util.round23up(8));
        assertEquals("9", 12, Util.round23up(9));
        assertEquals("max integer", 3 << 29, Util.round23up(0x40000001));
        assertEquals("max integer", 3 << 29, Util.round23up(0x60000000));

        assertEquals("0L", 0, Util.round23up(0L));
        assertEquals("1L", 1, Util.round23up(1L));
        assertEquals("2L", 2, Util.round23up(2L));
        assertEquals("3L", 3, Util.round23up(3L));
        assertEquals("4L", 4, Util.round23up(4L));
        assertEquals("5L", 6, Util.round23up(5L));
        assertEquals("6L", 6, Util.round23up(6L));
        assertEquals("7L", 8, Util.round23up(7L));
        assertEquals("8L", 8, Util.round23up(8L));
        assertEquals("9L", 12, Util.round23up(9L));
        assertEquals("max long rounded", 3L << 61, Util.round23up(0x4000000000000001L));
        assertEquals("max long", 3L << 61, Util.round23up(0x6000000000000000L));
    }

    public static void testSqrt4down()
    {
        assertEquals("0", 0, Util.sqrt4down(0));
        assertEquals("1", 1, Util.sqrt4down(1));
        assertEquals("2", 1, Util.sqrt4down(2));
        assertEquals("3", 1, Util.sqrt4down(3));
        assertEquals("4", 2, Util.sqrt4down(4));
        assertEquals("5", 2, Util.sqrt4down(5));
        assertEquals("7", 2, Util.sqrt4down(7));
        assertEquals("8", 2, Util.sqrt4down(8));
        assertEquals("9", 2, Util.sqrt4down(9));
        assertEquals("15", 2, Util.sqrt4down(15));
        assertEquals("16", 4, Util.sqrt4down(16));
        assertEquals("17", 4, Util.sqrt4down(17));
        assertEquals("max integer", 0x8000, Util.sqrt4down(Integer.MAX_VALUE));

        assertEquals("0L", 0, Util.sqrt4down(0L));
        assertEquals("1L", 1, Util.sqrt4down(1L));
        assertEquals("2L", 1, Util.sqrt4down(2L));
        assertEquals("3L", 1, Util.sqrt4down(3L));
        assertEquals("4L", 2, Util.sqrt4down(4L));
        assertEquals("5L", 2, Util.sqrt4down(5L));
        assertEquals("7L", 2, Util.sqrt4down(7L));
        assertEquals("8L", 2, Util.sqrt4down(8L));
        assertEquals("9L", 2, Util.sqrt4down(9L));
        assertEquals("15L", 2, Util.sqrt4down(15L));
        assertEquals("16L", 4, Util.sqrt4down(16L));
        assertEquals("17L", 4, Util.sqrt4down(17L));
        assertEquals("max long", 0x80000000L, Util.sqrt4down(Long.MAX_VALUE));
    }

    public static void testSqrt4up()
    {
        assertEquals("0", 0, Util.sqrt4up(0));
        assertEquals("1", 1, Util.sqrt4up(1));
        assertEquals("2", 2, Util.sqrt4up(2));
        assertEquals("3", 2, Util.sqrt4up(3));
        assertEquals("4", 2, Util.sqrt4up(4));
        assertEquals("5", 4, Util.sqrt4up(5));
        assertEquals("7", 4, Util.sqrt4up(7));
        assertEquals("8", 4, Util.sqrt4up(8));
        assertEquals("9", 4, Util.sqrt4up(9));
        assertEquals("15", 4, Util.sqrt4up(15));
        assertEquals("16", 4, Util.sqrt4up(16));
        assertEquals("17", 8, Util.sqrt4up(17));
        assertEquals("max integer", 0x10000, Util.sqrt4up(Integer.MAX_VALUE));

        assertEquals("0L", 0, Util.sqrt4up(0L));
        assertEquals("1L", 1, Util.sqrt4up(1L));
        assertEquals("2L", 2, Util.sqrt4up(2L));
        assertEquals("3L", 2, Util.sqrt4up(3L));
        assertEquals("4L", 2, Util.sqrt4up(4L));
        assertEquals("5L", 4, Util.sqrt4up(5L));
        assertEquals("7L", 4, Util.sqrt4up(7L));
        assertEquals("8L", 4, Util.sqrt4up(8L));
        assertEquals("9L", 4, Util.sqrt4up(9L));
        assertEquals("15L", 4, Util.sqrt4up(15L));
        assertEquals("16L", 4, Util.sqrt4up(16L));
        assertEquals("17L", 8, Util.sqrt4up(17L));
        assertEquals("max long", 0x100000000L, Util.sqrt4up(Long.MAX_VALUE));
    }

    public static void testLog2down()
    {
        assertEquals("1", 0, Util.log2down(1));
        assertEquals("2", 1, Util.log2down(2));
        assertEquals("3", 1, Util.log2down(3));
        assertEquals("4", 2, Util.log2down(4));
        assertEquals("5", 2, Util.log2down(5));
        assertEquals("7", 2, Util.log2down(7));
        assertEquals("8", 3, Util.log2down(8));
        assertEquals("9", 3, Util.log2down(9));
        assertEquals("max integer", 30, Util.log2down(Integer.MAX_VALUE));

        assertEquals("1L", 0, Util.log2down(1L));
        assertEquals("2L", 1, Util.log2down(2L));
        assertEquals("3L", 1, Util.log2down(3L));
        assertEquals("4L", 2, Util.log2down(4L));
        assertEquals("5L", 2, Util.log2down(5L));
        assertEquals("7L", 2, Util.log2down(7L));
        assertEquals("8L", 3, Util.log2down(8L));
        assertEquals("9L", 3, Util.log2down(9L));
        assertEquals("max long", 62, Util.log2down(Long.MAX_VALUE));
    }

    public static void testLog2up()
    {
        assertEquals("1", 0, Util.log2up(1));
        assertEquals("2", 1, Util.log2up(2));
        assertEquals("3", 2, Util.log2up(3));
        assertEquals("4", 2, Util.log2up(4));
        assertEquals("5", 3, Util.log2up(5));
        assertEquals("7", 3, Util.log2up(7));
        assertEquals("8", 3, Util.log2up(8));
        assertEquals("9", 4, Util.log2up(9));
        assertEquals("max integer", 31, Util.log2up(Integer.MAX_VALUE));

        assertEquals("1L", 0, Util.log2up(1L));
        assertEquals("2L", 1, Util.log2up(2L));
        assertEquals("3L", 2, Util.log2up(3L));
        assertEquals("4L", 2, Util.log2up(4L));
        assertEquals("5L", 3, Util.log2up(5L));
        assertEquals("7L", 3, Util.log2up(7L));
        assertEquals("8L", 3, Util.log2up(8L));
        assertEquals("9L", 4, Util.log2up(9L));
        assertEquals("max long", 63, Util.log2up(Long.MAX_VALUE));
    }

    public static void testIfFinite()
    {
        assertEquals("Finite", 2, Util.ifFinite(1, 2));
        assertEquals("Infinite", Apfloat.INFINITE, Util.ifFinite(Apfloat.INFINITE, 2));
    }
}
