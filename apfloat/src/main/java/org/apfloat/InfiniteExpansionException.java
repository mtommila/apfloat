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
package org.apfloat;

/**
 * Exception indicating that the result of an operation
 * would have infinite size.<p>
 *
 * For example, <code>new Apfloat(2).divide(new Apfloat(3))</code>, in radix 10.
 *
 * @since 1.5
 * @version 1.5
 * @author Mikko Tommila
 */

public class InfiniteExpansionException
    extends ApfloatRuntimeException
{
    /**
     * Constructs a new apfloat infinite expansion exception with an empty detail message.
     */

    public InfiniteExpansionException()
    {
    }

    /**
     * Constructs a new apfloat infinite expansion exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public InfiniteExpansionException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat infinite expansion exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public InfiniteExpansionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = -7022924635011038776L;
}
