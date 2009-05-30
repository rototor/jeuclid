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

package cTree.cAlter;

import java.util.ArrayList;
import java.util.HashMap;

import cTree.CElement;
import cTree.CFences;
import cTree.adapter.C_Event;

public class CA_Klammern extends CAlter {

    @Override
    public CElement doIt() {
        final ArrayList<CElement> els = this.getEvent().getSelection();
        return els.get(0).getParent().fence(els);
    }

    @Override
    public String getText() {
        return "Klammern setzen";
    }

    @Override
    public boolean canDo() {
        final C_Event event = this.getEvent();
        final CElement first = event.getFirst();
        final ArrayList<CElement> els = event.getSelection();
        return (els.size() > 0 && first != null && !(els.size() == 1 && (first instanceof CFences)));
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
