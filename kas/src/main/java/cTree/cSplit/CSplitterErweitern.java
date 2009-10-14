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

import cTree.CElement;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CSplitterErweitern extends CSplitterBase {

    public HashMap<String, CSplitterBase> getSplitter;

    /* nur Splits der Form e(x-45) oder e(y+2) oder e(3x+2) sind möglich! */

    public CSplitterErweitern() {
        this.getSplitter = new HashMap<String, CSplitterBase>();
        this.getSplitter.put("i", new CSplitterErweiternIdent());
        this.getSplitter.put("n", new CSplitterErweiternFences());
        this.getSplitter.put("f", new CSplitterErweiternNum());
    }

    @Override
    public C_Changer getChanger(final C_Event event) {
        if (event instanceof CS_Event) {
            for (final CSplitterBase test : this.getSplitter.values()) {
                final C_Changer c = test.getChanger(event);
                if (c.canDo()) {
                    return c;
                }
            }
        }
        return new C_No(event);
    }

    @Override
    protected CElement split() {
        // should never happen
        return null;
    }
}
