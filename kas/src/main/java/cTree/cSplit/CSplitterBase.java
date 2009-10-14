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

package cTree.cSplit;

import cTree.CElement;
import cTree.CFences;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;
import cTree.cDefence.CD_Event;
import cTree.cDefence.DefHandler;

/**
 * Overrides getChanger(C_Event), canDo() and doIt() in the superclass
 * C_Changer to provide hooks for init(CS_Event) in getChanger and split in
 * doIt(). Init retrieves the information from the C_Event-Object. DoIt
 * creates the splitted expression which is fenced in doIt() as a protection.
 * 
 * @version $Revision$
 */
public abstract class CSplitterBase extends C_Changer {

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement cF = CFences.condCreateFenced(this.split(), message);
        System.out.println("CSplitterBase message " + message.isDoDef());
        final CElement parent = this.getEvent().getParent();
        parent.replaceChild(cF, this.getEvent().getFirst(), true, true);
        if (message.isDoDef()) {
            message.setDoDef(false);
            final CD_Event e = new CD_Event(cF);
            final C_Changer c = DefHandler.getInst().getChanger(e);
            return c.doIt(null);
        } else {
            return cF;
        }
    }

    @Override
    public boolean canDo() {
        return false;
    }

    @Override
    public C_Changer getChanger(final C_Event event) {
        if (event instanceof CS_Event) {
            this.setEvent(event);
            this.init((CS_Event) event);
            return this;
        }
        return new C_No(event);
    }

    // ----------------- Hooks ------------------------------------------

    protected void init(final CS_Event event) {
    }

    protected abstract CElement split();

}
