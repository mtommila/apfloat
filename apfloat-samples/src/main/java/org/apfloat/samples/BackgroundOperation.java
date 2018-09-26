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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import org.apfloat.ApfloatContext;

/**
 * Class for running an {@link Operation} in the background in a separate thread.
 * The operation is executed using the ExecutorService retrieved from
 * {@link ApfloatContext#getExecutorService()}.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class BackgroundOperation<T>
{
    /**
     * Runs an operation in the background in a separate thread.
     * The execution is started immediately.
     *
     * @param operation The operation to execute.
     */

    public BackgroundOperation(Operation<T> operation)
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        ExecutorService executorService = ctx.getExecutorService();
        Callable<T> callable = () -> operation.execute();

        this.future = executorService.submit(callable);
    }

    /**
     * Check if the operation has been completed.
     *
     * @return <code>true</code> if the execution of the operation has been completed, otherwise <code>false</code>.
     */

    public boolean isFinished()
    {
        return this.future.isDone();
    }

    /**
     * Get the result of the operation.
     * This method blocks until the operation has been completed.
     *
     * @return Result of the operation.
     *
     * @exception RuntimeException If an exception was thrown by the executed operation.
     */

    public T getResult()
    {
        try
        {
            return this.future.get();
        }
        catch (InterruptedException ie)
        {
            throw new RuntimeException(ie);
        }
        catch (ExecutionException ee)
        {
            throw new RuntimeException(ee);
        }
    }

    private Future<T> future;
}
