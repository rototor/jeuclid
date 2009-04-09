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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import cTree.*;

public class JMathComponentHelper {
	
	// Die CalcTypes werden noch nicht entfernt
    public static String cleanString(String s){
    	String toRemove1 = " mathcolor=\"#FF0000\"";
    	String toRemove2 = " mathcolor=\"#007777\"";
    	String toRemove3 = " mathbackground=\"#B0C4DE\"";
    	String toReplace = "";
    	String t = s.replaceAll(toRemove1, toReplace);
    	t = t.replaceAll(toRemove2, toReplace);
    	return t.replaceAll(toRemove3, toReplace);
    }
    
    public static void getDocInfo(Node d){
    	if (d!=null){
    		if (d instanceof Element)  {
    			// System.out.println(el.getBaseURI()); null
    			// Tagname : mi, mo, mrow, mfrac, msqrt ... ebenso localName ebenso NodeName
    			System.out.println("*Element "+ d.getNodeName()+  " " + d.getAttributes().getNamedItem("name") ); // + d.getClass() + d.getNodeValue()
    			
    			if (d.hasAttributes()){
    				String result = "Attributes: "; 
    				org.w3c.dom.NamedNodeMap attr = d.getAttributes();
    				for (int i=0;i<attr.getLength();i++){
    					System.out.println(result+ " " +i + " " + attr.item(i).getNodeName()+ " " +attr.item(i).getNodeValue());
    				}
    				System.out.println(result);
    			}
    		} else {
	    		System.out.println("*keinElement "+ d.getNodeType()+ d.getNodeValue());
	    	}
	    	if (d.hasChildNodes()){
	    		System.out.println("*runter");
	    		getDocInfo(d.getFirstChild());
	    		System.out.println("*rauf");
	    	}
	    	getDocInfo(d.getNextSibling());
    	}
    }
    
    
    
    public static void getDocInfo(CElement d, boolean withSiblings){
    	if (d!=null){
    		if (d.getExtPraefix()!=null){
    			System.out.println("C*Element "+ d.getClass().getSimpleName()+" "+d.getCType() +  " " + d.getElement().getNodeName() + " " + d.getCRolle() + " " + d.getExtPraefix().getTextContent());
    		} else {
    			System.out.println("C*Element "+ d.getClass().getSimpleName()+" "+d.getCType() +  " " + d.getElement().getNodeName() + " " + d.getCRolle() + " extVZ null");
    		}
	    	if (d.hasChildC()){
	    		System.out.println("*runter");
	    		getDocInfo(d.getFirstChild(), true);
	    		System.out.println("*rauf");
	    	}
	    	if (d.hasNextC() && withSiblings){
	    		getDocInfo(d.getNextSibling(), true);
	    	}
    	}
    }
}
