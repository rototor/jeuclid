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
import cTree.CRow;
import cTree.CTimesRow;
import cTree.adapter.C_Event;

public class CE_2StrichPunkt1 extends CExtractBase {

    @Override
    public boolean canDo() {
        final C_Event e = this.getEvent();
        if (e == null || !(e instanceof C_Event)) {
            return false;
        }
        this.setEvent(e);
        final ArrayList<CElement> selection = this.getEvent().getSelection();
        for (final CElement cEl : selection) {
            if (cEl.getFirstChild() == null
                    || cEl.getFirstChild().getNextSibling() == null
                    || cEl.getFirstChild().getNextSibling().hasExtDiv()) {
                return false;
            }
        }
        // Wir prüfen, ob die Texte übereinstimmen, sehr provisorisch
        final String vorlage = this.getEvent().getFirst().getFirstChild()
                .getText();
        System.out.println("Vorlage" + vorlage);
        for (final CElement cEl : selection) {
            if (!vorlage.equals(cEl.getFirstChild().getText())) {
                System.out.println("Fehler gefunden");
                return false;
            }
        }
        return true;
    }

    @Override
    protected CElement createExtraction() {
        final ArrayList<CElement> selection = this.getEvent().getSelection();
        for (final CElement cEl : selection) {
            cEl.removeCLastProperty();
        }
        selection.get(0).removeCActiveProperty();
        final CElement defElement = this.getEvent().getFirstFirst()
                .cloneCElement(false);
        CTimesRow newChild = null;
        final ArrayList<CElement> foldedList = CTimesRow.fold(CTimesRow
                .castList(CRow.cloneList(selection)));
        final CPlusRow reducedSum = CPlusRow.createRow(foldedList);
        reducedSum.correctInternalPraefixesAndRolle();
        final CElement fencedSum = CFences.createFenced(reducedSum);
        fencedSum.setPraefix("*");
        final ArrayList<CElement> factors = CTimesRow.createList(defElement,
                fencedSum);
        newChild = CTimesRow.createRow(factors);
        newChild.correctInternalCRolles();
        return newChild;

    }
}
