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
import cTree.CFences;
import cTree.cDefence.CD_Event;

public class CA_DivMin1_InMalMin1 extends CA_Base {

    private CFences cF;

    @Override
    public CElement doIt(final CD_Event message) {
        this.cF.toggleTimesDiv(false);
        return this.cF;
    }

    @Override
    public String getText() {
        return ":(-1) in *(-1) umwandeln";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement first = this.getEvent().getFirst();
            if (first.hasExtDiv() && (first instanceof CFences)) {
                this.cF = (CFences) first;
                return this.cF.isFencedMin1();
            }
        }
        return false;
    }

}
