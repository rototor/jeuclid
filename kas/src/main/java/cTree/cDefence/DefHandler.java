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
import cTree.CFences;
import cTree.CMath;
import cTree.CType;
import cTree.adapter.DOMElementMap;

public class DefHandler {
    private volatile static DefHandler uniqueInstance;

    public HashMap<CType, CDefenceTyp> getTypDefencer;

    private DefHandler() {
        this.getTypDefencer = new HashMap<CType, CDefenceTyp>();
        final CDefenceTyp default1 = new CDefenceTyp();
        for (final CType cType : CType.values()) {
            this.getTypDefencer.put(cType, default1);
        }
        this.getTypDefencer.put(CType.MINROW, new CDefenceTMin());
        this.getTypDefencer.put(CType.PLUSROW, new CDefenceTStrich());
        this.getTypDefencer.put(CType.TIMESROW, new CDefenceTPunkt());
        this.getTypDefencer.put(CType.POT, new CDefenceTPot());
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

    public boolean canDefence(final CElement parent, final CElement fences,
            final CElement content) {
        System.out.println("DefenceHandler can Defence?");
        return this.getTypDefencer.get(parent.getCType()).canDefence(parent,
                fences, content);
    }

    /*
     * Es wird ein CDefenceTyp nach dem Parent gewählt, dieser entscheidet
     * nach dem Content
     */
    public CElement defence(final CElement parent, final CElement fences,
            final CElement content) {
        System.out.println("DefenceHandler call " + parent.getCType());
        return this.getTypDefencer.get(parent.getCType()).defence(parent,
                fences, content);
    }

    /*
     * Es wird ein CDefenceTyp nach dem Parent gewählt, dieser entscheidet
     * nach dem Content
     */
    public CElement conDefence(final CElement parent, final CElement fences,
            final CElement content, final boolean doIt) {
        System.out.println("DefenceHandler condCall " + parent.getCType());
        if (doIt && this.canDefence(parent, fences, content)) {
            return this.defence(parent, fences, content);
        } else {
            return fences;
        }
    }

    public void replaceFoP(final CElement parent, final CElement newC,
            final CElement repC, final boolean replace) {
        if (replace) {
            System.out.println("// replace Parent of Fences");
            final CElement grandParent = parent.getParent();
            if (grandParent instanceof CMath && newC instanceof CFences) {
                grandParent.replaceChild(newC.getFirstChild(), parent, true,
                        true);
            } else {
                parent.getParent().replaceChild(newC, parent, true, true);
            }
        } else {
            System.out.println("// replace Fences");
            parent.replaceChild(newC, repC, true, true);
        }
    }

}
