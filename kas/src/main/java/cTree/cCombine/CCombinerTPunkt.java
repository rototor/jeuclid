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
import cTree.CNum;
import cTree.CType;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CCombinerTPunkt extends CCombinerTyp {
    public CCombinerTPunkt() {
        super();
    }

    @Override
    protected HashMap<CType, CCombiner1> getOp1Comb() {
        if (this.op1Combiner == null) {
            this.op1Combiner = new HashMap<CType, CCombiner1>();
            this.op1Combiner.put(CType.FRAC, new CCombiner1PunktFrac());
            this.op1Combiner.put(CType.NUM, new CCombiner1PunktNum());
            this.op1Combiner.put(CType.IDENT, new CCombiner1PunktIdent());
            this.op1Combiner.put(CType.POT, new CCombiner1PunktPot());
            this.op1Combiner.put(CType.FENCES, new CCombiner1PunktFences());
            this.op1Combiner.put(CType.SQRT, new CCombiner1PunktSqrt());
        }
        return this.op1Combiner;
    }

    @Override
    public C_Changer getChanger(final C_Event e) {
        final CElement cE1 = e.getFirst();
        final CType cType = cE1.getCType();
        final CElement cE2 = e.getFirst().getNextSibling();
        if (cE1.is0() && !cE2.hasExtDiv()) {
            return (new CC_Punkt0Any()).getChanger(e);
        } else if (cE2.is0() && !cE2.hasExtDiv()) {
            return (new CC_PunktAny0()).getChanger(e);
        } else if ((cE1 instanceof CNum) && ((CNum) cE1).is1()
                && !cE2.hasExtDiv()) {
            return (new CC_Punkt1Any()).getChanger(e);
        } else if ((cE2 instanceof CNum) && ((CNum) cE2).is1()) {
            return (new CC_PunktAny1()).getChanger(e);
        } else if (this.getOp1Comb().containsKey(cType)) {
            System.out.println("Combiner1 " + cType);
            return this.getOp1Comb().get(cType).getChanger(e);
        } else {
            return new C_No(e);
        }
    }

}
