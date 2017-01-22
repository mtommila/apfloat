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

import java.io.PushbackReader;
import java.io.IOException;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ApfloatBuilder;
import org.apfloat.spi.ApfloatImpl;

/**
 * Builder class for building {@link ApfloatImpl} implementations with the
 * <code>rawtype</code> data element type.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeApfloatBuilder
    implements ApfloatBuilder
{
    /**
     * Default constructor.
     */

    public RawtypeApfloatBuilder()
    {
    }

    public ApfloatImpl createApfloat(String value, long precision, int radix, boolean isInteger)
        throws NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(value, precision, radix, isInteger);
    }

    public ApfloatImpl createApfloat(long value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(value, precision, radix);
    }

    public ApfloatImpl createApfloat(double value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(value, precision, radix);
    }

    public ApfloatImpl createApfloat(PushbackReader in, long precision, int radix, boolean isInteger)
        throws IOException, NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(in, precision, radix, isInteger);
    }
}
