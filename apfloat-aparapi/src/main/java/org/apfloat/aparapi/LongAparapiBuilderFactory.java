/*
 * MIT License
 *
 * Copyright (c) 2002-2025 Mikko Tommila
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
package org.apfloat.aparapi;

import org.apfloat.ApfloatContext;
import org.apfloat.spi.NTTBuilder;
import org.apfloat.internal.LongBuilderFactory;

/**
 * Builder factory for aparapi transform implementations for the <code>long</code> element type.
 *
 * @since 1.8.3
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class LongAparapiBuilderFactory
    extends LongBuilderFactory
{
    /**
     * Default constructor.
     */

    public LongAparapiBuilderFactory()
    {
        boolean rowOrientation = Boolean.parseBoolean(ApfloatContext.getContext().getProperty("rowOrientation"));
        this.nttBuilder = new LongAparapiNTTBuilder(rowOrientation);
    }

    @Override
    public NTTBuilder getNTTBuilder()
    {
        return this.nttBuilder;
    }

    private NTTBuilder nttBuilder;
}
