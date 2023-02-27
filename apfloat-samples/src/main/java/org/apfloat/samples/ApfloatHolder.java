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
package org.apfloat.samples;

import java.io.Serializable;

import org.apfloat.Apfloat;

/**
 * Simple JavaBean to hold one {@link org.apfloat.Apfloat}.
 * This class can also be thought of as a pointer to an {@link Apfloat}.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class ApfloatHolder
    implements Serializable
{
    /**
     * Construct an ApfloatHolder containing <code>null</code>.
     */

    public ApfloatHolder()
    {
        this(null);
    }

    /**
     * Construct an ApfloatHolder containing the specified apfloat.
     *
     * @param apfloat The number to hold.
     */

    public ApfloatHolder(Apfloat apfloat)
    {
        this.apfloat = apfloat;
    }

    /**
     * Return the apfloat contained in this bean.
     *
     * @return The apfloat contained in this bean.
     */

    public Apfloat getApfloat()
    {
        return this.apfloat;
    }

    /**
     * Set the apfloat contained in this bean.
     *
     * @param apfloat The apfloat.
     */

    public void setApfloat(Apfloat apfloat)
    {
        this.apfloat = apfloat;
    }

    private static final long serialVersionUID = 1L;

    private Apfloat apfloat;
}
