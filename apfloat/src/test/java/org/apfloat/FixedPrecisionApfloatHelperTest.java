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
package org.apfloat;

import java.math.RoundingMode;

import junit.framework.TestSuite;

/**
 * @version 1.11.0
 * @author Mikko Tommila
 */

public class FixedPrecisionApfloatHelperTest
    extends ApfloatTestCase
{
    public FixedPrecisionApfloatHelperTest(String methodName)
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

        suite.addTest(new FixedPrecisionApfloatHelperTest("testValue"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAdd"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSubtract"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMultiply"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testDivide"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testIntegerPow"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testScale"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testInverseRoot"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRoot"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSqrt"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCbrt"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAbs"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCopySign"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFloor"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCeil"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testTruncate"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFrac"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRound"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testNegate"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testModf"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMod"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFmod"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMultiplyAdd"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMultiplySubtract"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAgm"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFactorial"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testPi"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLogRadix"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLog"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLogBase"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testExp"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testPow"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAcosh"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAsinh"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAtanh"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCosh"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSinh"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testTanh"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAcos"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAsin"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAtan"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAtan2"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCos"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSin"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testTan"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testW"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testToDegrees"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testToRadians"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testProduct"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSum"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testE"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testEuler"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGamma"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGammaIncomplete"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGammaIncompleteGeneralized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testDigamma"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBinomial"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBernoulli"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testZeta"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRandom"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRandomGaussian"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMax"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMin"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testNextAfter"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testNextDown"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testNextUp"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testUlp"));

        return suite;
    }

    public static void testValue()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(20);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.valueOf(x);
        assertEquals("value", new Apfloat(2), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testAdd()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(20);
        Apfloat x = new Apfloat(2);
        Apfloat y = new Apfloat(3);
        Apfloat result = helper.add(x, y);
        assertEquals("value", new Apfloat(5), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testSubtract()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(20);
        Apfloat x = new Apfloat(3);
        Apfloat y = new Apfloat(5);
        Apfloat result = helper.subtract(x, y);
        assertEquals("value", new Apfloat(-2), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testMultiply()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(20);
        Apfloat x = new Apfloat("2");
        Apfloat y = new Apfloat("4");
        Apfloat result = helper.multiply(x, y);
        assertEquals("value", new Apfloat("8"), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testDivide()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("15");
        Apfloat y = new Apfloat("3.0");
        Apfloat result = helper.divide(x, y);
        assertEquals("value", new Apfloat(5), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testIntegerPow()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("2");
        Apfloat result = helper.pow(x, 60);
        assertEquals("value", new Apfloat(1L << 60), result);
        assertEquals("precision", 30, result.precision());

        try
        {
            helper.pow(new Apfloat(0), 0);
            fail("0^0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be undefined
        }
    }

    public static void testScale()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("2");
        Apfloat result = helper.scale(x, 60);
        assertEquals("value", new Apfloat("2e60"), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testInverseRoot()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("1.2345678901234567890123456789");
        Apfloat result = helper.inverseRoot(x, 1);
        assertEquals("value", new Apfloat("0.810000007290000066339000603685715"), result, new Apfloat(1e-30));
        assertEquals("precision", 30, result.precision());

        try
        {
            helper.inverseRoot(x, 0);
            fail("inverse zeroth root accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK: inverse zeroth root
        }

        try
        {
            helper.inverseRoot(new Apfloat(-2), 2);
            fail("inverse sqrt of -2 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK: result would be imaginary
        }

        try
        {
            helper.inverseRoot(new Apfloat(0), 2);
            fail("inverse sqrt of 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK: result would be infinite
        }
    }

    public static void testRoot()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.root(x, 2);
        assertEquals("value", new Apfloat("1.4142135623730950488016887242097"), result, new Apfloat(1e-29));
        assertEquals("precision", 30, result.precision());

        try
        {
            helper.root(x, 0);
            fail("zeroth root accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK: zeroth root
        }

        try
        {
            helper.root(new Apfloat(-2), 2);
            fail("sqrt of -2 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK: result would be imaginary
        }

        try
        {
            helper.root(new Apfloat(0), 0);
            fail("0th root of 0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK: result would be undefined
        }
    }

    public static void testSqrt()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.sqrt(x);
        assertEquals("value", new Apfloat("1.4142135623730950488016887242097"), result, new Apfloat(1e-29));
        assertEquals("precision", 30, result.precision());
    }

    public static void testCbrt()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat(-2);
        Apfloat result = helper.cbrt(x);
        assertEquals("value", new Apfloat("-1.2599210498948731647672106072782"), result, new Apfloat(1e-29));
        assertEquals("precision", 30, result.precision());
    }

    public static void testAbs()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("-5");
        Apfloat result = helper.abs(x);
        assertEquals("value", new Apfloat(5), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testCopySign()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("2");
        Apfloat y = new Apfloat(-1);
        Apfloat result = helper.copySign(x, y);
        assertEquals("value", new Apfloat(-2), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testFloor()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("1.1");
        Apfloat result = helper.floor(x);
        assertEquals("value", new Apfloat(1), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testCeil()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("1.1");
        Apfloat result = helper.ceil(x);
        assertEquals("value", new Apfloat(2), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testTruncate()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("-1.1");
        Apfloat result = helper.truncate(x);
        assertEquals("value", new Apfloat(-1), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testFrac()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("-1.1");
        Apfloat result = helper.frac(x);
        assertEquals("value", new Apfloat("-0.1"), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testRound()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(1);
        Apfloat x = new Apfloat("1.1");
        Apfloat result = helper.round(x, RoundingMode.DOWN);
        assertEquals("value", new Apfloat("1.0"), result);
        assertEquals("precision", 1, result.precision());
    }

    public static void testNegate()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.negate(x);
        assertEquals("value", new Apfloat(-2), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testModf()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat("1.5");
        Apfloat[] result = helper.modf(x);
        assertEquals("value 1", new Apfloat(1), result[0]);
        assertEquals("precision 1", 30, result[0].precision());
        assertEquals("value 2", new Apfloat("0.5"), result[1]);
        assertEquals("precision 2", 30, result[1].precision());

        x = new Apfloat("0.3");
        result = helper.modf(x);
        assertEquals("0 value 1", new Apfloat(0), result[0]);
        assertEquals("0 value 2", new Apfloat("0.3"), result[1]);
        assertEquals("0 precision 2", 30, result[1].precision());
    }

    public static void testMod()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat(-5);
        Apfloat y = new Apfloat(3);
        Apfloat result = helper.mod(x, y);
        assertEquals("value", new Apfloat(-2), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testFmod()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat x = new Apfloat(-5);
        Apfloat y = new Apfloat(3);
        Apfloat result = helper.fmod(x, y);
        assertEquals("value", new Apfloat(-2), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testMultiplyAdd()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat a = new Apfloat(2);
        Apfloat b = new Apfloat(3);
        Apfloat c = new Apfloat(4);
        Apfloat d = new Apfloat(5);
        Apfloat result = helper.multiplyAdd(a, b, c, d);
        assertEquals("value", new Apfloat(26), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testMultiplySubtract()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(30);
        Apfloat a = new Apfloat(2);
        Apfloat b = new Apfloat(3);
        Apfloat c = new Apfloat(4);
        Apfloat d = new Apfloat(5);
        Apfloat result = helper.multiplySubtract(a, b, c, d);
        assertEquals("value", new Apfloat(-14), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testAgm()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat a = new Apfloat("2");
        Apfloat b = new Apfloat("1");
        Apfloat result = helper.agm(a, b);
        assertEquals("value", new Apfloat("1.4567910310469068691864323832650819749738639432213055907941723832679264545802509002574737128184484443281894"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testFactorial()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat result = helper.factorial(100);
        assertEquals("value", new Apfloat(9.332621544e157), result, new Apfloat("5e148"));
        assertEquals("precision", 10, result.precision());

        result = helper.factorial(10, 16);
        assertEquals("value 16", new Apfloat(0x375F00, Apfloat.DEFAULT, 16), result);
        assertEquals("precision 16", 10, result.precision());

        try
        {
            helper.factorial(7, 1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testPi()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat result = helper.pi();
        assertEquals("value", new Apfloat(3.141592653), result, new Apfloat("1e-9"));
        assertEquals("precision", 10, result.precision());

        result = helper.pi(16);
        assertEquals("value 16", new Apfloat("3.243F6A888", Apfloat.DEFAULT, 16), result, new Apfloat(1e-9, 1, 16));
        assertEquals("precision 16", 10, result.precision());

        try
        {
            helper.pi(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testLogRadix()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat result = helper.logRadix(2);
        assertEquals("value", new Apfloat(0.69314718055994530941723212145818, Apfloat.DEFAULT, 2), result, new Apfloat("1e-10", Apfloat.DEFAULT, 2));
        assertEquals("precision", 10, result.precision());

        try
        {
            helper.logRadix(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testLog()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat("2");
        Apfloat result = helper.log(x);
        assertEquals("value", new Apfloat("0.693147180559945309417232121458176568075500134360255254120680009493393621969694715605863326996418687"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        x = new Apfloat("1.000000000000000000000000000000001");
        result = helper.log(x);
        assertEquals("close to 1 value", new Apfloat("9.999999999999999999999999999999995000000000000000000000000000000003333333333333333333333333333333331e-34"), result, new Apfloat("1e-134"));
        assertEquals("close to 1 precision", 100, result.precision());

        try
        {
            helper.log(new Apfloat(0));
            fail("log of zero accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be -infinite
        }

        try
        {
            helper.log(new Apfloat(-1));
            fail("log of -1 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }
    }

    public static void testLogBase()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat("4");
        Apfloat b = new Apfloat("2");
        Apfloat result = helper.log(x, b);
        assertEquals("value", new Apfloat(2), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testExp()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.exp(x);
        assertEquals("value", new Apfloat("7.389056098930650227230427460575007813180315570551847324087127822522573796079057763384312485079121794"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        x = new Apfloat(0, Apfloat.DEFAULT, 7);
        result = helper.exp(x);
        assertEquals("value 7", Apfloat.ONE, result);
        assertEquals("precision 7", 100, result.precision());
        assertEquals("radix", 7, result.radix());

        x = new Apfloat("0.0001");
        result = helper.exp(x);
        assertEquals("value limited", new Apfloat("1.000100005000166670833416668055575397073415454172178381034635390972311235972781757573430475110294393"), result);
        assertEquals("precision limited", 100, result.precision());

        x = new Apfloat(5000000000000L);
        result = helper.exp(x);
        assertEquals("big value", new Apfloat("1.816093715813449977121047779023089136640518780204394932476747172911899687306423540158950650025496032e2171472409516"), result, new Apfloat("5e2171472409417"));
        assertEquals("big precision", 100, result.precision());

        try
        {
            helper.exp(new Apfloat("50000000000000000000"));
            fail("Overflow should have occurred");
        }
        catch (OverflowException oe)
        {
            // OK; result would overflow
        }
    }

    public static void testPow()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat y = new Apfloat(3);
        Apfloat result = helper.pow(x, y);
        assertEquals("value", new Apfloat(8), result, new Apfloat("5e-99"));
        assertEquals("precision", 100, result.precision());

        x = new Apfloat(1);
        y = new Apfloat(1);
        result = helper.pow(x, y);
        assertEquals("value", new Apfloat(1), result, new Apfloat("5e-99"));
        assertEquals("precision", 100, result.precision());

        try
        {
            helper.pow(new Apfloat(0), new Apfloat(0));
            fail("0^0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be undefined
        }

        try
        {
            helper.pow(new Apfloat(100), new Apfloat(5000000000000000000L));
            fail("Overflow should have occurred");
        }
        catch (OverflowException oe)
        {
            // OK; result would overflow
        }

        try
        {
            helper.pow(new Apfloat(-2), new Apfloat("1.3"));
            fail("pow of negative number accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }
    }

    public static void testAcosh()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.acosh(x);
        assertEquals("value", new Apfloat("1.316957896924816708625046347307968444026981971467516479768472256920460185416443976074219013450101783"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        try
        {
            helper.acosh(new Apfloat("0.9"));
            fail("acosh(0.9) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }

        try
        {
            helper.acosh(new Apfloat("-0.5"));
            fail("acosh(-0.5) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }
    }

    public static void testAsinh()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat("2");
        Apfloat result = helper.asinh(x);
        assertEquals("value", new Apfloat("1.443635475178810342493276740273105269405553003156981558983054506520491602824665323236028287368170425"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testAtanh()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat("0.5");
        Apfloat result = helper.atanh(x);
        assertEquals("value", new Apfloat("0.5493061443340548456976226184612628523237452789113747258673471668187471466093044834368078774068660444"), result, new Apfloat("5e-100"));
        assertEquals("precision", 100, result.precision());

        try
        {
            helper.atanh(new Apfloat("1"));
            fail("atanh(1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }

        try
        {
            helper.atanh(new Apfloat("-1"));
            fail("atanh(-1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }
    }

    public static void testCosh()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.cosh(x);
        assertEquals("value", new Apfloat("3.7621956910836314595622134777737461082939735582307116027776433475883235850902727266607053037848894217"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testSinh()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.sinh(x);
        assertEquals("value", new Apfloat("3.626860407847018767668213982801261704886342012321135721309484474934250210988785036723607181294232373"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testTanh()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.tanh(x);
        assertEquals("value", new Apfloat("0.96402758007581688394641372410092315025502997624093477604826321741310794631761020255947485004520768915"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        x = new Apfloat(5000000000000L);
        result = helper.tanh(x);
        assertEquals("big value", new Apfloat(1), result, new Apfloat("1e-99"));
        assertEquals("big precision", 100, result.precision());
    }

    public static void testAcos()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat("0.5");
        Apfloat result = helper.acos(x);
        assertEquals("value", new Apfloat("1.04719755119659774615421446109316762806572313312503527365831486410260546876206966620934494178070569"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        result = helper.acos(new Apfloat(0));
        assertEquals("value 0", new Apfloat("1.570796326794896619231321691639751442098584699687552910487472296153908203143104499314017412671058534"), result, new Apfloat("1e-99"));
        assertEquals("precision 0", 100, result.precision());

        try
        {
            helper.acos(new Apfloat("1.1"));
            fail("acos(1.1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }
    }

    public static void testAsin()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = Apfloat.ONE.divide(new Apfloat(2));
        Apfloat result = helper.asin(x);
        assertEquals("value", new Apfloat("0.5235987755982988730771072305465838140328615665625176368291574320513027343810348331046724708903528447"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        try
        {
            helper.asin(new Apfloat("1.1"));
            fail("asin(1.1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }
    }

    public static void testAtan()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat("0.5");
        Apfloat result = helper.atan(x);
        assertEquals("value", new Apfloat("0.4636476090008061162142562314612144020285370542861202638109330887201978641657417053006002839848878926"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testAtan2()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.atan2(x, x);
        assertEquals("value", new Apfloat("0.7853981633974483096156608458198757210492923498437764552437361480769541015715522496570087063355292670"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        try
        {
            helper.atan2(new Apfloat(0), new Apfloat(0));
            fail("atan2(0,0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be undefined
        }
    }

    public static void testCos()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(1);
        Apfloat result = helper.cos(x);
        assertEquals("value", new Apfloat("0.5403023058681397174009366074429766037323104206179222276700972553811003947744717645179518560871830893"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        x = new Apfloat(1).add(ApfloatMath.scale(ApfloatMath.pi(200), 100));
        result = helper.cos(x);
        assertEquals("value 100", new Apfloat("0.5403023058681397174009366074429766037323104206179222276700972553811003947744717645179518560871830893"), result, new Apfloat("1e-99"));
        assertEquals("precision 100", 100, result.precision());

        // Loss of precision
        helper.cos(new Apfloat("1e1000"));
    }

    public static void testSin()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(1);
        Apfloat result = helper.sin(x);
        assertEquals("value", new Apfloat("0.8414709848078965066525023216302989996225630607983710656727517099919104043912396689486397435430526959"), result, new Apfloat("1e-99"));
        assertEquals("precision", 100, result.precision());

        x = new Apfloat(1).add(ApfloatMath.scale(ApfloatMath.pi(200), 100));
        result = helper.sin(x);
        assertEquals("value 100", new Apfloat("0.8414709848078965066525023216302989996225630607983710656727517099919104043912396689486397435430526959"), result, new Apfloat("1e-99"));
        assertEquals("precision 100", 100, result.precision());

        result = helper.sin(new Apfloat(0));
        assertEquals("value 0", new Apfloat(0), result);

        // Loss of precision
        helper.sin(new Apfloat("1e1000"));
    }

    public static void testTan()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(1);
        Apfloat result = helper.tan(x);
        assertEquals("value", new Apfloat("1.55740772465490223050697480745836017308725077238152003838394660569886139715172728955509996520224298"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        result = helper.tan(new Apfloat(0));
        assertEquals("value 0", new Apfloat(0), result);

        // Loss of precision
        helper.tan(new Apfloat("1e1000"));
    }

    public static void testW()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat x = new Apfloat(3);
        Apfloat result = helper.w(x);
        assertEquals("value", new Apfloat("1.049908895"), result, new Apfloat("5e-9"));
        assertEquals("precision", 10, result.precision());
    }

    public static void testToDegrees()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat x = new Apfloat(2.0 * Math.PI);
        Apfloat result = helper.toDegrees(x);
        assertEquals("value", new Apfloat(360), result, new Apfloat("5e-7"));
        assertEquals("precision", 10, result.precision());
    }

    public static void testToRadians()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat x = new Apfloat(-720);
        Apfloat result = helper.toRadians(x);
        assertEquals("value", new Apfloat(-4.0 * Math.PI), result, new Apfloat("5e-7"));
        assertEquals("precision", 10, result.precision());
    }

    public static void testProduct()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat[] x =  { new Apfloat(1),
                         new Apfloat("2.0"),
                         new Apfloat("3.00000000000000000000000") };
        Apfloat result = helper.product(x);
        assertEquals("value", new Apfloat(6), result);
        assertEquals("precision", 10, result.precision());
    }

    public static void testSum()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat[] x =  { new Apfloat("1234567891.1"),
                         new Apfloat("1.1"),
                         new Apfloat("8.000000000000000000000001") };
        Apfloat result = helper.sum(x);
        assertEquals("value", new Apfloat(1234567900), result);
        assertEquals("precision", 10, result.precision());
    }

    public static void testE()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat result = helper.e();
        assertEquals("value", new Apfloat("2.718281828459045235360287471352662497757247093699959574966967627724076630353547594571382178525166427"), result, new Apfloat("5e-99"));
        assertEquals("precision", 100, result.precision());

        result = helper.e(16);
        assertEquals("value 16", new Apfloat("2.b7e151628aed2a6abf7158809cf4f3c762e7160f38b4da56a784d9045190cfef324e7738926cfbe5f4bf8d8d8c31d763da0", Apfloat.DEFAULT, 16), result, new Apfloat(1e-100, 1, 16));
        assertEquals("precision 16", 100, result.precision());

        try
        {
            helper.euler(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testEuler()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat result = helper.euler();
        assertEquals("value", new Apfloat("0.5772156649015328606065120900824024310421593359399235988057672348848677267776646709369470632917467495"), result, new Apfloat("5e-100"));
        assertEquals("precision", 100, result.precision());

        result = helper.euler(16);
        assertEquals("value 16", new Apfloat("0.93c467e37db0c7a4d1be3f810152cb56a1cecc3af65cc0190c03df34709affbd8e4b59fa03a9f0eed0649ccb621057d11057", Apfloat.DEFAULT, 16), result, new Apfloat(1e-100, 1, 16));
        assertEquals("precision 16", 100, result.precision());

        try
        {
            helper.euler(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testGamma()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(27);
        Apfloat x = new Apfloat("1000000000000.1");
        Apfloat result = helper.gamma(x);
        assertEquals("value", new Apfloat("2.22465301759833318396785103e11565705518092"), result, new Apfloat("5e11565705518066"));
        assertEquals("precision", 27, result.precision());
    }

    public static void testGammaIncomplete()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(5);
        Apfloat a = new Apfloat("1.5");
        Apfloat x = new Apfloat("3.5");
        Apfloat result = helper.gamma(a, x);
        assertEquals("value", new Apfloat("0.063718"), result, new Apfloat("5e-6"));
        assertEquals("precision", 5, result.precision());
    }

    public static void testGammaIncompleteGeneralized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("1.5");
        Apfloat x0 = new Apfloat("2.5");
        Apfloat x1 = new Apfloat("3.5");
        Apfloat result = helper.gamma(a, x0, x1);
        assertEquals("value", new Apfloat("0.0885335"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testDigamma()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("2.5");
        Apfloat result = helper.digamma(x);
        assertEquals("value", new Apfloat("0.703157"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.digamma(new Apfloat(0));
            fail("digamma(0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testBinomial()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("2.5");
        Apfloat y = new Apfloat("1.2");
        Apfloat result = helper.binomial(x, y);
        assertEquals("value", new Apfloat("2.58529"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        result = helper.binomial(10, 2);
        assertEquals("long value", new Apfloat(45), result);
        assertEquals("long precision", 6, result.precision());

        result = helper.binomial(9, 3, 12);
        assertEquals("radix 12 value", new Apint(84, 12), result);
        assertEquals("radix 12 precision", 6, result.precision());

        try
        {
            helper.binomial(new Apfloat(-1), new Apfloat("-1.1"));
            fail("binomial(-1,-1.1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testBernoulli()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat result = helper.bernoulli(10);
        assertEquals("value", new Apfloat("0.0757576"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.bernoulli(-1);
            fail("bernoulli(-1) accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK
        }
    }

    public static void testZeta()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat s = new Apfloat("1.5");
        Apfloat result = helper.zeta(s);
        assertEquals("value", new Apfloat("2.61238"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        s = new Apfloat("1e8");
        result = helper.zeta(s);
        assertEquals("value", new Apfloat("1.00000"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        s = new Apfloat(-1001);
        result = helper.zeta(s);
        assertEquals("value", new Apfloat("-1.34859e1771"), result, new Apfloat("5e1766"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testRandom()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        for (int i = 0; i < 100; i++)
        {
            Apfloat result = helper.random();
            assertEquals("value", new Apfloat("0.5"), result, new Apfloat("0.5"));
            assertEquals("precision", 100, result.precision());

            result = helper.random(16);
            assertEquals("value 16", new Apfloat("0.8", Apfloat.DEFAULT, 16), result, new Apfloat("0.8", 1, 16));
            assertEquals("precision 16", 100, result.precision());
        }

        try
        {
            helper.random(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testRandomGaussian()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        for (int i = 0; i < 100; i++)
        {
            Apfloat result = helper.randomGaussian();
            assertEquals("value", new Apfloat("0"), result, new Apfloat("5"));
            assertEquals("precision", 100, result.precision());

            result = helper.randomGaussian(16);
            assertEquals("value 16", new Apfloat("0", Apfloat.DEFAULT, 16), result, new Apfloat("5", 1, 16));
            assertEquals("radix 16", 16, result.radix());
            assertEquals("precision 16", 100, result.precision());
        }

        try
        {
            helper.randomGaussian(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testMax()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat result = helper.max(new Apfloat(1), new Apfloat(2));
        assertEquals("1,2 value", new Apfloat("2"), result);
        assertEquals("1,2 precision", 100, result.precision());

        result = helper.max(new Apfloat(2), new Apfloat(1));
        assertEquals("2,1 value", new Apfloat("2"), result);
        assertEquals("2,1 precision", 100, result.precision());
    }

    public static void testMin()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat result = helper.min(new Apfloat(1), new Apfloat(2));
        assertEquals("1,2 value", new Apfloat("1"), result);
        assertEquals("1,2 precision", 100, result.precision());

        result = helper.min(new Apfloat(2), new Apfloat(1));
        assertEquals("2,1 value", new Apfloat("1"), result);
        assertEquals("2,1 precision", 100, result.precision());
    }

    public static void testNextAfter()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(5);
        Apfloat result = helper.nextAfter(new Apfloat(1), new Apfloat(2));
        assertEquals("1,2 value", new Apfloat("1.0001"), result);
        assertEquals("1,2 precision", 5, result.precision());
    }

    public static void testNextDown()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(5);
        Apfloat result = helper.nextDown(new Apfloat(1));
        assertEquals("1 value", new Apfloat("0.9999"), result);
        assertEquals("1 precision", 5, result.precision());
    }

    public static void testNextUp()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(5);
        Apfloat result = helper.nextUp(new Apfloat(1));
        assertEquals("1 value", new Apfloat("1.0001"), result);
        assertEquals("1 precision", 5, result.precision());
    }

    public static void testUlp()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(5);
        Apfloat result = helper.ulp(new Apfloat(1));
        assertEquals("1 value", new Apfloat("0.0001"), result);
        assertEquals("1 precision", 5, result.precision());
    }
}
