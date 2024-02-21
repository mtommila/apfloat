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

import static org.apfloat.Apfloat.ONE;
import static org.apfloat.Apfloat.ZERO;
import static org.apfloat.ApfloatMath.besselJ;
import static org.apfloat.ApfloatMath.digamma;
import static org.apfloat.ApfloatMath.exp;
import static org.apfloat.ApfloatMath.gamma;

/**
 * @version 1.13.0
 * @author Mikko Tommila
 */

public class RootFinderTest
    extends ApfloatTestCase
{
    public RootFinderTest(String methodName)
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

        suite.addTest(new RootFinderTest("testFindRoot"));

        return suite;
    }

    public static void testFindRoot()
    {
        Apfloat x = new Apfloat(2, 100);
        Apfloat a = RootFinder.findRoot(y -> exp(y), (y, f) -> f, x, new Apfloat(0.5), 100);
        assertEquals("log(2) precision", 100, a.precision());
        assertEquals("log(2) value", new Apfloat("0.6931471805599453094172321214581765680755001343602552541206800094933936219696947156058633269964186875"), a, new Apfloat("5e-100"));

        a = RootFinder.findRoot(y -> gamma(y), (y, f) -> f.multiply(digamma(y)), x, new Apfloat(0.3), 100);
        assertEquals("inverseGamma(2) precision", 100, a.precision());
        assertEquals("inverseGamma(2) value", new Apfloat("0.4428773964847274374520325165206056717103645380663664029912307198958524822841740804077009377298448221"), a, new Apfloat("5e-100"));

        a = RootFinder.findRoot(y -> besselJ(ZERO, y), (y, f) -> besselJ(ONE, y).negate(), ZERO, new Apfloat(2.4), 100);
        assertEquals("J_0 first zero precision", 100, a.precision());
        assertEquals("J_0 first zero value", new Apfloat("2.404825557695772768621631879326454643124244909145967135706999090596765838677194029204436343760145255"), a, new Apfloat("5e-99"));
    }
}
