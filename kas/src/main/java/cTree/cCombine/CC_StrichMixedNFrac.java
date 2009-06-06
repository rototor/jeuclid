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
import cTree.CMixedNumber;
import cTree.CNum;
import cTree.CRolle;
import cTree.cDefence.CD_Event;

public class CC_StrichMixedNFrac extends CC_Base {

    private CMixedNumber cM;

    private CFrac cF;

    private CNum cNenner;

    private CFrac cF1;

    private CNum cNenner1;

    @Override
    public boolean canDo() {
        this.cM = (CMixedNumber) this.getFirst();
        this.cF = (CFrac) this.cM.getFraction();
        this.cNenner = (CNum) this.cF.getNenner();
        this.cF1 = (CFrac) this.getSec();
        this.cNenner1 = (CNum) this.cF1.getNenner();
        return (this.cNenner.getValue() != 0)
                && (this.cNenner1.getValue() != 0)
                && (this.cF1.hasNumberValue());
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Add Frac and MixedNum");
        if (this.cF.hasNumberValue()) {
            System.out.println("Add Frac and MixedNum NumberV");
            return this.createNumberCombination(parent, cE1, cE2);
        } else {
            System.out.println("Add Frac and MixedNum Term");
            return this.createTermCombination(parent, cE1, cE2);
        }
    }

    protected CElement createNumberCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Add Frac and Mixed");

        final int gVal1 = ((CNum) this.cM.getWholeNumber()).getValue();
        final int zVal1 = ((CNum) this.cF.getZaehler()).getValue();
        final int nVal1 = ((CNum) this.cF.getNenner()).getValue();
        final int zVal2 = ((CNum) this.cF1.getZaehler()).getValue();
        final int nVal2 = ((CNum) this.cF1.getNenner()).getValue();
        final int vz1 = cE1.hasExtMinus() ? -1 : 1;
        final int vz2 = cE2.hasExtMinus() ? -1 : 1;
        final int wertZ = vz2 * zVal2 * nVal1 + vz1 * (gVal1 * nVal1 + zVal1)
                * nVal2;
        final int aWertZ = Math.abs(wertZ);
        final int newNVal = nVal1 * nVal2;
        final int vzWert = (wertZ < 0) ? -1 : 1;
        final int newGVal = aWertZ / newNVal;
        final int newZVal = aWertZ % newNVal;
        final CMixedNumber arg = (CMixedNumber) cE1.cloneCElement(false);
        ((CNum) arg.getWholeNumber()).setValue(newGVal);
        ((CNum) ((CFrac) arg.getFraction()).getZaehler()).setValue(newZVal);
        ((CNum) ((CFrac) arg.getFraction()).getNenner()).setValue(newNVal);
        CElement newChild = arg;
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
        // final CElement cZN = this.cF.getZaehler().cloneCElement(false);
        // final CElement cNN = this.cF.getNenner().cloneCElement(false);
        // final CElement cNN2 = this.cF.getNenner().cloneCElement(false);
        // final CElement cNumN = cE2.cloneCElement(false);
        // final CTimesRow cT =
        // CTimesRow.createRow(CTimesRow.createList(cNumN,
        // cNN));
        // cT.correctInternalPraefixesAndRolle();
        // final CPlusRow cP = CPlusRow.createRow(CPlusRow.createList(cT,
        // cZN));
        // cP.correctInternalPraefixesAndRolle();
        // final CFrac arg = CFrac.createFraction(cP, cNN2);
        // CElement newChild = arg;
        // if (!cE1.hasExtMinus() && cE2.hasExtMinus() || cE1.hasExtMinus()
        // && !cE2.hasExtMinus()) {
        // arg.getZaehler().getFirstChild().getNextSibling()
        // .togglePlusMinus(false);
        // }
        // if (cE1.getCRolle() == CRolle.NACHVZMINUS) {
        // newChild = CMinTerm.createMinTerm(newChild, CRolle.SUMMAND1);
        // }
        return null; // newChild;
    }
}
