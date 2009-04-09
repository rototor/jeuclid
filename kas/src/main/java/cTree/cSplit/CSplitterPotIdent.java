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

package cTree.cSplit;

import cTree.*;

public class CSplitterPotIdent extends CSplitter1 {
	
	public CElement split(CElement parent, CElement basis, String s){
		System.out.println("Split Pot Ident");
		if (canSplit((CPot) parent, s)){
			basis.removeCActiveProperty();
			String oldPotPraefix = parent.getPraefixAsString();
			
			CPot cPot = (CPot) parent.cloneCElement(false);
			cPot.setCRolle(CRolle.FAKTOR1);
			cPot.setExponent(""+(((CPot)parent).getExponentValue()-1));
			
			CElement newIdent = CIdent.createIdent(parent.getElement(), ""+s.charAt(1));
			newIdent.setCRolle(CRolle.FAKTORN1);
			newIdent.setPraefix("*");
			// Vorsicht, hier wirdd mit Clones gearbeitet, cPot ist nicht Teil der CTimesRow
			CElement cFRow = CTimesRow.createRow(CTimesRow.createList(cPot, newIdent));
			
			CFences cFences = CFences.createFenced(cFRow);
			cFences.setCRolle(parent.getCRolle());
			cFences.setPraefix(oldPotPraefix);
			parent.getParent().replaceChild(cFences, parent, true, true);
			basis = ((CPot) cFences.getInnen().getFirstChild()).getBasis();
			basis.setCActiveProperty();
		}
		return basis;
	}
	
	public boolean canSplit(CPot parent, String s){
		if (s=="" && s.length()!=2) {return false;}
		if (!(""+s.charAt(0)).equals("*")) {return false;}		
		return (parent.getVar().equals(""+s.charAt(1))) && (parent.getExponentValue()>1);
	}	
}
