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
import cTree.CFences;
import cTree.CType;

public class CombHandler {

    private volatile static CombHandler uniqueInstance;

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
            final CCombinerTyp default1 = new CCombinerTyp();
            for (final CType cType : CType.values()) {
                this.typCombiner.put(cType, default1);
            }
            this.typCombiner.put(CType.PLUSROW, new CCombinerTStrich());
            this.typCombiner.put(CType.TIMESROW, new CCombinerTPunkt());
            this.typCombiner.put(CType.POT, new CCombinerTPot());
            this.typCombiner.put(CType.FRAC, new CCombinerTFrac());
        }
        return this.typCombiner;
    }

    public boolean canCombine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        final CType pt = parent.getCType();
        return this.getTypComb().get(pt).canCombine(parent, cE1, cE2);
    }

    public CElement combine(final CElement parent, final CElement cE1,
            final CElement cE2) {
        final CType pt = parent.getCType();
        return this.getTypComb().get(pt).combine(parent, cE1, cE2);
    }

    public CC_ getCombiner(final CElement parent, final CElement cE1,
            final CElement cE2) {
        final CType pt = parent.getCType();
        return this.getTypComb().get(pt).getCombiner(parent, cE1, cE2);
    }

    // -------------------------------------------------------------------------------

    public boolean justTwo(final CElement first, final CElement second) {
        return !(first.hasPrevC() || second.hasNextC());
    }

    /*
     * Je nach replace wird entweder paren oder repC ersetzt. remC wird
     * entfernt.
     */
    public CElement insertOrReplace(final CElement parent,
            final CElement newC, final CElement repC, final CElement remC,
            final boolean replace) {
        if (replace) {
            final CElement grandParent = parent.getParent();
            if (grandParent instanceof CFences) {
                final CElement ggParent = grandParent.getParent();
                final CFences newF = CFences.createFenced(newC);
                return ggParent.replaceChild(newF, grandParent, true, true);
            } else {
                // parent.removeChild(remC, false, false, false);
                // parent.removeChild(repC, false, false, false);
                return grandParent.replaceChild(newC, parent, true, true);
            }
        } else {
            parent.removeChild(remC, true, true, false);
            return parent.replaceChild(newC, repC, true, true);
        }
    }
}
