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
import cTree.CIdent;
import cTree.CNum;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_StrichIdentIdent extends CC_Base {

    @Override
    public boolean canDo() {
        System.out.println("Repell add ident ident");
        final String s1 = ((CIdent) this.getFirst()).getVar();
        final String s2 = ((CIdent) this.getSec()).getVar();
        return s1.equals(s2);
    }

    private boolean gleicheMin(final CElement el1, final CElement el2) {
        final boolean result1 = (el1.hasExtMinus() && el2.hasExtMinus());
        final boolean result2 = (!el1.hasExtMinus() && !el2.hasExtMinus());
        return result1 || result2;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Add Ident and Ident");
        CElement newChild = null;
        if (cE1.getCRolle() == CRolle.SUMMAND1) {
            if (cE2.hasExtMinus()) { // bilde Zahl 0
                newChild = CNum.createNum(parent.getElement(), "0");
                newChild.setCRolle(cE1.getCRolle());
            } else { // bilde TimesRow
                final CElement newFirst = CNum.createNum(parent.getElement(),
                        "2");
                newFirst.setCRolle(CRolle.FAKTOR1);
                final CElement newSecond = cE1.cloneCElement(false);
                newSecond.setCRolle(CRolle.FAKTORN1);
                newSecond.setPraefix("*");
                newChild = CTimesRow.createRow(CTimesRow.createList(newFirst,
                        newSecond));
                newChild.setCRolle(cE1.getCRolle());
            }
        } else {
            if (this.gleicheMin(cE1, cE2)) { // bilde TimesRow
                final CElement newFirst = CNum.createNum(parent.getElement(),
                        "2");
                newFirst.setCRolle(CRolle.FAKTOR1);
                final CElement newSecond = cE1.cloneCElement(false);
                newSecond.setCRolle(CRolle.FAKTORN1);
                newSecond.setPraefix("*");
                newChild = CTimesRow.createRow(CTimesRow.createList(newFirst,
                        newSecond));
                newChild.setCRolle(cE1.getCRolle());
            } else { // bilde 0
                newChild = CNum.createNum(parent.getElement(), "0");
                newChild.setCRolle(cE1.getCRolle());
            }
        }
        return newChild;
    }
}
