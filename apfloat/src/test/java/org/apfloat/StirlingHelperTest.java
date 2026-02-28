/*
 * MIT License
 *
 * Copyright (c) 2002-2026 Mikko Tommila
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

import java.util.Arrays;
import java.util.Iterator;

import junit.framework.TestSuite;

/**
 * @version 1.16.0
 * @author Mikko Tommila
 */

public class StirlingHelperTest
    extends ApfloatTestCase
{
    public StirlingHelperTest(String methodName)
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

        suite.addTest(new StirlingHelperTest("testStirlingS1"));
        suite.addTest(new StirlingHelperTest("testStirlingS1s"));
        suite.addTest(new StirlingHelperTest("testStirlingS2"));
        suite.addTest(new StirlingHelperTest("testStirlingS2s"));

        return suite;
    }

    public static void testStirlingS1()
    {
        Apint[][] s1 = { { new Apint(1),      new Apint(0),       new Apint(0),       new Apint(0),      new Apint(0),      new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),      new Apint(1),       new Apint(0),       new Apint(0),      new Apint(0),      new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),      new Apint(1),       new Apint(1),       new Apint(0),      new Apint(0),      new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),      new Apint(2),       new Apint(3),       new Apint(1),      new Apint(0),      new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),      new Apint(6),      new Apint(11),       new Apint(6),      new Apint(1),      new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),     new Apint(24),      new Apint(50),      new Apint(35),     new Apint(10),      new Apint(1),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),    new Apint(120),     new Apint(274),     new Apint(225),     new Apint(85),     new Apint(15),     new Apint(1),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),    new Apint(720),    new Apint(1764),    new Apint(1624),    new Apint(735),    new Apint(175),    new Apint(21),    new Apint(1),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0),   new Apint(5040),   new Apint(13068),   new Apint(13132),   new Apint(6769),   new Apint(1960),   new Apint(322),   new Apint(28),   new Apint(1),  new Apint(0), new Apint(0) },
                         { new Apint(0),  new Apint(40320),  new Apint(109584),  new Apint(118124),  new Apint(67284),  new Apint(22449),  new Apint(4536),  new Apint(546),  new Apint(36),  new Apint(1), new Apint(0) },
                         { new Apint(0), new Apint(362880), new Apint(1026576), new Apint(1172700), new Apint(723680), new Apint(269325), new Apint(63273), new Apint(9450), new Apint(870), new Apint(45), new Apint(1) } };
        for (int n = 0; n <= 10; n++)
        {
            for (int k = 0; k <= 10; k++)
            {
                Apint expected = ((n - k & 1) == 0 ? s1[n][k] : s1[n][k].negate());
                assertEquals(n + ", " + k + " long", expected, StirlingHelper.stirlingS1(n, k, 10));
                assertEquals(n + ", " + k + " Apint", expected, StirlingHelper.stirlingS1(new Apint(n, 10), new Apint(k, 10)));
            }
        }

        for (int n = 0; n <= 10; n++)
        {
            for (int k = 0; k < n; k++)
            {
                Apint polynomial = StirlingHelper.stirlingS1s(n, k, 10)[k],
                      explicit = StirlingHelper.stirlingS1explicit(new Apint(n, 10), new Apint(k, 10));
                assertEquals(n + ", " + k, polynomial, explicit);
            }
        }
        for (int n = 7; n <= 8; n++)
        {
            for (int k = 3; k <= 4; k++)
            {
                for (int radix = 2; radix <= 36; radix++)
                {
                    Apint polynomial = StirlingHelper.stirlingS1s(n, k, radix)[k],
                          explicit = StirlingHelper.stirlingS1explicit(new Apint(n, radix), new Apint(k, radix));
                    assertEquals(n + ", " + k + " radix " + radix, polynomial, explicit);
                }
            }
        }

        assertEquals("100, 10", new Apint("1125272380578944825172147216455781795628518533063359467753257922095126858974036298524724548786755944321693935796805099192994799803568595147738316800000000000"), StirlingHelper.stirlingS1(100, 10, 10));
        assertEquals("300, 100", new Apint("376909250555303638611862421669595629299790553420938832632592924942890660751370250214457503296322416916733897209447624680799370645144096776841719390488412801434329444287898264317052120934511082840851466159631369481514328168584553866824352230755557700949439903033856394647932264234370267124457115577994120519487925645526965247828284936443913270442392475687447437060939940000305840240973447750195430127631008926094955368144735285714667403646205820279353418227349682499248817684031195125487525657199631138816"), StirlingHelper.stirlingS1(300, 100, 10));
        assertEquals("1000000000, 999999990", new Apint("269114426708726621220760745452897154612245298734060542185922879550237580569254927873526690006658540928589623220874676340225200261694666828903794953257387866382575750000000"), StirlingHelper.stirlingS1(new Apint(1000000000), new Apint(999999990)));
        assertEquals("1000000000000000000, 999999999999999990", new Apint("269114445546737194565868882275132871220772018298048691881326701205315309375401877570764643964219882919948439047707077907959089748256234197435001475704012686535433431354926975173741158628175486104225494540797594720800884763920250884596949502186796348937778426348885206571596003019608936262425845759238971673581048581048200757575757575750000000000000000"), StirlingHelper.stirlingS1(new Apint(1000000000000000000L), new Apint(999999999999999990L)));
        assertEquals("100000000000000000000, 99999999999999999990", new Apint("2691144455467372132154999586640211640807728708526234567788506114854313639050049456058706275718912792112368031060240048764374778010480390265235848551097513131650505686376271045248514207729160025559702158190859904983458722849038152133795532523838820858515388283328107185167395996204092359329626418203282097596333706925372274330607664000876435485810485810482007575757575757575000000000000000000"), StirlingHelper.stirlingS1(new Apint("100000000000000000000"), new Apint("99999999999999999990")));
        assertEquals("100000000000000000000, 99999999999999999990 radix 15", new Apint("ddc9cac020c2d0ad66e871b2259dbede73917dbd4a94ea14cb16e04b4a7aa399990ed97ca2087cec422051a8cc67bede3349d1b4433b28903bda7e7220e72946855994651d935276075895861eeb4b78055cb6021514ca2b03c9b86b812755901acbd80b3b9cb6b22b7b01d82b12323923b40de6e8acd71ed612517842026d85920e63e7d6c5dbc92c28870e0a0d36bc7cb5ada0c89c3d893304d733c69d0976e56c9ba93d50", 15), StirlingHelper.stirlingS1(new Apint("1035749e25953d996a", 15), new Apint("1035749e25953d9960", 15)));

        assertEquals("100, 10 radix 2 long", new Apint("101001111101101001110001101001010100101010111100011011011101110011010110000100001110011110101111100001101100110000000100101111100011101010000010111111011010100100001110010110001110111001111011001001001001110001001100001110011010110011110100001001100110111000110010010010011011101100101011001101001111011110111010000101000111010101001100010000111000001000101001110101011100000100001101010100001000010111000010101111101101111101111101011111111111000001010000000100000000000000000000000000000000000000000000000000000000000", 2), StirlingHelper.stirlingS1(100, 10, 2));
        assertEquals("100, 10 radix 2 Apint", new Apint("101001111101101001110001101001010100101010111100011011011101110011010110000100001110011110101111100001101100110000000100101111100011101010000010111111011010100100001110010110001110111001111011001001001001110001001100001110011010110011110100001001100110111000110010010010011011101100101011001101001111011110111010000101000111010101001100010000111000001000101001110101011100000100001101010100001000010111000010101111101101111101111101011111111111000001010000000100000000000000000000000000000000000000000000000000000000000", 2), StirlingHelper.stirlingS1(new Apint(100, 2), new Apint(10, 2)));

        try
        {
            StirlingHelper.stirlingS1(new Apint("100000000000000000000", 15), new Apint("99999999999999999990", 15));
            fail("Overflow should have occurred");
        }
        catch (OverflowException oe)
        {
            // OK; result would overflow
            assertEquals("Localization key", "overflow", oe.getLocalizationKey());
        }

        try
        {
            StirlingHelper.stirlingS1(-1, 0, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS1(0, -1, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS1(-1, -1, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS1(new Apint(-1), new Apint(0));
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS1(new Apint(0), new Apint(-1));
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS1(new Apint(-1), new Apint(-1));
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
    }

    public static void testStirlingS1s()
    {
        assertEquals("0", Arrays.asList(new Apint(1)), Arrays.asList(StirlingHelper.stirlingS1s(0, 10)));
        assertEquals("7", Arrays.asList(new Apint(0), new Apint(720), new Apint(-1764), new Apint(1624), new Apint(-735), new Apint(175), new Apint(-21), new Apint(1)), Arrays.asList(StirlingHelper.stirlingS1s(7, 10)));
        assertEquals("7 radix 3", Arrays.asList(new Apint(0, 3), new Apint(720, 3), new Apint(-1764, 3), new Apint(1624, 3), new Apint(-735, 3), new Apint(175, 3), new Apint(-21, 3), new Apint(1, 3)), Arrays.asList(StirlingHelper.stirlingS1s(7, 3)));

        try
        {
            StirlingHelper.stirlingS1s(-1, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
    }

    public static void testStirlingS2()
    {
        Apint[][] s2 = { { new Apint(1), new Apint(0),   new Apint(0),    new Apint(0),     new Apint(0),     new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1),   new Apint(0),    new Apint(0),     new Apint(0),     new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1),   new Apint(1),    new Apint(0),     new Apint(0),     new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1),   new Apint(3),    new Apint(1),     new Apint(0),     new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1),   new Apint(7),    new Apint(6),     new Apint(1),     new Apint(0),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1),  new Apint(15),   new Apint(25),    new Apint(10),     new Apint(1),     new Apint(0),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1),  new Apint(31),   new Apint(90),    new Apint(65),    new Apint(15),     new Apint(1),    new Apint(0),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1),  new Apint(63),  new Apint(301),   new Apint(350),   new Apint(140),    new Apint(21),    new Apint(1),   new Apint(0),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1), new Apint(127),  new Apint(966),  new Apint(1701),  new Apint(1050),   new Apint(266),   new Apint(28),   new Apint(1),  new Apint(0), new Apint(0) },
                         { new Apint(0), new Apint(1), new Apint(255), new Apint(3025),  new Apint(7770),  new Apint(6951),  new Apint(2646),  new Apint(462),  new Apint(36),  new Apint(1), new Apint(0) },
                         { new Apint(0), new Apint(1), new Apint(511), new Apint(9330), new Apint(34105), new Apint(42525), new Apint(22827), new Apint(5880), new Apint(750), new Apint(45), new Apint(1) } };
        for (int n = 8; n <= 10; n++)
        {
            for (int k = 1; k <= 10; k++)
            {
                assertEquals(n + ", " + k + " long", s2[n][k], StirlingHelper.stirlingS2(n, k, 10));
                assertEquals(n + ", " + k + " Apint", s2[n][k], StirlingHelper.stirlingS2(new Apint(n), new Apint(k)));
            }
        }

        assertEquals("100, 10", new Apint("2754999986711164035029356262910003922476368243643133591265713197865860436127311130380917269755"), StirlingHelper.stirlingS2(100, 10, 10));

        assertEquals("100, 10 radix 2 long", new Apint("10101001000011101001000101011010000111011010100111101111010011000000000111011111010101011101101010010000111100011101100011101011111001001000110000010011011100111010010101011010100001011111001000001111111111111111101101101111010110010111100100001110000111010011100000000001101000000010000100000111110010011111011", 2), StirlingHelper.stirlingS2(100, 10, 2));
        assertEquals("100, 10 radix 2 Apint", new Apint("10101001000011101001000101011010000111011010100111101111010011000000000111011111010101011101101010010000111100011101100011101011111001001000110000010011011100111010010101011010100001011111001000001111111111111111101101101111010110010111100100001110000111010011100000000001101000000010000100000111110010011111011", 2), StirlingHelper.stirlingS2(new Apint(100, 2), new Apint(10, 2)));

        try
        {
            StirlingHelper.stirlingS2(new Apint("100000000000000000000"), new Apint("99999999999999999990"));
            fail("Overflow should have occurred");
        }
        catch (OverflowException oe)
        {
            // OK; result would overflow
            assertEquals("Localization key", "overflow", oe.getLocalizationKey());
        }

        try
        {
            StirlingHelper.stirlingS2(-1, 0, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS2(0, -1, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS2(-1, -1, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS2(new Apint(-1), new Apint(0));
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS2(new Apint(0), new Apint(-1));
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS2(new Apint(-1), new Apint(-1));
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
    }

    public static void testStirlingS2s()
    {
        Iterator<Apint> s2 = StirlingHelper.stirlingS2s(0, 10);
        assertEquals("(0, 0)", new Apint(1), s2.next());
        assertFalse("0", s2.hasNext());

        s2 = StirlingHelper.stirlingS2s(1, 10);
        assertEquals("(1, 0)", new Apint(0), s2.next());
        assertEquals("(1, 1)", new Apint(1), s2.next());
        assertFalse("1", s2.hasNext());

        s2 = StirlingHelper.stirlingS2s(7, 10);
        assertEquals("(7, 0)", new Apint(0), s2.next());
        assertEquals("(7, 1)", new Apint(1), s2.next());
        assertEquals("(7, 2)", new Apint(63), s2.next());
        assertEquals("(7, 3)", new Apint(301), s2.next());
        assertEquals("(7, 4)", new Apint(350), s2.next());
        assertEquals("(7, 5)", new Apint(140), s2.next());
        assertEquals("(7, 6)", new Apint(21), s2.next());
        assertEquals("(7, 7)", new Apint(1), s2.next());
        assertFalse("7", s2.hasNext());

        s2 = StirlingHelper.stirlingS2s(7, 3);
        assertEquals("(7, 0) radix 3", new Apint(0, 3), s2.next());
        assertEquals("(7, 1) radix 3", new Apint(1, 3), s2.next());
        assertEquals("(7, 2) radix 3", new Apint(63, 3), s2.next());
        assertEquals("(7, 3) radix 3", new Apint(301, 3), s2.next());
        assertEquals("(7, 4) radix 3", new Apint(350, 3), s2.next());
        assertEquals("(7, 5) radix 3", new Apint(140, 3), s2.next());
        assertEquals("(7, 6) radix 3", new Apint(21, 3), s2.next());
        assertEquals("(7, 7) radix 3", new Apint(1, 3), s2.next());
        assertFalse("7 radix 3", s2.hasNext());

        try
        {
            StirlingHelper.stirlingS2s(-1, 10);
            fail("Illegal argument accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK; illegal
        }
        try
        {
            StirlingHelper.stirlingS2s(Integer.MAX_VALUE, 10);
            fail("Maximum array size exceeded");
        }
        catch (ApfloatRuntimeException are)
        {
            // OK; doesn't fit in an array
            assertEquals("Localization key", "maximumArraySizeExceeded", are.getLocalizationKey());
        }
    }
}
