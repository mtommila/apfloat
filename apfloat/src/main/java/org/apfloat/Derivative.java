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

import java.util.function.BiFunction;
import java.util.function.Function;

import org.apfloat.spi.Util;

/**
 * Helper class for calculating numerical derivatives.
 *
 * @since 1.16.0
 * @version 1.16.0
 * @author Mikko Tommila
 */

class Derivative
{
    private Derivative()
    {
    }

    /**
     * Calculate the numerical derivative of the function.
     *
     * @param f The function.
     * @param z The point where to calculate the derivative.
     *
     * @return <math xmlns="http://www.w3.org/1998/Math/MathML">
     *           <msup>
     *             <mi>f</mi>
     *             <mo>&prime;</mo>
     *           </msup>
     *           <mrow>
     *             <mo>(</mo>
     *             <mi>z</mi>
     *             <mo>)</mo>
     *           </mrow>
     *         </math>
     */

    public static Apcomplex derivative(Function<Apcomplex, Apcomplex> f, Apcomplex z)
    {
        int radix = z.radix();
        long precision = z.precision(),
             twicePrecision = Util.ifFinite(precision, precision + precision);
        Apcomplex result = null;
        do
        {
            try
            {
                Apfloat h = new Apfloat("0.1", precision, radix).scale(Util.subtractExact(z.scale(), precision));
                z = ApfloatHelper.ensurePrecision(z, twicePrecision);
                result = f.apply(z.add(h)).subtract(f.apply(z.subtract(h))).divide(h.add(h));
            }
            catch (LossOfPrecisionException lope)
            {
                precision = twicePrecision;
                twicePrecision = Util.ifFinite(precision, precision + precision);
            }
        } while (result == null);
        return ApfloatHelper.limitPrecision(result, precision);
    }

    /**
     * Numerical partial derivative with respect to the first argument.
     *
     * @param f The function.
     * @param z The first argument.
     * @param w The second argument.
     *
     * @return <math xmlns="http://www.w3.org/1998/Math/MathML">
     *           <mfrac>
     *             <mrow>
     *               <mo>&part;</mo>
     *               <mi>f</mi>
     *               <mo>(</mo>
     *               <mi>z</mi>
     *               <mo>,</mo>
     *               <mi>w</mi>
     *               <mo>)</mo>
     *             </mrow>
     *             <mrow>
     *               <mo>&part;</mo>
     *               <mi>z</mi>
     *             </mrow>
     *           </mfrac>
     *         </math>
     */

    public static Apcomplex partialDerivative(BiFunction<Apcomplex, Apcomplex, Apcomplex> f, Apcomplex z, Apcomplex w)
    {
        int radix = z.radix();
        long precision = Math.min(z.precision(), w.precision()),
             twicePrecision = Util.ifFinite(precision, precision + precision);
        Apcomplex result = null;
        do
        {
            try
            {
                Apfloat h = new Apfloat("0.1", precision, radix).scale(Util.subtractExact(w.scale(), precision));
                z = ApfloatHelper.ensurePrecision(z, twicePrecision);
                w = ApfloatHelper.ensurePrecision(w, twicePrecision);
                result = f.apply(z.add(h), w).subtract(f.apply(z.subtract(h), w)).divide(h.add(h));
            }
            catch (LossOfPrecisionException lope)
            {
                precision = twicePrecision;
                twicePrecision = Util.ifFinite(precision, precision + precision);
            }
        } while (result == null);
        return ApfloatHelper.limitPrecision(result, precision);
    }
}
