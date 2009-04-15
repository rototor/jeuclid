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

package cTree.adapter;

import cTree.CElement;
import cTree.CElementHelper;
import cTree.CRolle;

public abstract class RolleAdapter extends CTreeMutator {

    protected CRolle cRolle;

    public CRolle getCRolle() {
        return this.cRolle;
    }

    // --- Setter die von der Rolle abhängen --------------
    public void setCRolle(final CRolle c) {
        this.cRolle = c;
    }

    public void setCRolleAndPraefixFrom(final RolleAdapter c) {
        this.cRolle = c.getCRolle();
        this.setExtPraefix(c.getExtPraefix());
    }

    public void setPraefixEmptyOrPlus() { // ... - 3 -> ... + 3
        if (this.cRolle == CRolle.SUMMANDN1) {
            this.setPraefix("+");
        } else if ((this.praefix != null) && this.cRolle == CRolle.SUMMAND1) {
            this.removePraefix();
        }
    }

    public void toggleToPraefixEmptyOrPlus() { // ... - 3 -> ... + 3
        if ("-".equals(this.praefix.getTextContent())
                && this.cRolle == CRolle.SUMMANDN1) {
            this.setPraefix("+");
        } else if ("-".equals(this.praefix.getTextContent())
                && this.cRolle == CRolle.SUMMAND1) {
            this.removePraefix();
        }
    }

    public void toggleToTimesOrEmpty() { // ... : 3 -> ... * 3
        if (":".equals(this.praefix.getTextContent())
                && this.cRolle == CRolle.FAKTORN1) {
            this.setPraefix("·"); // : 3 -> 3
        } else if (":".equals(this.praefix.getTextContent())
                && this.cRolle == CRolle.FAKTOR1) {
            this.removePraefix();
        }
    }

    public void togglePlusMinus(final boolean checkAllowed) {
        if (checkAllowed && !CElementHelper.canHaveMinus(this.cRolle)) {
            return;
        }
        if (this.hasExtEmptyOrPlus()) {
            this.setPraefix("-");
        } else {
            this.toggleToPraefixEmptyOrPlus();
        }
    }

    public void toggleTimesDiv(final boolean checkAllowed) {
        if (checkAllowed && !CElementHelper.canHaveDiv(this.cRolle)) {
            return;
        }
        if (this.hasExtEmptyOrTimes()) {
            this.setPraefix(":");
        } else {
            this.toggleToTimesOrEmpty();
        }
    }

    // --- Mutatoren ---------------------------------------
    // der erste sollte bald entfernt werden
    public CElement cloneChild(final CElement cEl, final boolean withPraefix) {
        final CElement cE2 = cEl.cloneCElement(withPraefix);
        return cE2;
    }

    @Override
    public CElement cloneCElement(final boolean withPraefix) {
        final CElement cE2 = super.cloneCElement(withPraefix);
        cE2.setCRolle(this.getCRolle());
        return cE2;
    }
}
