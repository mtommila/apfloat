/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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
