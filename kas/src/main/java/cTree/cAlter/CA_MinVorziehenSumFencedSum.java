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
import cTree.CPlusRow;
import cTree.cDefence.CD_Event;

public class CA_MinVorziehenSumFencedSum extends CA_Base {

    @Override
    public CElement doIt(final CD_Event message) {
        final CElement fences = this.getEvent().getFirst();
        fences.togglePlusMinus(false);
        final CFences f = (CFences) fences;
        ((CPlusRow) f.getInnen()).toggleAllVZ();
        return fences;
    }

    @Override
    public String getText() {
        return "Vorzeichen der Klammer ändern";
    }

    @Override
    public boolean canDo() {
        if (this.getEvent() != null && this.getEvent().getFirst() != null) {
            final CElement fences = this.getFirst();
            if (fences instanceof CFences) {
                if (((CFences) fences).getInnen() instanceof CPlusRow) {
                    return fences.getParent() instanceof CPlusRow;
                }
            }
        }
        return false;
    }

}
