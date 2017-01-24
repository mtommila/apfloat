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
package org.apfloat.samples;

import java.applet.Applet;
import java.awt.Container;
import java.io.File;
import java.security.AccessControlException;

import org.apfloat.ApfloatContext;
import org.apfloat.spi.FilenameGenerator;

/**
 * Applet for calculating pi using four different algorithms.
 *
 * @version 1.8.3
 * @author Mikko Tommila
 */

public class PiApplet
    extends Applet
    implements PiAWT.StatusIndicator
{
    /**
     * Default constructor.
     */

    public PiApplet()
    {
    }

    /**
     * Initialize this applet.
     */

    public void init()
    {
        add(getContents());

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
     * Get the graphical elements of this applet.
     *
     * @return The graphical elements of this applet.
     */

    protected Container getContents()
    {
        return new PiAWT(this);
    }

    /**
     * Called when this applet is destroyed.
     */

    public void destroy()
    {
        removeAll();
    }

    /**
     * Get information about this applet.
     *
     * @return Information about this applet.
     */

    public String getAppletInfo()
    {
        Object builderFactory = ApfloatContext.getContext().getBuilderFactory();
        Package specificationPackage = Package.getPackage("org.apfloat"),
                implementationPackage = builderFactory.getClass().getPackage();

        String lineSeparator = System.getProperty("line.separator");
        return "Pi calculation applet" + lineSeparator +
               "Written by Mikko Tommila 2002 - 2017" + lineSeparator +
               "Specification-Title: "    + specificationPackage.getSpecificationTitle() + lineSeparator +
               "Specification-Version: "  + specificationPackage.getSpecificationVersion() + lineSeparator +
               "Specification-Vendor: "   + specificationPackage.getSpecificationVendor() + lineSeparator +
               "Implementation-Title: "   + implementationPackage.getImplementationTitle() + lineSeparator +
               "Implementation-Version: " + implementationPackage.getImplementationVersion() + lineSeparator +
               "Implementation-Vendor: "  + implementationPackage.getImplementationVendor() + lineSeparator +
               "Java version: "           + System.getProperty("java.version") + lineSeparator +
               "Java Virtual Machine: "   + System.getProperty("java.vm.name");
    }
}
