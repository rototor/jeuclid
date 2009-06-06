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
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CC_PunktFracFences extends CC_Base {

    private CC_PunktFracFencedSum cns;

    private CC_PunktFracFencedMin cnm;

    // @Override
    // protected CElement createComb(final CElement oldSumme,
    // final CElement cE1, final CElement cE2, CD_Event cDEvent) {
    // if (((CFences) cE2).getInnen() instanceof CMinTerm) {
    // return this.getCnm().createComb(oldSumme, cE1, cE2, null);
    // } else if (((CFences) cE2).getInnen() instanceof CPlusRow) {
    // return this.getCns().createComb(oldSumme, cE1, cE2, null);
    // }
    // return cE1;
    // }

    @Override
    public C_Changer getChanger(final C_Event e) {
        System.out.println("Can Combine Num times Fences?");
        this.setEvent(e);
        final CElement el = this.getFirst();
        System.out.println(el.toString());
        final CElement el2 = el.getNextSibling();
        if (((CFences) el2).getInnen() instanceof CMinTerm) {
            return this.getCnm().getChanger(e);
        } else if (((CFences) el2).getInnen() instanceof CPlusRow) {
            return this.getCns().getChanger(e);
        }
        return new C_No(e);
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

    protected CC_PunktFracFencedMin getCnm() {
        if (this.cnm == null) {
            this.cnm = new CC_PunktFracFencedMin();
        }
        return this.cnm;
    }

    protected CC_PunktFracFencedSum getCns() {
        if (this.cns == null) {
            this.cns = new CC_PunktFracFencedSum();
        }
        return this.cns;
    }
}
