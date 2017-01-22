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
