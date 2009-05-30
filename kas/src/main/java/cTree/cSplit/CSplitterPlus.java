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

package cTree.cSplit;

import cTree.CElement;
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CRolle;
import cTree.adapter.C_Event;

public class CSplitterPlus extends CSplitterBase {

    @Override
    public boolean canDo() {
        final C_Event event = this.getEvent();
        final CElement cE1 = event.getFirst();
        final String op = ((CS_Event) event).getOperator();
        if (cE1 instanceof CNum && (cE1.getCRolle() != CRolle.NACHVZPLUS)) {
            try {
                Integer.parseInt(op);
                return true;
            } catch (final NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public CElement split() {
        System.out.println("Do the Plus Num split");
        try {
            CElement cE1 = this.getEvent().getFirst();
            final String op = ((CS_Event) this.getEvent()).getOperator();
            final int zahl = Integer.parseInt(op);
            if (cE1 instanceof CNum) {
                final CNum cEl = (CNum) cE1;
                final int cZahl = Integer.parseInt(cEl.getText());
                final boolean zuZerlegenHatMinus = cEl.hasExtMinus();
                if (cEl.hasExtMinus() && cEl.getCRolle() == CRolle.SUMMANDN1) {
                    cE1.getExtPraefix().setTextContent("+");
                } else {
                    // Behandlung der MinRow
                }
                cE1 = this
                        .zerlegeStrich(cZahl, zahl, zuZerlegenHatMinus, cEl);
                return cE1;
            }
        } catch (final NumberFormatException e) {
            System.out.println("Ich kann das split num plus nicht");
        }
        return this.getEvent().getFirst();
    }

    private CElement zerlegeStrich(final int bisher, final int neu,
            final boolean bisherMin, final CNum cEl) {
        // den geänderten Term errechnen
        final int geAendert = bisherMin ? 0 - bisher - neu : bisher - neu;
        final boolean ergebnisNegativ = (geAendert < 0);

        final CNum first = CNum.createNum(cEl.getElement(), ""
                + Math.abs(geAendert));
        final CElement newfirst = ergebnisNegativ ? CMinTerm
                .createMinTerm(first) : first;
        final CNum second = CNum.createNum(cEl.getElement(), "" + neu);
        second.setPraefix("+");
        final CPlusRow newRow = CPlusRow.createRow(CPlusRow.createList(
                newfirst, second));
        newRow.correctInternalPraefixesAndRolle();
        System.out.println("new Row: " + newRow.getText());
        return newRow;
    }

}
