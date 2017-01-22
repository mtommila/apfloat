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

import org.apfloat.ApfloatRuntimeException;

import org.apfloat.spi.AdditionBuilder;
import org.apfloat.spi.BuilderFactory;
import org.apfloat.spi.ApfloatBuilder;
import org.apfloat.spi.DataStorageBuilder;
import org.apfloat.spi.ConvolutionBuilder;
import org.apfloat.spi.NTTBuilder;
import org.apfloat.spi.MatrixBuilder;
import org.apfloat.spi.CarryCRTBuilder;

/**
 * Factory class for getting instances of the various builder classes needed
 * to build an <code>ApfloatImpl</code> with the <code>rawtype</code> data element type.
 *
 * @version 1.7.0
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

    public ApfloatBuilder getApfloatBuilder()
    {
        return RawtypeBuilderFactory.apfloatBuilder;
    }

    public DataStorageBuilder getDataStorageBuilder()
    {
        return RawtypeBuilderFactory.dataStorageBuilder;
    }

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

    public ConvolutionBuilder getConvolutionBuilder()
    {
        return RawtypeBuilderFactory.convolutionBuilder;
    }

    public NTTBuilder getNTTBuilder()
    {
        return RawtypeBuilderFactory.nttBuilder;
    }

    public MatrixBuilder getMatrixBuilder()
    {
        return RawtypeBuilderFactory.matrixBuilder;
    }

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

    public Class<?> getElementType()
    {
        return RawType.TYPE;
    }

    public Class<?> getElementArrayType()
    {
        return rawtype[].class;
    }

    public int getElementSize()
    {
        return sizeof(rawtype);
    }

    public void shutdown()
        throws ApfloatRuntimeException
    {
        DiskDataStorage.cleanUp();
    }

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
}
