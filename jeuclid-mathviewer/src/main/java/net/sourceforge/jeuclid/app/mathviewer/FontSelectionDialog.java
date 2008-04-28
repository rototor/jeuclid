/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
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
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.jeuclid.font.FontFactory;

/**
 * Dialog that lets user select one or more fonts available in the system.
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
public class FontSelectionDialog extends JDialog {
    // CHECKSTYLE:ON

    private static final int SAMPLE_FONTSIZE = 14;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String PREVIEW_TEXT = "The quick brown fox jumps over the lazy dog. 123456790";

    private final List<String> fontNames = new ArrayList<String>();

    private JList list;

    /**
     * Default Constructor.
     * 
     * @param parent
     *            parent dialog
     * @param currentFontNames
     *            font names to select initially
     */
    public FontSelectionDialog(final Dialog parent,
            final List<String> currentFontNames) {
        super(parent);
        this.init(currentFontNames);
    }

    /**
     * Default Constructor.
     * 
     * @param parent
     *            parent frame
     * @param currentFontNames
     *            font names to select initially
     */
    public FontSelectionDialog(final Frame parent,
            final List<String> currentFontNames) {
        super(parent);
        this.init(currentFontNames);
    }

    private void init(final List<String> currentFontNames) {
        this.setTitle(Messages
                .getString("MathViewer.FontSelectionDialog.title"));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this
                .getContentPane()
                .add(
                        new JLabel(
                                Messages
                                        .getString("MathViewer.FontSelectionDialog.topLabel")),
                        BorderLayout.NORTH);
        this.setupFontsList(currentFontNames);
        this.setupButtons();
        this.pack();
        this.setLocationByPlatform(true);
        this.setModal(true);
    }

    /**
     * @return selected font names if OK was pressed to close the dialog
     */
    public List<String> getFontNames() {
        return this.fontNames;
    }

    private void setupFontsList(final List<String> currentFontNames) {
        // CHECKSTYLE:OFF
        // Vector is a must because JList requires it.
        final Vector<String> allFonts = new Vector<String>(FontFactory
                .getInstance().listFontNames());
        // CHECKSTYLE:ON
        Collections.sort(allFonts);
        this.list = new JList(allFonts);
        final List<Integer> selectedIndicies = new ArrayList<Integer>();
        if (currentFontNames != null && !currentFontNames.isEmpty()) {
            for (final String value : currentFontNames) {
                final int i = Collections.binarySearch(allFonts, value);
                if (i > -1) {
                    selectedIndicies.add(i);
                }
            }
        }
        final int[] selectedIndiciesArr = new int[selectedIndicies.size()];
        for (int i = 0; i < selectedIndiciesArr.length; i++) {
            selectedIndiciesArr[i] = selectedIndicies.get(i);
        }
        this.list.setSelectedIndices(selectedIndiciesArr);
        final JScrollPane scrollPane = new JScrollPane(this.list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        final JTextArea preview = new JTextArea(
                FontSelectionDialog.PREVIEW_TEXT);
        this.list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                preview.setFont(FontFactory.getInstance().getFont(
                        (String) FontSelectionDialog.this.list
                                .getSelectedValue(), Font.PLAIN,
                        FontSelectionDialog.SAMPLE_FONTSIZE));
                preview.revalidate();
            }
        });
        this.getContentPane().add(scrollPane, BorderLayout.WEST);
        this.getContentPane().add(preview, BorderLayout.CENTER);
    }

    private void setupButtons() {
        final JPanel buttonsPanel = new JPanel(new FlowLayout(
                FlowLayout.CENTER));
        final JButton btnOK = new JButton(Messages.getString("MathViewer.ok"));
        btnOK.setMnemonic('O');
        final ActionListener actionListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (e.getSource() == btnOK) {
                    for (Object val : FontSelectionDialog.this.list
                            .getSelectedValues()) {
                        FontSelectionDialog.this.fontNames.add((String) val);
                    }
                }
                FontSelectionDialog.this.dispose();
            }
        };
        btnOK.addActionListener(actionListener);
        buttonsPanel.add(btnOK);
        final JButton btnCancel = new JButton(Messages
                .getString("MathViewer.cancel"));
        btnCancel.setMnemonic('C');
        btnCancel.addActionListener(actionListener);
        buttonsPanel.add(btnCancel);
        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }

}
