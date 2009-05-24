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

package cTree.adapter;

import java.util.ArrayList;

import cTree.CElement;

public abstract class C_Changer {

    private C_Event event;

    public C_Event getEvent() {
        return this.event;
    }

    public void setEvent(final C_Event event) {
        this.event = event;
    }

    public CElement doIt() {
        return this.event.getFirst();
    }

    public boolean canDo(final C_Event e) {
        return false;
    }

    public C_Changer getChanger(final C_Event e) {
        return this;
    }

    // -- Utilities

    public boolean justAll(final ArrayList<CElement> selection) {
        return !(selection.get(0).hasPrevC() || selection.get(
                selection.size() - 1).hasNextC());
    }

    public void insertOrReplace(final CElement newC, final boolean replace) {
        final CElement parent = this.event.getParent();
        final ArrayList<CElement> selection = this.event.getSelection();
        if (replace) {
            parent.getParent().replaceChild(newC, parent, true, true);
        } else {
            parent.replaceChild(newC, this.event.getFirst(), true, true);
            for (int i = 1; i < selection.size(); i++) {
                parent.removeChild(selection.get(i), true, true, false);
            }
        }
    }
}
