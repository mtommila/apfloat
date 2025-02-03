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

import org.apfloat.spi.ArrayAccess;

/**
 * Six-step NTT implementation for the <code>int</code> element type using column orientation.<p>
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class IntAparapiColumnSixStepFNTStrategy
    extends ColumnSixStepFNTStrategy
{
    /**
     * Default constructor.
     */

    public IntAparapiColumnSixStepFNTStrategy()
    {
        super(new IntAparapiNTTStepStrategy(false), new IntAparapiMatrixStrategy());
    }

    @Override
    protected void preTransform(ArrayAccess arrayAccess)
    {
        IntKernel kernel = IntKernel.getInstance();
        kernel.setExplicit(true);
        kernel.put(arrayAccess.getIntData());

        super.preTransform(arrayAccess);
    }

    @Override
    protected void postTransform(ArrayAccess arrayAccess)
    {
        super.postTransform(arrayAccess);

        IntKernel kernel = IntKernel.getInstance();
        kernel.get(arrayAccess.getIntData());
        kernel.cleanUpArrays();
    }
}
