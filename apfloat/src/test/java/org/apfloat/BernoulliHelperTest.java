/*
 * MIT License
 *
 * Copyright (c) 2002-2025 Mikko Tommila
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
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class BernoulliHelperTest
    extends ApfloatTestCase
{
    public BernoulliHelperTest(String methodName)
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

        suite.addTest(new BernoulliHelperTest("testBernoulliApfloat"));
        suite.addTest(new BernoulliHelperTest("testBernoulliAprational"));

        return suite;
    }

    public static void testBernoulliApfloat()
    {
        Iterator<Apfloat> iterator = BernoulliHelper.bernoullis(4, 6, 10);
        assertEquals("iterator hasNext", true, iterator.hasNext());
        assertEquals("iterator next 1", new Apfloat("1.00000"), iterator.next(), new Apfloat("1e-5"));
        assertEquals("iterator next 2", new Apfloat("-0.500000"), iterator.next(), new Apfloat("1e-6"));
        assertEquals("iterator next 3", new Apfloat("0.166666"), iterator.next(), new Apfloat("1e-6"));
        assertEquals("iterator next 4", new Apfloat("0"), iterator.next());
        assertEquals("iterator next 5", new Apfloat("-0.0333333"), iterator.next(), new Apfloat("1e-7"));

        iterator = BernoulliHelper.bernoullisSmall(50, 10);
        assertEquals("iteratorSmall hasNext", true, iterator.hasNext());
        assertEquals("iteratorSmall next 1", new Apfloat("1.0000000000000000000000000000000000000000000000000"), iterator.next(), new Apfloat("1e-49"));
        assertEquals("iteratorSmall next 2", new Apfloat("-0.50000000000000000000000000000000000000000000000000"), iterator.next(), new Apfloat("1e-50"));
        assertEquals("iteratorSmall next 3", new Apfloat("0.16666666666666666666666666666666666666666666666666"), iterator.next(), new Apfloat("1e-50"));
        assertEquals("iteratorSmall next 4", new Apfloat("0"), iterator.next());
        assertEquals("iteratorSmall next 5", new Apfloat("-0.033333333333333333333333333333333333333333333333333"), iterator.next(), new Apfloat("1e-51"));
        assertEquals("iteratorSmall hasNext 2", true, iterator.hasNext());

        iterator = BernoulliHelper.bernoullisBig(4, 20, 10);
        assertEquals("iteratorBig hasNext", true, iterator.hasNext());
        assertEquals("iteratorBig next 1", new Apfloat("1.0000000000000000000"), iterator.next(), new Apfloat("1e-19"));
        assertEquals("iteratorBig next 2", new Apfloat("-0.50000000000000000000"), iterator.next(), new Apfloat("1e-20"));
        assertEquals("iteratorBig next 3", new Apfloat("0.16666666666666666666666666666666666666666666666666"), iterator.next(), new Apfloat("1e-20"));
        assertEquals("iteratorBig next 4", new Apfloat("0"), iterator.next());
        assertEquals("iteratorBig next 5", new Apfloat("-0.033333333333333333333333333333333333333333333333333"), iterator.next(), new Apfloat("1e-21"));
        assertEquals("iteratorBig hasNext 2", false, iterator.hasNext());

        try
        {
            iterator.next();
            fail("iteratorBig allowed next");
        }
        catch (NoSuchElementException nsee)
        {
            // OK
        }

        iterator = BernoulliHelper.bernoullis(4, 5, 15);
        assertEquals("iterator radix 15 next 1", new Apfloat("1.0000", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.0001", 1, 15));
        assertEquals("iterator radix 15 next 2", new Apfloat("-0.77777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.00001", 1, 15));
        assertEquals("iterator radix 15 next 3", new Apfloat("0.27777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.00001", 1, 15));
        assertEquals("iterator radix 15 next 4", new Apfloat("0", Apfloat.DEFAULT, 15), iterator.next());
        assertEquals("iterator radix 15 next 5", new Apfloat("-0.077777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.000001", 1, 15));

        iterator = BernoulliHelper.bernoullisSmall(5, 15);
        assertEquals("iteratorSmall radix 15 next 1", new Apfloat("1.0000", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.0001", 1, 15));
        assertEquals("iteratorSmall radix 15 next 2", new Apfloat("-0.77777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.00001", 1, 15));
        assertEquals("iteratorSmall radix 15 next 3", new Apfloat("0.27777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.00001", 1, 15));
        assertEquals("iteratorSmall radix 15 next 4", new Apfloat("0", Apfloat.DEFAULT, 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 5", new Apfloat("-0.077777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.000001", 1, 15));

        iterator = BernoulliHelper.bernoullisBig(4, 5, 15);
        assertEquals("iteratorBig radix 15 next 1", new Apfloat("1.0000", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.0001", 1, 15));
        assertEquals("iteratorBig radix 15 next 2", new Apfloat("-0.77777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.00001", 1, 15));
        assertEquals("iteratorBig radix 15 next 3", new Apfloat("0.27777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.00001", 1, 15));
        assertEquals("iteratorBig radix 15 next 4", new Apfloat("0", Apfloat.DEFAULT, 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 5", new Apfloat("-0.077777", Apfloat.DEFAULT, 15), iterator.next(), new Apfloat("0.000001", 1, 15));

        iterator = BernoulliHelper.bernoullis2(2, 5, 10);
        assertEquals("iterator2 hasNext", true, iterator.hasNext());
        assertEquals("iterator2 next 1", new Apfloat("0.16666"), iterator.next(), new Apfloat("1e-5"));
        assertEquals("iterator2 next 2", new Apfloat("-0.033333"), iterator.next(), new Apfloat("1e-5"));

        iterator = BernoulliHelper.bernoullis2Small(5, 10);
        assertEquals("iterator2Small hasNext", true, iterator.hasNext());
        assertEquals("iterator2Small next 1", new Apfloat("0.16666"), iterator.next(), new Apfloat("1e-5"));
        assertEquals("iterator2Small next 2", new Apfloat("-0.033333"), iterator.next(), new Apfloat("1e-5"));
        assertEquals("iterator2Small hasNext 2", true, iterator.hasNext());

        iterator = BernoulliHelper.bernoullis2Big(2, 5, 10);
        assertEquals("iterator2Big hasNext", true, iterator.hasNext());
        assertEquals("iterator2Big next 1", new Apfloat("0.16666"), iterator.next(), new Apfloat("1e-5"));
        assertEquals("iterator2Big next 2", new Apfloat("-0.033333"), iterator.next(), new Apfloat("1e-5"));
        assertEquals("iterator2Big hasNext 2", false, iterator.hasNext());

        try
        {
            iterator.next();
            fail("iterator2Big allowed next");
        }
        catch (NoSuchElementException nsee)
        {
            // OK
        }

        for (int radix = 2; radix <= 36; radix++)
        {
            iterator = BernoulliHelper.bernoullisSmall(17, radix);
            Iterator<Apfloat> iterator2 = BernoulliHelper.bernoullisBig(50, 17, radix);
            for (int i = 0; i <= 50; i++)
            {
                Apfloat a = iterator.next(),
                        b = iterator2.next(),
                        e = ApfloatMath.ulp(a).multiply(new Apint(radix / 2 + 1, radix));
                assertEquals("precision " + i + " radix " + radix, a.precision(), b.precision());
                assertEquals("iteratorSmall vs iteratorBig " + i + " radix " + radix, a, b, e);
            }
        }
    }

    public static void testBernoulliAprational()
    {
        // This is *extremely* time-consuming
        //assertEquals("Big equals small", BernoulliHelper.bernoulliSmall(200001, 10), BernoulliHelper.bernoulliBig(200001, 10));

        Iterator<Aprational> iterator = BernoulliHelper.bernoullis(4, 10);
        assertEquals("iterator hasNext", true, iterator.hasNext());
        assertEquals("iterator next 1", new Aprational("1"), iterator.next());
        assertEquals("iterator next 2", new Aprational("-1/2"), iterator.next());
        assertEquals("iterator next 3", new Aprational("1/6"), iterator.next());
        assertEquals("iterator next 4", new Aprational("0"), iterator.next());
        assertEquals("iterator next 5", new Aprational("-1/30"), iterator.next());

        iterator = BernoulliHelper.bernoullisSmall(10);
        assertEquals("iteratorSmall hasNext", true, iterator.hasNext());
        assertEquals("iteratorSmall next 1", new Aprational("1"), iterator.next());
        assertEquals("iteratorSmall next 2", new Aprational("-1/2"), iterator.next());
        assertEquals("iteratorSmall next 3", new Aprational("1/6"), iterator.next());
        assertEquals("iteratorSmall next 4", new Aprational("0"), iterator.next());
        assertEquals("iteratorSmall next 5", new Aprational("-1/30"), iterator.next());
        assertEquals("iteratorSmall hasNext 2", true, iterator.hasNext());

        iterator = BernoulliHelper.bernoullisBig(4, 10);
        assertEquals("iteratorBig hasNext", true, iterator.hasNext());
        assertEquals("iteratorBig next 1", new Aprational("1"), iterator.next());
        assertEquals("iteratorBig next 2", new Aprational("-1/2"), iterator.next());
        assertEquals("iteratorBig next 3", new Aprational("1/6"), iterator.next());
        assertEquals("iteratorBig next 4", new Aprational("0"), iterator.next());
        assertEquals("iteratorBig next 5", new Aprational("-1/30"), iterator.next());
        assertEquals("iteratorBig hasNext 2", false, iterator.hasNext());

        try
        {
            iterator.next();
            fail("iteratorBig allowed next");
        }
        catch (NoSuchElementException nsee)
        {
            // OK
        }

        iterator = BernoulliHelper.bernoullisSmall(2);
        Iterator<Aprational> iterator2 = BernoulliHelper.bernoullisBig(100, 2);
        for (int i = 0; i <= 100; i++)
        {
            Aprational a = (i > 0 ? BernoulliHelper.bernoulliSmall(i, 2) : AprationalMath.bernoulli(i, 2));
            assertEquals("directSmall vs iteratorSmall " + i, a, iterator.next());
            assertEquals("directSmall vs iteratorBig " + i, a, iterator2.next());
            if (i % 2 == 0 && i > 0)
            {
                assertEquals("directSmall vs directBig " + i, a, BernoulliHelper.bernoulliBig(i, 2));
            }
        }

        iterator = BernoulliHelper.bernoullis(4, 15);
        assertEquals("iterator radix 15 next 1", new Aprational("1", 15), iterator.next());
        assertEquals("iterator radix 15 next 2", new Aprational("-1/2", 15), iterator.next());
        assertEquals("iterator radix 15 next 3", new Aprational("1/6", 15), iterator.next());
        assertEquals("iterator radix 15 next 4", new Aprational("0", 15), iterator.next());
        assertEquals("iterator radix 15 next 5", new Aprational("-1/20", 15), iterator.next());

        iterator = BernoulliHelper.bernoullisSmall(15);
        assertEquals("iteratorSmall radix 15 next 1", new Aprational("1", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 2", new Aprational("-1/2", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 3", new Aprational("1/6", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 4", new Aprational("0", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 5", new Aprational("-1/20", 15), iterator.next());

        iterator = BernoulliHelper.bernoullisBig(4, 15);
        assertEquals("iteratorBig radix 15 next 1", new Aprational("1", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 2", new Aprational("-1/2", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 3", new Aprational("1/6", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 4", new Aprational("0", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 5", new Aprational("-1/20", 15), iterator.next());

        iterator = BernoulliHelper.bernoullis2(2, 10);
        assertEquals("iterator2 hasNext", true, iterator.hasNext());
        assertEquals("iterator2 next 1", new Aprational("1/6"), iterator.next());
        assertEquals("iterator2 next 2", new Aprational("-1/30"), iterator.next());

        iterator = BernoulliHelper.bernoullis2Small(10);
        assertEquals("iterator2Small hasNext", true, iterator.hasNext());
        assertEquals("iterator2Small next 1", new Aprational("1/6"), iterator.next());
        assertEquals("iterator2Small next 2", new Aprational("-1/30"), iterator.next());
        assertEquals("iterator2Small hasNext 2", true, iterator.hasNext());

        iterator = BernoulliHelper.bernoullis2Big(2, 10);
        assertEquals("iterator2Big hasNext", true, iterator.hasNext());
        assertEquals("iterator2Big next 1", new Aprational("1/6"), iterator.next());
        assertEquals("iterator2Big next 2", new Aprational("-1/30"), iterator.next());
        assertEquals("iterator2Big hasNext 2", false, iterator.hasNext());

        try
        {
            iterator.next();
            fail("iterator2Big allowed next");
        }
        catch (NoSuchElementException nsee)
        {
            // OK
        }

        for (int radix = 2; radix <= 36; radix++)
        {
            iterator = BernoulliHelper.bernoullisSmall(radix);
            iterator2 = BernoulliHelper.bernoullisBig(50, radix);
            for (int i = 0; i <= 50; i++)
            {
                assertEquals("iteratorSmall vs iteratorBig " + i + " radix " + radix, iterator.next(), iterator2.next());
            }
        }
    }
}
