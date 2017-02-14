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
package org.apfloat.aparapi;

import org.apfloat.spi.NTTBuilder;
import org.apfloat.internal.LongBuilderFactory;

/**
 * Builder factory for aparapi transform implementations for the <code>long</code> element type.
 *
 * @since 1.8.3
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class LongAparapiBuilderFactory
    extends LongBuilderFactory
{
    /**
     * Default constructor.
     */

    public LongAparapiBuilderFactory()
    {
    }

    @Override
    public NTTBuilder getNTTBuilder()
    {
        return LongAparapiBuilderFactory.nttBuilder;
    }

    private static NTTBuilder nttBuilder = new LongAparapiNTTBuilder();
}
