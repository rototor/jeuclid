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

public class CC_StrichNumNum extends CC_ {
	
	protected boolean canCombine(CElement minTerm, CElement tRow){
		return true;
	}
	
	protected CElement createCombination(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Add Num and Num");
		int wertE = Integer.parseInt(cE1.getElement().getTextContent()); 
		int wertZ = Integer.parseInt(cE2.getElement().getTextContent()); 
		CElement newChild = null;		
		if (cE1.getCRolle()==CRolle.SUMMAND1){
			System.out.println("// falls ein Summand1 dabei ist");
			if (cE2.hasExtMinus() && (wertE<wertZ)){ //So entsteht eine Minrow
				CElement arg = cE1.cloneCElement(false); // parent.cloneChild(cE1, false);
				arg.setText(""+(wertZ-wertE));
				newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
			} else {  // So entsteht eine Zahl als Summand1
				newChild = cE1.cloneCElement(false); //parent.cloneChild(cE1, false);
				int value = wertE+wertZ;
				if (cE2.hasExtMinus()){
					value = wertE-wertZ;
				}
				newChild.setText(""+value);
			}
		} else { 
			System.out.println("// falls weitere Summanden");
			int vzE = 1; if (cE1.hasExtMinus()) {vzE = -1;}
			int vzZ = 1; if (cE2.hasExtMinus()) {vzZ = -1;}
			int result = vzE*wertE+vzZ*wertZ;
			if (result*vzE<0){
				cE1.togglePlusMinus(false);
			}
			newChild = cE1.cloneCElement(false); //parent.cloneChild(cE1, false);
			newChild.setText(""+Math.abs(result));
		}
		return newChild;
	}
}
