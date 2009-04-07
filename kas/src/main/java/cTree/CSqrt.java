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

import org.w3c.dom.Element;
import java.util.*;

public class CSqrt extends CElement {

	public CSqrt(Element element){
		this.element = element;
	}
	
	public CType getCType() {
		return CType.SQRT;
	}
	
	public boolean hatGleichenBetrag(CElement cE2){
		if (cE2 instanceof CSqrt){
			return getRadikand().hatGleichenBetrag(((CSqrt) cE2).getRadikand());
		}
		return false;
	}
	
    public boolean istGleichartigesMonom(CElement e2){
    	if (e2 instanceof CSqrt){
    		return ((CSqrt) e2).getRadikand().istGleichartigesMonom(this.getRadikand());
		} else if (e2 instanceof CMinTerm){
			return hatGleichenBetrag(((CMinTerm) e2).getValue());
    	} 
    	return false;
    }
	
	public void normalize(){};
	
	public CElement getRadikand(){
		return getFirstChild();
	}
	
	public static ArrayList<CSqrt> createRootList(CElement cE1, CElement cE2){
		ArrayList<CSqrt> rootList = new ArrayList<CSqrt>(); 
		rootList.add((CSqrt) cE1); 
		rootList.add((CSqrt) cE2);
		return rootList;
	}
	
	public static CSqrt createSqrt(CElement cElement){
		CSqrt newSqrt = (CSqrt) CElementHelper.createAll(cElement.getElement(), "msqrt", "msqrt", cElement.getCRolle(), cElement.getExtPraefix());
		newSqrt.appendChild(cElement.cloneCElement(false));
		newSqrt.getRadikand().setCRolle(CRolle.RADIKANT);
		return newSqrt; 
	}
	
	// jedes Output-listenelement erhält das Vorzeichen */: vor seiner Wurzel, wie man 
	// es für das Multiplizieren braucht nur das erste nicht. 
	public static ArrayList<CElement> getRadikandList(ArrayList<CSqrt> list){
		ArrayList<CElement> result = new ArrayList<CElement>();
		for (CSqrt root : list){
			result.add(root.getRadikand());
		}
		boolean first = true;
		for (int i=0; i<result.size(); i++){
			if (first){
				first = false;
			} else {
				if (list.get(i).getExtPraefix()!=null){
					result.get(i).setExtPraefix((Element) list.get(i).getExtPraefix().cloneNode(true));
				}
			}
		}
		return result;
	}
}
