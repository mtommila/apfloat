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

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;

/**
 * Array access class based on a <code>rawtype[]</code>.
 *
 * @version 1.6.3
 * @author Mikko Tommila
 */

public class RawtypeMemoryArrayAccess
    extends ArrayAccess
{
    /**
     * Create an array access.<p>
     *
     * @param data The underlying array.
     * @param offset The offset of the access segment within the array.
     * @param length The access segment.
     */

    public RawtypeMemoryArrayAccess(rawtype[] data, int offset, int length)
    {
        super(offset, length);
        this.data = data;
    }

    public ArrayAccess subsequence(int offset, int length)
    {
        return new RawtypeMemoryArrayAccess(this.data, getOffset() + offset, length);
    }

    public Object getData()
    {
        return this.data;
    }

    public rawtype[] getRawtypeData()
    {
        return this.data;
    }

    public void close()
        throws ApfloatRuntimeException
    {
        this.data = null;       // Might have an impact on garbage collection
    }

    private static final long serialVersionUID = ${org.apfloat.internal.RawtypeMemoryArrayAccess.serialVersionUID};

    private rawtype[] data;
}
