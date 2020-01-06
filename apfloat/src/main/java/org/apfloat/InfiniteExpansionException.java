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
 * Exception indicating that the result of an operation
 * would have infinite size.<p>
 *
 * For example, <code>new Apfloat(2).divide(new Apfloat(3))</code>, in radix 10.
 *
 * @since 1.5
 * @version 1.5
 * @author Mikko Tommila
 */

public class InfiniteExpansionException
    extends ApfloatRuntimeException
{
    /**
     * Constructs a new apfloat infinite expansion exception with an empty detail message.
     */

    public InfiniteExpansionException()
    {
    }

    /**
     * Constructs a new apfloat infinite expansion exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public InfiniteExpansionException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat infinite expansion exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public InfiniteExpansionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = -7022924635011038776L;
}
