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

public class CPot extends CElement {

    public CPot(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.POT;
    }

    public boolean isHoch0() {
        if (this.getExponent().getCType() != CType.NUM) {
            return false;
        }
        return ((CNum) this.getExponent()).is0();
    }

    public boolean isHoch1() {
        if (this.getExponent().getCType() != CType.NUM) {
            return false;
        }
        return ((CNum) this.getExponent()).is1();
    }

    public static CPot createPot(final CElement basis, final CElement expo) {
        final CPot pot = (CPot) CElementHelper.createAll(basis.getElement(),
                "msup", "msup", CRolle.UNKNOWN, null);
        pot.appendChild(basis);
        pot.appendPraefixAndChild(expo);
        basis.setCRolle(CRolle.BASIS);
        expo.setCRolle(CRolle.EXPONENT);
        return pot;
    }

    // nur für positive Exponenten geeignet
    public static CPot createPot(final CElement basis, final int expo) {
        final CElement cExp = CNum.createNum(basis.getElement(), "" + expo);
        return CPot.createPot(basis, cExp);
    }

    // nur für positive Exponenten geeignet
    public static CPot createPot(final Element producer, final String ident,
            final int expo) {
        final CElement cBas = CIdent.createIdent(producer, ident);
        return CPot.createPot(cBas, expo);
    }

    @Override
    public boolean hatGleichenBetrag(final CElement cE2) {
        if (cE2 instanceof CPot) {
            return this.getBasis().hatGleichenBetrag(((CPot) cE2).getBasis())
                    && this.getExponent().hatGleichenBetrag(
                            ((CPot) cE2).getExponent());
        } else if (cE2 instanceof CMinTerm) {
            return this.hatGleichenBetrag(((CMinTerm) cE2).getValue());
        }
        return false;
    }

    public static CPot createSquare(final CElement cElement) {
        CPot newSquare = null;
        if (cElement instanceof CPot) {
            // erzeugt Basis
            final CElement newBase = ((CPot) cElement).getBasis()
                    .cloneCElement(true);
            // erzeugt Exponent, zuerst TR zuerst erster Faktor
            final CElement first = CElementHelper.createAll(cElement
                    .getElement(), "mn", "mn", CRolle.FAKTOR1, null);
            first.setText("2");
            final CElement second = ((CPot) cElement).getExponent()
                    .cloneCElement(true);
            final Element op = EElementHelper.createOp(cElement.getElement(),
                    "*");
            second.setExtPraefix(op);
            // erzeugt die TR
            final ArrayList<CElement> faktoren = new ArrayList<CElement>();
            faktoren.add(first);
            faktoren.add(second);
            // erzeugt Exponent
            final CElement newExp = CTimesRow.createRow(faktoren);
            // erzeugt die Potenz
            newSquare = (CPot) CElementHelper.createAll(
                    cElement.getElement(), "msup", "msup", cElement
                            .getCRolle(), cElement.getExtPraefix());
            newSquare.appendChild(newBase);
            newSquare.appendChild(newExp);
            newBase.setCRolle(CRolle.BASIS);
            newExp.setCRolle(CRolle.EXPONENT);
        } else {
            newSquare = (CPot) CElementHelper.createAll(
                    cElement.getElement(), "msup", "msup", cElement
                            .getCRolle(), cElement.getExtPraefix());
            final CElement newBase = cElement.cloneCElement(false);
            newSquare.appendChild(newBase);
            final CElement newExp = CElementHelper.createAll(cElement
                    .getElement(), "mn", "mn", CRolle.EXPONENT, null);
            newExp.setText("2");
            newSquare.appendChild(newExp);
            newBase.setCRolle(CRolle.BASIS);
            newExp.setCRolle(CRolle.EXPONENT);
        }
        return newSquare;
    }

    @Override
    public void normalize() {
    };

    @Override
    public boolean istGleichartigesMonom(final CElement el) {
        boolean result = false;
        if (el.getCType() == CType.IDENT) {
            result = (this.getElement().getFirstChild().getTextContent()
                    .equals(el.getElement().getTextContent()));
        } else if (el.getCType() == CType.POT) {
            result = (this.getElement().getFirstChild().getTextContent()
                    .equals(el.getElement().getFirstChild().getTextContent()));
        } else if (el.getCType() == CType.MINROW) {
            return this.istGleichartigesMonom(((CMinTerm) el).getValue());
        }
        System.out.println("Gleichartig: " + result);
        return result;
    }

    public CElement getBasis() {
        return this.getFirstChild();
    }

    public void setBasis(final String s) {
        this.element.getFirstChild().setTextContent(s);
    }

    public CElement getExponent() {
        return this.getFirstChild().getNextSibling();
    }

    public int getExponentValue() {
        final String exp = this.getExponent().getElement().getTextContent();
        try {
            return Integer.parseInt(exp);
        } catch (final NumberFormatException e) {
            System.out.println("Kein guter Exponent");
        }
        return -1;
    }

    public void setExponent(final String e) {
        this.element.getFirstChild().getNextSibling().setTextContent(e);
    }

    public String getVar() {
        return this.getBasis().getElement().getTextContent();
    }

    public String getSignatur() {
        final String nr = this.getBasis().getElement().getTextContent();
        final String exp = this.getExponent().getElement().getTextContent();
        return nr + exp;
    }

    @Override
    public boolean hasNumberValue() {
        return this.getBasis().hasNumberValue()
                && this.getExponent().hasNumberValue();
    }

    @Override
    public double getNumberValue() {
        return Math.pow(this.getBasis().getNumberValue(), this
                .getExponentValue());
    }

    @Override
    public int internalCompare(final CElement o) {
        if (o instanceof CPot) {
            final CPot f2 = (CPot) o;
            if (this.getBasis().compareTo(f2.getBasis()) != 0) {
                return this.getBasis().compareTo(f2.getBasis());
            } else {
                return this.getExponentValue() - f2.getExponentValue();
            }
        } else {
            final CIdent f2 = (CIdent) o;
            return -f2.internalCompare(this);
        }

    }
}
