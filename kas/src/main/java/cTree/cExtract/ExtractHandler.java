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

package cTree.cExtract;

import java.util.*;

import cTree.*;
import cTree.adapter.DOMElementMap;

public class ExtractHandler {
	private volatile static ExtractHandler uniqueInstance; 
	public HashMap<CType, CExtracterTyp> getTypExtracter;
	
	private ExtractHandler(){
		getTypExtracter = new HashMap<CType, CExtracterTyp>();
		CExtracterTyp default1 = new CExtracterTyp();
		for (CType cType : CType.values()){
			getTypExtracter.put(cType, default1);
		}
		getTypExtracter.put(CType.PLUSROW, new CExtracterTStrich());
		getTypExtracter.put(CType.SQRT, new CExtracterTSqrt());
	}
	
	public static ExtractHandler getInstance(){
		if (uniqueInstance == null) {
			synchronized (DOMElementMap.class){
				if (uniqueInstance == null){
					uniqueInstance = new ExtractHandler();
				}
			}
		}
		return uniqueInstance;
	}
	
	public CElement extract(CElement parent, ArrayList<CElement>selection, CElement defaultElement){
		System.out.println("Extract from " + parent.getCType());
		return getTypExtracter.get(parent.getCType()).extract(parent, selection, defaultElement);
	}
	
	public boolean justAll(ArrayList<CElement> selection){
		return !(selection.get(0).hasPrevC() || selection.get(selection.size()-1).hasNextC());
	}
	
	public void insertOrReplace(CElement parent, CElement newC, ArrayList<CElement> selection,  boolean replace){
		if (replace){
			System.out.println("// replace");
			parent.getParent().replaceChild(newC, parent, true, true);
		} else {
			System.out.println("// insert");
			parent.replaceChild(newC, selection.get(0), true, true);		
			for (int i =1; i<selection.size();i++){
				parent.removeChild(selection.get(i), true, true, false);
			}
		}
	}
}
