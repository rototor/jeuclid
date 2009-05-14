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
public final class MyTreeViewDialog extends JFrame {

    private static final long serialVersionUID = 20090416L;

    private JTextPane textPane;

    private JScrollPane scrollPane;

    private JPanel jContentPane;

    private CElement document;

    private AbstractDocument doc;

    private LayoutString[] strings;

    private final int maxLineNr;

    private HashMap<CType, String> getText;

    private final String newline = "\n";

    private SimpleAttributeSet standard;

    private SimpleAttributeSet highlighted;

    private SimpleAttributeSet addition;

    public MyTreeViewDialog() {
        super("Eine Baumdarstellung");
        this.setSize(500, 300);
        this.maxLineNr = 18;
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
        this.getText.put(CType.PLUSTERM, "PlusTerm");
        this.getText.put(CType.MIXEDN, "Gem. Zahl");
        this.getText.put(CType.PLUSROW, "Summe");
        this.getText.put(CType.POT, "Potenz");
        this.getText.put(CType.SQRT, "Wurzel");
        this.getText.put(CType.TIMESROW, "Produkt");
        this.getText.put(CType.UNKNOWN, "Unbekannt");
        this.strings = new LayoutString[this.maxLineNr];
        for (int i = 0; i < this.maxLineNr; i++) {
            this.strings[i] = new LayoutString();
        }
        this.standard = new SimpleAttributeSet();
        StyleConstants.setForeground(this.standard, Color.BLACK);
        this.highlighted = new SimpleAttributeSet();
        StyleConstants.setForeground(this.highlighted, Color.RED);
        this.addition = new SimpleAttributeSet();
        StyleConstants.setForeground(this.addition, Color.decode("#007777"));
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

    @SuppressWarnings("serial")
    private JTextPane getTextPane() {
        if (this.textPane == null) {
            this.textPane = new JTextPane() {
                @Override
                public boolean getScrollableTracksViewportWidth() {
                    return false;
                }
            };
            this.textPane.setEditable(false);
            this.textPane.setCaretPosition(0);
            this.textPane.setMargin(new Insets(5, 5, 5, 5));
            this.textPane.setFont(new Font("Monospaced", Font.PLAIN, 16));
            this.doc = (AbstractDocument) this.textPane.getStyledDocument();
            this.setData();
        }
        return this.textPane;
    }

    public void setData() {
        // hole das Document
        final Element el = (Element) ViewerFactory.getInst()
                .getMathComponent().getDocument().getFirstChild();
        this.document = DOMElementMap.getInstance().getCElement.get(el);
        // Alle LayoutStrings resetten
        for (int i = 0; i < this.maxLineNr; i++) {
            this.strings[i].resetLS();
        }
        // LayoutStrings fuellen
        this.fill(0, 0, 0, this.document);
        try {
            // Pane leeren
            this.doc.remove(0, this.doc.getLength());
            // LayoutStrings einfuegen
            for (final LayoutString string : this.strings) {
                // ohne ActiveC
                if (string.start == 0 && string.end == 0) {
                    this.doc.insertString(this.doc.getLength(),
                            string.content + this.newline, this.standard);
                    // mit ActiveC
                } else {
                    this.doc.insertString(this.doc.getLength(),
                            string.content.substring(0, string.start + 1),
                            this.standard);
                    this.doc.insertString(this.doc.getLength(),
                            string.content.substring(string.start + 1,
                                    string.mid + 1), this.highlighted);
                    this.doc.insertString(this.doc.getLength(),
                            string.content.substring(string.mid + 1,
                                    string.end + 1), this.addition);
                    this.doc.insertString(this.doc.getLength(),
                            string.content.substring(string.end + 1)
                                    + this.newline, this.standard);
                }
            }
        } catch (final BadLocationException ble) {
            System.err.println("Couldn't insert initial text.");
        }
    }

    public void update() {
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

    /**
     * Rückgabewert ist das Ende
     * 
     * @param tiefe
     *            Zeile
     * @param pbreite
     *            Start fuer neuen Eintrag
     * @param parentende
     *            Ende des Parents
     * @param node
     *            Zu bearbeitenden Knoten
     * @return MindestBreite von Unten garantiert
     */
    private int fill(final int tiefe, final int pbreite,
            final int parentende, final CElement node) {

        // Zu String den aktuellen Eintrag hinzufügen
        if (node.isActiveC()) {
            this.strings[tiefe].start = pbreite;
            this.strings[tiefe].mid = pbreite + this.text(node).length();
            this.strings[tiefe].end = this.strings[tiefe].mid;
        }
        if (node.isLastC()) {
            this.strings[tiefe].end = pbreite + this.text(node).length();
        }

        // evtl Anfang auffuellen
        final String oldString = this.strings[tiefe].content;
        if (pbreite > oldString.length()) {
            this.strings[tiefe].content = MyTreeViewDialog.fillString(
                    this.strings[tiefe].content, pbreite);
        }
        // aktuelle Info einfuellen
        this.strings[tiefe].content = this.strings[tiefe].content + "|"
                + this.text(node);
        int neuesEnde = this.strings[tiefe].content.length();
        // nach unten arbeiten
        int breiteVonUnten = neuesEnde;
        if (node.hasChildC()) {
            breiteVonUnten = this.fill(tiefe + 1, pbreite, neuesEnde, node
                    .getFirstChild());
        }
        if (breiteVonUnten > neuesEnde - 1) {
            this.strings[tiefe].content = MyTreeViewDialog.fillString(
                    this.strings[tiefe].content, breiteVonUnten - 1);
        }

        neuesEnde = this.strings[tiefe].content.length();
        if (node.hasNextC()) {
            this.strings[tiefe].content = this.strings[tiefe].content + " ";
            neuesEnde = this.fill(tiefe, neuesEnde + 1, parentende, node
                    .getNextSibling());
        } else {
            if (parentende > neuesEnde - 1) {
                this.strings[tiefe].content = MyTreeViewDialog.fillString(
                        this.strings[tiefe].content, parentende);
            }
            neuesEnde = this.strings[tiefe].content.length();
            this.strings[tiefe].content = this.strings[tiefe].content + "#";
        }
        return this.strings[tiefe].content.length();
    }

    private class LayoutString {
        public String content;

        public int start;

        public int mid;

        public int end;

        public LayoutString() {
            this.content = "";
            this.start = 0;
            this.mid = 0;
            this.end = 0;
        }

        public void resetLS() {
            this.content = "";
            this.start = 0;
            this.mid = 0;
            this.end = 0;
        }
    }
}
