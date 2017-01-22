package org.apfloat.calc;

import java.util.List;

/**
 * Calculator implementation interface.
 * The calculator parser uses this interface to perform the actual calculations.
 *
 * @version 1.2
 * @author Mikko Tommila
 */

public interface CalculatorImpl
{
    /**
     * Negative value.
     *
     * @param x The argument.
     *
     * @return <code>-x</code>
     *
     * @exception ParseException In case of invalid argument.
     */

    public Number negate(Number x)
        throws ParseException;

    /**
     * Addition.
     *
     * @param x First argument.
     * @param y Second argument.
     *
     * @return <code>x + y</code>
     *
     * @exception ParseException In case of invalid arguments.
     */

    public Number add(Number x, Number y)
        throws ParseException;

    /**
     * Subtraction.
     *
     * @param x First argument.
     * @param y Second argument.
     *
     * @return <code>x - y</code>
     *
     * @exception ParseException In case of invalid arguments.
     */

    public Number subtract(Number x, Number y)
        throws ParseException;

    /**
     * Multiplication.
     *
     * @param x First argument.
     * @param y Second argument.
     *
     * @return <code>x * y</code>
     *
     * @exception ParseException In case of invalid arguments.
     */

    public Number multiply(Number x, Number y)
        throws ParseException;

    /**
     * Division.
     *
     * @param x First argument.
     * @param y Second argument.
     *
     * @return <code>x / y</code>
     *
     * @exception ParseException In case of invalid arguments.
     */

    public Number divide(Number x, Number y)
        throws ParseException;

    /**
     * Remainder.
     *
     * @param x First argument.
     * @param y Second argument.
     *
     * @return <code>x % y</code>
     *
     * @exception ParseException In case of invalid arguments.
     */

    public Number mod(Number x, Number y)
        throws ParseException;

    /**
     * Power.
     *
     * @param x First argument.
     * @param y Second argument.
     *
     * @return <code>x<sup>y</sup></code>
     *
     * @exception ParseException In case of invalid arguments.
     */

    public Number pow(Number x, Number y)
        throws ParseException;

    /**
     * Arbitrary function.
     *
     * @param name Name of the function.
     * @param arguments Function arguments.
     *
     * @return Function value.
     *
     * @exception ParseException In case of invalid arguments.
     */

    public Number function(String name, List<Number> arguments)
        throws ParseException;

    /**
     * Parse a string to an integer number.
     *
     * @param value The string to parse.
     *
     * @return The number.
     *
     * @exception ParseException In case of invalid number.
     */

    public Number parseInteger(String value)
        throws ParseException;

    /**
     * Parse a string to a floating-point number.
     *
     * @param value The string to parse.
     *
     * @return The number.
     *
     * @exception ParseException In case of invalid number.
     */

    public Number parseDecimal(String value)
        throws ParseException;

    /**
     * Get a variable.
     *
     * @param name Name of the variable.
     *
     * @return Value of the variable, or <code>null</code> if the variable is not defined.
     *
     * @exception ParseException In case of invalid argument.
     */

    public Number getVariable(String name)
        throws ParseException;

    /**
     * Set a variable.
     *
     * @param name Name of the variable.
     * @param value Value of the variable.
     *
     * @exception ParseException In case of invalid arguments.
     */

    public void setVariable(String name, Number value)
        throws ParseException;

    /**
     * Set the formatting option.
     *
     * @param pretty If a fixed-point or a floating-point notation should be used.
     */

    public void setFormat(boolean pretty);

    /**
     * Convert a number to a String. The current formatting option is used.
     *
     * @param x The number.
     *
     * @return The String.
     */

    public String format(Number x);
}
