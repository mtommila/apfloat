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
package org.apfloat.samples;

/**
 * Class to execute {@link Operation}s locally.
 * The execution is done in the current JVM.
 *
 * @version 1.1
 * @author Mikko Tommila
 */

public class LocalOperationExecutor
    implements OperationExecutor
{
    /**
     * Default constructor.
     */

    public LocalOperationExecutor()
    {
    }

    /**
     * Execute an operation immediately.
     * This method will block until the operation is complete.
     *
     * @param operation The operation to execute.
     *
     * @return The result of the operation.
     */

    public <T> T execute(Operation<T> operation)
    {
        return operation.execute();
    }

    /**
     * Execute an operation in the background.
     * This method starts a new thread executing the operation and returns immediately.
     *
     * @param operation The operation to execute in the background.
     *
     * @return A {@link BackgroundOperation} for retrieving the result of the operation later.
     */

    public <T> BackgroundOperation<T> executeBackground(Operation<T> operation)
    {
        return new BackgroundOperation<T>(operation);
    }

    public int getWeight()
    {
        return 1;
    }
}
