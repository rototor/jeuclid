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
import cTree.CFrac;
import cTree.CMinTerm;
import cTree.adapter.C_Event;

public class CA_Frac_MinusKuerzen extends CAlter {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    @Override
    public CElement doIt() {
        final CElement newNum = ((CMinTerm) this.z).getValue().cloneCElement(
                false);
        final CElement newDen = ((CMinTerm) this.n).getValue().cloneCElement(
                false);
        final CFrac frac = CFrac.createFraction(newNum, newDen);
        this.cFrac.getParent().replaceChild(frac, this.cFrac, true, true);
        return newNum;
    }

    @Override
    public String getText() {
        return "Bruch: -1 kuerzen";
    }

    @Override
    public boolean canDo() {
        final C_Event event = this.getEvent();
        final ArrayList<CElement> els = event.getSelection();
        if (els.size() > 0 && els.get(0) instanceof CFrac) {
            this.cFrac = (CFrac) els.get(0);
            this.z = this.cFrac.getZaehler();
            this.n = this.cFrac.getNenner();
            return ((this.z instanceof CMinTerm) && (this.n instanceof CMinTerm));
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
