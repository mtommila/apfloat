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
package org.apfloat.samples;

import java.awt.Container;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * AWT client application for calculating pi using four different algorithms.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class PiGUI
    extends Frame
    implements PiAWT.StatusIndicator
{
    /**
     * Default constructor.
     */

    protected PiGUI()
    {
        super("Pi calculator");
        setSize(720, 540);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });

        setLayout(new BorderLayout());

        this.statusLabel = new Label();

        add(getContents(), BorderLayout.NORTH);
        add(this.statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void showStatus(String status)
    {
        this.statusLabel.setText(status);
    }

    /**
     * Get the graphical elements of this frame.
     *
     * @return The graphical elements of this frame.
     */

    protected Container getContents()
    {
        return new PiAWT(this);
    }

    /**
     * Command-line entry point.
     *
     * @param args Command-line parameters.
     */

    public static void main(String[] args)
    {
        new PiGUI();
    }

    private static final long serialVersionUID = 1L;

    private Label statusLabel;
}
