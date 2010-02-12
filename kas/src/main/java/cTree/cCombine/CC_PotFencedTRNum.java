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

package cTree.cCombine;

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFences;
import cTree.CPot;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_PotFencedTRNum extends CC_Base {

    @Override
    public boolean canDo() {
        final CElement basis = this.getFirst();
        if (basis instanceof CFences) {
            final CFences cF = (CFences) basis;
            return (cF.getInnen() instanceof CTimesRow);
        }
        return false;
    }

    @Override
    protected CElement createComb(final CElement potenz,
            final CElement fences, final CElement expo, final CD_Event cDEvent) {
        System.out.println("(abc)^n nach a^n b^n c^n");
        final CFences cF = (CFences) fences;
        final CTimesRow basis = (CTimesRow) cF.getInnen();
        final CTimesRow oldRow = (CTimesRow) basis.cloneCElement(false);
        final ArrayList<CElement> oldList = oldRow.getMemberList();
        final ArrayList<CElement> newList = new ArrayList<CElement>();
        for (final CElement cEl : oldList) {
            final boolean hasDiv = cEl.hasExtDiv();
            cEl.setExtPraefix(null);
            final CPot newPot = CPot
                    .createPot(cEl, expo.cloneCElement(false));
            if (hasDiv) {
                newPot.setPraefix(":");
            }
            newList.add(newPot);

        }
        final CTimesRow newChild = CTimesRow.createRow(newList);
        newChild.correctInternalPraefixesAndRolle();
        return CFences.condCreateFenced(newChild, cDEvent);
    }
}
