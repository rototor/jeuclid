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

public class CMinTerm extends CElement {

    public CMinTerm(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.MINROW;
    }

    public static CMinTerm createMinTerm(final CElement inhalt) {
        return CMinTerm.createMinTerm(inhalt, CRolle.UNKNOWN);
    }

    public static CMinTerm createMinTerm(final CElement inhalt,
            final CRolle cRolle) {
        final CMinTerm minTerm = (CMinTerm) CElementHelper.createAll(inhalt
                .getElement(), "mrow", "vzterm", cRolle, null);
        minTerm.appendPraefixAndChild(inhalt);
        if (!inhalt.hasExtMinus()) {
            inhalt.setPraefix("-");
        }
        inhalt.setCRolle(CRolle.NACHVZMINUS);
        return minTerm;
    }

    @Override
    public boolean hatGleichenBetrag(final CElement cE2) {
        if (cE2 instanceof CMinTerm) {
            return this.getValue().hatGleichenBetrag(
                    ((CMinTerm) cE2).getValue());
        } else {
            return this.getValue().hatGleichenBetrag(cE2);
        }
    }

    @Override
    public boolean istGleichartigesMonom(final CElement e2) {
        return this.getValue().istGleichartigesMonom(e2);
    }

    public CElement getValue() {
        return this.getFirstChild();
    }

    @Override
    public void normalize() {
    };

    @Override
    public boolean hasNumberValue() {
        return this.getValue().hasNumberValue();
    }

    @Override
    public double getNumberValue() {
        return (-1) * this.getValue().getNumberValue();
    }

    @Override
    public int internalCompare(final CElement o) {
        final CMinTerm t2 = (CMinTerm) o;
        return t2.getValue().compareTo(this.getValue());
    }
}
