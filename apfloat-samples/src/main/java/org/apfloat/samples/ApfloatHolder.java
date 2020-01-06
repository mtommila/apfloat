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
package org.apfloat.samples;

import java.io.Serializable;

import org.apfloat.Apfloat;

/**
 * Simple JavaBean to hold one {@link org.apfloat.Apfloat}.
 * This class can also be thought of as a pointer to an {@link Apfloat}.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class ApfloatHolder
    implements Serializable
{
    /**
     * Construct an ApfloatHolder containing <code>null</code>.
     */

    public ApfloatHolder()
    {
        this(null);
    }

    /**
     * Construct an ApfloatHolder containing the specified apfloat.
     *
     * @param apfloat The number to hold.
     */

    public ApfloatHolder(Apfloat apfloat)
    {
        this.apfloat = apfloat;
    }

    /**
     * Return the apfloat contained in this bean.
     *
     * @return The apfloat contained in this bean.
     */

    public Apfloat getApfloat()
    {
        return this.apfloat;
    }

    /**
     * Set the apfloat contained in this bean.
     *
     * @param apfloat The apfloat.
     */

    public void setApfloat(Apfloat apfloat)
    {
        this.apfloat = apfloat;
    }

    private static final long serialVersionUID = 1L;

    private Apfloat apfloat;
}
