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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.w3c.dom.Element;

import cTree.CElement;
import cTree.CIdent;
import cTree.CNum;
import cTree.CType;
import cTree.adapter.DOMElementMap;

/**
 * TreeViewDialog for KAS for JEuclid.
 * 
 */
public final class TreeViewDialog extends JFrame {

    // private Font textFont;

    private static final long serialVersionUID = 20090416L;

    private JTextPane textPane;

    private JScrollPane scrollPane;

    private JPanel jContentPane;

    private CElement document;

    private AbstractDocument doc;

    private final MathFrame owner;

    private LayoutString[] strings;

    private final int maxLineNr;

    private final int lineLength;

    // private final JTextLabel[] labels;

    private HashMap<CType, String> getText;

    private final String newline = "\n";

    public TreeViewDialog(final Frame owner) {
        super("Eine Baumdarstellung");
        this.setSize(500, 300);
        this.maxLineNr = 18;
        this.lineLength = 80;
        this.owner = (MathFrame) owner;
        this.init();
        this.setContentPane(this.getJContentPane());
    }

    private void init() {
        this.getText = new HashMap<CType, String>();
        this.getText.put(CType.EQU, "Gleichung");
        this.getText.put(CType.FENCES, "Klammer");
        this.getText.put(CType.FRAC, "Bruch");
        this.getText.put(CType.MATH, "Mathe");
        this.getText.put(CType.MINROW, "MinusTerm");
        this.getText.put(CType.PLUSROW, "Summe");
        this.getText.put(CType.POT, "Potenz");
        this.getText.put(CType.SQRT, "Wurzel");
        this.getText.put(CType.TIMESROW, "Produkt");
        this.getText.put(CType.UNKNOWN, "Unbekannt");
        this.strings = new LayoutString[this.maxLineNr];
        for (int i = 0; i < this.maxLineNr; i++) {
            this.strings[i] = new LayoutString();
        }
    }

    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new BorderLayout());
            this.jContentPane.add(this.getScrollPane(), BorderLayout.CENTER);
        }
        return this.jContentPane;
    }

    private JScrollPane getScrollPane() {
        if (this.scrollPane == null) {
            this.scrollPane = new JScrollPane();
            this.scrollPane.setViewportView(this.getTextPane());
        }
        return this.scrollPane;
    }

    private JTextPane getTextPane() {
        if (this.textPane == null) {
            this.textPane = new JTextPane() {
                @Override
                public boolean getScrollableTracksViewportWidth() {
                    return false;
                }
            };
            this.textPane.setCaretPosition(0);
            this.textPane.setMargin(new Insets(5, 5, 5, 5));
            this.textPane.setFont(new Font("Monospaced", Font.PLAIN, 16));
            this.doc = (AbstractDocument) this.textPane.getStyledDocument();
            this.setData();
        }
        return this.textPane;
    }

    public void setData() {
        final Element el = (Element) (this.owner).getMathComponent()
                .getDocument().getFirstChild();
        this.document = DOMElementMap.getInstance().getCElement.get(el);
        for (int i = 0; i < this.maxLineNr; i++) {
            this.strings[i].resetLS();
        }
        this.fill(0, 0, 0, this.document);

        final SimpleAttributeSet standard = new SimpleAttributeSet();
        StyleConstants.setForeground(standard, Color.BLACK);
        final SimpleAttributeSet highlighted = new SimpleAttributeSet();
        StyleConstants.setForeground(highlighted, Color.RED);
        try {
            this.doc.remove(0, this.doc.getLength());
            for (final LayoutString string : this.strings) {
                if (string.start == 0 && string.end == 0) {
                    this.doc.insertString(this.doc.getLength(),
                            string.content + this.newline, standard);
                } else {
                    final String start = string.content.substring(0,
                            string.start + 1);
                    final String mid = string.content.substring(
                            string.start + 1, string.end + 1);
                    final String end = string.content
                            .substring(string.end + 1);
                    this.doc.insertString(this.doc.getLength(), start,
                            standard);
                    this.doc.insertString(this.doc.getLength(), mid,
                            highlighted);
                    this.doc.insertString(this.doc.getLength(), end
                            + this.newline, standard);
                }
            }
        } catch (final BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        }
    }

    public void update() {
        System.out.println("TreeViewUpdate");
        this.setData();
        this.repaint();
    }

    private static String fillString(final String orig, final int max) {
        String neu = (orig != null) ? orig : "";
        while (neu.length() < max) {
            neu = neu + " ";
        }
        return neu;
    }

    private String text(final CElement cEl) {
        String result = cEl.getPraefixAsString();
        if (cEl instanceof CIdent || cEl instanceof CNum) {
            result = result + cEl.getText();
        } else {
            result = result + this.getText.get(cEl.getCType());
        }
        return result;
    }

    private int fill(final int tiefe, int breite, final int parentende,
            final CElement node) {
        // Zu String den aktuellen Eintrag hinzufügen
        if (node.isActiveC()) {
            System.out.println("Akiven Node gefunden");
            this.strings[tiefe].start = breite;
            this.strings[tiefe].end = breite + this.text(node).length();
        }
        this.strings[tiefe].content = this.strings[tiefe].content.substring(
                0, breite)
                + "|" + this.text(node);
        this.strings[tiefe].content = TreeViewDialog.fillString(
                this.strings[tiefe].content, this.lineLength);
        // 
        final int neueBreite = breite + this.text(node).length() + 1;

        // int untenBreite = 0;
        if (node.hasChildC()) {
            final int neueTiefe = tiefe + 1;
            breite = this.fill(neueTiefe, breite, breite
                    + this.text(node).length(), node.getFirstChild());
        }
        int naechsteBreite = Math.max(neueBreite, breite);
        if (node.hasNextC()) {
            naechsteBreite = this.fill(tiefe, naechsteBreite + 1, breite
                    + this.text(node).length(), node.getNextSibling());
        }
        if (parentende > naechsteBreite - 1) {
            System.out.println("Inserting");
            String insertString = " ";
            for (int i = naechsteBreite; i < parentende; i++) {
                insertString = insertString + " ";
            }
            this.strings[tiefe].content = this.strings[tiefe].content
                    .substring(0, naechsteBreite)
                    + insertString
                    + "|"
                    + this.strings[tiefe].content.substring(parentende + 1);
        } else {
            this.strings[tiefe].content = this.strings[tiefe].content
                    .substring(0, naechsteBreite)
                    + "|"
                    + this.strings[tiefe].content
                            .substring(naechsteBreite + 1);
        }

        return naechsteBreite;
    }

    private class LayoutString {
        public String content;

        public int start;

        public int end;

        public LayoutString() {
            this.content = TreeViewDialog.fillString("",
                    TreeViewDialog.this.lineLength);
            this.start = 0;
            this.end = 0;
        }

        public void resetLS() {
            this.content = TreeViewDialog.fillString("",
                    TreeViewDialog.this.lineLength);
            this.start = 0;
            this.end = 0;
        }
    }
}
