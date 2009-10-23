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

import java.util.ArrayList;

import cTree.CElement;
import cTree.CRolle;
import cTree.CSqrt;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_PunktSqrtSqrt extends CC_Base {

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Multipliziere Sqrts");
        if (cE1.hasExtDiv()) {
            System.out
                    .println("// es geht mit : los, darf nicht sein! Repeller verwenden");
        }
        final ArrayList<CElement> radList = CSqrt.getRadikandList(CSqrt
                .createRootList(cE1, cE2));
        final CTimesRow cTR = CTimesRow.createRow(radList);
        cTR.correctInternalPraefixesAndRolle();
        final CElement newChild = CSqrt.createSqrt(cTR);
        if (cE1.getCRolle() != CRolle.FAKTOR1) {
            newChild.setCRolle(CRolle.FAKTORN1);
        }
        return newChild;
    }

    @Override
    public boolean canDo() {
        System.out.println("Repell sqrt times sqrt: only faktor1");
        if (this.getFirst().hasExtDiv()) {
            return false;
        }
        return true;
    }
}
