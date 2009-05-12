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
import cTree.CNum;
import cTree.cDefence.DefHandler;
import cViewer.ViewerFactory;

public class CC_ {

    /**
     * This should be overwriten by each Class.
     * 
     * @param parent
     * @param cE1
     * @param cE2
     * @return Defaults to always false
     */
    protected boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        return false;
    }

    /**
     * The Standardmethod with a Hook for createCombination
     * 
     * @param parent
     * @param cE1
     * @param cE2
     * @return the newly inserted CElement.
     */
    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        if (!this.canCombine(parent, cE1, cE2)) {
            return cE1;
        }
        final boolean replace = CombHandler.getInst().justTwo(cE1, cE2);
        final CElement newChild = this.createCombination(parent, cE1, cE2);
        if (newChild instanceof CNum
                || (newChild instanceof CMinTerm && ((CMinTerm) newChild)
                        .getValue() instanceof CNum)) {
            final String sol = newChild.getText();
            ViewerFactory.getInst().getDialog(sol);
        }
        return CombHandler.getInst().insertOrReplace(parent, newChild, cE1,
                cE2, replace);
    }

    /**
     * Methode only to be inserted into combine.
     * 
     * @param parent
     * @param cE1
     * @param cE2
     * @return the newly created Element
     */
    protected CElement createCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        return cE1;
    }

    /**
     * Methode to remove unnecessarily injected fences
     */
    protected void clean() {

    }

    /**
     * Trys to remove fences el if doit is true. Usualy doit is provided by
     * CMessage.
     * 
     * @param el
     * @param doIt
     */
    protected void condCleanOne(final CElement el, final boolean doIt) {
        final CElement par = el.getParent();
        final CElement child = el.getFirstChild();
        if (doIt && DefHandler.getInst().canDefence(par, el, child)) {
            DefHandler.getInst().defence(par, el, child);
        }
    }
}
