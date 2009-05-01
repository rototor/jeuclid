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
import cTree.CMixedNumber;
import cTree.CNum;
import cTree.CPlusRow;

public class CA_GemZ_InSum extends CAlter {

    private CMixedNumber cMixed;

    private CElement w;

    private CElement f;

    private CElement z;

    private CElement n;

    private int nz;

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final CPlusRow cP = CPlusRow.createRow(CPlusRow.createList(this.w,
                this.f));
        cP.correctInternalPraefixesAndRolle();
        final CFences cEl = CFences.createFenced(cP);
        this.cMixed.getParent().replaceChild(cEl, this.cMixed, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "GemischteZ -> Summe";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        System.out.println("Check gemz -> Summe");
        if (els.size() > 0 && els.get(0) instanceof CMixedNumber) {
            this.cMixed = (CMixedNumber) els.get(0);
            this.w = this.cMixed.getWholeNumber();
            this.f = this.cMixed.getFraction();
            this.z = ((CFrac) this.cMixed.getFraction()).getZaehler();
            this.n = ((CFrac) this.cMixed.getFraction()).getNenner();
            if ((this.w instanceof CNum) && (this.z instanceof CNum)
                    && (this.n instanceof CNum)) {
                this.nz = ((CNum) this.n).getValue();
                if (this.nz != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
