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

package net.sourceforge.jeuclid.app;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sourceforge.jeuclid.swing.JMathComponent;
import net.sourceforge.jeuclid.util.MathMLParserSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * A simple application for viewing MathML documents.
 * 
 * @author Max Berger
 */
public final class MathViewer {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(MathViewer.class);

    private static boolean macOS = System.getProperty("mrj.version") != null;

    private static final float FONT_SIZE_MULTIPLICATOR = 1.20f;

    private JFrame jFrame;

    private JPanel jContentPane;

    private JMenuBar jJMenuBar;

    private JMenu fileMenu;

    private JMenu helpMenu;

    private JMenuItem exitMenuItem;

    private JMenuItem aboutMenuItem;

    private JMenuItem openMenuItem;

    private JDialog aboutDialog;

    private JPanel aboutContentPane;

    private JLabel aboutVersionLabel;

    private JScrollPane scrollPane;

    private JMathComponent mathComponent;

    private File lastPath;

    private JMenu viewMenu;

    private JMenuItem biggerMenuItem;

    private JMenuItem smallerMenuItem;

    private MathViewer() {
        // Empty on purpose.
    }

    /**
     * This method initializes jFrame
     * 
     * @return javax.swing.JFrame
     */
    private JFrame getJFrame() {
        if (this.jFrame == null) {
            this.jFrame = new JFrame();
            this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.jFrame.setJMenuBar(this.getJJMenuBar());
            this.jFrame.setSize(300, 200);
            this.jFrame.setContentPane(this.getJContentPane());
            this.jFrame.setTitle("JEuclid MathViewer");
        }
        return this.jFrame;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new BorderLayout());
            this.jContentPane.add(this.getScrollPane(), BorderLayout.CENTER);
        }
        return this.jContentPane;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (this.jJMenuBar == null) {
            this.jJMenuBar = new JMenuBar();
            this.jJMenuBar.add(this.getFileMenu());
            this.jJMenuBar.add(this.getViewMenu());
            this.jJMenuBar.add(this.getHelpMenu());
        }
        return this.jJMenuBar;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (this.fileMenu == null) {
            this.fileMenu = new JMenu();
            this.fileMenu.setText("File");
            this.fileMenu.add(this.getOpenMenuItem());
            if (!MathViewer.macOS) {
                this.fileMenu.add(this.getExitMenuItem());
            }
        }
        return this.fileMenu;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (this.helpMenu == null) {
            this.helpMenu = new JMenu();
            this.helpMenu.setText("Help");
            this.helpMenu.add(this.getAboutMenuItem());
        }
        return this.helpMenu;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (this.exitMenuItem == null) {
            this.exitMenuItem = new JMenuItem();
            this.exitMenuItem.setText("Exit");
            this.exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_Q, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));

            this.exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return this.exitMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (this.aboutMenuItem == null) {
            this.aboutMenuItem = new JMenuItem();
            this.aboutMenuItem.setText("About");
            this.aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final JDialog aDialog = MathViewer.this.getAboutDialog();
                    aDialog.pack();
                    final Point loc = MathViewer.this.getJFrame()
                            .getLocation();
                    loc.translate(20, 20);
                    aDialog.setLocation(loc);
                    aDialog.setVisible(true);
                }
            });
        }
        return this.aboutMenuItem;
    }

    /**
     * This method initializes aboutDialog
     * 
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog() {
        if (this.aboutDialog == null) {
            this.aboutDialog = new JDialog(this.getJFrame(), true);
            this.aboutDialog.setTitle("About");
            this.aboutDialog.setContentPane(this.getAboutContentPane());
        }
        return this.aboutDialog;
    }

    /**
     * This method initializes aboutContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPane() {
        if (this.aboutContentPane == null) {
            this.aboutContentPane = new JPanel();
            this.aboutContentPane.setLayout(new BorderLayout());
            this.aboutContentPane.add(this.getAboutVersionLabel(),
                    BorderLayout.CENTER);
        }
        return this.aboutContentPane;
    }

    /**
     * This method initializes aboutVersionLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel() {
        if (this.aboutVersionLabel == null) {
            this.aboutVersionLabel = new JLabel();
            this.aboutVersionLabel.setText("Version 1.0 preview");
            this.aboutVersionLabel
                    .setHorizontalAlignment(SwingConstants.CENTER);
        }
        return this.aboutVersionLabel;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getOpenMenuItem() {
        if (this.openMenuItem == null) {
            this.openMenuItem = new JMenuItem();
            this.openMenuItem.setText("Open File...");
            this.openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_O, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));
            this.openMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            MathViewer.this.openFile();
                        }
                    });
        }
        return this.openMenuItem;
    }

    /**
     * carries out the actual file-open procedure.
     */
    protected void openFile() {
        // Have to use AWT file chooser for Mac-friendlyness
        final FileDialog chooser = new FileDialog(this.jFrame);
        if (this.lastPath != null) {
            chooser.setDirectory(this.lastPath.toString());
        }
        chooser.setVisible(true);
        final String fileName = chooser.getFile();
        if (fileName != null) {
            final File selectedFile = new File(chooser.getDirectory(),
                    fileName);
            this.lastPath = selectedFile.getParentFile();
            try {
                this.getMathComponent().setDocument(
                        MathMLParserSupport.parseFile(selectedFile));
            } catch (final SAXException e) {
                MathViewer.LOGGER.warn(e.getMessage(), e);
                JOptionPane.showMessageDialog(this.jFrame, e.getMessage(),
                        "Error parsing file", JOptionPane.ERROR_MESSAGE);
            } catch (final IOException e) {
                MathViewer.LOGGER.warn(e.getMessage(), e);
                JOptionPane.showMessageDialog(this.jFrame, e.getMessage(),
                        "Error accessing file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method initializes scrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollPane() {
        if (this.scrollPane == null) {
            this.scrollPane = new JScrollPane();
            this.scrollPane.setViewportView(this.getMathComponent());

            if (MathViewer.macOS) {
                this.scrollPane
                        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                this.scrollPane
                        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            }
        }
        return this.scrollPane;
    }

    /**
     * This method initializes mathComponent
     * 
     * @return net.sourceforge.jeuclid.swing.JMathComponent
     */
    private JMathComponent getMathComponent() {
        if (this.mathComponent == null) {
            this.mathComponent = new JMathComponent();
            this.mathComponent
                    .setContent("<math><mtext>Please load a MathML file</mtext></math>");
        }
        return this.mathComponent;
    }

    /**
     * This method initializes viewMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getViewMenu() {
        if (this.viewMenu == null) {
            this.viewMenu = new JMenu();
            this.viewMenu.setText("View");
            this.viewMenu.add(this.getBiggerMenuItem());
            this.viewMenu.add(this.getSmallerMenuItem());
        }
        return this.viewMenu;
    }

    /**
     * This method initializes biggerMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getBiggerMenuItem() {
        if (this.biggerMenuItem == null) {
            this.biggerMenuItem = new JMenuItem();
            this.biggerMenuItem.setText("Make Text Bigger");
            this.biggerMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_ADD, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));
            this.biggerMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            final JMathComponent jmc = MathViewer.this
                                    .getMathComponent();
                            jmc.setFontSize(jmc.getFontSize()
                                    * MathViewer.FONT_SIZE_MULTIPLICATOR);
                        }
                    });
        }
        return this.biggerMenuItem;
    }

    /**
     * This method initializes smallerMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSmallerMenuItem() {
        if (this.smallerMenuItem == null) {
            this.smallerMenuItem = new JMenuItem();
            this.smallerMenuItem.setText("Make Text Smaller");
            this.smallerMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_SUBTRACT, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));

            this.smallerMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            final JMathComponent jmc = MathViewer.this
                                    .getMathComponent();
                            jmc.setFontSize(jmc.getFontSize()
                                    / MathViewer.FONT_SIZE_MULTIPLICATOR);
                        }
                    });
        }
        return this.smallerMenuItem;
    }

    /**
     * Launches this application.
     * 
     * @param args
     *            command line arguments. Ignored.
     */
    public static void main(final String[] args) {

        if (MathViewer.macOS) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            // ignore
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MathViewer application = new MathViewer();
                application.getJFrame().setVisible(true);
            }
        });
    }

}
