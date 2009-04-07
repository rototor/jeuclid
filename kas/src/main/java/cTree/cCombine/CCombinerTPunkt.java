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

public class CCombinerTPunkt extends CCombinerTyp {
	public CCombinerTPunkt(){
		super();
		op1Combiner.put(CType.NUM, new CCombiner1PunktNum());
		op1Combiner.put(CType.IDENT, new CCombiner1PunktIdent());
		op1Combiner.put(CType.POT, new CCombiner1PunktPot());
		op1Combiner.put(CType.FENCES, new CCombiner1PunktFences());
		op1Combiner.put(CType.SQRT, new CCombiner1PunktSqrt());
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Multi");
		if (cE1.is0() && !cE2.hasExtDiv()){
			return (new CC_Punkt0Any()).combine(parent, cE1, cE2);
		} else if (cE2.is0()&& !cE2.hasExtDiv()){
			return (new CC_PunktAny0()).combine(parent, cE1, cE2);
		} else if ((cE1 instanceof CNum) && ((CNum) cE1).is1()&& !cE2.hasExtDiv()){
			return (new CC_Punkt1Any()).combine(parent, cE1, cE2);
		} else if ((cE2 instanceof CNum) && ((CNum) cE2).is1()){
			return (new CC_PunktAny1()).combine(parent, cE1, cE2);
		}
		return op1Combiner.get(cE1.getCType()).combine(parent, cE1, cE2);
	}
	
	
}
