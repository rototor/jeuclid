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

public class CExtracterTyp {
    public HashMap<CType, CE_1> op1Extracter;

    public CExtracterTyp() {
        this.op1Extracter = new HashMap<CType, CE_1>();
        for (final CType cType : CType.values()) {
            this.op1Extracter.put(cType, new CE_1());
        }
    }

    public CElement extract(final CElement parent,
            final ArrayList<CElement> selection, final CElement defElement) {
        System.out.println("Extract simple");
        return this.op1Extracter.get(parent.getCType()).extract(parent,
                selection, defElement);
    }

    public boolean canExtract(final CElement parent,
            final ArrayList<CElement> selection) {
        System.out.println("Can Extract simple");
        return this.op1Extracter.get(selection.get(0).getCType()).canExtract(
                parent, selection);
    }
}
