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

import java.util.ArrayList;
import java.util.HashMap;

import cTree.CElement;
import cTree.CType;
import cTree.adapter.DOMElementMap;

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

    public static ExtractHandler getInstance() {
        if (ExtractHandler.uniqueInstance == null) {
            synchronized (DOMElementMap.class) {
                if (ExtractHandler.uniqueInstance == null) {
                    ExtractHandler.uniqueInstance = new ExtractHandler();
                }
            }
        }
        return ExtractHandler.uniqueInstance;
    }

    public CElement extract(final CElement parent,
            final ArrayList<CElement> selection, final CElement defaultElement) {
        System.out.println("Extract from " + parent.getCType());
        return this.getTypExtracter.get(parent.getCType()).extract(parent,
                selection, defaultElement);
    }

    public boolean canExtract(final ArrayList<CElement> selection) {
        System.out.println("Extract from " + selection.get(0).getCType());
        if (selection.size() > 0) {
            final CElement parent = selection.get(0).getParent();
            System.out.println("Extract from " + parent.getCType());
            return this.getTypExtracter.get(parent.getCType()).canExtract(
                    parent, selection);
        }
        return false;
    }

    public boolean justAll(final ArrayList<CElement> selection) {
        return !(selection.get(0).hasPrevC() || selection.get(
                selection.size() - 1).hasNextC());
    }

    public void insertOrReplace(final CElement parent, final CElement newC,
            final ArrayList<CElement> selection, final boolean replace) {
        if (replace) {
            System.out.println("// replace");
            parent.getParent().replaceChild(newC, parent, true, true);
        } else {
            System.out.println("// insert");
            parent.replaceChild(newC, selection.get(0), true, true);
            for (int i = 1; i < selection.size(); i++) {
                parent.removeChild(selection.get(i), true, true, false);
            }
        }
    }
}
