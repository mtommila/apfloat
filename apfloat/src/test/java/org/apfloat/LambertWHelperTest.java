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

import junit.framework.TestSuite;

/**
 * @since 1.8.0
 * @version 1.10.0
 * @author Mikko Tommila
 */

public class LambertWHelperTest
    extends ApfloatTestCase
{
    public LambertWHelperTest(String methodName)
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

        suite.addTest(new LambertWHelperTest("testReal"));
        suite.addTest(new LambertWHelperTest("testComplex0"));
        suite.addTest(new LambertWHelperTest("testComplexMinus1"));
        suite.addTest(new LambertWHelperTest("testComplex1"));
        suite.addTest(new LambertWHelperTest("testComplexMinus2"));
        suite.addTest(new LambertWHelperTest("testComplex2"));
        suite.addTest(new LambertWHelperTest("testComplexBigK"));

        return suite;
    }

    public static void testReal()
    {
        Apfloat a = new Apfloat(0);
        assertEquals("0", Apfloat.ZERO, LambertWHelper.w(a));

        a = new Apfloat("-0.1", 100);
        assertEquals("-0.1", new Apfloat("-0.1118325591589629648335694568202658422726453622912658633296897727621943319600088273854870109175450158"), LambertWHelper.w(a), new Apfloat("5e-100"));

        a = new Apfloat("0.1", 100);
        assertEquals("0.1", new Apfloat("0.09127652716086226429989572142317956865311922405147203264830839460717224625441755165020664592995606710"), LambertWHelper.w(a), new Apfloat("5e-101"));

        a = new Apfloat("0.9", 100);
        assertEquals("0.9", new Apfloat("0.5298329656334344121333664395454630485778813226980424276591307583895461718680181558074329260683928070"), LambertWHelper.w(a), new Apfloat("5e-100"));

        a = new Apfloat("1", 100);
        assertEquals("1", new Apfloat("0.5671432904097838729999686622103555497538157871865125081351310792230457930866845666932194469617522946"), LambertWHelper.w(a), new Apfloat("6e-100"));

        a = new Apfloat("2", 100);
        assertEquals("2", new Apfloat("0.8526055020137254913464724146953174668984533001514035087721073946525150656742630448965773783502494847"), LambertWHelper.w(a), new Apfloat("5e-100"));

        a = new Apfloat("2", 100, 16);
        assertEquals("2 radix 16", new Apfloat("0.da445aab89e28ccbe8ac8e1abd5cd1db4d2d1ee258d0f8a8b165f2b6e7e714a04cbc633a815cc724082c209dfbe85a495c83", 100, 16), LambertWHelper.w(a), new Apfloat(5, 1, 16).scale(-100));

        a = new Apfloat("1000000", 100);
        assertEquals("1000000", new Apfloat("11.38335808614005262200015678158500428903377470601886512143238610626898610768018867797709315493717650"), LambertWHelper.w(a), new Apfloat("5e-98"));

        a = new Apfloat("1e1000000", 100);
        assertEquals("1e1000000", new Apfloat("2.302570443457404704952666749321722464564231438920240812279165776095559120686218647242692419819942329e6"), LambertWHelper.w(a), new Apfloat("5e-93"));
        
        a = ApfloatMath.exp(new Apfloat(-1, 200)).negate().precision(100).add(new Apfloat("1e-50", 100));
        Apfloat w = LambertWHelper.w(a);
        assertEquals("-1/e + 1e-50 value", new Apfloat("-0.9999999999999999999999997668356018402875796636464119050387687556112724831557"), w, new Apfloat("1e-75"));
        assertEquals("-1/e + 1e-50 precision", 75, w.precision());

        a = ApfloatMath.exp(new Apfloat(-1, 200)).negate().precision(100);
        w = LambertWHelper.w(a);
        assertEquals("-1/e value", new Apfloat(-1), w, new Apfloat("1e-50"));
        assertEquals("-1/e precision", 50, w.precision());

        a = new Apfloat("-0.3");
        w = LambertWHelper.w(a);
        assertEquals("-0.3 low precision value", new Apfloat(-1), w, new Apfloat("0.5"));
        assertEquals("-0.3 low precision precision", 1, w.precision());

        String two = "0.8526055020137254913464724146953174668984533001514035087721073946525150656742630448965773783502494847334503972691804119834761668851953598826198984364998343940330324849743119327028383008883133161249045727544669202220292076639777316648311871183719040610274221013237163543451621208284315007250267190731048119566857455987975973474411544571619699938899354169616378479326962044241495398851839432070255805880";
        for (int precision = 100; precision <= 400; precision++)
        {
            a = new Apfloat(2, precision);
            assertEquals("2 precision " + precision, new Apfloat(two.substring(0, 2 + precision)), LambertWHelper.w(a), new Apfloat("5e-" + precision));
        }

        for (int radix = 2; radix <= 36; radix++)
        {
            String minusOnePerE = ApfloatMath.exp(new Apfloat(-1, 500, radix)).negate().toString(true);
            int precision = 400;
            a = new Apfloat(minusOnePerE.substring(0,  2 + precision), precision, radix);
            w = LambertWHelper.w(a);
            assertEquals("-1/e radix " + radix + " value", new Apfloat(-1, precision / 2, radix), w, new Apfloat(5 * radix, 1, radix).scale(-precision / 2));
            assertEquals("-1/e radix " + radix + " precision", precision / 2.0, w.precision(), 1.0);
        }

        String minusOnePerE = ApfloatMath.exp(new Apfloat(-1, 500)).negate().toString(true);
        for (int precision = 100; precision <= 400; precision++)
        {
            a = new Apfloat(minusOnePerE.substring(0,  2 + precision), precision);
            w = LambertWHelper.w(a);
            assertEquals("-1/e radix precision " + precision + " value", new Apfloat(-1, precision / 2), w, new Apfloat(5).scale(1 - precision / 2));
            assertEquals("-1/e radix precision " + precision + " precision", precision / 2.0, w.precision(), 1.0);
        }

        /*
        // Runs for half an hour or so...
        for (int radix = 2; radix <= 36; radix++)
        {
            String minusOnePerE = ApfloatMath.exp(new Apfloat(-1, 500, radix)).negate().toString(true);
            for (int precision = 100; precision <= 400; precision++)
            {
                a = new Apfloat(minusOnePerE.substring(0,  2 + precision), precision, radix);
                w = LambertWHelper.w(a);
                assertEquals("-1/e radix " + radix + " precision " + precision + " value", new Apfloat(-1, precision / 2, radix), w, new Apfloat(5 * radix, 1, radix).scale(-precision / 2));
                assertEquals("-1/e radix " + radix + " precision " + precision + " precision", precision / 2.0 + 0.5, w.precision(), 1.0);
            }
        }
        */

        try
        {
            LambertWHelper.w(new Apfloat("-0.4"));
            fail("Far beyond branch point accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }

        try
        {
            LambertWHelper.w(new Apfloat("-0.367879441171442321595523780"));
            fail("Beyond branch point accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be complex
        }

        try
        {
            LambertWHelper.w(new Apfloat(2));
            fail("Infinite precision accepted");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK; result would have infinite digits
        }
    }

    public static void testComplex0()
    {
        Apcomplex a = new Apcomplex("0");
        assertEquals("0", Apfloat.ZERO, LambertWHelper.w(a));

        a = new Apfloat("0.1", 100);
        assertEquals("0.1", new Apfloat("0.09127652716086226429989572142317956865311922405147203264830839460717224625441755165020664592995606710"), LambertWHelper.w(a), new Apfloat("5e-101"));

        a = new Apcomplex("(1e-100,1e-100)").precision(50);
        assertEquals("(1e-100,1e-100)", new Apcomplex("(1e-100,1e-100)").precision(50), LambertWHelper.w(a), new Apfloat("5e-149"));

        a = new Apcomplex("(1e-10,1e-10)").precision(50);
        assertEquals("(1e-10,1e-10)", new Apcomplex("(9.99999999999999999970000000010666666664583333333333e-11,9.9999999980000000002999999999999999999791666666753e-11)"), LambertWHelper.w(a), new Apfloat("5e-60"));

        a = new Apcomplex("(0.01,0.01)").precision(50);
        assertEquals("(0.01,0.01)", new Apcomplex("(0.0099971045851194534814524027709866897371957494651958,0.0098029980012010341194102411514982873087094006939282)"), LambertWHelper.w(a), new Apfloat("5e-52"));

        a = new Apcomplex("(0.1,0.1)").precision(50);
        assertEquals("(0.1,0.1)", new Apcomplex("(0.097870452916895276985068259728479600256253771097645,0.082860601390283166169392801012174651981013542958101)"), LambertWHelper.w(a), new Apfloat("5e-51"));

        a = new Apcomplex("(0.1,-0.1)").precision(50);
        assertEquals("(0.1,-0.1)", new Apcomplex("(0.097870452916895276985068259728479600256253771097645,-0.082860601390283166169392801012174651981013542958101)"), LambertWHelper.w(a), new Apfloat("5e-51"));

        a = new Apcomplex("(-0.1,0.1)").precision(50);
        assertEquals("(-0.1,0.1)", new Apcomplex("(-0.09575356787640000111807153287925558057943622268787,0.12268954209524810694854593242675798178478977541188)"), LambertWHelper.w(a), new Apfloat("5e-50"));

        a = new Apcomplex("(-0.1,-0.1)").precision(50);
        assertEquals("(-0.1,-0.1)", new Apcomplex("(-0.09575356787640000111807153287925558057943622268787,-0.12268954209524810694854593242675798178478977541188)"), LambertWHelper.w(a), new Apfloat("5e-50"));

        a = new Apcomplex("(0.9,0.9)").precision(50);
        assertEquals("(0.9,0.9)", new Apcomplex("(0.61367490011103129037350636966615982396555265609392,0.31332676840151583505948676785521310456288766083836)"), LambertWHelper.w(a), new Apfloat("5e-50"));

        a = new Apcomplex("(0.9,-0.9)").precision(50);
        assertEquals("(0.9,-0.9)", new Apcomplex("(0.61367490011103129037350636966615982396555265609392,-0.31332676840151583505948676785521310456288766083836)"), LambertWHelper.w(a), new Apfloat("5e-50"));

        a = new Apcomplex("(-0.9,0.9)").precision(50);
        assertEquals("(-0.9,0.9)", new Apcomplex("(0.21798309457331980992831606988147432124834284905112,1.00001975213679071996724784845911235217908252496852)"), LambertWHelper.w(a), new Apfloat("5e-50"));

        a = new Apcomplex("(-0.9,-0.9)").precision(50);
        assertEquals("(-0.9,-0.9)", new Apcomplex("(0.21798309457331980992831606988147432124834284905112,-1.00001975213679071996724784845911235217908252496852)"), LambertWHelper.w(a), new Apfloat("5e-50"));

        Apfloat minusOnePerE = new Apfloat("-0.36787944117144232159552377016146086744581113103177");
        a = new Apcomplex("(1e-1,1e-2)").precision(50).add(minusOnePerE).precision(50);
        assertEquals("-1/e + (1e-1,1e-2)", new Apcomplex("(-0.39856416144865505878487405032790278503831237022966,0.02477976526323124917823700098342674498925458678055)"), LambertWHelper.w(a), new Apfloat("5e-50"));

        a = new Apcomplex("(1e-2,1e-2)").precision(50).add(minusOnePerE).precision(50);
        Apcomplex w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-2,1e-2) value", new Apcomplex("(-0.76072254901895636327164466506061887160499199636868,0.09058702846060393422375172650148570490563684917247)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-2,1e-2) precision", 49, w.precision());

        a = new Apcomplex("(1e-3,1e-3)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-3,1e-3) value", new Apcomplex("(-0.92076351549145164412509774220456376946214404250169,0.03183363220504445084857857851911777030681297024975)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-3,1e-3) precision", 49, w.precision());

        a = new Apcomplex("(1e-4,1e-4)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-4,1e-4) value", new Apcomplex("(-0.97456257064644008106039222921174885372729080458095,0.01043281923771948088092118424656929963710261496991)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (1e-4,1e-4) precision", 48, w.precision());

        a = new Apcomplex("(1e-5,1e-5)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-5,1e-5) value", new Apcomplex("(-0.99191714865667190156669839954650235929288803603429,0.00333748945952032916845539337622356972009389475345)"), w, new Apfloat("5e-47"));
        assertEquals("-1/e + (1e-5,1e-5) precision", 47, w.precision());

        a = new Apcomplex("(1e-8,1e-8)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-8,1e-8) value", new Apcomplex("(-0.99974384410054809266419794964421548387024632303884,0.00010609263457633916917686652477996175142983720356)"), w, new Apfloat("5e-46"));
        assertEquals("-1/e + (1e-8,1e-8) precision", 46, w.precision());

        a = new Apcomplex("(1e-16,1e-16)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-16,1e-16) value", new Apcomplex("(-0.99999997438259817278287711182013622236756853118502,1.061107516343913271268043854017293402382483e-8)"), w, new Apfloat("5e-42"));
        assertEquals("-1/e + (1e-16,1e-16) precision", 42, w.precision());

        a = new Apcomplex("(1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-25,1e-25) value", new Apcomplex("(-0.99999999999918990661917188670455841174547849228700,3.3555166512757099178982471977490658151e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (1e-25,1e-25) precision", 37, w.precision());

        a = new Apcomplex("(1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (1e-25,-1e-25)", new Apcomplex("(-0.99999999999918990661917188670455841174547849228700,-3.3555166512757099178982471977490658151e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (1e-25,-1e-25) precision", 37, w.precision());

        a = new Apcomplex("(-1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (-1e-25,1e-25)", new Apcomplex("(-0.99999999999966444833487206657063304759783841878650,8.1009338082811329544158817569195577427e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (-1e-25,1e-25) precision", 37, w.precision());

        a = new Apcomplex("(-1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (-1e-25,-1e-25)", new Apcomplex("(-0.99999999999966444833487206657063304759783841878650,-8.1009338082811329544158817569195577427e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (-1e-25,-1e-25) precision", 37, w.precision());

        a = new Apcomplex("(0,1e-50)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a);
        assertEquals("-1/e + (0,1e-50)", new Apfloat("-1", 25), w, new Apfloat("5e-25"));
        assertEquals("-1/e + (0,1e-50) precision", 25, w.precision());

        a = new Apcomplex("(-0.3,0.01)");
        w = LambertWHelper.w(a);
        assertEquals("(-0.3,0.01) low precision value", new Apfloat(-1), w, new Apfloat("0.5"));
        assertEquals("(-0.3,0.01) low precision precision", 1, w.precision());

        Apcomplex one = new Apcomplex("(0.6569660692304364058739351896150295119261810504985686094708810348849230553332090089811209990930923361884822337635046832506679161073935215432141999633668086783842052290733792803001199461391811969507519731175665294093942297272688866492133887278935030771348369162776716469274104390721707403107629322610256867265039997015298781807300548738748048553243945570919106773762856665717128930867337457753104606262,0.3254503394134150299892819513886224030106515829224425965100201871391242964163981983384434529333246378839071010829232817221003148680000586621992702293807852649794166119757415274667292782447037751105434386197607160329394263166712361134026294024524617643743202183812262212671628767427209694220240411422491183527487987228384662597891505579819415701334004762447837029591767086368225252212869238674050125163)");
        for (int precision = 100; precision <= 400; precision++)
        {
            a = new Apcomplex(new Apfloat(1, precision), new Apfloat(1, precision));
            assertEquals("1 + I precision " + precision, one.precision(precision), LambertWHelper.w(a), new Apfloat(5).scale(-precision));
        }

        a = new Apcomplex("(2,1000000)").precision(50);
        assertEquals("(2,1000000)", new Apcomplex("(11.3760067875170608075520076107048712025774238213364,1.4444930636028915094393063196068810276369117644198)"), LambertWHelper.w(a), new Apfloat("5e-48"));

        a = new Apcomplex("(2,-1000000)").precision(50);
        assertEquals("(2,-1000000)", new Apcomplex("(11.3760067875170608075520076107048712025774238213364,-1.4444930636028915094393063196068810276369117644198)"), LambertWHelper.w(a), new Apfloat("5e-48"));

        a = new Apcomplex("(0,1000000)").precision(50);
        assertEquals("(0,1000000)", new Apcomplex("(11.3760067689068545924515956427133996741988651343661,1.4444949041717766545754105178986714424877495553053)"), LambertWHelper.w(a), new Apfloat("5e-48"));

        a = new Apcomplex("(0,-1000000)").precision(50);
        assertEquals("(0,-1000000)", new Apcomplex("(11.3760067689068545924515956427133996741988651343661,-1.4444949041717766545754105178986714424877495553053)"), LambertWHelper.w(a), new Apfloat("5e-48"));

        a = new Apcomplex("(-2,1000000)").precision(50);
        assertEquals("(-2,1000000)", new Apcomplex("(11.3760067503003063865838678439354530886497673603144,1.4444967447407042437063363618062470846843727797294)"), LambertWHelper.w(a), new Apfloat("5e-48"));

        a = new Apcomplex("(-2,-1000000)").precision(50);
        assertEquals("(-2,-1000000)", new Apcomplex("(11.3760067503003063865838678439354530886497673603144,-1.4444967447407042437063363618062470846843727797294)"), LambertWHelper.w(a), new Apfloat("5e-48"));

        a = new Apcomplex("(2,1e1000000)").precision(50);
        assertEquals("(2,1e1000000)", new Apcomplex("(2.3025704434574047047199737372509488036925000475234e6,1.5707956446026757185680399419191874646943170)"), LambertWHelper.w(a), new Apfloat("5e-43"));

        a = new Apcomplex("(2,-1e1000000)").precision(50);
        assertEquals("(2,-1e1000000)", new Apcomplex("(2.3025704434574047047199737372509488036925000475234e6,-1.5707956446026757185680399419191874646943170)"), LambertWHelper.w(a), new Apfloat("5e-43"));

        a = new Apcomplex("(0,1e1000000)").precision(50);
        assertEquals("(0,1e1000000)", new Apcomplex("(2.3025704434574047047199737372509488036925000475234e6,1.5707956446026757185680399419191874646943170)"), LambertWHelper.w(a), new Apfloat("5e-43"));

        a = new Apcomplex("(0,-1e1000000)").precision(50);
        assertEquals("(0,-1e1000000)", new Apcomplex("(2.3025704434574047047199737372509488036925000475234e6,-1.5707956446026757185680399419191874646943170)"), LambertWHelper.w(a), new Apfloat("5e-43"));

        a = new Apcomplex("(-2,1e1000000)").precision(50);
        assertEquals("(-2,1e1000000)", new Apcomplex("(2.3025704434574047047199737372509488036925000475234e6,1.5707956446026757185680399419191874646943170)"), LambertWHelper.w(a), new Apfloat("5e-43"));

        a = new Apcomplex("(-2,-1e1000000)").precision(50);
        assertEquals("(-2,-1e1000000)", new Apcomplex("(2.3025704434574047047199737372509488036925000475234e6,-1.5707956446026757185680399419191874646943170)"), LambertWHelper.w(a), new Apfloat("5e-43"));

        // Test for issue #10
        a = new Apcomplex(new Apfloat(1.0), new Apfloat(-6.123233995736766e-17));
        assertEquals("(1.0, -6.123233995736766e-17)", new Apcomplex("(0.5671432904097839, 0)"), LambertWHelper.w(a), new Apfloat("5e-16"));

        try
        {
            LambertWHelper.w(new Apcomplex(new Apfloat(2)));
            fail("Infinite precision accepted, real input");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK; result would have infinite digits
        }

        try
        {
            LambertWHelper.w(new Apcomplex(Apfloat.ZERO, new Apfloat(2)));
            fail("Infinite precision accepted, pure imaginary input");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK; result would have infinite digits
        }

        try
        {
            LambertWHelper.w(new Apcomplex(new Apfloat(2), new Apfloat(2)));
            fail("Infinite precision accepted, complex input");
        }
        catch (InfiniteExpansionException iee)
        {
            // OK; result would have infinite digits
        }
    }

    public static void testComplexMinus1()
    {
        Apcomplex a = new Apfloat("-0.9", 100);
        assertEquals("-0.9", new Apcomplex("(-0.391432526121527750133034526147075943687827652712403323727116341052850551878987815079192989219382464,-1.272337572189570832430671752662551965493618371061232468771183304041782647776233562678062247476551034)"), LambertWHelper.w(a, -1), new Apfloat("5e-99"));

        a = new Apfloat("-0.1", 100);
        assertEquals("-0.1", new Apcomplex("-3.577152063957297218409391963511994880401796257793075923683527755791687236350575462861463655620846808"), LambertWHelper.w(a, -1), new Apfloat("5e-99"));

        a = new Apfloat("0.1", 100);
        assertEquals("0.1", new Apcomplex("(-4.02809648961011506264076041156402069873813347744141842416512930094612491698102377715320004508143354,-3.91242454869037428736013966471897475301250989497551827987402128965086307260195851567480077547937895)"), LambertWHelper.w(a, -1), new Apfloat("5e-99"));

        a = new Apfloat("0.9", 100);
        assertEquals("0.9", new Apcomplex("(-1.642470593279425020495276790198233535547049490665863488077316751279119834648747227774223895906912197,-4.351470475183312597711213055387012655711778525544444292119730537944815740672766029381862336231076436)"), LambertWHelper.w(a, -1), new Apfloat("5e-99"));

        a = new Apfloat("1", 100);
        assertEquals("1", new Apcomplex("(-1.533913319793574507919741082072733779785298610650766671733076005689449081100439244990610565534637096,-4.375185153061898385470906564852584291623823114677011864961044491803721563089347281759881823990959514)"), LambertWHelper.w(a, -1), new Apfloat("5e-99"));

        a = new Apfloat("2", 100);
        assertEquals("2", new Apcomplex("(-0.834310366631110014694725298171332139697179984494606985270644699985232754988730730357308978327940795,-4.530265998555008292131366279840451269080850198446391800466755572147815249787522323401907849111228103)"), LambertWHelper.w(a, -1), new Apfloat("5e-99"));

        a = new Apfloat("1000000", 100);
        assertEquals("1000000", new Apcomplex("(11.2752468562544339947180593529396553368595844926367045416137821549829067032062579694569561623234427342,-5.8075536510615827049173421518236546415873061942318932562300202046160560302747533665111940363888637664)"), LambertWHelper.w(a, -1), new Apfloat("5e-98"));

        a = new Apfloat("1e1000000", 100);
        assertEquals("1e1000000", new Apcomplex("(2.3025704434574047012295785562023389265357833825088593826397356104077744144457e6,-6.2831825784107028806218101388997157457028155078601957175368061400651450)"), LambertWHelper.w(a, -1), new Apfloat("5e-93"));
        
        a = new Apcomplex("(0.1,0.1)").precision(50);
        assertEquals("(0.1,0.1)", new Apcomplex("(-3.494370237224259073810794317924915029229909917936,-3.078388134528402866198196276013121902626535540677)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(0.1,-0.1)").precision(50);
        assertEquals("(0.1,-0.1)", new Apcomplex("(-3.76940931372708814708271270800229359221355274363303,-4.83567134279640724016070688927989061085551400221897)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.1,0.1)").precision(50);
        assertEquals("(-0.1,0.1)", new Apcomplex("(-3.1689801292043678933642898831595709207335040405717,-1.1271168412956324398669025214666584531292112870714)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.1,-0.1)").precision(50);
        assertEquals("(-0.1,-0.1)", new Apcomplex("(-3.98985219695476256961953773435712367855538271559129,-6.51938865759154711525687235769823729506874694669240)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(0.9,0.9)").precision(50);
        assertEquals("(0.9,0.9)", new Apcomplex("(-1.0926454500596423319036442434651916858606625525617,-3.6349920188858778056033971806870738270264155979072)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(0.9,-0.9)").precision(50);
        assertEquals("(0.9,-0.9)", new Apcomplex("(-1.44972299161521104839286677766248538197930381108885,-5.22724706111442259544746566443002584026268188667821)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.9,0.9)").precision(50);
        assertEquals("(-0.9,0.9)", new Apcomplex("(-0.5355045584526520713025783875171692559639125322935,-2.1073485797364305882343695573825528332701956975255)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.9,-0.9)").precision(50);
        assertEquals("(-0.9,-0.9)", new Apcomplex("(-1.70954106162722618992015552843949798975878623087174,-6.82308486245240807403229525936443072239343329432382)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        Apfloat minusOnePerE = new Apfloat("-0.36787944117144232159552377016146086744581113103177");
        a = new Apcomplex("(1e-1,1e-2)").precision(50).add(minusOnePerE).precision(50);
        assertEquals("-1/e + (1e-1,1e-2)", new Apcomplex("(-2.0205678829547190357896012430738825194504249931872,-0.0738416343062390264115221514178280173059856118129)"), LambertWHelper.w(a, -1), new Apfloat("5e-49"));

        a = new Apcomplex("(1e-2,1e-2)").precision(50).add(minusOnePerE).precision(50);
        Apcomplex w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-2,1e-2) value", new Apcomplex("(-1.2755038208041206244353602468815721885958795761298,-0.1277888928494640657435110228230078875932085308564)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-2,1e-2) precision", 49, w.precision());

        a = new Apcomplex("(1e-2,-1e-2)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-2,-1e-2) value", new Apcomplex("(-3.11349050891483652161146165469929706550158668484065,-7.42919350583428899857090083748667060072764392328836)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-2,-1e-2) precision", 50, w.precision());

        a = new Apcomplex("(1e-3,1e-3)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-3,1e-3) value", new Apcomplex("(-1.08286084351113162002293325126217133344405890773,-0.03546743888232764678696099640150275124724728981)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-3,1e-3) precision", 49, w.precision());

        a = new Apcomplex("(1e-4,1e-4)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-4,1e-4) value", new Apcomplex("(-1.0257998669139797269627934713927798727163466425,-0.0107953509735967697958752446518968266762032938)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (1e-4,1e-4) precision", 48, w.precision());

        a = new Apcomplex("(1e-5,1e-5)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-5,1e-5) value", new Apcomplex("(-1.008119095101024183688608313618380516549321428681,-0.003373734158670298533292555437275306346525720479)"), w, new Apfloat("5e-47"));
        assertEquals("-1/e + (1e-5,1e-5) precision", 47, w.precision());

        a = new Apcomplex("(1e-8,1e-8)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-8,1e-8) value", new Apcomplex("(-1.0002561921432096201230551538165443521981490847,-0.0001061288783349933769441253168206744317626089)"), w, new Apfloat("5e-46"));
        assertEquals("-1/e + (1e-8,1e-8) precision", 46, w.precision());

        a = new Apcomplex("(1e-16,1e-16)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-16,1e-16) value", new Apcomplex("(-1.000000025617402189654700016052561825670761,-1.0611075525876709840553230730259339e-8)"), w, new Apfloat("5e-42"));
        assertEquals("-1/e + (1e-16,1e-16) precision", 42, w.precision());

        a = new Apcomplex("(1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-25,1e-25) value", new Apcomplex("(-1.0000000000008100933808284757330187161,-3.355516651279334293669526e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (1e-25,1e-25) precision", 37, w.precision());

        a = new Apcomplex("(1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (1e-25,-1e-25)", new Apcomplex("(-3.08884301561304385595708696427755538303311335575167,-7.46148928565425455690611629711746943754305678567092)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-25,-1e-25) precision", 50, w.precision());

        a = new Apcomplex("(-1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (-1e-25,1e-25)", new Apcomplex("(-1.0000000000003355516651275709917898245,-8.100933808284757330187160e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (-1e-25,1e-25) precision", 37, w.precision());

        a = new Apcomplex("(-1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -1);
        assertEquals("-1/e + (-1e-25,-1e-25)", new Apcomplex("(-3.08884301561304385595708640170600160357965553350553,-7.46148928565425455690611636468380745202167537848733)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (-1e-25,-1e-25) precision", 50, w.precision());

        a = new Apcomplex("-0.3678794411714423215955237701615").precision(400);
        w = LambertWHelper.w(a, -1);
        assertEquals("-0.3678794411714423215955237701615 value", new Apcomplex("(-0.9999999999999999999999999999999290844593648057274890111324409909772743495610372098324992841989871764928737740609004793534682540053443051399418812510079552126308029943163910621009255881821995159178880095852554333370463395603248151517019196520469670849976233621765675059713043410110477719736952492808165979901145821944546091377396631038507940797034413787153721563900362732037292590866126803411791060595,-4.61244644311002073593990950383005301185756786088441915947765419354022153345185286269640964675349907862653578915254654696678064789269795860983141052266643067979800363293013989928411810795941375083587751904376634023242645843420325518066327564804765851332735851986261489467922733871343154656608287717411712113540450424299636698379699050316619168405756676557611416361034660e-16)"), w, new Apfloat("5e-383"));
        assertEquals("-0.3678794411714423215955237701615 precision", 384, w.precision());

        a = new Apcomplex("-0.36787944117144232159552377016146").precision(185);
        w = LambertWHelper.w(a, -1);
        assertEquals("-0.36787944117144232159552377016146 value", new Apcomplex("(-1.000000000000000068672588207674257657443699681618940429313824645972320261044650790536548311713641687234758741030702101798690781938624824161254982003710688119992988154843)"), w, new Apfloat("5e-167"));
        assertEquals("-0.36787944117144232159552377016146 precision", 168, w.precision());

        try
        {
            LambertWHelper.w(new Apfloat(0), -1);
            fail("Zero accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }
    }

    public static void testComplex1()
    {
        Apcomplex a = new Apfloat("-0.9", 100);
        assertEquals("-0.9", new Apcomplex("(-2.1696402027752207874871174308091539919889333294692997574577771392574830573295888027357084819789128019,7.5750296170109843098532982876582625035605375877146409519391718892752281434037966807811301735860372005)"), LambertWHelper.w(a, 1), new Apfloat("5e-99"));

        a = new Apfloat("-0.1", 100);
        assertEquals("-0.1", new Apcomplex("(-4.4490981787008898640867157232460451274645883248560292243219377121537983732391815602785462059256437129,7.3070607892176086310144168455358639797400912986829032801724659855526522573777510811321213152295822684)"), LambertWHelper.w(a, 1), new Apfloat("5e-99"));

        a = new Apfloat("0.1", 100);
        assertEquals("0.1", new Apcomplex("(-4.02809648961011506264076041156402069873813347744141842416512930094612491698102377715320004508143354,3.91242454869037428736013966471897475301250989497551827987402128965086307260195851567480077547937895)"), LambertWHelper.w(a, 1), new Apfloat("5e-99"));

        a = new Apfloat("0.9", 100);
        assertEquals("0.9", new Apcomplex("(-1.642470593279425020495276790198233535547049490665863488077316751279119834648747227774223895906912197,4.351470475183312597711213055387012655711778525544444292119730537944815740672766029381862336231076436)"), LambertWHelper.w(a, 1), new Apfloat("5e-99"));

        a = new Apfloat("1", 100);
        assertEquals("1", new Apcomplex("(-1.533913319793574507919741082072733779785298610650766671733076005689449081100439244990610565534637096,4.375185153061898385470906564852584291623823114677011864961044491803721563089347281759881823990959514)"), LambertWHelper.w(a, 1), new Apfloat("5e-99"));

        a = new Apfloat("2", 100);
        assertEquals("2", new Apcomplex("(-0.834310366631110014694725298171332139697179984494606985270644699985232754988730730357308978327940795,4.530265998555008292131366279840451269080850198446391800466755572147815249787522323401907849111228103)"), LambertWHelper.w(a, 1), new Apfloat("5e-99"));

        a = new Apfloat("1000000", 100);
        assertEquals("1000000", new Apcomplex("(11.2752468562544339947180593529396553368595844926367045416137821549829067032062579694569561623234427342,5.8075536510615827049173421518236546415873061942318932562300202046160560302747533665111940363888637664)"), LambertWHelper.w(a, 1), new Apfloat("5e-98"));

        a = new Apfloat("1e1000000", 100);
        assertEquals("1e1000000", new Apcomplex("(2.3025704434574047012295785562023389265357833825088593826397356104077744144457e6,6.2831825784107028806218101388997157457028155078601957175368061400651450)"), LambertWHelper.w(a, 1), new Apfloat("5e-93"));

        a = new Apcomplex("(0.1,0.1)").precision(50);
        assertEquals("(0.1,0.1)", new Apcomplex("(-3.76940931372708814708271270800229359221355274363303,4.83567134279640724016070688927989061085551400221897)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));
        
        a = new Apcomplex("(0.1,-0.1)").precision(50);
        assertEquals("(0.1,-0.1)", new Apcomplex("(-3.494370237224259073810794317924915029229909917936,3.078388134528402866198196276013121902626535540677)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.1,0.1)").precision(50);
        assertEquals("(-0.1,0.1)", new Apcomplex("(-3.98985219695476256961953773435712367855538271559129,6.51938865759154711525687235769823729506874694669240)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.1,-0.1)").precision(50);
        assertEquals("(-0.1,-0.1)", new Apcomplex("(-3.1689801292043678933642898831595709207335040405717,1.1271168412956324398669025214666584531292112870714)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        a = new Apcomplex("(0.9,0.9)").precision(50);
        assertEquals("(0.9,0.9)", new Apcomplex("(-1.44972299161521104839286677766248538197930381108885,5.22724706111442259544746566443002584026268188667821)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        a = new Apcomplex("(0.9,-0.9)").precision(50);
        assertEquals("(0.9,-0.9)", new Apcomplex("(-1.0926454500596423319036442434651916858606625525617,3.6349920188858778056033971806870738270264155979072)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.9,0.9)").precision(50);
        assertEquals("(-0.9,0.9)", new Apcomplex("(-1.70954106162722618992015552843949798975878623087174,6.82308486245240807403229525936443072239343329432382)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.9,-0.9)").precision(50);
        assertEquals("(-0.9,-0.9)", new Apcomplex("(-0.5355045584526520713025783875171692559639125322935,2.1073485797364305882343695573825528332701956975255)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        Apfloat minusOnePerE = new Apfloat("-0.36787944117144232159552377016146086744581113103177");
        a = new Apcomplex("(1e-1,1e-2)").precision(50).add(minusOnePerE).precision(50);
        assertEquals("-1/e + (1e-1,1e-2)", new Apcomplex("(-3.41260053537264765727988995284861910196950965532302,7.38373371787849281512592031397374471437222278075429)"), LambertWHelper.w(a, 1), new Apfloat("5e-49"));

        a = new Apcomplex("(1e-2,1e-2)").precision(50).add(minusOnePerE).precision(50);
        Apcomplex w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-2,1e-2) value", new Apcomplex("(-3.11349050891483652161146165469929706550158668484065,7.42919350583428899857090083748667060072764392328836)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-2,1e-2) precision", 50, w.precision());

        a = new Apcomplex("(1e-2,-1e-2)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-2,-1e-2) value", new Apcomplex("(-1.2755038208041206244353602468815721885958795761298,0.1277888928494640657435110228230078875932085308564)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-2,-1e-2) precision", 49, w.precision());

        a = new Apcomplex("(1e-3,-1e-3)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-3,-1e-3) value", new Apcomplex("(-1.08286084351113162002293325126217133344405890773,0.03546743888232764678696099640150275124724728981)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-3,-1e-3) precision", 49, w.precision());

        a = new Apcomplex("(1e-4,-1e-4)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-4,-1e-4) value", new Apcomplex("(-1.0257998669139797269627934713927798727163466425,0.0107953509735967697958752446518968266762032938)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (1e-4,-1e-4) precision", 48, w.precision());

        a = new Apcomplex("(1e-5,-1e-5)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-5,-1e-5) value", new Apcomplex("(-1.008119095101024183688608313618380516549321428681,0.003373734158670298533292555437275306346525720479)"), w, new Apfloat("5e-47"));
        assertEquals("-1/e + (1e-5,-1e-5) precision", 47, w.precision());

        a = new Apcomplex("(1e-8,-1e-8)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-8,-1e-8) value", new Apcomplex("(-1.0002561921432096201230551538165443521981490847,0.0001061288783349933769441253168206744317626089)"), w, new Apfloat("5e-46"));
        assertEquals("-1/e + (1e-8,-1e-8) precision", 46, w.precision());

        a = new Apcomplex("(1e-16,-1e-16)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-16,-1e-16) value", new Apcomplex("(-1.000000025617402189654700016052561825670761,1.0611075525876709840553230730259339e-8)"), w, new Apfloat("5e-42"));
        assertEquals("-1/e + (1e-16,-1e-16) precision", 42, w.precision());

        a = new Apcomplex("(1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-25,1e-25)", new Apcomplex("(-3.08884301561304385595708696427755538303311335575167,7.46148928565425455690611629711746943754305678567092)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-25,1e-25) precision", 50, w.precision());

        a = new Apcomplex("(1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (1e-25,-1e-25) value", new Apcomplex("(-1.0000000000008100933808284757330187161,3.355516651279334293669526e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (1e-25,-1e-25) precision", 37, w.precision());

        a = new Apcomplex("(-1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (-1e-25,1e-25)", new Apcomplex("(-3.08884301561304385595708640170600160357965553350553,7.46148928565425455690611636468380745202167537848733)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (-1e-25,1e-25) precision", 50, w.precision());

        a = new Apcomplex("(-1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 1);
        assertEquals("-1/e + (-1e-25,-1e-25)", new Apcomplex("(-1.0000000000003355516651275709917898245,8.100933808284757330187160e-13)"), w, new Apfloat("5e-37"));
        assertEquals("-1/e + (-1e-25,-1e-25) precision", 37, w.precision());

        try
        {
            LambertWHelper.w(new Apfloat(0), 1);
            fail("Zero accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }
    }

    public static void testComplexMinus2()
    {
        Apcomplex a = new Apfloat("-0.9", 100);
        assertEquals("-0.9", new Apcomplex("(-2.1696402027752207874871174308091539919889333294692997574577771392574830573295888027357084819789128019,-7.5750296170109843098532982876582625035605375877146409519391718892752281434037966807811301735860372005)"), LambertWHelper.w(a, -2), new Apfloat("5e-99"));

        a = new Apfloat("-0.1", 100);
        assertEquals("-0.1", new Apcomplex("(-4.4490981787008898640867157232460451274645883248560292243219377121537983732391815602785462059256437129,-7.3070607892176086310144168455358639797400912986829032801724659855526522573777510811321213152295822684)"), LambertWHelper.w(a, -2), new Apfloat("5e-99"));

        a = new Apfloat("0.1", 100);
        assertEquals("0.1", new Apcomplex("(-4.752924560539896501162753801930674336761884015575296912634002580497382636717867505212605,-10.573111784534943705555301345953051927597028025159592622540268741980634029224381240134255)"), LambertWHelper.w(a, -2), new Apfloat("5e-99"));

        a = new Apfloat("0.9", 100);
        assertEquals("0.9", new Apcomplex("(-2.5082431083766972979390938498275007121312900409911210568146700207260115202611491149295270,-10.7666934268043841675736179633389172700542145561797254359291099360428020322733883326938578)"), LambertWHelper.w(a, -2), new Apfloat("5e-99"));

        a = new Apfloat("1", 100);
        assertEquals("1", new Apcomplex("(-2.40158510486800288417413977468407647920574720363862201506340595568500161119813263131511672,-10.77629951611507089849710334639114764356272881108572638210088260662431487130270095507071949)"), LambertWHelper.w(a, -2), new Apfloat("5e-99"));

        a = new Apfloat("2", 100);
        assertEquals("2", new Apcomplex("(-1.70225900555760418421169531849422743741100852687484668594853268958582652624730469228795175,-10.83980867635929817161430296528775785075955528184433941773103962590404738511147335839990100)"), LambertWHelper.w(a, -2), new Apfloat("5e-99"));

        a = new Apfloat("1000000", 100);
        assertEquals("1000000", new Apcomplex("(11.0354852870884541556114342850176398521815497733759312875404969277650410480510310486335176253571236420,-11.7496396980728366336315836335846570571748464337355079073702587880867608468150554007964025611923176174)"), LambertWHelper.w(a, -2), new Apfloat("5e-98"));

        a = new Apfloat("1e1000000", 100);
        assertEquals("1e1000000", new Apcomplex("(2.3025704434574046900603139770105247722136418061583123550509005233524648309767e6,-12.5663651568214058018813826529115311494596561375242805601367375513007391)"), LambertWHelper.w(a, -2), new Apfloat("5e-93"));

        a = new Apcomplex("(0.1,0.1)").precision(50);
        assertEquals("(0.1,0.1)", new Apcomplex("(-4.32694598298195301567101913174024904383609408294303,-9.79417240669519814321681889366099503452634756326525)"), LambertWHelper.w(a, -2), new Apfloat("5e-49"));
        
        a = new Apcomplex("(0.1,-0.1)").precision(50);
        assertEquals("(0.1,-0.1)", new Apcomplex("(-4.46150099885356534168179199389468758681480568726818,-11.40818006630707645202745568528364138775149089044037)"), LambertWHelper.w(a, -2), new Apfloat("5e-48"));

        a = new Apcomplex("(-0.1,0.1)").precision(50);
        assertEquals("(-0.1,0.1)", new Apcomplex("(-4.17206000198761389916980339367569553340272515041870,-8.16709991236722495654333433874576651246049617173758)"), LambertWHelper.w(a, -2), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.1,-0.1)").precision(50);
        assertEquals("(-0.1,-0.1)", new Apcomplex("(-4.58038275692009635302775100106541142184228107393803,-13.01333459901300720639047799260012653428906553185216)"), LambertWHelper.w(a, -2), new Apfloat("5e-48"));

        a = new Apcomplex("(0.9,0.9)").precision(50);
        assertEquals("(0.9,0.9)", new Apcomplex("(-2.08308039125534457091462580249672703675832197614088,-10.00490281337842995053643413552988782638091117564826)"), LambertWHelper.w(a, -2), new Apfloat("5e-48"));

        a = new Apcomplex("(0.9,-0.9)").precision(50);
        assertEquals("(0.9,-0.9)", new Apcomplex("(-2.22715505448192395274331336493646484435206146304835,-11.59114315011385962030468122170837190368462856991551)"), LambertWHelper.w(a, -2), new Apfloat("5e-48"));

        a = new Apcomplex("(-0.9,0.9)").precision(50);
        assertEquals("(-0.9,0.9)", new Apcomplex("(-1.91410930491369070543340051140645802624032344774990,-8.41574038133572741523579549611439943379759998796892)"), LambertWHelper.w(a, -2), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.9,-0.9)").precision(50);
        assertEquals("(-0.9,-0.9)", new Apcomplex("(-2.35280842816729699806331011414977130420894944780259,-13.17505104043316522138211734518438096809787404617582)"), LambertWHelper.w(a, -2), new Apfloat("5e-48"));

        Apfloat minusOnePerE = new Apfloat("-0.36787944117144232159552377016146086744581113103177");
        a = new Apcomplex("(1e-2,1e-2)").precision(50).add(minusOnePerE).precision(50);
        Apcomplex w = LambertWHelper.w(a, -2);
        assertEquals("-1/e + (1e-2,1e-2) value", new Apcomplex("(-3.12042317846178133420655257832849242941900358279720,-7.48703153555803272188014570620298820375269188816625)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-2,1e-2) precision", 51, w.precision());

        a = new Apcomplex("(1e-2,-1e-2)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -2);
        assertEquals("-1/e + (1e-2,-1e-2) value", new Apcomplex("(-3.68965926447077142781441532734581320117222723454881,-13.84885659062717730687065656572845992814464654071224)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (1e-2,-1e-2) precision", 51, w.precision());

        a = new Apcomplex("(1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -2);
        assertEquals("-1/e + (1e-25,1e-25)", new Apcomplex("(-3.08884301561304385595708703184389339751173194856795,-7.46148928565425455690611685968902321699651460791724)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-25,1e-25) precision", 51, w.precision());

        a = new Apcomplex("(1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -2);
        assertEquals("-1/e + (1e-25,-1e-25) value", new Apcomplex("(-3.66406814242907101707075129536537687200837112614960,-13.87905600274680942535355712749209564794866828664858)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (1e-25,-1e-25) precision", 51, w.precision());

        a = new Apcomplex("(-1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -2);
        assertEquals("-1/e + (-1e-25,1e-25)", new Apcomplex("(-3.08884301561304385595708646927233961805827412632177,-7.46148928565425455690611692725536123147513320073334)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (-1e-25,1e-25) precision", 51, w.precision());

        a = new Apcomplex("(-1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, -2);
        assertEquals("-1/e + (-1e-25,-1e-25)", new Apcomplex("(-3.66406814242907101707075074445736863315369647333033,-13.87905600274680942535355716527114169388794146277880)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (-1e-25,-1e-25) precision", 51, w.precision());

        try
        {
            LambertWHelper.w(new Apfloat(0), -2);
            fail("Zero accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }
    }

    public static void testComplex2()
    {
        Apcomplex a = new Apfloat("-0.9", 100);
        assertEquals("-0.9", new Apcomplex("(-2.75946327141826742489413365006537853029870165199682582964615371041076964501146169979262105021743979,13.94176464069150052152167624996073846234452900845388730939992976630026789069486464296816115244547952)"), LambertWHelper.w(a, 2), new Apfloat("5e-99"));

        a = new Apfloat("-0.1", 100);
        assertEquals("-0.1", new Apcomplex("(-4.9880136260605828005616558438145653171651930088796221871134876787550777308243035135248133243187902,13.7900985636568655239755915840066119002189639614688424337178890778888735747762802755938053596950001)"), LambertWHelper.w(a, 2), new Apfloat("5e-99"));

        a = new Apfloat("0.1", 100);
        assertEquals("0.1", new Apcomplex("(-4.752924560539896501162753801930674336761884015575296912634002580497382636717867505212605,10.573111784534943705555301345953051927597028025159592622540268741980634029224381240134255)"), LambertWHelper.w(a, 2), new Apfloat("5e-99"));

        a = new Apfloat("0.9", 100);
        assertEquals("0.9", new Apcomplex("(-2.5082431083766972979390938498275007121312900409911210568146700207260115202611491149295270,10.7666934268043841675736179633389172700542145561797254359291099360428020322733883326938578)"), LambertWHelper.w(a, 2), new Apfloat("5e-99"));

        a = new Apfloat("1", 100);
        assertEquals("1", new Apcomplex("(-2.40158510486800288417413977468407647920574720363862201506340595568500161119813263131511672,10.77629951611507089849710334639114764356272881108572638210088260662431487130270095507071949)"), LambertWHelper.w(a, 2), new Apfloat("5e-99"));

        a = new Apfloat("2", 100);
        assertEquals("2", new Apcomplex("(-1.70225900555760418421169531849422743741100852687484668594853268958582652624730469228795175,10.83980867635929817161430296528775785075955528184433941773103962590404738511147335839990100)"), LambertWHelper.w(a, 2), new Apfloat("5e-99"));

        a = new Apfloat("1000000", 100);
        assertEquals("1000000", new Apcomplex("(11.0354852870884541556114342850176398521815497733759312875404969277650410480510310486335176253571236420,11.7496396980728366336315836335846570571748464337355079073702587880867608468150554007964025611923176174)"), LambertWHelper.w(a, 2), new Apfloat("5e-98"));

        a = new Apfloat("1e1000000", 100);
        assertEquals("1e1000000", new Apcomplex("(2.3025704434574046900603139770105247722136418061583123550509005233524648309767e6,12.5663651568214058018813826529115311494596561375242805601367375513007391)"), LambertWHelper.w(a, 2), new Apfloat("5e-93"));
        
        a = new Apcomplex("(0.1,0.1)").precision(50);
        assertEquals("(0.1,0.1)", new Apcomplex("(-4.46150099885356534168179199389468758681480568726818,11.40818006630707645202745568528364138775149089044037)"), LambertWHelper.w(a, 2), new Apfloat("5e-48"));

        a = new Apcomplex("(0.1,-0.1)").precision(50);
        assertEquals("(0.1,-0.1)", new Apcomplex("(-4.32694598298195301567101913174024904383609408294303,9.79417240669519814321681889366099503452634756326525)"), LambertWHelper.w(a, 2), new Apfloat("5e-49"));

        a = new Apcomplex("(-0.1,0.1)").precision(50);
        assertEquals("(-0.1,0.1)", new Apcomplex("(-4.58038275692009635302775100106541142184228107393803,13.01333459901300720639047799260012653428906553185216)"), LambertWHelper.w(a, 2), new Apfloat("5e-48"));

        a = new Apcomplex("(-0.1,-0.1)").precision(50);
        assertEquals("(-0.1,-0.1)", new Apcomplex("(-4.17206000198761389916980339367569553340272515041870,8.16709991236722495654333433874576651246049617173758)"), LambertWHelper.w(a, 2), new Apfloat("5e-49"));

        a = new Apcomplex("(0.9,0.9)").precision(50);
        assertEquals("(0.9,0.9)", new Apcomplex("(-2.22715505448192395274331336493646484435206146304835,11.59114315011385962030468122170837190368462856991551)"), LambertWHelper.w(a, 2), new Apfloat("5e-48"));

        a = new Apcomplex("(0.9,-0.9)").precision(50);
        assertEquals("(0.9,-0.9)", new Apcomplex("(-2.08308039125534457091462580249672703675832197614088,10.00490281337842995053643413552988782638091117564826)"), LambertWHelper.w(a, 2), new Apfloat("5e-48"));

        a = new Apcomplex("(-0.9,0.9)").precision(50);
        assertEquals("(-0.9,0.9)", new Apcomplex("(-2.35280842816729699806331011414977130420894944780259,13.17505104043316522138211734518438096809787404617582)"), LambertWHelper.w(a, 2), new Apfloat("5e-48"));

        a = new Apcomplex("(-0.9,-0.9)").precision(50);
        assertEquals("(-0.9,-0.9)", new Apcomplex("(-1.91410930491369070543340051140645802624032344774990,8.41574038133572741523579549611439943379759998796892)"), LambertWHelper.w(a, 2), new Apfloat("5e-49"));

        Apfloat minusOnePerE = new Apfloat("-0.36787944117144232159552377016146086744581113103177");
        a = new Apcomplex("(1e-2,1e-2)").precision(50).add(minusOnePerE).precision(50);
        Apcomplex w = LambertWHelper.w(a, 2);
        assertEquals("-1/e + (1e-2,1e-2) value", new Apcomplex("(-3.68965926447077142781441532734581320117222723454881,13.84885659062717730687065656572845992814464654071224)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (1e-2,1e-2) precision", 51, w.precision());

        a = new Apcomplex("(1e-2,-1e-2)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 2);
        assertEquals("-1/e + (1e-2,-1e-2) value", new Apcomplex("(-3.12042317846178133420655257832849242941900358279720,7.48703153555803272188014570620298820375269188816625)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-2,-1e-2) precision", 51, w.precision());

        a = new Apcomplex("(1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 2);
        assertEquals("-1/e + (1e-25,1e-25) value", new Apcomplex("(-3.66406814242907101707075129536537687200837112614960,13.87905600274680942535355712749209564794866828664858)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (1e-25,1e-25) precision", 51, w.precision());

        a = new Apcomplex("(1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 2);
        assertEquals("-1/e + (1e-25,-1e-25)", new Apcomplex("(-3.08884301561304385595708703184389339751173194856795,7.46148928565425455690611685968902321699651460791724)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (1e-25,-1e-25) precision", 51, w.precision());

        a = new Apcomplex("(-1e-25,1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 2);
        assertEquals("-1/e + (-1e-25,1e-25)", new Apcomplex("(-3.66406814242907101707075074445736863315369647333033,13.87905600274680942535355716527114169388794146277880)"), w, new Apfloat("5e-48"));
        assertEquals("-1/e + (-1e-25,1e-25) precision", 51, w.precision());

        a = new Apcomplex("(-1e-25,-1e-25)").precision(50).add(minusOnePerE).precision(50);
        w = LambertWHelper.w(a, 2);
        assertEquals("-1/e + (-1e-25,-1e-25)", new Apcomplex("(-3.08884301561304385595708646927233961805827412632177,7.46148928565425455690611692725536123147513320073334)"), w, new Apfloat("5e-49"));
        assertEquals("-1/e + (-1e-25,-1e-25) precision", 51, w.precision());

        try
        {
            LambertWHelper.w(new Apfloat(0), 2);
            fail("Zero accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK; result would be infinite
        }
    }

    public static void testComplexBigK()
    {
        Apcomplex a = new Apcomplex("(0.1,0.1)").precision(60);
        Apcomplex w = LambertWHelper.w(a, 7);
        assertEquals("(0.1,0.1) k=7 value", new Apcomplex("(-5.727481440633124530039091428658978161456806576373186053989185,43.064677733769856152109519838050639882060333900419297108268996)"), w, new Apfloat("5e-59"));
        assertEquals("(0.1,0.1) k=7 precision", 61, w.precision());

        a = new Apcomplex("(-0.1,-0.1)").precision(40);
        w = LambertWHelper.w(a, 17);
        assertEquals("(-0.1,-0.1) k=17 value", new Apcomplex("(-6.5910722487103657564303352667701682922203,102.8231459290324957587950708059456713967620)"), w, new Apfloat("5e-39"));
        assertEquals("(-0.1,-0.1) k=17 precision", 42, w.precision());

        a = new Apcomplex("(-0.9,0.1)").precision(70);
        w = LambertWHelper.w(a, 37);
        assertEquals("(-0.9,0.1) k=37 value", new Apcomplex("(-5.5544619326951672831439378170318286914007057845042983632997693607309607,233.9142542147682233318982170262440480826987266245944321123830964887996136)"), w, new Apfloat("5e-69"));
        assertEquals("(-0.9,0.1) k=37 precision", 72, w.precision());

        a = new Apcomplex("(0.9,0.9)").precision(80);
        w = LambertWHelper.w(a, 330);
        assertEquals("(0.9,0.9) k=330 value", new Apcomplex("(-7.39538243061762074201129843802868979391130777923658009354747064410444890778712847,2072.66218516129995517643750165430389609331547118778642894683887552361804086525913277)"), w, new Apfloat("5e-79"));
        assertEquals("(0.9,0.9) k=330 precision", 83, w.precision());

        a = new Apcomplex("(0.9,0.9)").precision(90);
        w = LambertWHelper.w(a, 330);
        assertEquals("(0.9,0.9) k=330 value", new Apcomplex("(-7.39538243061762074201129843802868979391130777923658009354747064410444890778712847,2072.66218516129995517643750165430389609331547118778642894683887552361804086525913277)"), w, new Apfloat("5e-89"));
        assertEquals("(0.9,0.9) k=330 precision", 93, w.precision());

        a = new Apcomplex("(2,2)").precision(90);
        w = LambertWHelper.w(a, 1000000);
        assertEquals("(2,2) k=1000000 value", new Apcomplex("(-14.61366672853602840268817724838932227540464815235263968785440674,6.28318452177909724188970670954088541855494104163284957145366705105291e6)"), w, new Apfloat("5e-89"));
        assertEquals("(2,2) k=1000000 precision", 96, w.precision());

        a = new Apcomplex("(1,1)").precision(40);
        w = LambertWHelper.w(a, 1000000000);
        assertEquals("(1,1) k=1000000000 value", new Apcomplex("(-22.214569312950783990694,6.283185306394188309992279941688e9)"), w, new Apfloat("5e-39"));
        assertEquals("(1,1) k=1000000000 precision", 49, w.precision());

        a = new Apcomplex("(1,1)").precision(50);
        w = LambertWHelper.w(a, 1000000000000000000L);
        assertEquals("(1,1) k=1000000000000000000 value", new Apcomplex("(-42.937835150022195141050889596400702754759628494762,6.2831853071795864761398886031615574519449092431393702253372752910936e18)"), w, new Apfloat("5e-49"));
        assertEquals("(1,1) k=1000000000000000000 precision", 68, w.precision());
    }
}
