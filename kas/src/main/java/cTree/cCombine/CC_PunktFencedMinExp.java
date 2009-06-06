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

public class CC_PunktFencedMinExp extends CC_Base {

    // a*(b+c+d) -> (a*b+a*c+a*d)
    // geht nicht bei : vor a oder () oder wenn in der Klammer keine Summe
    // steht

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Multipliziere geklammerten MinTerm mit Exp");
        final CElement inCE1 = ((CMinTerm) cE1.getFirstChild()).getValue()
                .cloneCElement(false);
        final CElement newCE1 = CFences.condCreateFenced(inCE1, new CD_Event(
                false));
        final CElement newCE2 = cE2.cloneCElement(true);
        final CTimesRow cTR = CTimesRow.createRow(CTimesRow.createList(
                newCE1, newCE2));
        cTR.correctInternalPraefixesAndRolle();
        final CMinTerm cMin = CMinTerm.createMinTerm(cTR);
        return CFences.condCreateFenced(cMin, cDEvent);
    }

    @Override
    public boolean canDo() {
        final CElement cE1 = this.getFirst();
        System.out.println("Repell fenced min mult exp?");
        return !cE1.hasExtDiv();
    }
}
