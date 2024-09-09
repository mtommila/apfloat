/*
 * MIT License
 *
 * Copyright (c) 2002-2024 Mikko Tommila
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
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class ModuloApintFieldTest
    extends ApfloatTestCase
{
    public ModuloApintFieldTest(String methodName)
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

        suite.addTest(new ModuloApintFieldTest("testBasic"));
        suite.addTest(new ModuloApintFieldTest("testNoModulus"));
        suite.addTest(new ModuloApintFieldTest("testMatrixInverse"));
        suite.addTest(new ModuloApintFieldTest("testRationalFunction"));

        return suite;
    }

    public static void testBasic()
    {
        ModuloApintField.setModulus(new Apint(65537));
        ModuloApintField a = valueOf(100);
        ModuloApintField b = valueOf(200);
        ModuloApintField c = valueOf(200);
        ModuloApintField zero = valueOf(0);
        assertEquals("100 + 200", valueOf(300), a.plus(b));
        assertEquals("-100", valueOf(65537 - 100), a.opposite());
        assertEquals("100 - 200", valueOf(65537 - 100), a.minus(b));
        assertEquals("100 * 200", valueOf(20000), a.times(b));
        assertEquals("1 / 100", valueOf(17695), a.inverse());
        assertEquals("copy", a, a.copy());
        assertEquals("doubleValue", 100.0, a.doubleValue(), 0.00000000000005);
        assertEquals("longValue", 100L, a.longValue());
        assertEquals("String", "100", a.toString());
        assertEquals("Text", new Text("100"), a.toText());
        assertTrue("isLargerThan", b.isLargerThan(a));
        assertEquals("compareTo", -1, a.compareTo(b));
        HashSet<ModuloApintField> set = new HashSet<>();
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
            new ModuloApintField(null);
            fail("null accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }
    }

    public static void testNoModulus()
    {
        ModuloApintField.setModulus(null);
        ModuloApintField a = valueOf(100),
                         b = valueOf(200);
        assertEquals("100 + 200", valueOf(300), a.plus(b));
        assertEquals("-100", valueOf(-100), a.opposite());
        assertEquals("100 - 200", valueOf(-100), a.minus(b));
        assertEquals("100 * 200", valueOf(20000), a.times(b));
        assertEquals("copy", a, a.copy());

        try
        {
            a.inverse();
            fail("No divisor accepted");
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK, modulus is not set
        }
    }

    public static void testMatrixInverse()
    {
        ModuloApintField.setModulus(new Apint(43));
        ModuloApintField[][] elements = { { valueOf(1), valueOf(2), valueOf(3) },
                                          { valueOf(4), valueOf(5), valueOf(6) },
                                          { valueOf(7), valueOf(8), valueOf(10) } };
        Matrix<ModuloApintField> m = DenseMatrix.valueOf(elements);
        Vector<ModuloApintField> vectorization = m.minus(m.inverse().inverse()).vectorization();
        for (int i = 0; i < vectorization.getDimension(); i++)
        {
            assertEquals("Element " + i, 0, vectorization.get(i).value().signum());
        }
    }

    public static void testRationalFunction()
    {
        ModuloApintField.setModulus(new Apint(43));
        Variable.Local<ModuloApintField> x = new Variable.Local<ModuloApintField>("x");
        Polynomial<ModuloApintField> dividend = Polynomial.valueOf(valueOf(17), Term.valueOf(x, 2)).plus(Polynomial.valueOf(valueOf(23), x));
        Polynomial<ModuloApintField> divisor = Polynomial.valueOf(valueOf(37), x).plus(valueOf(26));
        RationalFunction<ModuloApintField> function = RationalFunction.valueOf(dividend, divisor);
        assertEquals("Function value", new Apint(2), function.evaluate(valueOf(2)).value());
        assertEquals("Function plus inverse, value", new Apint(24), function.plus(function.inverse()).evaluate(valueOf(2)).value());
    }

    private static ModuloApintField valueOf(int value)
    {
        return new ModuloApintField(new Apint(value));
    }
}
