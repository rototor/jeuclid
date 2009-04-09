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

package cTree.adapter;

import org.w3c.dom.Element;

import cTree.CElement;
import cTree.CElementHelper;
import cTree.CRolle;


public abstract class CTreeMutator extends CTreeWalker {

    //Support für die DOM Manipulation 
    public CElement appendChild(CElement newChild){
    	// CElementHelper.removeSupport_Rollenanpassung(newChild);
    	if (newChild.getExtPraefix()!=null){
    		getElement().appendChild(newChild.getExtPraefix());
    	}
    	Element e = (Element) getElement().appendChild(newChild.getElement());
    	return DOMElementMap.getInstance().getCElement.get(e);
    }
    
    public CElement appendPraefixAndChild(CElement newChild){
    	// CElementHelper.removeSupport_Rollenanpassung(newChild);
    	if (newChild.getExtPraefix()!=null){
    		getElement().appendChild(newChild.getExtPraefix());
    	}
    	Element e = (Element) getElement().appendChild(newChild.getElement());
    	return DOMElementMap.getInstance().getCElement.get(e);
    }
    
    public CElement insertBefore(CElement newChild, CElement refChild){
    	System.out.println("Insert in Element");
    	Element refElement = refChild.getElement();
    	if (refChild.getExtPraefix()!=null){
    		refElement = refChild.getExtPraefix();
    	}
    	removeSupport_RollenanpassungNext(newChild);
    	if (newChild.getExtPraefix()!=null){
    		System.out.println("CElement InsertBefore ExtPraefix" + newChild.getExtPraefix().getTextContent());
//    		Element f = (Element) getElement().insertBefore(newChild.getExtPraefix(), refElement);
    		getElement().insertBefore(newChild.getExtPraefix(), refElement);
    	}
    	Element e = (Element) getElement().insertBefore(newChild.getElement(), refElement);
    	return DOMElementMap.getInstance().getCElement.get(e);
    }
    
	public static void removeSupport_RollenanpassungNext(CElement toRemove){
		if (toRemove!=null && toRemove.getCRolle()==CRolle.FAKTOR1 && toRemove.hasNextC()){
			toRemove.getNextSibling().setCRolle(CRolle.FAKTOR1);
		}
		if (toRemove!=null && toRemove.getCRolle()==CRolle.SUMMAND1 && toRemove.hasNextC()){
			toRemove.getNextSibling().setCRolle(CRolle.SUMMAND1);
		}
	}
	
	// --- Mutatoren ---------------------------------------
	
	public CElement cloneCElement(boolean withPraefix){
		Element e = (Element) getElement().cloneNode(true);
//		System.out.println("Clone has no parent? " + (e.getParentNode()==null));
		CElement cE2 = CElementHelper.buildCFromENoPraefixSet(e, ((CElement) this).getCRolle(), false);
		if (getExtPraefix()!=null && withPraefix) {
			Element p = (Element) getExtPraefix().cloneNode(true);
			cE2.setExtPraefix(p);
		}
		return cE2;
	}
	
	public CElement removeChild(CElement e, boolean correctNextRolle, boolean unregister, boolean withNormalParent){
		// keine allgemeine Korrektur
		// evtl. Praefix aus DOM entfernen
		if (e.getExtPraefix()!=null) {
			getElement().removeChild(e.getExtPraefix());}
		// Element entfernen
		getElement().removeChild(e.getElement());
		// evtl. Element -> CElement entfernen
		if (unregister){
			DOMElementMap.getInstance().getCElement.remove(e);
		}
		if (withNormalParent && (getParent()!=null)){
			getParent().normalize();
		}
		return e;
	}
	
	public CElement replaceChild(CElement newChild, CElement oldChild, boolean setOldRolle, boolean setOldPraefix){
		System.out.println("Replace in Element");
		CRolle saveOldRolle = oldChild.getCRolle();
		System.out.println("Saved old Rolle" + saveOldRolle); 
		Element saveOldPraefix = oldChild.getExtPraefix();
		if (oldChild.isActiveC()){
			oldChild.removeCActiveProperty();
			newChild.setCActiveProperty();
		}
		insertBefore(newChild, oldChild);
		removeChild(oldChild, false, true, false);
		if (setOldRolle) {
			newChild.setCRolle(saveOldRolle);
		}
		if (setOldPraefix) {
			newChild.setExtPraefix(saveOldPraefix);
			if (oldChild.getExtPraefix()!=null){
				getElement().insertBefore(oldChild.getExtPraefix(), newChild.getElement());
			}
		}
		return oldChild;
	}
	
    public abstract void normalize();
	
	public void normalizeAll(){
		//CElement root sollte math sein
		CElement root = DOMElementMap.getInstance().getCElement.get(((Element) this.getElement().getOwnerDocument().getFirstChild()));
		if (root.hasChildC()){
			root.getFirstChild().normalizeTreeAndSiblings();
		}
	};
	
	public void normalizeTreeAndSiblings(){
		if (hasChildC()) {
			getFirstChild().normalizeTreeAndSiblings();
		}
		CElement reserve = getNextSibling();
		System.out.println("Normalize " + this.getText() + " " + this.getElement().getNodeName());
		normalize(); 
		if (reserve!=null) {
			reserve.normalizeTreeAndSiblings();
		}
	}
	
	// Support für die Verschiebung in dem CTree
    public CElement moveRight(CElement active){return active;}
    public CElement moveLeft(CElement active){return active;}
	
}
