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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MyOptionsDialog extends JDialog implements ActionListener,
        PropertyChangeListener {

    private static final long serialVersionUID = 20090512L;

    private JCheckPanel jCheckPanel;

    private JOptionPane optionPane;

    private final TransferObject transfer;

    public MyOptionsDialog(final TransferObject transfer) {
        super(ViewerFactory.getInst().getMathFrame(), true);
        this.setTitle("Wähle: ");
        this.transfer = transfer;
        this.setContentPane(this.getJOptionPane());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent we) {
                MyOptionsDialog.this.getJOptionPane().setValue(
                        JOptionPane.CLOSED_OPTION);
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent ce) {
                MyOptionsDialog.this.getCheckPanel().requestFocusInWindow();
            }
        });
        this.getJOptionPane().addPropertyChangeListener(this);
    }

    public JCheckPanel getCheckPanel() {
        if (this.jCheckPanel == null) {
            this.jCheckPanel = new JCheckPanel();
        }
        return this.jCheckPanel;
    }

    @SuppressWarnings("serial")
    class JCheckPanel extends JPanel implements ItemListener {
        private final JCheckBox firstButton;

        private final JCheckBox secButton;

        private final JCheckBox thirdButton;

        private final JCheckBox fButton;

        public JCheckPanel() {
            this.setLayout(new GridLayout(4, 0));
            this.firstButton = new JCheckBox(MyOptionsDialog.this.transfer
                    .getOptions()[0]);
            this.firstButton.setSelected(true);
            this.add(this.firstButton);
            this.firstButton.addItemListener(this);
            this.secButton = new JCheckBox(MyOptionsDialog.this.transfer
                    .getOptions()[1]);
            this.secButton.setSelected(true);
            this.add(this.secButton);
            this.secButton.addItemListener(this);
            this.thirdButton = new JCheckBox(MyOptionsDialog.this.transfer
                    .getOptions()[2]);
            this.thirdButton.setSelected(true);
            this.add(this.thirdButton);
            this.thirdButton.addItemListener(this);
            this.fButton = new JCheckBox(MyOptionsDialog.this.transfer
                    .getOptions()[3]);
            this.fButton.setSelected(true);
            this.add(this.fButton);
            this.fButton.addItemListener(this);
        }

        public void itemStateChanged(final ItemEvent e) {
            int index = 0;
            char c = '-';
            final Object source = e.getItemSelectable();
            if (source == this.firstButton) {
                index = 0;
                c = '0';
            } else if (source == this.secButton) {
                index = 1;
                c = '1';
            } else if (source == this.thirdButton) {
                index = 2;
                c = '2';
            } else if (source == this.fButton) {
                index = 3;
                c = '3';
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                c = '-';
            }
            final char[] options = MyOptionsDialog.this.transfer.getResult()
                    .toCharArray();
            options[index] = c;
            MyOptionsDialog.this.transfer.setResult(new String(options));
        }
    }

    public JOptionPane getJOptionPane() {
        if (this.optionPane == null) {
            final Object[] options = { "Ok" };
            this.optionPane = new JOptionPane(this.getCheckPanel(),
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_OPTION,
                    null, options, options[0]);
        }
        return this.optionPane;
    }

    public void actionPerformed(final ActionEvent e) {
        this.getJOptionPane().setValue("Ok");
    }

    /** Listens to propertyChanges of Buttons. */
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
            // final String r = (String)
            // this.getJComboBox().getSelectedItem();

            this.setVisible(false);
        }
    }
}
