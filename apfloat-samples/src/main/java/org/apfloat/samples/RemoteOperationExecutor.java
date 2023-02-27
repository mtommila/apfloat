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

import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Class to call an {@link OperationServer} to execute {@link Operation}s remotely.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RemoteOperationExecutor
    implements OperationExecutor
{
    /**
     * Create a new client that will connect to the server
     * running at the specified host and port.
     *
     * @param host Hostname of the remote server.
     * @param port Port of the remote server.
     */

    public RemoteOperationExecutor(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    /**
     * Execute an operation remotely.
     * This method will block until the return value has been received.
     *
     * @param operation The operation to execute remotely.
     *
     * @return The result of the operation.
     *
     * @exception RuntimeException In case of network error or if the return value class is unknown.
     */

    @Override
    public <T> T execute(Operation<T> operation)
    {
        T result;

        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(this.host, this.port)))
        {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Channels.newOutputStream(channel), BUFFER_SIZE));
            out.writeObject(operation);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(channel));
            @SuppressWarnings("unchecked")
            T obj = (T) in.readObject();
            result = obj;
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Execute an operation remotely.
     * This method starts a new thread running the remote call and returns immediately.
     *
     * @param operation The operation to execute remotely.
     *
     * @return A {@link BackgroundOperation} for retrieving the result of the operation later.
     */

    @Override
    public <T> BackgroundOperation<T> executeBackground(Operation<T> operation)
    {
        return new BackgroundOperation<>(() -> RemoteOperationExecutor.this.execute(operation));
    }

    @Override
    public int getWeight()
    {
        return 1;
    }

    /**
     * Returns the host name.
     *
     * @return The host name.
     */

    public String getHost()
    {
        return this.host;
    }

    /**
     * Returns the host port.
     *
     * @return The host port.
     */

    public int getPort()
    {
        return this.port;
    }

    private static final int BUFFER_SIZE = 8192;

    private String host;
    private int port;
}
