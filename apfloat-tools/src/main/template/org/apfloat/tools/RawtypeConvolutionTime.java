/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
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

import java.util.Random;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
import org.apfloat.spi.ConvolutionBuilder;
import org.apfloat.spi.NTTBuilder;
import org.apfloat.internal.RawtypeBuilderFactory;
import org.apfloat.internal.RawtypeKaratsubaConvolutionStrategy;
import org.apfloat.internal.RawtypeMediumConvolutionStrategy;
import org.apfloat.internal.ParallelThreeNTTConvolutionStrategy;

/**
 * This tool can be used to test whether the simple O(n<sup>2</sup>),
 * Karatsuba or the triple-NTT convolution algorithm is fastest for
 * specified multiplicand sizes. Tuning the algorithm selection
 * properly is crucial for a {@link ConvolutionBuilder} to work
 * efficiently.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class RawtypeConvolutionTime
{
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("Usage: RawtypeConvolutionTime n m [repetitions]");
            System.err.println("       where the convolution size is n + m digits");
            return;
        }

        NTTBuilder nttBuilder = ApfloatContext.getContext().getBuilderFactory().getNTTBuilder();
        float mediumTime = run(args, "medium", (radix, size1, size2, resultSize) -> new RawtypeMediumConvolutionStrategy(radix));
        float karatsubaTime = run(args, "Karatsuba", (radix, size1, size2, resultSize) -> new RawtypeKaratsubaConvolutionStrategy(radix));
        float nttTime = run(args, "3 NTT", (radix, size1, size2, resultSize) -> new ParallelThreeNTTConvolutionStrategy(radix, nttBuilder.createNTT(size1 + size2)));

        int n = Integer.parseInt(args[0]),
            m = Integer.parseInt(args[1]);

        System.out.println(n + " x " + m + " convolution medium " + mediumTime + " ms, Karatsuba " + karatsubaTime + " ms, NTT " + nttTime + " ms");
    }

    private static float run(String[] args, String method, ConvolutionBuilder convolutionBuilder)
    {
        int n = Integer.parseInt(args[0]),
            m = Integer.parseInt(args[1]),
            reps = (args.length > 2 ? Integer.parseInt(args[2]) : 1),
            radix = ApfloatContext.getContext().getDefaultRadix();

        Random random = new Random();

        ApfloatContext.getContext().setBuilderFactory(new RawtypeBuilderFactory()
        {
            @Override
            public ConvolutionBuilder getConvolutionBuilder()
            {
                return convolutionBuilder;
            }
        });
        ApfloatContext.getContext().setCacheL1Size(Integer.MAX_VALUE);  // To use simple single-thread NTT only

        long minTime = Long.MAX_VALUE;

        int length = Math.max(n, m);
        StringBuilder buffer = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            buffer.append(Character.forDigit(random.nextInt(radix), radix));
        }

        String s = buffer.toString();

        Apfloat a = new Apfloat(s.substring(0, n), Apfloat.INFINITE),
                b = new Apfloat(s.substring(0, m), Apfloat.INFINITE);

        int c = 0;

        for (int i = 0; i < TESTS; i++)
        {
            long time = System.currentTimeMillis();

            for (int j = 0; j < reps; j++)
            {
                c += System.identityHashCode(a.multiply(b));
            }

            time = System.currentTimeMillis() - time;
            minTime = Math.min(time, minTime);
        }

        System.err.println("Check: " + c);

        return (float) minTime / reps;
    }

    private static final int TESTS = 5;
}
