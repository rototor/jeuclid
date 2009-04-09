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
import cTree.CNavHelper;

public abstract class CTreeWalker extends ActivityAdapter{
	
	// boolesche Tester für Navigation ---------------------------------------
	public boolean hasParent(){
		return element!=null && element.getParentNode()!=null && 
		   DOMElementMap.getInstance().getCElement.get(element.getParentNode()) !=null;
	}		
	
	public boolean hasChildC(){
		if (element!=null && element.getFirstChild()!=null && (element.getFirstChild() instanceof Element)) {
			Element firstTest = (Element) element.getFirstChild();
			if ("mo".equals(firstTest.getNodeName())){
				return DOMElementMap.getInstance().getCElement.get(firstTest.getNextSibling())!=null;
			} else {
				return DOMElementMap.getInstance().getCElement.get(firstTest)!=null;
			}
		} else {
			return false;
		}
	}	
	public boolean hasNextC(){
		if (hasParent()) {
			return CNavHelper.hasNextC(CNavHelper.getNavType(getParent()), element);
		} else {
			return false;
		}
	}	
	public boolean hasPrevC(){
		if (hasParent()) {
			return CNavHelper.hasPrevC(CNavHelper.getNavType(getParent()), element);
		} else {
			return false;
		}
	}	
	
	// Getter sollten nur nach hasParent hasPrevC ... aufgerufen werden.
	public CElement getParent() {
		return DOMElementMap.getInstance().getCElement.get(element.getParentNode());
	}
	public CElement getNextSibling() {
		if(hasNextC()){
			return CNavHelper.getNextC(CNavHelper.getNavType(getParent()), element);
		} else {
			return null;
		}
	}
	public CElement getPrevSibling() {
		return CNavHelper.getPrevC(CNavHelper.getNavType(getParent()), element);
	}
	public CElement getFirstChild() {
		if (element.getFirstChild()==null || !(element.getFirstChild() instanceof Element) ){
			return null;
		} else {
			Element firstTest = (Element) element.getFirstChild();
			if ("mo".equals(firstTest.getNodeName())){
				return DOMElementMap.getInstance().getCElement.get(firstTest.getNextSibling());
			} else {
				return DOMElementMap.getInstance().getCElement.get(firstTest);
			}
		}	
	}
	
	// Support für die Auswahl im CTree ---------------------------------------
	public CElement tryToSelectParent(){
    	if (hasParent()) {
			CElement bEl = getParent();
    		removeCActiveProperty();
			bEl.setCActiveProperty();
			return bEl;
		} else {
			System.out.println("kein activer Node oder Parent oder Element");
		}
    	return (CElement) this;
    }
    public CElement tryToSelectFirstChild(){
    	if (hasChildC() ) {
    		CElement bEl = getFirstChild();
    		removeCActiveProperty();
			bEl.setCActiveProperty();
			return bEl;
		} else {
			System.out.println("kein active Node oder Child oder Element");
		}
    	return (CElement)this;
    }
    
    public CElement tryToSelectLeft(){
    	if (hasPrevC()) {
    		CElement bEl = getPrevSibling();
    		removeCActiveProperty();
			bEl.setCActiveProperty();
			return bEl;
		} else {
			System.out.println("kein activer Node, left Node or Element");
		}	
    	return (CElement)this;
    }
    
    public CElement tryToSelectRight(){
    	if (hasNextC()) {
    		CElement bEl = getNextSibling();
    		removeCActiveProperty();
			bEl.setCActiveProperty();
			return bEl;
		} else {
			System.out.println("kein activer Node, left Node or Element");
		}	
    	return (CElement) this;
    }
}
