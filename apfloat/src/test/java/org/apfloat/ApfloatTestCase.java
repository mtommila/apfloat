package org.apfloat;

import junit.framework.TestCase;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class ApfloatTestCase
    extends TestCase
{
    public ApfloatTestCase(String methodName)
    {
        super(methodName);
    }

    public static void assertEquals(String message, Apcomplex a, Apcomplex b, Apfloat delta)
    {
        if (ApcomplexMath.abs(a.subtract(b)).compareTo(delta) > 0)
        {
            assertEquals(message, a, b);
        }
    }

    public static void assertEquals(String message, Apfloat a, Apfloat b, Apfloat delta)
    {
        if (ApfloatMath.abs(a.subtract(b)).compareTo(delta) > 0)
        {
            assertEquals(message, a, b);
        }
    }
}
