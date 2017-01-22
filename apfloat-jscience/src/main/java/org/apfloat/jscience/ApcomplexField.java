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
package org.apfloat.jscience;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;

/**
 * This class represents an arbitrary precision complex number.<p>
 *
 * The precision of each calculation is determined separately, which means
 * that loss of precision can easily accumulate in complicated calculations
 * (e.g. matrix inversion). If this should be avoided, and a fixed precision is
 * required, then it may be better to use {@link FixedPrecisionApcomplexField}
 * instead.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class ApcomplexField
    extends AbstractField<ApcomplexField, Apcomplex>
{
    /**
     * Constructs a new complex field object with the specified value.
     *
     * @param value The value.
     */

    public ApcomplexField(Apcomplex value)
    {
        super(value);
    }

    public ApcomplexField plus(ApcomplexField that)
    {
        return new ApcomplexField(value().add(that.value()));
    }

    public ApcomplexField opposite()
    {
        return new ApcomplexField(value().negate());
    }

    public ApcomplexField times(ApcomplexField that)
    {
        return new ApcomplexField(value().multiply(that.value()));
    }

    public ApcomplexField inverse()
        throws ArithmeticException
    {
        return new ApcomplexField(ApcomplexMath.inverseRoot(value(), 1));
    }

    public ApcomplexField copy()
    {
        return new ApcomplexField(value());
    }

    private static final long serialVersionUID = -6242843580910131563L;
}
