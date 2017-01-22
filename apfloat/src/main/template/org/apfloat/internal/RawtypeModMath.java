/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.apfloat.internal;

/**
 * Modulo arithmetic functions for <code>rawtype</code> data.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeModMath
    extends RawtypeElementaryModMath
{
    /**
     * Default constructor.
     */

    public RawtypeModMath()
    {
    }

    /**
     * Create a table of powers of n:th root of unity.
     *
     * @param w The n:th root of unity modulo the current modulus.
     * @param n The table length (= transform length).
     *
     * @return Table of <code>table[i]=w<sup>i</sup> mod m</code>, i = 0, ..., n-1.
     */

    public final rawtype[] createWTable(rawtype w, int n)
    {
        rawtype[] wTable = new rawtype[n];
        rawtype wTemp = 1;

        for (int i = 0; i < n; i++)
        {
            wTable[i] = wTemp;
            wTemp = modMultiply(wTemp, w);
        }

        return wTable;
    }

    /**
     * Get forward n:th root of unity. This is <code>w</code>.<p>
     *
     * Assumes that the modulus is prime.
     *
     * @param primitiveRoot Primitive root of the modulus.
     * @param n The transform length.
     *
     * @return Forward n:th root of unity.
     */

    public rawtype getForwardNthRoot(rawtype primitiveRoot, long n)
    {
        return modPow(primitiveRoot, getModulus() - 1 - (getModulus() - 1) / (rawtype) n);
    }

    /**
     * Get inverse n:th root of unity. This is <code>w<sup>-1</sup></code>.<p>
     *
     * Assumes that the modulus is prime.
     *
     * @param primitiveRoot Primitive root of the modulus.
     * @param n The transform length.
     *
     * @return Inverse n:th root of unity.
     */

    public rawtype getInverseNthRoot(rawtype primitiveRoot, long n)
    {
        return modPow(primitiveRoot, (getModulus() - 1) / (rawtype) n);
    }

    /**
     * Modular inverse, that is <code>1 / a</code>. Assumes that the modulus is prime.
     *
     * @param a The operand.
     *
     * @return <code>a<sup>-1</sup> mod m</code>.
     */

    public final rawtype modInverse(rawtype a)
    {
        return modPow(a, getModulus() - 2);
    }

    /**
     * Modular division. Assumes that the modulus is prime.
     *
     * @param a The dividend.
     * @param b The divisor.
     *
     * @return <code>a*b<sup>-1</sup> mod m</code>.
     */

    public final rawtype modDivide(rawtype a, rawtype b)
    {
        return modMultiply(a, modInverse(b));
    }

    /**
     * Modular negation.
     *
     * @param a The argument.
     *
     * @return <code>-a mod m</code>.
     */

    public final rawtype negate(rawtype a)
    {
        return (a == 0 ? 0 : getModulus() - a);
    }

    /**
     * Modular power. Assumes that the modulus is prime.
     *
     * @param a The base.
     * @param n The exponent.
     *
     * @return <code>a<sup>n</sup> mod m</code>.
     */

    public final rawtype modPow(rawtype a, rawtype n)
    {
        assert (a != 0 || n != 0);

        if (n == 0)
        {
            return 1;
        }
        else if (n < 0)
        {
            return modPow(a, getModulus() - 1 + n);
        }

        long exponent = (long) n;

        while ((exponent & 1) == 0)
        {
            a = modMultiply(a, a);
            exponent >>= 1;
        }

        rawtype r = a;

        while ((exponent >>= 1) > 0)
        {
            a = modMultiply(a, a);
            if ((exponent & 1) != 0)
            {
                r = modMultiply(r, a);
            }
        }

        return r;
    }
}
