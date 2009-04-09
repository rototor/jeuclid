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
import org.w3c.dom.Node;

import cTree.CElement;

public class EElementHelper {

	//Getter
	public static String getTyp(Element e){
		if (e.getAttribute("calcType")!=null) {
			return e.getAttribute("calcTyp"); 
		} else {
			return "nix";
		}
	}
	public static int getNr(Element e) {
		if (isNr(e)) {
			return Integer.parseInt(e.getTextContent());
		} else {
			return 0;
		}
	}
	public static String getOp(Element e) {
		if (isOp(e)) {
			if (e.getFirstChild()!=null && e.getFirstChild().getAttributes()!=null && e.getFirstChild().getAttributes().getNamedItem("name")!=null) {
				return e.getFirstChild().getAttributes().getNamedItem("name").getNodeValue();
			} else {
				return e.getTextContent();
			}		
		} else {
			return "";
		}
	}
	
	// Erkennen der Operatoren
	public static boolean isInvTimes(Element e){
		if (e.getFirstChild()==null || e.getFirstChild().getAttributes()==null ||e.getFirstChild().getAttributes().getNamedItem("name")==null){
			return false;
		} else {
			return "InvisibleTimes".equals(e.getFirstChild().getAttributes().getNamedItem("name").getNodeValue());
		}
	}	
	public static boolean isVisTimes(Element e){
		return e!=null && "·".equals(e.getTextContent());
	}
	public static boolean isTimesOp(Element e){
		return isVisTimes(e)||isInvTimes(e);
	}
	public static boolean isDivOp(Element e){
		return (e!=null && ":".equals(e.getTextContent()));
	}
	
	public static boolean isOp(Element e) {
		return (e!=null && "mo".equals(e.getNodeName()));
	}
	
	public static boolean isNr(Element e) {
		return e!=null && e.getNodeName().equals("mn");
	}

	public static boolean isStrichOp(Element e){
		return e!=null && ("-".equals(e.getTextContent())|| "+".equals(e.getTextContent()));
	}
	
	public static boolean isId(Element e) {
		return "mi".equals(e.getNodeName());
	}

	public static boolean isSup(Element e) {
		return "msup".equals(e.getNodeName());
	}

	public static boolean isRow(Element e) {
		return "mrow".equals(e.getNodeName());
	}
	
	public static boolean isFencedPair(Element e){
		return "mfenced".equals(e.getNodeName());
	}
	
	public static boolean isFrac(Element e){
		return "mfrac".equals(e.getNodeName());
	}
	
	// erlaubte Typen +*-: null
	public static Element createOp(Element producer, String typ){
		Element op = null;
		if (!"".equals(typ)){
			op = producer.getOwnerDocument().createElement("mo");
			op.setTextContent(typ);
			if ("*".equals(typ)){ 
				op.setTextContent("·");}
			op.setAttribute("calcTyp", "mo"); 
		}
		return op;
	}
	
    public static void setDots(Node d){
    	if (d!=null){
    		if (d instanceof Element && isInvTimes((Element) d) && d.getParentNode()!=null) {
    			Element n = (Element) d.getNextSibling();
    			Element p = (Element) d.getPreviousSibling();
    			Element par = (Element) d.getParentNode();
    			if (n!=null && p!=null &&(isNr(n)||isRow(n)||isFencedPair(n)||isFencedPair(p)|| isFrac(n)|| isFrac(p))&& d.getParentNode()!=null){
    				CElement cEl = DOMElementMap.getInstance().getCElement.get(n);	
    				Node newNode = d.getOwnerDocument().createElement("mo");
    				newNode.setTextContent("·");
    				par.insertBefore(newNode, d);
    				if (cEl!=null && cEl.getExtPraefix().equals(d)){
    					cEl.setExtPraefix((Element) newNode);
    				}	
    				par.removeChild(d);
    				d=newNode;
    			}
    		}
    		if (d instanceof Element && isVisTimes((Element) d)) {
    			Element n = (Element) d.getNextSibling();
    			Element p = (Element) d.getPreviousSibling();
    			Element par = (Element) d.getParentNode();
    			if (n!=null && !(isNr(n)||isRow(n)||isFencedPair(n)||isFencedPair(p)|| isFrac(n)|| isFrac(p)) && d.getParentNode()!=null){
    				CElement cEl = DOMElementMap.getInstance().getCElement.get(n);	
    				Element newNode = d.getOwnerDocument().createElement("mo");
    				Element new2 = d.getOwnerDocument().createElement("mchar");
    				new2.setAttribute("name", "InvisibleTimes");
    				newNode.appendChild(new2);
    				par.insertBefore(newNode , d);
    				if (cEl!=null && cEl.getExtPraefix().equals(d)){
    					cEl.setExtPraefix((Element) newNode);
    				}	
    				par.removeChild(d);
    				d=newNode;
    			}	
    		}
	    	if (d.hasChildNodes()){
	    		setDots(d.getFirstChild());
	    	}
	    	setDots(d.getNextSibling());
    	}
    } 
}
