/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;

/**
 * Array access class based on a <code>rawtype[]</code>.
 *
 * @version 1.6.3
 * @author Mikko Tommila
 */

public class RawtypeMemoryArrayAccess
    extends ArrayAccess
{
    /**
     * Create an array access.<p>
     *
     * @param data The underlying array.
     * @param offset The offset of the access segment within the array.
     * @param length The access segment.
     */

    public RawtypeMemoryArrayAccess(rawtype[] data, int offset, int length)
    {
        super(offset, length);
        this.data = data;
    }

    @Override
    public ArrayAccess subsequence(int offset, int length)
    {
        return new RawtypeMemoryArrayAccess(this.data, getOffset() + offset, length);
    }

    @Override
    public Object getData()
    {
        return this.data;
    }

    @Override
    public rawtype[] getRawtypeData()
    {
        return this.data;
    }

    @Override
    public void close()
        throws ApfloatRuntimeException
    {
        this.data = null;       // Might have an impact on garbage collection
    }

    private static final long serialVersionUID = ${org.apfloat.internal.RawtypeMemoryArrayAccess.serialVersionUID};

    private rawtype[] data;
}
