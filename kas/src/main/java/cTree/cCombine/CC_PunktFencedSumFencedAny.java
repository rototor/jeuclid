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
import cTree.CPlusRow;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_PunktFencedSumFencedAny extends CC_Base {

    // a*(b+c+d) -> (a*b+a*c+a*d)
    // geht nicht bei : vor a oder () oder wenn in der Klammer keine Summe
    // steht

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        System.out
                .println("Multipliziere Klammer mit Klammer, die Summe enthält");
        final ArrayList<CElement> oldAddendList = ((CPlusRow) cE1
                .getFirstChild()).getMemberList();
        final ArrayList<CElement> newAddendList = CTimesRow.map(
                oldAddendList, cE2);
        final CPlusRow cPR = CPlusRow.createRow(newAddendList);
        cPR.correctInternalPraefixesAndRolle();
        return CFences.condCreateFenced(cPR, cDEvent);
    }

    @Override
    public boolean canDo() {
        final CElement cE1 = this.getFirst();
        final CElement cE2 = this.getSec();
        System.out.println("Repell fenced mult fenced");
        if (cE1.hasExtDiv() || cE2.hasExtDiv()) {
            return false;
        }
        if (!cE1.hasChildC() || !(cE1.getFirstChild() instanceof CPlusRow)) {
            return false;
        }
        System.out.println("Repell fenced mult fenced can do");
        return true;
    }
}
