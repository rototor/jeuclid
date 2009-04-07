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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import cViewer.JMathComponent.ZerlegeAction;
import euclid.MathMLSerializer;

public class MathFrame extends JFrame{
    
    private static final int DEFAULT_HEIGHT = 250;
    private static final int DEFAULT_WIDTH = 800;
    private static final long serialVersionUID= 20090301L;
    private File lastPath;
    private JPanel jContentPane;
    private JMenuBar jMenuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem;
    private JMenuItem exportMenuItem;
    private JScrollPane scrollPane;
    private JMathComponent mathComponent;
    private JTextField textField;

    /**
     * This is the default constructor.
     */
    public MathFrame() {
    	setTitle("KAS - Kein Algebrasystem A");
    	setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    	setJMenuBar(getJMenuBar());       
        setContentPane(getJContentPane()); 
    	addWindowFocusListener(new WindowAdapter() {
    	    public void windowGainedFocus(WindowEvent e) {
    	        mathComponent.requestFocusInWindow();
    	    }
    	});
    }
    
    public JMenuBar getJMenuBar() {
    	if (jMenuBar == null) {
            jMenuBar = new JMenuBar();
            jMenuBar.add(getFileMenu());
        }
        return jMenuBar;
    }

    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText("Datei");
            fileMenu.add(getOpenMenuItem());
            fileMenu.add(getExportMenuItem());
        }
        return fileMenu;
    }

    // Die FileMenuItems
    private JMenuItem getOpenMenuItem() {
        if (openMenuItem == null) {
            openMenuItem = new JMenuItem();
            openMenuItem.setText("Datei öffnen ..."); 
            openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
               KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            openMenuItem.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		openFile();
            	}
            });
        }
        return openMenuItem;
    }
    
    private void openFile() {
        File file = selectFileToOpen(this);
        BufferedReader r;
    	String result ="";
    	String line; 
    	try { 
    		r = new BufferedReader( new FileReader(file));
    		while ((line = r.readLine()) != null) {
    			result = result+line;
    		}
    		r.close();
    	} catch (IOException e) {
  	      	System.out.println("Fehler beim Lesen der Datei");
	    }
    	getMathComponent().setContent(result);
    	getMathComponent().requestFocusInWindow();
    }

    private File selectFileToOpen(Frame parent) {
        File selectedFile;
        JFileChooser fChooser = new JFileChooser(lastPath);
        if (fChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fChooser.getSelectedFile();
        } else {
            selectedFile = null;
        }
        if (selectedFile != null) {
            lastPath = selectedFile.getParentFile();
        }
        return selectedFile;
    }
    
    private JMenuItem getExportMenuItem() {
        if (exportMenuItem == null) {
            exportMenuItem = new JMenuItem();
            exportMenuItem.setText("Exportieren");
            exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(
               KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), true));
            exportMenuItem.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
            	   exportFile();
               }
            });
        }
        return exportMenuItem;
    }

    private void exportFile() {       
        JFileChooser fChooser = new JFileChooser(lastPath);
        if (fChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fChooser.getSelectedFile();
            if (selectedFile != null) {
            	String fileName = selectedFile.getAbsolutePath();
            	JMathElementHandler.removeCalcTyp(getMathComponent().getDocument());
                String s = MathMLSerializer.serializeDocument(getMathComponent().getDocument(), false, false);
                try{
                	BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                	bw.write(s); 
                	bw.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
        JMathElementHandler.parseDom(getMathComponent().getDocument());	
    }

    public JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getScrollPane(), BorderLayout.CENTER);
            jContentPane.add(getButtonPanel(), BorderLayout.WEST);
            getMathComponent().addKeyListener(new JMathKeyListener(getMathComponent()));
            getMathComponent().addMouseListener(new JMathMouseListener(getMathComponent(), getTextField()));
        }
        return this.jContentPane;
    }
    
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getMathComponent());
        }
        return scrollPane;
    }
    
    public JMathComponent getMathComponent() {
        if (mathComponent == null) {
            mathComponent = new JMathComponent();
        }
        return mathComponent;
    }
    
    public JPanel getButtonPanel() {
    	JPanel result = new JPanel(); 
    	result.setLayout(new GridLayout(9,2));
    	// Tree Walker
    	result.add(new JButton(getMathComponent().getActionByName("ZoomOut")));
    	result.add(new JButton(getMathComponent().getActionByName("ZoomIn")));
    	result.add(new JButton(getMathComponent().getActionByName("GeheLinks")));
    	result.add(new JButton(getMathComponent().getActionByName("GeheRechts")));
    	result.add(new JButton(getMathComponent().getActionByName("Selection+")));
    	result.add(new JButton(getMathComponent().getActionByName("Selection-")));
    	// Einfache Änderungen
    	result.add(new JButton(getMathComponent().getActionByName("BewegeLinks")));
    	result.add(new JButton(getMathComponent().getActionByName("BewegeRechts")));
    	result.add(new JButton(getMathComponent().getActionByName("Klammere")));
    	result.add(new JButton(getMathComponent().getActionByName("Entklammere")));
    	// Komplexere Änderungen
    	result.add(new JButton(getMathComponent().getActionByName("Rausziehen")));
    	result.add(new JButton(getMathComponent().getActionByName("Verbinden")));
    	ZerlegeAction z = (ZerlegeAction) getMathComponent().getActionByName("Zerlegen");
    	result.add(getTextField()); z.textField = getTextField();	
    	result.add(new JButton(getMathComponent().getActionByName("Zerlegen")));
    	// Undo and Redo (simple)
    	result.add(new JButton(getMathComponent().getActionByName("Meins")));
    	result.add(new JButton(getMathComponent().getActionByName("Undo")));  
    	return result;
    }
    public JTextField getTextField(){
    	if (textField==null){
        	textField = new JTextField(6); 
        	Font f = new Font("DialogInput",1, 18);
        	textField.setFont(f);
    	}
    	return textField;	
    }
}
