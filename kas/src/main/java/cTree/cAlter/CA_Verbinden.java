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

import java.util.HashMap;

import cTree.CElement;
import cTree.cCombine.CombineHandler;

public class CA_Verbinden extends CAlter {

    @Override
    public CElement change(final CElement first) {
        System.out.println("Verbinde " + first.getText());
        return CombineHandler.getInstance().combine(first.getParent(), first,
                first.getNextSibling());
    }

    @Override
    public String getText() {
        return "Verbinden mit Naechstem";
    }

    @Override
    public boolean check(final CElement el) {
        return el.hasNextC() && el.hasParent();
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}