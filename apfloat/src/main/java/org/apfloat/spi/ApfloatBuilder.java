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
package org.apfloat.spi;

import java.io.PushbackReader;
import java.io.IOException;

import org.apfloat.ApfloatRuntimeException;

/**
 * An ApfloatBuilder contains factory methods to create
 * new instances of {@link ApfloatImpl} implementations.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public interface ApfloatBuilder
{
    /**
     * Create a new <code>ApfloatImpl</code> instance from a <code>String</code>.
     *
     * @param value The string to be parsed to a number.
     * @param precision The precision of the number (in digits of the radix).
     * @param radix The radix in which the number is created.
     * @param isInteger Specifies if the number to be parsed from the string is to be treated as an integer or not.
     *
     * @return A new <code>ApfloatImpl</code>.
     *
     * @exception java.lang.NumberFormatException If the number is not valid.
     */

    public ApfloatImpl createApfloat(String value, long precision, int radix, boolean isInteger)
        throws NumberFormatException, ApfloatRuntimeException;

    /**
     * Create a new <code>ApfloatImpl</code> instance from a <code>long</code>.
     *
     * @param value The value of the number.
     * @param precision The precision of the number (in digits of the radix).
     * @param radix The radix in which the number is created.
     *
     * @return A new <code>ApfloatImpl</code>.
     *
     * @exception java.lang.NumberFormatException If the number is not valid.
     */

    public ApfloatImpl createApfloat(long value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException;

    /**
     * Create a new <code>ApfloatImpl</code> instance from a <code>double</code>.
     *
     * @param value The value of the number.
     * @param precision The precision of the number (in digits of the radix).
     * @param radix The radix in which the number is created.
     *
     * @return A new <code>ApfloatImpl</code>.
     *
     * @exception java.lang.NumberFormatException If the number is not valid.
     */

    public ApfloatImpl createApfloat(double value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException;

    /**
     * Create a new <code>ApfloatImpl</code> instance reading from a stream.
     *
     * @param in The stream to read from.
     * @param precision The precision of the number (in digits of the radix).
     * @param radix The radix in which the number is created.
     * @param isInteger Specifies if the number to be parsed from the stream is to be treated as an integer or not.
     *
     * @return A new <code>ApfloatImpl</code>.
     *
     * @exception java.io.IOException If an I/O error occurs accessing the stream.
     * @exception java.lang.NumberFormatException If the number is not valid.
     */

    public ApfloatImpl createApfloat(PushbackReader in, long precision, int radix, boolean isInteger)
        throws IOException, NumberFormatException, ApfloatRuntimeException;
}
