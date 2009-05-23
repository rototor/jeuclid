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
import cTree.cDefence.DefHandler;

public abstract class CSplitterBase extends C_Changer {

    @Override
    public CElement doIt() {
        final CFences cF = CFences.createFenced(this.split());
        final CElement parent = this.getEvent().getParent();
        parent.replaceChild(cF, this.getEvent().getFirst(), true, true);
        return DefHandler.getInst().defence(parent, cF, cF.getInnen());
    }

    @Override
    public boolean canDo(final C_Event e) {
        return false;
    }

    public CSplitterBase getSplitr(final CS_Event event) {
        return this;
    }

    public abstract CElement split();

}
