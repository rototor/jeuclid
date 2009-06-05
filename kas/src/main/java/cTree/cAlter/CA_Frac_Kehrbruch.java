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
import cTree.cDefence.CD_Event;

public class CA_Frac_Kehrbruch extends CA_Base {

    @Override
    public CElement doIt(final CD_Event message) {
        final CFrac old = (CFrac) this.getEvent().getFirst();
        final CElement newNum = old.getNenner().cloneCElement(true);
        final CElement newDen = old.getZaehler().cloneCElement(true);
        final CFrac newFrac = CFrac.createFraction(newNum, newDen);
        newFrac.correctInternalRolles();
        old.toggleTimesDiv(false);
        old.getParent().replaceChild(newFrac, old, true, true);
        return newFrac;
    }

    @Override
    public String getText() {
        return "a/b in b/a umwandeln";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement first = this.getFirst();
            return (first.hasExtDiv() || first.hasExtTimes())
                    && (first instanceof CFrac);
        } else {
            return false;
        }
    }

}
