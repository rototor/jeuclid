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

import cTree.CElement;
import cTree.CElementHelper;
import cTree.CRolle;

public abstract class RolleAdapter extends CTreeMutator {
	
	protected CRolle cRolle;
	
	public CRolle getCRolle(){
		return cRolle;
	}
	// --- Setter die von der Rolle abhängen --------------
	public void setCRolle(CRolle c){
		cRolle = c;
	}
	public void setCRolleAndPraefixFrom(RolleAdapter c){
		cRolle = c.getCRolle();
		setExtPraefix(c.getExtPraefix());
	}
	public void setPraefixEmptyOrPlus(){ // ... - 3 -> ... + 3
		if (cRolle == CRolle.SUMMANDN1){  
			setPraefix("+");
		} else if ((praefix!=null) && cRolle == CRolle.SUMMAND1){
			removePraefix();
		}
	}
	public void toggleToPraefixEmptyOrPlus(){ // ... - 3 -> ... + 3
		if ("-".equals(praefix.getTextContent()) && cRolle == CRolle.SUMMANDN1){  
			setPraefix("+");
		} else if ("-".equals(praefix.getTextContent()) && cRolle == CRolle.SUMMAND1){
			removePraefix();
		}
	}
	public void toggleToTimesOrEmpty(){ // ... : 3 -> ... * 3
		if (":".equals(praefix.getTextContent()) && cRolle == CRolle.FAKTORN1){  
			setPraefix("·");  // : 3 -> 3
		} else if (":".equals(praefix.getTextContent()) && cRolle == CRolle.FAKTOR1){
			removePraefix();
		}
	}
	public void togglePlusMinus(boolean checkAllowed){
		if (checkAllowed && !CElementHelper.canHaveMinus(cRolle)) { return;}
		if (hasExtEmptyOrPlus()){
			setPraefix("-");
		} else {
			toggleToPraefixEmptyOrPlus();
		}
	}
	public void toggleTimesDiv(boolean checkAllowed){
		if (checkAllowed && !CElementHelper.canHaveDiv(cRolle)) { return;}
		if (hasExtEmptyOrTimes()){
			setPraefix(":");
		} else {
			toggleToTimesOrEmpty();
		}
	}
	// --- Mutatoren ---------------------------------------
    // der erste sollte bald entfernt werden
	public CElement cloneChild(CElement cEl, boolean withPraefix){
		CElement cE2 = cEl.cloneCElement(withPraefix);
		return cE2;
	}
	
	public CElement cloneCElement(boolean withPraefix){
		CElement cE2 = super.cloneCElement(withPraefix);
		cE2.setCRolle(getCRolle());
		return cE2;
	}
}
