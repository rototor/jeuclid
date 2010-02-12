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
import cTree.CRow;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_PunktNumTR extends CC_Base {

    // noch alt

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Multipliziere Num TR");
        // a*|b*c| -> |a*b*c| und +-*a*|b*c| -> +-*|a*b*c| aber nicht :a*|b*c|
        final CElement faktor1 = cE1.cloneCElement(false); // parent.cloneChild(cE1,
        // false);
        final ArrayList<CElement> first = CRow.makeSingleElementList(faktor1);
        final ArrayList<CElement> second = ((CTimesRow) cE2).getMemberList();
        second.get(0).setPraefix("*");
        CRow.merge(first, second);
        final CTimesRow newChild = CTimesRow.createRow(first);
        newChild.correctInternalCRolles();
        newChild.setCRolleAndPraefixFrom(cE1);
        return newChild;
    }

    @Override
    public boolean canDo() {
        if (this.getFirst().hasExtDiv() || this.getSec().hasExtDiv()) {
            return false;
        }
        return true;
    }

}
