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
package org.apfloat.calc;

/**
 * Command-line calculator.
 *
 * @version 1.11.0
 * @author Mikko Tommila
 */

public class Calculator
{
    private Calculator()
    {
    }

    /**
     * Command-line entry point.
     *
     * @param args Command-line parameters.
     *
     * @exception ParseException In case of invalid input
     */

    public static void main(String args[])
        throws ParseException
    {
        boolean pretty = false;
        Long inputPrecision = null;
        for (int i = 0; i < args.length; i++)
        {
            switch (args[i])
            {
                case "-p":
                    pretty = true;
                    break;
                case "-i":
                    try
                    {
                        i++;
                        inputPrecision = Long.valueOf(args[i]);
                        break;
                    }
                    catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
                    {
                        // Fall through to usage
                    }
                default:
                    System.err.println("Usage: calculator [-p] [-i inputPrecision]");
                    System.exit(1);
            }
        }
        
        CalculatorImpl calculatorImpl = new ApfloatCalculatorImpl();
        calculatorImpl.setFormat(pretty);
        calculatorImpl.setInputPrecision(inputPrecision);
        CalculatorParser calculatorParser = new CalculatorParser(System.in, System.out, calculatorImpl);
        while (calculatorParser.parseOneLine())
        {
            // Loop until input ends or exception is thrown
        }
    }
}
