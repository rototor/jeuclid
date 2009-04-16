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

public class CC_FracTRTR extends CC_ {

    @Override
    protected boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("Repell frac TR TR?");
        if (cE1.getFirstChild().getNextSibling().hasExtDiv()
                || cE2.getFirstChild().getNextSibling().hasExtDiv()) {
            System.out.println("Zähler oder Nenner hat Div");
            return false;
        }
        if (!cE1.getFirstChild().getText().equals(
                cE2.getFirstChild().getText())) {
            System.out.println("Zähler und Nenner beginnen verschieden");
            return false;
        }
        return true;
    }

    @Override
    protected CFrac createCombination(final CElement parent,
            final CElement firstTR, final CElement secondTR) {
        final ArrayList<CElement> foldedList = CTimesRow.fold(CTimesRow
                .castList(CTimesRow.createList(firstTR, secondTR)));
        final CFrac newFrac = CFrac.createFraction(foldedList.get(0),
                foldedList.get(1));
        return newFrac;
    }

    @Override
    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        // parent ist frac cE1 der Zähler cE2 der Nenner
        if (!this.canCombine(parent, cE1, cE2)) {
            return cE1;
        }
        final CFrac newChild = this.createCombination(parent, cE1, cE2);
        parent.getParent().replaceChild(newChild, parent, true, true); // false
        // als
        // Praefix?
        // newChild.getZaehler().setCActiveProperty();
        return newChild.getZaehler();
    }
}
