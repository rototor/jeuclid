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

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import org.w3c.dom.Element;
import cTree.*;

public class JMathMouseListener implements MouseListener{
	private JMathComponent mathComponent; 
	private int x0;
	private JTextField jTextField;
	public JMathMouseListener(JMathComponent jm, JTextField s){
		mathComponent = jm;
		jTextField = s;
	}
	
    public void mousePressed(MouseEvent e) {
    	x0=e.getX();
//    	y0=e.getY();
    }
    public void mouseMoved(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    	// nach rechts ziehen: Zusammenfassen
    	if (e.getX()>x0+10) {
    		if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK){
    			mathComponent.getActionByName("Verbinden").actionPerformed(null);
    		} else {
    			mathComponent.getActionByName("BewegeRechts").actionPerformed(null);
    		}	
//    		x0=e.getX();
    	// nach links ziehen: Aufspalten oder Extrahieren
    	} else if (e.getX()<x0-10) {
    		if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK){
    			if (jTextField.getText()==null || "".equals(jTextField.getText())) {
    				mathComponent.getActionByName("Rausziehen").actionPerformed(null);
    			} else {
    				mathComponent.getActionByName("Zerlegen").actionPerformed(null);
    			}
    		} else {
    			mathComponent.getActionByName("BewegeLinks").actionPerformed(null);
    		}
    	}
    }
    
    public void mouseEntered(MouseEvent e) {
    	mathComponent.requestFocusInWindow();
    }
    
    public void mouseExited(MouseEvent e) { 
    }
    
    public void mouseDragged(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) { 
    	CElement cAct = mathComponent.getCActive();
    	// Rechtsklick: Zur Auswahl hinzufügen
    	if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK){
    		mathComponent.getActionByName("Selection+").actionPerformed(null);
    	// Herauszoomen
    	} else if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK){
    		mathComponent.getActionByName("ZoomOut").actionPerformed(null);
    	// Auswählen
    	} else {
    		CElement newE = cTree.adapter.DOMElementMap.getInstance().getCElement.get(
    				(Element) mathComponent.getUI().getNodeFromView(e.getX(), e.getY()));
    		mathComponent.setCActive(cTree.CNavHelper.chooseElement(cAct, newE));
    		mathComponent.clearCButFirst();
    		mathComponent.modifyDocument();
    	} 
    }
}
