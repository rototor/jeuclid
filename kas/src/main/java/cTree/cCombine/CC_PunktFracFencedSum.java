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

public class CC_PunktFracFencedSum extends CC_Base {

    @Override
    protected CElement createComb(final CElement oldSumme,
            final CElement cE1, final CElement cE2, final CD_Event cDEvent) {
        System.out
                .println("Multipliziere Num mit Klammer, die Summe enthält");
        final ArrayList<CElement> oldAddendList = ((CPlusRow) cE2
                .getFirstChild()).getMemberList();
        final ArrayList<CElement> newAddendList = CTimesRow.map(cE1,
                oldAddendList);
        final CPlusRow cPR = CPlusRow.createRow(newAddendList);
        cPR.correctInternalPraefixesAndRolle();
        return CFences.condCreateFenced(cPR, cDEvent);
    }

    @Override
    public boolean canDo() {
        return !this.getSec().hasExtDiv();
    }
}
