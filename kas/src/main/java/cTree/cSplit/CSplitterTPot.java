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

import cTree.CElement;
import cTree.CType;

public class CSplitterTPot extends CSplitterTyp {
	public CSplitterTPot(){
		super();
		this.op1Combiner.put(CType.IDENT, new CSplitterPotIdent());
	}
	
	public CElement split(CElement parent, CElement cE1, String s){
		System.out.println("Split Pot Here");
		return op1Combiner.get(cE1.getCType()).split(parent, cE1, s);
	}	
}
