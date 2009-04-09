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

import cTree.adapter.EElementHelper;

import java.util.*;

public class CPot extends CElement {

	public CPot(Element element){
		this.element = element;
	}
	
	public CType getCType() {
		return CType.POT;
	}
	
	public boolean isHoch0(){
		if (getExponent().getCType()!=CType.NUM) return false; 
		return ((CNum) getExponent()).is0();
	}
	
	public boolean isHoch1(){
		if (getExponent().getCType()!=CType.NUM) return false; 
		return ((CNum) getExponent()).is1();
	}
	
	public static CPot createPot(CElement basis, CElement expo){
		CPot pot = (CPot) CElementHelper.createAll(basis.getElement(), "msup", "msup", CRolle.UNKNOWN, null);
		pot.appendChild(basis);
		pot.appendPraefixAndChild(expo);
		basis.setCRolle(CRolle.BASIS);
		expo.setCRolle(CRolle.EXPONENT);
		return pot;
	}
	
	// nur für positive Exponenten geeignet
	public static CPot createPot(CElement basis, int expo){
		CElement cExp = CNum.createNum(basis.getElement(), ""+expo);
		return createPot(basis, cExp);
	}
	
	// nur für positive Exponenten geeignet
	public static CPot createPot(Element producer, String ident, int expo){
		CElement cBas = CIdent.createIdent(producer, ident);
		return createPot(cBas, expo);
	}
	
	public boolean hatGleichenBetrag(CElement cE2){
		if (cE2 instanceof CPot){
			return getBasis().hatGleichenBetrag(((CPot) cE2).getBasis()) 
			 	&& getExponent().hatGleichenBetrag(((CPot) cE2).getExponent());
		} else if (cE2 instanceof CMinTerm){
			return hatGleichenBetrag(((CMinTerm) cE2).getValue());
		}
		return false;
	}
	
	public static CPot createSquare(CElement cElement){
		CPot newSquare = null;
		if (cElement instanceof CPot){
			// erzeugt Basis
			CElement newBase = ((CPot) cElement).getBasis().cloneCElement(true);
			// erzeugt Exponent, zuerst TR zuerst erster Faktor 
			CElement first = CElementHelper.createAll(cElement.getElement(), "mn", "mn", CRolle.FAKTOR1, null);
			first.setText("2");
			CElement second = ((CPot) cElement).getExponent().cloneCElement(true);
			Element op = EElementHelper.createOp(cElement.getElement(), "*");
			second.setExtPraefix(op);
			// erzeugt die TR
			ArrayList<CElement> faktoren = new ArrayList<CElement>(); 
			faktoren.add(first); 
			faktoren.add(second);
			// erzeugt Exponent
			CElement newExp = CTimesRow.createRow(faktoren);
			// erzeugt die Potenz
			newSquare = (CPot) CElementHelper.createAll(cElement.getElement(), "msup", "msup", cElement.getCRolle(), cElement.getExtPraefix());
			newSquare.appendChild(newBase);
			newSquare.appendChild(newExp);	
			newBase.setCRolle(CRolle.BASIS);
			newExp.setCRolle(CRolle.EXPONENT);
		} else {
			newSquare = (CPot) CElementHelper.createAll(cElement.getElement(), "msup", "msup", cElement.getCRolle(), cElement.getExtPraefix());
			CElement newBase = cElement.cloneCElement(false);
			newSquare.appendChild(newBase);
			CElement newExp = CElementHelper.createAll(cElement.getElement(), "mn", "mn", CRolle.EXPONENT, null);
			newExp.setText("2");
			newSquare.appendChild(newExp);
			newBase.setCRolle(CRolle.BASIS);
			newExp.setCRolle(CRolle.EXPONENT);
		}
		return newSquare; 
	}
	
	public void normalize(){};
	
	public boolean istGleichartigesMonom(CElement el){
		boolean result = false;
		if (el.getCType()==CType.IDENT){
			result = (getElement().getFirstChild().getTextContent().equals(el.getElement().getTextContent()));
		} else if (el.getCType()==CType.POT){
			result = (getElement().getFirstChild().getTextContent().equals(el.getElement().getFirstChild().getTextContent()));
		} else if (el.getCType() ==CType.MINROW) {
			return this.istGleichartigesMonom(((CMinTerm) el).getValue());
		}
		System.out.println("Gleichartig: " + result);
		return result; 
	}
	
	public CElement getBasis(){
		return getFirstChild();
	}
	public void setBasis(String s){
		element.getFirstChild().setTextContent(s);
	}
	
	public CElement getExponent(){
		return getFirstChild().getNextSibling();
	}
	
	public int getExponentValue(){
		String exp = getExponent().getElement().getTextContent();
		try{
			return Integer.parseInt(exp);
		} catch (NumberFormatException e){
			System.out.println("Kein guter Exponent");
		}
		return -1;
	}
	
	public void setExponent(String e){
		element.getFirstChild().getNextSibling().setTextContent(e);
	}
	
	public String getVar(){
		return getBasis().getElement().getTextContent();
	}
	
	public String getSignatur(){
		String nr = getBasis().getElement().getTextContent();
		String exp = getExponent().getElement().getTextContent();
		return nr+exp;
	}
	
}
