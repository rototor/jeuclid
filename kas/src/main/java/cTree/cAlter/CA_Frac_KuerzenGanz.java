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
import cTree.adapter.C_Event;

public class CA_Frac_KuerzenGanz extends CAlter {

    private CElement z;

    private CElement n;

    @Override
    public CElement doIt() {
        final CFrac cFrac = (CFrac) this.getEvent().getFirst();
        int zVal = ((CNum) this.z).getValue();
        int nVal = ((CNum) this.n).getValue();
        final int newCom = CA_Frac_KuerzenGanz.ggT(zVal, nVal);
        zVal = zVal / newCom;
        nVal = nVal / newCom;
        ((CNum) cFrac.getZaehler()).setValue(zVal);
        ((CNum) cFrac.getNenner()).setValue(nVal);
        return cFrac;
    }

    @Override
    public String getText() {
        return "KuerzenKomplett";
    }

    @Override
    public boolean canDo() {
        final C_Event event = this.getEvent();
        final ArrayList<CElement> els = event.getSelection();
        if (els.size() > 0 && els.get(0) instanceof CFrac) {
            final CFrac cFrac = (CFrac) els.get(0);
            this.z = cFrac.getZaehler();
            this.n = cFrac.getNenner();
            return (this.z instanceof CNum) && (this.n instanceof CNum)
                    && (((CNum) this.n).getValue() != 0);
        }
        return false;
    }

    private static int ggT(final int a, final int b) {
        int g = a;
        int q = b;
        int r = 0;
        while (q != 0) {
            r = g % q;
            g = q;
            q = r;
        }
        return g;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
