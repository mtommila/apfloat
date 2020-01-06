/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.apfloat.internal.RawtypeModMath;
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
