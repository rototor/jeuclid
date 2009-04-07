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

public class CC_PotFencesNum extends CC_ {

	// parent basis und exponent können zusammengeholt sein, parent ist nur producer
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		if (!canCombine(((CFences) cE1).getInnen(), cE2)) {return cE1;}
		System.out.println("PotFences - Can combine");
		// parent ist die Potenz, cE1 die Basis /Klammer cE2 der Exponent
		  cE1.removeCActiveProperty();
		  CElement newArg = createCombination(parent.getParent(), ((CFences) cE1).getInnen(), cE2);
		  CFences newChild = CFences.createFenced(newArg);
		  newArg.setCRolle(CRolle.GEKLAMMERT);
		  parent.getParent().replaceChild(newChild, parent, true, true);
		  // CombineHandler.getInstance().insertOrReplace(parent, newChild, cE1, cE2, replace);
		  newChild.setCActiveProperty();
		  return newChild;
	}
	
	protected boolean canCombine(CElement basis, CElement expo){
		System.out.println("Repell pot fences num?"); 
		if (((CNum) expo).getValue()!=2){
			System.out.println("Ich kann nur quadrieren");
			return false;
		}
		if (!(basis instanceof CPlusRow)){
			System.out.println("Ich kann nur Summen " + basis.getCType());
			return false;
		}
		if (((CPlusRow) basis).getCount()!=2){
			System.out.println("Ich kann nur Summen mit zwei Summanden " + ((CPlusRow) basis).getCount());
			return false;
		}
		return true;
	}
	
	protected CElement createCombination(CElement potenz, CElement basis, CElement expo){
		System.out.println("Binomische Formel");
		CElement old1 = basis.getFirstChild();
		old1.show();
		CElement old2 = old1.getNextSibling();
		old2.show();
		String rz = old2.getPraefixAsString();
		
		ArrayList<CElement> list = new ArrayList<CElement>();
		CElement summand1 = CPot.createPot(old1.cloneCElement(false), 2);
		list.add(summand1);
		
		ArrayList<CElement> list2 = new ArrayList<CElement>();
		list2.add(CNum.createNum(potenz.getElement(), "2"));
		list2.add(old1.cloneCElement(false));
		list2.add(old2.cloneCElement(false));
		CTimesRow summand2 = CTimesRow.createRow(list2);
		summand2.correctInternalPraefixesAndRolle();
		System.out.println("Binomische Formel Summand 2:");
		summand2.show();
		summand2.setPraefix(rz);
		list.add(summand2);
		
		CElement summand3 = CPot.createPot(old2.cloneCElement(false), 2);
		summand3.setPraefix("+");
		list.add(summand3);
		
		CPlusRow newChild = CPlusRow.createRow(list);
		newChild.correctInternalPraefixesAndRolle();
		newChild.setCRolleAndPraefixFrom(potenz);
		return newChild;
	}
	
}
