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
import cTree.CType;

public class CCombiner1 {

    protected HashMap<CType, CC_> op2Combiner;

    /*
     * Bei bekanntem Operator wird nach dem ersten Operanden gemaess einer
     * HashMap verzweigt.
     */
    public CCombiner1() {

    }

    public HashMap<CType, CC_> getOp2Comb() {
        if (this.op2Combiner == null) {
            this.op2Combiner = new HashMap<CType, CC_>();
            final CC_ default2 = new CC_();
            for (final CType cType : CType.values()) {
                this.op2Combiner.put(cType, default2);
            }
        }
        return this.op2Combiner;
    }

    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        final CType ct2 = cE2.getCType();
        return this.getOp2Comb().get(ct2).combine(parent, cE1, cE2);
    }

    public boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        final CType ct2 = cE2.getCType();
        return this.getOp2Comb().get(ct2).canCombine(parent, cE1, cE2);
    }

    public CC_ getCombiner(final CElement parent, final CElement cE1,
            final CElement cE2) {
        return this.getOp2Comb().get(cE2.getCType());
    }

}
