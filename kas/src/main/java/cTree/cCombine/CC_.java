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

public class CC_ {

    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        if (!this.canCombine(cE1, cE2)) {
            System.out.println("Repelled!!");
            return cE1;
        }
        cE1.removeCActiveProperty();
        final boolean replace = CombineHandler.getInstance()
                .justTwo(cE1, cE2);
        final CElement newChild = this.createCombination(parent, cE1, cE2);
        CombineHandler.getInstance().insertOrReplace(parent, newChild, cE1,
                cE2, replace);
        System.out.println("CC inserted");
        newChild.setCActiveProperty();
        return newChild;
    }

    protected CElement createCombination(final CElement parent,
            final CElement cE1, final CElement cE2) {
        System.out.println("Cant create combination");
        // Vorsicht! Fehlbedienung möglich. Repeller einsetzen!
        return cE1;
    }

    protected boolean canCombine(final CElement cE1, final CElement cE2) {
        System.out.println("Repell standard?");
        return false;
    }

}
