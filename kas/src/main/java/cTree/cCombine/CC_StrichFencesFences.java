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
import cTree.CPlusTerm;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CC_StrichFencesFences extends CC_Base {

    private CC_StrichFencedMinFencedMin cmm;

    private CC_StrichFencedPlusFencedPlus cpp;

    private CC_StrichFencedMinFencedPlus cmp;

    private CC_StrichFencedPlusFencedMin cpm;

    private CC_StrichFencedSumFencedSum css;

    @Override
    public C_Changer getChanger(final C_Event e) {
        if (e != null && e.getFirst() != null && e.getFirst().hasNextC()) {
            this.setEvent(e);
            System.out.println("Try to combine Fences Fences");
            final CElement cE1 = this.getFirst();
            final CElement cE2 = this.getSec();
            if (cE1.getFirstChild() instanceof CMinTerm
                    && cE2.getFirstChild() instanceof CMinTerm) {
                System.out.println("Found MinTerms");
                return this.getCmm().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CPlusTerm
                    && cE2.getFirstChild() instanceof CMinTerm) {
                System.out.println("Found PM");
                return this.getCpm().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CPlusTerm
                    && cE2.getFirstChild() instanceof CPlusTerm) {
                System.out.println("Found PP");
                return this.getCpp().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CMinTerm
                    && cE2.getFirstChild() instanceof CPlusTerm) {
                System.out.println("Found MP");
                return this.getCmp().getChanger(e);
            } else if (cE1.getFirstChild() instanceof CPlusRow
                    && cE2.getFirstChild() instanceof CPlusRow) {
                System.out.println("Found SS");
                return this.getCss().getChanger(e);
            }
        }
        return new C_No(e);
    }

    /**
     * Getter method for cmm.
     * 
     * @return the cmm
     */
    protected CC_StrichFencedMinFencedMin getCmm() {
        if (this.cmm == null) {
            this.cmm = new CC_StrichFencedMinFencedMin();
        }
        return this.cmm;
    }

    protected CC_StrichFencedMinFencedPlus getCmp() {
        if (this.cmp == null) {
            this.cmp = new CC_StrichFencedMinFencedPlus();
        }
        return this.cmp;
    }

    protected CC_StrichFencedPlusFencedMin getCpm() {
        if (this.cpm == null) {
            this.cpm = new CC_StrichFencedPlusFencedMin();
        }
        return this.cpm;
    }

    protected CC_StrichFencedPlusFencedPlus getCpp() {
        if (this.cpp == null) {
            this.cpp = new CC_StrichFencedPlusFencedPlus();
        }
        return this.cpp;
    }

    protected CC_StrichFencedSumFencedSum getCss() {
        if (this.css == null) {
            this.css = new CC_StrichFencedSumFencedSum();
        }
        return this.css;
    }
}
