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

public class CC_StrichMinrowNum extends CC_ {
	
	protected boolean canCombine(CElement cE1, CElement cE2){
		System.out.println(" Repell Add Minrow and Num?");
		if (!(((CMinTerm) cE1).getValue() instanceof CNum)) { return false;}
		return true;
	}
	
	protected CElement createCombination(CElement parent, CElement cE1, CElement cE2){
		int wertE = Integer.parseInt(((CNum) ((CMinTerm) cE1).getValue()).getText()); 
		int wertZ = Integer.parseInt(cE2.getText()); 
		CElement newChild = null;
		System.out.println("// Minterm in Summe muss erster Summand sein");
		if (!cE2.hasExtMinus() && (wertE<=wertZ)){ //So entsteht eine Zahl
			newChild = CNum.createNum(parent.getElement(), ""+(wertZ-wertE));
			newChild.setCRolle(CRolle.SUMMAND1);
		} else {  // So bleibt minrow
			System.out.println("// Minterm in Summe muss erster Summand sein");
			newChild = cE1.cloneCElement(false); //parent.cloneChild(cE1, false);
			int value = wertE-wertZ;
			if (cE2.hasExtMinus()){
				value = wertE+wertZ;
			}
			((CMinTerm) newChild).getValue().setText(""+value);
		}
		return newChild;
	}
}