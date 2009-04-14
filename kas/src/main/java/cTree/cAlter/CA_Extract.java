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

package cTree.cAlter;

import java.util.ArrayList;
import java.util.HashMap;

import cTree.CElement;
import cTree.cExtract.ExtractHandler;

public class CA_Extract extends CAlter {

    @Override
    public CElement change(final ArrayList<CElement> els) {

        return els.get(0).getParent().extract(els);
    }

    @Override
    public String getText() {
        return "Extrahieren";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        System.out.println("CA-Extract-check "
                + ExtractHandler.getInstance().canExtract(els));
        return ExtractHandler.getInstance().canExtract(els);
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
