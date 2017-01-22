package org.apfloat;

import junit.framework.TestSuite;

/**
 * @version 1.8.0
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
        assertEquals("value", new Apcomplex(Apfloat.ZERO, new Apfloat("1.570796326794896619231321691639751442098584699687552910487472296153908203143104499314017412671058534")), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
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
    }

    public static void testTanh()
    {
        FixedPrecisionApcomplexHelper helper = new FixedPrecisionApcomplexHelper(100);
        Apcomplex z = new Apcomplex(new Apfloat(3), new Apfloat(4));
        Apcomplex result = helper.tanh(z);
        assertEquals("value", new Apcomplex("(1.000709536067232939329585472404172746215320905146760218019260729904286640361616955165037427906522640,0.004908258067496060259078786929932766843374215579355506974895511342674738432081043949327359968992711)"), result, new Apfloat("1e-98"));
        assertEquals("precision", 100, result.precision());
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
}
