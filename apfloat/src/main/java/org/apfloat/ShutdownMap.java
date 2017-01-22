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
package org.apfloat;

import java.util.Map;
import java.util.AbstractMap;
import java.util.Set;

/**
 * Map that always throws <code>ApfloatRuntimeException</code> on all operations.
 * Can be used to replace cache maps after JVM shutdown and clean-up
 * has been initiated to prevent other threads from performing any operations.
 *
 * @since 1.6.2
 * @version 1.6.2
 * @author Mikko Tommila
 */

class ShutdownMap<K, V>
    extends AbstractMap<K, V>
{
    public ShutdownMap()
    {
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
        throw new ApfloatRuntimeException("Shutdown in progress");
    }

    public V put(K key, V value)
    {
        throw new ApfloatRuntimeException("Shutdown in progress");
    }
}
