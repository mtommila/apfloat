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

import org.apfloat.ApfloatRuntimeException;

/**
 * Exception indicating some unexpected apfloat
 * implementation specific error situation.
 * This exception can be thrown in different situations, for example:
 *
 * <ul>
 *   <li>Backing storage failure. For example, if a number is stored on disk,
 *       an <code>IOException</code> can be thrown in any of the disk operations,
 *       if e.g. a file can't be created, or if the disk is full.</li>
 *   <li>Operands of some operation have different radixes.</li>
 *   <li>Other internal limitation, e.g. the maximum transform length
 *       mathematically possible for the implementation, is exceeded.</li>
 * </ul>
 *
 * @since 1.5
 * @version 1.5
 * @author Mikko Tommila
 */

public class ApfloatInternalException
    extends ApfloatRuntimeException
{
    /**
     * Constructs a new apfloat internal exception with an empty detail message.
     */

    public ApfloatInternalException()
    {
    }

    /**
     * Constructs a new apfloat internal exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public ApfloatInternalException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat internal exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public ApfloatInternalException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = -7022924635011038776L;
}
