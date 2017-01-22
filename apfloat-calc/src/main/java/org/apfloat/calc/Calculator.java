package org.apfloat.calc;

/**
 * Command-line calculator.
 *
 * @version 1.2
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
        CalculatorParser calculatorParser = new CalculatorParser(System.in, System.out, new ApfloatCalculatorImpl());
        while (calculatorParser.parseOneLine())
        {
            // Loop until input ends or exception is thrown
        }
    }
}
