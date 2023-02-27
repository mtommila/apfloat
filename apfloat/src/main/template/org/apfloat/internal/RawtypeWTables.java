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
package org.apfloat.internal;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static org.apfloat.internal.RawtypeModConstants.*;

/**
 * Helper class for generating and caching tables of powers of the n:th root of unity.
 *
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeWTables
    extends RawtypeModMath
{
    private RawtypeWTables()
    {
        // Default constructor
    }

    /**
     * Get a table of powers of n:th root of unity.
     *
     * @param modulus The index of the modulus to be used.
     * @param length The length of the table to be returned, i.e. n.
     *
     * @return The table of powers of the n:th root of unity.
     */

    public static rawtype[] getWTable(int modulus, int length)
    {
        return getWTable(modulus, length, false);
    }

    /**
     * Get a table of inverses of powers of n:th root of unity.
     *
     * @param modulus The index of the modulus to be used.
     * @param length The length of the table to be returned, i.e. n.
     *
     * @return The table of inverses of powers of the n:th root of unity.
     */

    public static rawtype[] getInverseWTable(int modulus, int length)
    {
        return getWTable(modulus, length, true);
    }

    private static rawtype[] getWTable(int modulus, int length, boolean isInverse)
    {
        List<Integer> key = Arrays.asList(isInverse ? 1 : 0, modulus, length);
        rawtype[] wTable = RawtypeWTables.cache.get(key);
        // Do not synchronize, multiple threads may do this at the same time, but only one gets to put the value in the cache
        if (wTable == null)
        {
            RawtypeModMath instance = getInstance(modulus);
            rawtype w = (isInverse ?
                         instance.getInverseNthRoot(PRIMITIVE_ROOT[modulus], length) :  // Inverse n:th root
                         instance.getForwardNthRoot(PRIMITIVE_ROOT[modulus], length));  // Forward n:th root
            wTable = instance.createWTable(w, length);
            // Check if another thread already put the wTable in the cache; if so then use it
            rawtype[] value = RawtypeWTables.cache.putIfAbsent(key, wTable);
            if (value != null)
            {
                // Another thread did put the value in the cache so use it
                wTable = value;
            }
        }
        return wTable;
    }

    private static RawtypeModMath getInstance(int modulus)
    {
        RawtypeModMath instance = new RawtypeModMath();
        instance.setModulus(MODULUS[modulus]);
        return instance;
    }

    // With inverses, three moduli and lengths being powers of two, the theoretical maximum map size is 2 * 3 * 30 = 180 entries
    private static ConcurrentMap<List<Integer>, rawtype[]> cache = new ConcurrentSoftHashMap<>();
}
