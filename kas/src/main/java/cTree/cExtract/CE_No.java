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
import cTree.adapter.C_Event;

public class CE_No extends CE_1 {

    @Override
    public CElement doIt() {
        if (this.getEvent() != null) {
            return this.getEvent().getFirst();
        }
        return null;
    }

    @Override
    public boolean canDo(final C_Event e) {
        this.setEvent(e);
        return true;
    }
}