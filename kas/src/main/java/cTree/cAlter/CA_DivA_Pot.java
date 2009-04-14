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
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CPot;

public class CA_DivA_Pot extends CAlter {

    @Override
    public CElement change(final ArrayList<CElement> els) {
        System.out.println("Changer DivA to Exp");
        final CElement old = els.get(0);
        old.removeCActiveProperty();
        final CElement newNum = CNum.createNum(old.getElement(), "1");
        final CElement newExp = CMinTerm.createMinTerm(newNum);
        final CElement newBase = old.cloneCElement(false);
        final CPot newPot = CPot.createPot(newBase, newExp);
        old.toggleTimesDiv(false);
        old.getParent().replaceChild(newPot, old, true, true);
        newPot.setCActiveProperty();
        return newPot;
    }

    @Override
    public String getText() {
        return ":a in *a^-1 umwandeln";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        return els.get(0).hasExtDiv();
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
