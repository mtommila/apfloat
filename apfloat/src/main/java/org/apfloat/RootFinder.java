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

import java.util.function.BiFunction;
import java.util.function.Function;

import org.apfloat.spi.Util;

/**
 * Helper class for finding roots of functions.
 *
 * @since 1.13.0
 * @version 1.13.0
 * @author Mikko Tommila
 */

class RootFinder
{
    private RootFinder()
    {
    }

    /**
     * Find the root of the given function with Newton's method, starting from the given initial guess.
     *
     * @param f The function.
     * @param fp The derivative of the function, first argument is x, second argument is f(x)
     * @param initialGuess The initial guess
     * @param targetPrecision Target precision of the result
     *
     * @return The root
     */
    public static Apfloat findRoot(Function<Apfloat, Apfloat> f, BiFunction<Apfloat, Apfloat, Apfloat> fp, Apfloat initialGuess, long targetPrecision)
    {
        Apfloat x = initialGuess;
        long workingPrecision = x.precision(),
             precision;
        do
        {
            Apfloat fn = f.apply(x),
                    fpn = fp.apply(x, fn),
                    d = fn.divide(fpn);
            precision = (d.signum() == 0 ? x.precision() : x.scale() - d.scale());
            if (precision > 0)
            {
                workingPrecision = Math.max(workingPrecision, Util.ifFinite(precision, 2 * Util.ifFinite(precision, 2 * precision)));
            }
            x = x.subtract(d).precision(workingPrecision);
        } while (precision < targetPrecision);

        return x.precision(targetPrecision);
    }
}
