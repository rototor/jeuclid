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
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public abstract class CCombiner1 {

    protected HashMap<CType, CC_Base> op2Combiner;

    /*
     * Bei bekanntem Operator wird nach dem ersten Operanden gemaess einer
     * HashMap verzweigt.
     */
    public CCombiner1() {

    }

    public HashMap<CType, CC_Base> getOp2Comb() {
        if (this.op2Combiner == null) {
            this.op2Combiner = new HashMap<CType, CC_Base>();
        }
        return this.op2Combiner;
    }

    public C_Changer getChanger(final C_Event e) {
        if (e != null && e.getFirst() != null && e.getFirst().hasNextC()) {
            final CType cType = e.getFirst().getNextSibling().getCType();
            if (this.getOp2Comb().containsKey(cType)) {
                System.out.println("combHandler 3 " + cType);
                return this.getOp2Comb().get(cType).getChanger(e);
            }
        }
        return new C_No(e);
    }

}
