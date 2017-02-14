/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
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
package org.apfloat.internal;

import org.apfloat.spi.NTTStrategy;
import org.apfloat.spi.NTTStepStrategy;
import org.apfloat.spi.Factor3NTTStepStrategy;
import org.apfloat.spi.NTTConvolutionStepStrategy;

/**
 * Creates Number Theoretic Transforms for the
 * <code>rawtype</code> type.
 *
 * @see RawtypeTableFNTStrategy
 * @see SixStepFNTStrategy
 * @see TwoPassFNTStrategy
 * @see Factor3NTTStrategy
 *
 * @version 1.7.0
 * @author Mikko Tommila
 */

public class RawtypeNTTBuilder
    extends AbstractNTTBuilder
{
    /**
     * Default constructor.
     */

    public RawtypeNTTBuilder()
    {
    }

    public NTTStepStrategy createNTTSteps()
    {
        return new RawtypeNTTStepStrategy();
    }

    public NTTConvolutionStepStrategy createNTTConvolutionSteps()
    {
        return new RawtypeNTTConvolutionStepStrategy();
    }

    public Factor3NTTStepStrategy createFactor3NTTSteps()
    {
        return new RawtypeFactor3NTTStepStrategy();
    }

    @Override
    protected NTTStrategy createSimpleFNTStrategy()
    {
        return new RawtypeTableFNTStrategy();
    }

    @Override
    protected NTTStrategy createSixStepFNTStrategy()
    {
        return new SixStepFNTStrategy();
    }

    @Override
    protected NTTStrategy createTwoPassFNTStrategy()
    {
        return new TwoPassFNTStrategy();
    }

    @Override
    protected NTTStrategy createFactor3NTTStrategy(NTTStrategy nttStrategy)
    {
        return new Factor3NTTStrategy(nttStrategy);
    }
}
