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

public class CMinTerm extends CElement{
	
	public CMinTerm(Element element){
		this.element = element;
	}
	
	public CType getCType() {
		return CType.MINROW;
	}
	
	public static CMinTerm createMinTerm(CElement inhalt){
		return createMinTerm(inhalt, CRolle.UNKNOWN);
	}
	
	public static CMinTerm createMinTerm(CElement inhalt, CRolle cRolle){
		CMinTerm minTerm = (CMinTerm) CElementHelper.createAll(inhalt.getElement(), "mrow", "vzterm", cRolle, null);
		minTerm.appendPraefixAndChild(inhalt);
		if (!inhalt.hasExtMinus()){
			inhalt.setPraefix("-");
		} 
		inhalt.setCRolle(CRolle.NACHVZMINUS);
		return minTerm;
	}	
	
	public boolean hatGleichenBetrag(CElement cE2){
		if (cE2 instanceof CMinTerm){
			return this.getValue().hatGleichenBetrag(((CMinTerm) cE2).getValue());
		} else {
			return this.getValue().hatGleichenBetrag(cE2);
		}
	}
	
    public boolean istGleichartigesMonom(CElement e2){
    	return this.getValue().istGleichartigesMonom(e2);
    }
	
	public CElement getValue(){
		return getFirstChild();
	}
	
	public void normalize(){};
	

	
}
