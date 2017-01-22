package org.apfloat.calc;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Label;
import java.security.AccessControlException;

import org.apfloat.ApfloatContext;
import org.apfloat.spi.FilenameGenerator;

/**
 * Calculator applet.
 *
 * @version 1.6.2
 * @author Mikko Tommila
 */

public class CalculatorApplet
    extends Applet
{
    // Workaround to make this applet run with Microsoft VM and Java 1.4 VMs
    private class Handler
    {
        public Container getContents()
        {
            return new CalculatorAWT();
        }

        public void init()
        {
            // Recreate the executor service in case the old thread group was destroyed by reloading the applet
            ApfloatContext ctx = ApfloatContext.getContext();
            ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

            try
            {
                // The applet may not be able to write files to the current directory, but probably can write to the temp directory
                FilenameGenerator filenameGenerator = new FilenameGenerator(System.getProperty("java.io.tmpdir"), null, null);
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

    public CalculatorApplet()
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
            add(new Handler().getContents());
            new Handler().init();
        }
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
        String lineSeparator = System.getProperty("line.separator");
        return "Calculator applet" + lineSeparator +
               "Written by Mikko Tommila 2011" + lineSeparator +
               "Java version: "         + System.getProperty("java.version") + lineSeparator +
               "Java Virtual Machine: " + System.getProperty("java.vm.name");
    }
}
