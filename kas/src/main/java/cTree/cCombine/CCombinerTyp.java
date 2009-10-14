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

public abstract class CCombinerTyp extends C_Changer {
    protected HashMap<CType, CCombiner1> op1Combiner;

    public CCombinerTyp() {

    }

    protected HashMap<CType, CCombiner1> getOp1Comb() {
        if (this.op1Combiner == null) {
            this.op1Combiner = new HashMap<CType, CCombiner1>();
        }
        return this.op1Combiner;
    }

    @Override
    public C_Changer getChanger(final C_Event e) {
        final CType cType = e.getFirst().getCType();
        if (this.getOp1Comb().containsKey(cType)) {
            System.out.println("combHandler 2 " + cType);
            return this.getOp1Comb().get(cType).getChanger(e);
        } else {
            return new C_No(e);
        }
    }
}
