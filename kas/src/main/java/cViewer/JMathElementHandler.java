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

package cViewer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JMathElementHandler {

    public static String testMathMLString(final String s) {
        return JMathElementHandler.removeSpaces(s);
    }

    private static String removeSpaces(final String s) {
        return s.replace("> ", ">");
    }

    public static boolean isNr(final Element e) {
        return e.getNodeName().equals("mn");
    }

    public static int getNr(final Element e) {
        if (JMathElementHandler.isNr(e)) {
            return Integer.parseInt(e.getTextContent());
        } else {
            return 0;
        }
    }

    public static boolean isOp(final Element e) {
        return "mo".equals(e.getNodeName());
    }

    public static boolean isTimesOp(final Element e) {
        return JMathElementHandler.isVisTimes(e)
                || JMathElementHandler.isInvTimes(e);
    }

    public static boolean isStrichOp(final Element e) {
        return "-".equals(e.getTextContent())
                || "+".equals(e.getTextContent());
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

    public static boolean isVisTimes(final Element e) {
        return "·".equals(e.getTextContent());
    }

    public static String getOp(final Element e) {
        if (JMathElementHandler.isOp(e)) {
            if (e.getFirstChild() != null
                    && e.getFirstChild().getAttributes() != null
                    && e.getFirstChild().getAttributes().getNamedItem("name") != null) {
                return e.getFirstChild().getAttributes().getNamedItem("name")
                        .getNodeValue();
            } else {
                return e.getTextContent();
            }
        } else {
            return null;
        }
    }

    public static void removeCalcTyp(final Node aNode) {
        if (aNode != null && aNode instanceof Element) {
            ((Element) aNode).removeAttribute("calcTyp");
            ((Element) aNode).removeAttribute("mathbackground");
            ((Element) aNode).removeAttribute("mathcolor");
        }
        if (aNode.hasChildNodes()) {
            JMathElementHandler.removeCalcTyp(aNode.getFirstChild());
        }
        if (aNode.getNextSibling() != null) {
            JMathElementHandler.removeCalcTyp(aNode.getNextSibling());
        }
    }

    // funktioniert nur für einige "saubere" Doms, d.h. pro Row entweder
    // PlusRow oder TimesRow oder Equ oder MinTerm oder mmixed
    public static void parseDom(final Node aNode) {
        if (aNode != null) {
            // try to set Infos
            if (aNode instanceof Element) {
                final Element el = (Element) aNode;
                if (JMathElementHandler.isRow(el)) {
                    final String result = JMathElementHandler.getRowTyp(el);
                    if (!"".equals(result)) {
                        el.setAttribute("calcTyp", result);
                    } else {
                        System.out.println("JMathElHandler no RowTyp found "
                                + el.getTextContent());
                    }
                } else {
                    el.setAttribute("calcTyp", el.getNodeName());
                }
            } else {
                // No Element
            }
        }
        if (aNode.hasChildNodes()) {
            JMathElementHandler.parseDom(aNode.getFirstChild());
        }
        if (aNode.getNextSibling() != null) {
            JMathElementHandler.parseDom(aNode.getNextSibling());
        }
    }

    private static String getRowTyp(final Element aRow) {
        String result = "";

        if (aRow.getFirstChild() != null
                && aRow.getFirstChild() instanceof Element) {
            final Element e1 = (Element) aRow.getFirstChild();
            // geht es mit einem Operator los?
            if (JMathElementHandler.isOp(e1)) {
                // dieser darf nur Minus sein
                if ("-".equals(e1.getTextContent())) {
                    // ein minTerm darf nur noch einen Sibling haben
                    if (e1.getNextSibling() != null
                            && e1.getNextSibling() instanceof Element
                            && e1.getNextSibling().getNextSibling() == null) {
                        return "vzterm";
                        // nach - und einem Element geht es noch weiter,
                        // Fehler
                    } else if (e1.getNextSibling() != null
                            && e1.getNextSibling() instanceof Element) {
                        System.out
                                .println("Fehler nach vz Minus mehr als ein Ausdruck");
                        return "unknown";
                    } else {
                        System.out
                                .println("Fehler nach vz Minus kein Element");
                        return "unknown";
                    }
                    // vz anders als Minus
                } else if ("+".equals(e1.getTextContent())) {
                    if (e1.getNextSibling() != null
                            && e1.getNextSibling() instanceof Element
                            && e1.getNextSibling().getNextSibling() == null) {
                        return "pterm";
                        // nach - und einem Element geht es noch weiter,
                        // Fehler
                    } else {
                        System.out
                                .println("Fehler nach vz Minus kein Element");
                        return "unknown";
                    }
                } else {
                    System.out.println("Fehler nach vz das nicht minus ist");
                    return "unknown";
                }
                // nun geht es nicht mit Operator los.
            } else {
                if (e1.getNextSibling() != null
                        && e1.getNextSibling() instanceof Element) {
                    final Element e2 = (Element) e1.getNextSibling();
                    if (JMathElementHandler.isOp(e2)) {
                        // Infix-Operatoren können = * : + - sein
                        result = JMathElementHandler.getOp(e2);
                    } else if (e2.getNextSibling() != null
                            && e2.getNextSibling() instanceof Element) {
                        result = JMathElementHandler.getOp((Element) e2
                                .getNextSibling());
                        System.out
                                .println("RowTyp bei e3 gefunden. Wann ist das?");
                    }
                    if ("·".equals(result) || "×".equals(result)
                            || "InvisibleTimes".equals(result)
                            || ":".equals(result)) {
                        // System.out.println("parsing dom getRowTyp *");
                        result = "*";
                    } else if ("+".equals(result) || "-".equals(result)) {
                        // System.out.println("parsing dom getRowTyp +");
                        result = "+";
                    } else if ("InvisiblePlus".equals(result)) {
                        System.out.println("parsing dom getRowTyp mmixed");
                        result = "mmixed";
                    } else {
                        // System.out.println("parsing dom getRowTyp " +
                        // result);
                    }
                } else { // nur ein Element, kein Operator
                    result = e1.getNodeName();
                    System.out.println("parsing dom - getRowTyp else");
                }
            }
        } else { // Row ist leer
            System.out.println("JMathElementHandler.getRowTyp: leere Row");
            result = "empty";
        }
        return result;
    }

    public static String getTyp(final Element el) {
        if (el.getAttribute("CalcType") != null) {
            return el.getAttribute("calcTyp");
        } else {
            return "nix";
        }
    }

    public static boolean isIn(final Node smaller, final Node bigger) {
        final boolean result = false;
        Node test = smaller;
        while (test.getParentNode() != null) {
            if (test.equals(bigger)) {
                return true;
            } else {
                test = test.getParentNode();
            }
        }
        return result;
    }
}
