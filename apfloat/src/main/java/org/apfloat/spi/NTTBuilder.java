/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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
package org.apfloat.spi;

/**
 * Interface of a factory for creating Number Theoretic Transforms.
 * The factory method pattern is used.
 *
 * @see NTTStrategy
 * @see NTTStepStrategy
 *
 * @version 1.7.0
 * @author Mikko Tommila
 */

public interface NTTBuilder
{
    /**
     * Creates a Number Theoretic Transform of suitable
     * type for the specified length.
     *
     * @param size The transform length that will be used.
     *
     * @return A suitable NTT object for performing the transform.
     */

    public NTTStrategy createNTT(long size);

    /**
     * Creates an object for implementing the steps of a step-based
     * Number Theoretic Transform.
     *
     * @return A suitable object for performing the transform steps.
     *
     * @since 1.7.0
     */

    public NTTStepStrategy createNTTSteps();

    /**
     * Creates an object for implementing the steps of a three-NTT
     * based convolution.
     *
     * @return A suitable object for performing the convolution steps.
     *
     * @since 1.7.0
     */

    public NTTConvolutionStepStrategy createNTTConvolutionSteps();

    /**
     * Creates an object for implementing the steps of factor-3 NTT.
     *
     * @return A suitable object for performing the factor-3 NTT steps.
     *
     * @since 1.7.0
     */

    public Factor3NTTStepStrategy createFactor3NTTSteps();
}
