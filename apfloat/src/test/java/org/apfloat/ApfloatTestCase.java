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

import junit.framework.TestCase;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class ApfloatTestCase
    extends TestCase
{
    public ApfloatTestCase(String methodName)
    {
        super(methodName);
    }

    public static void assertEquals(String message, Apcomplex a, Apcomplex b, Apfloat delta)
    {
        if (ApcomplexMath.abs(a.subtract(b)).compareTo(delta) > 0)
        {
            assertEquals(message, a.toString(), b.toString());
        }
    }

    public static void assertEquals(String message, Apfloat a, Apfloat b, Apfloat delta)
    {
        if (ApfloatMath.abs(a.subtract(b)).compareTo(delta) > 0)
        {
            assertEquals(message, a.toString(), b.toString());
        }
    }
}
