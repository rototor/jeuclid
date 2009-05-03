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

import java.util.HashMap;

import cTree.CElement;
import cTree.CFrac;
import cTree.CMinTerm;
import cTree.CMixedNumber;
import cTree.CNum;
import cTree.CType;

public class CCombiner1StrichMinrow extends CCombiner1 {

    private CElement cI;

    private CC_ myCombiner;

    public CCombiner1StrichMinrow() {
        super();

    }

    @Override
    public HashMap<CType, CC_> getOp2Comb() {
        if (this.op2Combiner == null) {
            this.op2Combiner = super.getOp2Comb();
            this.op2Combiner.put(CType.NUM, new CC_StrichMinrowNum());
            this.op2Combiner.put(CType.FRAC, new CC_StrichMinrowFrac());
            this.op2Combiner.put(CType.MIXEDN, new CC_StrichMinrowMixedN());
            this.op2Combiner.put(CType.IDENT, new CC_StrichMinrowIdent());
            this.op2Combiner.put(CType.TIMESROW, new CC_StrichMinrowTR());
        }
        return this.op2Combiner;
    }

    @Override
    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        if (this.canCombine(parent, cE1, cE2)) {
            System.out.println("Add Minrow");
            if (this.cI.istGleichartigesMonom(cE2)) {
                System.out.println("Gleichartig");
                return this.getOp2Comb().get(cE2.getCType()).combine(parent,
                        cE1, cE2);
            } else if ((this.cI instanceof CNum)
                    || (this.cI instanceof CFrac)
                    || (this.cI instanceof CMixedNumber)) {
                final boolean replace = CombHandler.getInst().justTwo(cE1,
                        cE2);
                final CElement newEl = this.myCombiner.createCombination(
                        parent, this.cI, cE2);
                return CombHandler.getInst().insertOrReplace(parent, newEl,
                        cE1, cE2, replace);
            }
        }
        return cE1;
    }

    @Override
    public boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        this.cI = ((CMinTerm) cE1).getValue();
        if (this.cI.istGleichartigesMonom(cE2)) {
            System.out.println("Gleichartig");
            return this.getOp2Comb().get(cE2.getCType()).canCombine(parent,
                    cE1, cE2);
        } else if ((this.cI instanceof CNum) || (this.cI instanceof CFrac)
                || (this.cI instanceof CMixedNumber)) {
            // this.myCombiner = this.op2Combiner.get(cE2.getCType());
            this.myCombiner = CombHandler.getInst().getCombiner(parent,
                    this.cI, cE2);
            System.out.println("My Combiner" + this.myCombiner.toString());
            return this.myCombiner.canCombine(parent, this.cI, cE2);
        }
        return false;
    }
}
