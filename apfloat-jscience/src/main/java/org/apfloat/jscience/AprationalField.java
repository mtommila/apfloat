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
package org.apfloat.jscience;

import org.apfloat.Aprational;
import org.apfloat.ApfloatArithmeticException;

/**
 * This class represents an arbitrary precision rational number.
 *
 * @since 1.8.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class AprationalField
    extends AbstractField<AprationalField, Aprational>
{
    /**
     * Constructs a new rational field object with the specified value.
     *
     * @param value The value.
     */

    public AprationalField(Aprational value)
    {
        super(value);
    }

    @Override
    public AprationalField plus(AprationalField that)
    {
        return new AprationalField(value().add(that.value()));
    }

    @Override
    public AprationalField opposite()
    {
        return new AprationalField(value().negate());
    }

    @Override
    public AprationalField times(AprationalField that)
    {
        return new AprationalField(value().multiply(that.value()));
    }

    @Override
    public AprationalField inverse()
        throws ArithmeticException
    {
        if (value().signum() == 0)
        {
            throw new ApfloatArithmeticException("Division by zero", "divide.byZero");
        }
        return new AprationalField(new Aprational(value().denominator(), value().numerator()));
    }

    @Override
    public AprationalField copy()
    {
        return new AprationalField(value());
    }

    @Override
    public boolean isLargerThan(AprationalField that)
    {
        return value().multiply(value()).compareTo(that.value().multiply(that.value())) > 0;
    }

    private static final long serialVersionUID = -4642791345140583865L;
}
