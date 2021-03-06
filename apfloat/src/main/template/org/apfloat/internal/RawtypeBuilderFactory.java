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
package org.apfloat.internal;

import org.apfloat.ApfloatRuntimeException;

import org.apfloat.spi.AdditionBuilder;
import org.apfloat.spi.BuilderFactory;
import org.apfloat.spi.ApfloatBuilder;
import org.apfloat.spi.DataStorageBuilder;
import org.apfloat.spi.ConvolutionBuilder;
import org.apfloat.spi.NTTBuilder;
import org.apfloat.spi.MatrixBuilder;
import org.apfloat.spi.CarryCRTBuilder;
import org.apfloat.spi.ExecutionBuilder;

/**
 * Factory class for getting instances of the various builder classes needed
 * to build an <code>ApfloatImpl</code> with the <code>rawtype</code> data element type.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeBuilderFactory
    implements BuilderFactory
{
    /**
     * Default constructor.
     */

    public RawtypeBuilderFactory()
    {
    }

    @Override
    public ApfloatBuilder getApfloatBuilder()
    {
        return RawtypeBuilderFactory.apfloatBuilder;
    }

    @Override
    public DataStorageBuilder getDataStorageBuilder()
    {
        return RawtypeBuilderFactory.dataStorageBuilder;
    }

    @Override
    public <T> AdditionBuilder<T> getAdditionBuilder(Class<T> elementType)
        throws IllegalArgumentException
    {
        if (!RawType.TYPE.equals(elementType))
        {
           throw new IllegalArgumentException("Unsupported element type: " + elementType);
        }
        @SuppressWarnings("unchecked")
        AdditionBuilder<T> additionBuilder = (AdditionBuilder<T>) RawtypeBuilderFactory.additionBuilder;
        return additionBuilder;
    }

    @Override
    public ConvolutionBuilder getConvolutionBuilder()
    {
        return RawtypeBuilderFactory.convolutionBuilder;
    }

    @Override
    public NTTBuilder getNTTBuilder()
    {
        return RawtypeBuilderFactory.nttBuilder;
    }

    @Override
    public MatrixBuilder getMatrixBuilder()
    {
        return RawtypeBuilderFactory.matrixBuilder;
    }

    @Override
    public <T> CarryCRTBuilder<T> getCarryCRTBuilder(Class<T> elementArrayType)
        throws IllegalArgumentException
    {
        if (!rawtype[].class.equals(elementArrayType))
        {
           throw new IllegalArgumentException("Unsupported element array type: " + elementArrayType);
        }
        @SuppressWarnings("unchecked")
        CarryCRTBuilder<T> carryCRTBuilder = (CarryCRTBuilder<T>) RawtypeBuilderFactory.carryCRTBuilder;
        return carryCRTBuilder;
    }

    @Override
    public ExecutionBuilder getExecutionBuilder()
    {
        return RawtypeBuilderFactory.executionBuilder;
    }

    @Override
    public Class<?> getElementType()
    {
        return RawType.TYPE;
    }

    @Override
    public Class<?> getElementArrayType()
    {
        return rawtype[].class;
    }

    @Override
    public int getElementSize()
    {
        return sizeof(rawtype);
    }

    @Override
    public void shutdown()
        throws ApfloatRuntimeException
    {
        DiskDataStorage.cleanUp();
    }

    @Override
    public void gc()
        throws ApfloatRuntimeException
    {
        System.gc();
        System.gc();
        System.runFinalization();
        DiskDataStorage.gc();
    }

    private static ApfloatBuilder apfloatBuilder = new RawtypeApfloatBuilder();
    private static DataStorageBuilder dataStorageBuilder = new RawtypeDataStorageBuilder();
    private static AdditionBuilder<RawType> additionBuilder = new RawtypeAdditionBuilder();
    private static ConvolutionBuilder convolutionBuilder = new RawtypeConvolutionBuilder();
    private static NTTBuilder nttBuilder = new RawtypeNTTBuilder();
    private static MatrixBuilder matrixBuilder = new RawtypeMatrixBuilder();
    private static CarryCRTBuilder<rawtype[]> carryCRTBuilder = new RawtypeCarryCRTBuilder();
    private static ExecutionBuilder executionBuilder = new ParallelExecutionBuilder();
}
