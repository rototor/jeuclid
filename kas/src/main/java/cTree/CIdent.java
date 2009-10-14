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

public class CIdent extends CElement {

    public CIdent(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.IDENT;
    }

    public static CIdent createIdent(final Element producer, final String s) {
        final CIdent ident = (CIdent) CElementHelper.createAll(producer,
                "mi", "mi", CRolle.UNKNOWN, null);
        ident.setText(s);
        return ident;
    }

    @Override
    public boolean hatGleichenBetrag(final CElement cE2) {
        if (cE2 instanceof CIdent) {
            return this.getText().equals(((CIdent) cE2).getText());
        } else if (cE2 instanceof CMinTerm) {
            return this.hatGleichenBetrag(((CMinTerm) cE2).getValue());
        }
        return false;
    }

    @Override
    public void normalize() {
    };

    @Override
    public boolean istGleichartigesMonom(final CElement el) {
        boolean result = false;
        if (el.getCType() == CType.IDENT) {
            result = (this.getElement().getTextContent().equals(el
                    .getElement().getTextContent()));
        } else if (el.getCType() == CType.MINROW) {
            return this.istGleichartigesMonom(((CMinTerm) el).getValue());
        } else if (el.getCType() == CType.POT) {
            result = (this.getElement().getTextContent().equals(el
                    .getElement().getFirstChild().getTextContent()));
        } else if (el.getCType() == CType.TIMESROW) {
            final String s1 = this.getVar();
            final String s2 = ((CTimesRow) el).getVarString();
            result = !(s1.equals("Fehler") || s2.equals("Fehler"));
            result = result && (s1.equals(s2));
            return result;
        }
        return result;
    }

    public String getVar() {
        return this.getElement().getTextContent();
    }

    public String getSignatur() {
        return this.getVar();
    }

    public CPot toPot() {
        final CPot cPot = CPot.createPot(this, 1);
        cPot.setCRolle(this.getCRolle());
        cPot.setExtPraefix(this.getExtPraefix());
        this.setExtPraefix(null);
        return cPot;
    }

    @Override
    public int internalCompare(final CElement o) {
        if (o instanceof CIdent) {
            final CIdent i2 = (CIdent) o;
            return this.getVar().compareTo(i2.getVar());
        } else {
            final CPot cP = (CPot) o;
            final int result1 = this.compareTo(cP.getBasis());
            if (result1 != 0) {
                return result1;
            } else {
                return -1;
            }
        }

    }
}
