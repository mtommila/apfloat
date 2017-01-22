package org.apfloat.samples;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Label;
import java.io.File;
import java.security.AccessControlException;

import org.apfloat.ApfloatContext;
import org.apfloat.spi.FilenameGenerator;

/**
 * Applet for calculating pi using four different algorithms.
 *
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class PiApplet
    extends Applet
{
    // Workaround to make this applet run with Microsoft VM and Java 1.4 VMs
    class Handler
        implements PiAWT.StatusIndicator
    {
        public void showStatus(String status)
        {
            PiApplet.this.showStatus(status);
        }

        public Container getContents()
        {
            return new PiAWT(this);
        }

        public void init()
        {
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
    }

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
        if (System.getProperty("java.version").compareTo("1.5") < 0)
        {
            add(new Label("This applet requires Java 5.0 or later. Download it from http://www.java.com"));
        }
        else
        {
            add(getContents());
            new Handler().init();
        }
    }

    /**
     * Get the graphical elements of this applet.
     *
     * @return The graphical elements of this applet.
     */

    protected Container getContents()
    {
        return new Handler().getContents();
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
               "Written by Mikko Tommila 2002 - 2015" + lineSeparator +
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
