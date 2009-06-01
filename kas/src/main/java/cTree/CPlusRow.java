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

import java.util.ArrayList;

import org.w3c.dom.Element;

public class CPlusRow extends CRow {

    public CPlusRow(final Element element) {
        this.element = element;
        this.cRolle = CRolle.UNKNOWN;
    }

    // --------- getter
    @Override
    public CType getCType() {
        return CType.PLUSROW;
    }

    @Override
    public CRolle getFirstElementRolle() {
        return CRolle.SUMMAND1;
    }

    @Override
    public CRolle getLastElementRolle() {
        return CRolle.SUMMANDN1;
    }

    // -------- boolesche Tester
    // eine mrow kann unnï¿½tig sein, wenn sie nur ein Child hat, aber nicht
    // MathChild ist
    @Override
    protected boolean isNecessaryRow(final CElement el) {
        if ("mrow".equals(el.getElement().getNodeName()) && el.hasChildC()
                && (el.getCRolle() != CRolle.MATHCHILD)) {
            return el.getFirstChild().hasNextC();
        }
        return true;
    }

    @Override
    public int internalCompare(final CElement o) {
        final CPlusRow r2 = (CPlusRow) o;
        if (this.getCount() == r2.getCount()) {
            CElement c1 = this.getFirstChild();
            CElement c2 = r2.getFirstChild();
            for (int i = 0; i < this.getCount(); i++) {
                if (c1.compareTo(c2) != 0) {
                    return c1.compareTo(c2);
                } else {
                    c1 = c1.getNextSibling();
                    c2 = c2.getNextSibling();
                }
            }
            return 0;
        } else {
            return this.getCount() - r2.getCount();
        }
    }

    // -------- bulk Praefix and Rolle Support
    public void correctInternalCRolles() {
        if (this.hasChildC()) {
            CElement first = this.getFirstChild();
            first.setCRolle(CRolle.SUMMAND1);
            while (first.hasNextC()) {
                first = first.getNextSibling();
                first.setCRolle(CRolle.SUMMANDN1);
            }
        }
    }

    public void correctInternalPraefixesAndRolle() {
        if (this.hasChildC()) {
            final CElement first = this.getFirstChild();
            CElement nextC = first.getNextSibling();
            if (first.hasExtMinus()) {
                final CMinTerm newFirst = CMinTerm.createMinTerm(first
                        .cloneCElement(true));
                this.replaceChild(newFirst, first, true, false);
            } else if (first.hasExtPlus()) {
                first.removePraefix();
            }
            while (nextC != null) {
                if (!nextC.hasExtMinus()
                        && !"+".equals(nextC.getExtPraefix())) {
                    nextC.setPraefix("+");
                }
                nextC = nextC.getNextSibling();
            }
        }
        this.correctInternalCRolles();
    }

    @Override
    public void toggleAllVZ() {
        System.out.println("!! CPlusRow VZ ï¿½ndern!!");
        if (this.hasChildC()) {
            CElement summand = this.getFirstChild();
            // Sonderbehandlung von toggle fï¿½r den ersten Summanden
            CElement weiter = summand.getNextSibling();
            if (summand.getCRolle() == CRolle.SUMMAND1) {
                if (summand.getCType() == CType.MINROW) {
                    System.out.println("minrow durch ihren Inhalt ersetzen"
                            + summand.getFirstChild().hasExtMinus());
                    summand.getElement().removeChild(
                            summand.getFirstChild().getExtPraefix());
                    summand.getFirstChild().setExtPraefix(null);
                    summand = this.replaceChild(summand.getFirstChild(),
                            summand, true, false);
                } else {
                    System.out
                            .println("minrow einfï¿½gen und summand in minrow einfï¿½gen");
                    final CElement newContent = this.cloneChild(summand,
                            false);
                    final CElement newMinRow = CMinTerm.createMinTerm(
                            newContent, CRolle.SUMMAND1);
                    summand = this.replaceChild(newMinRow, summand, true,
                            true);
                }
            } else {
                summand.togglePlusMinus(false);
            }
            while (weiter != null) {
                weiter.togglePlusMinus(false);
                weiter = weiter.getNextSibling();
            }
        }
    }

    /*
     * beim ersten Summanden wird eine minrow in einen vzlosen Summanden
     * gewandelt aber zB. ein Plus nicht in eine minrow
     */
    @Override
    public void toggleAllVZButFirst(final boolean resolveFirstMinrow) {
        System.out.println("A CPlusRow VZ ï¿½ndern!!");
        if (this.hasChildC()) {
            CElement summand = this.getFirstChild();
            // Sonderbehandlung von toggle fï¿½r den ersten Summanden
            CElement weiter = summand.getNextSibling();
            if (summand.getCRolle() == CRolle.SUMMAND1 && resolveFirstMinrow) {
                if (summand.getCType() == CType.MINROW) {
                    final CElement cArgument = ((CMinTerm) summand)
                            .getValue();
                    cArgument.removePraefix();
                    summand = this.replaceChild(cArgument, summand, true,
                            false);
                }
            } else {
                System.out.println("So nicht VZ ï¿½ndern in Plusrow");
                summand.togglePlusMinus(false);
            }
            while (weiter != null) {
                weiter.togglePlusMinus(false);
                weiter = weiter.getNextSibling();
            }
        }
    }

    // Bulkoperatoren jeweils ohne Rollenanpassung. Fehlende Vz sind jeweils +

    public static ArrayList<CElement> createList(final CElement cE1,
            final CElement cE2) {
        final ArrayList<CElement> addendList = new ArrayList<CElement>();
        addendList.add(cE1.cloneCElement(true));
        final CElement newCE2 = cE2.cloneCElement(true);
        if (!newCE2.hasExtMinus()) {
            newCE2.setPraefix("+");
        }
        addendList.add(newCE2);
        return addendList;
    }

    public static CPlusRow createRow(final ArrayList<CElement> list) {
        return (CPlusRow) CRow.createRow(list, "+");
    }

    // expects el as a Clone
    protected static void insertMember(final CRow row, final CElement el,
            boolean isFirst) {
        System.out.println("Creating Member of PlusRow " + el.getText());
        if (el instanceof CPlusRow) {
            // evtl Vorzeichen dem ersten Summand zuschlagen, dann alle
            // einzeln als clone anhï¿½ngen
            System.out.println("Creating Plusrow 1");
            CElement cEl = el.getFirstChild();
            final CElement elel = cEl.cloneCElement(false);
            if (el.getExtPraefix() != null) {
                final Element elpr = (Element) el.getExtPraefix().cloneNode(
                        true);
                elel.setExtPraefix(elpr);
            }
            row.appendPraefixAndChild(elel);
            while (cEl.hasNextC()) {
                cEl = cEl.getNextSibling();
                row.appendPraefixAndChild(cEl.cloneCElement(true));
            }
        } else if (el instanceof CMinTerm) {
            System.out
                    .println("CPlusRow insertMember: Insert CMinTerm in CPlusRow "
                            + el.getText());
            final CElement arg = el.cloneCElement(false);
            final CElement newEl = CFences.createFenced(arg);
            if (row.hasChildC()) {
                newEl.setCRolle(CRolle.SUMMANDN1);
                if (el.hasExtPraefix()) {
                    System.out.println("CPlusrow insertMember has Praefix"
                            + el.getExtPraefix().getTextContent());
                    newEl.setExtPraefix(el.getExtPraefix());
                } else {
                    System.out
                            .println("CPlusrow insertMember VZ plus gesetzt bei "
                                    + arg.getText());
                    newEl.setPraefix("+");
                }
            } else {
                System.out.println("CPlusrow insertMember has no Child");
                newEl.setCRolle(CRolle.SUMMANDN1);
            }
            row.appendPraefixAndChild(newEl);
        } else {
            System.out.println("CPlusRow insertMember: Else in CPlusRow "
                    + el.getText());
            if (isFirst && el.hasExtMinus()) {
                System.out.println("Creating Plusrow 3 " + el.getText());
                final CElement newEl = el.cloneCElement(true);
                row.appendChild(CMinTerm.createMinTerm(
                        el.cloneCElement(true), CRolle.NACHVZMINUS));
                newEl.setCRolle(CRolle.SUMMANDN1);
            } else {
                System.out.println("Creating Plusrow 4 " + el.getText());
                final CElement newEl = el.cloneCElement(true);
                row.appendPraefixAndChild(newEl);
                newEl.setCRolle(CRolle.SUMMANDN1);
            }
        }
        isFirst = false;
    }

    // Support für die Klammern in dem CTree noch keine Vorzeichenanpassung
    @Override
    public CElement fence(final ArrayList<CElement> active) {
        System.out.println("Klammern in Summe");
        for (final CElement el : active) {
            el.removeCLastProperty();
        }
        CElement result = null;
        if (active.size() == 1) {
            result = this.standardFencing(active.get(0));
        } else if (active.size() > 1) {
            System.out.println("Klammern in Summe 2");
            final CElement first = active.get(0);
            final CPlusRow innen = CPlusRow.createRow(CRow.cloneList(active));
            innen.correctInternalPraefixesAndRolle();
            result = CFences.createFenced(innen);
            if (first.hasExtMinus()) {
                first.toggleToPraefixEmptyOrPlus();
            }
            this.replaceChild(result, first, true, true);
            boolean butfirst = false;
            for (final CElement el : active) {
                if (butfirst) {
                    this.removeChild(el, true, true, false);
                }
                butfirst = true;
            }
            result.setCActiveProperty();
        }
        return result;
    }

    // Mutatoren
    @Override
    protected CElement tauscheMitVorzeichen(final CElement el1,
            final CElement el2, final boolean nachRechts) {
        CElement c1neu = null;
        CElement c2neu = null;
        if (el1.getCRolle() == CRolle.SUMMANDN1) {
            this.insertBefore(el2, el1);
            c1neu = el1;
            c2neu = el2;
        } else {
            if (el1.getCType() == CType.MINROW) {
                c1neu = el1.cloneChild(el1.getFirstChild(), true);
            } else {
                c1neu = this.cloneChild(el1, true);
                c1neu.setPraefix("+");
            }
            if (el2.hasExtMinus()) {
                c2neu = CMinTerm.createMinTerm(this.cloneChild(el2, true));
                c2neu.setCRolle(CRolle.SUMMAND1);
            } else {
                c2neu = this.cloneChild(el2, false);
            }
            this.insertBefore(c1neu, el1);
            this.insertBefore(c2neu, c1neu);
            this.removeChild(el1, false, true, false);
            this.removeChild(el2, false, true, false);
        }
        if (nachRechts) {
            return c1neu;
        } else {
            return c2neu;
        }
    }
}
