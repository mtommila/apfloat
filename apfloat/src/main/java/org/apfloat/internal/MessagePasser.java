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
package org.apfloat.internal;

import java.util.Map;
import java.util.HashMap;

import org.apfloat.ApfloatRuntimeException;

/**
 * Message passing helper class for parallel codes.
 * The message passer can hold one message for each receiver.
 * This class is safe for concurrent use from multiple threads.
 *
 * @param <K> The receiver type for this message passer.
 * @param <V> The message type for this message passer.
 *
 * @since 1.6
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class MessagePasser<K, V>
{
    private Map<K, V> messages;

    /**
     * Default constructor.
     */

    public MessagePasser()
    {
        this.messages = new HashMap<>();
    }

    /**
     * Send a message.
     *
     * @param receiver The receiver.
     * @param message The message. Must not be <code>null</code>.
     */

    public synchronized void sendMessage(K receiver, V message)
    {
        assert (message != null);
        assert (!this.messages.containsKey(receiver));

        this.messages.put(receiver, message);

        notifyAll();
    }

    /**
     * Get a message if one is available. This method will not block.
     *
     * @param receiver The receiver.
     *
     * @return The message, or <code>null</code> if none is available.
     */

    public synchronized V getMessage(K receiver)
    {
        V message = this.messages.remove(receiver);

        return message;
    }

    /**
     * Receive a message. This method will block until a message is available.
     *
     * @param receiver The receiver.
     *
     * @return The message.
     */

    public synchronized V receiveMessage(K receiver)
        throws ApfloatRuntimeException
    {
        V message;
        while ((message = this.messages.remove(receiver)) == null)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ie)
            {
                throw new ApfloatInternalException("Wait for received message interrupted", ie);
            }
        }

        return message;
    }
}
