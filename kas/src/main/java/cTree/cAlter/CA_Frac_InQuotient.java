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

import cTree.CElement;
import cTree.CFences;
import cTree.CFrac;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.adapter.C_Changer;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;

public class CA_Frac_InQuotient extends CA_Base {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    @Override
    public CElement doIt(final CD_Event message) {
        final CD_Event aroundFirst = new CD_Event(false);
        final CD_Event aroundSec = new CD_Event(false);
        final CElement newZ = CFences.condCreateFenced(this.z
                .cloneCElement(true), aroundFirst);
        final CElement newN = CFences.condCreateFenced(this.n
                .cloneCElement(true), aroundSec);
        final CTimesRow cTR = CTimesRow.createRow(CTimesRow.createList(newZ,
                newN));
        cTR.correctInternalPraefixesAndRolle();
        cTR.getFirstChild().getNextSibling().setPraefix(":");
        CElement cEl = CFences.condCreateFenced(cTR, message);
        cEl = this.cFrac.getParent()
                .replaceChild(cEl, this.cFrac, true, true);
        CElement toCorrect = (cEl instanceof CFences) ? ((CFences) cEl)
                .getInnen() : cEl;
        toCorrect = this.condCleanOne(toCorrect, aroundFirst.isDoDef());
        // this.condCleanOne(cEl.getFirstChild().getFirstChild()
        // .getNextSibling(), aroundSec.isMessage());
        // this.condCleanOne(cEl.getFirstChild().getFirstChild(), aroundFirst
        // .isMessage());
        return cEl;
    }

    protected CElement condCleanOne(final CElement el, final boolean doIt) {
        final CD_Event e = new CD_Event(el);
        final C_Changer c = DefHandler.getInst().getChanger(e);
        if (doIt && c.canDo()) {
            return c.doIt(null);
        }
        return el;
    }

    @Override
    public String getText() {
        return "Bruch -> Quotient";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement first = this.getFirst();
            if (first instanceof CFrac
                    && (first.getCRolle() != CRolle.FRACTION)) {
                this.cFrac = (CFrac) first;
                this.z = this.cFrac.getZaehler();
                this.n = this.cFrac.getNenner();
                return true;
            }
        }
        return false;
    }
}
