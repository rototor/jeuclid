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
import cTree.CMessage;
import cTree.CNum;
import cTree.CPot;
import cTree.CTimesRow;
import cTree.cDefence.DefHandler;

public class CA_PotA_Times extends CAlter {

    private CPot cPot;

    private CNum cExp;

    private CElement cBase;

    private int exp;

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final ArrayList<CElement> list = new ArrayList<CElement>();
        for (int i = 0; i < this.exp; i++) {
            list.add(this.cBase.cloneCElement(false));
        }
        final CTimesRow cTR = CTimesRow.createRow(list);
        cTR.correctInternalPraefixesAndRolle();
        final CMessage didIt = new CMessage(false);
        final CElement newEl = CFences.condCreateFenced(cTR, didIt);
        final CElement parent = els.get(0).getParent();
        parent.replaceChild(newEl, els.get(0), true, true);
        System.out.println("DidIt " + didIt);
        if (didIt.isMessage()) {
            return DefHandler.getInst().defence(parent, newEl,
                    newEl.getFirstChild());
        }
        return newEl;
    }

    @Override
    public String getText() {
        return "Potenz in Produkt";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        if (els.get(0) instanceof CPot) {
            this.cPot = (CPot) els.get(0);
            if (this.cPot.getExponent() instanceof CNum) {
                this.cExp = (CNum) this.cPot.getExponent();
                this.cBase = this.cPot.getBasis();
                this.exp = this.cExp.getValue();
                return this.exp < 4;
            }
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
