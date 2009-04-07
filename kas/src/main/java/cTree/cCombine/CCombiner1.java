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

import cTree.CElement;
import cTree.CType;

public class CCombiner1 {
	
	public HashMap<CType, CC_> op2Combiner;
	/*
	 * Bei bekanntem Operator wird nach dem ersten Operanden gemaess einer HashMap verzweigt.
	 */
	public CCombiner1(){
		op2Combiner = new HashMap<CType, CC_>(); 
		CC_ default2 = new CC_();
		for (CType cType : CType.values()){
			op2Combiner.put(cType, default2);
		}
		
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Default1");
		return op2Combiner.get(cE2.getCType()).combine(parent, cE1, cE2);
	}
	
}
