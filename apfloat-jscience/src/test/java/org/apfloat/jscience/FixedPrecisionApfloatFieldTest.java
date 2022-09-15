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

public class FixedPrecisionApfloatFieldTest
    extends ApfloatTestCase
{
    public FixedPrecisionApfloatFieldTest(String methodName)
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

        suite.addTest(new FixedPrecisionApfloatFieldTest("testBasic"));
        suite.addTest(new FixedPrecisionApfloatFieldTest("testMatrixInverse"));
        suite.addTest(new FixedPrecisionApfloatFieldTest("testRationalFunction"));

        return suite;
    }

    public static void testBasic()
    {
        FixedPrecisionApfloatField a = valueOf("100");
        FixedPrecisionApfloatField b = valueOf("200");
        FixedPrecisionApfloatField c = valueOf("200");
        FixedPrecisionApfloatField zero = valueOf("0");
        assertEquals("100 + 200", valueOf("300"), a.plus(b));
        assertEquals("-100", valueOf("-100"), a.opposite());
        assertEquals("100 - 200", valueOf("-100"), a.minus(b));
        assertEquals("100 * 200", valueOf("20000"), a.times(b));
        assertEquals("1 / 100", valueOf("0.01"), a.inverse());
        assertEquals("copy", a, a.copy());
        assertEquals("doubleValue", 100.0, a.doubleValue(), 0.00000000000005);
        assertEquals("longValue", 100L, a.longValue());
        assertEquals("String", "1e2", a.toString());
        assertEquals("Text", new Text("1e2"), a.toText());
        assertTrue("isLargerThan", b.isLargerThan(a));
        assertEquals("compareTo", -1, a.compareTo(b));
        HashSet<FixedPrecisionApfloatField> set = new HashSet<>();
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
            new FixedPrecisionApfloatField(null, HELPER);
            fail("null value accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }

        try
        {
            new FixedPrecisionApfloatField(Apcomplex.ZERO, null);
            fail("null helper accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }
    }

    public static void testMatrixInverse()
    {
        FixedPrecisionApfloatField[][] elements = { { valueOf("1"), valueOf("2"), valueOf("3") },
                                                    { valueOf("4"), valueOf("5"), valueOf("6") },
                                                    { valueOf("7"), valueOf("8"), valueOf("10") } };
        Matrix<FixedPrecisionApfloatField> m = DenseMatrix.valueOf(elements);
        Vector<FixedPrecisionApfloatField> vectorization = m.minus(m.inverse().inverse()).vectorization();
        Apfloat sum = Apfloat.ZERO;
        for (int i = 0; i < vectorization.getDimension(); i++)
        {
            sum = sum.add(ApfloatMath.abs(vectorization.get(i).value()));
        }
        assertEquals("Double inverse error", Apfloat.ZERO, sum, new Apfloat(5e-28));
    }

    public static void testRationalFunction()
    {
        Variable.Local<FixedPrecisionApfloatField> x = new Variable.Local<FixedPrecisionApfloatField>("x");
        Polynomial<FixedPrecisionApfloatField> dividend = Polynomial.valueOf(valueOf("2"), Term.valueOf(x, 2)).plus(Polynomial.valueOf(valueOf("3"), x));
        Polynomial<FixedPrecisionApfloatField> divisor = Polynomial.valueOf(valueOf("5"), x).plus(valueOf("6"));
        RationalFunction<FixedPrecisionApfloatField> function = RationalFunction.valueOf(dividend, divisor);
        assertEquals("Function value", new Apfloat("0.875"), function.evaluate(valueOf("2")).value(), new Apfloat("5e-30"));
        assertEquals("Function plus inverse, value", new Apfloat("2.017857142857142857142857142857"), function.plus(function.inverse()).evaluate(valueOf("2")).value(), new Apfloat("5e-29"));
    }

    private static FixedPrecisionApfloatField valueOf(String value)
    {
        return new FixedPrecisionApfloatField(new Apfloat(value), HELPER);
    }

    private static final FixedPrecisionApfloatHelper HELPER = new FixedPrecisionApfloatHelper(30);
}
