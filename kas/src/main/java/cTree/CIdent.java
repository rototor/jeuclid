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

package cTree;

import org.w3c.dom.Element;

public class CIdent extends CElement{

	public CIdent(Element element){
		this.element = element;
	}
	
	public CType getCType() {
		return CType.IDENT;
	}
	
	public static CIdent createIdent(Element producer, String s){
		CIdent ident = (CIdent) CElementHelper.createAll(producer, "mi", "mi", CRolle.UNKNOWN, null);
		ident.setText(s); 
		return ident;
	}
	
	public boolean hatGleichenBetrag(CElement cE2){
		if (cE2 instanceof CIdent){
			return this.getText().equals(((CIdent) cE2).getText());
		} else if (cE2 instanceof CMinTerm){
			return hatGleichenBetrag(((CMinTerm) cE2).getValue());
		}
		return false;
	}
	
	public void normalize(){};
	
	public boolean istGleichartigesMonom(CElement el){
		boolean result = false;
		if (el.getCType()==CType.IDENT){
			result = (getElement().getTextContent().equals(el.getElement().getTextContent()));
		} else if (el.getCType() ==CType.MINROW) {
			return this.istGleichartigesMonom(((CMinTerm) el).getValue());
		} else if (el.getCType()==CType.POT){
			result = (getElement().getTextContent().equals(el.getElement().getFirstChild().getTextContent()));
		} else if (el.getCType()==CType.TIMESROW) {
			String s1 = getVar();
    		String s2 = ((CTimesRow) el).getVarString();
    		result = !(s1.equals("Fehler") || s2.equals("Fehler"));
    		result = result && (s1.equals(s2));
    		return result;
		}
		return result; 
	}
	
	public String getVar(){
		return getElement().getTextContent();
	}
	
	public String getSignatur(){
		return getVar();
	}
	
	public CPot toPot(){
		CPot cPot = CPot.createPot(this, 1);
		cPot.setCRolle(getCRolle());
		cPot.setExtPraefix(getExtPraefix());
		setExtPraefix(null);
		return cPot;
	}
	
}
