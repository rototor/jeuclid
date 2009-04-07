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

import javax.swing.*;

public final class MathApp {

    private static void createAndShowGUI() {
        //Erzeugt das Fenster und garniert den Fensterrahmen
    	final MathFrame frame = new MathFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Wähle ein LookAndFeel
            	try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    System.out.println("ClassNotFound"); 
                } catch (InstantiationException e) {
                    System.out.println("InstantiationException"); 
                } catch (IllegalAccessException e) {
                    System.out.println("IllegalAccess"); 
                } catch (UnsupportedLookAndFeelException e) {
                    System.out.println("StrangeLookAndFeel"); 
                } 
            	createAndShowGUI();
            }
        });
    }
}
