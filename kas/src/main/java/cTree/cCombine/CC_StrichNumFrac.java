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
import cTree.CFences;
import cTree.CFrac;
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_StrichNumFrac extends CC_Base {

    private CFrac cF;

    private CElement cNenner;

    @Override
    public boolean canDo() {
        this.cF = (CFrac) this.getSec();
        this.cNenner = this.cF.getNenner();
        return !(this.cNenner instanceof CNum)
                || (((CNum) this.cNenner).getValue() != 0);
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Add Num and Frac");
        if (this.cF.hasNumberValue()) {
            System.out.println("Add Num and Frac NumberV");
            return this.createNumberCombination(parent, cE1, cE2);
        } else {
            System.out.println("Add Num and Frac Term");
            return this.createTermCombination(parent, cE1, cE2);
        }
    }

    protected CElement createNumberCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        final int numVal = ((CNum) cE1).getValue();
        final int zVal = ((CNum) this.cF.getZaehler()).getValue();
        final int nVal = ((CNum) this.cF.getNenner()).getValue();
        final int vz1 = cE1.hasExtMinus() ? -1 : 1;
        final int vz2 = cE2.hasExtMinus() ? -1 : 1;
        final int wertZ = vz1 * numVal * nVal + vz2 * zVal;
        final int aWertZ = Math.abs(wertZ);
        final int vzWert = (wertZ < 0) ? -1 : 1;
        CElement newChild = null;
        final CFrac arg = (CFrac) cE2.cloneCElement(false);
        ((CNum) arg.getZaehler()).setValue(aWertZ);
        newChild = arg;
        if (cE1.getCRolle() == CRolle.SUMMAND1) {
            if (cE2.hasExtMinus() && (wertZ < 0)) {
                newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
            }
        } else if (cE1.getCRolle() == CRolle.NACHVZMINUS) {
            if (wertZ < 0) {
                newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
            }
        } else {
            if (vzWert * vz1 < 0) {
                newChild = CFences.createFenced(CMinTerm.createMinTerm(arg));
            }
        }

        return newChild;
    }

    protected CElement createTermCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        final CElement cZN = this.cF.getZaehler().cloneCElement(false);
        final CElement cNN = this.cF.getNenner().cloneCElement(false);
        final CElement cNN2 = this.cF.getNenner().cloneCElement(false);
        final CElement cNumN = cE1.cloneCElement(false);
        final CTimesRow cT = CTimesRow.createRow(CTimesRow.createList(cNumN,
                cNN));
        cT.correctInternalPraefixesAndRolle();
        final CPlusRow cP = CPlusRow.createRow(CPlusRow.createList(cT, cZN));
        cP.correctInternalPraefixesAndRolle();
        final CFrac arg = CFrac.createFraction(cP, cNN2);
        CElement newChild = arg;
        if (!cE1.hasExtMinus() && cE2.hasExtMinus() || cE1.hasExtMinus()
                && !cE2.hasExtMinus()) {
            arg.getZaehler().getFirstChild().getNextSibling()
                    .togglePlusMinus(false);
        }
        if (cE1.getCRolle() == CRolle.NACHVZMINUS) {
            newChild = CMinTerm.createMinTerm(newChild, CRolle.SUMMAND1);
        }
        return newChild;
    }
}
