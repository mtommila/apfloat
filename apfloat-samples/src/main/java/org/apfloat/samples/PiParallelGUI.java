package org.apfloat.samples;

import java.awt.Container;

/**
 * AWT client application for calculating pi using multiple threads in parallel.
 *
 * @version 1.0.2
 * @author Mikko Tommila
 */

public class PiParallelGUI
    extends PiGUI
{
    /**
     * Default constructor.
     */

    protected PiParallelGUI()
    {
    }

    protected Container getContents()
    {
        return new PiParallelAWT(this);
    }

    /**
     * Command-line entry point.
     *
     * @param args Command-line parameters.
     */

    public static void main(String[] args)
    {
        new PiParallelGUI();
    }
}
