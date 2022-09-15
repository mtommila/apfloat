/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
