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

package cTree.cDefence;

import java.util.HashMap;

import cTree.CElement;
import cTree.CType;

public class CDefenceTyp {
    public HashMap<CType, CD_1> op1Defencer;

    public CDefenceTyp() {
        this.op1Defencer = new HashMap<CType, CD_1>();
        for (final CType cType : CType.values()) {
            this.op1Defencer.put(cType, new CD_1());
        }
    }

    public boolean canDefence(final CElement parent, final CElement fences,
            final CElement content) {
        System.out.println("DefenceTyp can Defence?");
        return this.op1Defencer.get(parent.getCType()).canDefence(parent,
                fences, content);
    }

    public CElement defence(final CElement parent, final CElement fences,
            final CElement content) {
        System.out.println("Defence");
        return this.op1Defencer.get(content.getCType()).defence(parent,
                fences, content);
    }
}
