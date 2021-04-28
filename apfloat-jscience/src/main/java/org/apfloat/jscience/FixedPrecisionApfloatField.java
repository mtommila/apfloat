/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
package org.apfloat.jscience;

import org.apfloat.Apfloat;
import org.apfloat.FixedPrecisionApfloatHelper;

/**
 * This class represents a fixed-precision floating-point number. The precision
 * is reset after each computation using the provided <code>FixedPrecisionApfloatHelper</code>.
 * This can help avoid accumulating round-off errors and loss of precision
 * in complicated computations such as matrix inversion.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class FixedPrecisionApfloatField
    extends AbstractField<FixedPrecisionApfloatField, Apfloat>
{
    /**
     * Constructs a new floating-point field object with the specified value.
     *
     * @param value The value.
     * @param helper The precision helper.
     */

    public FixedPrecisionApfloatField(Apfloat value, FixedPrecisionApfloatHelper helper)
    {
        super(value);
        if (helper == null)
        {
            throw new NullPointerException("Helper can't be null");
        }
        this.helper = helper;
    }

    @Override
    public FixedPrecisionApfloatField plus(FixedPrecisionApfloatField that)
    {
        return new FixedPrecisionApfloatField(helper().add(value(), that.value()), helper());
    }

    @Override
    public FixedPrecisionApfloatField opposite()
    {
        return new FixedPrecisionApfloatField(helper().negate(value()), helper());
    }

    @Override
    public FixedPrecisionApfloatField times(FixedPrecisionApfloatField that)
    {
        return new FixedPrecisionApfloatField(helper().multiply(value(), that.value()), helper());
    }

    @Override
    public FixedPrecisionApfloatField inverse()
        throws ArithmeticException
    {
        return new FixedPrecisionApfloatField(helper().inverseRoot(value(), 1), helper());
    }

    @Override
    public FixedPrecisionApfloatField copy()
    {
        return new FixedPrecisionApfloatField(value(), helper());
    }

    /**
     * Return the precision helper.
     *
     * @return The precision helper.
     */

    public FixedPrecisionApfloatHelper helper()
    {
        return this.helper;
    }

    private static final long serialVersionUID = -8969242537753892317L;

    private FixedPrecisionApfloatHelper helper;
}
