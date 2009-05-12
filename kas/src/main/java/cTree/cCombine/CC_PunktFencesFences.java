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

public class CC_PunktFencesFences extends CC_ {

    private CC_PunktFencedMinFencedMin cmm;

    private CC_PunktFencedPlusFencedPlus cpp;

    private CC_PunktFencedMinFencedPlus cmp;

    private CC_PunktFencedPlusFencedMin cpm;

    private CC_PunktFencedSumFencedSum css;

    @Override
    protected CElement createCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        if (cE1.getFirstChild() instanceof CMinTerm
                && cE2.getFirstChild() instanceof CMinTerm) {
            return this.getCmm().createCombination(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CPlusTerm
                && cE2.getFirstChild() instanceof CPlusTerm) {
            return this.getCpp().createCombination(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CPlusTerm
                && cE2.getFirstChild() instanceof CMinTerm) {
            return this.getCpm().createCombination(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CMinTerm
                && cE2.getFirstChild() instanceof CPlusTerm) {
            return this.getCmp().createCombination(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CPlusRow
                && cE2.getFirstChild() instanceof CPlusRow) {
            return this.getCss().createCombination(parent, cE1, cE2);
        }
        return cE1;
    }

    @Override
    protected boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("Try to combine Fences Fences");
        if (cE1.getFirstChild() instanceof CMinTerm
                && cE2.getFirstChild() instanceof CMinTerm) {
            System.out.println("Found MinTerms");
            return this.getCmm().canCombine(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CPlusTerm
                && cE2.getFirstChild() instanceof CMinTerm) {
            System.out.println("Found PM");
            return this.getCpm().canCombine(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CPlusTerm
                && cE2.getFirstChild() instanceof CPlusTerm) {
            System.out.println("Found PP");
            return this.getCpp().canCombine(parent, cE1, cE2);
        } else if (cE1.getFirstChild() instanceof CMinTerm
                && cE2.getFirstChild() instanceof CPlusTerm) {
            System.out.println("Found MP");
            return this.getCmp().canCombine(parent, cE1, cE2);
        }
        return false;
    }

    /**
     * Getter method for cmm.
     * 
     * @return the cmm
     */
    protected CC_PunktFencedMinFencedMin getCmm() {
        if (this.cmm == null) {
            this.cmm = new CC_PunktFencedMinFencedMin();
        }
        return this.cmm;
    }

    protected CC_PunktFencedMinFencedPlus getCmp() {
        if (this.cmp == null) {
            this.cmp = new CC_PunktFencedMinFencedPlus();
        }
        return this.cmp;
    }

    protected CC_PunktFencedPlusFencedMin getCpm() {
        if (this.cpm == null) {
            this.cpm = new CC_PunktFencedPlusFencedMin();
        }
        return this.cpm;
    }

    protected CC_PunktFencedPlusFencedPlus getCpp() {
        if (this.cpp == null) {
            this.cpp = new CC_PunktFencedPlusFencedPlus();
        }
        return this.cpp;
    }

    /**
     * Getter method for css.
     * 
     * @return the css
     */
    protected CC_PunktFencedSumFencedSum getCss() {
        if (this.css == null) {
            this.css = new CC_PunktFencedSumFencedSum();
        }
        return this.css;
    }
}
