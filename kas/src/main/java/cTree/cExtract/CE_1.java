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

public class CE_1 extends C_Changer {

    @Override
    public CElement doIt() {
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
    public boolean canDo(final C_Event e) {
        if (e instanceof C_Event) {
            this.setEvent((CE_Event) e);
        }
        return false;
    }

    public CE_1 getExt(final CE_Event event) {
        return this;
    }
}
