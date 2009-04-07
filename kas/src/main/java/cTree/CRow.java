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

package cTree;

import java.util.ArrayList;

import cTree.adapter.DOMElementMap;

public abstract class CRow extends CElement {

	// Getter
	public abstract CRolle getFirstElementRolle();
	public abstract CRolle getLastElementRolle();
	public int getCount(){
		int count = 1; 
		CElement cEl = this.getFirstChild();
		while (cEl.hasNextC()){
			cEl = cEl.getNextSibling();
			count++;
		}
		return count;
	}
	
	// boolesche Tester, warscheinlich nicht wichtig
	protected abstract boolean isNecessaryRow(CElement el);
	
	// Mutatoren
	protected abstract CElement tauscheMitVorzeichen(CElement el1,CElement el2, boolean nachRechts);	
	
	// Support für die Verschiebung in dem CTree
	public CElement moveRight(CElement el1) {
		if (el1.hasNextC()) {
			CElement el2 = el1.getNextSibling();
			return tauscheMitVorzeichen(el1, el2, true);
		}
		return el1;
	}

	public CElement moveLeft(CElement el2) {
		if (el2.hasPrevC()) {
			CElement el1 = el2.getPrevSibling();
			return tauscheMitVorzeichen(el1, el2, false);
		}
		return el2;
	}
	
	// ---------Support für die Vorzeichenbehandlung
    // noch nicht getestet
	public abstract void toggleAllVZ();
	
	// der erste Faktor wird nicht angetastet
	public abstract void toggleAllVZButFirst(boolean resolveFirstMinrow);
	
	// ------------------- insert und remove 
	public CElement insertBefore(CElement newChild, CElement refChild) {
		boolean hasPrev = refChild.hasPrevC();
		CElement e = super.insertBefore(newChild, refChild);
		if (hasPrev) {
			e.setCRolle(getLastElementRolle());
		} else {
			e.setCRolle(getFirstElementRolle());
			refChild.setCRolle(getLastElementRolle());
		}
		return DOMElementMap.getInstance().getCElement.get(e);
	}
	
	public CElement removeChild(CElement e, boolean correctNextRolle, boolean unregister, boolean withNormalParent){
		// evtl Rolle des nächsten umsetzen
		if (correctNextRolle && (e.getCRolle()==getFirstElementRolle()) && e.hasNextC()){
			e.getNextSibling().setCRolle(getFirstElementRolle());
			if (this instanceof CTimesRow){
				e.getNextSibling().setTimesToEmpty();
			} else {
				e.getNextSibling().setPlusToEmpty();
			}
		}
		return super.removeChild(e, correctNextRolle, unregister, withNormalParent);
	}
	
	// ----------- Bulkoperationen
	/* 
	 * enthält den Endwert nicht mehr
	 */
	public ArrayList<CElement> startTo(CElement stop) {
		ArrayList<CElement> result = new ArrayList<CElement>();
		if (hasChildC()) {
			CElement cElement = getFirstChild();
			if (cElement.equals(stop)) {
				return result;
			}
			CElement newElement = cElement.cloneCElement(true);
			result.add(newElement);
			while (cElement.hasNextC()) {
				cElement = cElement.getNextSibling();
				if (cElement.equals(stop)) {
					return result;
				}
				newElement = cElement.cloneCElement(true);
				result.add(newElement);
			}
		}
		return result;
	}

	/*
	 * enthält den Startwert noch nicht 
	 */
	public ArrayList<CElement> endFrom(CElement start) {
		ArrayList<CElement> result = new ArrayList<CElement>();
		if (hasChildC()) {
			CElement cElement = getFirstChild();
			while (!cElement.equals(start) && cElement.hasNextC()) {
				cElement = cElement.getNextSibling();
			}
			if (cElement.equals(start)) {
				while (cElement.hasNextC()) {
					cElement = cElement.getNextSibling();
					result.add(cElement.cloneCElement(true));
				}
			}
		}
		return result;
	}

	public ArrayList<CElement> getMemberList() {
		ArrayList<CElement> result = new ArrayList<CElement>();
		if (hasChildC()) {
			CElement cElement = getFirstChild();
			CElement newElement = cElement.cloneCElement(true);
			result.add(newElement);
			while (cElement.hasNextC()) {
				cElement = cElement.getNextSibling();
				newElement = cElement.cloneCElement(true);
				result.add(newElement);
			}
		}
		return result;
	}
	
	public static ArrayList<CElement> cloneList(ArrayList<CElement> oldList){
		ArrayList<CElement> newList = new ArrayList<CElement>(); 
		for (CElement el : oldList){
			newList.add(el.cloneCElement(true));
		}
		return newList;
	}
	
	public static ArrayList<CElement> makeSingleElementList(CElement el){
		ArrayList<CElement> result = new ArrayList<CElement>();
		result.add(el);
		return result;
	}
	
	public static ArrayList<CElement> merge(ArrayList<CElement> first, ArrayList<CElement> second){
		for (CElement el : second){
			first.add(el);
		}
		return first;
	}
	
	public static CRow createRow(ArrayList<CElement> list, String typ){
		if (list.isEmpty()){
			return null;
		} else {
			CRow newRow = (CRow) CElementHelper.createAll(list.get(0).getElement(), "mrow", typ, CRolle.UNKNOWN, null);
			boolean isFirst = true;
			for (CElement el : list){
				if ("*".equals(typ)){
					CTimesRow.insertMember(newRow, el, isFirst);
				} else {
					CPlusRow.insertMember(newRow, el, isFirst);
				}
				isFirst = false;
				System.out.println("Inserting Member"); 
			}
			return newRow;
		}
	}
	
	public static void showAll(ArrayList<CElement> list){
		for (CElement el : list){
			el.show();
			System.out.println("------------------");
		}
	}
	
	// ------------ normalize - Support - wohl nicht genutzt
	protected void loescheLeereChildRows() {
		if (this.hasChildC()) {
			CElement actChild = this.getFirstChild();
			CElement nextChild = actChild.getNextSibling();
			do {
				if (actChild instanceof CTimesRow
						&& (actChild.hasChildC() == false)) {
					this.removeChild(actChild, true, true, false);
				}
				actChild = nextChild;
				if (actChild != null && actChild.hasNextC()) {
					nextChild = actChild.getNextSibling();
				}
			} while ((actChild != null) && actChild.hasNextC());
		}
	}
	
	protected void loescheChildRowWithOneChild(){
		if (this.hasChildC()){
			CElement actChild = this.getFirstChild();
			CElement nextChild = actChild.getNextSibling();
			do {
				if (!isNecessaryRow(actChild)){
					CElement childsChild = actChild.getFirstChild();
					if (actChild.hasExtMinus()){
						childsChild.togglePlusMinus(false);
					} else if (actChild.hasExtDiv()){
						childsChild.toggleTimesDiv(false);
					}
					replaceChild(childsChild, actChild, false, false);
					if (childsChild.getCRolle()==getLastElementRolle()){
						childsChild.setEmptyToPlus();
					}
				}
				actChild = nextChild;
				if (actChild!=null && actChild.hasNextC()){ nextChild = actChild.getNextSibling();}
			} while ((actChild != null) && actChild.hasNextC());
		} 
	}

	public void normalize() {
		loescheLeereChildRows();
		loescheChildRowWithOneChild();
		// loeseDieseRowInGrosserGleichartigerRowAuf();
	};
}

