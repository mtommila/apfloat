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
        suite.addTest(new EllipticCurveGroupTest("testWeierstrass"));
        suite.addTest(new EllipticCurveGroupTest("testMontgomery"));
        suite.addTest(new EllipticCurveGroupTest("testEdwards"));
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

    public static void testWeierstrass()
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

    public static void testMontgomery()
    {
        EllipticCurveGroup.setMontgomeryParameters(new Apint(1), new Apint(486662), ApintMath.pow(new Apint(2), 255).subtract(new Apint(19)));
        assertEquals("a", new Apint("19298681539552699237261830834781317975544997444273427339909597334573241639236"), EllipticCurveGroup.getA());
        assertEquals("b", new Apint("55751746669818908907645289078257140818241103727901012315294400837956729358436"), EllipticCurveGroup.getB());

        EllipticCurveGroup.setMontgomeryParameters(new Apint(4, 5), new Apint(3, 5), new Apint(19, 5));
        assertEquals("a radix 5", new Apint(7, 5), EllipticCurveGroup.getA());
        assertEquals("b radix 5", new Apint(11, 5), EllipticCurveGroup.getB());
    }

    public static void testEdwards()
    {
        EllipticCurveGroup.setEdwardsParameters(new Apint(1), new Apint(-39081), ApintMath.pow(new Apint(2), 448).subtract(ApintMath.pow(new Apint(2), 224).subtract(new Apint(1))));
        assertEquals("a", new Apint("60569893691300574212443650657333711196136780057276505023457516598384360680560897723866365308223045494174073705153636446754458136389257"), EllipticCurveGroup.getA());
        assertEquals("b", new Apint("619158913288850314171646206719411270004953751696604273573121280783484575845733621177300623150724465051557197874903839233490085973713604"), EllipticCurveGroup.getB());

        EllipticCurveGroup.setEdwardsParameters(new Apint(3, 5), new Apint(4, 5), new Apint(19, 5));
        assertEquals("a radix 5", new Apint(13, 5), EllipticCurveGroup.getA());
        assertEquals("b radix 5", new Apint(15, 5), EllipticCurveGroup.getB());
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
