package cTree.cAlter;

import java.util.HashMap;

import cTree.*;

public class CA_DivA_Pot extends CAlter {
	
	public CElement change(CElement old){
		System.out.println("Changer DivA to Exp");
		old.removeCActiveProperty();
		CElement newNum = CNum.createNum(old.getElement(), "1");
		CElement newExp = CMinTerm.createMinTerm(newNum);
		CElement newBase = old.cloneCElement(false);
		CPot newPot = CPot.createPot(newBase, newExp);
		old.toggleTimesDiv(false);
		old.getParent().replaceChild(newPot, old, true, true);
		newPot.setCActiveProperty();
		return newPot;
	}
	
	public String getText(){
		return ":a in *a^-1 umwandeln";
	}
	public boolean check(CElement el){
		return el.hasExtDiv();
	}
	public void register(HashMap<String, CAlter> hashMap){
		hashMap.put(getText(), this);
	}
}
