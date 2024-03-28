/*
 * MIT License
 *
 * Copyright (c) 2002-2024 Mikko Tommila
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
 * Exception indicating that the computation was interrupted.<p>
 *
 * This exception is semantically somewhat similar to {@link InterruptedException} 
 * except that it's a runtime exception and not a checked exception.<p>
 *
 * @since 1.14.0
 * @version 1.14.0
 * @author Mikko Tommila
 */

public class ApfloatInterruptedException
    extends ApfloatRuntimeException
{
    /**
     * Constructs a new apfloat interrupted exception with an empty detail message.
     */

    public ApfloatInterruptedException()
    {
    }

    /**
     * Constructs a new apfloat interrupted exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public ApfloatInterruptedException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat interrupted exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause Originating cause of the exception.
     */

    public ApfloatInterruptedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    private static final long serialVersionUID = 1L;
}
