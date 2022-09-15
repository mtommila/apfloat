/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
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

import java.util.HashSet;

import org.apfloat.*;

import org.jscience.mathematics.function.*;
import org.jscience.mathematics.vector.*;

import javolution.text.*;

import junit.framework.TestSuite;

/**
 * @since 1.8.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class FixedPrecisionApcomplexFieldTest
    extends ApfloatTestCase
{
    public FixedPrecisionApcomplexFieldTest(String methodName)
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

        suite.addTest(new FixedPrecisionApcomplexFieldTest("testBasic"));
        suite.addTest(new FixedPrecisionApcomplexFieldTest("testMatrixInverse"));
        suite.addTest(new FixedPrecisionApcomplexFieldTest("testRationalFunction"));

        return suite;
    }

    public static void testBasic()
    {
        FixedPrecisionApcomplexField a = valueOf("(0,100)");
        FixedPrecisionApcomplexField b = valueOf("(0,200)");
        FixedPrecisionApcomplexField c = valueOf("(0,200)");
        FixedPrecisionApcomplexField zero = valueOf("0");
        assertEquals("100 + 200", valueOf("(0,300)"), a.plus(b));
        assertEquals("-100", valueOf("(0,-100)"), a.opposite());
        assertEquals("100 - 200", valueOf("(0,-100)"), a.minus(b));
        assertEquals("100 * 200", valueOf("-20000"), a.times(b));
        assertEquals("1 / 100", valueOf("(0,-0.01)"), a.inverse());
        assertEquals("copy", a, a.copy());
        assertEquals("doubleValue", 0.0, a.doubleValue());
        assertEquals("longValue", 0L, a.longValue());
        assertEquals("String", "(0, 1e2)", a.toString());
        assertEquals("Text", new Text("(0, 1e2)"), a.toText());
        assertTrue("isLargerThan", b.isLargerThan(a));
        assertEquals("compareTo", -1, a.compareTo(b));
        HashSet<FixedPrecisionApcomplexField> set = new HashSet<>();
        set.add(b);
        set.add(c);
        assertEquals("hashCode", 1, set.size());

        try
        {
            zero.inverse();
            fail("Zero divisor accepted");
        }
        catch (ArithmeticException ae)
        {
            // OK, modulus is not set
        }

        try
        {
            new FixedPrecisionApcomplexField(null, HELPER);
            fail("null value accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }

        try
        {
            new FixedPrecisionApcomplexField(Apcomplex.ZERO, null);
            fail("null helper accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }
    }

    public static void testMatrixInverse()
    {
        FixedPrecisionApcomplexField[][] elements = { { valueOf("1"), valueOf("(0,2)"), valueOf("3") },
                                                      { valueOf("4"), valueOf("5"), valueOf("(0,6)") },
                                                      { valueOf("(0,7)"), valueOf("8"), valueOf("9") } };
        Matrix<FixedPrecisionApcomplexField> m = DenseMatrix.valueOf(elements);
        Vector<FixedPrecisionApcomplexField> vectorization = m.minus(m.inverse().inverse()).vectorization();
        Apfloat sum = Apfloat.ZERO;
        for (int i = 0; i < vectorization.getDimension(); i++)
        {
            sum = sum.add(ApcomplexMath.abs(vectorization.get(i).value()));
        }
        assertEquals("Double inverse error", Apfloat.ZERO, sum, new Apfloat(5e-28));
    }

    public static void testRationalFunction()
    {
        Variable.Local<FixedPrecisionApcomplexField> x = new Variable.Local<FixedPrecisionApcomplexField>("x");
        Polynomial<FixedPrecisionApcomplexField> dividend = Polynomial.valueOf(valueOf("(1,2)"), Term.valueOf(x, 2)).plus(Polynomial.valueOf(valueOf("(3,4)"), x));
        Polynomial<FixedPrecisionApcomplexField> divisor = Polynomial.valueOf(valueOf("(5,6)"), x).plus(valueOf("(7,8)"));
        RationalFunction<FixedPrecisionApcomplexField> function = RationalFunction.valueOf(dividend, divisor);
        assertEquals("Function value", new Apcomplex("(0.570962479608482871125611745514,0.983686786296900489396411092985)"), function.evaluate(valueOf("(2,3)")).value(), new Apfloat("5e-30"));
        assertEquals("Function plus inverse, value", new Apcomplex("(1.01232439638023570845221956392,0.22328325540156631537371248012)"), function.plus(function.inverse()).evaluate(valueOf("(2,3)")).value(), new Apfloat("5e-29"));
    }

    private static FixedPrecisionApcomplexField valueOf(String value)
    {
        return new FixedPrecisionApcomplexField(new Apcomplex(value), HELPER);
    }

    private static final FixedPrecisionApcomplexHelper HELPER = new FixedPrecisionApcomplexHelper(30);
}
