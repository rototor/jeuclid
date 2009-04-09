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
/*
 * <mo>·</mo><mo>×</mo>
 */

package cViewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.accessibility.Accessible;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import cTree.*;
import cTree.adapter.DOMElementMap;
import cTree.adapter.EElementHelper;

import euclid.MathMLParserSupport;
import euclid.MathMLSerializer;
import euclid.MutableLayoutContext;
import euclid.context.LayoutContextImpl;
import euclid.context.Parameter;

/**
 * Displays MathML content in a Swing Component.
 */
public final class JMathComponent extends JComponent implements SwingConstants, FocusListener, Accessible {

	private static final long serialVersionUID = 20081230L;
    private static String uiClassId ="MathComponentUI16";
    private int horizontalAlignment = SwingConstants.LEFT;	// SwingConstants.CENTER;
    private int verticalAlignment = SwingConstants.TOP;   	// SwingConstants.CENTER;     
    protected Document document;							// Das "wohlgeformte" orgw3c.dom-Dokument
    protected ArrayList<CElement> activeC; 					// Einige aufeinanderfolgende CElemente mit gleichem Parent
    private String undoSave;								// Der MathML_String als Speicher für den letzten Zustand
    private final MutableLayoutContext parameters; 
    public HashMap<Object, Action> actions;					// Actions die mit Maus Tastatur oder Buttons ausgelöst werden können
    
    public JMathComponent() {
    	parameters = new LayoutContextImpl(LayoutContextImpl.getDefaultLayoutContext());
    	activeC = new ArrayList<CElement>();
    	setUI(new MathComponentUI16()); 
        setContent("<math><mrow><mi>x</mi><mo>=</mo>" +
        		 "<mfrac><mrow>" +
        		   "<mrow><mo>-</mo><mi>b</mi></mrow>" +
        		   "<mo>+</mo>" + 
        		   "<msqrt>" +
        		    "<mrow>" +
        		     "<msup><mi>b</mi><mn>2</mn></msup>" +
        		     "<mo>-</mo>" +
        		     "<mrow><mn>4</mn><mo><mchar name=\"InvisibleTimes\"/></mo><mi>a</mi><mo><mchar name=\"InvisibleTimes\"/></mo><mi>c</mi></mrow>" +
        		    "</mrow>" +
        		   "</msqrt>" +
        		  "</mrow>" +
        		  "<mrow>" +
        		   "<mn>2</mn>" +
        		   "<mo><mchar name=\"InvisibleTimes\"/></mo>" +
        		   "<mi>a</mi>" +
        		  "</mrow></mfrac>" +
        		"</mrow></math>");
        // für die Bedienung über die Tastur wichtig
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        actions = createActionTable(); 
        requestFocusInWindow();
    }
    
    public CElement getCActive(){
    	return activeC.isEmpty() ? null : activeC.get(0);
    }
    
    public void setCActive(CElement cElement){
    	activeC.set(0, cElement);
    }
    
    public void clearCButFirst(){
    	CElement first = activeC.get(0);
    	activeC.remove(first);
    	for (CElement el : activeC){
    		el.removeCLastProperty();
    	}
    	activeC.clear();
    	activeC.add(first);    	
    }
    
    public class ZerlegeAction extends AbstractAction{
    	private static final long serialVersionUID = 20081230L;
    	public JTextField textField;
    	public ZerlegeAction(String string){
    		super(string);
    	}
    	public void actionPerformed(ActionEvent ae) {	
    		saveForUndo();
    		clearCButFirst();
			String cleaned = textField.getText().replace(" ", "");
			CElement newAct = getCActive().getParent().split(getCActive(), cleaned);
			JMathComponentHelper.getDocInfo(newAct, false);
	    	setCActive(newAct);
	    	requestFocusInWindow();
			modifyDocument();
		}
    }
    
	private HashMap<Object, Action> createActionTable() {
        //Legt die Actions der Komponente in einem HashMap ab
    	HashMap<Object, Action> actions = new HashMap<Object, Action>();
        
    	AbstractAction myAction = new ZerlegeAction("Zerlegen");
        actions.put(myAction.getValue(Action.NAME),  myAction);  
        myAction = new AbstractAction("ZoomIn"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			saveForUndo();
    			clearCButFirst();
    			setCActive(getCActive().tryToSelectFirstChild());
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("ZoomOut"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			saveForUndo();
    			clearCButFirst();
    			setCActive(getCActive().tryToSelectParent());
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("GeheRechts"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			saveForUndo();
    			clearCButFirst();
    			setCActive(getCActive().tryToSelectRight());
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Selection+"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			CElement lastC = activeC.get(activeC.size()-1);
        		if (lastC.hasNextC() && lastC.hasParent() && (
        		  (lastC.getParent() instanceof CTimesRow)|| (lastC.getParent() instanceof CPlusRow))){
        			CElement nextC = lastC.getNextSibling();
        			nextC.setCLastProperty();
        			activeC.add(nextC);
        			modifyDocument();
        		}
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Selection-"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			CElement lastC = activeC.get(activeC.size()-1);
    			if (activeC.size()>1){
    	        	lastC.removeCLastProperty();
    	        	activeC.remove(lastC);
    	        	modifyDocument();
    	        }
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("GeheLinks"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			saveForUndo();
    			clearCButFirst();
    			setCActive(getCActive().tryToSelectLeft());
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("BewegeRechts"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			CElement cAct = getCActive();
    			saveForUndo();
    			clearCButFirst();
    			cAct.removeCActiveProperty();
    			setCActive(cAct.getParent().moveRight(cAct));
    			getCActive().setCActiveProperty();
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("BewegeLinks"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			CElement cAct = getCActive();
    			saveForUndo();
    			clearCButFirst();
    			cAct.removeCActiveProperty();
    			setCActive(cAct.getParent().moveLeft(cAct));
    			getCActive().setCActiveProperty();
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Verbinden"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			CElement cAct = getCActive();
    			saveForUndo();
    			clearCButFirst();
    			setCActive(cAct.getParent().combineRight(cAct));
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Rausziehen"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			CElement cAct = getCActive();
    			if (cAct.getParent()!=null)	{
    				saveForUndo();
    				setCActive(getCActive().getParent().extract(activeC));
    				modifyDocument();
    			}
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Klammere"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			saveForUndo();
    			setCActive(getCActive().getParent().fence(activeC));		
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Entklammere"){							
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			CElement cAct = getCActive();
    			saveForUndo();
    			clearCButFirst();
    			System.out.println("JMathComponent typ" + cAct.getParent().getCType());
    			setCActive(cAct.getParent().defence(cAct));
    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Meins"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {
    			CElement cAct = getCActive();
//    			cAct.normalizeAll();
    			JMathComponentHelper.getDocInfo(cAct, false);
    			CElementHelper.controlMath((Element) cAct.getElement().getOwnerDocument().getFirstChild());
//    			JMathComponentHelper.control(cAct.getElement().getOwnerDocument().getFirstChild());
//    			modifyDocument();
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Undo"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {
    			String redo = MathMLSerializer.serializeDocument(getDocument(), false, false);
    	        redo = JMathComponentHelper.cleanString(redo);
    			setContent(undoSave);
    			modifyDocument();
    			undoSave = redo;
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Vergrößern"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			setParameter(Parameter.MATHSIZE, getFontSize()*1.25f);
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
        myAction = new AbstractAction("Verkleinern"){
        	private static final long serialVersionUID = 20081230L;
    		public void actionPerformed(ActionEvent ae) {	
    			setParameter(Parameter.MATHSIZE, getFontSize()*0.8f);
    		}
        };
        actions.put(myAction.getValue(Action.NAME),  myAction); 
	return actions;
    }
	
    public Action getActionByName(String name) {
    	return actions.get(name);
    }
    
    private void saveForUndo(){
    	undoSave = MathMLSerializer.serializeDocument(getDocument(), false, false);
        undoSave = JMathComponentHelper.cleanString(undoSave);
    }
    

    public void setDocument(final Document doc) {    
    	DOMElementMap.getInstance().getCElement.clear();      		// Zuordnung zwischen Element und CElement löschen 
    	document = doc;
        JMathElementHandler.parseDom(document.getFirstChild()); 	// org.w3c.dom-Doc mit Anmerkungen über den RowTyp versehen
        EElementHelper.setDots(document.getFirstChild());        	// invisible und visible Dots richtig setzen in org.w3c.dom
        CElementHelper.buildCFromEPraefixAdjust(document.getFirstChild(), CRolle.DOCUMENTCHILD, false); // CTree aufbauen und activeC setzen
        activeC.clear();
        activeC.add(DOMElementMap.getInstance().getCElement.get(document.getFirstChild()));
    	firePropertyChange("documentNew", null, doc); 				// rearrangiere EuclidVisuals
    	reval();													// revalidate, repaint
    }
      
    public void modifyDocument() { 	
    	JMathElementHandler.parseDom(document.getFirstChild()); 	// anpassen der DOM-Attribute calcType
    	EElementHelper.setDots(document.getFirstChild());
        firePropertyChange("documentChange", null, document);  		// gibt der MathComponentUI Chance die Views anzupassen
    	reval();													// revalidate, repaint
        
    }
   
	
    // org.w3c.dom.Node -> String
    public String getContent() {
        return MathMLSerializer.serializeDocument(this.getDocument(), false,false);
    }

    // getter des org.w3c.dom.Node und weitere getter
    public Node getDocument() {
        return document;
    }

    public MutableLayoutContext getParameters() {
        return this.parameters;
    }

    public Color getForeground() {
        return (Color) this.parameters.getParameter(Parameter.MATHCOLOR);
    }
    
    public float getFontSize() {
        return (Float) this.parameters.getParameter(Parameter.MATHSIZE);
    }
    
    public int getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public Dimension getPreferredSize() {
    	Dimension m = this.getMinimumSize(); 
    	return new Dimension(m.width+200, m.height+200);
    }

    public MathComponentUI getUI() {
        return (MathComponentUI) this.ui;
    }

    public String getUIClassID() {
        return JMathComponent.uiClassId;
    }

    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public void setBackground(final Color c) {
        super.setBackground(c);
        this.reval();
    }

    // wird vom MathFrame aufgerufen
    public void setContent(final String contentString) {
        try {
        	String s = JMathElementHandler.testMathMLString(contentString);
        	// erzeugt das org.w3c.dom-Document und übergibt es
            setDocument(MathMLParserSupport.parseString(s));
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setParameter(final Parameter key, final Object newValue) {
        this.setParameters(Collections.singletonMap(key, newValue));
    }

    public void setParameters(final Map<Parameter, Object> newValues) {
        for (final Map.Entry<Parameter, Object> entry : newValues.entrySet()) {
            final Parameter key = entry.getKey();
            final Object oldValue = this.parameters.getParameter(key);
            this.parameters.setParameter(key, entry.getValue());
            this.firePropertyChange(key.name(), oldValue, this.parameters
                    .getParameter(key));
        }
        this.revalidate();
        this.repaint();
    }

    public void setFontSize(final float fontSize) {
        this.setParameter(Parameter.MATHSIZE, fontSize);
    }

    public void setForeground(final Color fg) {
        super.setForeground(fg);
        this.setParameter(Parameter.MATHCOLOR, fg);
    }

    public void setHorizontalAlignment(final int hAlignment) {
        this.horizontalAlignment = hAlignment;
    }

    public void setOpaque(final boolean opaque) {
        super.setOpaque(opaque);
        this.reval();
    }

    public void setVerticalAlignment(final int vAlignment) {
        this.verticalAlignment = vAlignment;
    }

    public void setSize(final int width, final int height) {
        super.setSize(width, height);
    }
    
    // Private Utilities
    public void reval() {
    	revalidate();
    	repaint();
    	requestFocusInWindow();
    }	
    
    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }
}
