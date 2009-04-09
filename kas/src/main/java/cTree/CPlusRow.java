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

public class CPlusRow extends CRow{
	
	public CPlusRow(Element element){
		this.element = element;
		cRolle = CRolle.UNKNOWN;
	}
	
	// ---------  getter 
	public CType getCType() {
		return CType.PLUSROW;
	}
	public CRolle getFirstElementRolle(){ return CRolle.SUMMAND1;}
	public CRolle getLastElementRolle(){ return CRolle.SUMMANDN1;}

	// -------- boolesche Tester
	// eine mrow kann unn�tig sein, wenn sie nur ein Child hat, aber nicht MathChild ist
	protected boolean isNecessaryRow(CElement el){
		if ("mrow".equals(el.getElement().getNodeName()) && el.hasChildC() && (el.getCRolle()!=CRolle.MATHCHILD)){
			 return el.getFirstChild().hasNextC();
		}
		return true;
	}
	
	// -------- bulk Praefix and Rolle Support
	public void correctInternalCRolles(){
		if (this.hasChildC()){
			CElement first = this.getFirstChild();
			first.setCRolle(CRolle.SUMMAND1);
			while (first.hasNextC()){
				first = first.getNextSibling();
				first.setCRolle(CRolle.SUMMANDN1);
			}
		}
	}
	
	public void correctInternalPraefixesAndRolle(){
		if (this.hasChildC()){
			CElement first = this.getFirstChild();
			if (first.hasExtMinus()){
				CMinTerm newFirst = CMinTerm.createMinTerm(first.cloneCElement(true));
				this.replaceChild(newFirst, first, true, false);
			} else if (first.hasExtPlus()){
				first.removePraefix();
			}
			while (first.hasNextC()){
				first = first.getNextSibling();
				if (!first.hasExtMinus() && !"+".equals(first.getExtPraefix())){
					first.setPraefix("+");
				}
			}
		}
		correctInternalCRolles();
	}
	
	public void toggleAllVZ(){
		System.out.println("!! CPlusRow VZ �ndern!!");
		if (hasChildC()){
			CElement summand = this.getFirstChild(); 
			// Sonderbehandlung von toggle f�r den ersten Summanden
			CElement weiter = summand.getNextSibling();
			if (summand.getCRolle()==CRolle.SUMMAND1){
				if (summand.getCType()==CType.MINROW){
					System.out.println("minrow durch ihren Inhalt ersetzen" + summand.getFirstChild().hasExtMinus());
					summand.getElement().removeChild(summand.getFirstChild().getExtPraefix());
					summand.getFirstChild().setExtPraefix(null);
					summand = replaceChild(summand.getFirstChild(), summand, true, false);
				} else {
					System.out.println("minrow einf�gen und summand in minrow einf�gen"); 
					CElement newContent = this.cloneChild(summand, false);
					CElement newMinRow = CMinTerm.createMinTerm(newContent, CRolle.SUMMAND1);
					summand = replaceChild(newMinRow, summand, true, true);
				}
			} else {
				summand.togglePlusMinus(false); 
			}
			while(weiter!=null){				
				weiter.togglePlusMinus(false); 
				weiter = weiter.getNextSibling();
			}
		}
	}
	
	/* beim ersten Summanden wird eine minrow in einen vzlosen Summanden gewandelt
	 * aber zB. ein Plus nicht in eine minrow
	 */
	public void toggleAllVZButFirst(boolean resolveFirstMinrow){
		System.out.println("A CPlusRow VZ �ndern!!");
		if (hasChildC()){
			CElement summand = this.getFirstChild(); 
			// Sonderbehandlung von toggle f�r den ersten Summanden
			CElement weiter = summand.getNextSibling();
			if (summand.getCRolle()==CRolle.SUMMAND1 && resolveFirstMinrow){
				if (summand.getCType()==CType.MINROW){
					CElement cArgument = ((CMinTerm) summand).getValue();
					cArgument.removePraefix();
					summand = replaceChild(cArgument, summand, true, false);
				}
			} else {
				System.out.println("So nicht VZ �ndern in Plusrow"); 
				summand.togglePlusMinus(false); 
			}
			while(weiter!=null){				 
				weiter.togglePlusMinus(false); 
				weiter = weiter.getNextSibling();
			}
		}
	}

	// Bulkoperatoren jeweils ohne Rollenanpassung. Fehlende Vz sind jeweils +
	
	public static ArrayList<CElement> createList(CElement cE1, CElement cE2){
		ArrayList<CElement> addendList = new ArrayList<CElement>(); 
		addendList.add(cE1.cloneCElement(true));
		CElement newCE2 = cE2.cloneCElement(true);
		if (!newCE2.hasExtMinus()){
			newCE2.setPraefix("+");
		}
		addendList.add(newCE2);
		return addendList;
	}
	
	/* es wird ohne Test einfach jeweils das erste Element entnommen und 
	 * die Vorzeichen umgesetzt aus Minrows werden zB Elemente mit VZ -
	 * ! Fold sollte auf einem Clone arbeiten
	 */
//	public static ArrayList<CElement> fold(ArrayList<CElement> list){
//		ArrayList<CElement> newList = new ArrayList<CElement>();
//		System.out.println("CPlusRow fold count list " + list.size());
//		for (CElement cEl : list){
//			CElement newElement;
//			if (cEl instanceof CTimesRow){
//				CTimesRow cTR = (CTimesRow) cEl;
//				System.out.println("Fold a TimesRow");
//				boolean timesRowAgain = false;
//				if (cTR.getFirstChild().hasNextC() && cTR.getFirstChild().getNextSibling().hasNextC()){
//					timesRowAgain = true;
//				}
//				if (timesRowAgain){
//					System.out.println("TimesRow found");
//					ArrayList<CElement> factorList = cTR.getMemberList();
//					factorList.remove(0);
////					factorList.get(0).removePraefix();
//					System.out.println("factorlistcount " + factorList.size());
//					CTimesRow newTR = CTimesRow.createRow(factorList);
//					newTR.correctInternalPraefixesAndRolle();
//					newTR.setCRolleAndPraefixFrom(cTR);
//					newElement = newTR;
//				} else {
//					// newElement ist das secondSibling evtl. mit dem externen Vorzeichen
//					System.out.println("Not Done yet");
//					newElement = cTR.getFirstChild().getNextSibling();
//					newElement.setCRolleAndPraefixFrom(cTR);
//				}
//			} else {
//				System.out.println("Fold-Fehler in CPlusRow");
//				newElement = cEl;
//			}
//			newList.add(newElement);
//		}
//		return newList;
//	}
	
	public static CPlusRow createRow(ArrayList<CElement> list){
		return (CPlusRow) CRow.createRow(list, "+");
	}
	
	
	// expects el as a Clone
	protected static void insertMember(CRow row, CElement el, boolean isFirst){
		System.out.println("Creating Member of PlusRow " + el.getText()); 
		if (el instanceof CPlusRow){
			// evtl Vorzeichen dem ersten Summand zuschlagen, dann alle einzeln als clone anh�ngen
			System.out.println("Creating Plusrow 1");
			CElement cEl = el.getFirstChild();
			CElement elel = cEl.cloneCElement(false);
			if (el.getExtPraefix()!=null){
				Element elpr = (Element) el.getExtPraefix().cloneNode(true);
				elel.setExtPraefix(elpr);
			} 
			row.appendPraefixAndChild(elel);
			while (cEl.hasNextC()){
				cEl = cEl.getNextSibling();
				row.appendPraefixAndChild(cEl.cloneCElement(true));
			}
		} else if (el instanceof CMinTerm){
			System.out.println("CPlusRow insertMember: Insert CMinTerm in CPlusRow "+ el.getText());
			CElement arg = el.cloneCElement(false);
			CElement newEl = CFences.createFenced(arg);
			if (row.hasChildC()){
				newEl.setCRolle(CRolle.SUMMANDN1);
				if (el.hasExtPraefix()){
					System.out.println("CPlusrow insertMember has Praefix" + el.getExtPraefix().getTextContent());
					newEl.setExtPraefix(el.getExtPraefix());
				} else {
					System.out.println("CPlusrow insertMember VZ plus gesetzt bei " + arg.getText());
					newEl.setPraefix("+");
				}
			} else {
				System.out.println("CPlusrow insertMember has no Child");
				newEl.setCRolle(CRolle.SUMMANDN1);
			}
			row.appendPraefixAndChild(newEl);
		} else {
			System.out.println("CPlusRow insertMember: Else in CPlusRow " + el.getText());
			if (isFirst && el.hasExtMinus()){
				System.out.println("Creating Plusrow 3 " + el.getText());
				CElement newEl = el.cloneCElement(true);
				row.appendChild(CMinTerm.createMinTerm(el.cloneCElement(true), CRolle.NACHVZMINUS));
				newEl.setCRolle(CRolle.SUMMANDN1);
			} else {
				System.out.println("Creating Plusrow 4 " + el.getText());
				CElement newEl = el.cloneCElement(true);
				row.appendPraefixAndChild(newEl);
				newEl.setCRolle(CRolle.SUMMANDN1);
			}
		}
		isFirst = false;
	}
	
	 // Support f�r die Klammern in dem CTree noch keine Vorzeichenanpassung
    public CElement fence(ArrayList<CElement> active){
    	System.out.println("Fencing in Summe" + active.get(0).hasExtMinus());
    	for (CElement el : active){
    		el.removeCLastProperty();
    	}
    	CElement result = null;
    	if (active.size()==1){
    		System.out.println("1 Summand");
    		result = this.standardFencing(active.get(0));
    	} else if (active.size()>1){
    		System.out.println("Mehr Summanden");
    		CElement first = active.get(0);
    		CElement innenRow = CElementHelper.createAl(this.getElement(), "mrow", "+", CRolle.GEKLAMMERT, "");
    		boolean newFirst = true;
    		CElement cNewEl = null;
    		for (CElement cEl : active){
    			if (newFirst){
    				if (cEl.hasExtMinus()){
    					System.out.println("Mit minus" + cEl.getText());
    					cNewEl = CElementHelper.createAll(getElement(), "minterm", "vzterm", CRolle.SUMMAND1, null);
    					CElement newArg = cEl.getParent().cloneChild(cEl, true);
    					newArg.setCRolle(CRolle.NACHVZMINUS);
    					cNewEl.appendChild(newArg);
    				} else {
    					System.out.println("Mit plus"+ cEl.getText());
    					cNewEl = cEl.getParent().cloneChild(cEl, false);
    					// cNewEl.removeCLastProperty();
    				}
    				cNewEl.setCRolle(CRolle.SUMMAND1);
    				newFirst = false;
    			} else {
    				cNewEl = cEl.getParent().cloneChild(cEl, true);
    			}
    			innenRow.appendChild(cNewEl);
    		}
    		result = CFences.createFenced(innenRow);
    		result.setCRolle(first.getCRolle());
    		result.setPraefixEmptyOrPlus();
    		insertBefore(result, first);
    		for (CElement cEl : active){
    			this.removeChild(cEl, false, true, false);
    		}
    		result.setCActiveProperty();
    	}
    	return result;
    }
	
	// Mutatoren
    protected CElement tauscheMitVorzeichen(CElement el1, CElement el2, boolean nachRechts){
    	CElement c1neu = null; 
		CElement c2neu = null;
    	if (el1.getCRolle()==CRolle.SUMMANDN1){
    		this.insertBefore(el2, el1);
    		c1neu = el1; 
    		c2neu = el2;
    	} else {
    		if (el1.getCType()==CType.MINROW){
    			c1neu = el1.cloneChild(el1.getFirstChild(), true);
    		} else {
    			c1neu = this.cloneChild(el1, true);
    			c1neu.setPraefix("+");
    		}
    		if (el2.hasExtMinus()){	
    			c2neu = CMinTerm.createMinTerm(cloneChild(el2, true));
    			c2neu.setCRolle(CRolle.SUMMAND1);
    		} else {
    			c2neu = cloneChild(el2, false);
    		}
    		insertBefore(c1neu, el1);
    		insertBefore(c2neu, c1neu);
    		removeChild(el1, false, true, false);
    		removeChild(el2, false, true, false);
    	}
    	if (nachRechts){
    		return c1neu;
    	} else {
    		return c2neu;
    	}
    } 
}
