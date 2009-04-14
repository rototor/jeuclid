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
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CPot;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.CType;

public class CC_PotFencesNum extends CC_ {

    // parent basis und exponent können zusammengeholt sein, parent ist nur
    // producer
    private CType cType;

    @Override
    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        if (!this.canCombine(parent, ((CFences) cE1).getInnen(), cE2)) {
            return cE1;
        }
        System.out.println("PotFences - Can combine");
        // parent ist die Potenz, cE1 die Basis /Klammer cE2 der Exponent
        cE1.removeCActiveProperty();
        final CElement newArg = this.createCombination(parent.getParent(),
                ((CFences) cE1).getInnen(), cE2);
        final CFences newChild = CFences.createFenced(newArg);
        newArg.setCRolle(CRolle.GEKLAMMERT);
        parent.getParent().replaceChild(newChild, parent, true, true);
        // CombineHandler.getInstance().insertOrReplace(parent, newChild, cE1,
        // cE2, replace);
        newChild.setCActiveProperty();
        return newChild;
    }

    @Override
    protected boolean canCombine(final CElement parent, final CElement basis,
            final CElement expo) {
        System.out.println("Repell pot fences num?");
        if ((basis instanceof CPlusRow) && ((CPlusRow) basis).getCount() == 2
                && ((CNum) expo).getValue() == 2) {
            this.cType = CType.PLUSROW;
            return true;
        }
        if (basis instanceof CTimesRow) {
            this.cType = CType.TIMESROW;
            return true;
        }
        return false;
    }

    @Override
    protected CElement createCombination(final CElement potenz,
            final CElement basis, final CElement expo) {
        if (this.cType == CType.PLUSROW) {
            return this.createP(potenz, basis, expo);
        } else {
            return this.createT(potenz, basis, expo);
        }
    }

    protected CElement createP(final CElement potenz, final CElement basis,
            final CElement expo) {
        System.out.println("Binomische Formel");
        final CElement old1 = basis.getFirstChild();
        old1.show();
        final CElement old2 = old1.getNextSibling();
        old2.show();
        final String rz = old2.getPraefixAsString();

        final ArrayList<CElement> list = new ArrayList<CElement>();
        final CElement summand1 = CPot
                .createPot(old1.cloneCElement(false), 2);
        list.add(summand1);

        final ArrayList<CElement> list2 = new ArrayList<CElement>();
        list2.add(CNum.createNum(potenz.getElement(), "2"));
        list2.add(old1.cloneCElement(false));
        list2.add(old2.cloneCElement(false));
        final CTimesRow summand2 = CTimesRow.createRow(list2);
        summand2.correctInternalPraefixesAndRolle();
        System.out.println("Binomische Formel Summand 2:");
        summand2.show();
        summand2.setPraefix(rz);
        list.add(summand2);

        final CElement summand3 = CPot
                .createPot(old2.cloneCElement(false), 2);
        summand3.setPraefix("+");
        list.add(summand3);

        final CPlusRow newChild = CPlusRow.createRow(list);
        newChild.correctInternalPraefixesAndRolle();
        newChild.setCRolleAndPraefixFrom(potenz);
        return newChild;
    }

    protected CElement createT(final CElement potenz, final CElement basis,
            final CElement expo) {
        System.out.println("(abc)^n nach a^n b^n c^n");
        final CTimesRow oldRow = (CTimesRow) basis.cloneCElement(false);
        final ArrayList<CElement> oldList = oldRow.getMemberList();
        final ArrayList<CElement> newList = new ArrayList<CElement>();
        for (final CElement cEl : oldList) {
            cEl.setExtPraefix(null);
            final CPot newPot = CPot
                    .createPot(cEl, expo.cloneCElement(false));
            if (cEl.hasExtDiv()) {
                newPot.setPraefix(":");
            }
            newList.add(newPot);

        }
        final CTimesRow newChild = CTimesRow.createRow(newList);
        newChild.correctInternalPraefixesAndRolle();
        newChild.setCRolleAndPraefixFrom(potenz);
        return newChild;
    }
}
