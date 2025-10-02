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

import org.jscience.mathematics.function.*;
import org.jscience.mathematics.vector.*;

import javolution.text.*;
import javolution.xml.*;
import javolution.xml.stream.*;
import junit.framework.TestSuite;

/**
 * @since 1.8.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class AprationalFieldTest
    extends ApfloatTestCase
{
    public AprationalFieldTest(String methodName)
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

        suite.addTest(new AprationalFieldTest("testBasic"));
        suite.addTest(new AprationalFieldTest("testXmlSerialization"));
        suite.addTest(new AprationalFieldTest("testMatrixInverse"));
        suite.addTest(new AprationalFieldTest("testRationalFunction"));

        return suite;
    }

    public static void testBasic()
    {
        AprationalField a = valueOf("100");
        AprationalField b = valueOf("200");
        AprationalField c = valueOf("200");
        AprationalField zero = valueOf("0");
        assertEquals("100 + 200", valueOf("300"), a.plus(b));
        assertEquals("-100", valueOf("-100"), a.opposite());
        assertEquals("100 - 200", valueOf("-100"), a.minus(b));
        assertEquals("100 * 200", valueOf("20000"), a.times(b));
        assertEquals("1 / 100", valueOf("1/100"), a.inverse());
        assertEquals("copy", a, a.copy());
        assertEquals("doubleValue", 100.0, a.doubleValue(), 0.00000000000005);
        assertEquals("longValue", 100L, a.longValue());
        assertEquals("String", "100", a.toString());
        assertEquals("Text", new Text("100"), a.toText());
        assertTrue("isLargerThan", b.isLargerThan(a));
        assertEquals("compareTo", -1, a.compareTo(b));
        HashSet<AprationalField> set = new HashSet<>();
        set.add(b);
        set.add(c);
        assertEquals("hashCode", 1, set.size());

        a = valueOf("2/7");
        b = valueOf("1/6");
        c = valueOf("1/6");
        assertEquals("2/7 + 1/6", valueOf("19/42"), a.plus(b));
        assertEquals("-2/7", valueOf("-2/7"), a.opposite());
        assertEquals("2/7 - 1/6", valueOf("5/42"), a.minus(b));
        assertEquals("2/7 * 1/6", valueOf("1/21"), a.times(b));
        assertEquals("1 / 2/7", valueOf("7/2"), a.inverse());
        assertEquals("copy", a, a.copy());
        assertEquals("doubleValue", 0.28571428571428571, a.doubleValue(), 0.0000000000000005);
        assertEquals("longValue", 0L, a.longValue());
        assertEquals("String", "2/7", a.toString());
        assertEquals("Text", new Text("2/7"), a.toText());
        assertTrue("isLargerThan", a.isLargerThan(b));
        assertEquals("compareTo", 1, a.compareTo(b));
        set = new HashSet<>();
        set.add(b);
        set.add(c);
        assertEquals("hashCode", 1, set.size());

        try
        {
            zero.inverse();
            fail("Zero divisor accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK, modulus is not set
        }

        try
        {
            new AprationalField(null);
            fail("null accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }
    }

    public static void testXmlSerialization()
        throws XMLStreamException
    {
        AprationalField field = new AprationalField(new Aprational("2/10", 13));
        StringWriter out = new StringWriter();

        XMLObjectWriter xmlWriter = XMLObjectWriter.newInstance(out);
        xmlWriter.write(field);
        xmlWriter.close();

        String xml = out.toString();
        assertEquals("XML", "<?xml version=\"1.0\" ?><org.apfloat.jscience.AprationalField numerator-mantissa=\"2\" numerator-exponent=\"0\" numerator-radix=\"13\" denominator-mantissa=\"1\" denominator-exponent=\"1\" denominator-radix=\"13\"/>", xml);

        StringReader in = new StringReader(xml);
        XMLObjectReader xmlReader = XMLObjectReader.newInstance(in);
        field = xmlReader.read();
        assertEquals("Value", new Aprational("2/10", 13), field.value());
        assertEquals("Radix", 13, field.value().radix());
    }

    public static void testMatrixInverse()
    {
        AprationalField[][] elements = { { valueOf("1"), valueOf("1/2"), valueOf("1/3"), valueOf("1/4"), valueOf("1/5") },
                                         { valueOf("1/2"), valueOf("1/3"), valueOf("1/4"), valueOf("1/5"), valueOf("1/6") },
                                         { valueOf("1/3"), valueOf("1/4"), valueOf("1/5"), valueOf("1/6"), valueOf("1/7") },
                                         { valueOf("1/4"), valueOf("1/5"), valueOf("1/6"), valueOf("1/7"), valueOf("1/8") },
                                         { valueOf("1/5"), valueOf("1/6"), valueOf("1/7"), valueOf("1/8"), valueOf("1/9") } };
        Matrix<AprationalField> m = DenseMatrix.valueOf(elements);
        Vector<AprationalField> vectorization = m.minus(m.inverse().inverse()).vectorization();
        for (int i = 0; i < vectorization.getDimension(); i++)
        {
            assertEquals("Element " + i, 0, vectorization.get(i).value().signum());
        }
    }

    public static void testRationalFunction()
    {
        Variable.Local<AprationalField> x = new Variable.Local<AprationalField>("x");
        Polynomial<AprationalField> dividend = Polynomial.valueOf(valueOf("1/2"), Term.valueOf(x, 2)).plus(Polynomial.valueOf(valueOf("2/3"), x));
        Polynomial<AprationalField> divisor = Polynomial.valueOf(valueOf("3/4"), x).plus(valueOf("5/6"));
        RationalFunction<AprationalField> function = RationalFunction.valueOf(dividend, divisor);
        assertEquals("Function value", valueOf("10/7"), function.evaluate(valueOf("2")));
        assertEquals("Function plus inverse, value", valueOf("149/70"), function.plus(function.inverse()).evaluate(valueOf("2")));
    }

    private static AprationalField valueOf(String value)
    {
        return new AprationalField(new Aprational(value));
    }
}
