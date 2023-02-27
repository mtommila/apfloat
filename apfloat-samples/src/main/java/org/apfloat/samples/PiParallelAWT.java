/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
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
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.TextField;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatRuntimeException;

/**
 * Graphical AWT elements for calculating pi using multiple threads in parallel.
 *
 * @version 1.9.0
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

    @Override
    protected void initThreads(Container container, GridBagConstraints constraints)
    {
        this.threadsLabel = new Label("Threads:");
        container.add(this.threadsLabel, constraints);

        this.threadsField = new TextField(ApfloatContext.getContext().getProperty(ApfloatContext.NUMBER_OF_PROCESSORS), 5);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        container.add(this.threadsField, constraints);
    }

    @Override
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

    @Override
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

    private static final long serialVersionUID = 1L;

    private Label threadsLabel;
    private TextField threadsField;
}
