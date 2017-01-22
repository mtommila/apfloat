/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class ApfloatFieldTest
    extends ApfloatTestCase
{
    public ApfloatFieldTest(String methodName)
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

        suite.addTest(new ApfloatFieldTest("testMatrixInverse"));
        suite.addTest(new ApfloatFieldTest("testRationalFunction"));
        suite.addTest(new ApfloatFieldTest("testBasic"));

        return suite;
    }

    public static void testBasic()
    {
        ApfloatField a = valueOf("100");
        ApfloatField b = valueOf("200");
        ApfloatField c = valueOf("200");
        ApfloatField zero = valueOf("0");
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
        HashSet<ApfloatField> set = new HashSet<ApfloatField>();
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
            new ApfloatField(null);
            fail("null accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }
    }

    public static void testMatrixInverse()
    {
        ApfloatField[][] elements = { { valueOf("1"), valueOf("2"), valueOf("3") },
                                      { valueOf("4"), valueOf("5"), valueOf("6") },
                                      { valueOf("7"), valueOf("8"), valueOf("10") } };
        Matrix<ApfloatField> m = DenseMatrix.valueOf(elements);
        Vector<ApfloatField> vectorization = m.minus(m.inverse().inverse()).vectorization();
        Apfloat sum = Apfloat.ZERO;
        for (int i = 0; i < vectorization.getDimension(); i++)
        {
            sum = sum.add(ApfloatMath.abs(vectorization.get(i).value()));
        }
        assertEquals("Double inverse error", Apfloat.ZERO, sum, new Apfloat(5e-25));
    }

    public static void testRationalFunction()
    {
        Variable.Local<ApfloatField> x = new Variable.Local<ApfloatField>("x");
        Polynomial<ApfloatField> dividend = Polynomial.valueOf(valueOf("2"), Term.valueOf(x, 2)).plus(Polynomial.valueOf(valueOf("3"), x));
        Polynomial<ApfloatField> divisor = Polynomial.valueOf(valueOf("5"), x).plus(valueOf("6"));
        RationalFunction<ApfloatField> function = RationalFunction.valueOf(dividend, divisor);
        assertEquals("Function value", new Apfloat("0.875"), function.evaluate(valueOf("2")).value(), new Apfloat("5e-30"));
        assertEquals("Function plus inverse, value", new Apfloat("2.017857142857142857142857142857"), function.plus(function.inverse()).evaluate(valueOf("2")).value(), new Apfloat("5e-29"));
    }

    private static ApfloatField valueOf(String value)
    {
        return new ApfloatField(new Apfloat(value, 30));
    }
}
