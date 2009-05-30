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
import cTree.CFences;
import cTree.cDefence.DefHandler;

public class CA_Entklammern extends CAlter {

    @Override
    public CElement doIt() {
        final CElement fences = this.getEvent().getFirst();
        System.out.println("Entferne Klammer " + fences.getText());
        return DefHandler.getInst().defence(fences.getParent(), fences,
                ((CFences) fences).getInnen());
    }

    @Override
    public String getText() {
        return "Klammern entfernen";
    }

    @Override
    public boolean canDo() {
        final CElement first = this.getEvent().getFirst();
        if (first instanceof CFences) {
            final CFences cF = (CFences) first;
            if (cF.hasParent() && cF.getInnen() != null) {
                return DefHandler.getInst().canDefence(cF.getParent(), cF,
                        cF.getInnen());
            }
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
