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

public class CPlusTerm extends CElement {

    public CPlusTerm(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.PLUSTERM;
    }

    public static CPlusTerm createPTerm(final CElement inhalt) {
        return CPlusTerm.createPTerm(inhalt, CRolle.UNKNOWN);
    }

    public static CPlusTerm createPTerm(final CElement inhalt,
            final CRolle cRolle) {
        final CPlusTerm pTerm = (CPlusTerm) CElementHelper.createAll(inhalt
                .getElement(), "mrow", "pterm", cRolle, null);
        pTerm.appendPraefixAndChild(inhalt);
        if (!inhalt.hasExtMinus()) {
            inhalt.setPraefix("-");
        }
        inhalt.setCRolle(CRolle.NACHVZPLUS);
        return pTerm;
    }

    @Override
    public boolean hatGleichenBetrag(final CElement cE2) {
        if (cE2 instanceof CPlusTerm) {
            return this.getValue().hatGleichenBetrag(
                    ((CPlusTerm) cE2).getValue());
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
        return this.getValue().getNumberValue();
    }

    @Override
    public int internalCompare(final CElement o) {
        final CPlusTerm t2 = (CPlusTerm) o;
        return t2.getValue().compareTo(this.getValue());
    }
}
