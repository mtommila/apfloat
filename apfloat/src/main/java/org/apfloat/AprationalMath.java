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
package org.apfloat;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.apfloat.spi.Util;

/**
 * Various mathematical functions for arbitrary precision rational numbers.
 *
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class AprationalMath
{
    private AprationalMath()
    {
    }

    /**
     * Integer power.
     *
     * @param x Base of the power operator.
     * @param n Exponent of the power operator.
     *
     * @return <code>x</code> to the <code>n</code>:th power, that is <code>x<sup>n</sup></code>.
     *
     * @exception ArithmeticException If both <code>x</code> and <code>n</code> are zero.
     */

    public static Aprational pow(Aprational x, long n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (n == 0)
        {
            if (x.signum() == 0)
            {
                throw new ApfloatArithmeticException("Zero to power zero", "pow.zeroToZero");
            }

            return Apint.ONES[x.radix()];
        }
        else if (n < 0)
        {
            x = Aprational.ONE.divide(x);
            n = -n;
        }

        // Algorithm improvements by Bernd Kellner
        int b2pow = 0;

        while ((n & 1) == 0)
        {
            b2pow++;
            n >>>= 1;
        }

        Aprational r = x;

        while ((n >>>= 1) > 0)
        {
            x = x.multiply(x);
            if ((n & 1) != 0)
            {
                r = r.multiply(x);
            }
        }

        while (b2pow-- > 0)
        {
            r = r.multiply(r);
        }

        return r;
    }

    /**
     * Returns an aprational whose value is <code>-x</code>.
     *
     * @deprecated Use {@link Aprational#negate()}.
     *
     * @param x The argument.
     *
     * @return <code>-x</code>.
     */

    @Deprecated
    public static Aprational negate(Aprational x)
        throws ApfloatRuntimeException
    {
        return x.negate();
    }

    /**
     * Absolute value.
     *
     * @param x The argument.
     *
     * @return Absolute value of <code>x</code>.
     */

    public static Aprational abs(Aprational x)
        throws ApfloatRuntimeException
    {
        if (x.signum() >= 0)
        {
            return x;
        }
        else
        {
            return x.negate();
        }
    }

    /**
     * Copy sign from one argument to another.
     *
     * @param x The value whose sign is to be adjusted.
     * @param y The value whose sign is to be used.
     *
     * @return <code>x</code> with its sign changed to match the sign of <code>y</code>.
     *
     * @since 1.1
     */

    public static Aprational copySign(Aprational x, Aprational y)
        throws ApfloatRuntimeException
    {
        if (y.signum() == 0)
        {
            return y;
        }
        else if (x.signum() != y.signum())
        {
            return x.negate();
        }
        else
        {
            return x;
        }
    }

    /**
     * Multiply by a power of the radix.
     * Note that this method is prone to intermediate overflow errors.
     * Also, scaling by a very large negative number won't result in an
     * underflow and a zero result, but an overflow of the denominator
     * and an exception thrown.
     *
     * @param x The argument.
     * @param scale The scaling factor.
     *
     * @return <code>x * x.radix()<sup>scale</sup></code>.
     */

    public static Aprational scale(Aprational x, long scale)
        throws ApfloatRuntimeException
    {
        if (scale >= 0)
        {
            return new Aprational(ApintMath.scale(x.numerator(), scale), x.denominator());
        }
        else if (scale == 0x8000000000000000L)
        {
            Apint scaler = ApintMath.pow(new Apint(x.radix(), x.radix()), 0x4000000000000000L);
            return new Aprational(x.numerator(), x.denominator().multiply(scaler)).divide(scaler);
        }
        else
        {
            return new Aprational(x.numerator(), ApintMath.scale(x.denominator(), -scale));
        }
    }

    /**
     * Rounds the given number to the specified precision with the specified rounding mode.
     *
     * @deprecated Use {@link #roundToPrecision(Aprational,long,RoundingMode)}.
     *
     * @param x The number to round.
     * @param precision The precision to round to.
     * @param roundingMode The rounding mode to use.
     *
     * @return The rounded number.
     *
     * @exception IllegalArgumentException If <code>precision</code> is less than zero or zero.
     * @exception ArithmeticException If rounding is necessary (result is not exact) and rounding mode is {@link RoundingMode#UNNECESSARY}.
     *
     * @since 1.7.0
     */

    @Deprecated
    public static Apfloat round(Aprational x, long precision, RoundingMode roundingMode)
        throws IllegalArgumentException, ArithmeticException, ApfloatRuntimeException
    {
        return roundToPrecision(x, precision, roundingMode);
    }

    /**
     * Rounds the given number to the specified precision with the specified rounding mode.
     *
     * @param x The number to round.
     * @param precision The precision to round to.
     * @param roundingMode The rounding mode to use.
     *
     * @return The rounded number.
     *
     * @exception IllegalArgumentException If <code>precision</code> is less than zero or zero.
     * @exception ArithmeticException If rounding is necessary (result is not exact) and rounding mode is {@link RoundingMode#UNNECESSARY}.
     *
     * @since 1.11.0
     */

    public static Apfloat roundToPrecision(Aprational x, long precision, RoundingMode roundingMode)
        throws IllegalArgumentException, ArithmeticException, ApfloatRuntimeException
    {
        return RoundingHelper.roundToPrecision(x, precision, roundingMode);
    }

    /**
     * Rounds <code>x</code> to integer using the specified rounding mode.
     *
     * @param x The number to round.
     * @param roundingMode The rounding mode to use.
     *
     * @return The rounded number.
     *
     * @exception ArithmeticException If rounding is necessary (result is not exact) and rounding mode is {@link RoundingMode#UNNECESSARY}.
     *
     * @since 1.11.0
     */

    public static Apint roundToInteger(Aprational x, RoundingMode roundingMode)
        throws IllegalArgumentException, ArithmeticException, ApfloatRuntimeException
    {
        return RoundingHelper.roundToInteger(x, roundingMode);
    }

    /**
     * Rounds <code>x</code> to the specified number of places using the specified rounding mode.
     *
     * @param x The number to round.
     * @param places The number of places to round to (in base 10, the number of decimal places).
     * @param roundingMode The rounding mode to use.
     *
     * @return The rounded number.
     *
     * @exception ArithmeticException If rounding is necessary (result is not exact) and rounding mode is {@link RoundingMode#UNNECESSARY}.
     *
     * @since 1.11.0
     */

    public static Apfloat roundToPlaces(Aprational x, long places, RoundingMode roundingMode)
        throws IllegalArgumentException, ArithmeticException, ApfloatRuntimeException
    {
        return RoundingHelper.roundToPlaces(x, places, roundingMode);
    }

    /**
     * Rounds <code>x</code> to the nearest multiple of <code>y</code> using the specified rounding mode.
     *
     * @param x The number to round.
     * @param y The integer multiple to round to.
     * @param roundingMode The rounding mode to use.
     *
     * @return The rounded number.
     *
     * @exception ArithmeticException If rounding is necessary (result is not exact) and rounding mode is {@link RoundingMode#UNNECESSARY}.
     *
     * @since 1.11.0
     */

    public static Aprational roundToMultiple(Aprational x, Aprational y, RoundingMode roundingMode)
        throws IllegalArgumentException, ArithmeticException, ApfloatRuntimeException
    {
        return RoundingHelper.roundToMultiple(x, y, roundingMode);
    }

    /**
     * Product of numbers.
     * This method may perform significantly better
     * than simply multiplying the numbers sequentially.<p>
     *
     * If there are no arguments, the return value is <code>1</code>.
     *
     * @param x The argument(s).
     *
     * @return The product of the given numbers.
     *
     * @since 1.3
     */

    public static Aprational product(Aprational... x)
        throws ApfloatRuntimeException
    {
        if (x.length == 0)
        {
            return Aprational.ONE;
        }

        Apint[] n = new Apint[x.length],
                m = new Apint[x.length];
        for (int i = 0; i < x.length; i++)
        {
            if (x[i].signum() == 0)
            {
                return Aprational.ZEROS[x[i].radix()];
            }
            n[i] = x[i].numerator();
            m[i] = x[i].denominator();
        }
        return new Aprational(ApintMath.product(n), ApintMath.product(m));
    }

    /**
     * Sum of numbers.
     * This method may perform significantly better
     * than simply adding the numbers sequentially.<p>
     *
     * If there are no arguments, the return value is <code>0</code>.
     *
     * @param x The argument(s).
     *
     * @return The sum of the given numbers.
     *
     * @since 1.3
     */

    public static Aprational sum(Aprational... x)
        throws ApfloatRuntimeException
    {
        if (x.length == 0)
        {
            return Aprational.ZERO;
        }

        // Sort by size
        x = x.clone();

        Arrays.sort(x, Comparator.comparing(ApfloatHelper::size));

        // Recursively add
        return recursiveSum(x, 0, x.length - 1);
    }

    /**
     * Generates the first <code>n</code> terms in the continued fraction representation of <code>x</code>.<p>
     *
     * Note that the result length might be less than <code>n</code>, depending on the input value.
     *
     * @param x The number whose continued fraction terms should be generated.
     * @param n The maximum number of terms to generate.
     *
     * @return The continued fraction.
     *
     * @exception IllegalArgumentException If <code>n</code> is less than one.
     *
     * @since 1.12.0
     */

    public static Apint[] continuedFraction(Aprational x, int n)
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException("Maximum number of terms is not positive");
        }
        return Util.stream(ContinuedFractionHelper.continuedFraction(x)).limit(n).toArray(Apint[]::new);
    }

    /**
     * Generates the first <code>n</code> convergents corresponding to the continued fraction of <code>x</code>.<p>
     *
     * Note that the result length might be less than <code>n</code>, depending on the input value.
     *
     * @param x The number whose continued fraction convergents should be generated.
     * @param n The maximum number of convergents to generate.
     *
     * @return The convergents.
     *
     * @exception IllegalArgumentException If <code>n</code> is less than one.
     *
     * @since 1.12.0
     */

    public static Aprational[] convergents(Aprational x, int n)
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException("Maximum number of convergents is not positive");
        }
        Iterator<Apint> continuedFraction = ContinuedFractionHelper.continuedFraction(x);
        return Util.stream(ContinuedFractionHelper.convergents(continuedFraction, x.radix())).limit(n).toArray(Aprational[]::new);
    }

    /**
     * Returns the greater of the two values.
     *
     * @param x An argument.
     * @param y Another argument.
     *
     * @return The greater of the two values.
     *
     * @since 1.9.0
     */

    public static Aprational max(Aprational x, Aprational y)
        throws ApfloatRuntimeException
    {
        return (x.compareTo(y) > 0 ? x : y);
    }

    /**
     * Returns the smaller of the two values.
     *
     * @param x An argument.
     * @param y Another argument.
     *
     * @return The smaller of the two values.
     *
     * @since 1.9.0
     */

    public static Aprational min(Aprational x, Aprational y)
        throws ApfloatRuntimeException
    {
        return (x.compareTo(y) < 0 ? x : y);
    }

    /**
     * Binomial coefficient.
     *
     * @param n The first argument.
     * @param k The second argument.
     *
     * @return <math xmlns="http://www.w3.org/1998/Math/MathML">
     *           <mrow>
     *             <mo>(</mo>
     *               <mfrac linethickness="0">
     *                 <mi>n</mi>
     *                 <mi>k</mi>
     *               </mfrac>
     *             <mo>)</mo>
     *           </mrow>
     *         </math>
     *
     * @throws ArithmeticException If the result is not finite or not a rational number.
     *
     * @since 1.11.0
     */

    public static Aprational binomial(Aprational n, Aprational k)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (n.isInteger() && k.isInteger())
        {
            return ApintMath.binomial(n.numerator(), k.numerator());
        }
        if (n.isInteger() && n.signum() < 0 && !k.isInteger())
        {
            throw new ApfloatArithmeticException("Binomial coefficient is not finite", "binomial.infinite");
        }
        if (!k.isInteger())
        {
            k = n.subtract(k);
        }
        if (!k.isInteger())
        {
            throw new ApfloatArithmeticException("Binomial coefficient is not a rational number", "binomial.nonRational");
        }
        int radix = n.radix();
        if (k.signum() < 0)
        {
            return Apint.ZEROS[radix];
        }
        Apint one = Apint.ONES[radix],
              f = ApintMath.factorial(ApfloatHelper.longValueExact(k.truncate()), radix);
        return pochhammer(n.subtract(k).add(one), k.numerator()).divide(f);
    }

    /**
     * Pochhammer symbol.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * The asymptotic complexity is at least O(n<sup>2</sup>log&nbsp;n) and it is
     * impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the pochhammer symbol.
     *
     * @param x The first argument.
     * @param n The second argument.
     *
     * @return <code>(x)<sub>n</sub></code>
     *
     * @since 1.13.0
     */

    public static Aprational pochhammer(Aprational x, Apint n)
    {
        Apint one = Apint.ONES[x.radix()];
        if (n.signum() == 0)
        {
            return one;
        }
        if (n.equals(one))
        {
            return x;
        }
        if (n.signum() < 0)
        {
            return one.divide(pochhammer(x.add(n), n.negate()));
        }
        Apint two = new Apint(2, x.radix());
        Apint k = n.divide(two);
        return pochhammer(x, k).multiply(pochhammer(x.add(k), n.subtract(k)));
    }

    /**
     * Returns the specified Bernoulli number. The default radix is used.
     *
     * @param n The argument.
     *
     * @return The Bernoulli number <code>B<sub>n</sub></code>.
     *
     * @throws IllegalArgumentException If <code>n &lt; 0</code>.
     *
     * @since 1.11.0
     */

    public static Aprational bernoulli(long n)
        throws IllegalArgumentException, ApfloatRuntimeException
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int radix = ctx.getDefaultRadix();

        return bernoulli(n, radix);
    }

    /**
     * Returns the specified Bernoulli number in the given radix.
     * 
     * @param n The argument.
     * @param radix The radix.
     *
     * @return The Bernoulli number <code>B<sub>n</sub></code>.
     *
     * @throws IllegalArgumentException If <code>n &lt; 0</code>.
     * @throws NumberFormatException If the radix is not valid.
     *
     * @since 1.11.0
     */

    public static Aprational bernoulli(long n, int radix)
        throws IllegalArgumentException, NumberFormatException, ApfloatRuntimeException
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("Negative Bernoulli number: " + n);
        }

        if (n == 0)
        {
            return Apint.ONES[radix];
        }
        if (n == 1)
        {
            return new Aprational(new Apint(-1, radix), new Apint(2, radix));
        }
        if ((n & 1) == 1)
        {
            return Apint.ZEROS[radix];
        }
        return BernoulliHelper.bernoulli(n, radix);
    }

    /**
     * Harmonic number.<p>
     *
     * @param n The argument.
     *
     * @return <i>H<sub>n</sub></i>
     *
     * @throws ArithmeticException If <code>n</code> is negative.
     *
     * @since 1.14.0
     */

    public static Aprational harmonicNumber(Apint n)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return harmonicNumber(n, Apint.ONES[n.radix()]);
    }

    /**
     * Generalized harmonic number.<p>
     *
     * @param n The first argument.
     * @param r The second argument.
     *
     * @return <i>H<sub>n</sub><sup style='position: relative; left: -0.4em;'>(r)</sup></i>
     *
     * @throws ArithmeticException If <code>n</code> is negative and <code>r</code> is positive.
     *
     * @since 1.14.0
     */

    public static Aprational harmonicNumber(Apint n, Apint r)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (n.signum() == 0 || r.signum() == 0)
        {
            return n;
        }
        Apint one = Apint.ONES[n.radix()];
        if (n.signum() < 0)
        {
            if (r.signum() > 0)
            {
                throw new ApfloatArithmeticException("Negative harmonic number", "harmonicNumber.negative");
            }
            return harmonicNumber(n.negate().subtract(one), r).negate();
        }
        Apint[] h = harmonicNumber(one, n, ApfloatHelper.longValueExact(r));
        return new Aprational(h[0], h[1]);
    }

    private static Apint[] harmonicNumber(Apint n, Apint m, long r)
        throws ArithmeticException, ApfloatRuntimeException
    {
        int radix = n.radix();
        Apint one = Apint.ONES[radix];
        if (n.equals(m))
        {
            if (r >= 0)
            {
                Apint[] h = { one, ApintMath.pow(n, r) };
                return h;
            }
            else
            {
                Apint[] h = { ApintMath.pow(n, -r), one };
                return h;
            }
        }
        Apint k = n.add(m).divide(new Apint(2, radix));
        Apint[] hl = harmonicNumber(n, k, r),
                hh = harmonicNumber(k.add(one), m, r),
                h = { hl[0].multiply(hh[1]).add(hl[1].multiply(hh[0])), hl[1].multiply(hh[1]) };
        return h;
    }

    private static Aprational recursiveSum(Aprational[] x, int n, int m)
        throws ApfloatRuntimeException
    {
        if (n == m)
        {
            return x[n];
        }
        else
        {
            int k = (n + m) >>> 1;
            return recursiveSum(x, n, k).add(recursiveSum(x, k + 1, m));
        }
    }
}
