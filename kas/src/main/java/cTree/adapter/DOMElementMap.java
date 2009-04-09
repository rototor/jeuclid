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

package cTree.adapter;

import java.util.*;
import org.w3c.dom.*;

import cTree.CElement;
import cTree.CType;

public class DOMElementMap {
	
	private volatile static DOMElementMap uniqueInstance; 
	public HashMap<Element, CElement> getCElement;
	public HashMap<String, CType> getType;
	
	private DOMElementMap(){
		getCElement = new HashMap<Element, CElement>();
		getType = new HashMap<String, CType>(); 
		getType.put("math", CType.MATH);
		getType.put("=", CType.EQU);
		getType.put("mi", CType.IDENT);
		getType.put("mn", CType.NUM);
		getType.put("+", CType.PLUSROW);
		getType.put("*", CType.TIMESROW);
		getType.put("mfrac", CType.FRAC);
		getType.put("msup", CType.POT);
		getType.put("msqrt", CType.SQRT);
		getType.put("VzTerm", CType.MINROW);
		getType.put("empty", CType.EMPTY);
		getType.put("unknown", CType.UNKNOWN);
		getType.put("mfenced", CType.FENCES);
	}; 
	
	public static DOMElementMap getInstance(){
		if (uniqueInstance == null) {
			synchronized (DOMElementMap.class){
				if (uniqueInstance == null){
					uniqueInstance = new DOMElementMap();
				}
			}
		}
		return uniqueInstance;
	}
}
