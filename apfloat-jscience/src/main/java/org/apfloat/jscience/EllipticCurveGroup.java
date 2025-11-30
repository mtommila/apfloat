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

import javolution.context.LocalContext;
import javolution.lang.Realtime;
import javolution.text.Text;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apfloat.Apint;
import org.apfloat.ApintMath;
import org.jscience.mathematics.structure.GroupAdditive;

import java.io.Serializable;
import java.util.Objects;

import static org.apfloat.jscience.AbstractField.format;
import static org.apfloat.jscience.AbstractField.parse;

/**
 * This class represents a point on an elliptic curve defined over the integers modulo a modulus.
 * All arithmetic is done in Weierstrass form.<p>
 *
 * This class sets the modulus using {@link ModuloApintField#setModulus(Apint)}.
 *
 * @since 1.15.0
 * @version 1.15.1
 * @author Mikko Tommila
 */

public class EllipticCurveGroup
    implements GroupAdditive<EllipticCurveGroup>, Realtime, XMLSerializable, Serializable
{
    /**
     * Holds the default XML representation for elliptic curve points.
     */

    static final XMLFormat<EllipticCurveGroup> XML = new XMLFormat<EllipticCurveGroup>(EllipticCurveGroup.class)
    {
        @Override
        public EllipticCurveGroup newInstance(Class<EllipticCurveGroup> cls, InputElement xml)
            throws XMLStreamException
        {
            return new EllipticCurveGroup(parse("x-", xml).truncate(), parse("y-", xml).truncate());
        }

        @Override
        public void write(EllipticCurveGroup group, OutputElement xml)
            throws XMLStreamException
        {
            format(group.x.value(), "x-", xml, null);
            format(group.y.value(), "y-", xml, null);
        }

        @Override
        public void read(InputElement xml, EllipticCurveGroup group)
            throws XMLStreamException
        {
            // Immutable, deserialization occurs at creation, see newInstance() 
        }
    };

    /**
     * The "point at infinity".
     */

    public static final EllipticCurveGroup O = new EllipticCurveGroup((ModuloApintField) null, (ModuloApintField) null);

    /**
     * Constructs a new point on the elliptic curve. Uses Weierstrass coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */

    public EllipticCurveGroup(Apint x, Apint y)
    {
        this(new ModuloApintField(x), new ModuloApintField(y));
    }

    private EllipticCurveGroup(ModuloApintField x, ModuloApintField y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a point from Montgomery curve coordinates.
     * The Montgomery curve parameters must be first set with {@link #setMontgomeryParameters(Apint, Apint, Apint)}.
     *
     * @param x The Montgomery curve x-coordinate.
     * @param y The Montgomery curve y-coordinate.
     *
     * @return The point in Weierstrass coordinates.
     */

    public static EllipticCurveGroup fromMontgomeryPoint(Apint x, Apint y)
    {
        return fromMontgomeryPoint(new ModuloApintField(x), new ModuloApintField(y));
    }

    private static EllipticCurveGroup fromMontgomeryPoint(ModuloApintField x, ModuloApintField y)
    {
        ModuloApintField A = montgomeryA.get(),
                         B = montgomeryB.get();
        if (A == null || B == null)
        {
            throw new IllegalStateException("Curve parameters have not been set");
        }
        int radix = x.value().radix();
        ModuloApintField inverseB = B.inverse(),
                         inverseThree = new ModuloApintField(new Apint(3, radix)).inverse();
        return new EllipticCurveGroup(x.times(inverseB).plus(A.times(inverseB).times(inverseThree)), y.times(inverseB));
    }

    /**
     * Construct a point from twisted Edwards curve coordinates.
     * The Edwards curve parameters must be first set with {@link #setEdwardsParameters(Apint, Apint, Apint)}.
     *
     * @param x The Edwards curve x-coordinate.
     * @param y The Edwards curve y-coordinate.
     *
     * @return The point in Weierstrass coordinates.
     */

    public static EllipticCurveGroup fromEdwardsPoint(Apint x, Apint y)
    {
        return fromEdwardsPoint(new ModuloApintField(x), new ModuloApintField(y));
    }

    private static EllipticCurveGroup fromEdwardsPoint(ModuloApintField x, ModuloApintField y)
    {
        int radix = x.value().radix();
        ModuloApintField one = new ModuloApintField(new Apint(1, radix)),
                         y1 = one.plus(y).times(one.minus(y).inverse());
        return fromMontgomeryPoint(y1, y1.times(x.inverse()));
    }

    /**
     * Set the curve parameters in Weierstrass form.
     * The elliptic curve specified in the Weierstrass form is<br/>
     * <i>y<sup>2</sup> = x<sup>3</sup> + a x + b</i><p>
     *
     * For example, Curve25519 can be set with<p>
     * <code>setWeierstrassParameters(new Apint(486662), new Apint(1), ApintMath.pow(new Apint(2), 255).subtract(new Apint(19)))</code>
     *
     * @param a The curve parameter a.
     * @param b The curve parameter b.
     * @param p The modulus.
     */

    public static void setWeierstrassParameters(Apint a, Apint b, Apint p)
    {
        ModuloApintField.setModulus(p);
        int radix = a.radix();
        ModuloApintField α = new ModuloApintField(a),
                         β = new ModuloApintField(b),
                         four = new ModuloApintField(new Apint(4, radix)),
                         twentyseven = new ModuloApintField(new Apint(3, radix));
        if (four.times(α).times(α).times(α).plus(twentyseven.times(β).times(β)).value().signum() == 0)
        {
            throw new IllegalArgumentException("Curve is not valid");
        }
        weierstrassA.set(α);
        weierstrassB.set(β);
        montgomeryA.set(null);
        montgomeryB.set(null);
    }

    /**
     * Set the curve parameters in Montgomery form.
     * The elliptic curve specified in the Montgomery form is<br/>
     * <i>B y<sup>2</sup> = x<sup>3</sup> + A x<sup>2</sup> + x</i><p>
     *
     * For example, Curve25519 can be set with<p>
     * <code>setMontgomeryParameters(new Apint(1), new Apint(486662), ApintMath.pow(new Apint(2), 255).subtract(new Apint(19)))</code>
     *
     * @param b The curve parameter B.
     * @param a The curve parameter A.
     * @param p The modulus.
     */

    public static void setMontgomeryParameters(Apint b, Apint a, Apint p)
    {
        int radix = p.radix();
        ModuloApintField.setModulus(p);
        ModuloApintField A = new ModuloApintField(a),
                         B = new ModuloApintField(b),
                         two = new ModuloApintField(new Apint(2, radix)),
                         three = new ModuloApintField(new Apint(3, radix)),
                         nine = new ModuloApintField(new Apint(9, radix)),
                         twentyseven = new ModuloApintField(new Apint(27, radix));
        setWeierstrassParameters(three.minus(A.times(A)).times(three.times(B).times(B).inverse()).value(),
                                 two.times(A).times(A).minus(nine).times(A).times(twentyseven.times(B).times(B).times(B).inverse()).value(),
                                 p);
        montgomeryA.set(A);
        montgomeryB.set(B);
    }

    /**
     * Set the curve parameters in twisted Edwards form.
     * The elliptic curve specified in the twisted Edwards form is<br/>
     * <i>a x<sup>2</sup> + y<sup>2</sup> = 1 + d x<sup>2</sup> y<sup>2</sup></i><p>
     *
     * For example, Curve448 can be set with<p>
     * <code>setEdwardsParameters(new Apint(1), new Apint(-39081), ApintMath.pow(new Apint(2), 448).subtract(ApintMath.pow(new Apint(2), 224).subtract(new Apint(1))))</code>
     *
     * @param a The curve parameter a.
     * @param d The curve parameter d.
     * @param p The modulus.
     */

    public static void setEdwardsParameters(Apint a, Apint d, Apint p)
    {
        int radix = p.radix();
        ModuloApintField.setModulus(p);
        ModuloApintField x = new ModuloApintField(a.subtract(d)).inverse(),
                         ad = new ModuloApintField(a.add(d)),
                         two = new ModuloApintField(new Apint(2, radix)),
                         four = new ModuloApintField(new Apint(4, radix));
        setMontgomeryParameters(four.times(x).value(),
                                two.times(ad).times(x).value(),
                                p);
    }

    /**
     * Returns the curve parameter <i>a</i> in Weierstrass form.
     *
     * @return The curve parameter <i>a</i>.
     *
     * @see #setWeierstrassParameters(Apint, Apint, Apint)
     */

    public static Apint getA()
    {
        return weierstrassA.get().value();
    }

    /**
     * Returns the curve parameter <i>b</i> in Weierstrass form.
     *
     * @return The curve parameter <i>b</i>.
     *
     * @see #setWeierstrassParameters(Apint, Apint, Apint)
     */

    public static Apint getB()
    {
        return weierstrassB.get().value();
    }

    /**
     * Returns the j-invariant of the curve.
     *
     * @return The j-invariant of the curve.
     */

    public static Apint getJInvariant()
    {
        ModuloApintField a = weierstrassA.get(),
                         b = weierstrassB.get();
        int radix = a.value().radix();
        ModuloApintField foura3 = new ModuloApintField(new Apint(4, radix)).times(a).times(a).times(a),
                         twentyseven = new ModuloApintField(new Apint(27, radix)),
                         twelve3 = new ModuloApintField(new Apint(1728, radix));

        return twelve3.times(foura3).times(foura3.plus(twentyseven.times(b).times(b)).inverse()).value();
    }

    /**
     * Returns the <i>x</i>-coordinate of this point.
     *
     * @return The <i>x</i>-coordinate.
     */

    public Apint getX()
    {
        return this.x.value();
    }

    /**
     * Returns the <i>y</i>-coordinate of this point.
     *
     * @return The <i>y</i>-coordinate.
     */

    public Apint getY()
    {
        return this.y.value();
    }

    @Override
    public EllipticCurveGroup plus(EllipticCurveGroup that)
    {
        if (this.equals(O))
        {
            return that;
        }
        if (that.equals(O))
        {
            return this;
        }

        ModuloApintField a = weierstrassA.get(),
                         b = weierstrassB.get(),
                         x,
                         y;
        if (a == null || b == null)
        {
            throw new IllegalStateException("Curve parameters have not been set");
        }
        if (!this.x.equals(that.x))
        {
            // P != Q
            ModuloApintField λ = that.y.minus(this.y).times(that.x.minus(this.x).inverse());
            x = λ.times(λ).minus(this.x).minus(that.x);
            y = λ.times(this.x.minus(x)).minus(this.y);
        }
        else if (this.y.equals(that.y))
        {
            // P == Q
            int radix = this.y.value().radix();
            ModuloApintField two = new ModuloApintField(new Apint(2, radix)),
                             three = new ModuloApintField(new Apint(3, radix)),
                             λ = three.times(this.x).times(this.x).plus(a).times(two.times(this.y).inverse());
            x = λ.times(λ).minus(two.times(this.x));
            y = λ.times(this.x.minus(x)).minus(this.y);
        }
        else
        {
            // P == -Q
            assert (this.x.equals(that.x) && this.y.equals(that.y.opposite()));
            // Result is O, the "point at infinity"
            return O;
        }
        assert (y.times(y).equals(x.times(x).plus(a).times(x).plus(b)));
        return new EllipticCurveGroup(x, y);
    }

    /**
     * Returns this elliptic curve point multiplied by a scalar.
     * 
     * @param that The multiplier.
     * 
     * @return The resulting elliptic curve point.
     */

    public EllipticCurveGroup times(Apint that)
    {
        if (this.equals(O))
        {
            return this;
        }
        if (that.signum() < 0)
        {
            return opposite().times(that.negate());
        }
        Apint two = new Apint(2, that.radix());
        EllipticCurveGroup a = this,
                           result = O;
        while (that.signum() != 0)
        {
            Apint[] qr = ApintMath.div(that, two);
            if (qr[1].signum() != 0)
            {
                result = result.plus(a);
            }
            if (qr[0].signum() != 0)
            {
                a = a.plus(a);
            }
            that = qr[0];
        }
        return result;
    }

    @Override
    public EllipticCurveGroup opposite()
    {
        if (this.equals(O))
        {
            return this;
        }
        return new EllipticCurveGroup(this.x, this.y.opposite());
    }

    @Override
    public EllipticCurveGroup copy()
    {
        if (this.equals(O))
        {
            return new EllipticCurveGroup((ModuloApintField) null, (ModuloApintField) null);
        }
        return new EllipticCurveGroup(this.x.value(), this.y.value());
    }

    @Override
    public Text toText()
    {
        if (this.equals(O))
        {
            return Text.valueOf("(∞, ∞)");
        }
        return Text.valueOf("(" + this.x + ", " + this.y + ')');
    }

    @Override
    public String toString() {
        return toText().toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.x) + 3 * Objects.hashCode(this.y);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof EllipticCurveGroup)
        {
            EllipticCurveGroup that = (EllipticCurveGroup) obj;
            return Objects.equals(this.x, that.x) && Objects.equals(this.y, that.y);
        }
        return false;
    }

    private static final long serialVersionUID = 4239380063596030297L;

    private final ModuloApintField x,
                                   y;

    private static final LocalContext.Reference<ModuloApintField> weierstrassA = new LocalContext.Reference<>(),
                                                                  weierstrassB = new LocalContext.Reference<>(),
                                                                  montgomeryA = new LocalContext.Reference<>(),
                                                                  montgomeryB = new LocalContext.Reference<>();
}
