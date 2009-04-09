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

import java.util.*;
import cTree.*;

public class CCombinerTyp {
	public HashMap<CType, CCombiner1> op1Combiner;
	
	public CCombinerTyp(){
		op1Combiner = new HashMap<CType, CCombiner1>(); 
		for (CType cType : CType.values()){
			op1Combiner.put(cType, new CCombiner1());
		}
	}
	
	public CElement combine(CElement parent, CElement cE1, CElement cE2){
		System.out.println("DefaultOp");
		return op1Combiner.get(cE1.getCType()).combine(parent, cE1, cE2);
	}
	
//	public CElement create(CElement producer, CElement first, CElement second){
//		System.out.println("Default"); 
//		return  op1Combiner.get(first.getCType()).create(producer, first, second);
//	}
}
