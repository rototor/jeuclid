package cTree.cAlter;

import java.util.HashMap;

import cTree.*;

public class CA_Minrow extends CAlter {
	
	public CElement change(CElement old){
		System.out.println("Changer Minrow to TR (-1)");
		old.removeCActiveProperty();
		CElement newOne = CNum.createNum(old.getElement(), "1");
		CElement newFirst = CFences.createFenced(CMinTerm.createMinTerm(newOne));
		CElement newSecond = ((CMinTerm) old).getValue().cloneCElement(false);
		CTimesRow newChild = CTimesRow.createRow(CTimesRow.createList(newFirst, newSecond));
		newChild.correctInternalPraefixesAndRolle();
		old.getParent().replaceChild(newChild, old, true, true);
		newChild.setCActiveProperty();
		return newChild;
	}
	
	public String getText(){
		return "-a in Produkt mit (-1)*a";
	}
	public boolean check(CElement el){
		return el.getCType().equals(CType.MINROW);
	}
	public void register(HashMap<String, CAlter> hashMap){
		hashMap.put(getText(), this);
	}
}
