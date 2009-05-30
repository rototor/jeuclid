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

import java.util.ArrayList;

import cTree.CElement;
import cTree.CFences;
import cTree.CMinTerm;
import cTree.CPlusRow;
import cTree.CTimesRow;

public class CC_PunktFencesIdent extends CC_Base {

    private CC_PunktFencedMinExp cme;

    private CC_PunktFencedSumExp cse;

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("Multipliziere geklammerte Summe/MinRow mit Num");
        if (cE1.getFirstChild() instanceof CMinTerm) {
            System.out.println("Found MinTerms");
            return this.getCme().createComb(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CPlusRow) {
            return this.getCse().createComb(parent, cE1, cE2);
        }
        return cE1;
    }

    protected CElement createCobination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Multipliziere geklammerte Summe mit Ident");
        final ArrayList<CElement> oldAddendList = ((CPlusRow) cE1
                .getFirstChild()).getMemberList();
        final ArrayList<CElement> newAddendList = CTimesRow.map(
                oldAddendList, cE2);
        final CElement newChild = CFences.createFenced(CPlusRow
                .createRow(newAddendList));
        return newChild;
    }

    @Override
    public boolean canDo() {
        final CElement cE1 = this.getFirst();
        System.out.println("Repell fenced sum/min mult num");
        if (cE1.getFirstChild() instanceof CMinTerm) {
            System.out.println("Found MinTerms");
            return this.getCme().canDo();
        } else if (cE1.getFirstChild() instanceof CPlusRow) {
            return this.getCse().canDo();
        }
        return false;
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
