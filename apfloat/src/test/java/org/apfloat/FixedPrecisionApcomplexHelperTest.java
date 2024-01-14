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

public class FixedPrecisionApcomplexHelperTest
    extends ApfloatTestCase
{
    public FixedPrecisionApcomplexHelperTest(String methodName)
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

        suite.addTest(new FixedPrecisionApcomplexHelperTest("testValue"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAdd"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testSubtract"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testMultiply"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testDivide"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testReal"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testImag"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testConj"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testArg"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testNegate"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAbs"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testNorm"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testScale"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testIntegerPow"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testInverseRoot"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testRoot"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testSqrt"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testCbrt"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAllRoots"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAgm"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testLog"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testLogBase"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testExp"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testPow"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAcosh"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAsinh"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAtanh"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testCosh"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testSinh"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testTanh"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAcos"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAsin"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAtan"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testCos"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testSin"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testTan"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testW"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testProduct"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testSum"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testGamma"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testGammaIncomplete"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testGammaIncompleteGeneralized"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testLogGamma"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testDigamma"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testPolygamma"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBeta"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBetaIncomplete"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBetaIncompleteGeneralized"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testPochhammer"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBinomial"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testZeta"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testZetaHurwitz"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testHypergeometric0F1"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testHypergeometric1F1"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testHypergeometric2F1"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testHypergeometric0F1Regularized"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testHypergeometric1F1Regularized"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testHypergeometric2F1Regularized"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testHypergeometricU"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testErf"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testErfc"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testErfi"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testFresnelS"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testFresnelC"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testExpIntegralE"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testExpIntegralEi"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testLogIntegral"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testSinIntegral"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testCosIntegral"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testSinhIntegral"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testCoshIntegral"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAiryAi"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAiryAiPrime"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAiryBi"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testAiryBiPrime"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBesselJ"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBesselI"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBesselY"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testBesselK"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testEllipticK"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testEllipticE"));
        suite.addTest(new FixedPrecisionApcomplexHelperTest("testUlp"));

        return suite;
    }

    public static void testValue()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("(2,3)");
        Apcomplex result = helper.valueOf(z);
        assertEquals("value", new Apcomplex("(2,3)"), result);

        assertEquals("precision", 20, result.precision());
    }

    public static void testAdd()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex(new Apfloat(2), new Apfloat(3));
        Apcomplex w = new Apcomplex(new Apfloat(4), new Apfloat(5));
        Apcomplex result = helper.add(z, w);
        assertEquals("value", new Apcomplex("(6, 8)"), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testSubtract()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex(new Apfloat(2), new Apfloat(3));
        Apcomplex w = new Apcomplex(new Apfloat(4), new Apfloat(7));
        Apcomplex result = helper.subtract(z, w);
        assertEquals("value", new Apcomplex(new Apfloat(-2), new Apfloat(-4)), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testMultiply()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("(1.0,1.0)");
        Apcomplex w = new Apcomplex("(2.0,2.0)");
        Apcomplex result = helper.multiply(z, w);
        assertEquals("value", new Apcomplex("(0,4.000)"), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testDivide()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(30);
        Apcomplex z = new Apcomplex("(0,4.0)");
        Apcomplex w = new Apcomplex("(2.0,2.0)");
        Apcomplex result = helper.divide(z, w);
        assertEquals("value", helper.valueOf(new Apcomplex("(1,1)")), result);
        assertEquals("precision", 30, result.precision());
    }

    public static void testReal()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(10);
        Apcomplex z = new Apcomplex("(2.0,3.0)");
        Apfloat result = helper.real(z);
        assertEquals("value", new Apfloat(2), result);
        assertEquals("precision", 10, result.precision());
    }

    public static void testImag()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(10);
        Apcomplex z = new Apcomplex("(2.0,3.0)");
        Apfloat result = helper.imag(z);
        assertEquals("value", new Apfloat(3), result);
        assertEquals("precision", 10, result.precision());
    }

    public static void testConj()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(10);
        Apcomplex z = new Apcomplex("(2.0,3.0)");
        Apcomplex result = helper.conj(z);
        assertEquals("value", new Apcomplex("(2.00,-3.00)"), result);
        assertEquals("precision", 10, result.precision());
    }

    public static void testArg()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(10);
        Apcomplex z = new Apcomplex("(2,-2)");
        Apfloat result = helper.arg(z);
        assertEquals("value", new Apfloat("-0.7853981634"), result, new Apfloat("1e-9"));
        assertEquals("precision", 10, result.precision());
    }

    public static void testNegate()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(50);
        Apcomplex z = new Apcomplex("(2.0,3.0)");
        Apcomplex result = helper.negate(z);
        assertEquals("value", new Apcomplex("(-2.0,-3.0)"), result);
        assertEquals("precision", 50, result.precision());
    }

    public static void testAbs()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("(4.0,3.0)");
        Apfloat result = helper.abs(z);
        assertEquals("value", new Apfloat(5), result, new Apfloat("1e-4"));
        assertEquals("precision", 20, result.precision());
    }

    public static void testNorm()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("(4.0,3.0)");
        Apcomplex result = helper.norm(z);
        assertEquals("value", new Apfloat(25), result, new Apfloat("1e-4"));
        assertEquals("precision", 20, result.precision());
    }

    public static void testScale()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("(4.0,3.0)");
        Apcomplex result = helper.scale(z, 1);
        assertEquals("value", new Apcomplex(new Apfloat(40), new Apfloat(30)), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testIntegerPow()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("(1.0,1.0)");
        Apcomplex result = helper.pow(z, 4);
        assertEquals("value", new Apfloat(-4), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testInverseRoot()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("-4.0");
        Apcomplex result = helper.inverseRoot(z, 4);
        assertEquals("value", new Apcomplex("(0.50,-0.50)"), result, new Apfloat("1e-20"));
        assertEquals("precision", 20, result.precision());

        result = helper.inverseRoot(z, 4, 1);
        assertEquals("value", new Apcomplex("(-0.50,-0.50)"), result, new Apfloat("1e-20"));
        assertEquals("precision", 20, result.precision());
    }

    public static void testRoot()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("-4.0");
        Apcomplex result = helper.root(z, 4);
        assertEquals("value", helper.valueOf(new Apcomplex("(1.0,1.0)")), result, new Apfloat("1e-19"));
        assertEquals("precision", 20, result.precision());

        result = helper.root(z, 4, 1);
        assertEquals("value", helper.valueOf(new Apcomplex("(-1.0,1.0)")), result, new Apfloat("1e-19"));
        assertEquals("precision", 20, result.precision());
    }

    public static void testSqrt()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("-4.0");
        Apcomplex result = helper.sqrt(z);
        assertEquals("value", helper.valueOf(new Apcomplex("(0,2)")), result, new Apfloat("1e-19"));
        assertEquals("precision", 20, result.precision());
    }

    public static void testCbrt()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("(-16,16)");
        Apcomplex result = helper.cbrt(z);
        assertEquals("value", helper.valueOf(new Apcomplex("(2,2)")), result, new Apfloat("1e-19"));
        assertEquals("precision", 20, result.precision());
    }

    public static void testAllRoots()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex z = new Apcomplex("-4.0");
        Apcomplex[] results = helper.allRoots(z, 4);
        assertEquals("results", 4, results.length);
        assertEquals("value 0", helper.valueOf(new Apcomplex("(1.0,1.0)")), results[0], new Apfloat("1e-19"));
        assertEquals("precision 0", 20, results[0].precision());
        assertEquals("value 1", helper.valueOf(new Apcomplex("(-1.0,1.0)")), results[1], new Apfloat("1e-19"));
        assertEquals("precision 1", 20, results[1].precision());
        assertEquals("value 2", helper.valueOf(new Apcomplex("(-1.0,-1.0)")), results[2], new Apfloat("1e-19"));
        assertEquals("precision 2", 20, results[2].precision());
        assertEquals("value 3", helper.valueOf(new Apcomplex("(1.0,-1.0)")), results[3], new Apfloat("1e-19"));
        assertEquals("precision 3", 20, results[3].precision());
    }

    public static void testAgm()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex a = new Apcomplex(new Apfloat(2), new Apfloat(3));
        Apcomplex b = new Apcomplex(new Apfloat(4), new Apfloat(5));
        Apcomplex result = helper.agm(a, b);
        assertEquals("value", new Apcomplex("(2.917544260525786263696583078746606829791948283056476178719125080604001346981059514497122989501231285,3.939113046692836869408348425071199799284873423064425353396384561367021573926690156072311985300901402)"), result, new Apfloat("5e-99"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testLog()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(40);
        Apcomplex z = new Apcomplex("-1");
        Apcomplex result = helper.log(z);
        assertEquals("value", new Apcomplex("(0,3.141592653589793238462643383279502884197)"), result, new Apfloat("1e-39"));
        assertEquals("precision", 40, result.precision());

        z = new Apcomplex("-1.000000000000000000000000000000001");
        result = helper.log(z);
        assertEquals("close to 1 value", new Apcomplex("(9.999999999999999999999999999999995000000e-34,3.141592653589793238462643383279502884197)"), result, new Apfloat("1e-39"));
        assertEquals("close to 1 precision", 40, result.precision());
    }

    public static void testLogBase()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(40);
        Apcomplex z = new Apcomplex("(0,1)");
        Apcomplex w = new Apcomplex("-1");
        Apcomplex result = helper.log(z, w);
        assertEquals("value", helper.valueOf(new Apcomplex("0.5")), result, new Apfloat("1e-39"));
        assertEquals("precision", 40, result.precision());
    }

    public static void testExp()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(40);
        Apcomplex z = new Apcomplex(new Apfloat(0, 1L, 17));
        Apcomplex result = helper.exp(z);
        assertEquals("value", Apfloat.ONE, result);
        assertEquals("precision", 40, result.precision());
        assertEquals("radix", 17, result.radix());

        z = new Apcomplex(new Apfloat(5000000000000L));
        result = helper.exp(z);
        assertEquals("big value", new Apcomplex("1.816093715813449977121047779023089136641e2171472409516"), result, new Apfloat("5e2171472409477"));
        assertEquals("big precision", 40, result.precision());

        z = new Apcomplex("(5000000000000,3.141592653589793238462643383279502884197)");
        result = helper.exp(z);
        assertEquals("big value", new Apcomplex("-1.816093715813449977121047779023089136641e2171472409516"), result, new Apfloat("5e2171472409477"));
        assertEquals("big precision", 40, result.precision());

        z = new Apcomplex(new Apfloat("1e-1000000000000000"));
        result = helper.exp(z);
        assertEquals("small value", new Apcomplex("1"), result, new Apfloat("5e-39"));
        assertEquals("small precision", 40, result.precision());
    }

    public static void testPow()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(5), new Apfloat(4));
        Apcomplex w = new Apcomplex(new Apfloat(3), new Apfloat(2));
        Apcomplex result = helper.pow(z, w);
        assertEquals("value", new Apcomplex("(58.21450097059152465652652712230313017748868340728034764752454732020721827721557252703139305798831592,-35.32343693485837533012378741846981988609521878485308616153729164456178404672985609549591376583171254)"), result, new Apfloat("15e-98"));
        assertEquals("precision", 100, result.precision());

        z = new Apcomplex(new Apfloat(5), new Apfloat(6));
        w = Apcomplex.ZERO;
        result = helper.pow(z, w);
        assertEquals("value z^0", Apcomplex.ONE, result);
        assertEquals("precision z^0", 100, result.precision());

        z = new Apcomplex(new Apfloat(5), new Apfloat(6));
        w = Apcomplex.ONE;
        result = helper.pow(z, w);
        assertEquals("value z^1", new Apcomplex(new Apfloat(5), new Apfloat(6)), result);
        assertEquals("precision z^1", 100, result.precision());

        z = Apcomplex.ONE;
        w = new Apcomplex(new Apfloat(5), new Apfloat(6));
        result = helper.pow(z, w);
        assertEquals("value 1^z", Apcomplex.ONE, result);
        assertEquals("precision 1^z", 100, result.precision());

        try
        {
            helper.pow(new Apcomplex(new Apfloat(0)), new Apcomplex(new Apfloat(0)));
            fail("0^0 accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be undefined
        }

        try
        {
            helper.pow(new Apcomplex(new Apfloat(100), new Apfloat(100)),
                       new Apcomplex(new Apfloat(5000000000000000000L), new Apfloat(5000000000000000000L)));
            fail("Overflow should have occurred");
        }
        catch (OverflowException oe)
        {
            // OK; result would overflow
        }
    }

    public static void testAcosh()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.acosh(z);
        assertEquals("value", new Apcomplex("(2.30550903124347694204183593813343089732908234612766434427244403789502387715767721380519816885689075,0.9368124611557199029125245765756089164871812290143448233044479241680079302681295000053794681278219233)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        z = Apcomplex.ZERO;
        result = helper.acosh(z);
        assertEquals("zero value", new Apcomplex(Apfloat.ZERO, new Apfloat("1.570796326794896619231321691639751442098584699687552910487472296153908203143104499314017412671058534")), result, new Apfloat("1e-98"));
        assertEquals("zero precision", 100, result.precision());

        z = new Apint(0, 12);
        result = helper.acosh(z);
        assertEquals("zero real radix", 12, result.real().radix());
    }

    public static void testAsinh()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.asinh(z);
        assertEquals("value", new Apcomplex("(2.299914040879269649955789630663175555365313484764636466611830082402437121311729696004733902877606405,0.9176168533514786557598627486701745415899523820362300027773647609161124445462833451286169894870273957)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testAtanh()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.atanh(z);
        assertEquals("value", new Apcomplex("(0.1175009073114338884127342577870855161752247622030620101123480342515004695503565955468640257240191129,1.409921049596575522530619384460420782588207051908724814771070766475530084440199227135813201495737847)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        try
        {
            helper.atanh(new Apcomplex("1"));
            fail("atanh(1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }

        try
        {
            helper.atanh(new Apcomplex("-1"));
            fail("atanh(-1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }
    }

    public static void testCosh()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.cosh(z);
        assertEquals("value", new Apcomplex("(-6.580663040551156432560744126538803616711267345515897773220218329756121215365251384163430874396326777,-7.581552742746544353716345286538426009387527590948852812949363456244614022672964969341075109130625439)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testSinh()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.sinh(z);
        assertEquals("value", new Apcomplex("(-6.548120040911001647766811018835324740820888396888583499736134313039666841835229556393917343956455199,-7.619231720321410208487135736804311796557265472675575619426852074665542955161180340917983240028178743)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        z = new Apcomplex("(3e-1000000000000,4e-1000000000000)");
        result = helper.sinh(z);
        assertEquals("small value", new Apcomplex("(3e-1000000000000,4e-1000000000000)").precision(100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());
    }

    public static void testTanh()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.tanh(z);
        assertEquals("value", new Apcomplex("(1.000709536067232939329585472404172746215320905146760218019260729904286640361616955165037427906522640,0.004908258067496060259078786929932766843374215579355506974895511342674738432081043949327359968992711)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        z = new Apcomplex(new Apfloat(5000000000000L), new Apfloat("3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117068"));
        result = helper.tanh(z);
        assertEquals("big value", new Apcomplex(new Apfloat(1)), result, new Apfloat("1e-99"));
        assertEquals("big precision", 100, result.precision());

        z = new Apcomplex(new Apfloat(-5000000000000L), new Apfloat("3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117068"));
        result = helper.tanh(z);
        assertEquals("big negative value", new Apcomplex(new Apfloat(-1)), result, new Apfloat("1e-99"));
        assertEquals("big negative precision", 100, result.precision());

        z = new Apcomplex("(3e-1000000000000,4e-1000000000000)");
        result = helper.tanh(z);
        assertEquals("small value", new Apcomplex("(3e-1000000000000,4e-1000000000000)").precision(100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());
    }

    public static void testAcos()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.acos(z);
        assertEquals("value", new Apcomplex("(0.9368124611557199029125245765756089164871812290143448233044479241680079302681295000053794681278219233,-2.305509031243476942041835938133430897329082346127664344272444037895023877157677213805198168856890747)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        z = Apcomplex.ZERO;
        result = helper.acos(z);
        assertEquals("value", new Apfloat("1.570796326794896619231321691639751442098584699687552910487472296153908203143104499314017412671058534"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testAsin()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.asin(z);
        assertEquals("value", new Apcomplex("(0.6339838656391767163187971150641425256114034706732080871830243719859002728749749993086379445432366107,2.305509031243476942041835938133430897329082346127664344272444037895023877157677213805198168856890747)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testAtan()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.atan(z);
        assertEquals("value", new Apcomplex("(1.448306995231464542145280451034113536641512650496960876923784338820230643349283451026750333836707538,0.1589971916799991743647610360070187815733054742350614709569622676518259973409283367912158396025096925)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
    }

    public static void testCos()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.cos(z);
        assertEquals("value", new Apcomplex("(-27.03494560307422464769480266827091348467753695567661661019265514673434246483988229429946831870519301,-3.851153334811777536563337123053124569704160846091637003157728595256494186490481089994453362578315815)"), result, new Apfloat("1e-97"));
        assertEquals("precision", 100, result.precision());

        // Loss of precision
        helper.cos(new Apcomplex(new Apfloat("1e1000", 3), new Apfloat("1.5")));
    }

    public static void testSin()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.sin(z);
        assertEquals("value", new Apcomplex("(3.853738037919377321617528940463730667068274946989034956763346803317838585207899050385464301460315524,-27.01681325800393448809754375499215226336386568976518470594798897425063415478434990691671779691472675)"), result, new Apfloat("1e-97"));
        assertEquals("precision", 100, result.precision());

        z = new Apcomplex("(3e-1000000000000,4e-1000000000000)");
        result = helper.sin(z);
        assertEquals("small value", new Apcomplex("(3e-1000000000000,4e-1000000000000)").precision(100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());

        // Loss of precision
        helper.sin(new Apcomplex(new Apfloat("1e1000", 3), new Apfloat("1.5")));
    }

    public static void testTan()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.tan(z);
        assertEquals("value", new Apcomplex("(-0.0001873462046294784262242556377282181042124242427296606263580802232052224832174311687842725259181727521,0.9993559873814731413916496303201330615648885028135384928319757364498179348866065958722698773248799920)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());

        z = new Apcomplex(Apfloat.ZERO, new Apfloat(5000000000000L));
        result = helper.tan(z);
        assertEquals("big value", new Apcomplex(Apfloat.ZERO, new Apfloat(1)), result, new Apfloat("1e-99"));
        assertEquals("big precision", 100, result.precision());

        z = new Apcomplex(Apfloat.ZERO, new Apfloat(-5000000000000L));
        result = helper.tan(z);
        assertEquals("big negative value", new Apcomplex(Apfloat.ZERO, new Apfloat(-1)), result, new Apfloat("1e-99"));
        assertEquals("big negative precision", 100, result.precision());

        z = new Apcomplex("(3e-1000000000000,4e-1000000000000)");
        result = helper.tan(z);
        assertEquals("small value", new Apcomplex("(3e-1000000000000,4e-1000000000000)").precision(100), result, new Apfloat("1e-1000000000099"));
        assertEquals("small precision", 100, result.precision());

        // Loss of precision
        helper.tan(new Apcomplex(new Apfloat("1e1000", 3), new Apfloat("1.5")));
    }

    public static void testW()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(10);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.w(z);
        assertEquals("value", new Apcomplex("(1.2815618061,0.5330952220)"), result, new Apfloat("5e-9"));
        assertEquals("precision", 10, result.precision());

        result = helper.w(z, -1);
        assertEquals("value -1", new Apcomplex("(0.2585674069,-3.8521166862)"), result, new Apfloat("5e-9"));
        assertEquals("precision -1", 10, result.precision());
    }

    public static void testProduct()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex[] z = { new Apcomplex("(1,1)"),
                          new Apcomplex("(10.1,10.1)"),
                          new Apcomplex(new Apfloat(4), new Apfloat(4)) };
        Apcomplex result = helper.product(z);
        assertEquals("value", new Apcomplex("(-80.8,80.8)"), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testSum()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(20);
        Apcomplex[] z = { new Apcomplex("(12345,1e10)"),
                          new Apcomplex("(0.6789,1)") };
        Apcomplex result = helper.sum(z);
        assertEquals("value", new Apcomplex("(12345.6789,10000000001)"), result);
        assertEquals("precision", 20, result.precision());
    }

    public static void testGamma()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(26);
        Apcomplex z = new Apcomplex("(1,1000000000000.1)");
        Apcomplex result = helper.gamma(z);
        assertEquals("value", new Apcomplex("(2.2799397381057012808806415e-682188176916,2.56143734228029034694359025e-682188176915)"), result, new Apfloat("5e-682188176941"));
        assertEquals("precision", 26, result.precision());
    }

    public static void testGammaIncomplete()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(5);
        Apcomplex a = new Apcomplex("(1,2)");
        Apcomplex z = new Apcomplex("(3,4)");
        Apcomplex result = helper.gamma(a, z);
        assertEquals("value", new Apcomplex("(0.0087875,-0.0046329)"), result, new Apfloat("5e-7"));
        assertEquals("precision", 5, result.precision());
    }

    public static void testGammaIncompleteGeneralized()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(1,2)");
        Apcomplex z0 = new Apcomplex("(3,4)");
        Apcomplex z1 = new Apcomplex("(5,6)");
        Apcomplex result = helper.gamma(a, z0, z1);
        assertEquals("value", new Apcomplex("(0.00896428,-0.00326815)"), result, new Apfloat("5e-8"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLogGamma()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(-2.5,1.3)");
        Apcomplex result = helper.logGamma(z);
        assertEquals("value", new Apcomplex("(-3.17613,-7.9530)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.logGamma(new Apcomplex("0"));
            fail("logGamma(0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testDigamma()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(-2.5,1.3)");
        Apcomplex result = helper.digamma(z);
        assertEquals("value", new Apcomplex("(1.18732,2.73369)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.digamma(new Apcomplex("0"));
            fail("digamma(0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testPolygamma()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.polygamma(2, z);
        assertEquals("value", new Apcomplex("(0.0144551,0.0206851)"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.polygamma(1, new Apcomplex("0"));
            fail("polygamma(1, 0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }

        try
        {
            helper.polygamma(-1, new Apcomplex("1"));
            fail("polygamma(-1, 1) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testBeta()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(1.2,3.4)"),
                  b = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.beta(a, b);
        assertEquals("value", new Apcomplex("(0.0133876,-0.0507409)"), result, new Apfloat("5e-7"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.beta(new Apcomplex("0"), new Apcomplex("0"));
            fail("beta(0, 0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testBetaIncomplete()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(1.2,3.4)"),
                  a = new Apcomplex("(3.4,5.6)"),
                  b = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.beta(z, a, b);
        assertEquals("value", new Apcomplex("(464507,2794.79)"), result, new Apfloat("5e0"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.beta(new Apcomplex("0"), new Apcomplex("0"), new Apcomplex("0"));
            fail("beta(0, 0, 0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testBetaIncompleteGeneralized()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z1 = new Apcomplex("(1.2,3.4)"),
                  z2 = new Apcomplex("(2.3,3.4)"),
                  a = new Apcomplex("(3.4,5.6)"),
                  b = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.beta(z1, z2, a, b);
        assertEquals("value", new Apcomplex("(2.07486e7,-3.71971e7)"), result, new Apfloat("5e2"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.beta(new Apcomplex("0"), new Apcomplex("1"), new Apcomplex("0"), new Apcomplex("0"));
            fail("beta(0, 1, 0, 0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testPochhammer()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(1.2,3.4)"),
                  n = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.pochhammer(z, n);
        assertEquals("value", new Apcomplex("(-0.220154,0.555530)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.pochhammer(new Apcomplex("-0.5"), new Apcomplex("0.5"));
            fail("pochhammer(-0.5, 0.5) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testBinomial()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(2.5,3.5)");
        Apcomplex y = new Apcomplex("(1.2,2.3)");
        Apcomplex result = helper.binomial(z, y);
        assertEquals("value", new Apcomplex("(-0.819659,2.18133)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.binomial(new Apcomplex("-3"), new Apcomplex("(0,1)"));
            fail("binomial(-3,i) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testZeta()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex s = new Apcomplex("(1.5,2.5)");
        Apcomplex result = helper.zeta(s);
        assertEquals("value", new Apcomplex("(0.725036,-0.213363)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        s = new Apcomplex("(0.5,10000)");
        result = helper.zeta(s);
        assertEquals("value", new Apcomplex("(-0.339374,-0.0370915)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        s = new Apcomplex("(0.5,51000)");
        result = helper.zeta(s);
        assertEquals("value", new Apcomplex("(0.00408268,0.00102467)"), result, new Apfloat("5e-8"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testZetaHurwitz()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex s = new Apcomplex("(-1.5,-2.5)"),
                  a = new Apcomplex("(1.5,2.5)");
        Apcomplex result = helper.zeta(s, a);
        assertEquals("value", new Apcomplex("(0.00199905,0.163402)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper = new FixedPrecisionApcomplexHelper(17);
            s = new Apcomplex("(-8.384883669867978e17, 0.0)");
            a = new Apcomplex("(-8e-1, 1.199999999999999)");
            helper.zeta(s, a);
            fail("No overflow");
        }
        catch (OverflowException oe)
        {
            // OK
        }
    }

    public static void testHypergeometric0F1()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(-1.5,-2.5)"),
                  z = new Apcomplex("(1.5,2.5)");
        Apcomplex result = helper.hypergeometric0F1(a, z);
        assertEquals("value", new Apcomplex("(0.414184,-0.0472966)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric1F1()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(-1.5,-2.5)"),
                  b = new Apcomplex("(-2.5,-3.5)"),
                  z = new Apcomplex("(1.5,2.5)");
        Apcomplex result = helper.hypergeometric1F1(a, b, z);
        assertEquals("value", new Apcomplex("(0.0471755,1.98433)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric2F1()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(-1.5,-2.5)"),
                  b = new Apcomplex("(-2.5,-3.5)"),
                  c = new Apcomplex("(-4.5,-5.5)"),
                  z = new Apcomplex("(1.5,2.5)");
        Apcomplex result = helper.hypergeometric2F1(a, b, c, z);
        assertEquals("value", new Apcomplex("(27.9682,-33.1208)"), result, new Apfloat("5e-3"));    // Note that result isn't that accurate
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric0F1Regularized()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(1.2,3.4)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.hypergeometric0F1Regularized(a, z);
        assertEquals("value", new Apcomplex("(-146.586,-325.487)"), result, new Apfloat("5e-3"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric1F1Regularized()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(1.2,3.4)"),
                  b = new Apcomplex("(3.4,5.6)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.hypergeometric1F1Regularized(a, b, z);
        assertEquals("value", new Apcomplex("(-166.217,-313.204)"), result, new Apfloat("5e-3"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometric2F1Regularized()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(1.2,3.4)"),
                  b = new Apcomplex("(2.3,4.5)"),
                  c = new Apcomplex("(3.4,5.6)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.hypergeometric2F1Regularized(a, b, c, z);
        assertEquals("value", new Apcomplex("(0.00228989,0.00227117)"), result, new Apfloat("5e-8"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testHypergeometricU()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex a = new Apcomplex("(1.2,3.4)"),
                  b = new Apcomplex("(3.4,5.6)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.hypergeometricU(a, b, z);
        assertEquals("value", new Apcomplex("(-0.27912,-1.99355)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        try
        {
            helper.hypergeometricU(new Apcomplex("1"), new Apcomplex("2"), new Apcomplex("0"));
            fail("U(1, 2, 0) accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testErf()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(1.2,3.4)");
        Apcomplex result = helper.erf(z);
        assertEquals("value", new Apcomplex("(3998.48,246.877)"), result, new Apfloat("5e-2"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("1e6");
        result = helper.erf(z);
        assertEquals("1000000 value", new Apcomplex("1.00000"), result, new Apfloat("5e-5"));
        assertEquals("1000000 precision", 6, result.precision());

        z = new Apcomplex("(1000000,0.100000)");
        result = helper.erf(z);
        assertEquals("1000000+01.i value", new Apcomplex("1.00000"), result, new Apfloat("5e-5"));
        assertEquals("1000000+01.i precision", 6, result.precision());

        z = new Apcomplex("(0.100000,1000000)");
        result = helper.erf(z);
        assertEquals("0.1+1000000i value", new Apcomplex("(-7.12728e434294481895,9.94943e434294481896)"), result, new Apfloat("5e434294481891"));
        assertEquals("0.1+1000000i precision", 6, result.precision());

        z = new Apcomplex("(-0.1,-0.2)");
        result = helper.erf(z);
        assertEquals("-0.1-0.2i value", new Apcomplex("(-0.117021,-0.226384)"), result, new Apfloat("5e-6"));
        assertEquals("-0.1-0.2i precision", 6, result.precision());

        z = new Apcomplex("0");
        result = helper.erf(z);
        assertEquals("0 value", new Apfloat("0"), result);
        assertEquals("0 precision", Apfloat.INFINITE, result.precision());
    }

    public static void testErfc()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(1.2,3.4)");
        Apcomplex result = helper.erfc(z);
        assertEquals("value", new Apcomplex("(-3997.48,-246.877)"), result, new Apfloat("5e-2"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("1e6");
        result = helper.erfc(z);
        assertEquals("1000000 value", new Apcomplex("3.15934e-434294481910"), result, new Apfloat("5e-434294481915"));
        assertEquals("1000000 precision", 6, result.precision());

        z = new Apcomplex("-1e6");
        result = helper.erfc(z);
        assertEquals("-1000000 value", new Apcomplex("2.00000"), result, new Apfloat("5e-5"));
        assertEquals("-1000000 precision", 6, result.precision());

        z = new Apcomplex("(1000000,0.100000)");
        result = helper.erfc(z);
        assertEquals("1000000+01.i value", new Apcomplex("(3.18294e-434294481910,2.28009e-434294481911)"), result, new Apfloat("5e-434294481915"));
        assertEquals("1000000+01.i precision", 6, result.precision());

        z = new Apcomplex("(-1000000,0.100000)");
        result = helper.erfc(z);
        assertEquals("-1000000+01.i value", new Apcomplex("2.00000"), result, new Apfloat("5e-5"));
        assertEquals("-1000000+01.i precision", 6, result.precision());

        z = new Apcomplex("(0.100000,1000000)");
        result = helper.erfc(z);
        assertEquals("0.1+1000000i value", new Apcomplex("(7.12728e434294481895,-9.94943e434294481896)"), result, new Apfloat("5e434294481891"));
        assertEquals("0.1+1000000i precision", 6, result.precision());

        z = new Apcomplex("(-0.100000,1000000)");
        result = helper.erfc(z);
        assertEquals("-0.1+1000000i value", new Apcomplex("(-7.12728e434294481895,-9.94943e434294481896)"), result, new Apfloat("5e434294481891"));
        assertEquals("-0.1+1000000i precision", 6, result.precision());

        z = new Apcomplex("(-0.1,-0.2)");
        result = helper.erfc(z);
        assertEquals("-0.1-0.2i value", new Apcomplex("(1.11702,0.226384)"), result, new Apfloat("5e-5"));
        assertEquals("-0.1-0.2i precision", 6, result.precision());
    }

    public static void testErfi()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(4.3,2.1)");
        Apcomplex result = helper.erfi(z);
        assertEquals("value", new Apcomplex("(47249.6,-148255)"), result, new Apfloat("5e0"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("1e6");
        result = helper.erfi(z);
        assertEquals("1000000 value", new Apcomplex("1.00751e434294481897"), result, new Apfloat("5e434294481892"));
        assertEquals("1000000 precision", 6, result.precision());

        z = new Apcomplex("(1000000,0.100000)");
        result = helper.erfi(z);
        assertEquals("1000000+01.i value", new Apcomplex("(9.94943e434294481896,-7.12728e434294481895)"), result, new Apfloat("5e434294481891"));
        assertEquals("1000000+01.i precision", 6, result.precision());

        z = new Apcomplex("(0.100000,1000000)");
        result = helper.erfi(z);
        assertEquals("0.1+1000000i value", new Apcomplex("(-2.28002e-434294481911,1.00000)"), result, new Apfloat("5e-5"));
        assertEquals("0.1+1000000i precision", 6, result.precision());

        z = new Apcomplex("(-0.1,-0.2)");
        result = helper.erfi(z);
        assertEquals("-0.1-0.2i value", new Apcomplex("(-0.108747,-0.224881)"), result, new Apfloat("5e-6"));
        assertEquals("-0.1-0.2i precision", 6, result.precision());
    }

    public static void testFresnelS()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.fresnelS(z);
        assertEquals("value", new Apcomplex("(-5.2504e23,2.26321e24)"), result, new Apfloat("5e19"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testFresnelC()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.fresnelC(z);
        assertEquals("value", new Apcomplex("(2.26321e24,5.2504e23)"), result, new Apfloat("5e19"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testExpIntegralE()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex ν = new Apcomplex("(1.2,3.4)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.expIntegralE(ν, z);
        assertEquals("value", new Apcomplex("(-0.000238621,-0.000158482)"), result, new Apfloat("5e-9"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testExpIntegralEi()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.expIntegralEi(z);
        assertEquals("value", new Apcomplex("(-1.37581,-1.4625)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testLogIntegral()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.logIntegral(z);
        assertEquals("value", new Apcomplex("(3.69954,3.27594)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testSinIntegral()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.sinIntegral(z);
        assertEquals("value", new Apcomplex("(11.0328,-21.5511)"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testCosIntegral()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.cosIntegral(z);
        assertEquals("value", new Apcomplex("(-21.5507,-9.46169)"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testSinhIntegral()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.sinhIntegral(z);
        assertEquals("value", new Apcomplex("(-0.685633,-0.731803)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testCoshIntegral()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.coshIntegral(z);
        assertEquals("value", new Apcomplex("(-0.690179,-0.730699)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testAiryAi()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.airyAi(z);
        assertEquals("value", new Apcomplex("(0.0504756,0.110807)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("0");
        result = helper.airyAi(z);
        assertEquals("0 value", new Apcomplex("0.355028"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testAiryAiPrime()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.airyAiPrime(z);
        assertEquals("value", new Apcomplex("(0.0219253,-0.311242)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("0");
        result = helper.airyAiPrime(z);
        assertEquals("0 value", new Apcomplex("-0.258819"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testAiryBi()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.airyBi(z);
        assertEquals("value", new Apcomplex("(-0.154222,-0.458070)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("0");
        result = helper.airyBi(z);
        assertEquals("0 value", new Apcomplex("0.614927"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testAiryBiPrime()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.airyBiPrime(z);
        assertEquals("value", new Apcomplex("(0.870487,-1.15895)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("0");
        result = helper.airyBiPrime(z);
        assertEquals("0 value", new Apcomplex("0.448288"), result, new Apfloat("5e-6"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testBesselJ()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex ν = new Apcomplex("(1.2,3.4)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.besselJ(ν, z);
        assertEquals("value", new Apcomplex("(-3.00868,0.804693)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBesselI()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex ν = new Apcomplex("(1.2,3.4)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.besselI(ν, z);
        assertEquals("value", new Apcomplex("(30.6487,11.1050)"), result, new Apfloat("5e-4"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBesselY()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex ν = new Apcomplex("(1.2,3.4)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.besselY(ν, z);
        assertEquals("value", new Apcomplex("(-0.79791,-2.99960)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testBesselK()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex ν = new Apcomplex("(1.2,3.4)"),
                  z = new Apcomplex("(5.6,7.8)");
        Apcomplex result = helper.besselK(ν, z);
        assertEquals("value", new Apcomplex("(0.000359807,-0.00146406)"), result, new Apfloat("5e-8"));
        assertEquals("precision", 6, result.precision());
    }

    public static void testEllipticK()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.ellipticK(z);
        assertEquals("value", new Apcomplex("(0.825751,0.572097)"), result, new Apfloat("5e-6"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("0");
        result = helper.ellipticK(z);
        assertEquals("0 value", new Apcomplex("1.57080"), result, new Apfloat("5e-5"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testEllipticE()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(6);
        Apcomplex z = new Apcomplex("(3.4,5.6)");
        Apcomplex result = helper.ellipticE(z);
        assertEquals("value", new Apcomplex("(1.71210,-1.85499)"), result, new Apfloat("5e-5"));
        assertEquals("precision", 6, result.precision());

        z = new Apcomplex("0");
        result = helper.ellipticE(z);
        assertEquals("0 value", new Apcomplex("1.57080"), result, new Apfloat("5e-5"));
        assertEquals("0 precision", 6, result.precision());
    }

    public static void testUlp()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(5);
        Apfloat result = helper.ulp(new Apcomplex("(10,2)"));
        assertEquals("(10,2) value", new Apfloat("0.001"), result);
        assertEquals("(10,2) precision", 5, result.precision());
    }
}
