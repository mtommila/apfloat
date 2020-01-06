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
package org.apfloat.jscience;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/**
 * This class represents an arbitrary precision floating-point number.<p>
 *
 * The precision of each calculation is determined separately, which means
 * that loss of precision can easily accumulate in complicated calculations
 * (e.g. matrix inversion). If this should be avoided, and a fixed precision is
 * required, then it may be better to use {@link FixedPrecisionApfloatField}
 * instead.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class ApfloatField
    extends AbstractField<ApfloatField, Apfloat>
{
    /**
     * Constructs a new floating-point field object with the specified value.
     *
     * @param value The value.
     */

    public ApfloatField(Apfloat value)
    {
        super(value);
    }

    @Override
    public ApfloatField plus(ApfloatField that)
    {
        return new ApfloatField(value().add(that.value()));
    }

    @Override
    public ApfloatField opposite()
    {
        return new ApfloatField(value().negate());
    }

    @Override
    public ApfloatField times(ApfloatField that)
    {
        return new ApfloatField(value().multiply(that.value()));
    }

    @Override
    public ApfloatField inverse()
        throws ArithmeticException
    {
        return new ApfloatField(ApfloatMath.inverseRoot(value(), 1));
    }

    @Override
    public ApfloatField copy()
    {
        return new ApfloatField(value());
    }

    private static final long serialVersionUID = -901594332306254700L;
}
