/*
 * MIT License
 *
 * Copyright (c) 2002-2022 Mikko Tommila
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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic calculator implementation.
 * Provides a storage for variables, and maps
 * the elementary operators to function calls.
 *
 * @version 1.9.1
 * @author Mikko Tommila
 */

public abstract class AbstractCalculatorImpl
    implements CalculatorImpl, Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */

    protected AbstractCalculatorImpl()
    {
        this.variables = new HashMap<>();
        this.pretty = false;
    }

    @Override
    public Number negate(Number x)
        throws ParseException
    {
        return function("negate", toList(x));
    }

    @Override
    public Number add(Number x, Number y)
        throws ParseException
    {
        return function("add", toList(x, y));
    }

    @Override
    public Number subtract(Number x, Number y)
        throws ParseException
    {
        return function("subtract", toList(x, y));
    }

    @Override
    public Number multiply(Number x, Number y)
        throws ParseException
    {
        return function("multiply", toList(x, y));
    }

    @Override
    public Number divide(Number x, Number y)
        throws ParseException
    {
        return function("divide", toList(x, y));
    }

    @Override
    public Number mod(Number x, Number y)
        throws ParseException
    {
        return function("mod", toList(x, y));
    }

    @Override
    public Number pow(Number x, Number y)
        throws ParseException
    {
        return function("pow", toList(x, y));
    }

    @Override
    public Number getVariable(String name)
        throws ParseException
    {
        Number variable = this.variables.get(name);
        if (variable == null)
        {
            throw new ParseException("Invalid variable: " + name);
        }
        return variable;
    }

    @Override
    public void setVariable(String name, Number value)
    {
        this.variables.put(name, value);
    }

    @Override
    public void setFormat(boolean pretty)
    {
        this.pretty = pretty;
    }

    @Override
    public void setInputPrecision(Long inputPrecision)
    {
        this.inputPrecision = inputPrecision;
    }

    /**
     * Get the formatting option.
     *
     * @return If a fixed-point or a floating-point notation should be used.
     */

    protected boolean getFormat()
    {
        return this.pretty;
    }

    /**
     * Get the input precision.
     *
     * @return The input precision if a fixed precision is used or <code>null</code> for arbitrary precision.
     */

    protected Long getInputPrecision()
    {
        return this.inputPrecision;
    }

    private static List<Number> toList(Number x)
    {
        List<Number> list = new ArrayList<>();
        list.add(x);
        return list;
    }

    private static List<Number> toList(Number x, Number y)
    {
        List<Number> list = new ArrayList<>();
        list.add(x);
        list.add(y);
        return list;
    }

    private Map<String, Number> variables;
    private boolean pretty;
    private Long inputPrecision;
}
