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

import cTree.CElement;

public class CC_ {
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		if (!canCombine(cE1, cE2)) {
			System.out.println("Repelled!!");
			return cE1;
		}
		  cE1.removeCActiveProperty();
		  boolean replace = CombineHandler.getInstance().justTwo(cE1, cE2);
		  CElement newChild = createCombination(parent, cE1, cE2);
		  CombineHandler.getInstance().insertOrReplace(parent, newChild, cE1, cE2, replace);
		  newChild.setCActiveProperty();
		  return newChild;
	}
	
	protected CElement createCombination(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Cant create combination"); 
		// Vorsicht! Fehlbedienung möglich. Repeller einsetzen!
		return cE1;
	}
	
	protected boolean canCombine(CElement cE1, CElement cE2){
		System.out.println("Repell standard?"); 
		return false;
	}
	
}
