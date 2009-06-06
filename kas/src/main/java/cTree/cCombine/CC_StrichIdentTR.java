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
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_StrichIdentTR extends CC_Base {

    @Override
    public boolean canDo() {
        System.out.println("Repell add ident tr");
        final CTimesRow cTR = (CTimesRow) this.getSec();
        final CIdent cIdent = (CIdent) this.getFirst();
        if (!cTR.isMonom()) {
            return false;
        }
        final String a = cIdent.getText();
        final CElement var = (cTR).getFirstVarInMonomRow();
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

    @Override
    protected CElement createComb(final CElement parent,
            final CElement cIdent, final CElement cTR, CD_Event cDEvent) {
        final int exp = ((CTimesRow) cTR).getKoeffAsBetragFromMonom()
                .getValue();
        CElement newChild = null;
        if (exp == 0) {
            newChild = cIdent.cloneCElement(false); // parent.cloneChild(cIdent,
            // false);
        } else if (cIdent.getCRolle() == CRolle.SUMMAND1) {
            if (cTR.hasExtMinus() && exp == 1) { // bilde Zahl 0
                newChild = CNum.createNum(parent.getElement(), "0");
                newChild.setCRolle(cIdent.getCRolle());
            } else if (cTR.hasExtMinus() && exp > 1) { // bilde MinTerm
                final CElement arg = cTR.cloneCElement(false); // parent.cloneChild(cTR,
                // false);
                ((CTimesRow) arg).getKoeffAsBetragFromMonom().setText(
                        "" + (exp - 1));
                newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
            } else { // bilde Timesrow
                newChild = cTR.cloneCElement(false); // parent.cloneChild(cTR,
                // false);
                ((CTimesRow) newChild).getKoeffAsBetragFromMonom().setText(
                        "" + (exp + 1));
            }
        } else {
            if (this.gleicheMin(cIdent, cTR)) { // erhöhe Koeff um 1
                newChild = cTR.cloneCElement(false); // parent.cloneChild(cTR,
                // false);
                ((CTimesRow) newChild).getKoeffAsBetragFromMonom().setText(
                        "" + (exp + 1));
            } else if (exp == 1) { // bilde 0
                newChild = CNum.createNum(parent.getElement(), "0");
                newChild.setCRolle(cIdent.getCRolle());
            } else { // erniedrige Koeffizient um 1
                cIdent.togglePlusMinus(false);
                newChild = cTR.cloneCElement(false); // parent.cloneChild(cTR,
                // false);
                ((CTimesRow) newChild).getKoeffAsBetragFromMonom().setText(
                        "" + (exp - 1));
            }
        }
        return newChild;
    }
}
