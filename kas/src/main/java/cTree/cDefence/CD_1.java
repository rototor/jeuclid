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

import cTree.CElement;

public class CD_1 {

    public CElement defence(final CElement parent, final CElement fences,
            final CElement content) {
        System.out.println("Do the defence work");
        final boolean replace = this.replaceP(parent, fences);
        final CElement insertion = this.createInsertion(fences, content);
        DefHandler.getInst().replaceFoP(parent, insertion, fences,
                replace);
        return insertion;
    }

    protected CElement createInsertion(final CElement fences,
            final CElement content) {
        System.out.println("Dont touch fences");
        return content.cloneCElement(false);
    }

    protected boolean replaceP(final CElement parent, final CElement fences) {
        return false;
    }

    public boolean canDefence(final CElement parent, final CElement fences,
            final CElement content) {
        System.out.println("DefencerCD1 can Defence?");
        return true;
    }
}
