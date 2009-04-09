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

package cTree.cDefence;

import cTree.*;
import org.w3c.dom.*;
import java.util.*;

public class CD_1StrichStrich extends CD_1{
	
	public CElement defence(CElement parent, CElement fences, CElement content){
		System.out.println("Do the defence work strich strich");
		fences.removeCActiveProperty();
		boolean aussenMinus = (fences.hasExtMinus());
		Element op = (fences.getExtPraefix()!=null) ? (Element) fences.getExtPraefix().cloneNode(true): null;
		
		// Wir bilden drei Rows, bis zur Klammer, das Innere und nach der Klammer
		ArrayList<CElement> rows = new ArrayList<CElement>();
		rows.addAll(((CPlusRow) parent).startTo(fences));							// clone der ersten Summanden
		rows.addAll(((CPlusRow) createInsertion(fences, content, aussenMinus, op)).getMemberList());						
		rows.addAll(((CPlusRow) parent).endFrom(fences));							// clone der letzten Summanden
		System.out.println("--- Anzahl der Member " + rows.size());
		for (CElement el : rows){
			System.out.println("--- " + el.getCType());
		}
		// Die drei Rows werden zu einer verschmolzen
		CPlusRow newParent = CPlusRow.createRow(rows);
		newParent.correctInternalPraefixesAndRolle();
		
		// Das Parent wird eingefügt
		parent.getParent().replaceChild(newParent, parent, true, true);
		newParent.getFirstChild().setCActiveProperty();
		return newParent.getFirstChild();
	}
	
	protected CElement createInsertion(CElement fences, CElement content, boolean aussenMinus, Element op){
		System.out.println("Creating insertion strich strich");
		CElement newChild = content.cloneCElement(true);
		if (aussenMinus){
			System.out.println("min vor Row!!!");
			((CPlusRow) newChild).toggleAllVZButFirst(false);
		}
		if (op!=null){
			newChild.getFirstChild().setExtPraefix(op);
		}
		return newChild;
	}
	
}
