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
import cTree.CNum;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_StrichTRIdent extends CC_Base {

    @Override
    protected CElement createComb(final CElement parent, final CElement cTR,
            final CElement cIdent, CD_Event cDEvent) {
        System.out.println("Add TR and ID ohne 0 und 1");
        final int koeff = ((CTimesRow) cTR).getKoeffAsBetragFromMonom()
                .getValue();// Vorsicht! Fehlbedienung möglich. Repeller
        // einsetzen!
        System.out.println("Koeffizient " + koeff);
        CElement newChild = null;
        if (cTR.getCRolle() == CRolle.SUMMAND1) {
            if (cIdent.hasExtMinus()) {
                newChild = cTR.cloneCElement(false);
                (((CTimesRow) newChild).getKoeffAsBetragFromMonom())
                        .setValue(koeff - 1);
            } else {
                newChild = cTR.cloneCElement(false);
                (((CTimesRow) newChild).getKoeffAsBetragFromMonom())
                        .setValue(koeff + 1);
            }
        } else { // cTR Summandn1
            if (this.gleicheMin(cIdent, cTR)) { // erhöhe Koeff um 1
                newChild = cTR.cloneCElement(false); // parent.cloneChild(cTR,
                // false);
                ((CTimesRow) newChild).getKoeffAsBetragFromMonom().setText(
                        "" + (koeff + 1));
            } else if (koeff == 0) {
                newChild = cIdent.cloneCElement(false);
            } else if (koeff == 1) {
                newChild = CNum.createNum(parent.getElement(), "0");
            } else {
                newChild = cTR.cloneCElement(false); // parent.cloneChild(cTR,
                // false);
                ((CTimesRow) newChild).getKoeffAsBetragFromMonom().setText(
                        "" + (koeff - 1));
            }
        }
        return newChild;
    }

    // @Override
    // protected boolean canDo(final CElement parent, final CElement cTR,
    // final CElement cIdent) {
    @Override
    public boolean canDo() {
        final CTimesRow cTR = (CTimesRow) this.getFirst();
        final CIdent cIdent = (CIdent) this.getSec();
        if (!cTR.isMonom()) {
            return false;
        }
        final String a = cIdent.getText();
        final CElement var = cTR.getFirstVarInMonomRow();
        if (!(var instanceof CIdent) || !a.equals(((CIdent) var).getText())) {
            return false;
        }
        return true;
    }

    private boolean gleicheMin(final CElement el1, final CElement el2) {
        final boolean result1 = (el1.hasExtMinus() && el2.hasExtMinus());
        final boolean result2 = (!el1.hasExtMinus() && !el2.hasExtMinus());
        return result1 || result2;
    }
}
