package org.apfloat;

import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @since 1.6
 * @version 1.6
 * @author Mikko Tommila
 */

public class GCDHelperTest
    extends TestCase
{
    public GCDHelperTest(String methodName)
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

        suite.addTest(new GCDHelperTest("testGcd"));

        return suite;
    }

    public static void testGcd()
    {
        Random random = new Random();

        for (int i = 0; i < 5; i++)
        {
            Apint a = new Apint(getString(random));
            Apint b = new Apint(getString(random));

            Apint c = elementaryGcd(a, b);
            Apint d = ApintMath.gcd(a, b);

            assertEquals("GCD", c, d);
        }
    }

    private static String getString(Random random)
    {
        int length = random.nextInt(65000) + 35000;
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            buffer.append((char) (random.nextInt(10) + (int) '0'));
        }

        return buffer.toString();
    }

    private static Apint elementaryGcd(Apint a, Apint b)
        throws ApfloatRuntimeException
    {
        while (b.signum() != 0)
        {
            Apint r = a.mod(b);
            a = b;
            b = r;
        }

        return ApintMath.abs(a);
    }
}
