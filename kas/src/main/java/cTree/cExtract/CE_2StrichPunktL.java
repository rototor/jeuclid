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

public class CE_2StrichPunktL extends CE_1 {

    @Override
    protected CElement createExtraction(final CElement parent,
            final ArrayList<CElement> selection, final CElement defElement) {
        for (final CElement cEl : selection) {
            cEl.removeCLastProperty();
        }
        selection.get(0).removeCActiveProperty();
        final CElement newLast = selection.get(0).getLastChild()
                .cloneCElement(true);
        CTimesRow newChild = null;
        final ArrayList<CElement> foldedList = CTimesRow.foldL(CTimesRow
                .castList(CRow.cloneList(selection)));
        final CPlusRow reducedSum = CPlusRow.createRow(foldedList);
        reducedSum.correctInternalPraefixesAndRolle();
        final CElement fencedSum = CFences.createFenced(reducedSum);
        // fencedSum.setPraefix("*");
        final ArrayList<CElement> factors = CTimesRow.createList(fencedSum,
                newLast);
        newChild = CTimesRow.createRow(factors);
        newChild.correctInternalCRolles();
        return newChild;
    }

    @Override
    protected boolean canExtract(final CElement parent,
            final ArrayList<CElement> selection) {
        final boolean praefix = selection.get(0).getLastChild().hasExtDiv();
        for (final CElement cEl : selection) {
            if (cEl.getLastChild() == null || !cEl.getLastChild().hasPrevC()
                    || !(praefix == cEl.getLastChild().hasExtDiv())) {
                return false;
            }
        }
        // Wir prüfen, ob die Texte übereinstimmen, sehr provisorisch
        final String vorlage = selection.get(0).getLastChild().getText();
        System.out.println("Vorlage" + vorlage);
        for (final CElement cEl : selection) {
            if (!vorlage.equals(cEl.getLastChild().getText())) {
                System.out.println("Fehler gefunden");
                return false;
            }
        }
        return true;
    }
}
