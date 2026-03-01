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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

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
            return new Apint(new PushbackReader(WriterReaderPipe.open(out -> {
                if (!(out instanceof BufferedWriter))
                {
                    out = new BufferedWriter(out);
                }
                for (int i = a.length - 1; i >= 0; i--)
                {
                    if (i < a.length - 1)
                    {
                        pad(out, stride - scale(a[i]));
                    }
                    a[i].writeTo(out);
                }
                out.flush();
            })), radix, scale(a[a.length - 1]) + stride * (a.length - 1));
        }
        catch (IOException ioe)
        {
            throw new ApfloatRuntimeException("Should not occur", ioe, "shouldNotOccur");
        }
    }

    private static long scale(Apint i)
    {
        return Math.max(1, i.scale());  // When printed out, zero has one digit i.e. effectively equivalent to a scale of 1
    }

    private static void pad(Writer out, long count)
        throws IOException
    {
        while (count-- > 0)
        {
            out.write('0');
        }
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
            Reader in = new BufferedReader(WriterReaderPipe.open(a::writeTo));
            for (int i = array.length - 1; i >= 0; i--)
            {
                long limit = (i == array.length - 1 ? digits - (length - 1) * stride : stride);
                array[i] = new Apint(new PushbackReader(new LimitedReader(in, limit)), radix, limit);
            }
        }
        catch (IOException ioe)
        {
            throw new ApfloatRuntimeException("Should not occur", ioe, "shouldNotOccur");
        }
        return array;
    }

    private static class LimitedReader
        extends Reader
    {
        private Reader in;
        private long remaining;

        public LimitedReader(Reader in, long limit)
        {
            this.in = in;
            this.remaining = limit;
        }

        @Override
        public int read(char[] cbuf, int off, int len)
            throws IOException
        {
            len = (int) Math.min(remaining, len);
            remaining -= len;
            return in.read(cbuf, off, len);
        }

        @Override
        public void close()
            throws IOException
        {
            in.close();
        }
    }
}
