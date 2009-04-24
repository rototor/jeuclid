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
import java.util.Collections;
import java.util.HashMap;

import cTree.CElement;
import cTree.CTComparator;
import cTree.CTimesRow;

public class CA_TimesRow_Sort extends CAlter {

    @Override
    public CElement change(final ArrayList<CElement> els) {
        if (els.get(0) instanceof CTimesRow) {
            final CTimesRow old = (CTimesRow) els.get(0);
            final ArrayList<CElement> prodMembers = old.getMemberList();
            Collections.sort(prodMembers, new CTComparator());
            if (prodMembers.size() > 0) {
                if (prodMembers.get(0).hasExtPraefix()) {
                    System.out.println("First Faktor has Ext Praefix");
                    prodMembers.get(0).setPraefix("");
                }
                for (int i = 1; i < prodMembers.size(); i++) {
                    if (prodMembers.get(i).hasExtNull()) {
                        prodMembers.get(i).setPraefix("*");
                    }
                }
            }
            final CTimesRow newRow = CTimesRow.createRow(prodMembers);
            newRow.correctInternalPraefixesAndRolle();
            els.get(0).getParent().replaceChild(newRow, els.get(0), true,
                    true);
            return newRow;
        } else {
            return els.get(0);
        }
    }

    @Override
    public String getText() {
        return "Faktoren Sortieren";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        return (els.size() > 0 && (els.get(0) instanceof CTimesRow));
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
