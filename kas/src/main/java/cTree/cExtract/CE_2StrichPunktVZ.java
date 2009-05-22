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

package cTree.cExtract;

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFences;
import cTree.CPlusRow;
import cTree.CTimesRow;
import cTree.adapter.C_Event;

public class CE_2StrichPunktVZ extends CE_1 {

    @Override
    protected CElement createExtraction() {
        final ArrayList<CElement> selection = this.getEvent().getSelection();
        for (final CElement cEl : selection) {
            cEl.removeCLastProperty();
        }
        selection.get(0).removeCActiveProperty();
        for (final CElement cEl : selection) {
            cEl.removeCLastProperty();
        }
        selection.get(0).removeCActiveProperty();
        CElement newChild = null;
        final ArrayList<CElement> newM = new ArrayList<CElement>();
        // boolean first = !selection.get(0).hasPrevC(); Unmöglich i
        for (final CElement member : selection) {
            // if (first) {
            // final CMinTerm mT = (CMinTerm) member;
            // newM.add(mT.getValue().cloneCElement(false));
            // first = false;
            // } else {
            final CElement newMember = member.cloneCElement(true);
            newMember.togglePlusMinus(false);
            newM.add(newMember);
            // }
        }
        final CPlusRow newP = CPlusRow.createRow(newM);
        newP.correctInternalPraefixesAndRolle();
        newChild = CFences.createFenced(newP);
        final CElement min1 = CFences.createFencedMin1(this.getEvent()
                .getParent());
        final CTimesRow newTR = CTimesRow.createRow(CTimesRow.createList(
                min1, newChild));
        newTR.correctInternalPraefixesAndRolle();
        // if (first) {
        // newChild = CMinTerm.createMinTerm(newChild);
        // }
        return newTR;
    }

    //
    @Override
    public boolean canDo(final C_Event e) {
        if (e == null) {
            return false;
        }
        if (e instanceof C_Event && !e.equals(this.getEvent())) {
            this.setEvent((CE_Event) e);
        }
        return this.getEvent().getFirst().hasExtMinus();
    }
}
