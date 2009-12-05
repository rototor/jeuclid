/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

/* $Id$ */

package net.sourceforge.jeuclid.app.mathviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.context.typewrapper.BooleanTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.ColorTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.EnumTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.NumberTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.TypeWrapper;
import net.sourceforge.jeuclid.swing.JMathComponent;

/**
 * Dialog that lets user see and change all Layout Parameters.
 * 
 * @see Parameter
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
public class ParametersDialog extends JDialog {
    // CHECKSTYLE:ON

    private static final int NUMBER_OF_COLUMNS = 5;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final int PADDING = 30;

    private static interface DataExtractor {
        Object extractData();
    }

    private final JMathComponent mathComponent;

    // CHECKSTYLE:OFF
    private final Map<Parameter, DataExtractor> extractors = new HashMap<Parameter, DataExtractor>();

    // CHECKSTYLE:ON

    /**
     * Initialize all the swing stuff.
     * 
     * @param parent
     *            parent MainFrame
     */
    public ParametersDialog(final MainFrameEx parent)
    {
        super(parent);
        this.mathComponent = parent.getMathComponent();
        init();
    }
    
    public ParametersDialog(final MainFrame parent) {
        super(parent);
        this.mathComponent = parent.getMathComponent();
        init();
    }

    private void init()
    {
        
        this
                .setTitle(Messages
                        .getString("MathViewer.ParametersDialog.title"));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final JPanel paramArea = new JPanel(new GridBagLayout());
        this.getContentPane().add(paramArea, BorderLayout.CENTER);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.ipadx = ParametersDialog.PADDING;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        for (final Parameter param : Parameter.values()) {
            final JLabel label = new JLabel(param.getOptionName());
            label.setToolTipText(param.getOptionDesc());
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.EAST;
            paramArea.add(label, gbc);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            paramArea.add(this.createInputForParameter(param), gbc);
            gbc.gridy++;
        }
        this.setupButtonsPanel();
        this.setSize(this.getMinimumSize().width,
                this.getPreferredSize().height);
        this.setLocationByPlatform(true);
        this.setModal(true);
    }

    private void setupButtonsPanel() {
        final JPanel buttonsPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER));
        final JButton btnSave = new JButton(Messages
                .getString("MathViewer.save"));
        btnSave.setMnemonic('S');
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Map<Parameter, Object> newValues = new HashMap<Parameter, Object>();
                for (final Map.Entry<Parameter, ParametersDialog.DataExtractor> entry : ParametersDialog.this.extractors
                        .entrySet()) {
                    newValues.put(entry.getKey(), entry.getValue()
                            .extractData());
                }
                ParametersDialog.this.mathComponent.setParameters(newValues);
                ParametersDialog.this.dispose();
            }
        });
        buttonsPanel.add(btnSave);
        final JButton btnCancel = new JButton(Messages
                .getString("MathViewer.cancel"));
        btnCancel.setMnemonic('C');
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ParametersDialog.this.dispose();
            }
        });
        buttonsPanel.add(btnCancel);
        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }

    private JComponent createInputForParameter(final Parameter param) {
        JComponent input = null;
        final TypeWrapper type = param.getTypeWrapper();
        DataExtractor dataExtractor = null;
        if (type instanceof BooleanTypeWrapper) {
            final JCheckBox checkbox = new JCheckBox();
            Boolean val = (Boolean) this.mathComponent.getParameters()
                    .getParameter(param);
            if (val == null) {
                val = Boolean.FALSE;
            }
            checkbox.setSelected(val.booleanValue());
            input = checkbox;
            dataExtractor = new DataExtractor() {
                public Object extractData() {
                    return Boolean.valueOf(checkbox.isSelected());
                }
            };
        } else if (type instanceof ColorTypeWrapper) {
            final Map.Entry<? extends JComponent, ? extends ParametersDialog.DataExtractor> entry = this
                    .setupColorParameter(param);
            input = entry.getKey();
            dataExtractor = entry.getValue();
        } else if (type instanceof EnumTypeWrapper) {
            final Object val = this.mathComponent.getParameters()
                    .getParameter(param);
            final JComboBox comboBox = new JComboBox(((EnumTypeWrapper) type)
                    .values());
            if (val != null) {
                comboBox.setSelectedItem(val);
            }
            input = comboBox;
            dataExtractor = new DataExtractor() {
                public Object extractData() {
                    return comboBox.getSelectedItem();
                }
            };
        } else if (type instanceof NumberTypeWrapper) {
            final JFormattedTextField ftf = new JFormattedTextField(
                    this.mathComponent.getParameters().getParameter(param));
            input = ftf;
            dataExtractor = new DataExtractor() {
                public Object extractData() {
                    return ftf.getValue();
                }
            };
        } else {
            final Map.Entry<? extends JComponent, ? extends ParametersDialog.DataExtractor> entry = this
                    .setupTextParameter(param);
            input = entry.getKey();
            dataExtractor = entry.getValue();
        }
        input.setToolTipText(param.getOptionDesc());
        this.extractors.put(param, dataExtractor);
        return input;
    }

    private Map.Entry<? extends JComponent, ? extends ParametersDialog.DataExtractor> setupColorParameter(
            final Parameter param) {
        Color color = (Color) this.mathComponent.getParameters()
                .getParameter(param);
        if (color == null) {
            color = Color.white;
        }
        final JColorChooser colorChooser = new JColorChooser(color);
        final JDialog colorChooserDialog = JColorChooser.createDialog(
                ParametersDialog.this, Messages
                        .getString("MathViewer.ParametersDialog.pickColor"),
                true, colorChooser, null, null);
        final JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        box.setBackground(color);
        final JButton btn = new JButton(Messages
                .getString("MathViewer.ParametersDialog.showColorChooser"));
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                colorChooserDialog.setVisible(true);
                box.setBackground(colorChooser.getColor());
                box.revalidate();
            }
        });
        box.add(btn);
        box.add(Box.createHorizontalStrut(ParametersDialog.PADDING));
        return Collections.singletonMap(box, new DataExtractor() {
            public Object extractData() {
                return colorChooser.getColor();
            }
        }).entrySet().iterator().next();
    }

    private Map.Entry<? extends JComponent, ? extends ParametersDialog.DataExtractor> setupTextParameter(
            final Parameter param) {
        final JTextField text = new JTextField(param
                .toString(this.mathComponent.getParameters().getParameter(
                        param)), 50);
        text.setMinimumSize(text.getPreferredSize());
        final Box box = Box.createHorizontalBox();
        box.add(text);
        box
                .add(Box
                        .createHorizontalStrut(ParametersDialog.NUMBER_OF_COLUMNS));
        final JButton btn = new JButton(Messages
                .getString("MathViewer.ParametersDialog.showFontSelection"));
        box.add(btn);
        btn.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
            public void actionPerformed(final ActionEvent e) {
                final FontSelectionDialog fsd = new FontSelectionDialog(
                        ParametersDialog.this, (List<String>) param
                                .fromString(text.getText()));
                fsd.setVisible(true);
                if (fsd.getFontNames().isEmpty()) {
                    text.setText("");
                } else {
                    text.setText(param.toString(fsd.getFontNames()));
                }
            }
        });
        return Collections.singletonMap(box, new DataExtractor() {
            public Object extractData() {
                return param.fromString(text.getText());
            }
        }).entrySet().iterator().next();
    }
}
