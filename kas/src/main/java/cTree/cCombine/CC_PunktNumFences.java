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
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.CTimesRow;

public class CC_PunktNumFences extends CC_ {

    @Override
    protected CElement createCombination(final CElement oldSumme,
            final CElement cE1, final CElement cE2) {
        if (cE2.getFirstChild() instanceof CPlusRow) {
            System.out
                    .println("Multipliziere Num mit Klammer, die Summe enthält");
            final ArrayList<CElement> oldAddendList = ((CPlusRow) cE2
                    .getFirstChild()).getMemberList();
            final ArrayList<CElement> newAddendList = CTimesRow.map(cE1,
                    oldAddendList);
            final CFences newChild = CFences.createFenced(CPlusRow
                    .createRow(newAddendList));
            ((CPlusRow) newChild.getInnen())
                    .correctInternalPraefixesAndRolle();
            return newChild;
        } else { // only for MinTerm
            System.out
                    .println("Multipliziere Num mit Klammer, die MinTerm enthält");
            final CElement newFirstFactor = cE1.cloneCElement(false);
            final CElement newSecondFactor = ((CMinTerm) cE2.getFirstChild())
                    .getValue().cloneCElement(false);
            newSecondFactor.setPraefix("*");
            final CTimesRow newTR = CTimesRow.createRow(CTimesRow.createList(
                    newFirstFactor, newSecondFactor));
            final CMinTerm newMinTerm = CMinTerm.createMinTerm(newTR);
            final CFences newFences = CFences.createFenced(newMinTerm);
            return newFences;
        }
    }

    @Override
    protected boolean canCombine(final CElement parent, final CElement el,
            final CElement el2) {
        System.out.println("Can Combine Num times Fences?");
        return ((!el2.hasExtDiv()) && ((el2.getFirstChild() instanceof CMinTerm) || (el2
                .getFirstChild() instanceof CPlusRow)));
    }

}
