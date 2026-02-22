/*
 * MIT License
 *
 * Copyright (c) 2002-2026 Mikko Tommila
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
 * @version 1.16.0
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
        public Number airyAi(Number x);
        public Number airyAiPrime(Number x);
        public Number airyBi(Number x);
        public Number airyBiPrime(Number x);
        public Number angerJ(Number x, Number y);
        public Number asin(Number x);
        public Number asinh(Number x);
        public Number atan(Number x);
        public Number atanh(Number x);
        public Number barnesG(Number x);
        public Number bernoulli(Number x);
        public Number bernoulliB(Number x, Number y);
        public Number besselI(Number x, Number y);
        public Number besselJ(Number x, Number y);
        public Number besselK(Number x, Number y);
        public Number besselY(Number x, Number y);
        public Number beta(Number a, Number b);
        public Number beta(Number x, Number a, Number b);
        public Number beta(Number x1, Number x2, Number a, Number b);
        public Number binomial(Number x, Number y);
        public Number catalan(Number x);
        public Number cbrt(Number x);
        public Number ceil(Number x);
        public Number chebyshevT(Number x, Number y);
        public Number chebyshevU(Number x, Number y);
        public Number clausenC(Number x, Number y);
        public Number clausenCl(Number x, Number y);
        public Number clausenS(Number x, Number y);
        public Number clausenSl(Number x, Number y);
        public Number cos(Number x);
        public Number cosIntegral(Number x);
        public Number cosh(Number x);
        public Number coshIntegral(Number x);
        public Number dawsonF(Number x);
        public Number digamma(Number x);
        public Number doubleFactorial(Number x);
        public Number e(Number x);
        public Number ellipticE(Number x);
        public Number ellipticK(Number x);
        public Number erf(Number x);
        public Number erfc(Number x);
        public Number erfi(Number x);
        public Number euler(Number x);
        public Number eulerE(Number x, Number y);
        public Number exp(Number x);
        public Number expIntegralE(Number x, Number y);
        public Number expIntegralEi(Number x);
        public Number factorial(Number x);
        public Number fibonacci(Number x, Number y);
        public Number floor(Number x);
        public Number frac(Number x);
        public Number fresnelC(Number x);
        public Number fresnelS(Number x);
        public Number gamma(Number x);
        public Number gamma(Number x, Number y);
        public Number gamma(Number x, Number y, Number z);
        public Number gegenbauerC(Number x, Number y);
        public Number gegenbauerC(Number x, Number y, Number z);
        public Number harmonicNumber(Number x);
        public Number harmonicNumber(Number x, Number y);
        public Number hermiteH(Number x, Number y);
        public Number hypergeometric0F1(Number a, Number z);
        public Number hypergeometric0F1Regularized(Number a, Number z);
        public Number hypergeometric1F1(Number a, Number b, Number z);
        public Number hypergeometric1F1Regularized(Number a, Number b, Number z);
        public Number hypergeometric2F1(Number a, Number b, Number c, Number z);
        public Number hypergeometric2F1Regularized(Number a, Number b, Number c, Number z);
        public Number hypergeometricU(Number a, Number b, Number z);
        public Number inverseErf(Number x);
        public Number inverseErfc(Number x);
        public Number jacobiP(Number x, Number y, Number z, Number w);
        public Number glaisher(Number x);
        public Number khinchin(Number x);
        public Number laguerreL(Number x, Number y);
        public Number laguerreL(Number x, Number y, Number z);
        public Number legendreP(Number x, Number y);
        public Number legendreP(Number x, Number y, Number z);
        public Number legendreQ(Number x, Number y);
        public Number legendreQ(Number x, Number y, Number z);
        public Number log(Number x);
        public Number log(Number x, Number y);
        public Number logBarnesG(Number x);
        public Number logGamma(Number x);
        public Number logIntegral(Number x);
        public Number logisticSigmoid(Number x);
        public Number max(Number x, Number y);
        public Number min(Number x, Number y);
        public Number nextAfter(Number x, Number y);
        public Number nextDown(Number x);
        public Number nextUp(Number x);
        public Number pi(Number x);
        public Number pochhammer(Number x, Number y);
        public Number polygamma(Number x, Number y);
        public Number polylog(Number x, Number y);
        public Number random(Number x);
        public Number randomGaussian(Number x);
        @Deprecated
        public Number round(Number x, Number y);
        public Number roundToPrecision(Number x, Number y);
        public Number roundToInteger(Number x);
        public Number roundToPlaces(Number x, Number y);
        public Number roundToMultiple(Number x, Number y);
        public Number sin(Number x);
        public Number sinc(Number x);
        public Number sinIntegral(Number x);
        public Number sinh(Number x);
        public Number sinhIntegral(Number x);
        public Number sphericalHarmonicY(Number x, Number y, Number z, Number w);
        public Number sqrt(Number x);
        public Number struveH(Number x, Number y);
        public Number struveL(Number x, Number y);
        public Number tan(Number x);
        public Number tanh(Number x);
        public Number truncate(Number x);
        public Number toDegrees(Number x);
        public Number toRadians(Number x);
        public Number ulp(Number x);
        public Number weberE(Number x, Number y);
        public Number zeta(Number x);
        public Number zeta(Number x, Number y);

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
        setFunction("airyAi", fixedFunction("airyAi", 1, (functions, arguments) -> functions.airyAi(arguments.get(0))));
        setFunction("airyAiPrime", fixedFunction("airyAiPrime", 1, (functions, arguments) -> functions.airyAiPrime(arguments.get(0))));
        setFunction("airyBi", fixedFunction("airyBi", 1, (functions, arguments) -> functions.airyBi(arguments.get(0))));
        setFunction("airyBiPrime", fixedFunction("airyBiPrime", 1, (functions, arguments) -> functions.airyBiPrime(arguments.get(0))));
        setFunction("angerJ", fixedFunction("angerJ", 2, (functions, arguments) -> functions.angerJ(arguments.get(0), arguments.get(1))));
        setFunction("asin", fixedFunction("asin", 1, (functions, arguments) -> functions.asin(arguments.get(0))));
        setFunction("asinh", fixedFunction("asinh", 1, (functions, arguments) -> functions.asinh(arguments.get(0))));
        setFunction("atan", fixedFunction("atan", 1, (functions, arguments) -> functions.atan(arguments.get(0))));
        setFunction("atanh", fixedFunction("atanh", 1, (functions, arguments) -> functions.atanh(arguments.get(0))));
        setFunction("barnesG", fixedFunction("barnesG", 1, (functions, arguments) -> functions.barnesG(arguments.get(0))));
        setFunction("bernoulli", fixedFunction("bernoulli", 1, (functions, arguments) -> functions.bernoulli(arguments.get(0))));
        setFunction("bernoulliB", fixedFunction("bernoulliB", 2, (functions, arguments) -> functions.bernoulliB(arguments.get(0), arguments.get(1))));
        setFunction("besselI", fixedFunction("besselI", 2, (functions, arguments) -> functions.besselI(arguments.get(0), arguments.get(1))));
        setFunction("besselJ", fixedFunction("besselJ", 2, (functions, arguments) -> functions.besselJ(arguments.get(0), arguments.get(1))));
        setFunction("besselK", fixedFunction("besselK", 2, (functions, arguments) -> functions.besselK(arguments.get(0), arguments.get(1))));
        setFunction("besselY", fixedFunction("besselY", 2, (functions, arguments) -> functions.besselY(arguments.get(0), arguments.get(1))));
        setFunction("beta", fixedFunction("beta", 2, 4, (functions, arguments) -> (arguments.size() == 2 ? functions.beta(arguments.get(0), arguments.get(1)) : arguments.size() == 3 ? functions.beta(arguments.get(0), arguments.get(1), arguments.get(2)): functions.beta(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3)))));
        setFunction("binomial", fixedFunction("binomial", 2, (functions, arguments) -> functions.binomial(arguments.get(0), arguments.get(1))));
        setFunction("catalan", fixedFunction("catalan", 0, 1, (functions, arguments) -> functions.catalan(argumentOrInputPrecision(arguments))));
        setFunction("cbrt", fixedFunction("cbrt", 1, (functions, arguments) -> functions.cbrt(arguments.get(0))));
        setFunction("ceil", fixedFunction("ceil", 1, (functions, arguments) -> functions.ceil(arguments.get(0))));
        setFunction("clausenC", fixedFunction("clausenC", 2, (functions, arguments) -> functions.clausenC(arguments.get(0), arguments.get(1))));
        setFunction("clausenCl", fixedFunction("clausenCl", 2, (functions, arguments) -> functions.clausenCl(arguments.get(0), arguments.get(1))));
        setFunction("clausenS", fixedFunction("clausenS", 2, (functions, arguments) -> functions.clausenS(arguments.get(0), arguments.get(1))));
        setFunction("clausenSl", fixedFunction("clausenSl", 2, (functions, arguments) -> functions.clausenSl(arguments.get(0), arguments.get(1))));
        setFunction("chebyshevT", fixedFunction("chebyshevT", 2, (functions, arguments) -> functions.chebyshevT(arguments.get(0), arguments.get(1))));
        setFunction("chebyshevU", fixedFunction("chebyshevU", 2, (functions, arguments) -> functions.chebyshevU(arguments.get(0), arguments.get(1))));
        setFunction("cos", fixedFunction("cos", 1, (functions, arguments) -> functions.cos(arguments.get(0))));
        setFunction("cosIntegral", fixedFunction("cosIntegral", 1, (functions, arguments) -> functions.cosIntegral(arguments.get(0))));
        setFunction("cosh", fixedFunction("cosh", 1, (functions, arguments) -> functions.cosh(arguments.get(0))));
        setFunction("coshIntegral", fixedFunction("coshIntegral", 1, (functions, arguments) -> functions.coshIntegral(arguments.get(0))));
        setFunction("dawsonF", fixedFunction("dawsonF", 1, (functions, arguments) -> functions.dawsonF(arguments.get(0))));
        setFunction("digamma", fixedFunction("digamma", 1, (functions, arguments) -> functions.digamma(arguments.get(0))));
        setFunction("doubleFactorial", fixedFunction("doubleFactorial", 1, (functions, arguments) -> functions.doubleFactorial(arguments.get(0))));
        setFunction("e", fixedFunction("e", 0, 1, (functions, arguments) -> functions.e(argumentOrInputPrecision(arguments))));
        setFunction("ellipticE", fixedFunction("ellipticE", 1, (functions, arguments) -> functions.ellipticE(arguments.get(0))));
        setFunction("ellipticK", fixedFunction("ellipticK", 1, (functions, arguments) -> functions.ellipticK(arguments.get(0))));
        setFunction("erf", fixedFunction("erf", 1, (functions, arguments) -> functions.erf(arguments.get(0))));
        setFunction("erfc", fixedFunction("erfc", 1, (functions, arguments) -> functions.erfc(arguments.get(0))));
        setFunction("erfi", fixedFunction("erfi", 1, (functions, arguments) -> functions.erfi(arguments.get(0))));
        setFunction("euler", fixedFunction("euler", 0, 1, (functions, arguments) -> functions.euler(argumentOrInputPrecision(arguments))));
        setFunction("eulerE", fixedFunction("eulerE", 2, (functions, arguments) -> functions.eulerE(arguments.get(0), arguments.get(1))));
        setFunction("exp", fixedFunction("exp", 1, (functions, arguments) -> functions.exp(arguments.get(0))));
        setFunction("expIntegralE", fixedFunction("expIntegralE", 2, (functions, arguments) -> functions.expIntegralE(arguments.get(0), arguments.get(1))));
        setFunction("expIntegralEi", fixedFunction("expIntegralEi", 1, (functions, arguments) -> functions.expIntegralEi(arguments.get(0))));
        setFunction("factorial", fixedFunction("factorial", 1, (functions, arguments) -> functions.factorial(arguments.get(0))));
        setFunction("fibonacci", fixedFunction("fibonacci", 2, (functions, arguments) -> functions.fibonacci(arguments.get(0), arguments.get(1))));
        setFunction("floor", fixedFunction("floor", 1, (functions, arguments) -> functions.floor(arguments.get(0))));
        setFunction("frac", fixedFunction("frac", 1, (functions, arguments) -> functions.frac(arguments.get(0))));
        setFunction("fresnelC", fixedFunction("fresnelC", 1, (functions, arguments) -> functions.fresnelC(arguments.get(0))));
        setFunction("fresnelS", fixedFunction("fresnelS", 1, (functions, arguments) -> functions.fresnelS(arguments.get(0))));
        setFunction("gamma", fixedFunction("gamma", 1, 3, (functions, arguments) -> (arguments.size() == 1 ? functions.gamma(arguments.get(0)) : arguments.size() == 2 ? functions.gamma(arguments.get(0), arguments.get(1)): functions.gamma(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("gegenbauerC", fixedFunction("gegenbauerC", 2, 3, (functions, arguments) -> (arguments.size() == 2 ? functions.gegenbauerC(arguments.get(0), arguments.get(1)) : functions.gegenbauerC(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("glaisher", fixedFunction("glaisher", 0, 1, (functions, arguments) -> functions.glaisher(argumentOrInputPrecision(arguments))));
        setFunction("harmonicNumber", fixedFunction("harmonicNumber", 1, 2, (functions, arguments) -> (arguments.size() == 1 ? functions.harmonicNumber(arguments.get(0)) : functions.harmonicNumber(arguments.get(0), arguments.get(1)))));
        setFunction("hermiteH", fixedFunction("hermiteH", 2, (functions, arguments) -> functions.hermiteH(arguments.get(0), arguments.get(1))));
        setFunction("hypergeometric0F1", fixedFunction("hypergeometric0F1", 2, (functions, arguments) -> functions.hypergeometric0F1(arguments.get(0), arguments.get(1))));
        setFunction("hypergeometric0F1Regularized", fixedFunction("hypergeometric0F1Regularized", 2, (functions, arguments) -> functions.hypergeometric0F1Regularized(arguments.get(0), arguments.get(1))));
        setFunction("hypergeometric1F1", fixedFunction("hypergeometric1F1", 3, (functions, arguments) -> functions.hypergeometric1F1(arguments.get(0), arguments.get(1), arguments.get(2))));
        setFunction("hypergeometric1F1Regularized", fixedFunction("hypergeometric1F1Regularized", 3, (functions, arguments) -> functions.hypergeometric1F1Regularized(arguments.get(0), arguments.get(1), arguments.get(2))));
        setFunction("hypergeometric2F1", fixedFunction("hypergeometric2F1", 4, (functions, arguments) -> functions.hypergeometric2F1(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3))));
        setFunction("hypergeometric2F1Regularized", fixedFunction("hypergeometric2F1Regularized", 4, (functions, arguments) -> functions.hypergeometric2F1Regularized(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3))));
        setFunction("hypergeometricU", fixedFunction("hypergeometricU", 3, (functions, arguments) -> functions.hypergeometricU(arguments.get(0), arguments.get(1), arguments.get(2))));
        setFunction("inverseErf", fixedFunction("inverseErf", 1, (functions, arguments) -> functions.inverseErf(arguments.get(0))));
        setFunction("inverseErfc", fixedFunction("inverseErfc", 1, (functions, arguments) -> functions.inverseErfc(arguments.get(0))));
        setFunction("jacobiP", fixedFunction("jacobiP", 4, (functions, arguments) -> functions.jacobiP(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3))));
        setFunction("khinchin", fixedFunction("khinchin", 0, 1, (functions, arguments) -> functions.khinchin(argumentOrInputPrecision(arguments))));
        setFunction("laguerreL", fixedFunction("laguerreL", 2, 3, (functions, arguments) -> (arguments.size() == 2 ? functions.laguerreL(arguments.get(0), arguments.get(1)) : functions.laguerreL(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("legendreP", fixedFunction("legendreP", 2, 3, (functions, arguments) -> (arguments.size() == 2 ? functions.legendreP(arguments.get(0), arguments.get(1)) : functions.legendreP(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("legendreQ", fixedFunction("legendreQ", 2, 3, (functions, arguments) -> (arguments.size() == 2 ? functions.legendreQ(arguments.get(0), arguments.get(1)) : functions.legendreQ(arguments.get(0), arguments.get(1), arguments.get(2)))));
        setFunction("log", fixedFunction("log", 1, 2, (functions, arguments) -> (arguments.size() == 1 ? functions.log(arguments.get(0)) : functions.log(arguments.get(0), arguments.get(1)))));
        setFunction("logBarnesG", fixedFunction("logBarnesG", 1, (functions, arguments) -> functions.logBarnesG(arguments.get(0))));
        setFunction("logGamma", fixedFunction("logGamma", 1, (functions, arguments) -> functions.logGamma(arguments.get(0))));
        setFunction("logIntegral", fixedFunction("logIntegral", 1, (functions, arguments) -> functions.logIntegral(arguments.get(0))));
        setFunction("logisticSigmoid", fixedFunction("logisticSigmoid", 1, (functions, arguments) -> functions.logisticSigmoid(arguments.get(0))));
        setFunction("max", fixedFunction("max", 2, (functions, arguments) -> functions.max(arguments.get(0), arguments.get(1))));
        setFunction("min", fixedFunction("min", 2, (functions, arguments) -> functions.min(arguments.get(0), arguments.get(1))));
        setFunction("nextAfter", fixedFunction("nextAfter", 2, (functions, arguments) -> functions.nextAfter(arguments.get(0), arguments.get(1))));
        setFunction("nextDown", fixedFunction("nextDown", 1, (functions, arguments) -> functions.nextDown(arguments.get(0))));
        setFunction("nextUp", fixedFunction("nextUp", 1, (functions, arguments) -> functions.nextUp(arguments.get(0))));
        setFunction("pi", fixedFunction("pi", 0, 1, (functions, arguments) -> functions.pi(argumentOrInputPrecision(arguments))));
        setFunction("pochhammer", fixedFunction("pochhammer", 2, (functions, arguments) -> functions.pochhammer(arguments.get(0), arguments.get(1))));
        setFunction("polygamma", fixedFunction("polygamma", 2, (functions, arguments) -> functions.polygamma(arguments.get(0), arguments.get(1))));
        setFunction("polylog", fixedFunction("polylog", 2, (functions, arguments) -> functions.polylog(arguments.get(0), arguments.get(1))));
        setFunction("random", fixedFunction("random", 0, 1, (functions, arguments) -> functions.random(argumentOrInputPrecision(arguments))));
        setFunction("randomGaussian", fixedFunction("randomGaussian", 0, 1, (functions, arguments) -> functions.randomGaussian(argumentOrInputPrecision(arguments))));
        setFunction("round", fixedFunction("round", 2, (functions, arguments) -> functions.round(arguments.get(0), arguments.get(1))));
        setFunction("roundToPrecision", fixedFunction("roundToPrecision", 2, (functions, arguments) -> functions.roundToPrecision(arguments.get(0), arguments.get(1))));
        setFunction("roundToInteger", fixedFunction("roundToInteger", 1, (functions, arguments) -> functions.roundToInteger(arguments.get(0))));
        setFunction("roundToPlaces", fixedFunction("roundToPlaces", 2, (functions, arguments) -> functions.roundToPlaces(arguments.get(0), arguments.get(1))));
        setFunction("roundToMultiple", fixedFunction("roundToMultiple", 2, (functions, arguments) -> functions.roundToMultiple(arguments.get(0), arguments.get(1))));
        setFunction("sin", fixedFunction("sin", 1, (functions, arguments) -> functions.sin(arguments.get(0))));
        setFunction("sinc", fixedFunction("sinc", 1, (functions, arguments) -> functions.sinc(arguments.get(0))));
        setFunction("sinIntegral", fixedFunction("sinIntegral", 1, (functions, arguments) -> functions.sinIntegral(arguments.get(0))));
        setFunction("sinh", fixedFunction("sinh", 1, (functions, arguments) -> functions.sinh(arguments.get(0))));
        setFunction("sinhIntegral", fixedFunction("sinhIntegral", 1, (functions, arguments) -> functions.sinhIntegral(arguments.get(0))));
        setFunction("sphericalHarmonicY", fixedFunction("sphericalHarmonicY", 4, (functions, arguments) -> functions.sphericalHarmonicY(arguments.get(0), arguments.get(1), arguments.get(2), arguments.get(3))));
        setFunction("sqrt", fixedFunction("sqrt", 1, (functions, arguments) -> functions.sqrt(arguments.get(0))));
        setFunction("struveH", fixedFunction("struveH", 2, (functions, arguments) -> functions.struveH(arguments.get(0), arguments.get(1))));
        setFunction("struveL", fixedFunction("struveL", 2, (functions, arguments) -> functions.struveL(arguments.get(0), arguments.get(1))));
        setFunction("tan", fixedFunction("tan", 1, (functions, arguments) -> functions.tan(arguments.get(0))));
        setFunction("tanh", fixedFunction("tanh", 1, (functions, arguments) -> functions.tanh(arguments.get(0))));
        setFunction("truncate", fixedFunction("truncate", 1, (functions, arguments) -> functions.truncate(arguments.get(0))));
        setFunction("toDegrees", fixedFunction("toDegrees", 1, (functions, arguments) -> functions.toDegrees(arguments.get(0))));
        setFunction("toRadians", fixedFunction("toRadians", 1, (functions, arguments) -> functions.toRadians(arguments.get(0))));
        setFunction("ulp", fixedFunction("ulp", 1, (functions, arguments) -> functions.ulp(arguments.get(0))));
        setFunction("weberE", fixedFunction("weberE", 2, (functions, arguments) -> functions.weberE(arguments.get(0), arguments.get(1))));
        setFunction("zeta", fixedFunction("zeta", 1, 2, (functions, arguments) -> (arguments.size() == 1 ? functions.zeta(arguments.get(0)) : functions.zeta(arguments.get(0), arguments.get(1)))));

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
        Functions functions = (arguments.isEmpty() ? getFunctions((Number) null) : null);
        for (Number argument : arguments)
        {
            Functions functions2 = getFunctions(argument);
            functions = (functions != null && functions.getClass().isAssignableFrom(functions2.getClass()) ? functions : functions2);
        }
        return functions;
    }

    private Number argumentOrInputPrecision(List<Number> arguments)
    {
        return arguments.size() == 0 ? getInputPrecision() : arguments.get(0);
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
