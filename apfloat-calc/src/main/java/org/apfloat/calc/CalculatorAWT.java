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
package org.apfloat.calc;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Label;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.TextField;
import java.awt.TextArea;
import java.awt.Button;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

/**
 * Graphical AWT elements for the calculator.
 *
 * @version 1.9.1
 * @author Mikko Tommila
 */

public class CalculatorAWT
    extends Panel
{
    private static final long serialVersionUID = 1L;

    /**
     * Construct a panel with graphical elements.
     */

    public CalculatorAWT()
    {
        this.calculatorImpl = new ApfloatCalculatorImpl();
        this.history = new ArrayList<>();
        this.historyPosition = 0;
        initGUI();
        this.out = new PrintWriter(new FlushStringWriter(this.outputArea), true);
    }

    // Initialize the container and add the graphical elements to it
    private void initGUI()
    {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;

        // Initial focus will be on the first element, which is added here although it's located at the bottom
        this.inputField = new TextField(null, 60);
        constraints.gridy = 8;
        add(this.inputField, constraints);

        this.calculateButton = new Button("Calculate");
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        add(this.calculateButton, constraints);

        this.outputArea = new TextArea(null, 20, 60, TextArea.SCROLLBARS_VERTICAL_ONLY);
        this.outputArea.setEditable(false);
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 8;
        add(this.outputArea, constraints);

        this.formatLabel = new Label("Format:");
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = 1;
        add(this.formatLabel, constraints);

        this.formats = new CheckboxGroup();
        this.floating = new Checkbox("Floating", true, this.formats);
        constraints.gridy = 1;
        add(this.floating, constraints);

        this.fixed = new Checkbox("Fixed", false, this.formats);
        constraints.gridy = 2;
        add(this.fixed, constraints);

        this.inputTypeLabel = new Label("Input precision:");
        constraints.gridy = 3;
        add(this.inputTypeLabel, constraints);

        this.inputPrecisionTypes = new CheckboxGroup();
        this.inputPrecisionArbitrary = new Checkbox("Arbitrary", true, this.inputPrecisionTypes);
        constraints.gridy = 4;
        add(this.inputPrecisionArbitrary, constraints);

        this.inputPrecisionFixed = new Checkbox("Fixed", false, this.inputPrecisionTypes);
        constraints.gridy = 5;
        add(this.inputPrecisionFixed, constraints);

        this.inputPrecisionField = new TextField(null, 10);
        constraints.gridy = 6;
        add(this.inputPrecisionField, constraints);

        this.clearButton = new Button("Clear");
        constraints.gridy = 7;
        constraints.weighty = 1000;
        add(this.clearButton, constraints);

        // Clear output area and command history
        this.clearButton.addActionListener((actionEvent) ->
        {
            CalculatorAWT.this.outputArea.setText(null);
            CalculatorAWT.this.history.clear();
            CalculatorAWT.this.historyPosition = 0;
        });

        // Calculate current input button
        this.calculateButton.addActionListener((actionEvent) -> processInput());

        // Calculate current input by hitting enter in the input field
        this.inputField.addActionListener((actionEvent) -> processInput());

        // Command history handler for up and down arrow keys
        this.inputField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent keyEvent)
            {
                switch(keyEvent.getKeyCode())
                {
                    case KeyEvent.VK_UP:
                        if (CalculatorAWT.this.historyPosition > 0)
                        {
                            CalculatorAWT.this.inputField.setText(CalculatorAWT.this.history.get(--CalculatorAWT.this.historyPosition));
                            CalculatorAWT.this.inputField.setCaretPosition(Integer.MAX_VALUE);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (CalculatorAWT.this.historyPosition < CalculatorAWT.this.history.size() - 1)
                        {
                            CalculatorAWT.this.inputField.setText(CalculatorAWT.this.history.get(++CalculatorAWT.this.historyPosition));
                            CalculatorAWT.this.inputField.setCaretPosition(Integer.MAX_VALUE);
                        }
                        break;
                    default:
                }
            }
        });
    }

    private void processInput()
    {
        Long inputPrecision;
        if (this.inputPrecisionArbitrary.getState())
        {
            inputPrecision = null;
        }
        else
        {
            try
            {
                inputPrecision = Long.parseLong(this.inputPrecisionField.getText());
            }
            catch (NumberFormatException nfe)
            {
                this.inputPrecisionField.requestFocus();
                return;
            }
        }

        String text = this.inputField.getText();
        this.inputField.setText(null);
        this.out.println(text);

        // Parse the input line; the parser prints the output
        try
        {
            this.calculatorImpl.setFormat(this.fixed.getState());
            this.calculatorImpl.setInputPrecision(inputPrecision);
            CalculatorParser calculatorParser = new CalculatorParser(new StringReader(text), this.out, this.calculatorImpl);
            calculatorParser.parseOneLine();
        }
        catch (Exception e)
        {
            this.out.println(e.getMessage());
        }

        // Show the bottom part of the output area
        this.outputArea.requestFocus();
        this.outputArea.setCaretPosition(Integer.MAX_VALUE);
        this.inputField.requestFocus();

        // Add last command to history and reset history position
        this.history.add(text);
        this.historyPosition = this.history.size();
    }

    // Prints output to a text area
    private static class FlushStringWriter
        extends StringWriter
    {
        public FlushStringWriter(TextArea dst)
        {
            this.dst = dst;
        }

        @Override
        public void flush()
        {
            super.flush();

            StringBuffer buffer = getBuffer();
            String text = buffer.toString();

            this.dst.append(text);

            buffer.setLength(0);
        }

        private TextArea dst;
    }

    private CalculatorImpl calculatorImpl;

    private List<String> history;
    private int historyPosition;

    private TextArea outputArea;
    private Label formatLabel;
    private CheckboxGroup formats;
    private Checkbox floating;
    private Checkbox fixed;
    private Label inputTypeLabel;
    private CheckboxGroup inputPrecisionTypes;
    private Checkbox inputPrecisionArbitrary;
    private Checkbox inputPrecisionFixed;
    private TextField inputPrecisionField;
    private Button clearButton;
    private TextField inputField;
    private Button calculateButton;

    private PrintWriter out;
}
