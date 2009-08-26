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

import java.util.HashMap;

import org.w3c.dom.Element;

import cTree.CElement;
import cTree.CType;

public class DOMElementMap {

    private volatile static DOMElementMap uniqueInstance;

    public HashMap<Element, CElement> getCElement;

    public HashMap<String, CType> getType;

    private DOMElementMap() {
        this.getCElement = new HashMap<Element, CElement>();
        this.getType = new HashMap<String, CType>();
        this.getType.put("math", CType.MATH);
        this.getType.put("=", CType.EQU);
        this.getType.put("mi", CType.IDENT);
        this.getType.put("mn", CType.NUM);
        this.getType.put("+", CType.PLUSROW);
        this.getType.put("*", CType.TIMESROW);
        this.getType.put("mfrac", CType.FRAC);
        this.getType.put("msup", CType.POT);
        this.getType.put("msqrt", CType.SQRT);
        this.getType.put("VzTerm", CType.MINROW);
        this.getType.put("PTerm", CType.PLUSTERM);
        this.getType.put("mmixed", CType.MIXEDN);
        this.getType.put("empty", CType.EMPTY);
        this.getType.put("unknown", CType.UNKNOWN);
        this.getType.put("mfenced", CType.FENCES);
    };

    public static DOMElementMap getInstance() {
        if (DOMElementMap.uniqueInstance == null) {
            synchronized (DOMElementMap.class) {
                if (DOMElementMap.uniqueInstance == null) {
                    DOMElementMap.uniqueInstance = new DOMElementMap();
                }
            }
        }
        return DOMElementMap.uniqueInstance;
    }
}
