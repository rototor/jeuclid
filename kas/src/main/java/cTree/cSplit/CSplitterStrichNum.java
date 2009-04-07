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

public class CSplitterStrichNum extends CSplitter1 {
	
	public CElement split(CElement parent, CElement zuZerlegen, String s){
		System.out.println("Split Num and Num");
		if (s!=""){
			String opString = ""+s.charAt(0);
			try {
				int zahl = Integer.parseInt(s.substring(1));
				if (zuZerlegen instanceof CNum){
					CNum cEl = (CNum) zuZerlegen;
					int cZahl = Integer.parseInt(cEl.getText());
					boolean zuZerlegenHatMinus = cEl.hasExtMinus();
					if ("+".equals(opString)){
						zerlegeStrich(cZahl, zahl, zuZerlegenHatMinus, cEl);
					} else if ("-".equals(opString)){
						zerlegeStrich(cZahl, (-1)*zahl, zuZerlegenHatMinus, cEl);
					} else if ("*".equals(opString) && (zahl!=0) && (cZahl % zahl == 0)){
						zuZerlegen = zerlegePunkt(parent, cZahl, zahl, "*", cEl);
					} else if (":".equals(opString) && (zahl!=0)){
						zuZerlegen = zerlegePunkt(parent, cZahl, zahl, ":", cEl);
					} 
				}
			} catch (NumberFormatException e){
				String var = ""+s.charAt(1);
				if (zuZerlegen instanceof CPot){
					CPot cPot = (CPot) zuZerlegen;
					if (cPot.getBasis().getElement().getTextContent().equals(var)){
						System.out.println("Ich kann das");
					} else {
						System.out.println("Ich kann das nicht");
					}
				}
			}
		}
		return zuZerlegen;
	}
	
    private void zerlegeStrich(int bisher, int neu, boolean bisherMin, CNum cEl){
    	// den geänderten Term errechnen
    	int geAendert = bisher - neu; 							
		if (bisherMin){geAendert = 0 - bisher - neu;};	//
		boolean ergebnisNegativ = (geAendert<0);
		cEl.setText(""+Math.abs(geAendert));
		// das CElement für den neuen Summanden erstellen
		String neuS = "-"; if (neu>=0) {neuS ="+";}	
		CElement newChild = CNum.createNum(cEl.getElement(), ""+Math.abs(neu));
		newChild.setCRolle(CRolle.SUMMANDN1);
		newChild.setPraefix(neuS);
		cEl.getParent().insertBefore(newChild, cEl);
		cEl.getParent().insertBefore(cEl, newChild);
		if (ergebnisNegativ && !bisherMin){cEl.togglePlusMinus(true);}
		if (!ergebnisNegativ && bisherMin){cEl.togglePlusMinus(true);}
    }
    private CElement zerlegePunkt(CElement parent, int bisher, int neu, String pop, CNum cEl){	
		cEl.removeCActiveProperty();
		CElement cFirst = cEl.cloneCElement(false);
		int geAendert = bisher * neu; if ("*".equals(pop)) {geAendert = bisher / neu; }
		cFirst.setText(""+geAendert);
		cFirst.setCRolle(CRolle.FAKTOR1);
		CElement cSecond = CNum.createNum(cEl.getElement(), ""+neu);
		String s = ":"; if ("*".equals(pop)){s="·";}
		cSecond.setPraefix(s);
		CRow cTRow = CTimesRow.createRow(CTimesRow.createList(cFirst, cSecond));
		cTRow.setCActiveProperty();
		cEl.getParent().replaceChild(cEl, cTRow, true, true);
		return cTRow;
    }
	
    
    
}
