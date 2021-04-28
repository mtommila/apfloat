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

import javolution.context.LocalContext;

import org.apfloat.Apint;
import org.apfloat.ApintMath;

/**
 * This class represents an arbitrary precision modulo integer.
 * The modulus must be set with {@link #setModulus(Apint)}; otherwise
 * the modulo reduction is not done.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class ModuloApintField
    extends AbstractField<ModuloApintField, Apint>
{
    /**
     * Constructs a new integer field object with the specified value.
     *
     * @param value The value.
     */

    public ModuloApintField(Apint value)
    {
        super(reduce(value));
    }

    /**
     * Returns the modulus or <code>null</code> if modulo reduction is not done.
     * The modulus can be set in a thread-specific way using
     * {@link javolution.context.LocalContext}.
     *
     * @return The local modulus or <code>null</code> if modulo reduction is not done.
     *
     * @see #setModulus
     */

    public static Apint getModulus()
    {
        return MODULUS.get();
    }

    /**
     * Sets the modulus.
     * The modulus can be set in a thread-specific way using
     * {@link javolution.context.LocalContext}.
     *
     * @param modulus The modulus or <code>null</code> if modulo reduction is not done.
     *
     * @throws IllegalArgumentException If <code>modulus</code> is not positive.
     */

    public static void setModulus(Apint modulus)
    {
        if (modulus != null && modulus.signum() <= 0)
        {
            throw new IllegalArgumentException("Modulus has to be greater than zero");
        }
        MODULUS.set(modulus);
    }

    /**
     * Reduce the value with the current modulus.
     *
     * @param value The value.
     *
     * @return The value mod the current modulus.
     */

    public static Apint reduce(Apint value)
    {
        Apint modulus = MODULUS.get();
        if (modulus != null)
        {
            value = value.mod(modulus);
            if (value.signum() < 0)         // The modulus is always positive, so reduce values to positive
            {
                value = value.add(modulus);
            }
        }
        return value;
    }

    @Override
    public ModuloApintField plus(ModuloApintField that)
    {
        return new ModuloApintField(value().add(that.value()));
    }

    @Override
    public ModuloApintField opposite()
    {
        return new ModuloApintField(value().negate());
    }

    @Override
    public ModuloApintField times(ModuloApintField that)
    {
        return new ModuloApintField(value().multiply(that.value()));
    }

    @Override
    public ModuloApintField inverse()
        throws ArithmeticException
    {
        Apint modulus = MODULUS.get();
        if (modulus == null)
        {
            throw new ArithmeticException("Modulus is not set");
        }
        return new ModuloApintField(ApintMath.modPow(value(), new Apint(-1), modulus));
    }

    @Override
    public ModuloApintField copy()
    {
        return new ModuloApintField(value());
    }

    private static final long serialVersionUID = 5308452222350777004L;

    private static final LocalContext.Reference<Apint> MODULUS = new LocalContext.Reference<Apint>();
}
