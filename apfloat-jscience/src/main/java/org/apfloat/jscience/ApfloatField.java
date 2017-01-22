package org.apfloat.jscience;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/**
 * This class represents an arbitrary precision floating-point number.<p>
 *
 * The precision of each calculation is determined separately, which means
 * that loss of precision can easily accumulate in complicated calculations
 * (e.g. matrix inversion). If this should be avoided, and a fixed precision is
 * required, then it may be better to use {@link FixedPrecisionApfloatField}
 * instead.
 *
 * @since 1.8.0
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class ApfloatField
    extends AbstractField<ApfloatField, Apfloat>
{
    /**
     * Constructs a new floating-point field object with the specified value.
     *
     * @param value The value.
     */

    public ApfloatField(Apfloat value)
    {
        super(value);
    }

    public ApfloatField plus(ApfloatField that)
    {
        return new ApfloatField(value().add(that.value()));
    }

    public ApfloatField opposite()
    {
        return new ApfloatField(value().negate());
    }

    public ApfloatField times(ApfloatField that)
    {
        return new ApfloatField(value().multiply(that.value()));
    }

    public ApfloatField inverse()
        throws ArithmeticException
    {
        return new ApfloatField(ApfloatMath.inverseRoot(value(), 1));
    }

    public ApfloatField copy()
    {
        return new ApfloatField(value());
    }

    private static final long serialVersionUID = -901594332306254700L;
}
