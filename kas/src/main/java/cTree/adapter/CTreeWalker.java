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
import cTree.CNavHelper;

public abstract class CTreeWalker extends ActivityAdapter {

    // boolesche Tester für Navigation ---------------------------------------
    @Override
    public boolean hasParent() {
        return this.element != null
                && this.element.getParentNode() != null
                && DOMElementMap.getInstance().getCElement.get(this.element
                        .getParentNode()) != null;
    }

    public boolean hasChildC() {
        if (this.element != null && this.element.getFirstChild() != null
                && (this.element.getFirstChild() instanceof Element)) {
            final Element firstTest = (Element) this.element.getFirstChild();
            if ("mo".equals(firstTest.getNodeName())) {
                return DOMElementMap.getInstance().getCElement.get(firstTest
                        .getNextSibling()) != null;
            } else {
                return DOMElementMap.getInstance().getCElement.get(firstTest) != null;
            }
        } else {
            return false;
        }
    }

    public boolean hasNextC() {
        if (this.hasParent()) {
            return CNavHelper.hasNextC(CNavHelper
                    .getNavType(this.getParent()), this.element);
        } else {
            return false;
        }
    }

    public boolean hasPrevC() {
        if (this.hasParent()) {
            return CNavHelper.hasPrevC(CNavHelper
                    .getNavType(this.getParent()), this.element);
        } else {
            return false;
        }
    }

    // Getter sollten nur nach hasParent hasPrevC ... aufgerufen werden.
    public CElement getParent() {
        return DOMElementMap.getInstance().getCElement.get(this.element
                .getParentNode());
    }

    public CElement getNextSibling() {
        if (this.hasNextC()) {
            return CNavHelper.getNextC(CNavHelper
                    .getNavType(this.getParent()), this.element);
        } else {
            return null;
        }
    }

    public CElement getPrevSibling() {
        return CNavHelper.getPrevC(CNavHelper.getNavType(this.getParent()),
                this.element);
    }

    public CElement getFirstChild() {
        if (this.element.getFirstChild() == null
                || !(this.element.getFirstChild() instanceof Element)) {
            return null;
        } else {
            final Element firstTest = (Element) this.element.getFirstChild();
            if ("mo".equals(firstTest.getNodeName())) {
                return DOMElementMap.getInstance().getCElement.get(firstTest
                        .getNextSibling());
            } else {
                return DOMElementMap.getInstance().getCElement.get(firstTest);
            }
        }
    }

    public CElement getLastChild() {
        if (this.element.getLastChild() == null
                || !(this.element.getLastChild() instanceof Element)) {
            return null;
        } else {
            final Element lastTest = (Element) this.element.getLastChild();
            return DOMElementMap.getInstance().getCElement.get(lastTest);
        }
    }

    // Support für die Auswahl im CTree
    // ---------------------------------------
    public CElement tryToSelectParent() {
        if (this.hasParent()) {
            final CElement bEl = this.getParent();
            this.removeCActiveProperty();
            bEl.setCActiveProperty();
            return bEl;
        } else {
            System.out.println("kein activer Node oder Parent oder Element");
        }
        return (CElement) this;
    }

    public CElement tryToSelectFirstChild() {
        if (this.hasChildC()) {
            final CElement bEl = this.getFirstChild();
            this.removeCActiveProperty();
            bEl.setCActiveProperty();
            return bEl;
        } else {
            System.out.println("kein active Node oder Child oder Element");
        }
        return (CElement) this;
    }

    public CElement tryToSelectLeft() {
        if (this.hasPrevC()) {
            final CElement bEl = this.getPrevSibling();
            this.removeCActiveProperty();
            bEl.setCActiveProperty();
            return bEl;
        } else {
            System.out.println("kein activer Node, left Node or Element");
        }
        return (CElement) this;
    }

    public CElement tryToSelectRight() {
        if (this.hasNextC()) {
            final CElement bEl = this.getNextSibling();
            this.removeCActiveProperty();
            bEl.setCActiveProperty();
            return bEl;
        } else {
            System.out.println("kein activer Node, left Node or Element");
        }
        return (CElement) this;
    }
}
