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

import cTree.*;

public class CCombiner1PunktFences extends CCombiner1 {
	public CCombiner1PunktFences(){
		super(); 
		op2Combiner.put(CType.IDENT, new CC_PunktFencesIdent());
		op2Combiner.put(CType.NUM, new CC_PunktFencesNum());
		op2Combiner.put(CType.POT, new CC_PunktFencesPot());
		op2Combiner.put(CType.FENCES, new CC_PunktFencesFences());
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Mult Fences");
		return op2Combiner.get(cE2.getCType()).combine(parent, cE1, cE2);
	}
}