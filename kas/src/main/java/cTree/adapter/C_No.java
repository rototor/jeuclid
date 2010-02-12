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

import cTree.CElement;
import cTree.cDefence.CD_Event;

public class C_No extends C_Changer {

    public C_No(final C_Event event) {
        this.setEvent(event);
    }

    /**
     * This should be overwriten by each Class.
     */
    @Override
    public boolean canDo() {
        return false;
    }

    /**
     * Do nothing
     */
    @Override
    public CElement doIt(final CD_Event message) {
        if (this.getEvent() != null) {
            return this.getEvent().getFirst();
        }
        return null;
    }
}
