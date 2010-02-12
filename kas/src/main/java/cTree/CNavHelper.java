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

package cTree;

import org.w3c.dom.Element;

import cTree.adapter.DOMElementMap;

public class CNavHelper {

    // Navigation im CTree
    public static CNavType getNavType(final CElement c) {
        if (c instanceof CTimesRow) {
            return CNavType.NEXTNEXT;
        }
        if (c instanceof CPlusRow) {
            return CNavType.NEXTNEXT;
        }
        if (c instanceof CMixedNumber) {
            return CNavType.NEXTNEXT;
        }
        if (c instanceof CEqu) {
            return CNavType.NEXTNEXT;
        }
        if (c instanceof CPot) {
            return CNavType.NEXT;
        }
        if (c instanceof CFrac) {
            return CNavType.NEXT;
        }
        if (c instanceof CSqrt) {
            return CNavType.NEXT;
        }
        return CNavType.NOSIBLING;
    }

    public static CElement chooseElement(final CElement oldE,
            final CElement newE) {
        if (oldE != null) {
            if (newE != null) {
                oldE.removeCActiveProperty();
                newE.setCActiveProperty();
                return newE;
            } else {
                return oldE;
            }
        } else {
            System.out.println("notFound");
        }
        return null;
    }

    public static boolean hasNextC(final CNavType type, final Element element) {
        if (type == CNavType.NOSIBLING) {
            return false;
        } else if (type == CNavType.NEXT) {
            return element != null
                    && element.getNextSibling() != null
                    && DOMElementMap.getInstance().getCElement.get(element
                            .getNextSibling()) != null;
        } else {
            return element != null
                    && element.getNextSibling() != null
                    && element.getNextSibling().getNextSibling() != null
                    && DOMElementMap.getInstance().getCElement.get(element
                            .getNextSibling().getNextSibling()) != null;
        }
    }

    public static boolean hasPrevC(final CNavType type, final Element element) {
        if (type == CNavType.NOSIBLING) {
            return false;
        } else if (type == CNavType.NEXT) {
            return element != null
                    && element.getPreviousSibling() != null
                    && DOMElementMap.getInstance().getCElement.get(element
                            .getPreviousSibling()) != null;
        } else {
            return element != null
                    && element.getPreviousSibling() != null
                    && element.getPreviousSibling().getPreviousSibling() != null
                    && DOMElementMap.getInstance().getCElement.get(element
                            .getPreviousSibling().getPreviousSibling()) != null;
        }
    }

    public static CElement getPrevC(final CNavType type, final Element element) {
        if (type == CNavType.NOSIBLING) {
            return null;
        } else if (type == CNavType.NEXT) {
            return DOMElementMap.getInstance().getCElement.get(element
                    .getPreviousSibling());
        } else {
            return DOMElementMap.getInstance().getCElement.get(element
                    .getPreviousSibling().getPreviousSibling());
        }
    }

    public static CElement getNextC(final CNavType type, final Element element) {
        if (type == CNavType.NOSIBLING) {
            return null;
        } else if (type == CNavType.NEXT) {
            return DOMElementMap.getInstance().getCElement.get(element
                    .getNextSibling());
        } else {
            return DOMElementMap.getInstance().getCElement.get(element
                    .getNextSibling().getNextSibling());
        }
    }
}
