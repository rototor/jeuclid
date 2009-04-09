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

import java.util.ArrayList;
import cTree.*;

public class CC_FracTRTR extends CC_ {
	
	protected boolean canCombine(CElement cE1, CElement cE2){
		System.out.println("Repell frac TR TR?");
		if (cE1.getFirstChild().getNextSibling().hasExtDiv() || cE2.getFirstChild().getNextSibling().hasExtDiv()){
			System.out.println("Zähler oder Nenner hat Div");
			return false;
		}
		if (!cE1.getFirstChild().getText().equals(cE2.getFirstChild().getText())){
			System.out.println("Zähler und Nenner beginnen verschieden");
			return false;
		}
		return true;
	}
	
	protected CFrac createCombination(CElement parent, CElement firstTR, CElement secondTR){
		ArrayList<CElement> foldedList = CTimesRow.fold(CTimesRow.castList(CTimesRow.createList(firstTR, secondTR)));
		CFrac newFrac = CFrac.createFraction(foldedList.get(0), foldedList.get(1));
		return newFrac;
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		// parent ist frac cE1 der Zähler cE2 der Nenner
		if (!canCombine(cE1, cE2)) {return cE1;}
		  cE1.removeCActiveProperty();
		  CFrac newChild = createCombination(parent, cE1, cE2);
		  parent.getParent().replaceChild(newChild, parent, true, true); // false als Praefix?
		  newChild.getZaehler().setCActiveProperty();
		  return newChild.getZaehler();
	}
}
