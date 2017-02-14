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

import org.apfloat.Aprational;

/**
 * This class represents an arbitrary precision rational number.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class AprationalField
    extends AbstractField<AprationalField, Aprational>
{
    /**
     * Constructs a new rational field object with the specified value.
     *
     * @param value The value.
     */

    public AprationalField(Aprational value)
    {
        super(value);
    }

    @Override
    public AprationalField plus(AprationalField that)
    {
        return new AprationalField(value().add(that.value()));
    }

    @Override
    public AprationalField opposite()
    {
        return new AprationalField(value().negate());
    }

    @Override
    public AprationalField times(AprationalField that)
    {
        return new AprationalField(value().multiply(that.value()));
    }

    @Override
    public AprationalField inverse()
        throws ArithmeticException
    {
        if (value().signum() == 0)
        {
            throw new ArithmeticException("Inverse of zero");
        }
        return new AprationalField(new Aprational(value().denominator(), value().numerator()));
    }

    @Override
    public AprationalField copy()
    {
        return new AprationalField(value());
    }

    private static final long serialVersionUID = -4642791345140583865L;
}
