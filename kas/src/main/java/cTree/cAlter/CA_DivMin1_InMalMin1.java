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
import cTree.CFences;

public class CA_DivMin1_InMalMin1 extends CAlter {

    private CFences cF;

    @Override
    public CElement change(final ArrayList<CElement> els) {
        this.cF.toggleTimesDiv(false);
        return this.cF;
    }

    @Override
    public String getText() {
        return ":(-1) in *(-1) umwandeln";
    }

    @Override
    public boolean check(final ArrayList<CElement> els) {
        if (els.get(0).hasExtDiv() && (els.get(0) instanceof CFences)) {
            System.out.println("Is fenced Min1");
            this.cF = (CFences) els.get(0);
            return this.cF.isFencedMin1();
        }
        return false;
    }

    @Override
    public void register(final HashMap<String, CAlter> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
