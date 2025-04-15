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

import com.aparapi.Kernel;

import org.apfloat.ApfloatRuntimeException;
import org.apfloat.spi.ArrayAccess;

/**
 * Kernel for the <code>int</code> element type. Contains everything needed for the NTT.<p>
 *
 * There are two ways the data can be organized:
 * <ul>
 *   <li>In columns: this is more efficient on older GPUs that don't have a cache</li>
 *   <li>In rows: this is more efficient on newer GPUs that have large high-bandwidth caches</li>
 * </ul>
 *
 * When the data is organized in columns, the NTT algorithm makes O(log n) passes through
 * the main memory of the GPU.<p>
 *
 * When the data is organized in rows, and the GPU cache is sufficiently large, the algorithm
 * makes a fixed number of passes through the main memory of the GPU (and O(log n) passes
 * through the GPU cache.)<p>
 *
 * Due to the extreme parallelization requirements (global size should be at lest 1024)
 * this algorithm works efficiently only with 4 million decimal digit calculations or bigger.
 * However with 4 million digits, it's only approximately as fast as the pure-Java
 * version (depending on the GPU and CPU hardware). On the other hand, the algorithm
 * mathematically only works up to about 226 million digits. So the useful range is only
 * somewhere around 10-200 million digits.<p>
 *
 * Some notes about the aparapi specific requirements for code that must be converted to OpenCL:
 * <ul>
 *   <li><code>assert()</code> does not work</li>
 *   <li>Can't check for null</li>
 *   <li>Can't get array length</li>
 *   <li>Arrays referenced by the kernel can't be null even if they are not accessed</li>
 *   <li>Arrays referenced by the kernel can't be zero-length even if they are not accessed</li>
 *   <li>Can't invoke methods in other classes e.g. enclosing class of an inner class</li>
 *   <li>Early return statements do not work</li>
 *   <li>Variables used inside loops must be initialized before the loop</li>
 *   <li>Must compile the class with full debug information i.e. with <code>-g</code></li>
 * </ul>
 *
 * @since 1.8.3
 * @version 1.15.0
 * @author Mikko Tommila
 */

class IntKernel
    extends Kernel
{
    private IntKernel()
    {
    }

    public static IntKernel getInstance()
    {
        return IntKernel.kernel.get();
    }

    private static ThreadLocal<IntKernel> kernel = ThreadLocal.withInitial(IntKernel::new);

    // Methods for calculating the column transforms in parallel
    public static final int TRANSFORM_ROWS_COLUMNORIENTATION = 1;
    public static final int INVERSE_TRANSFORM_ROWS_COLUMNORIENTATION = 2;
    // Methods for calculating the row transforms in parallel
    public static final int TRANSFORM_ROWS_ROWORIENTATION = 100001;
    public static final int INVERSE_TRANSFORM_ROWS_ROWORIENTATION = 100002;

    public void setLength(int length)
    {
        this.length = length;               // Transform length
    }

    public void setArrayAccess(ArrayAccess arrayAccess)
        throws ApfloatRuntimeException
    {
        this.data = arrayAccess.getIntData();
        this.offset = arrayAccess.getOffset();
        if (this.length != 0)
        {
            this.stride = arrayAccess.getLength() / this.length;
        }
    }

    public void setWTable(int[] wTable)
    {
        this.wTable = wTable;
    }

    public void setPermutationTable(int[] permutationTable)
    {
        this.permutationTable = (permutationTable == null ? new int[1] : permutationTable); // Zero-length array or null won't work
        this.permutationTableLength = (permutationTable == null ? 0 : permutationTable.length);
    }

    private void columnTableFNT()
    {
        int nn, istep = 0, mmax = 0, r = 0;

        int[] data = this.data;
        int offset = this.offset + getGlobalId();
        int stride = this.stride;
        nn     = this.length;

        if (nn >= 2)
        {
            r = 1;
            mmax = nn >> 1;
            while (mmax > 0)
            {
                istep = mmax << 1;

                // Optimize first step when wr = 1

                for (int i = offset; i < offset + nn * stride; i += istep * stride)
                {
                    int j = i + mmax * stride;
                    int a = data[i];
                    int b = data[j];
                    data[i] = modAdd(a, b);
                    data[j] = modSubtract(a, b);
                }

                int t = r;

                for (int m = 1; m < mmax; m++)
                {
                    for (int i = offset + m * stride; i < offset + nn * stride; i += istep * stride)
                    {
                        int j = i + mmax * stride;
                        int a = data[i];
                        int b = data[j];
                        data[i] = modAdd(a, b);
                        data[j] = modMultiply(this.wTable[t], modSubtract(a, b));
                    }
                    t += r;
                }
                r <<= 1;
                mmax >>= 1;
            }

            if (this.permutationTableLength > 0)
            {
                columnScramble(offset);
            }
        }
    }

    private void rowTableFNT()
    {
        int nn, offset, istep, mmax, r;

        int[] data = this.data;
        offset = this.offset + getGlobalId(1) * this.length;
        nn     = this.length;

        if (nn >= 2)
        {
            r = 1;
            mmax = nn >> 1;
            while (mmax > 0)
            {
                istep = mmax << 1;

                /*
                int t = 0;

                for (int m = 0; m < mmax; m++)
                {
                    for (int i = offset + m; i < offset + nn; i += istep)
                    {
                        int j = i + mmax;
                        int a = data[i];
                        int b = data[j];
                        data[i] = modAdd(a, b);
                        data[j] = modMultiply(this.wTable[t], modSubtract(a, b));
                    }
                    t += r;
                }
                */
                for (int k = getGlobalId(0); k < nn / 2; k += getGlobalSize(0))
                {
                    int m = k % mmax;
                    int t = m * r;
                    int i = offset + m + k / mmax * istep;
                    int j = i + mmax;
                    int a = data[i];
                    int b = data[j];
                    data[i] = modAdd(a, b);
                    data[j] = modMultiply(wTable[t], modSubtract(a, b));
                }
                r <<= 1;
                mmax >>= 1;
                localBarrier(); // Use a local barrier to synchronize memory writes within the same work-group but don't force memory to be written from the GPU cache to the GPU global memory
            }

            if (this.permutationTableLength > 0)
            {
                rowScramble(offset);
            }
        }
    }

    private void inverseColumnTableFNT()
    {
        int nn, istep = 0, mmax = 0, r = 0;

        int[] data = this.data;
        int offset = this.offset + getGlobalId();
        int stride = this.stride;
        nn     = this.length;

        if (nn >= 2)
        {
            if (this.permutationTableLength > 0)
            {
                columnScramble(offset);
            }

            r = nn;
            mmax = 1;
            while (nn > mmax)
            {
                istep = mmax << 1;
                r >>= 1;

                // Optimize first step when w = 1

                for (int i = offset; i < offset + nn * stride; i += istep * stride)
                {
                    int j = i + mmax * stride;
                    int wTemp = data[j];
                    data[j] = modSubtract(data[i], wTemp);
                    data[i] = modAdd(data[i], wTemp);
                }

                int t = r;

                for (int m = 1; m < mmax; m++)
                {
                    for (int i = offset + m * stride; i < offset + nn * stride; i += istep * stride)
                    {
                        int j = i + mmax * stride;
                        int wTemp = modMultiply(this.wTable[t], data[j]);
                        data[j] = modSubtract(data[i], wTemp);
                        data[i] = modAdd(data[i], wTemp);
                    }
                    t += r;
                }
                mmax = istep;
            }
        }
    }

    private void inverseRowTableFNT()
    {
        int nn, offset, istep, mmax, r;

        int[] data = this.data;
        offset = this.offset + getGlobalId(1) * this.length;
        nn     = this.length;

        if (nn >= 2)
        {
            if (permutationTableLength > 0)
            {
                rowScramble(offset);
            }

            r = nn;
            mmax = 1;
            while (nn > mmax)
            {
                istep = mmax << 1;
                r >>= 1;

                /*
                int t = 0;

                for (int m = 0; m < mmax; m++)
                {
                    for (int i = offset + m; i < offset + nn; i += istep)
                    {
                        int j = i + mmax;
                        int wTemp = modMultiply(this.wTable[t], data[j]);
                        data[j] = modSubtract(data[i], wTemp);
                        data[i] = modAdd(data[i], wTemp);
                    }
                    t += r;
                }
                */
                for (int k = getGlobalId(0); k < nn / 2; k += getGlobalSize(0))
                {
                    int m = k % mmax;
                    int t = m * r;
                    int i = offset + m + k / mmax * istep;
                    int j = i + mmax;
                    int wTemp = modMultiply(wTable[t], data[j]);
                    data[j] = modSubtract(data[i], wTemp);
                    data[i] = modAdd(data[i], wTemp);
                }
                mmax = istep;
                localBarrier(); // Use a local barrier to synchronize memory writes within the same work-group but don't force memory to be written from the GPU cache to the GPU global memory
            }
        }
    }

    private void columnScramble(int offset)
    {
        for (int k = 0; k < this.permutationTableLength; k += 2)
        {
            int i = offset + this.permutationTable[k] * this.stride,
                j = offset + this.permutationTable[k + 1] * this.stride;
            int tmp = this.data[i];
            this.data[i] = this.data[j];
            this.data[j] = tmp;
        }
    }

    private void rowScramble(int offset)
    {
        for (int k = 2 * getGlobalId(0); k < this.permutationTableLength; k += 2 * getGlobalSize(0))
        {
            int i = offset + this.permutationTable[k],
                j = offset + this.permutationTable[k + 1];
            int tmp = this.data[i];
            this.data[i] = this.data[j];
            this.data[j] = tmp;
        }
        localBarrier();
    }

    private int modMultiply(int a, int b)
    {
        long t = (long) a * (long) b;
        //int r1 = a * b - (int) (this.inverseModulus * (double) a * (double) b) * this.modulus,
        int r1 = (int) t - (int) ((t >>> 30) * this.inverseModulus >>> 33) * this.modulus,
            r2 = r1 - this.modulus;

        return (r2 < 0 ? r1 : r2);
    }

    private int modAdd(int a, int b)
    {
        int r1 = a + b,
            r2 = r1 - this.modulus;

        return (r2 < 0 ? r1 : r2);
    }

    private int modSubtract(int a, int b)
    {
        int r1 = a - b,
            r2 = r1 + this.modulus;

        return (r1 < 0 ? r2 : r1);
    }

    public void setModulus(int modulus)
    {
        //this.inverseModulus = 1.0 / (modulus + 0.5);    // Round down
        this.inverseModulus = (long) (9223372036854775808.0 / (double) modulus);
        this.modulus = modulus;
    }

    public int getModulus()
    {
        return this.modulus;
    }

    private int stride;
    private int length;
    private int[] data;
    private int offset;
    private int[] wTable = { 0 };
    private int[] permutationTable = { 0 };
    private int permutationTableLength;

    private int modulus;
    //private double inverseModulus;
    private long inverseModulus;

    // Methods for transposing the matrix
    public static final int TRANSPOSE = 3;
    public static final int PERMUTE = 4;

    public void setN2(int n2)
    {
        this.n2 = n2;
    }

    public void setIndex(int[] index)
    {
        this.index = index;
    }

    public void setIndexCount(int indexCount)
    {
        this.indexCount = indexCount;
    }

    private void transpose()
    {
        int i = getGlobalId(0),
            j = getGlobalId(1);

        if (i < j)
        {
            int position1 = this.offset + j * this.n2 + i,
                position2 = this.offset + i * this.n2 + j;
            int tmp = this.data[position1];
            this.data[position1] = this.data[position2];
            this.data[position2] = tmp;
        }
    }

    private void permute()
    {
        int j = getGlobalId();

        for (int i = 0; i < this.indexCount; i++)
        {
            int o = this.index[i];
            int tmp = this.data[this.offset + this.n2 * o + j];
            for (i++; this.index[i] != 0; i++)
            {
                int m = this.index[i];
                this.data[this.offset + this.n2 * o + j] = this.data[this.offset + this.n2 * m + j];
                o = m;
            }
            this.data[this.offset + this.n2 * o + j] = tmp;
        }
    }

    private int n2;
    private int[] index = { 0 };
    private int indexCount;

    // Methods for multiplying elements in the matrix
    public static final int MULTIPLY_ELEMENTS = 5;

    public void setStartRow(int startRow)
    {
        this.startRow = startRow;
    }

    public void setStartColumn(int startColumn)
    {
        this.startColumn = startColumn;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public void setColumns(int columns)
    {
        this.columns = columns;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public void setScaleFactor(int scaleFactor)
    {
        this.scaleFactor = scaleFactor;
    }

    private void multiplyElements()
    {
        int[] data = this.data;
        int position = this.offset + getGlobalId();
        int rowFactor = modPow(this.w, (int) this.startRow);
        int columnFactor = modPow(this.w, (int) this.startColumn + getGlobalId());
        int rowStartFactor = modMultiply(this.scaleFactor, modPow(rowFactor, (int) this.startColumn + getGlobalId()));

        for (int i = 0; i < this.rows; i++)
        {
            data[position] = modMultiply(data[position], rowStartFactor);
            position += this.columns;

            rowStartFactor = modMultiply(rowStartFactor, columnFactor);
        }
    }

    private int modPow(int a, int n)
    {
        if (n == 0)
        {
            return 1;
        }
        else if (n < 0)
        {
            n = getModulus() - 1 + n;
        }

        int exponent = (int) n;

        while ((exponent & 1) == 0)
        {
            a = modMultiply(a, a);
            exponent >>= 1;
        }

        int r = a;

        for (exponent >>= 1; exponent > 0; exponent >>= 1)
        {
            a = modMultiply(a, a);
            if ((exponent & 1) != 0)
            {
                r = modMultiply(r, a);
            }
        }

        return r;
    }

    private int startRow;
    private int startColumn;
    private int rows;
    private int columns;
    private int w;
    private int scaleFactor;

    // Methods for factor-3 transform
    public static final int TRANSFORM_COLUMNS = 6;
    public static final int INVERSE_TRANSFORM_COLUMNS = 7;

    public void setOp(int op)
    {
        this.op = op;
    }

    public void setWw(int ww)
    {
        this.ww = ww;
    }

    public void setW1(int w1)
    {
        this.w1 = w1;
    }

    public void setW2(int w2)
    {
        this.w2 = w2;
    }

    @Override
    public void run()
    {
        if (this.op == TRANSFORM_ROWS_COLUMNORIENTATION)
        {
            columnTableFNT();
        }
        else if (this.op == INVERSE_TRANSFORM_ROWS_COLUMNORIENTATION)
        {
            inverseColumnTableFNT();
        }
        else if (this.op == TRANSFORM_ROWS_ROWORIENTATION)
        {
            rowTableFNT();
        }
        else if (this.op == INVERSE_TRANSFORM_ROWS_ROWORIENTATION)
        {
            inverseRowTableFNT();
        }
        else if (this.op == TRANSPOSE)
        {
            transpose();
        }
        else if (this.op == PERMUTE)
        {
            permute();
        }
        else if (this.op == MULTIPLY_ELEMENTS)
        {
            multiplyElements();
        }
        else if (this.op == TRANSFORM_COLUMNS || this.op == INVERSE_TRANSFORM_COLUMNS)
        {
            transformColumns();
        }
    }

    private void transformColumns()
    {
        int i = getGlobalId();

        int tmp1 = modPow(this.w, (int) this.startColumn + i),
            tmp2 = modPow(this.ww, (int) this.startColumn + i);

        // 3-point WFTA on the corresponding array elements

        int x0 = this.data[this.offset + i],
            x1 = this.data[this.offset + this.columns + i],
            x2 = this.data[this.offset + 2 * this.columns + i],
            t;

        if (this.op == INVERSE_TRANSFORM_COLUMNS)
        {
            // Multiply before transform
            x1 = modMultiply(x1, tmp1);
            x2 = modMultiply(x2, tmp2);
        }

        // Transform column
        t = modAdd(x1, x2);
        x2 = modSubtract(x1, x2);
        x0 = modAdd(x0, t);
        t = modMultiply(t, this.w1);
        x2 = modMultiply(x2, this.w2);
        t = modAdd(t, x0);
        x1 = modAdd(t, x2);
        x2 = modSubtract(t, x2);

        if (this.op == TRANSFORM_COLUMNS)
        {
            // Multiply after transform
            x1 = modMultiply(x1, tmp1);
            x2 = modMultiply(x2, tmp2);
        }

        this.data[this.offset + i] = x0;
        this.data[this.offset + this.columns + i] = x1;
        this.data[this.offset + 2 * this.columns + i] = x2;
    }

    private int op;
    private int ww;
    private int w1;
    private int w2;
}
