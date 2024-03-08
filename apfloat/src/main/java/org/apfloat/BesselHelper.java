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
package org.apfloat;

import java.math.RoundingMode;
import java.util.function.BiFunction;

import org.apfloat.spi.Util;

import static org.apfloat.ApcomplexMath.cos;
import static org.apfloat.ApcomplexMath.exp;
import static org.apfloat.ApcomplexMath.hypergeometric0F1Regularized;
import static org.apfloat.ApcomplexMath.pow;
import static org.apfloat.ApcomplexMath.sin;
import static org.apfloat.ApfloatMath.pi;
import static org.apfloat.ApfloatMath.scale;
import static org.apfloat.ApfloatMath.sqrt;

/**
 * Helper class for Bessel functions.
 *
 * @since 1.13.0
 * @version 1.14.0
 * @author Mikko Tommila
 */

class BesselHelper
{
    /**
     * Helper for Bessel functions.<p>
     *
     * @implNote
     * This implementation is <i>slow</i>, meaning that it isn't a <i>fast algorithm</i>.
     * It is impractically slow beyond a precision of a few thousand digits. At the time of
     * implementation no generic fast algorithm is known for the functions.
     *
     * @param ν The first argument.
     * @param z The second argument.
     */

    private BesselHelper(Apcomplex ν, Apcomplex z)
    {
        this.radix = z.radix();
        this.targetPrecision = Math.min(ν.precision(), z.precision());
        this.workingPrecision = ApfloatHelper.extendPrecision(targetPrecision, ApfloatHelper.getSmallExtraPrecision(radix));
        this.ν = ensurePrecision(ν);
        this.z = ensurePrecision(z);
        this.two = new Apint(2, radix);
    }

    public static Apcomplex besselJ(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return new BesselHelper(ν, z).besselJ();
    }

    public static Apcomplex besselI(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return new BesselHelper(ν, z).besselI();
    }

    public static Apcomplex besselY(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return new BesselHelper(ν, z).besselY();
    }

    public static Apcomplex besselK(Apcomplex ν, Apcomplex z)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return new BesselHelper(ν, z).besselK();
    }

    private Apcomplex besselJ()
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex result = besselJ(ν);
        return ApfloatHelper.reducePrecision(result, ApfloatHelper.getSmallExtraPrecision(radix));
    }

    private Apcomplex besselI()
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex result = besselI(ν);
        return ApfloatHelper.reducePrecision(result, ApfloatHelper.getSmallExtraPrecision(radix));
    }

    private Apcomplex besselJ(Apcomplex ν)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return besselFirstKind(ν, true);
    }

    private Apcomplex besselI(Apcomplex ν)
        throws ArithmeticException, ApfloatRuntimeException
    {
        return besselFirstKind(ν, false);
    }

    private Apcomplex besselFirstKind(Apcomplex ν, boolean negate)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (ν.isInteger() && z.isZero())
        {
            return (ν.real().signum() == 0 ? Apint.ONES[radix] : z);
        }
        Apfloat one = Apint.ONES[radix].precision(ApfloatHelper.extendPrecision(workingPrecision, 1));
        Apcomplex z2 = z.divide(two),
                  z24 = z2.multiply(z2);
        return pow(z2, ν).multiply(hypergeometric0F1Regularized(ensurePrecision(ν.add(one)), negate ? z24.negate() : z24));
    }

    private Apcomplex besselY()
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isZero())
        {
            throw new ArithmeticException("Bessel Y of zero");
        }
        return besselSecondKind((ν, z) ->
        {
            Apcomplex piν = pi(workingPrecision, radix).multiply(ν);

            return besselJ(ν).multiply(cos(piν)).subtract(besselJ(ν.negate())).divide(sin(piν));
        });
    }

    private Apcomplex besselK()
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (z.isZero())
        {
            throw new ArithmeticException("Bessel K of zero");
        }
        Apint one = Apint.ONES[radix];
        Apfloat half = one.precision(workingPrecision).divide(two);
        Apcomplex ν12 = ensurePrecision(ν.add(half)),
                  ν21 = ensurePrecision(two.multiply(ν).add(one)),
                  z2 = two.multiply(z),
                  u = HypergeometricHelper.hypergeometricU(ν12, ν21, z2, true);
        if (u != null)
        {
            Apfloat pi = pi(workingPrecision, radix);
            Apcomplex result = sqrt(pi).multiply(pow(z2, ν)).multiply(exp(z.negate())).multiply(u);
            return ApfloatHelper.limitPrecision(result, targetPrecision);
        }

        return besselSecondKind((ν, z) ->
        {
            Apfloat pi = pi(workingPrecision, radix);

            return besselI(ν.negate()).subtract(besselI(ν)).multiply(pi).divide(sin(pi.multiply(ν)).multiply(two));
        });
    }

    private Apcomplex besselSecondKind(BiFunction<Apcomplex, Apcomplex, Apcomplex> f)
        throws ArithmeticException, ApfloatRuntimeException
    {
        // First check if ν is an integer or near-integer and adjust if necessary
        if (ν.isInteger())
        {
            long digitLoss = workingPrecision;
            workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
            Apfloat offset = offset(-digitLoss);
            ν = new Apcomplex(ν.real().precision(Apfloat.INFINITE).add(offset), ν.imag());
        }
        else
        {
            Apint νRounded = RoundingHelper.roundToInteger(ν.real(), RoundingMode.HALF_EVEN).truncate();
            long digitLoss = Math.min(workingPrecision, -ν.subtract(νRounded).scale());
            if (digitLoss > 0)
            {
                workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + digitLoss);
            }
        }

        // Evaluate two other Bessel functions, this often results in cancellation of significant digits so we may retry with higher precision
        Apcomplex result;
        long precisionLoss;

        do
        {
            ν = ensurePrecision(ν);
            z = ensurePrecision(z);

            result = f.apply(ν, z);

            if (result.isZero()) // The result shouldn't be exactly zero, it means full loss of significant digits
            {
                precisionLoss = workingPrecision;
            }
            else
            {
                precisionLoss = targetPrecision - result.precision();
            }
            workingPrecision = Util.ifFinite(workingPrecision, workingPrecision + precisionLoss);
        } while (precisionLoss > 0);
        return ApfloatHelper.limitPrecision(result, targetPrecision);
    }

    private Apfloat offset(long scale)
    {
        Apfloat offset = scale(new Apfloat("0.1", workingPrecision, radix), scale);
        return offset;
    }

    private Apcomplex ensurePrecision(Apcomplex z)
    {
        return ApfloatHelper.ensurePrecision(z, workingPrecision);
    }

    private long targetPrecision,
                 workingPrecision;
    private int radix;
    private Apcomplex ν,
                      z;
    private Apint two;
}
