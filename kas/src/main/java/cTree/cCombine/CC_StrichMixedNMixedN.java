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

public class CC_StrichMixedNMixedN extends CC_Base {

    private CMixedNumber cM1;

    private CFrac cF1;

    private CNum cN1;

    private CMixedNumber cM2;

    private CFrac cF2;

    private CNum cN2;

    @Override
    public boolean canDo() {
        this.cM1 = (CMixedNumber) this.getFirst();
        this.cF1 = (CFrac) this.cM1.getFraction();
        this.cN1 = (CNum) this.cF1.getNenner();
        this.cM2 = (CMixedNumber) this.getSec();
        this.cF2 = (CFrac) this.cM2.getFraction();
        this.cN2 = (CNum) this.cF2.getNenner();
        return (this.cN1.getValue() != 0) && (this.cN2.getValue() != 0);
    }

    // evtl einbauen dass auch ein Bruch entstehen kann
    @Override
    protected CElement createComb(final CElement parent,
            final CElement mixed1, final CElement mixed2,
            final CD_Event cDEvent) {
        System.out.println("Add Mixed and Mixed");
        final int gVal1 = ((CNum) this.cM1.getWholeNumber()).getValue();
        final int zVal1 = ((CNum) this.cF1.getZaehler()).getValue();
        final int nVal1 = ((CNum) this.cF1.getNenner()).getValue();
        final int gVal2 = ((CNum) this.cM2.getWholeNumber()).getValue();
        final int zVal2 = ((CNum) this.cF2.getZaehler()).getValue();
        final int nVal2 = ((CNum) this.cF2.getNenner()).getValue();
        final int vz1 = mixed1.hasExtMinus() ? -1 : 1;
        final int vz2 = mixed2.hasExtMinus() ? -1 : 1;
        final int wertZ = vz1 * (gVal1 * nVal1 + zVal1) * nVal2 + vz2
                * (gVal2 * nVal2 + zVal2) * nVal1;
        final int aWertZ = Math.abs(wertZ);
        final int newNVal = nVal1 * nVal2;
        final int vzWert = (wertZ < 0) ? -1 : 1;
        final int newGVal = aWertZ / newNVal;
        final int newZVal = aWertZ % newNVal;

        CElement newChild = null;
        final CMixedNumber arg = (CMixedNumber) mixed1.cloneCElement(false);
        ((CNum) arg.getWholeNumber()).setValue(newGVal);
        ((CNum) ((CFrac) arg.getFraction()).getZaehler()).setValue(newZVal);
        ((CNum) ((CFrac) arg.getFraction()).getNenner()).setValue(newNVal);
        newChild = arg;
        if (mixed1.getCRolle() == CRolle.SUMMAND1) {
            if (mixed2.hasExtMinus() && (wertZ < 0)) {
                newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
            }
        } else if (mixed1.getCRolle() == CRolle.NACHVZMINUS) {
            if (wertZ < 0) {
                newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
            }
        } else {
            if (vzWert * vz1 < 0) {
                final CMinTerm minTerm = CMinTerm.createMinTerm(arg);
                newChild = CFences.condCreateFenced(minTerm, cDEvent);
            }
        }
        return newChild;
    }
}
