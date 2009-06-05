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
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CA_Times_MinRaus extends CA_Base {

    private CTimesRow cT;

    private CElement parent;

    private ArrayList<CElement> members;

    @Override
    public CElement doIt(final CD_Event message) {
        int counter = 0;

        final ArrayList<CElement> newM = new ArrayList<CElement>();
        for (final CElement member : this.members) {
            if (member instanceof CFences
                    && (((CFences) member).getInnen() instanceof CMinTerm)) {
                counter++;
                final CFences cF = (CFences) member;
                final CMinTerm cMin = (CMinTerm) cF.getInnen();
                final CElement newArg = cMin.getValue().cloneCElement(false);
                final CD_Event didIt = new CD_Event(false);
                final CElement newMember = CFences.condCreateFenced(newArg,
                        didIt);
                newMember.setPraefix(cF.getPraefixAsString());
                newM.add(newMember);
            } else {
                newM.add(member);
            }
        }
        final CTimesRow newT = CTimesRow.createRow(newM);
        newT.correctInternalPraefixesAndRolle();
        CElement newEl = newT;
        if (counter % 2 == 1) {
            newEl = CMinTerm.createMinTerm(newT);
            newEl = CFences.createFenced(newEl);
        }
        if (this.parent instanceof CFences) {
            final CElement gParent = this.parent.getParent();
            gParent.replaceChild(CFences.createFenced(newEl), this.parent,
                    true, true);
        } else {
            this.parent.replaceChild(newEl, this.cT, true, true);
        }
        return newEl;
    }

    @Override
    public String getText() {
        return "Minus zusammenfassen?";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement first = this.getFirst();
            if (first instanceof CTimesRow) {
                this.cT = (CTimesRow) first;
                this.parent = first.getParent();
                this.members = this.cT.getMemberList();
                for (final CElement member : this.members) {
                    if (member instanceof CFences
                            && (((CFences) member).getInnen() instanceof CMinTerm)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
