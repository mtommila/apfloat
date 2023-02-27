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

import java.math.RoundingMode;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestSuite;

/**
 * @version 1.11.0
 * @author Mikko Tommila
 */

public class AprationalMathTest
    extends ApfloatTestCase
{
    public AprationalMathTest(String methodName)
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

        suite.addTest(new AprationalMathTest("testIntegerPow"));
        suite.addTest(new AprationalMathTest("testScale"));
        suite.addTest(new AprationalMathTest("testAbs"));
        suite.addTest(new AprationalMathTest("testRound"));
        suite.addTest(new AprationalMathTest("testCopySign"));
        suite.addTest(new AprationalMathTest("testNegate"));
        suite.addTest(new AprationalMathTest("testProduct"));
        suite.addTest(new AprationalMathTest("testSum"));
        suite.addTest(new AprationalMathTest("testMax"));
        suite.addTest(new AprationalMathTest("testMin"));
        suite.addTest(new AprationalMathTest("testBinomial"));
        suite.addTest(new AprationalMathTest("testBernoulli"));

        return suite;
    }

    public static void testIntegerPow()
    {
        Aprational x = new Aprational("2");
        assertEquals("2^30", new Aprational(new Apint(1 << 30)), AprationalMath.pow(x, 30));
        assertEquals("2^60", new Aprational(new Apint(1L << 60)), AprationalMath.pow(x, 60));
        assertEquals("2^-1", new Aprational("1/2"), AprationalMath.pow(x, -1));
        assertEquals("2^-3", new Aprational("1/8"), AprationalMath.pow(x, -3));
        assertEquals("2^0", new Aprational("1"), AprationalMath.pow(x, 0));

        try
        {
            AprationalMath.pow(new Aprational(new Apint(0)), 0);
            fail("0^0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be undefined
        }
    }

    public static void testScale()
    {
        Aprational x = AprationalMath.scale(new Aprational("3/7"), 5);
        assertEquals("1 string", "300000/7", x.toString());

        x = AprationalMath.scale(new Aprational("-7/9"), -1);
        assertEquals("-1 string", "-7/90", x.toString());

        x = AprationalMath.scale(new Apfloat("1e" + 0x4000000000000000L, 1, 2).truncate(), Long.MIN_VALUE);
        assertEquals("scale min", new Apfloat("1e-" + 0x4000000000000000L, 1, 2), x);
    }

    public static void testRound()
    {
        Apfloat x = AprationalMath.round(new Aprational("3/2"), 1, RoundingMode.UP);
        assertEquals("3/2 UP", "2", x.toString());

        x = AprationalMath.round(new Aprational("3/2"), 1, RoundingMode.DOWN);
        assertEquals("3/2 DOWN", "1", x.toString());

        x = AprationalMath.round(new Aprational("10/2", 3), 1, RoundingMode.HALF_EVEN);
        assertEquals("3/2 base 3 EVEN", "2", x.toString());

        x = AprationalMath.round(new Aprational("12/2", 3), 1, RoundingMode.HALF_EVEN);
        assertEquals("5/2 base 3 EVEN", "2", x.toString());

        x = AprationalMath.round(new Aprational("-10/2", 3), 1, RoundingMode.HALF_EVEN);
        assertEquals("-3/2 base 3 EVEN", "-2", x.toString());

        x = AprationalMath.round(new Aprational("-12/2", 3), 1, RoundingMode.HALF_EVEN);
        assertEquals("-5/2 base 3 EVEN", "-2", x.toString());
    }

    public static void testAbs()
    {
        Aprational x = new Aprational("2/3");
        assertEquals("2/3", new Aprational("2/3"), AprationalMath.abs(x));

        x = new Aprational("-2/3");
        assertEquals("-2/3", new Aprational("2/3"), AprationalMath.abs(x));

        x = new Aprational("0");
        assertEquals("0", new Aprational("0"), AprationalMath.abs(x));
    }

    public static void testCopySign()
    {
        assertEquals("2/3, 1/2", new Aprational("2/3"), AprationalMath.copySign(new Aprational("2/3"), new Aprational("1/2")));
        assertEquals("2/3, -1/2", new Aprational("-2/3"), AprationalMath.copySign(new Aprational("2/3"), new Aprational("-1/2")));
        assertEquals("-2/3, 1/2", new Aprational("2/3"), AprationalMath.copySign(new Aprational("-2/3"), new Aprational("1/2")));
        assertEquals("-2/3, -1/2", new Aprational("-2/3"), AprationalMath.copySign(new Aprational("-2/3"), new Aprational("-1/2")));

        assertEquals("0, 0", new Aprational("0"), AprationalMath.copySign(new Aprational("0"), new Aprational("0")));
        assertEquals("0, 1/2", new Aprational("0"), AprationalMath.copySign(new Aprational("0"), new Aprational("1/2")));
        assertEquals("0, -1/2", new Aprational("0"), AprationalMath.copySign(new Aprational("0"), new Aprational("-1/2")));
        assertEquals("1/2, 0", new Aprational("0"), AprationalMath.copySign(new Aprational("1/2"), new Aprational("0")));
        assertEquals("-1/2, 0", new Aprational("0"), AprationalMath.copySign(new Aprational("-1/2"), new Aprational("0")));
    }

    @SuppressWarnings("deprecation")
    public static void testNegate()
    {
        Aprational x = new Aprational("2/3");
        assertEquals("2/3", new Aprational("-2/3"), AprationalMath.negate(x));

        x = new Aprational("-2/3");
        assertEquals("-2/3", new Aprational("2/3"), AprationalMath.negate(x));

        x = new Aprational("0");
        assertEquals("0", new Aprational("0"), AprationalMath.negate(x));
    }

    public static void testProduct()
    {
        Aprational a = AprationalMath.product(new Aprational("2/3"), new Aprational("4/5"));
        assertEquals("2/3,4/5 value", new Aprational("8/15"), a);

        a = AprationalMath.product(new Aprational("2"), new Aprational("3/5"), new Aprational("7/11"));
        assertEquals("2,3/5,7/9 value", new Aprational("42/55"), a);

        a = AprationalMath.product(Aprational.ZERO, new Aprational("12345"));
        assertEquals("0 value", new Aprational("0"), a);

        a = AprationalMath.product(new Apint(0, 12));
        assertEquals("0 radix", 12, a.radix());

        Aprational[] x = new Aprational[] { new Aprational("1000000000000"), new Aprational("1") };
        AprationalMath.product(x);
        assertEquals("Array product 1 [0]", new Aprational("1000000000000"), x[0]);
        assertEquals("Array product 1 [1]", new Aprational("1"), x[1]);

        x = new Aprational[] { new Aprational("1"), new Aprational("1000000000000") };
        AprationalMath.product(x);
        assertEquals("Array product 2 [0]", new Aprational("1"), x[0]);
        assertEquals("Array product 2 [1]", new Aprational("1000000000000"), x[1]);

        assertEquals("Empty product", new Aprational("1"), AprationalMath.product());
    }

    public static void testSum()
    {
        Aprational a = AprationalMath.sum(new Aprational("1/2"), new Aprational("2/3"));
        assertEquals("1/2,2/3 value", new Aprational("7/6"), a);

        a = AprationalMath.sum(new Aprational("2/3"), new Aprational("3/4"), new Aprational("4/5"));
        assertEquals("2/3,3/4,4/5 value", new Aprational("133/60"), a);

        a = AprationalMath.sum(new Aprational("0"), new Aprational("12345"));
        assertEquals("0-0 value", new Aprational("12345"), a);

        a = AprationalMath.sum(new Aprational("2"));
        assertEquals("2 value", new Aprational("2"), a);

        a = AprationalMath.sum(new Aprational("2"), new Aprational("222"), new Aprational("22"));
        assertEquals("2 222 22 value", new Aprational("246"), a);

        Aprational[] x = new Aprational[] { new Aprational("1000000000000"), new Aprational("1") };
        AprationalMath.sum(x);
        assertEquals("Array sum 1 [0]", new Aprational("1000000000000"), x[0]);
        assertEquals("Array sum 1 [1]", new Aprational("1"), x[1]);

        x = new Aprational[] { new Aprational("1"), new Aprational("1000000000000") };
        AprationalMath.sum(x);
        assertEquals("Array sum 2 [0]", new Aprational("1"), x[0]);
        assertEquals("Array sum 2 [1]", new Aprational("1000000000000"), x[1]);

        assertEquals("Empty sum", new Aprational("0"), AprationalMath.sum());
    }

    public static void testMax()
    {
        assertEquals("max of 1/2 and 1/2", new Aprational("1/2"), AprationalMath.max(new Aprational("1/2"), new Aprational("1/2")));
        assertEquals("max of 1/2 and 2/3", new Aprational("2/3"), AprationalMath.max(new Aprational("1/2"), new Aprational("2/3")));
        assertEquals("max of 2/3 and 1/2", new Aprational("2/3"), AprationalMath.max(new Aprational("2/3"), new Aprational("1/2")));
    }

    public static void testMin()
    {
        assertEquals("min of 1/2 and 1/2", new Aprational("1/2"), AprationalMath.min(new Aprational("1/2"), new Aprational("1/2")));
        assertEquals("min of 1/2 and 2/3", new Aprational("1/2"), AprationalMath.min(new Aprational("1/2"), new Aprational("2/3")));
        assertEquals("min of 2/3 and 1/2", new Aprational("1/2"), AprationalMath.min(new Aprational("2/3"), new Aprational("1/2")));
    }

    public static void testBinomial()
    {
        Aprational a = AprationalMath.binomial(new Aprational("0"), new Aprational("0"));
        assertEquals("0,0 value", new Aprational("1"), a);

        a = AprationalMath.binomial(new Aprational("-9"), new Aprational("2"));
        assertEquals("-9,2 value", new Aprational("45"), a);

        a = AprationalMath.binomial(new Aprational("19/3"), new Aprational("3"));
        assertEquals("19/3,3 value", new Aprational("1976/81"), a);

        a = AprationalMath.binomial(new Aprational("-19/3"), new Aprational("3"));
        assertEquals("-19/3,3 value", new Aprational("-5225/81"), a);

        a = AprationalMath.binomial(new Aprational("19/3"), new Aprational("-3"));
        assertEquals("19/3,-3 value", new Aprational("0"), a);

        a = AprationalMath.binomial(new Aprational("-19/3"), new Aprational("-3"));
        assertEquals("-19/3,-3 value", new Aprational("0"), a);

        a = AprationalMath.binomial(new Aprational("7/3"), new Aprational("4"));
        assertEquals("7/3,4 value", new Aprational("-7/243"), a);

        a = AprationalMath.binomial(new Aprational("-7/3"), new Aprational("4"));
        assertEquals("-7/3,4 value", new Aprational("1820/243"), a);

        a = AprationalMath.binomial(new Aprational("7/3"), new Aprational("-4"));
        assertEquals("7/3,-4 value", new Aprational("0"), a);

        a = AprationalMath.binomial(new Aprational("-7/3"), new Aprational("-4"));
        assertEquals("-7/3,-4 value", new Aprational("0"), a);

        a = AprationalMath.binomial(new Aprational("19/3"), new Aprational("4/3"));
        assertEquals("19/3,4/3 value", new Aprational("6916/729"), a);

        a = AprationalMath.binomial(new Aprational("-19/3"), new Aprational("-4/3"));
        assertEquals("-19/3,-4/3 value", new Aprational("0"), a);

        a = AprationalMath.binomial(new Aprational("4/3"), new Aprational("19/3"));
        assertEquals("4/3,19/3 value", new Aprational("0"), a);

        a = AprationalMath.binomial(new Aprational("-4/3"), new Aprational("-19/3"));
        assertEquals("-4/3,-19/3 value", new Aprational("-1456/729"), a);

        try
        {
            AprationalMath.binomial(new Aprational("9"), new Aprational("4/3"));
            fail("9,4/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
        try
        {
            AprationalMath.binomial(new Aprational("9"), new Aprational("-4/3"));
            fail("9,-4/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
        try
        {
            AprationalMath.binomial(new Aprational("-9"), new Aprational("4/3"));
            fail("-9,4/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is infinite
        }
        try
        {
            AprationalMath.binomial(new Aprational("-9"), new Aprational("-4/3"));
            fail("-9,-4/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is infinite
        }

        try
        {
            AprationalMath.binomial(new Aprational("2"), new Aprational("19/3"));
            fail("2,19/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
        try
        {
            AprationalMath.binomial(new Aprational("2"), new Aprational("-19/3"));
            fail("2,-19/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
        try
        {
            AprationalMath.binomial(new Aprational("-2"), new Aprational("19/3"));
            fail("-2,19/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is infinite
        }
        try
        {
            AprationalMath.binomial(new Aprational("-2"), new Aprational("-19/3"));
            fail("-2,-19/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is infinite
        }

        try
        {
            AprationalMath.binomial(new Aprational("-19/3"), new Aprational("4/3"));
            fail("-19/3,4/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
        try
        {
            AprationalMath.binomial(new Aprational("19/3"), new Aprational("-4/3"));
            fail("19/3,-4/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
        try
        {
            AprationalMath.binomial(new Aprational("-4/3"), new Aprational("19/3"));
            fail("-4/3,19/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
        try
        {
            AprationalMath.binomial(new Aprational("4/3"), new Aprational("-19/3"));
            fail("4/3,-19/3 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result is not a rational number
        }
    }

    public static void testBernoulli()
    {
        Aprational a = AprationalMath.bernoulli(0L);
        assertEquals("0 value", new Aprational("1"), a);

        a = AprationalMath.bernoulli(1L);
        assertEquals("1 value", new Aprational("-1/2"), a);

        a = AprationalMath.bernoulli(2L);
        assertEquals("2 value", new Aprational("1/6"), a);

        a = AprationalMath.bernoulli(3L);
        assertEquals("3 value", new Aprational("0"), a);

        a = AprationalMath.bernoulli(4L);
        assertEquals("4 value", new Aprational("-1/30"), a);

        a = AprationalMath.bernoulli(6, 7);
        assertEquals("6 value", new Aprational("1/60", 7), a);

        Iterator<Aprational> iterator = AprationalMath.bernoullis(4, 10);
        assertEquals("iterator hasNext", true, iterator.hasNext());
        assertEquals("iterator next 1", new Aprational("1"), iterator.next());
        assertEquals("iterator next 2", new Aprational("-1/2"), iterator.next());
        assertEquals("iterator next 3", new Aprational("1/6"), iterator.next());
        assertEquals("iterator next 4", new Aprational("0"), iterator.next());
        assertEquals("iterator next 5", new Aprational("-1/30"), iterator.next());

        iterator = AprationalMath.bernoullisSmall(10);
        assertEquals("iteratorSmall hasNext", true, iterator.hasNext());
        assertEquals("iteratorSmall next 1", new Aprational("1"), iterator.next());
        assertEquals("iteratorSmall next 2", new Aprational("-1/2"), iterator.next());
        assertEquals("iteratorSmall next 3", new Aprational("1/6"), iterator.next());
        assertEquals("iteratorSmall next 4", new Aprational("0"), iterator.next());
        assertEquals("iteratorSmall next 5", new Aprational("-1/30"), iterator.next());
        assertEquals("iteratorSmall hasNext 2", true, iterator.hasNext());

        iterator = AprationalMath.bernoullisBig(4, 10);
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

        iterator = AprationalMath.bernoullisSmall(2);
        Iterator<Aprational> iterator2 = AprationalMath.bernoullisBig(100, 2);
        for (int i = 0; i <= 100; i++)
        {
            a = (i > 0 ? AprationalMath.bernoulliSmall(i, 2) : AprationalMath.bernoulli(i, 2));
            assertEquals("directSmall vs iteratorSmall " + i, a, iterator.next());
            assertEquals("directSmall vs iteratorBig " + i, a, iterator2.next());
            if (i % 2 == 0 && i > 0)
            {
                assertEquals("directSmall vs directBig " + i, a, AprationalMath.bernoulliBig(i, 2));
            }
        }

        iterator = AprationalMath.bernoullis(4, 15);
        assertEquals("iterator radix 15 next 1", new Aprational("1", 15), iterator.next());
        assertEquals("iterator radix 15 next 2", new Aprational("-1/2", 15), iterator.next());
        assertEquals("iterator radix 15 next 3", new Aprational("1/6", 15), iterator.next());
        assertEquals("iterator radix 15 next 4", new Aprational("0", 15), iterator.next());
        assertEquals("iterator radix 15 next 5", new Aprational("-1/20", 15), iterator.next());

        iterator = AprationalMath.bernoullisSmall(15);
        assertEquals("iteratorSmall radix 15 next 1", new Aprational("1", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 2", new Aprational("-1/2", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 3", new Aprational("1/6", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 4", new Aprational("0", 15), iterator.next());
        assertEquals("iteratorSmall radix 15 next 5", new Aprational("-1/20", 15), iterator.next());

        iterator = AprationalMath.bernoullisBig(4, 15);
        assertEquals("iteratorBig radix 15 next 1", new Aprational("1", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 2", new Aprational("-1/2", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 3", new Aprational("1/6", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 4", new Aprational("0", 15), iterator.next());
        assertEquals("iteratorBig radix 15 next 5", new Aprational("-1/20", 15), iterator.next());

        iterator = AprationalMath.bernoullis2(2, 10);
        assertEquals("iterator2 hasNext", true, iterator.hasNext());
        assertEquals("iterator2 next 1", new Aprational("1/6"), iterator.next());
        assertEquals("iterator2 next 2", new Aprational("-1/30"), iterator.next());

        iterator = AprationalMath.bernoullis2Small(10);
        assertEquals("iterator2Small hasNext", true, iterator.hasNext());
        assertEquals("iterator2Small next 1", new Aprational("1/6"), iterator.next());
        assertEquals("iterator2Small next 2", new Aprational("-1/30"), iterator.next());
        assertEquals("iterator2Small hasNext 2", true, iterator.hasNext());

        iterator = AprationalMath.bernoullis2Big(2, 10);
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
            iterator = AprationalMath.bernoullisSmall(radix);
            iterator2 = AprationalMath.bernoullisBig(50, radix);
            for (int i = 0; i <= 50; i++)
            {
                assertEquals("iteratorSmall vs iteratorBig " + i + " radix " + radix, iterator.next(), iterator2.next());
            }
        }

        try
        {
            AprationalMath.bernoulli(-1);
            fail("-1 accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK
        }
    }
}
