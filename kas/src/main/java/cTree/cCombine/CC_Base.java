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
import cTree.CMinTerm;
import cTree.CMixedNumber;
import cTree.CNum;
import cTree.adapter.C_Changer;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;
import cViewer.ViewerFactory;

public abstract class CC_Base extends C_Changer {

    /**
     * The standardmethod with a Hook for createCombination
     */
    @Override
    public CElement doIt(final CD_Event message) {
        final CElement p = this.getParent();
        final CElement cE1 = this.getFirst();
        final CElement cE2 = this.getSec();
        final boolean replace = this.justTwo(cE1, cE2);
        final CD_Event cDEvent = new CD_Event(false);
        final CElement newChild = this.createComb(p, cE1, cE2, cDEvent);
        this.calcNum(newChild);
        this.calcMixed(newChild);
        CElement result = this
                .insertOrReplace(p, newChild, cE1, cE2, replace);
        if (cDEvent.isDoDef()) {
            System.out.println("CC_Base doDef");
            cDEvent.setCElement(result);
            result = DefHandler.getInst().getChanger(cDEvent).doIt(
                    new CD_Event(false));
        }
        return result;
    }

    private void calcNum(final CElement newChild) {
        if (newChild instanceof CNum
                || (newChild instanceof CMinTerm && ((CMinTerm) newChild)
                        .getValue() instanceof CNum)) {
            final String sol = newChild.getText();
            if (ViewerFactory.getInst().getMathFrame().getStateTransfer()
                    .getResult().charAt(0) != '-') {
                ViewerFactory.getInst().getInputDialog(sol);
            }
        }
    }

    private void calcMixed(final CElement newChild) {
        if (newChild instanceof CMixedNumber
                || (newChild instanceof CMinTerm && ((CMinTerm) newChild)
                        .getValue() instanceof CMixedNumber)) {
            final String sol = newChild.getText();
            if (ViewerFactory.getInst().getMathFrame().getStateTransfer()
                    .getResult().charAt(1) != '-') {
                ViewerFactory.getInst().getInputDialog(sol);
            }
        }
    }

    /**
     * Methode only to be inserted into combine.
     * 
     * @param parent
     * @param cE1
     * @param cE2
     * @param cDEvent
     *            TODO
     * @return the newly created Element
     */
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2, final CD_Event cDEvent) {
        return cE1;
    }

    /**
     * Methode to remove unnecessarily injected fences
     */
    protected void clean() {

    }

    /**
     * Trys to remove fences el if doit is true. Usually doit is provided by
     * CMessage.
     * 
     * @param el
     * @param doIt
     */

    protected CElement condCleanOne(final CElement el, final boolean doIt) {
        final CD_Event e = new CD_Event(el);
        final C_Changer c = DefHandler.getInst().getChanger(e);
        if (doIt && c.canDo()) {
            return c.doIt(null);
        } else {
            return el;
        }
    }
}
