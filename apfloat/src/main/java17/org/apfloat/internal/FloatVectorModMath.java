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
import static org.apfloat.internal.VectorUtil.toDouble;
import static org.apfloat.internal.VectorUtil.toFloat;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

/**
 * Vector modulo arithmetic functions for <code>float</code> data.<p>
 *
 * All access to this class must be externally synchronized.
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class FloatVectorModMath
    extends FloatModMath
{
    /**
     * Default constructor.
     */

    public FloatVectorModMath()
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

    public FloatVector modMultiply(FloatVector a, FloatVector b)
    {
        DoubleVector r1 = toDouble(a, 0).mul(toDouble(b, 0));
        DoubleVector r2 = toDouble(a, 1).mul(toDouble(b, 1));

        r1 = r1.sub(floor(r1.mul(this.inverseModulus)).mul(this.modulusDouble));
        r2 = r2.sub(floor(r2.mul(this.inverseModulus)).mul(this.modulusDouble));

        return toFloat(r1, 0).add(toFloat(r2, -1));
    }

    /**
     * Modular addition.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a + b) % modulus</code>
     */

    public FloatVector modAdd(FloatVector a, FloatVector b)
    {
        FloatVector r = a.add(b);

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

    public FloatVector modSubtract(FloatVector a, FloatVector b)
    {
        FloatVector r = a.sub(b);

        return r.add(this.modulus, r.compare(VectorOperators.LT, 0));
    }

    @Override
    public void setModulus(float modulus)
    {
        super.setModulus(modulus);

        VectorSpecies<Double> species = DoubleVector.SPECIES_PREFERRED;
        this.modulus = FloatVector.broadcast(FloatVector.SPECIES_PREFERRED, modulus);
        this.modulusDouble = DoubleVector.broadcast(species, modulus);
        this.inverseModulus = DoubleVector.broadcast(species, 1.0 / modulus);
    }

    private FloatVector modulus;
    private DoubleVector modulusDouble;
    private DoubleVector inverseModulus;
}
