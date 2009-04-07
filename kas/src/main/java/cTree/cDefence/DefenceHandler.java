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

package cTree.cDefence;

import java.util.HashMap;
import cTree.*;
import cTree.adapter.DOMElementMap;

public class DefenceHandler {
	private volatile static DefenceHandler uniqueInstance; 
	public HashMap<CType, CDefenceTyp> getTypDefencer;
	
	private DefenceHandler(){
		getTypDefencer = new HashMap<CType, CDefenceTyp>();
		CDefenceTyp default1 = new CDefenceTyp();
		for (CType cType : CType.values()){
			getTypDefencer.put(cType, default1);
		}
		getTypDefencer.put(CType.MINROW, new CDefenceTMin());
		getTypDefencer.put(CType.PLUSROW, new CDefenceTStrich());
		getTypDefencer.put(CType.TIMESROW, new CDefenceTPunkt());
		getTypDefencer.put(CType.POT, new CDefenceTPot());
	}
	
	public static DefenceHandler getInstance(){
		if (uniqueInstance == null) {
			synchronized (DOMElementMap.class){
				if (uniqueInstance == null){
					uniqueInstance = new DefenceHandler();
				}
			}
		}
		return uniqueInstance;
	}
	/*
	 * Es wird ein CDefenceTyp nach dem Parent gewählt, dieser entscheidet nach dem Content
	 */
	public CElement defence(CElement parent, CElement fences, CElement content){
		System.out.println("DefenceHandler call " + parent.getCType());
		return getTypDefencer.get(parent.getCType()).defence(parent, fences, content);
	}
	
	public void replaceFoP(CElement parent, CElement newC, CElement repC, boolean replace){
		if (replace){
			System.out.println("// replace Parent of Fences");
			CElement grandParent = parent.getParent();
			if (grandParent instanceof CMath && newC instanceof CFences){
				grandParent.replaceChild(newC.getFirstChild(), parent, true, true);
			} else {
				parent.getParent().replaceChild(newC, parent, true, true);
			}
		} else {
			System.out.println("// replace Fences");
			parent.replaceChild(newC, repC, true, true);
		}
	}
	
	
}
