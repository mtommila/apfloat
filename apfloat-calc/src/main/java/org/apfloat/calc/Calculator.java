/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
