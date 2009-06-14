/*
 * Copyright 2009 JEuclid, http://jeuclid.sf.net
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
 * 
 * 
 * /* $Id$ */

package cTree.cCombine;

import java.util.HashMap;

import cTree.CType;
import cTree.adapter.C_Changer;
import cTree.adapter.C_Event;
import cTree.adapter.C_No;

/**
 * The way to get a combiner object.
 * 
 */
public class CombHandler {

    private static volatile CombHandler uniqueInstance;

    private HashMap<CType, CCombinerTyp> typCombiner;

    /*
     * Je nach Operatortyp wird gemaess einer HashMap verzweigt
     */
    private CombHandler() {
    }

    public static CombHandler getInst() {
        if (CombHandler.uniqueInstance == null) {
            synchronized (CombHandler.class) {
                if (CombHandler.uniqueInstance == null) {
                    CombHandler.uniqueInstance = new CombHandler();
                }
            }
        }
        return CombHandler.uniqueInstance;
    }

    private HashMap<CType, CCombinerTyp> getTypComb() {
        if (this.typCombiner == null) {
            this.typCombiner = new HashMap<CType, CCombinerTyp>();
            this.typCombiner.put(CType.PLUSROW, new CCombinerTStrich());
            this.typCombiner.put(CType.TIMESROW, new CCombinerTPunkt());
            this.typCombiner.put(CType.POT, new CCombinerTPot());
            this.typCombiner.put(CType.FRAC, new CCombinerTFrac());
        }
        return this.typCombiner;
    }

    // -------------------------------------------------------------------------------

    public C_Changer getChanger(final C_Event e) {
        final CType cType = e.getParent().getCType();
        if (this.getTypComb().containsKey(cType)) {
            System.out.println("combHandler" + cType);
            return this.getTypComb().get(cType).getChanger(e);
        } else {
            return new C_No(e);
        }
    }

}
