/*
 * Copyright 2009 Erhard Kuenzel 03/09
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

package cTree.cCombine;

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFrac;
import cTree.CTimesRow;
import cTree.adapter.C_Event;
import cTree.cDefence.CD_Event;

public class CC_FracTRTR extends CC_Base {

    @Override
    public boolean canDo() {
        final C_Event e = this.getEvent();
        this.setEvent(e);
        if (e.getParent() != null && e.getFirst() != null
                && e.getFirst().hasNextC()) {
            final CElement cE1 = e.getFirst();
            final CElement cE2 = cE1.getNextSibling();
            System.out.println("Repell frac TR TR?");
            if (cE1.getFirstChild().getNextSibling().hasExtDiv()
                    || cE2.getFirstChild().getNextSibling().hasExtDiv()) {
                System.out.println("Zähler oder Nenner hat Div");
                return false;
            }
            if (cE1.getFirstChild().compareTo(cE2.getFirstChild()) != 0) {
                System.out.println("Zähler und Nenner beginnen verschieden");
                return false;
            }
            // if (!cE1.getFirstChild().getText().equals(
            // cE2.getFirstChild().getText())) {
            // System.out.println("Zähler und Nenner beginnen verschieden");
            // return false;
            // }
        }
        return true;
    }

    @Override
    protected CFrac createComb(final CElement parent, final CElement firstTR,
            final CElement secondTR, final CD_Event cDEvent) {
        final ArrayList<CElement> foldedList = CTimesRow.fold(CTimesRow
                .castList(CTimesRow.createList(firstTR, secondTR)));
        final CFrac newFrac = CFrac.createFraction(foldedList.get(0),
                foldedList.get(1));
        return newFrac;
    }

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement parent = this.getParent();
        final CElement cE1 = this.getFirst();
        final CElement cE2 = this.getSec();
        final CFrac newChild = this.createComb(parent, cE1, cE2, null);
        parent.getParent().replaceChild(newChild, parent, true, true);
        return newChild.getZaehler();
    }
}
