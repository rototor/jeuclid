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

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class MyComboDialog extends JDialog implements ActionListener,
        PropertyChangeListener {

    private static final long serialVersionUID = 20090512L;

    private JComboBox jCombobox;

    private JOptionPane optionPane;

    private final TransferObject transfer;

    public MyComboDialog(final TransferObject transfer) {
        super(ViewerFactory.getInst().getMathFrame(), true);
        this.transfer = transfer;
        if (transfer.getComment() != null) {
            this.setTitle(transfer.getComment());
        } else {
            this.setTitle("Wähle: ");
        }
        this.setContentPane(this.getJOptionPane());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent we) {
                MyComboDialog.this.getJOptionPane().setValue(
                        JOptionPane.CLOSED_OPTION);
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent ce) {
                MyComboDialog.this.getJComboBox().requestFocusInWindow();
            }
        });
        this.getJComboBox().addActionListener(this);
        this.getJOptionPane().addPropertyChangeListener(this);
    }

    public JComboBox getJComboBox() {
        if (this.jCombobox == null) {
            final String[] strArray = this.transfer.getOptions();
            this.jCombobox = new JComboBox(strArray);
            this.jCombobox.setFont(new Font("Dialog", 1, 16));
        }
        return this.jCombobox;
    }

    public JOptionPane getJOptionPane() {
        if (this.optionPane == null) {
            final Object[] options = { "Ok", "Abbruch" };
            this.optionPane = new JOptionPane(this.getJComboBox(),
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
            final String r = (String) this.getJComboBox().getSelectedItem();
            this.transfer.setResult(r);
            this.setVisible(false);
        }
    }
}
