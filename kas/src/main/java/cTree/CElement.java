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

import java.util.*;
import org.w3c.dom.*;

import cTree.adapter.*; 
import cTree.cCombine.CombineHandler;
import cTree.cDefence.DefenceHandler;
import cTree.cSplit.SplitHandler;

public abstract class CElement extends RolleAdapter{

	public CType getCType() {
		return CType.UNKNOWN;
	}
	
	// --- boolesche Tester default Verhalten ----------------------------
	public boolean hatGleichenBetrag(CElement cE2){
		return false;
	}
		
	public boolean is0(){
    	return false;
    }
    public boolean istGleichartigesMonom(CElement e2){
    	return false;
    }
    
    // --- Support für das Verbinden, Extrahieren und Aufspalten in dem CTree
    public CElement combineRight(CElement active){
    	CElement erstesElement = active; 
    	if (erstesElement.hasNextC()){
    		CElement zweitesElement = erstesElement.getNextSibling();
    		return CombineHandler.getInstance().combine(this, erstesElement, zweitesElement);
    	} else {
    		return active;
    	}
    }

    public CElement extract(ArrayList<CElement> active){
    	return cTree.cExtract.ExtractHandler.getInstance().extract(this, active, active.get(0).getFirstChild());
    }; 
    
	public CElement split(CElement zuZerlegen, String s){
		return SplitHandler.getInstance().split(this, zuZerlegen, s);
	}
    
	// --- Support für die Klammern in dem CTree
	// --- wird von der CPlusRow und der CTimesRow überschrieben
    public CElement fence(ArrayList<CElement> active){
    	if (active.size()==1) {   		
    		return standardFencing(active.get(0));
    	} else 
    		return active.get(0);
    }
    
    public CElement standardFencing(CElement active){
    	System.out.println("CElement fencing");
    	active.removeCActiveProperty();
    	CElement activeNeu = simpleFenced(active);
    	activeNeu.setCRolle(active.getCRolle());
		activeNeu.setExtPraefix(active.getExtPraefix());	
    	active.setCRolle(CRolle.GEKLAMMERT);
    	active.setExtPraefix(null);
    	activeNeu.setCActiveProperty();
    	return activeNeu;
    }
    
    public CElement simpleFenced(CElement any){
    	Element fences = getElement().getOwnerDocument().createElement("mfenced");
    	getElement().insertBefore(fences, any.getElement());
    	fences.appendChild(any.getElement());
    	CFences cFences = new CFences(fences);
    	DOMElementMap.getInstance().getCElement.put(fences, cFences);
		return cFences;
    }
    
    public final CElement defence(CElement aFencePair){
    	if (aFencePair instanceof CFences && aFencePair.hasChildC()) {
    		return DefenceHandler.getInstance().defence(this, aFencePair, aFencePair.getFirstChild());
    	}
    	return aFencePair;
    }
    	
	// --- Ausgaben ---------------------------------------
	public void show(){
		showElement(element); 
		if (praefix!=null) {
			System.out.print("Vorzeichen: ");
			showPraefix(praefix); 
		} else {
			System.out.println("kein extVZ");
		}
		System.out.println("cType: " + getCType());
		System.out.println("cRolle: " + cRolle);
		if (hasChildC()){getFirstChild().showWithSiblings();}
	}
	
	private void showWithSiblings(){
		showElement(element); 
		if (praefix!=null) {
			System.out.print("Vorzeichen: ");
			showPraefix(praefix); 
		} else {
			System.out.println("kein intVZ");
		}
		System.out.println("cType: " + getCType());
		System.out.println("cRolle: " + cRolle);
		if (hasChildC()){getFirstChild().showWithSiblings();}
		if (hasNextC()){getNextSibling().showWithSiblings();}
	}
}
