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

public class CC_Strich0Any extends CC_ {
	
	protected boolean canCombine(CElement minTerm, CElement tRow){
		System.out.println("Repell 0 Any?"); 
		return true;
	}
	
	protected CElement createCombination(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Add 0 Any"); 
		CElement newChild = null;
		if (cE2.hasExtMinus() && cE1.getCRolle()==CRolle.SUMMAND1){ // bilde TimesRow	
			CElement newArg = cE2.cloneCElement(false); //parent.cloneChild(cE2, false);
			newChild = CMinTerm.createMinTerm(newArg, cE1.getCRolle());
		} else { // nimm cE2
			if ((cE1.hasExtMinus() && cE2.hasExtPlus()) || cE1.hasExtEmptyOrPlus() && cE2.hasExtMinus()){
				cE1.togglePlusMinus(false);
			}
			newChild = cE2.cloneCElement(false);  // parent.cloneChild(cE2, false);
		}
		return newChild;
	}
}
