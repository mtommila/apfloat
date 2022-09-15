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
package org.apfloat.jscience;

import org.apfloat.Apcomplex;
import org.apfloat.ApcomplexMath;

/**
 * This class represents an arbitrary precision complex number.<p>
 *
 * The precision of each calculation is determined separately, which means
 * that loss of precision can easily accumulate in complicated calculations
 * (e.g. matrix inversion). If this should be avoided, and a fixed precision is
 * required, then it may be better to use {@link FixedPrecisionApcomplexField}
 * instead.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class ApcomplexField
    extends AbstractField<ApcomplexField, Apcomplex>
{
    /**
     * Constructs a new complex field object with the specified value.
     *
     * @param value The value.
     */

    public ApcomplexField(Apcomplex value)
    {
        super(value);
    }

    @Override
    public ApcomplexField plus(ApcomplexField that)
    {
        return new ApcomplexField(value().add(that.value()));
    }

    @Override
    public ApcomplexField opposite()
    {
        return new ApcomplexField(value().negate());
    }

    @Override
    public ApcomplexField times(ApcomplexField that)
    {
        return new ApcomplexField(value().multiply(that.value()));
    }

    @Override
    public ApcomplexField inverse()
        throws ArithmeticException
    {
        return new ApcomplexField(ApcomplexMath.inverseRoot(value(), 1));
    }

    @Override
    public ApcomplexField copy()
    {
        return new ApcomplexField(value());
    }

    private static final long serialVersionUID = -6242843580910131563L;
}
