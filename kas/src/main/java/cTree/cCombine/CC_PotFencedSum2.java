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
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CPot;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.adapter.EElementHelper;
import cTree.cDefence.CD_Event;

public class CC_PotFencedSum2 extends CC_Base {

    @Override
    public boolean canDo() {
        final CElement basis = this.getFirst();
        if (basis instanceof CFences) {
            final CFences cF = (CFences) basis;
            if (cF.getInnen() instanceof CPlusRow) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected CElement createComb(final CElement potenz, final CElement fen,
            final CElement exp, final CD_Event cDEvent) {
        // Die CD-Events werden noch nicht ausgewertet.
        System.out.println("Binomische Formel");
        final CFences cF = (CFences) fen;
        final CPlusRow basis = (CPlusRow) cF.getInnen();
        final ArrayList<CElement> oldAddList = basis.getMemberList();
        if (oldAddList.get(0) instanceof CMinTerm) {
            final CFences f = CFences.createFenced(oldAddList.get(0));
            f.setCRolle(CRolle.SUMMAND1);
            oldAddList.set(0, f);
        }
        final ArrayList<CElement> newAddList = new ArrayList<CElement>();
        final ArrayList<CD_Event> cDEvents = new ArrayList<CD_Event>();
        boolean first = true;
        for (int i = 0; i < oldAddList.size() - 1; i++) {
            final CElement f1 = oldAddList.get(i);
            final String vz1 = f1.getPraefixAsString();
            final CD_Event event = new CD_Event(false);
            final CElement ff1 = CFences.condCreateFenced(f1
                    .cloneCElement(false), event);
            final CElement cEl = CPot.createPot(ff1, 2);
            if (first) {
                first = false;
                cEl.setCRolle(CRolle.SUMMAND1);
            } else {
                cEl.setPraefix("+");
                cEl.setCRolle(CRolle.SUMMANDN1);
            }
            newAddList.add(cEl);
            cDEvents.add(event);
            for (int j = i + 1; j < oldAddList.size(); j++) {
                final CElement f2 = oldAddList.get(j);
                final String vz2 = f2.getPraefixAsString();
                final boolean resMinus = (("-".equals(vz1) && !("-"
                        .equals(vz2))) || ("-".equals(vz2) && !("-"
                        .equals(vz1))));
                final ArrayList<CElement> args = new ArrayList<CElement>();
                args.add(CNum.createNum(f1.getElement(), "2"));
                final CElement newFirst = f1.cloneCElement(false);
                newFirst.setExtPraefix(EElementHelper.createOp(f1
                        .getElement(), "*"));
                args.add(newFirst);
                final CElement newSecond = f2.cloneCElement(true);
                newSecond.setExtPraefix(EElementHelper.createOp(f1
                        .getElement(), "*"));
                args.add(newSecond);
                final CElement prod = CTimesRow.createRow(args);
                prod.setCRolle(CRolle.SUMMANDN1);
                if (resMinus) {
                    prod.setPraefix("-");
                } else {
                    prod.setPraefix("+");
                }
                newAddList.add(prod);
            }
        }
        CElement f1 = oldAddList.get(oldAddList.size() - 1);
        final CD_Event event = new CD_Event(false);
        f1 = CFences.condCreateFenced(f1.cloneCElement(false), event);
        final CElement cEl = CPot.createPot(f1, 2);
        cEl.setPraefix("+");
        cEl.setCRolle(CRolle.SUMMANDN1);
        newAddList.add(cEl);
        cDEvents.add(event);
        final CPlusRow newSum = CPlusRow.createRow(newAddList);
        newSum.correctInternalPraefixesAndRolle();
        final CElement newChild = CFences.condCreateFenced(newSum, cDEvent);
        return newChild;
    }
}
