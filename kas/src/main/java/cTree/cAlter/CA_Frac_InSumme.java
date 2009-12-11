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
import cTree.CPlusRow;
import cTree.CRolle;
import cTree.cDefence.CD_Event;

public class CA_Frac_InSumme extends CA_Base {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    private ArrayList<CElement> zs = new ArrayList<CElement>();

    @Override
    public CElement doIt(final CD_Event message) {
        final ArrayList<CElement> fracs = new ArrayList<CElement>();
        for (final CElement z : this.zs) {
            final CFrac frac = CFrac.createFraction(z.cloneCElement(false),
                    this.n.cloneCElement(false));
            frac.setPraefix(z.getPraefixAsString());
            fracs.add(frac);
        }
        final CPlusRow cPR = CPlusRow.createRow(fracs);
        cPR.correctInternalPraefixesAndRolle();
        final CElement cEl = CFences.condCreateFenced(cPR, message);
        this.cFrac.getParent().replaceChild(cEl, this.cFrac, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "Bruch -> Summe";
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
                if (this.z instanceof CPlusRow) {
                    final CPlusRow zsumme = (CPlusRow) this.z;
                    this.zs = zsumme.getMemberList();
                    return true;
                }
            }
        }
        return false;
    }

}
