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
import org.w3c.dom.NamedNodeMap;

public abstract class ElementAdapter {

	protected Element element; 
	
	public Element getElement() {
		return element;
	}
	
	public String getText(){
		return getElement().getTextContent();
	}
	
	public void setText(String s){
		this.getElement().setTextContent(s);
	}
	
	public boolean hasParent(){
		return (element.getParentNode()!=null);
	}
	
	public static void showElement(Element e){
		System.out.println("*Element "+ e.getNodeName()+ " " + e.getTextContent());
		if (e.hasAttributes()){
			String result = "Attributes: "; 
			NamedNodeMap attr = e.getAttributes();
			for (int i=0;i<attr.getLength();i++){
				result = result+ " " +i + " " + attr.item(i).getNodeName()+ " " +attr.item(i).getNodeValue();
			}
			System.out.println(result);
		}
	}
}
