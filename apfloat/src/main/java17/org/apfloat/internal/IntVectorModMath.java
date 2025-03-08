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

import static org.apfloat.internal.VectorUtil.toDouble;
import static org.apfloat.internal.VectorUtil.toInt;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.IntVector;
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

public class IntVectorModMath
    extends IntModMath
{
    /**
     * Default constructor.
     */

    public IntVectorModMath()
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

    public IntVector modMultiply(IntVector a, IntVector b)
    {
        IntVector r = a.mul(b).sub(toInt(toDouble(a, 0).mul(toDouble(b, 0)).mul(this.inverseModulus), 0).mul(this.modulus))
                              .sub(toInt(toDouble(a, 1).mul(toDouble(b, 1)).mul(this.inverseModulus), -1).mul(this.modulus));

        r = r.sub(this.modulus, r.compare(VectorOperators.UNSIGNED_GE, this.modulus));

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

    public IntVector modAdd(IntVector a, IntVector b)
    {
        IntVector r = a.add(b);

        return r.sub(this.modulus, r.compare(VectorOperators.UNSIGNED_GE, this.modulus));
    }

    /**
     * Modular subtraction. The result is always &gt;= 0.
     *
     * @param a First operand.
     * @param b Second operand.
     *
     * @return <code>(a - b + modulus) % modulus</code>
     */

    public IntVector modSubtract(IntVector a, IntVector b)
    {
        IntVector r = a.sub(b);

        return r.add(this.modulus, r.compare(VectorOperators.LT, 0));
    }

    @Override
    public void setModulus(int modulus)
    {
        super.setModulus(modulus);

        this.modulus = IntVector.broadcast(IntVector.SPECIES_PREFERRED, modulus);
        this.inverseModulus = DoubleVector.broadcast(DoubleVector.SPECIES_PREFERRED, 1.0 / (modulus + 0.5));    // Round down
    }

    private IntVector modulus;
    private DoubleVector inverseModulus;
}
