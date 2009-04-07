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

package cViewer;

import org.w3c.dom.*;


public class JMathElementHandler {

	public static String testMathMLString(String s){
		return removeSpaces(s);
	}
	
	private static String removeSpaces(String s){
		return s.replace("> ", ">");
	}
	
	public static boolean isNr(Element e) {
		return e.getNodeName().equals("mn");
	}

	public static int getNr(Element e) {
		if (isNr(e)) {
			return Integer.parseInt(e.getTextContent());
		} else {
			return 0;
		}
	}

	public static boolean isOp(Element e) {
		return "mo".equals(e.getNodeName());
	}
	
	public static boolean isTimesOp(Element e){
		return isVisTimes(e)||isInvTimes(e);
	}
	
	public static boolean isStrichOp(Element e){
		return "-".equals(e.getTextContent())|| "+".equals(e.getTextContent());
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

	public static boolean isInvTimes(Element e){
		if (e.getFirstChild()==null || e.getFirstChild().getAttributes()==null ||e.getFirstChild().getAttributes().getNamedItem("name")==null){
			return false;
		} else {
			return "InvisibleTimes".equals(e.getFirstChild().getAttributes().getNamedItem("name").getNodeValue());
		}
	}
	
	public static boolean isVisTimes(Element e){
		return "·".equals(e.getTextContent());
	}
	
	
	public static String getOp(Element e) {
		if (isOp(e)) {
			if (e.getFirstChild()!=null && e.getFirstChild().getAttributes()!=null && e.getFirstChild().getAttributes().getNamedItem("name")!=null) {
				return e.getFirstChild().getAttributes().getNamedItem("name").getNodeValue();
			} else {
				return e.getTextContent();
			}		
		} else {
			return null;
		}
	}

    public static void removeCalcTyp(Node aNode){
    	if (aNode !=null && aNode instanceof Element){
			((Element) aNode).removeAttribute("calcTyp");
			((Element) aNode).removeAttribute("mathbackground");
			((Element) aNode).removeAttribute("mathcolor");
		}
		if (aNode.hasChildNodes()){
			removeCalcTyp(aNode.getFirstChild());
		}
		if (aNode.getNextSibling()!=null){
			removeCalcTyp(aNode.getNextSibling());
		}
    }
    
    // funktioniert nur für einige "saubere" Doms, d.h. pro Row entweder PlusRow oder TimesRow oder Equ oder MinTerm
	public static void parseDom(Node aNode){
		if (aNode !=null){
			// try to set Infos
			if (aNode instanceof Element) {
				Element el = (Element) aNode;
				if (isRow(el)){
					String result = getRowTyp(el);
					if (!"".equals(result)){
						el.setAttribute("calcTyp", result);
					} else {
						System.out.println("JMathElementHandler konnte RowTyp nicht ermitteln");
					}
				} else {					
					el.setAttribute("calcTyp", el.getNodeName());
				} 
			} else {
				// No Element
			}
		}
		if (aNode.hasChildNodes()){
			parseDom(aNode.getFirstChild());
		}
		if (aNode.getNextSibling()!=null){
			parseDom(aNode.getNextSibling());
		}
	}
	
	
	private static String getRowTyp(Element aRow){
		String result ="";
		
		if (aRow.getFirstChild()!=null && aRow.getFirstChild() instanceof Element) {
			Element e1 = (Element) aRow.getFirstChild();
			// geht es mit einem Operator los?
			if (isOp(e1)){
				// dieser darf nur Minus sein
				if ("-".equals(e1.getTextContent())){
					// ein minTerm darf nur noch einen Sibling haben
					if (e1.getNextSibling()!=null && e1.getNextSibling() instanceof Element && e1.getNextSibling().getNextSibling()==null){
						return "vzterm";
					// nach - und einem Element geht es noch weiter, Fehler
					} else if (e1.getNextSibling()!=null && e1.getNextSibling() instanceof Element) {
						System.out.println("Fehler nach vz Minus mehr als ein Ausdruck");
						return "unknown";
					} else {
						System.out.println("Fehler nach vz Minus kein Element");
						return "unknown";
					}
				// vz anders als Minus
				} else {
					System.out.println("Fehler nach vz das nicht minus ist");
					return "unknown";
				}
			// nun geht es nicht mit Operator los.
			} else {
				if (e1.getNextSibling()!=null && e1.getNextSibling() instanceof Element){
					Element e2 = (Element) e1.getNextSibling();
					if (isOp(e2)){
						// Infix-Operatoren können = * : + - sein
						result = getOp(e2);
					} else if (e2.getNextSibling()!=null && e2.getNextSibling() instanceof Element) {
						result = getOp((Element) e2.getNextSibling());
						System.out.println("RowTyp bei e3 gefunden. Wann ist das?");
					} 
					if ("·".equals(result) || "×".equals(result) || "InvisibleTimes".equals(result) || ":".equals(result)){
//						System.out.println("parsing dom getRowTyp *");
						result = "*";
					} else if ("+".equals(result) || "-".equals(result)) {
//					    System.out.println("parsing dom getRowTyp +");
						result = "+";
					} else {
//						System.out.println("parsing dom getRowTyp " + result);
					}
				} else { // nur ein Element, kein Operator
					result = e1.getNodeName();
					System.out.println("parsing dom - getRowTyp else");
				}
			}		
		} else { // Row ist leer
			System.out.println("JMathElementHandler.getRowTyp: leere Row");
			result = "empty";
		}		
		return result;
	}
	
	public static String getTyp(Element el){
		if (el.getAttribute("CalcType")!=null) {
			return el.getAttribute("calcTyp"); 
		} else {
			return "nix";
		}
	}
	public static boolean isIn(Node smaller, Node bigger){
		boolean result = false; 
		Node test = smaller;
		while (test.getParentNode()!=null){
			if (test.equals(bigger)){
				return true;
			} else {
				test = test.getParentNode();
			}
		}
		return result;
	}
}
