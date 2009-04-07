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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class JMathKeyListener implements KeyListener{
	private JMathComponent mathComponent; 
    private HashMap<String, String> myInputMap;

	public JMathKeyListener(JMathComponent jm){
		mathComponent = jm;
		myInputMap = new HashMap<String, String>();
		myInputMap.put("z", "Zerlegen");
    	myInputMap.put("y", "ZoomIn");
    	myInputMap.put("Y", "Entklammere");
    	myInputMap.put("w", "ZoomOut");
    	myInputMap.put("W", "Klammere");
    	myInputMap.put("a", "GeheLinks");
    	myInputMap.put("A", "BewegeLinks");
    	myInputMap.put("s", "GeheRechts");
    	myInputMap.put("S", "BewegeRechts");
    	myInputMap.put("v", "Verbinden");
    	myInputMap.put("V", "Rausziehen");
    	myInputMap.put("+", "Vergrößern");
    	myInputMap.put("-", "Verkleinern");
    	myInputMap.put("n", "Selection-");
    	myInputMap.put("m", "Selection+");
    	myInputMap.put(" ", "Undo");
	}
    
    public void keyTyped(KeyEvent e) {
    	mathComponent.requestFocusInWindow();
    	String actionName = myInputMap.get(String.valueOf(e.getKeyChar()));
    	if (actionName!=null){
    		mathComponent.getActionByName(myInputMap.get(String.valueOf(e.getKeyChar()))).actionPerformed(null);
    	}
    }
    
    public void keyPressed(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
    }
}
