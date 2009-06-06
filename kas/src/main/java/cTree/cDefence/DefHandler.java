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
import cTree.adapter.DOMElementMap;

public class DefHandler {

    private static volatile DefHandler uniqueInstance;

    public HashMap<CType, CDefenceTyp> typDef;

    private DefHandler() {
        // empty on purpose
    }

    public static DefHandler getInst() {
        if (DefHandler.uniqueInstance == null) {
            synchronized (DOMElementMap.class) {
                if (DefHandler.uniqueInstance == null) {
                    DefHandler.uniqueInstance = new DefHandler();
                }
            }
        }
        return DefHandler.uniqueInstance;
    }

    protected HashMap<CType, CDefenceTyp> getTypDef() {
        if (this.typDef == null) {
            this.typDef = new HashMap<CType, CDefenceTyp>();
            this.typDef.put(CType.SQRT, new CDefenceTMath());
            this.typDef.put(CType.FRAC, new CDefenceTMath());
            this.typDef.put(CType.MIXEDN, new CDefenceTMath());
            this.typDef.put(CType.MATH, new CDefenceTMath());
            this.typDef.put(CType.MINROW, new CDefenceTMin());
            this.typDef.put(CType.PLUSROW, new CDefenceTStrich());
            this.typDef.put(CType.TIMESROW, new CDefenceTPunkt());
            this.typDef.put(CType.POT, new CDefenceTPot());
        }
        return this.typDef;
    }

    public C_Changer getChanger(final C_Event e) {
        final CType cType = e.getParent().getCType();
        if (this.getTypDef().containsKey(cType)) {
            System.out.println("e: " + cType);
            return this.getTypDef().get(cType).getChanger(e);
        } else {
            return new C_No(e);
        }
    }
}
