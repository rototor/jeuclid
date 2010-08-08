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
import cTree.CFences;
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.adapter.EElementHelper;
import cTree.cDefence.CD_Event;

public class CC_PunktFencedSumFencedSum extends CC_Base {

    // a*(b+c+d) -> (a*b+a*c+a*d)
    // geht nicht bei : vor a oder () oder wenn in der Klammer keine Summe
    // steht

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        System.out.println("Multipliziere zwei Summen");
        final ArrayList<CElement> oldAddList1 = ((CPlusRow) cE1
                .getFirstChild()).getMemberList();
        if (oldAddList1.get(0) instanceof CMinTerm) {
            final CFences f = CFences.createFenced(oldAddList1.get(0));
            f.setCRolle(CRolle.SUMMAND1);
            oldAddList1.set(0, f);
        }
        final ArrayList<CElement> oldAddList2 = ((CPlusRow) cE2
                .getFirstChild()).getMemberList();
        if (oldAddList2.get(0) instanceof CMinTerm) {
            final CFences f = CFences.createFenced(oldAddList2.get(0));
            f.setCRolle(CRolle.SUMMAND1);
            oldAddList2.set(0, f);
        }
        final ArrayList<CElement> newAddList = new ArrayList<CElement>();
        boolean first = true;
        for (final CElement f1 : oldAddList1) {
            for (final CElement f2 : oldAddList2) {
                final String vz1 = f1.getPraefixAsString();
                final String vz2 = f2.getPraefixAsString();
                final boolean resMinus = (("-".equals(vz1) && !("-"
                        .equals(vz2))) || ("-".equals(vz2) && !("-"
                        .equals(vz1))));
                final ArrayList<CElement> args = new ArrayList<CElement>();
                final CElement newFirst = f1.cloneCElement(false);
                args.add(newFirst);
                final CElement newSecond = f2.cloneCElement(true);
                newSecond.setExtPraefix(EElementHelper.createOp(f1
                        .getElement(), "*"));
                args.add(newSecond);
                final CElement prod = CTimesRow.createRow(args);
                if (resMinus) {
                    prod.setPraefix("-");
                    prod.setCRolle(CRolle.SUMMANDN1);
                } else if (!first) {
                    prod.setPraefix("+");
                    prod.setCRolle(CRolle.SUMMANDN1);
                }
                if (first) {
                    first = false;
                    prod.setCRolle(CRolle.SUMMAND1);
                }
                newAddList.add(prod);
            }
        }
        final CPlusRow newSum = CPlusRow.createRow(newAddList);
        newSum.correctInternalPraefixesAndRolle();
        return CFences.condCreateFenced(newSum, cDEvent);
    }

    @Override
    public boolean canDo() {
        final CElement cE1 = this.getFirst();
        final CElement cE2 = this.getSec();
        System.out.println("Repell fenced sum mult fenced sum");
        if (cE1.hasExtDiv() || cE2.hasExtDiv()) {
            return false;
        }
        if (!cE2.hasChildC() || !(cE2.getFirstChild() instanceof CPlusRow)
                || !cE1.hasChildC()
                || !(cE1.getFirstChild() instanceof CPlusRow)) {
            return false;
        }
        System.out.println("Repell returns canDo true");
        return true;
    }
}
