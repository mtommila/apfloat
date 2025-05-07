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
package org.apfloat.calc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.apfloat.ApfloatArithmeticException;
import org.apfloat.ApfloatRuntimeException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class CalculatorTest
    extends TestCase
{
    public CalculatorTest(String methodName)
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

        suite.addTest(new CalculatorTest("testOperators"));
        suite.addTest(new CalculatorTest("testFunctions"));
        suite.addTest(new CalculatorTest("testArguments"));

        return suite;
    }

    private static void assertCalculation(String expected, String input, String... args)
        throws ParseException
    {
        String actual = runCalculation(input, args);
        assertEquals(input, expected + NEWLINE, actual);
    }

    private static void assertCalculationMatch(String expectedPattern, String input, String... args)
        throws ParseException
    {
        String actual = runCalculation(input, args);
        if (!actual.matches('(' + expectedPattern + ')' + NEWLINE))
        {
            assertEquals(input, expectedPattern + NEWLINE, actual);
        }
    }

    private static String runCalculation(String input, String... args)
        throws ParseException
    {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        String actual;

        try
        {
            InputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(buffer, true);
            System.setIn(in);
            System.setOut(out);

            Calculator.main(args);

            actual = new String(buffer.toByteArray());
        }
        finally
        {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }

        return actual;
    }

    private static void assertCalculationFailure(String input, String... args)
    {
        try
        {
            runCalculation(input, args);
            fail(input + " accepted");
        }
        catch (ParseException pe)
        {
            // OK: syntax error
        }
        catch (ApfloatArithmeticException aae)
        {
            // OK: result is not a number
        }
        catch (IllegalArgumentException iae)
        {
            // OK: invalid parameter
        }
        catch (ApfloatRuntimeException are)
        {
            // OK: attempt invalid calculation
        }
    }

    public static void testOperators()
        throws ParseException
    {
        assertCalculation("163", "1+2*3^4");
        assertCalculation("77", "99-22");
        assertCalculation("7.7e1", "99.1-22.1");
        assertCalculation("i", "2i-i");
        assertCalculation("5" + NEWLINE + "11", "x = 5; x + 6");
        assertCalculation("5" + NEWLINE + "11" + NEWLINE + "11", "x = 5; x += 6; x");
        assertCalculation("5" + NEWLINE + "-1" + NEWLINE + "-1", "x = 5; x -= 6; x");
        assertCalculation("5" + NEWLINE + "30" + NEWLINE + "30", "x = 5; x *= 6; x");
        assertCalculation("30" + NEWLINE + "5" + NEWLINE + "5", "x = 30; x /= 6; x");
        assertCalculation("31" + NEWLINE + "1" + NEWLINE + "1", "x = 31; x %= 6; x");
        assertCalculation("5" + NEWLINE + "125" + NEWLINE + "125", "x = 5; x ^= 3; x");
        assertCalculation("2", "5 % 3");
        assertCalculation("2.1", "5.1 % 3");
        assertCalculation("2", "5 + -3");
        assertCalculation("7/6", "1/2 + 2/3");
        assertCalculation("5e-1", "1./2");
        assertCalculation("5e-1", ".5");
        assertCalculation("5e-1", ".5e0");
        assertCalculation("5e-1", "5e-1");
        assertCalculation("5e-1", "5.e-1");
        assertCalculation("5e1", "5e1");
        assertCalculation("6.67", "20.0/3.00");
        assertCalculation("-1", "I*I");
        assertCalculation("3+2i", "2I+3");
        assertCalculation("3-2i", "i*-2+3");
        assertCalculation("35", "((5))*(3+4)");
        assertCalculation("7", "+++++++++++++7");
        assertCalculation("8", "--8");
        assertCalculation("-9", "-----9");
        assertCalculation("1", " ;;; \t 1 ;;;;;;;;; ");
        assertCalculation("1.41421", "2.0000^0.50000");
        assertCalculation("8.8080652584e646456992", "2^2147483647.0");
        assertCalculation("-1", "i^2");
        assertCalculation("1", "1^2i");
        assertCalculation("2417851639229258349412352", "2^3^4");
        assertCalculation("120", "5!");
        assertCalculation("15", "5!!");
        assertCalculation("64", "2^3!");
        assertCalculation("8", "2^3!!");
        assertCalculation("1307674368000", "5!!!");
        assertCalculation("2027025", "5!!!!");
        assertCalculation("-24", "-4!");
        assertCalculation("1/16777216", "2^-4!");
        assertCalculation("-1/16777216", "-2^-4!");
        assertCalculation("8", "2^3!!!!");
        assertCalculation("-1/8", "-2^-3!!!!");
        assertCalculation("4", "2!^2!");
        assertCalculation("4", "2!!^2!!");

        assertCalculationFailure("6%i");
        assertCalculationFailure("5e");
        assertCalculationFailure("2^^2");
        assertCalculationFailure("2a2");
        assertCalculationFailure("x=");
        assertCalculationFailure("x+=");
        assertCalculationFailure("x-=");
        assertCalculationFailure("x*=");
        assertCalculationFailure("x/=");
        assertCalculationFailure("x%=");
        assertCalculationFailure("x^=");
        assertCalculationFailure("*5");
        assertCalculationFailure("/");
        assertCalculationFailure("6%");
        assertCalculationFailure("2-");
        assertCalculationFailure("bogus^5");
        assertCalculationFailure("!");
        assertCalculationFailure("bogus!");
        assertCalculationFailure("bogus!!");
        assertCalculationFailure("5!=");
        assertCalculationFailure("5!=4");
    }

    public static void testFunctions()
        throws ParseException
    {
        assertCalculation("3", "abs(-3)");
        assertCalculation("3", "abs(-3i)");
        assertCalculation("5", "abs(3.00 + 4.00I)");
        assertCalculation("3.03703e-1", "airyAi(0.200000)");
        assertCalculation("-2.52405e-1", "airyAiPrime(0.200000)");
        assertCalculation("7.05464e-1", "airyBi(0.200000)");
        assertCalculation("4.61789e-1", "airyBiPrime(0.200000)");
        assertCalculation("7/6", "bernoulli(14)");
        assertCalculation("3.3124e4", "bernoulliB(4,14.0000)");
        assertCalculation("1.72763", "besselI(0.700000,1.90000)");
        assertCalculation("5.84978e-1", "besselJ(0.700000,1.90000)");
        assertCalculation("2.06052", "besselK(0.700000,0.300000)");
        assertCalculation("-1.6523", "besselY(0.700000,0.300000)");
        assertCalculation("3.55114", "beta(0.200000,4.00000)");
        assertCalculation("3.00545", "beta(0.100000,0.200000,4.00000)");
        assertCalculation("-1.02994", "beta(0.100000,2.90000,0.200000,4.00000)");
        assertCalculation("1.5057e2", "binomial(9.20000,4.60000)");
        assertCalculation("2.3282", "binomial(4,3.5000)");
        assertCalculation("6.5625", "binomial(4.5000,3)");
        assertCalculation("45", "binomial(10,2)");
        assertCalculation("366166666666666666665821666666666666666667100000000000000000000", "binomial(1300000000000000000000,3)");
        assertCalculation("0", "binomial(3,1300000000000000000000)");
        assertCalculation("1", "binomial(1300000000000000000000,1300000000000000000000)");
        assertCalculation("1040/81", "binomial(16/3,7/3)");
        assertCalculation("-455/81", "binomial(-7/3,-16/3)");
        assertCalculation("-1", "binomial(-1,1)");
        assertCalculation("0", "binomial(1,-1)");
        assertCalculation("0", "binomial(3.5,4.5)");
        assertCalculation("-3.109-1.604i", "binomial(3.000+4.000i,5.000+6.000i)");
        assertCalculation("9.1596559e-1", "catalan(8)");
        assertCalculation("2", "cbrt(8)");
        assertCalculation("2/3", "cbrt(8/27)");
        assertCalculation("7.07e-1+7.07e-1i", "cbrt(-0.707+0.707i)");
        assertCalculation("1.2599", "cbrt(2.0000)");
        assertCalculation("2", "ceil(1.1)");
        assertCalculation("2", "ceil(3/2)");
        assertCalculation("1.41288", "chebyshevT(0.70000,1.9000)");
        assertCalculation("2.58673", "chebyshevU(0.700000,1.90000)");
        assertCalculation("-2", "copySign(2, -3)");
        assertCalculation("-1.04221", "cosIntegral(0.200000)");
        assertCalculation("-1.02221", "coshIntegral(0.200000)");
        assertCalculation("1.7061", "digamma(6.0000)");
        assertCalculation("34459425", "doubleFactorial(17)");
        assertCalculation("1.48904", "ellipticE(0.200000)");
        assertCalculation("1.65962", "ellipticK(0.200000)");
        assertCalculation("2.22703e-1", "erf(0.200000)");
        assertCalculation("7.77297e-1", "erfc(0.200000)");
        assertCalculation("2.41591", "erfi(1.20000)");
        assertCalculation("1.2141", "eulerE(4, 1.90000)");
        assertCalculation("1.1722", "expIntegralE(0.70000,0.30000)");
        assertCalculation("2.44209", "expIntegralEi(1.20000)");
        assertCalculation("5040", "factorial(7)");
        assertCalculation("7.3936", "fibonacci(5.00000, 1.20000)");
        assertCalculation("2", "floor(2.9)");
        assertCalculation("2", "floor(29/10)");
        assertCalculation("9e-1", "frac(2.9)");
        assertCalculation("9/10", "frac(29/10)");
        assertCalculation("7.15438e-1", "fresnelC(1.20000)");
        assertCalculation("6.23401e-1", "fresnelS(1.20000)");
        assertCalculation("6", "gamma(4)");
        assertCalculation("5.8861", "gamma(4.0000, 1.0000)");
        assertCalculation("5.0928", "gamma(4.0000, 0, 6.0000)");
        assertCalculation("4.74294943677064514689542753377e1-3.27488916473624576880974867017e1i", "gamma(100.000000000000000000000000000+374.000000000000000000000000000i)");
        assertCalculation("-5.25676", "gegenbauerC(-1.10000,2.50000)");
        assertCalculation("-2.60129", "gegenbauerC(-1.10000,-1.30000,2.50000)");
        assertCalculation("1.282427", "glaisher(7)");
        assertCalculation("1.12151", "harmonicNumber(1.20000)");
        assertCalculation("1.104112", "harmonicNumber(1.200000,1.300000)");
        assertCalculation("42142223/12252240", "harmonicNumber(17)");
        assertCalculation("6301272372663207205033976933/6076911214672415134617600000", "harmonicNumber(13,5)");
        assertCalculation("3.04424", "hermiteH(1.20000,1.30000)");
        assertCalculation("1.453+1.237i", "hypergeometric0F1(3.456+2.890i,0.1234+4.678i)");
        assertCalculation("2.6328", "hypergeometric0F1Regularized(-3.00000,2.50000)");
        assertCalculation("-7.477+3.738i", "hypergeometric1F1(3.456+2.890i,3.456+2.890i,2.1234+2.678i)");
        assertCalculation("1.2856", "hypergeometric1F1Regularized(0.200000,-3,1.50000)");
        assertCalculation("-1.477-2.073i", "hypergeometric2F1(9.456+4.890i,3.456+2.890i,1.256+1.390i,0.11234+0.1678i)");
        assertCalculation("4.8317", "hypergeometric2F1Regularized(0.100000,0.200000,-3,0.800000)");
        assertCalculation("4.99704", "hypergeometricU(1.20000,2,0.200000)");
        assertCalculation("2", "fmod(5, 3)");
        assertCalculation("5", "hypot(3, 4)");
        assertCalculation("5e-1", "hypot(0.3, 0.4)");
        assertCalculation("1.16309", "inverseErf(0.900000)");
        assertCalculation("1.0179", "inverseErfc(0.150000)");
        assertCalculation("5e-1", "inverseRoot(2.0, 1)");
        assertCalculation("5e-1", "inverseRoot(4.0, 2)");
        assertCalculation("7.07e-1-7.07e-1i", "inverseRoot(-0.707+0.707i, 3)");
        assertCalculation("-5e-1", "inverseRoot(4.0, 2, 1)");
        assertCalculation("6.28477", "jacobiP(1.20000,1.70000,0.900000,1.90000)");
        assertCalculation("2.685452", "khinchin(7)");
        assertCalculation("-1.01693", "laguerreL(1.20000,1.90000)");
        assertCalculation("1.38308", "laguerreL(1.20000,1.70000,1.30000)");
        assertCalculation("2.26206", "legendreP(1.20000,1.90000)");
        assertCalculation("-3.36749", "legendreP(1.20000,1.70000,0.900000)");
        assertCalculation("1.07204", "legendreQ(1.70000,-0.900000)");
        assertCalculation("4.1358", "legendreQ(1.20000,1.70000,0.900000)");
        assertCalculation("-4.371-3.651i", "logGamma(-1.234+2.345i)");
        assertCalculation("1.04516", "logIntegral(2.0000)");
        assertCalculation("8.80797e-1", "logisticSigmoid(2.00000)");
        assertCalculation("1.23", "n(1.23456, 3)");
        assertCalculation("1.04748", "pochhammer(1.20000,0.700000)");
        assertCalculation("280/81", "pochhammer(1/3,4)");
        assertCalculation("-3.2018", "polygamma(2,0.900000)");
        assertCalculation("1.79112-2.61823i", "polylog(1.20000,1.30000)");
        assertCalculation("2", "root(8, 3)");
        assertCalculation("2/3", "root(16/81, 4)");
        assertCalculation("7.07e-1+7.07e-1i", "root(-0.707+0.707i, 3)");
        assertCalculation("1.2599", "root(2.0000, 3)");
        assertCalculation("-4", "root(16.0, 2, 1)");
        assertCalculation("3", "round(2.9, 1)");
        assertCalculation("3", "round(29/10, 1)");
        assertCalculation("3", "round(2.5, 1)");
        assertCalculation("4", "round(3.5, 1)");
        assertCalculation("-3", "round(-2.5, 1)");
        assertCalculation("-4", "round(-3.5, 1)");
        assertCalculation("3", "roundToPrecision(2.9, 1)");
        assertCalculation("3", "roundToPrecision(29/10, 1)");
        assertCalculation("3", "roundToPrecision(2.5, 1)");
        assertCalculation("4", "roundToPrecision(3.5, 1)");
        assertCalculation("-3", "roundToPrecision(-2.5, 1)");
        assertCalculation("-4", "roundToPrecision(-3.5, 1)");
        assertCalculation("3", "roundToInteger(2.9)");
        assertCalculation("3", "roundToInteger(29/10)");
        assertCalculation("3", "roundToInteger(2.5)");
        assertCalculation("-3", "roundToInteger(-2.5)");
        assertCalculation("-4", "roundToInteger(-3.5)");
        assertCalculation("2.9", "roundToPlaces(2.94, 1)");
        assertCalculation("3e-1", "roundToPlaces(29/100, 1)");
        assertCalculation("30", "roundToPlaces(25, -1)");
        assertCalculation("4", "roundToPlaces(3.5, 0)");
        assertCalculation("-2.35", "roundToPlaces(-2.345, 2)");
        assertCalculation("-3.36", "roundToPlaces(-3.355, 2)");
        assertCalculation("2.4", "roundToMultiple(2.9, 1.2)");
        assertCalculation("20/7", "roundToMultiple(29/10, 1/7)");
        assertCalculation("3", "roundToMultiple(2.5, 1.0)");
        assertCalculation("4", "roundToMultiple(3.5, 1.0)");
        assertCalculation("-2.5", "roundToMultiple(-2.5, 1.25)");
        assertCalculation("-3", "roundToMultiple(-3., 0.3)");
        assertCalculation("20000000000", "scale(2, 10)");
        assertCalculation("1/5", "scale(2, -1)");
        assertCalculation("2.1e10", "scale(2.1, 10)");
        assertCalculation("2.5e10+1.5e10i", "scale(2.5+1.5i, 10)");
        assertCalculation("200000/3", "scale(2/3, 5)");
        assertCalculation("4.54649e-1", "sinc(2.00000)");
        assertCalculation("1.60541", "sinIntegral(2.00000)");
        assertCalculation("2.50157", "sinhIntegral(2.00000)");
        assertCalculation("4.33712e-1-1.30178e-1i", "sphericalHarmonicY(1.20000,1.50000,0.900000,1.90000)");
        assertCalculation("3", "sqrt(9)");
        assertCalculation("2/3", "sqrt(4/9)");
        assertCalculation("7.07+7.07i", "sqrt(100.i)");
        assertCalculation("1.4142", "sqrt(2.0000)");
        assertCalculation("2", "truncate(2.5)");
        assertCalculation("2", "truncate(5/2)");
        assertCalculation("1.79e2", "toDegrees(3.14)");
        assertCalculation("1.57", "toRadians(90.0)");
        assertCalculation("1.644", "zeta(2.000)");
        assertCalculation("1.181e-1", "zeta(3.000,-1.500)");
        assertCalculation("3.14", "acos(-1.00)");
        assertCalculation("1.01", "acosh(1.55)");
        assertCalculation("1.57", "asin(1.00)");
        assertCalculation("0", "asinh(0)");
        assertCalculation("7.85e-1", "atan(1.00)");
        assertCalculation("0", "atanh(0)");
        assertCalculation("1.57", "atan2(1.00, 0)");
        assertCalculation("1", "cos(0)");
        assertCalculation("1.54", "cosh(1.00)");
        assertCalculation("8.414709848e-1", "sin(1.000000000)");
        assertCalculation("1.18", "sinh(1.00)");
        assertCalculation("1.56", "tan(1.00)");
        assertCalculation("9.64e-1", "tanh(2.00)");
        assertCalculation("2.7182818284590452353602874713526624977572470937", "e(47)");
        assertCalculation("5.772e-1", "euler(4)");
        assertCalculation("2.718", "exp(1.000)");
        assertCalculation("1.79+3.14i", "log(-6.00)");
        assertCalculation("1.58", "log(3.00, 2.00)");
        assertCalculation("1.585", "log(3.000, 2)");
        assertCalculation("1.585", "log(3, 2.0000)");
        assertCalculation("1.2", "max(1.1, 1.2)");
        assertCalculation("1/3", "max(1/3, 1/4)");
        assertCalculation("2", "max(1, 2)");
        assertCalculation("1.1", "min(1.1, 1.2)");
        assertCalculation("1/4", "min(1/3, 1/4)");
        assertCalculation("1", "min(1, 2)");
        assertCalculation("2", "nextAfter(1., 2)");
        assertCalculation("2", "nextUp(1.)");
        assertCalculation("1", "nextDown(2.)");
        assertCalculation("2/3", "nextDown(2/3)");
        assertCalculation("1", "ulp(2.)");
        assertCalculation("0", "ulp(2/3)");
        assertCalculation("0", "ulp(1+i)");
        assertCalculation("1e-1", "ulp(-10.00-2.0i)");
        assertCalculation("1.57", "arg(1.00i)");
        assertCalculation("2-i", "conj(2+i)");
        assertCalculation("1", "imag(2+i)");
        assertCalculation("2", "real(2+i)");
        assertCalculation("2.47468", "agm(2.000000, 3.000000)");
        assertCalculation("5.672e-1", "w(1.000)");
        assertCalculation("-3.181e-1+1.3372i", "w(-1.0000)");
        assertCalculation("-1.34285+5.24725i", "w(1.00000+i, 1)");
        assertCalculation("3", "gcd(15, 12)");
        assertCalculation("60", "lcm(15, 12)");
        assertCalculation("3.14159", "pi(6)");
        assertCalculationMatch("0|[1-9]e-1", "random(1)");
        assertCalculationMatch("0|-?([1-5]|[1-9]e-1)", "randomGaussian(1)");
        assertCalculation("5", "add(2, 3)");
        assertCalculation("-1", "subtract(2, 3)");
        assertCalculation("6", "multiply(2, 3)");
        assertCalculation("2/3", "divide(2, 3)");
        assertCalculation("-2", "negate(2)");
        assertCalculation("2", "mod(5, 3)");
        assertCalculation("8", "pow(2, 3)");

        assertCalculationFailure("airyAi()");
        assertCalculationFailure("airyAi(0,0)");
        assertCalculationFailure("airyAiPrime()");
        assertCalculationFailure("airyAiPrime(0,0)");
        assertCalculationFailure("airyBi()");
        assertCalculationFailure("airyBi(0,0)");
        assertCalculationFailure("airyBiPrime()");
        assertCalculationFailure("airyBiPrime(0,0)");
        assertCalculationFailure("bernoulli(-1)");
        assertCalculationFailure("bernoulli(i)");
        assertCalculationFailure("bernoulli()");
        assertCalculationFailure("bernoulli(1,1)");
        assertCalculationFailure("bernoulliB(1)");
        assertCalculationFailure("bernoulliB(1,1.0,1.0)");
        assertCalculationFailure("bernoulliB(-1,1.0)");
        assertCalculationFailure("bernoulliB(i,1.0)");
        assertCalculationFailure("bernoulliB(2/3,1.0)");
        assertCalculationFailure("besselI(1.0)");
        assertCalculationFailure("besselI(1.0,1.0,1.0)");
        assertCalculationFailure("besselJ(1.0)");
        assertCalculationFailure("besselJ(1.0,1.0,1.0)");
        assertCalculationFailure("besselK(1.0)");
        assertCalculationFailure("besselK(1.0,1.0,1.0)");
        assertCalculationFailure("besselY(1.0)");
        assertCalculationFailure("besselY(1.0,1.0,1.0)");
        assertCalculationFailure("beta(1.0)");
        assertCalculationFailure("beta(1.0,1.0,1.0,1.0,1.0)");
        assertCalculationFailure("binomial(-3,5i)");
        assertCalculationFailure("binomial(-3i,5)");
        assertCalculationFailure("binomial(-3i,5i)");
        assertCalculationFailure("binomial(-3,3.5)");
        assertCalculationFailure("binomial(4/3,3/2)");
        assertCalculationFailure("binomial()");
        assertCalculationFailure("binomial(2)");
        assertCalculationFailure("binomial(2,2,2)");
        assertCalculationFailure("catalan()");
        assertCalculationFailure("catalan(5, 5)");
        assertCalculationFailure("catalan(0)");
        assertCalculationFailure("catalan(-1)");
        assertCalculationFailure("catalan(0.5)");
        assertCalculationFailure("catalan(i)");
        assertCalculationFailure("cbrt(2)");
        assertCalculationFailure("cbrt()");
        assertCalculationFailure("cbrt(8, 2)");
        assertCalculationFailure("ceil(i)");
        assertCalculationFailure("ceil()");
        assertCalculationFailure("ceil(2, 2)");
        assertCalculationFailure("chebyshevT(1.0)");
        assertCalculationFailure("chebyshevT(1.0,1.0,1.0)");
        assertCalculationFailure("chebyshevU(1.0)");
        assertCalculationFailure("chebyshevU(1.0,1.0,1.0)");
        assertCalculationFailure("copySign(2i, -3)");
        assertCalculationFailure("copySign(2)");
        assertCalculationFailure("copySign(2, 2, 2)");
        assertCalculationFailure("cosIntegral()");
        assertCalculationFailure("cosIntegral(1.0,1.0)");
        assertCalculationFailure("coshIntegral()");
        assertCalculationFailure("coshIntegral(1.0,1.0)");
        assertCalculationFailure("digamma(0)");
        assertCalculationFailure("digamma()");
        assertCalculationFailure("digamma(2,2)");
        assertCalculationFailure("doubleFactorial(0.5)");
        assertCalculationFailure("doubleFactorial(2i)");
        assertCalculationFailure("doubleFactorial(2/3)");
        assertCalculationFailure("doubleFactorial()");
        assertCalculationFailure("doubleFactorial(2, 2)");
        assertCalculationFailure("ellipticE()");
        assertCalculationFailure("ellipticE(0.5,0.5)");
        assertCalculationFailure("ellipticK()");
        assertCalculationFailure("ellipticK(0.5,0.5)");
        assertCalculationFailure("erf()");
        assertCalculationFailure("erf(0.5,0.5)");
        assertCalculationFailure("erfc()");
        assertCalculationFailure("erfc(0.5,0.5)");
        assertCalculationFailure("erfi()");
        assertCalculationFailure("erfi(0.5,0.5)");
        assertCalculationFailure("eulerE(1)");
        assertCalculationFailure("eulerE(1,1.0,1.0");
        assertCalculationFailure("eulerE(-1,1.0)");
        assertCalculationFailure("eulerE(i,1.0)");
        assertCalculationFailure("eulerE(2/3,1.0)");
        assertCalculationFailure("expIntegralE(0.5)");
        assertCalculationFailure("expIntegralE(0.5,0.5,0.5)");
        assertCalculationFailure("expIntegralEi()");
        assertCalculationFailure("expIntegralEi(0.5,0.5)");
        assertCalculationFailure("factorial(0.5)");
        assertCalculationFailure("factorial(2i)");
        assertCalculationFailure("factorial(2/3)");
        assertCalculationFailure("factorial()");
        assertCalculationFailure("factorial(2, 2)");
        assertCalculationFailure("fibonacci(1.0)");
        assertCalculationFailure("fibonacci(1.0,1.0,1.0)");
        assertCalculationFailure("floor(i)");
        assertCalculationFailure("floor()");
        assertCalculationFailure("floor(2, 2)");
        assertCalculationFailure("frac(i)");
        assertCalculationFailure("frac()");
        assertCalculationFailure("frac(2, 2)");
        assertCalculationFailure("fresnelC()");
        assertCalculationFailure("fresnelC(1.0,1.0)");
        assertCalculationFailure("fresnelS()");
        assertCalculationFailure("fresnelS(1.0,1.0)");
        assertCalculationFailure("gamma(0)");
        assertCalculationFailure("gamma()");
        assertCalculationFailure("gamma(2, 2, 2, 2)");
        assertCalculationFailure("gegenbauerC(1.0)");
        assertCalculationFailure("gegenbauerC(1.0,1.0,1.0,1.0)");
        assertCalculationFailure("glaisher()");
        assertCalculationFailure("glaisher(5, 5)");
        assertCalculationFailure("glaisher(0)");
        assertCalculationFailure("glaisher(-1)");
        assertCalculationFailure("glaisher(0.5)");
        assertCalculationFailure("glaisher(i)");
        assertCalculationFailure("harmonicNumber()");
        assertCalculationFailure("harmonicNumber(-1)");
        assertCalculationFailure("harmonicNumber(-1,1)");
        assertCalculationFailure("harmonicNumber(1.0,1.0,1.0)");
        assertCalculationFailure("hermiteH(1.0)");
        assertCalculationFailure("hermiteH(1.0,1.0,1.0)");
        assertCalculationFailure("hypergeometric0F1(0)");
        assertCalculationFailure("hypergeometric0F1(0,0)");
        assertCalculationFailure("hypergeometric0F1(0,0,0)");
        assertCalculationFailure("hypergeometric0F1Regularized(0)");
        assertCalculationFailure("hypergeometric0F1Regularized(0,0,0)");
        assertCalculationFailure("hypergeometric1F1(0,0)");
        assertCalculationFailure("hypergeometric1F1(1,0,0)");
        assertCalculationFailure("hypergeometric1F1(0,0,0,0)");
        assertCalculationFailure("hypergeometric1F1Regularized(0,0)");
        assertCalculationFailure("hypergeometric1F1Regularized(0,0,0,0)");
        assertCalculationFailure("hypergeometric2F1(0,0,0)");
        assertCalculationFailure("hypergeometric2F1(1,2,0,0)");
        assertCalculationFailure("hypergeometric2F1(0,0,0,0,0)");
        assertCalculationFailure("hypergeometric2F1Regularized(0,0,0)");
        assertCalculationFailure("hypergeometric2F1Regularized(0,0,0,0,0)");
        assertCalculationFailure("hypergeometricU(0,0)");
        assertCalculationFailure("hypergeometricU(0,0,0,0)");
        assertCalculationFailure("fmod(2, i)");
        assertCalculationFailure("fmod(2)");
        assertCalculationFailure("fmod(2, 2, 2)");
        assertCalculationFailure("hypot(2, i)");
        assertCalculationFailure("hypot(2)");
        assertCalculationFailure("hypot(2, 2, 2)");
        assertCalculationFailure("inverseErf()");
        assertCalculationFailure("inverseErf(-1.0)");
        assertCalculationFailure("inverseErf(1.0)");
        assertCalculationFailure("inverseErf(2.0)");
        assertCalculationFailure("inverseErf(0.5i)");
        assertCalculationFailure("inverseErf(0.5,0.5)");
        assertCalculationFailure("inverseErfc()");
        assertCalculationFailure("inverseErfc(0)");
        assertCalculationFailure("inverseErfc(2.0)");
        assertCalculationFailure("inverseErfc(3.0)");
        assertCalculationFailure("inverseErfc(0.5i)");
        assertCalculationFailure("inverseErfc(0.5,0.5)");
        assertCalculationFailure("inverseRoot(2.0, i)");
        assertCalculationFailure("inverseRoot(2.0, 1/2)");
        assertCalculationFailure("inverseRoot(2, 2)");
        assertCalculationFailure("inverseRoot(2)");
        assertCalculationFailure("inverseRoot(2.0, 1, i)");
        assertCalculationFailure("inverseRoot(2.0, i, 1)");
        assertCalculationFailure("inverseRoot(2.0, i, i)");
        assertCalculationFailure("inverseRoot(2.0, 2, 2, 2)");
        assertCalculationFailure("jacobiP(1.0,1.0,1.0)");
        assertCalculationFailure("jacobiP(1.0,1.0,1.0,1.0,1.0)");
        assertCalculationFailure("khinchin()");
        assertCalculationFailure("khinchin(5, 5)");
        assertCalculationFailure("khinchin(0)");
        assertCalculationFailure("khinchin(-1)");
        assertCalculationFailure("khinchin(0.5)");
        assertCalculationFailure("khinchin(i)");
        assertCalculationFailure("laguerreL(1.0)");
        assertCalculationFailure("laguerreL(1.0,1.0,1.0,1.0)");
        assertCalculationFailure("legendreP(1.0)");
        assertCalculationFailure("legendreP(1.0,1.0,1.0,1.0)");
        assertCalculationFailure("legendreQ(1.0)");
        assertCalculationFailure("legendreQ(1.0,1.0,1.0,1.0)");
        assertCalculationFailure("logGamma(0)");
        assertCalculationFailure("logGamma(-1)");
        assertCalculationFailure("logGamma()");
        assertCalculationFailure("logGamma(2,2)");
        assertCalculationFailure("logIntegral()");
        assertCalculationFailure("logIntegral(1.0,1.0)");
        assertCalculationFailure("logisticSigmoid()");
        assertCalculationFailure("logisticSigmoid(2.0,2.0)");
        assertCalculationFailure("n(1.23456, i)");
        assertCalculationFailure("n(1.23456, 0.5)");
        assertCalculationFailure("n(1.23456)");
        assertCalculationFailure("n(1.23456, 2, 2)");
        assertCalculationFailure("pochhammer(1.0)");
        assertCalculationFailure("pochhammer(1.0,1.0,1.0)");
        assertCalculationFailure("pochhammer(1/3,1/4)");
        assertCalculationFailure("polygamma(1)");
        assertCalculationFailure("polygamma(-1,1.0)");
        assertCalculationFailure("polygamma(1,1.0,1.0)");
        assertCalculationFailure("polylog(0.5)");
        assertCalculationFailure("polylog(0.5,0.5,0.5)");
        assertCalculationFailure("root(2, 2)");
        assertCalculationFailure("root(2)");
        assertCalculationFailure("root(2, i)");
        assertCalculationFailure("root(2, 1, i)");
        assertCalculationFailure("root(2, i, 1)");
        assertCalculationFailure("root(2, i, i)");
        assertCalculationFailure("root(4.0, 2, 2, 2)");
        assertCalculationFailure("round(2)");
        assertCalculationFailure("round(2, 0)");
        assertCalculationFailure("round(2, 0.5)");
        assertCalculationFailure("round(2, i)");
        assertCalculationFailure("round(i, 2)");
        assertCalculationFailure("round(2, 2, 2)");
        assertCalculationFailure("roundToPrecision(2)");
        assertCalculationFailure("roundToPrecision(2, 0)");
        assertCalculationFailure("roundToPrecision(2, 0.5)");
        assertCalculationFailure("roundToPrecision(2, i)");
        assertCalculationFailure("roundToPrecision(i, 2)");
        assertCalculationFailure("roundToPrecision(2, 2, 2)");
        assertCalculationFailure("roundToInteger()");
        assertCalculationFailure("roundToInteger(i)");
        assertCalculationFailure("roundToInteger(2, 0)");
        assertCalculationFailure("roundToPlaces(2)");
        assertCalculationFailure("roundToPlaces(2, 0.5)");
        assertCalculationFailure("roundToPlaces(2, i)");
        assertCalculationFailure("roundToPlaces(i, 2)");
        assertCalculationFailure("roundToPlaces(2, 2, 2)");
        assertCalculationFailure("roundToMultiple(2)");
        assertCalculationFailure("roundToMultiple(2, 0)");
        assertCalculationFailure("roundToMultiple(2, i)");
        assertCalculationFailure("roundToMultiple(i, 2)");
        assertCalculationFailure("roundToMultiple(2, 2, 2)");
        assertCalculationFailure("scale(1.23456, i)");
        assertCalculationFailure("scale(1.23456, 0.5)");
        assertCalculationFailure("scale(1.23456)");
        assertCalculationFailure("scale(1.23456, 2, 2)");
        assertCalculationFailure("sinc()");
        assertCalculationFailure("sinc(1.0,1.0)");
        assertCalculationFailure("sinIntegral()");
        assertCalculationFailure("sinIntegral(1.0,1.0)");
        assertCalculationFailure("sinhIntegral()");
        assertCalculationFailure("sinhIntegral(1.0,1.0)");
        assertCalculationFailure("sphericalHarmonicY(1.0,1.0,1.0)");
        assertCalculationFailure("sphericalHarmonicY(1.0,1.0,1.0,1.0,1.0)");
        assertCalculationFailure("sqrt(2)");
        assertCalculationFailure("sqrt()");
        assertCalculationFailure("sqrt(4, 2)");
        assertCalculationFailure("truncate(i)");
        assertCalculationFailure("truncate()");
        assertCalculationFailure("truncate(2, 2)");
        assertCalculationFailure("toDegrees(2)");
        assertCalculationFailure("toDegrees(i)");
        assertCalculationFailure("toDegrees()");
        assertCalculationFailure("toDegrees(2, 2)");
        assertCalculationFailure("toRadians(2)");
        assertCalculationFailure("toRadians(i)");
        assertCalculationFailure("toRadians()");
        assertCalculationFailure("toRadians(2, 2)");
        assertCalculationFailure("zeta(1)");
        assertCalculationFailure("zeta(2,0)");
        assertCalculationFailure("zeta(2,-1)");
        assertCalculationFailure("zeta()");
        assertCalculationFailure("zeta(2,3,4)");
        assertCalculationFailure("acos()");
        assertCalculationFailure("acos(1, 1)");
        assertCalculationFailure("acosh()");
        assertCalculationFailure("acosh(1, 1)");
        assertCalculationFailure("asin()");
        assertCalculationFailure("asin(1, 1)");
        assertCalculationFailure("asinh()");
        assertCalculationFailure("asinh(1, 1)");
        assertCalculationFailure("atan()");
        assertCalculationFailure("atan(1, 1)");
        assertCalculationFailure("atanh()");
        assertCalculationFailure("atanh(1, 1)");
        assertCalculationFailure("atan2(1)");
        assertCalculationFailure("atan2(i, i)");
        assertCalculationFailure("atan2(1, 1, 1)");
        assertCalculationFailure("cos()");
        assertCalculationFailure("cos(1, 1)");
        assertCalculationFailure("cosh()");
        assertCalculationFailure("cosh(1, 1)");
        assertCalculationFailure("sin()");
        assertCalculationFailure("sin(1, 1)");
        assertCalculationFailure("sinh()");
        assertCalculationFailure("sinh(1, 1)");
        assertCalculationFailure("tan()");
        assertCalculationFailure("tan(1, 1)");
        assertCalculationFailure("tanh()");
        assertCalculationFailure("tanh(1, 1)");
        assertCalculationFailure("e()");
        assertCalculationFailure("e(0)");
        assertCalculationFailure("e(-1)");
        assertCalculationFailure("e(0.5)");
        assertCalculationFailure("e(i)");
        assertCalculationFailure("e(1, 1)");
        assertCalculationFailure("euler()");
        assertCalculationFailure("euler(0)");
        assertCalculationFailure("euler(-1)");
        assertCalculationFailure("euler(0.5)");
        assertCalculationFailure("euler(i)");
        assertCalculationFailure("euler(1, 1)");
        assertCalculationFailure("exp()");
        assertCalculationFailure("exp(1, 1)");
        assertCalculationFailure("log()");
        assertCalculationFailure("log(1, 1, 1)");
        assertCalculationFailure("max()");
        assertCalculationFailure("max(1)");
        assertCalculationFailure("max(1, 1, 1)");
        assertCalculationFailure("max(i, i)");
        assertCalculationFailure("min()");
        assertCalculationFailure("min(1)");
        assertCalculationFailure("min(1, 1, 1)");
        assertCalculationFailure("min(i, i)");
        assertCalculationFailure("nextAfter(1)");
        assertCalculationFailure("nextAfter(1, 2, 3)");
        assertCalculationFailure("nextAfter(1, i)");
        assertCalculationFailure("nextUp()");
        assertCalculationFailure("nextUp(1, 1)");
        assertCalculationFailure("nextUp(i)");
        assertCalculationFailure("nextDown()");
        assertCalculationFailure("nextDown(2, 2)");
        assertCalculationFailure("nextDown(i)");
        assertCalculationFailure("ulp()");
        assertCalculationFailure("ulp(2, 2)");
        assertCalculationFailure("arg()");
        assertCalculationFailure("arg(1, 1)");
        assertCalculationFailure("conj()");
        assertCalculationFailure("conj(1, 1)");
        assertCalculationFailure("imag()");
        assertCalculationFailure("imag(1, 1)");
        assertCalculationFailure("real()");
        assertCalculationFailure("real(1, 1)");
        assertCalculationFailure("agm(1)");
        assertCalculationFailure("agm(1, 1, 1)");
        assertCalculationFailure("w(1, 1, 1)");
        assertCalculationFailure("w(1, i)");
        assertCalculationFailure("w(1, 1.5)");
        assertCalculationFailure("w(1, 2/3)");
        assertCalculationFailure("gcd(2, i)");
        assertCalculationFailure("gcd(2, 2/3)");
        assertCalculationFailure("gcd(1)");
        assertCalculationFailure("gcd(1, 1, 1)");
        assertCalculationFailure("lcm(2, i)");
        assertCalculationFailure("lcm(2, 2/3)");
        assertCalculationFailure("lcm(1)");
        assertCalculationFailure("lcm(1, 1, 1)");
        assertCalculationFailure("pi(i)");
        assertCalculationFailure("pi(0.5)");
        assertCalculationFailure("pi()");
        assertCalculationFailure("pi(1, 1)");
        assertCalculationFailure("random(i)");
        assertCalculationFailure("random(0.5)");
        assertCalculationFailure("random()");
        assertCalculationFailure("random(1, 1)");
        assertCalculationFailure("randomGaussian(i)");
        assertCalculationFailure("randomGaussian(0.5)");
        assertCalculationFailure("randomGaussian()");
        assertCalculationFailure("randomGaussian(1, 1)");
        assertCalculationFailure("add(2)");
        assertCalculationFailure("add(2, 2, 2)");
        assertCalculationFailure("subtract(2)");
        assertCalculationFailure("subtract(2, 2, 2)");
        assertCalculationFailure("multiply(2)");
        assertCalculationFailure("multiply(2, 2, 2)");
        assertCalculationFailure("divide(2)");
        assertCalculationFailure("divide(2, 2, 2)");
        assertCalculationFailure("negate()");
        assertCalculationFailure("negate(2, 2)");
        assertCalculationFailure("mod(2, i)");
        assertCalculationFailure("mod(2)");
        assertCalculationFailure("mod(2, 2, 2)");
        assertCalculationFailure("pow(2)");
        assertCalculationFailure("pow(2, 2, 2)");
        assertCalculationFailure("bogusfunc(5)");
    }

    public static void testArguments()
        throws ParseException
    {
        assertCalculation("1.4142", "sqrt(2.)", "-i", "5");
        assertCalculation("0.5", "1/2", "-p", "-i", "1");
        assertCalculation("0.5", "1.0/2.0", "-p");
        assertCalculation("0.915966", "catalan()", "-p", "-i", "6");
        assertCalculation("2.71828", "e()", "-i", "6");
        assertCalculation("0.577216", "euler()", "-p", "-i", "6");
        assertCalculation("1.28243", "glaisher()", "-i", "6");
        assertCalculation("2.68545", "khinchin()", "-p", "-i", "6");
        assertCalculation("3.14159", "pi()", "-i", "6");
        assertCalculationMatch("0|[1-9]e-1", "random()", "-i", "1");
        assertCalculationMatch("0|-?([1-5]|[1-9]e-1)", "randomGaussian()", "-i", "1");
        assertCalculation("1.5708", "acos(0)", "-p", "-i", "6");
        assertCalculation("1.5708i", "acosh(0)", "-p", "-i", "6");
        assertCalculation("0.355028", "airyAi(0)", "-p", "-i", "6");
        assertCalculation("-0.258819", "airyAiPrime(0)", "-p", "-i", "6");
        assertCalculation("0.614927", "airyBi(0)", "-p", "-i", "6");
        assertCalculation("0.448288", "airyBiPrime(0)", "-p", "-i", "6");
        assertCalculation("-7.09216", "bernoulliB(16, 0)", "-i", "6");
        assertCalculation("1.5708", "ellipticE(0)", "-p", "-i", "6");
        assertCalculation("1.5708", "ellipticK(0)", "-p", "-i", "6");
        assertCalculation("58098.1", "eulerE(15, 0)", "-p", "-i", "6");
    }

    private static final String NEWLINE = System.lineSeparator();
}
