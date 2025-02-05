/*
 * MIT License
 *
 * Copyright (c) 2002-2025 Mikko Tommila
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
package org.apfloat.aparapi;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

import org.apfloat.internal.NTTStrategyDecorator;
import org.apfloat.spi.ArrayAccess;
import org.apfloat.spi.NTTStrategy;

/**
 * NTT strategy with decorator to set up the data for the GPU, for the <code>int</code> element type.
 *
 * @since 1.15.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public interface IntAparapiNTTStrategy
    extends NTTStrategy, NTTStrategyDecorator
{
    /**
     * @hidden
     */
    static final ThreadLocal<AtomicInteger> LEVEL = ThreadLocal.withInitial(AtomicInteger::new);
    /**
     * @hidden
     */
    static final ThreadLocal<WeakReference<int[]>> DATA = ThreadLocal.withInitial(() -> new WeakReference<>(null));

    @Override
    default public void beforeTransform(ArrayAccess arrayAccess)
    {
        int[] data = arrayAccess.getIntData();
        AtomicInteger currentLevel = LEVEL.get();
        int[] currentData = DATA.get().get();

        if (currentLevel.get() == 0 || data != currentData)
        {
            // If level is 0 then this is the outermost transform
            IntKernel kernel = IntKernel.getInstance();
            kernel.setExplicit(true);
            kernel.put(data);
            DATA.set(new WeakReference<>(data));

            // If level is not 0 but we are not setting the same data, then something must have gone wrong previously, reset the level
            currentLevel.set(0);
        }
        // If level is > 0 and we are just setting the same data again, then this is for a nested transform, increase nesting level and do nothing else 
        currentLevel.incrementAndGet();
    }

    @Override
    default public void afterTransform(ArrayAccess arrayAccess)
    {
        AtomicInteger level = LEVEL.get();

        // Decrement nesting level 
        int currentLevel = level.decrementAndGet();
        // Only when we reach the outermost transform, get the data 
        if (currentLevel == 0)
        {
            IntKernel kernel = IntKernel.getInstance();
            kernel.get(arrayAccess.getIntData());
            kernel.cleanUpArrays();
        }
    }
}
