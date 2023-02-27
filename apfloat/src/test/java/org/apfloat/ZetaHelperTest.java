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
package org.apfloat;

import junit.framework.TestSuite;

/**
 * @version 1.11.0
 * @author Mikko Tommila
 */

public class ZetaHelperTest
    extends ApfloatTestCase
{
    public ZetaHelperTest(String methodName)
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

        suite.addTest(new ZetaHelperTest("testZetafast"));

        return suite;
    }

    public static void testZetafast()
    {
        Apcomplex a = new ZetaHelper().zetafast(new Apcomplex("(0,30.00000)"));
        assertEquals("0+10i precision", 6, a.precision());
        assertEquals("0+10i value", new Apcomplex("(-1.26512,-0.60232)"), a, new Apfloat("5e-5"));

        a = new ZetaHelper().zetafast(new Apcomplex("(0.50000,1000.00000)"));
        assertEquals("0.5+1000i precision", 5, a.precision());
        assertEquals("0.5+1000i value", new Apcomplex("(0.356334,0.931998)"), a, new Apfloat("5e-5"));

        a = new ZetaHelper().zetafast(new Apcomplex("(1.00000,0.00100000)"));
        assertEquals("1+0.001i precision", 6, a.precision());
        assertEquals("1+0.001i value", new Apcomplex("(0.577216,-1000.00)"), a, new Apfloat("5e-2"));

        a = new ZetaHelper().zetafast(new Apcomplex(new Apfloat("0.5", 100), new Apfloat(1000, 104)));
        assertEquals("0.5+1000i precision 100", 100, a.precision());
        assertEquals("0.5+1000i value 100", new Apcomplex("(0.3563343671943960550744024767110296418750462109065525137341055161422510305547050764059845122070513740,0.9319978312329936651150604327370560741603548016645680162344141200846918466728345537721968349602390048)"), a, new Apfloat("5e-100"));

        /* These tests are quite slow
        a = new ZetaHelper().zetafast(new Apcomplex(new Apfloat("0.5", 100), new Apfloat(10000, 105)));
        assertEquals("0.5+10000i precision", 100, a.precision());
        assertEquals("0.5+10000i value", new Apcomplex("(-0.3393738026388344575674710779459893805666468101906410889337680247470798756615454924994040171851332603,-0.0370915059732060314743442068130120234022523694433894137028295017082175782077688242563900270774953535)"), a, new Apfloat("5e-100"));
        */

        a = new ZetaHelper().zetafast(new Apcomplex(new Apfloat(2, 6, 11), new Apfloat(3, 6, 11)));
        assertEquals("2+3i radix 11 precision", 6, a.precision());
        assertEquals("2+3i radix 11 value", new Apcomplex(new Apfloat("0.886193", 6, 11), new Apfloat("-0.128437", 6, 11)), a, new Apfloat("5e-6", 1, 11));
    }
}
