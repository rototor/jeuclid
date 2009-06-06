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
import cTree.CRolle;
import cTree.cDefence.CD_Event;

public class CC_StrichFracFracN extends CC_Base {

    private CFrac b1;

    private CFrac b2;

    @Override
    public boolean canDo() {
        final CElement cE1 = this.getFirst();
        final CElement cE2 = this.getSec();
        if (cE1 instanceof CFrac && cE2 instanceof CFrac) {
            this.b1 = (CFrac) cE1;
            this.b2 = (CFrac) cE2;
            if (this.b1.hasNumberValue() && this.b2.hasNumberValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
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
        final int newCom = CC_StrichFracFracN.ggT(aWertZ, newNVal);
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
                final CMinTerm minTerm = CMinTerm.createMinTerm(arg);
                newChild = CFences.condCreateFenced(minTerm, cDEvent);
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
}
