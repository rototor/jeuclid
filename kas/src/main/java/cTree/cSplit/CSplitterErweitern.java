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

public class CSplitterErweitern extends CSplitter1 {

    public HashMap<String, CSplitter1> getSplitter;

    private CSplitter1 splitter;

    /* nur Splits der Form e(x-45) oder e(y+2) sind möglich! */

    public CSplitterErweitern() {
        this.getSplitter = new HashMap<String, CSplitter1>();
        this.getSplitter.put("i", new CSplitterErweiternIdent());
        this.getSplitter.put("n", new CSplitterErweiternFences());
        this.getSplitter.put("f", new CSplitterErweiternNum());
    }

    @Override
    public boolean check(final CElement cE1, final String operator) {
        for (final CSplitter1 test : this.getSplitter.values()) {
            if (test.check(cE1, operator)) {
                this.splitter = test;
                return true;
            }
        }
        return false;
    }

    @Override
    public CElement split(final CElement parent, final CElement cE1,
            final String operator) {
        System.out.println("SplitterErweitern Split "
                + this.splitter.toString());
        return this.splitter.split(parent, cE1, operator);
    }

}
