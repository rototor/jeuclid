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

public class CNum extends CElement {
	
	public CNum(Element element){
		this.element = element;
	}
	
	public CType getCType() {
		return CType.NUM;
	}
	
	public static CNum createNum(Element producer, String s){
		CNum ident = (CNum) CElementHelper.createAll(producer, "mn", "mn", CRolle.UNKNOWN, null);
		ident.setText(s); 
		return ident;
	}
	
	public boolean hatGleichenBetrag(CElement cE2){
		if (cE2 instanceof CNum){
			return this.getValue()==((CNum) cE2).getValue();
		} else if (cE2 instanceof CMinTerm){
			return hatGleichenBetrag(((CMinTerm) cE2).getValue());
		}
		return false;
	}
	
    public boolean istGleichartigesMonom(CElement e2){
    	if (e2 instanceof CMinTerm){
    		return this.istGleichartigesMonom(((CMinTerm) e2).getValue());
    	}
    	return (e2 instanceof CNum);
    }
	public void normalize(){
		if ("mrow".equals(getElement().getNodeName()) && this.hasChildC()){
			CElement num = getFirstChild();
			num.setExtPraefix(getExtPraefix());
			this.setExtPraefix(null);
			this.getParent().replaceChild(getFirstChild(), this, true, false);
			
		}
	};
    
	public int getValue() throws NumberFormatException{
		return Integer.parseInt(this.getElement().getTextContent());
	}
	
	public void setValue(int i){
		getElement().setTextContent(""+i);
	}
	
    public boolean is0(){
    	return Integer.parseInt(this.getElement().getTextContent())==0;
    }
    
    public boolean is1(){
    	return Integer.parseInt(this.getElement().getTextContent())==1;
    }
}
