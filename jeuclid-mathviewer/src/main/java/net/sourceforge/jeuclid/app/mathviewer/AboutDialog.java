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
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.sourceforge.jeuclid.LayoutContext;

/**
 * About Dialog for MathViewer.
 * 
 * @version $Revision$
 */
public final class AboutDialog extends JDialog {

    private static final int LARGE_FONT = 14;

    private static final int SMALL_FONT = 12;

    private static final String ABOUT_FONT = "SansSerif";

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane;

    private JLabel iconLabel;

    private JLabel jeuclidLabel;

    private JLabel wwwLabel;

    /**
     * @param owner
     *            Owner for this frame.
     */
    public AboutDialog(final Frame owner) {
        super(owner);
        this.initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setModal(true);
        this.setResizable(false);
        this.setContentPane(this.getJContentPane());
        this.setTitle(Messages.getString("MathViewer.aboutWindowTitle")); //$NON-NLS-1$
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_V && e.isAltDown()) {
                    JOptionPane.showMessageDialog(AboutDialog.this,
                            "Version: "
                                    + LayoutContext.class.getPackage()
                                            .getImplementationVersion());
                }
            }
        });
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.wwwLabel = new JLabel();
            this.wwwLabel.setText(" http://jeuclid.sourceforge.net ");
            this.wwwLabel.setFont(new Font(AboutDialog.ABOUT_FONT,
                    Font.PLAIN, AboutDialog.SMALL_FONT));
            this.wwwLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.jeuclidLabel = new JLabel();
            this.jeuclidLabel.setText("JEuclid MathViewer");
            this.jeuclidLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.jeuclidLabel.setFont(new Font(AboutDialog.ABOUT_FONT,
                    Font.BOLD, AboutDialog.LARGE_FONT));
            this.iconLabel = new JLabel();
            this.iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.iconLabel.setIcon(new ImageIcon(this.getClass().getResource(
                    "/icons/jeuclid_128x128.png")));
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new BorderLayout());
            this.jContentPane.add(this.iconLabel, BorderLayout.NORTH);
            this.jContentPane.add(this.jeuclidLabel, BorderLayout.CENTER);
            this.jContentPane.add(this.wwwLabel, BorderLayout.SOUTH);
        }
        return this.jContentPane;
    }

}
