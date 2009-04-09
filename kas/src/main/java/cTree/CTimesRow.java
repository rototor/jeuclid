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

import org.w3c.dom.Element;

import cTree.adapter.EElementHelper;

public class CTimesRow extends CRow {
	
	public CTimesRow(Element element){
		this.element = element;
	}
	
	public CType getCType() {
		return CType.TIMESROW;
	}
	
	public CRolle getFirstElementRolle(){ return CRolle.FAKTOR1;}
	public CRolle getLastElementRolle(){ return CRolle.FAKTORN1;}

	public void correctInternalCRolles(){
		if (this.hasChildC()){
			CElement first = this.getFirstChild();
			first.setCRolle(CRolle.FAKTOR1);
			while (first.hasNextC()){
				first = first.getNextSibling();
				first.setCRolle(CRolle.FAKTORN1);
			}
		}
	}
	
	public void correctInternalPraefixesAndRolle(){
		if (this.hasChildC()){
			CElement first = this.getFirstChild();
			if (first.hasExtPraefix()){
				this.getElement().removeChild(first.getExtPraefix());
				this.setPraefix("");
			} else if (first.hasExtTimes()){
				first.removePraefix();
			}
			while (first.hasNextC()){
				first = first.getNextSibling();
				if (!first.hasExtDiv() && !first.hasExtTimes()){
					first.setPraefix("*");   // "·"
				}
			}
		}
		correctInternalCRolles();
	}
	
	// ---------Support für die Vorzeichenbehandlung
    // noch nicht getestet
	public void toggleAllVZ(){
		if (hasChildC()){
			CElement factor = getFirstChild(); 
			while(factor!=null){			
				factor.toggleTimesDiv(false); 
				factor = factor.getNextSibling();
			}
		}
	}
	
	// der erste Faktor wird nicht angetastet
	public void toggleAllVZButFirst(boolean resolveFirstMinrow){
		if (hasChildC()){
			CElement factor = this.getFirstChild(); 
			CElement weiter = factor.getNextSibling();
			while(weiter!=null){
				weiter.toggleTimesDiv(false);
				weiter = weiter.getNextSibling();	
			}
		}
	}
	
	// expects el as a Clone
	protected static void insertMember(CRow list, CElement toInsertClone, boolean isFirst){
		System.out.println("Creating Member of TimesRow"); 
		if (toInsertClone instanceof CTimesRow){
			// evtl Vorzeichen dem ersten Faktor zuschlagen, dann alle einzeln als clone anhängen
			CElement cEl = toInsertClone.getFirstChild();
			CElement elel = cEl.cloneCElement(false);
			if (toInsertClone.getExtPraefix()!=null){
				Element elpr = (Element) toInsertClone.getExtPraefix().cloneNode(true);
				elel.setExtPraefix(elpr);
			}
			if (!isFirst && (elel.getExtPraefix()==null)){
				elel.setPraefix("*");
			}
			list.appendPraefixAndChild(elel);
			elel.setCRolle(CRolle.FAKTORN1);
			while (cEl.hasNextC()){
				cEl = cEl.getNextSibling();
				list.appendPraefixAndChild(cEl.cloneCElement(true));
			}
		} else if (toInsertClone instanceof CMinTerm || toInsertClone instanceof CPlusRow){
			CElement newArg = toInsertClone.cloneCElement(false);
			CElement newEl = CFences.createFenced(newArg);
			newEl.setPraefix("*");
			list.appendPraefixAndChild(newEl);
			newEl.setCRolle(CRolle.FAKTORN1);
		} else {
			CElement newEl = toInsertClone.cloneCElement(true);
			if (!isFirst && !newEl.hasExtDiv() && !newEl.hasExtTimes()){
				newEl.setPraefix("*");
			}
			list.appendPraefixAndChild(newEl);
			newEl.setCRolle(CRolle.FAKTORN1);
		}
		isFirst = false;
	};

	public static CTimesRow createRow(ArrayList<CElement> list){
		return (CTimesRow) CRow.createRow(list, "*");
	}
	
	public static ArrayList<CElement> createList(CElement cE1, CElement cE2){
		ArrayList<CElement> factorList = new ArrayList<CElement>(); 
		factorList.add(cE1.cloneCElement(true));
		CElement newCE2 = cE2.cloneCElement(true);
//		if (!newCE2.hasExtDiv()){
//			newCE2.setPraefix("*");
//		}
		factorList.add(newCE2);
		return factorList;
	}
	
	public static ArrayList<CTimesRow> cloneList(ArrayList<CTimesRow> oldList){
		ArrayList<CTimesRow> newList = new ArrayList<CTimesRow>(); 
		for (CTimesRow el : oldList){
			newList.add((CTimesRow) el.cloneCElement(true));
		}
		return newList;
	}
	
	public static ArrayList<CTimesRow> castList(ArrayList<CElement> oldList){
		ArrayList<CTimesRow> newList = new ArrayList<CTimesRow>(); 
		for (CElement cEl : oldList){
			newList.add((CTimesRow) cEl);
		}
		return newList;
	}
	
	public static ArrayList<CElement> fold(ArrayList<CTimesRow> list){
		ArrayList<CElement> result = new ArrayList<CElement>();
		for (CElement cEl : list){
			CElement newElement;
			CTimesRow cTR = (CTimesRow) cEl;
			System.out.println("Fold a TimesRow");
			boolean timesRowAgain = false;
			if (cTR.getFirstChild().hasNextC() && cTR.getFirstChild().getNextSibling().hasNextC()){
				timesRowAgain = true;
			}
			if (timesRowAgain){
				System.out.println("TimesRow found");
				ArrayList<CElement> factorList = cTR.getMemberList();
				factorList.remove(0);
//				factorList.get(0).removePraefix();
				System.out.println("factorlistcount " + factorList.size());
				CTimesRow newTR = CTimesRow.createRow(factorList);
				newTR.correctInternalPraefixesAndRolle();
				newTR.setCRolleAndPraefixFrom(cTR);
				newElement = newTR;
			} else {
				// newElement ist das secondSibling evtl. mit dem externen Vorzeichen
				System.out.println("Not Done yet");
				newElement = cTR.getFirstChild().getNextSibling();
				newElement.setCRolleAndPraefixFrom(cTR);
			}
			result.add(newElement);
		}
		return result;
	}
	
	public static ArrayList<CElement> map(ArrayList<CElement> list, CElement second){
		ArrayList<CElement> result = new ArrayList<CElement>();
		for (CElement first : list){
			ArrayList<CElement> args = new ArrayList<CElement>(); 
			CElement newFirst = first.cloneCElement(false);
			Element res = null;
			if (first.getExtPraefix()!=null){
				res = (Element) first.getExtPraefix().cloneNode(true);
			}
			args.add(newFirst);
			CElement newSecond = second.cloneCElement(true);		
			args.add(newSecond);
			CElement prod = CTimesRow.createRow(args);
			if (res!=null){
				prod.setExtPraefix(res);
			}
			result.add(prod);
		}
		return result;
	}
	
	public static ArrayList<CElement> map(CElement first, ArrayList<CElement> list){
		ArrayList<CElement> result = new ArrayList<CElement>();
		for (CElement second : list){
			ArrayList<CElement> args = new ArrayList<CElement>(); 
			CElement newFirst = first.cloneCElement(false);
			args.add(newFirst);
			CElement newSecond = second.cloneCElement(true);
			newSecond.setExtPraefix(EElementHelper.createOp(first.getElement(), "*"));
			Element res = null;
			if (second.getExtPraefix()!=null){
				res = (Element) second.getExtPraefix().cloneNode(true);
			}
			args.add(newSecond);
			CElement prod = CTimesRow.createRow(args);
			if (res!=null){
				prod.setExtPraefix(res);
			}
			
			result.add(prod);
		}
		return result;
	}
	
	 // Support für die Klammern in dem CTree nicht auf Vorzeichen geachtet kommt später
    public CElement fence(ArrayList<CElement> active){
    	for (CElement el : active){
    		el.removeCLastProperty();
    	}
    	CElement result = null;
    	if (active.size()==1){
    		result = this.standardFencing(active.get(0));
    	} else if (active.size()>1){
    		CElement first = active.get(0);
    		Element opExt = null;
    		if (first.getCRolle()==CRolle.FAKTOR1){
    			result = CElementHelper.createAl(this.getElement(), "mfenced", "mfenced",first.getCRolle(), "");
    		} else {
    			opExt = (Element) active.get(0).getExtPraefix().cloneNode(true);
    			result = CElementHelper.createAll(this.getElement(), "mfenced", "mfenced",first.getCRolle(), opExt);
    		}
    		CElement innenRow = CElementHelper.createAl(this.getElement(), "mrow", "*", CRolle.GEKLAMMERT, "");
    		result.appendChild(innenRow);
    		boolean newFirst = true;
    		CElement cNewEl = null;
    		for (CElement cEl : active){
    			if (newFirst){
    				cNewEl = cEl.getParent().cloneChild(cEl, false);
    				cNewEl.setCRolle(CRolle.FAKTOR1);
    				cNewEl.removeCActiveProperty();
    			} else {
    				cNewEl = cEl.getParent().cloneChild(cEl, true);
    				if (opExt!=null && ":".equals(opExt.getTextContent())){
    					cNewEl.toggleTimesDiv(false);
    				}
    				cNewEl.removeCLastProperty();
    			}
    			innenRow.appendChild(cNewEl);
    			newFirst = false;
    		}
    		insertBefore(result, first);
    		for (CElement cEl : active){
    			this.removeChild(cEl, false, true, false);
    		}
    		result.setCActiveProperty();
    	}
    	return result;
    }
    
	protected boolean isNecessaryRow(CElement el){
		if (el.getCType()==CType.TIMESROW && el.hasChildC()){
			 return el.getFirstChild().hasNextC();
		}
		return true;
	}
	
	public boolean canRemoveChild(CElement e){
		if (e.getCRolle()==CRolle.FAKTOR1 && e.hasNextC() && e.getNextSibling().hasExtDiv()){
			return false;
		}
		return true;
	}
	
	public CElement removeChild(CElement e, boolean correctNextRolle, boolean unregister, boolean withNormalParent){
		if (canRemoveChild(e)){
			return super.removeChild(e, correctNextRolle, unregister, withNormalParent);
		} else {
			return e;
		}
	}
	
	public CNum getKoeffAsBetragFromMonom(){
		CElement num = getFirstChild(); 
		if (num instanceof CNum){
			return (CNum) num; 
		} else {
			CNum cEl = (CNum) CElementHelper.createAll(getElement(), "mn", "mn", CRolle.FAKTOR1, null);
			cEl.setText("1");
			return cEl;
		}
	}
	
	public CElement getFirstVarFromMonom(){
		CElement num = getFirstChild(); 
		if (num instanceof CNum){
			if (num==null){
				return null; 
			} else {
				return num.getNextSibling();
			}
		} else {
			return num;
		}
	}
	
	private boolean canBeMonomFaktor(CElement cEl){
		return (cEl instanceof CNum) || (cEl instanceof CIdent) || (cEl instanceof CPot);
	}
	
	private boolean canBeInMonomVars(CElement cEl){
		return (cEl instanceof CIdent) || (cEl instanceof CPot);
	}
	
	// null ist kein Monom
	public boolean isMonom(){
		boolean result = true; 
		CElement f = getFirstChild(); 
		if (f==null || !canBeMonomFaktor(f)){
			return false;
		} else {
			while (f.hasNextC()){
				f = f.getNextSibling();
				result = result && canBeInMonomVars(f);
			}
		}
		String simpleVarString = getSimpleVarString();
		System.out.println("SimpleVarString is: " + simpleVarString);
		char[] toSort = simpleVarString.toCharArray();
		java.util.Arrays.sort(toSort);
		boolean isSorted = java.util.Arrays.equals(simpleVarString.toCharArray(), toSort);
		return result && isSorted;
	}
	
	// ist gleichartig mit anderer TimesRow, wenn beide gleichartige sortierte Monome sind
    public boolean istGleichartigesMonom(CElement e2){
    	if (e2 instanceof CTimesRow){
    		String s1 = getVarString();
    		String s2 = ((CTimesRow) e2).getVarString();
    		boolean result = !(s1.equals("Fehler") || s2.equals("Fehler"));
    		result = result && (this.isMonom()) && (((CTimesRow) e2).isMonom()) && (s1.equals(s2));
    		return result;
    	} else if (e2.getCType() ==CType.MINROW) {
			return this.istGleichartigesMonom(((CMinTerm) e2).getValue());
    	} else if (e2 instanceof CIdent){
			String s1 = getVarString();
    		String s2 = ((CIdent) e2).getVar();
    		System.out.println("Stringvergleich: " + s1 + " " + s2);
    		boolean result = !(s1.equals("Fehler") || s2.equals("Fehler"));
    		result = result && (s1.equals(s2));
    		return result;
    	}
    	return false;
    }
    
    public String getVarString(){
    	String result=""; 
    	CElement vars = getFirstVarInMonomRow(); 
    	while (vars!=null){
    		if (vars instanceof CIdent){
    			result = result + ((CIdent) vars).getVar();
    		} else if (vars instanceof CPot){
    			result = result + ((CPot) vars).getSignatur();
    		} else {
    			return "Fehler";
    		}
    		vars = vars.getNextSibling();
    	}
    	return result;
    }
    
    private String getSimpleVarString(){
    	String result=""; 
    	CElement vars = getFirstVarInMonomRow(); 
    	while (vars!=null){
    		if (vars instanceof CIdent){
    			result = result + ((CIdent) vars).getVar();
    		} else if (vars instanceof CPot){
    			result = result + ((CPot) vars).getVar();
    		} else {
    			return "Fehler";
    		}
    		vars = vars.getNextSibling();
    	}
    	return result;
    }
    
	public CElement getFirstVarInMonomRow() {
		CElement e1 = this.getFirstChild();
		if (e1 instanceof CNum && e1.hasNextC()){
			return e1.getNextSibling();
		}
		return e1;
	}
    
    protected CElement tauscheMitVorzeichen(CElement el1, CElement el2, boolean nachRechts){
    	if (el1.getCRolle()==CRolle.FAKTORN1){
    		insertBefore(el2, el1);
    	} else if (!el2.hasExtDiv()){
//    		System.out.println(el1.getExtPraefix().getTextContent());
    		CElement el2neu = el2.cloneCElement(false);
    		el2neu.setCRolle(CRolle.FAKTOR1);
    		insertBefore(el2neu, el1);
    		el1.setPraefix("*");
    		el1.setCRolle(CRolle.FAKTORN1);
    		el1.getParent().removeChild(el2, false, true, false);
    	}
    	if (nachRechts){
    		return el1;
    	} else {
    		return el2;
    	}
    } 
	
}
