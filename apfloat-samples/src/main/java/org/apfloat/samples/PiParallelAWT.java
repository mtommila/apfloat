/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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
