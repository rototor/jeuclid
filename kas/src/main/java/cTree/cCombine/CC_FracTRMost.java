/*
 * Copyright 2009 Erhard Kuenzel 03/09
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

package cTree.cCombine;

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFences;
import cTree.CTimesRow;
import cTree.cDefence.CD_Event;

public class CC_FracTRMost extends CC_Base {

    private CElement found;

    private int foundNr;

    @Override
    public boolean canDo() {
        System.out.println("FracTRIdent cando");
        final CTimesRow cE1 = (CTimesRow) this.getFirst();
        final CElement cE2 = this.getSec();
        final ArrayList<CElement> list = cE1.getMemberList();
        // kann kuerzen, wenn erster richtig und Nachfolger kein Div
        this.found = list.get(0);
        this.foundNr = 0;
        final CElement sec = list.get(1);
        if (!sec.hasExtDiv() && this.found.compareTo(cE2) == 0) {
            System.out.println("FracTRIdent cando true");
            return true;
        }
        // kann kuerzen, wenn später und selber kein div
        for (int i = 1; i < list.size(); i++) {
            System.out.println("FracIdentTR check other");
            this.found = list.get(i);
            this.foundNr = i;
            if (!this.found.hasExtDiv() && (cE2.compareTo(this.found) == 0)) {
                System.out.println("FracTRIdent cando true");
                return true;
            }
        }
        return false;
    }

    @Override
    protected CElement createComb(final CElement parent,
            final CElement firstTR, final CElement secondTR,
            final CD_Event cDEvent) {
        final CElement newEl = CTimesRow.cutOne((CTimesRow) this.getFirst(),
                this.foundNr);
        return CFences.condCreateFenced(newEl, cDEvent);
    }
}
