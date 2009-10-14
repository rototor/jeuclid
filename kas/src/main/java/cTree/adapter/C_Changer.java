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
import cTree.CFences;
import cTree.CMath;
import cTree.cDefence.CD_Event;

/**
 * Abstract BaseClass for all Changers. References a C_Event-Objects which
 * provides the necessary informations for the change to do.
 * 
 * The getChanger(C_Event) returns this, if the change can be done, a better
 * C_Changer if one is known to this or C_No elsewise (cf. composite).
 * 
 * CanDo and DoIt should perform the change.
 * 
 * C_Changer also provides basic tools to get information from C_Event easily
 * and supports for replaceOrInsert.
 */

public abstract class C_Changer {

    private C_Event event;

    public C_Event getEvent() {
        return this.event;
    }

    public void setEvent(final C_Event event) {
        this.event = event;
    }

    public CElement doIt(final CD_Event message) {
        if (this.event != null) {
            return this.event.getFirst();
        }
        return null;
    }

    // hook fuer die Methode getChanger
    public boolean canDo() {
        return false;
    }

    public C_Changer getChanger(final C_Event e) {
        this.setEvent(e);
        if (this.canDo()) {
            return this;
        } else {
            return new C_No(e);
        }
    }

    // -- Utilities

    public CElement getParent() {
        if (this.getEvent() != null) {
            return this.getEvent().getParent();
        } else {
            return null;
        }
    }

    public CElement getFirst() {
        if (this.getEvent() != null) {
            return this.getEvent().getFirst();
        } else {
            return null;
        }
    }

    public CElement getSec() {
        if (this.getFirst() != null && this.getFirst().hasNextC()) {
            return this.getEvent().getFirst().getNextSibling();
        } else {
            return null;
        }
    }

    public boolean justAll(final ArrayList<CElement> selection) {
        return !(selection.get(0).hasPrevC() || selection.get(
                selection.size() - 1).hasNextC());
    }

    public boolean justTwo(final CElement first, final CElement second) {
        return !(first.hasPrevC() || second.hasNextC());
    }

    public void insertOrReplace(final CElement newC, final boolean replace)
            throws NullPointerException {
        if (this.event == null || this.event.getParent() == null
                || this.event.getFirst() == null) {
            throw new NullPointerException();
        } else {
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

    // * Je nach replace wird entweder paren oder repC ersetzt. remC wird
    // * entfernt.
    // */
    public CElement insertOrReplace(final CElement parent,
            final CElement newC, final CElement repC, final CElement remC,
            final boolean replace) throws NullPointerException {
        if (parent != null) {
            if (replace) {
                final CElement grandParent = parent.getParent();
                if (grandParent instanceof CFences) {
                    final CElement ggParent = grandParent.getParent();
                    final CFences newF = CFences.createFenced(newC);
                    return ggParent.replaceChild(newF, grandParent, true,
                            true);
                } else {
                    // parent.removeChild(remC, false, false, false);
                    // parent.removeChild(repC, false, false, false);
                    return grandParent.replaceChild(newC, parent, true, true);
                }
            } else {
                parent.removeChild(remC, true, true, false);
                return parent.replaceChild(newC, repC, true, true);
            }
        } else {
            throw new NullPointerException();
        }

    }

    public CElement replaceFoPDef(final CElement parent, final CElement newC,
            final CElement repC, final boolean replace) {
        if (replace) {
            System.out.println("// replace Parent of Fences");
            final CElement grandParent = parent.getParent();
            if (grandParent instanceof CMath && newC instanceof CFences) {
                return grandParent.replaceChild(newC.getFirstChild(), parent,
                        true, true);
            } else {
                return parent.getParent().replaceChild(newC, parent, true,
                        true);
            }
        } else {
            System.out.println("// replace Fences");
            return parent.replaceChild(newC, repC, true, true);
        }
    }

}
