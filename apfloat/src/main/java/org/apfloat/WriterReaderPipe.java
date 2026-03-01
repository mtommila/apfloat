/*
 * MIT License
 *
 * Copyright (c) 2002-2026 Mikko Tommila
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
package org.apfloat;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Like PipedWriter / PipedReader but handles failures reliably.
 *
 * @since 1.16.0
 * @author Mikko Tommila
 */

class WriterReaderPipe {

    @FunctionalInterface
    public interface WriterTask
    {
        void writeTo(Writer writer)
            throws IOException;
    }

    private static final int DEFAULT_QUEUE_CAPACITY = 128;
    private static final int DEFAULT_CHUNK_SIZE = 8192;
    private static final char[] EOF = new char[0];

    private WriterReaderPipe()
    {
    }

    /**
     * Opens a Reader that will receive everything written by the given WriterTask.
     * The WriterTask runs asynchronously in a separate thread.
     * 
     * @param task The task that writes the data.
     *
     * @return A reader for the data that is written by the writer task.
     *
     * @throws IOException In case of I/O failure.
     */

    public static Reader open(WriterTask task)
        throws IOException
    {
        BlockingQueue<char[]> queue = new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
        AtomicBoolean cancelled = new AtomicBoolean(false);
        AtomicReference<Throwable> failure = new AtomicReference<>(null);

        // Use common ForkJoinPool instead of context ExecutorService because the task mostly does I/O and is not CPU-intensive
        Future<?> future = ForkJoinPool.commonPool().submit(() ->
        {
            try (Writer pipeWriter = new PipeWriter(queue, cancelled, failure))
            {
                task.writeTo(pipeWriter);
                queue.put(EOF);
            }
            catch (Throwable t)
            {
                failure.compareAndSet(null, t);
                cancelled.set(true);
            }
        });

        PipeReader pipeReader = new PipeReader(queue, cancelled, failure, future);
        return pipeReader;
    }

    private static class PipeWriter
        extends Writer
    {
        private BlockingQueue<char[]> queue;
        private AtomicBoolean cancelled;
        private AtomicReference<Throwable> failure;
        private volatile boolean closed;

        public PipeWriter(BlockingQueue<char[]> queue, AtomicBoolean cancelled, AtomicReference<Throwable> failure)
        {
            this.queue = queue;
            this.cancelled = cancelled;
            this.failure = failure;
        }

        @Override
        public void write(char[] cbuf, int off, int len)
            throws IOException
        {
            ensureOpen();
            if (len == 0)
            {
                return;
            }
            int end = off + len;
            while (off < end)
            {
                checkFailure();
                int chunkLen = Math.min(DEFAULT_CHUNK_SIZE, end - off);
                char[] chunk = new char[chunkLen];
                System.arraycopy(cbuf, off, chunk, 0, chunkLen);
                off += chunkLen;
                try
                {
                    queue.put(chunk);
                }
                catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                    cancelled.set(true);
                    throw new IOException("Writer interrupted", ie);
                }
            }
        }

        @Override
        public void flush()
            throws IOException
        {
            ensureOpen();
            checkFailure();
        }

        @Override
        public void close()
        {
            closed = true;
        }

        private void ensureOpen()
            throws IOException
        {
            if (closed)
            {
                throw new IOException("Writer closed");
            }
        }

        private void checkFailure()
            throws IOException
        {
            Throwable t = failure.get();
            if (t != null)
            {
                if (t instanceof IOException)
                {
                    throw (IOException) t;
                }
                throw new IOException("Writer task failed", t);
            }
            if (cancelled.get())
            {
                throw new IOException("Pipe cancelled");
            }
        }
    }

    private static class PipeReader
        extends Reader
    {
        private BlockingQueue<char[]> queue;
        private AtomicBoolean cancelled;
        private AtomicReference<Throwable> failure;
        private Future<?> producerFuture;
        private volatile boolean closed;
        private char[] current = null;
        private int pos = 0;
        private boolean eof = false;

        public PipeReader(BlockingQueue<char[]> queue, AtomicBoolean cancelled, AtomicReference<Throwable> failure, Future<?> producerFuture)
        {
            this.queue = queue;
            this.cancelled = cancelled;
            this.failure = failure;
            this.producerFuture = producerFuture;
        }

        @Override
        public int read(char[] cbuf, int off, int len)
            throws IOException
        {
            ensureOpen();
            if (len == 0)
            {
                return 0;
            }
            checkFailure();
            if (eof)
            {
                return -1;
            }

            int totalRead = 0;
            while (len > 0)
            {
                if (current == null || pos >= current.length)
                {
                    char[] next = takeNextChunk();
                    if (next == EOF)
                    {
                        eof = true;
                    }
                    current = next;
                    pos = 0;
                }
                int remaining = current.length - pos;
                int toCopy = Math.min(remaining, len);
                System.arraycopy(current, pos, cbuf, off, toCopy);
                pos += toCopy;
                off += toCopy;
                len -= toCopy;
                totalRead += toCopy;
                if (totalRead > 0 || eof)
                {
                    break; // Return as soon as we have some data
                }
            }
            return totalRead == 0 ? -1 : totalRead;
        }

        private char[] takeNextChunk()
            throws IOException
        {
            while (true)
            {
                try
                {
                    checkFailure();
                    char[] chunk = queue.poll(1, TimeUnit.NANOSECONDS);
                    if (chunk == null)
                    {
                        continue;
                    }
                    /*
                    char[] chunk = queue.poll();
                    if (chunk == null)
                    {
                        Thread.yield();
                        continue;
                    }
                    */
                    return chunk;
                }
                catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                    cancelled.set(true);
                    throw new IOException("Reader interrupted", ie);
                }
            }
        }

        @Override
        public void close()
            throws IOException
        {
            if (closed)
            {
                return;
            }
            closed = true;
            cancelled.set(true);
            cancelProducer();
        }

        private void cancelProducer()
        {
            producerFuture.cancel(true);
        }

        private void ensureOpen()
            throws IOException
        {
            if (closed)
            {
                throw new IOException("Reader closed");
            }
        }

        private void checkFailure()
            throws IOException
        {
            Throwable t = failure.get();
            if (t != null)
            {
                if (t instanceof IOException)
                {
                    throw (IOException) t;
                }
                throw new IOException("Writer task failed", t);
            }
            if (cancelled.get())
            {
                throw new IOException("Pipe cancelled");
            }
        }
    }
}
