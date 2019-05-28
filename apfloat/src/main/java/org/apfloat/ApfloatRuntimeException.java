/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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
 * Exception indicating some unexpected error situation.
 * This exception can be thrown in different situations, for example:
 *
 * <ul>
 *   <li>The result of an operation would have infinite size. For example,
 *       <code>new Apfloat(2).divide(new Apfloat(3))</code>, in radix 10.</li>
 *   <li>Overflow. If the exponent is too large to fit in a <code>long</code>,
 *       the situation can't be handled. Also, there is no "infinity" apfloat
 *       value that could be returned as the result.</li>
 *   <li>Total loss of precision. For example, <code>ApfloatMath.sin(new Apfloat(1e100))</code>.
 *       If the magnitude (100) is far greater than the precision (1) then
 *       the value of the <code>sin()</code> function can't be determined
 *       to any accuracy.</li>
 *   <li>Some other internal limitation.</li>
 * </ul>
 *
 * @version 1.1
 * @author Mikko Tommila
 */

public class ApfloatRuntimeException
    extends RuntimeException
{
    /**
     * Constructs a new apfloat runtime exception with an empty detail message.
     */

    public ApfloatRuntimeException()
    {
    }

    /**
     * Constructs a new apfloat runtime exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public ApfloatRuntimeException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat runtime exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public ApfloatRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = -7022924635011038776L;
}
