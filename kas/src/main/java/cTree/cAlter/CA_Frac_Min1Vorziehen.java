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
import cTree.CMinTerm;
import cTree.CPlusRow;

public class CA_Frac_Min1Vorziehen extends CAlter {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final CElement newNum;
        final CElement newFrac;
        if (this.z instanceof CMinTerm) {
            final CMinTerm minTerm = (CMinTerm) this.z;
            newNum = minTerm.getValue().cloneCElement(false);
            newFrac = CFrac.createFraction(newNum, this.n
                    .cloneCElement(false));
        } else {
            newNum = CMinTerm.createMinTerm(this.z.cloneCElement(false));
            newFrac = CFrac.createFraction(newNum, this.n
                    .cloneCElement(false));
        }
        if (this.cFrac.getParent() instanceof CMinTerm) {
            final CMinTerm minTerm = (CMinTerm) this.cFrac.getParent();
            final CFences cF = CFences.createFenced(newFrac);
            this.cFrac.getParent().replaceChild(cF, minTerm, true, true);
            return cF;
        } else {
            final CMinTerm minTerm = CMinTerm.createMinTerm(newFrac);
            final CFences cF = CFences.createFenced(minTerm);
            this.cFrac.getParent().replaceChild(cF, this.cFrac, true, true);
            return cF;
        }
    }

    @Override
    public String getText() {
        return "Bruch: -1 vorziehen";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        if (els.size() > 0 && els.get(0) instanceof CFrac) {
            this.cFrac = (CFrac) els.get(0);
            this.z = this.cFrac.getZaehler();
            this.n = this.cFrac.getNenner();
            return !(this.z instanceof CPlusRow);

        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
