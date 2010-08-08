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

import cTree.CElement;
import cTree.CIdent;
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CRow;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_StrichMinrowTR extends CC_Base {

    @Override
    public boolean canDo() {
        System.out.println("Repell add Minrow and TR?");
        final CElement minTermArg = ((CMinTerm) this.getFirst()).getValue();
        if (!((CTimesRow) this.getSec()).isMonom()) {
            return false;
        }
        if (!((CTimesRow) this.getSec()).istGleichartigesMonom(minTermArg)) {
            return false;
        }
        return true;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Add Minrow and TR");
        final int koeff2 = ((CTimesRow) cE2).getKoeffAsBetragFromMonom()
                .getValue();
        CElement newChild = null;
        if (cE1 instanceof CMinTerm) {
            final CElement minTermArg = ((CMinTerm) cE1).getValue();
            int koeff1 = 0;
            if (minTermArg instanceof CIdent) {
                koeff1 = 1;
            } else if (minTermArg instanceof CNum) {
                koeff1 = ((CNum) minTermArg).getValue();
            } else if (minTermArg instanceof CTimesRow) {
                koeff1 = ((CTimesRow) minTermArg).getKoeffAsBetragFromMonom()
                        .getValue();
            } else {
                System.out
                        .println("CC-Strich Minrow TR: That should not have happened.");
            }
            final int vz2 = cE2.hasExtMinus() ? -1 : 1;
            if (koeff2 * vz2 > koeff1) { // -4a + 7a = 3a
                final CTimesRow newArg = (CTimesRow) cE2.cloneCElement(false); // .cloneChild(cE2,
                // false);
                newArg.getKoeffAsBetragFromMonom().setText(
                        "" + (koeff2 - koeff1));
                newChild = newArg;
            } else if (koeff2 * vz2 == koeff1) { // -4a + 4a = 0
                newChild = CNum.createNum(parent.getElement(), "0");
                newChild.setCRolle(cE1.getCRolle());
            } else { // -4a + 2a = -2a oder -4a - 2a = -6a
                if (minTermArg instanceof CTimesRow) {
                    final CTimesRow newArg = (CTimesRow) minTermArg
                            .cloneCElement(true); // .cloneChild(cE2, false);
                    newArg.getKoeffAsBetragFromMonom().setText(
                            "" + (koeff1 - koeff2 * vz2));
                    newChild = CMinTerm
                            .createMinTerm(newArg, cE1.getCRolle());
                } else if (minTermArg instanceof CIdent) { // -a - 2a = -3a
                    // oder -a - a =
                    // -2a
                    final CElement f1 = CNum.createNum(minTermArg
                            .getElement(), "" + (koeff1 - koeff2 * vz2));
                    final CElement f2 = minTermArg.cloneCElement(false);
                    final CTimesRow inhalt = (CTimesRow) CRow.createRow(
                            CTimesRow.createList(f1, f2), "*");
                    inhalt.correctInternalPraefixesAndRolle();
                    newChild = CMinTerm.createMinTerm(inhalt);
                    ((CMinTerm) newChild).setCRolleAndPraefixFrom(cE1);
                } else {
                    System.out
                            .println("CC-Strich Minrow TR: That should not have happened.");
                }
            }
        } else {
            if (cE2.hasExtMinus()) { // bilde TimesRow
                final CTimesRow newArg = (CTimesRow) cE2.cloneCElement(false); // .cloneChild(cE2,
                // false);
                newArg.getKoeffAsBetragFromMonom().setText("" + (koeff2 + 1));
                newChild = CMinTerm.createMinTerm(newArg, cE1.getCRolle());
            } else { // bilde 0
                if (koeff2 == 1) {
                    newChild = CNum.createNum(parent.getElement(), "0");
                    newChild.setCRolle(cE1.getCRolle());
                } else if (koeff2 == 2) {
                    newChild = cE1.getFirstChild().cloneCElement(false); // .cloneChild(cE1.getFirstChild(),
                    // false);
                } else if (koeff2 == 0) {
                    newChild = cE1.cloneCElement(true); // parent.cloneChild(cE1,
                    // true);
                } else {
                    final CTimesRow newArg = (CTimesRow) cE2
                            .cloneCElement(false); // .cloneChild(cE2, false);
                    newArg.getKoeffAsBetragFromMonom().setText(
                            "" + (koeff2 - 1));
                    newChild = newArg;
                }
            }
        }
        return newChild;
    }
}
