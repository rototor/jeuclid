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
import cTree.CRolle;
import cTree.adapter.EElementHelper;

public class CC_PunktNumNum extends CC_ {

    @Override
    protected CElement createCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Multipliziere Zahlen");
        final int wertE = Integer.parseInt(cE1.getElement().getTextContent());
        final int wertZ = Integer.parseInt(cE2.getElement().getTextContent());
        CElement newChild = null;

        if (cE1.getCRolle() == CRolle.FAKTOR1) {
            System.out.println("// falls ein Faktor1 dabei ist");
            if (cE2.hasExtDiv()) {
                // geht eine IntegerDivision?
                if ((wertE % wertZ) == 0) {
                    newChild = CNum.createNum(parent.getElement(), ""
                            + (wertE / wertZ));
                    newChild.setCRolle(cE1.getCRolle());
                }
                // Operation ist mal
            } else {
                newChild = CNum.createNum(parent.getElement(), ""
                        + (wertE * wertZ));
                newChild.setCRolle(cE1.getCRolle());
            }
            // weitere Faktoren
        } else {
            System.out.println("// falls weitere Faktoren");
            if (this.gleicheDiv(cE1.getExtPraefix(), cE2.getExtPraefix())) {
                newChild = CNum.createNum(parent.getElement(), ""
                        + (wertE * wertZ));
                newChild.setCRolle(cE1.getCRolle());
            } else {
                if ((wertE % wertZ) == 0) {
                    newChild = CNum.createNum(parent.getElement(), ""
                            + (wertE / wertZ));
                    newChild.setCRolle(cE1.getCRolle());
                } else if ((wertZ % wertE) == 0) {
                    newChild = CNum.createNum(parent.getElement(), ""
                            + (wertZ / wertE));
                    newChild.setCRolle(cE1.getCRolle());
                    cE1.toggleTimesDiv(false);
                }
            }
        }
        return newChild;
    }

    @Override
    protected boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("CombinerTypTNN can combine?");
        final int wertE = Integer.parseInt(cE1.getElement().getTextContent());
        final int wertZ = Integer.parseInt(cE2.getElement().getTextContent());
        if (cE1.getCRolle() == CRolle.FAKTOR1 && cE2.hasExtDiv()
                && (wertE % wertZ) != 0) {
            return false;
        }
        if (cE1.getCRolle() == CRolle.FAKTORN1
                && !this.gleicheDiv(cE1.getExtPraefix(), cE2.getExtPraefix())
                && (wertE % wertZ) != 0 && (wertZ % wertE) == 0) {
            return false;
        }
        return true;
    }

    private boolean gleicheDiv(final Element op1, final Element op2) {
        final boolean result = (EElementHelper.isTimesOp(op1) && EElementHelper
                .isTimesOp(op2));
        return result
                || (":".equals(op1.getTextContent()) && ":".equals(op2
                        .getTextContent()));
    }

}
