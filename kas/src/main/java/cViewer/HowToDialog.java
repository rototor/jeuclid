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
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.*;


/**
 * HowToDialog for KAS for JEuclid.
 * 
 */
public final class HowToDialog extends JDialog {

    private Font textFont;
    private Font headFont;
    private Font head2Font;
    private static final long serialVersionUID = 20090408L;
    private JPanel jContentPane;

    public HowToDialog(final Frame owner) {
        super(owner);
        this.initialize();
    }

    private void initialize() {
        setModal(false);
        setResizable(false);
        textFont = new Font("SansSerif", Font.PLAIN, 11);
        headFont = new Font("SansSerif", Font.BOLD, 14);
        headFont = new Font("SansSerif", Font.BOLD, 11);
        setContentPane(this.getJContentPane());
        setTitle("KAS - Bedienung"); 
    }

    private JPanel getJContentPane() {
        if (this.jContentPane == null) {
            jContentPane = new JPanel(); 
            jContentPane.setLayout(new GridLayout(24,3));
            //
            jContentPane.add(new JHeaderLabel("Aktion"));
            jContentPane.add(new JHeaderLabel("Mausbewegung"));
            jContentPane.add(new JHeaderLabel("Tasten"));
            //
            jContentPane.add(new JH2Label("Navigation"));
            jContentPane.add(new JH2Label(" ----------------- "));
            jContentPane.add(new JH2Label(" ----------------- "));
            jContentPane.add(new JTextLabel("Endelement auswählen"));
            jContentPane.add(new JTextLabel("Linksklick"));
            jContentPane.add(new JTextLabel("-/-"));        
            // 
            jContentPane.add(new JTextLabel("Zoom in"));
            jContentPane.add(new JTextLabel("-/-"));
            jContentPane.add(new JTextLabel("y"));
            jContentPane.add(new JTextLabel("Zoom out"));
            jContentPane.add(new JTextLabel("Shift Linksklick"));
            jContentPane.add(new JTextLabel("w"));
            //
            jContentPane.add(new JTextLabel("Gehe nach links"));
            jContentPane.add(new JTextLabel("Linksklick"));
            jContentPane.add(new JTextLabel("a"));
            jContentPane.add(new JTextLabel("Gehe nach rechts"));
            jContentPane.add(new JTextLabel("Linksklick"));
            jContentPane.add(new JTextLabel("s"));
            // 
            jContentPane.add(new JTextLabel("Selektion vergrößern"));
            jContentPane.add(new JTextLabel("Drag nach unten"));
            jContentPane.add(new JTextLabel("m"));
            jContentPane.add(new JTextLabel("Selektion verkleinern"));
            jContentPane.add(new JTextLabel("Drag nach oben"));
            jContentPane.add(new JTextLabel("n"));
            // 
            jContentPane.add(new JH2Label("Veränderungen"));
            jContentPane.add(new JH2Label(" ----------------- "));
            jContentPane.add(new JH2Label(" ----------------- "));
            jContentPane.add(new JTextLabel("Element ändern"));
            jContentPane.add(new JTextLabel("Rechtsklick"));
            jContentPane.add(new JTextLabel("-/-"));   
            jContentPane.add(new JTextLabel("Bewege nach links"));
            jContentPane.add(new JTextLabel("Drag links"));
            jContentPane.add(new JTextLabel("A"));
            jContentPane.add(new JTextLabel("Bewege nach rechts"));
            jContentPane.add(new JTextLabel("Drag rechts"));
            jContentPane.add(new JTextLabel("S"));
            // 
            jContentPane.add(new JTextLabel("Klammern um Auswahl setzen"));
            jContentPane.add(new JTextLabel("-/-"));
            jContentPane.add(new JTextLabel("k"));
            jContentPane.add(new JTextLabel("Klammern entfernen"));
            jContentPane.add(new JTextLabel("-/-"));
            jContentPane.add(new JTextLabel("K, bei aktiver Klammer"));
            // 
            jContentPane.add(new JTextLabel("Verbinden nach rechts"));
            jContentPane.add(new JTextLabel("Shift Drag rechts"));
            jContentPane.add(new JTextLabel("v"));
            jContentPane.add(new JTextLabel("Rausziehen"));
            jContentPane.add(new JTextLabel("Shift Drag links"));
            jContentPane.add(new JTextLabel("r, Textfeld leer"));
            // 
            jContentPane.add(new JTextLabel("Teilen"));
            jContentPane.add(new JTextLabel("Shift Drag links"));
            jContentPane.add(new JTextLabel("t, bei Textfeld zB +5"));
            // 
            jContentPane.add(new JTextLabel("Undo"));
            jContentPane.add(new JTextLabel("-/-"));
            jContentPane.add(new JTextLabel("z"));
            jContentPane.add(new JTextLabel("Redo"));
            jContentPane.add(new JTextLabel("-/-"));
            jContentPane.add(new JTextLabel("u"));
            // 
            jContentPane.add(new JH2Label("Darstellung"));
            jContentPane.add(new JH2Label(" ----------------- "));
            jContentPane.add(new JH2Label(" ----------------- "));
            jContentPane.add(new JTextLabel("Schrift verkleinern"));
            jContentPane.add(new JTextLabel("-/-"));
            jContentPane.add(new JTextLabel("-"));
            jContentPane.add(new JTextLabel("Schrift verkleinern"));
            jContentPane.add(new JTextLabel("-/-"));
            jContentPane.add(new JTextLabel("+"));
  
        }
        return jContentPane;
    }
    
    private class JTextLabel extends JLabel{
    	private static final long serialVersionUID = 20090408L;
    	JTextLabel(String s){
    		super(s);
    		this.setFont(textFont);
    		this.setForeground(Color.BLACK);
    	}
    }
    
    private class JHeaderLabel extends JLabel{
    	private static final long serialVersionUID = 20090408L;
    	JHeaderLabel(String s){
    		super(s);
    		this.setFont(headFont);
    		this.setForeground(Color.RED);
    	}
    }
    
    private class JH2Label extends JLabel{
    	private static final long serialVersionUID = 20090408L;
    	JH2Label(String s){
    		super(s);
    		this.setFont(head2Font);
    		this.setForeground(Color.RED);
    	}
    }

}

