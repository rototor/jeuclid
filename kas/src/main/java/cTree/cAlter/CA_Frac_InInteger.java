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

package cTree.cAlter;

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFrac;
import cTree.CNum;
import cTree.CRolle;
import cTree.adapter.C_Event;
import cTree.cDefence.CD_Event;

public class CA_Frac_InInteger extends CA_Base {

    private CFrac cFrac;

    private CElement z;

    private int zVal;

    private CElement n;

    private int nVal;

    private enum ChangeTyp {
        Eins, ZweiZahlen
    };

    private ChangeTyp changeTyp;

    @Override
    public CElement doIt(final CD_Event message) {
        if (this.changeTyp == ChangeTyp.ZweiZahlen) {
            final CNum cNum = CNum.createNum(this.cFrac.getElement(), ""
                    + (this.zVal / this.nVal));
            this.cFrac.getParent().replaceChild(cNum, this.cFrac, true, true);
            return cNum;
        } else {
            this.cFrac.getParent().replaceChild(this.z, this.cFrac, true,
                    true);
            return this.z;
        }
    }

    @Override
    public String getText() {
        return "Bruch -> ganze Zahl";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final C_Event event = this.getEvent();
            final CElement first = event.getFirst();
            final ArrayList<CElement> sel = event.getSelection();
            if (sel.size() > 0 && first instanceof CFrac) {
                this.cFrac = (CFrac) first;
                if (this.cFrac.getCRolle() != CRolle.FRACTION) {
                    this.z = this.cFrac.getZaehler();
                    this.n = this.cFrac.getNenner();
                    try {
                        if (this.n instanceof CNum) {
                            this.nVal = ((CNum) this.n).getValue();
                            if (this.nVal == 1) {
                                this.changeTyp = ChangeTyp.Eins;
                                return true;
                            } else if (this.z instanceof CNum) {
                                this.zVal = ((CNum) this.z).getValue();
                                if (this.nVal != 0
                                        && this.zVal % this.nVal == 0) {
                                    this.changeTyp = ChangeTyp.ZweiZahlen;
                                    return true;
                                }
                            }
                        }
                    } catch (final NumberFormatException e) {
                    }
                }
            }
        }
        return false;
    }

}
