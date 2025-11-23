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
package org.apfloat.jscience;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;

import org.apfloat.*;

import javolution.text.*;
import javolution.xml.*;
import javolution.xml.stream.*;
import junit.framework.TestSuite;

/**
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class EllipticCurveGroupTest
    extends ApfloatTestCase
{
    public EllipticCurveGroupTest(String methodName)
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

        suite.addTest(new EllipticCurveGroupTest("testBasic"));
        suite.addTest(new EllipticCurveGroupTest("testOpposite"));
        suite.addTest(new EllipticCurveGroupTest("testPlus"));
        suite.addTest(new EllipticCurveGroupTest("testTimes"));
        suite.addTest(new EllipticCurveGroupTest("testFromMontgomeryPoint"));
        suite.addTest(new EllipticCurveGroupTest("testFromEdwardsPoint"));
        suite.addTest(new EllipticCurveGroupTest("testSetWeierstrassParameters"));
        suite.addTest(new EllipticCurveGroupTest("testSetMontgomeryParameters"));
        suite.addTest(new EllipticCurveGroupTest("testSetEdwardsParameters"));
        suite.addTest(new EllipticCurveGroupTest("testJInvariant"));
        suite.addTest(new EllipticCurveGroupTest("testWeierstrassAliceBob"));
        suite.addTest(new EllipticCurveGroupTest("testMontgomeryAliceBob"));
        suite.addTest(new EllipticCurveGroupTest("testEdwardsAliceBob"));
        suite.addTest(new EllipticCurveGroupTest("testXmlSerialization"));

        return suite;
    }

    public static void testBasic()
    {
        EllipticCurveGroup.setWeierstrassParameters(new Apint(3), new Apint(4), new Apint(19));
        EllipticCurveGroup p = valueOf(5, 12);
        EllipticCurveGroup q = valueOf(7, 8);
        EllipticCurveGroup r = valueOf(7, 8);
        EllipticCurveGroup zero = EllipticCurveGroup.O;
        assertFalse("opposite", p.equals(p.opposite()));
        assertFalse("object", p.equals(new Object()));
        assertEquals("copy", p, p.copy());
        assertEquals("copy zero", zero, zero.copy());
        assertEquals("String", "(5, 12)", p.toString());
        assertEquals("String zero", "(∞, ∞)", zero.toString());
        assertEquals("Text", new Text("(5, 12)"), p.toText());
        HashSet<EllipticCurveGroup> set = new HashSet<>();
        set.add(q);
        set.add(r);
        assertEquals("hashCode", 1, set.size());
        set.add(zero);
        assertEquals("hashCode zero", 2, set.size());

        try
        {
            new EllipticCurveGroup(null, null);
            fail("null accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }
    }

    public static void testOpposite()
    {
        EllipticCurveGroup.setWeierstrassParameters(new Apint(3), new Apint(4), new Apint(19));
        EllipticCurveGroup p = valueOf(5, 12);
        EllipticCurveGroup zero = EllipticCurveGroup.O;
        assertEquals("-(5, 12)", valueOf(5, 7), p.opposite());
        assertEquals("-O", zero, zero.opposite());
    }

    public static void testPlus()
    {
        EllipticCurveGroup.setWeierstrassParameters(new Apint(3), new Apint(4), new Apint(19));
        EllipticCurveGroup p = valueOf(5, 12);
        EllipticCurveGroup q = valueOf(7, 8);
        EllipticCurveGroup zero = EllipticCurveGroup.O;
        assertEquals("(5, 12) + O", valueOf(5, 12), p.plus(zero));
        assertEquals("O + (5, 12)", valueOf(5, 12), zero.plus(p));
        assertEquals("(5, 12) + (7, 8)", valueOf(11, 0), p.plus(q));
        assertEquals("(5, 12) + (5, 12)", valueOf(16, 14), p.plus(p));
        assertEquals("(5, 12) - (5, 12)", zero, p.plus(p.opposite()));

        EllipticCurveGroup.setWeierstrassParameters(new Apint(3, 3), new Apint(4, 3), new Apint(19, 3));
        assertEquals("(5, 12) + (7, 8) radix 2", new EllipticCurveGroup(new Apint(11, 3), new Apint(0, 3)), new EllipticCurveGroup(new Apint(5, 3), new Apint(12, 3)).plus(new EllipticCurveGroup(new Apint(7, 3), new Apint(8, 3))));
        assertEquals("(5, 12) + (5, 12) radix 2", new EllipticCurveGroup(new Apint(16, 3), new Apint(14, 3)), new EllipticCurveGroup(new Apint(5, 3), new Apint(12, 3)).plus(new EllipticCurveGroup(new Apint(5, 3), new Apint(12, 3))));
    }

    public static void testTimes()
    {
        EllipticCurveGroup.setWeierstrassParameters(new Apint(3), new Apint(4), new Apint(19));
        EllipticCurveGroup p = valueOf(5, 12);
        EllipticCurveGroup zero = EllipticCurveGroup.O;
        assertEquals("O * 5", zero, zero.times(new Apint(5)));
        assertEquals("(5, 12) * 0", zero, p.times(new Apint(0)));
        assertEquals("(5, 12) * 1", valueOf(5, 12), p.times(new Apint(1)));
        assertEquals("(5, 12) * -1", valueOf(5, 7), p.times(new Apint(-1)));
        assertEquals("(5, 12) * 4", valueOf(15, 2), p.times(new Apint(4)));
        assertEquals("(5, 12) * -4", valueOf(15, 17), p.times(new Apint(-4)));
        assertEquals("(5, 12) * 5", valueOf(0, 2), p.times(new Apint(5)));

        EllipticCurveGroup.setWeierstrassParameters(new Apint(3, 3), new Apint(4, 3), new Apint(19, 3));
        assertEquals("(5, 12) * 5 radix 2", new EllipticCurveGroup(new Apint(0, 3), new Apint(2, 3)), new EllipticCurveGroup(new Apint(5, 3), new Apint(12, 3)).times(new Apint(5, 3)));
    }

    public static void testFromMontgomeryPoint()
    {
        EllipticCurveGroup.setMontgomeryParameters(new Apint(4, 5), new Apint(3, 5), new Apint(19, 5));
        EllipticCurveGroup e = EllipticCurveGroup.fromMontgomeryPoint(new Apint(3, 5), new Apint(5, 5));
        assertEquals("x radix 5", new Apint(1, 5), e.getX());
        assertEquals("y radix 5", new Apint(6, 5), e.getY());

        try
        {
            EllipticCurveGroup.setWeierstrassParameters(new Apint(3), new Apint(4), new Apint(19));
            EllipticCurveGroup.fromMontgomeryPoint(new Apint(3), new Apint(5));
            fail("Unset parameters accepted");
        }
        catch (IllegalStateException ise)
        {
            // OK, invalid
        }
    }

    public static void testFromEdwardsPoint()
    {
        EllipticCurveGroup.setEdwardsParameters(new Apint(3, 5), new Apint(4, 5), new Apint(19, 5));
        EllipticCurveGroup e = EllipticCurveGroup.fromEdwardsPoint(new Apint(1, 5), new Apint(8, 5));
        assertEquals("x radix 5", new Apint(18, 5), e.getX());
        assertEquals("y radix 5", new Apint(1, 5), e.getY());

        try
        {
            EllipticCurveGroup.setWeierstrassParameters(new Apint(3), new Apint(4), new Apint(19));
            EllipticCurveGroup.fromEdwardsPoint(Apint.ONE, new Apint(8));
            fail("Unset parameters accepted");
        }
        catch (IllegalStateException ise)
        {
            // OK, invalid
        }
    }

    public static void testSetWeierstrassParameters()
    {
        EllipticCurveGroup.setWeierstrassParameters(new Apint(486662), new Apint(1), ApintMath.pow(new Apint(2), 255).subtract(new Apint(19)));
        assertEquals("a", new Apint("486662"), EllipticCurveGroup.getA());
        assertEquals("b", new Apint("1"), EllipticCurveGroup.getB());

        try
        {
            EllipticCurveGroup.setWeierstrassParameters(new Apint(5), new Apint(6), new Apint(19));
            fail("Invalid parameters accepted");
        }
        catch (IllegalArgumentException iae)
        {
            // OK, invalid
        }
    }

    public static void testSetMontgomeryParameters()
    {
        EllipticCurveGroup.setMontgomeryParameters(new Apint(4, 5), new Apint(3, 5), new Apint(19, 5));
        assertEquals("a radix 5", new Apint(7, 5), EllipticCurveGroup.getA());
        assertEquals("b radix 5", new Apint(11, 5), EllipticCurveGroup.getB());
    }

    public static void testSetEdwardsParameters()
    {
        EllipticCurveGroup.setEdwardsParameters(new Apint(3, 5), new Apint(4, 5), new Apint(19, 5));
        assertEquals("a radix 5", new Apint(13, 5), EllipticCurveGroup.getA());
        assertEquals("b radix 5", new Apint(15, 5), EllipticCurveGroup.getB());
    }

    public static void testJInvariant()
    {
        Apint q = ApintMath.pow(new Apint(2), 255).subtract(new Apint(19));
        EllipticCurveGroup.setMontgomeryParameters(new Apint(1), new Apint(486662), q);
        Apint curve22519J = EllipticCurveGroup.getJInvariant();
        EllipticCurveGroup.setEdwardsParameters(new Apint(-1), new ModuloApintField(new Apint(-121665)).times(new ModuloApintField(new Apint(121666)).inverse()).value(), q);
        Apint ed22519J = EllipticCurveGroup.getJInvariant();
        assertEquals("Curve22519 == Ed25519 j-invariant", curve22519J, ed22519J);
    }

    public static void testWeierstrassAliceBob()
    {
        // P-521
        // Curve parameters and g are public
        EllipticCurveGroup.setWeierstrassParameters(new Apint("1fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc", 16),
                                                    new Apint("51953eb9618e1c9a1f929a21a0b68540eea2da725b99b315f3b8b489918ef109e156193951ec7e937b1652c0bd3bb1bf073573df883d2c34f1ef451fd46b503f00", 16),
                                                    new Apint("1ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16));
        EllipticCurveGroup g = new EllipticCurveGroup(new Apint("c6858e06b70404e9cd9e3ecb662395b4429c648139053fb521f828af606b4d3dbaa14b5e77efe75928fe1dc127a2ffa8de3348b3c1856a429bf97e7e31c2e5bd66", 16),
                                                      new Apint("11839296a789a3bc0045c8a5fb42c7d1bd998f54449579b446817afbd17273e662c97ee72995ef42640c550b9013fad0761353c7086a272c24088be94769fd16650", 16));
        // Private keys, generated as random
        Apint alicePrivateKey = ApintMath.random(130, 16);
        Apint bobPrivateKey = ApintMath.random(130, 16);
        // Key exchange
        EllipticCurveGroup alicePublicKey = g.times(alicePrivateKey);
        EllipticCurveGroup bobPublicKey = g.times(bobPrivateKey);
        // Shared secret
        EllipticCurveGroup aliceSharedSecret = bobPublicKey.times(alicePrivateKey);
        EllipticCurveGroup bobSharedSecret = alicePublicKey.times(bobPrivateKey);
        assertEquals("Alice shared secret == Bob shared secret", aliceSharedSecret, bobSharedSecret);
    }

    public static void testMontgomeryAliceBob()
    {
        // Curve25519
        // Curve parameters and g are public
        EllipticCurveGroup.setMontgomeryParameters(new Apint("1", 16),
                                                    new Apint("76d06", 16),
                                                    new Apint("7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffed", 16));
        EllipticCurveGroup g = EllipticCurveGroup.fromMontgomeryPoint(new Apint("9", 16),
                                                                      new Apint("20ae19a1b8a086b4e01edd2c7748d14c923d4d7e6d7c61b229e9c5a27eced3d9", 16));
        // Private keys, generated as random
        Apint alicePrivateKey = ApintMath.random(63, 16);
        Apint bobPrivateKey = ApintMath.random(63, 16);
        // Key exchange
        EllipticCurveGroup alicePublicKey = g.times(alicePrivateKey);
        EllipticCurveGroup bobPublicKey = g.times(bobPrivateKey);
        // Shared secret
        EllipticCurveGroup aliceSharedSecret = bobPublicKey.times(alicePrivateKey);
        EllipticCurveGroup bobSharedSecret = alicePublicKey.times(bobPrivateKey);
        assertEquals("Alice shared secret == Bob shared secret", aliceSharedSecret, bobSharedSecret);
    }

    public static void testEdwardsAliceBob()
    {
        // Ed448
        // Curve parameters and g are public
        EllipticCurveGroup.setEdwardsParameters(new Apint("1", 16),
                                                new Apint("d78b4bdc7f0daf19f24f38c29373a2ccad46157242a50f37809b1da3412a12e79ccc9c81264cfe9ad080997058fb61c4243cc32dbaa156b9", 16),
                                                new Apint("fffffffffffffffffffffffffffffffffffffffffffffffffffffffeffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16));
        EllipticCurveGroup g = EllipticCurveGroup.fromEdwardsPoint(new Apint("79a70b2b70400553ae7c9df416c792c61128751ac92969240c25a07d728bdc93e21f7787ed6972249de732f38496cd11698713093e9c04fc", 16),
                                                                   new Apint("7fffffffffffffffffffffffffffffffffffffffffffffffffffffff80000000000000000000000000000000000000000000000000000001", 16));
        // Private keys, generated as random
        Apint alicePrivateKey = ApintMath.random(111, 16);
        Apint bobPrivateKey = ApintMath.random(111, 16);
        // Key exchange
        EllipticCurveGroup alicePublicKey = g.times(alicePrivateKey);
        EllipticCurveGroup bobPublicKey = g.times(bobPrivateKey);
        // Shared secret
        EllipticCurveGroup aliceSharedSecret = bobPublicKey.times(alicePrivateKey);
        EllipticCurveGroup bobSharedSecret = alicePublicKey.times(bobPrivateKey);
        assertEquals("Alice shared secret == Bob shared secret", aliceSharedSecret, bobSharedSecret);
    }

    public static void testXmlSerialization()
        throws XMLStreamException
    {
        EllipticCurveGroup.setWeierstrassParameters(new Apint(486662, 36), new Apint(1, 36), ApintMath.pow(new Apint(2, 36), 255).subtract(new Apint(19, 36)));
        EllipticCurveGroup group = new EllipticCurveGroup(new Apint("12ez0", 36), new Apint("1", 36));
        StringWriter out = new StringWriter();

        XMLObjectWriter xmlWriter = XMLObjectWriter.newInstance(out);
        xmlWriter.write(group);
        xmlWriter.close();

        String xml = out.toString();
        assertEquals("XML", "<?xml version=\"1.0\" ?><org.apfloat.jscience.EllipticCurveGroup x-mantissa=\"12ez\" x-exponent=\"1\" x-radix=\"36\" y-mantissa=\"1\" y-exponent=\"0\" y-radix=\"36\"/>", xml);

        StringReader in = new StringReader(xml);
        XMLObjectReader xmlReader = XMLObjectReader.newInstance(in);
        group = xmlReader.read();
        assertEquals("x", new Apint("12ez0", 36), group.getX());
        assertEquals("y", new Apint("1", 36), group.getY());
        assertEquals("Radix", 36, group.getX().radix());

        EllipticCurveGroup.setEdwardsParameters(new Apint(5, 2), new Apint(6, 2), new Apint(7, 2));
        group = new EllipticCurveGroup(new Apint("0", 2), new Apint("0", 2));
        out = new StringWriter();

        xmlWriter = XMLObjectWriter.newInstance(out);
        xmlWriter.write(group);
        xmlWriter.close();

        xml = out.toString();
        assertEquals("XML 0", "<?xml version=\"1.0\" ?><org.apfloat.jscience.EllipticCurveGroup x-mantissa=\"0\" x-exponent=\"0\" x-radix=\"2\" y-mantissa=\"0\" y-exponent=\"0\" y-radix=\"2\"/>", xml);

        in = new StringReader(xml);
        xmlReader = XMLObjectReader.newInstance(in);
        group = xmlReader.read();
        assertEquals("x", new Apint("0", 2), group.getX());
        assertEquals("y", new Apint("0", 2), group.getY());
        assertEquals("Radix", 2, group.getX().radix());
    }

    private static EllipticCurveGroup valueOf(int x, int y)
    {
        return new EllipticCurveGroup(new Apint(x), new Apint(y));
    }
}
