package org.apfloat.samples;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.TextField;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatRuntimeException;

/**
 * Graphical AWT elements for calculating pi using multiple threads in parallel.
 *
 * @version 1.8.1
 * @author Mikko Tommila
 */

public class PiParallelAWT
    extends PiAWT
{
    /**
     * Construct a panel with graphical elements.
     *
     * @param statusIndicator Handler for showing error messages in the application.
     */

    public PiParallelAWT(StatusIndicator statusIndicator)
    {
        super(statusIndicator);
    }

    protected void initThreads(Container container, GridBagConstraints constraints)
    {
        this.threadsLabel = new Label("Threads:");
        container.add(this.threadsLabel, constraints);

        this.threadsField = new TextField(ApfloatContext.getContext().getProperty(ApfloatContext.NUMBER_OF_PROCESSORS), 5);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        container.add(this.threadsField, constraints);
    }

    protected boolean isInputValid()
    {
        if (!super.isInputValid())
        {
            return false;
        }
        else
        {
            String threadsString = this.threadsField.getText();
            try
            {
                int threads = Integer.parseInt(threadsString);
                if (threads <= 0)
                {
                    throw new NumberFormatException();
                }
                showStatus(null);
                return true;
            }
            catch (NumberFormatException nfe)
            {
                showStatus("Invalid number of threads: " + threadsString);
                this.threadsField.requestFocus();
                return false;
            }
        }
    }

    protected Operation<Apfloat> getOperation(long precision, int radix)
        throws ApfloatRuntimeException
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        int numberOfProcessors = Integer.parseInt(this.threadsField.getText());
        ctx.setNumberOfProcessors(numberOfProcessors);
        ctx.setExecutorService(ApfloatContext.getDefaultExecutorService());

        Operation<Apfloat> operation = super.getOperation(precision, radix);
        if (operation instanceof Pi.ChudnovskyPiCalculator)
        {
            operation = new PiParallel.ParallelChudnovskyPiCalculator(precision, radix);
        }
        else if (operation instanceof Pi.RamanujanPiCalculator)
        {
            operation = new PiParallel.ParallelRamanujanPiCalculator(precision, radix);
        }
        return operation;
    }

    private Label threadsLabel;
    private TextField threadsField;
}
