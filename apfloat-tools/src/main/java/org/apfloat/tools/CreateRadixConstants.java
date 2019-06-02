package org.apfloat.tools;

/**
 * This utility class can be used to generate the file
 * <code>RadixConstants.java</code>.
 *
 * @version 1.1
 * @author Mikko Tommila
 */

public class CreateRadixConstants
{
    public static void main(String[] args)
    {
        StringBuilder radixFactors = new StringBuilder("{ ");
        for (int i = 0 ; i <= Character.MAX_RADIX; i++)
        {
            StringBuilder buffer = new StringBuilder("{ ");
            int factors = 0;
            for (int j = 2; j <= i; j++)
            {
                if (i % j == 0 && isPrime(j))
                {
                    buffer.append(factors > 0 ? ", " : "").append(j);
                    factors++;
                }
            }
            buffer.append(" }");
            radixFactors.append(i > 0 ? ", " : "").append(i >= Character.MIN_RADIX ? buffer : null);
        }
        radixFactors.append(" };");

        StringBuilder floatPrecision = new StringBuilder("{ ");
        for (int i = 0 ; i <= Character.MAX_RADIX; i++)
        {
            int precision = (int) Math.ceil(Math.log(16777216.0) / Math.log((double) i));
            floatPrecision.append(i > 0 ? ", " : "").append(i >= Character.MIN_RADIX ? precision : -1);
        }
        floatPrecision.append(" };");

        StringBuilder doublePrecision = new StringBuilder("{ ");
        for (int i = 0 ; i <= Character.MAX_RADIX; i++)
        {
            int precision = (int) Math.ceil(Math.log(9007199254740992.0) / Math.log((double) i));
            doublePrecision.append(i > 0 ? ", " : "").append(i >= Character.MIN_RADIX ? precision : -1);
        }
        doublePrecision.append(" };");

        StringBuilder longPrecision = new StringBuilder("{ ");
        for (int i = 0 ; i <= Character.MAX_RADIX; i++)
        {
            int precision = (int) Math.ceil(Math.log(9223372036854775808.0) / Math.log((double) i));
            longPrecision.append(i > 0 ? ", " : "").append(i >= Character.MIN_RADIX ? precision : -1);
        }
        longPrecision.append(" };");

        System.out.println("package org.apfloat.spi;");
        System.out.println("");
        System.out.println("public interface RadixConstants");
        System.out.println("{");
        System.out.println("    public static final int RADIX_FACTORS[][] = " + radixFactors);
        System.out.println("");
        System.out.println("    public static final int FLOAT_PRECISION[] = " + floatPrecision);
        System.out.println("    public static final int DOUBLE_PRECISION[] = " + doublePrecision);
        System.out.println("    public static final int LONG_PRECISION[] = " + longPrecision);
        System.out.println("}");
    }

    private static boolean isPrime(int p)
    {
        return java.math.BigInteger.valueOf(p).isProbablePrime(128);
    }
}
