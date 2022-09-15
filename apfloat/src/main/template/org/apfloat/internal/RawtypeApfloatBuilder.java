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

    @Override
    public ApfloatImpl createApfloat(String value, long precision, int radix, boolean isInteger)
        throws NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(value, precision, radix, isInteger);
    }

    @Override
    public ApfloatImpl createApfloat(long value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(value, precision, radix);
    }

    @Override
    public ApfloatImpl createApfloat(double value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(value, precision, radix);
    }

    @Override
    public ApfloatImpl createApfloat(PushbackReader in, long precision, int radix, boolean isInteger)
        throws IOException, NumberFormatException, ApfloatRuntimeException
    {
        return new RawtypeApfloatImpl(in, precision, radix, isInteger);
    }
}
