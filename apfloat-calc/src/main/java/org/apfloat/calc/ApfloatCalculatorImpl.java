/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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
package org.apfloat.calc;

import java.math.RoundingMode;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.apfloat.ApintMath;
import org.apfloat.Aprational;
import org.apfloat.AprationalMath;
import org.apfloat.spi.Util;

/**
 * Arbitrary precision calculator implementation.
 *
 * @version 1.11.0
 * @author Mikko Tommila
 */

public class ApfloatCalculatorImpl
    extends FunctionCalculatorImpl
{
    private static final long serialVersionUID = 1L;

    private static class ApcomplexFunctions
        implements Functions
    {
        @Override
        public Number negate(Number x)
        {
            return ((Apcomplex) x).negate();
        }

        @Override
        public Number add(Number x, Number y)
        {
            return ((Apcomplex) x).add((Apcomplex) y);
        }

        @Override
        public Number subtract(Number x, Number y)
        {
            return ((Apcomplex) x).subtract((Apcomplex) y);
        }

        @Override
        public Number multiply(Number x, Number y)
        {
            return ((Apcomplex) x).multiply((Apcomplex) y);
        }

        @Override
        public Number divide(Number x, Number y)
        {
            return ((Apcomplex) x).divide((Apcomplex) y);
        }

        @Override
        public Number mod(Number x, Number y)
        {
            throw new IllegalArgumentException("Modulus can only be used with scalar values");
        }

        @Override
        public Number pow(Number x, Number y)
        {
            if (isLong(y))
            {
                if (((Apcomplex) y).precision() < ((Apcomplex) x).precision())
                {
                    x = ((Apcomplex) x).precision(((Apcomplex) y).precision());
                }
                return pow(x, y.longValue());
            }
            else
            {
                return ApcomplexMath.pow((Apcomplex) x, (Apcomplex) y);
            }
        }

        protected Number pow(Number x, long y)
        {
            return ApcomplexMath.pow((Apcomplex) x, y);
        }

        @Override
        public Number arg(Number x)
        {
            return ApcomplexMath.arg((Apcomplex) x);
        }

        @Override
        public Number conj(Number x)
        {
            return ((Apcomplex) x).conj();
        }

        @Override
        public Number imag(Number x)
        {
            return ((Apcomplex) x).imag();
        }

        @Override
        public Number real(Number x)
        {
            return ((Apcomplex) x).real();
        }

        @Override
        public Number abs(Number x)
        {
            return ApcomplexMath.abs((Apcomplex) x);
        }

        @Override
        public Number acos(Number x)
        {
            return ApcomplexMath.acos((Apcomplex) x);
        }

        @Override
        public Number acosh(Number x)
        {
            return ApcomplexMath.acosh((Apcomplex) x);
        }

        @Override
        public Number asin(Number x)
        {
            return ApcomplexMath.asin((Apcomplex) x);
        }

        @Override
        public Number asinh(Number x)
        {
            return ApcomplexMath.asinh((Apcomplex) x);
        }

        @Override
        public Number atan(Number x)
        {
            return ApcomplexMath.atan((Apcomplex) x);
        }

        @Override
        public Number atanh(Number x)
        {
            return ApcomplexMath.atanh((Apcomplex) x);
        }

        @Override
        public Number bernoulli(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Bernoulli can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return AprationalMath.bernoulli(n);
        }

        @Override
        public Number binomial(Number x, Number y)
        {
            return ApcomplexMath.binomial((Apcomplex) x, (Apcomplex) y);
        }

        @Override
        public Number catalan(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Catalan can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.catalan(n);
        }

        @Override
        public Number cbrt(Number x)
        {
            return root(x, 3);
        }

        @Override
        public Number ceil(Number x)
        {
            throw new IllegalArgumentException("Ceiling can only be used with scalar values");
        }

        @Override
        public Number cos(Number x)
        {
            return ApcomplexMath.cos((Apcomplex) x);
        }

        @Override
        public Number cosh(Number x)
        {
            return ApcomplexMath.cosh((Apcomplex) x);
        }

        @Override
        public Number digamma(Number x)
        {
            return ApcomplexMath.digamma((Apcomplex) x);
        }

        @Override
        public Number e(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("E can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.e(n);
        }

        @Override
        public Number euler(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Euler can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.euler(n);
        }

        @Override
        public Number exp(Number x)
        {
            return ApcomplexMath.exp((Apcomplex) x);
        }

        @Override
        public Number factorial(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Factorial can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApintMath.factorial(n);
        }

        @Override
        public Number floor(Number x)
        {
            throw new IllegalArgumentException("Floor can only be used with scalar values");
        }

        @Override
        public Number frac(Number x)
        {
            throw new IllegalArgumentException("Frac can only be used with scalar values");
        }

        @Override
        public Number glaisher(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Glaisher can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.glaisher(n);
        }

        @Override
        public Number gamma(Number x)
        {
            return ApcomplexMath.gamma((Apcomplex) x);
        }

        @Override
        public Number gamma(Number x, Number y)
        {
            return ApcomplexMath.gamma((Apcomplex) x, (Apcomplex) y);
        }

        @Override
        public Number gamma(Number x, Number y, Number z)
        {
            return ApcomplexMath.gamma((Apcomplex) x, (Apcomplex) y, (Apcomplex) z);
        }

        @Override
        public Number hypergeometric0F1(Number a, Number z)
        {
            return ApcomplexMath.hypergeometric0F1((Apcomplex) a, (Apcomplex) z);
        }

        @Override
        public Number hypergeometric1F1(Number a, Number b, Number z)
        {
            return ApcomplexMath.hypergeometric1F1((Apcomplex) a, (Apcomplex) b, (Apcomplex) z);
        }

        @Override
        public Number hypergeometric2F1(Number a, Number b, Number c, Number z)
        {
            return ApcomplexMath.hypergeometric2F1((Apcomplex) a, (Apcomplex) b, (Apcomplex) c, (Apcomplex) z);
        }

        @Override
        public Number khinchin(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Khinchin can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.khinchin(n);
        }

        @Override
        public Number log(Number x)
        {
            return ApcomplexMath.log((Apcomplex) x);
        }

        @Override
        public Number log(Number x, Number y)
        {
            return ApcomplexMath.log((Apcomplex) x, (Apcomplex) y);
        }

        @Override
        public Number logGamma(Number x)
        {
            return ApcomplexMath.logGamma((Apcomplex) x);
        }

        @Override
        public Number max(Number x, Number y)
        {
            throw new IllegalArgumentException("Max can only be used with scalar values");
        }

        @Override
        public Number min(Number x, Number y)
        {
            throw new IllegalArgumentException("Min can only be used with scalar values");
        }

        @Override
        public Number nextAfter(Number x, Number y)
        {
            throw new IllegalArgumentException("Next after can only be used with scalar values");
        }

        @Override
        public Number nextDown(Number x)
        {
            throw new IllegalArgumentException("Next down can only be used with scalar values");
        }

        @Override
        public Number nextUp(Number x)
        {
            throw new IllegalArgumentException("Next up can only be used with scalar values");
        }

        @Override
        public Number pi(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Pi can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.pi(n);
        }

        @Override
        public Number random(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Random can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.random(n);
        }

        @Override
        public Number randomGaussian(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Random Gaussian can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.randomGaussian(n);
        }

        @Override
        public Number round(Number x, Number y)
        {
            throw new IllegalArgumentException("Round can only be used with scalar values");
        }

        @Override
        public Number sin(Number x)
        {
            return ApcomplexMath.sin((Apcomplex) x);
        }

        @Override
        public Number sinh(Number x)
        {
            return ApcomplexMath.sinh((Apcomplex) x);
        }

        @Override
        public Number sqrt(Number x)
        {
            return root(x, 2);
        }

        @Override
        public Number tan(Number x)
        {
            return ApcomplexMath.tan((Apcomplex) x);
        }

        @Override
        public Number tanh(Number x)
        {
            return ApcomplexMath.tanh((Apcomplex) x);
        }

        @Override
        public Number truncate(Number x)
        {
            throw new IllegalArgumentException("Truncate can only be used with scalar values");
        }

        @Override
        public Number toDegrees(Number x)
        {
            throw new IllegalArgumentException("ToDegrees can only be used with scalar values");
        }

        @Override
        public Number toRadians(Number x)
        {
            throw new IllegalArgumentException("ToRadians can only be used with scalar values");
        }

        @Override
        public Number ulp(Number x)
        {
            return ApcomplexMath.ulp((Apcomplex) x);
        }

        @Override
        public Number zeta(Number x)
        {
            return ApcomplexMath.zeta((Apcomplex) x);
        }

        @Override
        public Number zeta(Number x, Number y)
        {
            return ApcomplexMath.zeta((Apcomplex) x, (Apcomplex) y);
        }

        @Override
        public Number agm(Number x, Number y)
        {
            return ApcomplexMath.agm((Apcomplex) x, (Apcomplex) y);
        }

        @Override
        public Number w(Number x)
        {
            return ApcomplexMath.w((Apcomplex) x);
        }

        @Override
        public Number w(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Lambert W can only be used with a valid integer argument");
            }
            return ApcomplexMath.w((Apcomplex) x, y.longValue());
        }

        @Override
        public Number atan2(Number x, Number y)
        {
            throw new IllegalArgumentException("Atan2 can only be used with scalar values");
        }

        @Override
        public Number copySign(Number x, Number y)
        {
            throw new IllegalArgumentException("CopySign can only be used with scalar values");
        }

        @Override
        public Number fmod(Number x, Number y)
        {
            throw new IllegalArgumentException("Fmod can only be used with scalar values");
        }

        @Override
        public Number gcd(Number x, Number y)
        {
            throw new IllegalArgumentException("Greatest Common Ddivisor can only be used with integer values");
        }

        @Override
        public Number hypot(Number x, Number y)
        {
            throw new IllegalArgumentException("Hypot can only be used with scalar values");
        }

        @Override
        public Number inverseRoot(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Inverse root can only be used with a valid integer argument");
            }
            return inverseRoot(x, y.longValue());
        }

        @Override
        public Number inverseRoot(Number x, Number y, Number z)
        {
            if (!isLong(y) || !isLong(z))
            {
                throw new IllegalArgumentException("Inverse root can only be used with valid integer arguments");
            }
            return inverseRoot(x, y.longValue(), z.longValue());
        }

        protected Number inverseRoot(Number x, long y)
        {
            return ApcomplexMath.inverseRoot((Apcomplex) x, y);
        }

        protected Number inverseRoot(Number x, long y, long z)
        {
            return ApcomplexMath.inverseRoot((Apcomplex) x, y, z);
        }

        @Override
        public Number lcm(Number x, Number y)
        {
            throw new IllegalArgumentException("Least Common Multiplier can only be used with integer values");
        }

        @Override
        public Number root(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Root can only be used with a valid integer argument");
            }
            return root(x, y.longValue());
        }

        @Override
        public Number root(Number x, Number y, Number z)
        {
            if (!isLong(y) || !isLong(z))
            {
                throw new IllegalArgumentException("Root can only be used with valid integer arguments");
            }
            return root(x, y.longValue(), z.longValue());
        }

        protected Number root(Number x, long y)
        {
            return ApcomplexMath.root((Apcomplex) x, y);
        }

        protected Number root(Number x, long y, long z)
        {
            return ApcomplexMath.root((Apcomplex) x, y, z);
        }

        @Override
        public Number scale(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Scale can only be used with a valid integer argument");
            }
            return scale(x, y.longValue());
        }

        protected Number scale(Number x, long y)
        {
            return ApcomplexMath.scale((Apcomplex) x, y);
        }

        @Override
        public Number precision(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Precision can only be used with a valid integer argument");
            }
            return precision(x, y.longValue());
        }

        protected Number precision(Number x, long precision)
        {
            return ((Apcomplex) x).precision(precision);
        }

        protected boolean isLong(Number value)
        {
            return new Apint(value.longValue()).equals(value);
        }
    }

    private static class ApfloatFunctions
        extends ApcomplexFunctions
    {
        @Override
        public Number mod(Number x, Number y)
        {
            return ((Apfloat) x).mod((Apfloat) y);
        }

        @Override
        public Number ceil(Number x)
        {
            return ((Apfloat) x).ceil();
        }

        @Override
        public Number floor(Number x)
        {
            return ((Apfloat) x).floor();
        }

        @Override
        public Number frac(Number x)
        {
            return ((Apfloat) x).frac();
        }

        @Override
        public Number max(Number x, Number y)
        {
            return ApfloatMath.max((Apfloat) x, (Apfloat) y);
        }

        @Override
        public Number min(Number x, Number y)
        {
            return ApfloatMath.min((Apfloat) x, (Apfloat) y);
        }

        @Override
        public Number nextAfter(Number x, Number y)
        {
            return ApfloatMath.nextAfter((Apfloat) x, (Apfloat) y);
        }

        @Override
        public Number nextDown(Number x)
        {
            return ApfloatMath.nextDown((Apfloat) x);
        }

        @Override
        public Number nextUp(Number x)
        {
            return ApfloatMath.nextUp((Apfloat) x);
        }

        @Override
        public Number truncate(Number x)
        {
            return ((Apfloat) x).truncate();
        }

        @Override
        public Number toDegrees(Number x)
        {
            return ApfloatMath.toDegrees((Apfloat) x);
        }

        @Override
        public Number toRadians(Number x)
        {
            return ApfloatMath.toRadians((Apfloat) x);
        }

        @Override
        public Number atan2(Number x, Number y)
        {
            return ApfloatMath.atan2((Apfloat) x, (Apfloat) y);
        }

        @Override
        public Number copySign(Number x, Number y)
        {
            return ApfloatMath.copySign((Apfloat) x, (Apfloat) y);
        }

        @Override
        public Number fmod(Number x, Number y)
        {
            return ApfloatMath.fmod((Apfloat) x, (Apfloat) y);
        }

        @Override
        public Number hypot(Number x, Number y)
        {
            return ApcomplexMath.abs(new Apcomplex((Apfloat) x, (Apfloat) y));
        }

        @Override
        public Number round(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Round can only be used with a valid integer argument");
            }
            return round(x, y.longValue());
        }

        protected Number round(Number x, long precision)
        {
            return ApfloatMath.round((Apfloat) x, precision, RoundingMode.HALF_UP);
        }
    }

    private static class AprationalFunctions
        extends ApfloatFunctions
    {
        @Override
        public Number add(Number x, Number y)
        {
            return ((Aprational) x).add((Aprational) y);
        }

        @Override
        public Number subtract(Number x, Number y)
        {
            return ((Aprational) x).subtract((Aprational) y);
        }

        @Override
        public Number multiply(Number x, Number y)
        {
            return ((Aprational) x).multiply((Aprational) y);
        }

        @Override
        public Number divide(Number x, Number y)
        {
            return ((Aprational) x).divide((Aprational) y);
        }

        @Override
        public Number mod(Number x, Number y)
        {
            return ((Aprational) x).mod((Aprational) y);
        }

        @Override
        protected Number pow(Number x, long y)
        {
            return AprationalMath.pow((Aprational) x, y);
        }

        @Override
        public Number binomial(Number x, Number y)
        {
            return AprationalMath.binomial((Aprational) x, (Aprational) y);
        }

        @Override
        public Number hypot(Number x, Number y)
        {
            return root(add(pow(x, 2), pow(y, 2)), 2);
        }

        @Override
        protected Number root(Number x, long y)
        {
            return new Aprational(root(((Aprational) x).numerator(), y), root(((Aprational) x).denominator(), y));
        }

        private Apint root(Apint x, long y)
        {
            Apint[] root = ApintMath.root(x, y);
            if (root[1].signum() != 0)
            {
                throw new IllegalArgumentException("Cannot calculate inexact root to infinite precision");
            }
            return root[0];
        }

        @Override
        protected Number round(Number x, long precision)
        {
            return AprationalMath.round((Aprational) x, precision, RoundingMode.HALF_EVEN);
        }

        @Override
        protected Number scale(Number x, long y)
        {
            return AprationalMath.scale((Aprational) x, y);
        }
    }

    private static class ApintFunctions
        extends AprationalFunctions
    {
        @Override
        public Number binomial(Number x, Number y)
        {
            if (isLong(x) && isLong(y))
            {
                return ApintMath.binomial(x.longValue(), y.longValue());
            }
            return ApintMath.binomial((Apint) x, (Apint) y);
        }

        @Override
        public Number gcd(Number x, Number y)
        {
            return ApintMath.gcd((Apint) x, (Apint) y);
        }

        @Override
        public Number lcm(Number x, Number y)
        {
            return ApintMath.lcm((Apint) x, (Apint) y);
        }

        @Override
        protected Number scale(Number x, long y)
        {
            return (y >= 0 ? ApintMath.scale((Apint) x, y) : super.scale(x, y));
        }
    }

    /**
     * Default constructor.
     */

    public ApfloatCalculatorImpl()
    {
    }

    @Override
    public Number parseInteger(String value)
    {
        Apcomplex x;
        if (value.equalsIgnoreCase("i"))
        {
            x = Apcomplex.I;
        }
        else if (value.endsWith("i") || value.endsWith("I"))
        {
            x = new Apint(value.substring(0, value.length() - 1)).multiply(Apcomplex.I);
        }
        else
        {
            x = new Apint(value);
        }
        if (getInputPrecision() != null)
        {
            x = x.precision(getInputPrecision());
        }
        return x;
    }

    @Override
    public Number parseDecimal(String value)
    {
        Apcomplex x;
        if (value.endsWith("i") || value.endsWith("I"))
        {
            x = new Apfloat(value.substring(0, value.length() - 1)).multiply(Apcomplex.I);
        }
        else
        {
            x = new Apfloat(value);
        }
        if (getInputPrecision() != null)
        {
            x = x.precision(getInputPrecision());
        }
        return x;
    }

    @Override
    public String format(Number x)
    {
        String text;
        boolean pretty = getFormat();
        if (x instanceof Aprational)
        {
            text = x.toString();
        }
        else if (x instanceof Apfloat)
        {
            Apfloat a = (Apfloat) x;
            a = ApfloatMath.round(a.precision(Util.ifFinite(a.precision(), a.precision() + 1)), a.precision(), RoundingMode.HALF_UP);
            text = a.toString(pretty);
        }
        else
        {
            Apcomplex z = (Apcomplex) x;
            String imag = (z.imag().equals(Apfloat.ONE) ? "" : (z.imag().negate().equals(Apfloat.ONE) ? "-" : format(z.imag())));
            if (z.real().signum() == 0)
            {
                text = imag + "i";
            }
            else
            {
                text = format(z.real()) + (z.imag().signum() < 0 ? "" : "+") + imag + "i";
            }
        }
        return text;
    }

    @Override
    protected Functions getFunctions(Number x)
    {
        Functions functions;
        if (x instanceof Apint)
        {
            functions = new ApintFunctions();
        }
        else if (x instanceof Aprational)
        {
            functions = new AprationalFunctions();
        }
        else if (x instanceof Apfloat)
        {
            functions = new ApfloatFunctions();
        }
        else
        {
            functions = new ApcomplexFunctions();
        }
        return functions;
    }

    @Override
    protected Number promote(Number x)
    {
        if (!(x instanceof Apfloat) && ((Apcomplex) x).imag().signum() == 0)
        {
            // Complex to float
            x = ((Apcomplex) x).real();
        }
        if (x instanceof Apfloat && !(x instanceof Aprational) && ((Apfloat) x).precision() == Apfloat.INFINITE && ((Apfloat) x).isInteger())
        {
            // Float to integer
            x = ((Apfloat) x).truncate();
        }
        if (x instanceof Aprational && !(x instanceof Apint) && ((Aprational) x).denominator().equals(Apint.ONE))
        {
            // Rational to integer
            x = ((Aprational) x).numerator();
        }
        return x;
    }
}
