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

package cTree.cAlter;

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFences;
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.CTimesRow;
import cTree.adapter.C_Event;
import cTree.cDefence.CD_Event;

public class CA_PlusRow_MinRaus extends CA_Base {

    private CPlusRow cP;

    private ArrayList<CElement> members;

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement parent = this.cP.getParent();
        this.members = this.cP.getMemberList();
        final ArrayList<CElement> newM = new ArrayList<CElement>();
        boolean first = true;
        for (final CElement member : this.members) {
            if (first) {
                final CMinTerm mT = (CMinTerm) member;
                newM.add(mT.getValue().cloneCElement(false));
                first = false;
            } else {
                final CElement newMember = member.cloneCElement(true);
                newMember.togglePlusMinus(false);
                newM.add(newMember);
            }
        }
        final CPlusRow newP = CPlusRow.createRow(newM);
        newP.correctInternalPraefixesAndRolle();
        final CElement min1 = CFences.createFencedMin1(parent);
        final CElement fencedSum = CFences.createFenced(newP);
        final CTimesRow newTR = CTimesRow.createRow(CTimesRow.createList(
                min1, fencedSum));
        newTR.correctInternalPraefixesAndRolle();
        parent.replaceChild(newTR, this.cP, true, true);
        return newTR;
    }

    @Override
    public String getText() {
        return "Minus vor Summe ziehen";
    }

    @Override
    public boolean canDo() {
        final C_Event event = this.getEvent();
        final ArrayList<CElement> els = event.getSelection();
        if (els.get(0) instanceof CPlusRow) {
            this.cP = (CPlusRow) els.get(0);
            return (this.cP.getFirstChild() instanceof CMinTerm);
        }
        return false;
    }

}
