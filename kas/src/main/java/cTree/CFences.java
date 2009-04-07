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

public class CFences extends CElement{

	public CFences(Element element){
		this.element = element;
	}
	
	public CType getCType() {
		return CType.FENCES;
	}
	
	public static CFences createFenced(CElement inhalt){
		CFences fences = (CFences) CElementHelper.createAll(inhalt.getElement(), "mfenced", "mfenced", CRolle.UNKNOWN, null);
		fences.appendPraefixAndChild(inhalt);
		inhalt.setCRolle(CRolle.GEKLAMMERT);
		return fences;
	}
	
	public CElement getInnen(){
		return getFirstChild();
	}
	
	public boolean hatGleichenBetrag(CElement cE2){
		if (cE2 instanceof CFences){
			return getInnen().hatGleichenBetrag(((CFences) cE2).getInnen());
		} else if (cE2 instanceof CMinTerm){
			return hatGleichenBetrag(((CMinTerm) cE2).getValue());
		}
		return false;
	}
	
	public void normalize(){
		if (hasChildC()){
			// falls das Child eine +Row ist, das als einziges
			// eine Malrow enthält kann die Plusrow verschwinden
			CElement firstRow = getFirstChild();
			if ((firstRow instanceof CPlusRow) && (firstRow.getExtPraefix()==null) && firstRow.hasChildC()){
				CElement secondRow = firstRow.getFirstChild();
				if ((secondRow instanceof CTimesRow) && !secondRow.hasNextC()){
					this.replaceChild(secondRow, firstRow, true, false);
				}
			}
			firstRow.normalizeTreeAndSiblings();
		}
	};
}
