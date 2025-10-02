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
package org.apfloat.jscience;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apfloat.Apcomplex;
import org.apfloat.FixedPrecisionApcomplexHelper;

/**
 * This class represents a fixed-precision complex number. The precision
 * is reset after each computation using the provided <code>FixedPrecisionApcomplexHelper</code>.
 * This can help avoid accumulating round-off errors and loss of precision
 * in complicated computations such as matrix inversion.
 *
 * @since 1.8.0
 * @version 1.15.0
 * @author Mikko Tommila
 */

public class FixedPrecisionApcomplexField
    extends AbstractField<FixedPrecisionApcomplexField, Apcomplex>
{
    /**
     * Holds the default XML representation for fixed-precision complex fields.
     */

    static final XMLFormat<FixedPrecisionApcomplexField> XML = new XMLFormat<FixedPrecisionApcomplexField>(FixedPrecisionApcomplexField.class)
    {
        @Override
        public FixedPrecisionApcomplexField newInstance(Class<FixedPrecisionApcomplexField> cls, InputElement xml)
            throws XMLStreamException
        {
            return new FixedPrecisionApcomplexField(new Apcomplex(parse("real-", xml), parse("imag-", xml)), new FixedPrecisionApcomplexHelper(xml.getAttribute("precision", 0L)));
        }

        @Override
        public void write(FixedPrecisionApcomplexField field, OutputElement xml)
            throws XMLStreamException
        {
            format(field.value().real(), "real-", xml);
            format(field.value().imag(), "imag-", xml);
            xml.setAttribute("precision", field.helper().precision());
        }

        @Override
        public void read(InputElement xml, FixedPrecisionApcomplexField complex)
        {
            // Immutable, deserialization occurs at creation, see newInstance() 
        }
    };

    /**
     * Constructs a new complex field object with the specified value and precision helper.
     *
     * @param value The value.
     * @param helper The precision helper.
     */

    public FixedPrecisionApcomplexField(Apcomplex value, FixedPrecisionApcomplexHelper helper)
    {
        super(value);
        if (helper == null)
        {
            throw new NullPointerException("Helper can't be null");
        }
        this.helper = helper;
    }

    @Override
    public FixedPrecisionApcomplexField plus(FixedPrecisionApcomplexField that)
    {
        return new FixedPrecisionApcomplexField(helper().add(value(), that.value()), helper());
    }

    @Override
    public FixedPrecisionApcomplexField opposite()
    {
        return new FixedPrecisionApcomplexField(helper().negate(value()), helper());
    }

    @Override
    public FixedPrecisionApcomplexField times(FixedPrecisionApcomplexField that)
    {
        return new FixedPrecisionApcomplexField(helper().multiply(value(), that.value()), helper());
    }

    @Override
    public FixedPrecisionApcomplexField inverse()
        throws ArithmeticException
    {
        return new FixedPrecisionApcomplexField(helper().inverseRoot(value(), 1), helper());
    }

    @Override
    public FixedPrecisionApcomplexField copy()
    {
        return new FixedPrecisionApcomplexField(value(), helper());
    }

    /**
     * Return the precision helper.
     *
     * @return The precision helper.
     */

    public FixedPrecisionApcomplexHelper helper()
    {
        return this.helper;
    }

    private static final long serialVersionUID = -2069599698604093434L;

    private FixedPrecisionApcomplexHelper helper;
}
