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
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * HowToDialog for KAS for JEuclid.
 * 
 */
public final class MyHowToDialog extends JDialog {

    private Font textFont;

    private Font headFont;

    private Font head2Font;

    private static final long serialVersionUID = 20090408L;

    private JPanel jContentPane;

    public MyHowToDialog() {
        super(ViewerFactory.getInst().getMathFrame());
        this.initialize();
    }

    private void initialize() {
        this.setModal(false);
        this.setResizable(false);
        this.textFont = new Font("SansSerif", Font.PLAIN, 11);
        this.headFont = new Font("SansSerif", Font.BOLD, 14);
        this.headFont = new Font("SansSerif", Font.BOLD, 11);
        this.setContentPane(this.getJContentPane());
        this.setTitle("KAS - Bedienung");
    }

    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            this.jContentPane = new JPanel();
            this.jContentPane.setLayout(new GridLayout(27, 3));
            //
            this.jContentPane.add(new JHeaderLabel("Aktion"));
            this.jContentPane.add(new JHeaderLabel("Mausbewegung"));
            this.jContentPane.add(new JHeaderLabel("Tasten"));
            //
            this.jContentPane.add(new JH2Label("Navigation"));
            this.jContentPane.add(new JH2Label(" ----------------- "));
            this.jContentPane.add(new JH2Label(" ----------------- "));
            this.jContentPane.add(new JTextLabel("Endelement auswählen"));
            this.jContentPane.add(new JTextLabel("Linksklick"));
            this.jContentPane.add(new JTextLabel("-/-"));
            // 
            this.jContentPane.add(new JTextLabel("Zoom in"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("y"));
            this.jContentPane.add(new JTextLabel("Zoom out"));
            this.jContentPane.add(new JTextLabel("Shift Linksklick"));
            this.jContentPane.add(new JTextLabel("w"));
            //
            this.jContentPane.add(new JTextLabel("Gehe nach links"));
            this.jContentPane.add(new JTextLabel("Linksklick"));
            this.jContentPane.add(new JTextLabel("a"));
            this.jContentPane.add(new JTextLabel("Gehe nach rechts"));
            this.jContentPane.add(new JTextLabel("Linksklick"));
            this.jContentPane.add(new JTextLabel("s"));
            // 
            this.jContentPane.add(new JTextLabel("Selektion vergrößern"));
            this.jContentPane.add(new JTextLabel("Drag nach unten"));
            this.jContentPane.add(new JTextLabel("m"));
            this.jContentPane.add(new JTextLabel("Selektion verkleinern"));
            this.jContentPane.add(new JTextLabel("Drag nach oben"));
            this.jContentPane.add(new JTextLabel("n"));
            // 
            this.jContentPane.add(new JH2Label("Veränderungen"));
            this.jContentPane.add(new JH2Label(" ----------------- "));
            this.jContentPane.add(new JH2Label(" ----------------- "));
            this.jContentPane.add(new JTextLabel("Element ändern"));
            this.jContentPane.add(new JTextLabel("Rechtsklick"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("Bewege nach links"));
            this.jContentPane.add(new JTextLabel("Drag links"));
            this.jContentPane.add(new JTextLabel("A"));
            this.jContentPane.add(new JTextLabel("Bewege nach rechts"));
            this.jContentPane.add(new JTextLabel("Drag rechts"));
            this.jContentPane.add(new JTextLabel("S"));
            // 
            this.jContentPane
                    .add(new JTextLabel("Klammern um Auswahl setzen"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("k"));
            this.jContentPane.add(new JTextLabel("Klammern entfernen"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("e, bei aktiver Klammer"));
            // 
            this.jContentPane.add(new JTextLabel("Verbinden nach rechts"));
            this.jContentPane.add(new JTextLabel("Shift Drag rechts"));
            this.jContentPane.add(new JTextLabel("v"));
            this.jContentPane.add(new JTextLabel("Rausziehen"));
            this.jContentPane.add(new JTextLabel("Shift Drag links"));
            this.jContentPane.add(new JTextLabel("r, Textfeld leer"));
            // 
            this.jContentPane.add(new JTextLabel("Teilen"));
            this.jContentPane.add(new JTextLabel("Shift Drag links"));
            this.jContentPane.add(new JTextLabel("t, bei Textfeld zB +5"));
            // 
            this.jContentPane.add(new JTextLabel("Undo"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("z"));
            this.jContentPane.add(new JTextLabel("Redo"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("u"));
            // 
            this.jContentPane.add(new JH2Label("Darstellung"));
            this.jContentPane.add(new JH2Label(" ----------------- "));
            this.jContentPane.add(new JH2Label(" ----------------- "));
            this.jContentPane.add(new JTextLabel("Schrift verkleinern"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("-"));
            this.jContentPane.add(new JTextLabel("Schrift verkleinern"));
            this.jContentPane.add(new JTextLabel("-/-"));
            this.jContentPane.add(new JTextLabel("+"));
            // 
            this.jContentPane.add(new JH2Label("Eingabefeld"));
            this.jContentPane.add(new JH2Label(" bei e auch e(x+4) "));
            this.jContentPane.add(new JH2Label(" bei e auch e(2x-3) "));
            this.jContentPane.add(new JTextLabel("9 |+3  -> 6+3"));
            this.jContentPane.add(new JTextLabel("9 |-3  -> 12-3"));
            this.jContentPane.add(new JTextLabel("9 |*3  -> 3*3"));
            this.jContentPane.add(new JTextLabel("9 |:3  -> 27:3"));
            this.jContentPane.add(new JTextLabel("9 |^2  -> 3^2"));
            this.jContentPane.add(new JTextLabel("3/2 |e5 -> 15/10"));
            this.jContentPane.add(new JTextLabel("3^5 |*3^2 -> 3^3*3^2"));
        }
        return this.jContentPane;
    }

    private class JTextLabel extends JLabel {
        private static final long serialVersionUID = 20090408L;

        JTextLabel(final String s) {
            super(s);
            this.setFont(MyHowToDialog.this.textFont);
            this.setForeground(Color.BLACK);
        }
    }

    private class JHeaderLabel extends JLabel {
        private static final long serialVersionUID = 20090408L;

        JHeaderLabel(final String s) {
            super(s);
            this.setFont(MyHowToDialog.this.headFont);
            this.setForeground(Color.RED);
        }
    }

    private class JH2Label extends JLabel {
        private static final long serialVersionUID = 20090408L;

        JH2Label(final String s) {
            super(s);
            this.setFont(MyHowToDialog.this.head2Font);
            this.setForeground(Color.RED);
        }
    }

}
