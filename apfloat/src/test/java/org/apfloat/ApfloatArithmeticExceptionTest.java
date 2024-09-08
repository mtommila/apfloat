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

import java.util.Locale;

import junit.framework.TestSuite;

/**
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class ApfloatArithmeticExceptionTest
    extends ApfloatTestCase
{
    public ApfloatArithmeticExceptionTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new ApfloatArithmeticExceptionTest("testLocalizedMessage"));
        suite.addTest(new ApfloatArithmeticExceptionTest("testLocalizedMessageWithArgs"));

        return suite;
    }

    @Override
    protected void setUp()
    {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.forLanguageTag("en-US"));
    }

    @Override
    protected void tearDown()
    {
        Locale.setDefault(defaultLocale);
    }

    public static void testLocalizedMessage()
    {
        ApfloatArithmeticException e = new ApfloatArithmeticException(null, "divide.byZero");
        assertEquals("Localization key", "divide.byZero", e.getLocalizationKey());
        assertEquals("Localization args length", 0, e.getLocalizationArgs().length);
        assertEquals("Localized message", "Division by zero", e.getLocalizedMessage());
    }

    public static void testLocalizedMessageWithArgs()
    {
        ApfloatArithmeticException e = new ApfloatArithmeticException(null, "w.ofZero", -1);
        assertEquals("Localization key", "w.ofZero", e.getLocalizationKey());
        assertEquals("Localization args length", 1, e.getLocalizationArgs().length);
        assertEquals("First localization arg", -1, e.getLocalizationArgs()[0]);
        assertEquals("Localized message", "W_-1 of zero", e.getLocalizedMessage());
    }

    private Locale defaultLocale;
}
