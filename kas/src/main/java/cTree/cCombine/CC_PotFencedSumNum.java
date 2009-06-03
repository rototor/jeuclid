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

import cTree.CNum;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CC_PotFencedSumNum extends CC_Base {

    @Override
    public C_Changer getChanger(final C_Event e) {
        this.setEvent(e);
        final int exp = ((CNum) this.getSec()).getValue();
        if (exp == 0) {
            return new CC_PotAny0().getChanger(e);
        } else if (exp == 1) {
            return new CC_PotAny1().getChanger(e);
        } else if (exp == 2) {
            return new CC_PotFencedSum2().getChanger(e);
        } else {
            return new C_No(e);
        }
    }
}
