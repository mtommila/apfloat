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

import junit.framework.TestSuite;

/**
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class ApfloatRuntimeExceptionTest
    extends ApfloatTestCase
{
    public ApfloatRuntimeExceptionTest(String methodName)
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

        suite.addTest(new ApfloatRuntimeExceptionTest("testLocalizedMessage"));
        suite.addTest(new ApfloatRuntimeExceptionTest("testLocalizedMessageWithArgs"));

        return suite;
    }

    public static void testLocalizedMessage()
    {
        ApfloatRuntimeException e = new ApfloatRuntimeException(null, "w.infinitePrecision");
        assertEquals("Localization key", "w.infinitePrecision", e.getLocalizationKey());
        assertEquals("Localization args length", 0, e.getLocalizationArgs().length);
        assertEquals("Localized message", "Cannot calculate W to infinite precision", e.getLocalizedMessage());
    }

    public static void testLocalizedMessageWithArgs()
    {
        ApfloatRuntimeException e = new ApfloatRuntimeException(null, "maximumTransformLengthExceeded", 2, 1);
        assertEquals("Localization key", "maximumTransformLengthExceeded", e.getLocalizationKey());
        assertEquals("Localization args length", 2, e.getLocalizationArgs().length);
        assertEquals("First localization arg", 2, e.getLocalizationArgs()[0]);
        assertEquals("Second localization arg", 1, e.getLocalizationArgs()[1]);
        assertEquals("Localized message", "Maximum transform length exceeded: 2 > 1", e.getLocalizedMessage());
    }
}
