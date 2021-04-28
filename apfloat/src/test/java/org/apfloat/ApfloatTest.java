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

import java.math.BigInteger;
import java.math.BigDecimal;
import java.io.PushbackReader;
import java.io.Writer;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Formatter;
import java.util.Locale;

import junit.framework.TestSuite;

/**
 * @version 1.10.0
 * @author Mikko Tommila
 */

public class ApfloatTest
    extends ApfloatTestCase
{
    public ApfloatTest(String methodName)
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

        suite.addTest(new ApfloatTest("testLongConstructor"));
        suite.addTest(new ApfloatTest("testFloatConstructor"));
        suite.addTest(new ApfloatTest("testDoubleConstructor"));
        suite.addTest(new ApfloatTest("testStringConstructor"));
        suite.addTest(new ApfloatTest("testStreamConstructor"));
        suite.addTest(new ApfloatTest("testBigIntegerConstructor"));
        suite.addTest(new ApfloatTest("testBigDecimalConstructor"));
        suite.addTest(new ApfloatTest("testComplexMethods"));
        suite.addTest(new ApfloatTest("testRadix"));
        suite.addTest(new ApfloatTest("testPrecision"));
        suite.addTest(new ApfloatTest("testScale"));
        suite.addTest(new ApfloatTest("testSize"));
        suite.addTest(new ApfloatTest("testIsInteger"));
        suite.addTest(new ApfloatTest("testNegate"));
        suite.addTest(new ApfloatTest("testAdd"));
        suite.addTest(new ApfloatTest("testSubtract"));
        suite.addTest(new ApfloatTest("testMultiply"));
        suite.addTest(new ApfloatTest("testDivide"));
        suite.addTest(new ApfloatTest("testMod"));
        suite.addTest(new ApfloatTest("testFloor"));
        suite.addTest(new ApfloatTest("testCeil"));
        suite.addTest(new ApfloatTest("testTruncate"));
        suite.addTest(new ApfloatTest("testFrac"));
        suite.addTest(new ApfloatTest("testRoundAway"));
        suite.addTest(new ApfloatTest("testAbs"));
        suite.addTest(new ApfloatTest("testCompareToHalf"));
        suite.addTest(new ApfloatTest("testNumberValues"));
        suite.addTest(new ApfloatTest("testNumberValuesExact"));
        suite.addTest(new ApfloatTest("testEqualDigits"));
        suite.addTest(new ApfloatTest("testCompareTo"));
        suite.addTest(new ApfloatTest("testEquals"));
        suite.addTest(new ApfloatTest("testTest"));
        suite.addTest(new ApfloatTest("testHashCode"));
        suite.addTest(new ApfloatTest("testToString"));
        suite.addTest(new ApfloatTest("testWriteTo"));
        suite.addTest(new ApfloatTest("testFormatTo"));
        suite.addTest(new ApfloatTest("testSerialization"));

        return suite;
    }

    public static void testLongConstructor()
    {
        Apfloat a = new Apfloat(5);
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", Apfloat.INFINITE, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        a = new Apfloat(6, Apfloat.DEFAULT);
        assertEquals("6 radix", 10, a.radix());
        assertEquals("6 precision", Apfloat.INFINITE, a.precision());
        assertEquals("6 String", "6", a.toString(true));
        a = new Apfloat(7, Apfloat.DEFAULT, 12);
        assertEquals("7 radix", 12, a.radix());
        assertEquals("7 precision", Apfloat.INFINITE, a.precision());
        assertEquals("7 String", "7", a.toString(true));

        try
        {
            new Apfloat(1, 1, Character.MIN_RADIX - 1);
            fail("Radix 1 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(1, 1, Character.MAX_RADIX + 1);
            fail("Radix 37 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(1, 0);
            fail("Precision 0 allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid precision
        }
    }

    public static void testFloatConstructor()
    {
        Apfloat a = new Apfloat(5.0f);
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", 8, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        a = new Apfloat(6.0f, Apfloat.DEFAULT);
        assertEquals("6 radix", 10, a.radix());
        assertEquals("6 precision", 8, a.precision());
        assertEquals("6 String", "6", a.toString(true));
        a = new Apfloat(7.0f, Apfloat.DEFAULT, 12);
        assertEquals("7 radix", 12, a.radix());
        assertEquals("7 precision", 7, a.precision());
        assertEquals("7 String", "7", a.toString(true));

        try
        {
            new Apfloat(1.0f, 1, Character.MIN_RADIX - 1);
            fail("Radix 1 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(1.0f, 1, Character.MAX_RADIX + 1);
            fail("Radix 37 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(1.0f, 0);
            fail("Precision 0 allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid precision
        }
    }

    public static void testDoubleConstructor()
    {
        Apfloat a = new Apfloat(5.0);
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", 16, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        a = new Apfloat(6.0, Apfloat.DEFAULT);
        assertEquals("6 radix", 10, a.radix());
        assertEquals("6 precision", 16, a.precision());
        assertEquals("6 String", "6", a.toString(true));
        a = new Apfloat(7.0, Apfloat.DEFAULT, 12);
        assertEquals("7 radix", 12, a.radix());
        assertEquals("7 precision", 15, a.precision());
        assertEquals("7 String", "7", a.toString(true));

        a = new Apfloat(-9.999999999999996E-10);        // Internally causes strange round-off errors
        assertEquals("-9.999999999999996E-10 precision", 16, a.precision());

        a = new Apfloat(0.33333333333333333, 14);
        assertEquals("0.3 size 14", 14, a.size());

        try
        {
            new Apfloat(1.0, 1, Character.MIN_RADIX - 1);
            fail("Radix 1 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(1.0, 1, Character.MAX_RADIX + 1);
            fail("Radix 37 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(1.0, 0);
            fail("Precision 0 allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid precision
        }
    }

    public static void testStringConstructor()
    {
        Apfloat a = new Apfloat("5");
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", 1, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        a = new Apfloat("99", Apfloat.DEFAULT);
        assertEquals("99 radix", 10, a.radix());
        assertEquals("99 precision", 2, a.precision());
        assertEquals("99 String", "99", a.toString(true));
        a = new Apfloat("aa", Apfloat.DEFAULT, 12);
        assertEquals("aa radix", 12, a.radix());
        assertEquals("aa precision", 2, a.precision());
        assertEquals("aa String", "aa", a.toString(true));

        a = new Apfloat("\u0967\u0968\u0969\u096a\u096b\u096c");
        assertEquals("localized", "123456", a.toString(true));

        try
        {
            new Apfloat("1", 1, Character.MIN_RADIX - 1);
            fail("Radix 1 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat("1", 1, Character.MAX_RADIX + 1);
            fail("Radix 37 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat("1", 0);
            fail("Precision 0 allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid precision
        }

        try
        {
            new Apfloat("1e" + (0x8000000000000000L - 100), 1, 2);
            fail("Overflow allowed");
        }
        catch (OverflowException iae)
        {
            // OK: overflow
        }
    }

    public static void testStreamConstructor()
        throws IOException
    {
        PushbackReader in = new PushbackReader(new StringReader("5"));
        Apfloat a = new Apfloat(in);
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", 1, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        in = new PushbackReader(new StringReader("99a"));
        a = new Apfloat(in, Apfloat.DEFAULT);
        assertEquals("99 radix", 10, a.radix());
        assertEquals("99 precision", 2, a.precision());
        assertEquals("99 String", "99", a.toString(true));
        in = new PushbackReader(new StringReader("aa"));
        a = new Apfloat(in, Apfloat.DEFAULT, 12);
        assertEquals("aa radix", 12, a.radix());
        assertEquals("aa precision", 2, a.precision());
        assertEquals("aa String", "aa", a.toString(true));

        in = new PushbackReader(new StringReader("00099.000"));
        a = new Apfloat(in, Apfloat.DEFAULT);
        assertEquals("00099.000 radix", 10, a.radix());
        assertEquals("00099.000 precision", 5, a.precision());
        assertEquals("00099.000 String", "99", a.toString(true));

        in = new PushbackReader(new StringReader("990.0"));
        a = new Apfloat(in, Apfloat.DEFAULT);
        assertEquals("990.0 radix", 10, a.radix());
        assertEquals("990.0 precision", 4, a.precision());
        assertEquals("990.0 String", "990", a.toString(true));

        try
        {
            in = new PushbackReader(new StringReader("0"));
            new Apfloat(in, 1, Character.MIN_RADIX - 1);
            fail("Radix 1 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            in = new PushbackReader(new StringReader("0"));
            new Apfloat(in, 1, Character.MAX_RADIX + 1);
            fail("Radix 37 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            in = new PushbackReader(new StringReader("0"));
            new Apfloat(in, 0);
            fail("Precision 0 allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid precision
        }
    }

    public static void testBigIntegerConstructor()
    {
        Apfloat a = new Apfloat(BigInteger.valueOf(5));
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", Apfloat.INFINITE, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        a = new Apfloat(BigInteger.valueOf(-6), Apfloat.DEFAULT);
        assertEquals("-6 radix", 10, a.radix());
        assertEquals("-6 precision", Apfloat.INFINITE, a.precision());
        assertEquals("-6 String", "-6", a.toString(true));
        a = new Apfloat(BigInteger.valueOf(7), Apfloat.DEFAULT, 12);
        assertEquals("7 radix", 12, a.radix());
        assertEquals("7 precision", Apfloat.INFINITE, a.precision());
        assertEquals("7 String", "7", a.toString(true));

        a = new Apfloat(BigInteger.valueOf(99999999999999L));
        assertEquals("99999999999999 radix", 10, a.radix());
        assertEquals("99999999999999 precision", Apfloat.INFINITE, a.precision());
        assertEquals("99999999999999 String", "99999999999999", a.toString(true));

        a = new Apfloat(BigInteger.valueOf(-99999999999999L));
        assertEquals("-99999999999999 radix", 10, a.radix());
        assertEquals("-99999999999999 precision", Apfloat.INFINITE, a.precision());
        assertEquals("-99999999999999 String", "-99999999999999", a.toString(true));

        a = new Apfloat(BigInteger.valueOf(999999999999999L));
        assertEquals("999999999999999 radix", 10, a.radix());
        assertEquals("999999999999999 precision", Apfloat.INFINITE, a.precision());
        assertEquals("999999999999999 String", "999999999999999", a.toString(true));

        a = new Apfloat(BigInteger.valueOf(-999999999999999L));
        assertEquals("-999999999999999 radix", 10, a.radix());
        assertEquals("-999999999999999 precision", Apfloat.INFINITE, a.precision());
        assertEquals("-999999999999999 String", "-999999999999999", a.toString(true));

        try
        {
            new Apfloat(BigInteger.valueOf(5), 1, Character.MIN_RADIX - 1);
            fail("Radix 1 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(BigInteger.valueOf(5), 1, Character.MAX_RADIX + 1);
            fail("Radix 37 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            new Apfloat(BigInteger.valueOf(5), 0);
            fail("Precision 0 allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid precision
        }
    }

    public static void testBigDecimalConstructor()
    {
        Apfloat a = new Apfloat(new BigDecimal("5"));
        assertEquals("5 radix", 10, a.radix());
        assertEquals("5 precision", 1, a.precision());
        assertEquals("5 String", "5", a.toString(true));
        a = new Apfloat(new BigDecimal("6"), Apfloat.DEFAULT);
        assertEquals("6 radix", 10, a.radix());
        assertEquals("6 precision", 1, a.precision());
        assertEquals("6 String", "6", a.toString(true));
        a = new Apfloat(new BigDecimal("1.23e7"));
        assertEquals("1.23e7 radix", 10, a.radix());
        assertEquals("1.23e7 precision", 3, a.precision());
        assertEquals("1.23e7 String", "1.23e7", a.toString());

        try
        {
            new Apfloat(new BigDecimal("5"), 0);
            fail("Precision 0 allowed");
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid precision
        }
    }

    public static void testComplexMethods()
    {
        Apfloat a = new Apfloat(5);
        assertEquals("real", a, a.real());
        assertEquals("imag", Apfloat.ZERO, a.imag());
    }

    public static void testRadix()
    {
        Apfloat a = new Apfloat(2, 500, 5);
        assertEquals("radix", 5, a.radix());

        a = new Apfloat(65535);
        assertEquals("65535 10 -> 16", new Apfloat("FFFF", Apfloat.INFINITE, 16), a.toRadix(16));
        assertEquals("65535 10 -> 16 precision", Apfloat.INFINITE, a.toRadix(16).precision());
        a = new Apfloat("65535.50");
        assertEquals("65535.5 10 -> 16", new Apfloat("FFFF.8", Apfloat.INFINITE, 16), a.toRadix(16));
        a = new Apfloat("65535.25");
        assertEquals("65535.25 10 -> 16", new Apfloat("FFFF.4", Apfloat.INFINITE, 16), a.toRadix(16));
        a = new Apfloat("65535.125");
        assertEquals("65535.125 10 -> 16", new Apfloat("FFFF.2", Apfloat.INFINITE, 16), a.toRadix(16));
        a = new Apfloat("123000000000");
        assertEquals("123000000000 10 -> 16", new Apfloat("1CA35F0E00", Apfloat.INFINITE, 16), a.toRadix(16));
        assertEquals("123000000000 10 -> 16 precision", 9, a.toRadix(16).precision());
        a = new Apfloat("0.000000000931322574615478515625");
        assertEquals("0.000000000931322574615478515625 10 -> 16", new Apfloat("0.00000004", Apfloat.INFINITE, 16), a.toRadix(16), new Apfloat("0.000000000000000000000001", Apfloat.INFINITE, 16));
        assertEquals("0.000000000931322574615478515625 10 -> 16 precision", 17, a.toRadix(16).precision());
        assertEquals("0", Apfloat.ZERO, Apfloat.ZERO.toRadix(16));

        a = new Apfloat(1, 1, 2);
        assertEquals("1 2 -> 36", new Apfloat(1, 1, 36), a.toRadix(36));
        assertEquals("1 2 -> 36 precision", 1, a.toRadix(36).precision());

        a = new Apfloat(1, Long.MAX_VALUE - 1000000000, 36);
        assertEquals("1 36 -> 2", new Apfloat(1, Apfloat.INFINITE, 2), a.toRadix(2));
        assertEquals("1 36 -> 2 precision", Apfloat.INFINITE, a.toRadix(2).precision());

        a = new Apfloat("30663651432036134110263402244652226643520650240155443215426431025161154565220002622436103301443233631011304100550041024125352116552105536251503033124242402610043630564530526330241326140210045006340104466521000354000404111331215235323543345406426461100254233215013111611145330503541442000526254312414615064636661250340621566513342655053121614145360201046310465243314220526011154664430043014201423420324045214415130132414620525166152443345045032350015230030362164454610650213462563452443511056450311105.22332541403314616306305566400025124136353353135500314364221261503410163015234165116544562313163301340550004143332123320511041210402240503346501010222303304655116161250452506544564304213630542260560421643101224533463224115516324432200114053015605444210505633501042351630165624443056253363046253533144415643620525422651636016443512214133340311231201415424110210002660121216352350661533034035553261641500660122112023312140250433042514535345621255544000222315031426131123345316551523042065315342155413506", 1000, 7);
        assertEquals("pi*7^499 7 -> 36", new Apfloat("2rv02k6m9ibha286eemjtdx59crclwjbkxum8hn0nc2eaj8u4fbltf547z5lqh4g0euy4p9wo0u5h3iza23atp64rirlqk9xvck8u1s2w4efuhhg3mjkhrpd8rx7hqgxgqadotv3gzun0elz30tlwrnrm8ydc8syy06a88yfdmgt37ee5gok338r2vwfhr6a5ifka3bv75xvardxighwf0r1o6sgtdsh9ynno3k0ug1u56s17ajcaj8lsox1dw02lyjk4vvi8u8atao1.c4cr2sltzlqouiqpz54wugysq73j65kwm4xlny74dwtxlmquu7l40nltbbhhyw40f6075p1pt8joyggssiri3x4151kai4hslu24fha924duoenty0w6ar6ivz92x51nbtot8n6pyxmbfomd4ph8nstk0fyy0hmawmf2c310s3dztsi7amyexdgoa7jobl4aigqexsqddwkshtfvo71l2omiomph837ocwaprkx9bm91a2mnx39e1uo7k8sca59ezhr1kqcrfh7olwt", 543, 36), a.toRadix(36), ApfloatMath.scale(new Apfloat(1, 1, 36), -271));
        assertEquals("pi*7^499 7 -> 36 precision", 543, a.toRadix(36).precision());

        a = new Apfloat("3184809493b918664573a6211bb151551a05729290a7809a492742140a60a55256a0661a03753a3aa54805646880181a3683083272bbba0a370b12265529a828903b4b256b8403759a71626b8a54687621849b849a8225616b442796a31737b229b2391489853943b8763725616447236b027a421aa17a38b52a18a838b01514a51144a23315a3009a8906b61b8b48a62253a88a50a43ba0944572315933664476b3aabb77583975120683526b75b462060bb03b432551913772729a2147553531793848a0402b999b5058535374465a68806716644039539a8431935198527b9399b112990abb0383b107645424577a51601b3624a88b7a676a.3992912121a213887b92873946a61332242217aa7354115357744939112602ba4b888818a3269222b528487747839994ab223b65b8762695422822669ba00a586097842a51750362073b5a768363b21bb1a97a4a194447749399804922175a068a46739461990a2065bb0a30bbab7024a585b1a84428195489784a07a331a7b0a1574565b373b05b03a5a80a13ab87857734679985558a5373178a7b28271992a3894a5776085083b9b238b2220542462888641a2bab8b3083ab49659172a312b78518654494a068662586a181835a64440b2970a122813975898815367208905801032881449223841428763329617531239b9a657405584014", 1000, 12);
        assertEquals("pi*7^499 12 -> 2", new Apfloat("101110110001110110000110100110001111010111001001000010110000010100110010101011001011010011000101011110110000111110011111101101110000111101010111000111101000101100010100011111011110101001011111111111101110000100101011110010011111001001110101010110111000110011111010000000001011011101111001110001110011011101011110100100010110000001001100000110110011011100111111011011011110010111001000010101110001100110110001101001010000110100110101111110101011110001001000100110110000110110001111110010100111110000110010001110111100000001000101000010101100110111111101110001101100100110010111000101010010001011111110110001111001011100011010001010101110101111010101010100001100000001110110100000110001001000111110011101001110101110000010000101001110000101101111011010000011110100110011011011111011101000110110011010101001001010000000101010000101101010111110111011010011110110111001111111111010011111000110011101110011111101111000110010101011010101000100100000011011001101110001000011010111111110110011000110100101111010011011100011101001001100001100100100011110001111100110010101001101100111010001010011111010101110111110101110000001000100011100001010110011000101010010110100101111101110110000010111101100010100000001101010111000001100100001000001011000100001001111110000111100111011010111001100111101000010000010101001001110011110011110001011110110001111000010011001110011000001001101011111000010110101010100101101001001001001111110000101000000010110111000010000101000101110010010101001100001010011010111011110010100101101010100110100100110100100001101000011101110100100010101100111110101000110010010011110011000100001111100001000011110001011011010000110010101111000011001010100101011001101011100111001100001101011010101011100001111000100111101100110010100100100111100100110000000101011010010001101011000010.01010001010111100000110011101100000011110100100010011101001100001011010000101001001100000111001001100001000000100110100001011001001010110011100011111000001110001101110110101010101010101001010000011101010110011000111101010100000011000010101001101011100000001110000011101100011100111110100001110110010111011001000100000000001100100100100011100101000111010011001011000101010001001101101001111101011100101001110100001101010101011011000001101000111011010101101001010011010111011011011000101101010010101110000000000001011110000010000100011010001001010101001011010110000100000100101011111011101110000001110101101001101111100011111110100000100111001100001010100100101000100100101011111100110001000110010001110100111110011000111101111101110111000001110000001001100101101100001111010100100110011000000010100010110100010010111101010010000010011011010101010101110010100100000010100111101100000111111111100100011000101000010010011111100110001011000010000101001111111111011111101010011101101101101100111101000101111001101000010110101000101100100100110101101001000000001011001000111100001000111000011011101010111010011001011111101111101110111010001000100101110100110011111101000111010100011101110100100011001000101100100000001000001110000001010011101110101110011111000000000001001101001011111000010111110100100010010101001000011000110111100010100111011111010100110010010100111101110011011110010100100001000111100111111101111110000011000111111110001000100010101000000001011110110010111011111111000001001111010001100111010110100010001110011100110110000011110100001010010000010011000011010110001011101110001011101110110011010001101111011111001101110100010101001110101110101000111100101100101110011101011111001001111010101110101110001010110000101000101011100110010110010111001110101111000100100111101101101101001", 3584, 2), a.toRadix(2), new Apfloat("1e-1793", 1, 2));
        assertEquals("pi*7^499 12 -> 2 precision", 3584, a.toRadix(2).precision());

        a = new Apfloat("1e1000000000", 10);
        assertEquals("1e1000000000 10 -> 8", new Apfloat("7.3142764265e1107309364", 11, 8), a.toRadix(8), new Apfloat("0.0000000001e1107309364", 1, 8));

        for (int fromRadix = Character.MIN_RADIX; fromRadix <= Character.MAX_RADIX; fromRadix++)
        {
            for (int toRadix = Character.MIN_RADIX; toRadix <= Character.MAX_RADIX; toRadix++)
            {
                final int LENGTH = 1000;
                StringBuilder buffer = new StringBuilder(LENGTH);
                for (int i = 0; i < LENGTH; i++)
                {
                    buffer.append(Character.forDigit(fromRadix - 1, fromRadix));
                }
                Apfloat actual = new Apfloat(buffer.toString(), Apfloat.INFINITE, fromRadix).toRadix(toRadix),
                        expected = ApfloatMath.pow(new Apfloat(fromRadix, Apfloat.INFINITE, toRadix), LENGTH).subtract(new Apfloat(1, Apfloat.INFINITE, toRadix));
                assertEquals("999...999 " + fromRadix + " -> " + toRadix, expected, actual);
            }
        }

        try
        {
            a.toRadix(Character.MIN_RADIX - 1);
            fail("To radix 1 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }

        try
        {
            a.toRadix(Character.MAX_RADIX + 1);
            fail("To radix 37 allowed");
        }
        catch (NumberFormatException nfe)
        {
            // OK: invalid radix
        }
    }

    public static void testPrecision()
    {
        Apfloat a = new Apfloat(5, 5);
        assertEquals("5 precision", 5, a.precision());
        a = a.precision(4);
        assertEquals("4 precision", 4, a.precision());
        a = a.precision(6);
        assertEquals("6 precision", 6, a.precision());
        a = new Apfloat(0);
        assertEquals("0 precision", Apfloat.INFINITE, a.precision());
    }

    public static void testScale()
    {
        Apfloat a = new Apfloat(9);
        assertEquals("9 scale", 1, a.scale());
        a = new Apfloat(10);
        assertEquals("10 scale", 2, a.scale());
        a = new Apfloat(0);
        assertEquals("0 scale", -Apfloat.INFINITE, a.scale());

        a = new Apfloat(9);
        assertEquals("9 scaled 1", new Apfloat(90), a.scale(1));
        assertEquals("9 scaled -1", new Apfloat("0.9"), a.scale(-1));
    }

    public static void testSize()
    {
        Apfloat a = new Apfloat(9);
        assertEquals("9 size", 1, a.size());
        a = new Apfloat(1);
        assertEquals("1 size", 1, a.size());
        a = new Apfloat(10);
        assertEquals("10 size", 1, a.size());
        a = new Apfloat(99);
        assertEquals("99 size", 2, a.size());
        a = new Apfloat(110);
        assertEquals("110 size", 2, a.size());
        a = new Apfloat("0.11");
        assertEquals("0.11 size", 2, a.size());
        a = new Apfloat("-0.00123456789");
        assertEquals("-0.00123456789 size", 9, a.size());
        a = new Apfloat(9909, 2);
        assertEquals("9909 prec 2 size", 2, a.size());
        a = new Apfloat(9999, 2);
        assertEquals("9999 prec 2 size", 2, a.size());
        a = new Apfloat(999999999999999L);
        assertEquals("999999999999999 size", 15, a.size());
        a = new Apfloat(999999999999990L);
        assertEquals("999999999999990 size", 14, a.size());
        a = new Apfloat(999999999999900L);
        assertEquals("999999999999900 size", 13, a.size());
        a = new Apfloat(999999999999000L);
        assertEquals("999999999999000 size", 12, a.size());
        a = new Apfloat(999999999990000L);
        assertEquals("999999999990000 size", 11, a.size());
        a = new Apfloat(999999999900000L);
        assertEquals("999999999900000 size", 10, a.size());
        a = new Apfloat(999999999000000L);
        assertEquals("999999999000000 size", 9, a.size());
        a = new Apfloat(999999990000000L);
        assertEquals("999999990000000 size", 8, a.size());
        a = new Apfloat(999999900000000L);
        assertEquals("999999900000000 size", 7, a.size());
        a = new Apfloat(999999000000000L);
        assertEquals("999999000000000 size", 6, a.size());
        a = new Apfloat(999990000000000L);
        assertEquals("999990000000000 size", 5, a.size());
        a = new Apfloat(999900000000000L);
        assertEquals("999900000000000 size", 4, a.size());
        a = new Apfloat(999000000000000L);
        assertEquals("999000000000000 size", 3, a.size());
        a = new Apfloat(990000000000000L);
        assertEquals("990000000000000 size", 2, a.size());
        a = new Apfloat(900000000000000L);
        assertEquals("900000000000000 size", 1, a.size());
        a = new Apfloat(0);
        assertEquals("0 size", 0, a.size());

        a = new Apfloat("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");
        assertEquals("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001 size", 100, a.size());
        a = a.precision(99);
        assertEquals("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001 prec 99 size", 1, a.size());
        a = a.precision(90);
        assertEquals("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001 prec 90 size", 1, a.size());

        a = new Apfloat("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000001");
        a = a.precision(99);
        assertEquals("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000001 prec 99 size", 90, a.size());

        a = new Apfloat("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000001");
        a = a.precision(90);
        assertEquals("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000001 prec 90 size", 90, a.size());

        a = new Apfloat("11000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000001");
        a = a.precision(1);
        assertEquals("11000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000001 prec 1 size", 1, a.size());
    }

    public static void testIsInteger()
    {
        Apfloat a = new Apfloat("1.5");
        assertFalse("1.5", a.isInteger());
        a = new Apfloat("-0.1");
        assertFalse("-0.1", a.isInteger());
        a = new Apfloat("1");
        assertTrue("1", a.isInteger());
        a = new Apfloat("0");
        assertTrue("0", a.isInteger());
        a = new Apfloat(-1);
        assertTrue("-1", a.isInteger());
        a = new Apfloat("1e1000000");
        assertTrue("1e1000000", a.isInteger());
    }

    public static void testNegate()
    {
        Apfloat x = new Apfloat(2);
        assertEquals("2", new Apfloat(-2), x.negate());

        x = new Apfloat(-2);
        assertEquals("-2", new Apfloat(2), x.negate());

        x = new Apfloat(0);
        assertEquals("0", new Apfloat(0), x.negate());

        Apcomplex y = new Apfloat(2);
        assertEquals("-2", new Apcomplex("-2"), y.negate());
    }

    public static void testAdd()
    {
        Apfloat a = new Apfloat(4),
                b = new Apfloat(5);
        assertEquals("4 + 5", new Apfloat(9), a.add(b));
        assertEquals("4 + 0", new Apfloat(4), a.add(new Apfloat(0)));
        assertEquals("0 + 4", new Apfloat(4), new Apfloat(0).add(a));

        a = new Apfloat("1");
        b = new Apfloat("0.00001");
        assertEquals("1 + 0.00001", new Apfloat(1), a.add(b), b);
        assertEquals("0.00001 + 1", new Apfloat(1), b.add(a), b);
    }

    public static void testSubtract()
    {
        Apfloat a = new Apfloat(4),
                b = new Apfloat(5);
        assertEquals("4 - 5", new Apfloat(-1), a.subtract(b));
        assertEquals("4 - 0", new Apfloat(4), a.subtract(new Apfloat(0)));
        assertEquals("0 - 4", new Apfloat(-4), new Apfloat(0).subtract(a));

        a = new Apfloat("1");
        b = new Apfloat("0.00001");
        assertEquals("1 - 0.00001", new Apfloat(1), a.subtract(b), b);
        assertEquals("0.00001 - 1", new Apfloat(-1), b.subtract(a), b);
    }

    public static void testMultiply()
    {
        Apfloat a = new Apfloat(4, Apfloat.INFINITE, 12),
                b = new Apfloat(5, Apfloat.INFINITE, 12);
        assertEquals("4 * 5", new Apfloat(20, Apfloat.DEFAULT, 12), a.multiply(b));
        assertEquals("4 * 0", new Apfloat(0), a.multiply(new Apfloat(0)));
        assertEquals("0 * 4", new Apfloat(0), new Apfloat(0).multiply(a));
        assertEquals("4 * 1", new Apfloat(4, Apfloat.DEFAULT, 12), a.multiply(new Apfloat(1, Apfloat.DEFAULT, 12)));
        assertEquals("1 * 4", new Apfloat(4, Apfloat.DEFAULT, 12), new Apfloat(1, Apfloat.DEFAULT, 12).multiply(a));
        assertEquals("4 * ONE", new Apfloat(4, Apfloat.DEFAULT, 12), a.multiply(Apfloat.ONE));
        assertEquals("ONE * 4", new Apfloat(4, Apfloat.DEFAULT, 12), Apfloat.ONE.multiply(a));
    }

    public static void testDivide()
    {
        Apfloat a = new Apfloat(4, Apfloat.INFINITE, 12),
                b = new Apfloat(6, Apfloat.INFINITE, 12);
        assertEquals("4 / 6", new Apfloat("0.8", Apfloat.DEFAULT, 12), a.divide(b));
        assertEquals("0 / 4", new Apfloat(0), new Apfloat(0).divide(a));
        assertEquals("4 / 1", new Apfloat(4, Apfloat.INFINITE, 12), a.divide(new Apfloat(1, Apfloat.DEFAULT, 12)));
        assertEquals("4 / ONE", new Apfloat(4, Apfloat.INFINITE, 12), a.divide(Apfloat.ONE));
        try
        {
            a.divide(new Apfloat(0));
            fail("Division by zero allowed");
        }
        catch (ArithmeticException ae)
        {
            // OK: division by zero
        }
        assertEquals("long / long", new Apfloat(1), new Apfloat("101010101010101010101010101010101010101").divide(new Apfloat("101010101010101010101010101010101010101")), new Apfloat(2e-38));
    }

    public static void testMod()
    {
        Apfloat a = new Apfloat(4),
                b = new Apfloat(3);
        assertEquals("4 % 3", new Apfloat(1), a.mod(b));
    }

    public static void testFloor()
    {
        Apfloat a = new Apfloat("1.1");
        assertEquals("1.1 floor", new Apfloat(1), a.floor());
        assertEquals("1.1 floor reverse", a.floor(), new Apfloat(1));
        a = new Apfloat(1);
        assertEquals("1 floor", new Apfloat(1), a.floor());
        a = new Apfloat("1.99999999999999");
        assertEquals("1.99999999999999 floor", new Apfloat(1), a.floor());
        a = new Apfloat(0);
        assertEquals("0 floor", new Apfloat(0), a.floor());
        a = new Apfloat("-1.1");
        assertEquals("-1.1 floor", new Apfloat(-2), a.floor());
    }

    public static void testCeil()
    {
        Apfloat a = new Apfloat("1.1");
        assertEquals("1.1 ceil", new Apfloat(2), a.ceil());
        a = new Apfloat(1);
        assertEquals("1 ceil", new Apfloat(1), a.ceil());
        a = new Apfloat("1.99999999999999");
        assertEquals("1.99999999999999 ceil", new Apfloat(2), a.ceil());
        a = new Apfloat(0);
        assertEquals("0 ceil", new Apfloat(0), a.ceil());
        a = new Apfloat("-1.1");
        assertEquals("-1.1 ceil", new Apfloat(-1), a.ceil());
    }

    public static void testTruncate()
    {
        Apfloat a = new Apfloat("1.1");
        assertEquals("1.1 truncate", new Apfloat(1), a.truncate());
        a = new Apfloat(1);
        assertEquals("1 truncate", new Apfloat(1), a.truncate());
        a = new Apfloat("1.99999999999999");
        assertEquals("1.99999999999999 truncate", new Apfloat(1), a.truncate());
        a = new Apfloat(0);
        assertEquals("0 truncate", new Apfloat(0), a.truncate());
        a = new Apfloat("-1.1");
        assertEquals("-1.1 truncate", new Apfloat(-1), a.truncate());
        a = new Apfloat("1e" + (0x8000000000000000L - 1000), 1, 2);
        assertEquals("1e7FFFFFFFFFFFFF9C base 2 truncate", new Apfloat("1e" + (0x8000000000000000L - 1000), 1, 2), a.truncate());
    }

    public static void testFrac()
    {
        Apfloat a = new Apfloat("1.1");
        assertEquals("1.1 frac", new Apfloat("0.1"), a.frac());
        a = new Apfloat(1);
        assertEquals("1 frac", new Apfloat(0), a.frac());
        a = new Apfloat("1.99999999999999");
        assertEquals("1.99999999999999 frac", new Apfloat("0.99999999999999"), a.frac());
        a = new Apfloat(0);
        assertEquals("0 frac", new Apfloat(0), a.frac());
        a = new Apfloat("-1.1");
        assertEquals("-1.1 frac", new Apfloat("-0.1"), a.frac());
        a = new Apfloat("1.01").precision(2);
        assertEquals("1.01 lost precision frac", new Apfloat(0), a.frac());
    }

    public static void testRoundAway()
    {
        Apfloat a = new Apfloat("1.1");
        assertEquals("1.1 roundAway", new Apfloat(2), a.roundAway());
        a = new Apfloat("-1.1");
        assertEquals("-1.1 roundAway", new Apfloat(-2), a.roundAway());
    }

    public static void testAbs()
    {
        Apfloat a = new Apfloat("2");
        assertEquals("2 abs", new Apfloat(2), a.abs());
        a = new Apfloat(0);
        assertEquals("0 abs", new Apfloat(0), a.abs());
        a = new Apfloat(-2);
        assertEquals("-2 abs", new Apfloat(2), a.abs());
    }

    public static void testCompareToHalf()
    {
        Apfloat a = new Apfloat("0.4");
        assertEquals("0.4 compareToHalf", -1, a.compareToHalf());
        a = new Apfloat("0.5");
        assertEquals("0.5 compareToHalf", 0, a.compareToHalf());
        a = new Apfloat("0.6");
        assertEquals("0.6 compareToHalf", 1, a.compareToHalf());
    }

    public static void testNumberValues()
    {
        Apfloat a = new Apfloat(5);
        assertEquals("5 longValue", 5, a.longValue());
        assertEquals("5 intValue", 5, a.intValue());
        assertEquals("5 shortValue", 5, a.shortValue());
        assertEquals("5 byteValue", 5, a.byteValue());
        assertEquals("5 floatValue", 5.0f, a.floatValue(), 0.0f);
        assertEquals("5 doubleValue", 5.0, a.doubleValue(), 0.0);
        a = new Apfloat(1000000000000L);
        assertEquals("1000000000000 intValue", Integer.MAX_VALUE, a.intValue());
        assertEquals("1000000000000 shortValue", Short.MAX_VALUE, a.shortValue());
        assertEquals("1000000000000 byteValue", Byte.MAX_VALUE, a.byteValue());
        a = new Apfloat(-1000000000000L);
        assertEquals("-1000000000000 intValue", Integer.MIN_VALUE, a.intValue());
        assertEquals("-1000000000000 shortValue", Short.MIN_VALUE, a.shortValue());
        assertEquals("-1000000000000 byteValue", Byte.MIN_VALUE, a.byteValue());
        a = new Apfloat("1e100");
        assertEquals("1e100 floatValue", Float.POSITIVE_INFINITY, a.floatValue(), 0.0f);
        a = new Apfloat("-1e100");
        assertEquals("-1e100 floatValue", Float.NEGATIVE_INFINITY, a.floatValue(), 0.0f);
        a = new Apfloat("1e1000");
        assertEquals("1e1000 doubleValue", Double.POSITIVE_INFINITY, a.doubleValue(), 0.0);
        a = new Apfloat("-1e1000");
        assertEquals("-1e1000 doubleValue", Double.NEGATIVE_INFINITY, a.doubleValue(), 0.0);
    }

    public static void testNumberValuesExact()
    {
        Apfloat a = new Apfloat(5);
        assertEquals("5 longValueExact", 5, a.longValueExact());
        assertEquals("5 intValueExact", 5, a.intValueExact());
        assertEquals("5 shortValueExact", 5, a.shortValueExact());
        assertEquals("5 byteValueExact", 5, a.byteValueExact());
        a = new Apfloat("5.5");
        assertEquals("5.5 longValueExact", 5, a.longValueExact());
        assertEquals("5.5 intValueExact", 5, a.intValueExact());
        assertEquals("5.5 shortValueExact", 5, a.shortValueExact());
        assertEquals("5.5 byteValueExact", 5, a.byteValueExact());
        assertEquals("MAX_VALUE longValueExact", Long.MAX_VALUE, new Apfloat(Long.MAX_VALUE).longValueExact());
        assertEquals("MIN_VALUE longValueExact", Long.MIN_VALUE, new Apfloat(Long.MIN_VALUE).longValueExact());
        assertEquals("MAX_VALUE intValueExact", Integer.MAX_VALUE, new Apfloat(Integer.MAX_VALUE).intValueExact());
        assertEquals("MIN_VALUE intValueExact", Integer.MIN_VALUE, new Apfloat(Integer.MIN_VALUE).intValueExact());
        assertEquals("MAX_VALUE shortValueExact", Short.MAX_VALUE, new Apfloat(Short.MAX_VALUE).shortValueExact());
        assertEquals("MIN_VALUE shortValueExact", Short.MIN_VALUE, new Apfloat(Short.MIN_VALUE).shortValueExact());
        assertEquals("MAX_VALUE byteValueExact", Byte.MAX_VALUE, new Apfloat(Byte.MAX_VALUE).byteValueExact());
        assertEquals("MIN_VALUE byteValueExact", Byte.MIN_VALUE, new Apfloat(Byte.MIN_VALUE).byteValueExact());
        try
        {
            new Apfloat(Long.MAX_VALUE).add(new Apfloat(1)).longValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
        try
        {
            new Apfloat(Long.MIN_VALUE).subtract(new Apfloat(1)).longValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
        try
        {
            new Apfloat(Integer.MAX_VALUE + 1L).intValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
        try
        {
            new Apfloat(Integer.MIN_VALUE - 1L).intValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
        try
        {
            new Apfloat(Short.MAX_VALUE + 1L).shortValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
        try
        {
            new Apfloat(Short.MIN_VALUE- 1L).shortValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
        try
        {
            new Apfloat(Byte.MAX_VALUE + 1L).byteValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
        try
        {
            new Apfloat(Byte.MIN_VALUE - 1L).byteValueExact();
            fail("Value out of range accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK
        }
    }

    public static void testEqualDigits()
    {
        Apfloat a = new Apfloat(5),
                b = new Apfloat(6);
        assertEquals("5 eq 6", 0, a.equalDigits(b));
        a = new Apfloat(10);
        b = new Apfloat(11);
        assertEquals("10 eq 11", 1, a.equalDigits(b));
        a = new Apfloat(7);
        b = new Apfloat(7);
        assertEquals("7 eq 7", Apfloat.INFINITE, a.equalDigits(b));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void testCompareTo()
    {
        Apfloat a = new Apfloat(0),
                b = new Apfloat(1);
        assertEquals("0 cmp 1", -1, a.compareTo(b));
        a = new Apfloat(6);
        b = new Apfloat(5);
        assertEquals("6 cmp 5", 1, a.compareTo(b));
        a = new Apfloat(6);
        b = new Apfloat(6);
        assertEquals("6 cmp 6", 0, a.compareTo(b));
        a = new Apfloat(6);
        b = a.floor();
        assertEquals("6 cmp 6.floor()", 0, a.compareTo(b));

        Comparable o = a;
        assertEquals("6 cmp object 6", 0, o.compareTo(o));

        try
        {
            Comparable c = a;
            c.compareTo("bogus");
            fail("Comparison to different class allowed");
        }
        catch (ClassCastException cce)
        {
            // OK: class can't be cast to Apfloat
        }
    }

    public static void testEquals()
    {
        Apfloat a = new Apfloat(0),
                b = new Apfloat(1);
        assertEquals("0 == 1", false, a.equals(b));
        a = new Apfloat(6);
        b = new Apfloat(5);
        assertEquals("6 == 5", false, a.equals(b));
        a = new Apfloat(6);
        b = new Apfloat(6);
        assertEquals("6 == 6", true, a.equals(b));

        assertEquals("6 == something else", false, a.equals("bogus"));

        a = new Apfloat(0, 1, 10);
        b = new Apfloat(0, 1, 12);
        assertEquals("0 == 0 different radixes", true, a.equals(b));
        a = new Apfloat(1, 1, 10);
        b = new Apfloat(1, 1, 12);
        assertEquals("1 == 1 different radixes", true, a.equals(b));

        ApfloatContext ctx = (ApfloatContext) ApfloatContext.getGlobalContext().clone();
        a = new Apfloat(0);
        b = new Apfloat(1);
        ctx.setBuilderFactory(new org.apfloat.internal.LongBuilderFactory());
        ApfloatContext.setThreadContext(ctx);
        assertEquals("0 == 0 different implementations", true, a.equals(new Apfloat(0)));
        assertEquals("1 == 1 different implementations", true, b.equals(new Apfloat(1)));
        ApfloatContext.removeThreadContext();
    }

    public static void testTest()
    {
        Apfloat a = new Apfloat(0),
                b = new Apfloat(0);
        assertTrue("0 test 0", a.test(b));
        a = new Apfloat(0);
        b = new Apfloat(1);
        assertFalse("0 test 1", a.test(b));
        a = new Apfloat(1);
        b = new Apfloat(0);
        assertFalse("1 test 0", a.test(b));
        a = new Apfloat(1);
        b = new Apfloat(1);
        assertTrue("1 test 1", a.test(b));
        a = new Apfloat(1);
        b = new Apfloat(-1);
        assertFalse("1 test -1", a.test(b));
        a = new Apfloat(-1);
        b = new Apfloat(1);
        assertFalse("-1 test 1", a.test(b));
        a = new Apfloat(1);
        b = new Apfloat(10);
        assertFalse("1 test 10", a.test(b));
        a = new Apfloat(10);
        b = new Apfloat(1);
        assertFalse("10 test 1", a.test(b));
        a = new Apfloat(10);
        b = new Apfloat(11);
        assertFalse("10 test 11", a.test(b));
        a = new Apfloat(11);
        b = new Apfloat(10);
        assertFalse("11 test 10", a.test(b));
        a = new Apfloat(1, 1);
        b = new Apfloat(1, 2);
        assertTrue("1 test 1, prec", a.test(b));
        a = new Apfloat(1, 1, 10);
        b = new Apfloat(1, 1, 11);
        assertTrue("1 test 1, radix", a.test(b));
    }

    public static void testHashCode()
    {
        Apfloat a = new Apfloat(0),
                b = new Apfloat(1);
        assertTrue("0 != 1", a.hashCode() != b.hashCode());
        a = new Apfloat(6);
        b = new Apfloat(5);
        assertTrue("5 != 6", a.hashCode() != b.hashCode());
        a = new Apfloat(6);
        b = new Apfloat(6);
        assertEquals("6 == 6", a.hashCode(), b.hashCode());
    }

    public static void testToString()
    {
        Apfloat a = new Apfloat(0);
        assertEquals("0", "0", "" + a);
        a = new Apfloat(6);
        assertEquals("6", "6", "" + a);
        a = new Apfloat(123456789, 9);
        assertEquals("123456789", "1.23456789e8", "" + a);
        a = new Apfloat(123456789, 9);
        assertEquals("123456789 pretty", "123456789", a.toString(true));
    }

    public static void testWriteTo()
        throws IOException
    {
        StringWriter out = new StringWriter();
        Apfloat a = new Apfloat(0);
        a.writeTo(out);
        a = new Apfloat(6);
        a.writeTo(out);
        a = new Apfloat(123456789, 9);
        a.writeTo(out);
        a.writeTo(out, true);
        assertEquals("string", "061.23456789e8123456789", out.toString());
    }

    public static void testFormatTo()
        throws IOException
    {
        System.setProperty("java.locale.providers", "COMPAT,SPI"); // Required since Java 10 to have all locale providers available

        Locale locale = null;
        assertEquals("null %s", "1.234567890123456e5", String.format(locale, "%s", new Apfloat("123456.7890123456")));
        assertEquals("null %S", "1.234567890123456E5", String.format(locale, "%S", new Apfloat("123456.7890123456")));
        assertEquals("null %#s", "123456.7890123456", String.format(locale, "%#s", new Apfloat("123456.7890123456")));
        assertEquals("null %.3s", "1.23e5", String.format(locale, "%.3s", new Apfloat("123456.7890123456")));
        assertEquals("null %.20s", "1.234567890123456e5", String.format(locale, "%.20s", new Apfloat("123456.7890123456")));
        assertEquals("null %20s", " 1.234567890123456e5", String.format(locale, "%20s", new Apfloat("123456.7890123456")));
        assertEquals("null %-20s", "1.234567890123456e5 ", String.format(locale, "%-20s", new Apfloat("123456.7890123456")));
        assertEquals("null %#20s", "   123456.7890123456", String.format(locale, "%#20s", new Apfloat("123456.7890123456")));
        assertEquals("null %#-20s", "123456.7890123456   ", String.format(locale, "%#-20s", new Apfloat("123456.7890123456")));

        locale = new Locale("fi", "FI");
        assertEquals("fi_FI %s", "1,234567890123456e5", String.format(locale, "%s", new Apfloat("123456.7890123456")));
        assertEquals("fi_FI %s radix 11", "1,23456e5", String.format(locale, "%s", new Apfloat("123456", 6, 11)));
        assertEquals("fi_FI %S radix 11", "1,23456789AE9", String.format(locale, "%S", new Apfloat("123456789a", 10, 11)));

        locale = new Locale("hi", "IN");
        assertEquals("hi_IN %#.6s", "\u0967\u0968\u0969\u096a\u096b\u096c", String.format(locale, "%#.6s", new Apfloat("123456.7890123456")));
        assertEquals("hi_IN %#s radix 9", "\u0967\u0968\u0969\u096a\u096b\u096c", String.format(locale, "%#s", new Apfloat("123456", 6, 9)));
        assertEquals("hi_IN %s radix 11", "1.23456e5", String.format(locale, "%s", new Apfloat("123456", 6, 11)));

        Writer writer = new Writer()
        {
            @Override
            public void write(char cbuf[], int off, int len)
                throws IOException
            {
                throw new IOException();
            }

            @Override
            public void flush()
                throws IOException
            {
                throw new IOException();
            }

            @Override
            public void close()
                throws IOException
            {
                throw new IOException();
            }
        };
        Formatter formatter = new Formatter(writer);
        new Apfloat("123456.789").formatTo(formatter, 0, -1, -1);
    }

    private static String getString(char character, int length)
    {
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            buffer.append(character);
        }

        return buffer.toString();
    }

    public static void testSerialization()
        throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);
        Apfloat a = new Apfloat(5);
        out.writeObject(a);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        Apfloat b = (Apfloat) in.readObject();
        assertEquals("5 equals", a, b);
        assertNotSame("5 !=", a, b);

        a = new Apfloat(getString('a', 1000000) + ".a", Apfloat.DEFAULT, 12);
        Apfloat a2 = a.floor();
        buffer.reset();
        out = new ObjectOutputStream(buffer);
        out.writeObject(a);
        assertTrue("Data has been used: " + buffer.size() + " > 400000", buffer.size() > 400000);
        out.writeObject(a2);
        out.close();
        in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        b = (Apfloat) in.readObject();
        Apfloat b2 = (Apfloat) in.readObject();
        assertEquals("1000000 equals", a, b);
        assertNotSame("1000000 !=", a, b);
        assertEquals("1000000 floor equals", a2, b2);
        assertNotSame("1000000 floor !=", a2, b2);
        assertTrue("Data has been shared: " + buffer.size() + " < 700000", buffer.size() < 700000);

        // Serialization from legacy data
        // new Apfloat(12345678900000L) in apfloat 1.0.3 format
        byte[] bytes = { -84, -19, 0, 5, 115, 114, 0, 19, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 102, 108, 111, 97,
                         116, -1, 125, -106, -56, -92, 28, 107, 73, 2, 0, 1, 76, 0, 4, 105, 109, 112, 108, 116, 0, 29, 76, 111, 114, 103, 47,
                         97, 112, 102, 108, 111, 97, 116, 47, 115, 112, 105, 47, 65, 112, 102, 108, 111, 97, 116, 73, 109, 112, 108, 59, 120,
                         114, 0, 21, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 99, 111, 109, 112, 108, 101, 120, 50,
                         -114, 76, 62, -100, 91, -70, -73, 2, 0, 2, 76, 0, 4, 105, 109, 97, 103, 116, 0, 21, 76, 111, 114, 103, 47, 97, 112,
                         102, 108, 111, 97, 116, 47, 65, 112, 102, 108, 111, 97, 116, 59, 76, 0, 4, 114, 101, 97, 108, 113, 0, 126, 0, 3, 120,
                         114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108,
                         -32, -117, 2, 0, 0, 120, 112, 112, 112, 115, 114, 0, 35, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 105,
                         110, 116, 101, 114, 110, 97, 108, 46, 73, 110, 116, 65, 112, 102, 108, 111, 97, 116, 73, 109, 112, 108, -53, -46, 125,
                         34, -23, 16, 34, -76, 2, 0, 7, 74, 0, 8, 101, 120, 112, 111, 110, 101, 110, 116, 73, 0, 8, 104, 97, 115, 104, 67, 111,
                         100, 101, 73, 0, 13, 105, 110, 105, 116, 105, 97, 108, 68, 105, 103, 105, 116, 115, 74, 0, 9, 112, 114, 101, 99, 105,
                         115, 105, 111, 110, 73, 0, 5, 114, 97, 100, 105, 120, 73, 0, 4, 115, 105, 103, 110, 76, 0, 11, 100, 97, 116, 97, 83,
                         116, 111, 114, 97, 103, 101, 116, 0, 29, 76, 111, 114, 103, 47, 97, 112, 102, 108, 111, 97, 116, 47, 115, 112, 105,
                         47, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 59, 120, 114, 0, 32, 111, 114, 103, 46, 97, 112, 102, 108, 111,
                         97, 116, 46, 105, 110, 116, 101, 114, 110, 97, 108, 46, 73, 110, 116, 66, 97, 115, 101, 77, 97, 116, 104, 30, 42, 36,
                         -111, 88, -64, 50, -9, 2, 0, 1, 73, 0, 5, 114, 97, 100, 105, 120, 120, 112, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
                         0, 0, -128, 0, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 10, 0, 0, 0, 1, 115, 114, 0, 41, 111, 114, 103, 46, 97,
                         112, 102, 108, 111, 97, 116, 46, 105, 110, 116, 101, 114, 110, 97, 108, 46, 73, 110, 116, 77, 101, 109, 111, 114, 121,
                         68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 55, -4, -106, 47, -6, 125, -73, 87, 2, 0, 1, 91, 0, 4, 100, 97, 116,
                         97, 116, 0, 2, 91, 73, 120, 114, 0, 27, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 115, 112, 105, 46, 68,
                         97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 25, -41, 65, 37, -43, -67, -21, -93, 2, 0, 5, 90, 0, 10, 105, 115, 82,
                         101, 97, 100, 79, 110, 108, 121, 90, 0, 14, 105, 115, 83, 117, 98, 115, 101, 113, 117, 101, 110, 99, 101, 100, 74, 0,
                         6, 108, 101, 110, 103, 116, 104, 74, 0, 6, 111, 102, 102, 115, 101, 116, 76, 0, 19, 111, 114, 105, 103, 105, 110, 97,
                         108, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 113, 0, 126, 0, 7, 120, 112, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0,
                         0, 0, 0, 0, 0, 0, 0, 112, 117, 114, 0, 2, 91, 73, 77, -70, 96, 38, 118, -22, -78, -91, 2, 0, 0, 120, 112, 0, 0, 0, 2,
                         0, 0, 48, 57, 40, 119, 49, 32 };
        in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        a = (Apfloat) in.readObject();
        assertEquals("Legacy scale", 14, a.scale());
        assertEquals("Legacy size", 9, a.size());
        assertFalse("Legacy isOne", a.getImpl(a.precision()).isOne());

        // new Apfloat(1) in apfloat 1.0 format
        byte[] bytes2 = { -84, -19, 0, 5, 115, 114, 0, 19, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 102, 108, 111, 97,
                          116, -1, 125, -106, -56, -92, 28, 107, 73, 2, 0, 1, 76, 0, 4, 105, 109, 112, 108, 116, 0, 29, 76, 111, 114, 103, 47,
                          97, 112, 102, 108, 111, 97, 116, 47, 115, 112, 105, 47, 65, 112, 102, 108, 111, 97, 116, 73, 109, 112, 108, 59, 120,
                          114, 0, 21, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 65, 112, 99, 111, 109, 112, 108, 101, 120, 50,
                          -114, 76, 62, -100, 91, -70, -73, 2, 0, 2, 76, 0, 4, 105, 109, 97, 103, 116, 0, 21, 76, 111, 114, 103, 47, 97, 112,
                          102, 108, 111, 97, 116, 47, 65, 112, 102, 108, 111, 97, 116, 59, 76, 0, 4, 114, 101, 97, 108, 113, 0, 126, 0, 3, 120,
                          114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108,
                          -32, -117, 2, 0, 0, 120, 112, 112, 112, 115, 114, 0, 36, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 105,
                          110, 116, 101, 114, 110, 97, 108, 46, 76, 111, 110, 103, 65, 112, 102, 108, 111, 97, 116, 73, 109, 112, 108, -30, 36,
                          -29, 106, -22, 40, -77, 43, 2, 0, 7, 74, 0, 8, 101, 120, 112, 111, 110, 101, 110, 116, 73, 0, 8, 104, 97, 115, 104,
                          67, 111, 100, 101, 73, 0, 13, 105, 110, 105, 116, 105, 97, 108, 68, 105, 103, 105, 116, 115, 74, 0, 9, 112, 114, 101,
                          99, 105, 115, 105, 111, 110, 73, 0, 5, 114, 97, 100, 105, 120, 73, 0, 4, 115, 105, 103, 110, 76, 0, 11, 100, 97, 116,
                          97, 83, 116, 111, 114, 97, 103, 101, 116, 0, 29, 76, 111, 114, 103, 47, 97, 112, 102, 108, 111, 97, 116, 47, 115,
                          112, 105, 47, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 59, 120, 114, 0, 33, 111, 114, 103, 46, 97, 112, 102,
                          108, 111, 97, 116, 46, 105, 110, 116, 101, 114, 110, 97, 108, 46, 76, 111, 110, 103, 66, 97, 115, 101, 77, 97, 116,
                          104, -90, 56, -79, 77, -38, 28, -98, -104, 2, 0, 2, 68, 0, 11, 105, 110, 118, 101, 114, 115, 101, 66, 97, 115, 101,
                          73, 0, 5, 114, 97, 100, 105, 120, 120, 112, 60, 103, 14, -11, 70, 70, -44, -105, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 1,
                          0, 0, 0, 0, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 10, 0, 0, 0, 1, 115, 114, 0, 42, 111, 114, 103, 46, 97,
                          112, 102, 108, 111, 97, 116, 46, 105, 110, 116, 101, 114, 110, 97, 108, 46, 76, 111, 110, 103, 77, 101, 109, 111,
                          114, 121, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, -84, 74, -31, 104, 107, 113, 66, 75, 2, 0, 1, 91, 0, 4,
                          100, 97, 116, 97, 116, 0, 2, 91, 74, 120, 114, 0, 27, 111, 114, 103, 46, 97, 112, 102, 108, 111, 97, 116, 46, 115,
                          112, 105, 46, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 25, -41, 65, 37, -43, -67, -21, -93, 2, 0, 5, 90, 0,
                          10, 105, 115, 82, 101, 97, 100, 79, 110, 108, 121, 90, 0, 14, 105, 115, 83, 117, 98, 115, 101, 113, 117, 101, 110,
                          99, 101, 100, 74, 0, 6, 108, 101, 110, 103, 116, 104, 74, 0, 6, 111, 102, 102, 115, 101, 116, 76, 0, 19, 111, 114,
                          105, 103, 105, 110, 97, 108, 68, 97, 116, 97, 83, 116, 111, 114, 97, 103, 101, 113, 0, 126, 0, 7, 120, 112, 1, 0, 0,
                          0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 112, 117, 114, 0, 2, 91, 74, 120, 32, 4, -75, 18, -79, 117, -109, 2, 0,
                          0, 120, 112, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1 };
        in = new ObjectInputStream(new ByteArrayInputStream(bytes2));
        a = (Apfloat) in.readObject();
        assertEquals("Legacy 1 scale", 1, a.scale());
        assertEquals("Legacy 1 size", 1, a.size());
        assertTrue("Legacy 1 isOne", a.getImpl(a.precision()).isOne());
    }
}
