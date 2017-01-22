package org.apfloat.internal;

import org.apfloat.spi.CarryCRTBuilder;
import org.apfloat.spi.CarryCRTStrategy;
import org.apfloat.spi.CarryCRTStepStrategy;

/**
 * Creates carry-CRT related objects, for the
 * <code>rawtype</code> type.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeCarryCRTBuilder
    implements CarryCRTBuilder<rawtype[]>
{
    /**
     * Default constructor.
     */

    public RawtypeCarryCRTBuilder()
    {
    }

    public CarryCRTStrategy createCarryCRT(int radix)
    {
        return new StepCarryCRTStrategy(radix);
    }

    public CarryCRTStepStrategy<rawtype[]> createCarryCRTSteps(int radix)
    {
        return new RawtypeCarryCRTStepStrategy(radix);
    }
}
