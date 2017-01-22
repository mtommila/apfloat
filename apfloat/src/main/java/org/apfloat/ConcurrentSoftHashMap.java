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

import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map with an underlying ConcurrentHashMap with softly referenced values.
 * The maximum map size is assumed to be limited so no effort is made to
 * expunge entries for stale values.
 *
 * @since 1.6
 * @version 1.6
 * @author Mikko Tommila
 */

class ConcurrentSoftHashMap<K, V>
    extends AbstractMap<K, V>
{
    private ConcurrentHashMap<K, SoftReference<V>> map;

    public ConcurrentSoftHashMap()
    {
        this.map = new ConcurrentHashMap<K, SoftReference<V>>();
    }

    public void clear()
    {
        this.map.clear();
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
        throw new UnsupportedOperationException();
    }

    public V get(Object key)
    {
        return unwrap(this.map.get(key));
    }

    public V put(K key, V value)
    {
        return unwrap(this.map.put(key, wrap(value)));
    }

    public V remove(Object key)
    {
        return unwrap(this.map.remove(key));
    }

    public int size()
    {
        return this.map.size();
    }

    private SoftReference<V> wrap(V value)
    {
        return new SoftReference<V>(value);
    }

    private V unwrap(SoftReference<V> value)
    {
        return (value == null ? null : value.get());
    }
}
