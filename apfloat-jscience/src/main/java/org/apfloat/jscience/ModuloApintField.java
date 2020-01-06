/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
