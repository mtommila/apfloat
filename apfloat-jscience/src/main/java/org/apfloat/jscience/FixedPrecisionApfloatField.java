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

    public FixedPrecisionApfloatField plus(FixedPrecisionApfloatField that)
    {
        return new FixedPrecisionApfloatField(helper().add(value(), that.value()), helper());
    }

    public FixedPrecisionApfloatField opposite()
    {
        return new FixedPrecisionApfloatField(helper().negate(value()), helper());
    }

    public FixedPrecisionApfloatField times(FixedPrecisionApfloatField that)
    {
        return new FixedPrecisionApfloatField(helper().multiply(value(), that.value()), helper());
    }

    public FixedPrecisionApfloatField inverse()
        throws ArithmeticException
    {
        return new FixedPrecisionApfloatField(helper().inverseRoot(value(), 1), helper());
    }

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
