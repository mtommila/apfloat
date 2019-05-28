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
package org.apfloat;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Combination of WeakHashMap and ConcurrentHashMap,
 * providing weak keys and non-blocking access.
 *
 * @since 1.5
 * @version 1.9.0
 * @author Mikko Tommila
 */

class ConcurrentWeakHashMap<K, V>
    extends AbstractMap<K, V>
{
    private static class Key
        extends WeakReference<Object>
    {
        private int hashCode;

        public Key(Object key, ReferenceQueue<Object> queue)
        {
            super(key, queue);
            this.hashCode = key.hashCode();
        }

        @Override
        public int hashCode()
        {
            return this.hashCode;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                // Always matches even if referenced object has been garbage collected, needed for expunging garbage collected keys
                return true;
            }
            if (obj instanceof Key)
            {
                Key that = (Key) obj;
                Object value = get();
                return (value != null && value.equals(that.get()));
            }
            return false;
        }
    }

    private ConcurrentHashMap<Key, V> map;
    private ReferenceQueue<Object> queue;

    public ConcurrentWeakHashMap()
    {
        this.map = new ConcurrentHashMap<>();
        this.queue = new ReferenceQueue<>();
    }

    @Override
    public void clear()
    {
        expungeStaleEntries();
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
        // Do not expunge stale entries here to improve performance
        return this.map.get(wrap(key));
    }

    @Override
    public V put(K key, V value)
    {
        expungeStaleEntries();
        return this.map.put(wrap(key), value);
    }

    @Override
    public V remove(Object key)
    {
        expungeStaleEntries();
        return this.map.remove(wrap(key));
    }

    @Override
    public boolean isEmpty()
    {
        // This is for the quick check, therefore we do not expunge stale entries here
        return this.map.isEmpty();
    }

    @Override
    public int size()
    {
        expungeStaleEntries();
        return this.map.size();
    }

    private Key wrap(Object key)
    {
        return new Key(key, this.queue);
    }

    private void expungeStaleEntries()
    {
        // Should not cause (much) blocking
        Key key;
        while ((key = (Key) this.queue.poll()) != null)
        {
            this.map.remove(key);
        }
    }
}
