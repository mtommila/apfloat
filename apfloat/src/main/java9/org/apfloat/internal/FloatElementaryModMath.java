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
package org.apfloat.internal;

/**
 * Elementary modulo arithmetic functions for <code>float</code> data.
 * Note that although a floating-point data type is used, the data
 * will always be integers.<p>
 *
 * Modular subtraction is trivial, but when using only the <code>float</code>,
 * type, modular addition must cater for intermediate overflow since
 * the moduli are close to 2<sup>24</sup>.<p>
 *
 * Modular multiplication is more complicated, and since it is usually
 * the single most time consuming operation in the whole program execution,
 * the very core of the Number Theoretic Transform (NTT), it should be
 * carefully optimized.<p>
 *
 * The algorithm for multiplying two <code>float</code>s containing an
 * integer and taking the remainder is not entirely obvious, especially
 * if we only want to use the <code>float</code> type. The basic problem
 * is to get the full 48-bit result of multiplying two 24-bit integers.
 * With the help of the {@link Math#fma(float, float, float)} operation, it
 * can be however done efficiently with a few tricks.<p>
 *
 * The first observation is that since the modulus is practically
 * constant, it should be more efficient to calculate (once) the inverse
 * of the modulus, and then subsequently multiply by the inverse of the
 * modulus instead of dividing by it.<p>
 *
 * The second observation is that to get the remainder of the division,
 * we don't necessarily need the actual result of the division (we just
 * want the remainder). So, we should discard the topmost 24 bits of the
 * full 48-bit result whenever possible, to save a few operations.<p>
 *
 * The basic approach is to get an approximation of <code>a * b / modulus</code>
 * (using floating-point operands, that is <code>float</code>s). The approximation
 * should be within +1 or -1 of the correct result. Then calculate
 * <code>a * b - approximateDivision * modulus</code> to get the remainder.
 * This calculation must use the lowest 24 (or more) bits and is done with the help
 * of the FMA (fused multiply-add) operation and Kahan's algorithm.<p>
 *
 * However, the algorithm only has an accuracy of 1.5 ulp so it doesn't work if
 * all arguments have 24 bits. A trick that can be employed is to first scale
 * the operands to be between -modulus / 2 and modulus / 2, effectively giving
 * one more bit of precision. Then the result can be calculated correctly with
 * the 1.5 ulp accuracty of the algorithm, as the modulus is less than
 * 2<sup>24</sup>. Finally, it is easy to detect the case when the approximate
 * division was off by one (and the remainder is <code>&#177;modulus</code> off).<p>
 *
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class FloatElementaryModMath
{
    /**
     * Default constructor.
     */

    public FloatElementaryModMath()
    {
    }

    /**
     * Modular multiplication.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>a * b % modulus</code>
     */

    public final float modMultiply(float a, float b)
    {
        a = (a > this.halfModulus ? a - this.modulus : a);
        b = (b > this.halfModulus ? b - this.modulus : b);
        float c = Math.round(a * b * this.inverseModulus);
        float d = this.modulus;
        /*
        Compute a * b - c * d with error <= 1.5 ulp.

        Claude-Pierre Jeannerod, Nicolas Louvet, and Jean-Michel Muller,
        "Further Analysis of Kahan's Algorithm for the Accurate Computation 
        of 2x2 Determinants", Mathematics of Computation, Vol. 82, No. 284, 
        Oct. 2013, pp. 2245-2264

        https://ens-lyon.hal.science/ensl-00649347v4
        */
        float w = d * c;
        float r = Math.fma(a, b, -w) + Math.fma(c, -d, w);

        return (r < 0.0f ? r + this.modulus : r);
    }

    /**
     * Modular addition.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a + b) % modulus</code>
     */

    public final float modAdd(float a, float b)
    {
        float r = a - this.modulus + b;

        return (r < 0.0f ? a + b : r);
    }

    /**
     * Modular subtraction. The result is always &gt;= 0.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a - b + modulus) % modulus</code>
     */

    public final float modSubtract(float a, float b)
    {
        float r = a - b;

        return (r < 0.0f ? r + this.modulus : r);
    }

    /**
     * Get the modulus.
     *
     * @return The modulus.
     */

    public final float getModulus()
    {
        return this.modulus;
    }

    /**
     * Set the modulus.
     *
     * @param modulus The modulus.
     */

    public void setModulus(float modulus)
    {
        this.inverseModulus = 1.0f / modulus;
        this.halfModulus = modulus * 0.5f;
        this.modulus = modulus;
    }

    private float inverseModulus;
    private float halfModulus;
    private float modulus;
}
