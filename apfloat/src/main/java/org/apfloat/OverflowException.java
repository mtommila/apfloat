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
package org.apfloat;

/**
 * Exception indicating an overflow in a calculation.<p>
 *
 * For example <code>ApfloatMath.exp(new Apfloat(1e100))</code>.<p>
 *
 * If the exponent is too large to fit in a <code>long</code>,
 * the situation can't be handled. Note that there is no
 * "infinity" apfloat value that could be returned as the result.
 *
 * @since 1.5
 * @version 1.5
 * @author Mikko Tommila
 */

public class OverflowException
    extends ApfloatRuntimeException
{
    /**
     * Constructs a new apfloat overflow exception with an empty detail message.
     */

    public OverflowException()
    {
    }

    /**
     * Constructs a new apfloat overflow exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public OverflowException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat overflow exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public OverflowException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = -7022924635011038776L;
}
