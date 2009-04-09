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

package cTree.cCombine;

import java.util.HashMap;
import cTree.*;

public class CombineHandler {
	
	private volatile static CombineHandler uniqueInstance; 
	public HashMap<CType, CCombinerTyp> getTypCombiner;
	
	/* 
	 * Je nach Operatortyp wird gemaess einer HashMap verzweigt
	 */
	private CombineHandler(){
		getTypCombiner = new HashMap<CType, CCombinerTyp>();
		CCombinerTyp default1 = new CCombinerTyp();
		for (CType cType : CType.values()){
			getTypCombiner.put(cType, default1);
		}
		getTypCombiner.put(CType.PLUSROW, new CCombinerTStrich());
		getTypCombiner.put(CType.TIMESROW, new CCombinerTPunkt());
		getTypCombiner.put(CType.POT, new CCombinerTPot());
		getTypCombiner.put(CType.FRAC, new CCombinerTFrac());
	}
	
	public static CombineHandler getInstance(){
		if (uniqueInstance == null) {
			synchronized (CombineHandler.class){
				if (uniqueInstance == null){
					uniqueInstance = new CombineHandler();
				}
			}
		}
		return uniqueInstance;
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		System.out.println("CombineHandler");
		return getTypCombiner.get(parent.getCType()).combine(parent, cE1, cE2);
	}
	
	// -------------------------------------------------------------------------------
	
	public boolean justTwo(CElement first, CElement second){
		return !(first.hasPrevC() || second.hasNextC());
	}
	/*
	 * Je nach replace wird entweder paren oder repC ersetzt. remC wird entfernt.
	 */
	public void insertOrReplace(CElement parent, CElement newC, CElement repC, CElement remC, boolean replace){
		if (replace){
			System.out.println("// replace");
			parent.getParent().replaceChild(newC, parent, true, true);
		} else {
			System.out.println("// insert");
			parent.replaceChild(newC, repC, true, true);
			parent.removeChild(remC, true, true, false);
		}
	}
	
}
