/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
 * Class to execute {@link Operation}s locally.
 * The execution is done in the current JVM.
 *
 * @version 1.9.0
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

    @Override
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

    @Override
    public <T> BackgroundOperation<T> executeBackground(Operation<T> operation)
    {
        return new BackgroundOperation<>(operation);
    }

    @Override
    public int getWeight()
    {
        return 1;
    }
}
