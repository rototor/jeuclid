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
import cTree.CMinTerm;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_PunktFracFencedMin extends CC_Base {

    @Override
    protected CElement createComb(final CElement oldSumme,
            final CElement cE1, final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Mult Frac mit Klammer, die MinTerm enthält");

        final boolean toggle = cE1.hasExtDiv();
        final CElement newFirstFactor = cE1.cloneCElement(false);
        final CElement newSecondFactor = ((CMinTerm) cE2.getFirstChild())
                .getValue().cloneCElement(false);
        newSecondFactor.setPraefix(cE2.getPraefixAsString());
        if (toggle) {
            if (newSecondFactor.hasExtDiv()) {
                newSecondFactor.setPraefix("*");
            } else {
                newSecondFactor.setPraefix(":");
            }
        }
        final CTimesRow newTR = CTimesRow.createRow(CTimesRow.createList(
                newFirstFactor, newSecondFactor));
        newTR.correctInternalPraefixesAndRolle();
        final CMinTerm newMinTerm = CMinTerm.createMinTerm(newTR);
        return CFences.condCreateFenced(newMinTerm, cDEvent);

    }

    @Override
    public boolean canDo() {
        System.out.println("Can Combine Frac times FencedMin?");
        return true;
    }
}
