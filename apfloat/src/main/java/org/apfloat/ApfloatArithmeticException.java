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
 * Exception indicating a mathematical condition.
 * For example if the result would be infinite or undefined, this exception is thrown.
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class ApfloatArithmeticException
    extends ArithmeticException
    implements ApfloatLocalizedException
{
    /**
     * Constructs a new apfloat arithmetic exception with an empty detail message.
     */

    public ApfloatArithmeticException()
    {
    }

    /**
     * Constructs a new apfloat arithmetic exception with the specified detail message.
     *
     * @param message The detail message.
     */

    public ApfloatArithmeticException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new apfloat arithmetic exception with the specified detail message and localization data.
     *
     * @param message The detail message.
     * @param localizationKey The localization key.
     * @param localizationArgs The localization arguments.
     */

    public ApfloatArithmeticException(String message, String localizationKey, Object... localizationArgs)
    {
        super(message);
        this.localizationKey = localizationKey;
        this.localizationArgs = localizationArgs.clone();
    }

    @Override
    public String getLocalizedMessage()
    {
        return ApfloatLocalizedException.super.getLocalizedMessage();
    }

    /**
     * Returns the localization key.
     *
     * @return The localization key.
     */

    @Override
    public String getLocalizationKey()
    {
        return this.localizationKey;
    }

    /**
     * Returns the localization arguments.
     *
     * @return The localization arguments.
     */

    @Override
    public Object[] getLocalizationArgs()
    {
        return this.localizationArgs.clone();
    }

    private static final long serialVersionUID = 1L;

    private String localizationKey;
    private Object[] localizationArgs;
}
