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
import cTree.CType;

public class CCombinerTPot extends CCombinerTyp {
    public CCombinerTPot() {
        for (final CType cType : CType.values()) {
            this.op1Combiner.put(cType, new CCombiner1PotDefault());
        }
        this.op1Combiner.put(CType.NUM, new CCombiner1PotNum());
        this.op1Combiner.put(CType.FENCES, new CCombiner1PotFences());
    }

    @Override
    public boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("CombinerTyp^ can combine?");
        return this.op1Combiner.get(cE1.getCType()).canCombine(parent, cE1,
                cE2);
    }

}
