/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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

import org.apfloat.ApfloatContext;
import org.apfloat.internal.RawtypeMatrixStrategy;
import org.apfloat.internal.RawtypeMemoryArrayAccess;

/**
 * This tool can be used for testing the matrix transposition
 * algorithm performance.<p>
 *
 * Note that you can't directly specify, which of the three
 * transposition algorithms is used internally in the matrix
 * class. You can specify it indirectly as follows:
 *
 * <ul>
 *   <li>"L1 cache algorithm": set L1 cache size to a maximum value</li>
 *   <li>"L2 cache algorithm": set L1 cache size to zero and L2 cache size to a maximum value</li>
 *   <li>"main memory algorithm": set L1 cache size and L2 cache size to zero</li>
 * </ul>
 *
 * Usually, the overall best performance is achieved by specifying
 * correctly the L1 and L2 cache sizes. If the L2 cache is very fast,
 * transposing matrixes that fit to the L2 cache entirely may be
 * faster using the "L1 cache algorithm". This can be done by (incorrectly)
 * setting the L1 cache size to the same value as the L2 cache size.
 * But with this setting transposing matrixes that do not fit to the
 * L2 cache is usually slower than when the L1 cache size is set correctly.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeMatrixTime
{
    public static void main(String[] args)
    {
        if (args.length < 3)
        {
            System.err.println("Usage: RawtypeMatrixTime n L1CacheSize L2CacheSize [repetitions]");
            System.err.println("       where the matrix size is n x n");
            return;
        }

        int n = Integer.parseInt(args[0]),
            l1CacheSize = Integer.parseInt(args[1]),
            l2CacheSize = Integer.parseInt(args[2]),
            reps = (args.length > 3 ? Integer.parseInt(args[3]) : 1),
            size = n * n;

        ApfloatContext.getContext().setCacheL1Size(l1CacheSize);
        ApfloatContext.getContext().setCacheL2Size(l2CacheSize);

        RawtypeMemoryArrayAccess arrayAccess = new RawtypeMemoryArrayAccess(new rawtype[size], 0, size);
        RawtypeMatrixStrategy rawtypeMatrix = new RawtypeMatrixStrategy();

        long minTime = Long.MAX_VALUE;

        for (int i = 0; i < TESTS; i++)
        {
            long time = System.currentTimeMillis();

            for (int j = 0; j < reps; j++)
            {
                rawtypeMatrix.transpose(arrayAccess, n, n);
            }

            time = System.currentTimeMillis() - time;
            minTime = Math.min(time, minTime);
        }

        System.out.println(n + " x " + n + " matrix transposition " + (float) minTime / reps + " ms");
    }

    private static final int TESTS = 3;
}
