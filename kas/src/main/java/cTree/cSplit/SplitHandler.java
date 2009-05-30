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

import java.util.HashMap;

import cTree.adapter.C_Changer;
import cTree.adapter.C_No;
import cTree.adapter.DOMElementMap;

public class SplitHandler {
    private volatile static SplitHandler uniqueInstance;

    public HashMap<String, CSplitterBase> getSplitter;

    private SplitHandler() {
        this.getSplitter = new HashMap<String, CSplitterBase>();
        this.getSplitter.put("*", new CSplitterTimes());
        this.getSplitter.put(":", new CSplitterDiv());
        this.getSplitter.put("+", new CSplitterPlus());
        this.getSplitter.put("-", new CSplitterMin());
        this.getSplitter.put("^", new CSplitterPot());
        this.getSplitter.put("e", new CSplitterErweitern());
    }

    public static SplitHandler getInst() {
        if (SplitHandler.uniqueInstance == null) {
            synchronized (DOMElementMap.class) {
                if (SplitHandler.uniqueInstance == null) {
                    SplitHandler.uniqueInstance = new SplitHandler();
                }
            }
        }
        return SplitHandler.uniqueInstance;
    }

    public C_Changer getSplitr(final CS_Event event) {
        final String s = event.getString();
        if (s.length() < 2) { // kein Operator oder Operand
            return new C_No(event);
        } else {
            final String op = event.getOperation();
            if (this.getSplitter.containsKey(op)) {
                return this.getSplitter.get(op).getSplitr(event);
            } else {
                return new C_No(event);
            }
        }
    }
}
