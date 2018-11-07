/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
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
 * @version 1.9.0
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

    /**
     * Get the formatting option.
     *
     * @return If a fixed-point or a floating-point notation should be used.
     */

    protected boolean getFormat()
    {
        return this.pretty;
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
}
