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

import cTree.adapter.EElementHelper;

public class CMixedNumber extends CElement {

    public CMixedNumber(final Element element) {
        this.element = element;
    }

    @Override
    public CType getCType() {
        return CType.MIXEDN;
    }

    public CElement getWholeNumber() {
        return this.getFirstChild();
    }

    public CElement getFraction() {
        return this.getFirstChild().getNextSibling();
    }

    public void correctInternalRolles() {
        this.getWholeNumber().setCRolle(CRolle.WHOLENUMBER);
        this.getWholeNumber().setPraefix("ip");
        this.getFraction().setCRolle(CRolle.FRACTION);
    }

    public static CMixedNumber createMixedNumber(final CElement wholeNumber,
            final CElement fraction) {
        final Element ivz = EElementHelper.createInvPlus(fraction);
        fraction.setExtPraefix(ivz);
        final CMixedNumber mixed = (CMixedNumber) CElementHelper.createAll(
                wholeNumber.getElement(), "mrow", "mmixed", CRolle.UNKNOWN,
                null);
        mixed.appendPraefixAndChild(wholeNumber);
        mixed.appendPraefixAndChild(fraction);
        wholeNumber.setCRolle(CRolle.WHOLENUMBER);
        fraction.setCRolle(CRolle.FRACTION);
        return mixed;
    }

    @Override
    public void normalize() {
    };

    @Override
    public boolean hasNumberValue() {
        return this.getWholeNumber().hasNumberValue()
                && this.getFraction().hasNumberValue();
    }

    @Override
    public double getNumberValue() {
        return (this.getWholeNumber().getNumberValue() + this.getFraction()
                .getNumberValue());
    }

    @Override
    public int internalCompare(final CElement o) {
        final CMixedNumber f2 = (CMixedNumber) o;
        if (this.getWholeNumber().compareTo(f2.getWholeNumber()) != 0) {
            return this.getWholeNumber().compareTo(f2.getWholeNumber());
        } else {
            return this.getFraction().compareTo(f2.getFraction());
        }
    }

}
