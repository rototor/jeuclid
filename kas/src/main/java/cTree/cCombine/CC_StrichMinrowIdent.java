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

import cTree.*;

public class CC_StrichMinrowIdent extends CC_ {
	
	protected boolean canCombine(CElement minTerm, CElement ident){
		System.out.println("Repell add Minrow and Ident?"); 
		CElement minTermArg = ((CMinTerm) minTerm).getValue();
		if (!(minTermArg instanceof CIdent) && !(minTermArg instanceof CTimesRow)) {
			return false;
		} else if (minTermArg instanceof CIdent){
			// CIdent minArg = (CIdent) minTermArg;
			if (!minTermArg.istGleichartigesMonom(ident)){return false;}
		} else {
			// CTimesRow minArg = (CTimesRow) minTermArg;
			if (!minTermArg.istGleichartigesMonom(ident)){return false;}
		}
		return true;
	}
	
	protected CElement createCombination(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Add Minrow and Ident");
		CElement minTermArg = ((CMinTerm) cE1).getValue();
		CElement newChild = null;
		if (minTermArg instanceof CIdent){
			if (cE2.hasExtMinus()){ // bilde TimesRow
				CElement newFirst = CNum.createNum(parent.getElement(), "2");
				newFirst.setCRolle(CRolle.FAKTOR1);
				CElement newSecond = CNum.createNum(parent.getElement(), ((CMinTerm) cE1).getValue().getText());
				newSecond.setCRolle(CRolle.FAKTORN1);
				newSecond.setPraefix("*");
				CElement cTR = CTimesRow.createRow(CTimesRow.createList(newFirst, newSecond));
				newChild = CMinTerm.createMinTerm(cTR, cE1.getCRolle());
			} else { // bilde 0
				newChild = CNum.createNum(parent.getElement(), "0");
				newChild.setCRolle(CRolle.SUMMAND1);
			}
		} else { // minTermArg ist eine CTimesRow
			CTimesRow minArg = (CTimesRow) minTermArg;
			CNum cNum = minArg.getKoeffAsBetragFromMonom();
			if (cNum.getValue()==0){
				if (cE2.hasExtMinus()){
					minArg.getKoeffAsBetragFromMonom().setText("1");
					newChild = cE1.cloneCElement(false);
				} else {
					newChild = cE2.cloneCElement(false);
				}
			} else {
				int toAdd = cE2.hasExtMinus() ? 1 : -1;
				newChild = cE1.cloneCElement(true);
				int koeff = minArg.getKoeffAsBetragFromMonom().getValue();
				((CTimesRow) ((CMinTerm) newChild).getValue()).getKoeffAsBetragFromMonom().setText(""+(koeff + toAdd));
			}
		}
		return newChild;
	}
}
