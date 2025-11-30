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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apfloat.ApfloatArithmeticException;
import org.jscience.mathematics.function.Constant;
import org.jscience.mathematics.function.Polynomial;
import org.jscience.mathematics.function.Term;
import org.jscience.mathematics.function.Variable;
import org.jscience.mathematics.structure.Field;
import org.jscience.mathematics.structure.Ring;

import javolution.context.LocalContext;
import javolution.lang.Realtime;
import javolution.text.Text;

/**
 * This class represents a polynomial modulo a modulus.
 * The modulus must be set with {@link #setModulus(Polynomial)}; otherwise
 * the modulo reduction is not done.<p>
 *
 * For the polynomials to actually form a field, the modulus must be irreducible
 * over the coefficient field. Otherwise it's just a {@link Ring} and the
 * multiplicative {@link #inverse()} method may fail.
 *
 * @param <F> The type of the polynomial coefficients.
 *
 * @since 1.15.0
 * @version 1.15.1
 * @author Mikko Tommila
 */
public class ModuloPolynomialField<F extends Field<F>>
    implements Field<ModuloPolynomialField<F>>, Serializable, Realtime
{
    private static class FiniteFieldPolynomial<F extends Field<F>>
    {
        public FiniteFieldPolynomial(Polynomial<F> polynomial)
        {
            this.polynomial = normalize(polynomial);
        }

        public FiniteFieldPolynomial<F> plus(FiniteFieldPolynomial<F> that)
        {
            return new FiniteFieldPolynomial<>(this.polynomial.plus(that.polynomial));
        }

        public FiniteFieldPolynomial<F> minus(FiniteFieldPolynomial<F> that)
        {
            return new FiniteFieldPolynomial<>(this.polynomial.minus(that.polynomial));
        }

        public FiniteFieldPolynomial<F> opposite()
        {
            return new FiniteFieldPolynomial<>(this.polynomial.opposite());
        }

        public FiniteFieldPolynomial<F> times(F that)
        {
            return new FiniteFieldPolynomial<>(this.polynomial.times(that));
        }

        public FiniteFieldPolynomial<F> times(FiniteFieldPolynomial<F> that)
        {
            // JScience does not handle correctly polynomial multiplication when the coefficients are defined over a finite field
            Map<Term, F> resultTermMap = new HashMap<>();
            F zero = null;
            for (Term t1 : this.polynomial.getTerms())
            {
                F c1 = this.polynomial.getCoefficient(t1);
                for (Term t2 : that.polynomial.getTerms())
                {
                    F c2 = that.polynomial.getCoefficient(t2);
                    Term t = t1.times(t2);
                    F c = c1.times(c2);
                    F prev = resultTermMap.get(t);
                    F coef = (prev != null) ? prev.plus(c) : c;
                    if (isZero(coef))
                    {
                        zero = coef;
                        resultTermMap.remove(t);    // This line is missing in JScience
                    }
                    else
                    {
                        resultTermMap.put(t, coef);
                    }
                }
            }
            Polynomial<F> result = resultTermMap.entrySet().stream().map(e -> Polynomial.valueOf(e.getValue(), e.getKey())).reduce(Polynomial::plus).orElse(Constant.valueOf(zero));
            return new FiniteFieldPolynomial<>(result);
        }

        public Set<Term> getTerms()
        {
            return this.polynomial.getTerms();
        }

        public F getCoefficient(Term term)
        {
            return this.polynomial.getCoefficient(term);
        }

        public Polynomial<F> value()
        {
            return this.polynomial;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof FiniteFieldPolynomial)
            {
                FiniteFieldPolynomial<?> that = (FiniteFieldPolynomial<?>) obj;
                return this.polynomial.equals(that.polynomial);
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            return this.polynomial.hashCode() * 3;
        }

        private static <F extends Field<F>> Polynomial<F> normalize(Polynomial<F> polynomial)
        {
            // This is necessary only because JScience has some bugs with a nonzero polynomial containing a zero constant term
            for (Term term : polynomial.getTerms())
            {
                F coefficient = polynomial.getCoefficient(term);
                if (term.size() == 0 && isZero(coefficient))
                {
                    return polynomial.plus(coefficient);
                }
            }
            return polynomial;
        }

        private Polynomial<F> polynomial;
    }

    /**
     * Create a new modulo polynomial.
     *
     * @param polynomial The polynomial. Will be reduced by the modulus, if any.
     */

    public ModuloPolynomialField(Polynomial<F> polynomial)
    {
        this(new FiniteFieldPolynomial<>(polynomial));
    }

    private ModuloPolynomialField(FiniteFieldPolynomial<F> polynomial)
    {
        this.polynomial = reduce(polynomial);
    }

    /**
     * Returns the modulus or <code>null</code> if modulo reduction is not done.
     * The modulus can be set in a thread-specific way using
     * {@link javolution.context.LocalContext}.
     *
     * @param <F> The type of the polynomial coefficients.
     *
     * @return The local modulus or <code>null</code> if modulo reduction is not done.
     *
     * @see #setModulus
     */
    public static <F extends Field<F>> Polynomial<F> getModulus()
    {
        @SuppressWarnings("unchecked")
        FiniteFieldPolynomial<F> modulus = (FiniteFieldPolynomial<F>) getModulusInternal();
        return modulus == null ? null : modulus.value();
    }

    private static <F extends Field<F>> FiniteFieldPolynomial<F> getModulusInternal()
    {
        @SuppressWarnings("unchecked")
        FiniteFieldPolynomial<F> modulus = (FiniteFieldPolynomial<F>) MODULUS.get();
        return modulus;
    }

    /**
     * Sets the modulus.
     * The modulus can be set in a thread-specific way using
     * {@link javolution.context.LocalContext}.
     *
     * @param <F> The type of the polynomial coefficients.
     * @param modulus The modulus or <code>null</code> if modulo reduction is not done.
     */
    public static <F extends Field<F>> void setModulus(Polynomial<F> modulus)
    {
        setModulus(modulus == null ? null : new FiniteFieldPolynomial<>(modulus));
    }

    private static <F extends Field<F>> void setModulus(FiniteFieldPolynomial<F> modulus)
    {
        if (modulus != null)
        {
            List<Term> terms = new ArrayList<>(modulus.getTerms());
            if (getDegree(terms) == 0)
            {
                throw new IllegalArgumentException("Polynomial must have degree > 0");
            }
            terms.sort(null);
            Term leadingTerm = terms.get(0);
            F leadingCoefficient = modulus.getCoefficient(leadingTerm);
            if (terms.size() == 1)
            {
                if (isZero(leadingCoefficient))
                {
                    throw new IllegalArgumentException("Polynomial must be nonzero");
                }
            }
            // Scale leading term of modulus to 1
            F inverseLeadingCoefficient = leadingCoefficient.inverse();
            FiniteFieldPolynomial<F> scaledModulus = modulus.times(inverseLeadingCoefficient);
            SCALED_MODULUS.set(scaledModulus);
            ZERO.set(new FiniteFieldPolynomial<>(Constant.valueOf(leadingCoefficient.plus(leadingCoefficient.opposite()))));
            ONE.set(new FiniteFieldPolynomial<>(Constant.valueOf(leadingCoefficient.times(inverseLeadingCoefficient))));
        }
        MODULUS.set(modulus);
    }

    /**
     * Reduce the polynomial with the current modulus.
     *
     * @param <F> The type of the polynomial coefficients.
     * @param polynomial The polynomial.
     *
     * @return The polynomial mod the current modulus.
     */

    public static <F extends Field<F>> Polynomial<F> reduce(Polynomial<F> polynomial)
    {
        return reduce(new FiniteFieldPolynomial<>(polynomial)).value();
    }

    private static <F extends Field<F>> FiniteFieldPolynomial<F> reduce(FiniteFieldPolynomial<F> polynomial)
    {
        FiniteFieldPolynomial<F> modulus = getModulusInternal();
        if (modulus != null)
        {
            polynomial = div(polynomial, modulus)[1];
        }
        return polynomial;
    }

    private static <F extends Field<F>> FiniteFieldPolynomial<F>[] div(FiniteFieldPolynomial<F> dividend, FiniteFieldPolynomial<F> divisor)
    {
        FiniteFieldPolynomial<F> quotient = zero(),
                                 remainder = dividend;
        List<Term> divisorTerms = new ArrayList<>(divisor.getTerms());
        int divisorOrder = getDegree(divisorTerms),
            dividendOrder;
        divisorTerms.sort(null);
        Term divisorLeadingTerm = divisorTerms.get(0);
        F divisorLeadingCoefficientInverse = divisor.getCoefficient(divisorLeadingTerm).inverse();

        do
        {
            List<Term> terms = new ArrayList<>(remainder.getTerms());
            dividendOrder = getDegree(terms);
            if (dividendOrder >= divisorOrder)
            {
                terms.sort(null);
                Term leadingTerm = terms.get(0);
                Variable<?> variable = (leadingTerm.size() == 0 ? null : leadingTerm.getVariable(0));
                F coefficient = remainder.getCoefficient(leadingTerm).times(divisorLeadingCoefficientInverse);
                FiniteFieldPolynomial<F> quotientTerm = new FiniteFieldPolynomial<>(variable == null ? Constant.valueOf(coefficient) : Polynomial.valueOf(coefficient, Term.valueOf(variable, dividendOrder - divisorOrder)));
                quotient = quotient.plus(quotientTerm);
                remainder = remainder.minus(divisor.times(quotientTerm));
            }
        } while (dividendOrder > divisorOrder);

        assert divisor.times(quotient).plus(remainder).equals(dividend) : divisor + " * " + quotient + " + " + remainder + " != " + dividend;

        @SuppressWarnings("unchecked")
        FiniteFieldPolynomial<F>[] qr = (FiniteFieldPolynomial<F>[]) new FiniteFieldPolynomial<?>[] { quotient, remainder };
        return qr;
    }

    private static int getDegree(Collection<Term> terms)
    {
        // Polynomial.getVariables() is very inefficient
        if (terms.stream().mapToInt(Term::size).anyMatch(i -> i > 1) ||
            terms.stream().filter(t -> t.size() > 0).map(t -> t.getVariable(0)).distinct().count() > 1)
        {
            throw new IllegalArgumentException("Polynomial must use a single variable");
        }
        return terms.stream().filter(t -> t.size() > 0).mapToInt(t -> t.getPower(0)).max().orElse(0);
    }

    @Override
    public ModuloPolynomialField<F> plus(ModuloPolynomialField<F> that)
    {
        return new ModuloPolynomialField<>(this.polynomial.plus(that.polynomial));
    }

    /**
     * Returns the difference of two polynomials.
     *
     * @param that The polynomial being subtracted.
     *
     * @return <code>this - that</code>
     */

    public ModuloPolynomialField<F> minus(ModuloPolynomialField<F> that)
    {
        return new ModuloPolynomialField<>(this.polynomial.minus(that.polynomial));
    }

    @Override
    public ModuloPolynomialField<F> opposite()
    {
        return new ModuloPolynomialField<>(this.polynomial.opposite());
    }

    @Override
    public ModuloPolynomialField<F> times(ModuloPolynomialField<F> that)
    {
        return new ModuloPolynomialField<>(this.polynomial.times(that.polynomial));
    }

    @Override
    public ModuloPolynomialField<F> inverse()
    {
        // Extended Euclidean algorithm
        FiniteFieldPolynomial<F> a = this.polynomial,
                                 zero = zero(),
                                 one = one(),
                                 x = zero,
                                 oldX = one,
                                 b = getModulusInternal();

        if (b == null)
        {
            throw new ApfloatArithmeticException("Modulus is not set");
        }

        while (!b.equals(zero))
        {
            FiniteFieldPolynomial<F>[] qr = div(a, b);

            FiniteFieldPolynomial<F> q = qr[0];
            a = b;
            b = qr[1];

            FiniteFieldPolynomial<F> tmp = x;
            x = oldX.minus(q.times(x));
            oldX = tmp;
        }

        List<Term> terms = new ArrayList<>(a.getTerms());
        if (getDegree(terms) != 0)
        {
            // GCD is not a unit
            throw new ApfloatArithmeticException("Modular inverse does not exist", "modInverse.notExists");
        }

        // Normalize by scaling with the inverse of the GCD
        oldX = oldX.times(a.getCoefficient(terms.get(0)).inverse());

        return new ModuloPolynomialField<>(oldX);
    }

    /**
     * Returns the underlying polynomial.
     *
     * @return The polynomial.
     */

    public Polynomial<F> value()
    {
        return this.polynomial.value();
    }

    @Override
    public Object copy()
    {
        return new ModuloPolynomialField<>(this.polynomial.value().copy());
    }

    @Override
    public Text toText()
    {
        return this.polynomial.value().toText();
    }

    @Override
    public String toString() {
        return toText().toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.polynomial);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ModuloPolynomialField)
        {
            ModuloPolynomialField<?> that = (ModuloPolynomialField<?>) obj;
            return Objects.equals(this.polynomial, that.polynomial);
        }
        return false;
    }

    private static <F extends Field<F>> boolean isZero(F coefficient)
    {
        return coefficient.equals(coefficient.opposite());
    }

    private static <F extends Field<F>> FiniteFieldPolynomial<F> zero()
    {
        @SuppressWarnings("unchecked")
        FiniteFieldPolynomial<F> zero = (FiniteFieldPolynomial<F>) ZERO.get();
        return zero;
    }

    private static <F extends Field<F>> FiniteFieldPolynomial<F> one()
    {
        @SuppressWarnings("unchecked")
        FiniteFieldPolynomial<F> one = (FiniteFieldPolynomial<F>) ONE.get();
        return one;
    }

    private static final long serialVersionUID = -7690334128073794662L;

    private static final LocalContext.Reference<FiniteFieldPolynomial<? extends Field<?>>> MODULUS = new LocalContext.Reference<>(),
                                                                                           SCALED_MODULUS = new LocalContext.Reference<>(),
                                                                                           ZERO = new LocalContext.Reference<>(),
                                                                                           ONE = new LocalContext.Reference<>();

    private FiniteFieldPolynomial<F> polynomial;
}
