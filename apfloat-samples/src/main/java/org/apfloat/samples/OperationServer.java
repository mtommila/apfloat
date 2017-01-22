package org.apfloat.samples;

import java.nio.channels.Channels;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.net.InetSocketAddress;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Server for executing {@link Operation}s from remote calls. The client
 * should simply send a class implementing the {@link Operation}
 * interface serialized through a socket connection. Obviously, the
 * class must exist also in the server's classpath. The server will then
 * simply call {@link Operation#execute()} on the operation, and send
 * the resulting object back in the socket, serialized. If an exception
 * occurs during the operation execution, nothing is returned and
 * the socket connection is closed.
 *
 * @version 1.4
 * @author Mikko Tommila
 */

public class OperationServer
{
    private static class Request
        implements Runnable
    {
        public Request(SocketChannel channel)
        {
            this.channel = channel;
        }

        public void run()
        {
            try
            {
                info("Request processing started");

                ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(this.channel));
                info("Ready to receive input data");
                Operation<?> operation = (Operation<?>) in.readObject();

                info("Executing operation");
                Object result = operation.execute();

                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Channels.newOutputStream(this.channel), BUFFER_SIZE));
                info("Ready to write output data");
                out.writeObject(result);
                out.flush();

                info("Request processing complete");
            }
            catch (Exception e)
            {
                // Avoid exiting the thread
                warning("Request processing failed", e);
            }
            finally
            {
                try
                {
                    this.channel.socket().shutdownOutput();
                    this.channel.close();
                }
                catch (Exception e)
                {
                    // Ignore
                }
            }
        }

        private SocketChannel channel;
    }

    private OperationServer()
    {
    }

    /**
     * Command-line entry point.
     *
     * @param args Command-line parameters.
     *
     * @exception IOException In case of unexpected network error.
     */

    public static void main(String[] args)
        throws IOException
    {
        int port,
            workerThreads = 1;

        if (args.length < 1)
        {
            System.err.println("USAGE: OperationServer port [workerThreads] [messageLevel]");

            return;
        }

        try
        {
            port = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("Invalid port: " + args[0]);

            return;
        }

        try
        {
            if (args.length > 1)
            {
                workerThreads = Integer.parseInt(args[1]);
                if (workerThreads <= 0)
                {
                    throw new NumberFormatException();
                }
            }
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("Invalid number of workerThreads: " + args[0]);

            return;
        }

        try
        {
            if (args.length > 2)
            {
                messageLevel = Integer.parseInt(args[2]);
            }
        }
        catch (NumberFormatException nfe)
        {
            System.err.println("Invalid messageLevel: " + args[0]);

            return;
        }

        // Create a selector object to monitor all open client connections.
        Selector selector = Selector.open();

        // Create a new non-blocking ServerSocketChannel, bind it to a port, and register it with the Selector
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();   // Open channel
        serverSocketChannel.configureBlocking(false);                           // Set non-blocking mode
        serverSocketChannel.socket().bind(new InetSocketAddress(port));         // Bind to port
        SelectionKey serverKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // Create a pool of worker threads that will run independently
        Executor executor = Executors.newFixedThreadPool(workerThreads);

        info("Waiting for connections to port " + port);

        while (true)    // The main server loop.  The server runs forever.
        {
            // This call blocks until there is activity on one of the registered channels
            selector.select();

            // Get a java.util.Set containing the SelectionKey objects for all channels that are ready for I/O
            Set<SelectionKey> keys = selector.selectedKeys();

            // Use a java.util.Iterator to loop through the selected keys
            for (Iterator<SelectionKey> i = keys.iterator(); i.hasNext(); )
            {
                SelectionKey key = i.next();
                i.remove();             // Remove the key from the set of selected keys

                // Check whether this key is the SelectionKey we got when we registered the ServerSocketChannel
                if (key == serverKey)
                {
                    // Activity on the ServerSocketChannel means a client is trying to connect to the server
                    if (key.isAcceptable())
                    {
                        // Accept the client connection
                        // Note that the client channel is in blocking mode; a separate thread will use it
                        SocketChannel clientSocketChannel = serverSocketChannel.accept();

                        info("New connection accepted");

                        // Put a new request to the queue so a worker thread can pick it up from there
                        executor.execute(new Request(clientSocketChannel));
                    }
                }
            }
        }
    }

    private static void warning(String message, Exception e)
    {
        if (messageLevel >= WARNING)
        {
            System.err.println("WARNING: " + Thread.currentThread().getName() + ": " + message);
            e.printStackTrace(System.err);
        }
    }

    private static void info(String message)
    {
        if (messageLevel >= INFO)
        {
            System.err.println("INFO: " + Thread.currentThread().getName() + ": " + message);
        }
    }

    private static void debug(String message)
    {
        if (messageLevel >= DEBUG)
        {
            System.err.println("DEBUG: " + Thread.currentThread().getName() + ": " + message);
        }
    }

    private static final int BUFFER_SIZE = 8192;
    private static final int ERROR = 0;
    private static final int WARNING = 1;
    private static final int INFO = 2;
    private static final int DEBUG = 3;

    private static int messageLevel = WARNING;
}
