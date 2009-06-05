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
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CPot;
import cTree.cDefence.CD_Event;

public class CA_DivA_Pot extends CA_Base {

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement old = this.getEvent().getFirst();
        final CElement newNum = CNum.createNum(old.getElement(), "1");
        final CElement newExp = CMinTerm.createMinTerm(newNum);
        final CElement newBase = old.cloneCElement(false);
        final CPot newPot = CPot.createPot(newBase, newExp);
        old.toggleTimesDiv(false);
        old.getParent().replaceChild(newPot, old, true, true);
        return newPot;
    }

    @Override
    public String getText() {
        return ":a in *a^-1 umwandeln";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            return this.getEvent().getFirst().hasExtDiv();
        } else {
            return false;
        }
    }

}
