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
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Combines multiple Apints into one, or splits one to multiple, for Kronecker substitution.
 *
 * @since 1.16.0
 * @author Mikko Tommila
 */

class Splitter {

    private Splitter()
    {
    }

    public static Apint combine(long stride, Apint... a)
    {
        // Equivalent to a sum of the numbers in array a, each element multiplied by radix^(stride * index)
        assert (a.length > 0);
        assert (Arrays.stream(a).mapToInt(Apint::radix).distinct().count() == 1);
        int radix = a[0].radix();
        try
        {
            List<Reader> readers = new ArrayList<>();
            for (int i = a.length - 1; i >= 0; i--)
            {
                if (i < a.length - 1)
                {
                    readers.add(padReader(stride - scale(a[i])));
                }
                readers.add(a[i].toReader());
            }
            
            long size = scale(a[a.length - 1]) + stride * (a.length - 1);
            return new Apint(new LimitedReader(new ConcatReader(readers), size), radix, size);
        }
        catch (IOException ioe)
        {
            if (ioe.getCause() instanceof InterruptedException)
            {
                throw new ApfloatInterruptedException("Interrupted", ioe, "interrupted");
            }
            throw new ApfloatRuntimeException("Should not occur", ioe, "shouldNotOccur");
        }
    }

    private static long scale(Apint i)
    {
        return Math.max(1, i.scale());  // When printed out, zero has one digit i.e. effectively equivalent to a scale of 1
    }

    private static Reader padReader(long count)
    {
        return new Reader()
        {
            @Override
            public int read()
            {
                if (remaining <= 0)
                {
                    return -1;
                }

                remaining--;
                return '0';
            }

            @Override
            public int read(char[] buffer, int offset, int length)
            {
                if (remaining <= 0)
                {
                    return -1;
                }

                int n = (int) Math.min(length, remaining);
                for (int i = 0; i < n; i++)
                {
                    buffer[offset + i] = '0';
                }
                remaining -= n;
                return n;
            }

            @Override
            public void close()
            {
            }

            private long remaining = count;
        };
    }

    public static Apint[] split(long stride, Apint a)
    {
        // Equivalent to a / radix^(stride * index) % radix^stride
        int radix = a.radix();
        long digits = scale(a),
             length = (digits + stride - 1) / stride;
        if (length > Integer.MAX_VALUE)
        {
            throw new ApfloatRuntimeException("Maximum array size exceeded: " + length, "maximumArraySizeExceeded", length);
        }
        Apint[] array = new Apint[(int) length];
        try
        {
            Reader in = a.toReader();
            for (int i = array.length - 1; i >= 0; i--)
            {
                long limit = (i == array.length - 1 ? digits - (length - 1) * stride : stride);
                array[i] = new Apint(new LimitedReader(in, limit), radix, limit);
            }
        }
        catch (IOException ioe)
        {
            if (ioe.getCause() instanceof InterruptedException)
            {
                throw new ApfloatInterruptedException("Interrupted", ioe, "interrupted");
            }
            throw new ApfloatRuntimeException("Should not occur", ioe, "shouldNotOccur");
        }
        return array;
    }

    private static class LimitedReader
        extends PushbackReader
    {
        public LimitedReader(Reader in, long limit)
        {
            super(in);
            this.in = in;
            this.remaining = limit;
        }

        @Override
        public int read()
            throws IOException
        {
            if (remaining <= 0)
            {
                return -1;
            }
            remaining--;
            return in.read();
        }

        @Override
        public int read(char[] buffer, int offset, int length)
            throws IOException
        {
            if (remaining <= 0)
            {
                return -1;
            }
            length = (int) Math.min(remaining, length);
            int n = in.read(buffer, offset, length);
            if (n == -1)
            {
                throw new ApfloatRuntimeException("Should not occur", "shouldNotOccur");
            }
            remaining -= n;
            return n;
        }

        @Override
        public void close()
            throws IOException
        {
            in.close();
        }

        @Override
        public void unread(int c)
        {
            throw new ApfloatRuntimeException("Should not occur", "shouldNotOccur");
        }

        private Reader in;
        private long remaining;
    }
}
