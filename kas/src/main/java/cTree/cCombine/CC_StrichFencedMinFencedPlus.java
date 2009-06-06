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
import cTree.CPlusRow;
import cTree.CPlusTerm;
import cTree.cDefence.CD_Event;

public class CC_StrichFencedMinFencedPlus extends CC_Base {

    // a*(b+c+d) -> (a*b+a*c+a*d)
    // geht nicht bei : vor a oder () oder wenn in der Klammer keine Summe
    // steht

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Addiere zwei geklammerte MinTerme");
        final CElement inCE1 = cE1.getFirstChild().cloneCElement(false);
        final CElement newCE1 = CFences.condCreateFenced(inCE1, new CD_Event(
                false));
        final CElement inCE2 = ((CPlusTerm) cE2.getFirstChild()).getValue()
                .cloneCElement(false);
        final CElement newCE2 = CFences.condCreateFenced(inCE2, new CD_Event(
                false));
        newCE2.setPraefix(cE2.getPraefixAsString());
        final CPlusRow cPR = CPlusRow.createRow(CPlusRow.createList(newCE1,
                newCE2));
        cPR.correctInternalPraefixesAndRolle();
        final CElement newChild = CFences.condCreateFenced(cPR, cDEvent);
        if (cE1.hasExtMinus()) {
            if (cDEvent.isDoDef()) {
                final CFences cF = (CFences) newChild;
                ((CPlusRow) cF.getInnen()).getFirstChild().getNextSibling()
                        .togglePlusMinus(false);
            } else {
                newChild.getFirstChild().getNextSibling().togglePlusMinus(
                        false);
            }
        }
        return newChild;
    }

    @Override
    public boolean canDo() {
        System.out.println("Repell fenced sum fenced?");
        return true;
    }
}
