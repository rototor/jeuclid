package cTree.cAlter;

import java.util.HashMap;

import cTree.*;

public class CA_DivA_Frac extends CAlter {
	
	public CElement change(CElement old){
		System.out.println("Changer DivA to TimesFracA");
		old.removeCActiveProperty();
		CElement newNum = CNum.createNum(old.getElement(), "1");
		CElement newDen = old.cloneCElement(false);
		CFrac newFrac = CFrac.createFraction(newNum, newDen);
		newFrac.correctInternalRolles();
		old.toggleTimesDiv(false);
		old.getParent().replaceChild(newFrac, old, true, true);
		newFrac.setCActiveProperty();
		return newFrac;
	}
	
	public String getText(){
		return ":a in *1/a umwandeln";
	}
	public boolean check(CElement el){
		return el.hasExtDiv();
	}
	
	public void register(HashMap<String, CAlter> hashMap){
		hashMap.put(getText(), this);
	}
}
