package org.apfloat.samples;

import java.io.Serializable;

import org.apfloat.Apfloat;

/**
 * Simple JavaBean to hold one {@link org.apfloat.Apfloat}.
 * This class can also be thought of as a pointer to an {@link Apfloat}.
 *
 * @version 1.0
 * @author Mikko Tommila
 */

public class ApfloatHolder
    implements Serializable
{
    /**
     * Construct an ApfloatHolder containing <code>null</code>.
     */

    public ApfloatHolder()
    {
        this(null);
    }

    /**
     * Construct an ApfloatHolder containing the specified apfloat.
     *
     * @param apfloat The number to hold.
     */

    public ApfloatHolder(Apfloat apfloat)
    {
        this.apfloat = apfloat;
    }

    /**
     * Return the apfloat contained in this bean.
     *
     * @return The apfloat contained in this bean.
     */

    public Apfloat getApfloat()
    {
        return this.apfloat;
    }

    /**
     * Set the apfloat contained in this bean.
     *
     * @param apfloat The apfloat.
     */

    public void setApfloat(Apfloat apfloat)
    {
        this.apfloat = apfloat;
    }

    private Apfloat apfloat;
}
