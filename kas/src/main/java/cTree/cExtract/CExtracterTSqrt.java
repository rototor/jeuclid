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

public class CExtracterTSqrt extends CExtracterTyp {

    @Override
    public HashMap<CType, CExtractBase> getOp1Extracter() {
        if (this.op1Extracter == null) {
            final HashMap<CType, CExtractBase> hm = super.getOp1Extracter();
            hm.put(CType.TIMESROW, new CE_2SqrtPunkt());
            hm.put(CType.POT, new CE_2SqrtPot());
        }
        return this.op1Extracter;
    }
}
