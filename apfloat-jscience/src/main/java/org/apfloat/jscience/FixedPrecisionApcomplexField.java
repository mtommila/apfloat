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
import org.apfloat.FixedPrecisionApcomplexHelper;

/**
 * This class represents a fixed-precision complex number. The precision
 * is reset after each computation using the provided <code>FixedPrecisionApcomplexHelper</code>.
 * This can help avoid accumulating round-off errors and loss of precision
 * in complicated computations such as matrix inversion.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class FixedPrecisionApcomplexField
    extends AbstractField<FixedPrecisionApcomplexField, Apcomplex>
{
    /**
     * Constructs a new complex field object with the specified value and precision helper.
     *
     * @param value The value.
     * @param helper The precision helper.
     */

    public FixedPrecisionApcomplexField(Apcomplex value, FixedPrecisionApcomplexHelper helper)
    {
        super(value);
        if (helper == null)
        {
            throw new NullPointerException("Helper can't be null");
        }
        this.helper = helper;
    }

    public FixedPrecisionApcomplexField plus(FixedPrecisionApcomplexField that)
    {
        return new FixedPrecisionApcomplexField(helper().add(value(), that.value()), helper());
    }

    public FixedPrecisionApcomplexField opposite()
    {
        return new FixedPrecisionApcomplexField(helper().negate(value()), helper());
    }

    public FixedPrecisionApcomplexField times(FixedPrecisionApcomplexField that)
    {
        return new FixedPrecisionApcomplexField(helper().multiply(value(), that.value()), helper());
    }

    public FixedPrecisionApcomplexField inverse()
        throws ArithmeticException
    {
        return new FixedPrecisionApcomplexField(helper().inverseRoot(value(), 1), helper());
    }

    public FixedPrecisionApcomplexField copy()
    {
        return new FixedPrecisionApcomplexField(value(), helper());
    }

    /**
     * Return the precision helper.
     *
     * @return The precision helper.
     */

    public FixedPrecisionApcomplexHelper helper()
    {
        return this.helper;
    }

    private static final long serialVersionUID = -2069599698604093434L;

    private FixedPrecisionApcomplexHelper helper;
}
