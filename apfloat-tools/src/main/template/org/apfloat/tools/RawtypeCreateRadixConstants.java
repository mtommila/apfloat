/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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
package org.apfloat.tools;

import static org.apfloat.internal.RawtypeModConstants.*;

/**
 * This utility class can be used to generate the file
 * <code>RawtypeRadixConstants.java</code>.
 *
 * @version 1.2
 * @author Mikko Tommila
 */

public class RawtypeCreateRadixConstants
{
    public static void main(String[] args)
    {
        StringBuilder base = new StringBuilder("{ ");
        StringBuilder baseDigits = new StringBuilder("{ ");
        StringBuilder minimumForDigits = new StringBuilder("{ ");
        StringBuilder maxExponent = new StringBuilder("{ ");
        for (int i = 0 ; i <= Character.MAX_RADIX; i++)
        {
            int radixBaseDigits;
            long radixBase = 1;
            StringBuilder buffer = new StringBuilder("{ ");

            // MODULUS[2] must be the smallest of the moduli
            assert (MODULUS[1] <= MODULUS[0]);
            assert (MODULUS[2] <= MODULUS[1]);
            for (radixBaseDigits = 0;
                 i >= Character.MIN_RADIX && radixBase <= MODULUS[2] / i;
                 radixBaseDigits++, radixBase *= i)
            {
                buffer.append(radixBaseDigits > 0 ? ", " : "").append("(rawtype) ").append(radixBase).append('L');
            }
            buffer.append(" }");

            long exponent = (i >= Character.MIN_RADIX ? Long.MAX_VALUE / radixBaseDigits - 6: -1);      // Leave enough slack e.g. for toString()

            base.append(i > 0 ? ", " : "").append("(rawtype) ").append(i >= Character.MIN_RADIX ? radixBase : -1).append('L');
            baseDigits.append(i > 0 ? ", " : "").append(i >= Character.MIN_RADIX ? radixBaseDigits : -1);
            minimumForDigits.append(i > 0 ? ", " : "").append(i >= Character.MIN_RADIX ? buffer : null);
            maxExponent.append(i > 0 ? ", " : "").append(exponent).append('L');
        }
        base.append(" };");
        baseDigits.append(" };");
        minimumForDigits.append(" };");
        maxExponent.append(" };");

        System.out.println("package org.apfloat.internal;");
        System.out.println("");
        System.out.println("public interface RawtypeRadixConstants");
        System.out.println("{");
        System.out.println("    public static final rawtype BASE[] = " + base);
        System.out.println("    public static final int BASE_DIGITS[] = " + baseDigits);
        System.out.println("    public static final rawtype MINIMUM_FOR_DIGITS[][] = " + minimumForDigits);
        System.out.println("    public static final long MAX_EXPONENT[] = " + maxExponent);
        System.out.println("}");
    }
}
