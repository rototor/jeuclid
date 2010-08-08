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

package cTree.adapter;

import org.w3c.dom.Element;

import cTree.CElement;
import cTree.CElementHelper;
import cTree.CRolle;

public abstract class CTreeMutator extends CTreeWalker {

    // Support für die DOM Manipulation
    public CElement appendChild(final CElement newChild) {
        // CElementHelper.removeSupport_Rollenanpassung(newChild);
        if (newChild.getExtPraefix() != null) {
            this.getElement().appendChild(newChild.getExtPraefix());
        }
        final Element e = (Element) this.getElement().appendChild(
                newChild.getElement());
        return DOMElementMap.getInstance().getCElement.get(e);
    }

    public CElement appendPraefixAndChild(final CElement newChild) {
        // CElementHelper.removeSupport_Rollenanpassung(newChild);
        if (newChild.getExtPraefix() != null) {
            this.getElement().appendChild(newChild.getExtPraefix());
        }
        final Element e = (Element) this.getElement().appendChild(
                newChild.getElement());
        return DOMElementMap.getInstance().getCElement.get(e);
    }

    public CElement insertBefore(final CElement newChild,
            final CElement refChild) {
        Element refElement = refChild.getElement();
        if (refChild.getExtPraefix() != null) {
            refElement = refChild.getExtPraefix();
        }
        CTreeMutator.removeSupport_RollenanpassungNext(newChild);
        if (newChild.getExtPraefix() != null) {
            this.getElement().insertBefore(newChild.getExtPraefix(),
                    refElement);
        }
        final Element e = (Element) this.getElement().insertBefore(
                newChild.getElement(), refElement);
        return DOMElementMap.getInstance().getCElement.get(e);
    }

    public static void removeSupport_RollenanpassungNext(
            final CElement toRemove) {
        if (toRemove != null && toRemove.getCRolle() == CRolle.FAKTOR1
                && toRemove.hasNextC()) {
            toRemove.getNextSibling().setCRolle(CRolle.FAKTOR1);
        }
        if (toRemove != null && toRemove.getCRolle() == CRolle.SUMMAND1
                && toRemove.hasNextC()) {
            toRemove.getNextSibling().setCRolle(CRolle.SUMMAND1);
        }
    }

    // --- Mutatoren ---------------------------------------

    public CElement cloneCElement(final boolean withPraefix) {
        final Element e = (Element) this.getElement().cloneNode(true);
        // System.out.println("Clone has no parent? " +
        // (e.getParentNode()==null));
        final CElement cE2 = CElementHelper.buildCFromENoPraefixSet(e,
                ((CElement) this).getCRolle(), false);
        if (this.getExtPraefix() != null && withPraefix) {
            final Element p = (Element) this.getExtPraefix().cloneNode(true);
            cE2.setExtPraefix(p);
        }
        return cE2;
    }

    public CElement removeChild(final CElement e,
            final boolean correctNextRolle, final boolean unregister,
            final boolean withNormalParent) {
        // keine allgemeine Korrektur
        // evtl. Praefix aus DOM entfernen
        if (e.getExtPraefix() != null) {
            this.getElement().removeChild(e.getExtPraefix());
        }
        // Element entfernen
        this.getElement().removeChild(e.getElement());
        // evtl. Element -> CElement entfernen
        if (unregister) {
            DOMElementMap.getInstance().getCElement.remove(e);
        }
        if (withNormalParent && (this.getParent() != null)) {
            this.getParent().normalize();
        }
        return e;
    }

    public CElement replaceChild(final CElement newChild,
            final CElement oldChild, final boolean setOldRolle,
            final boolean setOldPraefix) {
        final CRolle saveOldRolle = oldChild.getCRolle();
        final Element saveOldPraefix = oldChild.getExtPraefix();
        if (oldChild.isActiveC()) {
            oldChild.removeCActiveProperty();
            newChild.setCActiveProperty();
        }
        this.insertBefore(newChild, oldChild);
        this.removeChild(oldChild, false, true, false);
        if (setOldRolle) {
            newChild.setCRolle(saveOldRolle);
        }
        if (setOldPraefix) {
            newChild.setExtPraefix(saveOldPraefix);
            if (oldChild.getExtPraefix() != null) {
                this.getElement().insertBefore(oldChild.getExtPraefix(),
                        newChild.getElement());
            }
        }
        return newChild;
    }

    public abstract void normalize();

    public void normalizeAll() {
        // CElement root sollte math sein
        final CElement root = DOMElementMap.getInstance().getCElement
                .get((this.getElement().getOwnerDocument().getFirstChild()));
        if (root.hasChildC()) {
            root.getFirstChild().normalizeTreeAndSiblings();
        }
    };

    public void normalizeTreeAndSiblings() {
        if (this.hasChildC()) {
            this.getFirstChild().normalizeTreeAndSiblings();
        }
        final CElement reserve = this.getNextSibling();
        System.out.println("Normalize " + this.getText() + " "
                + this.getElement().getNodeName());
        this.normalize();
        if (reserve != null) {
            reserve.normalizeTreeAndSiblings();
        }
    }

    // Support für die Verschiebung in dem CTree
    public CElement moveRight(final CElement active) {
        return active;
    }

    public CElement moveLeft(final CElement active) {
        return active;
    }

    // Support für die Verschiebung in dem CTree
    public CElement moveRight(final int i, final CElement active) {
        return active;
    }

    public CElement moveLeft(final int i, final CElement active) {
        return active;
    }

}
