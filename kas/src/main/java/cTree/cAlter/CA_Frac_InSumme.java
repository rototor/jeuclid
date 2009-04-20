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
import cTree.CNum;

public class CA_Frac_InSumme extends CAlter {

    private CFrac cFrac;

    private CElement z;

    private int zVal;

    private CElement n;

    private int nVal;

    @Override
    public CElement change(final ArrayList<CElement> els) {
        System.out.println("Changer Frac to Int");
        final CNum cNum = CNum.createNum(this.cFrac.getElement(), ""
                + (this.zVal / this.nVal));
        this.cFrac.getParent().replaceChild(cNum, this.cFrac, true, true);
        return cNum;
    }

    @Override
    public String getText() {
        return "Bruch -> ganze Zahl";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        System.out.println("CA-Frac_in Int_check ");
        if (els.size() > 0 && els.get(0) instanceof CFrac) {
            this.cFrac = (CFrac) els.get(0);
            this.z = this.cFrac.getZaehler();
            this.n = this.cFrac.getNenner();
            try {
                if (this.z instanceof CNum && this.n instanceof CNum) {
                    this.zVal = ((CNum) this.z).getValue();
                    this.nVal = ((CNum) this.n).getValue();
                    if (this.nVal != 0 && this.zVal % this.nVal == 0) {
                        return true;
                    }
                }
            } catch (final NumberFormatException e) {
            }
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
