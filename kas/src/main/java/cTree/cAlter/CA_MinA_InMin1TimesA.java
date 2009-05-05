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
import cTree.CTimesRow;
import cTree.CType;

public class CA_MinA_InMin1TimesA extends CAlter {

    @Override
    public CElement change(final ArrayList<CElement> els) {
        final CElement old = els.get(0);
        final CElement newOne = CNum.createNum(old.getElement(), "1");
        final CElement newFirst = CFences.createFenced(CMinTerm
                .createMinTerm(newOne));
        final CElement newSecond = ((CMinTerm) old).getValue().cloneCElement(
                false);
        final CTimesRow newChild = CTimesRow.createRow(CTimesRow.createList(
                newFirst, newSecond));
        newChild.correctInternalPraefixesAndRolle();

        final CElement gParent = old.getParent();
        if (gParent instanceof CFences) {
            final CElement ggParent = gParent.getParent();
            final CElement newF = CFences.createFenced(newChild);
            ggParent.replaceChild(newF, gParent, true, true);
        } else {
            old.getParent().replaceChild(newChild, old, true, true);
        }
        return newChild;
    }

    @Override
    public String getText() {
        return "-a in Produkt mit (-1)*a";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        final CElement el = els.get(0);
        return el.getCType().equals(CType.MINROW);
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
