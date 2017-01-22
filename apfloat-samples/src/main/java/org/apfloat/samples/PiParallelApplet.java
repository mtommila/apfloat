package org.apfloat.samples;

import java.awt.Container;

/**
 * Applet for calculating pi using multiple threads in parallel.
 *
 * @version 1.1
 * @author Mikko Tommila
 */

public class PiParallelApplet
    extends PiApplet
{
    // Workaround to make this applet run with Microsoft VM and Java 1.4 VMs
    class Handler
        extends PiApplet.Handler
    {
        public Container getContents()
        {
            return new PiParallelAWT(this);
        }
    }

    /**
     * Default constructor.
     */

    public PiParallelApplet()
    {
    }

    protected Container getContents()
    {
        return new Handler().getContents();
    }
}
