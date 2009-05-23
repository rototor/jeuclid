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
import cTree.CFences;
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CRolle;

public class CSplitterPunktPot extends CSplitterBase {

    @Override
    public CElement split() {
        System.out.println("Split Punkt and Num");
        final CElement parent = this.getEvent().getParent();
        final CElement zuZerlegen = this.getEvent().getFirst();
        final String s = ((CS_Event) this.getEvent()).getOperator();
        if (s != "") {
            final String opString = "" + s.charAt(0);
            try {
                final int zahl = Integer.parseInt(s.substring(1));
                if (zuZerlegen instanceof CNum) {
                    final CNum cEl = (CNum) zuZerlegen;
                    final int cZahl = Integer.parseInt(cEl.getText());
                    final boolean zuZerlegenHatDiv = cEl.hasExtDiv();
                    if ("+".equals(opString) || "-".equals(opString)) {
                        this
                                .zerlegeStrich(parent, cZahl, zahl, opString,
                                        cEl);
                    } else if ("*".equals(opString) && (zahl != 0)
                            && ((cZahl % zahl == 0) || zuZerlegenHatDiv)) {
                        int geAendert = cZahl * zahl;
                        if (!zuZerlegenHatDiv) {
                            geAendert = cZahl / zahl;
                        }
                        cEl.setText("" + geAendert);
                        final CElement cE2 = CNum.createNum(parent
                                .getElement(), "" + zahl);
                        cE2.setPraefix("*");
                        cE2.setCRolle(CRolle.FAKTORN1);
                        parent.insertBefore(cE2, zuZerlegen);
                        parent.insertBefore(zuZerlegen, cE2);
                    } else if (":".equals(opString) && (zahl != 0)
                            && ((cZahl % zahl == 0) || !zuZerlegenHatDiv)) {
                        int geAendert = cZahl * zahl;
                        if (zuZerlegenHatDiv) {
                            geAendert = cZahl / zahl;
                        }
                        cEl.setText("" + geAendert);
                        final CElement cE2 = CNum.createNum(parent
                                .getElement(), "" + zahl);
                        cE2.setPraefix(":");
                        cE2.setCRolle(CRolle.FAKTORN1);
                        parent.insertBefore(cE2, zuZerlegen);
                        parent.insertBefore(zuZerlegen, cE2);
                    }
                }
            } catch (final NumberFormatException e) {
                System.out.println("Ich kann das nicht");
            }
        }
        return zuZerlegen;
    }

    private void zerlegeStrich(final CElement parent, final int bisher,
            final int neu, final String opString, final CNum cEl) {
        // eine Klammer bilden und vor zuZerlegen einbringen
        String s = "";
        if (cEl.getExtPraefix() != null) {
            s = cEl.getExtPraefix().getTextContent();
        }
        // in die Plusrow die bearbeitete Zahl (mit Vorzeichen)
        int geAendert = bisher - neu;
        if ("-".equals(opString)) {
            geAendert = bisher + neu;
        }
        final boolean ergebnisNegativ = (geAendert < 0);
        final CElement cE1 = CNum.createNum(parent.getElement(), ""
                + Math.abs(geAendert));
        cE1.setCRolle(CRolle.SUMMAND1);
        // die zweite Zahl
        final CElement cE2 = CNum.createNum(parent.getElement(), "" + neu);
        cE2.setPraefix(opString);
        cE2.setCRolle(CRolle.SUMMANDN1);
        // die Row
        final CElement cFRow = CPlusRow.createRow(CPlusRow.createList(cE1,
                cE2));
        cFRow.setCRolle(CRolle.GEKLAMMERT);
        // die Klammer
        final CElement cFences = CFences.createFenced(cFRow);
        cFences.setCRolle(cEl.getCRolle());
        cFences.setPraefix(s);
        // Anpassung der Vorzeichen, falls nötig
        if (ergebnisNegativ) {
            cE1.togglePlusMinus(true);
        } // kann erst hier sein, weil für toggle ein Parent nötig ist.
        // Schlussarbeiten

        parent.removeChild(cEl, true, true, false);
    }

}
