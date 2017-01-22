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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
 * @version 1.2
 * @author Mikko Tommila
 */

public class CalculatorAWT
    extends Panel
{
    /**
     * Construct a panel with graphical elements.
     */

    public CalculatorAWT()
    {
        this.calculatorImpl = new ApfloatCalculatorImpl();
        this.history = new ArrayList<String>();
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
        constraints.gridy = 4;
        add(this.inputField, constraints);

        this.calculateButton = new Button("Calculate");
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        add(this.calculateButton, constraints);

        this.outputArea = new TextArea(null, 20, 60, TextArea.SCROLLBARS_VERTICAL_ONLY);
        this.outputArea.setEditable(false);
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 4;
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

        this.clearButton = new Button("Clear");
        constraints.gridy = 3;
        constraints.weighty = 1000;
        add(this.clearButton, constraints);

        // Clear output area and command history
        this.clearButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                CalculatorAWT.this.outputArea.setText(null);
                CalculatorAWT.this.history.clear();
                CalculatorAWT.this.historyPosition = 0;
            }
        });

        // Calculate current input button
        this.calculateButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                processInput();
            }
        });

        // Calculate current input by hitting enter in the input field
        this.inputField.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                processInput();
            }
        });

        // Command history handler for up and down arrow keys
        this.inputField.addKeyListener(new KeyAdapter()
        {
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
        String text = this.inputField.getText();
        this.inputField.setText(null);
        this.out.println(text);

        // Parse the input line; the parser prints the output
        try
        {
            this.calculatorImpl.setFormat(this.fixed.getState());
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
    private Button clearButton;
    private TextField inputField;
    private Button calculateButton;

    private PrintWriter out;
}
