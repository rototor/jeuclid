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
import cTree.CFences;
import cTree.CMinTerm;
import cTree.CPlusRow;

public class CC_PunktNumFences extends CC_Base {

    private CC_PunktNumFencedSum cns;

    private CC_PunktNumFencedMin cnm;

    @Override
    protected CElement createComb(final CElement oldSumme,
            final CElement cE1, final CElement cE2) {
        if (((CFences) cE2).getInnen() instanceof CMinTerm) {
            return this.getCnm().createComb(oldSumme, cE1, cE2);
        } else if (((CFences) cE2).getInnen() instanceof CPlusRow) {
            return this.getCns().createComb(oldSumme, cE1, cE2);
        }
        return cE1;
    }

    @Override
    public boolean canDo() {
        System.out.println("Can Combine Num times Fences?");
        final CElement el = this.getFirst();
        final CElement el2 = el.getNextSibling();
        if (((CFences) el2).getInnen() instanceof CMinTerm) {
            return this.getCnm().canDo();
        } else if (((CFences) el2).getInnen() instanceof CPlusRow) {
            return this.getCns().canDo();
        }
        return false;
    }

    protected CC_PunktNumFencedMin getCnm() {
        if (this.cnm == null) {
            this.cnm = new CC_PunktNumFencedMin();
        }
        return this.cnm;
    }

    protected CC_PunktNumFencedSum getCns() {
        if (this.cns == null) {
            this.cns = new CC_PunktNumFencedSum();
        }
        return this.cns;
    }
}
