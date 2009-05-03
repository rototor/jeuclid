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
import cTree.CMessage;
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CRolle;

public class CC_StrichFracFrac extends CC_ {

    private CFrac b1;

    private CElement z1;

    private CElement n1;

    private CFrac b2;

    private CElement z2;

    private final CMessage z1_TryDefence = new CMessage(false);

    private final CMessage z2_TryDefence = new CMessage(false);

    private final CMessage n1_TryDefence = new CMessage(false);

    private boolean gleicheMin;

    private CFrac newChild;

    @Override
    protected boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        if (cE1 instanceof CFrac && cE2 instanceof CFrac) {
            this.b1 = (CFrac) cE1;
            this.b2 = (CFrac) cE2;
            if (this.b1.hasNumberValue() && this.b2.hasNumberValue()) {
                return true;
            }
            this.z1 = this.b1.getZaehler().cloneCElement(false);
            this.n1 = this.b1.getNenner().cloneCElement(false);
            this.z2 = this.b2.getZaehler().cloneCElement(false);
            if (this.n1.getText().equals(this.b2.getNenner().getText())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected CElement createCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Add Frac and MixedNum");
        if (this.b1.hasNumberValue() && this.b2.hasNumberValue()) {
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

        final int zVal1 = ((CNum) this.b1.getZaehler()).getValue();
        final int nVal1 = ((CNum) this.b1.getNenner()).getValue();
        final int zVal2 = ((CNum) this.b2.getZaehler()).getValue();
        final int nVal2 = ((CNum) this.b2.getNenner()).getValue();
        final int vz1 = cE1.hasExtMinus() ? -1 : 1;
        final int vz2 = cE2.hasExtMinus() ? -1 : 1;
        final int wertZ = vz1 * zVal1 * nVal2 + vz2 * zVal2 * nVal1;
        int aWertZ = Math.abs(wertZ);
        int newNVal = nVal1 * nVal2;
        final int newCom = CC_StrichFracFrac.ggT(aWertZ, newNVal);
        aWertZ = aWertZ / newCom;
        newNVal = newNVal / newCom;
        final int vzWert = (wertZ < 0) ? -1 : 1;
        final CFrac arg = (CFrac) cE1.cloneCElement(false);
        ((CNum) arg.getZaehler()).setValue(aWertZ);
        ((CNum) arg.getNenner()).setValue(newNVal);
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

    private static int ggT(final int a, final int b) {
        int g = a;
        int q = b;
        int r = 0;
        while (q != 0) {
            r = g % q;
            g = q;
            q = r;
        }
        return g;
    }

    protected CElement createTermCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Addieren Brüche");
        final boolean zuerstMin = cE1.hasExtMinus();
        final boolean dannMin = cE2.hasExtMinus();
        this.gleicheMin = zuerstMin && dannMin || !zuerstMin && !dannMin;

        this.z1 = CFences.condCreateFenced(this.z1, this.z1_TryDefence);
        this.z2 = CFences.condCreateFenced(this.z2, this.z2_TryDefence);
        this.n1 = CFences.condCreateFenced(this.n1, this.n1_TryDefence);
        if (cE1.getCRolle() == CRolle.NACHVZMINUS) {
            final CMinTerm cM = CMinTerm.createMinTerm(this.z1);
            final CPlusRow zaehlerNeu = CPlusRow.createRow(CPlusRow
                    .createList(cM, this.z2));
            if (dannMin) {
                zaehlerNeu.getFirstChild().getNextSibling().togglePlusMinus(
                        false);
            }
            zaehlerNeu.correctInternalPraefixesAndRolle();
            this.newChild = CFrac.createFraction(zaehlerNeu, this.n1);
        } else {
            if (this.gleicheMin) {
                final CPlusRow zaehlerNeu = CPlusRow.createRow(CPlusRow
                        .createList(this.z1, this.z2));
                zaehlerNeu.correctInternalPraefixesAndRolle();
                this.newChild = CFrac.createFraction(zaehlerNeu, this.n1);
            } else {
                final CPlusRow zaehlerNeu = CPlusRow.createRow(CPlusRow
                        .createList(this.z1, this.z2));
                zaehlerNeu.correctInternalPraefixesAndRolle();
                zaehlerNeu.getFirstChild().getNextSibling().togglePlusMinus(
                        false);
                this.newChild = CFrac.createFraction(zaehlerNeu, this.n1);
            }
            this.z1 = this.newChild.getZaehler().getFirstChild();
            this.z2 = this.z1.getNextSibling();
            this.n1 = this.newChild.getNenner().getFirstChild();
        }

        return this.newChild;
    }

    @Override
    protected void clean() {
        this.condCleanOne(this.z2, this.z2_TryDefence.isMessage());
        this.z1 = this.newChild.getZaehler().getFirstChild();
        this.condCleanOne(this.z1, this.z1_TryDefence.isMessage());
        this.n1 = this.newChild.getNenner().getFirstChild();
        this.condCleanOne(this.n1, this.n1_TryDefence.isMessage());
    }
}
