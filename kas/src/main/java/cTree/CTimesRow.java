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

import cTree.adapter.EElementHelper;

public class CTimesRow extends CRow {

    public CTimesRow(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.TIMESROW;
    }

    @Override
    public CRolle getFirstElementRolle() {
        return CRolle.FAKTOR1;
    }

    @Override
    public CRolle getLastElementRolle() {
        return CRolle.FAKTORN1;
    }

    public void correctInternalCRolles() {
        if (this.hasChildC()) {
            CElement first = this.getFirstChild();
            first.setCRolle(CRolle.FAKTOR1);
            while (first.hasNextC()) {
                first = first.getNextSibling();
                first.setCRolle(CRolle.FAKTORN1);
            }
        }
    }

    public void correctInternalPraefixesAndRolle() {
        if (this.hasChildC()) {
            CElement first = this.getFirstChild();
            if (first.hasExtPraefix()) {
                this.getElement().removeChild(first.getExtPraefix());
                this.setPraefix("");
            } else if (first.hasExtTimes()) {
                first.removePraefix();
            }
            while (first.hasNextC()) {
                first = first.getNextSibling();
                if (!first.hasExtDiv() && !first.hasExtTimes()) {
                    first.setPraefix("·"); // "·"
                }
            }
        }
        this.correctInternalCRolles();
    }

    // ---------Support für die Vorzeichenbehandlung
    // noch nicht getestet
    @Override
    public void toggleAllVZ() {
        if (this.hasChildC()) {
            CElement factor = this.getFirstChild();
            while (factor != null) {
                factor.toggleTimesDiv(false);
                factor = factor.getNextSibling();
            }
        }
    }

    // der erste Faktor wird nicht angetastet
    @Override
    public void toggleAllVZButFirst(final boolean resolveFirstMinrow) {
        if (this.hasChildC()) {
            final CElement factor = this.getFirstChild();
            CElement weiter = factor.getNextSibling();
            while (weiter != null) {
                weiter.toggleTimesDiv(false);
                weiter = weiter.getNextSibling();
            }
        }
    }

    // expects el as a Clone
    protected static void insertMember(final CRow list,
            final CElement toInsertClone, boolean isFirst) {
        if (toInsertClone instanceof CTimesRow) {
            // evtl Vorzeichen dem ersten Faktor zuschlagen, dann alle einzeln
            // als clone anhängen
            CElement cEl = toInsertClone.getFirstChild();
            final CElement elel = cEl.cloneCElement(false);
            if (toInsertClone.getExtPraefix() != null) {
                final Element elpr = (Element) toInsertClone.getExtPraefix()
                        .cloneNode(true);
                elel.setExtPraefix(elpr);
            }
            if (!isFirst && (elel.getExtPraefix() == null)) {
                elel.setPraefix("*");
            }
            list.appendPraefixAndChild(elel);
            elel.setCRolle(CRolle.FAKTORN1);
            while (cEl.hasNextC()) {
                cEl = cEl.getNextSibling();
                list.appendPraefixAndChild(cEl.cloneCElement(true));
            }
        } else if (toInsertClone instanceof CMinTerm
                || toInsertClone instanceof CPlusRow) {
            final CElement newArg = toInsertClone.cloneCElement(false);
            final CElement newEl = CFences.createFenced(newArg);
            newEl.setPraefix("*");
            list.appendPraefixAndChild(newEl);
            newEl.setCRolle(CRolle.FAKTORN1);
        } else if (isFirst) {
            final CElement newEl = toInsertClone.cloneCElement(false);
            list.appendChild(newEl);
            newEl.setCRolle(CRolle.FAKTOR1);
        } else {
            final CElement newEl = toInsertClone.cloneCElement(true);
            if (!newEl.hasExtDiv() && !newEl.hasExtTimes()) {
                newEl.setPraefix("*");
            }
            list.appendPraefixAndChild(newEl);
            newEl.setCRolle(CRolle.FAKTORN1);
        }
        isFirst = false;
    };

    public static CTimesRow createRow(final ArrayList<CElement> list) {
        return (CTimesRow) CRow.createRow(list, "*");
    }

    public static ArrayList<CElement> createList(final CElement cE1,
            final CElement cE2) {
        final ArrayList<CElement> factorList = new ArrayList<CElement>();
        factorList.add(cE1.cloneCElement(true));
        final CElement newCE2 = cE2.cloneCElement(true);
        factorList.add(newCE2);
        return factorList;
    }

    public static ArrayList<CTimesRow> cloneList(
            final ArrayList<CTimesRow> oldList) {
        final ArrayList<CTimesRow> newList = new ArrayList<CTimesRow>();
        for (final CTimesRow el : oldList) {
            newList.add((CTimesRow) el.cloneCElement(true));
        }
        return newList;
    }

    public static ArrayList<CTimesRow> castList(
            final ArrayList<CElement> oldList) {
        final ArrayList<CTimesRow> newList = new ArrayList<CTimesRow>();
        for (final CElement cEl : oldList) {
            newList.add((CTimesRow) cEl);
        }
        return newList;
    }

    public static CElement cutOne(final CTimesRow cTR, final int i) {
        final ArrayList<CElement> factorList = cTR.getMemberList();
        if (i < 0 || i >= factorList.size()) {
            return cTR;
        } else {
            CElement newElement;
            boolean timesRowAgain = false;
            if (cTR.getFirstChild().hasNextC()
                    && cTR.getFirstChild().getNextSibling().hasNextC()) {
                timesRowAgain = true;
            }
            if (timesRowAgain) {
                factorList.remove(i);
                final CTimesRow newTR = CTimesRow.createRow(factorList);
                newTR.correctInternalPraefixesAndRolle();
                newTR.setCRolleAndPraefixFrom(cTR);
                newElement = newTR;
            } else {
                if (i == 0) {
                    newElement = cTR.getFirstChild().getNextSibling();
                } else {
                    newElement = cTR.getFirstChild();
                }
                newElement.setCRolleAndPraefixFrom(cTR);
            }
            return newElement;
        }
    }

    public static CElement foldOne(final CTimesRow cTR) {
        CElement newElement;
        boolean timesRowAgain = false;
        if (cTR.getFirstChild().hasNextC()
                && cTR.getFirstChild().getNextSibling().hasNextC()) {
            timesRowAgain = true;
        }
        if (timesRowAgain) {
            final ArrayList<CElement> factorList = cTR.getMemberList();
            factorList.remove(0);
            final CTimesRow newTR = CTimesRow.createRow(factorList);
            newTR.correctInternalPraefixesAndRolle();
            newTR.setCRolleAndPraefixFrom(cTR);
            newElement = newTR;
        } else {
            // newElement ist das secondSibling evtl. mit dem externen
            // Vorzeichen
            newElement = cTR.getFirstChild().getNextSibling();
            newElement.setCRolleAndPraefixFrom(cTR);
        }
        return newElement;
    }

    public static CElement foldOneL(final CTimesRow cTR) {
        CElement newElement;
        boolean timesRowAgain = false;
        if (cTR.getFirstChild().hasNextC()
                && cTR.getFirstChild().getNextSibling().hasNextC()) {
            timesRowAgain = true;
        }
        if (timesRowAgain) {
            final ArrayList<CElement> factorList = cTR.getMemberList();
            factorList.remove(factorList.size() - 1);
            final CTimesRow newTR = CTimesRow.createRow(factorList);
            newTR.correctInternalPraefixesAndRolle();
            newTR.setCRolleAndPraefixFrom(cTR);
            newElement = newTR;
        } else {
            // newElement ist das secondSibling evtl. mit dem externen
            // Vorzeichen
            newElement = cTR.getFirstChild();
            newElement.setCRolleAndPraefixFrom(cTR);
        }
        return newElement;
    }

    public static ArrayList<CElement> fold(final ArrayList<CTimesRow> list) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        for (final CTimesRow cEl : list) {
            result.add(CTimesRow.foldOne(cEl));
        }
        return result;
    }

    public static ArrayList<CElement> foldL(final ArrayList<CTimesRow> list) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        for (final CTimesRow cEl : list) {
            result.add(CTimesRow.foldOneL(cEl));
        }
        return result;
    }

    public static ArrayList<CElement> map(final ArrayList<CElement> list,
            final CElement second) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        for (final CElement first : list) {
            final ArrayList<CElement> args = new ArrayList<CElement>();
            final CElement newFirst = first.cloneCElement(false);
            Element res = null;
            if (first.getExtPraefix() != null) {
                res = (Element) first.getExtPraefix().cloneNode(true);
            }
            args.add(newFirst);
            final CElement newSecond = second.cloneCElement(true);
            args.add(newSecond);
            final CTimesRow prod = CTimesRow.createRow(args);
            prod.correctInternalPraefixesAndRolle();
            if (res != null) {
                prod.setExtPraefix(res);
            }
            result.add(prod);
        }
        return result;
    }

    public static ArrayList<CElement> map(final CElement first,
            final ArrayList<CElement> list) {
        final ArrayList<CElement> result = new ArrayList<CElement>();
        for (final CElement second : list) {
            final ArrayList<CElement> args = new ArrayList<CElement>();
            final CElement newFirst = first.cloneCElement(false);
            args.add(newFirst);
            final CElement newSecond = second.cloneCElement(true);
            newSecond.setExtPraefix(EElementHelper.createOp(first
                    .getElement(), "*"));
            Element res = null;
            if (second.getExtPraefix() != null) {
                res = (Element) second.getExtPraefix().cloneNode(true);
            }
            args.add(newSecond);
            final CElement prod = CTimesRow.createRow(args);
            if (res != null) {
                prod.setExtPraefix(res);
            }

            result.add(prod);
        }
        return result;
    }

    // Support für die Klammern in dem CTree
    @Override
    public CElement fence(final ArrayList<CElement> active) {
        for (final CElement el : active) {
            el.removeCLastProperty();
        }
        if (!active.get(0).hasPrevC()
                && !active.get(active.size() - 1).hasNextC()) {
            System.out.println("ganzes Produkt");
            final CElement parent = active.get(0).getParent();
            if (parent.hasParent()) {
                final CElement realParent = parent.getParent();
                final ArrayList<CElement> newList = new ArrayList<CElement>();
                newList.add(parent);
                return realParent.fence(newList);
            }
        }

        CElement result = null;
        if (active.size() == 1) {
            result = this.standardFencing(active.get(0));
        } else if (active.size() > 1) {
            final CElement first = active.get(0);
            final boolean hasDiv = first.hasExtDiv();
            final CTimesRow innen = CTimesRow.createRow(CRow
                    .cloneList(active));
            if (hasDiv) {
                innen.toggleAllVZ();
            }
            innen.getFirstChild().setPraefix("");
            innen.correctInternalPraefixesAndRolle();
            result = CFences.createFenced(innen);
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

    @Override
    protected boolean isNecessaryRow(final CElement el) {
        if (el.getCType() == CType.TIMESROW && el.hasChildC()) {
            return el.getFirstChild().hasNextC();
        }
        return true;
    }

    public boolean canRemoveChild(final CElement e) {
        if (e.getCRolle() == CRolle.FAKTOR1 && e.hasNextC()
                && e.getNextSibling().hasExtDiv()) {
            return false;
        }
        return true;
    }

    @Override
    public CElement removeChild(final CElement e,
            final boolean correctNextRolle, final boolean unregister,
            final boolean withNormalParent) {
        if (this.canRemoveChild(e)) {
            return super.removeChild(e, correctNextRolle, unregister,
                    withNormalParent);
        } else {
            return e;
        }
    }

    public CNum getKoeffAsBetragFromMonom() {
        final CElement num = this.getFirstChild();
        if (num instanceof CNum) {
            return (CNum) num;
        } else {
            final CNum cEl = (CNum) CElementHelper.createAll(this
                    .getElement(), "mn", "mn", CRolle.FAKTOR1, null);
            cEl.setText("1");
            return cEl;
        }
    }

    public CElement getFirstVarFromMonom() {
        final CElement num = this.getFirstChild();
        if (num instanceof CNum) {
            if (num == null) {
                return null;
            } else {
                return num.getNextSibling();
            }
        } else {
            return num;
        }
    }

    private boolean canBeMonomFaktor(final CElement cEl) {
        return (cEl instanceof CNum) || (cEl instanceof CIdent)
                || (cEl instanceof CPot);
    }

    private boolean canBeInMonomVars(final CElement cEl) {
        return (cEl instanceof CIdent) || (cEl instanceof CPot);
    }

    // null ist kein Monom
    public boolean isMonom() {
        boolean result = true;
        CElement f = this.getFirstChild();
        if (f == null || !this.canBeMonomFaktor(f)) {
            return false;
        } else {
            while (f.hasNextC()) {
                f = f.getNextSibling();
                result = result && this.canBeInMonomVars(f);
            }
        }
        final String simpleVarString = this.getSimpleVarString();
        final char[] toSort = simpleVarString.toCharArray();
        java.util.Arrays.sort(toSort);
        final boolean isSorted = java.util.Arrays.equals(simpleVarString
                .toCharArray(), toSort);
        return result && isSorted;
    }

    // ist gleichartig mit anderer TimesRow, wenn beide gleichartige sortierte
    // Monome sind
    @Override
    public boolean istGleichartigesMonom(final CElement e2) {
        if (e2 instanceof CTimesRow) {
            final String s1 = this.getVarString();
            final String s2 = ((CTimesRow) e2).getVarString();
            boolean result = !(s1.equals("Fehler") || s2.equals("Fehler"));
            result = result && (this.isMonom())
                    && (((CTimesRow) e2).isMonom()) && (s1.equals(s2));
            return result;
        } else if (e2.getCType() == CType.MINROW) {
            return this.istGleichartigesMonom(((CMinTerm) e2).getValue());
        } else if (e2 instanceof CIdent) {
            final String s1 = this.getVarString();
            final String s2 = ((CIdent) e2).getVar();
            boolean result = !(s1.equals("Fehler") || s2.equals("Fehler"));
            result = result && (s1.equals(s2));
            return result;
        }
        return false;
    }

    public String getVarString() {
        String result = "";
        CElement vars = this.getFirstVarInMonomRow();
        while (vars != null) {
            if (vars instanceof CIdent) {
                result = result + ((CIdent) vars).getVar();
            } else if (vars instanceof CPot) {
                result = result + ((CPot) vars).getSignatur();
            } else {
                return "Fehler";
            }
            vars = vars.getNextSibling();
        }
        return result;
    }

    private String getSimpleVarString() {
        String result = "";
        CElement vars = this.getFirstVarInMonomRow();
        while (vars != null) {
            if (vars instanceof CIdent) {
                result = result + ((CIdent) vars).getVar();
            } else if (vars instanceof CPot) {
                result = result + ((CPot) vars).getVar();
            } else {
                return "Fehler";
            }
            vars = vars.getNextSibling();
        }
        return result;
    }

    public CElement getFirstVarInMonomRow() {
        final CElement e1 = this.getFirstChild();
        if (e1 instanceof CNum && e1.hasNextC()) {
            return e1.getNextSibling();
        }
        return e1;
    }

    @Override
    protected CElement tauscheMitVorzeichen(final CElement el1, CElement el2,
            final boolean nachRechts) {
        if (el1.getCRolle() == CRolle.FAKTORN1) {
            this.insertBefore(el2, el1);
        } else if (!el2.hasExtDiv()) {
            // System.out.println(el1.getExtPraefix().getTextContent());
            final CElement el2neu = el2.cloneCElement(false);
            el2neu.setCRolle(CRolle.FAKTOR1);
            this.insertBefore(el2neu, el1);
            el1.setPraefix("*");
            el1.setCRolle(CRolle.FAKTORN1);
            el1.getParent().removeChild(el2, false, true, false);
            el2 = el2neu;
        }
        if (nachRechts) {
            return el1;
        } else {
            el2.setCActiveProperty();
            return el2;
        }
    }

    @Override
    public int internalCompare(final CElement o) {
        final CTimesRow r2 = (CTimesRow) o;
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

}
