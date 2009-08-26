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

package cTree.cDefence;

import cTree.CElement;
import cTree.CFences;
import cTree.adapter.C_Changer;

public abstract class CD_Base extends C_Changer {

    @Override
    public CElement doIt(final CD_Event message) {
        System.out.println("Do the defence work ");
        final CElement p = this.getParent();
        final CFences f = this.getFences();
        final CElement content = this.getInside();
        final boolean replace = this.replaceP(p, f);
        CElement insertion = this.createInsertion(f, content);
        insertion = this.replaceFoPDef(p, insertion, f, replace);
        return insertion;
    }

    protected CElement createInsertion(final CElement fences,
            final CElement content) {
        return fences;
    }

    protected boolean replaceP(final CElement parent, final CElement fences) {
        return false;
    }

    @Override
    public boolean canDo() {
        System.out.println("Defencer can Defence?");
        return true;
    }

    public CFences getFences() {
        return (CFences) ((CD_Event) this.getEvent()).getFences();
    }

    public CElement getInside() {
        return ((CD_Event) this.getEvent()).getInside();
    }
}
