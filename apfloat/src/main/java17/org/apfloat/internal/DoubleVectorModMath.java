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

import static org.apfloat.internal.VectorUtil.floor;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

/**
 * Vector modulo arithmetic functions for <code>double</code> data.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class DoubleVectorModMath
    extends DoubleModMath
{
    /**
     * Default constructor.
     */

    public DoubleVectorModMath()
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

    public DoubleVector modMultiply(DoubleVector a, DoubleVector b)
    {
        DoubleVector c = floor(a.mul(b).mul(this.inverseModulus));
        DoubleVector d = this.modulus;

        DoubleVector w = c.mul(d);
        DoubleVector r = a.fma(b, w.neg()).add(c.fma(d.neg(), w));

        r = r.sub(this.modulus, r.compare(VectorOperators.GE, this.modulus));

        return r;
    }

    /**
     * Modular addition.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a + b) % modulus</code>
     */

    public DoubleVector modAdd(DoubleVector a, DoubleVector b)
    {
        DoubleVector r = a.add(b);

        return r.sub(this.modulus, r.compare(VectorOperators.GE, this.modulus));
    }

    /**
     * Modular subtraction. The result is always &gt;= 0.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a - b + modulus) % modulus</code>
     */

    public DoubleVector modSubtract(DoubleVector a, DoubleVector b)
    {
        DoubleVector r = a.sub(b);

        return r.add(this.modulus, r.compare(VectorOperators.LT, 0));
    }

    @Override
    public void setModulus(double modulus)
    {
        super.setModulus(modulus);

        VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
        this.modulus = DoubleVector.broadcast(species, modulus);
        this.inverseModulus = DoubleVector.broadcast(species, 1.0 / (modulus + 0.5));   // Round down
    }

    private DoubleVector modulus;
    private DoubleVector inverseModulus;
}
