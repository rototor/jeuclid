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

import cTree.CElement;
import cTree.CFences;
import cTree.CNum;
import cTree.CPlusTerm;
import cTree.CPot;
import cTree.cDefence.CD_Event;

public class CC_PotFencedPlusrowNum extends CC_Base {

    @Override
    public boolean canDo() {
        final CElement basis = this.getFirst();
        if (basis instanceof CFences) {
            final CFences cF = (CFences) basis;
            return (cF.getInnen() instanceof CPlusTerm);
        }
        return false;
    }

    @Override
    protected CElement createComb(final CElement parent,
            final CElement basis, final CElement expo, CD_Event cDEvent) {
        final CPot cP = (CPot) parent;
        final CFences cF = (CFences) cP.getBasis();
        final CPlusTerm cM = (CPlusTerm) cF.getInnen();
        final CElement cEl = cM.getValue();
        final CNum cExp = (CNum) cP.getExponent();
        final CElement newEl = cEl.cloneCElement(false);
        final CElement newExp = cExp.cloneCElement(false);
        final CElement nBase = CFences.condCreateFenced(newEl, new CD_Event(
                false));
        final CElement nPot = CPot.createPot(nBase, newExp);
        return nPot;
    }
}
