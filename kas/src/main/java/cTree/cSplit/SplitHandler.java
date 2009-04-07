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

package cTree.cSplit;

import java.util.HashMap;
import cTree.*;
import cTree.adapter.DOMElementMap;

public class SplitHandler {
	private volatile static SplitHandler uniqueInstance; 
	public HashMap<CType, CSplitterTyp> getTypSplitter;
	
	private SplitHandler(){
		getTypSplitter = new HashMap<CType, CSplitterTyp>();
		CSplitterTyp default1 = new CSplitterTyp();
		for (CType cType : CType.values()){
			getTypSplitter.put(cType, default1);
		}
		getTypSplitter.put(CType.PLUSROW, new CSplitterTStrich());
		getTypSplitter.put(CType.TIMESROW, new CSplitterTPunkt());
		getTypSplitter.put(CType.POT, new CSplitterTPot());
	}
	
	public static SplitHandler getInstance(){
		if (uniqueInstance == null) {
			synchronized (DOMElementMap.class){
				if (uniqueInstance == null){
					uniqueInstance = new SplitHandler();
				}
			}
		}
		return uniqueInstance;
	}
	
	public CElement split(CElement parent, CElement cE1, String s){
		System.out.println("Split");
		return getTypSplitter.get(parent.getCType()).split(parent, cE1, s);
	}
	
}
