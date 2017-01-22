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

    public ModuloApintField plus(ModuloApintField that)
    {
        return new ModuloApintField(value().add(that.value()));
    }

    public ModuloApintField opposite()
    {
        return new ModuloApintField(value().negate());
    }

    public ModuloApintField times(ModuloApintField that)
    {
        return new ModuloApintField(value().multiply(that.value()));
    }

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

    public ModuloApintField copy()
    {
        return new ModuloApintField(value());
    }

    private static final long serialVersionUID = 5308452222350777004L;

    private static final LocalContext.Reference<Apint> MODULUS = new LocalContext.Reference<Apint>();
}
