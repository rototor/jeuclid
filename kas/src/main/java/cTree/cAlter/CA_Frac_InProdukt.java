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
import cTree.CFences;
import cTree.CFrac;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.adapter.C_Event;
import cTree.cDefence.CD_Event;

public class CA_Frac_InProdukt extends CA_Base {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    private ArrayList<CElement> zp = new ArrayList<CElement>();

    @Override
    public CElement doIt(final CD_Event message) {
        final ArrayList<CElement> els1 = new ArrayList<CElement>();
        boolean first = true;

        for (final CElement z : this.zp) {
            if (first) {
                final CFrac frac = CFrac.createFraction(z
                        .cloneCElement(false), this.n.cloneCElement(false));
                frac.setPraefix(this.z.getPraefixAsString());
                els1.add(frac);
                first = false;
            } else {
                els1.add(z.cloneCElement(true));
            }
        }
        final CTimesRow cTR = CTimesRow.createRow(els1);
        cTR.correctInternalPraefixesAndRolle();
        final CElement cEl = CFences.condCreateFenced(cTR,
                new CD_Event(false));
        this.cFrac.getParent().replaceChild(cEl, this.cFrac, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "Bruch -> Produkt";
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
                if (this.z instanceof CTimesRow) {
                    final CTimesRow zprod = (CTimesRow) this.z;
                    this.zp = zprod.getMemberList();
                    return true;
                }
            }
        }
        return false;
    }

}
