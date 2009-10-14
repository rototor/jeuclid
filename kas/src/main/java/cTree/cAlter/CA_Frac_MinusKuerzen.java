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

import cTree.CElement;
import cTree.CFrac;
import cTree.CMinTerm;
import cTree.cDefence.CD_Event;

public class CA_Frac_MinusKuerzen extends CA_Base {

    private CFrac cFrac;

    private CElement z;

    private CElement n;

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement newNum = ((CMinTerm) this.z).getValue().cloneCElement(
                false);
        final CElement newDen = ((CMinTerm) this.n).getValue().cloneCElement(
                false);
        final CFrac frac = CFrac.createFraction(newNum, newDen);
        this.cFrac.getParent().replaceChild(frac, this.cFrac, true, true);
        return newNum;
    }

    @Override
    public String getText() {
        return "Bruch: -1 kuerzen";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement first = this.getFirst();
            if (first instanceof CFrac) {
                this.cFrac = (CFrac) first;
                this.z = this.cFrac.getZaehler();
                this.n = this.cFrac.getNenner();
                return ((this.z instanceof CMinTerm) && (this.n instanceof CMinTerm));
            }
        }
        return false;
    }

}
