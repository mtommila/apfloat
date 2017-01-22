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
package org.apfloat.calc;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * AWT client application for the calculator.
 *
 * @version 1.2
 * @author Mikko Tommila
 */

public class CalculatorGUI
    extends Frame
{
    /**
     * Default constructor.
     */

    protected CalculatorGUI()
    {
        super("Calculator");
        setSize(720, 540);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });

        setLayout(new BorderLayout());

        add(new CalculatorAWT(), BorderLayout.NORTH);
        add(new Label(), BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Command-line entry point.
     *
     * @param args Command-line parameters.
     */

    public static void main(String[] args)
    {
        new CalculatorGUI();
    }
}
