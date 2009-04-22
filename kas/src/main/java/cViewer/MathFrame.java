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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.sourceforge.jeuclid.MathMLSerializer;
import cViewer.JMathComponent.ZerlegeAction;

public class MathFrame extends JFrame {

    private static final int DEFAULT_HEIGHT = 250;

    private static final int DEFAULT_WIDTH = 800;

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

    private AboutDialog aboutDialog;

    private HowToDialog howToDialog;

    private TreeViewDialog treeViewDialog;

    private JSplitPane splitPane;

    private JScrollPane scrollPane1;

    private JScrollPane scrollPane;

    private JMathComponent mathComponent;

    private JMathViewer viewComponent;

    public JTextField textField;

    /**
     * This is the default constructor.
     */
    public MathFrame() {
        this.setTitle("KAS - Kein Algebrasystem A");
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
        final JFrame aDialog = this.getTreeViewDialog();
        aDialog.pack();
        final Point loc = this.getLocation();
        loc.translate(this.getWidth(), 0);
        aDialog.setLocation(loc);
        aDialog.setVisible(true);
    }

    public TreeViewDialog getTreeViewDialog() {
        if (this.treeViewDialog == null) {
            this.treeViewDialog = new TreeViewDialog(this);
        }
        return this.treeViewDialog;
    }

    private JMenuItem getHowToItem() {
        if (this.howToMenuItem == null) {
            this.howToMenuItem = new JMenuItem();
            this.howToMenuItem.setText("Tastatur und Maussteuerung");
            this.howToMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.displayHowTo();
                }
            });
        }
        return this.howToMenuItem;
    }

    private void displayHowTo() {
        final JDialog aDialog = this.getHowToDialog();
        aDialog.pack();
        final Point loc = this.getLocation();
        loc.translate((this.getWidth() - aDialog.getWidth()) / 2, 0);
        aDialog.setLocation(loc);
        aDialog.setVisible(true);
    }

    private JDialog getHowToDialog() {
        if (this.howToDialog == null) {
            this.howToDialog = new HowToDialog(this);
        }
        return this.howToDialog;
    }

    private JMenuItem getAboutMenuItem() {
        if (this.aboutMenuItem == null) {
            this.aboutMenuItem = new JMenuItem();
            this.aboutMenuItem.setText("About KAS");
            this.aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    MathFrame.this.displayAbout();
                }
            });
        }
        return this.aboutMenuItem;
    }

    public void displayAbout() {
        final JDialog aDialog = this.getAboutDialog();
        aDialog.pack();
        final Point loc = this.getLocation();
        loc.translate((this.getWidth() - aDialog.getWidth()) / 2, 0);
        aDialog.setLocation(loc);
        aDialog.setVisible(true);
    }

    private JDialog getAboutDialog() {
        if (this.aboutDialog == null) {
            this.aboutDialog = new AboutDialog(this);
        }
        return this.aboutDialog;
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
        this.getMathComponent().setContent(result);
        this.getViewComponent().setContent(result);
        this.getTreeViewDialog().update();
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
            this.jContentPane.add(this.getSplitPane(), BorderLayout.CENTER);
            this.getMathComponent().addKeyListener(
                    new JMathKeyListener(this.getMathComponent()));
            this.getMathComponent().addMouseListener(
                    new JMathMouseListener(this.getMathComponent(), this
                            .getTextField()));
        }
        return this.jContentPane;
    }

    private JSplitPane getSplitPane() {
        if (this.splitPane == null) {
            this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this
                    .getScrollPane1(), this.getScrollPane());
            this.splitPane.setOneTouchExpandable(true);
            this.splitPane.setResizeWeight(0.1);
        }
        return this.splitPane;
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

    public JMathComponent getMathComponent() {
        if (this.mathComponent == null) {
            this.mathComponent = new JMathComponent(this);
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
                .getActionByName("Zerlegen");
        result.add(this.getTextField());
        z.textField = this.getTextField();
        result.add(new JButton(this.getMathComponent().getActionByName(
                "Zerlegen")));
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
            final Font f = new Font("DialogInput", 1, 18);
            this.textField.setFont(f);
        }
        return this.textField;
    }
}
