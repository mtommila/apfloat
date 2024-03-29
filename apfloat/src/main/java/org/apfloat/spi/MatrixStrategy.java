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
package org.apfloat.spi;

import org.apfloat.ApfloatRuntimeException;

/**
 * Matrix operations.
 *
 * @since 1.7.0
 * @version 1.7.0
 * @author Mikko Tommila
 */

public interface MatrixStrategy
{
    /**
     * Transpose a n<sub>1</sub> x n<sub>2</sub> matrix.<p>
     *
     * Both n<sub>1</sub> and n<sub>2</sub> must be powers of two.
     * Additionally, one of these must be true:<p>
     *
     * n<sub>1</sub> = n<sub>2</sub><br>
     * n<sub>1</sub> = 2*n<sub>2</sub><br>
     * n<sub>2</sub> = 2*n<sub>1</sub><br>
     *
     * @param arrayAccess Accessor to the matrix data. This data will be transposed.
     * @param n1 Number of rows in the matrix.
     * @param n2 Number of columns in the matrix.
     */

    public void transpose(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException;

    /**
     * Transpose a square n<sub>1</sub> x n<sub>1</sub> block of n<sub>1</sub> x n<sub>2</sub> matrix.<p>
     *
     * Both n<sub>1</sub> and n<sub>2</sub> must be powers of two,
     * and n<sub>1</sub> &lt;= n<sub>2</sub>.
     *
     * @param arrayAccess Accessor to the matrix data. This data will be transposed.
     * @param n1 Number of rows and columns in the block to be transposed.
     * @param n2 Number of columns in the matrix.
     */

    public void transposeSquare(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException;

    /**
     * Permute the rows of the n<sub>1</sub> x n<sub>2</sub> matrix so that it is shaped like a
     * n<sub>1</sub>/2 x 2*n<sub>2</sub> matrix. Logically, the matrix is split in half, and the
     * lower half is moved to the right side of the upper half.<p>
     *
     * Both n<sub>1</sub> and n<sub>2</sub> must be powers of two,
     * and n<sub>1</sub> &gt;= 2.<p>
     *
     * E.g. if the matrix layout is originally as follows:
     * <table style="width:100px; border-collapse:collapse; border:1px solid black" border="1">
     *   <caption>Matrix before</caption>
     *   <tr>
     *     <td>0</td><td>1</td><td>2</td><td>3</td>
     *   </tr>
     *   <tr>
     *     <td>4</td><td>5</td><td>6</td><td>7</td>
     *   </tr>
     *   <tr style="background:lightgray">
     *     <td>8</td><td>9</td><td>10</td><td>11</td>
     *   </tr>
     *   <tr style="background:lightgray">
     *     <td>12</td><td>13</td><td>14</td><td>15</td>
     *   </tr>
     * </table>
     * <p>
     *
     * Then after this method it is as follows:
     * <table style="width:200px; border-collapse:collapse; border:1px solid black" border="1">
     *   <caption>Matrix after</caption>
     *   <tr>
     *     <td>0</td><td>1</td><td>2</td><td>3</td><td style="background:lightgray">8</td><td style="background:lightgray">9</td><td style="background:lightgray">10</td><td style="background:lightgray">11</td>
     *   </tr>
     *   <tr>
     *     <td>4</td><td>5</td><td>6</td><td>7</td><td style="background:lightgray">12</td><td style="background:lightgray">13</td><td style="background:lightgray">14</td><td style="background:lightgray">15</td>
     *   </tr>
     * </table>
     *
     * @param arrayAccess Accessor to the matrix data. This data will be permuted.
     * @param n1 Number of rows in the matrix.
     * @param n2 Number of columns in the matrix.
     *
     * @since 1.7.0
     */

    public void permuteToDoubleWidth(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException;

    /**
     * Permute the rows of the n<sub>1</sub> x n<sub>2</sub> matrix so that it is shaped like a
     * 2*n<sub>1</sub> x n<sub>2</sub>/2 matrix. Logically, the matrix is split in half, and the
     * right half is moved below the left half.<p>
     *
     * Both n<sub>1</sub> and n<sub>2</sub> must be powers of two.
     *
     * E.g. if the matrix layout is originally as follows:
     * <table style="width:200px; border-collapse:collapse; border:1px solid black" border="1">
     *   <caption>Matrix before</caption>
     *   <tr>
     *     <td>0</td><td>1</td><td>2</td><td>3</td><td style="background:lightgray">4</td><td style="background:lightgray">5</td><td style="background:lightgray">6</td><td style="background:lightgray">7</td>
     *   </tr>
     *   <tr>
     *     <td>8</td><td>9</td><td>10</td><td>11</td><td style="background:lightgray">12</td><td style="background:lightgray">13</td><td style="background:lightgray">14</td><td style="background:lightgray">15</td>
     *   </tr>
     * </table>
     * <p>
     *
     * Then after this method it is as follows:
     * <table style="width:100px; border-collapse:collapse; border:1px solid black" border="1">
     *   <caption>Matrix after</caption>
     *   <tr>
     *     <td>0</td><td>1</td><td>2</td><td>3</td>
     *   </tr>
     *   <tr>
     *     <td>8</td><td>9</td><td>10</td><td>11</td>
     *   </tr>
     *   <tr style="background:lightgray">
     *     <td>4</td><td>5</td><td>6</td><td>7</td>
     *   </tr>
     *   <tr style="background:lightgray">
     *     <td>12</td><td>13</td><td>14</td><td>15</td>
     *   </tr>
     * </table>
     *
     * @param arrayAccess Accessor to the matrix data. This data will be permuted.
     * @param n1 Number of rows in the matrix.
     * @param n2 Number of columns in the matrix.
     */

    public void permuteToHalfWidth(ArrayAccess arrayAccess, int n1, int n2)
        throws ApfloatRuntimeException;
}
