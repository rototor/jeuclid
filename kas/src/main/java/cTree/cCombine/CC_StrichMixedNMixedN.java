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

public class CC_StrichMixedNMixedN extends CC_ {

    private CMixedNumber cM1;

    private CFrac cF1;

    private CNum cN1;

    private CMixedNumber cM2;

    private CFrac cF2;

    private CNum cN2;

    @Override
    protected boolean canCombine(final CElement parent,
            final CElement mixed1, final CElement mixed2) {
        this.cM1 = (CMixedNumber) mixed1;
        this.cF1 = (CFrac) this.cM1.getFraction();
        this.cN1 = (CNum) this.cF1.getNenner();
        this.cM2 = (CMixedNumber) mixed2;
        this.cF2 = (CFrac) this.cM2.getFraction();
        this.cN2 = (CNum) this.cF2.getNenner();
        return (this.cN1.getValue() != 0) && (this.cN2.getValue() != 0);
    }

    // evtl einbauen dass auch ein Bruch entstehen kann
    @Override
    protected CElement createCombination(final CElement parent,
            final CElement mixed1, final CElement mixed2) {
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
        System.out.println(aWertZ);
        final int newNVal = nVal1 * nVal2;
        final int vzWert = (wertZ < 0) ? -1 : 1;
        final int newGVal = aWertZ / newNVal;
        final int newZVal = aWertZ % newNVal;
        System.out.println(newZVal);

        CElement newChild = null;
        if (mixed1.getCRolle() == CRolle.SUMMAND1) {
            System.out.println("// falls ein Summand1 dabei ist");
            if (mixed2.hasExtMinus() && (wertZ < 0)) { // So entsteht eine
                // Minrow
                final CMixedNumber arg = (CMixedNumber) mixed1
                        .cloneCElement(false);
                ((CNum) arg.getWholeNumber()).setValue(newGVal);
                ((CNum) ((CFrac) arg.getFraction()).getZaehler())
                        .setValue(newZVal);
                ((CNum) ((CFrac) arg.getFraction()).getNenner())
                        .setValue(newNVal);
                newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
            } else { // So entsteht eine Zahl als Summand1
                final CMixedNumber arg = (CMixedNumber) mixed1
                        .cloneCElement(false);
                ((CNum) arg.getWholeNumber()).setValue(newGVal);
                ((CNum) ((CFrac) arg.getFraction()).getZaehler())
                        .setValue(newZVal);
                ((CNum) ((CFrac) arg.getFraction()).getNenner())
                        .setValue(newNVal);
                newChild = arg;
            }
        } else {
            System.out.println("// falls weitere Summanden");
            final CMixedNumber arg = (CMixedNumber) mixed1
                    .cloneCElement(false);
            ((CNum) arg.getWholeNumber()).setValue(newGVal);
            ((CNum) ((CFrac) arg.getFraction()).getZaehler())
                    .setValue(newZVal);
            ((CNum) ((CFrac) arg.getFraction()).getNenner())
                    .setValue(newNVal);
            if (vzWert * vz1 < 0) {
                newChild = CFences.createFenced(CMinTerm.createMinTerm(arg));
            } else {
                newChild = arg;
            }
        }
        return newChild;
    }
}
