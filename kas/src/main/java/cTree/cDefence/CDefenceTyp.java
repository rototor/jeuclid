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

import cTree.CType;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CDefenceTyp extends C_Changer {
    protected HashMap<CType, CD_Base> op1Defencer;

    public CDefenceTyp() {

    }

    protected HashMap<CType, CD_Base> getOp1Def() {
        if (this.op1Defencer == null) {
            this.op1Defencer = new HashMap<CType, CD_Base>();
        }
        return this.op1Defencer;
    }

    @Override
    public C_Changer getChanger(final C_Event e) {
        if (e instanceof CD_Event) {
            final CType cType = ((CD_Event) e).getInside().getCType();
            if (this.getOp1Def().containsKey(cType)) {
                return this.getOp1Def().get(cType).getChanger(e);
            }
        }
        return new C_No(e);
    }
}
