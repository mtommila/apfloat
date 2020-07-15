/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
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

import java.applet.Applet;
import java.io.File;
import java.security.AccessControlException;

import org.apfloat.ApfloatContext;
import org.apfloat.spi.FilenameGenerator;

/**
 * Calculator applet.
 *
 * @version 1.9.1
 * @author Mikko Tommila
 */

public class CalculatorApplet
    extends Applet
{
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */

    public CalculatorApplet()
    {
    }

    /**
     * Initialize this applet.
     */

    @Override
    public void init()
    {
        add(new CalculatorAWT());

        // Recreate the executor service in case the old thread group was destroyed by reloading the applet
        ApfloatContext ctx = ApfloatContext.getContext();
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

        try
        {
            // The applet may not be able to write files to the current directory, but probably can write to the temp directory
            String path = System.getProperty("java.io.tmpdir");
            if (path != null && !path.endsWith(File.separator))
            {
                path = path + File.separator;
            }
            FilenameGenerator filenameGenerator = new FilenameGenerator(path, null, null);
            ctx.setFilenameGenerator(filenameGenerator);
        }
        catch (AccessControlException ace)
        {
            // Ignore - reading the system property may not be allowed in unsigned applets
        }
    }

    /**
     * Called when this applet is destroyed.
     */

    @Override
    public void destroy()
    {
        removeAll();
    }

    /**
     * Get information about this applet.
     *
     * @return Information about this applet.
     */

    @Override
    public String getAppletInfo()
    {
        String lineSeparator = System.lineSeparator();
        return "Calculator applet" + lineSeparator +
               "Written by Mikko Tommila 2011 - 2020" + lineSeparator +
               "Java version: "         + System.getProperty("java.version") + lineSeparator +
               "Java Virtual Machine: " + System.getProperty("java.vm.name");
    }
}
