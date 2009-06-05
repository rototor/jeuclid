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
import cTree.CFences;
import cTree.adapter.C_Event;
import cTree.cDefence.CD_Event;

public class CA_Klammern extends CA_Base {

    @Override
    public CElement doIt(final CD_Event message) {
        final ArrayList<CElement> els = this.getEvent().getSelection();
        return els.get(0).getParent().fence(els);
    }

    @Override
    public String getText() {
        return "Klammern setzen";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final C_Event event = this.getEvent();
            final CElement first = event.getFirst();
            final ArrayList<CElement> els = event.getSelection();
            return (els.size() > 0 && first != null && !(els.size() == 1 && (first instanceof CFences)));
        } else {
            return false;
        }
    }

}
