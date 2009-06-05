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

import cTree.CElement;
import cTree.CFrac;
import cTree.CMixedNumber;
import cTree.CNum;
import cTree.CRolle;
import cTree.adapter.C_Event;
import cTree.cDefence.CD_Event;

public class CA_Frac_InGemZahl extends CA_Base {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    private int zz;

    private int nz;

    @Override
    public CElement doIt(final CD_Event message) {
        final int gz = this.zz / this.nz;
        System.out.println("GanzZahl " + gz);
        final int newZZ = this.zz % this.nz;
        System.out.println("Neuer Zaehler " + this.nz);
        final CFrac cF = (CFrac) this.cFrac.cloneCElement(false);
        ((CNum) cF.getZaehler()).setValue(newZZ);
        final CNum cGanz = CNum.createNum(this.cFrac.getElement(), "" + gz);
        final CMixedNumber cEl = CMixedNumber.createMixedNumber(cGanz, cF);
        // cEl.correctInternalRolles();
        this.cFrac.getParent().replaceChild(cEl, this.cFrac, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "Bruch -> GemZahl";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final C_Event event = this.getEvent();
            final CElement first = event.getFirst();
            final ArrayList<CElement> sel = event.getSelection();
            if (sel.size() > 0 && first instanceof CFrac
                    && (first.getCRolle() != CRolle.FRACTION)) {
                this.cFrac = (CFrac) first;
                this.z = this.cFrac.getZaehler();
                this.n = this.cFrac.getNenner();
                if ((this.z instanceof CNum) && (this.n instanceof CNum)) {
                    this.zz = ((CNum) this.z).getValue();
                    this.nz = ((CNum) this.n).getValue();
                    if (this.zz > this.nz) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
