package org.apfloat.internal;

import org.apfloat.spi.AdditionBuilder;
import org.apfloat.spi.AdditionStrategy;

/**
 * Creates additions for the specified radix and the <code>rawtype</code> element type.<p>
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeAdditionBuilder
    implements AdditionBuilder<RawType>
{
    /**
     * Default constructor.
     */

    public RawtypeAdditionBuilder()
    {
    }

    public AdditionStrategy<RawType> createAddition(int radix)
    {
        AdditionStrategy<RawType> additionStrategy = new RawtypeAdditionStrategy(radix);
        return additionStrategy;
    }
}
