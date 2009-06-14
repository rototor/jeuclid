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

import cTree.CType;

public class CCombinerTFrac extends CCombinerTyp {
    public CCombinerTFrac() {
        super();

    }

    @Override
    protected HashMap<CType, CCombiner1> getOp1Comb() {
        if (this.op1Combiner == null) {
            this.op1Combiner = new HashMap<CType, CCombiner1>();
            this.op1Combiner.put(CType.POT, new CCombiner1FracPot());
            this.op1Combiner.put(CType.MIXEDN, new CCombiner1FracMixedN());
            this.op1Combiner.put(CType.MINROW, new CCombiner1FracMinRow());
            this.op1Combiner.put(CType.SQRT, new CCombiner1FracSqrt());
            this.op1Combiner.put(CType.NUM, new CCombiner1FracNum());
            this.op1Combiner.put(CType.FENCES, new CCombiner1FracFences());
            this.op1Combiner.put(CType.IDENT, new CCombiner1FracIdent());
            this.op1Combiner.put(CType.PLUSROW, new CCombiner1FracPR());
            this.op1Combiner.put(CType.TIMESROW, new CCombiner1FracTR());
        }
        return this.op1Combiner;
    }
}
