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

package cTree.adapter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public abstract class PraefixAdapter extends ElementAdapter{
	
	protected Element praefix;

	public Element getExtPraefix() {
		return praefix;
	}
	/* 
	 * gibt "*" "+" "-" "/" oder "" zurück
	 * InvisibleTimes wird z.B. zu "*"
	 */
	public String getPraefixAsString(){
		if (praefix==null){
			return "";
		} else {
			if (praefix.getFirstChild()!=null && praefix.getFirstChild().getAttributes()!=null && praefix.getFirstChild().getAttributes().getNamedItem("name")!=null && "InvisibleTimes".equals(praefix.getFirstChild().getAttributes().getNamedItem("name").getNodeValue())) {
				return "*";
			}
			if (praefix.getFirstChild()!=null && praefix.getFirstChild().getAttributes()!=null && praefix.getFirstChild().getAttributes().getNamedItem("name")!=null && "InvisiblePlus".equals(praefix.getFirstChild().getAttributes().getNamedItem("name").getNodeValue())) {
				return "+";
			}
			String s = praefix.getTextContent();
			if ("·".equals(s)) {return "*";}
			if ("*".equals(s) || "+".equals(s) || "-".equals(s) || ":".equals(s)){
				return s; 
			} else {
				System.out.println("Vorzeichen konnte in cTree.adapter.PraefixAdapter.getPraefixAsString() nicht ermittelt werden.");
				return " ";
			}
		}
	}
	
	public void setExtPraefix(Element vz){
		praefix = vz;
	}
	/*
	 * falls Praefix vorhanden ist, wird der Wert gesetzt, sonst ein neuer Praefix erzeugt.
	 * falls ein parent existiert, wird das praefix davor gesetzt. (Nicht bei Clones).
	 */
	public void setPraefix(String s){
		Element parent = (Element) getElement().getParentNode();
		if (!"".equals(s)){
			if (praefix==null){
				Element newOp = EElementHelper.createOp(getElement(), s);
				System.out.println("Praefixadapter setPraefix" + newOp.getTextContent());
				if (parent!=null){
					parent.insertBefore(newOp, getElement());
				}
				setExtPraefix(newOp);
			} else {
				praefix.setTextContent(s);
			}
		} else { // s=""
			if (praefix!=null){
				if (parent!=null){
					System.out.println("PraefixAdapter setPraefix Try to remove praefix");
					parent.removeChild(praefix);
				}
				praefix=null;
			}
		}
	}
	/*
	 * falls Praefix vorhanden ist, wird er beim Parent deregistriert.
	 * die Referenz des CElements wird null gesetzt. 
	 * evtl fehlerhaft
	 */
	public void removePraefix(){
		Element el = getElement();
		Node parent = el.getParentNode();
		if (parent!=null){
			parent.removeChild(el.getPreviousSibling());
			setPraefix(null);
		}
		
	}
	
	public void setPlusToEmpty(){
		if (getExtPraefix()!=null && "+".equals(getExtPraefix().getTextContent())){
			Node parent = getElement().getParentNode();
			if (parent!=null){
				parent.removeChild(getExtPraefix());
			}
			setExtPraefix(null);
		}
	}

	public void setTimesToEmpty(){
		if (EElementHelper.isTimesOp(getExtPraefix())){
			Node parent = getElement().getParentNode();
			if (parent!=null){
				((Element) getElement().getParentNode()).removeChild(getExtPraefix());
			}
			setExtPraefix(null);
		}
	}

	public void setEmptyTo(String s){
		if (getExtPraefix()==null){
			setPraefix(s);
		}
	}

	public void setEmptyToPlus(){
		setEmptyTo("+");
	}

	public void setEmptyToTimes(){ 
		setEmptyTo("·");
	}

	public void setEmptyToMinus(){
		setEmptyTo("-");
	}

	public void setEmptyToDiv(){
		setEmptyTo(":");
	}

	public void toggleEmptyAndPlus(){
		if (getExtPraefix()==null){
			setEmptyToPlus();
		} else {
			setPlusToEmpty();
		}
	}
	
	// ------- Tester ----------------
	public boolean hasExtPraefix(){
		return (praefix!=null);
	}	
	
	
	public boolean hasExtNull(){
		return (praefix==null);
	}
	
	public boolean hasExtPlus(){
		return (praefix!=null && "+".equals(praefix.getTextContent()));
	}
	public boolean hasExtEmptyOrPlus(){
		return (praefix==null || "+".equals(praefix.getTextContent()));
	}
	public boolean hasExtEmptyOrTimes(){
		return (praefix==null || EElementHelper.isTimesOp(getExtPraefix()));
	}
	
	public boolean hasExtTimes(){
		return (praefix!=null) && EElementHelper.isTimesOp(getExtPraefix());
	}
	
	public boolean hasExtMinus(){
		return praefix!=null && "-".equals(praefix.getTextContent());
	}
	public boolean hasExtDiv(){
		return praefix!=null && ":".equals(praefix.getTextContent());
	}
	
	// -- Ausgabe 
	public static void showPraefix(Element e){
		System.out.println("*VZ "+ e.getNodeName()+ " " + e.getTextContent());
	}
	
}
