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

public class CC_StrichMixedNNum extends CC_Base {

    private CMixedNumber cM;

    private CFrac cF;

    private CNum cNenner;

    @Override
    public boolean canDo() {
        this.cM = (CMixedNumber) this.getFirst();
        this.cF = (CFrac) this.cM.getFraction();
        this.cNenner = (CNum) this.cF.getNenner();
        return this.cNenner.getValue() != 0;
    }

    // evtl einbauen dass auch ein Bruch entstehen kann
    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Add Mixed and Num");

        final int numVal = ((CNum) cE2).getValue();
        final int gVal = ((CNum) this.cM.getWholeNumber()).getValue();
        final int zVal = ((CNum) this.cF.getZaehler()).getValue();
        final int nVal = ((CNum) this.cF.getNenner()).getValue();
        final int vz1 = cE1.hasExtMinus() ? -1 : 1;
        final int vz2 = cE2.hasExtMinus() ? -1 : 1;
        final int wertZ = vz2 * numVal * nVal + vz1 * (gVal * nVal + zVal);
        final int aWertZ = Math.abs(wertZ);
        final int vzWert = (wertZ < 0) ? -1 : 1;
        final int newGVal = aWertZ / nVal;
        final int newZVal = aWertZ % nVal;
        CElement newChild = null;
        final CMixedNumber arg = (CMixedNumber) cE1.cloneCElement(false);
        ((CNum) arg.getWholeNumber()).setValue(newGVal);
        ((CNum) ((CFrac) arg.getFraction()).getZaehler()).setValue(newZVal);
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
                final CMinTerm minTerm = CMinTerm.createMinTerm(arg);
                newChild = CFences.condCreateFenced(minTerm, cDEvent);
            }
        }
        return newChild;
    }
}
