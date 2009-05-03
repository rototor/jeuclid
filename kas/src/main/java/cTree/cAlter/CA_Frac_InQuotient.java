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
import cTree.CFrac;
import cTree.CMessage;
import cTree.CTimesRow;
import cTree.cDefence.DefenceHandler;

public class CA_Frac_InQuotient extends CAlter {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final CMessage aroundFirst = new CMessage(false);
        final CMessage aroundSec = new CMessage(false);
        final CElement newZ = CFences.condCreateFenced(this.z
                .cloneCElement(true), aroundFirst);
        final CElement newN = CFences.condCreateFenced(this.n
                .cloneCElement(true), aroundSec);
        final CTimesRow cTR = CTimesRow.createRow(CTimesRow.createList(newZ,
                newN));
        cTR.correctInternalPraefixesAndRolle();
        cTR.getFirstChild().getNextSibling().setPraefix(":");
        final CMessage aroundAll = new CMessage(false);
        CElement cEl = CFences.condCreateFenced(cTR, aroundAll);
        cEl = this.cFrac.getParent()
                .replaceChild(cEl, this.cFrac, true, true);
        if (aroundAll.isMessage()) {
            this.condCleanOne(cEl.getFirstChild().getFirstChild()
                    .getNextSibling(), aroundSec.isMessage());
            this.condCleanOne(cEl.getFirstChild().getFirstChild(),
                    aroundFirst.isMessage());
        } else {
            this.condCleanOne(cEl.getFirstChild().getNextSibling(), aroundSec
                    .isMessage());
            this.condCleanOne(cEl.getFirstChild(), aroundFirst.isMessage());
        }
        this.condCleanOne(cEl, aroundFirst.isMessage());
        return cEl;
    }

    @Override
    public String getText() {
        return "Bruch -> Quotient";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        if (els.size() > 0 && els.get(0) instanceof CFrac) {
            this.cFrac = (CFrac) els.get(0);
            this.z = this.cFrac.getZaehler();
            this.n = this.cFrac.getNenner();
            return true;
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }

    protected void condCleanOne(final CElement el, final boolean doIt) {
        if (doIt
                && DefenceHandler.getInstance().canDefence(el.getParent(),
                        el, el.getFirstChild())) {
            DefenceHandler.getInstance().defence(el.getParent(), el,
                    el.getFirstChild());
        }
    }
}
