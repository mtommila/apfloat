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

import org.apfloat.spi.AdditionBuilder;
import org.apfloat.spi.AdditionStrategy;

/**
 * Creates additions for the specified radix and the <code>rawtype</code> element type.<p>
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeAdditionBuilder
    implements AdditionBuilder<RawType>
{
    /**
     * Default constructor.
     */

    public RawtypeAdditionBuilder()
    {
    }

    @Override
    public AdditionStrategy<RawType> createAddition(int radix)
    {
        AdditionStrategy<RawType> additionStrategy = new RawtypeAdditionStrategy(radix);
        return additionStrategy;
    }
}
