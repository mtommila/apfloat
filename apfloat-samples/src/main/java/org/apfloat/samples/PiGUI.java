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
 * @version 1.5
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

    private Label statusLabel;
}
