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
package org.apfloat.samples;

/**
 * Interface for implementing objects that can execute {@link Operation}s.
 * An operation can e.g. be executed locally or remotely.
 *
 * @version 1.1
 * @author Mikko Tommila
 */

public interface OperationExecutor
{
    /**
     * Executes some code, returning a value.
     *
     * @param <T> Return value type of the operation.
     * @param operation The operation to execute.
     *
     * @return Return value of the operation.
     */

    public <T> T execute(Operation<T> operation);

    /**
     * Starts executing some code in the background.
     *
     * @param <T> Return value type of the operation.
     * @param operation The operation to execute in the background.
     *
     * @return An object for retrieving the result of the operation later.
     */

    public <T> BackgroundOperation<T> executeBackground(Operation<T> operation);

    /**
     * Returns the relative weight of this executor.
     * The weights of different operation executors can be used
     * to distribute work more equally.
     *
     * @return The relative weight of this operation executor.
     */

    public int getWeight();
}
