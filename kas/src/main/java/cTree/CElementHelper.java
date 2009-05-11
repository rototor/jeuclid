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
import org.w3c.dom.Node;

import cTree.adapter.DOMElementMap;
import cTree.adapter.EElementHelper;

public class CElementHelper {

    // mögliche Vorzeichen nach Rolle und nach Normalisierung
    public static boolean canHaveMinus(final CRolle cRolle) {
        return (cRolle == CRolle.NACHVZMINUS || cRolle == CRolle.SUMMANDN1 || cRolle == CRolle.FAKTORN1);
    }

    public static boolean canHaveDiv(final CRolle cRolle) {
        return (cRolle == CRolle.FAKTORN1);
    }

    // Erstellen eines CElements zB. "mn", CType.NUM, CRolle.SUMMANDN1, vz
    // ohne es in das Dokument zu integrieren
    protected static CElement createAll(final Element producer,
            final String mTyp, final String cTyp, final CRolle cRolle,
            final Element extVZ) {
        final Element el = producer.getOwnerDocument().createElement(mTyp);
        el.setAttribute("calcTyp", cTyp);
        final CElement cEl = CElementHelper.buildCFromENoPraefixSet(el,
                cRolle, false);
        cEl.setExtPraefix(extVZ);
        return cEl;
    }

    // Erstellen eines CElements zB. "mn", CType.NUM, CRolle.SUMMANDN1, vz
    // ohne es oder sein VZ in das Dokument zu integrieren
    protected static CElement createAl(final Element producer,
            final String mTyp, final String cTyp, final CRolle cRolle,
            final String extVZ) {
        final Element op = EElementHelper.createOp(producer, extVZ);
        return CElementHelper.createAll(producer, mTyp, cTyp, cRolle, op);
    }

    // Erstellt einen CTree aus einem ETree, trägt in die Map ein und setzt
    // Rolle
    // Bei den Kindern werden die Praefixe kontrolliert
    /*
     * @deprecated
     */
    public static CElement buildCFromENoPraefixSet(final Node aNode,
            final CRolle cRolle, final boolean withNextSiblings) {
        CElement cEl = null;
        CElementHelper.cCElement(aNode, withNextSiblings);
        if (aNode != null && aNode instanceof Element) {
            cEl = DOMElementMap.getInstance().getCElement.get(aNode);
            CElementHelper.setRolle(cRolle, cEl, withNextSiblings);
        }
        return cEl;
    }

    // Erstellen eines CTree aus einem ETree, setzte Rolle und aktualisiert
    /*
     * @deprecated
     */
    public static void buildCFromEPraefixAdjust(final Node aNode,
            final CRolle cRolle, final boolean withNextSiblings) {
        CElementHelper.cCElement(aNode, withNextSiblings);
        if (aNode != null && aNode instanceof Element) {
            final CElement cEl = DOMElementMap.getInstance().getCElement
                    .get(aNode);
            CElementHelper.setRolleAndPraefix(cRolle, cEl, withNextSiblings);
        }
    }

    // Erstellt zu einem Element-Tree den rohen CElement-Tree und aktualisiert
    // die DOMElementMap
    private static void cCElement(final Node aNode,
            final boolean withNextSiblings) {
        if (aNode != null) {
            // try to set Infos cRolle anfangs DOCCHILD
            if (aNode instanceof Element
                    && !EElementHelper.isOp((Element) aNode)) {
                final Element dEl = (Element) aNode;
                final CElement cEl = CElementHelper.creatRawCElement(dEl);
                DOMElementMap.getInstance().getCElement.put(dEl, cEl);
            } else {
                // No CElement
            }
        }
        if (aNode.hasChildNodes() && aNode instanceof Element
                && !EElementHelper.isOp((Element) aNode)) {
            CElementHelper.cCElement(aNode.getFirstChild(), true);
        }
        if (aNode.hasChildNodes() && !(aNode instanceof Element)) {
            CElementHelper.cCElement(aNode.getFirstChild(), true);
        }
        if (withNextSiblings && (aNode.getNextSibling() != null)) {
            CElementHelper.cCElement(aNode.getNextSibling(), true);
        }
    }

    // Erstellt zu einem Element das rohe CElement
    private static CElement creatRawCElement(final Element el) {
        final String name = el.getAttribute("calcTyp");
        if (name.equals("empty")) {
            return new CEmpty(el);
        }
        if (name.equals("=")) {
            return new CEqu(el);
        }
        if (name.equals("mfrac")) {
            return new CFrac(el);
        }
        if (name.equals("mi")) {
            return new CIdent(el);
        }
        if (name.equals("math")) {
            return new CMath(el);
        }
        if (name.equals("vzterm")) {
            return new CMinTerm(el);
        }
        if (name.equals("pterm")) {
            return new CPlusTerm(el);
        }
        if (name.equals("mmixed")) {
            return new CMixedNumber(el);
        }
        if (name.equals("mn")) {
            return new CNum(el);
        }
        if (name.equals("+")) {
            return new CPlusRow(el);
        }
        if (name.equals("msup")) {
            return new CPot(el);
        }
        if (name.equals("msqrt")) {
            return new CSqrt(el);
        }
        if (name.equals("*")) {
            return new CTimesRow(el);
        }
        if (name.equals("mfenced")) {
            return new CFences(el);
        }
        return null;
    }

    private static void setRolle(final CRolle cRolle,
            final CElement cElement, final boolean withNextSiblings) {
        cElement.setCRolle(cRolle);
        if (cElement.hasChildC()) {
            CElementHelper.setRolleAndPraefix(CElementHelper
                    .childRolle(cElement.getCType()), cElement
                    .getFirstChild(), true);
        }
        if (withNextSiblings && cElement.hasNextC()) {
            CElementHelper.setRolleAndPraefix(CElementHelper
                    .nextRolle(cRolle), cElement.getNextSibling(), true);
        }
    }

    private static void setRolleAndPraefix(final CRolle cRolle,
            final CElement cElement, final boolean withNextSiblings) {
        // das Vorzeichen vom previousElement in der Row holen
        if (CElementHelper.canHaveMinus(cRolle)
                && cElement.getElement().getPreviousSibling() != null
                && cElement.getElement().getPreviousSibling() instanceof Element) {
            final String s = ((Element) cElement.getElement()
                    .getPreviousSibling()).getTextContent();
            if ("+".equals(s) || "-".equals(s)) {
                cElement.setExtPraefix((Element) cElement.getElement()
                        .getPreviousSibling());
            }
        }
        if (CElementHelper.canHaveDiv(cRolle)
                && cElement.getElement().getPreviousSibling() != null
                && cElement.getElement().getPreviousSibling() instanceof Element) {
            cElement.setExtPraefix((Element) cElement.getElement()
                    .getPreviousSibling());
        }
        cElement.setCRolle(cRolle);
        if (cElement.hasChildC()) {
            CElementHelper.setRolleAndPraefix(CElementHelper
                    .childRolle(cElement.getCType()), cElement
                    .getFirstChild(), true);
        }
        if (withNextSiblings && cElement.hasNextC()) {
            CElementHelper.setRolleAndPraefix(CElementHelper
                    .nextRolle(cRolle), cElement.getNextSibling(), true);
        }
    }

    // Sucht nächste Rolle, etwa Basis -> Exponent
    protected static CRolle nextRolle(final CRolle cRolle) {
        if (cRolle == CRolle.BASIS) {
            return CRolle.EXPONENT;
        }
        if (cRolle == CRolle.ZAEHLER) {
            return CRolle.NENNER;
        }
        if (cRolle == CRolle.FAKTOR1 || cRolle == CRolle.FAKTORN1) {
            return CRolle.FAKTORN1;
        }
        if (cRolle == CRolle.LINKESEITE) {
            return CRolle.RECHTESEITE;
        }
        if (cRolle == CRolle.SUMMAND1 || cRolle == CRolle.SUMMANDN1) {
            return CRolle.SUMMANDN1;
        }
        if (cRolle == CRolle.WHOLENUMBER) {
            return CRolle.FRACTION;
        }
        return CRolle.UNKNOWN;
    }

    // Sucht Rolle des Kindes, etwa Pot -> Basis
    protected static CRolle childRolle(final CType cType) {
        if (cType == CType.EQU) {
            return CRolle.LINKESEITE;
        }
        if (cType == CType.FENCES) {
            return CRolle.GEKLAMMERT;
        }
        if (cType == CType.FRAC) {
            return CRolle.ZAEHLER;
        }
        if (cType == CType.MIXEDN) {
            return CRolle.WHOLENUMBER;
        }
        if (cType == CType.MATH) {
            return CRolle.MATHCHILD;
        }
        if (cType == CType.MINROW) {
            return CRolle.NACHVZMINUS;
        }
        if (cType == CType.PLUSTERM) {
            return CRolle.NACHVZPLUS;
        }
        if (cType == CType.PLUSROW) {
            return CRolle.SUMMAND1;
        }
        if (cType == CType.POT) {
            return CRolle.BASIS;
        }
        if (cType == CType.SQRT) {
            return CRolle.RADIKANT;
        }
        if (cType == CType.TIMESROW) {
            return CRolle.FAKTOR1;
        }
        if (cType == CType.UNKNOWN) {
            return CRolle.UNKNOWN;
        }
        return CRolle.UNKNOWN;
    }

    public static void controlMath(final Element math) {
        if (math != null) {
            System.out.println("Kontrolliere Rollen");
            // kontrolliere ob es ein zugehöriges CElement gibt:
            if (!DOMElementMap.getInstance().getCElement.containsKey(math)) {
                System.out.println("Ein Element ohne CElement gefunden math");
            } else {
                final CType myTyp = DOMElementMap.getInstance().getCElement
                        .get(math).getCType();
                CElementHelper.control(math.getFirstChild(), CElementHelper
                        .childRolle(myTyp));
            }
            System.out.println("Rollenfehlerkontrolle abgeschlossen");
        }
    }

    private static void control(final Node d, final CRolle expectedRolle) {
        if (d != null) {
            if (d instanceof Element) {
                if ("mo".equals(d.getNodeName())) {
                    CElementHelper.control(d.getNextSibling(), expectedRolle);
                } else {
                    final CElement cEl = CElementHelper.controlElement(
                            ((Element) d), expectedRolle);
                    if (cEl != null) {
                        if (d.hasChildNodes()
                                && (d.getFirstChild() instanceof Element)) {
                            CElementHelper
                                    .control(d.getFirstChild(),
                                            CElementHelper.childRolle(cEl
                                                    .getCType()));
                        }
                        CElementHelper.control(d.getNextSibling(),
                                CElementHelper.nextRolle(cEl.getCRolle()));
                    }
                }
            } else {
                System.out.println("*keinElement" + d.getNodeType()
                        + d.getNodeValue());
            }
        }
    }

    private static CElement controlElement(final Element e,
            final CRolle expectedRolle) {
        CElement result = null;
        // Fehlerquelle DOM-Element, das kein Operator ist, aber kein Pendant
        // im CTree hat
        if (!cTree.adapter.DOMElementMap.getInstance().getCElement
                .containsKey(e)) {
            System.out.println("Ein Element ohne CElement gefunden"
                    + e.getTextContent());
        } else {
            result = cTree.adapter.DOMElementMap.getInstance().getCElement
                    .get(e);
            // Fehlerquelle in der Rollenverteilung
            if (result.getCRolle() != expectedRolle) {
                System.out.println("Fehler mit der Rollenverteilung"
                        + result.getCRolle() + " vs " + expectedRolle);
                System.out.println("in " + result.getText() + " "
                        + result.getCType());
            }
            // Zuordnungsfehler bei Operatoren
            if (result.getCRolle() == CRolle.SUMMAND1
                    && result.getExtPraefix() != null) {
                System.out.println("Fehler 1 Summand mit Vorzeichen"
                        + e.getTextContent());
            } else if (result.getCRolle() == CRolle.SUMMANDN1
                    && result.getExtPraefix() == null) {
                System.out.println("Fehler weiterer Summand ohne Vorzeichen"
                        + e.getTextContent());
            } else if (result.getCRolle() == CRolle.NACHVZMINUS
                    && !result.hasExtMinus()) {
                System.out.println("Fehler negative Zahl ohne Minus"
                        + e.getTextContent());
            } else if (result.getCRolle() == CRolle.FAKTOR1
                    && result.getExtPraefix() != null) {
                System.out.println("Fehler 1 Faktor mit Vorzeichen"
                        + e.getTextContent());
            } else if (result.getCRolle() == CRolle.FAKTORN1
                    && result.getExtPraefix() == null) {
                System.out.println("Fehler weiterer Faktor ohne Vorzeichen"
                        + e.getTextContent());
            }

        }
        return result;
    }
}
