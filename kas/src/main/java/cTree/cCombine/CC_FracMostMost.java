/*
 * Copyright 2009 Erhard Kuenzel 03/09
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
import cTree.cDefence.CD_Event;

public class CC_FracMostMost extends CC_Base {

    @Override
    public boolean canDo() {
        final CElement cE1 = this.getFirst();
        final CElement cE2 = this.getSec();
        return cE1.compareTo(cE2) == 0;
    }

    @Override
    protected CElement createComb(final CElement parent,
            final CElement firstTR, final CElement secondTR,
            final CD_Event cDEvent) {
        return CNum.createNum(parent.getElement(), "1");
    }

}
