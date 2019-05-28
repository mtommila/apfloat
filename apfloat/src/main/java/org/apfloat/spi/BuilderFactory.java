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

import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatRuntimeException;

/**
 * A <code>BuilderFactory</code> object contains factory methods for building
 * the various parts of an apfloat using the Builder pattern. There
 * is no separate "director" object in the apfloat SPI; it is suggested
 * that the <code>ApfloatImpl</code> implementation itself acts as the director,
 * calling the different builders directly.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public interface BuilderFactory
{
    /**
     * Returns an <code>ApfloatBuilder</code> object.
     *
     * @return An <code>ApfloatBuilder</code> object.
     */

    public ApfloatBuilder getApfloatBuilder();

    /**
     * Returns a <code>DataStorageBuilder</code> object.
     *
     * @return A <code>DataStorageBuilder</code> object.
     */

    public DataStorageBuilder getDataStorageBuilder();

    /**
     * Returns an <code>AdditionBuilder</code> object.
     *
     * @param <T> The element type of the additions.
     * @param elementType The element type of the additions.
     *
     * @return An <code>AdditionBuilder</code> object.
     *
     * @throws IllegalArgumentException In case of unsupported element type.
     *
     * @since 1.7.0
     */

    public <T> AdditionBuilder<T> getAdditionBuilder(Class<T> elementType)
        throws IllegalArgumentException;

    /**
     * Returns a <code>ConvolutionBuilder</code> object.
     *
     * @return A <code>ConvolutionBuilder</code> object.
     */

    public ConvolutionBuilder getConvolutionBuilder();

    /**
     * Returns an <code>NTTBuilder</code> object.
     *
     * @return An <code>NTTBuilder</code> object.
     */

    public NTTBuilder getNTTBuilder();

    /**
     * Returns a <code>MatrixBuilder</code> object.
     *
     * @return A <code>MatrixBuilder</code> object.
     *
     * @since 1.7.0
     */

    public MatrixBuilder getMatrixBuilder();

    /**
     * Returns a <code>CarryCRTBuilder</code> object.
     *
     * @param <T> The element array type of the carry-CRT.
     * @param elementArrayType The element array type of the carry-CRT.
     *
     * @return A <code>CarryCRTBuilder</code> object.
     *
     * @throws IllegalArgumentException In case of unsupported element array type.
     *
     * @since 1.7.0
     */

    public <T> CarryCRTBuilder<T> getCarryCRTBuilder(Class<T> elementArrayType)
        throws IllegalArgumentException;

    /**
     * Returns an <code>ExecutionBuilder</code> object.
     *
     * @return An <code>ExecutionBuilder</code> object.
     *
     * @since 1.9.0
     */

    public ExecutionBuilder getExecutionBuilder();

    /**
     * Returns the element type of the data objects created.
     *
     * @return The element type of the data objects created.
     *
     * @since 1.7.0
     */

    public Class<?> getElementType();

    /**
     * Returns the element array type of the data objects created.
     *
     * @return The element array type of the data objects created.
     *
     * @since 1.7.0
     */

    public Class<?> getElementArrayType();

    /**
     * Returns the element size of the data objects created, in bytes.
     *
     * @return The element size of the data objects created, in bytes.
     *
     * @since 1.7.0
     */

    public int getElementSize();

    /**
     * Shuts down the builder factory. Clean-up tasks can be executed by this method.
     * This method is invoked by the {@link ApfloatContext} when cleanupAtExit is enabled.
     *
     * @since 1.6.2
     */

    public void shutdown()
        throws ApfloatRuntimeException;

    /**
     * Do garbage collection and related things such as empty any reference queues.
     *
     * @since 1.6.2
     */

    public void gc()
        throws ApfloatRuntimeException;
}
