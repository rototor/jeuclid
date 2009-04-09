package cTree.cAlter;

import java.util.HashMap;
import cTree.*;

public abstract class CAlter {
	
	public abstract CElement change(CElement old); 
	public abstract String getText();
	public abstract boolean check(CElement el);
	public abstract void register(HashMap<String, CAlter> hashMap);
}
