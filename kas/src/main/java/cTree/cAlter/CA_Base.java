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

import java.util.HashMap;

import cTree.adapter.C_Changer;

/**
 * Adds support for being displayed in a popup menu. getText(): delivers the
 * string which is displayed in the list. register(): a hashmap is find the
 * changer when the text is chosen.
 * 
 * @version $Revision$
 */
public abstract class CA_Base extends C_Changer {

    public abstract String getText();

    public void register(final HashMap<String, CA_Base> hashMap) {
        hashMap.put(this.getText(), this);
    }
}
