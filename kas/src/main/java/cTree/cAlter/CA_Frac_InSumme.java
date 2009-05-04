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
import cTree.CPlusRow;
import cTree.CRolle;

public class CA_Frac_InSumme extends CAlter {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    private ArrayList<CElement> zs = new ArrayList<CElement>();

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final ArrayList<CElement> fracs = new ArrayList<CElement>();
        for (final CElement z : this.zs) {
            final CFrac frac = CFrac.createFraction(z.cloneCElement(false),
                    this.n.cloneCElement(false));
            frac.setPraefix(z.getPraefixAsString());
            fracs.add(frac);
        }
        final CPlusRow cPR = CPlusRow.createRow(fracs);
        cPR.correctInternalPraefixesAndRolle();
        final CElement cEl = CFences.condCreateFenced(cPR,
                new CMessage(false));
        this.cFrac.getParent().replaceChild(cEl, this.cFrac, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "Bruch -> Summe";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        if (els.size() > 0 && els.get(0) instanceof CFrac
                && (els.get(0).getCRolle() != CRolle.FRACTION)) {
            this.cFrac = (CFrac) els.get(0);
            this.z = this.cFrac.getZaehler();
            this.n = this.cFrac.getNenner();
            if (this.z instanceof CPlusRow) {
                final CPlusRow zsumme = (CPlusRow) this.z;
                this.zs = zsumme.getMemberList();
                return true;
            }
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
