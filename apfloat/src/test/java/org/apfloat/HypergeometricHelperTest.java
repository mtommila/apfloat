/*
 * MIT License
 *
 * Copyright (c) 2002-2024 Mikko Tommila
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
 * @version 1.13.0
 * @author Mikko Tommila
 */

public class HypergeometricHelperTest
    extends ApfloatTestCase
{
    public HypergeometricHelperTest(String methodName)
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

        suite.addTest(new HypergeometricHelperTest("testHypergeometricPFQ"));

        return suite;
    }

    public static void testHypergeometricPFQ()
    {
        // 3F2
        Apcomplex a = HypergeometricHelper.hypergeometricPFQ(new Apcomplex[] { new Apcomplex("1.00000"), new Apcomplex("1.00000"), new Apcomplex("1.00000") }, new Apcomplex[] { new Apcomplex("2.00000"), new Apcomplex("2.00000") }, new Apcomplex("0.100000"));    // p = q + 1, this is actually Li_2(z) / z
        assertEquals("1, 1, 1; 2, 2; 0.1 precision", 6, a.precision());
        assertEquals("1, 1, 1; 2, 2; 0.1 value", new Apcomplex("1.02618"), a, new Apfloat("5e-5"));

        // z = 0
        a = HypergeometricHelper.hypergeometricPFQ(new Apcomplex[] { new Apcomplex("(2,3)"), new Apcomplex("(4,5)"), new Apcomplex("(6,7)") }, new Apcomplex[] {}, new Apcomplex("0"));    // p > q + 1
        assertEquals("(2,3), (4,5), (6,7);; 0 precision", 1, a.precision());
        assertEquals("(2,3), (4,5), (6,7);; 0 value", new Apcomplex("1"), a);

        // Polynomial case
        a = HypergeometricHelper.hypergeometricPFQ(new Apcomplex[] { new Apcomplex("-1.00000"), new Apcomplex("2.00000"), new Apcomplex("3.00000") }, new Apcomplex[] { new Apcomplex("4.00000") }, new Apcomplex("5.00000"));    // p > q + 1
        assertEquals("-1, 2, 3; 4; 5 precision", 6, a.precision());
        assertEquals("-1, 2, 3; 4; 5 value", new Apcomplex("-6.50000"), a, new Apfloat("5e-5"));

        // Reduction to 2F1
        a = HypergeometricHelper.hypergeometricPFQ(new Apcomplex[] { new Apcomplex("1.00000"), new Apcomplex("42.0000"), new Apcomplex("2.20000") }, new Apcomplex[] { new Apcomplex("3.30000"), new Apcomplex("42.0000") }, new Apcomplex("5.00000"));
        assertEquals("1, 42, 2.2; 3.3, 42; 5 precision", 5, a.precision());
        assertEquals("1, 42, 2.2; 3.3, 42; 5 value", new Apcomplex("(-0.527130,-0.228019)"), a, new Apfloat("5e-6"));

        // Non converging cases
        try
        {
            HypergeometricHelper.hypergeometricPFQ(new Apcomplex[] { new Apcomplex("1.00000"), new Apcomplex("42.0000"), new Apcomplex("2.20000") }, new Apcomplex[] { new Apcomplex("3.30000") }, new Apcomplex("0.10000"));
            fail("p > q + 1 accepted with z != 0 and not polynomial");
        }
        catch (ArithmeticException ae)
        {
            // OK, result is infinite
        }
    }

    public static void testHypergeometricPFQRegularized()
    {
        Apcomplex a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("3.00000"), new Apcomplex("3.00000") }, new Apcomplex[] { new Apcomplex("4.00000"), new Apcomplex("4.00000"), new Apcomplex("4.00000") }, new Apcomplex("0.100000"));
        assertEquals("3, 3; 4, 4, 4; 0.1 precision", 6, a.precision());
        assertEquals("3, 3; 4, 4, 4; 0.1 value", new Apcomplex("0.00469515"), a, new Apfloat("5e-8"));

        a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("0"), new Apcomplex("3.00000") }, new Apcomplex[] { new Apcomplex("4.00000"), new Apcomplex("4.00000"), new Apcomplex("4.00000") }, new Apcomplex("0.100000"));
        assertEquals("0, 3; 4, 4, 4; 0.1 precision", 6, a.precision());
        assertEquals("0, 3; 4, 4, 4; 0.1 value", new Apcomplex("0.00462963"), a, new Apfloat("5e-8"));

        a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("3.00000"), new Apcomplex("0") }, new Apcomplex[] { new Apcomplex("4.00000"), new Apcomplex("4.00000"), new Apcomplex("4.00000") }, new Apcomplex("0.100000"));
        assertEquals("3, 0; 4, 4, 4; 0.1 precision", 6, a.precision());
        assertEquals("3, 0; 4, 4, 4; 0.1 value", new Apcomplex("0.00462963"), a, new Apfloat("5e-8"));

        a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("3.000000"), new Apcomplex("3.000000") }, new Apcomplex[] { new Apcomplex("-4.000000"), new Apcomplex("4.000000"), new Apcomplex("4.000000") }, new Apcomplex("0.1000000"));
        assertEquals("3, 3; -4, 4, 4; 0.1 precision", 6, a.precision());
        assertEquals("3, 3; -4, 4, 4; 0.1 value", new Apcomplex("3.29832e-10"), a, new Apfloat("5e-15"));

        a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("0"), new Apcomplex("3.00000") }, new Apcomplex[] { new Apcomplex("-4.00000"), new Apcomplex("4.00000"), new Apcomplex("4.00000") }, new Apcomplex("0.100000"));
        assertEquals("0, 3; -4, 4, 4; 0.1 precision", Apfloat.INFINITE, a.precision());
        assertEquals("0, 3; -4, 4, 4; 0.1 value", new Apcomplex("0"), a);

        a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("(2.00000,3.00000)"), new Apcomplex("(3.00000,4.00000)") }, new Apcomplex[] { new Apcomplex("-4.00000"), new Apcomplex("(4.00000,5.00000)"), new Apcomplex("(5.00000,6.00000)") }, new Apcomplex("(0.100000,0.200000)"));
        assertEquals("2+3i, 3+4i; -4, 4+5i, 5+6i; 0.1+0.2i precision", 6, a.precision());
        assertEquals("2+3i, 3+4i; -4, 4+5i, 5+6i; 0.1+0.2i value", new Apcomplex("(2.39246e-7,4.75224e-8)"), a, new Apfloat("5e-12"));

        a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("(2.00000,3.00000)"), new Apcomplex("(3.00000,4.00000)") }, new Apcomplex[] { new Apcomplex("-4.00000"), new Apcomplex("-5.00000"), new Apcomplex("(5.00000,6.00000)") }, new Apcomplex("(0.100000,0.200000)"));
        assertEquals("2+3i, 3+4i; -4, -5, 5+6i; 0.1+0.2i precision", 6, a.precision());
        assertEquals("2+3i, 3+4i; -4, -5, 5+6i; 0.1+0.2i value", new Apcomplex("(0.000531945,0.0000629876 )"), a, new Apfloat("5e-9"));

        a = HypergeometricHelper.hypergeometricPFQRegularized(new Apcomplex[] { new Apcomplex("(2.00000,3.00000)"), new Apcomplex("(3.00000,4.00000)") }, new Apcomplex[] { new Apcomplex("-5.00000"), new Apcomplex("-4.00000"), new Apcomplex("(5.00000,6.00000)") }, new Apcomplex("(0.100000,0.200000)"));
        assertEquals("2+3i, 3+4i; -5, -4, 5+6i; 0.1+0.2i precision", 6, a.precision());
        assertEquals("2+3i, 3+4i; -5, -4, 5+6i; 0.1+0.2i value", new Apcomplex("(0.000531945,0.0000629876 )"), a, new Apfloat("5e-9"));
    }
}
