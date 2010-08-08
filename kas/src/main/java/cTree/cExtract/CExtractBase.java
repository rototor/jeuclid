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

package cTree.cExtract;

import cTree.CElement;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;
import cTree.cDefence.CD_Event;

public class CExtractBase extends C_Changer {

    @Override
    public CElement doIt(final CD_Event message) {
        final boolean replace = this.justAll(this.getEvent().getSelection());
        final boolean hasMinus = this.getEvent().getFirst().hasExtMinus();
        final CElement newChild = this.createExtraction();
        this.insertOrReplace(newChild, replace);
        if (hasMinus) {
            newChild.toggleToPraefixEmptyOrPlus();
        }
        return newChild;
    }

    protected CElement createExtraction() {
        return this.getEvent().getFirst();
    }

    @Override
    public boolean canDo() {
        return false;
    }

    @Override
    public C_Changer getChanger(final C_Event event) {
        this.setEvent(event);
        if (this.canDo()) {
            return this;
        } else {
            return new C_No(event);
        }
    }
}
