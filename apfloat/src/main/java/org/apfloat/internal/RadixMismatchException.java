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
package org.apfloat.internal;

/**
 * Exception indicating a different radix being used in two operands
 * of a calculation.<p>
 *
 * While it's possible to convert numbers to different radixes using the
 * <code>toRadix()</code> methods, this is highly inefficient. If numbers
 * of different radixes need to be used in a calculation, they should be
 * explicitly converted to matching radixes before attempting the calculation.
 * Otherwise this exception should be thrown.
 *
 * @since 1.5
 * @version 1.5
 * @author Mikko Tommila
 */

public class RadixMismatchException
    extends ApfloatInternalException
{
    /**
     * Constructs a new apfloat radix mismatch exception with an empty detail message.
     */

    public RadixMismatchException()
    {
    }

    /**
     * Constructs a new apfloat radix mismatch exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public RadixMismatchException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat radix mismatch exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public RadixMismatchException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = -7022924635011038776L;
}
