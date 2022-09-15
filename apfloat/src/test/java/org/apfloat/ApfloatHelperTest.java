/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
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

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.10.1
 * @author Mikko Tommila
 */

public class ApfloatHelperTest
    extends TestCase
{
    public ApfloatHelperTest(String methodName)
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

        suite.addTest(new ApfloatHelperTest("testGetMatchingPrecisions2"));
        suite.addTest(new ApfloatHelperTest("testGetMatchingPrecisions4"));
        suite.addTest(new ApfloatHelperTest("testLimitPrecision"));
        suite.addTest(new ApfloatHelperTest("testEnsurePrecision"));
        suite.addTest(new ApfloatHelperTest("testExtendPrecision"));
        suite.addTest(new ApfloatHelperTest("testReducePrecision"));
        suite.addTest(new ApfloatHelperTest("testSetPrecision"));

        return suite;
    }

    public static void testGetMatchingPrecisions2()
    {
        long[] precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(12345678, 5), new Apfloat(12345, 5));

        assertEquals("5-2 [0]", 5, precisions[0]);
        assertEquals("5-2 [1]", 2, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(12345678, 10), new Apfloat(12345, 5));

        assertEquals("8-5 [0]", 8, precisions[0]);
        assertEquals("8-5 [1]", 5, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(0), new Apfloat(12345, 5));

        assertEquals("0-0 [0]", 0, precisions[0]);
        assertEquals("0-0 [1]", 0, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(12345678, 6), new Apfloat(12, 2));

        assertEquals("6-0 [0]", 6, precisions[0]);
        assertEquals("6-0 [1]", 0, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(12345678, 5), new Apfloat(12, 2));

        assertEquals("5-0 [0]", 5, precisions[0]);
        assertEquals("5-0 [1]", 0, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(10), new Apfloat(100, 2));

        assertEquals("10, 100 2 [0]", 1, precisions[0]);
        assertEquals("10, 100 2 [1]", 2, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(10), new Apfloat(100, 3));

        assertEquals("10, 100 3 [0]", 2, precisions[0]);
        assertEquals("10, 100 3 [1]", 3, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(10), new Apfloat(1, 2));

        assertEquals("10, 1 2 [0]", 3, precisions[0]);
        assertEquals("10, 1 2 [1]", 2, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(10), new Apfloat(1, 3));

        assertEquals("10, 1 3 [0]", 4, precisions[0]);
        assertEquals("10, 1 3 [1]", 3, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(10), new Apfloat(10));

        assertEquals("10, 10 MAX [0]", Apfloat.INFINITE, precisions[0]);
        assertEquals("10, 10 MAX [1]", Apfloat.INFINITE, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(10), new Apfloat(100));

        assertEquals("10, 100 MAX [0]", Apfloat.INFINITE, precisions[0]);
        assertEquals("10, 100 MAX [1]", Apfloat.INFINITE, precisions[1]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat("1e9000000000000000000", Apfloat.INFINITE), new Apfloat("1e-9000000000000000000", Apfloat.INFINITE));

        assertEquals("MAX-MIN [0]", Apfloat.INFINITE, precisions[0]);
        assertEquals("MAX-MIN [1]", 0, precisions[1]);
    }

    public static void testGetMatchingPrecisions4()
    {
        // xxxxx000 * xxxxx + xxx00000 * xxx0 =
        // xxxxxx0000000 + xxxx00000000 =
        // xxxxx00000000 + xxxx00000000 =
        // xxxx000000000 / xxxx00000000
        long[] precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(12345678, 8), new Apfloat(12345, 5), new Apfloat(12345678, 5), new Apfloat(1234, 3));

        assertEquals("5-4-4 [0]", 5, precisions[0]);
        assertEquals("5-4-4 [1]", 4, precisions[1]);
        assertEquals("5-4-4 [2]", 4, precisions[2]);

        // xxxxx000 * xxxxx + x * x =
        // xxxxxx0000000 + xx =
        // xxxxx00000000 / xxxxx0000000
        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat(12345678, 8), new Apfloat(12345, 5), new Apfloat(1, 1), new Apfloat(2, 1));

        assertEquals("6-0-5 [0]", 6, precisions[0]);
        assertEquals("6-0-5 [1]", 0, precisions[1]);
        assertEquals("6-0-5 [2]", 5, precisions[2]);

        precisions = ApfloatHelper.getMatchingPrecisions(new Apfloat("1e4500000000000000000", Apfloat.INFINITE), new Apfloat("1e4500000000000000000", Apfloat.INFINITE), new Apfloat("1e-4500000000000000000", Apfloat.INFINITE), new Apfloat("1e-4500000000000000000", Apfloat.INFINITE));

        assertEquals("MAX-0-MAX [0]", Apfloat.INFINITE, precisions[0]);
        assertEquals("MAX-0-MAX [1]", 0, precisions[1]);
        assertEquals("MAX-0-MAX [2]", Apfloat.INFINITE, precisions[2]);
    }

    public static void testLimitPrecision()
    {
        Apfloat x = ApfloatHelper.limitPrecision(new Apfloat("1.23"), 2);
        assertEquals("1.23, 2 value", new Apfloat("1.2"), x);
        assertEquals("1.23, 2 precision", 2, x.precision());
        x = ApfloatHelper.limitPrecision(new Apfloat("1.23"), 4);
        assertEquals("1.23, 4 value", new Apfloat("1.23"), x);
        assertEquals("1.23, 4 precision", 3, x.precision());
        Apcomplex z = ApfloatHelper.limitPrecision(new Apcomplex("(1.23,4.56)"), 2);
        assertEquals("(1.23, 4.56), 2 value", new Apcomplex("(1.2,4.5)"), z);
        assertEquals("(1.23, 4.56), 2 precision", 2, z.precision());
        z = ApfloatHelper.limitPrecision(new Apcomplex("(1.23,4.56)"), 4);
        assertEquals("(1.23, 4.56), 4 value", new Apcomplex("(1.23,4.56)"), z);
        assertEquals("(1.23, 4.56), 4 precision", 3, z.precision());
    }

    public static void testEnsurePrecision()
    {
        Apfloat x = ApfloatHelper.ensurePrecision(new Apfloat("1.23"), 2);
        assertEquals("1.23, 2 value", new Apfloat("1.23"), x);
        assertEquals("1.23, 2 precision", 3, x.precision());
        x = ApfloatHelper.ensurePrecision(new Apfloat("1.23"), 4);
        assertEquals("1.23, 4 value", new Apfloat("1.23"), x);
        assertEquals("1.23, 4 precision", 4, x.precision());
        Apcomplex z = ApfloatHelper.ensurePrecision(new Apcomplex("(1.23,4.56)"), 2);
        assertEquals("(1.23, 4.56), 2 value", new Apcomplex("(1.23,4.56)"), z);
        assertEquals("(1.23, 4.56), 2 precision", 3, z.precision());
        z = ApfloatHelper.ensurePrecision(new Apcomplex("(1.23,4.56)"), 4);
        assertEquals("(1.23, 4.56), 4 value", new Apcomplex("(1.23,4.56)"), z);
        assertEquals("(1.23, 4.56), 4 precision", 4, z.precision());
    }

    public static void testExtendPrecision()
    {
        Apfloat x = ApfloatHelper.extendPrecision(new Apfloat("1.23"), 2);
        assertEquals("1.23, 2 value", new Apfloat("1.23"), x);
        assertEquals("1.23, 2 precision", 5, x.precision());
        x = ApfloatHelper.extendPrecision(new Apfloat("1.23"));
        assertEquals("1.23 value", new Apfloat("1.23"), x);
        assertEquals("1.23 precision", 3 + Apfloat.EXTRA_PRECISION, x.precision());
        Apcomplex z = ApfloatHelper.extendPrecision(new Apcomplex("(1.23,4.56)"), 2);
        assertEquals("(1.23, 4.56), 2 value", new Apcomplex("(1.23,4.56)"), z);
        assertEquals("(1.23, 4.56), 2 precision", 5, z.precision());
        z = ApfloatHelper.extendPrecision(new Apcomplex("(1.23,4.56)"));
        assertEquals("(1.23, 4.56) value", new Apcomplex("(1.23,4.56)"), z);
        assertEquals("(1.23, 4.56) precision", 3 + Apcomplex.EXTRA_PRECISION, z.precision());
    }

    public static void testReducePrecision()
    {
        Apfloat x = ApfloatHelper.reducePrecision(new Apfloat("1.23").precision(3 + Apfloat.EXTRA_PRECISION));
        assertEquals("1.23 value", new Apfloat("1.23"), x);
        assertEquals("1.23 precision", 3, x.precision());
        Apcomplex z = ApfloatHelper.reducePrecision(new Apcomplex("(1.23,4.56)").precision(3 + Apcomplex.EXTRA_PRECISION));
        assertEquals("(1.23, 4.56) value", new Apcomplex("(1.23,4.56)"), z);
        assertEquals("(1.23, 4.56) precision", 3, z.precision());

        try
        {
            ApfloatHelper.reducePrecision(new Apfloat("1.23").precision(Apfloat.EXTRA_PRECISION));
            fail("1.23 accepted");
        }
        catch (LossOfPrecisionException lope)
        {
            // OK
        }
        try
        {
            ApfloatHelper.reducePrecision(new Apcomplex("(1.23,4.56)").precision(Apcomplex.EXTRA_PRECISION));
            fail("(1.23, 4.56) accepted");
        }
        catch (LossOfPrecisionException lope)
        {
            // OK
        }
    }

    public static void testSetPrecision()
    {
        Apcomplex a = ApfloatHelper.setPrecision(new Apcomplex(new Apfloat(2), Apfloat.ZERO), 20);
        assertEquals("(2, 0) real prec", 20, a.real().precision());
        assertEquals("(2, 0) real value", new Apfloat(2, 20), a.real());
        assertEquals("(2, 0) imag prec", Apfloat.INFINITE, a.imag().precision());
        assertEquals("(2, 0) imag value", Apfloat.ZERO, a.imag());
        assertEquals("(2, 0) prec", 20, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex(Apfloat.ZERO, new Apfloat(2)), 20);
        assertEquals("(0, 2) real prec", Apfloat.INFINITE, a.real().precision());
        assertEquals("(0, 2) real value", Apfloat.ZERO, a.real());
        assertEquals("(0, 2) imag prec", 20, a.imag().precision());
        assertEquals("(0, 2) imag value", new Apfloat(2, 20), a.imag());
        assertEquals("(0, 2) prec", 20, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex("(0.0000000000123, 1.123456789012345)"), 11);
        assertEquals("(0.0000000000123, 1.123456789012345) real prec", Apfloat.INFINITE, a.real().precision());
        assertEquals("(0.0000000000123, 1.123456789012345) real value", Apfloat.ZERO, a.real());
        assertEquals("(0.0000000000123, 1.123456789012345) imag prec", 11, a.imag().precision());
        assertEquals("(0.0000000000123, 1.123456789012345) imag value", new Apfloat("1.1234567890"), a.imag());
        assertEquals("(0.0000000000123, 1.123456789012345) prec", 11, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex("(1.123456789012345, 0.0000000000123)"), 11);
        assertEquals("(1.123456789012345, 0.0000000000123) real prec", 11, a.real().precision());
        assertEquals("(1.123456789012345, 0.0000000000123) real real value", new Apfloat("1.1234567890"), a.real());
        assertEquals("(1.123456789012345, 0.0000000000123) imag prec", Apfloat.INFINITE, a.imag().precision());
        assertEquals("(1.123456789012345, 0.0000000000123) imag value", Apfloat.ZERO, a.imag());
        assertEquals("(1.123456789012345, 0.0000000000123) prec", 11, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex("(0.0000000000987, 1.098765432109876)"), 13);
        assertEquals("(0.0000000000987, 1.098765432109876) real prec", 2, a.real().precision());
        assertEquals("(0.0000000000987, 1.098765432109876) real value", new Apfloat("0.000000000098"), a.real());
        assertEquals("(0.0000000000987, 1.098765432109876) imag prec", 15, a.imag().precision());
        assertEquals("(0.0000000000987, 1.098765432109876) imag value", new Apfloat("1.09876543210987"), a.imag());
        assertEquals("(0.0000000000987, 1.098765432109876) prec", 13, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex("(1.098765432109876, 0.0000000000987)"), 13);
        assertEquals("(1.098765432109876, 0.0000000000987) real prec", 15, a.real().precision());
        assertEquals("(1.098765432109876, 0.0000000000987) real real value", new Apfloat("1.09876543210987"), a.real());
        assertEquals("(1.098765432109876, 0.0000000000987) imag prec", 2, a.imag().precision());
        assertEquals("(1.098765432109876, 0.0000000000987) imag value", new Apfloat("0.000000000098"), a.imag());
        assertEquals("(1.098765432109876, 0.0000000000987) prec", 13, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex(new Apfloat(111), new Apfloat("22.2")), 10);
        assertEquals("(111, 22.2) real prec", Apfloat.INFINITE, a.real().precision());
        assertEquals("(111, 22.2) real value", new Apfloat(111), a.real());
        assertEquals("(111, 22.2) imag prec", 9, a.imag().precision());
        assertEquals("(111, 22.2) imag value", new Apfloat("22.2"), a.imag());
        assertEquals("(111, 22.2) prec", 10, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex(new Apfloat("22.2"), new Apfloat(111)), 10);
        assertEquals("(22.2, 111) real prec", 9, a.real().precision());
        assertEquals("(22.2, 111) real real value", new Apfloat("22.2"), a.real());
        assertEquals("(22.2, 111) imag prec", Apfloat.INFINITE, a.imag().precision());
        assertEquals("(22.2, 111) imag value", new Apfloat(111), a.imag());
        assertEquals("(22.2, 111) prec", 10, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex("(22, 3.33)"), 5);
        assertEquals("(22, 3.33) real prec", 5, a.real().precision());
        assertEquals("(22, 3.33) real real value", new Apfloat("22"), a.real());
        assertEquals("(22, 3.33) imag prec", 6, a.imag().precision());
        assertEquals("(22, 3.33) imag value", new Apfloat("3.33"), a.imag());
        assertEquals("(22, 3.33) prec", 5, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex("(3.33, 22)"), 5);
        assertEquals("(3.33, 22) real prec", 6, a.real().precision());
        assertEquals("(3.33, 22) real value", new Apfloat("3.33"), a.real());
        assertEquals("(3.33, 22) imag prec", 5, a.imag().precision());
        assertEquals("(3.33, 22) imag value", new Apfloat("22"), a.imag());
        assertEquals("(3.33, 22) prec", 5, a.precision());

        a = ApfloatHelper.setPrecision(new Apcomplex(new Apfloat(1, 1, 12), new Apfloat(12, 2, 12)), 1);
        assertEquals("(1, 12) real radix", 12, a.real().radix());
        a = ApfloatHelper.setPrecision(new Apcomplex(new Apfloat(12, 2, 12), new Apfloat(1, 1, 12)), 1);
        assertEquals("(12, 1) imag radix", 12, a.imag().radix());
    }
}
