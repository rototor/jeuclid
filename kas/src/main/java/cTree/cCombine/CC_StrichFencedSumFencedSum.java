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
import cTree.cDefence.CD_Event;

public class CC_StrichFencedSumFencedSum extends CC_Base {

    // a*(b+c+d) -> (a*b+a*c+a*d)
    // geht nicht bei : vor a oder () oder wenn in der Klammer keine Summe
    // steht

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Addiere zwei geklammerte Summen");
        final boolean gleicheVZ = cE1.hasExtMinus() && cE2.hasExtMinus()
                || !cE1.hasExtMinus() && !cE2.hasExtMinus();
        final CPlusRow in1 = (CPlusRow) cE1.getFirstChild();
        final ArrayList<CElement> list1 = in1.getMemberList();
        final CPlusRow in2 = (CPlusRow) cE2.getFirstChild();
        final ArrayList<CElement> list2 = in2.getMemberList();
        list2.get(0).setPraefix("+");
        if (!gleicheVZ) {
            for (final CElement cEl : list2) {
                cEl.togglePlusMinus(false);
            }
        }
        list1.addAll(list2);
        final CPlusRow out1 = CPlusRow.createRow(list1);
        out1.correctInternalPraefixesAndRolle();
        return CFences.condCreateFenced(out1, cDEvent);
    }

    @Override
    public boolean canDo() {
        System.out.println("Repell fenced sum fenced sum?");
        return true;
    }
}
