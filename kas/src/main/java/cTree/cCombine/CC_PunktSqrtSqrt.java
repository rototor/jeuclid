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
import java.util.*;

public class CC_PunktSqrtSqrt extends CC_ {
	
	protected CElement createCombination(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Multipliziere Sqrts");
		if (cE1.hasExtDiv()){ 
			System.out.println("// es geht mit : los, darf nicht sein! Repeller verwenden");
		} 
		ArrayList<CElement> radList = CSqrt.getRadikandList(CSqrt.createRootList(cE1, cE2));
		CElement newChild = CSqrt.createSqrt(CTimesRow.createRow(radList));
    	if (cE1.getCRolle()!=CRolle.FAKTOR1){
    		newChild.setCRolle(CRolle.FAKTORN1);			
		}
		return newChild;
	}
	
	protected boolean canCombine(CElement cE1, CElement cE2){
		System.out.println("Repell sqrt times sqrt: only faktor1"); 
		if (cE1.hasExtDiv()) {return false;}
		return true;
	}
}
