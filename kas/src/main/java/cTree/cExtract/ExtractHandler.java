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

public class ExtractHandler {
    private volatile static ExtractHandler uniqueInstance;

    public HashMap<CType, CExtracterTyp> getTypExtracter;

    private ExtractHandler() {
        this.getTypExtracter = new HashMap<CType, CExtracterTyp>();
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

    public C_Changer getChanger(final C_Event event) {
        final CType parentTyp = event.getParent().getCType();
        if (this.getTypExtracter.containsKey(parentTyp)) {
            System.out.println("ExtractHandler " + parentTyp);
            return this.getTypExtracter.get(parentTyp).getChanger(event);
        } else {
            return new C_No(event);
        }
    }
}
