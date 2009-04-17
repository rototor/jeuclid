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

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
public final class TreeViewDialog extends JDialog {

    private Font textFont;

    private static final long serialVersionUID = 20090408L;

    private JPanel jContentPane;

    private CElement document;

    private final MathFrame owner;

    private final String[] strings;

    private final JTextLabel[] labels;

    private final HashMap<CType, String> getText;

    public TreeViewDialog(final Frame owner) {
        super(owner);
        this.owner = (MathFrame) owner;
        this.getText = new HashMap<CType, String>();
        this.getText.put(CType.EQU, "Gleichung");
        this.getText.put(CType.FENCES, "Klammer");
        this.getText.put(CType.FRAC, "Bruch");
        this.getText.put(CType.MATH, "Mathe");
        this.getText.put(CType.MINROW, "-Term");
        this.getText.put(CType.PLUSROW, "Summe");
        this.getText.put(CType.POT, "Potenz");
        this.getText.put(CType.SQRT, "Wurzel");
        this.getText.put(CType.TIMESROW, "Produkt");
        this.getText.put(CType.UNKNOWN, "Unbekannt");
        this.strings = new String[20];
        this.labels = new JTextLabel[20];
        this.setData();
        this.initialize();
    }

    public void update() {
        System.out.println("TreeViewUpdate");
        this.setData();
        for (int i = 0; i < 20; i++) {
            this.labels[i].setText(this.strings[i]);
        }
        this.repaint();
    }

    public void setData() {
        final Element el = (Element) (this.owner).getMathComponent()
                .getDocument().getFirstChild();
        System.out.println(DOMElementMap.getInstance().getCElement.get(el)
                .equals(this.document));
        this.document = DOMElementMap.getInstance().getCElement.get(el);
        for (int i = 0; i < 20; i++) {
            this.strings[i] = "";
            this.strings[i] = this.fillString(this.strings[i], 60);
        }
        this.fill(0, 0, this.document);
    }

    private String fillString(final String orig, final int max) {
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

    private int fill(final int tiefe, int breite, final CElement node) {
        // Zu String den aktuellen Eintrag hinzufügen
        this.strings[tiefe] = this.strings[tiefe].substring(0, breite) + "|"
                + this.text(node);
        this.strings[tiefe] = this.fillString(this.strings[tiefe], 60);
        // 
        final int neueBreite = breite + this.text(node).length() + 1;

        // int untenBreite = 0;
        if (node.hasChildC()) {
            final int neueTiefe = tiefe + 1;
            breite = this.fill(neueTiefe, breite, node.getFirstChild());
        }
        int naechsteBreite = Math.max(neueBreite, breite);
        if (node.hasNextC()) {
            naechsteBreite = this.fill(tiefe, naechsteBreite + 1, node
                    .getNextSibling());
        }
        this.strings[tiefe] = this.strings[tiefe]
                .substring(0, naechsteBreite)
                + "|" + this.strings[tiefe].substring(naechsteBreite + 1);
        return naechsteBreite;
    }

    private void initialize() {
        this.setModal(false);
        this.setResizable(false);
        this.textFont = new Font("Monospaced", Font.PLAIN, 14);
        this.setContentPane(this.getJContentPane());
        this.setTitle("Die Ansicht als Baum");
    }

    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new GridLayout(20, 1));
            for (int i = 0; i < 20; i++) {
                this.labels[i] = new JTextLabel(this.strings[i]);
            }
            for (int i = 0; i < 20; i++) {
                this.jContentPane.add(this.labels[i]);
            }
        }
        return this.jContentPane;
    }

    private class JTextLabel extends JLabel {
        private static final long serialVersionUID = 20090408L;

        JTextLabel(final String s) {
            super(s);
            this.setFont(TreeViewDialog.this.textFont);
            this.setForeground(Color.BLACK);
        }
    }

}
