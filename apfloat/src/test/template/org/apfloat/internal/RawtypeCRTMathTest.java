package org.apfloat.internal;

import junit.framework.TestSuite;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeCRTMathTest
    extends RawtypeTestCase
    implements RawtypeModConstants
{
    public RawtypeCRTMathTest(String methodName)
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

        suite.addTest(new RawtypeCRTMathTest("testMultiply"));
        suite.addTest(new RawtypeCRTMathTest("testCompare"));
        suite.addTest(new RawtypeCRTMathTest("testAdd"));
        suite.addTest(new RawtypeCRTMathTest("testSubtract"));
        suite.addTest(new RawtypeCRTMathTest("testDivide"));

        return suite;
    }

    public static void testMultiply()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype[] src = { b1, b1 };
        rawtype[] dst = new rawtype[3];

        new RawtypeCRTMath(2).multiply(src, b1, dst);

        assertEquals("max[0]", (long) b1 - 1, (long) dst[0]);
        assertEquals("max[1]", (long) b1, (long) dst[1]);
        assertEquals("max[2]", (long) 1, (long) dst[2]);

        src = new rawtype[] { (rawtype) 2, (rawtype) 4 };

        new RawtypeCRTMath(2).multiply(src, (rawtype) 3, dst);

        assertEquals("normal[0]", 0, (long) dst[0]);
        assertEquals("normal[1]", 6, (long) dst[1]);
        assertEquals("normal[2]", 12, (long) dst[2]);
    }

    public static void testCompare()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 },
                                                new rawtype[] { (rawtype) 2, (rawtype) 1, (rawtype) 1 });
        assertTrue("1st", result < 0);

        result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 2, (rawtype) 1 },
                                                new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 });
        assertTrue("2nd", result > 0);

        result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 0 },
                                                new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) b1 });
        assertTrue("3rd", result < 0);

        result = new RawtypeCRTMath(2).compare(new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 },
                                                new rawtype[] { (rawtype) 1, (rawtype) 1, (rawtype) 1 });
        assertTrue("equal", result == 0);
    }

    public static void testAdd()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype[] src = { b1, b1, b1 };
        rawtype[] srcDst = { b1, b1, b1 };

        rawtype carry = new RawtypeCRTMath(2).add(src, srcDst);

        assertEquals("max carry", 1, (long) carry);
        assertEquals("max[0]", (long) b1, (long) srcDst[0]);
        assertEquals("max[1]", (long) b1, (long) srcDst[1]);
        assertEquals("max[2]", (long) b1 - 1, (long) srcDst[2]);

        src = new rawtype[] { (rawtype) 2, (rawtype) 4, (rawtype) 6 };
        srcDst = new rawtype[] { (rawtype) 3, (rawtype) 5, (rawtype) 7 };

        carry = new RawtypeCRTMath(2).add(src, srcDst);

        assertEquals("normal carry", 0, (long) carry);
        assertEquals("normal[0]", 5, (long) srcDst[0]);
        assertEquals("normal[1]", 9, (long) srcDst[1]);
        assertEquals("normal[2]", 13, (long) srcDst[2]);
    }

    public static void testSubtract()
    {
        rawtype b1 = MAX_POWER_OF_TWO_BASE - (rawtype) 1;
        rawtype[] src = { b1, b1, b1 };
        rawtype[] srcDst = { b1, b1, b1 };

        new RawtypeCRTMath(2).subtract(src, srcDst);

        assertEquals("max[0]", 0, (long) srcDst[0]);
        assertEquals("max[1]", 0, (long) srcDst[1]);
        assertEquals("max[2]", 0, (long) srcDst[2]);

        src = new rawtype[] { 0, 0, (rawtype) 1 };
        srcDst = new rawtype[] { (rawtype) 1, 0, 0 };

        new RawtypeCRTMath(2).subtract(src, srcDst);

        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", (long) b1, (long) srcDst[1]);
        assertEquals("normal[2]", (long) b1, (long) srcDst[2]);
    }

    public static void testDivide()
    {
        rawtype[] srcDst = new rawtype[] { (rawtype) 1, 0, 1 };

        rawtype remainder = new RawtypeCRTMath(2).divide(srcDst);

        assertEquals("normal remainder", 1, (long) remainder);
        assertEquals("normal[0]", 0, (long) srcDst[0]);
        assertEquals("normal[1]", 2, (long) srcDst[1]);
        assertEquals("normal[2]", 0, (long) srcDst[2]);
    }
}
