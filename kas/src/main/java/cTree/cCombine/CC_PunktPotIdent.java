/*
 * Copyright 2009 Erhard Kuenzel 03/09
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

import cTree.CElement;
import cTree.CNum;
import cTree.CPot;
import cTree.CRolle;
import cTree.adapter.EElementHelper;


public class CC_PunktPotIdent extends CC_ {
	
	protected CElement createCombination(CElement parent, CElement firstPot, CElement ident){
		System.out.println("Multipliziere Pot Ident");
    	CElement newChild = null;
    	// erster Faktor	
    	if (firstPot.getCRolle()==CRolle.FAKTOR1){
			if (ident.hasExtDiv()){
				int iExp = ((CNum)((CPot) firstPot).getExponent()).getValue();
				if (iExp>0){
					CPot newPot = (CPot) firstPot.cloneCElement(false);
					newPot.getExponent().setText(""+(iExp-1));
					newChild = newPot;
				} else {
					firstPot.setCActiveProperty();
					return firstPot;
				}
			} else {
				int iExp = ((CNum)((CPot) firstPot).getExponent()).getValue();
				CPot newPot = (CPot) firstPot.cloneCElement(false);
				newPot.getExponent().setText(""+(iExp+1));
				newChild = newPot;
			}
		// weitere Faktoren	
		} else {
			if (gleicheDiv(firstPot.getExtPraefix(), ident.getExtPraefix())){
				int iExp = ((CNum)((CPot) firstPot).getExponent()).getValue();
				CPot newPot = (CPot) firstPot.cloneCElement(false);
				newPot.getExponent().setText(""+(iExp+1));
				newChild = newPot;
			} else {
				int iExp = ((CNum)((CPot) firstPot).getExponent()).getValue();
				if (iExp>0){
					CPot newPot = (CPot) firstPot.cloneCElement(false);
					newPot.getExponent().setText(""+(iExp-1));
					newChild = newPot;
				} else {
					firstPot.setCActiveProperty();
					return firstPot;
				}
			}
		}
		return newChild;
	}
	
	protected boolean canCombine(CElement cE1, CElement cE2){
		System.out.println("Repell pot times ident?");
		return true;
	}
	
	private boolean gleicheDiv(Element op1, Element op2){
		boolean result = (EElementHelper.isTimesOp(op1)&&EElementHelper.isTimesOp(op2));
		return result || (":".equals(op1.getTextContent()) && ":".equals(op2.getTextContent()));
	}	
}
