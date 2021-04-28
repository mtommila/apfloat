/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
