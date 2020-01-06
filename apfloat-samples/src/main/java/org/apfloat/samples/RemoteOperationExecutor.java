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
