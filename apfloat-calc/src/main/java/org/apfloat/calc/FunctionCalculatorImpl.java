package org.apfloat.calc;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculator implementation with function support.
 * Provides a mapping mechanism for functions.
 *
 * @version 1.8.0
 * @author Mikko Tommila
 */

public abstract class FunctionCalculatorImpl
    extends AbstractCalculatorImpl
{
    /**
     * Arbitrary function.
     */

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

    protected abstract class FixedFunction
        implements Function
    {
        /**
         * Constructor.
         *
         * @param name The function's name.
         * @param arguments The number of arguments that the function takes.
         */

        protected FixedFunction(String name, int arguments)
        {
            this(name, arguments, arguments);
        }

        /**
         * Constructor.
         *
         * @param name The function's name.
         * @param minArguments The minimum number of arguments that the function takes.
         * @param maxArguments The maximum number of arguments that the function takes.
         */

        protected FixedFunction(String name, int minArguments, int maxArguments)
        {
            this.name = name;
            this.minArguments = minArguments;
            this.maxArguments = maxArguments;
        }

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

        public final Number call(List<Number> arguments)
            throws ParseException
        {
            validate (arguments);
            return promote(call(getFunctions(arguments), arguments));
        }

        /**
         * Call the function.
         *
         * @param functions The function implementations.
         * @param arguments The function's argument(s).
         *
         * @return The function's value.
         */

        protected abstract Number call(Functions functions, List<Number> arguments);

        private String name;
        private int minArguments;
        private int maxArguments;
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
        public Number exp(Number x);
        public Number factorial(Number x);
        public Number floor(Number x);
        public Number frac(Number x);
        public Number log(Number x);
        public Number log(Number x, Number y);
        public Number pi(Number x);
        public Number round(Number x, Number y);
        public Number sin(Number x);
        public Number sinh(Number x);
        public Number sqrt(Number x);
        public Number tan(Number x);
        public Number tanh(Number x);
        public Number truncate(Number x);
        public Number toDegrees(Number x);
        public Number toRadians(Number x);

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
        this.functions = new HashMap<String, Function>();

        setFunction("negate", new FixedFunction("negate", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.negate(arguments.get(0)); } });
        setFunction("add", new FixedFunction("add", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.add(arguments.get(0), arguments.get(1)); } });
        setFunction("subtract", new FixedFunction("subtract", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.subtract(arguments.get(0), arguments.get(1)); } });
        setFunction("multiply", new FixedFunction("multiply", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.multiply(arguments.get(0), arguments.get(1)); } });
        setFunction("divide", new FixedFunction("divide", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.divide(arguments.get(0), arguments.get(1)); } });
        setFunction("mod", new FixedFunction("mod", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.mod(arguments.get(0), arguments.get(1)); } });
        setFunction("pow", new FixedFunction("pow", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.pow(arguments.get(0), arguments.get(1)); } });

        setFunction("abs", new FixedFunction("abs", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.abs(arguments.get(0)); } });
        setFunction("acos", new FixedFunction("acos", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.acos(arguments.get(0)); } });
        setFunction("acosh", new FixedFunction("acosh", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.acosh(arguments.get(0)); } });
        setFunction("asin", new FixedFunction("asin", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.asin(arguments.get(0)); } });
        setFunction("asinh", new FixedFunction("asinh", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.asinh(arguments.get(0)); } });
        setFunction("atan", new FixedFunction("atan", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.atan(arguments.get(0)); } });
        setFunction("atanh", new FixedFunction("atanh", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.atanh(arguments.get(0)); } });
        setFunction("cbrt", new FixedFunction("cbrt", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.cbrt(arguments.get(0)); } });
        setFunction("ceil", new FixedFunction("ceil", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.ceil(arguments.get(0)); } });
        setFunction("cos", new FixedFunction("cos", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.cos(arguments.get(0)); } });
        setFunction("cosh", new FixedFunction("cosh", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.cosh(arguments.get(0)); } });
        setFunction("exp", new FixedFunction("exp", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.exp(arguments.get(0)); } });
        setFunction("factorial", new FixedFunction("factorial", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.factorial(arguments.get(0)); } });
        setFunction("floor", new FixedFunction("floor", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.floor(arguments.get(0)); } });
        setFunction("frac", new FixedFunction("frac", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.frac(arguments.get(0)); } });
        setFunction("log", new FixedFunction("log", 1, 2) { protected Number call(Functions functions, List<Number> arguments) { return (arguments.size() == 1 ? functions.log(arguments.get(0)) : functions.log(arguments.get(0), arguments.get(1))); } });
        setFunction("pi", new FixedFunction("pi", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.pi(arguments.get(0)); } });
        setFunction("round", new FixedFunction("round", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.round(arguments.get(0), arguments.get(1)); } });
        setFunction("sin", new FixedFunction("sin", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.sin(arguments.get(0)); } });
        setFunction("sinh", new FixedFunction("sinh", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.sinh(arguments.get(0)); } });
        setFunction("sqrt", new FixedFunction("sqrt", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.sqrt(arguments.get(0)); } });
        setFunction("tan", new FixedFunction("tan", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.tan(arguments.get(0)); } });
        setFunction("tanh", new FixedFunction("tanh", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.tanh(arguments.get(0)); } });
        setFunction("truncate", new FixedFunction("truncate", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.truncate(arguments.get(0)); } });
        setFunction("toDegrees", new FixedFunction("toDegrees", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.toDegrees(arguments.get(0)); } });
        setFunction("toRadians", new FixedFunction("toRadians", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.toRadians(arguments.get(0)); } });

        setFunction("arg", new FixedFunction("arg", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.arg(arguments.get(0)); } });
        setFunction("conj", new FixedFunction("conj", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.conj(arguments.get(0)); } });
        setFunction("imag", new FixedFunction("imag", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.imag(arguments.get(0)); } });
        setFunction("real", new FixedFunction("real", 1) { protected Number call(Functions functions, List<Number> arguments) { return functions.real(arguments.get(0)); } });

        setFunction("agm", new FixedFunction("agm", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.agm(arguments.get(0), arguments.get(1)); } });
        setFunction("w", new FixedFunction("w", 1, 2) { protected Number call(Functions functions, List<Number> arguments) { return (arguments.size() == 1 ? functions.w(arguments.get(0)) : functions.w(arguments.get(0), arguments.get(1))); } });
        setFunction("atan2", new FixedFunction("atan2", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.atan2(arguments.get(0), arguments.get(1)); } });
        setFunction("copySign", new FixedFunction("copySign", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.copySign(arguments.get(0), arguments.get(1)); } });
        setFunction("fmod", new FixedFunction("fmod", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.fmod(arguments.get(0), arguments.get(1)); } });
        setFunction("gcd", new FixedFunction("gcd", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.gcd(arguments.get(0), arguments.get(1)); } });
        setFunction("hypot", new FixedFunction("hypot", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.hypot(arguments.get(0), arguments.get(1)); } });
        setFunction("inverseRoot", new FixedFunction("inverseRoot", 2, 3) { protected Number call(Functions functions, List<Number> arguments) { return (arguments.size() == 2 ? functions.inverseRoot(arguments.get(0), arguments.get(1)) : functions.inverseRoot(arguments.get(0), arguments.get(1), arguments.get(2))); } });
        setFunction("lcm", new FixedFunction("lcm", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.lcm(arguments.get(0), arguments.get(1)); } });
        setFunction("root", new FixedFunction("root", 2, 3) { protected Number call(Functions functions, List<Number> arguments) { return (arguments.size() == 2 ? functions.root(arguments.get(0), arguments.get(1)) : functions.root(arguments.get(0), arguments.get(1), arguments.get(2))); } });
        setFunction("scale", new FixedFunction("scale", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.scale(arguments.get(0), arguments.get(1)); } });
        setFunction("n", new FixedFunction("precision", 2) { protected Number call(Functions functions, List<Number> arguments) { return functions.precision(arguments.get(0), arguments.get(1)); } });
    }

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
