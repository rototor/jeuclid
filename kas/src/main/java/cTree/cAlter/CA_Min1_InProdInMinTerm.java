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

import java.util.HashMap;

import cTree.CElement;
import cTree.CFences;
import cTree.CMinTerm;
import cTree.CRolle;
import cTree.CTimesRow;

public class CA_Min1_InProdInMinTerm extends CAlter {

    private CTimesRow oldTimesRow;

    private CMinTerm oldMinTerm;

    @Override
    public CElement change(final CElement old) {
        System.out.println("Changer -(-1)a to a");
        old.removeCActiveProperty();
        final CElement newChild = CTimesRow
                .foldOne((CTimesRow) this.oldTimesRow.cloneCElement(false));
        if (newChild instanceof CTimesRow
                && newChild.getFirstChild().hasExtPraefix()) {
            newChild.getFirstChild().setExtPraefix(null);
        }
        this.oldMinTerm.getParent().replaceChild(newChild, this.oldMinTerm,
                true, true);
        newChild.setCActiveProperty();
        return newChild;
    }

    @Override
    public String getText() {
        return "(-1) auflösen und VZ ändern 1-";
    }

    @Override
    public boolean check(final CElement el) {
        System.out.println("Check CA");
        if (el instanceof CFences) {
            final CFences elF = (CFences) el;
            if (elF.isFencedMin1() && elF.getCRolle().equals(CRolle.FAKTOR1)) {
                if (el.hasParent() && el.getParent() instanceof CTimesRow) {
                    this.oldTimesRow = (CTimesRow) el.getParent();
                    if (this.oldTimesRow.hasParent()
                            && this.oldTimesRow.getParent() instanceof CMinTerm) {
                        this.oldMinTerm = (CMinTerm) this.oldTimesRow
                                .getParent();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
