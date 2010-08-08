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
import cTree.CIdent;
import cTree.CNum;
import cTree.CPot;
import cTree.CRolle;
import cTree.adapter.EElementHelper;
import cTree.cDefence.CD_Event;

// wegen toggle noch gelassen

public class CC_PunktIdentPot extends CC_Base {

    @Override
    public boolean canDo() {
        final CPot cE2 = (CPot) this.getSec();
        final CIdent cE1 = (CIdent) this.getFirst();
        if ((cE2).getBasis() instanceof CIdent) {
            final CIdent cId = (CIdent) (cE2).getBasis();
            return (cE1).getVar().equals(cId.getVar());
        }
        return false;
    }

    private boolean gleicheDiv(final Element op1, final Element op2) {
        final boolean result = (EElementHelper.isTimesOp(op1) && EElementHelper
                .isTimesOp(op2));
        return result
                || (":".equals(op1.getTextContent()) && ":".equals(op2
                        .getTextContent()));
    }

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement parent = this.getParent();
        final CElement ident = this.getFirst();
        final CElement oldPot = this.getSec();
        System.out.println("Multipliziere Ident and Pot");
        final boolean replace = this.justTwo(ident, oldPot);
        CElement newChild = null;
        boolean toggle = false;
        // erster Faktor
        if (ident.getCRolle() == CRolle.FAKTOR1) {
            if (oldPot.hasExtDiv()) {
                return ident;
            } else {
                final int iExp = ((CNum) ((CPot) oldPot).getExponent())
                        .getValue();
                final CElement newBase = ident.cloneCElement(false); // parent.cloneChild(cE1,
                // false);
                final CElement newExp = CNum.createNum(parent.getElement(),
                        "" + (iExp + 1));
                newChild = CPot.createPot(newBase, newExp);
                newChild.setCRolle(ident.getCRolle());
            }
            // weitere Faktoren
        } else {
            if (this
                    .gleicheDiv(ident.getExtPraefix(), oldPot.getExtPraefix())) {
                final int iExp = ((CNum) ((CPot) oldPot).getExponent())
                        .getValue();
                final CElement newBase = ident.cloneCElement(false); // parent.cloneChild(cE1,
                // false);
                final CElement newExp = CNum.createNum(parent.getElement(),
                        "" + (iExp + 1));
                newChild = CPot.createPot(newBase, newExp);
                newChild.setCRolle(ident.getCRolle());
            } else {
                final int iExp = ((CNum) ((CPot) oldPot).getExponent())
                        .getValue();
                if (iExp > 0) {
                    toggle = true; // anderes Vorzeichen da sich das von der
                    // Potenz durchsetzt
                    final CElement newBase = ident.cloneCElement(false); // parent.cloneChild(cE1,
                    // false);
                    final CElement newExp = CNum.createNum(parent
                            .getElement(), "" + (iExp - 1));
                    newChild = CPot.createPot(newBase, newExp);
                    newChild.setCRolle(ident.getCRolle());
                } else {
                    return ident;
                }
            }
        }

        if (replace) {
            System.out.println("// replace");
            parent.getParent().replaceChild(newChild, parent, true, true);
        } else {
            System.out.println("// insert");
            if (toggle) {
                ident.toggleTimesDiv(false);
            }
            parent.replaceChild(newChild, ident, true, true);
            parent.removeChild(oldPot, true, true, false);
        }
        return newChild;
    }
}
