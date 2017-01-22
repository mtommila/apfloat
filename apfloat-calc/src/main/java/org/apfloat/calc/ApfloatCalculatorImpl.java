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

/**
 * Arbitrary precision calculator implementation.
 *
 * @version 1.8.1
 * @author Mikko Tommila
 */

public class ApfloatCalculatorImpl
    extends FunctionCalculatorImpl
{
    private static class ApcomplexFunctions
        implements Functions
    {
        public Number negate(Number x)
        {
            return ((Apcomplex) x).negate();
        }

        public Number add(Number x, Number y)
        {
            return ((Apcomplex) x).add((Apcomplex) y);
        }

        public Number subtract(Number x, Number y)
        {
            return ((Apcomplex) x).subtract((Apcomplex) y);
        }

        public Number multiply(Number x, Number y)
        {
            return ((Apcomplex) x).multiply((Apcomplex) y);
        }

        public Number divide(Number x, Number y)
        {
            return ((Apcomplex) x).divide((Apcomplex) y);
        }

        public Number mod(Number x, Number y)
        {
            throw new IllegalArgumentException("Modulus can only be used with scalar values");
        }

        public Number pow(Number x, Number y)
        {
            if (isLong(y))
            {
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

        public Number arg(Number x)
        {
            return ApcomplexMath.arg((Apcomplex) x);
        }

        public Number conj(Number x)
        {
            return ((Apcomplex) x).conj();
        }

        public Number imag(Number x)
        {
            return ((Apcomplex) x).imag();
        }

        public Number real(Number x)
        {
            return ((Apcomplex) x).real();
        }

        public Number abs(Number x)
        {
            return ApcomplexMath.abs((Apcomplex) x);
        }

        public Number acos(Number x)
        {
            return ApcomplexMath.acos((Apcomplex) x);
        }

        public Number acosh(Number x)
        {
            return ApcomplexMath.acosh((Apcomplex) x);
        }

        public Number asin(Number x)
        {
            return ApcomplexMath.asin((Apcomplex) x);
        }

        public Number asinh(Number x)
        {
            return ApcomplexMath.asinh((Apcomplex) x);
        }

        public Number atan(Number x)
        {
            return ApcomplexMath.atan((Apcomplex) x);
        }

        public Number atanh(Number x)
        {
            return ApcomplexMath.atanh((Apcomplex) x);
        }

        public Number cbrt(Number x)
        {
            return root(x, 3);
        }

        public Number ceil(Number x)
        {
            throw new IllegalArgumentException("Ceiling can only be used with scalar values");
        }

        public Number cos(Number x)
        {
            return ApcomplexMath.cos((Apcomplex) x);
        }

        public Number cosh(Number x)
        {
            return ApcomplexMath.cosh((Apcomplex) x);
        }

        public Number exp(Number x)
        {
            return ApcomplexMath.exp((Apcomplex) x);
        }

        public Number factorial(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Factorial can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApintMath.factorial(n);
        }

        public Number floor(Number x)
        {
            throw new IllegalArgumentException("Floor can only be used with scalar values");
        }

        public Number frac(Number x)
        {
            throw new IllegalArgumentException("Frac can only be used with scalar values");
        }

        public Number log(Number x)
        {
            return ApcomplexMath.log((Apcomplex) x);
        }

        public Number log(Number x, Number y)
        {
            return ApcomplexMath.log((Apcomplex) x, (Apcomplex) y);
        }

        public Number pi(Number x)
        {
            if (!isLong(x))
            {
                throw new IllegalArgumentException("Pi can only be used with a valid integer argument");
            }
            long n = x.longValue();
            return ApfloatMath.pi(n);
        }

        public Number round(Number x, Number y)
        {
            throw new IllegalArgumentException("Round can only be used with scalar values");
        }

        public Number sin(Number x)
        {
            return ApcomplexMath.sin((Apcomplex) x);
        }

        public Number sinh(Number x)
        {
            return ApcomplexMath.sinh((Apcomplex) x);
        }

        public Number sqrt(Number x)
        {
            return root(x, 2);
        }

        public Number tan(Number x)
        {
            return ApcomplexMath.tan((Apcomplex) x);
        }

        public Number tanh(Number x)
        {
            return ApcomplexMath.tanh((Apcomplex) x);
        }

        public Number truncate(Number x)
        {
            throw new IllegalArgumentException("Truncate can only be used with scalar values");
        }

        public Number toDegrees(Number x)
        {
            throw new IllegalArgumentException("ToDegrees can only be used with scalar values");
        }

        public Number toRadians(Number x)
        {
            throw new IllegalArgumentException("ToRadians can only be used with scalar values");
        }

        public Number agm(Number x, Number y)
        {
            return ApcomplexMath.agm((Apcomplex) x, (Apcomplex) y);
        }

        public Number w(Number x)
        {
            return ApcomplexMath.w((Apcomplex) x);
        }

        public Number w(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Lambert W can only be used with a valid integer argument");
            }
            return ApcomplexMath.w((Apcomplex) x, y.longValue());
        }

        public Number atan2(Number x, Number y)
        {
            throw new IllegalArgumentException("Atan2 can only be used with scalar values");
        }

        public Number copySign(Number x, Number y)
        {
            throw new IllegalArgumentException("CopySign can only be used with scalar values");
        }

        public Number fmod(Number x, Number y)
        {
            throw new IllegalArgumentException("Fmod can only be used with scalar values");
        }

        public Number gcd(Number x, Number y)
        {
            throw new IllegalArgumentException("Greatest Common Ddivisor can only be used with integer values");
        }

        public Number hypot(Number x, Number y)
        {
            throw new IllegalArgumentException("Hypot can only be used with scalar values");
        }

        public Number inverseRoot(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Inverse root can only be used with a valid integer argument");
            }
            return inverseRoot(x, y.longValue());
        }

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

        public Number lcm(Number x, Number y)
        {
            throw new IllegalArgumentException("Least Common Multiplier can only be used with integer values");
        }

        public Number root(Number x, Number y)
        {
            if (!isLong(y))
            {
                throw new IllegalArgumentException("Root can only be used with a valid integer argument");
            }
            return root(x, y.longValue());
        }

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
        public Number mod(Number x, Number y)
        {
            return ((Apfloat) x).mod((Apfloat) y);
        }

        public Number ceil(Number x)
        {
            return ((Apfloat) x).ceil();
        }

        public Number floor(Number x)
        {
            return ((Apfloat) x).floor();
        }

        public Number frac(Number x)
        {
            return ((Apfloat) x).frac();
        }

        public Number truncate(Number x)
        {
            return ((Apfloat) x).truncate();
        }

        public Number toDegrees(Number x)
        {
            return ApfloatMath.toDegrees((Apfloat) x);
        }

        public Number toRadians(Number x)
        {
            return ApfloatMath.toRadians((Apfloat) x);
        }

        public Number atan2(Number x, Number y)
        {
            return ApfloatMath.atan2((Apfloat) x, (Apfloat) y);
        }

        public Number copySign(Number x, Number y)
        {
            return ApfloatMath.copySign((Apfloat) x, (Apfloat) y);
        }

        public Number fmod(Number x, Number y)
        {
            return ApfloatMath.fmod((Apfloat) x, (Apfloat) y);
        }

        public Number hypot(Number x, Number y)
        {
            return ApcomplexMath.abs(new Apcomplex((Apfloat) x, (Apfloat) y));
        }

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
        public Number add(Number x, Number y)
        {
            return ((Aprational) x).add((Aprational) y);
        }

        public Number subtract(Number x, Number y)
        {
            return ((Aprational) x).subtract((Aprational) y);
        }

        public Number multiply(Number x, Number y)
        {
            return ((Aprational) x).multiply((Aprational) y);
        }

        public Number divide(Number x, Number y)
        {
            return ((Aprational) x).divide((Aprational) y);
        }

        public Number mod(Number x, Number y)
        {
            return ((Aprational) x).mod((Aprational) y);
        }

        protected Number pow(Number x, long y)
        {
            return AprationalMath.pow((Aprational) x, y);
        }

        public Number hypot(Number x, Number y)
        {
            return root(add(pow(x, 2), pow(y, 2)), 2);
        }

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

        protected Number round(Number x, long precision)
        {
            return AprationalMath.round((Aprational) x, precision, RoundingMode.HALF_EVEN);
        }

        protected Number scale(Number x, long y)
        {
            return AprationalMath.scale((Aprational) x, y);
        }
    }

    private static class ApintFunctions
        extends AprationalFunctions
    {
        public Number gcd(Number x, Number y)
        {
            return ApintMath.gcd((Apint) x, (Apint) y);
        }

        public Number lcm(Number x, Number y)
        {
            return ApintMath.lcm((Apint) x, (Apint) y);
        }

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

    public Number parseInteger(String value)
    {
        Number x;
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
        return x;
    }

    public Number parseDecimal(String value)
    {
        Number x;
        if (value.endsWith("i") || value.endsWith("I"))
        {
            x = new Apfloat(value.substring(0, value.length() - 1)).multiply(Apcomplex.I);
        }
        else
        {
            x = new Apfloat(value);
        }
        return x;
    }

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
            text = ((Apfloat) x).toString(pretty);
        }
        else
        {
            Apcomplex z = (Apcomplex) x;
            String imag = (z.imag().equals(Apfloat.ONE) ? "" : (z.imag().negate().equals(Apfloat.ONE) ? "-" : z.imag().toString(pretty)));
            if (z.real().signum() == 0)
            {
                text = imag + "i";
            }
            else
            {
                text = z.real().toString(pretty) + (z.imag().signum() < 0 ? "" : "+") + imag + "i";
            }
        }
        return text;
    }

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

    protected Number promote(Number x)
    {
        if (!(x instanceof Apfloat) && ((Apcomplex) x).imag().signum() == 0)
        {
            // Complex to float
            x = ((Apcomplex) x).real();
        }
        if (x instanceof Apfloat && !(x instanceof Aprational) && ((Apfloat) x).precision() == Apfloat.INFINITE && ((Apfloat) x).frac().signum() == 0)
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
