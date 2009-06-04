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

package cTree.cExtract;

import java.util.HashMap;

import cTree.CType;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

public class CExtracterTyp extends C_Changer {
    protected HashMap<CType, CExtractBase> op1Extracter;

    /**
     * Getter method for op1Extracter.
     * 
     * @return the op1Extracter
     */
    public HashMap<CType, CExtractBase> getOp1Extracter() {
        if (this.op1Extracter == null) {
            this.op1Extracter = new HashMap<CType, CExtractBase>();
        }
        return this.op1Extracter;
    }

    @Override
    public C_Changer getChanger(final C_Event event) {
        final CType firstTyp = event.getFirst().getCType();
        if (this.getOp1Extracter().containsKey(firstTyp)) {
            System.out.println("ExtractTyp" + firstTyp);
            return this.getOp1Extracter().get(firstTyp).getChanger(event);
        } else {
            return new C_No(event);
        }
    }

}
