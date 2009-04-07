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

package cTree.cExtract;

import cTree.*;
import cTree.cDefence.DefenceHandler;

import java.util.*;

import org.w3c.dom.*;

public class CE_2SqrtPunkt extends CE_1{
	
	public CElement extract(CElement parent, ArrayList<CElement>selection, CElement cE2){
		// selection.get(0) ist das Produkt, Parent die Wurzel
		System.out.println("Lazy extracting");
		if (!canExtract(selection)) {return selection.get(0);}
		  selection.get(0).removeCActiveProperty();

		  // Praefix sichern
		  CRolle rolle = parent.getCRolle();
		  CTimesRow newArg = createExtraction(parent, selection, cE2);
		  newArg.correctInternalPraefixesAndRolle();
		  CFences newChild = CFences.createFenced(newArg);
		  newArg.setCRolle(CRolle.GEKLAMMERT);
		  ExtractHandler.getInstance().insertOrReplace(parent, newChild, selection, true);
		  newChild.setCRolle(rolle);
		  newChild.setCActiveProperty();
		  return DefenceHandler.getInstance().defence(newChild.getParent(), newChild, newChild.getFirstChild()); 
	}
	
	protected CTimesRow createExtraction(CElement parent, ArrayList<CElement> selection, CElement defElement){
		System.out.println("Extract sqrt punkt ");
		// Analyse des bisherigen Produkts
		CTimesRow oldProduct = (CTimesRow) selection.get(0);
		CElement oldFirst = selection.get(0).getFirstChild();
		CElement newFirst = oldFirst.cloneCElement(false);
		CSqrt firstSqrt = CSqrt.createSqrt(newFirst);
		newFirst.setCRolle(CRolle.RADIKANT);
		boolean hasDiv = oldFirst.getNextSibling().hasExtDiv();
		Element newOp = (Element) oldFirst.getNextSibling().getExtPraefix().cloneNode(true);
		boolean timesRowAgain = (oldFirst.getNextSibling().hasNextC());
		CElement newSecond = null;
		if (timesRowAgain){
			ArrayList<CElement> factorList = ((CTimesRow) oldProduct.cloneCElement(false)).getMemberList();
			factorList.remove(0);
			newSecond = CTimesRow.createRow(factorList);
			((CTimesRow) newSecond).correctInternalPraefixesAndRolle();
			if (hasDiv){
				((CTimesRow) newSecond).toggleAllVZButFirst(false);
			}
		} else {
			newSecond = oldFirst.getNextSibling().cloneCElement(false);
		}
		CSqrt secSqrt = CSqrt.createSqrt(newSecond);
		newSecond.setCRolle(CRolle.RADIKANT);
		secSqrt.setExtPraefix(newOp);
		CTimesRow newChild = CTimesRow.createRow(CTimesRow.createList(firstSqrt, secSqrt));
		newChild.correctInternalPraefixesAndRolle();
		return newChild;
	}
	
	protected boolean canExtract(ArrayList<CElement> selection){
		// Man kann nur die ganz linken Elemente extrahieren
		if (selection.size()!=1 || !selection.get(0).getCType().equals(CType.TIMESROW)){
			System.out.println("Wir extrahieren nur aus einem Produkt");
			return false;
		} 
		return true;
	}
}
