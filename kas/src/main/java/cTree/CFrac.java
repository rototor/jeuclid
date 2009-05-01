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

public class CFrac extends CElement {

    public CFrac(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.FRAC;
    }

    public CElement getZaehler() {
        return this.getFirstChild();
    }

    public CElement getNenner() {
        return this.getFirstChild().getNextSibling();
    }

    public void correctInternalRolles() {
        this.getZaehler().setCRolle(CRolle.ZAEHLER);
        this.getNenner().setCRolle(CRolle.NENNER);
    }

    public static CFrac createFraction(final CElement zaehler,
            final CElement nenner) {
        final CFrac fraction = (CFrac) CElementHelper.createAll(zaehler
                .getElement(), "mfrac", "mfrac", CRolle.UNKNOWN, null);
        fraction.appendPraefixAndChild(zaehler);
        fraction.appendPraefixAndChild(nenner);
        zaehler.setCRolle(CRolle.ZAEHLER);
        nenner.setCRolle(CRolle.NENNER);
        return fraction;
    }

    @Override
    public void normalize() {
    };

    @Override
    public boolean hasNumberValue() {
        return this.getZaehler().hasNumberValue()
                && this.getNenner().hasNumberValue()
                && (Math.abs(this.getNenner().getNumberValue()) > 0.000001);
    }

    @Override
    public double getNumberValue() {
        return (this.getZaehler().getNumberValue() / this.getNenner()
                .getNumberValue());
    }

    @Override
    public int internalCompare(final CElement o) {
        final CFrac f2 = (CFrac) o;
        if (this.getNenner().compareTo(f2.getNenner()) != 0) {
            return this.getNenner().compareTo(f2.getNenner());
        } else {
            return this.getZaehler().compareTo(f2.getZaehler());
        }
    }

}
