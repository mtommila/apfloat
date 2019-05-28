/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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

import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap with softly referenced values.
 * The maximum map size is assumed to be limited so no
 * effort is made to expunge entries for stale values.<p>
 *
 * Values are not properly compared for equality so
 * the only actual concurrent method implemented is
 * <code>putIfAbsent()</code>.<p>
 *
 * @since 1.7.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

class ConcurrentSoftHashMap<K, V>
    extends AbstractMap<K, V>
    implements ConcurrentMap<K, V>
{
    private ConcurrentHashMap<K, SoftReference<V>> map;

    public ConcurrentSoftHashMap()
    {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void clear()
    {
        this.map.clear();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key)
    {
        return unwrap(this.map.get(key));
    }

    @Override
    public V put(K key, V value)
    {
        return unwrap(this.map.put(key, wrap(value)));
    }

    @Override
    public V putIfAbsent(K key, V value)
    {
        return unwrap(this.map.putIfAbsent(key, wrap(value)));
    }

    @Override
    public V remove(Object key)
    {
        return unwrap(this.map.remove(key));
    }

    @Override
    public boolean remove(Object key, Object value)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public V replace(K key, V value)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size()
    {
        return this.map.size();
    }

    private SoftReference<V> wrap(V value)
    {
        return new SoftReference<>(value);
    }

    private V unwrap(SoftReference<V> value)
    {
        return (value == null ? null : value.get());
    }
}
