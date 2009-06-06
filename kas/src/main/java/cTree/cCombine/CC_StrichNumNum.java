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
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CRolle;
import cTree.cDefence.CD_Event;

public class CC_StrichNumNum extends CC_Base {

    @Override
    public boolean canDo() {
        return true;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cN1,
            final CElement cN2, final CD_Event cDEvent) {
        System.out.println("Add Num and Num");
        final int wert1 = ((CNum) cN1).getValue();
        final int wert2 = ((CNum) cN2).getValue();
        final int vz1 = cN1.hasExtMinus() ? -1 : 1;
        final int vz2 = cN2.hasExtMinus() ? -1 : 1;
        final int wertZ = vz1 * wert1 + vz2 * wert2;
        final int aWertZ = Math.abs(wertZ);
        final int vzWert = (wertZ < 0) ? -1 : 1;
        CElement newChild = null;
        final CNum arg = (CNum) cN1.cloneCElement(false);
        arg.setValue(aWertZ);
        newChild = arg;

        if (cN1.getCRolle() == CRolle.SUMMAND1) {
            if (cN2.hasExtMinus() && (wertZ < 0)) {
                newChild = CMinTerm.createMinTerm(arg, CRolle.SUMMAND1);
            }
        } else if (cN1.getCRolle() == CRolle.NACHVZMINUS) {
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
