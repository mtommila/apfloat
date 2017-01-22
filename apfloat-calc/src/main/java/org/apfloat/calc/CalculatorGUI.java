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
