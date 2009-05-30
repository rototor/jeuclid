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

import java.util.ArrayList;

import cTree.CElement;

public class CC_PotFencesNum extends CC_Base {

    private ArrayList<CC_Base> combs;

    private CC_Base comb;

    public CC_PotFencesNum() {
        super();
    }

    public ArrayList<CC_Base> getCombs() {
        if (this.combs == null) {
            this.combs = new ArrayList<CC_Base>();
            this.combs.add(new CC_PotFencedPlusrowNum());
            this.combs.add(new CC_PotFencedMinrowNum());
            this.combs.add(new CC_PotFencedSumNum());
            this.combs.add(new CC_PotFencedTRNum());
        }
        return this.combs;
    }

    @Override
    public boolean canDo() {
        for (final CC_Base testComb : this.getCombs()) {
            if (testComb.canDo()) {
                this.comb = testComb;
                return true;
            }
        }
        return false;
    }

    @Override
    protected CElement createComb(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("Pot Fences and Num");
        return this.comb.createComb(parent, cE1, cE2);
    }
}
