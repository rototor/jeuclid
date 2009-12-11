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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cTree.CElement;

public class EElementHelper {

    // Getter
    public static String getTyp(final Element e) {
        if (e.getAttribute("calcType") != null) {
            return e.getAttribute("calcTyp");
        } else {
            return "nix";
        }
    }

    public static int getNr(final Element e) {
        if (EElementHelper.isNr(e)) {
            return Integer.parseInt(e.getTextContent());
        } else {
            return 0;
        }
    }

    public static String getOp(final Element e) {
        if (EElementHelper.isOp(e)) {
            if (e.getFirstChild() != null
                    && e.getFirstChild().getAttributes() != null
                    && e.getFirstChild().getAttributes().getNamedItem("name") != null) {
                return e.getFirstChild().getAttributes().getNamedItem("name")
                        .getNodeValue();
            } else {
                return e.getTextContent();
            }
        } else {
            return "";
        }
    }

    // Erkennen der Operatoren
    public static boolean isInvTimes(final Element e) {
        if (e.getFirstChild() == null
                || e.getFirstChild().getAttributes() == null
                || e.getFirstChild().getAttributes().getNamedItem("name") == null) {
            return false;
        } else {
            return "InvisibleTimes".equals(e.getFirstChild().getAttributes()
                    .getNamedItem("name").getNodeValue());
        }
    }

    public static boolean isVisTimes(final Element e) {
        return e != null && "·".equals(e.getTextContent());
    }

    public static boolean isTimesOp(final Element e) {
        return EElementHelper.isVisTimes(e) || EElementHelper.isInvTimes(e);
    }

    public static boolean isDivOp(final Element e) {
        return (e != null && ":".equals(e.getTextContent()));
    }

    public static boolean isOp(final Element e) {
        return (e != null && "mo".equals(e.getNodeName()));
    }

    public static boolean isNr(final Element e) {
        return e != null && e.getNodeName().equals("mn");
    }

    public static boolean isInvPlus(final Element e) {
        if (e.getFirstChild() == null
                || e.getFirstChild().getAttributes() == null
                || e.getFirstChild().getAttributes().getNamedItem("name") == null) {
            return false;
        } else {
            return "InvisiblePlus".equals(e.getFirstChild().getAttributes()
                    .getNamedItem("name").getNodeValue());
        }
    }

    public static boolean isStrichOp(final Element e) {
        return e != null
                && ("-".equals(e.getTextContent()) || "+".equals(e
                        .getTextContent()));
    }

    public static boolean isId(final Element e) {
        return "mi".equals(e.getNodeName());
    }

    public static boolean isSup(final Element e) {
        return "msup".equals(e.getNodeName());
    }

    public static boolean isRow(final Element e) {
        return "mrow".equals(e.getNodeName());
    }

    public static boolean isFencedPair(final Element e) {
        return "mfenced".equals(e.getNodeName());
    }

    public static boolean isFrac(final Element e) {
        return "mfrac".equals(e.getNodeName());
    }

    // erlaubte Typen +*-: null
    public static Element createOp(final Element producer, final String typ) {
        Element op = null;
        if (!"".equals(typ)) {
            final Document doc = producer.getOwnerDocument();
            if ("ip".equals(typ)) {
                op = doc.createElement("mo");
                final Element op2 = doc.createElement("mchar");
                op2.setAttribute("name", "InvisiblePlus");
                op.appendChild(op2);
            } else {
                op = doc.createElement("mo");
                op.setTextContent(typ);
                if ("*".equals(typ)) {
                    op.setTextContent("·");
                }
            }
            op.setAttribute("calcTyp", "mo");
        }
        return op;
    }

    public static void setDots(Node d) {
        if (d != null) {
            if (d instanceof Element
                    && EElementHelper.isInvTimes((Element) d)
                    && d.getParentNode() != null) {
                final Element n = (Element) d.getNextSibling();
                final Element p = (Element) d.getPreviousSibling();
                final Element par = (Element) d.getParentNode();
                if (n != null
                        && p != null
                        && (EElementHelper.isNr(n) || EElementHelper.isRow(n)
                                || EElementHelper.isFencedPair(n)
                                || EElementHelper.isFencedPair(p)
                                || EElementHelper.isFrac(n) || EElementHelper
                                .isFrac(p)) && d.getParentNode() != null) {
                    final CElement cEl = DOMElementMap.getInstance().getCElement
                            .get(n);
                    final Node newNode = d.getOwnerDocument().createElement(
                            "mo");
                    newNode.setTextContent("·");
                    par.insertBefore(newNode, d);
                    if (cEl != null && cEl.getExtPraefix().equals(d)) {
                        cEl.setExtPraefix((Element) newNode);
                    }
                    par.removeChild(d);
                    d = newNode;
                }
            }
            if (d instanceof Element
                    && EElementHelper.isVisTimes((Element) d)) {
                final Element n = (Element) d.getNextSibling();
                final Element p = (Element) d.getPreviousSibling();
                final Element par = (Element) d.getParentNode();
                if (n != null
                        && !(EElementHelper.isNr(n)
                                || EElementHelper.isRow(n)
                                || EElementHelper.isFencedPair(n)
                                || EElementHelper.isFencedPair(p)
                                || EElementHelper.isFrac(n) || EElementHelper
                                .isFrac(p)) && d.getParentNode() != null) {
                    final CElement cEl = DOMElementMap.getInstance().getCElement
                            .get(n);
                    final Element newNode = d.getOwnerDocument()
                            .createElement("mo");
                    final Element new2 = d.getOwnerDocument().createElement(
                            "mchar");
                    new2.setAttribute("name", "InvisibleTimes");
                    newNode.appendChild(new2);
                    par.insertBefore(newNode, d);
                    if (cEl != null && cEl.getExtPraefix().equals(d)) {
                        cEl.setExtPraefix(newNode);
                    }
                    par.removeChild(d);
                    d = newNode;
                }
            }
            if (d.hasChildNodes()) {
                EElementHelper.setDots(d.getFirstChild());
            }
            EElementHelper.setDots(d.getNextSibling());
        }
    }

    public static Element createInvPlus(final CElement ownerEl) {
        final Element nextEl = ownerEl.getElement();
        final Element newNode = nextEl.getOwnerDocument().createElement("mo");
        final Element new2 = nextEl.getOwnerDocument().createElement("mchar");
        new2.setAttribute("name", "InvisiblePlus");
        newNode.appendChild(new2);
        // nextEl.getParentNode().insertBefore(newNode, nextEl);
        // ownerEl.setExtPraefix(newNode);
        return newNode;
    }
}
