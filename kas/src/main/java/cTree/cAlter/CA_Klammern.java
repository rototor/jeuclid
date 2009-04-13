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

public class CA_Klammern extends CAlter {

    @Override
    public CElement change(final CElement old) {
        System.out.println("Setze Klammer " + old.getText());
        final ArrayList<CElement> active = new ArrayList<CElement>();
        active.add(old);
        CElement act = old;
        while (act.hasNextC() && (act.getNextSibling().isLastC())) {
            act = act.getNextSibling();
            active.add(act);
        }
        System.out.println("Setze Klammer " + active.size());
        old.getParent().fence(active);
        return old;
    }

    @Override
    public String getText() {
        return "Klammern setzen";
    }

    @Override
    public boolean check(final CElement el) {
        return el != null;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
