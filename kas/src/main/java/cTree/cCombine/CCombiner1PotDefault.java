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
import cTree.CNum;
import cTree.CPot;

public class CCombiner1PotDefault extends CCombiner1 {

    @Override
    public CElement combine(final CElement potenz, final CElement basis,
            final CElement exp) {
        System.out.println("Combine Pot Default / kann nur ^1");
        if (((CPot) potenz).isHoch1()) {
            System.out.println("Pot hoch 1");
            final CElement newChild = basis.cloneCElement(false); // potenz.cloneChild(basis,
            // false);
            potenz.getParent().replaceChild(newChild, potenz, true, true);
            newChild.setCActiveProperty();
            return newChild;
        } else if (((CPot) potenz).isHoch0()) {
            System.out.println("Pot hoch 0");
            final CElement newChild = CNum.createNum(potenz.getElement(),
                    "" + 1);
            newChild.setCRolleAndPraefixFrom(potenz);
            potenz.getParent().replaceChild(newChild, potenz, true, true);
            newChild.setCActiveProperty();
            return newChild;
        }
        return potenz;
    }

    @Override
    public boolean canCombine(final CElement potenz, final CElement basis,
            final CElement exp) {
        if (((CPot) potenz).isHoch1()) {
            return true;
        } else if (((CPot) potenz).isHoch0()) {
            return true;
        }
        return false;
    }
}
