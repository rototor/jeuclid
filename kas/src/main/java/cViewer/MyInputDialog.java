package cViewer;

/*
 * Copyright 2009 Erhard Kuenzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class MyInputDialog extends JDialog implements ActionListener,
        PropertyChangeListener {

    private static final long serialVersionUID = 20090512L;

    private String stringInput = null;

    private JTextField textField;

    private final String solution;

    private JOptionPane optionPane;

    public String getValidatedText() {
        return this.stringInput;
    }

    public MyInputDialog(final String sol) {
        super(ViewerFactory.getInst().getMathFrame(), true);
        this.setTitle("Berechne");
        this.solution = sol;
        this.setContentPane(this.getJOptionPane());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent we) {
                MyInputDialog.this.optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent ce) {
                MyInputDialog.this.getJTextField().requestFocusInWindow();
            }
        });
        this.getJTextField().addActionListener(this);
        this.getJOptionPane().addPropertyChangeListener(this);
    }

    public JTextField getJTextField() {
        if (this.textField == null) {
            this.textField = new JTextField(10);
            this.textField.setFont(new Font("Dialog", 1, 16));
        }
        return this.textField;
    }

    public JOptionPane getJOptionPane() {
        if (this.optionPane == null) {
            final Object[] options = { "Ok", "Abbruch" };
            this.optionPane = new JOptionPane(this.getJTextField(),
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION,
                    null, options, options[0]);
        }
        return this.optionPane;
    }

    public void actionPerformed(final ActionEvent e) {
        this.getJOptionPane().setValue("Ok");
    }

    public void propertyChange(final PropertyChangeEvent e) {
        final String prop = e.getPropertyName();
        if (this.isVisible()
                && (e.getSource() == this.getJOptionPane())
                && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
                        .equals(prop))) {
            final Object value = this.getJOptionPane().getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            this.getJOptionPane().setValue(JOptionPane.UNINITIALIZED_VALUE);
            if ("Ok".equals(value)) {
                this.stringInput = this.getJTextField().getText();
                if (this.solution.equals(this.stringInput)) {
                    this.getJTextField().setText(null);
                    this.setVisible(false);
                } else {
                    this.getJTextField().selectAll();
                    JOptionPane.showMessageDialog(MyInputDialog.this,
                            "Probiers doch mal mit  " + this.solution,
                            "Versuchs nochmal!", JOptionPane.ERROR_MESSAGE);
                    this.stringInput = null;
                    this.getJTextField().requestFocusInWindow();
                }
            } else {
                this.stringInput = null;
                this.getJTextField().setText(null);
                this.setVisible(false);
            }
        }
    }
}
