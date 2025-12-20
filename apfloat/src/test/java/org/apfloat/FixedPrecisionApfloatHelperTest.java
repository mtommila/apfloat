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

import java.math.RoundingMode;

import junit.framework.TestSuite;

/**
 * @version 1.16.0
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
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRoundToPrecision"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRoundToInteger"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRoundToPlaces"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRoundToMultiple"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testNegate"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testModf"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMod"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFmod"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMultiplyAdd"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testMultiplySubtract"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAgm"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFactorial"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testDoubleFactorial"));
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
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSinc"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testW"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testToDegrees"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testToRadians"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testProduct"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSum"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testE"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testEuler"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCatalan"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGlaisher"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testKhinchin"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGamma"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGammaIncomplete"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGammaIncompleteGeneralized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLogGamma"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testDigamma"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testPolygamma"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBarnesG"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLogBarnesG"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBeta"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBetaIncomplete"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBetaIncompleteGeneralized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testPochhammer"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBinomial"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBernoulli"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testZeta"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testZetaHurwitz"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHypergeometric0F1"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHypergeometric1F1"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHypergeometric2F1"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHypergeometric0F1Regularized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHypergeometric1F1Regularized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHypergeometric2F1Regularized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHypergeometricU"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testErf"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testErfc"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testErfi"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testInverseErf"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testInverseErfc"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFresnelS"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFresnelC"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testExpIntegralE"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testExpIntegralEi"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLogIntegral"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSinIntegral"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCosIntegral"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testSinhIntegral"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testCoshIntegral"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAiryAi"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAiryAiPrime"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAiryBi"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAiryBiPrime"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBesselJ"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBesselI"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBesselY"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBesselK"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testStruveH"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testStruveL"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testAngerJ"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testWeberE"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testEllipticK"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testEllipticE"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHermiteH"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLaguerreL"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLaguerreLGeneralized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLegendreP"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLegendrePAssociated"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLegendreQ"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLegendreQAssociated"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testChebyshevT"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testChebyshevU"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGegenbauerCRenormalized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testGegenbauerC"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testJacobiP"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testFibonacci"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testEulerE"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testBernoulliB"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHarmonicNumber"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testHarmonicNumberGeneralized"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testPolylog"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testClausenC"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testClausenCl"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testClausenS"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testClausenSl"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testLogisticSigmoid"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRandom"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testRandomGaussian"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testContinuedFraction"));
        suite.addTest(new FixedPrecisionApfloatHelperTest("testConvergents"));
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be undefined
            assertEquals("Localization key", "pow.zeroToZero", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK: inverse zeroth root
            assertEquals("Localization key", "inverseRoot.zeroth", aae.getLocalizationKey());
        }

        try
        {
            helper.inverseRoot(new Apfloat(-2), 2);
            fail("inverse sqrt of -2 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result would be imaginary
            assertEquals("Localization key", "root.evenOfNegative", aae.getLocalizationKey());
        }

        try
        {
            helper.inverseRoot(new Apfloat(0), 2);
            fail("inverse sqrt of 0 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result would be infinite
            assertEquals("Localization key", "inverseRoot.ofZero", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK: zeroth root
            assertEquals("Localization key", "root.zeroth", aae.getLocalizationKey());
        }

        try
        {
            helper.root(new Apfloat(-2), 2);
            fail("sqrt of -2 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result would be imaginary
            assertEquals("Localization key", "root.evenOfNegative", aae.getLocalizationKey());
        }

        try
        {
            helper.root(new Apfloat(0), 0);
            fail("0th root of 0 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result would be undefined
            assertEquals("Localization key", "root.zeroth", aae.getLocalizationKey());
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

    @SuppressWarnings("deprecation")
    public static void testRound()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(1);
        Apfloat x = new Apfloat("1.1");
        Apfloat result = helper.round(x, RoundingMode.DOWN);
        assertEquals("value", new Apfloat("1.0"), result);
        assertEquals("precision", 1, result.precision());
    }

    public static void testRoundToPrecision()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(1);
        Apfloat x = new Apfloat("1.1");
        Apfloat result = helper.roundToPrecision(x, RoundingMode.DOWN);
        assertEquals("value", new Apfloat("1.0"), result);
        assertEquals("precision", 1, result.precision());
    }

    public static void testRoundToInteger()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(1);
        Apfloat x = new Apfloat("1.1");
        Apfloat result = helper.roundToInteger(x, RoundingMode.DOWN);
        assertEquals("value", new Apfloat("1.0"), result);
        assertEquals("precision", 1, result.precision());
    }

    public static void testRoundToPlaces()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("1.19");
        Apfloat result = helper.roundToPlaces(x, 1, RoundingMode.DOWN);
        assertEquals("value", new Apfloat("1.1"), result);
        assertEquals("precision", 6, result.precision());
    }

    public static void testRoundToMultiple()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(5);
        Apfloat x = new Apfloat("3.1"),
                y = new Apfloat("1.1");
        Apfloat result = helper.roundToMultiple(x, y, RoundingMode.DOWN);
        assertEquals("value", new Apfloat("2.2"), result);
        assertEquals("precision", 5, result.precision());
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

    public static void testDoubleFactorial()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(10);
        Apfloat result = helper.doubleFactorial(100);
        assertEquals("value", new Apfloat(3.424322470e79), result, new Apfloat("5e70"));
        assertEquals("precision", 10, result.precision());

        result = helper.doubleFactorial(10, 16);
        assertEquals("value 16", new Apfloat(0xF00, Apfloat.DEFAULT, 16), result);
        assertEquals("precision 16", 10, result.precision());

        result = helper.doubleFactorial(-5);
        assertEquals("value -5", new Apfloat("0.3333333333"), result, new Apfloat("1e-10"));
        assertEquals("precision -5", 10, result.precision());

        result = helper.doubleFactorial(-7, 16);
        assertEquals("value -7", new Apfloat(-1.0 / 15.0, Apfloat.DEFAULT, 16), result, new Apfloat("0.0000000001", 1, 16));
        assertEquals("precision -7", 10, result.precision());

        try
        {
            helper.doubleFactorial(7, 1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }

        try
        {
            helper.doubleFactorial(-2, 16);
            fail("-2 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK
            assertEquals("Localization key", "doubleFactorial.ofNegativeEven", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be -infinite
            assertEquals("Localization key", "log.ofZero", aae.getLocalizationKey());
        }

        try
        {
            helper.log(new Apfloat(-1));
            fail("log of -1 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be complex
            assertEquals("Localization key", "log.ofNegative", aae.getLocalizationKey());
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

        result = helper.pow(new Apfloat("1.5"), new Apfloat("2.5"));
        assertEquals("1.5^2.5 value", new Apfloat("2.755675960631075360471944584044127815961690915738753894486779138157330424639479404857341862242720264"), result, new Apfloat("5e-99"));
        assertEquals("1.5^2.5 precision", 100, result.precision());

        result = helper.pow(new Apfloat(-4), new Apfloat(-2));
        assertEquals("-4^-2 value", new Apfloat("0.0625"), result, new Apfloat("5e-101"));
        assertEquals("-4^-2 precision", 100, result.precision());

        result = helper.pow(new Apfloat("-1.00000000000000000000000000001"), new Apfloat("1e20"));
        assertEquals("-1.00000000000000000000000000001^1e20 value", new Apfloat("1.0000000010000000005000000001666666667033333333366666666655555555549539682538306"), result, new Apfloat("5e-79"));
        assertEquals("-1.00000000000000000000000000001^1e20 precision", 100, result.precision());

        try
        {
            helper.pow(new Apfloat(0), new Apfloat(0));
            fail("0^0 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be undefined
            assertEquals("Localization key", "pow.zeroToZero", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be complex
            assertEquals("Localization key", "pow.negativeToNonInteger", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be complex
            assertEquals("Localization key", "root.evenOfNegative", aae.getLocalizationKey());
        }

        try
        {
            helper.acosh(new Apfloat("-0.5"));
            fail("acosh(-0.5) accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be complex
            assertEquals("Localization key", "root.evenOfNegative", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be infinite
            assertEquals("Localization key", "divide.byZero", aae.getLocalizationKey());
        }

        try
        {
            helper.atanh(new Apfloat("-1"));
            fail("atanh(-1) accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be infinite
            assertEquals("Localization key", "log.ofZero", aae.getLocalizationKey());
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

        x = new Apfloat("2e-1000000000000");
        result = helper.sinh(x);
        assertEquals("small value", new Apfloat("2e-1000000000000", 100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());
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

        x = new Apfloat("2e-1000000000000");
        result = helper.tanh(x);
        assertEquals("small value", new Apfloat("2e-1000000000000", 100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be complex
            assertEquals("Localization key", "root.evenOfNegative", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be complex
            assertEquals("Localization key", "root.evenOfNegative", aae.getLocalizationKey());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be undefined
            assertEquals("Localization key", "atan2.ofOrigin", aae.getLocalizationKey());
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

        x = new Apfloat("2e-1000000000000");
        result = helper.sin(x);
        assertEquals("small value", new Apfloat("2e-1000000000000", 100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());

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

        x = new Apfloat("2e-1000000000000");
        result = helper.tan(x);
        assertEquals("small value", new Apfloat("2e-1000000000000", 100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());

        // Loss of precision
        helper.tan(new Apfloat("1e1000"));
    }

    public static void testSinc()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat x = new Apfloat(2);
        Apfloat result = helper.sinc(x);
        assertEquals("value", new Apfloat("0.4546487134128408476980099329558724213511274857239451341894865057654836507703917723100633444624796902"), result, new Apfloat("1e-100"));
        assertEquals("precision", 100, result.precision());

        x = new Apfloat(3).add(ApfloatMath.scale(ApfloatMath.pi(200), 100));
        result = helper.sinc(x);
        assertEquals("value 100", new Apfloat("4.491989370379195769076292132633729542441923579269675954842294682403756171256157205040164432640257852e-102"), result, new Apfloat("1e-201"));
        assertEquals("precision 100", 100, result.precision());

        result = helper.sinc(new Apfloat(0));
        assertEquals("value 0", new Apfloat(1), result);

        x = new Apfloat("2e-1000000000000");
        result = helper.sinc(x);
        assertEquals("small value", new Apfloat(1, 100), result, new Apfloat("1e-99"));
        assertEquals("small precision", 100, result.precision());

        // Loss of precision
        helper.sinc(new Apfloat("1e1000"));
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
        assertEquals("value 16", new Apfloat("2.b7e151628aed2a6abf7158809cf4f3c762e7160f38b4da56a784d9045190cfef324e7738926cfbe5f4bf8d8d8c31d763da0", Apfloat.DEFAULT, 16), result, new Apfloat(4e-121, 1, 16));
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
        assertEquals("value 16", new Apfloat("0.93c467e37db0c7a4d1be3f810152cb56a1cecc3af65cc0190c03df34709affbd8e4b59fa03a9f0eed0649ccb621057d11057", Apfloat.DEFAULT, 16), result, new Apfloat(4e-121, 1, 16));
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

    public static void testCatalan()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat result = helper.catalan();
        assertEquals("value", new Apfloat("0.9159655941772190150546035149323841107741493742816721342664981196217630197762547694793565129261151062"), result, new Apfloat("5e-100"));
        assertEquals("precision", 100, result.precision());

        result = helper.catalan(16);
        assertEquals("value 16", new Apfloat("0.ea7cb89f409ae845215822e37d32d0c63ec43e1381c2ff8094a263e5a3ccd76f94dc058a46eec5858f924d663f739c42ec96", Apfloat.DEFAULT, 16), result, new Apfloat(4e-121, 1, 16));
        assertEquals("precision 16", 100, result.precision());

        try
        {
            helper.catalan(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testGlaisher()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat result = helper.glaisher();
        assertEquals("value", new Apfloat("1.282427129100622636875342568869791727767688927325001192063740021740406308858826461129736491958202374"), result, new Apfloat("5e-99"));
        assertEquals("precision", 100, result.precision());

        result = helper.glaisher(16);
        assertEquals("value 16", new Apfloat("1.484d24f2fd8731313ed56e343da775794768e0cabd1a57f64185e87606793b86ed92e2208eddc71810d323a0fb6eced612d8", Apfloat.DEFAULT, 16), result, new Apfloat(7e-120, 1, 16));
        assertEquals("precision 16", 100, result.precision());

        try
        {
            helper.glaisher(1);
            fail("Radix 1 accepted");
        }
        catch (NumberFormatException nfe)
        {
            // OK; invalid radix
        }
    }

    public static void testKhinchin()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(100);
        Apfloat result = helper.khinchin();
        assertEquals("value", new Apfloat("2.685452001065306445309714835481795693820382293994462953051152345557218859537152002801141174931847698"), result, new Apfloat("5e-99"));
        assertEquals("precision", 100, result.precision());

        result = helper.khinchin(16);
        assertEquals("value 16", new Apfloat("2.af79c8478da1aef2fdf3e394667f9c3392e021e61829ddfa4d87063b39522441a3b8f9696bfe8c1fd5ea06533d2dbf594d55", Apfloat.DEFAULT, 16), result, new Apfloat(7e-120, 1, 16));
        assertEquals("precision 16", 100, result.precision());

        try
        {
            helper.khinchin(1);
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

        x = new Apfloat("-99.99999999999999999999999999");
        result = helper.gamma(x);
        assertEquals("-99.99999999999999999999999999 value", new Apfloat("1.07151028812546692318354681e-132"), result, new Apfloat("5e-158"));
        assertEquals("-99.99999999999999999999999999 precision", 27, result.precision());
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

    public static void testLogGamma()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("2.5");
        Apfloat result = helper.logGamma(x);
        assertEquals("value", new Apfloat("0.284683"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.logGamma(new Apfloat(0));
            fail("logGamma(0) accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK
            assertEquals("Localization key", "logGamma.ofZero", aae.getLocalizationKey());
        }
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
        catch (ApfloatArithmeticException aae)
        {
            // OK
            assertEquals("Localization key", "digamma.ofNonpositiveInteger", aae.getLocalizationKey());
        }
    }

    public static void testPolygamma()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("2.5");
        Apfloat result = helper.polygamma(2, x);
        assertEquals("value", new Apfloat("-0.236204"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBarnesG()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("2.5");
        Apfloat result = helper.barnesG(x);
        assertEquals("value", new Apfloat("0.947574"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLogBarnesG()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("2.5");
        Apfloat result = helper.logBarnesG(x);
        assertEquals("value", new Apfloat("-0.0538503"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBeta()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-1.5"),
                b = new Apfloat("2.5");
        Apfloat result = helper.beta(a, b);
        assertEquals("value", new Apfloat("3.14159"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBetaIncomplete()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("0.5"),
                a = new Apfloat("-1.5"),
                b = new Apfloat("2.5");
        Apfloat result = helper.beta(x, a, b);
        assertEquals("value", new Apfloat("2.90413"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.beta(new Apfloat("1.5"), new Apfloat("-1.5"), new Apfloat("2.5"));
            fail("beta(1.5, -1.5, 2.5) accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK
            assertEquals("Localization key", "complex", aae.getLocalizationKey());
        }
    }

    public static void testBetaIncompleteGeneralized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x1 = new Apfloat("0.5"),
                x2 = new Apfloat("0.6"),
                a = new Apfloat("1.5"),
                b = new Apfloat("2.5");
        Apfloat result = helper.beta(x1, x2, a, b);
        assertEquals("value", new Apfloat("0.0223574"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testPochhammer()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5"),
                n = new Apfloat("2.5");
        Apfloat result = helper.pochhammer(x, n);
        assertEquals("value", new Apfloat("0.423142"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
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
        catch (ApfloatArithmeticException aae)
        {
            // OK
            assertEquals("Localization key", "gamma.ofZero", aae.getLocalizationKey());
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

    public static void testZetaHurwitz()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat s = new Apfloat("-1.5"),
                a = new Apfloat("1.5");
        Apfloat result = helper.zeta(s, a);
        assertEquals("value", new Apfloat("-0.337079"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric0F1()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-1.5"),
                x = new Apfloat("1.5");
        Apfloat result = helper.hypergeometric0F1(a, x);
        assertEquals("value", new Apfloat("3.42337"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric1F1()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-1.5"),
                b = new Apfloat("-2.5"),
                x = new Apfloat("1.5");
        Apfloat result = helper.hypergeometric1F1(a, b, x);
        assertEquals("value", new Apfloat("1.79268"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric2F1()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-1.5"),
                b = new Apfloat("-2.5"),
                c = new Apfloat("-4.5"),
                x = new Apfloat("0.5");
        Apfloat result = helper.hypergeometric2F1(a, b, c, x);
        assertEquals("value", new Apfloat("0.606092"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric0F1Regularized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-3"),
                x = new Apfloat("1.5");
        Apfloat result = helper.hypergeometric0F1Regularized(a, x);
        assertEquals("value", new Apfloat("0.28272"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric1F1Regularized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-1.5"),
                b = new Apfloat("-3"),
                x = new Apfloat("1.5");
        Apfloat result = helper.hypergeometric1F1Regularized(a, b, x);
        assertEquals("value", new Apfloat("0.263170"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric2F1Regularized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-1.5"),
                b = new Apfloat("-2.5"),
                c = new Apfloat("-4"),
                x = new Apfloat("0.5");
        Apfloat result = helper.hypergeometric2F1Regularized(a, b, c, x);
        assertEquals("value", new Apfloat("-0.00131556"), result, new Apfloat("5e-8"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometricU()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat a = new Apfloat("-1.5"),
                b = new Apfloat("-3"),
                x = new Apfloat("1.5");
        Apfloat result = helper.hypergeometricU(a, b, x);
        assertEquals("value", new Apfloat("8.44588"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testErf()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.erf(x);
        assertEquals("value", new Apfloat("-0.966105"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("-0.5");
        result = helper.erf(x);
        assertEquals("-0.5 value", new Apfloat("-0.520499"), result, new Apfloat("5e-6"));
        assertEquals("-0.5 precision", 6, result.precision());

        x = new Apfloat("0");
        result = helper.erf(x);
        assertEquals("0 value", new Apfloat("0"), result);
        assertEquals("0 precision", Apfloat.INFINITE, result.precision());
    }

    public static void testErfc()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.erfc(x);
        assertEquals("value", new Apfloat("1.96611"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("-0.5");
        result = helper.erfc(x);
        assertEquals("-0.5 value", new Apfloat("1.52049"), result, new Apfloat("5e-5"));
        assertEquals("-0.5 precision", 6, result.precision());
    }

    public static void testErfi()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.erfi(x);
        assertEquals("value", new Apfloat("-4.58473"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("-0.5");
        result = helper.erfi(x);
        assertEquals("-0.5 value", new Apfloat("-0.614952"), result, new Apfloat("5e-6"));
        assertEquals("-0.5 precision", 6, result.precision());
    }

    public static void testInverseErf()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-0.5");
        Apfloat result = helper.inverseErf(x);
        assertEquals("value", new Apfloat("-0.476936"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("0.999999");
        result = helper.inverseErf(x);
        assertEquals("value", new Apfloat("3.45891"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        helper = new FixedPrecisionApfloatHelper(40);
        x = new Apfloat("0.9999999999999999999999999999999999999999");
        result = helper.inverseErf(x);
        assertEquals("value", new Apfloat("9.448789766720858262503185054107090039066"), result, new Apfloat("5e-39"));
        assertEquals("precision", 40, result.precision());

        x = new Apfloat("-0.9999999999999999999999999999999999999999");
        result = helper.inverseErf(x);
        assertEquals("value", new Apfloat("-9.448789766720858262503185054107090039066"), result, new Apfloat("5e-39"));
        assertEquals("precision", 40, result.precision());

        x = new Apfloat("0.1111111111111111111111111111111111111111", Apfloat.DEFAULT, 2);
        result = helper.inverseErf(x);
        assertEquals("0.1111111111111111111111111111111111111111 radix 2 value", new Apfloat("101.0000110100011110111111001101101111101", 40, 2), result, new Apfloat("1e-37", 1, 2));
        assertEquals("0.1111111111111111111111111111111111111111 radix 2 precision", 40, result.precision());
    }

    public static void testInverseErfc()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("0.5");
        Apfloat result = helper.inverseErfc(x);
        assertEquals("value", new Apfloat("0.476936"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("1.99999");
        result = helper.inverseErfc(x);
        assertEquals("value", new Apfloat("-3.12341"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("1e-1000");
        result = helper.inverseErfc(x);
        assertEquals("1e-1000 value", new Apfloat("47.9389"), result, new Apfloat("5e-4"));
        assertEquals("1e-1000 precision", 6, result.precision());

        helper = new FixedPrecisionApfloatHelper(40);
        x = new Apfloat("1.9999999999999999999999999999999999999999");
        result = helper.inverseErfc(x);
        assertEquals("value", new Apfloat("-9.448789766720858262503185054107090039065"), result, new Apfloat("5e-39"));
        assertEquals("precision", 40, result.precision());

        x = new Apfloat("1.111111111111111111111111111111111111111", Apfloat.DEFAULT, 2);
        result = helper.inverseErfc(x);
        assertEquals("1.111111111111111111111111111111111111111 radix 2 value", new Apfloat("-100.1111101111000101001000000001001100101", 40, 2), result, new Apfloat("1e-37", 1, 2));
        assertEquals("1.111111111111111111111111111111111111111 radix 2 precision", 40, result.precision());
    }

    public static void testFresnelS()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.fresnelS(x);
        assertEquals("value", new Apfloat("-0.697505"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testFresnelC()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.fresnelC(x);
        assertEquals("value", new Apfloat("-0.445261"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testExpIntegralE()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.expIntegralE(ν, x);
        assertEquals("value", new Apfloat("0.0559441"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testExpIntegralEi()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.expIntegralEi(x);
        assertEquals("value", new Apfloat("-0.100020"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLogIntegral()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("1.5");
        Apfloat result = helper.logIntegral(x);
        assertEquals("value", new Apfloat("0.125065"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testSinIntegral()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.sinIntegral(x);
        assertEquals("value", new Apfloat("-1.32468"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testCosIntegral()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("1.5");
        Apfloat result = helper.cosIntegral(x);
        assertEquals("value", new Apfloat("0.470356"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testSinhIntegral()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.sinhIntegral(x);
        assertEquals("value", new Apfloat("-1.70065"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testCoshIntegral()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("1.5");
        Apfloat result = helper.coshIntegral(x);
        assertEquals("value", new Apfloat("1.60063"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testAiryAi()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.airyAi(x);
        assertEquals("value", new Apfloat("0.464257"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("0");
        result = helper.airyAi(x);
        assertEquals("0 value", new Apfloat("0.355028"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testAiryAiPrime()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.airyAiPrime(x);
        assertEquals("value", new Apfloat("0.309187"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("0");
        result = helper.airyAiPrime(x);
        assertEquals("0 value", new Apfloat("-0.258819"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testAiryBi()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.airyBi(x);
        assertEquals("value", new Apfloat("-0.191785"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("0");
        result = helper.airyBi(x);
        assertEquals("0 value", new Apfloat("0.614927"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testAiryBiPrime()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.airyBiPrime(x);
        assertEquals("value", new Apfloat("0.557908"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("0");
        result = helper.airyBiPrime(x);
        assertEquals("0 value", new Apfloat("0.448288"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testBesselJ()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.besselJ(ν, x);
        assertEquals("value", new Apfloat("-0.140294"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        helper = new FixedPrecisionApfloatHelper(17);
        ν = new Apfloat("-1.0");
        x = new Apfloat("-1009.0");
        result = helper.besselJ(ν, x);
        assertEquals("-1 value", new Apfloat("0.0058796558493124600"), result, new Apfloat("5e-19"));
        assertEquals("-1 precision", 17, result.precision());
    }

    public static void testBesselI()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.besselI(ν, x);
        assertEquals("value", new Apfloat("1.81529"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBesselY()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.besselY(ν, x);
        assertEquals("value", new Apfloat("-0.525080"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBesselK()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.besselK(ν, x);
        assertEquals("value", new Apfloat("0.0910923"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testStruveH()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.struveH(ν, x);
        assertEquals("value", new Apfloat("-0.525080"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testStruveL()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.struveL(ν, x);
        assertEquals("value", new Apfloat("1.87328"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testAngerJ()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.angerJ(ν, x);
        assertEquals("value", new Apfloat("0.0698371"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testWeberE()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.weberE(ν, x);
        assertEquals("value", new Apfloat("0.447994"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testEllipticK()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.ellipticK(x);
        assertEquals("value", new Apfloat("1.23301"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("0");
        result = helper.ellipticK(x);
        assertEquals("0 value", new Apfloat("1.57080"), result, new Apfloat("5e-5"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testEllipticE()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("-1.5");
        Apfloat result = helper.ellipticE(x);
        assertEquals("value", new Apfloat("2.05299"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        x = new Apfloat("0");
        result = helper.ellipticE(x);
        assertEquals("0 value", new Apfloat("1.57080"), result, new Apfloat("5e-5"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testHermiteH()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.hermiteH(ν, x);
        assertEquals("value", new Apfloat("0.0789789"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLaguerreL()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.laguerreL(ν, x);
        assertEquals("value", new Apfloat("24.0654"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLaguerreLGeneralized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                λ = new Apfloat("-2.3"),
                x = new Apfloat("2.5");
        Apfloat result = helper.laguerreL(ν, λ, x);
        assertEquals("value", new Apfloat("84.8071"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLegendreP()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.legendreP(ν, x);
        assertEquals("value", new Apfloat("1.46890"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLegendrePAssociated()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                μ = new Apfloat("-2.3"),
                x = new Apfloat("0.9");
        Apfloat result = helper.legendreP(ν, μ, x);
        assertEquals("value", new Apfloat("0.0124664"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLegendreQ()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("0.9");
        Apfloat result = helper.legendreQ(ν, x);
        assertEquals("value", new Apfloat("0.787390"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLegendreQAssociated()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                μ = new Apfloat("-2.3"),
                x = new Apfloat("0.9");
        Apfloat result = helper.legendreQ(ν, μ, x);
        assertEquals("value", new Apfloat("-21.6240"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testChebyshevT()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.chebyshevT(ν, x);
        assertEquals("value", new Apfloat("5.29150"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testChebyshevU()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.chebyshevU(ν, x);
        assertEquals("value", new Apfloat("-0.377964"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testGegenbauerCRenormalized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.gegenbauerC(ν, x);
        assertEquals("value", new Apfloat("-7.05534"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testGegenbauerC()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                λ = new Apfloat("-2.3"),
                x = new Apfloat("2.5");
        Apfloat result = helper.gegenbauerC(ν, λ, x);
        assertEquals("value", new Apfloat("-136.555"), result, new Apfloat("5e-3"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testJacobiP()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                a = new Apfloat("-1.9"),
                b = new Apfloat("-2.3"),
                x = new Apfloat("2.5");
        Apfloat result = helper.jacobiP(ν, a, b, x);
        assertEquals("value", new Apfloat("29.5169"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testFibonacci()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("2.5");
        Apfloat result = helper.fibonacci(ν, x);
        assertEquals("value", new Apfloat("0.0648922"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testEulerE()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat result = helper.eulerE(4, new Apfloat("2.5"));
        assertEquals("value", new Apfloat("10.3125"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBernoulliB()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat result = helper.bernoulliB(4, new Apfloat("2.5"));
        assertEquals("value", new Apfloat("14.0292"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHarmonicNumber()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat result = helper.harmonicNumber(new Apfloat("2.5"));
        assertEquals("value", new Apfloat("1.68037"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHarmonicNumberGeneralized()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("1.5"),
                r = new Apfloat("2.5");
        Apfloat result = helper.harmonicNumber(x, r);
        assertEquals("value", new Apfloat("1.11412"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testPolylog()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat ν = new Apfloat("-1.5"),
                x = new Apfloat("0.9");
        Apfloat result = helper.polylog(ν, x);
        assertEquals("value", new Apfloat("368.902"), result, new Apfloat("5e-3"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testClausenC()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat s = new Apfloat("-1.5"),
                x = new Apfloat("0.9");
        Apfloat result = helper.clausenC(s, x);
        assertEquals("value", new Apfloat("-1.25061"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        helper = new FixedPrecisionApfloatHelper(6);
        result = helper.clausenC(new Apfloat(7), new Apfloat("0"));
        assertEquals("value", new Apfloat("1.00835"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testClausenCl()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat result = helper.clausenCl(6, new Apfloat("2.5"));
        assertEquals("value", new Apfloat("0.584647"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        helper = new FixedPrecisionApfloatHelper(6);
        result = helper.clausenCl(5, new Apfloat("0"));
        assertEquals("value", new Apfloat("1.03693"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testClausenS()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat s = new Apfloat("-1.5"),
                x = new Apfloat("0.9");
        Apfloat result = helper.clausenS(s, x);
        assertEquals("value", new Apfloat("-1.21519"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testClausenSl()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat result = helper.clausenSl(6, new Apfloat("2.5"));
        assertEquals("value", new Apfloat("-0.796391"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        helper = new FixedPrecisionApfloatHelper(6);
        result = helper.clausenSl(6, new Apfloat("0"));
        assertEquals("value", new Apfloat("1.01734"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLogisticSigmoid()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat x = new Apfloat("0.9");
        Apfloat result = helper.logisticSigmoid(x);
        assertEquals("value", new Apfloat("0.710950"), result, new Apfloat("5e-6"));
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

    public static void testContinuedFraction()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat[] result = helper.continuedFraction(new Apfloat("3.14159265358979323846264338328"), 3);
        assertEquals("values", 3, result.length);
        assertEquals("value[0]", new Apfloat(3), result[0]);
        assertEquals("precision[0]", 6, result[0].precision());
        assertEquals("value[1]", new Apfloat(7), result[1]);
        assertEquals("precision[1]", 6, result[1].precision());
        assertEquals("value[2]", new Apfloat(15), result[2]);
        assertEquals("precision[2]", 6, result[2].precision());
    }

    public static void testConvergents()
    {
        FixedPrecisionApfloatHelper helper = new FixedPrecisionApfloatHelper(6);
        Apfloat[] result = helper.convergents(new Apfloat("3.14159265358979323846264338328"), 3);
        assertEquals("values", 3, result.length);
        assertEquals("value[0]", new Apfloat("3.00000"), result[0]);
        assertEquals("precision[0]", 6, result[0].precision());
        assertEquals("value[1]", new Apfloat("3.14286"), result[1], new Apfloat("0.00005"));
        assertEquals("precision[1]", 6, result[1].precision());
        assertEquals("value[2]", new Apfloat("3.14151"), result[2], new Apfloat("0.00005"));
        assertEquals("precision[2]", 6, result[2].precision());
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
