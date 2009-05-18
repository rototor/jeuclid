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

package cViewer;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import net.sourceforge.jeuclid.MathMLSerializer;
import cViewer.JMathComponent.ZerlegeAction;

public class MathFrame extends JFrame {

    private static final int DEFAULT_HEIGHT = 250;

    private static final int DEFAULT_WIDTH = 1000;

    private static final long serialVersionUID = 20090301L;

    private File lastPath;

    private JPanel jContentPane;

    private JMenuBar jMenuBar;

    private JMenu fileMenu;

    private JMenu helpMenu;

    private JMenuItem openMenuItem;

    private JMenuItem exportMenuItem;

    private JMenuItem aboutMenuItem;

    private JMenuItem howToMenuItem;

    private JMenuItem treeViewItem;

    private JMenuItem optionItem;

    private JSplitPane splitPaneVM;

    private JSplitPane splitPaneCV;

    private JScrollPane scrollPane1;

    private JScrollPane scrollPane;

    private MyOptionsDialog optionsDialog;

    private TransferObject stateTransfer;

    private JMathComponent mathComponent;

    private JMathViewer viewComponent;

    private JTextField textField;

    private CountLabel countLabel;

    private Counter counter;

    /**
     * This is the default constructor.
     */
    public MathFrame() {
        this.setTitle("KAS - Kein Algebrasystem");
        this.setSize(MathFrame.DEFAULT_WIDTH, MathFrame.DEFAULT_HEIGHT);
        this.setJMenuBar(this.getJMenuBar());
        this.setContentPane(this.getJContentPane());
        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(final WindowEvent e) {
                MathFrame.this.getMathComponent().requestFocusInWindow();
            }
        });
    }

    @Override
    public JMenuBar getJMenuBar() {
        if (this.jMenuBar == null) {
            this.jMenuBar = new JMenuBar();
            this.jMenuBar.add(this.getFileMenu());
            this.jMenuBar.add(this.getHelpMenu());
        }
        return this.jMenuBar;
    }

    private JMenu getHelpMenu() {
        if (this.helpMenu == null) {
            this.helpMenu = new JMenu();
            this.helpMenu.setText("Hilfe");
            this.helpMenu.add(this.getHowToItem());
            this.helpMenu.add(this.getAboutMenuItem());
            this.helpMenu.add(this.getTreeViewItem());
            this.helpMenu.add(this.getOptionMenuItem());
        }
        return this.helpMenu;
    }

    private JMenuItem getTreeViewItem() {
        if (this.treeViewItem == null) {
            this.treeViewItem = new JMenuItem();
            this.treeViewItem.setText("Ansicht als Baum");
            this.treeViewItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.displayTreeView();
                }
            });
        }
        return this.treeViewItem;
    }

    private void displayTreeView() {
        final JFrame aDialog = ViewerFactory.getInst().getTreeViewDialog();
        aDialog.pack();
        final Point loc = this.getLocation();
        loc.translate(this.getWidth(), 0);
        aDialog.setLocation(loc);
        aDialog.setVisible(true);
    }

    private JMenuItem getHowToItem() {
        if (this.howToMenuItem == null) {
            this.howToMenuItem = new JMenuItem();
            this.howToMenuItem.setText("Tastatur und Maussteuerung");
            this.howToMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    ViewerFactory.getInst().getHowToDialog();
                }
            });
        }
        return this.howToMenuItem;
    }

    private JMenuItem getAboutMenuItem() {
        if (this.aboutMenuItem == null) {
            this.aboutMenuItem = new JMenuItem();
            this.aboutMenuItem.setText("About KAS");
            this.aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    ViewerFactory.getInst().getAboutDialog();
                }
            });
        }
        return this.aboutMenuItem;
    }

    private JMenuItem getOptionMenuItem() {
        if (this.optionItem == null) {
            this.optionItem = new JMenuItem();
            this.optionItem.setText("Options ...");
            this.optionItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final Dialog dialog = MathFrame.this.getOptionsDialog();
                    dialog.setLocationRelativeTo(MathFrame.this);
                    dialog.setVisible(true);
                }
            });
        }
        return this.optionItem;
    }

    public MyOptionsDialog getOptionsDialog() {
        if (this.optionsDialog == null) {
            this.optionsDialog = new MyOptionsDialog(this.getStateTransfer());
            this.optionsDialog.pack();
        }
        return this.optionsDialog;
    }

    public TransferObject getStateTransfer() {
        if (this.stateTransfer == null) {
            final String[] strings = new String[4];
            strings[0] = "Ganze Zahlen selber berechnen?";
            strings[1] = "Gemischte Zahlen selber berechnen?";
            strings[2] = "unbelegt";
            strings[3] = "unbelegt";
            this.stateTransfer = new TransferObject(strings);
            this.stateTransfer.setResult("0123");
        }
        return this.stateTransfer;
    }

    private JMenu getFileMenu() {
        if (this.fileMenu == null) {
            this.fileMenu = new JMenu();
            this.fileMenu.setText("Datei");
            this.fileMenu.add(this.getOpenMenuItem());
            this.fileMenu.add(this.getExportMenuItem());
        }
        return this.fileMenu;
    }

    // Die FileMenuItems
    private JMenuItem getOpenMenuItem() {
        if (this.openMenuItem == null) {
            this.openMenuItem = new JMenuItem();
            this.openMenuItem.setText("Datei öffnen ...");
            this.openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_O, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));
            this.openMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.openFile();
                }
            });
        }
        return this.openMenuItem;
    }

    private void openFile() {
        final File file = this.selectFileToOpen(this);
        this.setTitle("KAS " + file.getName());
        this.getCounter().reset();
        BufferedReader r;
        String result = "";
        String line;
        try {
            r = new BufferedReader(new FileReader(file));
            while ((line = r.readLine()) != null) {
                result = result + line;
            }
            r.close();
        } catch (final IOException e) {
            System.out.println("Fehler beim Lesen der Datei");
        }
        this.getMathComponent().setNewContent(result);
        this.getViewComponent().setContent(result);
        ViewerFactory.getInst().getTreeViewDialog().update();
        this.getMathComponent().requestFocusInWindow();
    }

    private File selectFileToOpen(final Frame parent) {
        File selectedFile;
        final JFileChooser fChooser = new JFileChooser(this.lastPath);
        if (fChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fChooser.getSelectedFile();
        } else {
            selectedFile = null;
        }
        if (selectedFile != null) {
            this.lastPath = selectedFile.getParentFile();
        }
        return selectedFile;
    }

    private JMenuItem getExportMenuItem() {
        if (this.exportMenuItem == null) {
            this.exportMenuItem = new JMenuItem();
            this.exportMenuItem.setText("Exportieren");
            this.exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_S, Toolkit.getDefaultToolkit()
                            .getMenuShortcutKeyMask(), true));
            this.exportMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.exportFile();
                }
            });
        }
        return this.exportMenuItem;
    }

    private void exportFile() {
        final JFileChooser fChooser = new JFileChooser(this.lastPath);
        if (fChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fChooser.getSelectedFile();
            if (selectedFile != null) {
                final String fileName = selectedFile.getAbsolutePath();
                JMathElementHandler.removeCalcTyp(this.getMathComponent()
                        .getDocument());
                final String s = MathMLSerializer.serializeDocument(this
                        .getMathComponent().getDocument(), false, false);
                try {
                    final BufferedWriter bw = new BufferedWriter(
                            new FileWriter(fileName));
                    bw.write(s);
                    bw.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        JMathElementHandler.parseDom(this.getMathComponent().getDocument());
    }

    public JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new BorderLayout());
            this.jContentPane.add(this.getButtonPanel(), BorderLayout.WEST);
            this.jContentPane.add(this.getSplitPaneVM(), BorderLayout.CENTER);
        }
        return this.jContentPane;
    }

    private JSplitPane getSplitPaneVM() {
        if (this.splitPaneVM == null) {
            this.splitPaneVM = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this
                    .getSplitPaneCountView(), this.getScrollPane());
            this.splitPaneVM.setOneTouchExpandable(true);
            this.splitPaneVM.setResizeWeight(0.1);
        }
        return this.splitPaneVM;
    }

    private JScrollPane getScrollPane() {
        if (this.scrollPane == null) {
            this.scrollPane = new JScrollPane();
            this.scrollPane.setViewportView(this.getMathComponent());
        }
        return this.scrollPane;
    }

    private JScrollPane getScrollPane1() {
        if (this.scrollPane1 == null) {
            this.scrollPane1 = new JScrollPane();
            this.scrollPane1.setViewportView(this.getViewComponent());
        }
        return this.scrollPane1;
    }

    public JMathViewer getViewComponent() {
        if (this.viewComponent == null) {
            this.viewComponent = new JMathViewer();
        }
        return this.viewComponent;
    }

    public JSplitPane getSplitPaneCountView() {
        if (this.splitPaneCV == null) {
            this.splitPaneCV = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    this.getCountLabel(), this.getScrollPane1());
            this.splitPaneCV.setOneTouchExpandable(true);
            this.splitPaneCV.setResizeWeight(0);
        }
        return this.splitPaneCV;
    }

    public JLabel getCountLabel() {
        if (this.countLabel == null) {
            this.countLabel = new CountLabel();
            final Font curFont = this.countLabel.getFont();
            this.countLabel.setFont(new Font(curFont.getFontName(), curFont
                    .getStyle(), 20));
            this.countLabel.setPreferredSize(new Dimension(40, 20));
            this.countLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.countLabel.setText("" + this.getCounter().getCount());
            this.getCounter().addObserver(this.countLabel);
        }
        return this.countLabel;
    }

    public class CountLabel extends JLabel implements Observer {
        public static final long serialVersionUID = 20090429;

        public void update(final Observable obs, final Object o) {
            this.setText("" + ((Counter) obs).getCount());
        }
    }

    public Counter getCounter() {
        if (this.counter == null) {
            this.counter = new Counter();
        }
        return this.counter;
    }

    public JMathComponent getMathComponent() {
        if (this.mathComponent == null) {
            this.mathComponent = ViewerFactory.getInst().getMathComponent();
            this.mathComponent.initialize();
            this.mathComponent.addKeyListener(new JMathKeyListener(
                    this.mathComponent));
            final JMathMouseListener jMML = new JMathMouseListener(
                    this.mathComponent, this.getTextField());
            this.mathComponent.addMouseListener(jMML);
            this.mathComponent.addMouseMotionListener(jMML);
        }
        return this.mathComponent;
    }

    public JPanel getButtonPanel() {
        final JPanel result = new JPanel();
        result.setLayout(new GridLayout(9, 2));
        // Tree Walker
        result.add(new JButton(this.getMathComponent().getActionByName(
                "ZoomOut")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "ZoomIn")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "GeheZurueck")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "GeheWeiter")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Selection+")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Selection-")));
        // Einfache Änderungen
        result.add(new JButton(this.getMathComponent().getActionByName(
                "BewegeLinks")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "BewegeRechts")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Klammere")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Entklammere")));
        // Änderungen an einem Element
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Aendern")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Meins")));
        // Komplexere Änderungen
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Rausziehen")));
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Verbinden")));
        final ZerlegeAction z = (ZerlegeAction) this.getMathComponent()
                .getActionByName("Splitten");
        result.add(this.getTextField());
        z.textField = this.getTextField();
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Splitten")));
        // Undo and Redo (simple)
        result.add(new JButton(this.getMathComponent()
                .getActionByName("Redo")));
        result.add(new JButton(this.getMathComponent()
                .getActionByName("Undo")));
        return result;
    }

    public JTextField getTextField() {
        if (this.textField == null) {
            this.textField = new JTextField(6);
            final Font f = new Font("DialogInput", 1, 16);
            this.textField.setFont(f);
        }
        return this.textField;
    }

}
