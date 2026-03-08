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
package org.apfloat.internal;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.Supplier;

/**
 * Concatenates multiple reader suppliers lazily.
 *
 * @since 1.16.0
 * @author Mikko Tommila
 */

class LazyConcatReader
    extends Reader
{
    /**
     * Creates a Reader that concatenates the data from the given reader suppliers for lazy initialization.
     *
     * @param readers The underlying readers.
     */

    public LazyConcatReader(List<Supplier<Reader>> readers)
    {
        this.readers = readers;
        nextReader();
    }

    @Override
    public int read(char[] buffer, int offset, int length)
        throws IOException
    {
        while (this.index < this.readers.size())
        {
            int n = this.reader.read(buffer, offset, length);
            if (n != -1)
            {
                return n;
            }
            this.index++;
            nextReader();
        }
        return -1;
    }

    @Override
    public int read()
        throws IOException
    {
        while (this.index < this.readers.size())
        {
            int c = this.reader.read();
            if (c != -1)
            {
                return c;
            }
            this.index++;
            nextReader();
        }
        return -1;
    }

    @Override
    public void close()
        throws IOException
    {
        for (Supplier<Reader> reader : this.readers)
        {
            reader.get().close();
        }
    }

    private void nextReader()
    {
        if ((this.index < this.readers.size()))
        {
            this.reader = this.readers.get(this.index).get();
        }
    }

    private List<Supplier<Reader>> readers;
    private Reader reader;
    private int index = 0;
}
