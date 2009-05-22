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

public class ExtractHandler {
    private volatile static ExtractHandler uniqueInstance;

    public HashMap<CType, CExtracterTyp> getTypExtracter;

    private ExtractHandler() {
        this.getTypExtracter = new HashMap<CType, CExtracterTyp>();
        final CExtracterTyp default1 = new CExtracterTyp();
        for (final CType cType : CType.values()) {
            this.getTypExtracter.put(cType, default1);
        }
        this.getTypExtracter.put(CType.PLUSROW, new CExtracterTStrich());
        this.getTypExtracter.put(CType.SQRT, new CExtracterTSqrt());
    }

    public static ExtractHandler getInst() {
        if (ExtractHandler.uniqueInstance == null) {
            synchronized (ExtractHandler.class) {
                if (ExtractHandler.uniqueInstance == null) {
                    ExtractHandler.uniqueInstance = new ExtractHandler();
                }
            }
        }
        return ExtractHandler.uniqueInstance;
    }

    public CE_1 getExt(final CE_Event event) {
        final CType parentTyp = event.getParent().getCType();
        return this.getTypExtracter.get(parentTyp).getExt(event);
    }

}
