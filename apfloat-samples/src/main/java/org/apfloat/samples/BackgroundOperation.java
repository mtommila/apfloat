/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
        ApfloatContext ctx = ApfloatContext.getContext();
        ctx.wait(this.future);
        try
        {
            return this.future.get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Future<T> future;
}
