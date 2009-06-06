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

package cTree.cCombine;

import cTree.CElement;
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CC_PunktFencesIdent extends CC_Base {

    private CC_PunktFencedMinExp cme;

    private CC_PunktFencedSumExp cse;

    @Override
    public C_Changer getChanger(final C_Event event) {
        this.setEvent(event);
        final CElement cE1 = this.getFirst();
        System.out.println("Repell fenced sum/min mult num");
        if (cE1.getFirstChild() instanceof CMinTerm) {
            System.out.println("Found MinTerms");
            return this.getCme().getChanger(event);
        } else if (cE1.getFirstChild() instanceof CPlusRow) {
            return this.getCse().getChanger(event);
        }
        return new C_No(event);
    }

    protected CC_PunktFencedMinExp getCme() {
        if (this.cme == null) {
            this.cme = new CC_PunktFencedMinExp();
        }
        return this.cme;
    }

    /**
     * Getter method for css.
     * 
     * @return the css
     */
    protected CC_PunktFencedSumExp getCse() {
        if (this.cse == null) {
            this.cse = new CC_PunktFencedSumExp();
        }
        return this.cse;
    }

    protected boolean canCobine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("Repell fenced sum mult ident");
        if (cE1.hasExtDiv()) {
            return false;
        }
        if (!cE1.hasChildC() || !(cE1.getFirstChild() instanceof CPlusRow)) {
            return false;
        }
        return true;
    }
}
