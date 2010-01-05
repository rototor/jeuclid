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
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sourceforge.jeuclid.MathMLSerializer;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.swing.JMathComponent;

import org.apache.batik.util.gui.xmleditor.XMLContext;
import org.apache.batik.util.gui.xmleditor.XMLEditorKit;
import org.apache.batik.util.gui.xmleditor.XMLTextEditor;
import org.w3c.dom.Document;

/**
 * Main frame for the MathViewer application.
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
public class MainFrame extends JFrame {
    // CHECKSTYLE:ON
    private static final int DEFAULT_HEIGHT = 400;

    private static final int DEFAULT_WIDTH = 700;

    private static final FileIO FILEIO = FileIO.getInstance();

    // /**
    // * Logger for this class
    // */
    // currently unused.
    // private static final Log LOGGER = LogFactory.getLog(MainFrame.class);

    private static final long serialVersionUID = 1L;

    private static final float FONT_SIZE_MULTIPLICATOR = 1.20f;

    private JPanel jContentPane;

    private JMenuBar jJMenuBar;

    private JMenu fileMenu;

    private JMenu editMenu;

    private JMenu helpMenu;

    private JMenuItem exitMenuItem;

    private JMenuItem unformattedCopyMenuItem;

    private JMenuItem formattedCopyMenuItem;

    private JMenuItem pasteMenuItem;

    private JMenuItem aboutMenuItem;

    private JMenuItem openMenuItem;

    private JDialog aboutDialog;

    private JSplitPane splitPane;

    private JScrollPane scrollPane;

    private JScrollPane scrollPane2;

    private XMLTextEditor xmlEditor;

    private JMathComponent mathComponent;

    private JMenu viewMenu;

    private JMenuItem refreshMenuItem;

    private JMenuItem biggerMenuItem;

    private JMenuItem smallerMenuItem;

    private JMenuItem exportMenuItem;

    private JCheckBoxMenuItem aliasMenuItem;

    private JCheckBoxMenuItem debugMenuItem;

    //==================================================
    // context menu elements
    //==================================================
    
    private JPopupMenu contextPopupMenu;
    
    private JMenu insertMenu;

    private JMenu greekMenu;

    private JMenu logicsMenu;

    private JMenu symbolsMenu;

    private JMenuItem c_refreshMenuItem;

    private JMenuItem tableMenuItem;

    private JMenuItem polynomMenuItem;

    private JMenuItem contentMLMenuItem;

    private JMenuItem orMenuItem;

    private JMenuItem andMenuItem;

    private JMenuItem notMenuItem;

    private JMenuItem alphaMenuItem;

    private JMenuItem betaMenuItem;

    private JMenuItem gammaMenuItem;

    private JMenuItem deltaMenuItem;

    private JMenuItem omegaMenuItem;

    private JMenuItem existsMenuItem;

    private JMenuItem forallMenuItem;

    /**
     * This is the default constructor.
     */
    public MainFrame() {
        super();
        this.initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setJMenuBar(this.getJJMenuBar());
        this.setSize(MainFrame.DEFAULT_WIDTH, MainFrame.DEFAULT_HEIGHT);
        this.setContentPane(this.getJContentPane());
        this.setTitle(Messages.getString("MathViewer.windowTitle")); //$NON-NLS-1$
        this.setLocationByPlatform(true);
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
            this.jContentPane.add(this.getSplitPane(), BorderLayout.CENTER);
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
            this.jJMenuBar.add(this.getEditMenu());
            this.jJMenuBar.add(this.getViewMenu());
            if (!MathViewer.OSX) {
                // This will need to be changed once the Help menu contains
                // more that just the About item.
                this.jJMenuBar.add(this.getHelpMenu());
            }
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
            this.fileMenu.setText(Messages.getString("MathViewer.FileMenu")); //$NON-NLS-1$
            this.fileMenu.add(this.getOpenMenuItem());
            this.fileMenu.add(this.getExportMenuItem());
            if (!MathViewer.OSX) {
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
    private JMenu getEditMenu() {
        if (this.editMenu == null) {
            this.editMenu = new JMenu();
            this.editMenu.setText(Messages.getString("MathViewer.EditMenu")); //$NON-NLS-1$
            this.editMenu.add(this.getUnformattedCopyMenuItem());
            this.editMenu.add(this.getFormattedCopyMenuItem());
            this.editMenu.add(this.getPasteMenuItem());
        }
        return this.editMenu;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (this.helpMenu == null) {
            this.helpMenu = new JMenu();
            this.helpMenu.setText(Messages.getString("MathViewer.helpMenu")); //$NON-NLS-1$
            // If there are more items, please modify getJJMenuBar to always
            // display the help menu and this function to not display about on
            // OS X
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
            this.exitMenuItem.setText(Messages.getString("MathViewer.exit")); //$NON-NLS-1$
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

    private JMenuItem getUnformattedCopyMenuItem() {
        if (this.unformattedCopyMenuItem == null) {
            this.unformattedCopyMenuItem = new JMenuItem();
            this.unformattedCopyMenuItem.setText(Messages
                    .getString("MathViewer.unformattedCopy")); //$NON-NLS-1$
            this.unformattedCopyMenuItem.setAccelerator(KeyStroke
                    .getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));

            this.unformattedCopyMenuItem
                    .addActionListener(new ActionListener() {
                        public void actionPerformed(final ActionEvent e) {
                            MainFrame.this.copyToClipboard(false);
                        }
                    });
        }
        return this.unformattedCopyMenuItem;
    }

    private JMenuItem getFormattedCopyMenuItem() {
        if (this.formattedCopyMenuItem == null) {
            this.formattedCopyMenuItem = new JMenuItem();
            this.formattedCopyMenuItem.setText(Messages
                    .getString("MathViewer.formattedCopy")); //$NON-NLS-1$
            this.formattedCopyMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_C, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask()
                            | InputEvent.SHIFT_DOWN_MASK, true));

            this.formattedCopyMenuItem
                    .addActionListener(new ActionListener() {
                        public void actionPerformed(final ActionEvent e) {
                            MainFrame.this.copyToClipboard(true);
                        }
                    });
        }
        return this.formattedCopyMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getPasteMenuItem() {
        if (this.pasteMenuItem == null) {
            this.pasteMenuItem = new JMenuItem();
            this.pasteMenuItem
                    .setText(Messages.getString("MathViewer.paste")); //$NON-NLS-1$
            this.pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_V, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));

            this.pasteMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MainFrame.this.pasteFromClipboard();
                }
            });
        }
        return this.pasteMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (this.aboutMenuItem == null) {
            this.aboutMenuItem = new JMenuItem();
            this.aboutMenuItem.setText(Messages
                    .getString("MathViewer.aboutMenuItem")); //$NON-NLS-1$
            this.aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MainFrame.this.displayAbout();
                }
            });
        }
        return this.aboutMenuItem;
    }

    /**
     * Display the about dialog.
     */
    public void displayAbout() {
        final JDialog aDialog = MainFrame.this.getAboutDialog();
        aDialog.pack();
        final Point loc = MainFrame.this.getLocation();
        loc
                .translate(
                        (MainFrame.this.getWidth() - aDialog.getWidth()) / 2,
                        0);
        aDialog.setLocation(loc);
        aDialog.setVisible(true);

    }

    /**
     * This method initializes aboutDialog
     * 
     * @return javax.swing.JDialog
     */
    private JDialog getAboutDialog() {
        if (this.aboutDialog == null) {
            this.aboutDialog = new AboutDialog(this);
        }
        return this.aboutDialog;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getOpenMenuItem() {
        if (this.openMenuItem == null) {
            this.openMenuItem = new JMenuItem();
            this.openMenuItem.setText(Messages.getString("MathViewer.open")); //$NON-NLS-1$
            this.openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_O, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));
            this.openMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            MainFrame.this.openFile();
                        }
                    });
        }
        return this.openMenuItem;
    }

    /**
     * Try to load a given file into this frame.
     * 
     * @param f
     *            reference to the file.
     */
    public void loadFile(final File f) {
        final Document doc = MainFrame.FILEIO.loadFile(this, f);
        if (doc != null) {
            this.getMathComponent().setDocument(doc);
            this.getXMLEditor().setText(
                    MathMLSerializer.serializeDocument(doc, false, false));
        }

    }

    /**
     * carries out the actual file-open procedure.
     */
    protected void openFile() {
        final File file = MainFrame.FILEIO.selectFileToOpen(this);
        this.loadFile(file);
    }

    /**
     * This method initializes splitPane
     * 
     * @return {@link JSplitPane}
     */
    private JSplitPane getSplitPane() {
        if (this.splitPane == null) {
            this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this
                    .getScrollPane(), this.getScrollPane2());
            this.splitPane.setOneTouchExpandable(true);
            this.splitPane.setResizeWeight(1.0);
        }
        return this.splitPane;
    }

    /**
     * This method initializes xmlEditor
     * 
     * @return {@link XMLTextEditor}
     */
    private XMLTextEditor getXMLEditor() {
        if (this.xmlEditor == null) {
            this.xmlEditor = new XMLTextEditor();
            this.xmlEditor.setEditorKit(new XMLEditorKit(new XMLContext()));
            /*this.xmlEditor.setText("<?xml version='1.0'?>\n"
                    + "<math xmlns='http://www.w3.org/1998/Math/MathML'>\n"
                    + "</math>");*/
            this.xmlEditor.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    //DOCTYPE for W3C compliance obviously not supported
                    //+ "<!DOCTYPE math PUBLIC -//W3C//DTD MathML 2.0//EN' "
                    //+ "'http://www.w3.org/Math/DTD/mathml2/mathml2.dtd'>\n"
                    + "<math xmlns='http://www.w3.org/1998/Math/MathML'>\n"
                    + "<mrow>\n"
                    + "<mi>a</mi>\n"
                    + "<msup><mi>x</mi><mn>2</mn></msup>\n"
                    + "<mo>+</mo><mi>b</mi>\n"
                    + "<mi>x</mi><mo>+</mo><mi>c</mi>\n"
                    + "<mo>=</mo><mo>0</mo>\n"
                    + "</mrow>\n"
                    + "</math>");

            this.xmlEditor.setEditable(true);
            this.xmlEditor.setComponentPopupMenu(this.getContextPopupMenu());
            this.xmlEditor.getDocument().addDocumentListener(
                    new DocumentListener() {
                        public void changedUpdate(
                                final DocumentEvent documentevent) {
                            MainFrame.this.updateFromTextArea();
                        }

                        public void insertUpdate(
                                final DocumentEvent documentevent) {
                            MainFrame.this.updateFromTextArea();
                        }

                        public void removeUpdate(
                                final DocumentEvent documentevent) {
                            MainFrame.this.updateFromTextArea();
                        }
                    });
            this.xmlEditor.setBackground(Color.WHITE);
        }
        return this.xmlEditor;
    }

    private void updateFromTextArea() {
        try {
            this.getMathComponent().setContent(this.getXMLEditor().getText());
            this.xmlEditor.setBackground(Color.getHSBColor(0.3f, 0.2f, 1.0f));
            // CHECKSTYLE:OFF
            // in this case, we want to explicitly provide catch-all error
            // handling.
        } catch (final RuntimeException e) {
            // CHECKSTYLE:ON
            this.xmlEditor.setBackground(Color.getHSBColor(0f, 0.2f, 1.0f));
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

            if (MathViewer.OSX) {
                this.scrollPane
                        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                this.scrollPane
                        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            }
        }
        return this.scrollPane;
    }

    /**
     * This method initializes scrollPane2
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollPane2() {
        if (this.scrollPane2 == null) {
            this.scrollPane2 = new JScrollPane();
            this.scrollPane2.setViewportView(this.getXMLEditor());

            if (MathViewer.OSX) {
                this.scrollPane2
                        .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                this.scrollPane2
                        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            }
        }
        return this.scrollPane2;
    }

    /**
     * This method initializes mathComponent.
     * 
     * @return net.sourceforge.jeuclid.swing.JMathComponent
     */
    public JMathComponent getMathComponent() {
        if (this.mathComponent == null) {
            this.mathComponent = new JMathComponent();
            this.mathComponent
                    .setContent("<math><mtext>" //$NON-NLS-1$
                            + Messages.getString("MathViewer.noFileLoaded") + "</mtext></math>"); //$NON-NLS-1$ //$NON-NLS-2$
            this.mathComponent.setFocusable(true);
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
            this.viewMenu.setText(Messages.getString("MathViewer.viewMenu")); //$NON-NLS-1$
            this.viewMenu.add(this.getRefreshMenuItem());
            this.viewMenu.add(this.getBiggerMenuItem());
            this.viewMenu.add(this.getSmallerMenuItem());
            this.viewMenu.add(this.getAliasMenuItem());
            this.viewMenu.add(this.getDebugMenuItem());
            if (!MathViewer.OSX) {
                this.viewMenu.add(this.getViewModifyParams());
            }
        }
        return this.viewMenu;
    }

    private JMenuItem getViewModifyParams() {
        final JMenuItem mi = new JMenuItem(Messages
                .getString("MathViewer.viewModifyParams"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.displaySettings();
            }
        });
        return mi;
    }

    /**
     * Display the settings dialog.
     */
    public void displaySettings() {
        new ParametersDialog(MainFrame.this).setVisible(true);
        MainFrame.this.debugMenuItem
                .setSelected(((Boolean) MainFrame.this.mathComponent
                        .getParameters().getParameter(Parameter.DEBUG))
                        .booleanValue());
        MainFrame.this.aliasMenuItem
                .setSelected(((Boolean) MainFrame.this.mathComponent
                        .getParameters().getParameter(Parameter.ANTIALIAS))
                        .booleanValue());

    }

    /**
     * This method initializes refreshMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getRefreshMenuItem()
    {
        if(this.refreshMenuItem == null) {
            this.refreshMenuItem = new JMenuItem();
            this.refreshMenuItem.setText(Messages
                    .getString("MathViewer.textRefresh"));
            this.refreshMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_Y, Toolkit.getDefaultToolkit()
                     .getMenuShortcutKeyMask(), true));
            this.refreshMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            MainFrame.this.updateFromTextArea();
                        }
                    });
        }
        return this.refreshMenuItem;
    }

    /**
     * This method initializes biggerMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getBiggerMenuItem() {
        if (this.biggerMenuItem == null) {
            this.biggerMenuItem = new JMenuItem();
            this.biggerMenuItem.setText(Messages
                    .getString("MathViewer.textBigger")); //$NON-NLS-1$
            this.biggerMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    /*KeyEvent.VK_ADD, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));*/
                    KeyEvent.VK_PLUS, Toolkit.getDefaultToolkit()
                    .getMenuShortcutKeyMask(), true));
            this.biggerMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            final JMathComponent jmc = MainFrame.this
                                    .getMathComponent();
                            jmc.setFontSize(jmc.getFontSize()
                                    * MainFrame.FONT_SIZE_MULTIPLICATOR);
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
            this.smallerMenuItem.setText(Messages
                    .getString("MathViewer.textSmaller")); //$NON-NLS-1$
            this.smallerMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    /*KeyEvent.VK_SUBTRACT, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));*/
                    KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));

            this.smallerMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            final JMathComponent jmc = MainFrame.this
                                    .getMathComponent();
                            jmc.setFontSize(jmc.getFontSize()
                                    / MainFrame.FONT_SIZE_MULTIPLICATOR);
                        }
                    });
        }
        return this.smallerMenuItem;
    }

    /**
     * This method initializes exportMenuItem.
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExportMenuItem() {
        if (this.exportMenuItem == null) {
            this.exportMenuItem = new JMenuItem();
            this.exportMenuItem.setText(Messages
                    .getString("MathViewer.export")); //$NON-NLS-1$
            this.exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_S, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));
            this.exportMenuItem
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(
                                final java.awt.event.ActionEvent e) {
                            MainFrame.this.exportFile();
                        }
                    });
        }
        return this.exportMenuItem;
    }

    /**
     * Carries out the actual export File operation.
     */
    protected void exportFile() {
        MainFrame.FILEIO.saveDocument(this, this.getMathComponent()
                .getDocument(), this.getMathComponent().getParameters());
    }

    /**
     * This method initializes aliasMenuItem
     * 
     * @return javax.swing.JCheckBoxMenuItem
     */
    private JCheckBoxMenuItem getAliasMenuItem() {
        if (this.aliasMenuItem == null) {
            this.aliasMenuItem = new JCheckBoxMenuItem();
            this.aliasMenuItem
                    .setText(Messages.getString("MathViewer.alias")); //$NON-NLS-1$
            this.aliasMenuItem.setSelected((Boolean) LayoutContextImpl
                    .getDefaultLayoutContext().getParameter(
                            Parameter.ANTIALIAS));
            this.aliasMenuItem
                    .addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(
                                final java.awt.event.ItemEvent e) {
                            MainFrame.this.getMathComponent()
                                    .setParameter(
                                            Parameter.ANTIALIAS,
                                            MainFrame.this.aliasMenuItem
                                                    .isSelected());
                        }
                    });
        }
        return this.aliasMenuItem;
    }

    /**
     * This method initializes debugMenuItem
     * 
     * @return javax.swing.JCheckBoxMenuItem
     */
    private JCheckBoxMenuItem getDebugMenuItem() {
        if (this.debugMenuItem == null) {
            this.debugMenuItem = new JCheckBoxMenuItem();
            this.debugMenuItem
                    .setText(Messages.getString("MathViewer.debug")); //$NON-NLS-1$
            this.debugMenuItem.setSelected((Boolean) LayoutContextImpl
                    .getDefaultLayoutContext().getParameter(Parameter.DEBUG));
            this.debugMenuItem
                    .addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(
                                final java.awt.event.ItemEvent e) {
                            MainFrame.this.getMathComponent()
                                    .setParameter(
                                            Parameter.DEBUG,
                                            MainFrame.this.debugMenuItem
                                                    .isSelected());
                        }
                    });
        }
        return this.debugMenuItem;
    }

    private void pasteFromClipboard() {
        final Transferable content = Toolkit.getDefaultToolkit()
                .getSystemClipboard().getContents(null);
        if (content != null
                && content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                final String newContent = (String) content
                        .getTransferData(DataFlavor.stringFlavor);
                this.getMathComponent().setContent(newContent);
                this.getXMLEditor().setText(newContent);
                // CHECKSTYLE:OFF
                // in this case, we want to explicitly provide catch-all error
                // handling.
            } catch (final Exception e) {
                // CHECKSTYLE:ON
                JOptionPane.showMessageDialog(this, new String[] {
                        Messages.getString("MathViewer.pasteFailure"),
                        e.toString(), }, Messages
                        .getString("MathViewer.error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void copyToClipboard(final boolean formatted) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(MathMLSerializer.serializeDocument(
                        this.mathComponent.getDocument(), false, formatted)),
                null);
    }

    private JMenuItem getCRefreshMenuItem() {
        if (this.c_refreshMenuItem == null) {
            this.c_refreshMenuItem = new JMenuItem();
            this.c_refreshMenuItem.setText(Messages
                    .getString("MathViewer.cRefreshMenuItem")); //$NON-NLS-1$
            this.c_refreshMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MainFrame.this.updateFromTextArea();
                }
            });
        }
        return this.c_refreshMenuItem;
    }

    private JMenuItem getTableMenuItem() {
        if (this.tableMenuItem == null) {
            this.tableMenuItem = new JMenuItem();
            this.tableMenuItem.setText(Messages
                    .getString("MathViewer.tableMenuItem")); //$NON-NLS-1$
            this.tableMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    InsertTableDialog dlg = new InsertTableDialog(MainFrame.this, true);
                    dlg.setVisible(true);

                    if(dlg.getMathMLText() != null) {
                        insertMacro(dlg.getMathMLText());
                    }
                }
            });
        }
        return this.tableMenuItem;
    }

    private JMenuItem getPolynomMenuItem() {
        if (this.polynomMenuItem == null) {
            this.polynomMenuItem = new JMenuItem();
            this.polynomMenuItem.setText(Messages
                    .getString("MathViewer.polynomMenuItem")); //$NON-NLS-1$
            this.polynomMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    InsertPolynomDialog dlg = new InsertPolynomDialog(MainFrame.this, true);
                    dlg.setVisible(true);
                    if(dlg.getMathMLText() != null) {
                        insertMacro(dlg.getMathMLText());
                    }
                }
            });
        }
        return this.polynomMenuItem;
    }

    private JMenuItem getContentMLMenuItem() {
        if (this.contentMLMenuItem == null) {
            this.contentMLMenuItem = new JMenuItem();
            this.contentMLMenuItem.setText(Messages
                    .getString("MathViewer.contentMLMenuItem")); //$NON-NLS-1$
            this.contentMLMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    InsertCMLDialog dlg = new InsertCMLDialog(MainFrame.this, true);
                    dlg.setVisible(true);

                    if(dlg.getMathMLText() != null) {
                        insertMacro(dlg.getMathMLText());
                    }
                }
            });
        }
        return this.contentMLMenuItem;
    }

    private JMenuItem getOrMenuItem() {
        if (this.orMenuItem == null) {
            this.orMenuItem = new JMenuItem();
            this.orMenuItem.setText(Messages
                    .getString("MathViewer.orMenuItem")); //$NON-NLS-1$
            this.orMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<apply><or/><ci>a</ci><ci>b</ci></apply>");
                }
            });
        }
        return this.orMenuItem;
    }

    private JMenuItem getAndMenuItem() {
        if (this.andMenuItem == null) {
            this.andMenuItem = new JMenuItem();
            this.andMenuItem.setText(Messages
                    .getString("MathViewer.andMenuItem")); //$NON-NLS-1$
            this.andMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<apply><and/><ci>a</ci><ci>b</ci></apply>");
                }
            });
        }
        return this.andMenuItem;
    }

    private JMenuItem getNotMenuItem() {
        if (this.notMenuItem == null) {
            this.notMenuItem = new JMenuItem();
            this.notMenuItem.setText(Messages
                    .getString("MathViewer.notMenuItem")); //$NON-NLS-1$
            this.notMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<apply><not/><ci>a</ci></apply>");
                }
            });
        }
        return this.notMenuItem;
    }


    private JMenuItem getAlphaMenuItem() {
        if (this.alphaMenuItem == null) {
            this.alphaMenuItem = new JMenuItem();
            this.alphaMenuItem.setText(Messages
                    .getString("MathViewer.alphaMenuItem")); //$NON-NLS-1$
            this.alphaMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<mi>&#x003b1;</mi>");
                }
            });
        }
        return this.alphaMenuItem;
    }

    private JMenuItem getBetaMenuItem() {
        if (this.betaMenuItem == null) {
            this.betaMenuItem = new JMenuItem();
            this.betaMenuItem.setText(Messages
                    .getString("MathViewer.betaMenuItem")); //$NON-NLS-1$
            this.betaMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<mi>&#x003b2;</mi>");
                }
            });
        }
        return this.betaMenuItem;
    }

    private JMenuItem getGammaMenuItem() {
        if (this.gammaMenuItem == null) {
            this.gammaMenuItem = new JMenuItem();
            this.gammaMenuItem.setText(Messages
                    .getString("MathViewer.gammaMenuItem")); //$NON-NLS-1$
            this.gammaMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<mi>&#x003b3;</mi>");
                }
            });
        }
        return this.gammaMenuItem;
    }

    private JMenuItem getDeltaMenuItem() {
        if (this.deltaMenuItem == null) {
            this.deltaMenuItem = new JMenuItem();
            this.deltaMenuItem.setText(Messages
                    .getString("MathViewer.deltaMenuItem")); //$NON-NLS-1$
            this.deltaMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<mi>&#x003b4;</mi>");
                }
            });
        }
        return this.deltaMenuItem;
    }

    private JMenuItem getOmegaMenuItem() {
        if (this.omegaMenuItem == null) {
            this.omegaMenuItem = new JMenuItem();
            this.omegaMenuItem.setText(Messages
                    .getString("MathViewer.omegaMenuItem")); //$NON-NLS-1$
            this.omegaMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<mi>&#x003c9;</mi>");
                }
            });
        }
        return this.omegaMenuItem;
    }

    private JMenuItem getExistsMenuItem() {
        if (this.existsMenuItem == null) {
            this.existsMenuItem = new JMenuItem();
            this.existsMenuItem.setText(Messages
                    .getString("MathViewer.existsMenuItem")); //$NON-NLS-1$
            this.existsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<apply><exists/></apply>");
                }
            });
        }
        return this.existsMenuItem;
    }

    private JMenuItem getForAllMenuItem() {
        if (this.forallMenuItem == null) {
            this.forallMenuItem = new JMenuItem();
            this.forallMenuItem.setText(Messages
                    .getString("MathViewer.forallMenuItem")); //$NON-NLS-1$
            this.forallMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    insertMacro("<apply><forall/></apply>");
                }
            });
        }
        return this.forallMenuItem;
    }

    private JMenu getGreekMenu() {
        if (this.greekMenu == null) {
            this.greekMenu = new JMenu();
            this.greekMenu.setText(Messages.getString("MathViewer.GreekMenu")); //$NON-NLS-1$
            this.greekMenu.add(this.getAlphaMenuItem());
            this.greekMenu.add(this.getBetaMenuItem());
            this.greekMenu.add(this.getGammaMenuItem());
            this.greekMenu.add(this.getDebugMenuItem());
            this.greekMenu.add(this.getOmegaMenuItem());
        }
        return this.greekMenu;
    }

    private JMenu getLogicsMenu() {
        if(this.logicsMenu == null) {
            this.logicsMenu = new JMenu();
            this.logicsMenu.setText(Messages.getString("MathViewer.LogicsMenu")); //$NON-NLS-1$
            this.logicsMenu.add(this.getAndMenuItem());
            this.logicsMenu.add(this.getOrMenuItem());
            this.logicsMenu.add(this.getNotMenuItem());
        }
        return this.logicsMenu;
    }

    private JMenu getSymbolsMenu() {
        if(this.symbolsMenu == null) {
            this.symbolsMenu = new JMenu();
            this.symbolsMenu.setText(Messages.getString("MathViewer.SymbolsMenu")); //$NON-NLS-1$
            this.symbolsMenu.add(this.getForAllMenuItem());
            this.symbolsMenu.add(this.getExistsMenuItem());
        }
        return this.symbolsMenu;
    }

    private JMenu getInsertMenu() {
        if (this.insertMenu == null) {
            this.insertMenu = new JMenu();
            this.insertMenu.setText(Messages.getString("MathViewer.InsertMenu")); //$NON-NLS-1$
            this.insertMenu.add(this.getTableMenuItem());
            this.insertMenu.add(this.getPolynomMenuItem());
            this.insertMenu.add(this.getContentMLMenuItem());
            this.insertMenu.add(this.getGreekMenu());
            this.insertMenu.add(this.getLogicsMenu());
            this.insertMenu.add(this.getSymbolsMenu());
        }
        return this.insertMenu;
    }

    private JPopupMenu getContextPopupMenu() {
        if(this.contextPopupMenu == null) {
            this.contextPopupMenu = new JPopupMenu();
            this.contextPopupMenu.add(this.getCRefreshMenuItem());
            this.contextPopupMenu.add(this.getInsertMenu());
        }
        return this.contextPopupMenu;
    }

    private void insertMacro(final String macroText)
    {
        int pos = getXMLEditor().getCaretPosition();
        String s1 = getXMLEditor().getText().substring(0,pos);
        String s2 = getXMLEditor().getText().substring(pos);
        s1 += macroText + s2;
        getXMLEditor().setText(s1);
        updateFromTextArea();
    }
}
