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

import cTree.*;
import cTree.adapter.EElementHelper;

public class CC_PunktPotPot extends CC_ {
	
	protected boolean canCombine(CElement cE1, CElement cE2){
		System.out.println("Repell pot times pot?"); 
		CPot pot1 = (CPot) cE1; 
		CPot pot2 = (CPot) cE2;
    	int exp1 = ((CNum)(pot1).getExponent()).getValue();
    	int exp2 = ((CNum)(pot2).getExponent()).getValue();
		if (!pot1.getBasis().getText().equals(pot2.getBasis().getText())) {return false;}
		if (pot1.getCRolle()==CRolle.FAKTOR1 && pot2.hasExtDiv() && (exp2>exp1)) {return false;}	
		return true;
	}
	
	private boolean gleicheDiv(Element op1, Element op2){
		boolean result = (EElementHelper.isTimesOp(op1)&&EElementHelper.isTimesOp(op2));
		return result || (":".equals(op1.getTextContent()) && ":".equals(op2.getTextContent()));
	}

	protected CElement createCombination(CElement parent, CElement firstPot, CElement secondPot){
		CElement newChild = null;
    	int exp1 = ((CNum)((CPot) firstPot).getExponent()).getValue();
    	int exp2 = ((CNum)((CPot) secondPot).getExponent()).getValue();
    	// erster Faktor	
    	if (firstPot.getCRolle()==CRolle.FAKTOR1){
			if (secondPot.hasExtDiv() && (exp2>exp1)){
				System.out.println("Should not happen");
				firstPot.setCActiveProperty();
				return firstPot;
			} else {
				CPot newPot = (CPot) firstPot.cloneCElement(true); // parent.cloneChild(firstPot, true);
				int newExp = exp1+exp2; 
				if (secondPot.hasExtDiv()){
					newExp = exp1-exp2;
				}
				newPot.getExponent().setText(""+newExp);
				newChild = newPot;
			}
		// weitere Faktoren	
		} else {
			if (gleicheDiv(firstPot.getExtPraefix(), secondPot.getExtPraefix())){
				CPot newPot = (CPot) firstPot.cloneCElement(false); // parent.cloneChild(firstPot, false);
				int newExp = exp1+exp2; 
				newPot.getExponent().setText(""+newExp);
				newChild = newPot;
			} else {
				if (exp1>=exp2){
					CPot newPot = (CPot) firstPot.cloneCElement(false); // parent.cloneChild(firstPot, false);
					int newExp = exp1-exp2; 
					newPot.getExponent().setText(""+newExp);
					newChild = newPot;
				} else {
					// Element op = EElementHelper.createOp(parent.getElement(), ":");
					CPot newPot = (CPot) firstPot.cloneCElement(false); //parent.cloneChild(firstPot, false);
					int newExp = exp2-exp1; 
					newPot.getExponent().setText(""+newExp);
					// newPot.setExtPraefix(op);
					newChild = newPot;
				}
			}
		}
		return newChild;
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		if (!canCombine(cE1, cE2)) {return cE1;}
		  cE1.removeCActiveProperty();
		  boolean replace = CombineHandler.getInstance().justTwo(cE1, cE2);
		  // evtl muss man das Zeichen vor cE1 ändern
		  int exp1 = ((CNum)((CPot) cE1).getExponent()).getValue();
		  int exp2 = ((CNum)((CPot) cE2).getExponent()).getValue();
		  boolean toggle = (cE1.hasExtDiv() && !cE2.hasExtDiv() && (exp2>exp1));
		  toggle = toggle || (!cE1.hasExtDiv() && cE2.hasExtDiv() && (exp2>exp1));
		  System.out.println("Toggle?" + toggle);
		  CElement newChild = createCombination(parent, cE1, cE2);
		  CombineHandler.getInstance().insertOrReplace(parent, newChild, cE1, cE2, replace);
		  if (toggle){
			  newChild.toggleTimesDiv(false);
		  }
		  newChild.setCActiveProperty();
		  return newChild;
	}
}
