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

import cTree.CElement;
import cTree.CFrac;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.cCombine.CombHandler;
import cTree.cDefence.CD_Event;

public class CA_Frac_Kuerzen extends CA_Base {

    private C_Changer c;

    @Override
    public CElement doIt(final CD_Event message) {
        return this.c.doIt(null);
    }

    @Override
    public String getText() {
        return "Kuerzen";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement first = this.getFirst();
            if (first instanceof CFrac) {
                final CElement z = ((CFrac) first).getZaehler();
                final C_Event e = new C_Event(z);
                this.c = CombHandler.getInst().getChanger(e);
                return this.c.canDo();
            }
        }
        return false;
    }

}
