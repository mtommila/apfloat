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
 * @version 1.1
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

    public BackgroundOperation(final Operation<T> operation)
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        ExecutorService executorService = ctx.getExecutorService();
        Callable<T> callable = new Callable<T>()
        {
            public T call()
            {
                return operation.execute();
            }
        };

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
