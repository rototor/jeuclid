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
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.CRow;
import cTree.CTimesRow;
import cTree.adapter.C_Event;

public class CE_2StrichPunktLM extends CExtractBase {

    private String vorlage = "";

    private CMinTerm mT;

    private CTimesRow arg;

    @Override
    protected CElement createExtraction() {
        final ArrayList<CElement> selection = this.getEvent().getSelection();
        for (final CElement cEl : selection) {
            cEl.removeCLastProperty();
        }
        selection.get(0).removeCActiveProperty();
        CTimesRow newChild = null;
        final CTimesRow firstC = (CTimesRow) this.arg.cloneCElement(false);
        final CElement outC = this.arg.getLastChild();
        final CElement newFirstArg = CTimesRow.foldOneL(firstC);
        final CElement newFirst = CMinTerm.createMinTerm(newFirstArg);
        final ArrayList<CElement> clones = CRow.cloneList(selection);
        clones.remove(0);
        final ArrayList<CTimesRow> cloneTR = CTimesRow.castList(clones);
        final ArrayList<CElement> foldedSList = CTimesRow.foldL(cloneTR);
        final ArrayList<CElement> foldedList = new ArrayList<CElement>();
        foldedList.add(newFirst);
        foldedList.addAll(foldedSList);
        final CPlusRow reducedSum = CPlusRow.createRow(foldedList);
        reducedSum.correctInternalPraefixesAndRolle();
        final CElement fencedSum = CFences.createFenced(reducedSum);
        final ArrayList<CElement> factors = CTimesRow.createList(fencedSum,
                outC);
        newChild = CTimesRow.createRow(factors);
        newChild.correctInternalCRolles();
        return newChild;
    }

    @Override
    public boolean canDo() {
        final C_Event e = this.getEvent();
        if (e == null || !(e instanceof C_Event)) {
            return false;
        }
        this.setEvent(e);
        final ArrayList<CElement> selection = this.getEvent().getSelection();
        boolean first = true;
        boolean praefix = false;
        for (final CElement cEl : selection) {
            if (first) {
                this.mT = (CMinTerm) cEl;
                this.arg = (CTimesRow) this.mT.getValue();
                praefix = this.arg.getLastChild().hasExtDiv();
                this.vorlage = this.arg.getLastChild().getText();
                first = false;
            } else {
                if (cEl.getLastChild() == null
                        || !cEl.getLastChild().hasPrevC()
                        || !(praefix == cEl.getLastChild().hasExtDiv())) {
                    return false;
                }
                if (!this.vorlage.equals(cEl.getLastChild().getText())) {
                    System.out.println("Fehler gefunden");
                    return false;
                }
            }
        }
        return true;
    }
}
