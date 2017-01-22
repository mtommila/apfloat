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
