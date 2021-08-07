/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculator implementation with function support.
 * Provides a mapping mechanism for functions.
 *
 * @version 1.11.0
 * @author Mikko Tommila
 */

public abstract class FunctionCalculatorImpl
    extends AbstractCalculatorImpl
{
    private static final long serialVersionUID = 1L;

    /**
     * Arbitrary function.
     */

    @FunctionalInterface
    protected static interface Function
        extends Serializable
    {
        /**
         * Call the function.
         *
         * @param arguments The function's arguments.
         *
         * @return The function's value.
         *
         * @exception ParseException In case the arguments are invalid.
         */

        public Number call(List<Number> arguments)
            throws ParseException;
    }

    /**
     * Function taking a fixed number of arguments.
     */

    protected class FixedFunction
        implements Function
    {
        private static final long serialVersionUID = 1L;

        /**
         * Validate the number of arguments.
         *
         * @param arguments The function's arguments.
         *
         * @exception ParseException In case of incorrect number of arguments.
         */

        protected void validate(List<Number> arguments)
            throws ParseException
        {
            if (this.minArguments == this.maxArguments && arguments.size() != this.minArguments)
            {
                throw new ParseException("Function " + this.name + " takes " + this.minArguments + " argument" + (this.minArguments == 1 ? "" : "s") + ", not " + arguments.size());
            }
            if (arguments.size() < this.minArguments || arguments.size() > this.maxArguments)
            {
                throw new ParseException("Function " + this.name + " takes " + this.minArguments + " to " + this.maxArguments + " arguments, not " + arguments.size());
            }
        }

        @Override
        public final Number call(List<Number> arguments)
            throws ParseException
        {
            validate(arguments);
            return promote(handler.call(getFunctions(arguments), arguments));
        }

        private String name;
        private int minArguments;
        private int maxArguments;
        private FixedFunctionHandler handler;
    }

    /**
     * Handler for FixedFunction.
     */

    @FunctionalInterface
    protected static interface FixedFunctionHandler
        extends Serializable
    {
        /**
         * Call the function.
         *
         * @param functions The function implementations.
         * @param arguments The function's argument(s).
         *
         * @return The function's value.
         */

        public Number call(Functions functions, List<Number> arguments);
    }

    /**
     * Function implementations.
     */

    protected static interface Functions
    {
        public Number negate(Number x);
        public Number add(Number x, Number y);
        public Number subtract(Number x, Number y);
        public Number multiply(Number x, Number y);
        public Number divide(Number x, Number y);
        public Number mod(Number x, Number y);
        public Number pow(Number x, Number y);

        public Number arg(Number x);
        public Number conj(Number x);
        public Number imag(Number x);
        public Number real(Number x);

        public Number abs(Number x);
        public Number acos(Number x);
        public Number acosh(Number x);
        public Number asin(Number x);
        public Number asinh(Number x);
        public Number atan(Number x);
        public Number atanh(Number x);
        public Number cbrt(Number x);
        public Number ceil(Number x);
        public Number cos(Number x);
        public Number cosh(Number x);
        public Number euler(Number x);
        public Number exp(Number x);
        public Number factorial(Number x);
        public Number floor(Number x);
        public Number frac(Number x);
        public Number gamma(Number x);
        public Number gamma(Number x, Number y);
        public Number gamma(Number x, Number y, Number z);
        public Number log(Number x);
        public Number log(Number x, Number y);
        public Number max(Number x, Number y);
        public Number min(Number x, Number y);
        public Number nextAfter(Number x, Number y);
        public Number nextDown(Number x);
        public Number nextUp(Number x);
        public Number pi(Number x);
        public Number random(Number x);
        public Number randomGaussian(Number x);
        public Number round(Number x, Number y);
        public Number sin(Number x);
        public Number sinh(Number x);
        public Number sqrt(Number x);
        public Number tan(Number x);
        public Number tanh(Number x);
        public Number truncate(Number x);
        public Number toDegrees(Number x);
        public Number toRadians(Number x);
        public Number ulp(Number x);
        public Number zeta(Number x);

        public Number agm(Number x, Number y);
        public Number w(Number x);
        public Number w(Number x, Number y);
        public Number atan2(Number x, Number y);
        public Number copySign(Number x, Number y);
        public Number fmod(Number x, Number y);
        public Number gcd(Number x, Number y);
        public Number hypot(Number x, Number y);
        public Number inverseRoot(Number x, Number y);
        public Number inverseRoot(Number x, Number y, Number z);
        public Number lcm(Number x, Number y);
        public Number root(Number x, Number y);
        public Number root(Number x, Number y, Number z);
        public Number scale(Number x, Number y);
        public Number precision(Number x, Number y);
    }

    /**
     * Default constructor.
     */

    protected FunctionCalculatorImpl()
    {
        this.functions = new HashMap<>();

        setFunction("negate", fixedFunction("negate", 1, (functions, arguments) -> functions.negate(arguments.get(0))));
        setFunction("add", fixedFunction("add", 2, (functions, arguments) -> functions.add(arguments.get(0), arguments.get(1))));
        setFunction("subtract", fixedFunction("subtract", 2, (functions, arguments) -> functions.subtract(arguments.get(0), arguments.get(1))));
        setFunction("multiply", fixedFunction("multiply", 2, (functions, arguments) -> functions.multiply(arguments.get(0), arguments.get(1))));
        setFunction("divide", fixedFunction("divide", 2, (functions, arguments) -> functions.divide(arguments.get(0), arguments.get(1))));
        setFunction("mod", fixedFunction("mod", 2, (functions, arguments) -> functions.mod(arguments.get(0), arguments.get(1))));
        setFunction("pow", fixedFunction("pow", 2, (functions, arguments) -> functions.pow(arguments.get(0), arguments.get(1))));

        setFunction("abs", fixedFunction("abs", 1, (functions, arguments) -> functions.abs(arguments.get(0))));
        setFunction("acos", fixedFunction("acos", 1, (functions, arguments) -> functions.acos(arguments.get(0))));
        setFunction("acosh", fixedFunction("acosh", 1, (functions, arguments) -> functions.acosh(arguments.get(0))));
        setFunction("asin", fixedFunction("asin", 1, (functions, arguments) -> functions.asin(arguments.get(0))));
        setFunction("asinh", fixedFunction("asinh", 1, (functions, arguments) -> functions.asinh(arguments.get(0))));
        setFunction("atan", fixedFunction("atan", 1, (functions, arguments) -> functions.atan(arguments.get(0))));
        setFunction("atanh", fixedFunction("atanh", 1, (functions, arguments) -> functions.atanh(arguments.get(0))));
        setFunction("cbrt", fixedFunction("cbrt", 1, (functions, arguments) -> functions.cbrt(arguments.get(0))));
        setFunction("ceil", fixedFunction("ceil", 1, (functions, arguments) -> functions.ceil(arguments.get(0))));
        setFunction("cos", fixedFunction("cos", 1, (functions, arguments) -> functions.cos(arguments.get(0))));
        setFunction("cosh", fixedFunction("cosh", 1, (functions, arguments) -> functions.cosh(arguments.get(0))));
        setFunction("euler", fixedFunction("euler", 1, (functions, arguments) -> functions.euler(arguments.get(0))));
        setFunction("exp", fixedFunction("exp", 1, (functions, arguments) -> functions.exp(arguments.get(0))));
        setFunction("factorial", fixedFunction("factorial", 1, (functions, arguments) -> functions.factorial(arguments.get(0))));
        setFunction("floor", fixedFunction("floor", 1, (functions, arguments) -> functions.floor(arguments.get(0))));
        setFunction("frac", fixedFunction("frac", 1, (functions, arguments) -> functions.frac(arguments.get(0))));
        setFunction("gamma", fixedFunction("gamma", 1, 3, (functions, arguments) -> (arguments.size() == 1 ? functions.gamma(arguments.get(0)) : arguments.size() == 2 ? functions.gamma(arguments.get(0), arguments.get(1)): functions.gamma(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("log", fixedFunction("log", 1, 2, (functions, arguments) -> (arguments.size() == 1 ? functions.log(arguments.get(0)) : functions.log(arguments.get(0), arguments.get(1)))));
        setFunction("max", fixedFunction("max", 2, (functions, arguments) -> functions.max(arguments.get(0), arguments.get(1))));
        setFunction("min", fixedFunction("min", 2, (functions, arguments) -> functions.min(arguments.get(0), arguments.get(1))));
        setFunction("nextAfter", fixedFunction("nextAfter", 2, (functions, arguments) -> functions.nextAfter(arguments.get(0), arguments.get(1))));
        setFunction("nextDown", fixedFunction("nextDown", 1, (functions, arguments) -> functions.nextDown(arguments.get(0))));
        setFunction("nextUp", fixedFunction("nextUp", 1, (functions, arguments) -> functions.nextUp(arguments.get(0))));
        setFunction("pi", fixedFunction("pi", 1, (functions, arguments) -> functions.pi(arguments.get(0))));
        setFunction("random", fixedFunction("random", 1, (functions, arguments) -> functions.random(arguments.get(0))));
        setFunction("randomGaussian", fixedFunction("randomGaussian", 1, (functions, arguments) -> functions.randomGaussian(arguments.get(0))));
        setFunction("round", fixedFunction("round", 2, (functions, arguments) -> functions.round(arguments.get(0), arguments.get(1))));
        setFunction("sin", fixedFunction("sin", 1, (functions, arguments) -> functions.sin(arguments.get(0))));
        setFunction("sinh", fixedFunction("sinh", 1, (functions, arguments) -> functions.sinh(arguments.get(0))));
        setFunction("sqrt", fixedFunction("sqrt", 1, (functions, arguments) -> functions.sqrt(arguments.get(0))));
        setFunction("tan", fixedFunction("tan", 1, (functions, arguments) -> functions.tan(arguments.get(0))));
        setFunction("tanh", fixedFunction("tanh", 1, (functions, arguments) -> functions.tanh(arguments.get(0))));
        setFunction("truncate", fixedFunction("truncate", 1, (functions, arguments) -> functions.truncate(arguments.get(0))));
        setFunction("toDegrees", fixedFunction("toDegrees", 1, (functions, arguments) -> functions.toDegrees(arguments.get(0))));
        setFunction("toRadians", fixedFunction("toRadians", 1, (functions, arguments) -> functions.toRadians(arguments.get(0))));
        setFunction("ulp", fixedFunction("ulp", 1, (functions, arguments) -> functions.ulp(arguments.get(0))));
        setFunction("zeta", fixedFunction("zeta", 1, (functions, arguments) -> functions.zeta(arguments.get(0))));

        setFunction("arg", fixedFunction("arg", 1, (functions, arguments) -> functions.arg(arguments.get(0))));
        setFunction("conj", fixedFunction("conj", 1, (functions, arguments) -> functions.conj(arguments.get(0))));
        setFunction("imag", fixedFunction("imag", 1, (functions, arguments) -> functions.imag(arguments.get(0))));
        setFunction("real", fixedFunction("real", 1, (functions, arguments) -> functions.real(arguments.get(0))));

        setFunction("agm", fixedFunction("agm", 2, (functions, arguments) -> functions.agm(arguments.get(0), arguments.get(1))));
        setFunction("w", fixedFunction("w", 1, 2, (functions, arguments) -> (arguments.size() == 1 ? functions.w(arguments.get(0)) : functions.w(arguments.get(0), arguments.get(1)))));
        setFunction("atan2", fixedFunction("atan2", 2, (functions, arguments) -> functions.atan2(arguments.get(0), arguments.get(1))));
        setFunction("copySign", fixedFunction("copySign", 2, (functions, arguments) -> functions.copySign(arguments.get(0), arguments.get(1))));
        setFunction("fmod", fixedFunction("fmod", 2, (functions, arguments) -> functions.fmod(arguments.get(0), arguments.get(1))));
        setFunction("gcd", fixedFunction("gcd", 2, (functions, arguments) -> functions.gcd(arguments.get(0), arguments.get(1))));
        setFunction("hypot", fixedFunction("hypot", 2, (functions, arguments) -> functions.hypot(arguments.get(0), arguments.get(1))));
        setFunction("inverseRoot", fixedFunction("inverseRoot", 2, 3, (functions, arguments) -> (arguments.size() == 2 ? functions.inverseRoot(arguments.get(0), arguments.get(1)) : functions.inverseRoot(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("lcm", fixedFunction("lcm", 2, (functions, arguments) -> functions.lcm(arguments.get(0), arguments.get(1))));
        setFunction("root", fixedFunction("root", 2, 3, (functions, arguments) -> (arguments.size() == 2 ? functions.root(arguments.get(0), arguments.get(1)) : functions.root(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("scale", fixedFunction("scale", 2, (functions, arguments) -> functions.scale(arguments.get(0), arguments.get(1))));
        setFunction("n", fixedFunction("precision", 2, (functions, arguments) -> functions.precision(arguments.get(0), arguments.get(1))));
    }

    @Override
    public Number function(String name, List<Number> arguments)
        throws ParseException
    {
        Function function = functions.get(name);
        if (function == null)
        {
            throw new ParseException("Invalid function: " + name);
        }
        return function.call(arguments);
    }

    private Functions getFunctions(List<Number> arguments)
    {
        Functions functions = null;
        for (Number argument : arguments)
        {
            Functions functions2 = getFunctions(argument);
            functions = (functions != null && functions.getClass().isAssignableFrom(functions2.getClass()) ? functions : functions2);
        }
        return functions;
    }

    /**
     * Factory method.
     *
     * @param name The function's name.
     * @param arguments The number of arguments that the function takes.
     * @param handler The handler of the function.
     *
     * @return The function.
     */

    protected FixedFunction fixedFunction(String name, int arguments, FixedFunctionHandler handler)
    {
        return fixedFunction(name, arguments, arguments, handler);
    }

    /**
     * Factory method.
     *
     * @param name The function's name.
     * @param minArguments The minimum number of arguments that the function takes.
     * @param maxArguments The maximum number of arguments that the function takes.
     * @param handler The handler of the function.
     *
     * @return The function.
     */

    protected FixedFunction fixedFunction(String name, int minArguments, int maxArguments, FixedFunctionHandler handler)
    {
        FixedFunction fixedFunction = new FixedFunction();
        fixedFunction.name = name;
        fixedFunction.minArguments = minArguments;
        fixedFunction.maxArguments = maxArguments;
        fixedFunction.handler = handler;

        return fixedFunction;
    }

    /**
     * Define a function.
     *
     * @param name The function name.
     * @param function The function.
     */

    protected void setFunction(String name, Function function)
    {
        this.functions.put(name, function);
    }

    /**
     * Get the function implementations.
     *
     * @param x The number to use as the function argument.
     *
     * @return The function implementations.
     */

    protected abstract Functions getFunctions(Number x);

    /**
     * Promote a number to a more specific class.
     *
     * @param x The argument.
     *
     * @return The argument, possibly converted to a more specific subclass.
     */

    protected abstract Number promote(Number x);

    private Map<String, Function> functions;
}
