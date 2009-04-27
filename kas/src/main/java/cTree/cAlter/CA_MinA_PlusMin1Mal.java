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

package cTree.cAlter;

import java.util.ArrayList;
import java.util.HashMap;

import cTree.CElement;
import cTree.CFences;
import cTree.CMinTerm;
import cTree.CNum;
import cTree.CRolle;
import cTree.CTimesRow;

public class CA_MinA_PlusMin1Mal extends CAlter {

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final CElement old = els.get(0);
        final CElement newOne = CNum.createNum(old.getElement(), "1");
        final CElement newFirst = CFences.createFenced(CMinTerm
                .createMinTerm(newOne));
        final CElement newSecond = old.cloneCElement(false);
        final CTimesRow newChild = CTimesRow.createRow(CTimesRow.createList(
                newFirst, newSecond));
        newChild.correctInternalPraefixesAndRolle();
        old.togglePlusMinus(false);
        old.getParent().replaceChild(newChild, old, true, true);
        return newChild;
    }

    @Override
    public String getText() {
        return "-a in Produkt mit (-1)*a 1";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        final CElement el = els.get(0);
        return el.hasExtMinus() && el.getCRolle().equals(CRolle.SUMMANDN1);
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
