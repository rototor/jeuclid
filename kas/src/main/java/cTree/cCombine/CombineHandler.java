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

import java.util.HashMap;

import cTree.CElement;
import cTree.CType;

public class CombineHandler {

    private volatile static CombineHandler uniqueInstance;

    public HashMap<CType, CCombinerTyp> getTypCombiner;

    /*
     * Je nach Operatortyp wird gemaess einer HashMap verzweigt
     */
    private CombineHandler() {
        this.getTypCombiner = new HashMap<CType, CCombinerTyp>();
        final CCombinerTyp default1 = new CCombinerTyp();
        for (final CType cType : CType.values()) {
            this.getTypCombiner.put(cType, default1);
        }
        this.getTypCombiner.put(CType.PLUSROW, new CCombinerTStrich());
        this.getTypCombiner.put(CType.TIMESROW, new CCombinerTPunkt());
        this.getTypCombiner.put(CType.POT, new CCombinerTPot());
        this.getTypCombiner.put(CType.FRAC, new CCombinerTFrac());
    }

    public static CombineHandler getInstance() {
        if (CombineHandler.uniqueInstance == null) {
            synchronized (CombineHandler.class) {
                if (CombineHandler.uniqueInstance == null) {
                    CombineHandler.uniqueInstance = new CombineHandler();
                }
            }
        }
        return CombineHandler.uniqueInstance;
    }

    public boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("CombineHandler can combine?");
        return this.getTypCombiner.get(parent.getCType()).canCombine(parent,
                cE1, cE2);
    }

    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        System.out.println("CombineHandler combine");
        return this.getTypCombiner.get(parent.getCType()).combine(parent,
                cE1, cE2);
    }

    // -------------------------------------------------------------------------------

    public boolean justTwo(final CElement first, final CElement second) {
        return !(first.hasPrevC() || second.hasNextC());
    }

    /*
     * Je nach replace wird entweder paren oder repC ersetzt. remC wird
     * entfernt.
     */
    public void insertOrReplace(final CElement parent, final CElement newC,
            final CElement repC, final CElement remC, final boolean replace) {
        if (replace) {
            System.out.println("// replace");
            final CElement gparent = parent.getParent();
            // Das erste funktioniert nicht
            // if (gparent instanceof CFences) {
            // gparent.removeChild(parent, true, true, false);
            // gparent.appendPraefixAndChild(newC);
            // newC.setCRolle(CRolle.GEKLAMMERT);
            // } else {
            // parent.getParent().replaceChild(newC, parent, true, true);
            // }
            // so wie unten gehts, aber lieber wäre mir nur
            // gparent.replaceChild(newC, parent, true, true);
            // if (gparent instanceof CFences) {
            // gparent.getParent().replaceChild(newC, gparent, true, true);
            // } else {
            parent.getParent().replaceChild(newC, parent, true, true);
            // }
        } else {
            System.out.println("// insert");
            parent.replaceChild(newC, repC, true, true);
            parent.removeChild(remC, true, true, false);
        }
    }

}
