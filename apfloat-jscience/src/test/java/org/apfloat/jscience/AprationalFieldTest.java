/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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
 * @version 1.9.0
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

        suite.addTest(new AprationalFieldTest("testMatrixInverse"));
        suite.addTest(new AprationalFieldTest("testRationalFunction"));
        suite.addTest(new AprationalFieldTest("testBasic"));

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
            new AprationalField(null);
            fail("null accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }
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
