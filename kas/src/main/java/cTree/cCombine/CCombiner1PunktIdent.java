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

public class CCombiner1PunktIdent extends CCombiner1 {
    public CCombiner1PunktIdent() {
        super();

    }

    @Override
    public HashMap<CType, CC_Base> getOp2Comb() {
        if (this.op2Combiner == null) {
            this.op2Combiner = super.getOp2Comb();
            this.op2Combiner.put(CType.IDENT, new CC_PunktIdentIdent());
            this.op2Combiner.put(CType.FRAC, new CC_PunktDefFrac());
            this.op2Combiner.put(CType.FENCES, new CC_PunktIdentFences());
            this.op2Combiner.put(CType.POT, new CC_PunktIdentPot());
            this.op2Combiner.put(CType.TIMESROW, new CC_PunktIdentTR());
        }
        return this.op2Combiner;
    }

}
