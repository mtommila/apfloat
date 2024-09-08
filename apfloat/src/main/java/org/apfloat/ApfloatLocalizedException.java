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

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Interface for exceptions that have localized messages.
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public interface ApfloatLocalizedException
{
    /**
     * Returns the localized detail message.
     *
     * @return The localized detail message.
     */

    default public String getLocalizedMessage()
    {
        String localizationKey = getLocalizationKey();
        if (localizationKey == null)
        {
            return getMessage();
        }
        String localizedMessage;
        try
        {
            String pattern = ResourceBundle.getBundle("org.apfloat.apfloat-exceptions").getString(localizationKey);
            localizedMessage = MessageFormat.format(pattern, getLocalizationArgs());
        }
        catch (MissingResourceException mre)
        {
            localizedMessage = getMessage();
        }
        return localizedMessage;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return The detail message string of this {@code Throwable} instance (which may be {@code null}).
     */

    public String getMessage();

    /**
     * Returns the localization key.
     *
     * @return The localization key.
     */

    public String getLocalizationKey();

    /**
     * Returns the localization arguments.
     *
     * @return The localization arguments.
     */

    public Object[] getLocalizationArgs();
}
