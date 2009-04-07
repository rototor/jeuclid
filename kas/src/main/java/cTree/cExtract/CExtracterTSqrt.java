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

package cTree.cExtract;

import cTree.*;
import java.util.*;

public class CExtracterTSqrt extends CExtracterTyp {
	public CExtracterTSqrt(){
		super();
		this.op1Extracter.put(CType.TIMESROW, new CE_2SqrtPunkt());
		this.op1Extracter.put(CType.POT, new CE_2SqrtPot());
	}
	
	public CElement extract(CElement parent, ArrayList<CElement> selection, CElement defElement){
		return op1Extracter.get(selection.get(0).getCType()).extract(parent, selection, defElement);
	}
}
