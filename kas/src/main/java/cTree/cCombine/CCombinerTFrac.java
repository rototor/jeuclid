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

public class CCombinerTFrac extends CCombinerTyp {
	public CCombinerTFrac(){
		super();
		op1Combiner.put(CType.TIMESROW, new CCombiner1FracTR());
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		System.out.println("Frac");
		return op1Combiner.get(cE1.getCType()).combine(parent, cE1, cE2);
	}
	
	
}
