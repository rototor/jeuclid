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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sourceforge.jeuclid.swing.JMathComponent;
import net.sourceforge.jeuclid.util.ODFSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * A simple application for viewing MathML documents.
 * 
 * @author Max Berger
 */
public class MathViewer {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(MathViewer.class);

    private static boolean macOS = System.getProperty("mrj.version") != null;

    private JFrame jFrame = null;

    private JPanel jContentPane = null;

    private JMenuBar jJMenuBar = null;

    private JMenu fileMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem openMenuItem = null;

    private JDialog aboutDialog = null;

    private JPanel aboutContentPane = null;

    private JLabel aboutVersionLabel = null;

    private JScrollPane scrollPane = null;

    private JMathComponent mathComponent = null;

    private File lastPath;

    /**
     * This method initializes jFrame
     * 
     * @return javax.swing.JFrame
     */
    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setSize(300, 200);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("JEuclid MathViewer");
        }
        return jFrame;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("File");
            fileMenu.add(getOpenMenuItem());
            if (!macOS) {
                fileMenu.add(getExitMenuItem());
            }
        }
        return fileMenu;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText("Help");
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return exitMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("About");
            aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JDialog aboutDialog = getAboutDialog();
                    aboutDialog.pack();
                    Point loc = getJFrame().getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    aboutDialog.setVisible(true);
                }
            });
        }
        return aboutMenuItem;
    }

    /**
     * This method initializes aboutDialog
     * 
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog(getJFrame(), true);
            aboutDialog.setTitle("About");
            aboutDialog.setContentPane(getAboutContentPane());
        }
        return aboutDialog;
    }

    /**
     * This method initializes aboutContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAboutContentPane() {
        if (aboutContentPane == null) {
            aboutContentPane = new JPanel();
            aboutContentPane.setLayout(new BorderLayout());
            aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
        }
        return aboutContentPane;
    }

    /**
     * This method initializes aboutVersionLabel
     * 
     * @return javax.swing.JLabel
     */
    private JLabel getAboutVersionLabel() {
        if (aboutVersionLabel == null) {
            aboutVersionLabel = new JLabel();
            aboutVersionLabel.setText("Version 1.0 preview");
            aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return aboutVersionLabel;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getOpenMenuItem() {
        if (openMenuItem == null) {
            openMenuItem = new JMenuItem();
            openMenuItem.setText("Open File...");
            openMenuItem
                    .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                            Toolkit.getDefaultToolkit()
                                    .getMenuShortcutKeyMask(), true));
            openMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    openFile();
                }
            });
        }
        return openMenuItem;
    }

    /**
     * carries out the actual file-open procedure.
     */
    protected void openFile() {
        // Have to use AWT file chooser for Mac-friendlyness
        final FileDialog chooser = new FileDialog(jFrame);
        if (lastPath != null) {
            chooser.setDirectory(lastPath.toString());
        }
        chooser.setVisible(true);
        final String fileName = chooser.getFile();
        if (fileName != null) {
            final File selectedFile = new File(chooser.getDirectory(), fileName);
            lastPath = selectedFile.getParentFile();
            try {
                getMathComponent().setDocument(
                        ODFSupport.parseFile(selectedFile));
            } catch (SAXException e) {
                logger.warn(e.getMessage(), e);
                JOptionPane.showMessageDialog(jFrame, e.getMessage(),
                        "Error parsing file", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
                JOptionPane.showMessageDialog(jFrame, e.getMessage(),
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
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getMathComponent());

            if (macOS) {
                scrollPane
                        .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane
                        .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            }
        }
        return scrollPane;
    }

    /**
     * This method initializes mathComponent
     * 
     * @return net.sourceforge.jeuclid.swing.JMathComponent
     */
    private JMathComponent getMathComponent() {
        if (mathComponent == null) {
            mathComponent = new JMathComponent();
        }
        return mathComponent;
    }

    /**
     * Launches this application.
     * 
     * @param args
     *            command line arguments. Ignored.
     */
    public static void main(String[] args) {

        if (macOS) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MathViewer application = new MathViewer();
                application.getJFrame().setVisible(true);
            }
        });
    }

}
