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

/**
 * Exception indicating that the "size" of the numbers used in a
 * multiplication is too large. The "size" is equivalent to the number
 * of significant digits in the mantissa of the number, excluding any
 * leading or trailing zeros.<p>
 *
 * This exception indicates a mathematical limitation. The exact
 * maximum transform length depends on the apfloat implementation.
 *
 * @since 1.5
 * @version 1.5
 * @author Mikko Tommila
 */

public class TransformLengthExceededException
    extends ApfloatInternalException
{
    /**
     * Constructs a new apfloat transform length exceeded exception with an empty detail message.
     */

    public TransformLengthExceededException()
    {
    }

    /**
     * Constructs a new apfloat transform length exceeded exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public TransformLengthExceededException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat transform length exceeded exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public TransformLengthExceededException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = -7022924635011038776L;
}
