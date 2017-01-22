package org.apfloat.internal;

import java.util.Properties;

import org.apfloat.*;

import junit.framework.TestCase;

/**
 * @version 1.0
 * @author Mikko Tommila
 */

public class RawtypeTestCase
    extends TestCase
{
    public RawtypeTestCase()
    {
    }

    public RawtypeTestCase(String methodName)
    {
        super(methodName);
    }

    protected void setUp()
    {
        Properties properties = new Properties();
        properties.setProperty(ApfloatContext.BUILDER_FACTORY, "org.apfloat.internal.RawtypeBuilderFactory");

        ApfloatContext.getContext().setProperties(properties);
    }
}
