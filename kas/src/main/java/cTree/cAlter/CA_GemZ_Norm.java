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
import cTree.CMixedNumber;
import cTree.CNum;

public class CA_GemZ_Norm extends CAlter {

    private CMixedNumber cMixed;

    private CElement w;

    private CElement z;

    private CElement n;

    private int wz;

    private int zz;

    private int nz;

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final int raus = this.zz / this.nz;
        final int newZZ = this.zz % this.nz;
        final int newWZ = this.wz + raus;
        final CElement cEl;
        if (newZZ == 0) {
            cEl = CNum.createNum(this.cMixed.getElement(), "" + newWZ);
        } else {
            cEl = this.cMixed.cloneCElement(false);
            final CMixedNumber cM = (CMixedNumber) cEl;
            final CFrac cFrac = (CFrac) cM.getFraction();
            final CNum cWhole = (CNum) cM.getWholeNumber();
            ((CNum) cFrac.getZaehler()).setValue(newZZ);
            cWhole.setValue(newWZ);
        }
        this.cMixed.getParent().replaceChild(cEl, this.cMixed, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "GemischteZ zusammenfassen";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        System.out.println("Check gemz zusammenfassen");
        if (els.size() > 0 && els.get(0) instanceof CMixedNumber) {
            this.cMixed = (CMixedNumber) els.get(0);
            this.w = this.cMixed.getWholeNumber();
            this.z = ((CFrac) this.cMixed.getFraction()).getZaehler();
            this.n = ((CFrac) this.cMixed.getFraction()).getNenner();
            if ((this.w instanceof CNum) && (this.z instanceof CNum)
                    && (this.n instanceof CNum)) {
                this.wz = ((CNum) this.w).getValue();
                this.zz = ((CNum) this.z).getValue();
                this.nz = ((CNum) this.n).getValue();
                if (this.nz != 0 && (this.zz >= this.nz)) {
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
