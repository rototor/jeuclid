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
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;

public class CA_Entklammern extends CA_Base {

    private C_Changer defencer;

    @Override
    public C_Changer getChanger(final C_Event e) {
        if (e instanceof CD_Event) {
            this.setEvent(e);
            this.defencer = DefHandler.getInst().getChanger(e);
        }
        this.defencer = new C_No(e);
        return this.defencer;
    }

    @Override
    public CElement doIt(final CD_Event message) {
        return this.defencer.doIt(null);
    }

    @Override
    public String getText() {
        return "Klammern entfernen";
    }

    @Override
    public boolean canDo() {
        return this.defencer.canDo();
    }

}
