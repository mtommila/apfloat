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

    public ApcomplexField plus(ApcomplexField that)
    {
        return new ApcomplexField(value().add(that.value()));
    }

    public ApcomplexField opposite()
    {
        return new ApcomplexField(value().negate());
    }

    public ApcomplexField times(ApcomplexField that)
    {
        return new ApcomplexField(value().multiply(that.value()));
    }

    public ApcomplexField inverse()
        throws ArithmeticException
    {
        return new ApcomplexField(ApcomplexMath.inverseRoot(value(), 1));
    }

    public ApcomplexField copy()
    {
        return new ApcomplexField(value());
    }

    private static final long serialVersionUID = -6242843580910131563L;
}
