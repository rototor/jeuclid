/*
 * Copyright 2009 Erhard Kuenzel 03/09
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
import cTree.adapter.EElementHelper;
import cTree.cDefence.CD_Event;

public class CC_PunktPotPot extends CC_Base {

    @Override
    public boolean canDo() {
        System.out.println("Repell pot times pot?");
        final CPot pot1 = (CPot) this.getFirst();
        final CPot pot2 = (CPot) this.getSec();
        final int exp1 = ((CNum) (pot1).getExponent()).getValue();
        final int exp2 = ((CNum) (pot2).getExponent()).getValue();
        if (!pot1.getBasis().getText().equals(pot2.getBasis().getText())) {
            return false;
        }
        if (pot1.getCRolle() == CRolle.FAKTOR1 && pot2.hasExtDiv()
                && (exp2 > exp1)) {
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

    @Override
    protected CElement createComb(final CElement parent,
            final CElement firstPot, final CElement secondPot, CD_Event cDEvent) {
        CElement newChild = null;
        final int exp1 = ((CNum) ((CPot) firstPot).getExponent()).getValue();
        final int exp2 = ((CNum) ((CPot) secondPot).getExponent()).getValue();
        // erster Faktor
        if (firstPot.getCRolle() == CRolle.FAKTOR1) {
            if (secondPot.hasExtDiv() && (exp2 > exp1)) {
                System.out.println("Should not happen");
                return firstPot;
            } else {
                final CPot newPot = (CPot) firstPot.cloneCElement(true); // parent.cloneChild(firstPot,
                // true);
                int newExp = exp1 + exp2;
                if (secondPot.hasExtDiv()) {
                    newExp = exp1 - exp2;
                }
                newPot.getExponent().setText("" + newExp);
                newChild = newPot;
            }
            // weitere Faktoren
        } else {
            if (this.gleicheDiv(firstPot.getExtPraefix(), secondPot
                    .getExtPraefix())) {
                final CPot newPot = (CPot) firstPot.cloneCElement(false); // parent.cloneChild(firstPot,
                // false);
                final int newExp = exp1 + exp2;
                newPot.getExponent().setText("" + newExp);
                newChild = newPot;
            } else {
                if (exp1 >= exp2) {
                    final CPot newPot = (CPot) firstPot.cloneCElement(false); // parent.cloneChild(firstPot,
                    // false);
                    final int newExp = exp1 - exp2;
                    newPot.getExponent().setText("" + newExp);
                    newChild = newPot;
                } else {
                    // Element op =
                    // EElementHelper.createOp(parent.getElement(), ":");
                    final CPot newPot = (CPot) firstPot.cloneCElement(false); // parent.cloneChild(firstPot,
                    // false);
                    final int newExp = exp2 - exp1;
                    newPot.getExponent().setText("" + newExp);
                    // newPot.setExtPraefix(op);
                    newChild = newPot;
                }
            }
        }
        return newChild;
    }

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement parent = this.getParent();
        final CElement cE1 = this.getFirst();
        final CElement cE2 = cE1.getNextSibling();
        final boolean replace = this.justTwo(cE1, cE2);
        // evtl muss man das Zeichen vor cE1 ändern
        final int exp1 = ((CNum) ((CPot) cE1).getExponent()).getValue();
        final int exp2 = ((CNum) ((CPot) cE2).getExponent()).getValue();
        boolean toggle = (cE1.hasExtDiv() && !cE2.hasExtDiv() && (exp2 > exp1));
        toggle = toggle
                || (!cE1.hasExtDiv() && cE2.hasExtDiv() && (exp2 > exp1));
        System.out.println("Toggle?" + toggle);
        final CElement newChild = this.createComb(parent, cE1, cE2, null);
        this.insertOrReplace(parent, newChild, cE1, cE2, replace);
        if (toggle) {
            newChild.toggleTimesDiv(false);
        }
        return newChild;
    }
}
