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
