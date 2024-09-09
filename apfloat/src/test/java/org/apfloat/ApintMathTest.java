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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestSuite;

/**
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class ApintMathTest
    extends ApfloatTestCase
{
    public ApintMathTest(String methodName)
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

        suite.addTest(new ApintMathTest("testIntegerPow"));
        suite.addTest(new ApintMathTest("testRoot"));
        suite.addTest(new ApintMathTest("testScale"));
        suite.addTest(new ApintMathTest("testAbs"));
        suite.addTest(new ApintMathTest("testCopySign"));
        suite.addTest(new ApintMathTest("testNegate"));
        suite.addTest(new ApintMathTest("testDiv"));
        suite.addTest(new ApintMathTest("testGcd"));
        suite.addTest(new ApintMathTest("testLcm"));
        suite.addTest(new ApintMathTest("testModMultiply"));
        suite.addTest(new ApintMathTest("testModPow"));
        suite.addTest(new ApintMathTest("testFactorial"));
        suite.addTest(new ApintMathTest("testDoubleFactorial"));
        suite.addTest(new ApintMathTest("testProduct"));
        suite.addTest(new ApintMathTest("testBinomial"));
        suite.addTest(new ApintMathTest("testSum"));
        suite.addTest(new ApintMathTest("testRandom"));
        suite.addTest(new ApintMathTest("testMax"));
        suite.addTest(new ApintMathTest("testMin"));

        return suite;
    }

    public static void testIntegerPow()
    {
        Apint x = new Apint(2);
        assertEquals("2^30", new Apint(1 << 30), ApintMath.pow(x, 30));
        assertEquals("2^60", new Apint(1L << 60), ApintMath.pow(x, 60));
        assertEquals("2^-1", new Apint(0), ApintMath.pow(x, -1));
        assertEquals("2^-3", new Apint(0), ApintMath.pow(x, -3));
        assertEquals("2^0", new Apint(1), ApintMath.pow(x, 0));

        assertEquals("2^-3 radix", 12, ApintMath.pow(new Apint(2, 12), -3).radix());

        try
        {
            ApintMath.pow(new Apint(0), 0);
            fail("0^0 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be undefined
            assertEquals("Localization key", "pow.zeroToZero", aae.getLocalizationKey());
        }
    }

    public static void testRoot()
    {
        Apint[] r = ApintMath.sqrt(new Apint(143));
        assertEquals("sqrt(143) quot", new Apint(11), r[0]);
        assertEquals("sqrt(143) rem", new Apint(22), r[1]);

        r = ApintMath.sqrt(new Apint(144));
        assertEquals("sqrt(144) quot", new Apint(12), r[0]);
        assertEquals("sqrt(144) rem", new Apint(0), r[1]);

        r = ApintMath.sqrt(new Apint("15241578780673678546105778281054720515622620750190520"));
        assertEquals("sqrt(123456789123456789123456789^2-1) quot", new Apint("123456789123456789123456788"), r[0]);
        assertEquals("sqrt(123456789123456789123456789^2-1) rem", new Apint("246913578246913578246913576"), r[1]);

        r = ApintMath.sqrt(new Apint("15241578780673678546105778281054720515622620750190521"));
        assertEquals("sqrt(123456789123456789123456789^2) quot", new Apint("123456789123456789123456789"), r[0]);
        assertEquals("sqrt(123456789123456789123456789^2) rem", new Apint(0), r[1]);

        r = ApintMath.sqrt(new Apint("15241578780673678546105778311537878076969977842402077577351019811918920046486820281054720515622620750190521"));
        assertEquals("sqrt(123456789123456789123456789123456789123456789123456789^2) quot", new Apint("123456789123456789123456789123456789123456789123456789"), r[0]);
        assertEquals("sqrt(123456789123456789123456789123456789123456789123456789^2) rem", new Apint(0), r[1]);

        r = ApintMath.cbrt(new Apint(-1727));
        assertEquals("cbrt(-1727) quot", new Apint(-11), r[0]);
        assertEquals("cbrt(-1727) rem", new Apint(-396), r[1]);

        r = ApintMath.cbrt(new Apint(-1728));
        assertEquals("cbrt(-1728) quot", new Apint(-12), r[0]);
        assertEquals("cbrt(-1728) rem", new Apint(0), r[1]);

        r = ApintMath.cbrt(new Apint("1881676377434183987554591826597870779196556262659441282631554954480361860897068"));
        assertEquals("cbrt(123456789123456789123456789^3-1) quot", new Apint("123456789123456789123456788"), r[0]);
        assertEquals("cbrt(123456789123456789123456789^3-1) rem", new Apint("45724736342021035638317334472793794176497494880201196"), r[1]);

        r = ApintMath.cbrt(new Apint("1881676377434183987554591826597870779196556262659441282631554954480361860897069"));
        assertEquals("cbrt(123456789123456789123456789^3) quot", new Apint("123456789123456789123456789"), r[0]);
        assertEquals("cbrt(123456789123456789123456789^3) rem", new Apint(0), r[1]);

        r = ApintMath.root(new Apint(20735), 4);
        assertEquals("root(20735, 4) quot", new Apint(11), r[0]);
        assertEquals("root(20735, 4) rem", new Apint(6094), r[1]);

        r = ApintMath.root(new Apint(20736), 4);
        assertEquals("root(20736, 4) quot", new Apint(12), r[0]);
        assertEquals("root(20736, 4) rem", new Apint(0), r[1]);

        r = ApintMath.root(new Apint("232305723727482137666188006551300203692658625799727977970043302090695104949336681913044437155857798251440"), 4);
        assertEquals("root(123456789123456789123456789^4-1, 4) quot", new Apint("123456789123456789123456788"), r[0]);
        assertEquals("root(123456789123456789123456789^4-1, 4) rem", new Apint("7526705509736735950218367214942010432744153774003095938025053218012879436272304"), r[1]);

        r = ApintMath.root(new Apint("232305723727482137666188006551300203692658625799727977970043302090695104949336681913044437155857798251441"), 4);
        assertEquals("root(123456789123456789123456789^4, 4) quot", new Apint("123456789123456789123456789"), r[0]);
        assertEquals("root(123456789123456789123456789^4, 4) rem", new Apint(0), r[1]);

        r = ApintMath.root(new Apint("-28679718746395774517519299647974067853199588896463036970972834315105461935781603131036162289536454167206060221256216795681720482948"), 5);
        assertEquals("root(-123456789123456789123456789^5-1, 5) quot", new Apint("-123456789123456789123456788"), r[0]);
        assertEquals("root(-123456789123456789123456789^5-1, 5) rem", new Apint("-1161528618637410688330940013939737244121453253452721776287296525224698359210052776512932179917932266907780"), r[1]);

        r = ApintMath.root(new Apint("-28679718746395774517519299647974067853199588896463036970972834315105461935781603131036162289536454167206060221256216795681720482949"), 5);
        assertEquals("root(-123456789123456789123456789^5, 5) quot", new Apint("-123456789123456789123456789"), r[0]);
        assertEquals("root(-123456789123456789123456789^5, 5) rem", new Apint(0), r[1]);

        for (int i = 6; i < 40; i++)
        {
            Apint one = new Apint((i & 1) == 1 ? -1 : 1),
                  x = new Apint("123456789123456789123456789").multiply(one),
                  xm1 = x.subtract(one),
                  pow = ApintMath.pow(x, i);

            r = ApintMath.root(pow.subtract(one), i);
            assertEquals("root(-123456789123456789123456789^" + i + "-1, " + i + ") quot", xm1, r[0]);
            assertEquals("root(-123456789123456789123456789^" + i + "-1, " + i + ") rem", ApintMath.pow(x, i).subtract(ApintMath.pow(xm1, i)).subtract(one), r[1]);

            r = ApintMath.root(pow, i);
            assertEquals("root(-123456789123456789123456789^" + i + ", " + i + ") quot", x, r[0]);
            assertEquals("root(-123456789123456789123456789^" + i + ", " + i + ") rem", new Apint(0), r[1]);
        }

        r = ApintMath.root(ApintMath.pow(new Apint(23), 7912), 7912);
        assertEquals("root(23^7912, 7912) quot", new Apint(23), r[0]);
        assertEquals("root(23^7912, 7912) rem", new Apint(0), r[1]);

        assertEquals("0 quot", new Apint(0), ApintMath.root(Apint.ZERO, 3)[0]);
        assertEquals("0 rem", new Apint(0), ApintMath.root(Apint.ZERO, 3)[1]);
        assertEquals("1 quot", new Apint(1), ApintMath.root(Apint.ONE, 5)[0]);
        assertEquals("1 rem", new Apint(0), ApintMath.root(Apint.ONE, 5)[1]);
        assertEquals("zeroth root quot", new Apint(1), ApintMath.root(new Apint(3), 0)[0]);
        assertEquals("zeroth root rem", new Apint(2), ApintMath.root(new Apint(3), 0)[1]);
        assertEquals("1st root quot", new Apint(2), ApintMath.root(new Apint(2), 1)[0]);
        assertEquals("1st root rem", new Apint(0), ApintMath.root(new Apint(2), 1)[1]);
        assertEquals("-1st root quot", new Apint(0), ApintMath.root(new Apint(2), -1)[0]);
        assertEquals("-1st root rem", new Apint(2), ApintMath.root(new Apint(2), -1)[1]);

        assertEquals("1st root rem radix", 12, ApintMath.root(new Apint(2, 12), 1)[1].radix());
        assertEquals("-1st root quot radix", 12, ApintMath.root(new Apint(2, 12), -1)[0].radix());

        try
        {
            ApintMath.root(new Apint(-2), 2);
            fail("sqrt of -2 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result would be imaginary
            assertEquals("Localization key", "root.evenOfNegative", aae.getLocalizationKey());
        }

        try
        {
            ApintMath.root(new Apint(0), 0);
            fail("0th root of 0 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result would be undefined
            assertEquals("Localization key", "root.zerothOfZero", aae.getLocalizationKey());
        }
    }

    public static void testScale()
    {
        Apint x = ApintMath.scale(new Apint(1), 5);
        assertEquals("1 string", "100000", x.toString());

        x = ApintMath.scale(new Apint(-1), -1);
        assertEquals("-1 string", "0", x.toString());

        x = ApintMath.scale(new Apint(1), Long.MIN_VALUE);
        assertEquals("underflow", "0", x.toString());

        x = ApintMath.scale(new Apint(1), Long.MIN_VALUE / 2);
        assertEquals("underflow 2", "0", x.toString());
    }

    public static void testAbs()
    {
        Apint x = new Apint(2);
        assertEquals("2", new Apint(2), ApintMath.abs(x));

        x = new Apint(-2);
        assertEquals("-2", new Apint(2), ApintMath.abs(x));

        x = new Apint(0);
        assertEquals("0", new Apint(0), ApintMath.abs(x));
    }

    public static void testCopySign()
    {
        assertEquals("2, 1", new Apint(2), ApintMath.copySign(new Apint(2), new Apint(1)));
        assertEquals("2, -1", new Apint(-2), ApintMath.copySign(new Apint(2), new Apint(-1)));
        assertEquals("-2, 1", new Apint(2), ApintMath.copySign(new Apint(-2), new Apint(1)));
        assertEquals("-2, -1", new Apint(-2), ApintMath.copySign(new Apint(-2), new Apint(-1)));

        assertEquals("0, 0", new Apint(0), ApintMath.copySign(new Apint(0), new Apint(0)));
        assertEquals("0, 1", new Apint(0), ApintMath.copySign(new Apint(0), new Apint(1)));
        assertEquals("0, -1", new Apint(0), ApintMath.copySign(new Apint(0), new Apint(-1)));
        assertEquals("1, 0", new Apint(0), ApintMath.copySign(new Apint(1), new Apint(0)));
        assertEquals("-1, 0", new Apint(0), ApintMath.copySign(new Apint(-1), new Apint(0)));
    }

    @SuppressWarnings("deprecation")
    public static void testNegate()
    {
        Apint x = new Apint(2);
        assertEquals("2", new Apint(-2), ApintMath.negate(x));

        x = new Apint(-2);
        assertEquals("-2", new Apint(2), ApintMath.negate(x));

        x = new Apint(0);
        assertEquals("0", new Apint(0), ApintMath.negate(x));
    }

    public static void testDiv()
    {
        Apint[] r = ApintMath.div(new Apint(5), new Apint(3));
        assertEquals("5 / 3", new Apint(1), r[0]);
        assertEquals("5 % 3", new Apint(2), r[1]);

        r = ApintMath.div(new Apint(-5), new Apint(3));
        assertEquals("-5 / 3", new Apint(-1), r[0]);
        assertEquals("-5 % 3", new Apint(-2), r[1]);

        r = ApintMath.div(new Apint(5), new Apint(-3));
        assertEquals("5 / -3", new Apint(-1), r[0]);
        assertEquals("5 % -3", new Apint(2), r[1]);

        r = ApintMath.div(new Apint(-5), new Apint(-3));
        assertEquals("-5 / -3", new Apint(1), r[0]);
        assertEquals("-5 % -3", new Apint(-2), r[1]);

        r = ApintMath.div(new Apint(0), new Apint(3));
        assertEquals("0 / 3", new Apint(0), r[0]);
        assertEquals("0 % 3", new Apint(0), r[1]);

        r = ApintMath.div(new Apint("101010101010101010101010101010101010101010101010101010101010101010101010101010101"),
                          new Apint("101010101010101010101010101010101010101010101010101010101010101010101010101010101"));
        assertEquals("101...101 / 101...101", new Apint(1), r[0]);
        assertEquals("101...101 % 101...101", new Apint(0), r[1]);

        r = ApintMath.div(new Apint("-101010101010101010101010101010101010101010101010101010101010101010101010101010101"),
                          new Apint("101010101010101010101010101010101010101010101010101010101010101010101010101010101"));
        assertEquals("-101...101 / 101...101", new Apint(-1), r[0]);
        assertEquals("-101...101 % 101...101", new Apint(0), r[1]);

        r = ApintMath.div(new Apint("101010101010101010101010101010101010101010101010101010101010101010101010101010101"),
                          new Apint("-101010101010101010101010101010101010101010101010101010101010101010101010101010101"));
        assertEquals("101...101 / -101...101", new Apint(-1), r[0]);
        assertEquals("101...101 % -101...101", new Apint(0), r[1]);

        r = ApintMath.div(new Apint("-101010101010101010101010101010101010101010101010101010101010101010101010101010101"),
                          new Apint("-101010101010101010101010101010101010101010101010101010101010101010101010101010101"));
        assertEquals("-101...101 / -101...101", new Apint(1), r[0]);
        assertEquals("-101...101 % -101...101", new Apint(0), r[1]);

        r = ApintMath.div(new Apint("610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547"),
                          new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151"));
        assertEquals("very long / very long", new Apint("88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), r[0]);
        assertEquals("very long % very long", new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), r[1]);

        r = ApintMath.div(new Apint("610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547"),
                          new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151"));
        assertEquals("very long / -very long", new Apint("-88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), r[0]);
        assertEquals("very long % -very long", new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), r[1]);

        r = ApintMath.div(new Apint("-610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547"),
                          new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151"));
        assertEquals("-very long / very long", new Apint("-88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), r[0]);
        assertEquals("-very long % very long", new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), r[1]);

        r = ApintMath.div(new Apint("-610195307303654352666518121441737650909904045487886269771428999892641560353287146586044778246190438153093623532301738649952435264069156612677948549906937800453397951028502213195775639617773794408985442106591307693813584425141736150542412855661591984057451813085814343390198354303417947862296970200397097469208547"),
                          new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151"));
        assertEquals("-very long / -very long", new Apint("88887588172852973806344237990766854843278434484762916030592876582400800681711796943572298887271879881107698627193143861585371238964537515138970410491320347"), r[0]);
        assertEquals("-very long % -very long", new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), r[1]);

        r = ApintMath.div(new Apint(1, 12), new Apint(3, 12));
        assertEquals("1 / 3 radix", 12, r[0].radix());
        r = ApintMath.div(new Apint(3, 12), new Apint(1, 12));
        assertEquals("3 % 1 radix", 12, r[1].radix());

        try
        {
            ApintMath.div(new Apint(3), new Apint(0));
            fail("3 div 0 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result would be infinite
            assertEquals("Localization key", "divide.byZero", aae.getLocalizationKey());
        }
    }

    public static void testGcd()
    {
        assertEquals("2 gcd 3", new Apint(1), ApintMath.gcd(new Apint(2), new Apint(3)));
        assertEquals("2 gcd -4", new Apint(2), ApintMath.gcd(new Apint(2), new Apint(-4)));
        assertEquals("12 gcd 18", new Apint(6), ApintMath.gcd(new Apint(12), new Apint(18)));
        assertEquals("1 gcd 3", new Apint(1), ApintMath.gcd(new Apint(1), new Apint(3)));
        assertEquals("3 gcd 1", new Apint(1), ApintMath.gcd(new Apint(3), new Apint(1)));
        assertEquals("0 gcd 3", new Apint(3), ApintMath.gcd(new Apint(0), new Apint(3)));
        assertEquals("3 gcd 0", new Apint(3), ApintMath.gcd(new Apint(3), new Apint(0)));
        assertEquals("0 gcd -3", new Apint(3), ApintMath.gcd(new Apint(0), new Apint(-3)));
        assertEquals("-3 gcd 0", new Apint(3), ApintMath.gcd(new Apint(-3), new Apint(0)));
        assertEquals("0 gcd 0", new Apint(0), ApintMath.gcd(new Apint(0), new Apint(0)));
        Apint a = new Apfloat("1e100000").truncate().add(new Apint(1));
        assertEquals("1e100000+1 gcd 0", a, ApintMath.gcd(a, new Apint(0)));
        assertEquals("0 gcd 1e100000+1", a, ApintMath.gcd(new Apint(0), a));
    }

    public static void testLcm()
    {
        assertEquals("2 lcm 3", new Apint(6), ApintMath.lcm(new Apint(2), new Apint(3)));
        assertEquals("2 lcm -4", new Apint(4), ApintMath.lcm(new Apint(2), new Apint(-4)));
        assertEquals("12 lcm 18", new Apint(36), ApintMath.lcm(new Apint(12), new Apint(18)));
        assertEquals("1 lcm 3", new Apint(3), ApintMath.lcm(new Apint(1), new Apint(3)));
        assertEquals("3 lcm 1", new Apint(3), ApintMath.lcm(new Apint(3), new Apint(1)));
        assertEquals("0 lcm 3", new Apint(0), ApintMath.lcm(new Apint(0), new Apint(3)));
        assertEquals("3 lcm 0", new Apint(0), ApintMath.lcm(new Apint(3), new Apint(0)));
        assertEquals("0 lcm 0", new Apint(0), ApintMath.lcm(new Apint(0), new Apint(0)));
        assertEquals("0 lcm 0 radix", 12, ApintMath.lcm(new Apint(0, 12), new Apint(0, 12)).radix());
    }

    public static void testModMultiply()
    {
        assertEquals("2 * 4 % 5", new Apint(3), ApintMath.modMultiply(new Apint(2), new Apint(4), new Apint(5)));
        assertEquals("0 * 4 % 5", new Apint(0), ApintMath.modMultiply(new Apint(0), new Apint(4), new Apint(5)));

        assertEquals("long % long", new Apint(0), ApintMath.modMultiply(new Apint(41), new Apint("2463661000246366100024636610002463661"), new Apint("101010101010101010101010101010101010101")));
        assertEquals("long % -long", new Apint(0), ApintMath.modMultiply(new Apint(41), new Apint("2463661000246366100024636610002463661"), new Apint("-101010101010101010101010101010101010101")));
        assertEquals("-long % long", new Apint(0), ApintMath.modMultiply(new Apint(41), new Apint("-2463661000246366100024636610002463661"), new Apint("101010101010101010101010101010101010101")));
        assertEquals("-long % -long", new Apint(0), ApintMath.modMultiply(new Apint(41), new Apint("-2463661000246366100024636610002463661"), new Apint("-101010101010101010101010101010101010101")));

        assertEquals("very long % very long", new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), ApintMath.modMultiply(new Apint(3), new Apint("203398435767884784222172707147245883636634681829295423257142999964213853451095715528681592748730146051031207844100579549984145088023052204225982849968979266817799317009500737731925213205924598136328480702197102564604528141713912050180804285220530661352483937695271447796732784767805982620765656733465699156402849"), new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
        assertEquals("very long % -very long", new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), ApintMath.modMultiply(new Apint(3), new Apint("203398435767884784222172707147245883636634681829295423257142999964213853451095715528681592748730146051031207844100579549984145088023052204225982849968979266817799317009500737731925213205924598136328480702197102564604528141713912050180804285220530661352483937695271447796732784767805982620765656733465699156402849"), new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
        assertEquals("-very long % very long", new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), ApintMath.modMultiply(new Apint(3), new Apint("-203398435767884784222172707147245883636634681829295423257142999964213853451095715528681592748730146051031207844100579549984145088023052204225982849968979266817799317009500737731925213205924598136328480702197102564604528141713912050180804285220530661352483937695271447796732784767805982620765656733465699156402849"), new Apint("6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
        assertEquals("-very long % -very long", new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057150"), ApintMath.modMultiply(new Apint(3), new Apint("-203398435767884784222172707147245883636634681829295423257142999964213853451095715528681592748730146051031207844100579549984145088023052204225982849968979266817799317009500737731925213205924598136328480702197102564604528141713912050180804285220530661352483937695271447796732784767805982620765656733465699156402849"), new Apint("-6864797660130609714981900799081393217269435300143305409394463459185543183397656052122559640661454554977296311391480858037121987999716643812574028291115057151")));
    }

    public static void testModPow()
    {
        assertEquals("2 ^ 7 % 63", new Apint(2), ApintMath.modPow(new Apint(2), new Apint(7), new Apint(63)));
        assertEquals("-2 ^ 7 % 63", new Apint(-2), ApintMath.modPow(new Apint(-2), new Apint(7), new Apint(63)));
        assertEquals("2 ^ 7 % -63", new Apint(2), ApintMath.modPow(new Apint(2), new Apint(7), new Apint(-63)));
        assertEquals("-2 ^ 7 % -63", new Apint(-2), ApintMath.modPow(new Apint(-2), new Apint(7), new Apint(-63)));

        assertEquals("brute 60 ^ 20 % 63", new Apint(9), ApintMath.pow(new Apint(60), 20).mod(new Apint(63)));

        assertEquals("60 ^ 20 % 63", new Apint(9), ApintMath.modPow(new Apint(60), new Apint(20), new Apint(63)));
        assertEquals("-60 ^ 20 % 63", new Apint(9), ApintMath.modPow(new Apint(-60), new Apint(20), new Apint(63)));
        assertEquals("60 ^ 20 % -63", new Apint(9), ApintMath.modPow(new Apint(60), new Apint(20), new Apint(-63)));
        assertEquals("-60 ^ 20 % -63", new Apint(9), ApintMath.modPow(new Apint(-60), new Apint(20), new Apint(-63)));

        assertEquals("12345 ^ 23456 % 63", new Apint(9), ApintMath.modPow(new Apint(12345), new Apint(23456), new Apint(63)));
        assertEquals("-12345 ^ 23456 % 63", new Apint(9), ApintMath.modPow(new Apint(-12345), new Apint(23456), new Apint(63)));
        assertEquals("12345 ^ 23456 % -63", new Apint(9), ApintMath.modPow(new Apint(12345), new Apint(23456), new Apint(-63)));
        assertEquals("-12345 ^ 23456 % -63", new Apint(9), ApintMath.modPow(new Apint(-12345), new Apint(23456), new Apint(-63)));

        assertEquals("a ^ b % c", new Apint("7491153279929021784053819542230143577477242735007791983639160648989490758264323"),
                                  ApintMath.modPow(new Apint("6251289725125823481258278078012201234862578962346891205091256612909786215690123"),
                                                   new Apint("7124389714096813469801349081346689134098613461490781691409146149904781661401906"),
                                                   new Apint("11111111111111111111111111111111111111111111111111111111111111111111111111111203")));

        assertEquals("2 ^ 7 % 0", new Apint(0), ApintMath.modPow(new Apint(2), new Apint(7), new Apint(0)));
        assertEquals("2 ^ 0 % 63", new Apint(1), ApintMath.modPow(new Apint(2, 12), new Apint(0), new Apint(63)));
        assertEquals("2 ^ 0 % 63 radix", 12, ApintMath.modPow(new Apint(2, 12), new Apint(0), new Apint(63)).radix());
        assertEquals("0 ^ 7 % 2", new Apint(0), ApintMath.modPow(new Apint(0), new Apint(7), new Apint(2)));

        assertEquals("3 ^ -1 % 5", new Apint(2), ApintMath.modPow(new Apint(3), new Apint(-1), new Apint(5)));
        assertEquals("2 ^ -1 % 15", new Apint(8), ApintMath.modPow(new Apint(2), new Apint(-1), new Apint(15)));
        assertEquals("4 ^ -1 % 15", new Apint(4), ApintMath.modPow(new Apint(4), new Apint(-1), new Apint(15)));
        assertEquals("-4 ^ -1 % 15", new Apint(-11), ApintMath.modPow(new Apint(-4), new Apint(-1), new Apint(15)));
        assertEquals("4 ^ -1 % -15", new Apint(4), ApintMath.modPow(new Apint(4), new Apint(-1), new Apint(-15)));
        assertEquals("-4 ^ -1 % -15", new Apint(-11), ApintMath.modPow(new Apint(-4), new Apint(-1), new Apint(-15)));
        assertEquals("12345 ^ -345 % 64", new Apint(9), ApintMath.modPow(new Apint(12345), new Apint(-345), new Apint(64)));
        assertEquals("-12345 ^ -345 % 64", new Apint(-55), ApintMath.modPow(new Apint(-12345), new Apint(-345), new Apint(64)));
        assertEquals("12345 ^ -345 % -64", new Apint(9), ApintMath.modPow(new Apint(12345), new Apint(-345), new Apint(-64)));
        assertEquals("-12345 ^ -345 % -64", new Apint(-55), ApintMath.modPow(new Apint(-12345), new Apint(-345), new Apint(-64)));
        assertEquals("2 ^ -7 % 63", new Apint(32, 12), ApintMath.modPow(new Apint(2, 12), new Apint(-7, 12), new Apint(63, 12)));
        assertEquals("2 ^ -7 % 63 radix", 12, ApintMath.modPow(new Apint(2, 12), new Apint(-7, 12), new Apint(63, 12)).radix());

        assertEquals("1 ^ -1 % 2", new Apint(1), ApintMath.modPow(new Apint(1), new Apint(-1), new Apint(2)));
        assertEquals("1 ^ -1 % 1", new Apint(0), ApintMath.modPow(new Apint(1), new Apint(-1), new Apint(1)));

        try
        {
            ApintMath.modPow(new Apint(14), new Apint(-1), new Apint(65536));
            fail("Non-existent modular inverse allowed");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: modular inverse does not exist
            assertEquals("Localization key", "modInverse.notExists", aae.getLocalizationKey());
        }

        try
        {
            ApintMath.modPow(new Apint(0), new Apint(-1), new Apint(2));
            fail("Non-existent modular inverse allowed");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: modular inverse does not exist
            assertEquals("Localization key", "modInverse.notExists", aae.getLocalizationKey());
        }

        try
        {
            ApintMath.modPow(new Apint(0), new Apint(0), new Apint(7));
            fail("0^0 accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK; result would be undefined
            assertEquals("Localization key", "pow.zeroToZero", aae.getLocalizationKey());
        }
    }

    public static void testFactorial()
    {
        assertEquals("0!", new Apint(1), ApintMath.factorial(0));
        assertEquals("1!", new Apint(1), ApintMath.factorial(1));
        assertEquals("2!", new Apint(2), ApintMath.factorial(2));
        assertEquals("3!", new Apint(6), ApintMath.factorial(3));
        assertEquals("7!", new Apint(5040), ApintMath.factorial(7));
        assertEquals("7! radix", 10, ApintMath.factorial(7).radix());
        assertEquals("7! radix 7", new Apint(5040, 7), ApintMath.factorial(7, 7));
        assertEquals("7! radix 7 radix", 7, ApintMath.factorial(7, 7).radix());
        assertEquals("20!", new Apint("2432902008176640000"), ApintMath.factorial(20));
        assertEquals("29!", new Apint("8841761993739701954543616000000"), ApintMath.factorial(29));

        try
        {
            ApintMath.factorial(-1);
            fail("Factorial of negative number allowed");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: factorial of negative number
            assertEquals("Localization key", "factorial.ofNegative", aae.getLocalizationKey());
        }
    }

    public static void testDoubleFactorial()
    {
        assertEquals("0!!", new Apint(1), ApintMath.doubleFactorial(0));
        assertEquals("1!!", new Apint(1), ApintMath.doubleFactorial(1));
        assertEquals("2!!", new Apint(2), ApintMath.factorial(2));
        assertEquals("3!!", new Apint(3), ApintMath.doubleFactorial(3));
        assertEquals("7!!", new Apint(105), ApintMath.doubleFactorial(7));
        assertEquals("7!! radix", 10, ApintMath.doubleFactorial(7).radix());
        assertEquals("7!! radix 7", new Apint(105, 7), ApintMath.doubleFactorial(7, 7));
        assertEquals("7!! radix 7 radix", 7, ApintMath.doubleFactorial(7, 7).radix());
        assertEquals("20!!", new Apint("3715891200"), ApintMath.doubleFactorial(20));
        assertEquals("29!!", new Apint("6190283353629375"), ApintMath.doubleFactorial(29));

        try
        {
            ApintMath.doubleFactorial(-1);
            fail("Double factorial of negative number allowed");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: double factorial of negative number
            assertEquals("Localization key", "doubleFactorial.ofNegative", aae.getLocalizationKey());
        }
    }

    public static void testBinomial()
    {
        Apint a = ApintMath.binomial(10, 2);
        assertEquals("10,2 value", new Apint(45), a);

        a = ApintMath.binomial(10, 3);
        assertEquals("10,3 value", new Apint(120), a);

        a = ApintMath.binomial(10, 4);
        assertEquals("10,4 value", new Apint(210), a);

        a = ApintMath.binomial(10, 5);
        assertEquals("10,5 value", new Apint(252), a);

        a = ApintMath.binomial(10, 6);
        assertEquals("10,6 value", new Apint(210), a);

        a = ApintMath.binomial(10, 0);
        assertEquals("10,0 value", new Apint(1), a);

        a = ApintMath.binomial(10, 1);
        assertEquals("10,1 value", new Apint(10), a);

        a = ApintMath.binomial(10, 10);
        assertEquals("10,10 value", new Apint(1), a);

        a = ApintMath.binomial(11, 4);
        assertEquals("11,4 value", new Apint(330), a);

        a = ApintMath.binomial(11, 5);
        assertEquals("11,5 value", new Apint(462), a);

        a = ApintMath.binomial(11, 6);
        assertEquals("11,6 value", new Apint(462), a);

        a = ApintMath.binomial(2, 3);
        assertEquals("2,3 value", new Apint(0), a);

        a = ApintMath.binomial(0,0);
        assertEquals("0,0 value", new Apint(1), a);

        a = ApintMath.binomial(-4, -9);
        assertEquals("-4,-9 value", new Apint(-56), a);

        a = ApintMath.binomial(-4, 9);
        assertEquals("-4,9 value", new Apint(-220), a);

        a = ApintMath.binomial(4, -9);
        assertEquals("4,-9 value", new Apint(0), a);

        a = ApintMath.binomial(-9, -4);
        assertEquals("-9,-4 value", new Apint(0), a);

        a = ApintMath.binomial(-9, 4);
        assertEquals("-9,4 value", new Apint(495), a);

        a = ApintMath.binomial(9, -4);
        assertEquals("9,-4 value", new Apint(0), a);

        a = ApintMath.binomial(Long.MAX_VALUE, Long.MAX_VALUE - 1);
        assertEquals("MAX_VALUE,MAX_VALUE-1 value", new Apint(Long.MAX_VALUE), a);

        a = ApintMath.binomial(Long.MAX_VALUE, 1);
        assertEquals("MAX_VALUE,1 value", new Apint(Long.MAX_VALUE), a);

        a = ApintMath.binomial(Long.MIN_VALUE, 0);
        assertEquals("MIN_VALUE, 0 value", new Apint(1), a);

        a = ApintMath.binomial(-1, Long.MAX_VALUE, 9);
        assertEquals("-1 Long.MAX_VALUE value", new Apint(-1, 9), a);

        a = ApintMath.binomial(Long.MAX_VALUE, -1, 9);
        assertEquals("MAX_VALUE, -1 value", new Apint(0, 9), a);

        a = ApintMath.binomial(9, 5, 11);
        assertEquals("9,5 radix 11 value", new Apint(126, 11), a);

        a = ApintMath.binomial(new Apint(10), new Apint(2));
        assertEquals("10,2 apint value", new Apint(45), a);

        a = ApintMath.binomial(new Apint(10), new Apint(3));
        assertEquals("10,3 apint value", new Apint(120), a);

        a = ApintMath.binomial(new Apint(10), new Apint(4));
        assertEquals("10,4 apint value", new Apint(210), a);

        a = ApintMath.binomial(new Apint(10), new Apint(5));
        assertEquals("10,5 apint value", new Apint(252), a);

        a = ApintMath.binomial(new Apint(10), new Apint(6));
        assertEquals("10,6 apint value", new Apint(210), a);

        a = ApintMath.binomial(new Apint(10), new Apint(0));
        assertEquals("10,0 apint value", new Apint(1), a);

        a = ApintMath.binomial(new Apint(10), new Apint(1));
        assertEquals("10,1 apint value", new Apint(10), a);

        a = ApintMath.binomial(new Apint(10), new Apint(10));
        assertEquals("10,10 apint value", new Apint(1), a);

        a = ApintMath.binomial(new Apint(11), new Apint(4));
        assertEquals("11,4 apint value", new Apint(330), a);

        a = ApintMath.binomial(new Apint(11), new Apint(5));
        assertEquals("11,5 apint value", new Apint(462), a);

        a = ApintMath.binomial(new Apint(11), new Apint(6));
        assertEquals("11,6 apint value", new Apint(462), a);

        a = ApintMath.binomial(new Apint(2), new Apint(3));
        assertEquals("2,3 apint value", new Apint(0), a);

        a = ApintMath.binomial(new Apint(0), new Apint(0));
        assertEquals("0,0 apint value", new Apint(1), a);

        a = ApintMath.binomial(new Apint(-4), new Apint(-9));
        assertEquals("-4,-9 apint value", new Apint(-56), a);

        a = ApintMath.binomial(new Apint(-4), new Apint(9));
        assertEquals("-4,9 apint value", new Apint(-220), a);

        a = ApintMath.binomial(new Apint(4), new Apint(-9));
        assertEquals("4,-9 apint value", new Apint(0), a);

        a = ApintMath.binomial(new Apint(-9), new Apint(-4));
        assertEquals("-9,-4 apint value", new Apint(0), a);

        a = ApintMath.binomial(new Apint(-9), new Apint(4));
        assertEquals("-9,4 apint value", new Apint(495), a);

        a = ApintMath.binomial(new Apint(9), new Apint(-4));
        assertEquals("9,-4 apint value", new Apint(0), a);

        a = ApintMath.binomial(new Apint("1000000000000000000000000000000"), new Apint(3));
        assertEquals("1000000000000000000000000000000,3 value", new Apint("166666666666666666666666666666166666666666666666666666666667000000000000000000000000000000"), a);

        a = ApintMath.binomial(new Apint(9, 9), new Apint(4, 9));
        assertEquals("9,4 radix 9 apint value", new Apint(126, 9), a);

        try
        {
            ApintMath.binomial(new Apint("200000000000000000000"), new Apint("100000000000000000000"));
            fail("Overflow allowed");
        }
        catch (OverflowException oe)
        {
            // OK: overflow
            assertEquals("Localization key", "overflow", oe.getLocalizationKey());
        }
    }

    public static void testProduct()
    {
        Apint a = ApintMath.product(new Apint(2), new Apint(3));
        assertEquals("2,3 value", new Apint(6), a);

        a = ApintMath.product(new Apint(2), new Apint(3), new Apint(7));
        assertEquals("2,3,7 value", new Apint(42), a);

        a = ApintMath.product(Apint.ZERO, new Apint(12345));
        assertEquals("0 value", new Apint("0"), a);

        Apint[] x = new Apint[] { new Apint("1000000000000"), new Apint("1") };
        ApintMath.product(x);
        assertEquals("Array product 1 [0]", new Apint("1000000000000"), x[0]);
        assertEquals("Array product 1 [1]", new Apint("1"), x[1]);

        x = new Apint[] { new Apint("1"), new Apint("1000000000000") };
        ApintMath.product(x);
        assertEquals("Array product 2 [0]", new Apint("1"), x[0]);
        assertEquals("Array product 2 [1]", new Apint("1000000000000"), x[1]);

        assertEquals("Empty product", new Apint("1"), ApintMath.product());
    }

    public static void testSum()
    {
        Apint a = ApintMath.sum(new Apint("2"), new Apint("3"));
        assertEquals("2,3 value", new Apint("5"), a);

        a = ApintMath.sum(new Apint(2), new Apint(3), new Apint(4));
        assertEquals("2,3,4 value", new Apint(9), a);

        a = ApintMath.sum(new Apint(0), new Apint(12345));
        assertEquals("0-0 value", new Apint(12345), a);

        a = ApintMath.sum(new Apint(2));
        assertEquals("2 value", new Apint(2), a);

        Apint[] x = new Apint[] { new Apint("1000000000000"), new Apint("1") };
        ApintMath.sum(x);
        assertEquals("Array sum 1 [0]", new Apint("1000000000000"), x[0]);
        assertEquals("Array sum 1 [1]", new Apint("1"), x[1]);

        x = new Apint[] { new Apint("1"), new Apint("1000000000000") };
        ApintMath.sum(x);
        assertEquals("Array sum 2 [0]", new Apint("1"), x[0]);
        assertEquals("Array sum 2 [1]", new Apint("1000000000000"), x[1]);

        assertEquals("Empty sum", new Apint("0"), ApintMath.sum());
    }

    public static void testRandom()
    {
        long maxScale = 0;
        long minScale = Long.MAX_VALUE;
        for (int i = 0; i < 1000; i++)
        {
            Apint a = ApintMath.random(1000);
            maxScale = Math.max(maxScale, a.scale());
            minScale = Math.min(minScale, a.scale());
        }
        assertEquals("random max scale", 1000, maxScale);
        assertTrue("random min scale", minScale < 1000);

        for (int radix = Character.MIN_RADIX; radix <= Character.MAX_RADIX; radix++)
        {
            Map<Apint, Integer> counts = new HashMap<>();
            for (int i = 0; i < 20 * radix; i++)
            {
                Apint a = ApintMath.random(1, radix);
                counts.put(a, counts.getOrDefault(a, 0) + 1);
                assertEquals("value less than radix", -1, a.compareTo(new Apint(radix, radix)));
                assertTrue("value >= 0", a.signum() >= 0);
            }
            assertEquals("All values occurred", radix, counts.size());
        }

        try
        {
            ApintMath.random(0);
            fail("Zero allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: zero precision
        }
        try
        {
            ApintMath.random(Apfloat.INFINITE);
            fail("Infinite allowed");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK: Infinite size
            assertEquals("Localization key", "random.infinitePrecision", iee.getLocalizationKey());
        }
    }

    public static void testMax()
    {
        assertEquals("max of 1 and 1", new Apint(1), ApintMath.max(new Apint(1), new Apint(1)));
        assertEquals("max of 1 and 2", new Apint(2), ApintMath.max(new Apint(1), new Apint(2)));
        assertEquals("max of 2 and 1", new Apint(2), ApintMath.max(new Apint(2), new Apint(1)));
    }

    public static void testMin()
    {
        assertEquals("min of 1 and 1", new Apint(1), ApintMath.min(new Apint(1), new Apint(1)));
        assertEquals("min of 1 and 2", new Apint(1), ApintMath.min(new Apint(1), new Apint(2)));
        assertEquals("min of 2 and 1", new Apint(1), ApintMath.min(new Apint(2), new Apint(1)));
    }
}
