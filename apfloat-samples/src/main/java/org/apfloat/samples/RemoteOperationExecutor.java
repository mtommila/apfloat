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
 * @version 1.1
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

    public <T> T execute(Operation<T> operation)
    {
        SocketChannel channel = null;
        T result;

        try
        {
            channel = SocketChannel.open(new InetSocketAddress(this.host, this.port));

            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Channels.newOutputStream(channel), BUFFER_SIZE));
            out.writeObject(operation);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(channel));
            @SuppressWarnings("unchecked")
            T obj = (T) in.readObject();
            result = obj;
        }
        catch (IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new RuntimeException(cnfe);
        }
        finally
        {
            try
            {
                channel.socket().shutdownOutput();
                channel.close();
            }
            catch (Exception e)
            {
                // Ignore
            }
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

    public <T> BackgroundOperation<T> executeBackground(final Operation<T> operation)
    {
        return new BackgroundOperation<T>(new Operation<T>()
        {
            public T execute()
            {
                return RemoteOperationExecutor.this.execute(operation);
            }
        });
    }

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
