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

package cTree.cCombine;

import org.w3c.dom.Element;

import cTree.*;
import cTree.adapter.EElementHelper;

// wegen toggle noch gelassen

public class CC_PunktIdentPot extends CC_ {
	
	private boolean gleicheDiv(Element op1, Element op2){
		boolean result = (EElementHelper.isTimesOp(op1)&&EElementHelper.isTimesOp(op2));
		return result || (":".equals(op1.getTextContent()) && ":".equals(op2.getTextContent()));
	}
	
	private boolean justTwo(CElement first, CElement second){
		return !(first.hasPrevC() || second.hasNextC());
	}

	public CElement combine(CElement parent, CElement ident, CElement oldPot){
    	System.out.println("Multipliziere Ident and Pot");
    	ident.removeCActiveProperty();
    	boolean replace = justTwo(ident, oldPot);
    	CElement newChild = null;
    	boolean toggle = false;
    	// erster Faktor	
    	if (ident.getCRolle()==CRolle.FAKTOR1){
			if (oldPot.hasExtDiv()){
				ident.setCActiveProperty();
				return ident;
			} else {
				int iExp = ((CNum)((CPot) oldPot).getExponent()).getValue();
				CElement newBase = ident.cloneCElement(false); // parent.cloneChild(cE1, false);
				CElement newExp = CNum.createNum(parent.getElement(), ""+(iExp+1));
				newChild = CPot.createPot(newBase, newExp);
				newChild.setCRolle(ident.getCRolle());
			}
		// weitere Faktoren	
		} else {
			if (gleicheDiv(ident.getExtPraefix(), oldPot.getExtPraefix())){
				int iExp = ((CNum)((CPot) oldPot).getExponent()).getValue();
				CElement newBase = ident.cloneCElement(false); // parent.cloneChild(cE1, false);
				CElement newExp = CNum.createNum(parent.getElement(), ""+(iExp+1));
				newChild = CPot.createPot(newBase, newExp);
				newChild.setCRolle(ident.getCRolle());
			} else {
				int iExp = ((CNum)((CPot) oldPot).getExponent()).getValue();
				if (iExp>0){
					toggle = true; // anderes Vorzeichen da sich das von der Potenz durchsetzt
					CElement newBase = ident.cloneCElement(false); // parent.cloneChild(cE1, false);
					CElement newExp = CNum.createNum(parent.getElement(), ""+(iExp-1));
					newChild = CPot.createPot(newBase, newExp);
					newChild.setCRolle(ident.getCRolle());
				} else {
					ident.setCActiveProperty();
					return ident;
				}
			}
		}
    	
		if (replace){
			System.out.println("// replace");
			parent.getParent().replaceChild(newChild, parent, true, true);
		} else {
			System.out.println("// insert");
			if (toggle){
				ident.toggleTimesDiv(false);
			}
			parent.replaceChild(newChild, ident, true, true);
			parent.removeChild(oldPot, true, true, false);
		}
		newChild.setCActiveProperty();
		return newChild;
	}
}
