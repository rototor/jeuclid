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
import cTree.adapter.C_Event;

public class CA_GemZ_InFrac extends CAlter {

    private CMixedNumber cMixed;

    private CElement w;

    private CElement z;

    private CElement n;

    private int wz;

    private int zz;

    private int nz;

    @Override
    public CElement doIt() {
        final int newZZ = this.zz + this.nz * this.wz;
        final CFrac cEl = (CFrac) this.cMixed.getFraction().cloneCElement(
                false);
        ((CNum) cEl.getZaehler()).setValue(newZZ);
        this.cMixed.getParent().replaceChild(cEl, this.cMixed, true, true);
        return cEl;
    }

    @Override
    public String getText() {
        return "GemischteZ -> Bruch";
    }

    @Override
    public boolean canDo(final C_Event event) {
        this.setEvent(event);
        final ArrayList<CElement> els = event.getSelection();
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
