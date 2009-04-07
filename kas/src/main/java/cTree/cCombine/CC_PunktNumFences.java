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

import java.util.ArrayList;

import cTree.*;

public class CC_PunktNumFences extends CC_ {
	
	
	protected CElement createCombination(CElement oldSumme, CElement cE1, CElement cE2){
		if (cE2.getFirstChild() instanceof CPlusRow){
			System.out.println("Multipliziere Num mit Klammer, die Summe enthält");
			ArrayList<CElement> oldAddendList = ((CPlusRow) cE2.getFirstChild()).getMemberList();
			ArrayList<CElement> newAddendList = CTimesRow.map(cE1, oldAddendList);
			CElement newChild = CFences.createFenced(CPlusRow.createRow(newAddendList));
			return newChild;
		} else { // only for MinTerm
			System.out.println("Multipliziere Num mit Klammer, die MinTerm enthält");
			CElement newFirstFactor = cE1.cloneCElement(false);
			CElement newSecondFactor = ((CMinTerm) cE2.getFirstChild()).getValue().cloneCElement(false);
			newSecondFactor.setPraefix("*");
			CTimesRow newTR = CTimesRow.createRow(CTimesRow.createList(newFirstFactor, newSecondFactor));
			CMinTerm newMinTerm = CMinTerm.createMinTerm(newTR);
			CFences newFences = CFences.createFenced(newMinTerm);
			return newFences;
		}
	}
	
	
	protected boolean canCombine(CElement el, CElement el2){
		System.out.println("Can Combine Num times Fences?");
		return ((!el2.hasExtDiv()) && ((el2.getFirstChild() instanceof CMinTerm)||(el2.getFirstChild() instanceof CPlusRow)));
	}
	
}
