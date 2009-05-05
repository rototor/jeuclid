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
import cTree.CMessage;
import cTree.CNum;
import cTree.CPlusRow;
import cTree.CPot;
import cTree.CTimesRow;

public class CC_PotFencedSumNum extends CC_ {

    @Override
    protected boolean canCombine(final CElement parent, final CElement basis,
            final CElement expo) {
        System.out.println("Repell pot fencedSum num?");
        if (basis instanceof CFences) {
            final CFences cF = (CFences) basis;
            return (cF.getInnen() instanceof CPlusRow);
        }
        return false;
    }

    @Override
    protected CElement createCombination(final CElement potenz,
            final CElement fences, final CElement expo) {
        System.out.println("Binomische Formel");
        final CFences cF = (CFences) fences;
        final CPlusRow basis = (CPlusRow) cF.getInnen();
        final CElement old1 = basis.getFirstChild();
        final CElement old2 = old1.getNextSibling();
        final String rz = old2.getPraefixAsString();

        final ArrayList<CElement> list = new ArrayList<CElement>();
        final CElement s1 = CFences.condCreateFenced(old1
                .cloneCElement(false), new CMessage(false));
        final CElement summand1 = CPot.createPot(s1, 2);
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

        final CElement s3 = CFences.condCreateFenced(old2
                .cloneCElement(false), new CMessage(false));
        final CElement summand3 = CPot.createPot(s3, 2);
        summand3.setPraefix("+");
        list.add(summand3);

        final CPlusRow newChild = CPlusRow.createRow(list);
        newChild.correctInternalPraefixesAndRolle();
        newChild.setCRolleAndPraefixFrom(potenz);
        return CFences.createFenced(newChild);
    }
}
