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

import org.w3c.dom.Element;

import cTree.CElement;
import cTree.CNum;
import cTree.CPot;
import cTree.CRolle;
import cTree.CTimesRow;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;
import cTree.adapter.EElementHelper;
import cTree.cDefence.CD_Event;

public class CC_PunktIdentIdent extends CC_Base {

    @Override
    public C_Changer getChanger(final C_Event e) {
        this.setEvent(e);
        if (this.canDo()) {
            return this;
        } else {
            return new C_No(e);
        }
    }

    public CElement create(final CElement producer, final CElement first,
            final CElement second) {
        System.out.println("Create TR Ident Ident");
        final CElement newTR = CTimesRow.createRow(CTimesRow.createList(
                first, second));
        return newTR;
    }

    private boolean gleicheDiv(final Element op1, final Element op2) {
        final boolean result = (EElementHelper.isTimesOp(op1) && EElementHelper
                .isTimesOp(op2));
        return result
                || (":".equals(op1.getTextContent()) && ":".equals(op2
                        .getTextContent()));
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, CD_Event cDEvent) {
        System.out.println("Multipliziere Idents");
        CElement newChild = null;
        if (cE1.getCRolle() == CRolle.FAKTOR1) {
            System.out.println("// falls ein Faktor1 dabei ist");
            if (cE2.hasExtDiv()) {
                newChild = CNum.createNum(parent.getElement(), "" + 1);
                // Operation ist mal
            } else {
                final CElement newBase = cE1.cloneCElement(false); // parent.cloneChild(cE1,
                // false);
                final CElement newExp = CNum.createNum(parent.getElement(),
                        "2");
                newChild = CPot.createPot(newBase, newExp);
                newChild.setCRolle(cE1.getCRolle());
            }
            // weitere Faktoren
        } else {
            System.out.println("// falls weitere Faktoren");
            if (this.gleicheDiv(cE1.getExtPraefix(), cE2.getExtPraefix())) {
                final CElement newBase = cE1.cloneCElement(false); // parent.cloneChild(cE1,
                // false);
                final CElement newExp = CNum.createNum(parent.getElement(),
                        "2");
                newChild = CPot.createPot(newBase, newExp);
                newChild.setCRolle(cE1.getCRolle());
            } else {
                newChild = CNum.createNum(parent.getElement(), "" + 1);
            }
        }
        return newChild;
    }

    @Override
    public boolean canDo() {
        final String s1 = this.getFirst().getText();
        final String s2 = this.getSec().getText();
        return s1.equals(s2);
    }
}
