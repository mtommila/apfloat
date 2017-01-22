package org.apfloat.jscience;

import org.apfloat.Aprational;

/**
 * This class represents an arbitrary precision rational number.
 *
 * @since 1.8.0
 * @version 1.8.0
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

    public AprationalField plus(AprationalField that)
    {
        return new AprationalField(value().add(that.value()));
    }

    public AprationalField opposite()
    {
        return new AprationalField(value().negate());
    }

    public AprationalField times(AprationalField that)
    {
        return new AprationalField(value().multiply(that.value()));
    }

    public AprationalField inverse()
        throws ArithmeticException
    {
        if (value().signum() == 0)
        {
            throw new ArithmeticException("Inverse of zero");
        }
        return new AprationalField(new Aprational(value().denominator(), value().numerator()));
    }

    public AprationalField copy()
    {
        return new AprationalField(value());
    }

    private static final long serialVersionUID = -4642791345140583865L;
}
