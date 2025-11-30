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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import org.apfloat.*;

import org.jscience.mathematics.function.*;
import org.jscience.mathematics.vector.*;

import javolution.text.*;
import junit.framework.TestSuite;

/**
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class ModuloPolynomialFieldTest
    extends ApfloatTestCase
{
    public ModuloPolynomialFieldTest(String methodName)
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

        suite.addTest(new ModuloPolynomialFieldTest("testBasic"));
        suite.addTest(new ModuloPolynomialFieldTest("testNoModulus"));
        suite.addTest(new ModuloPolynomialFieldTest("testMatrixInverse"));
        suite.addTest(new ModuloPolynomialFieldTest("testAliceBob"));

        return suite;
    }

    public static void testBasic()
    {
        ModuloPolynomialField.setModulus(rationalPolynomialOf(2, 1, 1));
        ModuloPolynomialField<AprationalField> r = rationalOf(1, 2, 3);
        assertEquals("1 / (y^2 + 2y + 3)", "[-3/11]y + [7/22]", r.inverse().toString());

        ModuloApintField.setModulus(new Apint(3329));
        ModuloPolynomialField.setModulus(polynomialOf(1, 1, 1));
        ModuloPolynomialField<ModuloApintField> a = valueOf(100),
                                                b = valueOf(200),
                                                c = valueOf(200),
                                                zero = valueOf(0);
        assertEquals("modulus", polynomialOf(1, 1, 1), ModuloPolynomialField.getModulus());
        assertEquals("reduce 1", polynomialOf(1), ModuloPolynomialField.reduce(polynomialOf(1, 1, 2)));
        assertEquals("reduce 100", polynomialOf(100), ModuloPolynomialField.reduce(a.value()));
        assertEquals("100 + 200", valueOf(300), a.plus(b));
        assertEquals("-100", valueOf(3329 - 100), a.opposite());
        assertEquals("100 - 200", valueOf(3229), a.minus(b));
        assertEquals("100 * 200", valueOf(20000), a.times(b));
        assertEquals("1 / 100", valueOf(1032), a.inverse());
        assertEquals("String", "[100]", a.toString());
        assertEquals("Text", new Text("[100]"), a.toText());
        HashSet<ModuloPolynomialField<?>> set = new HashSet<>();
        set.add(b);
        set.add(c);
        assertEquals("hashCode", 1, set.size());
        assertEquals("equals", false, a.equals(null));

        a = valueOf(2, 0, 1);
        b = valueOf(3, 1, 2, 1);
        c = valueOf(1, 0, 0, 0, 1);
        assertEquals("2x^2+1 + 3x^3+x^2+2x+1", valueOf(3, 3, 2, 2), a.plus(b));
        assertEquals("-(2x^2+1)", valueOf(2, 1), a.opposite());
        assertEquals("(2x^2+1) * (3x^3+x^2+2x+1)", valueOf(3324, 3328), a.times(b));
        assertEquals("1 / (2x^2+1)", valueOf(2220, 1110), a.inverse());
        assertEquals("copy", a, a.copy());
        assertEquals("String", "[3327]x + [3328]", a.toString());
        assertEquals("Text", new Text("[3327]x + [3328]"), a.toText());

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
            new ModuloPolynomialField<>(null);
            fail("null accepted");
        }
        catch (NullPointerException npe)
        {
            // OK, illegal
        }

        try
        {
            ModuloPolynomialField.setModulus(polynomialOf(0));
        }
        catch (IllegalArgumentException iae)
        {
            // OK, zero modulus
        }

        try
        {
            ModuloPolynomialField.setModulus(polynomialOf(0, 5));
        }
        catch (IllegalArgumentException iae)
        {
            // OK, zero degree modulus
        }

        try
        {
            ModuloPolynomialField.setModulus(Polynomial.valueOf(new ModuloApintField(Apint.ONE), X).times(Polynomial.valueOf(new ModuloApintField(Apint.ONE), new Variable.Local<ModuloApintField>("x"))));
        }
        catch (IllegalArgumentException iae)
        {
            // OK, multi-variable polynomial
        }

        try
        {
            ModuloPolynomialField.setModulus(Polynomial.valueOf(new ModuloApintField(Apint.ONE), X).plus(Polynomial.valueOf(new ModuloApintField(Apint.ONE), new Variable.Local<ModuloApintField>("y"))));
        }
        catch (IllegalArgumentException iae)
        {
            // OK, multi-variable polynomial
        }
    }

    public static void testNoModulus()
    {
        ModuloApintField.setModulus(null);
        ModuloPolynomialField.setModulus(null);
        ModuloPolynomialField<ModuloApintField> a = valueOf(100),
                                                b = valueOf(200);
        assertEquals("modulus", null, ModuloPolynomialField.getModulus());
        assertEquals("reduce", polynomialOf(100), ModuloPolynomialField.reduce(polynomialOf(100)));
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
        ModuloApintField.setModulus(new Apint(3329));
        ModuloPolynomialField.setModulus(polynomialOf(1, 1, 1));
        ModuloPolynomialField<?>[][] elements = { { valueOf(9, 1), valueOf(4, 2), valueOf(3, 3) },
                                                  { valueOf(8, 4), valueOf(5, 5), valueOf(2, 6) },
                                                  { valueOf(7, 7), valueOf(6, 8), valueOf(1, 10) } };
        @SuppressWarnings("unchecked")
        Matrix<ModuloPolynomialField<ModuloApintField>> matrix = DenseMatrix.valueOf((ModuloPolynomialField<ModuloApintField>[][]) elements);
        Vector<ModuloPolynomialField<ModuloApintField>> vectorization = matrix.minus(matrix.inverse().inverse()).vectorization();
        for (int i = 0; i < vectorization.getDimension(); i++)
        {
            assertEquals("Element " + i, valueOf(0), vectorization.get(i));
        }

        int q = 3329,
            k = 2,
            m = 25,
            n = 256;
        Random random = new Random();
        ModuloApintField.setModulus(new Apint(q));
        int[] coefficients = new int[n + 1];
        coefficients[0] = 1;
        coefficients[m] = 1;
        coefficients[n] = 1;
        ModuloPolynomialField.setModulus(polynomialOf(coefficients));
        ModuloPolynomialField<?>[][] elementsBig = IntStream.range(0, k).mapToObj(i -> IntStream.range(0, k).mapToObj(i2 -> valueOf(IntStream.range(0, n).map(i3 -> random.nextInt(q)).toArray())).toArray(ModuloPolynomialField[]::new)).toArray(ModuloPolynomialField<?>[][]::new);
        @SuppressWarnings("unchecked")
        Matrix<ModuloPolynomialField<ModuloApintField>> matrixBig = DenseMatrix.valueOf((ModuloPolynomialField<ModuloApintField>[][]) elementsBig);
        Vector<ModuloPolynomialField<ModuloApintField>> vectorizationBig = matrixBig.minus(matrixBig.inverse().inverse()).vectorization();
        for (int i = 0; i < vectorizationBig.getDimension(); i++)
        {
            assertEquals("Element " + i, valueOf(0), vectorizationBig.get(i));
        }

        ModuloPolynomialField.setModulus(rationalPolynomialOf(2, 1, 1));
        ModuloPolynomialField<?>[][] elementsRational = { { rationalOf(9, 1), rationalOf(4, 2), rationalOf(3, 3) },
                                                          { rationalOf(8, 4), rationalOf(5, 5), rationalOf(2, 6) },
                                                          { rationalOf(7, 7), rationalOf(6, 8), rationalOf(1, 10) } };
        @SuppressWarnings("unchecked")
        Matrix<ModuloPolynomialField<AprationalField>> matrixRational = DenseMatrix.valueOf((ModuloPolynomialField<AprationalField>[][]) elementsRational);
        Vector<ModuloPolynomialField<AprationalField>> vectorizationRational = matrixRational.minus(matrixRational.inverse().inverse()).vectorization();
        for (int i = 0; i < vectorizationRational.getDimension(); i++)
        {
            assertEquals("Element " + i, rationalOf(0), vectorizationRational.get(i));
        }
    }

    public static void testAliceBob()
    {
        // ML-KEM
        // See e.g. https://thibautprobst.fr/en/posts/ml-kem/#ml-kem for an explanation
        Random random = new Random();
        IntFunction<Integer> cbd = η -> IntStream.range(0, η).map(i -> random.nextInt(2)).sum() - IntStream.range(0, η).map(i -> random.nextInt(2)).sum();
        BiFunction<Integer, Integer, ModuloPolynomialField<ModuloApintField>> cbdPolynomial = (n, η) -> valueOf(IntStream.range(0, n).map(i -> cbd.apply(η)).toArray());
        TriFunction<Integer, Integer, Integer, ModuloPolynomialField<?>[]> cbdVector = (k, n, η) -> IntStream.range(0, k).mapToObj(i -> cbdPolynomial.apply(n, η)).toArray(ModuloPolynomialField<?>[]::new);
        BiFunction<Integer, Integer, ModuloPolynomialField<ModuloApintField>> uniformPolynomial = (n, q) -> valueOf(IntStream.range(0, n).map(i -> random.nextInt(q)).toArray());
        TriFunction<Integer, Integer, Integer, ModuloPolynomialField<?>[]> uniformVector = (k, n, q) -> IntStream.range(0, k).mapToObj(i -> uniformPolynomial.apply(n, q)).toArray(ModuloPolynomialField<?>[]::new);
        TriFunction<Integer, Integer, Integer, ModuloPolynomialField<?>[][]> uniformMatrix = (k, n, q) -> IntStream.range(0, k).mapToObj(i -> uniformVector.apply(k, n, q)).toArray(ModuloPolynomialField<?>[][]::new);
        int q = 3329,   // Or 7681
            n = 256,
            k = 4,
            η = 2;      // Use 3 for k = 2 and 2 for k = 3, 4
        // Modulus, A and pk are public (generated as random by Alice)
        ModuloApintField.setModulus(new Apint(q));
        int[] coefficients = new int[n + 1];
        coefficients[0] = 1;
        coefficients[n] = 1;
        ModuloPolynomialField.setModulus(polynomialOf(coefficients));
        @SuppressWarnings("unchecked")
        Matrix<ModuloPolynomialField<ModuloApintField>> A = DenseMatrix.valueOf((ModuloPolynomialField<ModuloApintField>[][]) uniformMatrix.apply(k, n, q));
        // Private keys, generated as random (e and s by Alice)
        @SuppressWarnings("unchecked")
        Vector<ModuloPolynomialField<ModuloApintField>> e = DenseVector.valueOf((ModuloPolynomialField<ModuloApintField>[]) cbdVector.apply(k, n, η)),  // Noise
                                                        s = DenseVector.valueOf((ModuloPolynomialField<ModuloApintField>[]) cbdVector.apply(k, n, η)),  // The "secret", small
        // Public key (by Alice)
                                                        pk = A.times(s).plus(e);                                                                        // Private key
        // Key exchange (e1, e2, r, m, c1 and c2 generated by Bob)
        ModuloPolynomialField<ModuloApintField> bobSharedSecret = valueOf(IntStream.range(0, n).map(i -> random.nextInt(2)).toArray());                 // Bit vector in the polynomial coefficients
        @SuppressWarnings("unchecked")
        Vector<ModuloPolynomialField<ModuloApintField>> e1 = DenseVector.valueOf((ModuloPolynomialField<ModuloApintField>[]) cbdVector.apply(k, n, η)), // Noise
                                                        r = DenseVector.valueOf((ModuloPolynomialField<ModuloApintField>[]) cbdVector.apply(k, n, η));  // Random, small
        ModuloPolynomialField<ModuloApintField> e2 = cbdPolynomial.apply(n, η),                                                                         // Noise
                                                m = bobSharedSecret.times(valueOf(q / 2));                                                              // The "message"
        Vector<ModuloPolynomialField<ModuloApintField>> c1 = A.transpose().times(r).plus(e1);                                                           // The "ciphertext"
        ModuloPolynomialField<ModuloApintField> c2 = pk.times(r).plus(e2).plus(m);                                                                      // The "ciphertext"
        // Shared secret
        ModuloPolynomialField<ModuloApintField> mApprox = c2.minus(c1.times(s)),                                                                        // The "message" approximately, with some noise added
                                                aliceSharedSecret = valueOf(Arrays.stream(coefficients(mApprox)).map(i -> (i + q / 4) % q / (q / 2)).toArray());    // Round message values to the original bits
        assertEquals("Alice shared secret == Bob shared secret", aliceSharedSecret, bobSharedSecret);
    }

    private static Polynomial<ModuloApintField> polynomialOf(int... coefficients)
    {
        Polynomial<ModuloApintField> polynomial = Constant.valueOf(new ModuloApintField(Apint.ZERO));
        Polynomial<ModuloApintField> shift = Polynomial.valueOf(new ModuloApintField(Apint.ONE), X);
        for (int coefficient : coefficients)
        {
            polynomial = polynomial.times(shift);
            Polynomial<ModuloApintField> term = Constant.valueOf(new ModuloApintField(new Apint(coefficient)));
            polynomial = polynomial.plus(term);
        }
        return polynomial;
    }

    private static ModuloPolynomialField<ModuloApintField> valueOf(int... coefficients)
    {
        return new ModuloPolynomialField<>(polynomialOf(coefficients));
    }

    private static int[] coefficients(ModuloPolynomialField<ModuloApintField> field)
    {
        Polynomial<ModuloApintField> polynomial = field.value();
        int n = polynomial.getOrder(X);
        int[] coefficients = new int[n + 1];
        polynomial.getTerms().forEach(term -> coefficients[n - term.getPower(X)] = polynomial.getCoefficient(term).intValue());
        return coefficients;
    }

    private static Polynomial<AprationalField> rationalPolynomialOf(int... coefficients)
    {
        Polynomial<AprationalField> polynomial = Constant.valueOf(new AprationalField(Apint.ZERO));
        Polynomial<AprationalField> shift = Polynomial.valueOf(new AprationalField(Apint.ONE), Y);
        for (int coefficient : coefficients)
        {
            polynomial = polynomial.times(shift);
            Polynomial<AprationalField> term = Constant.valueOf(new AprationalField(new Apint(coefficient)));
            polynomial = polynomial.plus(term);
        }
        return polynomial;
    }

    private static ModuloPolynomialField<AprationalField> rationalOf(int... coefficients)
    {
        return new ModuloPolynomialField<>(rationalPolynomialOf(coefficients));
    }

    public static interface TriFunction<T, U, V, R>
    {
        R apply(T t, U u, V v);
    }

    private static final Variable<ModuloApintField> X = new Variable.Local<ModuloApintField>("x");
    private static final Variable<AprationalField> Y = new Variable.Local<AprationalField>("y");
}
