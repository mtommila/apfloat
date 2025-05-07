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
package org.apfloat;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Writer;
import java.io.IOException;

import org.apfloat.spi.ApfloatBuilder;
import org.apfloat.spi.ApfloatImpl;
import org.apfloat.spi.Util;
import static org.apfloat.spi.RadixConstants.*;

/**
 * Various utility methods related to apfloats.
 *
 * @version 1.15.0
 * @author Mikko Tommila
 */

class ApfloatHelper
{
    private ApfloatHelper()
    {
    }

    public static ApfloatImpl createApfloat(String value, boolean isInteger)
        throws NumberFormatException, ApfloatRuntimeException
    {
        long precision = (isInteger ? Apfloat.INFINITE : Apfloat.DEFAULT);
        int radix = getDefaultRadix();
        return implCreateApfloat(value, precision, radix, isInteger);
    }

    public static ApfloatImpl createApfloat(String value, long precision, boolean isInteger)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return createApfloat(value, precision, radix, isInteger);
    }

    public static ApfloatImpl createApfloat(String value, long precision, int radix, boolean isInteger)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        if (precision != Apfloat.DEFAULT)
        {
            checkPrecision(precision);
        }
        return implCreateApfloat(value, precision, radix, isInteger);
    }

    private static ApfloatImpl implCreateApfloat(String value, long precision, int radix, boolean isInteger)
        throws NumberFormatException, ApfloatRuntimeException
    {
        ApfloatBuilder factory = getApfloatBuilder();
        return factory.createApfloat(value, precision, radix, isInteger);
    }

    public static ApfloatImpl createApfloat(long value)
        throws NumberFormatException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return implCreateApfloat(value, Apfloat.INFINITE, radix);
    }

    public static ApfloatImpl createApfloat(long value, long precision)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return createApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(long value, long precision, int radix)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        precision = (precision == Apfloat.DEFAULT ? Apfloat.INFINITE : precision);
        checkPrecision(precision);
        return implCreateApfloat(value, precision, radix);
    }

    private static ApfloatImpl implCreateApfloat(long value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException
    {
        ApfloatBuilder factory = getApfloatBuilder();
        return factory.createApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(float value)
        throws NumberFormatException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        int precision = getFloatPrecision(radix);
        return implCreateApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(float value, long precision)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return createApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(float value, long precision, int radix)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        precision = (precision == Apfloat.DEFAULT ? getFloatPrecision(radix) : precision);
        checkPrecision(precision);
        return implCreateApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(double value)
        throws NumberFormatException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        int precision = getDoublePrecision(radix);
        return implCreateApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(double value, long precision)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return createApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(double value, long precision, int radix)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        precision = (precision == Apfloat.DEFAULT ? getDoublePrecision(radix) : precision);
        checkPrecision(precision);
        return implCreateApfloat(value, precision, radix);
    }

    private static ApfloatImpl implCreateApfloat(double value, long precision, int radix)
        throws NumberFormatException, ApfloatRuntimeException
    {
        ApfloatBuilder factory = getApfloatBuilder();
        return factory.createApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(PushbackReader in, boolean isInteger)
        throws IOException, NumberFormatException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return implCreateApfloat(in, Apfloat.DEFAULT, radix, isInteger);
    }

    public static ApfloatImpl createApfloat(PushbackReader in, long precision, boolean isInteger)
        throws IOException, NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return createApfloat(in, precision, radix, isInteger);
    }

    public static ApfloatImpl createApfloat(PushbackReader in, long precision, int radix, boolean isInteger)
        throws IOException, NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        if (precision != Apfloat.DEFAULT)
        {
            checkPrecision(precision);
        }
        return implCreateApfloat(in, precision, radix, isInteger);
    }

    private static ApfloatImpl implCreateApfloat(PushbackReader in, long precision, int radix, boolean isInteger)
        throws IOException, NumberFormatException, ApfloatRuntimeException
    {
        ApfloatBuilder factory = getApfloatBuilder();
        return factory.createApfloat(in, precision, radix, isInteger);
    }

    public static ApfloatImpl createApfloat(BigInteger value)
        throws NumberFormatException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return createApfloat(value, Apfloat.INFINITE, radix);
    }

    public static ApfloatImpl createApfloat(BigInteger value, long precision)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        int radix = getDefaultRadix();
        return createApfloat(value, precision, radix);
    }

    public static ApfloatImpl createApfloat(BigInteger value, long precision, int radix)
        throws NumberFormatException, IllegalArgumentException, ApfloatRuntimeException
    {
        if (precision != Apfloat.DEFAULT)
        {
            checkPrecision(precision);
        }
        checkRadix(radix);
        Apfloat a;
        try
        {
            a = new Apfloat(createApfloat(toPushbackReader(value), Apfloat.INFINITE, 16, true));
        }
        catch (IOException ioe)
        {
            throw new ApfloatRuntimeException("Should not occur", ioe, "shouldNotOccur");
        }
        precision = (precision == Apfloat.DEFAULT ? Apfloat.INFINITE : precision);
        return a.toRadix(radix).getImpl(precision);
    }

    public static ApfloatImpl createApfloat(BigDecimal value)
        throws ApfloatRuntimeException
    {
        return implCreateApfloat(value.toString(), Apfloat.DEFAULT, 10, false);
    }

    public static ApfloatImpl createApfloat(BigDecimal value, long precision)
        throws IllegalArgumentException, ApfloatRuntimeException
    {
        return createApfloat(value.toString(), precision, 10, false);
    }

    /**
     * Extracts matching character from stream.
     * A non-matching character is pushed back to the stream.
     *
     * @param in The input.
     * @param c The character to expect from the stream.
     *
     * @return <code>true</code> if the specified character was extracted from the stream, <code>false</code> otherwise.
     *
     * @exception IOException In case of read error in the stream.
     */

    public static boolean readMatch(PushbackReader in, int c)
        throws IOException
    {
        int i = in.read();

        if (i != c)
        {
            if (i != -1)
            {
                in.unread(i);
            }
            return false;
        }

        return true;
    }

    /**
     * Extracts whitespace from stream.
     *
     * @param in The input.
     *
     * @exception IOException In case of read error in the stream.
     */

    public static void extractWhitespace(PushbackReader in)
        throws IOException
    {
        int c;

        while (Character.isWhitespace((char) (c = in.read())))
        {
            // Extracts any whitespace
        }
        if (c != -1)
        {
            in.unread(c);
        }
    }

    /**
     * Get working precisions for the arguments of e.g. an add, subtract or compare operation.<p>
     *
     * Note that the returned precision can be zero to indicate that the number is insignificant
     * in the calculation. This is the case if either operand is zero, or if one number lies
     * completely outside the significant range of the other number. Consider e.g. the case<p>
     *
     * x.scale() = 100<br>
     * x.precision() = 50<br>
     * y.scale() = 10<br>
     * y.precision() = 5<p>
     *
     * In e.g. the sum of x and y, the operand y would now be insignificant.
     *
     * @param x First argument.
     * @param y Second argument.
     *
     * @return Array of two longs containing the working precisions for <code>x</code> and <code>y</code>, correspondingly.
     */

    public static long[] getMatchingPrecisions(Apfloat x, Apfloat y)
        throws ApfloatRuntimeException
    {
        if (x.signum() == 0 || y.signum() == 0)
        {
            return new long[] { 0, 0 };
        }

        long xPrec  = x.precision(),
             yPrec  = y.precision(),
             xScale = x.scale(),
             yScale = y.scale(),
             maxScale = Math.max(xScale, yScale),
             xScaleDiff = (maxScale - xScale < 0 ? Apfloat.INFINITE : maxScale - xScale),
             yScaleDiff = (maxScale - yScale < 0 ? Apfloat.INFINITE : maxScale - yScale),
             maxPrec = Math.min(Util.ifFinite(xPrec, xPrec + xScaleDiff),
                                Util.ifFinite(yPrec, yPrec + yScaleDiff)),
             destXPrec = (maxPrec - xScaleDiff <= 0 ? 0 : Util.ifFinite(maxPrec, maxPrec - xScaleDiff)),
             destYPrec = (maxPrec - yScaleDiff <= 0 ? 0 : Util.ifFinite(maxPrec, maxPrec - yScaleDiff));

        return new long[] { destXPrec, destYPrec };
    }

    /**
     * Get working precisions for the arguments of an multiply-add operation
     * a * b + c * d. Works as well for a multiply-subtract operation, of course.<p>
     *
     * The returned array contains three longs:<p>
     *
     * [0] Working precisions for <code>a</code> and <code>b</code><br>
     * [1] Working precisions for <code>c</code> and <code>d</code><br>
     * [2] Maximum precision of the final result <code>a * b + c * d</code><p>
     *
     * Note that the precisions can be zero. See {@link #getMatchingPrecisions(Apfloat,Apfloat)}
     * for details.
     *
     * @param a First argument.
     * @param b Second argument.
     * @param c Third argument.
     * @param d Fourth argument.
     *
     * @return Array of three longs containing the precisions.
     */

    public static long[] getMatchingPrecisions(Apfloat a, Apfloat b, Apfloat c, Apfloat d)
        throws ApfloatRuntimeException
    {
        long abPrec  = (a.signum() == 0 || b.signum() == 0 ? 0 : Math.min(a.precision(), b.precision())),
             cdPrec  = (c.signum() == 0 || d.signum() == 0 ? 0 : Math.min(c.precision(), d.precision()));

        if (abPrec == 0 || cdPrec == 0)
        {
            return new long[] { abPrec, cdPrec, Math.max(abPrec, cdPrec) };
        }

        long abScale = a.scale() + b.scale(),
             cdScale = c.scale() + d.scale(),
             maxScale = Math.max(abScale, cdScale),
             abScaleDiff = (maxScale - abScale < 0 ? Apfloat.INFINITE : maxScale - abScale),
             cdScaleDiff = (maxScale - cdScale < 0 ? Apfloat.INFINITE : maxScale - cdScale),
             maxPrec = Math.min(Util.ifFinite(abPrec, abPrec + abScaleDiff),
                                Util.ifFinite(cdPrec, cdPrec + cdScaleDiff)),
             destAbPrec = (maxPrec - abScaleDiff <= 0 ? 0 : Util.ifFinite(maxPrec, maxPrec - abScaleDiff + 1)), // Add 1 since the scale may be 1 less
             destCdPrec = (maxPrec - cdScaleDiff <= 0 ? 0 : Util.ifFinite(maxPrec, maxPrec - cdScaleDiff + 1));

        return new long[] { destAbPrec, destCdPrec, maxPrec };
    }

    public static void checkPrecision(long precision)
        throws IllegalArgumentException
    {
        if (precision <= 0)
        {
            throw new IllegalArgumentException("Precision " + precision + " is not positive");
        }
    }

    public static void checkRadix(int radix)
        throws NumberFormatException
    {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
        {
            throw new NumberFormatException("Invalid radix " + radix + "; radix must be between " + Character.MIN_RADIX + " and " + Character.MAX_RADIX);
        }
    }

    private static void checkPowPrecision(long targetPrecision)
        throws InfiniteExpansionException
    {
        if (targetPrecision == Apfloat.INFINITE)
        {
            throw new InfiniteExpansionException("Cannot calculate power to infinite precision", "pow.infinitePrecision");
        }
    }

    private static Apcomplex checkPowBasic(Apcomplex z, Apcomplex w, long targetPrecision)
        throws ArithmeticException, ApfloatRuntimeException
    {
        if (w.isZero())
        {
            if (z.isZero())
            {
                throw new ApfloatArithmeticException("Zero to power zero", "pow.zeroToZero");
            }

            return new Apcomplex(new Apfloat(1, Apfloat.INFINITE, z.radix()));
        }
        else if (z.isZero())
        {
            if (w.real().signum() <= 0)
            {
                throw new ApfloatArithmeticException("Zero to power of " + (w.imag().signum() == 0 ? "negative number" : "number with nonpositive real part"), w.imag().signum() == 0 ? "pow.zeroToNegative" : "pow.zeroToNonpositiveReal");
            }

            return z;
        }
        else if (z.equals(Apcomplex.ONE) || w.equals(Apcomplex.ONE))
        {
            return z.precision(targetPrecision);
        }

        return null;
    }

    public static Apcomplex checkPow(Apcomplex z, Apcomplex w, long targetPrecision)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex result = checkPowBasic(z, w, targetPrecision);
        if (result != null)
        {
            return result;
        }

        checkPowPrecision(targetPrecision);

        return null;
    }

    public static Apfloat checkPow(Apfloat x, Apfloat y, long targetPrecision)
        throws ArithmeticException, ApfloatRuntimeException
    {
        Apcomplex result = checkPowBasic(x, y, targetPrecision);
        if (result != null)
        {
            return result.real();
        }
        else if (x.signum() < 0 && !y.isInteger())
        {
            throw new ApfloatArithmeticException("Power of negative number to non-integer; result would be complex", "pow.negativeToNonInteger");
        }

        checkPowPrecision(targetPrecision);

        return null;
    }

    public static int getFloatPrecision(int radix)
    {
        assert (radix > 0);
        return FLOAT_PRECISION[radix];
    }

    public static int getDoublePrecision(int radix)
    {
        assert (radix > 0);
        return DOUBLE_PRECISION[radix];
    }

    public static int getLongPrecision(int radix)
    {
        assert (radix > 0);
        return LONG_PRECISION[radix];
    }

    // To handle accumulating round-off errors better in slow functions
    public static long getSmallExtraPrecision(int radix)
    {
        assert (radix > 0);
        return Math.min(3, (long) Math.ceil(5 / Math.log(radix)));
    }

    // Returns x with precision at most as specified
    public static Apfloat limitPrecision(Apfloat x, long precision)
        throws ApfloatRuntimeException
    {
        return x.precision(Math.min(x.precision(), precision));
    }

    // Returns x with precision at least as specified
    public static Apfloat ensurePrecision(Apfloat x, long precision)
        throws ApfloatRuntimeException
    {
        return x.precision(Math.max(x.precision(), precision));
    }

    // Returns given precision extended by specified amount
    public static long extendPrecision(long precision, long extraPrecision)
    {
        return Util.ifFinite(precision, precision + extraPrecision);
    }

    // Returns given precision extended by Apfloat.EXTRA_PRECISION
    public static long extendPrecision(long precision)
    {
        return extendPrecision(precision, Apfloat.EXTRA_PRECISION);
    }

    // Returns given precision reduced by specified amount
    public static long reducePrecision(long precision, long extraPrecision)
        throws ApfloatRuntimeException
    {
        if (precision == Apfloat.INFINITE)
        {
            return precision;
        }
        precision = precision - extraPrecision;
        if (precision <= 0)
        {
            throw new LossOfPrecisionException("Complete loss of accurate digits", "lossOfPrecision");
        }
        return precision;
    }

    // Returns given precision reduced by Apfloat.EXTRA_PRECISION
    public static long reducePrecision(long precision)
        throws ApfloatRuntimeException
    {
        return reducePrecision(precision, Apfloat.EXTRA_PRECISION);
    }

    // Returns x with precision extended by Apfloat.EXTRA_PRECISION
    public static Apfloat extendPrecision(Apfloat x)
        throws ApfloatRuntimeException
    {
        return x.precision(extendPrecision(x.precision()));
    }

    // Returns x with precision extended by specified amount
    public static Apfloat extendPrecision(Apfloat x, long extraPrecision)
        throws ApfloatRuntimeException
    {
        return x.precision(extendPrecision(x.precision(), extraPrecision));
    }

    // Returns x with precision reduced by Apfloat.EXTRA_PRECISION
    public static Apfloat reducePrecision(Apfloat x)
        throws ApfloatRuntimeException
    {
        return x.precision(reducePrecision(x.precision()));
    }

    // Returns x with precision reduced by specified amount
    public static Apfloat reducePrecision(Apfloat x, long extraPrecision)
        throws ApfloatRuntimeException
    {
        return x.precision(reducePrecision(x.precision(), extraPrecision));
    }

    // Returns z with precision as specified
    public static Apcomplex setPrecision(Apcomplex z, long precision)
        throws ApfloatRuntimeException
    {
        if (z.real().signum() == 0)
        {
            return new Apcomplex(z.real(),
                                 z.imag().precision(precision));
        }
        else if (z.imag().signum() == 0)
        {
            return new Apcomplex(z.real().precision(precision),
                                 z.imag());
        }

        long precisionChange = precision - z.precision(),
             realPrecision = z.real().precision(),
             imagPrecision = z.imag().precision(),
             newRealPrecision = Util.ifFinite(realPrecision, realPrecision + precisionChange),
             newImagPrecision = Util.ifFinite(imagPrecision, imagPrecision + precisionChange);

        if (precisionChange < 0)
        {
            if (realPrecision + precisionChange <= 0)
            {
                return new Apcomplex(Apfloat.ZEROS[z.radix()],
                                     z.imag().precision(precision));
            }
            else if (imagPrecision + precisionChange <= 0)
            {
                return new Apcomplex(z.real().precision(precision),
                                     Apfloat.ZEROS[z.radix()]);
            }
        }

        return new Apcomplex(z.real().precision(newRealPrecision),
                             z.imag().precision(newImagPrecision));
    }

    // Returns z with precision at most as specified
    public static Apcomplex limitPrecision(Apcomplex z, long precision)
        throws ApfloatRuntimeException
    {
        return new Apcomplex(limitPrecision(z.real(), precision),
                             limitPrecision(z.imag(), precision));
    }

    // Returns z with precision at least as specified
    public static Apcomplex ensurePrecision(Apcomplex z, long precision)
        throws ApfloatRuntimeException
    {
        return new Apcomplex(ensurePrecision(z.real(), precision),
                             ensurePrecision(z.imag(), precision));
    }

    // Returns z with precision extended by Apfloat.EXTRA_PRECISION
    public static Apcomplex extendPrecision(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return new Apcomplex(extendPrecision(z.real()),
                             extendPrecision(z.imag()));
    }

    // Returns z with precision extended by specified precision
    public static Apcomplex extendPrecision(Apcomplex z, long extraPrecision)
        throws ApfloatRuntimeException
    {
        return new Apcomplex(extendPrecision(z.real(), extraPrecision),
                             extendPrecision(z.imag(), extraPrecision));
    }

    // Returns z with precision reduced by Apfloat.EXTRA_PRECISION
    public static Apcomplex reducePrecision(Apcomplex z)
        throws ApfloatRuntimeException
    {
        return reducePrecision(z, Apfloat.EXTRA_PRECISION);
    }

    // Returns z with precision reduced by specified amount
    public static Apcomplex reducePrecision(Apcomplex z, long extraPrecision)
        throws ApfloatRuntimeException
    {
        long scale = z.scale();
        return new Apcomplex(z.real().scale() < scale ? reducePrecisionOrZero(z.real(), extraPrecision) : reducePrecision(z.real(), extraPrecision),
                             z.imag().scale() < scale ? reducePrecisionOrZero(z.imag(), extraPrecision) : reducePrecision(z.imag(), extraPrecision));
    }

    private static Apfloat reducePrecisionOrZero(Apfloat x, long extraPrecision)
    {
        long precision = x.precision();
        if (precision == Apfloat.INFINITE)
        {
            return x;
        }
        precision = precision - extraPrecision;
        if (precision <= 0)
        {
            return Apfloat.ZEROS[x.radix()];
        }
        return x.precision(precision);
    }

    // Returns z so that gamma(z) should have the given precision 
    public static Apcomplex ensureGammaPrecision(Apcomplex z, long precision)
    {
        Apint zRounded = RoundingHelper.roundToInteger(z.real(), RoundingMode.HALF_EVEN).truncate();
        if (zRounded.signum() < 0)
        {
            long digitLoss = Math.min(precision, -z.subtract(zRounded).scale());
            if (digitLoss > 0)
            {
                precision = Util.ifFinite(precision, precision + digitLoss);
            }
        }
        return ensurePrecision(z, precision);
    }

    public static long size(Aprational x)
        throws ApfloatRuntimeException
    {
        return Math.max(x.numerator().size(), x.denominator().size());
    }

    public static BigInteger toBigInteger(Apint x)
    {
        assert (x.signum() != 0);

        // The naive approach to convert to String and then to BigInteger is highly
        // inefficient as the BigInteger String constructor has O(n^2) complexity.
        // Therefore we first convert to radix-16 and then to a byte array.
        Apint a = ApintMath.abs(x.toRadix(16));
        long scale = a.scale();
        long byteCount = (scale + 1) >> 1;

        if (byteCount > Integer.MAX_VALUE)
        {
            throw new IllegalArgumentException("Maximum array size exceeded");
        }

        byte[] bytes = new byte[(int) byteCount];
        boolean startHi = ((scale & 1) == 0);

        try
        {
            a.writeTo(new Writer()
            {
                @Override
                public void write(int c)
                {
                    c = Character.digit(c, 16);
                    if (this.hi)
                    {
                        this.b = (c << 4);
                    }
                    else
                    {
                        this.b += (c & 0x0F);
                        bytes[this.bytePosition] = (byte) this.b;
                        this.bytePosition++;
                    }
                    this.hi = !this.hi;
                }

                @Override
                public void write(char cbuf[], int off, int len)
                {
                    for (int i = 0; i < len; i++)
                    {
                        write(cbuf[off + i]);
                    }
                }

                @Override
                public void close()
                {
                }

                @Override
                public void flush()
                {
                }

                private int b;
                private int bytePosition;
                private boolean hi = startHi;
            });
        }
        catch (IOException ioe)
        {
            throw new ApfloatRuntimeException("Should not occur", ioe, "shouldNotOccur");
        }

        BigInteger b = new BigInteger(x.signum(), bytes);
        return b;
    }

    // Get a reader for the radix-16 presentation of the BigInteger.
    // The BigInteger.toString() method has O(n^2) complexity,
    // therefore we convert to a byte array instead.
    public static PushbackReader toPushbackReader(BigInteger x)
        throws IOException
    {
        byte[] bytes = x.abs().toByteArray();
        int startB = (x.signum() < 0 ? '-' : -1);     // Start the stream with minus sign in case of negative number
        InputStream in = new ByteArrayInputStream(bytes)
        {
            @Override
            public int read()
            {
                int c;
                if (this.b == -1)
                {
                    this.b = super.read();
                    if (this.b == -1)
                    {
                        c = -1;
                    }
                    else
                    {
                        c = Character.forDigit(this.b >> 4, 16);
                        this.b = Character.forDigit(this.b & 0x0F, 16);
                    }
                }
                else
                {
                    c = this.b;
                    this.b = -1;
                }
                return c;
            }

            @Override
            public int read(byte[] b, int off, int len)
            {
                int i = 0;
                for (; i < len; i++)
                {
                    int c = read();
                    if (c == -1)
                    {
                        i = (i == 0 ? -1 : i);  // In case of EOF; there was nothing to read in the stream
                        break;
                    }
                    b[i + off] = (byte) c;
                }
                return i;
            }

            private int b = startB;
        };

        return new PushbackReader(new InputStreamReader(in, "ISO-8859-1"));
    }

    // Converts ArithmeticException to OverflowException if the Apint overflows a long
    public static long longValueExact(Apint x)
        throws OverflowException
    {
        try
        {
            return x.longValueExact();
        }
        catch (ArithmeticException ae)
        {
            throw new OverflowException("Overflow", ae, "overflow");
        }
    }

    private static int getDefaultRadix()
        throws NumberFormatException
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        return ctx.getDefaultRadix();
    }

    private static ApfloatBuilder getApfloatBuilder()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        return ctx.getBuilderFactory().getApfloatBuilder();
    }
}
